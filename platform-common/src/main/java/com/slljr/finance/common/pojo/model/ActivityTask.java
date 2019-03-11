package com.slljr.finance.common.pojo.model;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
/**
 * 1、活动任务的实体类
 * @author XueYi
 * 2018年12月11日 下午5:31:35
 */
public class ActivityTask implements Serializable{
	/**
	 * 序列化
	 */
	private static final long serialVersionUID = 6207731073022885072L;

	@ApiModelProperty(value = "任务编号", required = false)
    private Integer id;

    @ApiModelProperty(value = "代码", required = false)
    private String code;

	@ApiModelProperty(value = "标题", required = false)
    private String title;

	@ApiModelProperty(value = "奖励值", required = false)
    private Double number;

	@ApiModelProperty(value = "图片url", required = false)
    private String imgUrl;

	@ApiModelProperty(value = "奖励类型[1积分,2现金]", required = false)
    private Integer type;

	@ApiModelProperty(value = "开始时间", required = false)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private String startTime;

	@ApiModelProperty(value = "结束时间", required = false)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private String endTime;

	@ApiParam(hidden = true)
	@ApiModelProperty(value = "状态[-1禁用,0启用]", required = false)
    private Integer status;

	@ApiModelProperty(value = "操作者用户id", required = false)
    private Integer optUid;

	@ApiParam(hidden = true)
	@ApiModelProperty(value = "创建时间", required = false)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;

	@ApiParam(hidden = true)
	@ApiModelProperty(value = "修改时间", required = false)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date updateTime;

	@ApiModelProperty(value = "详情", required = false)
    private String detail;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public Double getNumber() {
        return number;
    }

    public void setNumber(Double number) {
        this.number = number;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl == null ? null : imgUrl.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }


    public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getOptUid() {
        return optUid;
    }

    public void setOptUid(Integer optUid) {
        this.optUid = optUid;
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

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail == null ? null : detail.trim();
    }

	@Override
	public String toString() {
		return "ActivityTask [id=" + id + ", title=" + title + ", number=" + number + ", imgUrl=" + imgUrl + ", type="
				+ type + ", startTime=" + startTime + ", endTime=" + endTime + ", status=" + status + ", optUid="
				+ optUid + ", createTime=" + createTime + ", updateTime=" + updateTime + ", detail=" + detail + "]";
	}
}