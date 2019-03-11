package com.slljr.finance.common.pojo.dto;

import com.slljr.finance.common.pojo.model.UserProxyAudit;
import io.swagger.annotations.ApiModelProperty;

/**
 * @description:
 * @author: uncle.quentin.
 * @date: 2019/2/14.
 * @time: 16:06.
 */
public class UserProxyAuditDTO extends UserProxyAudit {

    @ApiModelProperty(value = "用户姓名")
    private String userName;

    @ApiModelProperty(value = "用户手机号")
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
