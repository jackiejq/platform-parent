package com.slljr.finance.front.controller;

import com.alibaba.fastjson.JSONObject;
import com.slljr.finance.common.enums.BankCardTypeEnum;
import com.slljr.finance.common.exception.InterfaceException;
import com.slljr.finance.common.pojo.dto.UserBankCardDTO;
import com.slljr.finance.common.pojo.model.UserBankCard;
import com.slljr.finance.common.pojo.vo.BankValidVO;
import com.slljr.finance.common.pojo.vo.UserBankCardDetailVo;
import com.slljr.finance.common.pojo.vo.UserBankCardVo;
import com.slljr.finance.common.pojo.vo.UserBasicVO;
import com.slljr.finance.common.utils.JsonUtil;
import com.slljr.finance.common.utils.MsgEnum;
import com.slljr.finance.common.utils.ValidUtil;
import com.slljr.finance.common.utils.WriteJson;
import com.slljr.finance.front.service.RsaService;
import com.slljr.finance.front.service.UserBankCardSerivce;
import com.slljr.finance.front.service.UserBasicService;
import com.slljr.finance.front.service.UserTradeOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户银行卡控制访问层
 * @author LanXinKui
 *
 */
@RestController
@Api(tags={"用户银行卡/信用卡操作API"})
@RequestMapping(value="/userBankCard")
@CrossOrigin
public class UserBankCardController extends BaseController{

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
	 *RSA私钥解密	
	 */	
	@Value("${app.security.privateKey}")
	private String privateKey;
	
	@Autowired
	private UserBankCardSerivce userBankCardSerivce;

	@Autowired
	private UserTradeOrderService userTradeOrderService;

	@Autowired
	private UserBasicService userBasicService;

	@Autowired
	private RsaService rsaService;

	private static final Logger log = LogManager.getLogger();
	

	/**
	 * 删除银行卡
	 */
	@RequestMapping(value="/deleteCardInfo",method=RequestMethod.POST)
	@ApiOperation(httpMethod = "POST", value = "删除银行卡",notes="{'id':'银行卡id'}")
	public ModelMap deleteCardInfo(@RequestBody UserBankCard bankCard,HttpServletRequest request) throws InterfaceException {
		//获取登录用户信息
		UserBasicVO user = getLoginUser(request);
		//校验参数有效性
		validParam(bankCard,bankCard.getId());
		try {
			userBankCardSerivce.deleteCardInfo(user.getId(), bankCard.getId());
			return WriteJson.success();
		} catch (Exception e) {			
			return WriteJson.errorWebMsg("删除银行卡失败");
		}			
	}
	
	
	/**
	 * 查询银行卡号是否存在
	 */
	@RequestMapping(value="/findCardInfo",method=RequestMethod.POST)
	@ApiOperation(httpMethod = "POST", value = "查找卡号是否存在",notes="'bankCardNo':'银行卡号' ")
	public ModelMap findCardInfo(@RequestBody String param) throws InterfaceException {
		String bankCardNo = JsonUtil.getJson(param);
		//校验参数有效性
		validParam(bankCardNo);

		int num = userBankCardSerivce.findCardInfo(bankCardNo);
		if(num == 1) {
			return WriteJson.success();
		}
		return WriteJson.errorWebMsg("查询无此银行卡数据");
	}

