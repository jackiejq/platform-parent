package com.slljr.finance.admin.controller;

import com.github.pagehelper.PageInfo;
import com.slljr.finance.admin.service.PaymentChannelBankService;
import com.slljr.finance.common.pojo.model.PaymentChannelBank;
import com.slljr.finance.common.pojo.vo.PaymentChannelBankVO;
import com.slljr.finance.common.utils.WriteJson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@Api(tags = "通道支持银行接口")
@RestController
@RequestMapping("/paymentChannelBank")
public class PaymentChannelBankController {
    @Autowired
    PaymentChannelBankService paymentChannelBankService;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation(httpMethod = "POST", value = "添加通道支持银行")
    public ModelMap add(PaymentChannelBank pcBank){
        Assert.notNull(pcBank.getBankId(), "请选择银行");
        Assert.notNull(pcBank.getChannelId(), "请选择通道");
        Assert.notNull(pcBank.getSingleLimit(), "请输入单笔限额");
        Assert.notNull(pcBank.getSingleDayLimit(), "请输入单日限额");

        if (paymentChannelBankService.add(pcBank)){
            return WriteJson.success();
        }
        return WriteJson.errorWebMsg("添加失败");
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ApiOperation(httpMethod = "POST", value = "修改通道支持银行")
    public ModelMap update(PaymentChannelBank pcBank){
        Assert.notNull(pcBank.getId(), "id不能为空");
        pcBank.setUpdateTime(new Date());
        if (paymentChannelBankService.update(pcBank)){
            return WriteJson.success();
        }
        return WriteJson.errorWebMsg("修改失败");
    }

    @RequestMapping(value = "/disableOrOpenById", method = RequestMethod.GET)
    @ApiOperation(httpMethod = "GET", value = "根据id启用或禁用")
    public ModelMap disableOrOpenById(Integer id){
        Assert.notNull(id, "id不能为空");
        PaymentChannelBankVO pcbank = paymentChannelBankService.findById(id);
        if (pcbank != null){
            PaymentChannelBank upt = new PaymentChannelBank();
            upt.setId(id);
            upt.setStatus(pcbank.getStatus()==0?-1:0);
            paymentChannelBankService.update(upt);
            return WriteJson.success();
        }
        return WriteJson.errorWebMsg("操作失败");
    }

    @RequestMapping(value = "/findById", method = RequestMethod.GET)
    @ApiOperation(httpMethod = "GET", value = "根据id查询通道支持银行")
    public ModelMap findById(Integer id){
        Assert.notNull(id, "id不能为空");
        PaymentChannelBankVO pcbank = paymentChannelBankService.findById(id);
        if (pcbank != null){
            return WriteJson.successData(pcbank);
        }
        return WriteJson.errorWebMsg("查询失败");
    }

    @RequestMapping(value = "/queryByAll", method = RequestMethod.POST)
    @ApiOperation(httpMethod = "POST", value = "分页查询通道支持银行列表")
    public ModelMap queryByAll(PaymentChannelBank pcBank, @RequestParam(defaultValue = "1")int pageNum, @RequestParam(defaultValue = "10") int pageSize){
        PageInfo<PaymentChannelBankVO> page = paymentChannelBankService.queryByAll(pcBank, pageNum, pageSize);
        return WriteJson.successPage(page);
    }
}
