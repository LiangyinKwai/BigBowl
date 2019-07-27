package com.bb.video.service;

import com.bb.video.common.vo.Resp;
import com.bb.video.model.VideoCn;

import java.util.List;

/**
 * Created by LiangyinKwai on 2019-07-01.
 */
public interface VideoCnService {

    Resp index();

    void addVideoCn(List<VideoCn> batch);

}
