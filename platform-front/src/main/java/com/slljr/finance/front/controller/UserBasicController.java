package com.slljr.finance.front.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.slljr.finance.common.enums.SysConfigEnum;
import com.slljr.finance.common.exception.InterfaceException;
import com.slljr.finance.common.pojo.model.UserBalanceDetail;
import com.slljr.finance.common.pojo.model.UserBasic;
import com.slljr.finance.common.pojo.vo.*;
import com.slljr.finance.common.utils.JsonUtil;
import com.slljr.finance.common.utils.MathUtils;
import com.slljr.finance.common.utils.WriteJson;
import com.slljr.finance.front.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @description: 基础用户信息
 * @author: uncle.quentin.
 * @date: 2018/12/12.
 * @time: 14:50.
 */
@RestController
@RequestMapping(value = "/userBasic")
@Api(tags={"基础用户信息"})
public class UserBasicController extends BaseController{

    @Autowired
    private UserBasicService userBasicService;
    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private UserLoginService userLoginService;
    @Autowired
    UserBalanceDetailService userBalanceDetailService;
    @Autowired
    SysConfigService sysConfigService;

    
    private static final Logger log = LogManager.getLogger();
    /**
     * 邀请好友主页
     */
    @RequestMapping(value="/getInviteUsersInfo",method=RequestMethod.POST)
    @ApiOperation(httpMethod = "POST", value = "查询用户下所有邀请好友信息",notes="{'month':'日期筛选[格式:201812]','pageNum':'pageNum','pageSize':'pageSize'}")
    @ResponseBody
    public ModelMap getInviteUsersInfo(@RequestBody UserFriendsVO userFriendsVO,HttpServletRequest request) throws InterfaceException {
        //获取登录用户信息
        UserBasicVO user = getLoginUser(request);
        log.info("查询用户下所有邀请好友信息接口入参：" + JsonUtil.getJson(userFriendsVO));
        //获取用户类型[0用户,1代理,2系统用户]
        Integer type = user.getType();
        if(type == null) {
        	return WriteJson.errorWebMsg("用户类型有误");
        }
	    JSONObject resultObj = new JSONObject();
    	//查询用户累计邀请好友数量
        Integer numberTotal = userBasicService.totalNumber(user.getId());
		resultObj.put("numberTotal", numberTotal);
		//查询本月邀请好友
		Integer monthTotal = userBasicService.monthTotal(user.getId());
        //本月邀请好友
		resultObj.put("monthTotal", monthTotal);

        //查询本月佣金数量       
        UserBalanceDetail userAccountmMonth = userBasicService.usercCommission(user.getId());
        Double monthCashBalance;
        if(userAccountmMonth ==null || null == userAccountmMonth.getNumber()) {
        	monthCashBalance=0.00;
        }else {
        	monthCashBalance = userAccountmMonth.getNumber();
        }
        
        //查询用户累计佣金
        UserBalanceDetail userAccountmCount = userBasicService.usercCommissionCount(user.getId());
        Double cashBalanceCount = 0.00;  
        if(userAccountmCount ==null ) {
        	cashBalanceCount=0.00;
        }else {
        	cashBalanceCount = userAccountmCount.getNumber();
        }
        		
        //查询好友手机/注册日期/佣金
		userFriendsVO.setId(user.getId());
		userFriendsVO.setType(type);
		PageInfo<UserFriendsVO> userFriends = userBasicService.queryFriendsInfo(userFriendsVO);
        //查询好友手机/注册日期/佣金
        resultObj.put("data", userFriends);
        //用户累计佣金
		resultObj.put("cashBalanceCount", cashBalanceCount);
        //本月佣金数量
		resultObj.put("monthCashBalance", monthCashBalance);
        return WriteJson.successData(resultObj);      
    }
    
    /**
     * @author lxk
              * 任务中心用户表头(积分和金币查询)
     */
    @RequestMapping(value="/getUserTask",method=RequestMethod.POST)
    @ApiOperation(httpMethod = "POST", value = "任务中心用户表头(积分和金币查询)")
    @ResponseBody
    public ModelMap getUserTask(HttpServletRequest request) throws InterfaceException {
        //获取登录用户信息
        UserBasicVO user = getLoginUser(request);
        UserTaskAccountVO  userTaskAccountVO= userBasicService.getUserTask(user.getId());
        if (null != userTaskAccountVO) {
            return WriteJson.successData(userTaskAccountVO);
        } else {
            return WriteJson.errorWebMsg("数据为空");
        }
    }
    
