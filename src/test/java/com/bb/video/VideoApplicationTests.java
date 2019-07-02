package com.bb.video;

import com.bb.video.common.task.ScheduledTask;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VideoApplicationTests {

    @Autowired
    private ScheduledTask scheduledTask;

    @Test
    public void contextLoads() {

        scheduledTask.collectVideo();

    }

}
