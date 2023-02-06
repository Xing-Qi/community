package com.nowcoder.community.service.impl;

import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.mapper.DiscussPostMapper;
import com.nowcoder.community.service.DiscussPostService;
import com.nowcoder.community.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * @author Oliver
 * @create 2022-11-25 10:02
 */
@Service
public class DiscussPostServiceImpl implements DiscussPostService {
    @Autowired
    private DiscussPostMapper discussPostMapper;
    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Override
    public List<DiscussPost> findDiscussPost(int id, int offset, int limit) {
        return discussPostMapper.selectDiscussPost(id, offset, limit);
    }

    @Override
    public int findDiscussPostRows(int id) {
        return discussPostMapper.selectDiscussPostRows(id);
    }

    @Override
    public int addDiscussPost(DiscussPost post) {
        //判空
        if(post==null){
            throw  new IllegalArgumentException("参数不能为空");
        }
        //转义HTML标记 HtmlUtils.htmlEscape()
        post.setTitle(HtmlUtils.htmlEscape(post.getTitle()));
        post.setContent(HtmlUtils.htmlEscape(post.getContent()));

        //过滤敏感词
        post.setTitle(sensitiveFilter.filter(post.getTitle()));
        post.setContent(sensitiveFilter.filter(post.getContent()));

        //插入数据，异常情况放到最后统一处理
        return discussPostMapper.insertDiscussPost(post);
    }

    @Override
    public DiscussPost findDiscussById(int id) {
        return discussPostMapper.findDiscussById(id);
    }

    @Override
    public int updateCommentCount(int id, int commentCount) {
        return discussPostMapper.updateCommentCount(id,commentCount);
    }
    public int updateType(int id,int type){
       return discussPostMapper.updateType(id,type);
    }
    public int updateStatus(int id,int status){
        return discussPostMapper.updateStatus(id,status);
    }

}