	/**
	 * 校验银行卡信息、并且添加或者修改银行卡信息
	 *
	 * @author uncle.quentin
	 * @date   2018/12/24 15:53
	 * @param   user
	 * @param   userBankCard
	 * @return org.springframework.ui.ModelMap
	 * @version 1.0
	 */
	private ModelMap saveOrUpdateBankCard(UserBasicVO user, UserBankCardDTO userBankCard) throws InterfaceException {
		try {
			//设置未解密数据
			userBankCard.setBankCardNoSign(userBankCard.getBankCardNo());
			//解密银行卡账号信息
			String bankCardNo = rsaService.decode(userBankCard.getBankCardNo());
			userBankCard.setBankCardNo(bankCardNo);
			//校验银行卡信息是否正确
			BankValidVO resultVo = ValidUtil.validBank(validBankUrl, validBankAppCode, user.getName(), userBankCard.getBankCardNo(), user.getIdCard(), userBankCard.getPhone());
			if (null != resultVo) {
				if (0 == Integer.valueOf(resultVo.getCode())) {
					int successCount = userBankCardSerivce.addOrUpdateUserBankCard(user.getId(), resultVo, userBankCard);
					if (successCount > 0) {
						//成功返回最新用户信息（是否绑定信用卡状态）
						UserBasicVO userBasic = userBasicService.selectPortionUserBasicById(user.getId());

						Map resMap = new HashMap();
						resMap.put("userInfo", userBasic);
						resMap.put("cardInfo", userBankCard);
						return WriteJson.successData(resMap);
					} else {
						return WriteJson.errorWebMsg("绑定失败");
					}
				}else {
					return WriteJson.errorWebMsg("验证失败:" + resultVo.getMsg());
				}
			} else {
                log.info(String.format("卡片验证失败：姓名：%s;卡号：%s;身份证：%s;手机号：%s;", user.getName(), userBankCard.getBankCardNo(), user.getIdCard(), userBankCard.getPhone()));
				return WriteJson.errorWebMsg("验证失败");
			}
		} catch (InterfaceException e) {
			log.error("UserBankCardController.saveOrUpdateBankCard添加失败:", e);
			throw new InterfaceException(MsgEnum.FAIL);
		}
	}
	
	/**
	 * 添加用户信用卡
	 * 
	 * @author uncle.quentin
	 * @date   2018/12/24 15:49
	 * @param   userBankCard
	 * @param   request
	 * @return org.springframework.ui.ModelMap
	 * @version 1.0
	 */
	@RequestMapping(value="/addCreditCard",method=RequestMethod.POST)
	@ApiOperation(httpMethod = "POST", value = "添加用户信用卡信息",notes="{\"bankCardNo\":\"银行卡号\",\"phone\":\"预留手机号码\" ," +
			"\"bankCode\":\"银行编号\" ,\"bankName\":\"银行名称\" ,\"imgUrl\":\"银行卡照片url\" ,\"expDate\":\"信用卡有效期MMyy\",\"cvvCode\":\"信用卡安全码\"," +
			"\"billDate\":\"账单日dd\",\"lastRepaymentDate\":\"最后还款日\"}")
	public ModelMap addCreditCard(@RequestBody UserBankCardDTO userBankCard,HttpServletRequest request) throws InterfaceException {
		//校验参数
		validParam(userBankCard);
		log.info("添加信用卡接口入参：" + JsonUtil.getJson(userBankCard));
		//获取登录用户信息
		UserBasicVO user = getLoginUser(request);

		userBankCard.setUid(user.getId());
		//添加银行卡（1信用卡）
		userBankCard.setBankCardType(1);
		//校验银行卡信息、并且添加或者修改银行卡信息
		return saveOrUpdateBankCard(user, userBankCard);
	}

	/**
	 * 添加用户银行卡
	 *
	 * @author uncle.quentin
	 * @date   2018/12/24 15:49
	 * @param   userBankCard
	 * @param   request
	 * @return org.springframework.ui.ModelMap
	 * @version 1.0
	 */
	@RequestMapping(value = "/addBankCard", method = RequestMethod.POST)
	@ApiOperation(httpMethod = "POST", value = "添加用户银行卡信息", notes = " {\"bankCardNo\":\"银行卡号\",\"phone\":\"预留手机号码\"" +
			",\"bankCode\":\"银行编号\",\"bankName\":\"银行名称\",\"imgUrl\":\"银行卡照片url\"}")
	public ModelMap addBankCard(@RequestBody UserBankCardDTO userBankCard, HttpServletRequest request) throws InterfaceException {
		//校验参数
		validParam(userBankCard);
		log.info("添加银行卡接口入参：" + JsonUtil.getJson(userBankCard));

		//获取登录用户信息
		UserBasicVO user = getLoginUser(request);

		userBankCard.setUid(user.getId());
		//添加银行卡（2银行卡）
        userBankCard.setBankCardType(2);
        //校验银行卡信息、并且添加或者修改银行卡信息
		return saveOrUpdateBankCard(user, userBankCard);
	}

