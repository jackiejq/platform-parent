package com.slljr.finance.admin.service;

import com.slljr.finance.common.pojo.model.UserAccount;

/**
 * @description:
 * @author: goodni.
 * @date: 2019/2/27
 * @time: 20:13
 */
public interface UserAccountService {

    int updateByPrimaryKey(UserAccount record);

    int updateByUid(UserAccount record);

    UserAccount getByUid(Integer uid);

}
