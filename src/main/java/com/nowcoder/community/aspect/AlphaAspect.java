package com.nowcoder.community.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * @author Oliver
 * @create 2022-12-10 11:18
 */
//@Component
//@Aspect //切面
public class AlphaAspect {
    //切入点
    @Pointcut("execution(* com.nowcoder.community.service.*.*(..))")
    public void pointcut(){

    }
    //前置通知
    @Before("pointcut()")
    public void before(){
        System.out.println("Before");
    }
    //后置通知
    @After("pointcut()")
    public void after(){
        System.out.println("After");
    }
    //返回值之后通知
    @AfterReturning("pointcut()")
    public void afterReturning(){
        System.out.println("AfterReturning");
    }
    //异常通知
    @AfterThrowing("pointcut()")
    public void afterThrowing(){
        System.out.println("AfterThrowing");
    }
    //环绕通知
    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("aroundBefore");
        Object proceed = joinPoint.proceed();
        System.out.println("aroundAfter");
        return proceed;
    }
}
