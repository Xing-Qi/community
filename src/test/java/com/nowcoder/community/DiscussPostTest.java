package com.nowcoder.community;

import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.mapper.DiscussPostMapper;
import com.nowcoder.community.mapper.UserMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

/**
 * @author Oliver
 * @create 2022-11-23 17:16
 */

@SpringBootTest
public class DiscussPostTest {
    @Autowired
    DiscussPostMapper discussPostMapper;
    @Autowired
    UserMapper userMapper;
    @Test
    @DisplayName("testUpdateType")
    public void testUpdateType(){
        discussPostMapper.updateType(275,0);
    }
    @Test
    @DisplayName("testUpdateStatus")
    public void testUpdateStatus(){
        discussPostMapper.updateStatus(275,0);
    }
    @Test
    public void testDiscussPost(){
        List<DiscussPost> discussPosts = discussPostMapper.selectDiscussPost(101, 0, 10);
        for (DiscussPost discussPost:discussPosts
             ) {
            System.out.println(discussPost);
        }
        int i = discussPostMapper.selectDiscussPostRows(101);
        System.out.println(i
        );
    }
    @Test
    public void testInsertDiscussPost(){
        DiscussPost discussPost = new DiscussPost();
        discussPost.setUserId(101);
        discussPost.setTitle("test");
        discussPost.setContent("testeststsets");
        discussPost.setCreateTime(new Date());
        discussPostMapper.insertDiscussPost(discussPost);
    }
    @Test
    public void testFindDiscussPostById(){
        DiscussPost discussById = discussPostMapper.findDiscussById(280);
        System.out.println(discussById);
    }
    @Test
    public void testUpdateCommentCount(){
        discussPostMapper.updateCommentCount(324,1);
    }
}
