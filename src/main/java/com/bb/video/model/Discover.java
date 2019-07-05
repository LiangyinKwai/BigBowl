package com.bb.video.model;

import java.sql.Timestamp;

/**
 * Created by LiangyinKwai on 2019-07-04.
 */
//发现
public class Discover {

    private Integer id;

    private String title;

    private String content;

    //讨论  可以存入 json 格式  每条评论限制长度   根据时间进行排序
    private String comments;

    private Timestamp created;

    private Timestamp updated;
}
