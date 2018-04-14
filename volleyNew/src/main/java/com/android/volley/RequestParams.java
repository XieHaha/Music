package com.android.volley;

import android.text.TextUtils;

import org.apache.http.protocol.HTTP;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by miku on 15-9-10.
 */
public class RequestParams {

    private static final String DEFAULT_CHARSET = HTTP.UTF_8;

    private String charset = DEFAULT_CHARSET;

    private Map<String, StringContent> stringsParams;
    private Map<String, FileContent> filesParams;


    public RequestParams() {
    }

    public RequestParams(String charset) {
        if (!TextUtils.isEmpty(charset)) {
            this.charset = charset;
        }
    }

    /**
     * 添加字符，使用默认字符编码
     *
     * @param name
     * @param value
     */
    public void addBodyParameter(String name, String value) {
        if (stringsParams == null) {
            stringsParams = new LinkedHashMap<String, StringContent>();
        }
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(value)) {
            StringContent content = new StringContent(value, null);
            stringsParams.put(name, content);
        }
    }

    /**
     * 添加字符使用自定义字符编码
     *
     * @param charset
     * @param name
     * @param value
     */
    public void addBodyParameter(String name, String value, String charset) {
        if (stringsParams == null) {
            stringsParams = new LinkedHashMap<String, StringContent>();
        }
        StringContent content = new StringContent(value, charset);
        stringsParams.put(name, content);
    }

    /**
     * 添加文件使用默认文件类型，和编码
     *
     * @param file
     * @param key
     */
    public void addBodyParameter(String key, File file) {
        addBodyParameter(key, file, null);
    }

    /**
     * 添加文件使用自定义文件类型，和默认编码
     *
     * @param file
     * @param key
     * @param mimeType
     */
    public void addBodyParameter(String key, File file, String mimeType) {
        addBodyParameter(key, file, mimeType, null);
    }

    /**
     * 添加文件使用自定义文件类型，和编码
     *
     * @param charset
     * @param file
     * @param key
     * @param mimeType
     */
    public void addBodyParameter(String key, File file, String mimeType, String charset) {
        if (filesParams == null) {
            filesParams = new LinkedHashMap<String, FileContent>();
        }
        filesParams.put(key, new FileContent(file, null, mimeType, charset));
    }

    /**
     * getString Params
     *
     * @return java.util.Map
     */
    public Map<String, StringContent> getStringsParams() {
        return stringsParams;
    }

    /**
     * getFile Params
     *
     * @return java.util.Map
     */
    public Map<String, FileContent> getFilesParams() {
        return filesParams;
    }

    /**
     * String 内容实体，包含具体内容和内容的编码
     */
    public static class StringContent {
        private final String value;
        private final String charset;

        public StringContent(String value, String charset) {
            this.value = value;
            if (TextUtils.isEmpty(charset)) {
                this.charset = DEFAULT_CHARSET;
            } else {
                this.charset = charset;
            }
        }

        public String getValue() {
            return value;
        }

        public String getCharset() {
            return charset;
        }

        @Override
        public String toString() {
            return "StringContent{" +
                    "value='" + value + '\'' +
                    ", charset='" + charset + '\'' +
                    '}';
        }
    }

    /**
     * 文件内容实体，包含实体文件，文件名字，文件编码，文件类型
     */
    public static class FileContent {
        private final File file;
        private final String filename;
        private final String charset;
        private final String mimeType;

        public FileContent(final File file, String filename, String mimeType, String charset) {
            if (file == null) {
                throw new RuntimeException("file is null!");
            }
            this.file = file;
            if (filename != null) {
                this.filename = filename;
            } else {
                this.filename = file.getName();
            }
            if (TextUtils.isEmpty(charset)) {
                this.charset = DEFAULT_CHARSET;
            } else {
                this.charset = charset;
            }


            this.mimeType = mimeType;
        }

        public File getFile() {
            return file;
        }

        public String getFilename() {
            return filename;
        }

        public String getCharset() {
            return charset;
        }

        public String getMimeType() {
            return mimeType;
        }

        @Override
        public String toString() {
            return "FileContent{" +
                    "file=" + file +
                    ", filename='" + filename + '\'' +
                    ", charset='" + charset + '\'' +
                    ", mimeType='" + mimeType + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "RequestParams{" +
                "charset='" + charset + '\'' +
                ", stringsParams=" + stringsParams +
                ", filesParams=" + filesParams +
                '}';
    }
}
