package com.jt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;

@Configuration
public class MybatisConfig {

	// 添加分页拦截器,否则分页有问题!!!!
	@Bean // 将对象交给Spring容器管理
	public PaginationInterceptor paginationInterceptor() {

		return new PaginationInterceptor();
	}

}
