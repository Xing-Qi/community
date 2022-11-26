package com.nowcoder.community.mapper;

import com.nowcoder.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Oliver
 * @create 2022-11-23 16:49
 */
@Mapper
public interface DiscussPostMapper {
    /**
     * 分页查询帖子
     * @param userId
     * @param offset
     * @param limit
     * @return
     */
    List<DiscussPost> selectDiscussPost(
          @Param("userId") int userId,
          @Param("offset") int offset,
          @Param("limit") int limit

    );

    /**
     * 根据用户id查询帖子数量
     * @param userId
     * @return
     */
    int selectDiscussPostRows(@Param("userId") int userId);
}
