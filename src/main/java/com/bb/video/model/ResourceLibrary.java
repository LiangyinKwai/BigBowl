package com.bb.video.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by LiangyinKwai on 2019/12/18.
 */

@Data
@ApiModel(description = "用于自定义资源库的新建/编辑")
public class ResourceLibrary {

    @ApiModelProperty("自定义资源库的编号,当且仅当当前操作为编辑时才传该值")
    private Integer id;

    @ApiModelProperty("自定义资源库的名称")
    private String name;

    @ApiModelProperty("自定义资源库的链接地址")
    private String location;

    @ApiModelProperty("自定义资源库的参数")
    private String params;
}
