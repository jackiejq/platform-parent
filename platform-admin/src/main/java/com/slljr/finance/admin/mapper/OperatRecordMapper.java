package com.slljr.finance.admin.mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Date;

import com.slljr.finance.common.pojo.model.OperatRecord;

public interface OperatRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OperatRecord record);

    int insertSelective(OperatRecord record);

    OperatRecord selectByPrimaryKey(Integer id);

    List<OperatRecord> findByUid(@Param("uid")Integer uid);


    int updateByPrimaryKeySelective(OperatRecord record);

    int updateByPrimaryKey(OperatRecord record);
}