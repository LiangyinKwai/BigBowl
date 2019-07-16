package com.bb.video.common.constant;


import lombok.extern.slf4j.Slf4j;

/**
 * Created by LiangyinKwai on 2019-06-14.
 */
@Slf4j
public enum LanguageEnum {

    ELSE(0, "其他"),

    CHN(1, "国语"),

    ENG(2, "英语"),

    KOA(3, "韩语"),

    JPA(4, "日语"),

    YUE(5, "粤语"),

    GER(6, "德语"),

    FRE(7, "法语"),

    PLP(8, "菲律宾语"),

    SBY(9, "西班牙语"),

    ;

    int order;

    String name;

    LanguageEnum(int order, String name) {

        this.order = order;
        this.name = name;
    }

    public static int getOrderByName(String name) {
        for (LanguageEnum value : LanguageEnum.values()) {
            if(value.name.equals(name))
                return value.order;
        }
        log.error("===>>>当前未匹配的枚举值为 : {}<<<===", name);
        throw new RuntimeException("没有找到对应的枚举值");
    }

    public String getName() {
        return name;
    }
}
