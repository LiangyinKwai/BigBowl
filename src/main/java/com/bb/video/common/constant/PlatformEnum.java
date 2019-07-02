package com.bb.video.common.constant;

/**
 * Created by LiangyinKwai on 2019-06-14.
 */
public enum PlatformEnum {

    KUYUN((byte)1, "kkm3u8", "酷云"),

    ZUIDAZIYUAN((byte)2, "zuidam3u8", "最大资源"),

    ;

    byte order;

    String code;

    String name;

    PlatformEnum(byte order, String code, String name) {
        this.order = order;
        this.code = code;
        this.name = name;
    }

    public static byte getOrderByName(String code)
    {
        for (PlatformEnum value : PlatformEnum.values()) {
            if(value.code.equals(code))
                return value.order;
        }
        throw new RuntimeException("没有找到对应的平台");
    }

}
