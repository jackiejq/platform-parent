package com.slljr.finance.front.controller;

import com.alibaba.fastjson.JSONObject;
import com.slljr.finance.common.exception.InterfaceException;
import com.slljr.finance.common.pojo.vo.*;
import com.slljr.finance.common.utils.JsonUtil;
import com.slljr.finance.common.utils.ValidUtil;
import com.slljr.finance.common.utils.WriteJson;
import com.slljr.finance.front.mapper.IdcardAddrMapper;
import com.slljr.finance.front.service.UserBasicService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @description: 银行卡、身份证验证API
 * @author: uncle.quentin.
 * @date: 2018/12/20.
 * @time: 11:28.
 */
@RestController
@RequestMapping(value = "/valid")
@Api(tags = {"银行卡/身份证验证API"})
public class ValidController extends BaseController {
    private static final Logger log = LogManager.getLogger();
    /**
     * 阿里银行卡验证URL
     */
    @Value("${ali.bankvalid.url}")
    private String validBankUrl;

    /**
     * 阿里银行卡验证APPCODE
     */
    @Value("${ali.bankvalid.appcode}")
    private String validBankAppCode;
    /**
     * 阿里身份信息验证URL
     */
    @Value("${ali.idcardvalid.url}")
    private String validIdCardUrl;
    /**
     * 阿里身份信息验证APPCODE
     */
    @Value("${ali.idcardvalid.appcode}")
    private String validIdCardAppCode;

    /**
     * 阿里银行卡信息查询接口URL
     */
    @Value("${ali.bankquery.url}")
    private String bankQueryUrl = "http://aliyun.apistore.cn/7";
    /**
     * 阿里银行卡信息查询接口qppCode
     */
    @Value("${ali.bankquery.appcode}")
    private String bankQueryAppcode = "f38a9f51164c43dd989bb6eda031ec78";


    /**
     * 用户服务接口
     */
    @Autowired
    private UserBasicService userBasicService;
    @Autowired
    private IdcardAddrMapper idcardAddrMapper;

    /**
     * 银行卡四要素校验
     *
     * @param param
     * @return org.springframework.ui.ModelMap
     * @author uncle.quentin
     * @date 2018/12/20 11:30
     * @version 1.0
     */
    @RequestMapping(value = "/validBank", method = RequestMethod.POST)
    @ApiOperation(httpMethod = "POST", value = "银行卡四要素校验", notes = "{\"acctName\":\"持卡人姓名\",\"acctPan\":\"银行卡号\",\"certId\":\"身份证号码\",\"phoneNum\":\"银行预留手机号\"}")
    @ResponseBody
    public ModelMap validBank(@RequestBody String param) throws InterfaceException {
        //解析参数
        JSONObject obj = JsonUtil.strToJson(param);
        String acctName = obj.getString("acctName");
        String acctPan = obj.getString("acctPan");
        String certId = obj.getString("certId");
        String phoneNum = obj.getString("phoneNum");
        //参数校验
        validParam(acctName, acctPan, certId, phoneNum);

        log.info(String.format("ValidController.validBank参数acctName:%s;参数acctPan:%s;参数certId:%s;参数phoneNum:%s", acctName, acctPan, certId, phoneNum));

        BankValidVO resultVo = ValidUtil.validBank(validBankUrl, validBankAppCode, acctName, acctPan, certId, phoneNum);
        if (null != resultVo) {
            if (0 == Integer.valueOf(resultVo.getCode())) {
                return WriteJson.successData(resultVo);
            } else {
                return WriteJson.errorWebMsg("验证失败:" + resultVo.getMsg());
            }
        } else {
            return WriteJson.errorWebMsg("验证失败");
        }

    }

    /**
     * 身份证二要素校验
     *
     * @param param
     * @return org.springframework.ui.ModelMap
     * @author uncle.quentin
     * @date 2018/12/20 11:52
     * @version 1.0
     */
    @RequestMapping(value = "/validIdCard", method = RequestMethod.POST)
    @ApiOperation(httpMethod = "POST", value = "身份证二要素校验", notes = "{\"idcard\":\"身份证号码\",\"name\":\"姓名\",\"idcImgUrl1\":\"身份证正面URL\",\"idcImgUrl2\":\"身份证反面URL\"}")
    @ResponseBody
    public ModelMap validIdCard(@RequestBody String param, HttpServletRequest request) throws InterfaceException {
        //解析参数
        JSONObject obj = JsonUtil.strToJson(param);
        String idcard = obj.getString("idcard");
        String name = obj.getString("name");
        String idcImgUrl1 = obj.getString("idcImgUrl1");
        String idcImgUrl2 = obj.getString("idcImgUrl2");

        log.info(String.format("ValidController.validIdCard参数idcard:%s;参数name:%s", idcard, name));

        //参数校验
        validParam(idcard, name);

        //执行校验
        IdCardValidVO resultVo = ValidUtil.validIdCard(validIdCardUrl, validIdCardAppCode, idcard, name);

        if (null != resultVo) {
            if (0 == Integer.valueOf(resultVo.getCode())) {
                //认证成功更新用户信息
                //获取登录用户信息
                UserBasicVO user = getLoginUser(request);
                user.setIdcImgUrl1(idcImgUrl1);
                user.setIdcImgUrl2(idcImgUrl2);

                String cityCode = StringUtils.substring(resultVo.getIdCard(),0,6);
                IdcardAddrVO idcardAddr = idcardAddrMapper.findByCode(cityCode);
                if (idcardAddr != null){
                    if(StringUtils.isNotBlank(idcardAddr.getProvinceName()))user.setIdcProvince(idcardAddr.getProvinceName());
                    if (StringUtils.isNotBlank(idcardAddr.getCityName()))user.setIdcCity(idcardAddr.getCityName());
                }

                UserBasicVO userBasic = userBasicService.updateUserAfterValid(user, resultVo);
                if (null != userBasic) {
                    //重新获取一遍数据
                    UserBasicVO userBasicRefresh = userBasicService.selectUserBasicById(user.getId());
                    //更新用户缓存信息
                    updateUserCache(request, userBasicRefresh);
                    return WriteJson.successData(userBasic);
                } else {
                    return WriteJson.errorWebMsg("绑定用户信息失败");
                }
            } else {
                return WriteJson.errorWebMsg("验证失败:" + resultVo.getMsg());
            }
        } else {
            return WriteJson.errorWebMsg("验证失败");
        }

    }

    /**
     * 银行卡信息查询
     */
    @RequestMapping(value = "/queryBankInfo", method = RequestMethod.POST)
    @ApiOperation(httpMethod = "POST", value = "银行卡信息查询", notes = " {\"bankCardNo\":\"银行卡号\"}")
    @ResponseBody
    public ModelMap queryBankInfo(@RequestBody String param) throws InterfaceException {
        //解析参数
        JSONObject obj = JsonUtil.strToJson(param);
        String bankCardNo = (String) obj.get("bankCardNo");
        //校验参数
        validParam(bankCardNo);

        log.info(String.format("ValidController.queryBankInfo参数bankCardNo:%s", bankCardNo));

        try {
            QueryBankResultVO bankCardNoInfo = ValidUtil.queryBankInfo(bankQueryUrl, bankQueryAppcode, bankCardNo);
            if (null != bankCardNoInfo) {
                return WriteJson.successData(bankCardNoInfo);
            } else {
                return WriteJson.errorWebMsg("查询银行卡信息失败");
            }
        } catch (Exception e) {
            return WriteJson.errorWebMsg("查询银行卡信息失败");
        }
    }

}
