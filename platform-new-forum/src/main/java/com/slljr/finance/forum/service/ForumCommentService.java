package com.slljr.finance.forum.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.slljr.finance.common.constants.Constant;
import com.slljr.finance.common.enums.DataStatusEnum;
import com.slljr.finance.common.pojo.model.ForumComment;
import com.slljr.finance.common.pojo.model.ForumLikes;
import com.slljr.finance.common.pojo.model.ForumParentChild;
import com.slljr.finance.common.pojo.vo.BasePageVO;
import com.slljr.finance.common.pojo.vo.ForumCommentVO;
import com.slljr.finance.common.pojo.vo.ForumPostDetailVO;
import com.slljr.finance.common.pojo.vo.ForumPostVO;
import com.slljr.finance.common.redis.RedisUtil;
import com.slljr.finance.forum.mapper.ForumCommentMapper;
import com.slljr.finance.forum.mapper.ForumLikesMapper;
import com.slljr.finance.forum.mapper.ForumPostMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description: 评论、回复服务层
 * @author: uncle.quentin.
 * @date: 2019/2/12.
 * @time: 10:36.
 */
@Service
public class ForumCommentService {
    /**
     * 评论、回复Mapper
     */
    @Autowired
    private ForumCommentMapper forumCommentMapper;
    /**
     * 赞Mapper
     */
    @Autowired
    private ForumLikesMapper forumLikesMapper;

    @Autowired
    private ForumPostMapper forumPostMapper;

    @Autowired
    private ForumParentChildService forumParentChildService;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 评论
     *
     * @param forumComment
     * @return int
     * @author uncle.quentin
     * @date 2019/2/12 11:03
     * @version 1.0
     */
    public int comment(ForumComment forumComment) {
        //获取评论量，楼层=评论量+1
        int successCount;
        {
            String commentCacheKey = String.format("%s_%s_%s", Constant.POST_COMMENT, 1, forumComment.getPostId());
            Object commentObj = redisUtil.get(commentCacheKey);
            Integer commentCount = Integer.valueOf(null == commentObj ? "0" : commentObj.toString());
            forumComment.setIndexes(commentCount + 1);

            forumComment.setStatus(DataStatusEnum.NORMAL.getKey());
            successCount = forumCommentMapper.insertSelective(forumComment);
        }

        //回复评论的情况添加评论父子关系
        {
            if (ForumComment.targetTypeEnum.COMMENT.getKey() == forumComment.getTargetType()) {
                ForumParentChild forumParentChild = new ForumParentChild();
                forumParentChild.setParentId(forumComment.getTargetId());
                forumParentChild.setChildId(forumComment.getId());
                forumParentChildService.addParentChild(forumParentChild);
            }
        }

        return successCount;
    }

    /**
     * 删除评论、回复
     *
     * @param id
     * @return int
     * @author uncle.quentin
     * @date 2019/2/12 11:17
     * @version 1.0
     */
    public int deleteComment(int id) {
        return forumCommentMapper.deleteByPrimaryKey(id);
    }

    /**
     * 根据ID查询评论
     *
     * @param commentId
     * @return com.slljr.finance.common.pojo.model.ForumPost
     * @author uncle.quentin
     * @date 2019/2/13 9:43
     * @version 1.0
     */
    public ForumComment selectCommentById(int commentId) {
        return forumCommentMapper.selectByPrimaryKey(commentId);
    }

