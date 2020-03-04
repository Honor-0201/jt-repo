package com.jt.util;

import com.jt.pojo.User;
/**
 * 如果存储多个数据使用map集合
 * @author 刘旭凯
 *
 */
public class UserThreadLocal {

	private static ThreadLocal<User> thread = new ThreadLocal<User>();

	public static void setUser(User user) {
		thread.set(user);
	}
	
	public static User getUser() {
		return thread.get();
	}
	
	/**
	 * 为了防止内存溢出，必须添加移除
	 */
	
	public static void remove() {
		thread.remove();
	}

}
