package com.bb.video.common.task;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.bb.video.common.configuration.Collector;
import com.bb.video.common.constant.LanguageEnum;
import com.bb.video.common.constant.PlatformEnum;
import com.bb.video.common.constant.TypeEnum;
import com.bb.video.common.constant.VideoEnum;
import com.bb.video.model.MovType;
import com.bb.video.model.Video;
import com.bb.video.model.VideoCn;
import com.bb.video.service.VideoCnService;
import com.bb.video.service.VideoService;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.http.HttpUtil;
import com.bb.video.service.impl.MovTypeServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
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
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

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
    public void collector() {
        //资源地址列表
        List<String> urls = collector.getUrls();
        ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setNameFormat("采集任务").build();
        ExecutorService executorService = Executors.newFixedThreadPool(urls.size(), threadFactory);
        urls.forEach(url -> executorService.execute(() -> collectTask(url)));
        boolean shutdown = executorService.isShutdown();
        log.info("线程池执行状态:{}", shutdown);
        if(shutdown)
            executorService.shutdown();
        boolean terminated = executorService.isTerminated();
        log.info("线程池结束状态:{}", terminated ? "已终止" : "未终止");
    }

    public void collectTask(String url) {
        int source = 0;
        if (url.contains("kuyun"))
            source = 1;
        if (url.contains("zdzy"))
            source = 2;
        HashMap<String, Object> params = Maps.newHashMap();
        //active
        params.put("ac", "videolist");
        //ids
        params.put("ids", "");
        //wd
        params.put("wd", "");
        //h  该参数为采集最新的时间段    24为采集最新的一天的    168为采集最新一周的    为空则采集全部的
        params.put("h", "");
        //记录页码
        int pg = 1;
        params.put("pg", pg);
        String paramsStr = HttpUtil.toParams(params, CharsetUtil.CHARSET_UTF_8);
        url = url + "?" + paramsStr;
        log.info("===>>>当前请求地址:{}<<<===", url);
        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //视频列表
        Elements listEles = doc.getElementsByTag("list");
        if (CollUtil.isEmpty(listEles)) {
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
        log.info("===>>>当前的采集平台:{}, 当前采集的页码:{}, 当前页面大小:{}, 总页数:{}, 当前总记录数:{}<<<===", source, page, pagesize, pagecount, recordcount);
        while (pg < Convert.toInt(pagecount)) {
            params.put("pg", ++pg);
            paramsStr = HttpUtil.toParams(params, CharsetUtil.CHARSET_UTF_8);
            url = url + "?" + paramsStr;
            log.info("===>>>当前请求地址:{}<<<===", url);
            try {
                doc = Jsoup.connect(url).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //视频列表
            listEles = doc.getElementsByTag("list");
            if (CollUtil.isEmpty(listEles)) {
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

            log.info("===>>>当前的采集平台:{}, 当前采集的页码:{}, 当前页面大小:{}, 总页数:{}, 当前总记录数:{}<<<===", source, page, pagesize, pagecount, recordcount);

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
            String area;
            if(language.equals("韩语"))
                area = "韩国";
            else if(language.equals("国语"))
                area = "大陆";
            else
                area = videoEle.textNodes().get(0).text();
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
        for (Element videoEle : videoEles) {//最后更新时间
            String last = videoEle.getElementsByTag("last").text();
            //平台id
            String platformId = videoEle.getElementsByTag("id").text();
            //片名
            String name = videoEle.getElementsByTag("name").text();
            //类型
            String type = videoEle.getElementsByTag("type").text();
            //封面
            String cover = videoEle.getElementsByTag("pic").text();
            //语言
            String language = videoEle.getElementsByTag("lang").text();
            //类型id
            String tid = videoEle.getElementsByTag("tid").text();
            //区域
            String area = "";
            if (videoEle.getElementsByTag("area") != null) {

                if (language.equals(LanguageEnum.KOA.getName()))
                    area = "韩国";
                /*else if (language.equals(LanguageEnum.CHN.getName()))
                    area = "大陆";*/
                else if (language.equals(LanguageEnum.YUE.getName()) && type.equals("香港剧"))
                    area = "香港";
                /*else if (language.equals(LanguageEnum.ENG.getName()))
                    area = "欧美";*/
                else {
                    int size = videoEle.textNodes().size();
                    if (size > 0)
                        area = videoEle.textNodes().get(0).text();
                    else
                        area = "其他";
                }

            }
            if (StrUtil.isEmpty(language)) {
                if (area.equals("香港"))
                    language = "粤语";
                else if (area.equals("美国"))
                    language = "英语";
            }
//            String area = videoEle.getElementsByTag("area").text();
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
            if(description.length() > 5000 && description.contains("\">"))
                description = description.substring(description.lastIndexOf("\">") + 2);
//            log.info("=>>>当前的描述长度:{},内容:{}", description.length(), description);
            //链接
            Elements ddEle = videoEle.getElementsByTag("dd");
            String flag = ddEle.attr("flag");
            String link = ddEle.text();
            VideoCn video = new VideoCn();
            Integer pId = Convert.toInt(platformId);
            video.setReleaseTime(new Timestamp(DateUtil.parse(last).getTime()));
            video.setTypeId(TypeEnum.getIdByZdzyTid(Convert.toInt(tid)));
            video.setPlatformId(pId);
            video.setActor(actor);
            video.setDescription(description);
            video.setCover(cover);
            video.setDirector(director);
            video.setLanguage(language);
            video.setArea(area);
            video.setName(name);
            video.setYear(year);
//            video.setType(type);
            video.setLink(link);
            video.setNote(note);
            video.setState(Convert.toInt(state));
            video.setSource(PlatformEnum.getOrderByName(flag));
            videos.add(video);
        }
        videoCnService.addVideoCn(videos);
    }

    public void collectTypeTask(String url) {
        int source = 0;
        if (url.contains("kuyun"))
            source = 1;
        if (url.contains("zdzy"))
            source = 2;
        HashMap<String, Object> params = Maps.newHashMap();
        //active
        params.put("ac", "videolist");
        //ids
        params.put("ids", "");
        //wd
        params.put("wd", "");
        //h  该参数为采集最新的时间段    24为采集最新的一天的    168为采集最新一周的    为空则采集全部的
        params.put("h", "");
        //记录页码
        int pg = 1;
        params.put("pg", pg);
        String paramsStr = HttpUtil.toParams(params, CharsetUtil.CHARSET_UTF_8);
        String currentUrl = url + "?" + paramsStr;
        log.info("===>>>当前请求地址:{}<<<===", currentUrl);
        Document doc = null;
        try {
            doc = Jsoup.connect(currentUrl).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //视频列表
        Elements listEles = doc.getElementsByTag("list");
        if (CollUtil.isEmpty(listEles)) {
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
        log.info("===>>>当前的采集平台:{}, 当前采集的页码:{}, 当前页面大小:{}, 总页数:{}, 当前总记录数:{}<<<===", source, page, pagesize, pagecount, recordcount);
        while (pg < Convert.toInt(pagecount)) {
            params.put("pg", ++pg);
            paramsStr = HttpUtil.toParams(params, CharsetUtil.CHARSET_UTF_8);
            currentUrl = url + "?" + paramsStr;
            log.info("===>>>当前请求地址:{}<<<===", currentUrl);
            try {
                doc = Jsoup.connect(currentUrl).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //视频列表
            listEles = doc.getElementsByTag("list");
            if (CollUtil.isEmpty(listEles)) {
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

            log.info("===>>>当前的采集平台:{}, 当前采集的页码:{}, 当前页面大小:{}, 总页数:{}, 当前总记录数:{}<<<===", source, page, pagesize, pagecount, recordcount);

            saveVideoCn(videoEles);
        }
        /*ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setNameFormat("采集任务").build();
        ExecutorService executorService = Executors.newFixedThreadPool(2, threadFactory);

        while (pg.get() < Convert.toInt(pagecount)) {
            int finalSource = source;
            executorService.execute(() -> {
                synchronized (ScheduledTask.class) {
                    params.put("pg", pg.incrementAndGet());
                }
                String paramsStrIn = HttpUtil.toParams(params, CharsetUtil.CHARSET_UTF_8);
                String currentUrlIn = url + "?" + paramsStrIn;
                log.info("===>>>当前请求地址:{}<<<===", currentUrlIn);
                Document docIn = null;
                try {
                    docIn = Jsoup.connect(currentUrlIn).get();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //视频列表
                Elements listElesIn = docIn.getElementsByTag("list");
                if (CollUtil.isEmpty(listElesIn)) {
                    return;
                }
                Element listEleIn = listElesIn.get(0);
                //当前的页码
                String pageIn = listEleIn.attr("page");
                //页面大小
                String pagesizeIn = listEleIn.attr("pagesize");
                //总记录数
                String recordcountIn = listEleIn.attr("recordcount");
                //视频集合
                Elements videoElesIn = listEleIn.select("video");
                //总页数
                String pagecountIn = listEleIn.attr("pagecount");

                log.info("===>>>当前的采集平台:{}, 当前采集的页码:{}, 当前页面大小:{}, 总页数:{}, 当前总记录数:{}<<<===", finalSource, pageIn, pagesizeIn, pagecountIn, recordcountIn);

                saveType(videoElesIn);
            });
        }
        boolean shutdown = executorService.isShutdown();
        log.info("线程池执行状态:{}", shutdown);
        if(shutdown)
            executorService.shutdown();
        boolean terminated = executorService.isTerminated();
        log.info("线程池结束状态:{}", terminated ? "已终止" : "未终止");*/
    }

    @Autowired
    private MovTypeServiceImpl movTypeService;

    void saveType(Elements e) {

        for (Element element : e) {
            //类型
            String type = element.getElementsByTag("type").text();
            //类型id
            String tid = element.getElementsByTag("tid").text();
            MovType movType = new MovType();
            movType.setTid(Convert.toInt(tid));
            movType.setType(type);
            List<MovType> movTypes = movTypeService.selectList(new EntityWrapper<MovType>().eq("tid", movType.getTid()).eq("type", type));
            if(CollUtil.isNotEmpty(movTypes))
                continue;
            movTypeService.insert(movType);
        }
    }

}
