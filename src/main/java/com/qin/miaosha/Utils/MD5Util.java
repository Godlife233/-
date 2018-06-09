package com.qin.miaosha.Utils;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.Date;

public class MD5Util {
    private static  final  String cliSalt="uisi2df3g3";

    public static String md5(String src){
        return DigestUtils.md5Hex(src);
    }

    public static String inputPassToDBPass(String inputPass,String salt){
        String str = inputPassToFormPass(inputPass);
        return formPassToDBPass(str,salt);
    }

    public static String inputPassToFormPass(String inputPass){
        String str = cliSalt+inputPass;
        return md5(str);
    }

    public static String formPassToDBPass(String formPass,String salt){
        String str = salt+formPass;
        return md5(str);
    }

   public static void main(String[] args) {

        System.out.println(System.currentTimeMillis());
    }

}
