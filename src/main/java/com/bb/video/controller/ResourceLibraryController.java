package com.bb.video.controller;

import com.bb.video.common.vo.Resp;
import com.bb.video.model.ResourceLibrary;
import com.bb.video.service.ResourceLibraryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by LiangyinKwai on 2019/12/18.
 */

@Api(description = "自定义资源库")
@RestController
@RequestMapping("resource")
public class ResourceLibraryController {

    @Autowired
    private ResourceLibraryService resourceLibraryService;

    @GetMapping("list")
    @ApiOperation("自定义资源库的列表")
    public Resp<List<ResourceLibrary>> queryResourceLibrary() {
        return resourceLibraryService.queryResourceLibrary();
    }

    @PostMapping("create")
    @ApiOperation("自定义资源库的新建/编辑")
    public Resp customResourceLibrary(@RequestBody ResourceLibrary sourceLibraryReq) {
        return resourceLibraryService.customResourceLibrary(sourceLibraryReq);
    }

    @ApiImplicitParam(name = "id", value = "指定的id", paramType = "PATH")
    @ApiOperation("删除指定的自定义影集资源库")
    @GetMapping("delete/{id}")
    public Resp deleteResourceLibrary(@PathVariable("id") Integer id) {
        return resourceLibraryService.deleteResourceLibrary(id);
    }

}
