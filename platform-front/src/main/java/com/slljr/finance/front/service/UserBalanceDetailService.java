package com.slljr.finance.front.service;

import com.slljr.finance.common.pojo.vo.UserBalanceDetailVO;
import com.slljr.finance.front.mapper.UserAccountMapper;
import com.slljr.finance.front.mapper.UserBalanceDetailMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserBalanceDetailService {
    @Resource
    UserBalanceDetailMapper userBalanceDetailMapper;
    @Resource
    UserAccountMapper userAccountMapper;

    /**
     * 查询近一个月TOP20金币变更信息
     *
     * @author uncle.quentin
     * @date   2019/1/15 19:30
     * @param
     * @return java.util.List<com.slljr.finance.common.pojo.vo.UserBalanceDetailVO>
     * @version 1.0
     */
    public List<UserBalanceDetailVO> selectBalanceTopNearlyOneMonth(){
        return userBalanceDetailMapper.findTopNearlyOneMonth();
    }

}
