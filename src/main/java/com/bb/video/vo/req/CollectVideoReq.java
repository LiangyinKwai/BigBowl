package com.bb.video.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Created by LiangyinKwai on 2019/12/25.
 */
@Data
@ApiModel(description = "视频采集规则")
public class CollectVideoReq {

    @NotNull(message = "需要指定要采集的资源库")
    @ApiModelProperty("资源库平台的编号")
    private Integer resourceLibrary;

    @NotNull(message = "需要指定采集的时间跨度")
    @ApiModelProperty("采集的时间跨度 0:当天； 1:本周;  其他数值:所有")
    private Integer timeRange;

}
