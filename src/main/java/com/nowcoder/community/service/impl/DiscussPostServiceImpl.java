package com.nowcoder.community.service.impl;

import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.mapper.DiscussPostMapper;
import com.nowcoder.community.service.DiscussPostService;
import com.nowcoder.community.util.SensitiveFilter;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Oliver
 * @create 2022-11-25 10:02
 */
@Service
@Slf4j
public class DiscussPostServiceImpl implements DiscussPostService {
    @Autowired
    private DiscussPostMapper discussPostMapper;
    @Autowired
    private SensitiveFilter sensitiveFilter;
    @Value("${caffeine.posts.max-size}")
    private int postsMaxSize;
    @Value("${caffeine.posts.expired-seconds}")
    private int PostsExpiredSeconds;

    //Caffeine核心接口:Cache,LoadingCache,AsyncLoadingCache
    //帖子列表缓存
    private LoadingCache<String, List<DiscussPost>> postListCache;
    //帖子总数缓存
    private LoadingCache<Integer, Integer> postRowsCache;

    //初始化缓存数据
    @PostConstruct
    public void init() {
        //初始化帖子列表缓存
        postListCache = Caffeine.newBuilder()
                .maximumSize(postsMaxSize)
                .expireAfterWrite(PostsExpiredSeconds, TimeUnit.SECONDS)
                .build(new CacheLoader<String, List<DiscussPost>>() {
                    @Override
                    public @Nullable List<DiscussPost> load(@NonNull String key) throws Exception {
                        if (key == null || key.length() == 0) {
                            throw new IllegalArgumentException("参数错误");
                        }
                        String[] params = key.split(":");
                        if (params == null || params.length == 0) {
                            throw new IllegalArgumentException("参数错误");
                        }
                        int offset = Integer.valueOf(params[0]);
                        int limit = Integer.valueOf(params[1]);
                        log.debug("load post list from DB");
                        return discussPostMapper.selectDiscussPost(0, offset, limit, 1);

                    }
                });
        //初始化帖子总数缓存
        postRowsCache = Caffeine.newBuilder()
                .maximumSize(postsMaxSize)
                .expireAfterWrite(PostsExpiredSeconds, TimeUnit.SECONDS)
                .build(new CacheLoader<Integer, Integer>() {
                    @Override
                    public @Nullable Integer load(@NonNull Integer key) throws Exception {
                        log.debug("load postRows list from DB");
                        return discussPostMapper.selectDiscussPostRows(key);
                    }
                });
    }

    @Override
    public List<DiscussPost> findDiscussPost(int userId, int offset, int limit, int orderMode) {
        if(userId==0&&orderMode==1){//只关注userId==0和orderMode==1情况
            return postListCache.get(offset+":"+limit);
        }
        log.debug("load post list from DB");
        return discussPostMapper.selectDiscussPost(userId, offset, limit, orderMode);
    }

    @Override
    public int findDiscussPostRows(int userId) {
        if(userId==0){
            return postRowsCache.get(userId);
        }
        log.debug("load post list from DB");
        return discussPostMapper.selectDiscussPostRows(userId);
    }

    @Override
    public int addDiscussPost(DiscussPost post) {
        //判空
        if (post == null) {
            throw new IllegalArgumentException("参数不能为空");
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
        return discussPostMapper.updateCommentCount(id, commentCount);
    }

    public int updateType(int id, int type) {
        return discussPostMapper.updateType(id, type);
    }

    public int updateStatus(int id, int status) {
        return discussPostMapper.updateStatus(id, status);
    }

    public int updateScore(int postId, double score) {
        return discussPostMapper.updateScore(postId, score);
    }
}
