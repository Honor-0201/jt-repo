package com.jt.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.pojo.User;
import com.jt.service.DubboUserService;
import com.jt.util.CookieUtil;
import com.jt.util.IPUtil;
import com.jt.vo.SysResult;

import redis.clients.jedis.JedisCluster;

@Controller // 页面跳转
@RequestMapping("/user")
public class UserController {

	@Reference(check = false)
	private DubboUserService userService;

	@Autowired
	private JedisCluster jedisCluster;

	@RequestMapping("/{moduleName}")
	public String moduleName(@PathVariable String moduleName) {
		return moduleName;
	}

	@RequestMapping("/doRegister")
	@ResponseBody
	public SysResult doRegister(User user) {
		userService.insertUser(user);
		return SysResult.success();

	}

	/**
	 * 实现用户的单点登录
	 * @param user
	 * @return 
	 * 1.动态获取IP地址 
	 * 2.将ticket信息发送到cookie中
	 */
	@RequestMapping("/doLogin")
	@ResponseBody
	public SysResult login(User user, HttpServletRequest request, HttpServletResponse response) {
		// 动态获取用户ip地址
		String ip = IPUtil.getIpAddr(request);
		// 获取校验的结果
		String ticket = userService.findUserByUP(user, ip);
		if (StringUtils.isEmpty(ticket)) {
			// 表示用户名密码错误
			return SysResult.fail();
		}
		// 数据保存道cookie中
		Cookie ticketCookie = new Cookie("JT_TICKET", ticket);
		ticketCookie.setMaxAge(7 * 24 * 60 * 60);
		ticketCookie.setPath("/");
		// 将cookie设置为共享数据
		ticketCookie.setDomain("jt.com");
		response.addCookie(ticketCookie);
		return SysResult.success();
	}

	/**
	 * @return 
	 * 1.删除redis中的数据 key ticket 
	 * 2.删除cookie key JT_TICKET 
	 * -实现思路  1.先获取JT_TICKET cookie中的值 
	 *          2.删除redis数据 
	 *          3.删除cookie数据
	 */
	@RequestMapping("/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response) {

		Cookie cookie = CookieUtil.getCookie(request, "JT_TICKET");
		if (cookie == null) {
			return "redirect:/";
		}
		// 删除redis中的数据
		String ticket = cookie.getValue();
		jedisCluster.del(ticket);

		/*
		 * 删除cookie规则 定义一个与原来名称配置一致的cookie 之后删除
		 */

		CookieUtil.deleteCookie(response, "JT_TICKET", 0, "jt.com", "/");

		return "redirect:/";

	}

}
