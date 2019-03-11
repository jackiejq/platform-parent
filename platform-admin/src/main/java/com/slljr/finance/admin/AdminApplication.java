package com.slljr.finance.admin;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
@MapperScan("com.slljr.finance.admin.mapper")
@ComponentScan(basePackages = "com.slljr.finance")
public class AdminApplication {

    private static Logger logger = LogManager.getLogger();

	public static void main(String[] args) {
		SpringApplication.run(AdminApplication.class, args);
	}
}
