package com.bb.video.common.util;

import cn.hutool.core.codec.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by LiangyinKwai on 2019-07-08.
 */
public class MD5Encrypt {

    public static String md5(String s) {
        byte[] input = s.getBytes();
        String output = null;
        // 声明16进制字母
        char[] hexChar = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f' };
        try {
            // 获得一个MD5摘要算法的对象
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(input);
            /*
             * MD5算法的结果是128位一个整数，在这里javaAPI已经把结果转换成字节数组了
             */
            byte[] tmp = md.digest();// 获得MD5的摘要结果
            char[] str = new char[32];
            byte b = 0;
            for (int i = 0; i < 16; i++) {
                b = tmp[i];
                str[2 * i] = hexChar[b >>> 4 & 0xf];// 取每一个字节的低四位换成16进制字母
                str[2 * i + 1] = hexChar[b & 0xf];// 取每一个字节的高四位换成16进制字母
            }
            output = new String(str);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return output;
    }

    public static String md5_base64(String s) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(s.getBytes());
            return Base64.encode(messageDigest);
        } catch (Exception var3) {
            return null;
        }
    }
}
