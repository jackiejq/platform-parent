package com.slljr.finance.admin.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.slljr.finance.admin.mapper.UserBankCardMapper;
import com.slljr.finance.common.pojo.vo.UserBankCardVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserBankCardService {
    @Resource
    UserBankCardMapper userBankCardMapper;

    public PageInfo findByUid(int uid, int pageNum, int pageSize){
        PageHelper.startPage(pageNum, pageSize);
        List<UserBankCardVo> list = userBankCardMapper.findByUid(uid);
        return new PageInfo<>(list);
    }

}
