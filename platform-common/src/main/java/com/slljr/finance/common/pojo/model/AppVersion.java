package com.slljr.finance.common.pojo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;

@ApiModel(value="com.slljr.finance.common.pojo.model.AppVersion")
public class AppVersion implements Serializable {
    /**
	* 主键
	*/
    @ApiModelProperty(value="主键")
    private Integer id;

    /**
	* 最新版本号
	*/
    @ApiModelProperty(value="最新版本号")
    private String newversioncode;

    /**
	* 最小支持版本号
	*/
    @ApiModelProperty(value="最小支持版本号")
    private String minversioncode;

    /**
	* 版本类型：【1：Android；2：IOS】
	*/
    @ApiModelProperty(value="版本类型：【1：Android；2：IOS】")
    private Integer type;

    /**
	* 下载url
	*/
    @ApiModelProperty(value="下载url")
    private String apkurl;

    /**
	* 更新文案
	*/
    @ApiModelProperty(value="更新文案")
    private String updatecontent;

    /**
	* 是否强制更新【-1：否；0：是】
	*/
    @ApiModelProperty(value="是否强制更新【-1：否；0：是】")
    private Integer forceupdate;

    /**
	* 创建日期
	*/
    @ApiModelProperty(value="创建日期")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;

    /**
	* 修改时间
	*/
    @ApiModelProperty(value="修改时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date updateTime;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNewversioncode() {
        return newversioncode;
    }

    public void setNewversioncode(String newversioncode) {
        this.newversioncode = newversioncode;
    }

    public String getMinversioncode() {
        return minversioncode;
    }

    public void setMinversioncode(String minversioncode) {
        this.minversioncode = minversioncode;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getApkurl() {
        return apkurl;
    }

    public void setApkurl(String apkurl) {
        this.apkurl = apkurl;
    }

    public String getUpdatecontent() {
        return updatecontent;
    }

    public void setUpdatecontent(String updatecontent) {
        this.updatecontent = updatecontent;
    }

    public Integer getForceupdate() {
        return forceupdate;
    }

    public void setForceupdate(Integer forceupdate) {
        this.forceupdate = forceupdate;
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