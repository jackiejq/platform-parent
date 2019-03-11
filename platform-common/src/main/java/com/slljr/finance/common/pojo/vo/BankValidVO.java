package com.slljr.finance.common.pojo.vo;

/**
 * @description: 银行卡验证结果信息
 * @author: uncle.quentin.
 * @date: 2018/12/20.
 * @time: 10:53.
 */
public class BankValidVO {

    /**
     * 储蓄卡
     */
    public static final String DEBIT_CARD = "借记卡";
    /**
     * 信用卡
     */
    public static final String CREDIT_CARD = "贷记卡";

    /**
     * 返回编码
     */
    private String code;
    /**
     * 返回提示
     */
    private String msg;

    /**
     * 持卡人姓名
     */
    private String acctName;
    /**
     * 开卡使用的证件号码
     */
    private String certId;

    /**
     * 绑定手机号
     */
    private String phoneNum;
    /**
     * 地区
     */
    private String area;
    /**
     * 银行客服
     */
    private String tel;
    /**
     * 银行卡产品名称
     */
    private String brand;
    /**
     * 银行名称
     */
    private String bankName;
    /**
     * 银行卡种
     */
    private String cardType;
    /**
     * 银行官网
     */
    private String url;
    /**
     * 卡号
     */
    private String cardNum;

    public String getAcctName() {
        return acctName;
    }

    public void setAcctName(String acctName) {
        this.acctName = acctName;
    }

    public String getCertId() {
        return certId;
    }

    public void setCertId(String certId) {
        this.certId = certId;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "BankValidVO{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", acctName='" + acctName + '\'' +
                ", certId='" + certId + '\'' +
                ", phoneNum='" + phoneNum + '\'' +
                ", area='" + area + '\'' +
                ", tel='" + tel + '\'' +
                ", brand='" + brand + '\'' +
                ", bankName='" + bankName + '\'' +
                ", cardType='" + cardType + '\'' +
                ", url='" + url + '\'' +
                ", cardNum='" + cardNum + '\'' +
                '}';
    }
}
