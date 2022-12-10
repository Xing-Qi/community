package com.nowcoder.community;

import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.SensitiveFilter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Oliver
 * @create 2022-12-03 12:00
 */
@Slf4j
@SpringBootTest
public class SensitiveTests {

    @Autowired
    private SensitiveFilter sensitiveFilter;
    @Test
    public void testSensitiveFilter(){
        String text = "我要赌博";
        text = sensitiveFilter.filter(text);
        log.debug(text);
    }
    @Test
    public void testGetJson(){
        CommunityUtil communityUtil = new CommunityUtil();
        Map map = new HashMap<>();
        map.put("name","zhang");
        map.put("age",20);
        String json = communityUtil.getJsonString(0, "ok", map);
        System.out.println(json);
    }
}