	/**
	 * 修改银行卡、信用卡
	 *
	 * @param userBankCard
	 * @param request
	 * @return org.springframework.ui.ModelMap
	 * @author uncle.quentin
	 * @date 2019/1/8 11:33
	 * @version 1.0
	 */
	@RequestMapping(value = "/updateBankCard", method = RequestMethod.POST)
	@ApiOperation(httpMethod = "POST", value = "修改银行卡信息", notes = "{\"id\":\"银行卡id\",\"phone\":\"预留手机号码\",\"billDate\":\"账单日\",\"lastRepaymentDate\":\"还款日\",\"creditLimit\":\"额度\",\"smsCode\":\"短信验证码\"}")
	public ModelMap updateBankCard(@RequestBody UserBankCardDTO userBankCard, HttpServletRequest request) throws InterfaceException {
		//校验参数
		validParam(userBankCard,userBankCard.getId());
		log.info("修改卡片信息接口入参：" + JsonUtil.getJson(userBankCard));
		//获取登录用户信息
		UserBasicVO user = getLoginUser(request);
		userBankCard.setUid(user.getId());
		//验证并修改卡片
		int successCount = userBankCardSerivce.updateBankCard(userBankCard, user);

		if (successCount > 0) {
			return WriteJson.success();
		} else {
			return WriteJson.errorWebMsg("修改失败");
		}
	}
	
	/**
	 * 1、查找用户银行卡信息
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/listUserBank", method = RequestMethod.POST)
    @ApiOperation(value = "查询用户已绑卡列表", httpMethod = "POST")
    public ModelMap listUserBank(HttpServletRequest request) throws InterfaceException {
		//获取登录用户信息
		UserBasicVO user = getLoginUser(request);
		//返回
		//查询银行卡
		List<UserBankCardVo> depositCardList = userBankCardSerivce.findByUidAndType(user.getId(),BankCardTypeEnum.DEPOSIT_CARD.getKey());
		//查询信用卡
		List<UserBankCardVo> creaditCardList = userBankCardSerivce.findByUidAndType(user.getId(),BankCardTypeEnum.CREADIT_CARD.getKey());
		
		//返回
		JSONObject resultObj = new JSONObject();
		resultObj.put("depositCardList", depositCardList);
		resultObj.put("creaditCardList", creaditCardList);

		//本月已收款、累计收款
		JSONObject receiptObj = new JSONObject();
		Double getAmount = userBankCardSerivce.findBankCardReceivable(user.getId());
		Double payServiceCharge3 = serviceChargeFormat(getAmount);
		Double getAmountCount = userBankCardSerivce.findBankCardAccumulatedReceipts(user.getId());
		Double payServiceCharge4 = serviceChargeFormat(getAmountCount);
		receiptObj.put("thisMonthAmount", payServiceCharge3);
		receiptObj.put("totalAmount", payServiceCharge4);
		resultObj.put("receiveAmount", receiptObj);


		//本月剩余代偿、累计代偿
		JSONObject compensationObj = new JSONObject();
		Double surplusAmount = userTradeOrderService.findSurplusAmount(user.getId());
		Double payServiceCharge = serviceChargeFormat(surplusAmount);
		compensationObj.put("thisMonthAmount", payServiceCharge);
		Double totalCompensationAmount = userTradeOrderService.findTotalCompensationAmount(user.getId());
		Double payServiceCharge1 = serviceChargeFormat(totalCompensationAmount);
		compensationObj.put("totalAmount", payServiceCharge1);
		resultObj.put("compensationAmount", compensationObj);

		//更新用户信息
		UserBasicVO userBasic = userBasicService.selectPortionUserBasicById(user.getId());
		resultObj.put("userInfo", userBasic);

        return WriteJson.successData(resultObj);
    }
		

	private double serviceChargeFormat(Double surplusAmount){
        return new BigDecimal(surplusAmount).setScale(2, BigDecimal.ROUND_UP).doubleValue();
    }

	

	/**
	 * 用户银行卡本月已收款查询
	 */
	@RequestMapping(value="/findBankCardReceivable",method=RequestMethod.POST)
	@ApiOperation(httpMethod = "POST", value = "查找银行卡本月已收款")
	public ModelMap findBankCardReceivable(HttpServletRequest request) throws InterfaceException {
		//获取登录用户信息
		UserBasicVO user = getLoginUser(request);

		Double getAmount = userBankCardSerivce.findBankCardReceivable(user.getId());

		return WriteJson.successData(getAmount);
	}
	
