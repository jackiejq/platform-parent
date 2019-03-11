package com.slljr.finance.admin.mapper;

import com.slljr.finance.common.pojo.dto.WithdrawDetailDTO;
import com.slljr.finance.common.pojo.model.WithdrawDetail;
import com.slljr.finance.common.pojo.vo.WithdrawDepositDetailVO;

import java.util.Date;
import java.util.List;

/**
 * @description:
 * @author: goodni.
 * @date: 2019/2/27
 * @time: 15:27
 */
public interface WithdrawDetailMapper {
    List<WithdrawDetail> findWithdrawDetailList(WithdrawDetailDTO withdrawDetail);

    WithdrawDepositDetailVO findWithdrawDepositDetailById(Integer id);

    int updateWithdrawDetailById(Integer id, Integer status, Integer auditUid, String remark, Date updateTime);

    WithdrawDetail findWithdrawDetailById(Integer id);
}
