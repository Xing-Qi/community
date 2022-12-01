package com.nowcoder.community.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Oliver
 * @create 2022-12-01 15:55
 */
//限定自定义注解能够被应用在哪些Java元素上面的
@Target(ElementType.METHOD)
//注解的生命周期有三个阶段：1、Java源文件阶段(source)2、编译到class文件阶段(class)3、运行期阶段(runtime)
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginRequired {

}
