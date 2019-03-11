package com.slljr.finance.admin.mapper;

import com.slljr.finance.common.pojo.model.UserOauth;

public interface UserOauthMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserOauth record);

    int insertSelective(UserOauth record);

    UserOauth selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserOauth record);

    int updateByPrimaryKey(UserOauth record);
}