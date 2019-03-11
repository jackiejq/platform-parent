package com.slljr.finance.common.pojo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;

@ApiModel(value="com.slljr.finance.common.pojo.model.IdcardAddr")
public class IdcardAddr implements Serializable {
    /**
	* 
	*/
    @ApiModelProperty(value="")
    private Integer id;

    /**
	* 身份证前6位
	*/
    @ApiModelProperty(value="身份证前6位")
    private String code;

    /**
	* 省/市名称
	*/
    @ApiModelProperty(value="省/市名称")
    private String name;

    /**
	* 所属省份id
	*/
    @ApiModelProperty(value="所属省份id")
    private Integer provinceId;

    /**
	* 所属城市id
	*/
    @ApiModelProperty(value="所属城市id")
    private Integer cityId;

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

    public Integer getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Integer provinceId) {
        this.provinceId = provinceId;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
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