package com.slljr.finance.forum.service;

import com.slljr.finance.common.constants.Constant;
import com.slljr.finance.common.enums.DataStatusEnum;
import com.slljr.finance.common.pojo.model.ForumLikes;
import com.slljr.finance.common.redis.RedisUtil;
import com.slljr.finance.forum.mapper.ForumLikesMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description: 点赞服务层
 * @author: uncle.quentin.
 * @date: 2019/2/12.
 * @time: 10:35.
 */
@Service
public class ForumLikesService {
    /**
     * 点赞Mapper
     */
    @Autowired
    private ForumLikesMapper forumLikesMapper;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 点赞
     *
     * @param forumLikes
     * @return int
     * @author uncle.quentin
     * @date 2019/2/12 10:59
     * @version 1.0
     */
    public int like(ForumLikes forumLikes) {
        forumLikes.setStatus(DataStatusEnum.NORMAL.getKey());
        return forumLikesMapper.insertSelective(forumLikes);
    }

    /**
     * 取消赞
     *
     * @param id
     * @return int
     * @author uncle.quentin
     * @date 2019/2/12 10:59
     * @version 1.0
     */
    public int cancelLike(int id) {
        return forumLikesMapper.deleteByPrimaryKey(id);
    }

    /**
     * 条件查询
     *
     * @param forumLikes
     * @return java.util.List<com.slljr.finance.common.pojo.model.ForumLikes>
     * @author uncle.quentin
     * @date 2019/3/4 11:01
     * @version 1.0
     */
    public List<ForumLikes> selectByCondition(ForumLikes forumLikes) {
        return forumLikesMapper.findByAll(forumLikes);
    }

    /**
     * 刷新点赞量缓存
     *
     * @param operateType
     * @param forumLikes
     * @return void
     * @author uncle.quentin
     * @date 2019/2/28 11:15
     * @version 1.0
     */
    public void refreshLikeCountCache(Integer operateType, ForumLikes forumLikes) {
        //更新缓存 POST_LIKE_TYPE_POSTID
        String likeCacheKey = String.format("%s_%s_%s", Constant.POST_LIKE, forumLikes.getTargetType(), forumLikes.getTargetId());
        if (operateType.equals(1)) {
            //点赞
            redisUtil.incr(likeCacheKey, 1);
        } else {
            //取消赞
            Object likeObj = redisUtil.get(likeCacheKey);
            Integer likeCount = Integer.valueOf(null == likeObj ? "0" : likeObj.toString());
            if (likeCount >= 0) {
                redisUtil.decr(likeCacheKey, 1);
            }
        }
    }
}
