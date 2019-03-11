package com.slljr.finance.common.pojo.model;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
/**
 * 1、轮播图的实体类
 * @author XueYi
 * 2018年12月7日 下午4:08:45
 */
public class AdvConfig implements Serializable{
    /**
	 * 序列化
	 */
	private static final long serialVersionUID = -689800220518269615L;

	@ApiModelProperty(value = "轮播图编号", required = false)
	private Integer id;

	@ApiModelProperty(value = "图片标题", required = false)
    private String title;

	@ApiModelProperty(value = "图片地址", required = false)
    private String imgUrl;

	@ApiModelProperty(value = "超链接", required = false)
    private String link;
	
	@ApiModelProperty(value = "状态[-1删除,0正常]", required = false)
    private Integer status;

	@ApiParam( hidden=true )
	@ApiModelProperty(value = "创建时间", required = false)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;

	@ApiParam( hidden=true )
	@ApiModelProperty(value = "修改时间", required = false)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl == null ? null : imgUrl.trim();
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link == null ? null : link.trim();
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