    /**
     * 修改用户信息ok
     * @author uncle.quentin
     * @date   2019/1/22 19:04
     * @param   userBasic
     * @param   request
     * @return org.springframework.ui.ModelMap
     * @version 1.0
     */
    @RequestMapping(value="/updateUserInfo",method=RequestMethod.POST)
    @ApiOperation(httpMethod = "POST", value = "修改用户信息",notes="{'nickName':'昵称','headImg':'头像'}")
    @ResponseBody
    public ModelMap updateUserInfo(@RequestBody UserBasic userBasic,HttpServletRequest request) throws InterfaceException {
        //校验参数
        validParam(userBasic);
        log.info("修改用户信息接口入参：" + JsonUtil.getJson(userBasic));

        //获取登录用户信息
        UserBasicVO user = getLoginUser(request);

        //重新赋值，避免多传参数
        UserBasicVO param = new UserBasicVO();
        param.setId(user.getId());
        if (StringUtils.isNotEmpty(userBasic.getNickName())) {
            param.setNickName(userBasic.getNickName());
        }
        if (StringUtils.isNotEmpty(userBasic.getHeadImg())) {
            param.setHeadImg(userBasic.getHeadImg());
        }

        Integer num = userBasicService.updateUserInfo(param);
        if(num != null && num > 0 ) {
            //重新获取一遍数据
            UserBasicVO userBasicRefresh = userBasicService.selectUserBasicById(user.getId());
            //更新用户缓存信息
            updateUserCache(request, userBasicRefresh);

            return WriteJson.success();
        }else {
            return WriteJson.errorWebMsg("修改失败");
        }
    }  
    
    /*
     * @author lxk
              * 通过手机号码查询详情OK
     */
    @RequestMapping(value="/queryFriendsByPhone",method=RequestMethod.POST)
    @ApiOperation(httpMethod = "POST", value = "通过手机号码查询详情",notes="{'phone':'手机号码'}")
    @ResponseBody
    public ModelMap queryFriendsByPhone(@RequestBody UserFriendsVO userFriends,HttpServletRequest request) throws InterfaceException {
        //获取登录用户信息
        UserBasicVO user = getLoginUser(request);
        log.info("通过手机号码查询详情接口入参：" + JsonUtil.getJson(userFriends));
        userFriends.setId(user.getId());
        List<UserFriendsVO> userFriendsVO = userBasicService.queryFriendsByPhone(userFriends);       
        if(userFriendsVO != null && !userFriendsVO.isEmpty() && userFriendsVO.toString().length() !=0) {
        	 return WriteJson.successData(userFriendsVO);
        }else {
        	 return WriteJson.successData(new ArrayList<>());        
        }                             
    } 
    
    /*
     * @author lxk
              * 好友明细ok
     */
    @RequestMapping(value="/queryFriendsDetail",method=RequestMethod.POST)
    @ApiOperation(httpMethod = "POST", value = "好友明细",notes="{'phone':'手机号码'}")
    @ResponseBody
    public ModelMap queryFriendsDetail(@RequestBody UserFriendsVO userFriends,HttpServletRequest request) throws InterfaceException {
        //获取登录用户信息
        UserBasicVO user = getLoginUser(request);
        log.info("查询好友明细接口入参：" + JsonUtil.getJson(userFriends)); 
        userFriends.setId(user.getId());
        JSONObject resultObj = new JSONObject();          
        Integer type = user.getType();//获取用户类型[0用户,1代理,2系统用户]              
        if(type == 0 || type == null) {
        	return WriteJson.errorWebMsg("对非代理用户未开放此功能");
        }
        List<UserFriendsVO> userFriendsVO = userBasicService.queryFriendsDetail(userFriends);
        double totalSum = 0.00;//总佣金
        if(userFriendsVO !=null && !userFriendsVO.isEmpty() ) {
        	for (UserFriendsVO userFriendsVO2 : userFriendsVO) {
        		double sDouble = userFriendsVO2.getAgencyProfit();
        		totalSum = MathUtils.add(sDouble, totalSum,2);
        		//totalSum += userFriendsVO2.getAgencyProfit();       		
			} 
        	resultObj.put("totalSum", totalSum); //总佣金 
        	resultObj.put("userFriendsVO", userFriendsVO);//列表  
        }else {  
        	resultObj.put("totalSum", totalSum);  
        	resultObj.put("userFriendsVO", new ArrayList<>());  
		}       
        UserBasic queryInfo = new UserBasic();
        Integer id = user.getId();
        String phone = userFriends.getPhone();
        queryInfo = userBasicService.queryInfo(id,phone); 
        if(queryInfo !=null) {        	
        	resultObj.put("queryInfo", queryInfo); //好友电话,注册日期 
        }else {
        	resultObj.put("queryInfo", new UserBasic());  
		}                
        resultObj.put("userFriendsVO", userFriendsVO);
        return WriteJson.successData(resultObj);                   
    }
    
