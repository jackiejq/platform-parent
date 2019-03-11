package com.slljr.finance.common.pojo.vo;

import com.slljr.finance.common.pojo.model.UserTradePaymentRecord;

/**
 * @description:
 * @author: uncle.quentin.
 * @date: 2019/2/22.
 * @time: 15:22.
 */
public class UserTradePaymentRecordVO extends UserTradePaymentRecord {
    /**
     * 引用支付数据ID
     */
    private Integer refRecordId;

    /**
     * 1: 正常代偿计划数据 2：异常还款数据
     */
    private Integer DataType;

    public Integer getRefRecordId() {
        return refRecordId;
    }

    public void setRefRecordId(Integer refRecordId) {
        this.refRecordId = refRecordId;
    }

    public Integer getDataType() {
        return DataType;
    }

    public void setDataType(Integer dataType) {
        DataType = dataType;
    }
}

