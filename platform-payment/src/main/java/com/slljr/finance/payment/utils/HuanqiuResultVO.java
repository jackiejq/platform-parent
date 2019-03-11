package com.slljr.finance.payment.utils;

/**
 * @description: 环球汇聚落地还款通道返回对象
 * @author: uncle.quentin.
 * @date: 2018/12/21.
 * @time: 10:02.
 */
public class HuanqiuResultVO {

    /**
     * 查询订单状态返回状态码
     */
    public enum HuanqiuQueryResultEnum {

        QUERY_RESUL_SUCCESS("00", "成功"),
        QUERY_RESUL_FAIL("02", "失败"),
        QUERY_RESUL_CLOSED("04", "订单关闭"),
        QUERY_RESUL_PROCESSING("01", "处理中，半个小时后再次发起对账"),
        HUANQIU_RESUL_NOT_EXIST("99", "订单号不存在");
        public String key;
        public String value;

        HuanqiuQueryResultEnum(String key, String value) {
            this.key = key;
            this.value = value;
        }

        /**
         * 获取枚举值
         *
         * @author  guoqun.yang
         * @date    2018/4/11 9:25
         * @param   operate
         * @return  com.hsl.waybill.common.enums.MessageTypeEnum
         * @version 1.0
         */
        public static HuanqiuQueryResultEnum val(String operate) {
            //values()方法返回enum实例的数组
            for(HuanqiuQueryResultEnum s : values()) {
                if(operate.equals(s.getKey())){
                    return s;
                }
            }
            return null;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

    }

    public static final String DATASUCCESS = "SUCCESS";

    /**
     * 是否成功
     */
    private boolean isSuccess;

    /**
     * 交易码
     */
    private String transcode;

    /**
     * 流水号
     */
    private String ordersn;

    /**
     * 商户号
     */
    private String subMerNo;

    /**
     * 商户订单号
     */
    private String dsorderid;

    /**
     * 流水号
     */
    private String orderid;

    /**
     * 返回码
     */
    private String returncode;

    /**
     * 返回信息
     */
    private String errtext;

    /**
     * 加密校验值
     */
    private String sign;

    /**
     * 返回数据信息
     */
    private String message;

    /**
     * 明细信息
     */
    private String messageDetail;

    /**
     * 支付金额
     */
    private String amount;

    /**
     * 支付时间
     */
    private String paytime;

    /**
     * 支付状态
     * 00 成功
     * 02 失败
     * 04 订单关闭
     * 01 处理中（半个小时后再次发起对账）
     * 99 订单号不存在
     */
    private String status;

    /**
     * 余额
     */
    private String banlanceAmount;


    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public String getTranscode() {
        return transcode;
    }

    public void setTranscode(String transcode) {
        this.transcode = transcode;
    }

    public String getOrdersn() {
        return ordersn;
    }

    public void setOrdersn(String ordersn) {
        this.ordersn = ordersn;
    }

    public String getSubMerNo() {
        return subMerNo;
    }

    public void setSubMerNo(String subMerNo) {
        this.subMerNo = subMerNo;
    }

    public String getDsorderid() {
        return dsorderid;
    }

    public void setDsorderid(String dsorderid) {
        this.dsorderid = dsorderid;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getReturncode() {
        return returncode;
    }

    public void setReturncode(String returncode) {
        this.returncode = returncode;
    }

    public String getErrtext() {
        return errtext;
    }

    public void setErrtext(String errtext) {
        this.errtext = errtext;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageDetail() {
        return messageDetail;
    }

    public void setMessageDetail(String messageDetail) {
        this.messageDetail = messageDetail;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPaytime() {
        return paytime;
    }

    public void setPaytime(String paytime) {
        this.paytime = paytime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBanlanceAmount() {
        return banlanceAmount;
    }

    public void setBanlanceAmount(String banlanceAmount) {
        this.banlanceAmount = banlanceAmount;
    }

    @Override
    public String toString() {
        return "HuanqiuResultVO{" +
                "isSuccess=" + isSuccess +
                ", transcode='" + transcode + '\'' +
                ", ordersn='" + ordersn + '\'' +
                ", subMerNo='" + subMerNo + '\'' +
                ", dsorderid='" + dsorderid + '\'' +
                ", orderid='" + orderid + '\'' +
                ", returncode='" + returncode + '\'' +
                ", errtext='" + errtext + '\'' +
                ", sign='" + sign + '\'' +
                ", message='" + message + '\'' +
                ", messageDetail='" + messageDetail + '\'' +
                ", amount='" + amount + '\'' +
                ", paytime='" + paytime + '\'' +
                ", status='" + status + '\'' +
                ", banlanceAmount='" + banlanceAmount + '\'' +
                '}';
    }
}
