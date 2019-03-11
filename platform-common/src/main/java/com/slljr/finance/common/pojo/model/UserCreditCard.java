package com.slljr.finance.common.pojo.model;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
/**
 * 
 * @author XueYi
 * 2018年12月4日 下午2:10:48
 */
public class UserCreditCard implements Serializable{
	/**
	 * 序列化
	 */
	private static final long serialVersionUID = -5725701194303883780L;

	//主键id
	@ApiParam(hidden = true)
    private Integer id;

    //用户id
    @ApiModelProperty(value = "用户id", required = false)
    private Integer uid;

    //银行id
    @ApiModelProperty(value = "银行id", required = false)
    private Integer bankId;

    //信用卡号
    @ApiModelProperty(value = "信用卡号", required = false)
    private String bankCardNo;

    //银行名称
    @ApiModelProperty(value = "银行名称", required = false)
    private String bankName;

    //信用卡有效期
    @ApiModelProperty(value = "信用卡有效期", required = false)
    private Date expirationDate;

    //信用卡安全码
    @ApiModelProperty(value = "信用卡安全码", required = false)
    private String securityCode;

    //预留手机号码
    @ApiModelProperty(value = "预留手机号码", required = false)
    private String phone;

    //账单日
    @ApiModelProperty(value = "账单日", required = false)
    private Date billDate;

    //最后还款日
    @ApiModelProperty(value = "最后还款日", required = false)
    private Date lastRepaymentDate;

    //信用卡额度
    @ApiModelProperty(value = "信用卡额度", required = false)
    private Integer creditLimit;

    //状态[-1删除,0正常]
    @ApiModelProperty(value = "状态[-1删除,0正常]", required = false)
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

    public Integer getBankId() {
        return bankId;
    }

    public void setBankId(Integer bankId) {
        this.bankId = bankId;
    }

    public String getBankCardNo() {
        return bankCardNo;
    }

    public void setBankCardNo(String bankCardNo) {
        this.bankCardNo = bankCardNo == null ? null : bankCardNo.trim();
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName == null ? null : bankName.trim();
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(String securityCode) {
        this.securityCode = securityCode == null ? null : securityCode.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public Date getBillDate() {
        return billDate;
    }

    public void setBillDate(Date billDate) {
        this.billDate = billDate;
    }

    public Date getLastRepaymentDate() {
        return lastRepaymentDate;
    }

    public void setLastRepaymentDate(Date lastRepaymentDate) {
        this.lastRepaymentDate = lastRepaymentDate;
    }

    public Integer getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(Integer creditLimit) {
        this.creditLimit = creditLimit;
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
		return "UserCreditCard [id=" + id + ", uid=" + uid + ", bankId=" + bankId + ", bankCardNo=" + bankCardNo
				+ ", bankName=" + bankName + ", expirationDate=" + expirationDate + ", securityCode=" + securityCode
				+ ", phone=" + phone + ", billDate=" + billDate + ", lastRepaymentDate=" + lastRepaymentDate
				+ ", creditLimit=" + creditLimit + ", status=" + status + ", createTime=" + createTime + ", updateTime="
				+ updateTime + "]";
	}
}