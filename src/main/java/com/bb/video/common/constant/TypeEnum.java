package com.bb.video.common.constant;

/**
 * Created by LiangyinKwai on 2019-07-16.
 */
public enum TypeEnum {

    MOVIE(1 , 1, "电影"),

    TV(2, 2, "电视剧"),

    VARIETY(3, 3, "综艺"),

    CARTOON(4, 4, "动漫"),

    ANIME(5, 1, "喜剧"),

    INFORMATION(6, 1, "动作"),

    COMEDY(7, 1, "爱情"),

    ACTIONER(8, 1, "科幻"),

    LOVE(9, 1, "恐怖"),

    SCIENCE_FICTION(10, 1, "剧情"),

    WAR(12, 1, "战争"),

    ETHICAL(13, 1, "伦理"),

    MADE_IN_CHINA(14, 2, "国产"),

    GANG_TAI(15, 2, "港台"),

    RI_HAN(16, 2, "日韩"),

    OU_MEI(17, 2, "欧美"),

    ;

    int id;

    int pid;

    String name;

    TypeEnum(int id, int pid, String name)
    {
        this.id = id;
        this.pid = pid;
        this.name = name;
    }

    public void hasType() {

    }

}
