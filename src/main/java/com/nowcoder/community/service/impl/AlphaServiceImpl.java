package com.nowcoder.community.service.impl;

import com.nowcoder.community.dao.AlphaDao;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.mapper.DiscussPostMapper;
import com.nowcoder.community.mapper.UserMapper;
import com.nowcoder.community.service.AlphaService;
import com.nowcoder.community.util.CommunityUtil;
import com.sun.org.apache.bcel.internal.generic.NEW;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Date;

/**
 * @author Oliver
 * @create 2022-11-20 16:36
 */
@Service
@Slf4j
public class AlphaServiceImpl implements AlphaService {
    @Autowired
    private AlphaDao alphaDao;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private DiscussPostMapper discussPostMapper;
    @Autowired
    private TransactionTemplate transactionTemplate;

    @Override
    public String select() {
        return alphaDao.select();
    }

    /**
     * 使用注解实现事务管理
     * @return
     */
    //REQUIRED 支持当前事务（外部事务），如果不存在则创建新事务
    //REQUIRES_NEW 创建一个新事务，并且暂停当前事务（外部事务）
    //NESTED 如果当前存在事务（外部事物），则嵌套在该事务中执行（独立的提交和回滚），否则和REQUIRED一样
    @Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
    public Object save1(){
        //插入用户
        User user = new User();
        user.setUsername("transaction");
        user.setSalt(CommunityUtil.generateUUID().substring(0,5));
        user.setPassword(CommunityUtil.md5("123" + user.getSalt()));
        user.setEmail("transaction@1231.com");
        user.setHeaderUrl("http://www.nowcoder.com/head/77t.png");
        user.setCreateTime(new Date());
        userMapper.insertUser(user);
        //发布帖子
        DiscussPost post = new DiscussPost();
        post.setUserId(user.getId());
        post.setTitle("test");
        post.setContent("xxxx");
        post.setCreateTime(new Date());
        discussPostMapper.insertDiscussPost(post);

        Integer.valueOf("abc");
        return "ok";

    }

    /**
     * 使用TransactionTemplate实现
     * @return
     */
    @Override
    public Object save2() {
        //设置隔离级别
        transactionTemplate.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
        //设置事务传播行为
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        //回调并返回
        return transactionTemplate.execute(new TransactionCallback<Object>() {

            @Override
            //实现事务方法
            public Object doInTransaction(TransactionStatus status) {
                //插入用户
                User user = new User();
                user.setUsername("transaction2");
                user.setSalt(CommunityUtil.generateUUID().substring(0,5));
                user.setPassword(CommunityUtil.md5("123" + user.getSalt()));
                user.setEmail("transaction@1231.com");
                user.setHeaderUrl("http://www.nowcoder.com/head/77t.png");
                user.setCreateTime(new Date());
                userMapper.insertUser(user);
                //发布帖子
                DiscussPost post = new DiscussPost();
                post.setUserId(user.getId());
                post.setTitle("test2");
                post.setContent("xxxx2");
                post.setCreateTime(new Date());
                discussPostMapper.insertDiscussPost(post);

                Integer.valueOf("abc");
                return "ok";
            }
        });
    }
    @Async
    public void execute1(){
        log.debug("execute1");
    }
//    @Scheduled(initialDelay = 10000,fixedRate = 1000)
    public void execute2(){
        log.debug("execute2");
    }
}
