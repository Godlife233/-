package com.qin.miaosha.Utils;


import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisUtil {


    private static volatile   JedisPool  jedisPool =null;

    private RedisUtil(){}

    public static JedisPool getJedisPool(){
        if (jedisPool==null){
            synchronized(RedisUtil.class){
                if(jedisPool==null){
                    JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
                    jedisPoolConfig.setMaxWaitMillis(3*1000);
                    jedisPoolConfig.setMaxTotal(10);
                    jedisPoolConfig.setMaxIdle(10);

                    jedisPool = new JedisPool(jedisPoolConfig,"127.0.0.1",6379,3*1000);
                }
            }
        }
        return jedisPool;
    }

    public static void returnToPool(Jedis jedis){
        if(jedis!=null){
            jedis.close();
        }

    }
}
