package com.bb.video.model;

import java.sql.Timestamp;

/**
 * Created by LiangyinKwai on 2019-07-04.
 */
//用户的收藏夹
public class Collections {

    private Integer id;

    //该列与videoId建立联合唯一索引
    private Integer userId;

    private Integer videoId;

    private Timestamp created;

    private Timestamp updated;

}
