package com.slljr.finance.front.service;

import com.slljr.finance.common.enums.PaymentStatusEnum;
import com.slljr.finance.common.pojo.model.RepayErrorRecord;
import com.slljr.finance.front.mapper.RepayErrorRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description: 异常还款数据服务接口
 * @author: uncle.quentin.
 * @date: 2019/2/22.
 * @time: 15:41.
 */
@Service
public class RepayErrorRecordService {

    @Autowired
    private RepayErrorRecordMapper repayErrorRecordMapper;

    /**
     * 新增异常还款数据
     * @author uncle.quentin
     * @date   2019/2/22 15:43
     * @param   record
     * @return int
     * @version 1.0
     */
    public int addRepayErrorRecord(RepayErrorRecord record) {
        record.setStatus(PaymentStatusEnum.WAITING_PAY.getKey());
        return repayErrorRecordMapper.insertSelective(record);
    }

    /**
     * 修改异常还款数据
     * @author uncle.quentin
     * @date   2019/2/22 15:43
     * @param   record
     * @return int
     * @version 1.0
     */
    public int updateRepayErrorRecord(RepayErrorRecord record){
        return repayErrorRecordMapper.updateByPrimaryKeySelective(record);
    }

    /**
     * 条件查询异常还款数据
     *
     * @author uncle.quentin
     * @date   2019/2/22 16:27
     * @param   repayErrorRecord
     * @return java.util.List<com.slljr.finance.common.pojo.model.RepayErrorRecord>
     * @version 1.0
     */
    public List<RepayErrorRecord> selectRepayErrorRecordByCondition(RepayErrorRecord repayErrorRecord){
        return repayErrorRecordMapper.findByAll(repayErrorRecord);
    }

    public List<RepayErrorRecord> selectRepayErrorByStatus(List<Integer> status){
        return repayErrorRecordMapper.findByStatus(status);
    }

}
