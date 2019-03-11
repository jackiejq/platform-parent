package com.slljr.finance.common.pojo.dto;

import com.slljr.finance.common.pojo.model.GoodsOrder;
import io.swagger.annotations.ApiModelProperty;

/**
 * @description:
 * @author: uncle.quentin.
 * @date: 2019/2/14.
 * @time: 15:42.
 */
public class GoodsOrderDTO extends GoodsOrder {

    /**
     * 用户姓名
     */
    @ApiModelProperty(value = "用户姓名", required = false)
    private String userName;

    /**
     * 用户手机号
     */
    @ApiModelProperty(value = "用户手机号", required = false)
    private String userPhone;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }
}
