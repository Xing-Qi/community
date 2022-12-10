package com.nowcoder.community;

import com.nowcoder.community.entity.Comment;
import com.nowcoder.community.mapper.CommentMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

/**
 * @author Oliver
 * @create 2022-12-07 16:50
 */
@SpringBootTest
public class CommentTests {
    @Autowired
    private CommentMapper commentMapper;
    @Test
    public void testInsertComment(){
        Comment comment = new Comment();

        comment.setUserId(149);
        comment.setContent("测试数据");
        comment.setTargetId(0);
        comment.setEntityType(1);
        comment.setCreateTime(new Date());
        comment.setEntityId(280);
        comment.setStatus(0);

        int result = commentMapper.insertComment(comment);
        System.out.println(result);
    }
}
