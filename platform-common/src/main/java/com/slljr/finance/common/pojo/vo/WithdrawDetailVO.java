package com.slljr.finance.common.pojo.vo;

import com.slljr.finance.common.pojo.model.WithdrawDetail;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @description: 提现记录VO对象
 * @author: goodni.
 * @date: 2019/2/27.
 * @time: 15:08.
 */
public class WithdrawDetailVO extends WithdrawDetail implements Serializable {

    @ApiModelProperty(value = "用户姓名", required = false)
    private String userName;

    @ApiModelProperty(value = "银行卡号", required = false)
    private String bankCardNo;

    @ApiModelProperty(value = "开户银行", required = false)
    private String openingBank;

    @ApiModelProperty(value = "审核人员", required = false)
    private String auditorName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getBankCardNo() {
        return bankCardNo;
    }

    public void setBankCardNo(String bankCardNo) {
        this.bankCardNo = bankCardNo;
    }

    public String getOpeningBank() {
        return openingBank;
    }

    public void setOpeningBank(String openingBank) {
        this.openingBank = openingBank;
    }

    public String getAuditorName() {
        return auditorName;
    }

    public void setAuditorName(String auditorName) {
        this.auditorName = auditorName;
    }
}
