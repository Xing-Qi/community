package com.nowcoder.community.controller.interceptor;

import com.nowcoder.community.annotation.LoginRequired;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @author Oliver
 * @create 2022-12-01 16:04
 */
@Deprecated
@Component
public class LoginRequiredInterceptor  implements HandlerInterceptor {
    @Autowired
    private HostHolder hostHolder;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if(handler instanceof HandlerMethod){
            // HandlerMethod封装了很多属性，
            // 在访问请求方法的时候可以方便的访问到方法、方法参数、方法上的注解、所属类等并且对方法参数封装处理，
            // 也可以方便的访问到方法参数的注解等信息。
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            //method.getDeclaredAnnotations() 获取方法上的所有注解
            //method.getAnnotation(Class<T> annotations) 获取指定的注解类型
            LoginRequired loginRequired = method.getAnnotation(LoginRequired.class);
            User user = hostHolder.getUser();
            if(loginRequired != null && user == null){
                response.sendRedirect(request.getContextPath() + "/login");
                return false;
            }
        }
        return true;
    }
}
