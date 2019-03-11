package com.slljr.finance.front.service;

import com.slljr.finance.common.pojo.model.AppVersion;
import com.slljr.finance.front.mapper.AppVersionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @description: APP版本服务接口
 * @author: uncle.quentin.
 * @date: 2019/1/28
 * @time: 16:56
 */
@Service
public class AppVersionService {

    @Autowired
    private AppVersionMapper appVersionMapper;

    /**
     * 获取该类型最新版本信息
     *
     * @Author: 杨国群
     * @Date:   2019/1/28 16:59
     * @param   type
     * @version 1.0
     */
    public AppVersion getNewVersion(Integer type){
        if (null == type) {
            return null;
        }
        return appVersionMapper.getNewVersionByType(type);
    }

}
