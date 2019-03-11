package com.slljr.finance.admin.service.impl;

import com.slljr.finance.admin.mapper.UserAccountMapper;
import com.slljr.finance.admin.service.UserAccountService;
import com.slljr.finance.common.pojo.model.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: goodni.
 * @date: 2019/2/27
 * @time: 20:13
 */
@Service
public class UserAccountServiceImpl implements UserAccountService {

    @Autowired
    private UserAccountMapper userAccountMapper;

    public int updateByPrimaryKey(UserAccount record) {
        return userAccountMapper.updateByPrimaryKey(record);
    }

    public int updateByUid(UserAccount record) {

        return userAccountMapper.updateByUid(record);

    }

    @Override
    public UserAccount getByUid(Integer uid) {
        return userAccountMapper.selectByUid(uid);
    }
}
