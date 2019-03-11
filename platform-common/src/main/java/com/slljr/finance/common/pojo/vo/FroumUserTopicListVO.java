package com.slljr.finance.common.pojo.vo;

import java.io.Serializable;

import com.slljr.finance.common.pojo.model.ForumTopic;

import io.swagger.annotations.ApiModelProperty;

public class FroumUserTopicListVO extends ForumTopic implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7869310972917176944L;

	 /* 性别[1男,2女]*/
	@ApiModelProperty(value=" 性别[1男,2女]")
    private Integer sex;
    
	@ApiModelProperty(value="姓名")
    private String name;
	
	@ApiModelProperty(value="积分点数")
    private Integer integral;

   @ApiModelProperty(value = "第三方app用户头像链接")
   private String headImg;
     
   @ApiModelProperty(value = "第三方app用户昵称")
   private String nickName;
   
   @ApiModelProperty(value="用户等级")
   private Integer grade;
	
	public String getHeadImg() {
	return headImg;
	}
	
	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}
	
	public String getNickName() {
		return nickName;
	}
	
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	
	public Integer getGrade() {
		return grade;
	}
	
	public void setGrade(Integer grade) {
		this.grade = grade;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public Integer getIntegral() {
		return integral;
	}

	public void setIntegral(Integer integral) {
		this.integral = integral;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	
}
