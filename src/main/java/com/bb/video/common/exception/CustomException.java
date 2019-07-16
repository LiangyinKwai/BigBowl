package com.bb.video.common.exception;

import lombok.Data;

/**
 * Created by LiangyinKwai on 2019-07-08.
 */
@Data
public class CustomException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private String message;
    private int code = -1;

    public CustomException(String message) {
        super(message);
        this.message = message;
    }

    public CustomException(String message, Throwable e) {
        super(message, e);
        this.message = message;
    }

    public CustomException(String message, int code) {
        super(message);
        this.message = message;
        this.code = code;
    }

    public CustomException(String message, int code, Throwable e) {
        super(message, e);
        this.message = message;
        this.code = code;
    }

}
