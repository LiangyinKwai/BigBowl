package com.bb.video.mapper;

import com.bb.video.model.Video;
import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * Created by LiangyinKwai on 2019-06-21.
 */
public interface VideoMapper extends BaseMapper<Video> {

    /*@Insert("INSERT INTO `video`(platform_id,source,name,`type`,cover,language,release_time,state,note,actor,director,area,description,year,link,remark,created) \n" +
            "  \tvalues(#{video.platformId},#{video.source},#{video.name},#{video.type},#{video.cover},#{video.language},#{video.releaseTime},#{video.state},#{video.note}," +
            "#{video.actor,#{video.director,#{video.area}) \n" +
            "\tON DUPLICATE KEY UPDATE updated_at=#{updatedAt}")
    void inseartOnNotExists(Video video);*/

}
