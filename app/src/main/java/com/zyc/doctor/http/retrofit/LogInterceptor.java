package com.zyc.doctor.http.retrofit;

import com.zyc.doctor.utils.LogUtils;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.concurrent.TimeUnit;

import okhttp3.Connection;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http.HttpHeaders;
import okio.Buffer;
import okio.BufferedSource;

/**
 * @author dundun
 */
public class LogInterceptor implements Interceptor {
    private static final String TAG = "OkHttp";
    private static final Charset UTF8 = Charset.forName("UTF-8");

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        RequestBody requestBody = request.body();
        boolean hasRequestBody = requestBody != null;
        Connection connection = chain.connection();
        Protocol protocol = connection != null ? connection.protocol() : Protocol.HTTP_1_1;
        LogUtils.i(TAG, "请求开始-->" + request.method() + ' ' + request.url() + ' ' + protocol);
        if (!hasRequestBody) {
            LogUtils.i(TAG, "--> END " + request.method());
        }
        else if (bodyEncoded(request.headers())) {
            LogUtils.i(TAG, "--> END " + request.method() + " (encoded body omitted)");
        }
        else {
            Buffer buffer = new Buffer();
            requestBody.writeTo(buffer);
            Charset charset = UTF8;
            MediaType contentType = requestBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(UTF8);
            }
            if (isPlaintext(buffer)) {
                LogUtils.i(TAG, "params:" + buffer.readString(charset));
            }
            else {
                LogUtils.i(TAG, "--> END " + request.method() + " (binary " + requestBody.contentLength() +
                                "-byte body omitted)");
            }
        }
        long startNs = System.nanoTime();
        Response response;
        try {
            response = chain.proceed(request);
        }
        catch (Exception e) {
            LogUtils.i(TAG, "<-- HTTP FAILED: " + e);
            throw e;
        }
        long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);
        ResponseBody responseBody = response.body();
        long contentLength = responseBody.contentLength();
        LogUtils.i(TAG, "<-- " + response.code() + ' ' + response.message() + ' ' + response.request().url() + " (" +
                        tookMs + "ms" + ')');
        if (!HttpHeaders.hasBody(response)) {
            LogUtils.i(TAG, "<-- END HTTP");
        }
        else if (bodyEncoded(response.headers())) {
            LogUtils.i(TAG, "<-- END HTTP (encoded body omitted)");
        }
        else {
            BufferedSource source = responseBody.source();
            // Buffer the entire body.
            source.request(Long.MAX_VALUE);
            Buffer buffer = source.buffer();
            Charset charset = UTF8;
            MediaType contentType = responseBody.contentType();
            if (contentType != null) {
                try {
                    charset = contentType.charset(UTF8);
                }
                catch (UnsupportedCharsetException e) {
                    LogUtils.i(TAG, "Couldn't decode the response body; charset is likely malformed.");
                    LogUtils.i(TAG, "<-- END HTTP");
                    return response;
                }
            }
            if (!isPlaintext(buffer)) {
                LogUtils.i(TAG, "<-- END HTTP (binary " + buffer.size() + "-byte body omitted)");
                return response;
            }
            if (contentLength != 0) {
                LogUtils.i(TAG, buffer.clone().readString(charset));
            }
            LogUtils.i(TAG, "请求结束<-- END HTTP (" + buffer.size() + "-byte body)");
        }
        return response;
    }

    /**
     * Returns true if the body in question probably contains human readable text. Uses a small sample
     * of code points to detect unicode control characters commonly used in binary file signatures.
     */
    static boolean isPlaintext(Buffer buffer) {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        }
        catch (EOFException e) {
            // Truncated UTF-8 sequence.
            return false;
        }
    }

    private boolean bodyEncoded(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        return contentEncoding != null && !"identity".equalsIgnoreCase(contentEncoding);
    }
}
