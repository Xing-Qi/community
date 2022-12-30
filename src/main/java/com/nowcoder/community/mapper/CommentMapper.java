package com.nowcoder.community.mapper;

import com.nowcoder.community.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommentMapper {
    /**
     *分页查询数据
     * @param entityType
     * @param entityId
     * @param offset
     * @param limit
     * @return
     */
    List<Comment> selectCommentByEntity(int entityType,int entityId,int offset,int limit);

    /**
     * 根据实体类型和实体id查询条数
     * @param entityType
     * @param entityId
     * @return
     */
    int selectCountByEntity(int entityType,int entityId);

    /**
     * 插入评论
     * @param comment
     * @return
     */
    int insertComment(Comment comment);

    /**
     * 根据userId查询评论
     * @param userId
     * @param offset
     * @param limit
     * @return
     */
    List<Comment> selectCommentByUser(int userId,int offset,int limit);

    /**
     * 根据评论id查询评论
     * @param commentId
     * @return
     */
    Comment selectCommentById(@Param("id") int commentId);

    /**
     * 跟据用户id查询评论数量
     * @param userId
     * @return
     */
    int selectCommentCountByUserId(int userId);

}