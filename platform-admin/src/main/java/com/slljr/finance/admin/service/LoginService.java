package com.slljr.finance.admin.service;

import com.slljr.finance.admin.mapper.UserBasicMapper;
import com.slljr.finance.common.pojo.model.UserBasic;
import com.slljr.finance.common.utils.AESUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LoginService {
    @Autowired
    UserBasicMapper userBasicMapper;

    @Value("${app.password.security.key}")
    private String passwordKey;
    
    @Transactional
    public UserBasic login(String phone, String password){
        //加密后密码
    	String ecodes = AESUtil.ecodes(password, passwordKey);
    	UserBasic userBasic = userBasicMapper.findByPhoneAndPasswordAndType(phone, ecodes, UserBasic.TypeEnum.SYSTEM.getKey());   	
        return  userBasic;  
    }


}
