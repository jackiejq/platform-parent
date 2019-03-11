package com.slljr.finance.admin.controller;

import com.github.pagehelper.PageInfo;
import com.slljr.finance.admin.mapper.UserBankCardMapper;
import com.slljr.finance.admin.service.OperatRecordService;
import com.slljr.finance.admin.service.UserBankCardService;
import com.slljr.finance.admin.service.UserBasicService;
import com.slljr.finance.common.pojo.model.OperatRecord;
import com.slljr.finance.common.pojo.model.UserBasic;
import com.slljr.finance.common.pojo.vo.UserBasicByVO;
import com.slljr.finance.common.utils.WriteJson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@Api(tags = "基础用户数据接口")
public class UserController {
    @Autowired
    UserBasicService userBasicService;
    @Autowired
    OperatRecordService operatRecordService;
    @Autowired
    UserBankCardService userBankCardService;


    @RequestMapping(value = "/listPtUser", method = RequestMethod.POST)
    @ApiOperation(value = "查询平台用户列表", httpMethod = "POST")
    public ModelMap listPtUser(UserBasicByVO userBasic, @RequestParam(defaultValue = "1")int pageNum, @RequestParam(defaultValue = "10")int pageSize){
        PageInfo<UserBasicByVO> pageInfo = userBasicService.listPtUser(userBasic, pageNum, pageSize);
        return WriteJson.successPage(pageInfo);
    }

    @RequestMapping(value = "/listAdminUser", method = RequestMethod.POST)
    @ApiOperation(value = "查询系统用户列表", httpMethod = "POST")
    public ModelMap listAdminUser(UserBasic userBasic, @RequestParam(defaultValue = "1")int pageNum, @RequestParam(defaultValue = "10")int pageSize){
        PageInfo<UserBasic> pageInfo = userBasicService.listAdminUser(userBasic, pageNum, pageSize);
        return WriteJson.successPage(pageInfo);
    }

    @RequestMapping(value = "/listOperatRecord", method = RequestMethod.POST)
    @ApiOperation(value = "查询用户操作记录", httpMethod = "POST")
    public ModelMap list(Integer uid, @RequestParam(defaultValue = "1")int pageNum, @RequestParam(defaultValue = "10")int pageSize){
        Assert.notNull(uid, "用户id不能为空");
        System.out.println("ddd");
        PageInfo<OperatRecord> pageInfo = operatRecordService.listOperatRecord(uid, pageNum, pageSize);
        return WriteJson.successPage(pageInfo);
    }

    /**
     * 新增/修改用户基础信息
     *
     * @author uncle.quentin
     * @date   2018/12/11 14:46
     * @param   userBasic
     * @return org.springframework.ui.ModelMap
     * @version 1.0
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userBasic", value = "userBasic", required = false, dataType = "UserBasic", paramType = "body")
    })
    @ApiOperation(value = "新增或修改用户基础信息", httpMethod = "POST")
    @RequestMapping(value = "/addOrUpdateUserBasic", method = RequestMethod.POST)
    public ModelMap addOrUpdateUserBasic(UserBasic userBasic) {
        //验证数据有效性
        Assert.notNull(userBasic, "无效参数");

        //成功条数
        int successCount;
        //是否修改操作
        if (null != userBasic.getId()) {
            successCount = userBasicService.updateUserBasic(userBasic);
        } else {
            //新增用户
            Assert.notNull(userBasic.getPhone(), "手机号码不能为空");
            Assert.notNull(userBasic.getPassword(), "密码不能为空");
            //校验手机号是否已经绑定
            if (StringUtils.isNotEmpty(userBasic.getPhone())) {
                UserBasic searchParam = new UserBasic();
                searchParam.setPhone(userBasic.getPhone());
                List<UserBasic> result = userBasicService.findByCondition(searchParam);
                if (null != result && !result.isEmpty()) {
                    return WriteJson.errorWebMsg("该手机已绑定用户");
                }
            }
            userBasic.setStatus(0);
            userBasic.setType(UserBasic.TypeEnum.SYSTEM.getKey());
            successCount = userBasicService.addUserBasic(userBasic);
        }

        //返回成功条数
        return WriteJson.successData(successCount);
    }

    /**
     * 根据ID禁用或开启用户
     *
     * @param userId
     * @return org.springframework.ui.ModelMap
     * @author uncle.quentin
     * @date 2018/12/11 15:06
     * @version 1.0
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "[-1锁定, 0正常]", required = true, dataType = "int", paramType = "query")
    })
    @ApiOperation(value = "禁用或开启用户状态", httpMethod = "POST")
    @RequestMapping(value = "/disableOrOpenUserBasic", method = RequestMethod.POST)
    public ModelMap disableUserBasic(Integer userId, Integer status) {
        //验证数据有效性
        Assert.notNull(userId, "用户ID为空");
        Assert.notNull(status, "状态参数为空");

        //禁用用户状态
        UserBasic userBasic = new UserBasic();
        userBasic.setId(userId);
        userBasic.setStatus(status);
        int successCount = userBasicService.updateUserBasic(userBasic);

        //返回成功条数
        return WriteJson.successData(successCount);
    }

    @RequestMapping(value = "/listUserBank", method = RequestMethod.POST)
    @ApiOperation(value = "查询用户银卡列表", httpMethod = "POST")
    public ModelMap listUserBank(Integer uid, @RequestParam(defaultValue = "1")int pageNum, @RequestParam(defaultValue = "10")int pageSize){
        Assert.notNull(uid, "用户id不能为空");
        PageInfo pageInfo = userBankCardService.findByUid(uid, pageNum, pageSize);
        return WriteJson.successPage(pageInfo);
    }

    /**
     * 根据用户ID查询用户明细
     *
     * @author uncle.quentin
     * @date   2018/12/12 10:45
     * @param   uid
     * @return org.springframework.ui.ModelMap
     * @version 1.0
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户ID", required = true, dataType = "int", paramType = "query"),
    })
    @ApiOperation(value = "根据ID查询用户", httpMethod = "GET")
    @RequestMapping(value = "/findUserBasicById", method = RequestMethod.GET)
    public ModelMap findUserBasicById(Integer uid){
        Assert.notNull(uid, "用户id不能为空");
        UserBasic userBasic = userBasicService.findUserBasicById(uid);
        return  WriteJson.successData(userBasic);
    }
}
