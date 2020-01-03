package com.bb.video.common.vo;

import com.bb.video.common.constant.RespEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by LiangyinKwai on 2019-06-10.
 */

@Data
public class Resp<T> {

    @ApiModelProperty(value = "响应数据体")
    private T data;

    @ApiModelProperty(value = "响应代码")
    private Integer code;

    @ApiModelProperty(value = "响应消息")
    private String message;

    public static Resp Default() {
        return new Resp();
    }

    public Resp setData(T data) {
        this.data = data;
        return this;
    }

    public Resp setCodeAndMsg(RespEnum respEnum) {
        this.code = respEnum.code();
        this.message = respEnum.msg();
        return this;
    }

    public static Resp ok() {
        return Resp.Default().setCodeAndMsg(RespEnum.SUCCESS);
    }

    public static <T> Resp ok(T data){
        return Resp.ok().setData(data);
    }

    /**
     * 通用型错误 通用错误的code为-1
     * @param message 自定义返回的错误信息
     * @return
     */
    public static Resp error(String message) {
        Resp resp = Resp.Default();
        resp.setCode(RespEnum.NORMAL_ERROR.code());
        resp.setMessage(message);
        return resp;
    }

    public static Resp error(RespEnum respEnum) {
        return Resp.Default().setCodeAndMsg(respEnum);
    }

    public static Resp error() {
        return error(RespEnum.NORMAL_ERROR);
    }

    public static <T> Resp error(RespEnum respEnum, T data) {
        return error(respEnum).setData(data);
    }

}
