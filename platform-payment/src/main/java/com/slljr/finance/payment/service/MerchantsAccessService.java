package com.slljr.finance.payment.service;

public interface MerchantsAccessService {

    String dhRegister(String merchantNo, String version, String tranSerialNum, String userName,
                      String certificateType, String certificateNum);

    String dhBind(String merchantNo, String version, String tranSerialNum, String userId, String cardNum, String bankAgentId, String userName, String certificateType, String certificateNum, String mobile, String notifyUrl, String returnUrl);

    String dhUnBind(String merchantNo, String version, String tranSerialNum, String userId, String bindId, String cardNum);

    String dhDeductPay(String merchantNo, String version, String tranSerialNum, String bindId, String amount, String fee, String notifyUrl, String userId, String province, String riskInfo, String deviceID, String deviceType, String sourceIP, String accountIdHash, String mobile, String deviceLocation, String fullDeviceNumber, String deviceSimNumber, String usrRgstrDt, String deviceName);

    String dhRepay(String merchantNo, String version, String tranSerialNum, String bindId, String amount, String fee, String userId);

    String dhDeductQuery(String merchantNo, String version, String tranSerialNum);

    String dhRepayQuery(String merchantNo, String version, String tranSerialNum);

    String dhQueryBalance(String merchantNo, String version, String userId);


}
