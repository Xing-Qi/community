package com.nowcoder.community.service.impl;

import com.nowcoder.community.entity.LoginTicket;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.mapper.LoginTicketMapper;
import com.nowcoder.community.mapper.UserMapper;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author Oliver
 * @create 2022-11-25 10:08
 */
@Service
public class UserServiceImpl implements UserService, CommunityConstant {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MailClient mailClient;
    @Autowired
    private TemplateEngine templateEngine;
    @Autowired
    private LoginTicketMapper loginTicketMapper;
    @Value("${community.path.domain}")
    String domain;
    @Value("${server.servlet.context-path}")
    private String contextPath;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public User findUserById(int id) {
        User user = getCache(id);
        if(user == null){
            user = initCache(id);
        }
        return user;

//        return userMapper.selectUserById(id);
    }

    @Override
    public Map<String, Object> register(User user) {
        Map<String, Object> map = new HashMap<>();
        //1.空值处理
        //1.1用户对象
        if (user == null) {
            throw new IllegalArgumentException("参数不能为空！");
        }
        //1.2用户名
        if (StringUtils.isBlank(user.getUsername())) {
            map.put("usernameMsg", "用户名不能为空！");
            return map;
        }
        //1.3密码
        if (StringUtils.isBlank(user.getPassword())) {
            map.put("passwordMsg", "密码不能为空！");
            return map;
        }
        //1.4邮箱
        if (StringUtils.isBlank(user.getEmail())) {
            map.put("emailMsg", "邮箱不能为空！");
            return map;
        }
        //2.验证账号
        //2.1判断用户名是否存在
        User uName = userMapper.selectUserByName(user.getUsername());
        if (uName != null) {
            map.put("usernameMsg", "用户名已存在！");
            return map;
        }
        //2.2判断邮箱
        User uEmail = userMapper.selectUserByEmail(user.getEmail());
        if (uEmail != null) {
            map.put("emailMsg", "邮箱已存在！");
            return map;
        }
        //3.注册用户
        user.setSalt(CommunityUtil.generateUUID().substring(0, 5));
        user.setPassword(CommunityUtil.md5(user.getPassword() + user.getSalt()));
        user.setUsername(user.getUsername());
        user.setEmail(user.getEmail());
        user.setActivationCode(CommunityUtil.generateUUID());
        user.setType(0);//0表示普通用户,1表示管理员,2表示版主
        user.setStatus(0); //0表示未激活,1表示已激活
        user.setHeaderUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        user.setCreateTime(new Date());
        userMapper.insertUser(user);
        //4.发送激活邮件
        //thymeleafContext
        Context context = new Context();
        context.setVariable("email", user.getEmail());
        //http://hostname/contextPath/activation/userId/code 激活地址
        String url = domain + contextPath + "/" + "activation/" + user.getId() + "/" + user.getActivationCode();
        context.setVariable("url", url);
        String content = templateEngine.process("/mail/activation", context);
        mailClient.sendMail(user.getEmail(), "激活邮件", content);
        return map;
    }

    @Override
    public int activation(int userId, String code) {
        User user = userMapper.selectUserById(userId);
        //判断账号状态
        if (user.getStatus() == 1) {
            return ACTIVATION_REPEAT;
        } else if (user.getActivationCode().equals(code)) {
            userMapper.updateStatus(userId, 1);
            clearCache(userId);
            return ACTIVATION_SUCCESS;
        } else {
            return ACTIVATION_FAILED;
        }
    }

    @Override
    public Map<String, Object> login(String username, String password, int expiredSeconds) {
        Map<String, Object> map = new HashMap();
        User user = userMapper.selectUserByName(username);
        //空值处理
        if (StringUtils.isBlank(username)) {
            map.put("usernameMsg", "用户名不能为空!");
            return map;
        }

        if (StringUtils.isBlank(password)) {
            map.put("passwordMsg", "密码不能为空!");
            return map;
        }
        //验证用户
        //账号
        if (user == null) {
            map.put("usernameMsg", "用户不存在!");
            return map;
        }
        //状态
        if (user.getStatus() == 0) {
            map.put("usernameMsg", "用户未激活!");
            return map;
        }
        //密码
        password = CommunityUtil.md5(password + user.getSalt());
        if (!password.equals(user.getPassword())) {
            map.put("passwordMsg", "密码错误!");
            return map;
        }
        //生成登录凭证
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(user.getId());
        loginTicket.setTicket(CommunityUtil.generateUUID());
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis() + expiredSeconds * 1000));
        //存入登录验证
//        loginTicketMapper.insertLoginTicket(loginTicket);
        String ticketKey = RedisKeyUtil.getTicketKey(loginTicket.getTicket());
        //存入redis中
        redisTemplate.opsForValue().set(ticketKey, loginTicket);
        map.put("ticket", loginTicket.getTicket());
        return map;
    }

    //退出
    @Override
    public void logout(String ticket) {
        String ticketKey = RedisKeyUtil.getTicketKey(ticket);
        //获取redis中的ticket
        LoginTicket loginTicket = (LoginTicket) redisTemplate.opsForValue().get(ticketKey);
        loginTicket.setStatus(1);
        //改变状态
        redisTemplate.opsForValue().set(ticketKey, loginTicket);

//        loginTicketMapper.updateStatus(ticket, 1);

    }

    //查询凭证
    @Override
    public LoginTicket finLoginTicket(String ticket) {
//        return loginTicketMapper.selectLoginTicketByTicket(ticket);
        String ticketKey = RedisKeyUtil.getTicketKey(ticket);
        LoginTicket loginTicket = (LoginTicket) redisTemplate.opsForValue().get(ticketKey);
        return loginTicket;
    }

    @Override
    //修改用户头像
    public int updateHeader(int userId, String headerUrl) {
        int rows = userMapper.upateHeader(userId, headerUrl);
//        return userMapper.upateHeader(userId, headerUrl);
        clearCache(userId);
        return rows;
    }

    @Override
    //修改用户密码
    public int updateUserPassword(int userId, String password) {
        //
        int rows = userMapper.updateUserPassword(userId, password);
        clearCache(userId);
        return rows;
    }

    @Override
    public User selectUserByUsername(String username) {
        return userMapper.selectUserByName(username);
    }

    /**
     * 从redis中获取用户数据
     *
     * @param userId
     * @return
     */
    private User getCache(int userId) {
        String userKey = RedisKeyUtil.getUserKey(userId);
        return (User) redisTemplate.opsForValue().get(userKey);
    }

    /**
     * 初始化redis中的用户数据
     * @param userId
     * @return
     */
    public User initCache(int userId){
        String userKey = RedisKeyUtil.getUserKey(userId);
        User user = userMapper.selectUserById(userId);
        redisTemplate.opsForValue().set(userKey,user,3600, TimeUnit.SECONDS);
        return user;
    }

    /**
     * 清空redis中的数据
     * @param userId
     */
    public void clearCache(int userId){
        String userKey = RedisKeyUtil.getUserKey(userId);
        redisTemplate.delete(userKey);
    }

}
