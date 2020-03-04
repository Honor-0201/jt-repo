package com.jt.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtil {
	/**
	 * 获取cookie对象
	 */
	public static Cookie getCookie(HttpServletRequest request, String cookieName) {

		Cookie[] cookies = request.getCookies();
		// 校验cookie是否有效
		if (cookies != null && cookies.length > 0) {
			for (Cookie cookie : cookies) {
				if (cookieName.equals(cookie.getName())) {
					return cookie;
				}
			}
		}
		return null;
	}
	
	public static void deleteCookie(HttpServletResponse response ,
			                        String cookieName,
			                        int maxAge,
			                        String domain,
			                        String path) {
		Cookie cookie = new Cookie(cookieName, "");
		cookie.setDomain(domain);
		cookie.setPath(path);
		cookie.setMaxAge(maxAge);
		response.addCookie(cookie);
	}
	
	
	
}
