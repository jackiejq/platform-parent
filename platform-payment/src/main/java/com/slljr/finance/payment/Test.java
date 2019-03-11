package com.slljr.finance.payment;

import com.ielpm.mer.sdk.secret.CertUtil;

import com.slljr.finance.common.utils.Base64Util;
import com.slljr.finance.payment.config.Config;

import com.slljr.finance.payment.utils.util.ParamUtil;
import com.slljr.finance.payment.utils.util.ResponseUtil;
import com.slljr.finance.payment.utils.util.http.Httpz;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

public class Test {

    private static final Logger logger = LogManager.getLogger(Test.class);
    private static final String REGEX_NUMBER = "^[-\\+]?[\\d]*$";


    public static void main(String[] args) throws Exception {
        Test test = new Test();
        //测试注册userId=dc0fa85eb8e0fc3842291f24cd98d64d
        test.Dhregister("S20181212036033", "v1", "1123321123", "321123", "ZR01", "341126197709218311");
        //测试绑卡
        test.dhBind("S20181212036033","v1","1123321123",
                "dc0fa85eb8e0fc3842291f24cd98d64d","6221558812340000", "0000",
                "互联网","ZR01","341126197709218366","13552535506",
                Config.getInstance().getNotifyUrl(),Config.getInstance().getReturnUrl());
        //测试解绑
        test.dhUnBind("S20181212036033","v1","1123321123",
                "dc0fa85eb8e0fc3842291f24cd98d64d","BK9030813167317444984832","6221558812340000");
        //测试扣款
        test.dhDeductPay("S20181212036033","v1","321312312",
                "e677f85fee57649f810315145cd5ab95","6221558812340000", "12312",
                "互联网","ZR01","341126197709218366", "{key1=value1&key2=value2&key3=value3}",
                "url","retrunurl","S20181212036033","v1","321312312",
                "e677f85fee57649f810315145cd5ab95","6221558812340000", "12312",
                "互联网","ZR01");
        //测试还款
        test.dhRepay("S20181212036033","v1","321312312",
                "e677f85fee57649f810315145cd5ab95","622.01", "123.01",
                "e677f85fee57649f810315145cd5ab95");
        //测试扣款查询
        test.dhDeductQuery("S20181212036033","v1","321312312"
                );
        //测试还款查询
        test.dhRepayQuery("S20181212036033","v1","321312312"
        );
        //测试账户余额查询
        test.dhQueryBalance("S20181212036033","v1","e677f85fee57649f810315145cd5ab95"
        );
        //测试异步通知
        test.dhNotify("S20181212036033","v1","e677f85fee57649f810315145cd5ab95",
                "S20181212036033","v1","e677f85fee57649f810315145cd5ab95",
                "S20181212036033","v1");
    }

/**
 * @Author jiangqi
 * @Description //TODO 
 * @Date 16:38 2019/3/11
 * @Param [merchantNo, version, tranSerialNum, userName, certificateType, certificateNum]
 * @return java.lang.String
 **/
    public String Dhregister(String merchantNo, String version, String tranSerialNum, String userName, String certificateType, String certificateNum) throws Exception {
        String TAG = "【注册】-";
        Map<String, String> resultMap = null;
        // 利用treeMap对参数按key值进行排序
        Map<String, String> transMap = new TreeMap<>();
        // 组织交易报文
        try {
            transMap.put("merchantNo", merchantNo);
            transMap.put("tranSerialNum", tranSerialNum);
            transMap.put("version", version);
            // 敏感信息加密
            transMap.put("userName", CertUtil.getInstance().encrypt(userName));
            // 敏感信息加密
            transMap.put("certificateNum", CertUtil.getInstance().encrypt(certificateNum));
            transMap.put("certificateType", certificateType);
            // 签名
            String signMsg = ParamUtil.getSignMsg(transMap);
            logger.info(TAG + "[签名串]signMsg={}", signMsg);
            String sign = CertUtil.getInstance().sign(signMsg);
            System.out.println(sign);
            // 将签名放入交易map中
            transMap.put("sign", sign);
            // 发送扫码请求报文
            logger.info(TAG + "[请求报文]transMap={}", transMap);
            String asynMsg = new Httpz().post(Config.getInstance().getRegisterUrl(), transMap);
            logger.info(TAG + "[返回报文]asynMsg={}", asynMsg);


            //解析返回
            ResponseUtil.parseResponse(asynMsg);
            return asynMsg;
        } catch (Exception e) {
            logger.error("注册异常，", e);
        }
        return null;
    }

