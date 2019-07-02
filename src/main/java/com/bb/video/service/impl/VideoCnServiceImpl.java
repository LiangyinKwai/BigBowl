package com.bb.video.service.impl;

import com.bb.video.mapper.VideoCnMapper;
import com.bb.video.model.VideoCn;
import com.bb.video.service.VideoCnService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by LiangyinKwai on 2019-07-01.
 */
@Service
public class VideoCnServiceImpl extends ServiceImpl<VideoCnMapper, VideoCn> implements VideoCnService {

    @Override
    public void addVideoCn(List<VideoCn> batch) {


    }
}
