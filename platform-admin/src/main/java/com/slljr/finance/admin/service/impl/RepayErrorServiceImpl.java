package com.slljr.finance.admin.service.impl;/**
 * @description:
 * @author: uncle.quentin.
 * @date: 2019/2/26
 * @time: 15:04
 */

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.slljr.finance.admin.mapper.RepayErrorRecordMapper;
import com.slljr.finance.admin.service.RepayErrorService;
import com.slljr.finance.common.pojo.dto.RepayErrorRecordDTO;
import com.slljr.finance.common.pojo.model.RepayErrorRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author goodni
 * @date 2019/2/26 15:04
 */
@Service
public class RepayErrorServiceImpl implements RepayErrorService {

    @Autowired
    private RepayErrorRecordMapper repayErrorRecordMapper;
    @Override
    public PageInfo<RepayErrorRecord> findRepayErrorRecordList(RepayErrorRecordDTO repayErrorRecord) {
        return PageHelper.startPage(repayErrorRecord.getPageNum(), repayErrorRecord.getPageSize())
                .doSelectPageInfo(() -> repayErrorRecordMapper.findRepayErrorRecordList(repayErrorRecord));
    }

    @Override
    public int updateStatusByPrimaryKey(RepayErrorRecord record) {
        record.setUpdateTime(new Date());
        //前端手动处理后，默认不传状态，此时订单状态修改为-3已手动处理
        if(record.getStatus()==null){
            record.setStatus(-3);
        }
        return repayErrorRecordMapper.updateStatusByPrimaryKey(record);
    }
}
