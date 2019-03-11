package com.slljr.finance.common.pojo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.slljr.finance.common.pojo.vo.BasePageVO;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class ForumPost extends BasePageVO {
    /**
     * 主键
     */
    @ApiModelProperty(value="主键")
    private Integer id;

    /**
     * 发帖人ID
     */
    @ApiModelProperty(value="发帖人ID")
    private Integer uid;

    /**
     * 帖子标题
     */
    @ApiModelProperty(value="帖子标题")
    private String title;

    /**
     * 是否置顶[-1不置顶, 0置顶]
     */
    @ApiModelProperty(value="是否置顶[-1不置顶, 0置顶]")
    private Integer top;

    /**
     * 浏览量
     */
    @ApiModelProperty(value="浏览量")
    private Integer viewCount;

    /**
     * 帖子状态（0：正常、-1：失效）
     */
    @ApiModelProperty(value="帖子状态（0：正常、-1：失效）")
    private Integer status;

    /**
     * 创建时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 修改时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 帖子内容
     */
    @ApiModelProperty(value="帖子内容")
    private String content;

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
        this.title = title;
    }

    public Integer getTop() {
        return top;
    }

    public void setTop(Integer top) {
        this.top = top;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
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
        this.content = content;
    }
}