package com.bb.video.common.task;

import com.bb.video.common.configuration.Collector;
import com.bb.video.common.constant.LanguageEnum;
import com.bb.video.common.constant.PlatformEnum;
import com.bb.video.common.constant.VideoEnum;
import com.bb.video.model.Video;
import com.bb.video.model.VideoCn;
import com.bb.video.service.VideoCnService;
import com.bb.video.service.VideoService;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.http.HttpUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by LiangyinKwai on 2019-06-10.
 */

@Component
@Slf4j
public class ScheduledTask {

    @Autowired
    private Collector collector;

    @Autowired
    private VideoService videoService;

    @Autowired
    private VideoCnService videoCnService;

    @Scheduled(cron = "0 0 2/3 * * ? ")
    public void collectVideo() {

        String domain = "http://caiji.kuyun98.com/inc/s_ldg_kkm3u8.php";
        int source = 0;
        if(domain.contains("kuyun"))
            source = 1;
        if(domain.contains("zdzy"))
            source = 2;
        HashMap<String, Object> params = Maps.newHashMap();
        //active
        params.put("ac", "videolist");
        //ids
        params.put("ids","");
        //wd
        params.put("wd","");
        //h  该参数为采集最新的时间段    24为采集最新的一天的    168为采集最新一周的    为空则采集全部的
        params.put("h","");
        //记录页码
        int pg = 1;
        params.put("pg", pg);
        String paramsStr = HttpUtil.toParams(params, CharsetUtil.CHARSET_UTF_8);
        String url = domain + "?" +paramsStr;
        log.info("===>>>当前请求地址:{}<<<===", url);
        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //视频列表
        Elements listEles = doc.getElementsByTag("list");
        if(CollUtil.isEmpty(listEles))
        {
            return;
        }
        Element listEle = listEles.get(0);
        //当前的页码
        String page = listEle.attr("page");
        //总页数
        String pagecount = listEle.attr("pagecount");
        //页面大小
        String pagesize = listEle.attr("pagesize");
        //总记录数
        String recordcount = listEle.attr("recordcount");
        //视频集合
        Elements videoEles = listEle.select("video");
        saveVideoCn(videoEles);
        log.info("===>>>当前的采集平台:{}, 当前采集的页码:{}, 当前页面大小:{}, 总页数:{}, 当前总记录数<<<===", source, page, pagesize, pagecount, recordcount);
        while (pg < Convert.toInt(pagecount)) {
            params.put("pg", ++ pg);
            paramsStr = HttpUtil.toParams(params, CharsetUtil.CHARSET_UTF_8);
            url = domain + "?" +paramsStr;
            log.info("===>>>当前请求地址:{}<<<===", url);
            try {
                doc = Jsoup.connect(url).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //视频列表
            listEles = doc.getElementsByTag("list");
            if(CollUtil.isEmpty(listEles))
            {
                continue;
            }
            listEle = listEles.get(0);
            //当前的页码
            page = listEle.attr("page");
            //页面大小
            pagesize = listEle.attr("pagesize");
            //总记录数
            recordcount = listEle.attr("recordcount");
            //视频集合
            videoEles = listEle.select("video");
            //总页数
            pagecount = listEle.attr("pagecount");

            log.info("===>>>当前的采集平台:{}, 当前采集的页码:{}, 当前页面大小:{}, 总页数:{}, 当前总记录数<<<===", source, page, pagesize, pagecount, recordcount);

            saveVideoCn(videoEles);
        }

    }

    private void saveVideo(Elements videoEles) {
        ArrayList<Video> videos = Lists.newArrayList();
        //更新时间
//            String last = element.getElementsByTag("last").get(0).text();
        videoEles.forEach(videoEle -> {
            //最后更新时间
            String last = videoEle.getElementsByTag("last").text();
            //平台id
            String id = videoEle.getElementsByTag("id").text();
            //片名
            String name = videoEle.getElementsByTag("name").text();
            //类型
            String type = videoEle.getElementsByTag("type").text();
            //封面
            String cover = videoEle.getElementsByTag("pic").text();
            //语言
            String language = videoEle.getElementsByTag("lang").text();
            //区域
            String area = videoEle.textNodes().get(0).text();
//                String area = videoEle.getElementsByTag("area").text();
            //年份
            String year = videoEle.getElementsByTag("year").text();
            //对应资源的状态   example是否收到侵权律师函
            String state = videoEle.getElementsByTag("state").text();
            //分辨率字幕等说明
            String note = videoEle.getElementsByTag("note").text();
            //演员
            String actor = videoEle.getElementsByTag("actor").text();
            //导演
            String director = videoEle.getElementsByTag("director").text();
            //描述
            String description = videoEle.getElementsByTag("des").text();
            //链接
            Elements ddEle = videoEle.getElementsByTag("dd");
            String flag = ddEle.attr("flag");
            String link = ddEle.text();
            Video video = new Video();
            video.setReleaseTime(new Timestamp(DateUtil.parse(last).getTime()));
            video.setPlatformId(Convert.toInt(id));
            video.setActor(actor);
            video.setDescription(description);
            video.setCover(cover);
            video.setDirector(director);
            video.setLanguage(LanguageEnum.getOrderByName(language));
            video.setArea(area);
            video.setName(name);
            video.setYear(Convert.toInt(year));
            video.setType(VideoEnum.getOrderByCn(type));
            video.setLink(link);
            video.setNote(note);
            video.setState(Convert.toByte(state));
            video.setSource(PlatformEnum.getOrderByName(flag));
            videos.add(video);
        });
        videoService.addVideo(videos);
    }

    private void saveVideoCn(Elements videoEles) {
        ArrayList<VideoCn> videos = Lists.newArrayList();
        //更新时间
//            String last = element.getElementsByTag("last").get(0).text();
        videoEles.forEach(videoEle -> {
            //最后更新时间
            String last = videoEle.getElementsByTag("last").text();
            //平台id
            String id = videoEle.getElementsByTag("id").text();
            //片名
            String name = videoEle.getElementsByTag("name").text();
            //类型
            String type = videoEle.getElementsByTag("type").text();
            //封面
            String cover = videoEle.getElementsByTag("pic").text();
            //语言
            String language = videoEle.getElementsByTag("lang").text();
            //区域
            String area = videoEle.textNodes().get(0).text();
//                String area = videoEle.getElementsByTag("area").text();
            //年份
            String year = videoEle.getElementsByTag("year").text();
            //对应资源的状态   example是否收到侵权律师函
            String state = videoEle.getElementsByTag("state").text();
            //分辨率字幕等说明
            String note = videoEle.getElementsByTag("note").text();
            //演员
            String actor = videoEle.getElementsByTag("actor").text();
            //导演
            String director = videoEle.getElementsByTag("director").text();
            //描述
            String description = videoEle.getElementsByTag("des").text();
            //链接
            Elements ddEle = videoEle.getElementsByTag("dd");
            String flag = ddEle.attr("flag");
            String link = ddEle.text();
            VideoCn video = new VideoCn();
            video.setReleaseTime(new Timestamp(DateUtil.parse(last).getTime()));
            video.setPlatformId(Convert.toInt(id));
            video.setActor(actor);
            video.setDescription(description);
            video.setCover(cover);
            video.setDirector(director);
            video.setLanguage(language);
            video.setArea(area);
            video.setName(name);
            video.setYear(year);
            video.setType(type);
            video.setLink(link);
            video.setNote(note);
            video.setState(Convert.toByte(state));
            video.setSource(PlatformEnum.getOrderByName(flag));
            videos.add(video);
        });
        videoCnService.addVideoCn(videos);
    }

    public static void main(String[] args) {
        //http://caiji.kuyun98.com/inc/s_ldg_kkm3u8.php?ac=videolist&t=&pg=2&h=&ids=&wd=
        //每次任务应该记录时间   每次开始获取该时间    若存在时间   则根据时间差进行赠量采集   若没有    则进行全量采集
        try {
            String domain = "http://caiji.kuyun98.com/inc/s_ldg_kkm3u8.php";
            HashMap<String, Object> params = Maps.newHashMap();
            //active
            params.put("ac", "videolist");
            //ids
            params.put("ids","");
            //wd
            params.put("wd","");
            //h  该参数为采集最新的时间段    24为采集最新的一天的    168为采集最新一周的    为空则采集全部的
            params.put("h","");
            //记录页码
            int pg = 1;
            params.put("pg", pg);
            String paramsStr = HttpUtil.toParams(params, CharsetUtil.CHARSET_UTF_8);
            String url = domain + "?" +paramsStr;
            log.info("===>>>当前请求地址:{}<<<===", url);
            Document doc = Jsoup.connect(url).get();
            //视频列表
            Element listEle = doc.getElementsByTag("list").get(0);
            //当前的页码
            String page = listEle.attr("page");
            //总页数
            String pagecount = listEle.attr("pagecount");
            //页面大小
            String pagesize = listEle.attr("pagesize");
            //总记录数
            String recordcount = listEle.attr("recordcount");
            //视频集合
            Elements videos = listEle.select("video");

            //更新时间
//            String last = element.getElementsByTag("last").get(0).text();
            videos.forEach(videoEle -> {
                //最后更新时间
                String last = videoEle.getElementsByTag("last").text();
                //平台id
                String id = videoEle.getElementsByTag("id").text();
                //片名
                String name = videoEle.getElementsByTag("name").text();
                //类型
                String type = videoEle.getElementsByTag("type").text();
                //封面
                String cover = videoEle.getElementsByTag("pic").text();
                //语言
                String language = videoEle.getElementsByTag("lang").text();
                //区域
                String area = videoEle.textNodes().get(0).text();
//                String area = videoEle.getElementsByTag("area").text();
                //年份
                String year = videoEle.getElementsByTag("year").text();
                //对应资源的状态   example是否收到侵权律师函
                String state = videoEle.getElementsByTag("state").text();
                //分辨率字幕等说明
                String note = videoEle.getElementsByTag("note").text();
                //演员
                String actor = videoEle.getElementsByTag("actor").text();
                //导演
                String director = videoEle.getElementsByTag("director").text();
                //描述
                String description = videoEle.getElementsByTag("des").text();
                //链接
                Elements ddEle = videoEle.getElementsByTag("dd");
                String flag = ddEle.attr("flag");
                String link = ddEle.text();
                Video video = new Video();
                video.setReleaseTime(new Timestamp(DateUtil.parse(last).getTime()));
                video.setPlatformId(Convert.toInt(id));
                video.setActor(actor);
                video.setDescription(description);
                video.setCover(cover);
                video.setDirector(director);
                video.setLanguage(LanguageEnum.getOrderByName(language));
                video.setArea(area);
                video.setName(name);
                video.setYear(Convert.toInt(year));
                video.setType(VideoEnum.getOrderByCn(type));
                video.setLink(link);
                video.setNote(note);
                video.setState(Convert.toByte(state));

            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
