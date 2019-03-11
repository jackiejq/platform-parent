package com.slljr.finance.admin.mapper;

import com.slljr.finance.common.pojo.model.UserBalanceDetail;

public interface UserBalanceDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserBalanceDetail record);

    int insertSelective(UserBalanceDetail record);

    UserBalanceDetail selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserBalanceDetail record);

    int updateByPrimaryKey(UserBalanceDetail record);
}