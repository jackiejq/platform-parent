package com.slljr.finance.front.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.slljr.finance.common.pojo.model.IntegralTask;

public interface IntegralTaskMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(IntegralTask record);

    int insertSelective(IntegralTask record);

    IntegralTask selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(IntegralTask record);

    int updateByPrimaryKeyWithBLOBs(IntegralTask record);

    int updateByPrimaryKey(IntegralTask record);

    /**
     * 查询所有积分任务
     */
    @Select({"select id, title, number, img_url as imgUrl, start_time as startTime, end_time as endTime, create_time as createTime, update_time as updateTime ,detail  from integral_task"})
	List<IntegralTask> findAll();
}