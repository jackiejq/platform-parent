package com.slljr.finance.common.pojo.vo;

import com.slljr.finance.common.pojo.model.UserBalanceDetail;

/**
 * @description: 积分/金币变更业务实体对象
 * @author: uncle.quentin.
 * @date: 2019/1/15.
 * @time: 19:24.
 */
public class UserBalanceDetailVO extends UserBalanceDetail {
    /**
     * 用户手机
     */
    private String phone;
    /**
     * 用户姓名
     */
    private String name;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
