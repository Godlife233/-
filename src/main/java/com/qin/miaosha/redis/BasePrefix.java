package com.qin.miaosha.redis;

import org.springframework.stereotype.Service;


public abstract class  BasePrefix implements KeyPrefix {

    private  int expireSeconds;
    private  String prefix;

    public BasePrefix(String prefix){
        this(0,prefix);
    }

    public BasePrefix(int expireSeconds,String prefix){
        this.expireSeconds =expireSeconds;
        this.prefix = prefix;
    }
    @Override
    public int getexpireSeconds() {//默认0代表永不过期
        return expireSeconds;
    }

    @Override
    public String getPrefix() {
        String str = getClass().getSimpleName();
        return str+":"+prefix;
    }
}
