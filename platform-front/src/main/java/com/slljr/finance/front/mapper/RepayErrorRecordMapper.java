package com.slljr.finance.front.mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Date;

import com.slljr.finance.common.pojo.model.RepayErrorRecord;

public interface RepayErrorRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RepayErrorRecord record);

    int insertSelective(RepayErrorRecord record);

    RepayErrorRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RepayErrorRecord record);

    int updateByPrimaryKey(RepayErrorRecord record);

    List<RepayErrorRecord> findByAll(RepayErrorRecord repayErrorRecord);

    List<RepayErrorRecord> findByStatus(@Param("status") List<Integer> status);



}