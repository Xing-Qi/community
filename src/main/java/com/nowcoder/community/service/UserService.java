package com.nowcoder.community.service;

import com.nowcoder.community.entity.LoginTicket;
import com.nowcoder.community.entity.User;

import java.util.Map;

/**
 * @author Oliver
 * @create 2022-11-25 10:07
 */
public interface UserService {
    /**
     * 根据id查询用户
     * @param id
     * @return
     */
    User findUserById(int id);

    /**
     * 注册
     * @param user
     * @return
     */
    Map<String,Object> register(User user);

    /**
     * 激活账号
     * @return
     */
    int activation(int userId ,String code);

    /**
     * 登录
     * @param username
     * @param password
     * @param expiredSeconds
     * @return
     */
    Map<String,Object> login(String username,String password,int expiredSeconds);

    /**
     * 退出
     * @param ticket
     */
    void logout(String ticket);

    /**
     * 根据ticket查询LoginTicket对象
     * @param ticket
     * @return
     */
    LoginTicket finLoginTicket(String ticket);

    /**
     * 更新用户头像
     * @param userId
     * @param url
     * @return
     */
    int updateHeader(int userId,String url);

    /**
     * 根据用户id修改密码
     * @param userId
     * @param password
     * @return
     */
    int updateUserPassword(int userId,String password);

    /**
     * 根据用户名查询用户
     * @param username
     * @return
     */
    User selectUserByUsername(String username);
}
