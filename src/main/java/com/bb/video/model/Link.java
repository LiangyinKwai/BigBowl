package com.bb.video.model;

import lombok.Data;

/**
 * Created by LiangyinKwai on 2019/12/30.
 */
@Data
public class Link {

    private Integer id;

    /**
     * 所属视频id
     */
    private Integer vId;

    private String title;

    private String link;

}
