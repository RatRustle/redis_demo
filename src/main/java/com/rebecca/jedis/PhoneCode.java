package com.rebecca.jedis;

import redis.clients.jedis.Jedis;

import java.util.Random;

/**
 * 手机验证码功能
 */
public class PhoneCode {
    public static void main(String[] args) {
        //模拟验证码发送
        verifyCode("13812345678");
        //校验
        //getRedisCode("13812345678", "662995");
    }

    //1.生成6位数字验证码
    public static String getCode(){
        Random random = new Random();
        String code = "";
        for (int i=0;i<6;i++){
            int rand = random.nextInt(10);
            code += rand;
        }
        return code;
    }

    //2.让每个手机每天只能发送三次，验证码放到redis中，设置过期时间
    public static void verifyCode(String phone){
        //创建Jedis对象
        Jedis jedis = new Jedis("192.168.182.100",6379);
        //输入密码
        jedis.auth("123456");

        //拼接key
        //手机发送次数key
        String countKey = "VerifyCode"+phone+":count";
        //验证码key
        String codeKey = "VerifyCode"+phone+":code";

        //每个手机每天只能发送3次
        String count = jedis.get(countKey);
        if (count==null){
            //没有发送次数，第一次发送
            //设置发送次数为1
            jedis.setex(countKey,24*60*60, "1");
        }
        else if(Integer.parseInt(count)<=2){
            //发送次数+1
            jedis.incr(countKey);
        }
        else {
            //发送三次，不能再发送
            System.out.println("今天发送次数已经超过三次");
            //关闭jedis
            jedis.close();
            return;
        }

        //发送的验证码放到redis中
        String vccode = getCode();
        jedis.setex(codeKey, 120, vccode);
        jedis.close();
    }

    //3.验证码校验
    public static void getRedisCode(String phone,String code){
        //创建Jedis对象
        Jedis jedis = new Jedis("192.168.182.100",6379);
        //输入密码
        jedis.auth("123456");
        //验证码key
        String codeKey = "VerifyCode"+phone+":code";
        //从redis中获取验证码
        String redisCode = jedis.get(codeKey);
        System.out.println(redisCode);
        //判断
        if (redisCode==null){
            System.out.println("验证码失效");
        }
        else {
            if (redisCode.equals(code)){
                System.out.println("验证码一致,成功");
            }
            else {
                System.out.println("验证失败");
            }
        }

        jedis.close();
    }
}
