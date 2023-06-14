package com.maynormoe.test;

import com.maynormoe.takeout.TakeoutApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.*;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TakeoutApplication.class)
public class SpringDataRedisTest {

    @Resource
    private RedisTemplate<String, String> redisTemplate;


    @Test
    public void testString() {
        redisTemplate.opsForValue().set("city", "beijing");
        String city = redisTemplate.opsForValue().get("city");
        System.out.println(city);

        redisTemplate.opsForValue().set("key1", "value1", 10l, TimeUnit.SECONDS);
    }

    @Test
    public void testHash() {
        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
        hashOperations.put("002", "name", "xiaoming");
        hashOperations.put("002", "age", "2");

        String name = (String) hashOperations.get("002", "name");
        System.out.println(name);

        Set<Object> keys = hashOperations.keys("002");
        for (Object key : keys) {
            System.out.println(key);
        }

        List<Object> values = hashOperations.values("002");
        for (Object value : values) {
            System.out.println(value);
        }
        hashOperations.delete("002","name","age");
    }

    @Test
    public void testList() {
        ListOperations<String, String> listOperations = redisTemplate.opsForList();

        listOperations.leftPush("mylist", "a");
        listOperations.leftPushAll("mylist", "b", "c");

        List<String> mylist = listOperations.range("mylist", 0, -1);
        if (mylist != null) {
            for (String value : mylist) {
                System.out.println(value);
            }
        }

        Long size = listOperations.size("mylist");
        int lsize = 0;
        if (size != null) {
            lsize = size.intValue();
        }
        for (int i = 0; i < lsize; i++) {
            String el = listOperations.rightPop("mylist");
            System.out.println(el);
        }
        listOperations.remove("mylist",3, "a");
    }

    @Test
    public void testZset() {
        ZSetOperations<String, String> zSetOperations = redisTemplate.opsForZSet();

        zSetOperations.add("myZset", "a", 10.0);
        zSetOperations.add("myZset", "b", 9.0);
        zSetOperations.add("myZset", "c", 8.0);

        Set<String> myZset = zSetOperations.range("myZset", 0, -1);
        System.out.println(myZset);

        zSetOperations.incrementScore("myZset", "b", 1000.0);
        myZset = zSetOperations.range("myZset", 0, -1);
        System.out.println(myZset);

        zSetOperations.remove("myZset", "a", "b", "c");
    }
}
