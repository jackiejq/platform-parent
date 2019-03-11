package com.slljr.finance.common.pojo.vo;

import com.slljr.finance.common.pojo.model.RepayErrorRecord;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author goodni
 * @date 2019/2/27 11:46
 */
public class RepayErrorRecordVO extends RepayErrorRecord implements Serializable {
    @ApiModelProperty(value = "用户姓名", required = false)
    private String userName;

    @ApiModelProperty(value = "银行卡号", required = false)
    private String bankCardNo;

    @ApiModelProperty(value = "开户银行", required = false)
    private String openingBank;

    @ApiModelProperty(value = "通道名称", required = false)
    private String channelName;

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

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }
}
