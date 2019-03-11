package com.slljr.finance.forum.mapper;


import com.slljr.finance.common.pojo.model.UserBasic;

public interface UserBasicMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserBasic record);

    int insertSelective(UserBasic record);

    UserBasic selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserBasic record);

    int updateByPrimaryKey(UserBasic record);
}