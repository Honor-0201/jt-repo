package com.jt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

	/**
	 * 实现页面的跳转
	 * 
	 * @param moduleName
	 * @return
	 * @PathVariable注解动态获取数据 参数一般与url名称保持一致或者@PathVariable("moduleName")
	 *                       参数数量众多时候，采用对象形式
	 */
	@RequestMapping("/page/{moduleName}")
	public String module(@PathVariable String moduleName) {

		return moduleName;
	}
}
