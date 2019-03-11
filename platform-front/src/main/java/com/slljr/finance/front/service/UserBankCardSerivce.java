package com.slljr.finance.front.service;

import com.slljr.finance.common.enums.BankCardTypeEnum;
import com.slljr.finance.common.enums.TradeOrderStatusEnum;
import com.slljr.finance.common.exception.InterfaceException;
import com.slljr.finance.common.pojo.dto.UserBankCardDTO;
import com.slljr.finance.common.pojo.model.Bank;
import com.slljr.finance.common.pojo.model.UserBankCard;
import com.slljr.finance.common.pojo.model.UserBasic;
import com.slljr.finance.common.pojo.vo.BankValidVO;
import com.slljr.finance.common.pojo.vo.UserBankCardDetailVo;
import com.slljr.finance.common.pojo.vo.UserBankCardVo;
import com.slljr.finance.common.utils.HideDataUtil;
import com.slljr.finance.common.utils.MsgEnum;
import com.slljr.finance.common.utils.ValidUtil;
import com.slljr.finance.front.mapper.BankMapper;
import com.slljr.finance.front.mapper.UserBankCardMapper;
import com.slljr.finance.front.mapper.UserTradeOrderMapper;
import com.slljr.finance.front.mapper.UserTradePaymentRecordMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 用户银行卡的业务逻辑层实现类
 * @author LanXinKui
 *
 */
@Service
public class UserBankCardSerivce {

	private static final Logger log = LogManager.getLogger();

	@Autowired
	private UserBankCardMapper userBankCardMapper;

	@Autowired
	private UserTradePaymentRecordMapper userTradePaymentRecordMapper;

	@Autowired
	private UserTradeOrderMapper userTradeOrderMapper;

	@Autowired
	private BankMapper bankMapper;

	/**
	 *RSA私公钥加密
	 */
	@Value("${app.security.publicKey}")
	private String publicKey;

	@Value("${app.security.privateKey}")
	private String privateKey;

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

	@Autowired
	private SmsService smsService;

	@Autowired
	private RsaService rsaService;

	/**
	 * 1、添加/修改用户银行卡
	 */
	