    public String dhBind(String merchantNo, String version, String tranSerialNum,
                         String userId, String cardNum, String bankAgentId, String userName,
                         String certificateType, String certificateNum, String mobile,
                         String notifyUrl, String returnUrl) {
        String TAG = "【绑卡】-";

        // 利用treeMap对参数按key值进行排序
        Map<String, String> transMap = new TreeMap<>();
        // 组织交易报文
        try { transMap.put("merchantNo",merchantNo);
            transMap.put("version",version);
            transMap.put("tranSerialNum",tranSerialNum);
            transMap.put("userId",userId);
            // 敏感信息加密
            transMap.put("cardNum",CertUtil.getInstance().encrypt(cardNum));
            transMap.put("bankAgentId",bankAgentId);
            // 敏感信息加密
            transMap.put("userName",CertUtil.getInstance().encrypt(userName));
            transMap.put("certificateType",certificateType);
            // 敏感信息加密
            transMap.put("certificateNum",CertUtil.getInstance().encrypt(certificateNum));
            // 敏感信息加密
            transMap.put("mobile",CertUtil.getInstance().encrypt(mobile));

            transMap.put("notifyUrl",notifyUrl);
            transMap.put("returnUrl",returnUrl);
            String signMsg = ParamUtil.getSignMsg(transMap);
            logger.info(TAG+"[签名串]signMsg={}", signMsg);
            String sign = CertUtil.getInstance().sign(signMsg);
            transMap.put("sign",sign);
            // 发送扫码请求报文
            logger.info(TAG + "[请求报文]transMap={}", transMap);
            String asynMsg = new Httpz().post(Config.getInstance().getDhBindUrl(), transMap);
            logger.info(TAG + "[返回报文]asynMsg={}", asynMsg);

            //解析返回
            ResponseUtil.parseResponse(asynMsg);
            return asynMsg;

        } catch (Exception e) {
            logger.error("绑卡异常，", e);
        }
        return null;
    }

