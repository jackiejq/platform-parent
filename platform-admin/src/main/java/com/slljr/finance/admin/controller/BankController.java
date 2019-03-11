package com.slljr.finance.admin.controller;

import com.slljr.finance.admin.service.BankService;
import com.slljr.finance.common.pojo.model.Bank;
import com.slljr.finance.common.utils.WriteJson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "银行接口")
@RequestMapping("/bank")
@RestController
public class BankController {
    @Autowired
    BankService bankService;

    @ApiOperation(value = "查询所有银行", httpMethod = "GET")
    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    public ModelMap getAll(){
        List<Bank> list = bankService.queryAll();
        return WriteJson.successData(list);
    }

}
