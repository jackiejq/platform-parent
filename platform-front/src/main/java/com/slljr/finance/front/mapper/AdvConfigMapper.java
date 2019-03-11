package com.slljr.finance.front.mapper;
import org.apache.ibatis.annotations.Param;
import java.util.Date;

import java.util.List;

import com.slljr.finance.common.pojo.model.AdvConfig;
/**
 * 1、轮播图的数据访问层接口
 * @author XueYi
 * 2018年12月7日 下午3:47:54
 */
public interface AdvConfigMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AdvConfig record);

    int insertSelective(AdvConfig record);

    AdvConfig selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AdvConfig record);

    int updateByPrimaryKey(AdvConfig record);

    /**
     * 条件查询广告位
     *
     * @author uncle.quentin
     * @date   2018/12/14 15:37
     * @param   advConfig
     * @return java.util.List<com.slljr.finance.common.pojo.model.AdvConfig>
     * @version 1.0
     */
    List<AdvConfig> findByAll(AdvConfig advConfig);

    List<AdvConfig> findByIds(@Param("ids")String ids);




}