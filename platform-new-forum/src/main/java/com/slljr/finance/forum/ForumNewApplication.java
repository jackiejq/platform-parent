package com.slljr.finance.forum;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication
@EnableRedisHttpSession
@EnableCaching
@MapperScan("com.slljr.finance.forum.mapper")
@ComponentScan(basePackages = "com.slljr.finance")
public class ForumNewApplication {

	public static void main(String[] args) {
		SpringApplication.run(ForumNewApplication.class, args);
	}
}
