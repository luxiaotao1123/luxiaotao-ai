package com.wuai.company.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 统一处理response
 */
public class Response implements Serializable {

    private static final int SUCCESS_CODE = 200;
    private static final int ERROR_CODE = 400;
    private static final int FAIL_CODE = 500;

    public static ResponseData success() {
        ResponseData responseData = new ResponseData();
        responseData.msg = "请求成功";
        responseData.data = "请求成功";
        responseData.code = SUCCESS_CODE;
        return responseData;
    }

    public static <E> ResponseData success(E data) {
        ResponseData responseData = new ResponseData();
        responseData.msg = "请求成功";
        responseData.data = data;
        responseData.code = SUCCESS_CODE;
        return responseData;
    }

    public static <E> ResponseData success(int code, E data) {
        ResponseData responseData = new ResponseData();
        responseData.data = data;
        responseData.code = code;
        return responseData;
    }

    public static ResponseData successByArray() {
        ResponseData responseData = new ResponseData();
        responseData.data = new ArrayList<>();
        responseData.code = SUCCESS_CODE;
        return responseData;
    }

    public static ResponseData successByMap() {
        ResponseData responseData = new ResponseData();
        responseData.data = new HashMap<>();
        responseData.code = SUCCESS_CODE;
        return responseData;
    }

    public static ResponseData error(){
        ResponseData responseData = new ResponseData();
        responseData.code = ERROR_CODE;
        responseData.data = "服务器内部错误";
        return responseData;
    }

    public static <E> ResponseData error(E data) {
        ResponseData responseData = new ResponseData();
        responseData.data = data;
        responseData.code = ERROR_CODE;
        return responseData;
    }

    public static <E> ResponseData error(int code, E data) {
        ResponseData responseData = new ResponseData();
        responseData.data = data;
        responseData.code = code;
        return responseData;
    }

    public static <E> ResponseData errorByArray() {
        ResponseData responseData = new ResponseData();
        responseData.data = new ArrayList<>();
        responseData.code = ERROR_CODE;
        return responseData;
    }

    public static ResponseData response(int code) {
        ResponseData responseData = new ResponseData();
        responseData.data = new ArrayList<>();
        responseData.code = code;
        return responseData;
    }

    public static <E> ResponseData response(int code, E data) {
        ResponseData responseData = new ResponseData();
        responseData.data = data;
        responseData.code = code;
        return responseData;
    }

    public static <E> ResponseData errorByMap() {
        ResponseData responseData = new ResponseData();
        responseData.data = new HashMap<>();
        responseData.code = ERROR_CODE;
        return responseData;
    }

    public static <E> ResponseData fail() {
        ResponseData responseData = new ResponseData();
        responseData.data = "";
        responseData.code = FAIL_CODE;
        return responseData;
    }


    private static class ResponseData<T> extends Response {

        private String msg = "".intern();

        private int code;

        private T data;

        public String getMsg() {
            return msg;
        }

        public int getCode() {
            return code;
        }

        public T getData() {
            return data;
        }

        private ResponseData() {

        }
    }

    private Response() {

    }

    public Object readResolve() {
        return this;
    }

}
