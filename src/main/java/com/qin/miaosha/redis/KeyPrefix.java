package com.qin.miaosha.redis;

public interface KeyPrefix {
    public int getexpireSeconds();
    public String getPrefix();
}
