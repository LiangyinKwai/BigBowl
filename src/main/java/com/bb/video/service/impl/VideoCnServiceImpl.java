package com.bb.video.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.bb.video.common.constant.Cons;
import com.bb.video.common.constant.TypeEnum;
import com.bb.video.common.task.ScheduledTask;
import com.bb.video.common.vo.Resp;
import com.bb.video.mapper.ResourceLibraryMapper;
import com.bb.video.mapper.VideoCnMapper;
import com.bb.video.model.ResourceLibrary;
import com.bb.video.model.VideoCn;
import com.bb.video.service.VideoCnService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.bb.video.vo.req.CollectVideoReq;
import com.bb.video.vo.req.SearchVideoReq;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.highlight.SimpleHTMLEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.net.URLDecoder;
import java.util.List;

/**
 * Created by LiangyinKwai on 2019-07-01.
 */
@Slf4j
@Service
public class VideoCnServiceImpl extends ServiceImpl<VideoCnMapper, VideoCn> implements VideoCnService {

    @Autowired
    private ResourceLibraryMapper resourceLibraryMapper;

    @Autowired
    private ScheduledTask scheduledTask;

    @Override
    public Resp collectVideo(CollectVideoReq collectVideoReq) {
        log.info("----->>>>开始采集, 入参:{}", collectVideoReq);
        Integer resourceLibraryId = collectVideoReq.getResourceLibrary();
        ResourceLibrary resourceLibrary = resourceLibraryMapper.selectById(resourceLibraryId);
        log.info("----->>>>当前要采集的资源库为:{}", resourceLibrary);
        String location = resourceLibrary.getLocation();
        Integer timeRange = collectVideoReq.getTimeRange();
        Object h;
        if(timeRange == 0)
            h = Cons.DAY_HOUR;
        else if(timeRange == 1)
            h = Cons.WEEK_HOUR;
        else
            h = "";
        scheduledTask.collectTask(location, h);
//        String link = "/admin.php/admin/collect/api.html?ac=cj&amp;cjflag=01b5c597b6a8fc558f2db9b656557db2&amp;cjurl=http%3A%2F%2Fwww.zdziyuan.com%2Finc%2Fs_api_zuidam3u8.php&amp;h=24&amp;t=&amp;ids=&amp;wd=&amp;type=1&amp;mid=1&amp;param=";
        return Resp.ok();
    }

    @Override
    public Resp index() {
        Integer[][] allParentType = TypeEnum.getAllParentType();
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

    @Override
    public Resp<Page<VideoCn>> searchVideo(SearchVideoReq searchVideoReq) {
        Integer pageIndex = searchVideoReq.getPageIndex();
        Integer pageSize = searchVideoReq.getPageSize();
        log.info("查找影视,当前查找第{}页,每页{}条", pageIndex, pageSize);
        Page<VideoCn> videoCnPage = selectPage(new Page<VideoCn>((pageIndex - 1) * pageSize, pageSize), new EntityWrapper<VideoCn>().setSqlSelect("id, type_id, cover, state, release_time, note"));
        return Resp.ok(videoCnPage);
    }

}
