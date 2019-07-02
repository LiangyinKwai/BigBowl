package com.bb.video.common.configuration;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import java.util.List;

/**
 * Created by LiangyinKwai on 2019-06-10.
 */

@Component
@ConfigurationProperties(prefix = "collect")
@Data
public class Collector {

    private List<String> urls;

}
