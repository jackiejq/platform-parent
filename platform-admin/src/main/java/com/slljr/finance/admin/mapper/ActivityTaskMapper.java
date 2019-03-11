package com.slljr.finance.admin.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import com.slljr.finance.common.pojo.dto.PageActivityTaskDto;
import com.slljr.finance.common.pojo.model.ActivityTask;

public interface ActivityTaskMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ActivityTask record);

    int insertSelective(ActivityTask record);

    ActivityTask selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ActivityTask record);

    int updateByPrimaryKeyWithBLOBs(ActivityTask record);

    int updateByPrimaryKey(ActivityTask record);
    
    @Update({"update activity_task set status = -1 where id= #{id}"})
	void deleteByStatus(@Param("id") Integer id);

	List<ActivityTask> findSysConfigList(PageActivityTaskDto activityTaskDto);
}