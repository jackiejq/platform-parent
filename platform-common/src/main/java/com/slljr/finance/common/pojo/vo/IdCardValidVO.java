package com.slljr.finance.common.pojo.vo;

/**
 * @description: 身份证校验结果信息
 * @author: uncle.quentin.
 * @date: 2018/12/20.
 * @time: 9:46.
 */
public class IdCardValidVO {

    public static final String MALE = "M";
    public static final String FEMALE = "F";

    /**
     * 返回编码
     */
    private String code;
    /**
     * 返回消息
     */
    private String msg;
    /**
     * 身份证地址
     */
    private String address;
    /**
     * 身份证出生年月
     */
    private String birthday;
    /**
     * 身份证性别 (M:男 F：女)
     */
    private String sex;
    /**
     * 错误描述
     */
    private String error;

    /**
     * 姓名
     */
    private String name;

    /**
     * 身份证
     */
    private String idCard;

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

}
