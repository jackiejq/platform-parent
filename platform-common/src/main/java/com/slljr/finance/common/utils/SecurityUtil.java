package com.slljr.finance.common.utils;


/**
 * @description: 数据加密工具类
 * @author: uncle.quentin.
 * @date: 2018/12/18.
 * @time: 19:19.
 */
public class SecurityUtil {

    /**
     * 默认编码
     */
    public static final String CHARSET = "UTF-8";

    /**
     * 不可实例化
     */
    private SecurityUtil() {

    }

    /**
     * 数据加密
     *
     * @param encryptStr 需加密字符串
     * @return java.lang.String
     * @author uncle.quentin
     * @date 2018/12/18 19:29
     * @version 1.0
     */
    public static String encrypt(String encryptStr) {
        return encryptMd5(SecurityUtil.encryptSHA(encryptStr));
    }

    /**
     * 基于MD5算法的单向加密
     *
     * @param strSrc
     * @return java.lang.String
     * @author uncle.quentin
     * @date 2018/12/18 19:33
     * @version 1.0
     */
    public static final String encryptMd5(String strSrc) {
        String outString;
        try {
            outString = encryptBASE64(MD5Util.encodeMD5(strSrc.getBytes(CHARSET)));
        } catch (Exception e) {
            throw new RuntimeException("加密错误，错误信息：", e);
        }
        return outString;
    }

    /**
     * SHA加密
     *
     * @param data
     * @return java.lang.String
     * @author uncle.quentin
     * @date 2018/12/18 19:28
     * @version 1.0
     */
    public static final String encryptSHA(String data) {
        try {
            return encryptBASE64(SHAUtil.encodeSHA256(data.getBytes(CHARSET)));
        } catch (Exception e) {
            throw new RuntimeException("加密错误，错误信息：", e);
        }
    }

    /**
     * BASE64编码
     *
     * @param key
     * @return java.lang.String
     * @author uncle.quentin
     * @date 2018/12/18 19:29
     * @version 1.0
     */
    public static final String encryptBASE64(byte[] key) {
        try {
            return new BASE64Encoder().encode(key);
        } catch (Exception e) {
            throw new RuntimeException("加密错误，错误信息：", e);
        }
    }

    /**
     * BASE64解码
     *
     * @param key
     * @return
     * @throws Exception
     */
    public static final byte[] decryptBASE64(String key) {
        try {
            return new BASE64Encoder().decode(key);
        } catch (Exception e) {
            throw new RuntimeException("解密错误，错误信息：", e);
        }
    }

}
