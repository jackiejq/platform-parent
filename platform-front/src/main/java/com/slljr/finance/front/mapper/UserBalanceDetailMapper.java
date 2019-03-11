package com.slljr.finance.front.mapper;

import com.slljr.finance.common.pojo.model.UserBalanceDetail;
import com.slljr.finance.common.pojo.vo.UserBalanceDetailVO;

import java.util.List;

public interface UserBalanceDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserBalanceDetail record);

    int insertSelective(UserBalanceDetail record);

    UserBalanceDetail selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserBalanceDetail record);

    int updateByPrimaryKey(UserBalanceDetail record);

    List<UserBalanceDetailVO> findTopNearlyOneMonth();
}