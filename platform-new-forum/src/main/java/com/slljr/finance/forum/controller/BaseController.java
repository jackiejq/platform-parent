package com.slljr.finance.forum.controller;

import com.slljr.finance.common.enums.DataStatusEnum;
import com.slljr.finance.common.exception.InterfaceException;
import com.slljr.finance.common.pojo.model.UserBasic;
import com.slljr.finance.common.pojo.vo.UserBasicVO;
import com.slljr.finance.common.redis.RedisUtil;
import com.slljr.finance.common.utils.MsgEnum;
import com.slljr.finance.forum.service.UserBasicService;
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

import static com.slljr.finance.common.constants.Constant.AUTHORIZATION;
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
    public RedisUtil redisUtil;

    @Autowired
    private UserBasicService userBasicService;

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

    /**
     * 校验用户是否可以操作论坛
     *
     * @author uncle.quentin
     * @date   2019/3/1 16:31
     * @param   uid
     * @return boolean
     * @version 1.0
     */
    public void validUserForumOperateStatus(Integer uid) throws InterfaceException {
        UserBasic userbasic = userBasicService.selectUserBasicById(uid);

        if (null != userbasic && DataStatusEnum.INVALID.getKey() == userbasic.getForumStatus()) {
            throw new InterfaceException(MsgEnum.NOT_ALLOW_OPERATE);
        }

    }

}
