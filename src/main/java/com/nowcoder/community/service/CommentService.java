package com.nowcoder.community.service;

import com.nowcoder.community.entity.Comment;
import com.nowcoder.community.mapper.CommentMapper;
import com.nowcoder.community.mapper.DiscussPostMapper;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import javax.swing.text.html.HTML;
import java.util.List;

/**
 * @author Oliver
 * @create 2022-12-06 16:55
 */
@Service
public class CommentService implements CommunityConstant {
    @Autowired
    public CommentMapper commentMapper;
    @Autowired
    private DiscussPostMapper discussPostMapper;
    @Autowired
    private SensitiveFilter sensitiveFilter;

    /**
     * 分页查找评论
     *
     * @param entityType 实体类型
     * @param entityId   实体id
     * @param offset     偏移量
     * @param limit      每页显示的条数
     * @return
     */
    public List<Comment> findCommentByEntity(int entityType, int entityId, int offset, int limit) {
        return commentMapper.selectCommentByEntity(entityType, entityId, offset, limit);
    }

    /**
     * 查询评论条数
     * @param entityType
     * @param entityId
     * @return
     */
    public int findCommentCount(int entityType,int entityId){
        return commentMapper.selectCountByEntity(entityType,entityId);
    }

    /**
     * 新增评论
     * @param comment
     * @return
     */
    @Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
    public int addComment(Comment comment){
        //判空
        if(comment == null){
            throw new IllegalArgumentException("参数不能为空");
        }
        //添加评论
        //转义字符
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        //过滤敏感字符
        comment.setContent(sensitiveFilter.filter(comment.getContent()));
        //返回插入的评论数量
        int rows = commentMapper.insertComment(comment);

        //更新帖子评论数量
        if(comment.getEntityType() == ENTITY_TYPE_POST){
            //查询帖子的评论数量
            int commentCount = commentMapper.selectCountByEntity(comment.getEntityType(), comment.getEntityId());
            discussPostMapper.updateCommentCount(comment.getEntityId(),commentCount);
        }
        return rows;
    }

    public List<Comment> findCommentByUser(int userId,int offset,int limit){
        return commentMapper.selectCommentByUser(userId,offset,limit);
    }
    public Comment findCommentByCommentId(int commentId){
        return commentMapper.selectCommentById(commentId);
    }
    public int findCommentCountByUserId(int userId){
        return commentMapper.selectCommentCountByUserId(userId);
    }

}
