package com.slljr.finance.admin.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.slljr.finance.admin.mapper.UserAccountMapper;
import com.slljr.finance.admin.mapper.WithdrawDetailMapper;
import com.slljr.finance.admin.service.WithdrawDetailService;
import com.slljr.finance.common.pojo.dto.WithdrawDetailDTO;
import com.slljr.finance.common.pojo.model.UserAccount;
import com.slljr.finance.common.pojo.model.WithdrawDetail;
import com.slljr.finance.common.pojo.vo.WithdrawDepositDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author goodni
 * @date 2019/2/27 15:24
 */
@Service
public class WithdrawDetailServiceImpl implements WithdrawDetailService {

    @Autowired
    private WithdrawDetailMapper withdrawDetailMapper;

    @Autowired
    private UserAccountMapper userAccountMapper;

    /**
     * 分页查询提现详情表
     * @param withdrawDetail
     * @return
     */
    @Override
    public PageInfo<WithdrawDetail> findWithdrawDetailList(WithdrawDetailDTO withdrawDetail) {
        return PageHelper.startPage(withdrawDetail.getPageNum(), withdrawDetail.getPageSize())
                .doSelectPageInfo(() -> withdrawDetailMapper.findWithdrawDetailList(withdrawDetail));
    }

    /**
     * 通过主键查询提现详情信息
     * @param id
     * @return
     */
    @Override
    public WithdrawDepositDetailVO findWithdrawDepositDetailById(Integer id) {
        return withdrawDetailMapper.findWithdrawDepositDetailById(id);
    }

    /**
     * 提现详情信息审核
     * @param id
     * @param status
     * @param auditUid
     * @param remark
     * @param updateTime
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateWithdrawDetailById(Integer id, Integer status, Integer auditUid, String remark, Date updateTime) {
        int index = withdrawDetailMapper.updateWithdrawDetailById(id, status, auditUid, remark, updateTime);
        //当修改成功后，判断status为审核不通过-1则将提现返还至useraccount表中
        if (index > 0 && status == -1) {
            WithdrawDetail withdrawDetail = withdrawDetailMapper.findWithdrawDetailById(id);
            UserAccount userAccount = new UserAccount();
            userAccount.setCashBalance(withdrawDetail.getAmount());
            userAccount.setUid(withdrawDetail.getUid());
            userAccount.setUpdateTime(new Date());
            userAccountMapper.updateByUid(userAccount);
        }
        return index;
    }

    /**
     * 通过id查询提现详情信息
     * @param id
     * @return
     */
    @Override
    public WithdrawDetail findWithdrawDetailById(Integer id) {
        return withdrawDetailMapper.findWithdrawDetailById(id);
    }
}
