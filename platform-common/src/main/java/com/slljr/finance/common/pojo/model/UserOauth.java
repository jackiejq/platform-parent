package com.slljr.finance.common.pojo.model;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;

import java.io.Serializable;
import java.util.Date;
/**
 * 
 * @author XueYi
 * 2018年12月4日 下午2:12:39
 */
public class UserOauth implements Serializable{
    /**
	 * 序列化
	 */
	private static final long serialVersionUID = 382261617482598848L;

	//主键id
    @ApiParam(hidden = true)
	private Integer id;

	//用户id
    @ApiParam(hidden = true)
    private Integer uid;

    //第三方唯一id
    @ApiModelProperty(value = "第三方app用户id", required = true)
    private String openId;

    //昵称
    @ApiModelProperty(value = "第三方app用户昵称")
    private String nickName;

    //头像链接
    @ApiModelProperty(value = "第三方app用户头像链接")
    private String headImg;

    //性别[1男,2女]
    @ApiModelProperty(value = "性别[1男,2女]")
    private Integer sex;

    //第三方[1微信/2QQ/3alipay/4新浪]
    @ApiModelProperty(value = "第三方app标识[1微信,2QQ,3alipay,4新浪]")
    private int target;

    //状态[-1锁定, 0正常]
    @ApiParam(hidden = true)
    private Integer status;

    //创建时间
    @ApiParam(hidden = true)
    private Date createTime;

    //修改时间
    @ApiParam(hidden = true)
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

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId == null ? null : openId.trim();
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName == null ? null : nickName.trim();
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg == null ? null : headImg.trim();
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Integer getTarget() {
        return target;
    }

    public void setTarget(Integer target) {
        this.target = target;
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

	@Override
	public String toString() {
		return "UserOauth [id=" + id + ", uid=" + uid + ", openId=" + openId + ", nickName=" + nickName + ", headImg="
				+ headImg + ", sex=" + sex + ", target=" + target + ", status=" + status + ", createTime=" + createTime
				+ ", updateTime=" + updateTime + "]";
	}
}