package com.slljr.finance.admin.controller;

import com.github.pagehelper.PageInfo;
import com.slljr.finance.admin.service.RsaService;
import com.slljr.finance.admin.service.WithdrawDetailService;
import com.slljr.finance.common.pojo.dto.WithdrawDetailDTO;
import com.slljr.finance.common.pojo.model.WithdrawDetail;
import com.slljr.finance.common.pojo.vo.WithdrawDepositDetailVO;
import com.slljr.finance.common.utils.WriteJson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.Date;


/**
 * @description:提现订单操作的API
 * @author: goodni.
 * @date: 2019/2/27
 * @time: 16:43
 */
@RestController
@Api(tags = {"提现订单操作的API"})
@RequestMapping(value = "/withdraw")
@CrossOrigin
public class WithdrawDetailController {

    @Autowired
    private WithdrawDetailService withdrawDetailService;

    @Autowired
    private RsaService rsaService;

    /**
     * 分页查看提现记录列表
     *
     * @return
     */
    @RequestMapping(value = "/findWithdrawDetailList", method = RequestMethod.POST)
    @ApiOperation(httpMethod = "POST", value = "分页查看提现记录列表")
    public ModelMap findWithdrawDetailList(WithdrawDetailDTO withdrawDetail) {
        PageInfo<WithdrawDetail> pageInfo = withdrawDetailService.findWithdrawDetailList(withdrawDetail);
        return WriteJson.successPage(pageInfo);
    }

    /**
     * 提现详情信息查询
     * @param id
     * @return
     */
    @RequestMapping(value = "/findWithdrawDepositDetailById", method = RequestMethod.GET)
    @ApiOperation(httpMethod = "GET", value = "查找提现记录详情信息")
    public ModelMap findWithdrawDepositDetail(Integer id) {
        Assert.notNull(id, "id不能为空");
        WithdrawDepositDetailVO withdrawDepositDetailVo = withdrawDetailService.findWithdrawDepositDetailById(id);
        if (withdrawDepositDetailVo == null) {
            return WriteJson.errorWebMsg("查询无记录");
        }
        //将银行卡号解密
        String bankCardNo = rsaService.decode(withdrawDepositDetailVo.getBankCardNoSign());
        withdrawDepositDetailVo.setBankCardNo(bankCardNo);
        withdrawDepositDetailVo.setBankCardNoSign(null);//清空已加密的卡号
        return WriteJson.successData(withdrawDepositDetailVo);
    }

    /**
     * 审核提现表单功能
     *
     * @param id
     * @param status
     * @param auditUid
     * @return
     */
    @RequestMapping(value = "/WithdrawalAudit", method = RequestMethod.POST)
    @ApiOperation(httpMethod = "POST", value = "提现审核，修改状态和审核人员id")
    public ModelMap WithdrawalAudit(Integer id, Integer status, Integer auditUid, String remark) {
        Assert.notNull(id, "id不能为空");
        Assert.notNull(status, "status不能为空");
        Assert.notNull(auditUid, "auditUid不能为空");
        int index = withdrawDetailService.updateWithdrawDetailById(id, status, auditUid, remark,new Date());
        //当修改成功后，判断status为审核不通过-1则将提现返还至useraccount表中
        if (index > 0) {
            return WriteJson.success("修改成功");
        }
        return WriteJson.errorWebMsg("修改失败");
    }
}
