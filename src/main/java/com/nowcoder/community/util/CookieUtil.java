package com.nowcoder.community.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Oliver
 * @create 2022-11-30 11:44
 */
public class CookieUtil {
    /**
     * 获取cookie
     * @param request
     * @param name
     * @return
     */
    public static String getCookie(HttpServletRequest request,String name){
        if(request == null || name == null){
            throw new IllegalArgumentException("参数为空!");
        }
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for (Cookie cookie:cookies
                 ) {
                if(cookie.getName().equals(name)){
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

}
