package com.slljr.finance.common.pojo.model;

import java.io.Serializable;
/**
 * 1、地区实体类
 * @author XueYi
 * 2018年12月4日 上午11:32:43
 */
public class BasicRegion implements Serializable{
    /**
	 * 序列化
	 */
	private static final long serialVersionUID = -2332864509892556076L;

	//地区主键编号
	private String regionId;

	//地区名称
    private String regionName;

    //地区缩写
    private String regionShortName;

    //行政地区编号
    private String regionCode;

    //地区父id
    private String regionParentId;

    //地区级别 1-省、自治区、直辖市 2-地级市、地区、自治州、盟 3-市辖区、县级市、县
    private Integer regionLevel;

    //创建时间
    private String createTime;

    //修改时间
    private String updateTime;

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId == null ? null : regionId.trim();
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName == null ? null : regionName.trim();
    }

    public String getRegionShortName() {
        return regionShortName;
    }

    public void setRegionShortName(String regionShortName) {
        this.regionShortName = regionShortName == null ? null : regionShortName.trim();
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode == null ? null : regionCode.trim();
    }

    public String getRegionParentId() {
        return regionParentId;
    }

    public void setRegionParentId(String regionParentId) {
        this.regionParentId = regionParentId == null ? null : regionParentId.trim();
    }

    public Integer getRegionLevel() {
        return regionLevel;
    }

    public void setRegionLevel(Integer regionLevel) {
        this.regionLevel = regionLevel;
    }

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	@Override
	public String toString() {
		return "BasicRegion [regionId=" + regionId + ", regionName=" + regionName + ", regionShortName="
				+ regionShortName + ", regionCode=" + regionCode + ", regionParentId=" + regionParentId
				+ ", regionLevel=" + regionLevel + ", createTime=" + createTime + ", updateTime=" + updateTime + "]";
	}
}