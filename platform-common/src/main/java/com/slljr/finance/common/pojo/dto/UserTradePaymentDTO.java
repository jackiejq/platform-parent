package com.slljr.finance.common.pojo.dto;

import com.slljr.finance.common.pojo.model.UserTradePaymentRecord;

import java.util.Date;

/**
 * @description: 交易订单明细DTO参数对象
 * @author: uncle.quentin.
 * @date: 2018/12/14.
 * @time: 17:15.
 */
public class UserTradePaymentDTO extends UserTradePaymentRecord {

    /**
     * 开始时间(支付)
     */
    private Date payStartTime;
    /**
     * 开始时间(支付)
     */
    private Date payEndTime;

    public Date getPayStartTime() {
        return payStartTime;
    }

    public void setPayStartTime(Date payStartTime) {
        this.payStartTime = payStartTime;
    }

    public Date getPayEndTime() {
        return payEndTime;
    }

    public void setPayEndTime(Date payEndTime) {
        this.payEndTime = payEndTime;
    }
}
