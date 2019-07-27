package com.bb.video.common.constant;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by LiangyinKwai on 2019-07-16.
 */
public enum TypeEnum {

    MOVIE(1 , 1, 1, 1, "电影"),

    TV(2, 2, 2, 2, "电视剧"),

    VARIETY(3, 3, 3, 3, "综艺"),

    CARTOON(4, 4, 4, 4, "动漫"),

    ANIME(11, 1, 6, 6, "喜剧"),

    INFORMATION(12, 1, 5, 5, "动作"),

    COMEDY(13, 1, 7, 7, "爱情"),

    SCIENCE_FICTION(14, 1, 8, 8, "科幻"),

    LOVE(15, 1, 9, 9, "恐怖"),

    STORY(16, 1, 10, 10, "剧情"),

    WAR(17, 1, 11, 11, "战争"),

    ETHICAL(18, 1, 17, 22, "伦理"),

    MUSIC(19, 1, 18, 0, "音乐"),

    FULI(110, 1, 16, 0, "福利片"),

    MADE_IN_CHINA(21, 2, 12, 12, "国产"),

    HK(22, 2, 13, 13, "香港"),

    TAI_WAN(23, 2, 19, 14, "台湾"),

    KOR(24, 2, 14, 19, "韩国"),

    OU_MEI(25, 2, 15, 20, "欧美"),

    JPA(26, 2, 20, 15, "日本剧"),

    HAI_WAI(27, 2, 21, 21, "海外剧"),

    ANIMATION(111, 1, 0, 16, "动画片"),

    RECORD(112, 1, 0, 18, "纪录片"),

    MICRO(113, 1, 0, 17, "微电影")

    ;

    int id;

    int pid;

    int zdzyTid;

    int kuyunTid;

    String name;

    TypeEnum(int id, int pid, int zdzyTid, int kuyunTid, String name)
    {
        this.id = id;
        this.pid = pid;
        this.zdzyTid = zdzyTid;
        this.kuyunTid = kuyunTid;
        this.name = name;
    }

    public static int getIdByZdzyTid(int zdzyTid) {
        for (TypeEnum value : TypeEnum.values()) {
            if(value.zdzyTid == zdzyTid)
                return value.id;
        }
        return 0;
    }

    /**
     * 过去所有的父类型及其子类型
     * @return
     */
    public static Integer[][] getAllParentType() {
        Integer[][] parentType = new Integer[4][];
        List<Integer> movie = Lists.newArrayList();
        List<Integer> tv = Lists.newArrayList();
        List<Integer> variety = Lists.newArrayList();
        List<Integer> cartoon = Lists.newArrayList();
        for (TypeEnum value : TypeEnum.values()) {
            switch (value.pid){
                case 1:
                    addVar(movie, value.id);
                    break;
                case 2:
                    addVar(tv, value.id);
                    break;
                case 3:
                    addVar(variety, value.id);
                    break;
                case 4:
                    addVar(cartoon, value.id);
                    break;
            }
        }
        Integer[] ints = {};
        parentType[0] = movie.toArray(ints);
        parentType[1] = tv.toArray(ints);
        parentType[2] = variety.<Integer>toArray(ints);
        parentType[3] = cartoon.toArray(ints);
        return parentType;
    }

    private static void addVar(List<Integer> list, Integer var) {
        if(! list.contains(var))
            list.add(var);
    }

}
