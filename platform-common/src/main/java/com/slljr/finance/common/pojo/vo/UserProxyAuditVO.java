package com.slljr.finance.common.pojo.vo;

import com.slljr.finance.common.pojo.model.UserProxyAudit;

/**
 * @description: 代理审核业务对象
 * @author: uncle.quentin.
 * @date: 2019/1/22.
 * @time: 17:40.
 */
public class UserProxyAuditVO extends UserProxyAudit {

    /**
     * 用户名
     */
    private String userName;
    /**
     * 用户手机
     */
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
