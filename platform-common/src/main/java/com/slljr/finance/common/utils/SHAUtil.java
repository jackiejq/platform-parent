package com.slljr.finance.common.utils;

import java.security.MessageDigest;

/**
 * @description: SHA加密组件
 * @author: uncle.quentin.
 * @date: 2018/12/18.
 * @time: 19:19.
 */
public abstract class SHAUtil {

    /**
     * SHA-1加密
     *
     * @param data 待加密数据
     * @return byte[] 消息摘要
     * @throws Exception
     */
    public static byte[] encodeSHA(byte[] data) throws Exception {
        // 初始化MessageDigest
        MessageDigest md = MessageDigest.getInstance("SHA");
        // 执行消息摘要
        return md.digest(data);
    }

    /**
     * SHA-1加密
     *
     * @param data 待加密数据
     * @return byte[] 消息摘要
     * @throws Exception
     */
    public static byte[] encodeSHA1(byte[] data) throws Exception {
        // 初始化MessageDigest
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        // 执行消息摘要
        return md.digest(data);
    }

    /**
     * SHA-256加密
     *
     * @param data 待加密数据
     * @return byte[] 消息摘要
     * @throws Exception
     */
    public static byte[] encodeSHA256(byte[] data) throws Exception {
        // 初始化MessageDigest
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        // 执行消息摘要
        return md.digest(data);
    }

    /**
     * SHA-384加密
     *
     * @param data 待加密数据
     * @return byte[] 消息摘要
     * @throws Exception
     */
    public static byte[] encodeSHA384(byte[] data) throws Exception {
        // 初始化MessageDigest
        MessageDigest md = MessageDigest.getInstance("SHA-384");
        // 执行消息摘要
        return md.digest(data);
    }

    /**
     * SHA-512加密
     *
     * @param data 待加密数据
     * @return byte[] 消息摘要
     * @throws Exception
     */
    public static byte[] encodeSHA512(byte[] data) throws Exception {
        // 初始化MessageDigest
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        // 执行消息摘要
        return md.digest(data);
    }

    /**
     * SHA-224加密
     *
     * @param data 待加密数据
     * @return byte[] 消息摘要
     * @throws Exception
     */
    public static byte[] encodeSHA224(byte[] data) throws Exception {
        // 初始化MessageDigest
        MessageDigest md = MessageDigest.getInstance("SHA-224");
        // 执行消息摘要
        return md.digest(data);
    }
}
