package com.nowcoder.community.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * @author Oliver
 * @create 2022-11-26 16:48
 */
@Component
@Slf4j
public class MailClient {
    @Autowired
    //注入组件
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    //定义发送端用户
    private String from;

    /**
     * 定义发送方法
     */
    public void sendMail(String to,String subject,String content){
        try {
            MimeMessage message = mailSender.createMimeMessage();
            //true表示创建multipart message
            MimeMessageHelper helper = new MimeMessageHelper(message,true);
            helper.setFrom(from);
            helper.setTo(to);
            //设置邮件主题
            helper.setSubject(subject);
            helper.setText(content,true);
            mailSender.send(helper.getMimeMessage());
        } catch (MessagingException e) {
            log.info("发送邮件失败:{}",e.getMessage());
        }
    }
}
