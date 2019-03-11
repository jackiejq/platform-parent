package com.slljr.finance.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.slljr.finance.common.exception.InterfaceException;
import com.slljr.finance.common.pojo.vo.BankValidVO;
import com.slljr.finance.common.pojo.vo.IdCardValidVO;
import com.slljr.finance.common.pojo.vo.QueryBankResultVO;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @description: 身份证、银行卡验证工具类
 * @author: uncle.quentin.
 * @date: 2018/12/20.
 * @time: 9:42.
 */
public class ValidUtil {

    private static final Logger log = LogManager.getLogger();

    /**
     * 身份证二要素校验
     *
     * @param url     url
     * @param appCode appCode
     * @param idcard  身份证号码
     * @param name    姓名
     * @return com.slljr.finance.common.pojo.vo.IdCardValidVO
     * @author uncle.quentin
     * @date 2018/12/20 9:52
     * @version 1.0
     */
    public static IdCardValidVO validIdCard(String url, String appCode, String idcard, String name) {
        Map<String, String> headerMap = Maps.newHashMap();
        headerMap.put("Authorization", "APPCODE " + appCode);
        Map<String, String> paramsMap = Maps.newHashMap();
        paramsMap.put("idcard", idcard);
        paramsMap.put("name", name);

        try {
            log.info("身份证验证入参：" + paramsMap.toString());
            String response = OKHttpUtils.getInstance().syncGetRequest(url, headerMap, paramsMap).replaceAll("\n","");
            log.info("身份证验证结果响应：" + response);

            JSONObject resultObj = JSONObject.parseObject(response);
            IdCardValidVO cardVO = JsonUtil.getObject(resultObj.get("showapi_res_body").toString(), IdCardValidVO.class);
            if (null != cardVO) {
                cardVO.setName(name);
                cardVO.setIdCard(idcard);
                return cardVO;
            }
        } catch (IOException e) {
            log.error("ValidUtil.validIdCard", e);
        }

        return null;
    }

    /**
     * 银行卡四要素校验
     *
     * @param url            url
     * @param appCode        appCode
     * @param acctName       持卡人姓名
     * @param acctPan        银行卡帐号
     * @param certId         开卡使用的证件号码
     * @param phoneNum       绑定手机号
     * @return com.slljr.finance.common.pojo.vo.BankValidVO
     * @author uncle.quentin
     * @date 2018/12/20 11:02
     * @version 1.0
     */
    public static BankValidVO validBank(String url, String appCode, String acctName, String acctPan, String certId, String phoneNum) {
        Map<String, String> headerMap = Maps.newHashMap();
        headerMap.put("Authorization", "APPCODE " + appCode);
        Map<String, String> paramsMap = Maps.newHashMap();
        paramsMap.put("acct_name", acctName);
        paramsMap.put("acct_pan", acctPan);
        paramsMap.put("cert_id", certId);
        paramsMap.put("cert_type", "01");
        paramsMap.put("needBelongArea", "true");
        paramsMap.put("phone_num", phoneNum);

        try {
        // {"showapi_res_error":"","showapi_res_id":"0b60fcaaab2140a3a544d8ce2978c41a","showapi_res_code":0,
            // "showapi_res_body":{"belong":{"cardType":"借记卡","brand":"芙蓉卡(银联卡)","cardNum":"6223687310878625490","bankName":"长沙银行股份有限公司"},"msg":"认证通过","ret_code":0,"code":0}}
            log.info("银行卡验证入参：" + paramsMap.toString());
            String response = OKHttpUtils.getInstance().syncGetRequest(url, headerMap, paramsMap);
            log.info("银行卡验证结果响应：" + response.replaceAll("\n",""));

            //返回空提示欠费
            if (StringUtils.isEmpty(response)) {
                throw new InterfaceException(MsgEnum.ARREARS);
            }

            JSONObject obj = JSONObject.parseObject(response).getJSONObject("showapi_res_body");
            if (obj.get("belong") != null){
                String belong = obj.get("belong").toString();
                //卡类型识别错误矫正
                JSONObject temp = JSON.parseObject(belong);
                if (BankValidVO.DEBIT_CARD.equals(temp.getString("cardType")) && temp.getString("brand").contains("信用")){
                    temp.put("cardType", BankValidVO.CREDIT_CARD);
                    belong = temp.toJSONString();
                    log.warn("卡片类型识别错误,校正为:信用卡");
                }

                BankValidVO bankValid = JsonUtil.getObject(belong, BankValidVO.class);
                if (null != bankValid) {
                    bankValid.setCode(obj.getString("code"));
                    bankValid.setMsg(obj.getString("msg"));
                    bankValid.setAcctName(acctName);
                    bankValid.setCertId(certId);
                    bankValid.setPhoneNum(phoneNum);
                    return bankValid;
                }
            }

        } catch (IOException e) {
            log.error("ValidUtil.validBank:", e);
        } catch (InterfaceException e) {
            log.error("ValidUtil.validBank:", e);
        }

        return null;
    }

    /**
     * 查询银行卡信息
     */
    public static QueryBankResultVO queryBankInfo(String url, String appcode, String bankCardNo) {
        Map<String, String> headerMap = new HashMap<>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headerMap.put("Authorization", "APPCODE " + appcode);
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("bankcard", bankCardNo);
        try {
            log.info("查询银行卡入参：" + paramsMap.toString());
            String response = OKHttpUtils.getInstance().syncGetRequest(url, headerMap, paramsMap);
            //{"error_code":0,"reason":"Succes",
            // "result":{"bankname":"交通银行","cardprefixnum":"622252","cardname":"太平洋人民币贷记卡","cardtype":"银联贷记卡","cardprefixlength":"6","banknum":"3010000","isLuhn":false,"iscreditcard":2,"cardlength":"16","bankurl":"http://www.bankcomm.com/","enbankname":"Bank of Communications","abbreviation":"COMM","bankimage":"http://auth.apis.la/bank/9_COMM.png","servicephone":"95559"}}
            log.info("查询银行卡结果响应：" + response);

            //返回空提示欠费
            if (StringUtils.isEmpty(response)) {
                throw new InterfaceException(MsgEnum.ARREARS);
            }

            JSONObject obj = JsonUtil.strToJson(response);
            if (obj.getString("reason").toLowerCase().contains("succes")) {
                QueryBankResultVO queryBankResult = JsonUtil.getObject(obj.get("result").toString(), QueryBankResultVO.class);
                if (bankCardNo.startsWith("40925700")){
                    //长沙银行信用卡40925700
                    queryBankResult.setCardname("信用卡");
                    queryBankResult.setBankname("长沙银行");
                    queryBankResult.setIscreditcard("2");
                }
                return queryBankResult;
            }
        } catch (InterfaceException e) {
            log.error("异常信息ValidUtil.queryBankInfo：", e);
        } catch (Exception e) {
            log.error("异常信息ValidUtil.queryBankInfo：", e);
        }
        return null;
    }
        
}
