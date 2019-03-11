package com.slljr.finance.common.pojo.vo;

/**
 * @description: 阿里银行卡信息查询返回业务实体类
 * @author: uncle.quentin.
 * @date: 2018/12/24.
 * @time: 9:57.
 */
public class QueryBankResultVO {
    /**
     * 发卡银行
     */
    private String bankname;
    /**
     * bin
     */
    private String cardprefixnum;
    /**
     * 银行卡名称
     */
    private String cardname;
    /**
     * 银行卡类型
     */
    private String cardtype;
    /**
     * bin长度
     */
    private String cardprefixlength;
    /**
     * 起始数
     */
    private String banknum;
    /**
     * 是否符合编码规范
     */
    private String isLuhn;

    /**
     * 是否是信用卡,1为借记卡,2为信用卡
     */
    private String iscreditcard;
    /**
     * 	银行卡号长度
     */
    private String cardlength;
    /**
     * 银行网址
     */
    private String bankurl;
    /**
     *
     */
    private String enbankname;
    /**
     * 简写
     */
    private String abbreviation;
    /**
     * 银行LOGO
     */
    private String bankimage;
    /**
     * 银行电话
     */
    private String servicephone;

    public String getBankname() {
        return bankname;
    }

    public void setBankname(String bankname) {
        this.bankname = bankname;
    }

    public String getCardprefixnum() {
        return cardprefixnum;
    }

    public void setCardprefixnum(String cardprefixnum) {
        this.cardprefixnum = cardprefixnum;
    }

    public String getCardname() {
        return cardname;
    }

    public void setCardname(String cardname) {
        this.cardname = cardname;
    }

    public String getCardtype() {
        return cardtype;
    }

    public void setCardtype(String cardtype) {
        this.cardtype = cardtype;
    }

    public String getCardprefixlength() {
        return cardprefixlength;
    }

    public void setCardprefixlength(String cardprefixlength) {
        this.cardprefixlength = cardprefixlength;
    }

    public String getBanknum() {
        return banknum;
    }

    public void setBanknum(String banknum) {
        this.banknum = banknum;
    }

    public String getIsLuhn() {
        return isLuhn;
    }

    public void setIsLuhn(String isLuhn) {
        this.isLuhn = isLuhn;
    }

    public String getIscreditcard() {
        return iscreditcard;
    }

    public void setIscreditcard(String iscreditcard) {
        this.iscreditcard = iscreditcard;
    }

    public String getCardlength() {
        return cardlength;
    }

    public void setCardlength(String cardlength) {
        this.cardlength = cardlength;
    }

    public String getBankurl() {
        return bankurl;
    }

    public void setBankurl(String bankurl) {
        this.bankurl = bankurl;
    }

    public String getEnbankname() {
        return enbankname;
    }

    public void setEnbankname(String enbankname) {
        this.enbankname = enbankname;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getBankimage() {
        return bankimage;
    }

    public void setBankimage(String bankimage) {
        this.bankimage = bankimage;
    }

    public String getServicephone() {
        return servicephone;
    }

    public void setServicephone(String servicephone) {
        this.servicephone = servicephone;
    }

    @Override
    public String toString() {
        return "QueryBankResultVO{" +
                "bankname='" + bankname + '\'' +
                ", cardprefixnum='" + cardprefixnum + '\'' +
                ", cardname='" + cardname + '\'' +
                ", cardtype='" + cardtype + '\'' +
                ", cardprefixlength='" + cardprefixlength + '\'' +
                ", banknum='" + banknum + '\'' +
                ", isLuhn='" + isLuhn + '\'' +
                ", iscreditcard='" + iscreditcard + '\'' +
                ", cardlength='" + cardlength + '\'' +
                ", bankurl='" + bankurl + '\'' +
                ", enbankname='" + enbankname + '\'' +
                ", abbreviation='" + abbreviation + '\'' +
                ", bankimage='" + bankimage + '\'' +
                ", servicephone='" + servicephone + '\'' +
                '}';
    }
}
