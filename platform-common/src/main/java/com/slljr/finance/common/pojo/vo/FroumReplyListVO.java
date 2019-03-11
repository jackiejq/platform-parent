package com.slljr.finance.common.pojo.vo;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.slljr.finance.common.pojo.model.ForumReply;

import io.swagger.annotations.ApiModelProperty;

/**
   * 回帖列表vo
 * @author LXK
 *
 */
public class FroumReplyListVO extends ForumReply implements Serializable{

	
	
   /**
	 * 
	 */
	private static final long serialVersionUID = -8501798426446662169L;

@ApiModelProperty(value = "一级用户头像链接")
   private String headImg;
     
   @ApiModelProperty(value = "一级用户昵称")
   private String nickName;
   
   @ApiModelProperty(value = "二级用户用户名字")
   private String twoName;
   
   @ApiModelProperty(value = "一级用户用户名字")
   private String name;
   
   @ApiModelProperty(value="一级用户等级")
   private Integer grade;
   
   @ApiModelProperty(value="二级用户名字")
   private String twoNickname;
   
   @ApiModelProperty(value="二级头像")
   private String twoHeadimg;
   
   @ApiModelProperty(value="二级用户等级")
   private Integer twoGrade;
   
   @ApiModelProperty(value="二级用户回复评论时间")
   @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
   @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
   private Date twoCreatetime;
   
   @ApiModelProperty(value="二级回帖内容")
   private String twoContent;

   @ApiModelProperty(value="是否点赞true:已经点过赞,flase:未点赞")
   private String flag;
   
	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}
   
	
	public String getTwoNickname() {
		return twoNickname;
	}

	public void setTwoNickname(String twoNickname) {
		this.twoNickname = twoNickname;
	}

	public String getTwoHeadimg() {
		return twoHeadimg;
	}

	public void setTwoHeadimg(String twoHeadimg) {
		this.twoHeadimg = twoHeadimg;
	}

	public Integer getTwoGrade() {
		return twoGrade;
	}

	public void setTwoGrade(Integer twoGrade) {
		this.twoGrade = twoGrade;
	}

	public Date getTwoCreatetime() {
		return twoCreatetime;
	}

	public void setTwoCreatetime(Date twoCreatetime) {
		this.twoCreatetime = twoCreatetime;
	}

	public String getTwoContent() {
		return twoContent;
	}
	
	public void setTwoContent(String twoContent) {
		this.twoContent = twoContent;
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

	public String getTwoName() {
		return twoName;
	}

	public void setTwoName(String twoName) {
		this.twoName = twoName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


}
