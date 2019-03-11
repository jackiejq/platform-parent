package com.slljr.finance.admin.mapper;

import com.slljr.finance.common.pojo.dto.UserProxyAuditDTO;
import com.slljr.finance.common.pojo.model.UserProxyAudit;
import com.slljr.finance.common.pojo.vo.UserProxyAuditVO;

import java.util.List;

public interface UserProxyAuditMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserProxyAudit record);

    int insertSelective(UserProxyAudit record);

    UserProxyAudit selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserProxyAudit record);

    int updateByPrimaryKey(UserProxyAudit record);

    List<UserProxyAuditVO> findByAll(UserProxyAuditDTO userProxyAudit);
}