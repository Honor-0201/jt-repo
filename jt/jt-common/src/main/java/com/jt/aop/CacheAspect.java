package com.jt.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.druid.util.StringUtils;
import com.jt.anno.CacheFind;
import com.jt.util.ObjectMapperUtil;

import redis.clients.jedis.JedisCluster;

@Component // 将类交给容器管理
@Aspect // 标识是一个切面
public class CacheAspect {

	@Autowired(required = false)//懒加载
	private JedisCluster jedis;//连接redis集群
//	private Jedis jedis;//哨兵机制多例
//	private ShardedJedis jedis;
//	private Jedis jedis;

//	@Pointcut("@annotation(com.jt.anno.CacheFind)")
//	public void joinPoint() {
//		
//	}
	/*
	 * JoinPoint如果是环绕通知参数必须为这个 虽然ProceedingJoinPoint是JoinPoint的子类
	 * 但是ProceedingJoinPoint有一个特有的方法执行目标方法 切必须位于第一个
	 */
//	@Around("@annotation(com.jt.anno.CacheFind)")//更灵活
//	@Around(joinPoint()) //这样可以 与上面的方法一起
	@SuppressWarnings("unchecked")
	@Around("@annotation(cacheFind)") // 只有这两个类型一致，名字一致切面才会运行
	public Object around(ProceedingJoinPoint joinPoint, CacheFind cacheFind) {

		Object obj = null;

		System.out.println("成功进入AOP切面中");
		String key = getKey(joinPoint, cacheFind);
		System.out.println(key);

		// 先查询缓存数据
		String result = jedis.get(key);
		try {

			if (StringUtils.isEmpty(result)) {
				// 第一次查询数据库
				obj = joinPoint.proceed();// 执行目标方法
				String json = ObjectMapperUtil.toJson(obj);
				// 如果用户传递了超时时间
				if (cacheFind.seconds() == 0)
					jedis.set(key, json);
				else
					jedis.setex(key, cacheFind.seconds(), json);
				System.out.println("执行数据库查询");
			} else {
				// 数据不为空，将缓存数据转换为对象
				@SuppressWarnings("rawtypes")
				Class returnType = getType(joinPoint);
				obj = ObjectMapperUtil.toObject(result, returnType);
				System.out.println("执行Redis缓存");
			}

		} catch (Throwable e) {
			throw new RuntimeException(e);
		}

		return obj;
	}

	/**
	 * 获取返回值类型
	 * 
	 * @param joinPoint
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private Class getType(ProceedingJoinPoint joinPoint) {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		return signature.getReturnType();
	}

	/**
	 * 获取key
	 * 
	 * @param joinPoint
	 * @param cacheFind
	 * @return
	 */
	private String getKey(ProceedingJoinPoint joinPoint, CacheFind cacheFind) {
		/*
		 * 获取当前的 类名.方法名 getSignature()目标方法的工具API
		 */
		String className = joinPoint.getSignature().getDeclaringTypeName();// 类名
		String methodName = joinPoint.getSignature().getName();// 方法名

//	        判断用户是否传递了参数,如果用户自己指定了，则使用，否则动态生成
		String key = cacheFind.key();
		if (!StringUtils.isEmpty(key)) {
			// 不为空，以用户的为准
			return className + "." + methodName + "::" + key;

		} else {
			// 类名.方法名::第一个参数的值
			Object args0 = joinPoint.getArgs()[0];

			return className + "." + methodName + "::" + args0;

		}
	}

}
