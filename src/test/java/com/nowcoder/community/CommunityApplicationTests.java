package com.nowcoder.community;


import com.mysql.cj.jdbc.JdbcConnection;
import com.nowcoder.community.entity.Comment;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.mapper.CommentMapper;
import com.nowcoder.community.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.JdbcProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.activation.DataSource;
import java.util.List;

@SpringBootTest
@Slf4j
class CommunityApplicationTests {
    @Autowired
    UserMapper userMapper;
    @Autowired
    CommentMapper commentMapper;

    @Test
    void contextLoads() {
        User user = userMapper.selectUserById(101);
        System.out.println(user);
//        List<User> nowcoder2 = userMapper.selectUserByName("nowcoder2");
//        List<User> users = userMapper.selectUserByEmail("123456@qq.com");
//        log.info("user:{}",user);
//        log.info("{}",nowcoder2);
//        log.info("users:{}",users);
//        User user2 = new User();
//        user2.setUsername("test2");
//        user2.setPassword("123456");
//        user2.setEmail("123456@qq.com");
//        int i = userMapper.insertUser(user2);
//        System.out.println(user2.getId());
    }

    @Test
    public void test1(){
        Comment comment = commentMapper.selectByPrimaryKey(1);
        System.out.println(comment);
    }

}
