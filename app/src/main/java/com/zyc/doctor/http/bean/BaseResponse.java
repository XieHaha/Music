package com.zyc.doctor.http.bean;

import java.io.Serializable;

/**
 * @author DUNDUN
 * @date 2015/12/31
 */
public class BaseResponse<T> implements Serializable {
    private static final long serialVersionUID = -4363411262887201686L;
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
    T data;
    /**
     * 总页数
     */
    int totalPage;
    /**
     * 总数量
     */
    int totalSize;
    /**
     * 当前页码
     */
    int number;
    /**
     * 分页最大数据
     */
    int size;

    public int getCode() {
        return code;
    }

    public BaseResponse setCode(int code) {
        this.code = code;
        return this;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(int totalSize) {
        this.totalSize = totalSize;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getMsg() {
        return msg;
    }

    public BaseResponse setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    @SuppressWarnings({ "unchecked", "UnusedDeclaration" })
    public <T extends Object> T getData() {
        return (T)data;
    }

    public BaseResponse setData(T data) {
        this.data = data;
        return this;
    }

    @Override
    public String toString() {
        return "BaseResponse{" + "code=" + code + ", '" + (msg == null ? "msg为空" : "msg=" + msg) + '\'' + ", " +
               (data == null ? "data为空" : "data=" + data.toString()) + '}';
    }
}
