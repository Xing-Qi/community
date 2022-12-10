package com.nowcoder.community.mapper;

import com.nowcoder.community.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

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
}