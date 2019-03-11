package com.slljr.finance.common.pojo.dto;

import com.slljr.finance.common.pojo.model.UserTradeOrder;
import io.swagger.annotations.ApiModelProperty;

/**
 * @description: 用户支付订单DTO参数对象
 * @author: uncle.quentin.
 * @date: 2018/12/14.
 * @time: 17:10.
 */
public class UserTradeOrderDTO extends UserTradeOrder {

    /**
     * 用户姓名
     */
    @ApiModelProperty(value = "用户姓名", required = false)
   private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
