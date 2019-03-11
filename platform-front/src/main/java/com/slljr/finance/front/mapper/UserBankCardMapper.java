package com.slljr.finance.front.mapper;
import com.slljr.finance.common.pojo.vo.UserBankCardDetailVo;
import com.slljr.finance.common.pojo.vo.UserBankCardVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

import com.slljr.finance.common.pojo.model.UserBankCard;

public interface UserBankCardMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserBankCard record);

    int insertSelective(UserBankCard record);

    UserBankCardVo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserBankCard record);

    int updateByPrimaryKey(UserBankCard record);
	//查询用户银行卡信息
    List<UserBankCardVo> findByUid(@Param("uid")Integer uid);

    /**
     * 根据用户ID和查询用户卡信息
     *
     * @author uncle.quentin
     * @date   2018/12/25 14:00
     * @param   userBankCard
     * @return java.util.List<com.slljr.finance.common.pojo.vo.UserBankCardVo>
     * @version 1.0
     */
    List<UserBankCardVo> findByUidAndType(UserBankCard userBankCard);

    //查找信用卡列表
    @Select({"SELECT  id,uid,bank_card_no bankCardNo,bank_id bankId, bank_card_type bankCardType, phone, opening_bank openingBank, opening_bank_province openingBankProvince, \n" + 
    		"    opening_bank_city openingBankCity, exp_date expDate, cvv_code cvvCode, bill_date billDate, last_repayment_date lastRepaymentDate, credit_limit creditLimit, \n" + 
    		"    `status`, create_time createTime, update_time updateTime FROM user_bank_card WHERE uid = #{uid} AND bank_card_type = 1"})
	List<UserBankCard> selectByCreditCardList(@Param("uid")int uid);

    //查询当前绑卡的卡是否存在
    @Select({"select count(*) from user_bank_card where uid = #{uid} AND bank_card_no= #{bank_card_no}"})
    int selectByCard(@Param("uid") Integer uid, @Param("bank_card_no") String bank_card_no);

    //修改用户银行卡信息
   	int updateByBankCard(UserBankCard userBankCard);

   	//查询银行卡是否存在
   	@Select({"select count(*) from user_bank_card where bank_card_no= #{bank_card_no}"})
	Integer selectByCardInfo(@Param("bankCardNo")String bankCardNo);

   	//删除银行卡
	void deleteCardInfo(@Param("uid") Integer uid,@Param("bankCardId") Integer bankCardId);
   	
   	//查询用户银行卡信息
	List<UserBankCard> findBankCardInfo(int uid);


    /**
     * 根据用户和银行卡号信息查询银行卡信息
     *
     * @author uncle.quentin
     * @date   2018/12/20 16:18
     * @param   uid
     * @param   bankCardNo
     * @return java.util.List<com.slljr.finance.common.pojo.model.UserBankCard>
     * @version 1.0
     */
    UserBankCard findByUidAndBankCardNo(@Param("uid")Integer uid,@Param("bankCardNo")String bankCardNo);
    //查询总代偿金额
	UserBankCardDetailVo totalCompensationAmount(UserBankCard userBankCard);
	//已还总金额
	UserBankCardDetailVo totalAmountRepaid(UserBankCard userBankCard);
	//还款中比数
	UserBankCardDetailVo numberPayments(UserBankCard record);

	//查詢信用卡列表
	List<UserBankCardDetailVo> findByUidAndInfo(UserBankCard record);




}