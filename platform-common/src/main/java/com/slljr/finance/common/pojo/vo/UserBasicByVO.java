package com.slljr.finance.common.pojo.vo;

import java.io.Serializable;

import com.slljr.finance.common.pojo.model.UserBasic;

public class UserBasicByVO extends UserBasic implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4564467361978981530L;

	/* 论坛用户状态[-1禁言, 0正常]*/
    private Integer bbsStatus;

	/**
	 * 推荐人姓名
	 */
	private String refererUName;
    
    public Integer getBbsStatus() {
 		return bbsStatus;
 	}

 	public void setBbsStatus(Integer bbsStatus) {
 		this.bbsStatus = bbsStatus;
 	}

	public String getRefererUName() {
		return refererUName;
	}

	public void setRefererUName(String refererUName) {
		this.refererUName = refererUName;
	}
}
