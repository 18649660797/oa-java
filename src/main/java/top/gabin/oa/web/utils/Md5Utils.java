/**
 * Copyright (c) 2015 云智盛世
 * Created with Md5Utils.
 */
package top.gabin.oa.web.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * md5工具类
 * @author linjiabin  on  15/9/1
 */
public class Md5Utils {
    /**
     * md5编码
     * @param str
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static String encryptMD5(String str) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] bytes = md.digest(str.getBytes());
        StringBuilder sign = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 255);
            if (hex.length() == 1) {
                sign.append("0");
            }
            sign.append(hex.toUpperCase());
        }
        return sign.toString();
    }

}
