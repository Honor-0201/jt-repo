package com.jt.interceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.alibaba.druid.util.StringUtils;
import com.jt.pojo.User;
import com.jt.util.CookieUtil;
import com.jt.util.IPUtil;
import com.jt.util.ObjectMapperUtil;
import com.jt.util.UserThreadLocal;

import redis.clients.jedis.JedisCluster;

@Component // 交给spring容器管理
public class UserInterceptor implements HandlerInterceptor {

	@Autowired
	private JedisCluster jedisCluster;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		/**
		 * 业务思路 如何判断用户是否登录 
		 * 1.动态获取cookie中JT_TICKET中的值 
		 * 2.获取用户IP地址，校验IP 
		 * 3.查询redis服务器中是否有数据
		 */

		Cookie cookie = CookieUtil.getCookie(request, "JT_TICKET");
		if (cookie != null) {
			String ticket = cookie.getValue();

			if (!StringUtils.isEmpty(ticket)) {

				if (jedisCluster.exists(ticket)) {
					// 校验IP
					String nowIP = IPUtil.getIpAddr(request);
					String realIP = jedisCluster.hget(ticket, "JT_USER_IP");

					if (nowIP.equals(realIP)) {
						String userJSON = jedisCluster.hget(ticket, "JT_USER");
						User user = ObjectMapperUtil.toObject(userJSON, User.class);

						/*
						 * request.setAttribute("JT_USER", user); 这是方法1，可以在控制器传入request解决
						 * 但是只能到controller层
						 */
						// 利用threadlocal方式动态获取数据
						UserThreadLocal.setUser(user);

						return true;
					}
				}
			}
		}

		response.sendRedirect("/user/login.html");
		return false;// 表示拦截
	}

	/**
	 * 在拦截器最后一步实现数据回收
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		UserThreadLocal.remove();
	}

}
