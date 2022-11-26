package com.nowcoder.community.service;

import com.nowcoder.community.entity.User;

/**
 * @author Oliver
 * @create 2022-11-25 10:07
 */
public interface UserService {
    User findUserById(int id);
}
