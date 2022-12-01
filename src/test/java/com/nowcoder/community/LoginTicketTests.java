package com.nowcoder.community;

import com.nowcoder.community.entity.LoginTicket;
import com.nowcoder.community.mapper.LoginTicketMapper;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

/**
 * @author Oliver
 * @create 2022-11-28 19:59
 */
@SpringBootTest
@Slf4j
public class LoginTicketTests {
    @Autowired
    private LoginTicketMapper loginTicketMapper;
    @Test
    public void testInsert(){
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(1001);
        loginTicket.setStatus(0);
        loginTicket.setTicket("ajfkllafs");
        loginTicket.setExpired(new Date());
        int insert = loginTicketMapper.insertLoginTicket(loginTicket);
        log.info("{}",insert);
    }
    @Test
    public void testSelectLoginTicketByTicket(){
        LoginTicket loginTicket = loginTicketMapper.selectLoginTicketByTicket("ajfkllafs");
        log.info("loginTicket:{}",loginTicket);
    }
    @Test
    public void testUpdateStatus(){
        loginTicketMapper.updateStatus("ajfkllafs",1);
    }
}
