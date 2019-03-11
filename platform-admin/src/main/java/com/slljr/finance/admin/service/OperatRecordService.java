package com.slljr.finance.admin.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.slljr.finance.admin.mapper.OperatRecordMapper;
import com.slljr.finance.common.pojo.model.OperatRecord;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class OperatRecordService {
    @Resource
    OperatRecordMapper operatRecordMapper;

    public PageInfo<OperatRecord> listOperatRecord(int uid, int pageNum, int pageSize){
        PageHelper.startPage(pageNum, pageSize);
        List<OperatRecord> list = operatRecordMapper.findByUid(uid);
        return new PageInfo<>(list);
    }

}
