package com.bb.video.model;

import java.sql.Timestamp;

/**
 * Created by LiangyinKwai on 2019-07-04.
 */
//播放记录
public class PlayRecord {

    private Integer userId;

    //记录内容，以字符串数组(JSON)方式进行存储   数组存入的信息包含视频id 该视频的播放时间等 播放进度等 固定最大长度 采用排队的方式 先进先删的原则
    private String content;

    private Timestamp created;

    private Timestamp updated;

}
