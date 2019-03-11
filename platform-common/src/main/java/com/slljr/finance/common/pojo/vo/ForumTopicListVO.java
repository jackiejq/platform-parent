package com.slljr.finance.common.pojo.vo;

import java.io.Serializable;

import com.slljr.finance.common.pojo.model.ForumTopic;

import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * @author LXK
 *
 */
public class ForumTopicListVO extends ForumTopic implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -600744867236923400L;
	
	 //头像链接
    @ApiModelProperty(value = "第三方app用户头像链接")
    private String headImg;
      
    @ApiModelProperty(value = "第三方app用户昵称")
    private String nickName;
    
    @ApiModelProperty(value="用户等级")
    private Integer grade;

    @ApiModelProperty(value="是否点赞true:已经点过赞,flase:未点赞")
    private String flag;
    
	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

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
   
}
