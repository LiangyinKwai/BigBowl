package com.bb.video.common.constant;


import lombok.extern.slf4j.Slf4j;

/**
 * Created by LiangyinKwai on 2019-06-13.
 */
@Slf4j
public enum VideoEnum {

    ELSE(0, "其他"),

    MOVIE(1 , "电影"),

    SERIES(2, "连续剧"),

    VARIETY(3, "综艺"),

    CARTOON(4,"动画片"),

    ANIME(5, "动漫"),

    INFORMATION(6, "资讯"),

    COMEDY(7, "喜剧片"),

    ACTIONER(8, "动作片"),

    LOVE(9, "爱情片"),

    SCIENCE_FICTION(10, "科幻片"),

    HORROR(11, "恐怖片"),

    STORY(11, "剧情片"),

    WAR(12, "战争片"),

    ETHICAL(13, "伦理片"),

    MADE_IN_CHINA(14, "国产剧"),

    GANG_TAI(15, "港台剧"),

    RI_HAN(16, "日韩剧"),

    OU_MEI(17, "欧美剧"),

    JAPAN(18, "日本剧"),

    ANNOUNCEMENT(19, "公告"),

    HEADLINES(20, "头条"),

    ;

    int order;

    String cn;

    VideoEnum(int order, String cn) {
        this.order = order;
        this.cn = cn;
    }

    public static int getOrderByCn(String cn) {
        for (VideoEnum value : VideoEnum.values()) {
            if(cn.contains(value.cn))
                return value.order;
        }
        log.error("===>>>当前未匹配的枚举值为 : {}<<<===", cn);
        throw new RuntimeException("没有对应的枚举");
    }

}
