package com.slljr.finance.front.mapper;

import com.slljr.finance.common.pojo.model.WithdrawDetail;
import com.slljr.finance.common.pojo.vo.WithdrawDetailVO;

import java.util.List;

public interface WithdrawDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(WithdrawDetail record);

    int insertSelective(WithdrawDetail record);

    WithdrawDetail selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WithdrawDetail record);

    int updateByPrimaryKey(WithdrawDetail record);

    /**
     * 条件查询用户提现记录
     *
     * @param record
     * @return java.util.List<com.slljr.finance.common.pojo.vo.WithdrawDetailVO>
     * @author uncle.quentin
     * @date 2019/2/20 15:31
     * @version 1.0
     */
    List<WithdrawDetailVO> selectWithdrawByCondition(WithdrawDetail record);
}