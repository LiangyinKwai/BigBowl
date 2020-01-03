package com.bb.video.service;

import com.bb.video.common.vo.Resp;
import com.bb.video.model.ResourceLibrary;

import java.util.List;

/**
 * Created by LiangyinKwai on 2019/12/18.
 */
public interface ResourceLibraryService {

    Resp queryResourceLibrary();

    Resp customResourceLibrary(ResourceLibrary resourceLibrary);

    Resp deleteResourceLibrary(Integer id);
}
