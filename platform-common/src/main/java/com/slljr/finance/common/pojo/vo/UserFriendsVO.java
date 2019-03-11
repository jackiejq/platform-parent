package com.slljr.finance.common.pojo.vo;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.slljr.finance.common.pojo.model.UserBasic;

import io.swagger.annotations.ApiModelProperty;

public class UserFriendsVO extends UserBasic implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2055908448137932457L;
	//佣金
	private Double commissionNum;
	//交易金额
	private Double paymentAmount;
	//交易佣金
	private Double agencyProfit;
	//交易时间
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date payTime;
	//订单ID
	private Integer tradeId;
	//查询月份
	private String months;
	//移动端给后台查询月份(201810)
	private String month;
	
	/**
	 * 用户创建时间
	 */
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	
	/**
	 * 用户手机号码
	 */
	private String phone;
    /**
     * 开始时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    /**
     * 开始时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
    
    @ApiModelProperty(value = "当前页", required = false)
    private Integer pageNum;

    @ApiModelProperty(value = "每页显示的条数", required = false)
    private Integer pageSize;
    
    public Integer getPageNum() {
        if (null == pageNum) {
            return 1;
        }
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        if (null == pageSize) {
            return 10;
        }
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }


	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getMonths() {
		return months;
	}

	public void setMonths(String months) {
		this.months = months;
	}

	public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
    
	public Double getPaymentAmount() {
		return paymentAmount;
	}

	public void setPaymentAmount(Double paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

	public Double getAgencyProfit() {
		return agencyProfit;
	}

	public void setAgencyProfit(Double agencyProfit) {
		this.agencyProfit = agencyProfit;
	}

	public Date getPayTime() {
		return payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Double getCommissionNum() {
		return commissionNum;
	}

	public void setCommissionNum(Double commissionNum) {
		this.commissionNum = commissionNum;
	}

	public Integer getTradeId() {
		return tradeId;
	}

	public void setTradeId(Integer tradeId) {
		this.tradeId = tradeId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	

}
