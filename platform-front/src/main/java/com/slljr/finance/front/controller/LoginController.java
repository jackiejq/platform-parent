package com.slljr.finance.front.controller;

import com.alibaba.fastjson.JSONObject;
import com.slljr.finance.common.exception.InterfaceException;
import com.slljr.finance.common.pojo.model.UserBasic;
import com.slljr.finance.common.pojo.model.UserOauth;
import com.slljr.finance.common.pojo.vo.UserBasicVO;
import com.slljr.finance.common.utils.JsonUtil;
import com.slljr.finance.common.utils.MsgEnum;
import com.slljr.finance.common.utils.WriteJson;
import com.slljr.finance.front.config.NoLoginAnnotation;
import com.slljr.finance.front.service.SmsService;
import com.slljr.finance.front.service.UserBasicService;
import com.slljr.finance.front.service.UserLoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static com.slljr.finance.common.constants.Constant.TOKEN_KEY;

@RestController
@Api(tags = "登录API")
public class LoginController extends BaseController {

    private static final Logger log = LogManager.getLogger();

    @Autowired
    UserLoginService userLoginService;

    @Autowired
    private UserBasicService userBasicService;

    @Autowired
    private SmsService smsService;

    /**
     * 第三方应用授权登录
     *
     * @param param {\"uid\":\"用户id\",\"openId\":\"第三方唯一id\",\"nickName\":\"昵称\"," "\"headImg\":\"头像链接\",\"sex\":\"1男,2女\",\"target\":\"第三方[1微信/2QQ/3alipay/4新浪]\"}
     * @return org.springframework.ui.ModelMap
     * @author uncle.quentin
     * @date 2018/12/20 18:57
     * @version 1.0
     */
    @RequestMapping(value = "/oauth/login", method = RequestMethod.POST)
    @ApiOperation(httpMethod = "POST", value = "第三方应用授权登录", notes = "参数：{\"uid\":\"用户id\",\"openId\":\"第三方唯一id\",\"nickName\":\"昵称\"," +
            "\"headImg\":\"头像链接\",\"sex\":\"1男,2女\",\"target\":\"第三方[1微信/2QQ/3alipay/4新浪]\"}")
    @NoLoginAnnotation
    public ModelMap oauthLogin(@RequestBody UserOauth userOauth) throws InterfaceException {
        //校验参数
        validParam(userOauth);

        Assert.notNull(userOauth.getOpenId(), "第三方用户id不能为空");
        Assert.notNull(userOauth.getTarget(), "第三方标识不能为空");
        UserBasic res = userLoginService.oauthLogin(userOauth);
        //生成Token
        if (null != res) {
            String token = getToken(String.valueOf(res.getId()));
            //获取返回数据
            return getResult(res, token);
        } else {
            return WriteJson.errorWebMsg("登录失败");
        }
    }

    /**
     * 短信验证码登录
     *
     * @param param {"phone":"手机号","smscode":"验证码"}
     * @return org.springframework.ui.ModelMap
     * @author uncle.quentin
     * @date 2018/12/20 18:56
     * @version 1.0
     */
    @RequestMapping(value = "/sms/login", method = RequestMethod.POST)
    @ApiOperation(httpMethod = "POST", value = "短信验证码登录", notes = "{\"phone\":\"手机号\",\"smscode\":\"验证码\"}")
    @NoLoginAnnotation
    public ModelMap smsLogin(@RequestBody String param) {
        JSONObject paramObj = JsonUtil.strToJson(param);
        String phone = paramObj.getString("phone");
        String smscode = paramObj.getString("smscode");
        Assert.notNull(phone, "手机号码不能为空");
        Assert.notNull(smscode, "短信验证码不能为空");
        //这里校验验证码
        boolean istrue = smsService.checkIsCorrectCode(phone, smscode, 1);
        if (istrue) {
            //短信登录
            UserBasic res = userLoginService.smsLogin(phone);
            //生成Token
            if (null != res) {
                String token = getToken(String.valueOf(res.getId()));
                //获取返回数据
                return getResult(res, token);
            } else {
                return WriteJson.errorWebMsg("登录失败");
            }
        } else {
            return WriteJson.errorWebMsg("登录失败,验证码失效");
        }

    }

