package com.slljr.finance.common.utils;

import com.alibaba.druid.filter.config.ConfigTools;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * @description: Druid密码加解密工具类
 * @author: uncle.quentin.
 * @date: 2019/1/8.
 * @time: 17:47.
 */
public class DruidPasswordUtil {

    private static final Logger log = LogManager.getLogger();

    public static final String MAP_PUBLIC_KEY = "publicKey";
    public static final String MAP_PRIVATE_KEY = "privateKey";
    public static final String MAP_PASSWORD = "password";

    /**
     * 初始化密钥
     *
     * @param
     * @return java.util.Map<java.lang.String                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               ,                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               java.lang.Object>
     * @author uncle.quentin
     * @date 2019/1/8 17:57
     * @version 1.0
     */
    public static Map<String, Object> initKey() throws Exception {
        // 实例化密钥对儿生成器
        String[] arr = ConfigTools.genKeyPair(512);
        // 封装密钥
        Map<String, Object> keyMap = new HashMap<>(2);
        keyMap.put(MAP_PUBLIC_KEY, arr[1]);
        keyMap.put(MAP_PRIVATE_KEY, arr[0]);
        return keyMap;
    }

    /**
     * 获取私钥
     *
     * @param keyMap
     * @return java.lang.String
     * @author uncle.quentin
     * @date 2019/1/8 17:59
     * @version 1.0
     */
    public static String getPrivateKey(Map<String, Object> keyMap) {
        return (String) keyMap.get(MAP_PRIVATE_KEY);
    }

    /**
     * 获取公钥
     *
     * @param keyMap
     * @return java.lang.String
     * @author uncle.quentin
     * @date 2019/1/8 17:59
     * @version 1.0
     */
    public static String getPublicKey(Map<String, Object> keyMap) {
        return (String) keyMap.get(MAP_PUBLIC_KEY);
    }

    /**
     * 私钥加密
     *
     * @param privateKey 私钥
     * @param password   密码
     * @return java.lang.String
     * @author uncle.quentin
     * @date 2019/1/8 18:50
     * @version 1.0
     */
    public static String encrypt(String privateKey, String password) throws Exception {
        try {
            return ConfigTools.encrypt(privateKey, password);
        } catch (Exception e) {
            log.error("druid encrypt error !", e);
        }

        return null;
    }

    /**
     * 公钥解密
     *
     * @param publicKey       公钥
     * @param encryptPassword 加密后的密码
     * @return java.lang.String
     * @author uncle.quentin
     * @date 2019/1/8 18:50
     * @version 1.0
     */
    public static String decrypt(String publicKey, String encryptPassword) {
        try {
            return ConfigTools.decrypt(publicKey, encryptPassword);
        } catch (Exception e) {
            log.error("druid decrypt error !", e);
        }

        return null;
    }

    public static void main(String[] args) {
        try {
            Map<String, Object> keys = initKey();
            //获取私钥
            String privateKey = getPrivateKey(keys);
            //获取公钥
            String publicKey = getPublicKey(keys);

            System.out.println("==============私钥==========：" + privateKey);
            System.out.println("==============公钥==========：" + publicKey);

            String password = "dev123456";

            {
                //加密结果
                String encryptResult = encrypt(privateKey, password);
                System.out.println("==============私钥加密结果==========：" + encryptResult);
                //解密结果
                String decryptResult = decrypt(publicKey, encryptResult);
                System.out.println("==============公钥解密结果==========：" + decryptResult);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
