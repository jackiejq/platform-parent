package com.slljr.finance.front.mapper;

import com.slljr.finance.common.pojo.model.UserOauth;
import org.apache.ibatis.annotations.Param;

public interface UserOauthMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserOauth record);

    int insertSelective(UserOauth record);

    UserOauth selectByPrimaryKey(Integer id);

    UserOauth selectByOpenIdAndTarget(@Param("openId") String openId, @Param("target") int target);

    int updateByPrimaryKeySelective(UserOauth record);

    int updateByPrimaryKey(UserOauth record);
}