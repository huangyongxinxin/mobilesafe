package com.txs.mobilesafe.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Util {
    public static String encoder(String psd){
        psd = psd +"fkjdslfjds";
        //加盐处理
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            //将字符串 转 byte类型数组，然后随机hash
            byte[] digest = md5.digest(psd.getBytes());
            StringBuffer stringBuffer = new StringBuffer();
            for (byte b : digest) {
                int i = b&0xff;
                String s = Integer.toHexString(i);
                if (s.length()<2){
                    s="0"+s;
                }
                stringBuffer.append(s);
            }
            return stringBuffer.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
