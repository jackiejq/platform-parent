package com.slljr.finance.admin.service;

import com.github.pagehelper.PageInfo;
import com.slljr.finance.common.pojo.dto.WithdrawDetailDTO;
import com.slljr.finance.common.pojo.model.WithdrawDetail;
import com.slljr.finance.common.pojo.vo.WithdrawDepositDetailVO;

import java.util.Date;

/**
 * @description:
 * @author: goodni.
 * @date: 2019/2/27
 * @time: 15:23
 */
public interface WithdrawDetailService {

    //分页查询提现记录表
    PageInfo<WithdrawDetail> findWithdrawDetailList(WithdrawDetailDTO withdrawDetail);

    WithdrawDepositDetailVO findWithdrawDepositDetailById(Integer id);

    int updateWithdrawDetailById(Integer id, Integer status, Integer auditUid, String remark, Date updateTime);

    WithdrawDetail findWithdrawDetailById(Integer id);
}
