package com.slljr.finance.admin.mapper;

import com.slljr.finance.common.pojo.model.UserCreditCard;

public interface UserCreditCardMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserCreditCard record);

    int insertSelective(UserCreditCard record);

    UserCreditCard selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserCreditCard record);

    int updateByPrimaryKey(UserCreditCard record);
}