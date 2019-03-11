package com.slljr.finance.common.pojo.vo;

import java.io.Serializable;

import com.slljr.finance.common.pojo.model.UserAccount;
/**
 * 任务中心返回 用户积分和金币
 * @author LXK
 *
 */

public class UserTaskAccountVO extends UserAccount implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -295185823889056419L;
	
	/*用户名字*/
	private String name;
	
	/*宣传语*/
	private String slogan;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSlogan() {
		return slogan;
	}

	public void setSlogan(String slogan) {
		this.slogan = slogan;
	}
	
	

}
