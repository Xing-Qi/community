package com.nowcoder.community.service;

import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.mapper.DiscussPostMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.PrivateKey;
import java.util.List;

/**
 * @author Oliver
 * @create 2022-11-25 9:58
 */
public interface DiscussPostService {
    /**
     * 查找评论
     * @param id
     * @param offset
     * @param limit
     * @return
     */
    List<DiscussPost> findDiscussPost(int id,int offset,int limit);

    /**
     * 查询评论条数
     * @param id
     * @return
     */
    int findDiscussPostRows(int id);
}
