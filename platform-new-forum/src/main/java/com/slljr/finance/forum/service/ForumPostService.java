package com.slljr.finance.forum.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.slljr.finance.common.constants.Constant;
import com.slljr.finance.common.enums.DataStatusEnum;
import com.slljr.finance.common.pojo.model.ForumPost;
import com.slljr.finance.common.pojo.vo.ForumPostVO;
import com.slljr.finance.common.pojo.vo.ForumStatisticsVO;
import com.slljr.finance.common.redis.RedisUtil;
import com.slljr.finance.common.utils.HtmlUtil;
import com.slljr.finance.forum.mapper.ForumCommentMapper;
import com.slljr.finance.forum.mapper.ForumLikesMapper;
import com.slljr.finance.forum.mapper.ForumPostMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @description: 帖子服务层
 * @author: uncle.quentin.
 * @date: 2019/2/12.
 * @time: 10:34.
 */
@Service
public class ForumPostService {

    /**
     * 帖子Mapper
     */
    @Autowired
    private ForumPostMapper forumPostMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private ForumLikesMapper forumLikesMapper;

    @Autowired
    private ForumCommentMapper forumCommentMapper;


    /**
     * 发帖
     *
     * @author uncle.quentin
     * @date   2019/2/12 10:39
     * @param   forumPost
     * @return int
     * @version 1.0
     */
    public int posting(ForumPost forumPost){
        forumPost.setStatus(DataStatusEnum.NORMAL.getKey());
        return forumPostMapper.insertSelective(forumPost);
    }

    /**
     * 修改帖子内容
     *
     * @author uncle.quentin
     * @date   2019/2/12 10:39
     * @param   forumPost
     * @return int
     * @version 1.0
     */
    public int updatePost(ForumPost forumPost){
        return forumPostMapper.updateByPrimaryKeySelective(forumPost);
    }

    /**
     * 增加访问量
     *
     * @author uncle.quentin
     * @date   2019/2/12 15:59
     * @param   forumPost
     * @return int
     * @version 1.0
     */
    public int updatePostViewCount(ForumPost forumPost){
        return forumPostMapper.updateAddViewCount(forumPost);
    }

    /**
     * 删除帖子
     *
     * @author uncle.quentin
     * @date   2019/2/12 10:40
     * @param   id
     * @return int
     * @version 1.0
     */
    public int deletePost(int id){
        ForumPost forumPost = new ForumPost();
        forumPost.setId(id);
        forumPost.setStatus(DataStatusEnum.INVALID.getKey());
        return forumPostMapper.updateByPrimaryKeySelective(forumPost);
    }

    /**
     * 根据ID查询详情
     *
     * @author uncle.quentin
     * @date   2019/2/12 11:41
     * @param   id
     * @return com.slljr.finance.common.pojo.model.ForumPost
     * @version 1.0
     */
    public ForumPostVO selectPostDetail(int id){
        return forumPostMapper.selectByPrimaryKey(id);
    }


    /**
     * 追加分页返回信息
     *
     * @author uncle.quentin
     * @date   2019/3/4 14:29
     * @param   pageInfo
     * @return com.github.pagehelper.PageInfo<com.slljr.finance.common.pojo.vo.ForumPostVO>
     * @version 1.0
     */
    private PageInfo<ForumPostVO> extraResult(PageInfo<ForumPostVO> pageInfo){
        if (!pageInfo.getList().isEmpty()) {
            List<ForumPostVO> tempList = pageInfo.getList();
            for (ForumPostVO forumPostVO : tempList) {
                //点赞量
                String likeCacheKey = String.format("%s_%s_%s", Constant.POST_LIKE, 1, forumPostVO.getId());
                Object likeObj = redisUtil.get(likeCacheKey);
                Integer likeCount = Integer.valueOf(null == likeObj ? "0" : likeObj.toString());
                forumPostVO.setLikeCount(likeCount);

                //评论量
                String commentCacheKey = String.format("%s_%s_%s", Constant.POST_COMMENT, 1, forumPostVO.getId());
                Object commentObj = redisUtil.get(commentCacheKey);
                Integer commentCount = Integer.valueOf(null == commentObj ? "0" : commentObj.toString());
                forumPostVO.setCommentCount(commentCount);

                //图片URL
                String firstImgUrl = HtmlUtil.getFirstImgStr(forumPostVO.getContent());
                forumPostVO.setContentImg(firstImgUrl);

                //格式化（去除img标签）帖子内容
                String content = HtmlUtil.replaceAllImgToEmpty(forumPostVO.getContent());
                forumPostVO.setContent(content);
            }

            pageInfo.setList(tempList);
        }

        return pageInfo;
    }

