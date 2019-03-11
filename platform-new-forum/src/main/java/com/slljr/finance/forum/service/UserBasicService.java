package com.slljr.finance.forum.service;

import com.slljr.finance.common.pojo.model.UserBasic;
import com.slljr.finance.forum.mapper.UserBasicMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @description: 用户服务接口
 * @author: uncle.quentin.
 * @date: 2019/3/1.
 * @time: 16:36.
 */
@Service
public class UserBasicService {

    @Autowired
    private UserBasicMapper userBasicMapper;

    /**
     * 根据ID查询用户信息
     *
     * @author uncle.quentin
     * @date   2019/3/1 16:37
     * @param   id
     * @return com.slljr.finance.common.pojo.model.UserBasic
     * @version 1.0
     */
    public UserBasic selectUserBasicById(Integer id){
        return userBasicMapper.selectByPrimaryKey(id);
    }

}
