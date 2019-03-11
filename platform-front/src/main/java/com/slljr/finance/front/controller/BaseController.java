package com.slljr.finance.front.controller;

import com.slljr.finance.common.exception.InterfaceException;
import com.slljr.finance.common.pojo.vo.UserBasicVO;
import com.slljr.finance.common.redis.RedisUtil;
import com.slljr.finance.common.utils.MsgEnum;
import com.slljr.finance.common.utils.SecurityUtil;
import com.slljr.finance.front.service.UserBasicService;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static com.slljr.finance.common.constants.Constant.AUTHORIZATION;
import static com.slljr.finance.common.constants.Constant.SPACER;
import static com.slljr.finance.common.constants.Constant.TOKEN_KEY;

/**
 * @description:
 * @author: uncle.quentin.
 * @date: 2018/12/18.
 * @time: 20:37.
 */
@RestController
public class BaseController {
    private static final Logger log = LogManager.getLogger();

    @Autowired
    private UserBasicService userBasicService;

    @Autowired
    public RedisUtil redisUtil;

    /**
     * 获取用户信息
     *
     * @param request
     * @return com.slljr.finance.common.pojo.vo.UserBasicVO
     * @author uncle.quentin
     * @date 2018/12/18 20:52
     * @version 1.0
     */
    public UserBasicVO getLoginUser(HttpServletRequest request) throws InterfaceException {
        //获取Token
        String token = request.getHeader(AUTHORIZATION);

        if (StringUtils.isNotEmpty(token)) {
            Object user = redisUtil.get(TOKEN_KEY + token);
            if (null != user) {
                UserBasicVO userBasic = (UserBasicVO) user;
                if (null != userBasic) {
                    log.debug(userBasic.getPhone() + " token: " + token);
                    return userBasic;
                }
            }
        }

        throw new InterfaceException(MsgEnum.UNREGISTERED.getMsg());
    }

    /**
     * 生成Token
     *
     * @param uid 用户ID
     * @return void
     * @author uncle.quentin
     * @date 2018/12/18 20:02
     * @version 1.0
     */
    public String getToken(String uid) {
        //判断该用户是否已登录，登录则踢掉已登录用户
        Object obj = redisUtil.get(TOKEN_KEY + uid);
        if (null != obj) {
            redisUtil.del(TOKEN_KEY + uid);
            redisUtil.del(TOKEN_KEY + obj);
        }

        //生成token（前缀+uid+token）
        String uuid = UUID.randomUUID().toString();
        String token = SecurityUtil.encrypt(uid + SPACER + uuid + System.currentTimeMillis());
        //获取用户信息，存入Redis
        UserBasicVO userBasic = userBasicService.selectUserBasicById(Integer.valueOf(uid));
        //保存token,8天失效（维护两个KEY，uid作为key保证单设备登录，token作为key用于请求接口校验用户登录状态）
        redisUtil.set(TOKEN_KEY + uid, token, 691200);
        redisUtil.set(TOKEN_KEY + token, userBasic, 691200);

        return token;
    }

    /**
     * 更新用户缓存信息
     *
     * @author uncle.quentin
     * @date   2019/1/2 20:35
     * @param   request
     * @param   user
     * @return void
     * @version 1.0
     */
    public void updateUserCache(HttpServletRequest request,UserBasicVO user){
        //获取Token
        String token = request.getHeader(AUTHORIZATION);
        Long keyLifeTime = redisUtil.getExpire(TOKEN_KEY + token);
        //更新用户缓存信息
        redisUtil.set(TOKEN_KEY + token, user, keyLifeTime);
    }

    /**
     * 校验必填参数
     *
     * @param objects
     * @return void
     * @author uncle.quentin
     * @date 2018/12/26 11:06
     * @version 1.0
     */
    public void validParam(Object... objects) throws InterfaceException {
        for (Object obj : objects) {
            Assert.notNull(obj, MsgEnum.NOTPARAM.getMsg());
            //字符串校验非空
            if (obj instanceof String) {
                if (StringUtils.isEmpty(obj.toString())) {
                    throw new InterfaceException(MsgEnum.NOTPARAM);
                }
            } else if (obj instanceof List) {
                if (((List) obj).size() == 0) {
                    throw new InterfaceException(MsgEnum.NOTPARAM);
                }
            } else if (obj instanceof Map) {
                if (((Map) obj).size() == 0) {
                    throw new InterfaceException(MsgEnum.NOTPARAM);
                }
            } else if (obj instanceof Set) {
                if (((Set) obj).size() == 0) {
                    throw new InterfaceException(MsgEnum.NOTPARAM);
                }
            } else if (obj instanceof Object[]) {
                if (((Object[]) obj).length == 0) {
                    throw new InterfaceException(MsgEnum.NOTPARAM);
                }
            } else if (obj instanceof int[]) {
                if (((int[]) obj).length == 0) {
                    throw new InterfaceException(MsgEnum.NOTPARAM);
                }
            } else if (obj instanceof long[]) {
                if (((long[]) obj).length == 0) {
                    throw new InterfaceException(MsgEnum.NOTPARAM);
                }
            }

        }
    }

}
