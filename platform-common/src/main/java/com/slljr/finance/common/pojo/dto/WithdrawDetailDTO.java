package com.slljr.finance.common.pojo.dto;/**
 * @description:
 * @author: uncle.quentin.
 * @date: 2019/2/27
 * @time: 14:57
 */

import com.slljr.finance.common.pojo.model.WithdrawDetail;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author goodni
 * @date 2019/2/27 14:57
 */
public class WithdrawDetailDTO extends WithdrawDetail {
    @ApiModelProperty(value = "用户姓名", required = false)
    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
