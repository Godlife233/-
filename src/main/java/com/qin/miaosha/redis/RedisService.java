package com.qin.miaosha.redis;

import com.alibaba.fastjson.JSON;
import com.qin.miaosha.Utils.RedisUtil;
import com.qin.miaosha.common.ServerResponse;
import com.qin.miaosha.domain.MiaoShaUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;


@Service
public class RedisService {

    @Autowired
    RedisConfig redisConfig;







    public <T>ServerResponse get(KeyPrefix keyPrefix,String key,Class<T> clazz){
        Jedis jedis = null;

        try {
            JedisPool jedisPool = RedisUtil.getJedisPool();
            jedis =jedisPool.getResource();
            String realKey = keyPrefix.getPrefix()+key;
            String str =jedis.get(realKey);
            T t =stringToBean(str,clazz);

            if(t!=null)
            return  ServerResponse.createBySuccess(t);
            else
                return ServerResponse.createByErrorMessage("缓存不存在");
        }
        finally {
            RedisUtil.returnToPool(jedis);
        }

    }

    public ServerResponse<Boolean> exists(KeyPrefix prefix, String key) {
        Jedis jedis = null;
        try {
            jedis =  RedisUtil.getJedisPool().getResource();
            //生成真正的key
            String realKey  = prefix.getPrefix() + key;
            return  ServerResponse.createBySuccess(jedis.exists(realKey));
        }finally {
            RedisUtil.returnToPool(jedis);
        }
    }

    public <T>ServerResponse<Boolean> set(KeyPrefix keyPrefix,String key,T value){
        Jedis jedis = null;
        try {
            Boolean flag = false;
            jedis =RedisUtil.getJedisPool().getResource();
            String valueString = beantoString(value);
            String realKey = keyPrefix.getPrefix()+key;
            String str = null ;
            int seconds = keyPrefix.getexpireSeconds();

            if(valueString==null||valueString.length()<=0){
                return ServerResponse.createBySuccess(flag);
            }
            if(seconds<=0){
                 str =jedis.set(realKey,valueString);
            }else {
                 str =jedis.setex(realKey,keyPrefix.getexpireSeconds(),valueString);
            }

            if(str!=null)
                flag=true;

            return  ServerResponse.createBySuccess(flag);
        }
        finally {
            RedisUtil.returnToPool(jedis);
        }

    }

    public ServerResponse<Long>  incr(KeyPrefix prefix, String key) {
        Jedis jedis = null;
        try {
            jedis =  RedisUtil.getJedisPool().getResource();
            //生成真正的key
            String realKey  = prefix.getPrefix() + key;
            return  ServerResponse.createBySuccess(jedis.incr(realKey));
        }finally {
            RedisUtil.returnToPool(jedis);
        }
    }

    public ServerResponse<Long> decr(KeyPrefix prefix, String key) {
        Jedis jedis = null;
        try {
            jedis =  RedisUtil.getJedisPool().getResource();
            //生成真正的key
            String realKey  = prefix.getPrefix() + key;
            return  ServerResponse.createBySuccess(jedis.decr(realKey));
        }finally {
            RedisUtil.returnToPool(jedis);
        }
    }

    public static  <T> T stringToBean(String str,Class<T> clazz){
        if(str == null || str.length() <= 0 || clazz == null) {
            return null;
        }
        if(clazz == int.class || clazz == Integer.class) {
            return (T)Integer.valueOf(str);
        }else if(clazz == String.class) {
            return (T)str;
        }else if(clazz == long.class || clazz == Long.class) {
            return  (T)Long.valueOf(str);
        }else {
            return JSON.toJavaObject(JSON.parseObject(str), clazz);
        }
    }

    public static  <T> String beantoString(T value){
        if(value==null)
            return null;
        Class<?> clazz = value.getClass();
        if(clazz==int.class||clazz==Integer.class){
            return ""+value;
        }else if(clazz==long.class||clazz==Long.class){
            return ""+value;
        }else if(clazz==String.class){
            return (String) value;
        }
        else
        return JSON.toJSONString(value);
    }


}
