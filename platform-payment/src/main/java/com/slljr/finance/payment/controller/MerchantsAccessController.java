package com.slljr.finance.payment.controller;

import com.ielpm.mer.sdk.secret.CertUtil;
import com.slljr.finance.common.utils.MD5Util;
import com.slljr.finance.payment.service.MerchantsAccessService;
import com.slljr.finance.payment.utils.HttpUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
@RestController
public class MerchantsAccessController {
    @Autowired
    MerchantsAccessService merchantsAccessService;

    //注册
    @RequestMapping(value = "/dhRegister")
    public String dhRegister(String merchantNo, String version, String tranSerialNum, String userName,
                             String certificateType, String certificateNum) {

        String result =  merchantsAccessService.dhRegister(merchantNo,version,tranSerialNum,userName,certificateType,certificateNum);

        return result;
    }

    //绑卡
    @RequestMapping(value = "/dhBind")
    public String dhBind(String merchantNo, String version, String tranSerialNum, String userId,
                         String cardNum, String bankAgentId, String userName, String certificateType,
                         String certificateNum, String mobile, String notifyUrl, String returnUrl) {

        String result = merchantsAccessService.dhBind(merchantNo, version, tranSerialNum, userId, cardNum, bankAgentId,
                userName, certificateType, certificateNum, mobile, notifyUrl, returnUrl);

        return result;
    }

    //解绑
    @RequestMapping(value = "/dhUnBind")
    public String dhUnBind(String merchantNo, String version, String tranSerialNum, String userId,
                           String bindId, String cardNum) {

        String result = merchantsAccessService.dhUnBind(merchantNo, version, tranSerialNum, userId, bindId, cardNum);

        return result;
    }

    //扣款
    @RequestMapping(value = "/dhDeductPay")
    public String dhDeductPay(String merchantNo, String version, String tranSerialNum, String bindId,
                              String amount, String fee, String notifyUrl, String userId,
                              String province, String riskInfo, String deviceID,
                              String deviceType, String sourceIP, String accountIdHash,
                              String mobile, String deviceLocation, String fullDeviceNumber,
                              String deviceSimNumber, String usrRgstrDt, String deviceName) {

        String result = merchantsAccessService.dhDeductPay(merchantNo, version, tranSerialNum, bindId, amount, fee,
                notifyUrl, userId, province, riskInfo, deviceID, deviceType, sourceIP, accountIdHash, mobile,
                deviceLocation, fullDeviceNumber, deviceSimNumber, usrRgstrDt, deviceName);

        return result;
    }

    //还款
    @RequestMapping(value = "/dhRepay")
    public String dhRepay(String merchantNo, String version, String tranSerialNum, String bindId, String amount, String fee, String userId) {
        String result = merchantsAccessService.dhRepay(merchantNo, version, tranSerialNum, bindId, amount, fee, userId);
        return result;
    }

    //扣款查询
    @RequestMapping(value = "/dhDeductQuery")
    public String dhDeductQuery(String merchantNo, String version, String tranSerialNum) {
        String result = merchantsAccessService.dhDeductQuery(merchantNo, version, tranSerialNum);
        return result;
    }

    //还款查询
    @RequestMapping(value = "/dhRepayQuery")
    public String dhRepayQuery(String merchantNo, String version, String tranSerialNum) {
        String result = merchantsAccessService.dhRepayQuery(merchantNo, version, tranSerialNum);
        return result;
    }

    //用户余额查询
    @RequestMapping(value = "/dhQueryBalance")
    public String dhQueryBalance(String merchantNo, String version, String userId) {
        String result = merchantsAccessService.dhQueryBalance(merchantNo, version, userId);
        return result;
    }
}
