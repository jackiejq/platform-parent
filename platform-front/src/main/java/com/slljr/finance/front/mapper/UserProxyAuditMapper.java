package com.slljr.finance.front.mapper;

import com.slljr.finance.common.pojo.model.UserProxyAudit;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserProxyAuditMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserProxyAudit record);

    int insertSelective(UserProxyAudit record);

    UserProxyAudit selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserProxyAudit record);

    int updateByPrimaryKey(UserProxyAudit record);

    List<UserProxyAudit> findByAll(UserProxyAudit userProxyAudit);

    List<UserProxyAudit> findByStatus(@Param("uid") Integer uid, @Param("status") List<Integer> status);
}