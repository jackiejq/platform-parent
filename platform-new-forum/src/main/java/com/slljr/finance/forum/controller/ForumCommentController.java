package com.slljr.finance.forum.controller;

import com.alibaba.fastjson.JSONObject;
import com.slljr.finance.common.exception.InterfaceException;
import com.slljr.finance.common.pojo.model.ForumComment;
import com.slljr.finance.common.pojo.vo.UserBasicVO;
import com.slljr.finance.common.utils.JsonUtil;
import com.slljr.finance.common.utils.MsgEnum;
import com.slljr.finance.common.utils.WriteJson;
import com.slljr.finance.forum.service.ForumCommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @description:
 * @author: uncle.quentin.
 * @date: 2019/2/12.
 * @time: 14:10.
 */
@RestController
@RequestMapping(value = "/comment")
@Api(tags = {"评论、回复操作API"})
public class ForumCommentController extends BaseController {

    /**
     * 评论回复服务接口
     */
    @Autowired
    private ForumCommentService forumCommentService;


    /**
     * 评论
     *
     * @param forumComment
     * @param request
     * @return org.springframework.ui.ModelMap
     * @author uncle.quentin
     * @date 2019/2/13 9:40
     * @version 1.0
     */
    @ResponseBody
    @RequestMapping(value = "/addComment", method = RequestMethod.POST)
    @ApiOperation(httpMethod = "POST", value = "用户评论（帖子、回复评论）", notes = "{\"postId\":\"帖子id\",\"targetId\":\"（评论||回复）目标ID\",\"targetType\":\"目标类型（1：帖子、2：评论）\",\"content\":\"回帖内容\"}")
    public ModelMap addComment(@RequestBody ForumComment forumComment, HttpServletRequest request) throws InterfaceException {
        //校验参数有效性
        validParam(forumComment, forumComment.getPostId(), forumComment.getTargetId(), forumComment.getTargetType(), forumComment.getContent());

        //获取登录用户信息
        UserBasicVO user = getLoginUser(request);
        forumComment.setUid(user.getId());

        //校验用户论坛状态
        validUserForumOperateStatus(user.getId());

        //添加评论
        int successCount = forumCommentService.comment(forumComment);
        if (successCount > 0) {
            //刷新评论量缓存
            forumCommentService.refreshLikeCountCache(1, forumComment);

            return WriteJson.successData(forumComment);
        } else {
            return WriteJson.errorWebMsg("评论失败");
        }
    }

    /**
     * 用户删除评论
     *
     * @param param
     * @param request
     * @return org.springframework.ui.ModelMap
     * @author uncle.quentin
     * @date 2019/2/13 9:46
     * @version 1.0
     */
    @ResponseBody
    @RequestMapping(value = "/deleteComment", method = RequestMethod.POST)
    @ApiOperation(httpMethod = "POST", value = "用户删除评论操作", notes = "{\"id\":\"评论id\"}")
    public ModelMap deleteComment(@RequestBody String param, HttpServletRequest request) throws InterfaceException {
        //获取评论ID
        JSONObject obj = JsonUtil.strToJson(param);
        Integer id = obj.getInteger("id");
        //校验参数
        validParam(id);

        ForumComment forumComment = forumCommentService.selectCommentById(id);
        //判断是否当前用户的评论
        {
            //获取登录用户信息
            UserBasicVO user = getLoginUser(request);
            if (null != forumComment) {
                if (!forumComment.getUid().equals(user.getId())) {
                    throw new InterfaceException(MsgEnum.NOT_ALLOW_DELETE_COMMENT);
                }
            } else {
                throw new InterfaceException(MsgEnum.DATA_NULL);
            }
        }

        //删除操作
        int successCount = forumCommentService.deleteComment(id);
        if (successCount > 0) {
            //刷新评论量缓存
            forumCommentService.refreshLikeCountCache(2, forumComment);

            return WriteJson.success();
        } else {
            return WriteJson.errorWebMsg("删除评论失败");
        }
    }

}
