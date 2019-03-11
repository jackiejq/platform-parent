package com.slljr.finance.common.pojo.vo;

import com.slljr.finance.common.pojo.model.UserBasic;
import io.swagger.annotations.ApiModelProperty;

/**
 * @description: 用户实体VO类
 * @author: uncle.quentin.
 * @date: 2018/12/14.
 * @time: 15:18.
 */
public class UserBasicVO extends UserBasic {

    /**
     * 推荐人姓名
     */
    @ApiModelProperty(value = "推荐人姓名", required = false)
    private String refererUname;

    /**
     * 是否绑定信用卡信息[-1、已绑定 0、未绑定]
     */
    private String isBindingCreditCard;

    /**
     * 是否绑定银行卡信息[-1、已绑定 0、未绑定]
     */
    private String isBindingDebitCard;

    /**
     * 是否实名认证 [-1、已绑定 0、未认证]
     */
    private String isVerified;

    /**
     * 等级编号（代理用户，APP显示）
     */
    private String levelCode;

    /**
     * 等级类型[1每月新增有效会员, 2累计有效会员]
     */
    private String levelType;

    public String getRefererUname() {
        return refererUname;
    }

    public void setRefererUname(String refererUname) {
        this.refererUname = refererUname;
    }

    public String getIsBindingCreditCard() {
        return isBindingCreditCard;
    }

    public void setIsBindingCreditCard(String isBindingCreditCard) {
        this.isBindingCreditCard = isBindingCreditCard;
    }

    public String getIsBindingDebitCard() {
        return isBindingDebitCard;
    }

    public void setIsBindingDebitCard(String isBindingDebitCard) {
        this.isBindingDebitCard = isBindingDebitCard;
    }

    public String getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(String isVerified) {
        this.isVerified = isVerified;
    }

    public String getLevelCode() {
        return levelCode;
    }

    public void setLevelCode(String levelCode) {
        this.levelCode = levelCode;
    }

    public String getLevelType() {
        return levelType;
    }

    public void setLevelType(String levelType) {
        this.levelType = levelType;
    }
}
