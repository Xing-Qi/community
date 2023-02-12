package com.nowcoder.community;

import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.service.impl.DiscussPostServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

/**
 * @author Oliver
 * @create 2023-02-11 23:39
 */
@SpringBootTest
public class CaffeineTests {
    @Autowired
    private DiscussPostServiceImpl discussPostService;

    @Test
    public void initDataForTest() {
        for (int i = 0; i < 100000; i++) {
            DiscussPost post = new DiscussPost();
            post.setUserId(111);
            post.setTitle("互联网秋招暖春计划");
            post.setContent("xxxxxxxxxxxxxxxxxxxxx互联网XXX");
            post.setCreateTime(new Date());
            post.setScore(Math.random() * 2000);
            discussPostService.addDiscussPost(post);
        }
    }
    @Test
    public void testCache(){
        System.out.println(discussPostService.findDiscussPost(0,0,10,1));
        System.out.println(discussPostService.findDiscussPost(0,0,10,1));
        System.out.println(discussPostService.findDiscussPost(0,0,10,1));
        System.out.println(discussPostService.findDiscussPost(0,0,10,0));
    }


}
