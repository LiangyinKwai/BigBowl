package com.bb.video.common.constant;

/**
 * Created by LiangyinKwai on 2019/12/18.
 */
public enum RespEnum {

    SUCCESS(0 , "OK"),

    NORMAL_ERROR(-1, "FAILED"),

    ;

    /**
     * 	状态码
     */
    private int code;

    /**
     *	 异常信息
     */
    private String msg;

    public String msg() {
        return msg;
    }

    public Integer code() {
        return code;
    }

    RespEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
