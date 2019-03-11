package com.slljr.finance.front.service;

import com.slljr.finance.common.pojo.dto.UserSignLogDTO;
import com.slljr.finance.common.pojo.model.UserSignLog;
import com.slljr.finance.front.mapper.UserSignLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description: 用户签到信息服务层
 * @author: uncle.quentin.
 * @date: 2019/1/7.
 * @time: 16:45.
 */
@Service
public class UserSignLogService {

    @Autowired
    private UserSignLogMapper userSignLogMapper;

    /**
     * 条件查询用户签到信息
     *
     * @param userSignLogDTO
     * @return java.util.List<com.slljr.finance.common.pojo.model.UserSignLog>
     * @author uncle.quentin
     * @date 2019/1/7 16:51
     * @version 1.0
     */
    public List<UserSignLog> selectUserSignedLogByCondition(UserSignLogDTO userSignLogDTO) {
        return userSignLogMapper.findByAll(userSignLogDTO);
    }

}
