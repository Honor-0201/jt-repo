package com.jt.service;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.mapper.UserMapper;
import com.jt.pojo.User;
import com.jt.util.ObjectMapperUtil;

import redis.clients.jedis.JedisCluster;

/**
 * @author 刘旭凯
  *编辑提供者的实现类
 *1.为了避免数据库报错，暂时使用手机
 *2.密码加密
 *3.设定操作时间
 */
@Service
public class DubboUserServiceimpl implements DubboUserService {
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private JedisCluster jedisCluster;

	@Override
	public void insertUser(User user) {
		
		String md5Pass = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
		
		user.setEmail(user.getPhone())
		    .setPassword(md5Pass)
		    .setCreated(new Date())
		    .setUpdated(user.getCreated());
		userMapper.insert(user);
		
	}

	/**
	 * 1.返回值数据 返回加密之后的秘钥
	 * 2.校验用户信息是否有效 无效返回null
	 * 3.如果用户密码正确，将用户信息保存至redis中
	 * 
	 */
	@Override
	public String findUserByUP(User user, String ip) {
		//将密码加密
		String md5Pass = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
		user.setPassword(md5Pass);
		//根据用户信息查询数据库记录
		//这样写 就是把此对象中不为null的属性作为where条件
		QueryWrapper<User> queryWrapper = new QueryWrapper<User>(user);
		
		
		User userDB = userMapper.selectOne(queryWrapper);
		
		if (userDB == null) {
			return null;
		}
		//用户名密码正确
		//动态生成ticket
		String uuid = UUID.randomUUID().toString();
		String ticket = DigestUtils.md5DigestAsHex(uuid.getBytes());
		
		//将user对象转化为json
		userDB.setPassword("1111");
		String userJson = ObjectMapperUtil.toJson(userDB);
		
		/*
		 * 防止用户重复登录，需要将用户之前的登录信息删除
		 */
		if (jedisCluster.exists("JT_USERNAME_"+userDB.getUsername())) {
			//存在 删除 ticket所包含的一些列数据
			String oldTicket = jedisCluster.get("JT_USERNAME_"+userDB.getUsername());
			jedisCluster.del(oldTicket);
		}
		
		//将用户信息保存至redis
		jedisCluster.hset(ticket, "JT_USER", userJson);
		jedisCluster.hset(ticket, "JT_USER_IP", ip);
		jedisCluster.expire(ticket, 7*24*3600);
		//实现用户名与ticket绑定
		jedisCluster.setex("JT_USERNAME_"+userDB.getUsername(),7*24*3600,ticket);
		
		
		return ticket;
	}
	

}
