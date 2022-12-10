package com.nowcoder.community.mapper;

import com.nowcoder.community.entity.DiscussPost;
import jdk.nashorn.internal.runtime.arrays.IntOrLongElements;
import org.apache.ibatis.annotations.Insert;
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
    @Insert({
            "insert into discuss_post(user_id,title,content,type,status,create_time,comment_count,score) values" ,
                    "(#{userId},#{title},#{content},#{type},#{status},#{createTime},#{commentCount},#{score})"
    })
    int insertDiscussPost(DiscussPost discussPost);

    /**
     * 根据id查询帖子
     * @param id
     * @return
     */
    DiscussPost findDiscussById(@Param("id") int id);

    int updateCommentCount(@Param("id") int id,@Param("commentCount") int commentCount);
}
