package com.slljr.finance.admin.mapper;
import org.apache.ibatis.annotations.Param;

import com.slljr.finance.common.pojo.model.Bank;

import java.util.List;

public interface BankMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Bank record);

    int insertSelective(Bank record);

    Bank selectByPrimaryKey(Integer id);

    List<Bank> findAll();

    int updateByPrimaryKeySelective(Bank record);

    int updateByPrimaryKey(Bank record);
}