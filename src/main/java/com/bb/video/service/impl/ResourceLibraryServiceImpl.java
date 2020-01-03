package com.bb.video.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.bb.video.common.vo.Resp;
import com.bb.video.mapper.ResourceLibraryMapper;
import com.bb.video.model.ResourceLibrary;
import com.bb.video.service.ResourceLibraryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by LiangyinKwai on 2019/12/18.
 */

@Slf4j
@Service
public class ResourceLibraryServiceImpl extends ServiceImpl<ResourceLibraryMapper, ResourceLibrary> implements ResourceLibraryService {

    @Override
    public Resp<List<ResourceLibrary>> queryResourceLibrary() {
        return Resp.ok(selectList(new EntityWrapper()));
    }

    @Transactional
    @Override
    public Resp customResourceLibrary(ResourceLibrary resourceLibrary) {
        Integer id = resourceLibrary.getId();
        if(null != id)
        {
            //编辑操作
            updateById(resourceLibrary);
        }else {
            //新增操作
            insert(resourceLibrary);
        }
        return Resp.ok();
    }

    @Transactional
    @Override
    public Resp deleteResourceLibrary(Integer id) {
        boolean b = deleteById(id);
        return b ? Resp.ok() : Resp.error();
    }
}
