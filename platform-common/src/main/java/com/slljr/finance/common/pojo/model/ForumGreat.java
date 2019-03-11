package com.slljr.finance.common.pojo.model;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value="com.slljr.finance.common.pojo.model.ForumGreat")
public class ForumGreat implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7902345870755044101L;

	@ApiModelProperty(value="主键")
    private Integer id;

	@ApiModelProperty(value="帖子id")
    private Integer topicId;

	@ApiModelProperty(value="上级回帖人uid")
    private Integer parentReplyUid;

	@ApiModelProperty(value="回帖id")
    private Integer replyId;

	@ApiModelProperty(value="发帖人uid")
    private Integer topicUid;

	@ApiModelProperty(value="回帖人uid")
    private Integer replyUid;

	@ApiModelProperty(value="点赞数量")
    private Integer likeCount;

	@ApiModelProperty(value="1.发帖点赞,2.回帖点赞")
    private Integer type;

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

    public Integer getParentReplyUid() {
        return parentReplyUid;
    }

    public void setParentReplyUid(Integer parentReplyUid) {
        this.parentReplyUid = parentReplyUid;
    }

    public Integer getReplyId() {
        return replyId;
    }

    public void setReplyId(Integer replyId) {
        this.replyId = replyId;
    }

    public Integer getTopicUid() {
        return topicUid;
    }

    public void setTopicUid(Integer topicUid) {
        this.topicUid = topicUid;
    }

    public Integer getReplyUid() {
        return replyUid;
    }

    public void setReplyUid(Integer replyUid) {
        this.replyUid = replyUid;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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