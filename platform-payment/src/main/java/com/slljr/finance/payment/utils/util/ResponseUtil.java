package com.slljr.finance.payment.utils.util;

import com.ielpm.mer.sdk.secret.CertUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class ResponseUtil {
	
	private static final Logger logger = LogManager.getLogger(ResponseUtil.class);
	
	/**
	 * 解析返回数据
	 * @param response
	 * @return
	 */
	public static Map parseResponse(String response){
		//解析返回信息到map中
		Map transMap = ParamUtil.getParamsMap(response, "utf-8");
		//获取签名
		String sign = (String) transMap.get("sign");
		sign = sign.replaceAll(" ", "+");
		transMap.remove("sign");
		//验签
		String transData = ParamUtil.getSignMsg(transMap);
		boolean result = false;
		try {
			result = CertUtil.getInstance().verify(transData, sign);
		} catch (Exception e) {
			logger.error("验签失败", e);
		}
		logger.error("验签结果：" + result);
		if(!result){
			transMap.clear();
			transMap.put("tranData", transData);
			transMap.put("sign", sign);
			transMap.put("msg", "验签失败");
		}
		return transMap;
	}
	
	/**
	 * 解析返回数据
	 * @param response
	 * @return
	 */
	public static Map parseResponse(Map transMap){
		//获取签名
		String sign = (String) transMap.get("sign");
		sign = sign.replaceAll(" ", "+");
		transMap.remove("sign");
		//验签
		String transData = ParamUtil.getSignMsg(transMap);
		boolean result = false;
		try {
			result = CertUtil.getInstance().verify(transData, sign);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(!result){
			transMap.clear();
			transMap.put("tranData", transData);
			transMap.put("sign", sign);
			transMap.put("msg", "验签失败");
		}
		return transMap;
	}

}
