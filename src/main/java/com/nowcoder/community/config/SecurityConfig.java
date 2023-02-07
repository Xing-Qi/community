package com.nowcoder.community.config;

import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.CommunityUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Oliver
 * @create 2023-02-05 16:05
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter implements CommunityConstant {

    @Override
    public void configure(WebSecurity web) throws Exception {
     //忽略静态资源
     web.ignoring().antMatchers("/resources/**");
    }
    //授权
    @Override
    protected void configure(HttpSecurity http) throws Exception {
     http.authorizeRequests()
             //将下列访问路径授权给
             .antMatchers(
                     "/comment/add/**",
                     "/discuss/add",
                     "follow",
                     "/unfollow",
                     "/like",
                     "/letter/**",
                     "/user/setting",
                     "/user/upload",
                     "/notice/**"
             ).hasAnyAuthority(AUTHORITY_ADMIN,
                     AUTHORITY_MODERATOR,
                     AUTHORITY_USER)
             .antMatchers("/discuss/top","/discuss/wonderful").hasAnyAuthority(AUTHORITY_MODERATOR)
             .antMatchers("/discuss/delete").hasAnyAuthority(AUTHORITY_ADMIN)
             .antMatchers("/data/**").hasAnyAuthority(AUTHORITY_ADMIN)
             .anyRequest().permitAll()//除了以上路径任意访问
             .and().csrf().disable();//禁用csrf
        ;

        //权限不够时的处理
        http.exceptionHandling()
                //没有登录
                .authenticationEntryPoint(new AuthenticationEntryPoint() {
                    @Override
                    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
                        //获取请求头中的数据，判断是否为异步请求
                        String xRequestedWith = request.getHeader("x-requested-with");
                        //请求头中x-requested-with等于XMLHttpRequest
                        // 则为异步请求：返回json字符串
                        // 否则为同步：重定向页面
                        if("XMLHttpRequest".equals(xRequestedWith)){
                            //application/plain:普通json字符串
                            response.setContentType("application/plain;charset=utf-8");
                            //设置字符流
                            PrintWriter writer = response.getWriter();
                            writer.write(CommunityUtil.getJsonString(403,"你还没有登录！"));
                        }else {
                            //重定向到登录页面
                            response.sendRedirect(request.getContextPath()+"/login");
                        }
                    }
                })
                //权限不足
                .accessDeniedHandler(new AccessDeniedHandler() {
                    @Override
                    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
                        String xRequestedWith = request.getHeader("x-requested-with");
                        if("XMLHttpRequest".equals(xRequestedWith)){
                            response.setContentType("application/plain;charset=utf-8");
                            PrintWriter writer = response.getWriter();
                            writer.write(CommunityUtil.getJsonString(403,"权限不足！"));
                        }else {
                            response.sendRedirect(request.getContextPath() + "/denied");
                        }
                    }
                });

        //security底层默认拦截"/logout"请求
        //覆盖默认逻辑，才能执行退出代码
        http.logout().logoutUrl("/security/logout");
    }
}
