package custom.frame.bean;

import java.io.Serializable;

/**
 * Created by luozi on 2015/12/31.
 */
public class BaseResponse implements Serializable {
    /**
     * 请求响应码
     */
    int code;
    /**
     * 请求响应文本
     */
    String msg;

    /**
     * 响应实体类
     */
    Object data;

    public int getCode() {
        return code;
    }

    public BaseResponse setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public BaseResponse setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    @SuppressWarnings({"unchecked", "UnusedDeclaration"})
    public <T extends Object> T getData() {
        return (T) data;
    }

    public BaseResponse setData(Object data) {
        this.data = data;
        return this;
    }

    @Override
    public String toString() {
        return "BaseResponse{" +
                "code=" + code +
                ", '" + (msg == null ? "msg为空" : "msg=" + msg) + '\'' +
                ", " + (data == null ? "data为空" : "data=" + data.toString()) +
                '}';
    }
}
