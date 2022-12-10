package com.nowcoder.community.service;

/**
 * @author Oliver
 * @create 2022-11-20 16:36
 */
public interface AlphaService {
    String select();

    /**
     *  通过注解事务
     * @return
     */
    Object save1();

    /**
     * 通过TransactionTemplate 管理事务
     * @return
     */
    Object save2();
}
