package com.slljr.finance.common.pojo.vo;

import com.slljr.finance.common.pojo.model.WithdrawDetail;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @description:
 * @author: goodni.
 * @date: 2019/2/27
 * @time: 17:39
 */
public class WithdrawDepositDetailVO extends WithdrawDetail implements Serializable {
    @ApiModelProperty(value = "用户姓名", required = false)
    private String userName;

    @ApiModelProperty(value = "银行卡号", required = false)
    private String bankCardNo;

    @ApiModelProperty(value = "开户银行", required = false)
    private String openingBank;

    @ApiModelProperty(value = "审核人员", required = false)
    private String auditorName;

    @ApiModelProperty(value = "手机", required = false)
    private String phone;

    @ApiModelProperty(value = "开户省份", required = false)
    private String openingBankProvince;

    @ApiModelProperty(value = "开户城市", required = false)
    private String openingBankCity;

    //公钥，RSAUtil使用该公钥解密
    private String bankCardNoSign;

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOpeningBankProvince() {
        return openingBankProvince;
    }

    public void setOpeningBankProvince(String openingBankProvince) {
        this.openingBankProvince = openingBankProvince;
    }

    public String getOpeningBankCity() {
        return openingBankCity;
    }

    public void setOpeningBankCity(String openingBankCity) {
        this.openingBankCity = openingBankCity;
    }

    public String getBankCardNoSign() {
        return bankCardNoSign;
    }

    public void setBankCardNoSign(String bankCardNoSign) {
        this.bankCardNoSign = bankCardNoSign;
    }
}
