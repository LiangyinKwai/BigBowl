package com.bb.video.model;

import java.sql.Timestamp;

/**
 * Created by LiangyinKwai on 2019-07-08.
 */
public class User {

    private Integer id;

    //采用手机号格式的限制或者采用同一ip段注册个数的限制
    private String account;

    private String password;

    private String nickname;

    private String lastLoginIp;

    private Timestamp lastLoginTime;

    private Timestamp created;

    private Timestamp updated;

}
