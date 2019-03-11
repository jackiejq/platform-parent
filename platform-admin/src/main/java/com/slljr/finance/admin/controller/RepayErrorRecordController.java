package com.slljr.finance.admin.controller;

import com.github.pagehelper.PageInfo;
import com.slljr.finance.admin.service.RepayErrorService;
import com.slljr.finance.common.pojo.dto.RepayErrorRecordDTO;
import com.slljr.finance.common.pojo.model.RepayErrorRecord;
import com.slljr.finance.common.utils.WriteJson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author goodni
 * @date 2019/2/26 15:46
 */
@RestController
@Api(tags = {"支付错误订单操作的API"})
@RequestMapping(value = "/repayError")
@CrossOrigin
public class RepayErrorRecordController {

    @Autowired
    private RepayErrorService repayErrorService;

    /**
     * 分页查看支付错误记录列表
     *
     * @return
     */
    @RequestMapping(value = "/findRepayErrorRecordList", method = RequestMethod.POST)
    @ApiOperation(httpMethod = "POST", value = "分页查看支付错误记录列表")
    public ModelMap findRepayErrorRecordList(RepayErrorRecordDTO repayErrorRecord) {
        PageInfo<RepayErrorRecord> pageInfo = repayErrorService.findRepayErrorRecordList(repayErrorRecord);
        return WriteJson.successPage(pageInfo);
    }

    /**
     * 手动异常提现处理，修改状态，更新时间并添加备注
     *
     * @return
     */
    @RequestMapping(value = "/updateStatusByPrimaryKey", method = RequestMethod.POST)
    @ApiOperation(httpMethod = "POST", value = "手动异常提现处理，修改状态，更新时间并添加备注")
    public ModelMap updateStatusByPrimaryKey(RepayErrorRecordDTO repayErrorRecord) {
        int index = repayErrorService.updateStatusByPrimaryKey(repayErrorRecord);
        if (index > 0) {
            return WriteJson.success("更新状态成功");
        }
        return WriteJson.errorWebMsg("更新状态失败");
    }
}
