package com.slljr.finance.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.slljr.finance.admin.service.ForumReplyService;
import com.slljr.finance.admin.service.ForumTopicService;
import com.slljr.finance.common.pojo.model.ForumReply;
import com.slljr.finance.common.pojo.vo.FroumReplyListVO;
import com.slljr.finance.common.pojo.vo.ReplyDetailsVo;
import com.slljr.finance.common.utils.WriteJson;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value="/adminReply")
@Api(tags= {"管理后台论坛评论操作的API"})
@CrossOrigin
public class ForumReplyController {

	@Autowired
	private ForumReplyService forumReplyService;
	

	@Autowired
	private ForumTopicService forumTopicService;
	
	/**
	 * 评论列表查询list
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	@RequestMapping(value="/queryReplylist",method=RequestMethod.POST)
	@ApiOperation(httpMethod = "POST", value = "评论列表查询list",notes="{'id':'帖子id'},'uid':'用户uid'}")		
	public ModelMap queryReplylist(Integer id,Integer uid,@RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize) throws InstantiationException, IllegalAccessException{
		if(id == null ) {
			return WriteJson.errorWebMsg("帖子id为空");
		}
		if(uid ==null) {
			PageInfo<FroumReplyListVO> pageInfo = forumReplyService.queryReplylist(id,pageNum, pageSize);
			return WriteJson.successPage(pageInfo);
		}else {
			PageInfo<FroumReplyListVO> pageInfo = forumReplyService.queryReplylist1(id,uid,pageNum, pageSize);
			return WriteJson.successPage(pageInfo);
		}
		
		
	}
	
	/**
	 * 评论点赞操作
	 */
	@RequestMapping(value="/replyGreat",method=RequestMethod.POST)
	@ApiOperation(httpMethod = "POST", value = "评论点赞操作",notes="{'id':'评论id','uid':'点赞用户uid','parent_reply_uid':'一级回帖人uid'}")		
	public ModelMap replyGreat(Integer id,Integer uid){
		if(id == null ) {
			return WriteJson.errorWebMsg("评论id为空");
		}
		if(uid == null ) {
			return WriteJson.errorWebMsg("点赞用户uid为空");
		};	
			try {
				forumReplyService.replyGreat(id, uid);
				return WriteJson.success();
			} catch (Exception e) {
				return WriteJson.errorWebMsg("操作失败");
			}
									
	}
	
	/**
	 * 发表评论操作
	 */
	@RequestMapping(value="/replyInfo",method=RequestMethod.POST)
	@ApiOperation(httpMethod = "POST", value = "管理员发表评论操作",notes="{'topicId':'帖子id','content':'评论内容为空','parentReplyUid':'一级评论id'}")		
	public ModelMap replyInfo(ForumReply forumReply){
		if(forumReply.getTopicId() == null ) {
			return WriteJson.errorWebMsg("帖子id为空");
		}
		if(forumReply.getContent() == null ) {
			return WriteJson.errorWebMsg("评论内容为空");
		}		
		try {
			forumReplyService.replyInfo(forumReply);
			return WriteJson.success();
		} catch (Exception e) {
			return WriteJson.errorWebMsg("操作失败");
		}		
	}
	
	/**
	 * 删除评论
	 */
	@RequestMapping(value="/deleteReply",method=RequestMethod.POST)
	@ApiOperation(httpMethod = "POST", value = "删除评论",notes="'id':'评论id'")		
	public ModelMap deleteReply(Integer id){
		if(id == null ) {
			return WriteJson.errorWebMsg("评论id为空");
		}
		try {
			forumReplyService.deleteReply(id);			
			return WriteJson.success();				
		} catch (Exception e) {
			return WriteJson.errorWebMsg("删除失败");
		}		
	}
	/**
	 * 评论详情编辑页
	 */
	@RequestMapping(value="/replyEditDetails",method=RequestMethod.POST)
	@ApiOperation(httpMethod = "POST", value = "评论详情编辑页",notes="'id':'帖子id','parentReplyUid':'一级评论id'")		
	public ModelMap replyEditDetails(Integer id,Integer parentReplyUid){
		if(id == null ) {
			return WriteJson.errorWebMsg("帖子id为空");
		}
		if(parentReplyUid !=null) {
			ReplyDetailsVo replyDetailsVo = forumReplyService.replyEditDetails(id,parentReplyUid);
			if(replyDetailsVo !=null) {
				return WriteJson.successData(replyDetailsVo);		
			}
			return WriteJson.errorWebMsg("查询无数据");
		}else {
			String content = forumTopicService.queryTopicCotent(id);
			return WriteJson.successData(content);	
		}
					
	}
}
