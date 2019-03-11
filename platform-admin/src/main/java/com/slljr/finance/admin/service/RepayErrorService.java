package com.slljr.finance.admin.service;

import com.github.pagehelper.PageInfo;
import com.slljr.finance.common.pojo.dto.RepayErrorRecordDTO;
import com.slljr.finance.common.pojo.model.RepayErrorRecord;

/**
 * @description: 支付错误订单服务层
 * @author: goodni
 * @date: 2019/2/26
 * @time: 14:59
 */
public interface RepayErrorService {
    /**
     * 分页查询异常提现记录list
     * @param repayErrorRecord
     * @return
     */
    //分页查询异常提现记录list
    PageInfo<RepayErrorRecord> findRepayErrorRecordList(RepayErrorRecordDTO repayErrorRecord);

    /**
     * 手动异常处理，修改状态，更新时间并添加备注
     * @param record
     * @return
     */
    int updateStatusByPrimaryKey(RepayErrorRecord record);
}
