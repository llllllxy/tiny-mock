package org.tinycloud.tinymock.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * <p>
 * </p>
 *
 * @author liuxingyu01
 * @since 2024-03-25 10:50
 */
@Slf4j
public class DigestUtils {

    public static String md5Hex(byte[] input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(input);
            return byte2Hex(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String sha1Hex(byte[] input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] digest = md.digest(input);
            return byte2Hex(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 将byte转为16进制
     *
     * @param bytes 字节数组
     * @return 16进制字符串
     */
    private static String byte2Hex(byte[] bytes) {
        StringBuilder sBuilder = new StringBuilder();
        String temp = null;
        for (byte aByte : bytes) {
            temp = Integer.toHexString(aByte & 0xFF);
            if (temp.length() == 1) {
                // 1得到一位的进行补0操作
                sBuilder.append("0");
            }
            sBuilder.append(temp);
        }
        return sBuilder.toString();
    }
}
