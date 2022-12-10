package com.nowcoder.community;

import com.nowcoder.community.service.AlphaService;
import com.nowcoder.community.service.impl.AlphaServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Oliver
 * @create 2022-12-06 11:44
 */
@SpringBootTest
public class TransactionTests {
    @Autowired
   private AlphaService alphaService;
    @Test
    public void save1(){
        Object o = alphaService.save1();
        System.out.println(o);
    }
    @Test
    public void save2(){
        Object o = alphaService.save2();
        System.out.println(o);
    }
}