	public int addBankCard(UserBankCard userBankCard) {
		Integer uid = userBankCard.getUid();
		int rows = 0;
		String bank_card_no = userBankCard.getBankCardNo();
		if(uid == null || bank_card_no.isEmpty()) {
			return rows>1?0:null;
		}
		try {
			int num =userBankCardMapper.selectByCard(uid,bank_card_no);
			//状态[-1删除,0正常]
			userBankCard.setStatus(0);
			//userBankCard.setBankCardType(2);
			userBankCard.setUpdateTime(new Date());
			if(num == 0) {
				userBankCard.setCreateTime(new Date());
				rows = userBankCardMapper.insertSelective(userBankCard);
			}else {
				rows = userBankCardMapper.updateByBankCard(userBankCard);
			}
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 1、修改用户银行卡信息
	 */
	
	@Transactional(rollbackFor = Exception.class)
	public int updateBankCard(UserBankCardDTO userBankCard, UserBasic userBasic) throws InterfaceException {
		int successCount = 0;
		if (StringUtils.isNotEmpty(userBankCard.getPhone())) {
			//校验验证码
			boolean validResult = smsService.checkIsCorrectCode(userBankCard.getPhone(), userBankCard.getSmsCode(), 2);
			if (validResult) {
				//修改卡片手机号码校验四要素信息
				UserBankCardVo userBankCardVo = userBankCardMapper.selectByPrimaryKey(userBankCard.getId());
				if (null != userBankCardVo) {
					//解密银行卡号
					String cardNo = rsaService.decode(userBankCardVo.getBankCardNoSign());
					BankValidVO resultVo = ValidUtil.validBank(validBankUrl, validBankAppCode, userBasic.getName(), cardNo, userBasic.getIdCard(), userBankCard.getPhone());
					if (null != resultVo) {
						if (0 == Integer.valueOf(resultVo.getCode())) {
							successCount = userBankCardMapper.updateByPrimaryKeySelective(userBankCard);
						}
					} else {
						throw new InterfaceException(MsgEnum.CARD_VALID_FAIL);
					}
				}
			} else {
				throw new InterfaceException(MsgEnum.VERIFICATIONCODEERROR);
			}
		} else {
			successCount = userBankCardMapper.updateByPrimaryKeySelective(userBankCard);
		}

		return successCount;
	}

	/**
	 * 修改最后还款日
	 * @param cardId
	 * @param lastPaymentDate
	 * @throws InterfaceException
	 */
	public void updateLastPaymentDate(Integer cardId, Integer lastPaymentDate) throws InterfaceException{
		UserBankCard uptCard = new UserBankCard();
		uptCard.setId(cardId);
		uptCard.setLastRepaymentDate(String.valueOf(lastPaymentDate));
		uptCard.setUpdateTime(new Date());
		userBankCardMapper.updateByPrimaryKeySelective(uptCard);
	}


	/**
	 * 用户银行卡本月已收款查询
	 */
	
	public Double findBankCardReceivable(int uid) {
		Double getAmount = userTradePaymentRecordMapper.findByGetAmount(uid);
		return getAmount;
	}

	/**
	 *  用户银行卡累计收款查询
	 */
	
	public Double findBankCardAccumulatedReceipts(int uid) {
		Double getAmountCount = userTradePaymentRecordMapper.findBygetAmountCount(uid);
		return getAmountCount;
	}

	
	public Integer findByCard(Integer uid) {
		/*Integer num = userBankCardMapper.selectByCard(uid);
		return num;*/
		return null;
	}

	/**
	 * 修改/增加信用卡
	 */
	
	public int addCreditCard(UserBankCard userBankCard) {
		Integer uid = userBankCard.getUid();
		String bank_card_no = userBankCard.getBankCardNo();
		int rows = 0;
		if(uid == null || bank_card_no.isEmpty()) {
			return rows>1?0:null;
		}
		try {
			int num =userBankCardMapper.selectByCard(uid,bank_card_no);
			//状态[-1删除,0正常]
			userBankCard.setStatus(0);
			userBankCard.setBankCardType(1);
			userBankCard.setUpdateTime(new Date());
			if(num == 0) {
				userBankCard.setCreateTime(new Date());
				rows = userBankCardMapper.insertSelective(userBankCard);
			}else {
				rows = userBankCardMapper.updateByBankCard(userBankCard);
			}
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 *查询用户银行卡信息
	 * @param uid
	 * @return
	 */
	
	public List<UserBankCardVo> findByUid(Integer uid) {
		return userBankCardMapper.findByUid(uid);
	}

	
	public List<UserBankCardVo> findByUidAndType(Integer uid, Integer type) {
		UserBankCard record = new UserBankCard();
		record.setUid(uid);
		record.setBankCardType(type);

		return userBankCardMapper.findByUidAndType(record);
	}

	
	public int findCardInfo(String bankCardNo) {
		Integer num = userBankCardMapper.selectByCardInfo(bankCardNo);
		return num;
	}

	/**
	 * 删除银行卡信息
	 */
	
	public void deleteCardInfo(Integer uid, Integer bankCardId) {
		userBankCardMapper.deleteCardInfo(uid,bankCardId);
	}

	
	public List<UserBankCard> findBankCardInfo(int uid) {
		return userBankCardMapper.findBankCardInfo(uid);
	}

	
	public UserBankCard selectCardDetailByUidAndCardNo(Integer uid, String cardNo) {
		return userBankCardMapper.findByUidAndBankCardNo(uid, cardNo);
	}

	
	public int insertBankCard(UserBankCard userBankCard) {
		return userBankCardMapper.insertSelective(userBankCard);
	}

	
	public int addOrUpdateUserBankCard(Integer uid, BankValidVO bankValidVO, UserBankCardDTO userBankCard) throws InterfaceException {
		int successCount;
		userBankCard.setUid(uid);

		String bankCard = bankValidVO.getCardNum();

		//隐藏后的明文, 显示前6位,中间*号, 后4位,
		String bankCardNo = HideDataUtil.hidebankCard(bankCard);
		userBankCard.setBankCardNo(bankCardNo);

		//获取用户银行卡信息
		UserBankCard existUserBankCard = userBankCardMapper.findByUidAndBankCardNo(uid, userBankCard.getBankCardNo());

		//借记卡或信用卡  !!!!!!!银行卡四要素校验接口返回银行卡类型有误!信用卡有卡能识别成储蓄卡
		if (BankValidVO.DEBIT_CARD.equals(bankValidVO.getCardType())) {
			userBankCard.setBankCardType(BankCardTypeEnum.DEPOSIT_CARD.getKey());
		} else {
			userBankCard.setBankCardType(BankCardTypeEnum.CREADIT_CARD.getKey());
		}
		//预留手机号码
		userBankCard.setPhone(bankValidVO.getPhoneNum());
		//开户行
		userBankCard.setOpeningBank(bankValidVO.getBankName());
		//区域（省市）
		if (null != bankValidVO.getArea()) {
			String[] strs = bankValidVO.getArea().split("-");
			if (null != strs && strs.length > 0) {
				userBankCard.setOpeningBankProvince(StringUtils.isEmpty(strs[0].trim()) ? null : strs[0].trim());
				userBankCard.setOpeningBankCity(StringUtils.isEmpty(strs[1].trim()) ? null : strs[1].trim());
			}
		}
		//获取银行ID
		Bank bank = bankMapper.findByCodeOrName(userBankCard.getBankCode(), userBankCard.getBankName());
		if (null != bank) {
			userBankCard.setBankId(bank.getId());
		} else {
			userBankCard.setBankId(0);
		}

		userBankCard.setStatus(0);
		//存在更新、不存在添加
		if (null == existUserBankCard) {
			try {
				successCount = userBankCardMapper.insertSelective(userBankCard);
			} catch (Exception e) {
				log.error("新增卡片失败：", e);
				throw new InterfaceException(MsgEnum.CARD_IS_BINDING);
			}
		} else {
			userBankCard.setId(existUserBankCard.getId());
			successCount = userBankCardMapper.updateByPrimaryKeySelective(userBankCard);
		}
		return successCount;
	}


	
	public List<UserBankCardDetailVo> queryCreditCardInfo(Integer uid) {
		long startTime = System.currentTimeMillis();

		UserBankCard record = new UserBankCard();
		record.setUid(uid);
		record.setBankCardType(BankCardTypeEnum.CREADIT_CARD.getKey());
		//查询用户下信用卡
		List<UserBankCardDetailVo> creditCards = userBankCardMapper.findByUidAndInfo(record);

		for (UserBankCardDetailVo detailVo : creditCards) {
			//查询当前用户当前卡信息
			UserBankCard searchParam = new UserBankCard();
			searchParam.setUid(uid);
			searchParam.setId(detailVo.getId());
			//还款中笔数
			UserBankCardDetailVo ub = userBankCardMapper.numberPayments(searchParam);
			detailVo.setNumberPayments(ub.getNumberPayments());
			//查询总代偿金额
			UserBankCardDetailVo ubc = userBankCardMapper.totalCompensationAmount(searchParam);
			detailVo.setTotalCompensationAmount(ubc.getTotalCompensationAmount());
			//已还总金额
			UserBankCardDetailVo ubcd = userBankCardMapper.totalAmountRepaid(searchParam);
			detailVo.setTotalAmountRepaid(ubcd.getTotalAmountRepaid());
			//失败笔数
			int failCount = userTradeOrderMapper.countFailOrderByUidAndPaymentCardId(uid, detailVo.getId());
			detailVo.setFailCount(failCount);
		}

		long endTime = System.currentTimeMillis();

		log.info("====UserBankCardSerivceImpl.queryCreditCardInfo统计耗时(毫秒)===={}", endTime - startTime);

		return creditCards;
	}

	/**
	 * @Title: mergeListMap
	 * @Description: 合并List<Map>
	 * @param mapList1
	 * @param mapList2
	 * @param ID 公共ID部分
	 * @return 参数说明
	 * @return List<Map<String,Object>>    返回类型
	 */
	public static List<Map<String, Object>> mergeListMap(List<Map<String, Object>> mapList1, List<Map<String, Object>> mapList2,String ID) {
		Map<String, Map<String, Object>> tempMap = new HashMap<>();
		/**将mapList1分解为map**/
		for (Map<String, Object> map : mapList1) {
			tempMap.put(map.get(ID).toString(), map);
		}
		/**将mapList2进行拆解**/
		for (Map<String, Object> map : mapList2) {
			/**和tempMap中相同的ID**/
			String sameID = map.get(ID).toString();
			Map<String, Object> secondMap = tempMap.get(sameID);
			/**合并**/
			secondMap.putAll(map);
		}
		return new ArrayList<Map<String, Object>>(tempMap.values());
	}


	
	public UserBankCardVo selectBankCardById(Integer id) {
		return userBankCardMapper.selectByPrimaryKey(id);
	}

	
	public int updateBankCardNotVerify(UserBankCard userBankCard) {
		return userBankCardMapper.updateByPrimaryKeySelective(userBankCard);
	}


}
