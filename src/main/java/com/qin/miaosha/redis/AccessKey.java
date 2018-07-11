package com.qin.miaosha.redis;

//限流
public class AccessKey extends  BasePrefix {
    private AccessKey(int expireSeconds,String prefix){
        super(expireSeconds,prefix);
    }


    public static  AccessKey withExpire(int expireSecond){
        return new AccessKey(expireSecond,"access");
    }

}
