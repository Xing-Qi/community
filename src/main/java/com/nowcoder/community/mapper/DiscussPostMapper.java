package com.nowcoder.community.mapper;

import com.nowcoder.community.entity.DiscussPost;
import jdk.nashorn.internal.runtime.arrays.IntOrLongElements;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
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
     * @param orderMode 1按热度排序
     * @return
     */
    List<DiscussPost> selectDiscussPost(
          @Param("userId") int userId,
          @Param("offset") int offset,
          @Param("limit") int limit,
          @Param("orderMode") int orderMode

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
    @Options(useGeneratedKeys = true,keyProperty = "id")
    int insertDiscussPost(DiscussPost discussPost);

    /**
     * 根据id查询帖子
     * @param id
     * @return
     */
    DiscussPost findDiscussById(@Param("id") int id);

    /**
     * 更新评论数量
     * @param id
     * @param commentCount
     * @return
     */
    int updateCommentCount(@Param("id") int id,@Param("commentCount") int commentCount);

    /**
     * 根据帖子id更改帖子类型  0-普通; 1-置顶;
     *
     * @param id
     * @return
     */
    int updateType(int id,int type);

    /**
     * 根据id修改帖子状态 0-正常; 1-精华; 2-拉黑;
     * @param id
     * @return
     */
    int updateStatus(int id,int status);

    /**
     * 更新帖子分数
     * @param postId
     * @param score
     * @return
     */
    int updateScore(int postId, double score);
}
