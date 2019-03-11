package com.slljr.finance.forum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: uncle.quentin.
 * @date: 2019/2/28.
 * @time: 15:48.
 */
@Component
public class AfterServiceStarted implements ApplicationRunner {

    @Autowired
    private ForumPostService forumPostService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        //刷新点赞量、评论量缓存
        forumPostService.refreshAllPostCache();
    }
}
