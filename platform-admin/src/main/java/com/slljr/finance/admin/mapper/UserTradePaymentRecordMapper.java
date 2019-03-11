package com.slljr.finance.admin.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.slljr.finance.common.pojo.model.UserTradePaymentRecord;
import com.slljr.finance.common.pojo.model.UserTradePaymentRecordExample;
import com.slljr.finance.common.pojo.vo.UserTradePayDetailsVo;

public interface UserTradePaymentRecordMapper {
    long countByExample(UserTradePaymentRecordExample example);

    int deleteByExample(UserTradePaymentRecordExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(UserTradePaymentRecord record);

    int insertSelective(UserTradePaymentRecord record);

    List<UserTradePaymentRecord> selectByExample(UserTradePaymentRecordExample example);

    UserTradePaymentRecord selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") UserTradePaymentRecord record, @Param("example") UserTradePaymentRecordExample example);

    int updateByExample(@Param("record") UserTradePaymentRecord record, @Param("example") UserTradePaymentRecordExample example);

    int updateByPrimaryKeySelective(UserTradePaymentRecord record);

    int updateByPrimaryKey(UserTradePaymentRecord record);
    //查询用户交易支付详情  
//  	List<UserTradePayDetailsVo> findUserTradeDetails(Integer id);

  	List<UserTradePayDetailsVo> findByTradeId(@Param("tradeId")Integer tradeId);



}