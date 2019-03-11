package com.slljr.finance.common.pojo.model;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 论坛回复实体
 * @author LXK
 *
 */
@ApiModel(value="com.slljr.finance.common.pojo.model.ForumReply")
public class ForumReply implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7308458656854233981L;

	@ApiModelProperty(value="主键")
    private Integer id;

	@ApiModelProperty(value="帖子id")
    private Integer topicId;

	@ApiModelProperty(value="回帖人uid")
    private Integer replyUid;
    
	@ApiModelProperty(value="上级回复帖子id")
    private Integer parentReplyUid;
	
	@ApiModelProperty(value="发帖内容")
    private String content;
    
	@ApiModelProperty(value="点赞数量")
    private Integer likeCount;
    
	@ApiModelProperty(value="状态[-1隐藏, 0显示]")
    private Integer status;
    
	@ApiModelProperty(value="创建时间")
	 @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    
	@ApiModelProperty(value="修改时间")
	 @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTopicId() {
        return topicId;
    }

    public void setTopicId(Integer topicId) {
        this.topicId = topicId;
    }

    

    public Integer getReplyUid() {
		return replyUid;
	}

	public void setReplyUid(Integer replyUid) {
		this.replyUid = replyUid;
	}

	public Integer getParentReplyUid() {
        return parentReplyUid;
    }

    public void setParentReplyUid(Integer parentReplyUid) {
        this.parentReplyUid = parentReplyUid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}