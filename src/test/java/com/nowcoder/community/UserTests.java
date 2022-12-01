package com.nowcoder.community;

import com.nowcoder.community.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Oliver
 * @create 2022-11-30 22:21
 */
@SpringBootTest
public class UserTests {
    @Autowired
    private UserMapper userMapper;
    @Test
    public void testUpdateHeaderUrl(){
        userMapper.upateHeader(160,"http://images.nowcoder.com/head/457t.png");
    }
    @Test
    public void testUpdateUserPassword(){
        userMapper.updateUserPassword(101,"123456");
    }
}
