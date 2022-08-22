package com.rebecca.jedis;

import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Set;


public class JedisDemo1 {
    public static void main(String[] args) {
        //创建Jedis对象
        Jedis jedis = new Jedis("192.168.182.100",6379);
        //输入密码
        jedis.auth("123456");
        //测试
        String value = jedis.ping();
        System.out.println(value);
    }

    //操作key
    @Test
    public void demo1(){
        //创建Jedis对象
        Jedis jedis = new Jedis("192.168.182.100",6379);
        //输入密码
        jedis.auth("123456");

        //添加
        jedis.set("name", "rebecca");
        //获取
        String name = jedis.get("name");
        System.out.println("name:"+name);

        //添加多个key和value
        jedis.mset("r1","v1","r2","v2");
        //批量获取
        List<String> mget = jedis.mget("r1", "r2");
        for (String s : mget) {
            System.out.println("mget:"+s);
        }

        //获取所有key
        Set<String> keys = jedis.keys("*");
        for (String key : keys) {
            System.out.println("key:"+key);
        }
    }
}