	/**
	 * 用户银行卡累计收款查询
	 */
	@RequestMapping(value="/findBankCardAccumulatedReceipts",method=RequestMethod.POST)
	@ApiOperation(httpMethod = "POST", value = "查找用户银行卡累计收款")
	public ModelMap findBankCardAccumulatedReceipts(HttpServletRequest request) throws InterfaceException {
		//获取登录用户信息
		UserBasicVO user = getLoginUser(request);

		Double getAmountCount = userBankCardSerivce.findBankCardAccumulatedReceipts(user.getId());

		return WriteJson.successData(getAmountCount);
	}
	
	/**
	 * 查询用户银行卡信息
	 */
	@RequestMapping(value="/findBankCardInfo",method=RequestMethod.POST)
	@ApiOperation(httpMethod = "POST", value = "查询用户银行卡信息")
	public ModelMap findBankCardInfo(HttpServletRequest request) throws InterfaceException {
		//获取登录用户信息
		UserBasicVO user = getLoginUser(request);
		//查询银行卡
		List<UserBankCardVo> depositCardList = userBankCardSerivce.findByUidAndType(user.getId(),BankCardTypeEnum.DEPOSIT_CARD.getKey());
		//查询信用卡
		List<UserBankCardVo> creaditCardList = userBankCardSerivce.findByUidAndType(user.getId(),BankCardTypeEnum.CREADIT_CARD.getKey());

		//返回
		JSONObject resultObj = new JSONObject();
		resultObj.put("depositCardList", depositCardList);
		resultObj.put("creaditCardList", creaditCardList);

		return WriteJson.successData(resultObj);
	}

	/*
	 * 查询信用卡卡列表显示还款中笔数,总代偿金额、已还总金额,还款进度
	 */
	@RequestMapping(value="/queryCreditCardInfo",method=RequestMethod.POST)
	@ApiOperation(httpMethod = "POST", value = "查询信用卡卡列表还款进度等信息")
	public ModelMap queryCreditCardInfo(HttpServletRequest request) throws InterfaceException {
		//获取登录用户信息
		UserBasicVO user = getLoginUser(request);
		
		List<UserBankCardDetailVo> ubcd= userBankCardSerivce.queryCreditCardInfo(user.getId());
		return WriteJson.successData(ubcd);
	}


	/**
	 * 修改信用卡账单日
	 *
	 * @param userBankCard
	 * @param request
	 * @return org.springframework.ui.ModelMap
	 * @author uncle.quentin
	 * @date 2019/1/15 18:33
	 * @version 1.0
	 */
	@RequestMapping(value = "/updateBillingDay", method = RequestMethod.POST)
	@ApiOperation(httpMethod = "POST", value = "修改信用卡账单日", notes = "{\"id\":\"银行卡id\",\"billDate\":\"账单日\"}")
	public ModelMap updateBillingDay(@RequestBody UserBankCard userBankCard, HttpServletRequest request) throws InterfaceException {
		//校验参数有效性
		validParam(userBankCard, userBankCard.getId());
		//获取当前用户
		UserBasicVO user = getLoginUser(request);
		//获取卡信息
		UserBankCardVo userBankCardVo = userBankCardSerivce.selectBankCardById(userBankCard.getId());
		if (null != userBankCardVo) {
			if (user.getId().equals(userBankCardVo.getUid())) {
				userBankCardVo.setBillDate(userBankCard.getBillDate());
				int successCount = userBankCardSerivce.updateBankCardNotVerify(userBankCardVo);
				if (successCount > 0) {
					return WriteJson.success();
				} else {
					return WriteJson.errorWebMsg(MsgEnum.FAIL);
				}
			} else {
				return WriteJson.errorWebMsg(MsgEnum.NOT_ALLOWED_UPDATE);
			}
		} else {
			return WriteJson.errorWebMsg(MsgEnum.CARD_NOT_EXIST);
		}
	}
	
}
