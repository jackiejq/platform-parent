package com.slljr.finance.admin.mapper;

import com.slljr.finance.common.pojo.model.ForumIntegral;

public interface ForumIntegralMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ForumIntegral record);

    int insertSelective(ForumIntegral record);

    ForumIntegral selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ForumIntegral record);

    int updateByPrimaryKey(ForumIntegral record);
}