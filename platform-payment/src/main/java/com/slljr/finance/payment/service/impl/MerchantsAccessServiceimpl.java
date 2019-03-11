package com.slljr.finance.payment.service.impl;

import com.ielpm.mer.sdk.secret.CertUtil;
import com.slljr.finance.payment.config.Config;
import com.slljr.finance.payment.service.MerchantsAccessService;
import com.slljr.finance.payment.utils.HttpUtils;
import com.slljr.finance.payment.utils.util.ParamUtil;
import com.slljr.finance.payment.utils.util.ResponseUtil;
import com.slljr.finance.payment.utils.util.http.Httpz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

@Service
public class MerchantsAccessServiceimpl implements MerchantsAccessService {
    private static final Logger logger = LoggerFactory.getLogger(MerchantsAccessServiceimpl.class);
    private static final String REGEX_NUMBER = "^[-\\+]?[\\d]*$";

    @Override
    public String dhRegister(String merchantNo, String version, String tranSerialNum, String userName,
                             String certificateType, String certificateNum) {

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

            // 解析返回
            resultMap = ResponseUtil.parseResponse(asynMsg);
            String msg = resultMap.get("rtnMsg").toString();
            //解析返回
            ResponseUtil.parseResponse(asynMsg);
            return asynMsg;
        } catch (Exception e) {
            logger.error("注册异常，", e);
        }
        return null;
    }


    @Override
    public String dhBind(String merchantNo, String version, String tranSerialNum,
                         String userId, String cardNum, String bankAgentId, String userName,
                         String certificateType, String certificateNum, String mobile,
                         String notifyUrl, String returnUrl) {
        String TAG = "【绑卡】-";
        // 利用treeMap对参数按key值进行排序
        Map<String, String> transMap = new TreeMap<>();
        // 组织交易报文
        try {
            transMap.put("merchantNo", merchantNo);
            transMap.put("version", version);
            transMap.put("tranSerialNum", tranSerialNum);
            transMap.put("userId", userId);
            // 敏感信息加密
            transMap.put("cardNum", CertUtil.getInstance().encrypt(cardNum));
            transMap.put("bankAgentId", bankAgentId);
            transMap.put("userName", CertUtil.getInstance().encrypt(userName));
            transMap.put("certificateType", certificateType);
            transMap.put("certificateNum", CertUtil.getInstance().encrypt(certificateNum));
            transMap.put("mobile", CertUtil.getInstance().encrypt(mobile));
            transMap.put("userName", userName);
            transMap.put("notifyUrl", notifyUrl);
            String signMsg = ParamUtil.getSignMsg(transMap);
            logger.info(TAG + "[签名串]signMsg={}", signMsg);
            String sign = CertUtil.getInstance().sign(signMsg);
            transMap.put("sign", sign);
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

    @Override
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
            String asynMsg = new Httpz().post(Config.getInstance().getDhBindUrl(), transMap);
            logger.info(TAG + "[返回报文]asynMsg={}", asynMsg);
            //解析返回
            ResponseUtil.parseResponse(asynMsg);
            return asynMsg;

        } catch (Exception e) {
            logger.error("解绑异常，", e);
        }
        return null;
    }

    @Override
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

    @Override
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

    @Override
    public String dhDeductQuery(String merchantNo, String version, String tranSerialNum) {
        String TAG="【扣款查询】-";
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

    @Override
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

    @Override
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

    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile(REGEX_NUMBER);
        return pattern.matcher(str).matches();
    }
}
