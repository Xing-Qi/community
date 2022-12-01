package com.nowcoder.community;

import com.nowcoder.community.util.MailClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/**
 * @author Oliver
 * @create 2022-11-26 17:11
 */
@SpringBootTest
public class MailTests {
    @Autowired
    private MailClient mailClient;
    @Autowired
    //注入thymeleaf模板引擎
    private TemplateEngine templateEngine;

    @Test
    public void testSendSimple(){
        mailClient.sendMail("1244997287@qq.com","Text","welcome");
    }
    @Test
    public void testSendHtml(){
        //thymeleaf Context
        Context context = new Context();
        //向页面中共享数据

        context.setVariable("username","Sender");
        String content = templateEngine.process("/mail/demo", context);
        System.out.println(content);
        mailClient.sendMail("1244997287@qq.com","Html",content);
    }
}
