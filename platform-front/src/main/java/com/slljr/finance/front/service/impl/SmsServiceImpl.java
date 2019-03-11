package com.slljr.finance.front.service.impl;

import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.slljr.finance.common.constants.Constant;
import com.slljr.finance.common.exception.InterfaceException;
import com.slljr.finance.common.redis.RedisUtil;
import com.slljr.finance.common.utils.AliSmsUtil;
import com.slljr.finance.common.utils.JsonUtil;
import com.slljr.finance.common.utils.MsgEnum;
import com.slljr.finance.front.service.SmsService;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Random;

@Service
@PropertySource(value="classpath:application.properties",encoding = "UTF-8")
public class SmsServiceImpl implements SmsService {

	private static final Logger log = LogManager.getLogger();

	@Value("${ali.sms.accessKeyId}")
	private String accessKeyId;
	@Value("${ali.sms.accessKeySecret}")
	private String accessKeySecret;

	@Value("${ali.sms.signName}")
	private String signName;

	@Value("${ali.sms.template}")
	private String template;

	@Value("${sms.send.switch}")
	private boolean SmsSendSwitch;

	/**
	 * 短信发送成功响应状态
	 */
	private static final String SUCCESSCODE = "OK";

	@Autowired
	RedisUtil redisUtil;

	@Override
	public String sendMessage(String phone, int type) throws InterfaceException{
		//生成验证码
		String verifyCode = String.valueOf(new Random().nextInt(899999) + 100000);
		//发送
		boolean sendStatus = false;
		if (SmsSendSwitch){
			sendStatus = sendAliSmsCode(template, phone, verifyCode);
		}else {
			log.info("用户{},短信验证码{}", phone, verifyCode);
		}
		if (!SmsSendSwitch || sendStatus) {
			//先移除存在的key、存入验证码
			switch (type) {
				case 1:
					redisUtil.del(Constant.VERIFICATION_CODE_KEY + Constant.LOGIN_SMS_KEY + phone);
					redisUtil.set(Constant.VERIFICATION_CODE_KEY + Constant.LOGIN_SMS_KEY + phone, verifyCode, 2*60);
					break;
				case 2:
					redisUtil.del(Constant.VERIFICATION_CODE_KEY + Constant.UPDATE_BANK_SMS_KEY + phone);
					//设置五分钟有效
					long expiredTime = 5 * 60;
					redisUtil.set(Constant.VERIFICATION_CODE_KEY + Constant.UPDATE_BANK_SMS_KEY + phone, verifyCode, expiredTime);
					break;
				default:
					break;
			}

			return verifyCode;
		}
		return null;
	}

	@Override
	public boolean checkIsCorrectCode(String phone, String smscode, int type) {
		Object code = null;
		switch (type) {
			case 1:
				code = redisUtil.get(Constant.VERIFICATION_CODE_KEY + Constant.LOGIN_SMS_KEY + phone);
				break;
			case 2:
				code = redisUtil.get(Constant.VERIFICATION_CODE_KEY + Constant.UPDATE_BANK_SMS_KEY + phone);

				break;
			default:
				break;

		}
		//验证短信验证码是否失效
		if (null != code) {
			if (StringUtils.isNotEmpty(String.valueOf(code)) && smscode.equals(String.valueOf(code))) {
				return true;
			}
		}

		return false;
	}

	@Override
	public boolean sendAliSmsCode(String templateCode, String targetPhoneNum, String code) throws InterfaceException {
		try {
			log.info(String.format("发送短信入参→模板编号：%s;手机号码:%s;验证码:%s", templateCode, targetPhoneNum, code));
			SendSmsResponse sendSmsResponse = AliSmsUtil.sendSms(accessKeyId, accessKeySecret, signName, templateCode, targetPhoneNum, "", code);
			log.info(String.format("发送短信响应：%s", null == sendSmsResponse ? "" : JsonUtil.getJson(sendSmsResponse)));
			if (null != sendSmsResponse) {
				if (SUCCESSCODE.equals(sendSmsResponse.getCode())) {
					return true;
				} else {
					log.error("短信验证码发送失败：", sendSmsResponse.getMessage());
					throw new InterfaceException(MsgEnum.VERIFICATIONCODFAIL);
				}
			} else {
				throw new InterfaceException(MsgEnum.VERIFICATIONCODFAIL);
			}
		} catch (ClientException e) {
			log.error("短信验证码发送失败：", e);
			throw new InterfaceException(MsgEnum.VERIFICATIONCODFAIL);
		}
	}

	@Override
	public QuerySendDetailsResponse selectSmsSendResult(String bizId, String targetPhoneNum, Date sendDate) throws InterfaceException {
		try {
			return AliSmsUtil.querySendDetails(accessKeyId, accessKeySecret, bizId, targetPhoneNum, sendDate);
		} catch (ClientException e) {
			log.error("查询失败：", e);
			throw new InterfaceException(MsgEnum.GET_SMS_DETAIL_FAIL.getMsg());
		}
	}

}
