package com.qin.miaosha.redis;

public class OrderKey extends BasePrefix {
    public  OrderKey(String prefix){
        super(60,prefix);
    }

    public  static OrderKey getMiaoshaOrderByUidGid = new OrderKey("morder");
}
