package com.slljr.finance.common.pojo.vo;

import java.io.Serializable;

import com.slljr.finance.common.pojo.model.ForumReply;

import io.swagger.annotations.ApiModelProperty;

public class ReplyDetailsVo extends ForumReply implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3279961743250561620L;

	@ApiModelProperty(value = "第三方app用户昵称")
	private String nickName;
	
	@ApiModelProperty(value = "主贴内容")
	private String title;

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	
	
}