    /**
     * @author lxk
              * 佣金详情
     * ok
     */
    @RequestMapping(value="/queryCommissionDetail",method=RequestMethod.POST)
    @ApiOperation(httpMethod = "POST", value = "佣金详情",notes="{\"startTime\":\"开始时间【格式：yyyy-MM-dd HH:mm:ss:】\",\"endTime\":\"结束时间【格式：yyyy-MM-dd HH:mm:ss:】\",\"pageNum\":\"当前页码\",\"pageSize\":\"每页数\"}")
    @ResponseBody
    public ModelMap queryCommissionDetail(@RequestBody UserFriendsVO userFriends,HttpServletRequest request) throws InterfaceException {
        //获取登录用户信息
        UserBasicVO user = getLoginUser(request);
        log.info("佣金详情接口入参：{}", JsonUtil.getJson(userFriends));
        //获取用户类型[0用户,1代理,2系统用户]
        Integer type = user.getType();

        if(UserBasic.TypeEnum.AGENT.getKey() != type) {
        	return WriteJson.errorWebMsg("对非代理用户未开放此功能");
        }
        //设置当前用户ID参数
        userFriends.setId(user.getId());

        //好友代偿明细
        PageInfo<UserFriendsVO> userFriendsVO = userBasicService.queryCommissionDetail(userFriends);

        //总佣金、总人数
        Map<String, Object> sumCommission = userBasicService.querySumCommission(userFriends);

        JSONObject resultObj = new JSONObject();
        //邀请总人数
        resultObj.put("totalnumber", sumCommission.get("totalNumber"));
        //总佣金
        resultObj.put("totalsum", sumCommission.get("agencyProfit"));
        //列表
        resultObj.put("userFriendsVO", userFriendsVO);
        return WriteJson.successData(resultObj);                   
    }
    

    /**
     * 邀请好友信息
     *
     * @author uncle.quentin
     * @date   2019/1/14 11:59
     * @return org.springframework.ui.ModelMap
     * @version 1.0
     */
    @RequestMapping(value="/queryInviteFriendsInfo",method=RequestMethod.POST)
    @ApiOperation(httpMethod = "POST", value = "邀请好友信息")
    @ResponseBody
    public ModelMap getInviteFriendsInfo(HttpServletRequest request) throws InterfaceException {
    	UserBasicVO user = getLoginUser(request);
        Integer uid = user.getId();
        validParam(uid);

        //获取用户邀请好友信息→累计邀请好友→累计获取佣金→二维码连接→广告图片
        InviteFriendsVO commissions = userAccountService.selectInviteFriendsInfo(uid);
        //转字符串,便于修改文案
        List<String> noticList = new ArrayList<>();
        userBalanceDetailService.selectBalanceTopNearlyOneMonth().forEach(bd->{
            StringBuffer buffer = new StringBuffer();
            buffer.append("恭喜")
                    .append(" ").append(bd.getPhone().substring(0, 3)).append("****").append(bd.getPhone().substring(7, 11)).append(" ")
                    .append("本月获得").append(bd.getNumber()).append("元红包");
            noticList.add(buffer.toString());
        });

        JSONObject data = new JSONObject();
        //shareTitle/shareSummary/shareLogo
        JSONObject shareInfo = JSON.parseObject(sysConfigService.selectByKey(SysConfigEnum.SHARE_CONTENT_CONFIG).getSysValue());
        shareInfo.put("shareUrl", userLoginService.getShareUrl(uid));

        //二维码链接为空，生成并更新二维码链接到用户信息
        if (StringUtils.isEmpty(user.getShareQrUrl())) {
            UserBasic updateUser = userLoginService.updateUserShareQr(user);
            shareInfo.put("shareQrUrl",updateUser.getShareQrUrl());
        } else {
            shareInfo.put("shareQrUrl",user.getShareQrUrl());
        }



        data.put("shareInfo", shareInfo);
        data.put("noticList", noticList);
        data.put("commissions", commissions);
        data.put("bgImageUrl", sysConfigService.selectByKey(SysConfigEnum.INVITE_FRIENDS_BACKGROUND_IMG).getSysValue());
        return WriteJson.successData(data);
    }


    /**
     * 好友佣金报表统计
     * @throws Exception 
     */
    @RequestMapping(value="/queryReportTotal",method=RequestMethod.POST)
    @ApiOperation(httpMethod = "POST", value = "好友佣金报表统计 ",notes="")
    @ResponseBody
    public ModelMap queryCommissionReport(HttpServletRequest request) throws Exception {
        //获取登录用户信息
        UserBasicVO user = getLoginUser(request); 

        List<UserReportTotalVO> userReportTotalVO = userBasicService.queryCommissionReport(user.getId());
        if(userReportTotalVO !=null && !userReportTotalVO.isEmpty()) {
        	return WriteJson.successData(userReportTotalVO);     
        }      
        return WriteJson.success();                   
    }
    
     
    
}
