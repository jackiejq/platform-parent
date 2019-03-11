package com.slljr.finance.admin.controller;

import com.slljr.finance.admin.service.LoginService;
import com.slljr.finance.common.enums.SysEnum;
import com.slljr.finance.common.pojo.model.UserBasic;
import com.slljr.finance.common.utils.WriteJson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@Api(tags = "登录接口")
public class LoginController {
    @Autowired
    LoginService loginService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ApiOperation(value = "后台管理员登录", httpMethod = "POST")
    public ModelMap login(String phone, String password, HttpServletRequest request){
        Assert.notNull(phone, "手机号码不能为空");
        Assert.notNull(password, "登录密码不能为空");
        UserBasic userBasic = loginService.login(phone, password);
        if (userBasic!=null){
            request.getSession().setAttribute(SysEnum.LOGIN_SESSION_KEY.getKey(),userBasic);
            userBasic.setPassword("");
            return WriteJson.successData(userBasic);
        }

        return WriteJson.errorWebMsg("帐号或密码错误");
    }

}
