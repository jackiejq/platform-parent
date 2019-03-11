package com.slljr.finance.forum.service;

import com.slljr.finance.common.pojo.model.ForumParentChild;
import com.slljr.finance.forum.mapper.ForumParentChildMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @description: 评论链路
 * @author: uncle.quentin.
 * @date: 2019/2/13.
 * @time: 10:16.
 */
@Service
public class ForumParentChildService {

    @Autowired
    private ForumParentChildMapper forumParentChildMapper;

    /**
     * 添加评论链路
     *
     * @param forumParentChild
     * @return int
     * @author uncle.quentin
     * @date 2019/2/13 10:18
     * @version 1.0
     */
    public int addParentChild(ForumParentChild forumParentChild) {
        return forumParentChildMapper.insertSelective(forumParentChild);
    }

}
