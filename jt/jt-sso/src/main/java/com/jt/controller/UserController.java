package com.jt.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.druid.util.StringUtils;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.jt.service.UserService;
import com.jt.util.CookieUtil;
import com.jt.util.IPUtil;
import com.jt.vo.SysResult;

import redis.clients.jedis.JedisCluster;

@RestController
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService userService;
	
	@Autowired
	private JedisCluster jedisCluster;

	/**
	 * 利用JSONP实现用户信息校验
	 */
	@RequestMapping("/check/{param}/{type}")
	public JSONPObject checkUser(
			@PathVariable String param,
			@PathVariable Integer type,
			String callback) {
//		 int a = 1/0;
		 boolean result = userService.checkUser(param,type);
		 return new JSONPObject(callback, SysResult.success(result));
	}
	
	/*
	 * 根据ticket检索用户信息
	 * 1.动态获取ticket
	 * 2.查询redis缓存
	 *   2.1 没有数据，用户ticket不可用，返回201
	 *   2.2 查到，校验用户ip是否正确
	 *     2.2.1 校验通过，表示用户正常登陆 返回用户json数据
	 *     2.2.2 否则 不允许登陆
	 */
	@RequestMapping("/query/{ticket}")
	public JSONPObject findUserByTicket(@PathVariable String ticket,
			                             String callback,HttpServletRequest request,
			                             HttpServletResponse response) {
		
		JSONPObject jsonpObject;
		//校验 ticket
		if (StringUtils.isEmpty(ticket)) {
			jsonpObject = new JSONPObject(callback, SysResult.fail()) ;
			return jsonpObject;
		}
		
		//判断当前key是否存在
		if (!jedisCluster.exists(ticket)) {
			//ticket不为null，但是redis没数据  表示 cookie数据有误 删除cookie
			CookieUtil.deleteCookie(response, "JT_TICKET", 0, "jt.com", "/");
			
			
			jsonpObject = new JSONPObject(callback, SysResult.fail()) ;
			return jsonpObject;
		}
		
		//校验用户信息
		String nowIP = IPUtil.getIpAddr(request);
		String realIP = jedisCluster.hget(ticket, "JT_USER_IP");
		
		if (!nowIP.equals(realIP)) {
			jsonpObject = new JSONPObject(callback, SysResult.fail()) ;
			return jsonpObject;
		}
		
		//说明用户信息正确，返回用户json数据
		String userJson = jedisCluster.hget(ticket, "JT_USER");
		
		jsonpObject =  new JSONPObject(callback, SysResult.success(userJson));
		return jsonpObject;
	}
	


}
