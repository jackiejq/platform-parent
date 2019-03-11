package com.slljr.finance.admin.controller;

import com.slljr.finance.common.exception.InterfaceException;
import com.slljr.finance.common.utils.MsgEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @description:
 * @author: uncle.quentin.
 * @date: 2018/12/18.
 * @time: 20:37.
 */
@RestController
public class BaseController {
    /**
     * 校验必填参数
     *
     * @param objects
     * @return void
     * @author uncle.quentin
     * @date 2018/12/26 11:06
     * @version 1.0
     */
    public void validParamNotNull(Object... objects) throws InterfaceException {
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
