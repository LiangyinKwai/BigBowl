package com.bb.video.model;

import cn.hutool.json.JSONUtil;
import lombok.Data;
import java.sql.Timestamp;

/**
 * Created by LiangyinKwai on 2019-06-10.
 */
@Data
public class Video {

    private int id;

    private int platformId;

    private int douBanId;

    //来源平台 1:酷云  2:最大资源
    private Byte source;

    private String name;

    private Integer pid;

    private Integer typeId;

    private String cover;

    private String language;

    //最后更新时间
    private Timestamp releaseTime;

    //资源的版权状态
    private Integer state;

    //资源的分辨率 字幕等说明
    private String note;

    private String version;

    private String actor;

    private String director;

    private String area;

    private String description;

    private String year;

    private String link;

    //资源的备注   比如连载状态  哪一期等等
    private String remark;

    private Timestamp created;

    private Timestamp updated;

    @Override
    public String toString() {
        return JSONUtil.parse(this).toString();
    }

}
