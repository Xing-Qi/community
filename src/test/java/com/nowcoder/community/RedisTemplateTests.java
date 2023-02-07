package com.nowcoder.community;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.*;

/**
 * @author Oliver
 * @create 2022-12-11 14:08
 */
@SpringBootTest
public class RedisTemplateTests {
    @Autowired
    private RedisTemplate redisTemplate;
    @Test
    @DisplayName("bitmapunion")
    public void testBitmapUnion(){
        String redisKey2 = "test:bm:02";
        redisTemplate.opsForValue().setBit(redisKey2, 0, true);
        redisTemplate.opsForValue().setBit(redisKey2, 1, true);
        redisTemplate.opsForValue().setBit(redisKey2, 2, true);
        String redisKey3 = "test:bm:03";
        redisTemplate.opsForValue().setBit(redisKey3, 2, true);
        redisTemplate.opsForValue().setBit(redisKey3, 3, true);
        redisTemplate.opsForValue().setBit(redisKey3, 4, true);
        String redisKey4 = "test:bm:04";
        redisTemplate.opsForValue().setBit(redisKey4, 4, true);
        redisTemplate.opsForValue().setBit(redisKey4, 5, true);
        redisTemplate.opsForValue().setBit(redisKey4, 6, true);
        String redisKey = "test:bm:or";
        Object obj = redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                redisConnection.bitOp(
                        RedisStringCommands.BitOperation.OR,
                        redisKey.getBytes(),
                        redisKey2.getBytes(),redisKey3.getBytes(),redisKey4.getBytes()
                );
                return redisConnection.bitCount(redisKey.getBytes());
            }
        });
        System.out.println(obj);
    }

    @Test
    @DisplayName("bitmap")
    public void testBitmap(){
        String redisKey = "test:bm:01";
        //设置
        redisTemplate.opsForValue().setBit(redisKey,1,true);
        redisTemplate.opsForValue().setBit(redisKey,4,true);
        redisTemplate.opsForValue().setBit(redisKey,7,true);
        //查询
        System.out.println(redisTemplate.opsForValue().getBit(redisKey,0));
        System.out.println(redisTemplate.opsForValue().getBit(redisKey,1));
        System.out.println(redisTemplate.opsForValue().getBit(redisKey,2));
        //统计
        Object obj = redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                return redisConnection.bitCount(redisKey.getBytes());
            }
        });
        System.out.println(obj);
    }
    @Test
    @DisplayName("将3组数据合并再统计合并的重复数据的独立总数")
    public void testHyperLogLogUnion(){
        String redisKey2 = "test:hll:02";
        for (int i = 1; i <= 10000; i++) {
            redisTemplate.opsForHyperLogLog().add(redisKey2,i);
        }
        String redisKey3 = "test:hll:03";
        for (int i = 5001; i <= 15000; i++) {
            redisTemplate.opsForHyperLogLog().add(redisKey3,i);
        }
        String redisKey4 = "test:hll:04";
        for (int i = 10001; i <= 20000; i++) {
            redisTemplate.opsForHyperLogLog().add(redisKey4,i);
        }

        String unionkey = "test:hll:union";
        redisTemplate.opsForHyperLogLog().union(unionkey,redisKey2,redisKey3,redisKey4);
        Long size = redisTemplate.opsForHyperLogLog().size(unionkey);
        System.out.println(size);
    }
    @Test
    @DisplayName("统计20万个重复数据的独立总数HyperLogLog")
    public void testHyperLogLog(){
        String redisKey = "test:hll:01";
        for (int i = 0; i < 100000; i++) {
            redisTemplate.opsForHyperLogLog().add(redisKey,i);
        }
        for (int i = 0; i < 100000; i++) {
            int r = (int) (Math.random() * 100000 + 1);
            redisTemplate.opsForHyperLogLog().add(redisKey,r);
        }
        Long size = redisTemplate.opsForHyperLogLog().size(redisKey);
        System.out.println(size);
    }

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
