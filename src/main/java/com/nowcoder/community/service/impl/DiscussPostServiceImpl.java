package com.nowcoder.community.service.impl;

import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.mapper.DiscussPostMapper;
import com.nowcoder.community.service.DiscussPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Oliver
 * @create 2022-11-25 10:02
 */
@Service
public class DiscussPostServiceImpl  implements DiscussPostService {
    @Autowired
    private DiscussPostMapper discussPostMapper;
    @Override
    public List<DiscussPost> findDiscussPost(int id, int offset, int limit) {
       return discussPostMapper.selectDiscussPost(id,offset,limit);
    }

    @Override
    public int findDiscussPostRows(int id) {
      return   discussPostMapper.selectDiscussPostRows(id);
    }
}
