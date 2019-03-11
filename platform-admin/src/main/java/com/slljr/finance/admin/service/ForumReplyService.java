package com.slljr.finance.admin.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.slljr.finance.admin.mapper.ForumGreatMapper;
import com.slljr.finance.admin.mapper.ForumReplyMapper;
import com.slljr.finance.admin.mapper.ForumTopicMapper;
import com.slljr.finance.common.pojo.model.ForumGreat;
import com.slljr.finance.common.pojo.model.ForumReply;
import com.slljr.finance.common.pojo.model.ForumTopic;
import com.slljr.finance.common.pojo.vo.FroumReplyListVO;
import com.slljr.finance.common.pojo.vo.ReplyDetailsVo;
import com.slljr.finance.common.utils.ConvertUtils;

@Service
public class ForumReplyService {

	@Autowired
	private ForumTopicMapper forumTopicMapper;
	
	@Autowired
	private ForumReplyMapper forumReplyMapper;
	
	@Autowired
	private ForumGreatMapper forumGreatMapper;
	
	public PageInfo<FroumReplyListVO> queryReplylist(Integer id, int pageNum, int pageSize) {
		 PageHelper.startPage(pageNum, pageSize);
	        List<FroumReplyListVO> list = forumReplyMapper.queryReplylist(id);
	        for (FroumReplyListVO froumReplyListVO : list) {
	        	froumReplyListVO.setFlag("flase");
			}
	        return new PageInfo<>(list);
	}
	

	/**
	 * 登陆用户获取回帖列表
	 * @param id
	 * @param uid
	 * @param pageNum
	 * @param pageSize
	 * @return
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	@Transactional
	public PageInfo<FroumReplyListVO> queryReplylist1(Integer id, Integer uid, int pageNum, int pageSize) throws InstantiationException, IllegalAccessException {
		 PageHelper.startPage(pageNum, pageSize);
		 Integer parentReplyUid = uid;
	         List<FroumReplyListVO> list = forumReplyMapper.queryReplylist(id);
	         if(!list.isEmpty() || list.size() > 0) {
	        	 List<Map<String, Object>> list2 = ConvertUtils.objectsToMaps(list);      
	  	         for(int i=0;i<list2.size();i++) { 
	  	        	 int  ids = (int) list2.get(i).get("id");       	  
	  	        	  List<ForumGreat> list1 = forumGreatMapper.selectByInfo(ids, parentReplyUid);       
	  	        	if(list1 != null && list1.size() > 0) {       		        		 
	  	        		list2.get(i).put("flag", "true");       		
	  	        	 }else {
	  	        		list2.get(i).put("flag", "false");
	  	        	 }
	  	         }         
	  			list = ConvertUtils.mapsToObjects(list2, FroumReplyListVO.class); 
	         }	      
		return new PageInfo<>(list);							
	}

	
	@Transactional(rollbackFor = Exception.class)
	public void replyGreat(Integer id, Integer uid) {
		try {
			//查询是否有该用户对该文章的点赞记录
			Integer parentReplyUid = uid;
			List<ForumGreat> list = forumGreatMapper.selectByInfo(id, parentReplyUid);
			if (list != null && list.size() > 0) {
				//如果找到了这条记录，则删除该记录，同时文章的点赞数减1
				ForumGreat great = list.get(0);
				//删除记录				
				forumGreatMapper.deleteByPrimaryKey(great.getId());
				ForumReply forumReply = forumReplyMapper.selectByPrimaryKey(id);
				//文章点赞数减1
				int num = (int)forumReply.getLikeCount();
				forumReply.setLikeCount(num - 1);
				forumReply.setUpdateTime(new Date());
				forumReplyMapper.updateByPrimaryKeySelective(forumReply);
			} else {
				ForumGreat great = new ForumGreat();
				great.setReplyId(id);
				great.setParentReplyUid(uid);
				great.setType(2);
				great.setLikeCount(1);
				great.setCreateTime(new Date());
				great.setUpdateTime(new Date());
				forumGreatMapper.insertSelective(great);
				//文章点赞数加1
				ForumReply forumReply = forumReplyMapper.selectByPrimaryKey(id);
				int num = (int)forumReply.getLikeCount();
				forumReply.setLikeCount(num + 1);
				forumReplyMapper.updateByPrimaryKeySelective(forumReply);
			} 
		} catch (Exception e) {
			
		}		
	}

	/**
	 * 查询评论是否点赞
	 * @param id
	 * @param uid
	 * @return
	 */
	/*public ForumGreat selectByInfo(int id, int uid) {
		ForumGreat forumGreat = forumGreatMapper.selectByGreat(id, uid);
		return forumGreat;
	}*/

	/**
	 * 发表评论
	 * @param forumReply
	 * @return
	 */
	@Transactional
	public void replyInfo(ForumReply forumReply) {
		forumReply.setCreateTime(new Date());
		forumReply.setUpdateTime(new Date());
		forumReply.setLikeCount(0);
		forumReply.setStatus(0);
		ForumTopic forumTopic = forumTopicMapper.selectByPrimaryKey(forumReply.getTopicId());
		if(forumTopic !=null) {
			int num = (int)forumTopic.getReplyCount();	
			forumTopic.setReplyCount(num+1);
			forumTopic.setUpdateTime(new Date());
			forumTopicMapper.updateByPrimaryKeySelective(forumTopic);
		}		
		forumReplyMapper.insertSelective(forumReply);				
	}

	/**
	 * 删除评论
	 * @param id
	 */
	@Transactional
	public void deleteReply(Integer id) {
		ForumReply forumReply  = new ForumReply();
		forumReply = forumReplyMapper.selectByPrimaryKey(id);
		if(forumReply !=null) {
			ForumTopic forumTopic = forumTopicMapper.selectByPrimaryKey(forumReply.getTopicId());
			if(forumTopic !=null) {
				int num = (int)forumTopic.getReplyCount();		
				int countReply = forumReplyMapper.queryReplyCount(id);						
				forumTopic.setReplyCount(num-countReply);
				forumTopic.setUpdateTime(new Date());
				forumTopicMapper.updateByPrimaryKeySelective(forumTopic);
			}
		}		
		forumReplyMapper.updateByStatus(id);	
		
	}


	/**
	 * 查询评论详情
	 * @param id
	 */
	
	public ReplyDetailsVo replyEditDetails(Integer id,Integer parentReplyUid) {
		ReplyDetailsVo replyDetailsVo = forumReplyMapper.queryReplyDetails(id,parentReplyUid);		
		return replyDetailsVo;
	}


}