    public String dhUnBind(String merchantNo, String version, String tranSerialNum,
                           String userId, String bindId, String cardNum) {
        String TAG = "【解绑】-";
        // 利用treeMap对参数按key值进行排序
        Map<String, String> transMap = new TreeMap<>();
        // 组织交易报文
        try {
            transMap.put("merchantNo", merchantNo);
            transMap.put("version", version);
            transMap.put("tranSerialNum", tranSerialNum);
            transMap.put("userId", userId);
            transMap.put("bindId", bindId);
            transMap.put("cardNum", CertUtil.getInstance().encrypt(cardNum));
            String signMsg = ParamUtil.getSignMsg(transMap);
            logger.info(TAG + "[签名串]signMsg={}", signMsg);
            String sign = CertUtil.getInstance().sign(signMsg);
            transMap.put("sign", sign);
            // 发送扫码请求报文
            logger.info(TAG + "[请求报文]transMap={}", transMap);
            String asynMsg = new Httpz().post(Config.getInstance().getDhUnbindUrl(), transMap);
            logger.info(TAG + "[返回报文]asynMsg={}", asynMsg);

            //解析返回
            ResponseUtil.parseResponse(asynMsg);
            return asynMsg;

        } catch (Exception e) {
            logger.error("解绑异常，", e);
        }
        return null;
    }
/**
 * @Author jiang
 * @Description //TODO 
 * @Date 16:42 2019/3/11
 * @Param [merchantNo, version, tranSerialNum, bindId, amount, fee, notifyUrl, userId, province, riskInfo, deviceID, deviceType, sourceIP, accountIdHash, mobile, deviceLocation, fullDeviceNumber, deviceSimNumber, usrRgstrDt, deviceName]
 * @return java.lang.String
 **/
    public String dhDeductPay(String merchantNo, String version, String tranSerialNum,
                              String bindId, String amount, String fee, String notifyUrl,
                              String userId, String province, String riskInfo, String deviceID,
                              String deviceType, String sourceIP, String accountIdHash, String mobile,
                              String deviceLocation, String fullDeviceNumber, String deviceSimNumber,
                              String usrRgstrDt, String deviceName) {
        String TAG = "【代还扣款交易】-";
        // 利用treeMap对参数按key值进行排序
        Map<String, String> transMap = new TreeMap<>();
        // 组织交易报文
        try {
        //金额参数判断
            if (!isInteger(amount)) {
                logger.error("金额格式不正确，单位是分(内部参数校验失败)。");
                throw new Exception("金额格式不正确。");
            }
            transMap.put("merchantNo", merchantNo);
            transMap.put("version", version);
            transMap.put("tranSerialNum", tranSerialNum);
            transMap.put("bindId", bindId);
            transMap.put("amount", amount);
            transMap.put("fee", fee);
            transMap.put("notifyUrl", notifyUrl);
            transMap.put("userId", userId);
            transMap.put("province", province);
            transMap.put("riskInfo", riskInfo);
            transMap.put("deviceID", deviceID);
            transMap.put("deviceType", deviceType);
            transMap.put("sourceIP", sourceIP);
            transMap.put("accountIdHash", accountIdHash);
            transMap.put("mobile", mobile);
            transMap.put("deviceLocation", deviceLocation);
            transMap.put("fullDeviceNumber", fullDeviceNumber);
            transMap.put("deviceSimNumber", deviceSimNumber);
            transMap.put("usrRgstrDt", usrRgstrDt);
            transMap.put("deviceName", deviceName);
            String signMsg = ParamUtil.getSignMsg(transMap);
            logger.info(TAG + "[签名串]signMsg={}", signMsg);
            String sign = CertUtil.getInstance().sign(signMsg);
            transMap.put("sign", sign);
            // 发送扫码请求报文
            logger.info(TAG + "[请求报文]transMap={}", transMap);
            String asynMsg = new Httpz().post(Config.getInstance().getDhDeductUrl(), transMap);
            logger.info(TAG + "[返回报文]asynMsg={}", asynMsg);
            //解析返回
            ResponseUtil.parseResponse(asynMsg);
            return asynMsg;


        } catch (Exception e) {
            logger.error("扣款异常，", e);
        }

        return null;
    }
    public String dhRepay(String merchantNo, String version, String tranSerialNum, String bindId, String amount, String fee, String userId) {
        String TAG="【代还还款交易】-";

        try {
            // 组织交易报文
            Map<String,String> transMap=new TreeMap<>();
            transMap.put("merchantNo",merchantNo);
            transMap.put("version",version);
            transMap.put("tranSerialNum",tranSerialNum);
            transMap.put("bindId",bindId);
            transMap.put("amount",amount);
            transMap.put("fee",fee);
            transMap.put("userId",userId);
            String signMsg = ParamUtil.getSignMsg(transMap);
            logger.info(TAG + "[签名串]signMsg={}", signMsg);
            String sign = CertUtil.getInstance().sign(signMsg);
            transMap.put("sign",sign);
            logger.info(TAG+"[请求报文]transMap={}",transMap);
            String asynMsg = new Httpz().post(Config.getInstance().getDhRepayUrl(), transMap);
            logger.info(TAG+"[返回报文]asynMsg={}");
            //解析返回
            ResponseUtil.parseResponse(asynMsg);
            return asynMsg;
        } catch (Exception e) {
            logger.error("还款异常,",e);

        }



        return null;
    }