    /**
     * {"phone":"手机号","password":"密码"}
     *
     * @param param
     * @return org.springframework.ui.ModelMap
     * @author uncle.quentin
     * @date 2018/12/20 18:56
     * @version 1.0
     */
    @RequestMapping(value = "/auth/login", method = RequestMethod.POST)
    @ApiOperation(httpMethod = "POST", value = "帐号密码登录", notes = "{\"phone\":\"手机号\",\"password\":\"密码\"}")
    @NoLoginAnnotation
    public ModelMap authLogin(@RequestBody String param) {
        JSONObject paramObj = JsonUtil.strToJson(param);

        String phone = paramObj.getString("phone");
        String password = paramObj.getString("password");
        Assert.notNull(phone, "手机号码不能为空");
        Assert.notNull(password, "登录密码不能为空");
        UserBasic res = userLoginService.authLogin(phone, password);

        //生成Token
        if (null != res) {
            String token = getToken(String.valueOf(res.getId()));
            //获取返回数据
            return getResult(res, token);
        } else {
            return WriteJson.errorWebMsg("登录失败");
        }
    }

    /**
     * 构建返回具体信息
     *
     * @param user
     * @param token
     * @return java.lang.Object
     * @author uncle.quentin
     * @date 2018/12/18 21:02
     * @version 1.0
     */
    private ModelMap getResult(UserBasic user, String token) {
        UserBasicVO userBasic = userBasicService.selectPortionUserBasicById(user.getId());
        return WriteJson.successData(userBasic, token, "登录成功");
    }

    /**
     * 发送短信验证码
     *
     * @return org.springframework.ui.ModelMap
     * @author uncle.quentin
     * @date 2018/12/20 18:38
     * @version 1.0
     */
    @RequestMapping(value = "/sms/sendSms", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(httpMethod = "POST", value = "发送短信验证码", notes = "{\"phone\":\"手机号\",\"type\":\"验证码类型：1：登录 2：修改银行卡预留手机号 3：注册\"}")
    @NoLoginAnnotation
    public ModelMap sendSms(@RequestBody String param) throws InterfaceException {
        JSONObject obj = JsonUtil.strToJson(param);
        String phone = obj.getString("phone");
        Integer type = obj.getInteger("type");
        Assert.notNull(phone, "手机号为空");
        Assert.notNull(type, "类型为空");

        //发送验证码
        if (type == 3){
            if (userBasicService.selectByPhone(phone) != null){
                return WriteJson.errorWebMsg("该手机号码已注册!");
            }else{
                //注册登录共用一种验证码
                type = 1;
            }
        }

        String code = smsService.sendMessage(phone, type);
        if (StringUtils.isNotEmpty(code)) {
            return WriteJson.success();
        } else {
            return WriteJson.errorWebMsg("验证码发送失败");
        }
    }

    /**
     * 登出
     *
     * @param request
     * @return org.springframework.ui.ModelMap
     * @author uncle.quentin
     * @date 2019/1/8 16:12
     * @version 1.0
     */
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(httpMethod = "POST", value = "退出登录")
    public ModelMap logout(HttpServletRequest request) throws InterfaceException {
        //获取登录用户信息
        UserBasicVO user = getLoginUser(request);
        //删除缓存token信息
        Object obj = redisUtil.get(TOKEN_KEY + user.getId());
        if (null != obj) {
            redisUtil.del(TOKEN_KEY + user.getId());
            redisUtil.del(TOKEN_KEY + obj);
        }

        return WriteJson.success();
    }

    /**
     * 用户注册
     *
     * @author uncle.quentin
     * @date   2019/1/13 13:23
     * @param   param
     * @return org.springframework.ui.ModelMap
     * @version 1.0
     */
    @RequestMapping(value = "/userRegister", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(httpMethod = "POST", value = "用户注册", notes = "{\"phone\":\"手机号\",\"smscode\":\"验证码\",\"refererUid\":\"邀请人ID\"}")
    @NoLoginAnnotation
    public ModelMap userRegister(@RequestBody String param) throws InterfaceException {
        JSONObject paramObj = JsonUtil.strToJson(param);
        String phone = paramObj.getString("phone");
        String smscode = paramObj.getString("smscode");
        Integer refererUid = paramObj.getInteger("refererUid");
        Assert.notNull(phone, "手机号码不能为空");
        Assert.notNull(smscode, "短信验证码不能为空");
        Assert.notNull(refererUid, "邀请人不能为空");
        //这里校验验证码
        boolean istrue = smsService.checkIsCorrectCode(phone, smscode, 1);
        if (istrue) {
            //用户注册
            userLoginService.userRegister(phone, refererUid);
            return WriteJson.success();
        } else {
            return WriteJson.errorWebMsg("注册失败,验证码无效");
        }
    }

}
