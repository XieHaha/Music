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
        boolean logBody = true;
        boolean logHeaders = true;
        RequestBody requestBody = request.body();
        boolean hasRequestBody = requestBody != null;
        Connection connection = chain.connection();
        Protocol protocol = connection != null ? connection.protocol() : Protocol.HTTP_1_1;
        String requestStartMessage = "--> " + request.method() + ' ' + request.url() + ' ' + protocol;
        if (!logHeaders && hasRequestBody) {
            requestStartMessage += " (" + requestBody.contentLength() + "-byte body)";
        }
        LogUtils.i(TAG, "111" + requestStartMessage);
        if (logHeaders) {
            if (hasRequestBody) {
                // Request body headers are only present when installed as a network interceptor. Force
                // them to be included (when available) so there values are known.
                if (requestBody.contentType() != null) {
                    LogUtils.i(TAG, "222" + "Content-Type: " + requestBody.contentType());
                }
                if (requestBody.contentLength() != -1) {
                    LogUtils.i(TAG, "333" + "Content-Length: " + requestBody.contentLength());
                }
            }
            Headers headers = request.headers();
            for (int i = 0, count = headers.size(); i < count; i++) {
                String name = headers.name(i);
                // Skip headers from the request body as they are explicitly logged above.
                if (!"Content-Type".equalsIgnoreCase(name) && !"Content-Length".equalsIgnoreCase(name)) {
                    LogUtils.i(TAG, name + ": " + headers.value(i));
                }
            }
            if (!logBody || !hasRequestBody) {
                LogUtils.i(TAG, "444" + "--> END " + request.method());
            }
            else if (bodyEncoded(request.headers())) {
                LogUtils.i(TAG, "555" + "--> END " + request.method() + " (encoded body omitted)");
            }
            else {
                Buffer buffer = new Buffer();
                requestBody.writeTo(buffer);
                Charset charset = UTF8;
                MediaType contentType = requestBody.contentType();
                if (contentType != null) {
                    charset = contentType.charset(UTF8);
                }
                LogUtils.i(TAG, "666");
                if (isPlaintext(buffer)) {
                    LogUtils.i(TAG, "777" + buffer.readString(charset));
                    LogUtils.i(TAG, "888" + "--> END " + request.method() + " (" + requestBody.contentLength() +
                                    "-byte body)");
                }
                else {
                    LogUtils.i(TAG, "999" + "--> END " + request.method() + " (binary " + requestBody.contentLength() +
                                    "-byte body omitted)");
                }
            }
        }
        long startNs = System.nanoTime();
        Response response;
        try {
            response = chain.proceed(request);
        }
        catch (Exception e) {
            LogUtils.i(TAG, "aaa" + "<-- HTTP FAILED: " + e);
            throw e;
        }
        long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);
        ResponseBody responseBody = response.body();
        long contentLength = responseBody.contentLength();
        String bodySize = contentLength != -1 ? contentLength + "-byte" : "unknown-length";
        LogUtils.i(TAG,
                   "bbb" + "<-- " + response.code() + ' ' + response.message() + ' ' + response.request().url() + " (" +
                   tookMs + "ms" + (!logHeaders ? ", " + bodySize + " body" : "") + ')');
        if (logHeaders) {
            Headers headers = response.headers();
            for (int i = 0, count = headers.size(); i < count; i++) {
                LogUtils.i(TAG, "ccc" + headers.name(i) + ": " + headers.value(i));
            }
            if (!logBody || !HttpHeaders.hasBody(response)) {
                LogUtils.i(TAG, "ddd" + "<-- END HTTP");
            }
            else if (bodyEncoded(response.headers())) {
                LogUtils.i(TAG, "eee" + "<-- END HTTP (encoded body omitted)");
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
                        LogUtils.i(TAG, "fff");
                        LogUtils.i(TAG, "ggg" + "Couldn't decode the response body; charset is likely malformed.");
                        LogUtils.i(TAG, "hhh" + "<-- END HTTP");
                        return response;
                    }
                }
                if (!isPlaintext(buffer)) {
                    LogUtils.i(TAG, "iii" + "");
                    LogUtils.i(TAG, "jjj" + "<-- END HTTP (binary " + buffer.size() + "-byte body omitted)");
                    return response;
                }
                if (contentLength != 0) {
                    LogUtils.i(TAG, "kkk");
                    LogUtils.i(TAG, "LLL" + buffer.clone().readString(charset));
                }
                LogUtils.i(TAG, "mmm" + "<-- END HTTP (" + buffer.size() + "-byte body)");
            }
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
