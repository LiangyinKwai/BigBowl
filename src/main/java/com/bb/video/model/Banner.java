package com.bb.video.model;

import lombok.Data;

import java.sql.Timestamp;

/**
 * Created by LiangyinKwai on 2019-07-30.
 */
@Data
public class Banner {

    private Integer id;

    private Byte priority;

    private String cover;

    private String link;

    private Timestamp start;

    private Timestamp end;

    private Timestamp created;

    private Timestamp updated;

}