    /**
     * 分页查询
     *
     * @author uncle.quentin
     * @date   2019/2/12 14:23
     * @param   forumPost
     * @return com.github.pagehelper.PageInfo<com.slljr.finance.common.pojo.model.ForumPost>
     * @version 1.0
     */
    public PageInfo<ForumPostVO> selectPostByPage(ForumPost forumPost) {
        PageInfo<ForumPostVO> pageInfo = PageHelper.startPage(forumPost.getPageNum(), forumPost.getPageSize()).doSelectPageInfo(() -> forumPostMapper.selectByCondition(forumPost));

        //追加分页返回信息
        pageInfo = extraResult(pageInfo);

        return pageInfo;
    }

    /**
     * 条件分页，根据创建时间降序
     *
     * @author uncle.quentin
     * @date   2019/3/4 14:30
     * @param   forumPost
     * @return com.github.pagehelper.PageInfo<com.slljr.finance.common.pojo.vo.ForumPostVO>
     * @version 1.0
     */
    public PageInfo<ForumPostVO> selectPostByPageOrderByCreateTime(ForumPost forumPost) {
        PageInfo<ForumPostVO> pageInfo = PageHelper.startPage(forumPost.getPageNum(), forumPost.getPageSize()).doSelectPageInfo(() -> forumPostMapper.selectByConditionOrderByCreateTimeDesc(forumPost));

        //追加分页返回信息
        pageInfo = extraResult(pageInfo);

        return pageInfo;
    }

    /**
     * 刷新帖子浏览量缓存
     *
     * @author uncle.quentin
     * @date   2019/2/28 10:30
     * @param
     * @return void
     * @version 1.0
     */
    public void refreshPostViewCountCache(ForumPost forumPost){
        //更新缓存 POST_POSTVIEW_POSTID
        String viewCountCacheKey = String.format("%s_%s", Constant.POST_POSTVIEW, forumPost.getId());

        //浏览量+1
        redisUtil.incr(viewCountCacheKey, 1);
    }

    /**
     * 刷新缓存
     *
     * @author uncle.quentin
     * @date   2019/2/28 14:23
     * @param
     * @return void
     * @version 1.0
     */
    public void refreshAllPostCache(){
        //帖子点赞量
        List<ForumStatisticsVO> likes = forumLikesMapper.selectLikeCountByType(1);
        for (ForumStatisticsVO forumStatistics : likes) {
            String likeCacheKey = String.format("%s_%s_%s", Constant.POST_LIKE, 1, forumStatistics.getDataId());
            redisUtil.del(likeCacheKey);

            redisUtil.incr(likeCacheKey, forumStatistics.getCount());
        }
        //帖子评论量
        List<ForumStatisticsVO> comment = forumCommentMapper.selectCommentCountByPostId();
        for (ForumStatisticsVO forumStatistics : comment) {
            String commentCacheKey = String.format("%s_%s_%s", Constant.POST_COMMENT, 1, forumStatistics.getDataId());
            redisUtil.del(commentCacheKey);

            redisUtil.incr(commentCacheKey, forumStatistics.getCount());
        }
        //回复点赞量
        List<ForumStatisticsVO> likes1 = forumLikesMapper.selectLikeCountByType(2);
        for (ForumStatisticsVO forumStatistics : likes1) {
            String likeCacheKey = String.format("%s_%s_%s", Constant.POST_LIKE, 2, forumStatistics.getDataId());
            redisUtil.del(likeCacheKey);

            redisUtil.incr(likeCacheKey, forumStatistics.getCount());
        }
        //回复评论量
        List<ForumStatisticsVO> comment1 = forumCommentMapper.selectCommentCountByType(2);
        for (ForumStatisticsVO forumStatistics : comment1) {
            String commentCacheKey = String.format("%s_%s_%s", Constant.POST_COMMENT, 2, forumStatistics.getDataId());
            redisUtil.del(commentCacheKey);

            redisUtil.incr(commentCacheKey, forumStatistics.getCount());
        }


    }

}
