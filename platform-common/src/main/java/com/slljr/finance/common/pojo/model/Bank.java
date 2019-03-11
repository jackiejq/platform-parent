package com.slljr.finance.common.pojo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;

@ApiModel(value="com.slljr.finance.common.pojo.model.Bank")
public class Bank implements Serializable {
    /**
	* 
	*/
    @ApiModelProperty(value="")
    private Integer id;

    /**
	* 代码
	*/
    @ApiModelProperty(value="代码")
    private String code;

    /**
	* 名称
	*/
    @ApiModelProperty(value="名称")
    private String name;

    /**
	* 大logo(有logo和银行名称)
	*/
    @ApiModelProperty(value="大logo(有logo和银行名称)")
    private String bigLogoUrl;

    /**
	* 小logo(只有logo)
	*/
    @ApiModelProperty(value="小logo(只有logo)")
    private String smallLogoUrl;

    /**
	* 状态[-1禁用, 0显示]
	*/
    @ApiModelProperty(value="状态[-1禁用, 0显示]")
    private Integer status;

    /**
	* 
	*/
    @ApiModelProperty(value="")
    private Date createTime;

    /**
	* 
	*/
    @ApiModelProperty(value="")
    private Date updateTime;

    private static final long serialVersionUID = 1L;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBigLogoUrl() {
        return bigLogoUrl;
    }

    public void setBigLogoUrl(String bigLogoUrl) {
        this.bigLogoUrl = bigLogoUrl;
    }

    public String getSmallLogoUrl() {
        return smallLogoUrl;
    }

    public void setSmallLogoUrl(String smallLogoUrl) {
        this.smallLogoUrl = smallLogoUrl;
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