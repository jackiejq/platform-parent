package com.slljr.finance.admin.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.slljr.finance.admin.mapper.PaymentChannelBankMapper;
import com.slljr.finance.common.pojo.model.PaymentChannelBank;
import com.slljr.finance.common.pojo.vo.PaymentChannelBankVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class PaymentChannelBankService {
    @Resource
    PaymentChannelBankMapper paymentChannelBankMapper;

    public PaymentChannelBankVO findById(int id){
        return paymentChannelBankMapper.selectByPrimaryKey(id);
    }

    public boolean add(PaymentChannelBank pcBank){
        return paymentChannelBankMapper.insertSelective(pcBank)==1;
    }

    public boolean update(PaymentChannelBank pcBank){
        return paymentChannelBankMapper.updateByPrimaryKeySelective(pcBank) == 1;
    }

    public PageInfo<PaymentChannelBankVO> queryByAll(PaymentChannelBank pcBank, int pageNum, int pageSize){
        PageHelper.startPage(pageNum, pageSize);
        List<PaymentChannelBankVO> list = paymentChannelBankMapper.findByAll(pcBank);
        return new PageInfo<>(list);
    }

}
