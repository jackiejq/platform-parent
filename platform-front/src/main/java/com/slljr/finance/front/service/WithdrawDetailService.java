package com.slljr.finance.front.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.slljr.finance.common.enums.WithdrewAuditStatusEnum;
import com.slljr.finance.common.exception.InterfaceException;
import com.slljr.finance.common.pojo.model.UserAccount;
import com.slljr.finance.common.pojo.model.WithdrawDetail;
import com.slljr.finance.common.pojo.vo.WithdrawDetailVO;
import com.slljr.finance.common.utils.MsgEnum;
import com.slljr.finance.front.mapper.UserAccountMapper;
import com.slljr.finance.front.mapper.WithdrawDetailMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @description: 提现明细记录服务接口
 * @author: uncle.quentin.
 * @date: 2019/2/20.
 * @time: 11:50.
 */
@Service
public class WithdrawDetailService {

    @Autowired
    private WithdrawDetailMapper withdrawDetailMapper;

    @Autowired
    private UserAccountMapper userAccountMapper;


    /**
     * 新增提现记录
     *
     * @param withdrawDetail
     * @return int
     * @author uncle.quentin
     * @date 2019/2/20 11:52
     * @version 1.0
     */
    public int addWithdrawDetail(WithdrawDetail withdrawDetail) throws InterfaceException {
        //查询账户余额信息
        UserAccount userAccount = userAccountMapper.findByUid(withdrawDetail.getUid());

        //判断余额是否充足
        if (null != userAccount) {
            Double balance = userAccount.getCashBalance();
            balance = balance - withdrawDetail.getAmount();
            if (balance < 0) {
                throw new InterfaceException(MsgEnum.INSUFFICIENT_ACCOUNT_BALANCE);
            }

            //更新余额
            UserAccount record = new UserAccount();
            record.setUid(withdrawDetail.getUid());
            record.setCashBalance(balance);
            userAccountMapper.updateByUid(record);
        } else {
            throw new InterfaceException(MsgEnum.ACCOUNT_NOT_EXIST);
        }


        //新增提现明细
        return withdrawDetailMapper.insertSelective(withdrawDetail);
    }

    /**
     * 根据用户ID获取提现记录
     *
     * @author uncle.quentin
     * @date   2019/2/20 15:23
     * @param   withdrawDetail
     * @return com.github.pagehelper.PageInfo<com.slljr.finance.common.pojo.model.WithdrawDetail>
     * @version 1.0
     */
    public PageInfo<WithdrawDetailVO> getWithdrawDetailByUid(WithdrawDetail withdrawDetail){
        PageInfo<WithdrawDetailVO> goodsPage = PageHelper.startPage(withdrawDetail.getPageNum(), withdrawDetail.getPageSize()).doSelectPageInfo(() -> withdrawDetailMapper.selectWithdrawByCondition(withdrawDetail));
        return goodsPage;
    }

    /**
     * 审核提现信息
     *
     * @param withdrawDetail
     * @return int
     * @author uncle.quentin
     * @date 2019/2/20 11:58
     * @version 1.0
     */
    public int auditWithdrawDetail(WithdrawDetail withdrawDetail) {
        int successCount = 0;
        WithdrewAuditStatusEnum withdrewAuditStatusEnum = WithdrewAuditStatusEnum.val(withdrawDetail.getStatus());
        switch (withdrewAuditStatusEnum) {
            //审核不通过
            case FAIL:
                //TODO 提现审核不通过处理
                break;
            //审核通过
            case PASS:
                //TODO 审核通过处理
                break;
            default:
                break;

        }
        return successCount;
    }

}
