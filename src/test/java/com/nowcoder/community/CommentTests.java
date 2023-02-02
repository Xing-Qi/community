package com.nowcoder.community;

import com.nowcoder.community.entity.Comment;
import com.nowcoder.community.mapper.CommentMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

/**
 * @author Oliver
 * @create 2022-12-07 16:50
 */
@SpringBootTest
public class CommentTests {
    @Autowired
    private CommentMapper commentMapper;

    @Test
    @DisplayName("根据评论id查询评论")
    public void  testselectCommentById(){
        Comment comment = commentMapper.selectCommentById(215);
        System.out.println(comment);
    }
    @Test
    @DisplayName("根据用户id查询评论数量")
    public void testselectCommentCountByUserId(){
        int count = commentMapper.selectCommentCountByUserId(111);
        System.out.println(count);
    }
    @Test
    @DisplayName("根据userId查询评论")
    public void testselectCommentByUser(){
        List<Comment> comments = commentMapper.selectCommentByUser(111, 0, 20);
        for (Comment comment : comments){
            System.out.println(comment);
        }

    }
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
