package com.slljr.finance.admin.mapper;

import com.slljr.finance.common.pojo.dto.RepayErrorRecordDTO;
import com.slljr.finance.common.pojo.model.RepayErrorRecord;

import java.util.List;

/**
 * @description:支付错误订单映射
 * @author: goodni.
 * @date: 2019/2/26
 * @time: 15:12
 */
public interface RepayErrorRecordMapper {

    List<RepayErrorRecord> findRepayErrorRecordList(RepayErrorRecordDTO repayErrorRecord);

    int deleteByPrimaryKey(Integer id);

    int insert(RepayErrorRecord record);

    int insertSelective(RepayErrorRecord record);

    RepayErrorRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RepayErrorRecord record);

    int updateByPrimaryKey(RepayErrorRecord record);

    int updateStatusByPrimaryKey(RepayErrorRecord record);
}