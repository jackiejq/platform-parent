package com.slljr.finance.admin.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.slljr.finance.admin.mapper.UserAccountMapper;
import com.slljr.finance.admin.mapper.UserBasicMapper;
import com.slljr.finance.admin.mapper.UserProxyAuditMapper;
import com.slljr.finance.common.enums.SysConfigEnum;
import com.slljr.finance.common.pojo.dto.UserProxyAuditDTO;
import com.slljr.finance.common.pojo.model.SysConfig;
import com.slljr.finance.common.pojo.model.UserAccount;
import com.slljr.finance.common.pojo.model.UserBasic;
import com.slljr.finance.common.pojo.model.UserProxyAudit;
import com.slljr.finance.common.pojo.vo.UserProxyAuditVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Autowired
    private UserBasicMapper userBasicMapper;

    @Autowired
    private UserAccountMapper userAccountMapper;

    /**
     * 系统配置服务接口
     */
    @Autowired
    private SysConfigSerivce sysConfigService;

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
     * 处理代理用户申请
     *
     * @param id              数据ID
     * @param auditRemark     审核描述
     * @param auditStatusEnum 审核状态
     * @return int
     * @author uncle.quentin
     * @date 2019/1/15 13:44
     * @version 1.0
     */
    public int dealAudit(int id, UserProxyAudit.AuditStatusEnum auditStatusEnum, String auditRemark) {
        UserProxyAudit userProxyAudit = new UserProxyAudit();
        userProxyAudit.setId(id);
        userProxyAudit.setAuditStatus(auditStatusEnum.getKey());
        userProxyAudit.setAuditRemark(auditRemark);
        int count = userProxyAuditMapper.updateByPrimaryKeySelective(userProxyAudit);
        //审批通过更新用户信息
        if (count > 0) {
            if (UserProxyAudit.AuditStatusEnum.PASS == auditStatusEnum) {
                //获取用户ID
                UserProxyAudit userProxyAudit1 = userProxyAuditMapper.selectByPrimaryKey(id);
                //更新用户类型
                UserBasic userBasic = new UserBasic();
                userBasic.setId(userProxyAudit1.getUid());
                userBasic.setType(UserBasic.TypeEnum.AGENT.getKey());
                userBasicMapper.updateByPrimaryKeySelective(userBasic);
                //更新用户账户代理等级
                SysConfigEnum sysConfigEnum = SysConfigEnum.DEFAULT_PROXY_LEVEL;
                SysConfig sysConfig = sysConfigService.selectByKey(sysConfigEnum);
                UserAccount userAccount = new UserAccount();
                userAccount.setUid(userProxyAudit1.getUid());
                userAccount.setAgentLevelId(null == sysConfig.getSysValue() ? 0 : Integer.valueOf(sysConfig.getSysValue()));
                userAccountMapper.updateByUid(userAccount);
            }
        }

        return count;
    }


    /**
     * 条件查询数据
     *
     * @param userProxyAudit
     * @return java.util.List<com.slljr.finance.common.pojo.model.UserProxyAudit>
     * @author uncle.quentin
     * @date 2019/1/15 14:01
     * @version 1.0
     */
    public PageInfo<UserProxyAuditVO> listProxyUserAudit(UserProxyAuditDTO userProxyAudit) {
        PageInfo<UserProxyAuditVO> goodsPage = PageHelper.startPage(userProxyAudit.getPageNum(), userProxyAudit.getPageSize()).doSelectPageInfo(() -> userProxyAuditMapper.findByAll(userProxyAudit));
        return goodsPage;
    }


}
