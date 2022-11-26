package com.nowcoder.community.service.impl;

import com.nowcoder.community.dao.AlphaDao;
import com.nowcoder.community.service.AlphaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Oliver
 * @create 2022-11-20 16:36
 */
@Service
public class AlphaServiceImpl implements AlphaService {
@Autowired
private AlphaDao alphaDao;
    @Override
    public String select() {
        return alphaDao.select();
    }
}
