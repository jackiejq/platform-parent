package com.slljr.finance.common.pojo.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 主帖实体
 * @author LXK
 *
 */
@ApiModel(value="com.slljr.finance.common.pojo.model.ForumTopic")
public class ForumTopic implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5926993751424603380L;

	@ApiModelProperty(value="帖子编号")
    private Integer id;

    @ApiModelProperty(value="发帖人ID")
    private Integer uid;

    @ApiModelProperty(value="标题")
    private String title;

    @ApiModelProperty(value="版块编号")
    private Integer sectionId;
    
    @ApiModelProperty(value="点赞数")
    private Integer likeCount;
    
    @ApiModelProperty(value="回复数量'")
    private Integer replyCount;

    @ApiModelProperty(value="浏览量")
    private Integer viewCount;

    @ApiModelProperty(value="发帖图片链接地址")
    private List<Object> topicUrl1;
     
    @ApiModelProperty(value="发帖图片链接地址")
    private String topicUrl;
    
    @ApiModelProperty(value="置顶[-1不置顶, 0置顶]")
    private Integer top;

    @ApiModelProperty(value="最后回复时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastReply;

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

    @ApiModelProperty(value="发帖内容")
    private String content;
       
  
	public String getTopicUrl() {
		return topicUrl;
	}

	public void setTopicUrl(String topicUrl) {
		this.topicUrl = topicUrl;
	}

	public List<Object> getTopicUrl1() {
		return topicUrl1;
	}

	public void setTopicUrl1(List<Object> topicUrl1) {
		this.topicUrl1 = topicUrl1;
	}

	public Integer getLikeCount() {
		return likeCount;
	}

	public void setLikeCount(Integer likeCount) {
		this.likeCount = likeCount;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public Integer getSectionId() {
        return sectionId;
    }

    public void setSectionId(Integer sectionId) {
        this.sectionId = sectionId;
    }

    public Integer getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(Integer replyCount) {
        this.replyCount = replyCount;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public Integer getTop() {
        return top;
    }

    public void setTop(Integer top) {
        this.top = top;
    }

    public Date getLastReply() {
        return lastReply;
    }

    public void setLastReply(Date lastReply) {
        this.lastReply = lastReply;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }
}