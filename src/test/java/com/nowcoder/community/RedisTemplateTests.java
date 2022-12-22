package com.nowcoder.community;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;

/**
 * @author Oliver
 * @create 2022-12-11 14:08
 */
@SpringBootTest
public class RedisTemplateTests {
    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    @DisplayName("redis事务管理")
    public void testTransactional(){
        //执行
        Object object = redisTemplate.execute(new SessionCallback() {
            @Override
            //redis执行队列
            public Object execute(RedisOperations operations) throws DataAccessException {//operations执行语句
                String redisKey = "test:tr";
                //开始组队
                operations.multi();
                operations.opsForSet().add(redisKey,"zhang");
                operations.opsForSet().add(redisKey,"li");
                //未执行取不到值
                System.out.println(operations.opsForSet().members(redisKey));
                //执行
                return operations.exec();
            }
        });
        System.out.println(object);
    }
    @Test
    @DisplayName("多次访问一个值")
    public void testBoundOperations() {
        String redisKey = "test:tx";
        BoundValueOperations operations = redisTemplate.boundValueOps(redisKey);
        operations.increment();
        operations.increment();
        System.out.println(operations.get());
    }

    @Test
    @DisplayName("sortSet有序列表")
    public void sortSet() {
        String redisKey = "test;zset";
        redisTemplate.opsForZSet().add(redisKey, "测试1", 20);
        redisTemplate.opsForZSet().add(redisKey, "测试2", 10);
        //返回元素
        System.out.println(redisTemplate.opsForZSet().range(redisKey, 0, -1));
        //元素个数
        System.out.println(redisTemplate.opsForZSet().size(redisKey));
    }

    @Test
    @DisplayName("set无序列表")
    public void testSet() {
        String redisKey = "test:set";
        redisTemplate.opsForSet().add(redisKey, "测试1", "测试2", "测试3");

        //返回元素个数
        redisTemplate.opsForSet().size(redisKey);
        //随机
        System.out.println(redisTemplate.opsForSet().pop(redisKey));
        //查看所有value
        System.out.println(redisTemplate.opsForSet().members(redisKey));
    }

    @Test
    @DisplayName("Hash")
    public void testHash() {
        String redisKey = "test:hash";
        redisTemplate.opsForHash().put(redisKey, "id", 1);
        redisTemplate.opsForHash().put(redisKey, "name", "张三");
        System.out.println(redisTemplate.opsForHash().keys(redisKey));
        System.out.println(redisTemplate.opsForHash().get(redisKey, "id"));
    }

    @Test
    @DisplayName("List")
    public void testList() {
        String redisKey = "test:list";
        //设置
        redisTemplate.opsForList().leftPush(redisKey, 101);
        redisTemplate.opsForList().leftPush(redisKey, 102);
        redisTemplate.opsForList().leftPush(redisKey, 103);
        //获取list大小
        System.out.println(redisTemplate.opsForList().size(redisKey));
        //获取指定索引的值
        System.out.println(redisTemplate.opsForList().index(redisKey, 0));
        System.out.println(redisTemplate.opsForList().range(redisKey, 0, -1));
        //获取值
        System.out.println(redisTemplate.opsForList().leftPop(redisKey));
        System.out.println(redisTemplate.opsForList().leftPop(redisKey));
    }

    @Test
    @DisplayName("String")
    public void testString() {
        //设置
        redisTemplate.opsForValue().set("test:tx", 1);
        //获取
        System.out.println(redisTemplate.opsForValue().get("test:tx"));
    }

}
