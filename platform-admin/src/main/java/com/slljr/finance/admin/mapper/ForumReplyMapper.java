package com.slljr.finance.admin.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.slljr.finance.common.pojo.model.ForumReply;
import com.slljr.finance.common.pojo.vo.FroumReplyListVO;
import com.slljr.finance.common.pojo.vo.ReplyDetailsVo;

public interface ForumReplyMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ForumReply record);

    int insertSelective(ForumReply record);

    ForumReply selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ForumReply record);

    int updateByPrimaryKey(ForumReply record);

    /**
             * 查询回帖列表list
     * @return
     */
	List<FroumReplyListVO> queryReplylist(@Param("id") Integer id);

	/**
	 * 发表评论
	 * @param forumReply
	 * @return
	 */
	ForumReply insertreplyInfo(ForumReply forumReply);

	/**
	 * 删除评论
	 * @param id
	 */
	void updateByStatus(Integer id);

	/**
	 * 通过评论id查找帖子id
	 * @param id
	 * @return
	 */
	int selectByTopic(Integer id);

	int queryReplyCount(Integer id);

	/**
	 * 查询单条评论详情
	 * @param id
	 * @return
	 */
	ReplyDetailsVo queryReplyDetails(Integer id,Integer parentReplyUid);

	
}