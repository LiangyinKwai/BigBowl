package com.bb.video.mapper;

import com.bb.video.model.VideoCn;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;

/**
 * Created by LiangyinKwai on 2019-07-01.
 */
public interface VideoCnMapper extends BaseMapper<VideoCn> {

    void addVideoCn(List<VideoCn> batch);

}
