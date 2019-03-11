package com.slljr.finance.common.pojo.model;

import java.util.Date;

public class UserSignLog {
    /**
	* 主键
	*/
    private Integer id;

    /**
	* 用户ID
	*/
    private Integer uid;

    /**
	* 签到备注
	*/
    private String mark;

    /**
	* 签到日期
	*/
    private Date signTime;

    /**
	* 创建时间
	*/
    private Date createTime;

    /**
	* 修改时间
	*/
    private Date updateTime;

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

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public Date getSignTime() {
        return signTime;
    }

    public void setSignTime(Date signTime) {
        this.signTime = signTime;
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