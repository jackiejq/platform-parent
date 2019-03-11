package com.slljr.finance.forum.controller;

import com.slljr.finance.common.constants.Constant;
import com.slljr.finance.common.exception.InterfaceException;
import com.slljr.finance.common.pojo.model.ForumLikes;
import com.slljr.finance.common.pojo.vo.UserBasicVO;
import com.slljr.finance.common.utils.WriteJson;
import com.slljr.finance.forum.service.ForumLikesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @description: 点赞
 * @author: uncle.quentin.
 * @date: 2019/2/12.
 * @time: 11:48.
 */
@RestController
@RequestMapping(value = "/like")
@Api(tags = {"点赞操作API"})
public class ForumLikesController extends BaseController{

    /**
     * 点赞服务层接口
     */
    @Autowired
    private ForumLikesService forumLikesService;

    /**
     * 点赞或取消赞
     *
     * @param forumLikes
     * @param request
     * @return org.springframework.ui.ModelMap
     * @author uncle.quentin
     * @date 2019/2/12 11:52
     * @version 1.0
     */
    @ResponseBody
    @RequestMapping(value = "/addOrCancelLike", method = RequestMethod.POST)
    @ApiOperation(httpMethod = "POST", value = "用户点赞操作", notes = "{\"targetId\":\"点赞目标ID\",\"targetType\":\"点赞目标类型（1：帖子、2：回复）\"}")
    public ModelMap addOrCancelLike(@RequestBody ForumLikes forumLikes, HttpServletRequest request) throws InterfaceException {
        //校验参数
        validParam(forumLikes, forumLikes.getTargetId(), forumLikes.getTargetType());
        //获取登录用户信息
        UserBasicVO user = getLoginUser(request);
        int successCount = 0;

        //操作类型
        Integer operateType;

        //判断用户是否点赞过
        ForumLikes tempLike = new ForumLikes();
        tempLike.setUid(user.getId());
        tempLike.setTargetType(forumLikes.getTargetType());
        tempLike.setTargetId(forumLikes.getTargetId());
        List<ForumLikes> forumLikes1 = forumLikesService.selectByCondition(tempLike);
        //已经点过赞则取消赞
        if (null != forumLikes1 && !forumLikes1.isEmpty()) {
            //取消赞
            for (ForumLikes forumLikes2 : forumLikes1) {
                successCount = forumLikesService.cancelLike(forumLikes2.getId());
            }
            operateType = 2;
        } else {
            //点赞操作
            forumLikes.setUid(user.getId());
            successCount = forumLikesService.like(forumLikes);
            operateType = 1;
        }

        if (successCount > 0) {
            //刷新缓存
            forumLikesService.refreshLikeCountCache(operateType, forumLikes);

            //获取最新点赞量
            String likeCacheKey = String.format("%s_%s_%s", Constant.POST_LIKE, forumLikes.getTargetType(), forumLikes.getTargetId());
            Object likeObj = redisUtil.get(likeCacheKey);
            Integer likeCount = Integer.valueOf(null == likeObj ? "0" : likeObj.toString());

            return WriteJson.successData(likeCount);
        } else {
            return WriteJson.errorWebMsg("发帖失败");
        }
    }

}
