package com.slljr.finance.front.service;

import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse;
import com.slljr.finance.common.exception.InterfaceException;

import java.util.Date;

public interface SmsService {

    /**
     * 发送短信息
     *
     * @param phone 手机号码
     * @param type  类型【1：登录 2：修改银行卡信息】
     * @return java.lang.String
     * @author uncle.quentin
     * @date 2018/12/20 18:11
     * @version 1.0
     */
    String sendMessage(String phone, int type) throws InterfaceException;

    /**
     * 校验验证码
     *
     * @param phone   手机号码
     * @param smscode 验证码
     * @param type    类型【1：登录 2：修改银行卡信息】
     * @return java.lang.Integer
     * @author uncle.quentin
     * @date 2018/12/20 18:11
     * @version 1.0
     */
    boolean checkIsCorrectCode(String phone, String smscode, int type);

    /**
     * 阿里发送短信验证码
     *
     * @param templateCode   模板编号
     * @param targetPhoneNum 手机号码
     * @param code           验证码
     * @return boolean
     * @author uncle.quentin
     * @date 2018/12/20 15:14
     * @version 1.0
     */
    boolean sendAliSmsCode(String templateCode, String targetPhoneNum, String code) throws InterfaceException;

    /**
     * 查询短信验证码发送信息
     *
     * @param bizId          短信ID
     * @param targetPhoneNum 手机号码
     * @param sendDate       发送日期（30天内有效）
     * @return com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse
     * @author uncle.quentin
     * @date 2018/12/20 15:21
     * @version 1.0
     */
    QuerySendDetailsResponse selectSmsSendResult(String bizId, String targetPhoneNum, Date sendDate) throws InterfaceException;

}

