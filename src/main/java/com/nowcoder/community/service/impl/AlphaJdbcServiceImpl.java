package com.nowcoder.community.service.impl;

import com.nowcoder.community.service.AlphaService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * @author Oliver
 * @create 2022-11-20 16:45
 */
@Primary //实现类的主类
@Service
public class AlphaJdbcServiceImpl implements AlphaService {
    @Override
    public String select() {
        return "jdbc";
    }
}
