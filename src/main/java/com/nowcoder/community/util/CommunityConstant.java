package com.nowcoder.community.util;

/**
 * @author Oliver
 * @create 2022-11-27 15:45
 */
public interface CommunityConstant {
    /**
     * 激活成功
     */
    int ACTIVATION_SUCCESS = 0;
    /**
     * 重复激活
     */
    int ACTIVATION_REPEAT = 1;
    /**
     * 激活失败
     */
    int ACTIVATION_FAILED = 2;
    /**
     * 默认状态的凭证超时时间12小时
     */
    int DEFAULT_EXPIRED_SECONDS = 3600 * 12;
    /**
     * 记住状态下的登录凭证超过时间100天
     */
    int REMEMBER_EXPIRED_SECONDS = 3600 * 24 * 15;
}
