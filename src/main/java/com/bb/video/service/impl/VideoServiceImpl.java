package com.bb.video.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.bb.video.mapper.VideoMapper;
import com.bb.video.model.Video;
import com.bb.video.service.VideoService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * Created by LiangyinKwai on 2019-06-21.
 */
@Service
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video> implements VideoService {

    @Transactional
    public void addVideo(List<Video> batch) {
        if(batch.size() > 10) {
            List<Video> sectionFirst = batch.subList(0, 10);
            insertBatch(sectionFirst);
            List<Video> sectionSecond = batch.subList(10, batch.size());
            insertBatch(sectionSecond);
        }else {
            insertBatch(batch);
        }
    }

}
