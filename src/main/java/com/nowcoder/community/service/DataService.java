package com.nowcoder.community.service;

import com.nowcoder.community.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisCommands;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * @author Oliver
 * @create 2023-02-07 9:47
 */
@Service
public class DataService {
    @Autowired
    private RedisTemplate redisTemplate;
    private SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");

    /**
     * 统计UV，将指定ip计入UV
     *
     * @param ip
     */
    public void recordUV(String ip) {
        String redisKey = RedisKeyUtil.getUvKey(df.format(new Date()));
        redisTemplate.opsForHyperLogLog().add(redisKey, ip);
    }

    /**
     * 统计指定日期范围内的UV
     *
     * @param starDate
     * @param endDate
     * @return
     */
    public long calculateUV(Date starDate, Date endDate) {
        if (starDate == null || endDate == null) {
            throw new IllegalArgumentException("参数不能为空!");
        }
        //整理该日期范围内的key
        List<String> keyList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(starDate);
        while (!calendar.getTime().after(endDate)) {
            String key = RedisKeyUtil.getUvKey(df.format(calendar.getTime()));
            keyList.add(key);
            calendar.add(calendar.DATE, 1);
        }
        //合并数据
        String redisKey = RedisKeyUtil.getUvKey(df.format(starDate), df.format(endDate));
        redisTemplate.opsForHyperLogLog().union(redisKey, keyList.toArray());
        return redisTemplate.opsForHyperLogLog().size(redisKey);
    }

    /**
     * 统计DAU，将指定userId计入
     *
     * @param userId
     */
    public void recordDAU(int userId) {
        String redisKey = RedisKeyUtil.getDauKey(df.format(new Date()));
        redisTemplate.opsForValue().setBit(redisKey, userId, true);
    }

    public long calculateDAU(Date startDate, Date endDate) {
        //整理该日期范围内的key
        List<byte[]> keyList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        //循环指定日期数据，每次加1天
        while (!calendar.getTime().after(endDate)) {
            String key = RedisKeyUtil.getDauKey(df.format(calendar.getTime()));
            keyList.add(key.getBytes());
            calendar.add(Calendar.DATE, 1);
        }
        //合并这些数据
        String redisKey = RedisKeyUtil.getDauKey(df.format(startDate), df.format(endDate));
        return (long) redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                redisConnection.bitOp(
                        RedisStringCommands.BitOperation.OR,
                        redisKey.getBytes(), keyList.toArray(new byte[0][0])
                );
                return redisConnection.bitCount(redisKey.getBytes());
            }
        });
    }
}
