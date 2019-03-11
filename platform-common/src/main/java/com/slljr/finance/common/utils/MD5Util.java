package com.slljr.finance.common.utils;

import java.security.MessageDigest;

/**
 * @description: MD5加密工具类
 * @author: uncle.quentin.
 * @date: 2018/12/18.
 * @time: 19:31.
 */
public class MD5Util {

    /**
     * MD5加密
     *
     * @param data 需加密数据
     * @return byte[]
     * @author uncle.quentin
     * @date 2018/12/18 19:32
     * @version 1.0
     */
    public static byte[] encodeMD5(byte[] data) throws Exception {
        // 初始化MessageDigest
        MessageDigest md = MessageDigest.getInstance("MD5");
        // 执行消息摘要
        return md.digest(data);
    }

}
