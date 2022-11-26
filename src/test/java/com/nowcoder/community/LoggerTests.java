package com.nowcoder.community;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

/**
 * @author Oliver
 * @create 2022-11-26 10:32
 */
@SpringBootTest
@Slf4j
public class LoggerTests {
    @Test
    public void testLogger(){
        log.debug("debug log");
        log.info("info log");
        log.warn("warn log");
        log.error("error log");

    }
}
