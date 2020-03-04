package tests;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;

public class TestSentinel {
	/**
	 * 程序连接哨兵案例
	 * masterName :mymaster
	 * sentinels : 哨兵的集合信息
	 * 
	 * 端口说明：
	 * 默认端口 6379
	 * 通信端口 16379  PING-PONG
	 * 哨兵端口 26379
	 * 
	 */
	@Test
	public void test01() {
		Set<String> sentinels = new HashSet<String>();
		sentinels.add("192.168.160.128:26379");
		JedisSentinelPool pool = new JedisSentinelPool("mymaster", sentinels);
		Jedis jedis = pool.getResource();
		
		jedis.set("111","设置成功");
		System.out.println(jedis.get("111"));
		
	}
	
	
	
	
	
	
	
	
	
	
	
	

}
