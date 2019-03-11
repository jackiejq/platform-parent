package com.slljr.finance.front.service.impl;

import com.slljr.finance.common.pojo.model.AdvConfig;
import com.slljr.finance.front.mapper.AdvConfigMapper;
import com.slljr.finance.front.service.IAdvConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 1、查询轮播图的信息
 *
 * @author XueYi
 * 2018年12月7日 下午6:13:47
 */
@Service
public class AdvConfigServiceImpl implements IAdvConfigService {

    @Autowired
    private AdvConfigMapper advConfigMapper;

    /**
     * 查询所有广告位的信息
     *
     * @param
     * @return java.util.List<com.slljr.finance.common.pojo.model.AdvConfig>
     * @author uncle.quentin
     * @date 2018/12/14 15:38
     * @version 1.0
     */
    @Override
    public List<AdvConfig> listAdvConfig() {
        return advConfigMapper.findByAll(null);
    }

    @Override
    public List<AdvConfig> selectAdvByIds(String ids) {
        return advConfigMapper.findByIds(ids);
    }

}
