package com.slljr.finance.admin.service;

import com.slljr.finance.admin.mapper.BankMapper;
import com.slljr.finance.common.pojo.model.Bank;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class BankService {
    @Resource
    BankMapper bankMapper;

    public List<Bank> queryAll(){
        return bankMapper.findAll();
    }

}
