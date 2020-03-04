package tests;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

public class TestCluster {
	
	@Test
	public void test01() {
		
		Set<HostAndPort> nodes = new HashSet<HostAndPort>();
		
		nodes.add(new HostAndPort("192.168.160.128", 7000));
		nodes.add(new HostAndPort("192.168.160.128", 7001));
		nodes.add(new HostAndPort("192.168.160.128", 7002));
		nodes.add(new HostAndPort("192.168.160.128", 7003));
		nodes.add(new HostAndPort("192.168.160.128", 7004));
		nodes.add(new HostAndPort("192.168.160.128", 7005));
		
		JedisCluster jedisCluster = new JedisCluster(nodes);
		
		jedisCluster.set("Cluster", "Cluster搭建完成！");
		System.out.println(jedisCluster.get("Cluster"));
		
		
		
		
		
	}
	
	
	
	
	

}
