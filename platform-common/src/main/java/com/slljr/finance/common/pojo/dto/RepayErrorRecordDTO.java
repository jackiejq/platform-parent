package com.slljr.finance.common.pojo.dto;

import com.slljr.finance.common.pojo.model.RepayErrorRecord;
import io.swagger.annotations.ApiModelProperty;

/**
 * @description: 支付错误订单DTO参数对象
 * @author: goodni
 * @date: 2018/2/26
 */
public class RepayErrorRecordDTO extends RepayErrorRecord {
    @ApiModelProperty(value = "用户姓名", required = false)
    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
