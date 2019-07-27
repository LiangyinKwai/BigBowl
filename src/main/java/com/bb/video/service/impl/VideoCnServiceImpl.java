package com.bb.video.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.bb.video.common.vo.Resp;
import com.bb.video.mapper.VideoCnMapper;
import com.bb.video.model.VideoCn;
import com.bb.video.service.VideoCnService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by LiangyinKwai on 2019-07-01.
 */
@Service
public class VideoCnServiceImpl extends ServiceImpl<VideoCnMapper, VideoCn> implements VideoCnService {

    @Override
    public Resp index() {
        Page<VideoCn> videoCnPage = selectPage(new Page<VideoCn>(), new EntityWrapper<VideoCn>().setSqlSelect("id, type_id, cover, state, release_time, note"));
        return null;
    }

    @Transactional
    @Override
    public void addVideoCn(List<VideoCn> batch) {
        if(batch.size() > 10) {
            int size = batch.size();
            for (int i = 0; i < Integer.MAX_VALUE; i++) {
                if(size - i * 10 > 10)
                {
                    List<VideoCn> section = batch.subList(i * 10, i * 10 + 10);
                    insertBatch(section);
                    continue;
                }else
                {
                    List<VideoCn> section = batch.subList(i * 10, size);
                    insertBatch(section);
                    break;
                }
            }
        }else {
            insertBatch(batch);
        }

    }

}
