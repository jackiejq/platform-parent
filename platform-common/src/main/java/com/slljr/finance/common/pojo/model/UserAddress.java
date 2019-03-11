package com.slljr.finance.common.pojo.model;

import java.io.Serializable;
/**
 * 1、用户地址的实体类
 * @author XueYi
 * 2018年12月4日 下午2:00:59
 */
public class UserAddress implements Serializable{
    /**
	 * 序列化
	 */
	private static final long serialVersionUID = -2046740691348484139L;

	//主键id
	private Integer id;

	//用户id
    private Integer uid;

    //姓名
    private String name;

    //手机号码
    private String phone;

    //省
    private String province;

    //市
    private String city;

    //县
    private String area;

    //街道等详细信息
    private String street;

    //状态[-1已删除,0正常]
    private Integer status;

    //创建时间
    private String createTime;

    //修改时间
    private String updateTime;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province == null ? null : province.trim();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area == null ? null : area.trim();
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street == null ? null : street.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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
		return "UserAddress [id=" + id + ", uid=" + uid + ", name=" + name + ", phone=" + phone + ", province="
				+ province + ", city=" + city + ", area=" + area + ", street=" + street + ", status=" + status
				+ ", createTime=" + createTime + ", updateTime=" + updateTime + "]";
	}
}