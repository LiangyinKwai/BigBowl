package com.bb.video;

import com.bb.video.common.constant.TypeEnum;
import com.bb.video.common.task.ScheduledTask;
import com.bb.video.mapper.TypeMapper;
import com.bb.video.model.Type;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VideoApplicationTests {

    @Autowired
    private ScheduledTask scheduledTask;

    @Test
    public void contextLoads() {
//        String url = "http://caiji.kuyun98.com/inc/s_ldg_kkm3u8.php";
        String url = "http://www.zdziyuan.com/inc/s_api_zuidam3u8.php";
//        scheduledTask.collectTask(url);
        scheduledTask.collectTask(url, "");
    }

    @Autowired
    private TypeMapper typeMapper;

    @Test
    public void insertType() {
        List<Type> types = TypeEnum.insertType();
        for (Type type : types) {
            typeMapper.insert(type);
        }
    }

}
