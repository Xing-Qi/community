package com.nowcoder.community.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.UUID;

/**
 * @author Oliver
 * @create 2022-11-26 19:57
 */
public class CommunityUtil {
    //生产随机字符串
    public static String generateUUID(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }
    //md5加密
    public static String md5(String key){
        if(StringUtils.isBlank(key)){
           return null;
        }
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }
}
