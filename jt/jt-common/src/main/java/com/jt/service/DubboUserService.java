package com.jt.service;

import com.jt.pojo.User;

/**
 * @author 刘旭凯
 *定义dubbo的UserService接口
 */
public interface DubboUserService {

	void insertUser(User user);

	String findUserByUP(User user, String ip);

}
