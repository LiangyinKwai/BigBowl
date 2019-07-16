package com.bb.video.common.util;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.plugins.Page;
import com.bb.video.common.constant.Cons;
import com.bb.video.common.xss.SQLFilter;
import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by LiangyinKwai on 2019-07-08.
 */
@Data
public class Query<T> extends LinkedHashMap<String, Object> {

    private static final long serialVersionUID = 1L;
    /**
     * mybatis-plus分页参数
     */
    private Page<T> page;
    /**
     * 当前页码
     */
    private int currPage = 1;
    /**
     * 每页条数
     */
    private int limit = 10;

    public Query(Map<String, Object> params){
        this.putAll(params);

        //分页参数
        if(params.get(Cons.PAGE_INDEX) != null){
            currPage = Integer.parseInt((String)params.get(Cons.PAGE_INDEX));
        }
        if(params.get(Cons.PAGE_SIZE) != null){
            limit = Convert.toInt(params.get(Cons.PAGE_SIZE));
            limit = limit > 500 ? 500 : limit;
        }

        this.put(Cons.OFFSET, (currPage - 1) * limit);
        this.put(Cons.PAGE_INDEX, currPage);
        this.put(Cons.PAGE_SIZE, limit);

        //防止SQL注入（因为sidx、order是通过拼接SQL实现排序的，会有SQL注入风险）
        String sidx = SQLFilter.sqlInject((String)params.get(Cons.SIDX));
        String order = SQLFilter.sqlInject((String)params.get(Cons.ORDER));
        this.put(Cons.SIDX, sidx);
        this.put(Cons.ORDER, order);

        //mybatis-plus分页
        this.page = new Page<>(currPage, limit);

        //排序
        if(StrUtil.isNotBlank(sidx) && StrUtil.isNotBlank(order)){
            this.page.setOrderByField(sidx);
            this.page.setAsc("ASC".equalsIgnoreCase(order));
        }

    }

    public Page<T> getPage() {
        return page;
    }

}
