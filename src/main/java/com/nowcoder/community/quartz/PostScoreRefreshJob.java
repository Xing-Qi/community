package com.nowcoder.community.quartz;

import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.service.ElasticSearchService;
import com.nowcoder.community.service.LikeService;
import com.nowcoder.community.service.impl.DiscussPostServiceImpl;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.RedisKeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Oliver
 * @create 2023-02-09 16:39
 */
@Slf4j
public class PostScoreRefreshJob implements Job , CommunityConstant {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private DiscussPostServiceImpl discussPostService;
    @Autowired
    private LikeService likeService;
    @Autowired
    private ElasticSearchService elasticSearchService;
    //定义时间纪元
    private static final Date EPOCH;

    static {
        try {
            EPOCH = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2014-08-01 00:00:00");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        String redisKey = RedisKeyUtil.getPostScoreKey();
        //BoundValueOperations就是一个绑定key的对象，我们可以通过这个对象来进行与key相关的操作
        BoundSetOperations operations = redisTemplate.boundSetOps(redisKey);
        if(operations.size()==0){
            log.info("任务取消，没有需要刷新的帖子！");
            return;
        }
        log.info("[任务开始] 正在刷新帖子分数"+operations.size());
        while (operations.size() > 0){
            this.refresh((Integer) operations.pop());
        }
        log.info("[任务结束] 帖子分数刷新完毕！");
    }

    /**
     * 刷新帖子分数
     * @param postId
     */
    private void refresh(int postId) {
        DiscussPost post = discussPostService.findDiscussById(postId);
        if (post==null){
            log.error("该帖子不存在：id=" + postId);
            return;
        }
        //是否精华
        boolean wonderful = post.getStatus() == 1;
        //评论数量
        int commentCount = post.getCommentCount();
        //点赞数量
        long likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_POST, postId);

        //计算权重 log(精华分 + 评论数*10 + 点赞数*2 + 收藏数*2) + (发布时间 – 牛客纪元)
        double w = (wonderful ? 75 : 0) + commentCount * 10 + likeCount;
        double score = Math.log10(Math.max(w, 1)) + (post.getCreateTime().getTime() - EPOCH.getTime()) / (1000 * 3600 * 24);
        //更新帖子分数
        discussPostService.updateScore(postId,score);
        //同步搜索数据
        post.setScore(score);
        elasticSearchService.saveDiscussPost(post);
    }

}
