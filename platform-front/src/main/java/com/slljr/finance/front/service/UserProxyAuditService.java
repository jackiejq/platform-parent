package com.slljr.finance.front.service;

import com.slljr.finance.common.pojo.model.UserProxyAudit;
import com.slljr.finance.front.mapper.UserProxyAuditMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: 代理用户审核服务接口
 * @author: uncle.quentin.
 * @date: 2019/1/15.
 * @time: 11:45.
 */
@Service
public class UserProxyAuditService {

    /**
     * 代理审核Mapper
     */
    @Autowired
    private UserProxyAuditMapper userProxyAuditMapper;

    /**
     * 申请代理用户
     *
     * @param userProxyAudit
     * @return void
     * @author uncle.quentin
     * @date 2019/1/15 11:57
     * @version 1.0
     */
    public int applyProxyUser(UserProxyAudit userProxyAudit) {
        return userProxyAuditMapper.insertSelective(userProxyAudit);
    }

    /**
     * 查询待审核或已审核过的数据的申请数据
     *
     * @author uncle.quentin
     * @date   2019/1/21 10:46
     * @param
     * @return java.util.List<com.slljr.finance.common.pojo.model.UserProxyAudit>
     * @version 1.0
     */
    public List<UserProxyAudit> selectCheckingOrPassByStatus(int uid){
        List<Integer> paramList = new ArrayList<>();
        paramList.add(UserProxyAudit.AuditStatusEnum.CHECKING.getKey());
        paramList.add(UserProxyAudit.AuditStatusEnum.PASS.getKey());

        return userProxyAuditMapper.findByStatus(uid,paramList);
    }

}
