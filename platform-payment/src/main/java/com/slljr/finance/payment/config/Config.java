package com.slljr.finance.payment.config;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;


/**
 * 配置文件信息(！！！切记生产需要替换配置信息)
 * 
 * 2018年10月11日
 * @author zcc
 */
public class Config {
	//注册地址
	private String registerUrl;
	//绑卡地址
	private String dhBindUrl;
	//解绑地址
	private String dhUnbindUrl;
	//扣款地址
	private String dhDeductUrl;
	//还款地址
	private String dhRepayUrl;
	//商户号
	private String merchantNo;
	//异步通知地址
	private String notifyUrl;
	//前台通知地址
	private String returnUrl;
	//余额查询地址
	private String queryBalanceUrl;
	//还款查询地址
	private String queryRepayUrl;
	//扣款查询地址
	private String queryDeductUrl;
	
	private static Config config = new Config();
	
	private Config(){
		try {
			PropertiesConfiguration config = new PropertiesConfiguration("serviceconfig.properties");
			this.merchantNo = config.getString("merchantNo");
			
			this.registerUrl = config.getString("paygate_register_url");
			this.dhBindUrl = config.getString("paygate_bind_url");
			this.dhUnbindUrl = config.getString("paygate_unbind_url");
			this.dhRepayUrl = config.getString("paygate_dhrepay_url");
			this.dhDeductUrl = config.getString("paygate_dhdeduct_url");
			
			this.queryBalanceUrl = config.getString("paygate_querybalance_url");
			this.queryRepayUrl = config.getString("paygate_queryrepay_url");
			this.queryDeductUrl = config.getString("paygate_querydeduct_url");
			
			this.notifyUrl = config.getString("notify_url");
			this.returnUrl = config.getString("return_url");
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}

	public static Config getInstance(){
		return config;
	}

	public String getMerchantNo() {
		return merchantNo;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public String getReturnUrl() {
		return returnUrl;
	}

	public String getRegisterUrl() {
		return registerUrl;
	}

	public String getDhBindUrl() {
		return dhBindUrl;
	}

	public String getDhUnbindUrl() {
		return dhUnbindUrl;
	}

	public String getDhDeductUrl() {
		return dhDeductUrl;
	}

	public String getDhRepayUrl() {
		return dhRepayUrl;
	}

	public String getQueryBalanceUrl() {
		return queryBalanceUrl;
	}

	public String getQueryRepayUrl() {
		return queryRepayUrl;
	}

	public String getQueryDeductUrl() {
		return queryDeductUrl;
	}
	
}
