package com.nowcoder.community.util;

import com.nowcoder.community.entity.User;
import org.springframework.stereotype.Component;

/**
 * 线程隔离
 * @author Oliver
 * @create 2022-11-30 16:08
 */
@Component
public class HostHolder {
    private ThreadLocal<User> users = new ThreadLocal<>();
    //存入user
    public void setUser(User user){
        users.set(user);
    }

    public User getUser(){
       return users.get();
    }
    public void clear(){
        users.remove();
    }
}
