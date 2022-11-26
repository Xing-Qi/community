package com.nowcoder.community.dao.impl;

import com.nowcoder.community.dao.AlphaDao;
import org.springframework.stereotype.Repository;

/**
 * @author Oliver
 * @create 2022-11-20 16:35
 */
@Repository
public class AlphaDaoImpl implements AlphaDao {
    @Override
    public String select() {
        return "mybatis";
    }
}
