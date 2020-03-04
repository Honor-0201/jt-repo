package com.jt.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 86540 测试集群可用
 */
@RestController
public class MessageController {
	@Value("${server.port}")
	private String port;

	@RequestMapping("/getPort")
	public String getMsg() {
		return "当前服务器的端口号为："+port;

	}

}
