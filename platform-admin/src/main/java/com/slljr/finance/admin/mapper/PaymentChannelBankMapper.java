package com.slljr.finance.admin.mapper;
import java.util.Date;

import com.slljr.finance.common.pojo.model.PaymentChannelBank;
import com.slljr.finance.common.pojo.vo.PaymentChannelBankVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PaymentChannelBankMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PaymentChannelBank record);

    int insertSelective(PaymentChannelBank record);

    PaymentChannelBankVO selectByPrimaryKey(Integer id);

    /**
     * 根据通道类型和银行查询哪些通道支持当前银行卡操作
     * @param channelType
     * @param bankId
     * @return
     */
    List<PaymentChannelBankVO> findByChannelTypeAndBankId(@Param("channelType")Integer channelType, @Param("bankId")Integer bankId);

    List<PaymentChannelBankVO> findByAll(PaymentChannelBank paymentChannelBank);



    int updateByPrimaryKeySelective(PaymentChannelBank record);

    int updateByPrimaryKey(PaymentChannelBank record);
}