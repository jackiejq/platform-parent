package com.slljr.finance.admin.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import com.slljr.finance.common.pojo.model.SysConfig;

public interface SysConfigMapper {
   
    int deleteByPrimaryKey(Integer id);

    int insert(SysConfig record);

    int insertSelective(SysConfig record);

    SysConfig selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysConfig record);

    int updateByPrimaryKey(SysConfig record);

    @Update({"update sys_config set status = -1 where id= #{id}"})
	void deleteSysConfig(@Param("id") Integer id);

	List<SysConfig> findSysConfigList(SysConfig sysConfig);

    /**
     * 根据KEY查询
     *
     * @param sysKey
     * @author uncle.quentin
     * @date 2018/12/13 20:42
     * @version 1.0
     */
    SysConfig findBySysKey(@Param("sysKey") String sysKey);

    /**
     * 根据批量KEY查询
     *
     * @param sysKeys
     * @return java.util.List<com.slljr.finance.common.pojo.model.SysConfig>
     * @author uncle.quentin
     * @date 2018/12/13 20:42
     * @version 1.0
     */
    List<SysConfig> findBySysKeys(List<String> sysKeys);
}