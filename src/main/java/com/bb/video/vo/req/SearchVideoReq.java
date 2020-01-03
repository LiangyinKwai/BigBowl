package com.bb.video.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by LiangyinKwai on 2019/12/23.
 */
@Data
@ApiModel
public class SearchVideoReq {

    @ApiModelProperty("根据名称来查找")
    private String name;

    @ApiModelProperty("根据类型的来查找")
    private String type;

    @ApiModelProperty("根据地区来查找")
    private String area;

    @ApiModelProperty("根据语言来查找")
    private Integer language;

    @ApiModelProperty("页码")
    private Integer pageIndex = 1;

    @ApiModelProperty("单个页面的大小")
    private Integer pageSize = 20;

}
