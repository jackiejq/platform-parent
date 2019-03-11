package com.slljr.finance.common.pojo.model;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;

public class UserBasic implements Serializable {
    public enum TypeEnum{
        NORMAL(0,"普通用户"), AGENT(1,"代理用户"), SYSTEM(2,"系统用户");

        int key;
        String msg;
        TypeEnum(int key, String msg){
            this.key = key;
            this.msg = msg;
        }

        public int getKey() {
            return key;
        }
        public void setKey(int key) {
            this.key = key;
        }
        public String getMsg() {
            return msg;
        }
        public void setMsg(String msg) {
            this.msg = msg;
        }
    }
    /* 用户id*/
    private Integer id;

    /* 手机号码*/
    private String phone;

    /* 登录密码*/
    private String password;

    /* 姓名*/
    private String name;

    /* 性别[1男,2女]*/
    private Integer sex;

    /* 身份证号*/
    private String idCard;

    /* 身份证省份*/
    private String idcProvince;

    /* 身份证城市*/
    private String idcCity;

    /* 身份证地址*/
    private String idcAddress;

    /*身份证正面url*/
    private String idcImgUrl1;
    
    /*身份证反面url*/
    private String idcImgUrl2;
    
    /* 推荐人用户id*/
    private Integer refererUid;

    /* 用户类型[0用户,1代理,2系统用户]*/
    private Integer type;

    /* 状态[-1锁定, 0正常]*/
    private Integer status;

    /* 创建时间*/
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;

    /* 修改时间*/
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date updateTime;

    /*用户昵称*/
    private String nickName;

    private String headImg;

    /**
     * 分享二维码链接URL
     */
    private String shareQrUrl;

    /**
     * 论坛等级
     */
    @ApiModelProperty(value="论坛等级")
    private Integer grade;

    /**
     * 论坛积分
     */
    @ApiModelProperty(value="论坛积分")
    private Integer integral;

    /**
     * 论坛状态【-1禁言, 0正常】
     */
    @ApiModelProperty(value="论坛状态【-1禁言, 0正常】")
    private Integer forumStatus;
    
    private static final long serialVersionUID = 1L;

    public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getIdcProvince() {
        return idcProvince;
    }

    public void setIdcProvince(String idcProvince) {
        this.idcProvince = idcProvince;
    }

    public String getIdcCity() {
        return idcCity;
    }

    public void setIdcCity(String idcCity) {
        this.idcCity = idcCity;
    }

    public String getIdcAddress() {
        return idcAddress;
    }

    public void setIdcAddress(String idcAddress) {
        this.idcAddress = idcAddress;
    }

    public Integer getRefererUid() {
        return refererUid;
    }

    public void setRefererUid(Integer refererUid) {
        this.refererUid = refererUid;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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

	public String getIdcImgUrl1() {
		return idcImgUrl1;
	}

	public void setIdcImgUrl1(String idcImgUrl1) {
		this.idcImgUrl1 = idcImgUrl1;
	}

	public String getIdcImgUrl2() {
		return idcImgUrl2;
	}

	public void setIdcImgUrl2(String idcImgUrl2) {
		this.idcImgUrl2 = idcImgUrl2;
	}

    public String getShareQrUrl() {
        return shareQrUrl;
    }

    public void setShareQrUrl(String shareQrUrl) {
        this.shareQrUrl = shareQrUrl;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public Integer getIntegral() {
        return integral;
    }

    public void setIntegral(Integer integral) {
        this.integral = integral;
    }

    public Integer getForumStatus() {
        return forumStatus;
    }

    public void setForumStatus(Integer forumStatus) {
        this.forumStatus = forumStatus;
    }
}