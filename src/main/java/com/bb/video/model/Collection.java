package com.bb.video.model;

import java.sql.Timestamp;

/**
 * Created by LiangyinKwai on 2019-07-04.
 */
//用户的收藏夹
public class Collection {

    private Integer id;

    //该列建立唯一索引
    private Integer videoId;

    private Timestamp created;

    private Timestamp updated;

}
