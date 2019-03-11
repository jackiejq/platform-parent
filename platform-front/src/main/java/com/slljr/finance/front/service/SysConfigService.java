package com.slljr.finance.front.service;

import com.slljr.finance.common.enums.SysConfigEnum;
import com.slljr.finance.common.pojo.model.SysConfig;
import com.slljr.finance.front.mapper.SysConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @description: 系统配置服务接口
 * @author: uncle.quentin.
 * @date: 2018/12/13.
 * @time: 20:27.
 */
@Service
public class SysConfigService {

    @Autowired
    private SysConfigMapper sysConfigMapper;

    /**
     * 根据KEY查询
     *
     * @param sysConfigEnum
     * @return com.slljr.finance.common.pojo.model.SysConfig
     * @author uncle.quentin
     * @date 2018/12/14 10:33
     * @version 1.0
     */
    public SysConfig selectByKey(SysConfigEnum sysConfigEnum) {
        SysConfig sysConfigs = sysConfigMapper.findBySysKey(sysConfigEnum.getKey());
        return sysConfigs;
    }

    /**
     * 获取指定配置ID
     *
     * @param
     * @return java.util.List<com.slljr.finance.common.pojo.model.SysConfig>
     * @author uncle.quentin
     * @date 2018/12/13 21:03
     * @version 1.0
     */
    public SysConfig selectSysConfigByKey(SysConfigEnum sysConfigEnums) {
        return sysConfigMapper.findBySysKey(sysConfigEnums.getKey());
    }

}
