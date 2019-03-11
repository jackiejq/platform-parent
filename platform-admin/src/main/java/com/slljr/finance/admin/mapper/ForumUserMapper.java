package com.slljr.finance.admin.mapper;

import org.apache.ibatis.annotations.Param;

import com.slljr.finance.common.pojo.model.ForumUser;

public interface ForumUserMapper {
    int deleteByPrimaryKey(Integer uid);

    int insert(ForumUser record);

    int insertSelective(ForumUser record);

    ForumUser selectByPrimaryKey(Integer uid);

    int updateByPrimaryKeySelective(ForumUser record);

    int updateByPrimaryKey(ForumUser record);

    //管理员操作用户状态(-1:禁言;0:正常)
	void updateUserStatus(@Param("uid")Integer uid,@Param("status") Integer status);
}