package com.bb.video.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.bb.video.common.vo.Resp;
import com.bb.video.model.VideoCn;
import com.bb.video.vo.req.CollectVideoReq;
import com.bb.video.vo.req.SearchVideoReq;

import java.util.List;

/**
 * Created by LiangyinKwai on 2019-07-01.
 */
public interface VideoCnService {

    Resp collectVideo(CollectVideoReq collectVideoReq);

    Resp index();

    void addVideoCn(List<VideoCn> batch);

    Resp<Page<VideoCn>> searchVideo(SearchVideoReq searchVideoReq);

}