    /**
     * 帖子详情
     *
     * @param postId
     * @param uid
     * @param basePageVO
     * @return com.slljr.finance.common.pojo.vo.ForumPostDetailVO
     * @author uncle.quentin
     * @date 2019/2/12 17:27
     * @version 1.0
     */
    public ForumPostDetailVO listPostDetail(Integer postId, Integer uid, BasePageVO basePageVO) {
        ForumPostDetailVO forumPostDetail = new ForumPostDetailVO();
        //帖子信息
        ForumPostVO forumPost = forumPostMapper.selectByPrimaryKey(postId);
        if (null != forumPost) {
            BeanUtils.copyProperties(forumPost, forumPostDetail);
        }
        //评论信息
        PageInfo<ForumCommentVO> pageInfo = PageHelper.startPage(basePageVO.getPageNum(), basePageVO.getPageSize()).doSelectPageInfo(() -> forumCommentMapper.selectByPostId(postId, uid));
        forumPostDetail.setCommentList(pageInfo);
        //评论的点赞量和回复量
        pageInfo = getCommentLikeAndReplyCount(pageInfo);

        //帖子评论量
        forumPostDetail.setCommentCount(Integer.valueOf(String.valueOf(pageInfo.getTotal())));
        //当前用户是否点赞过该帖子
        if (null == uid) {
            forumPostDetail.setLike(false);
        } else {
            ForumLikes forumLike = new ForumLikes();
            forumLike.setUid(uid);
            forumLike.setTargetId(postId);
            forumLike.setTargetType(1);
            List<ForumLikes> forumLikes = forumLikesMapper.findByAll(forumLike);
            if (null != forumLikes && !forumLikes.isEmpty()) {
                forumPostDetail.setLike(true);
            } else {
                forumPostDetail.setLike(false);
            }
        }

        //点赞量
        String likeCacheKey = String.format("%s_%s_%s", Constant.POST_LIKE, 1, postId);
        Object likeObj = redisUtil.get(likeCacheKey);
        Integer likeCount = Integer.valueOf(null == likeObj ? "0" : likeObj.toString());
        forumPostDetail.setLikeCount(likeCount);

        return forumPostDetail;
    }

    /**
     * 回复点赞量、评论量
     *
     * @param pageInfo
     * @return com.github.pagehelper.PageInfo<com.slljr.finance.common.pojo.vo.ForumCommentVO>
     * @author uncle.quentin
     * @date 2019/2/28 14:36
     * @version 1.0
     */
    private PageInfo<ForumCommentVO> getCommentLikeAndReplyCount(PageInfo<ForumCommentVO> pageInfo) {
        if (null != pageInfo) {
            List<ForumCommentVO> tempList = pageInfo.getList();
            for (ForumCommentVO forumComment : tempList) {
                //评论点赞量
                String likeCacheKey = String.format("%s_%s_%s", Constant.POST_LIKE, 2, forumComment.getCommentId());
                Object likeObj = redisUtil.get(likeCacheKey);
                Integer likeCount = Integer.valueOf(null == likeObj ? "0" : likeObj.toString());
                forumComment.setLikeCount(likeCount);
                //评论回复量
                String commentCacheKey = String.format("%s_%s_%s", Constant.POST_COMMENT, 2, forumComment.getCommentId());
                Object commentObj = redisUtil.get(commentCacheKey);
                Integer commentCount = Integer.valueOf(null == commentObj ? "0" : commentObj.toString());
                forumComment.setReplyCount(commentCount);
            }

            pageInfo.setList(tempList);
        }

        return pageInfo;
    }

    /**
     * 刷新评论量缓存
     *
     * @param operateType
     * @param forumComment
     * @return void
     * @author uncle.quentin
     * @date 2019/2/28 11:15
     * @version 1.0
     */
    public void refreshLikeCountCache(Integer operateType, ForumComment forumComment) {
        //更新缓存 POST_COMMENT_TYPE_POSTID
        String commentCacheKey = String.format("%s_%s_%s", Constant.POST_COMMENT, forumComment.getTargetType(), forumComment.getTargetId());

        String secondKey = "";
        //回复评论量也+1
        if (forumComment.getTargetType() == 2) {
            secondKey = String.format("%s_%s_%s", Constant.POST_COMMENT, 1, forumComment.getPostId());
        }
        if (operateType.equals(1)) {
            //评论
            redisUtil.incr(commentCacheKey, 1);
            if (StringUtils.isNotEmpty(secondKey)) {
                redisUtil.incr(secondKey, 1);
            }
        } else {
            //取消评论
            redisUtil.decr(commentCacheKey, 1);
            if (StringUtils.isNotEmpty(secondKey)) {
                redisUtil.decr(secondKey, 1);
            }
        }
    }
}
