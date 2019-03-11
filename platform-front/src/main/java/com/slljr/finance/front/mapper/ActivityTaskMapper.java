package com.slljr.finance.front.mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.slljr.finance.common.pojo.model.ActivityTask;

public interface ActivityTaskMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ActivityTask record);

    int insertSelective(ActivityTask record);

    ActivityTask selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ActivityTask record);

    int updateByPrimaryKeyWithBLOBs(ActivityTask record);

    int updateByPrimaryKey(ActivityTask record);

    List<ActivityTask> findByStatus(@Param("status")Integer status);

    /**
     * 根据ID集合查询任务
     *
     * @author uncle.quentin
     * @date   2018/12/14 9:29
     * @param   ids
     * @return java.util.List<com.slljr.finance.common.pojo.model.ActivityTask>
     * @version 1.0
     */
	List<ActivityTask> selectByIds(List<Integer> ids);
}