    public String dhDeductQuery(String merchantNo, String version, String tranSerialNum) {
        String TAG="【扣款查询】-";
        Map<String,String> transMap=new TreeMap<>();

        try {
            transMap.put("merchantNo",merchantNo);
            transMap.put("version",version);
            transMap.put("tranSerialNum",tranSerialNum);
            String signMsg = ParamUtil.getSignMsg(transMap);
            logger.info(TAG+"[签名串]signMsg={}",signMsg);
            String sign = CertUtil.getInstance().sign(signMsg);
            transMap.put("sign",sign);
            logger.info(TAG+"[请求报文]transMap={}",transMap);
            String asynMsg = new Httpz().post(Config.getInstance().getQueryDeductUrl(), transMap);
            logger.info(TAG+"[返回报文]asynMsg={}",asynMsg);
            //解析返回
            ResponseUtil.parseResponse(asynMsg);
            return asynMsg;
        } catch (Exception e) {
            logger.error("扣款查询异常",e);
        }

        return null;
    }

    public String dhRepayQuery(String merchantNo, String version, String tranSerialNum) {
        String TAG="【还款查询】-";
        Map<String,String> transMap=new TreeMap<>();

        try {
            transMap.put("merchantNo",merchantNo);
            transMap.put("version",version);
            transMap.put("tranSerialNum",tranSerialNum);
            String signMsg = ParamUtil.getSignMsg(transMap);
            String sign = CertUtil.getInstance().sign(signMsg);
            logger.info(TAG+"[签名串]signMsg={}",signMsg);
            transMap.put("sign",sign);
            logger.info(TAG+"[请求报文]transMap={}",transMap);
            String asynMsg = new Httpz().post(Config.getInstance().getQueryRepayUrl(), transMap);
            logger.info(TAG+"[返回报文]asynMsg={}",asynMsg);
            //解析返回
            ResponseUtil.parseResponse(asynMsg);
            return asynMsg;
        } catch (Exception e) {
            logger.error("还款查询异常",e);
        }
        return null;
    }
    public String dhQueryBalance(String merchantNo, String version, String userId) {
        String TAG="【用户余额查询】-";
        Map<String,String> transMap=new TreeMap<>();

        try {
            transMap.put("merchantNo",merchantNo);
            transMap.put("version",version);
            transMap.put("userId",userId);
            String signMsg = ParamUtil.getSignMsg(transMap);
            String sign = CertUtil.getInstance().sign(signMsg);
            logger.info(TAG+"[签名串]signMsg={}",signMsg);
            transMap.put("sign",sign);
            logger.info(TAG+"[请求报文]transMap={}",transMap);
            String asynMsg = new Httpz().post(Config.getInstance().getQueryBalanceUrl(), transMap);
            logger.info(TAG+"[返回报文]asynMsg={}",asynMsg);
            //解析返回
            ResponseUtil.parseResponse(asynMsg);
            return asynMsg;
        } catch (Exception e) {
            logger.error("用户余额查询异常",e);
        }
        return null;
    }
    public boolean dhNotify(String merchantNo, String version, String bindId,String userId,
                           String amount,String rtnCode,String rtnMsg,String sign){
        String TAG="【异步通知】-";
        Map<String,String> transMap=new TreeMap<>();
        transMap.put("merchantNo",merchantNo);
        transMap.put("version",version);
        transMap.put("bindId",bindId);
        transMap.put("userId",userId);
        transMap.put("amount",amount);
        transMap.put("rtnCode",rtnCode);
        transMap.put("merchantNo",merchantNo);
        transMap.put("rtnMsg",rtnMsg);


        logger.info(TAG+"[-后台通知-]-获取参数transMap=" + transMap);

        // 拼接签名串
        String signMsg = ParamUtil.getSignMsg(transMap);
        // 验签
        boolean verifyResults = false;
        try {
            verifyResults = CertUtil.getInstance().verify(signMsg, sign);
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info(TAG+"[-异步通知-]-验签结果=" + verifyResults);
        return verifyResults;
    }

    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile(REGEX_NUMBER);
        return pattern.matcher(str).matches();
    }
}
