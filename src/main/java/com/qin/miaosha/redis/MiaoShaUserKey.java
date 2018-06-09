package com.qin.miaosha.redis;

public class MiaoShaUserKey extends BasePrefix {
    public static final int TOKEN_EXPIRE = 3600*24*2;

    private MiaoShaUserKey(int exSeconds ,String str){
        super(exSeconds,str);
    }
    public  static  MiaoShaUserKey token = new MiaoShaUserKey(TOKEN_EXPIRE,"tk");
    public  static  MiaoShaUserKey getById = new MiaoShaUserKey(0,"id");
}
