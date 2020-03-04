package com.jt.config;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

/**
 * 实现spring容器管理配置类
 * 
 * @author 刘旭凯
 */
@Configuration
@PropertySource("classpath:/properties/redis.properties")
public class RedisConfig {
	
	@Value("${redis.nodes}")
	private String redisNode;
	
	//将set集合交给容器管理 可能有许多集合  所以起个名字 不能给别人干扰
	@Bean("redisSet")
	public Set<HostAndPort> redisSet(){
		
        Set<HostAndPort> nodes = new HashSet<HostAndPort>();
		
		String[] arrayNodes = redisNode.split(",");
		
		for (String s : arrayNodes) {
			String host = s.split(":")[0];
			int port =Integer.parseInt(s.split(":")[1]);
//			System.out.println(host+" : "+port);
			nodes.add(new HostAndPort(host, port));
		}
		return nodes;
	}
	
	@Bean
	@Scope("prototype")//可以指定名字
	public JedisCluster jedisCluster(@Qualifier("redisSet")Set<HostAndPort> nodes) {
		
		return new JedisCluster(nodes);
		
	}
	
	
}

	
	
	
	/*
	@Value("${redis.sentinels}")
	private String sentinels;
	
	@Bean
	public JedisSentinelPool pool() {
		
		Set<String> set = new HashSet<String>();
		set.add(sentinels);
		
		return new JedisSentinelPool("mymaster", set) ;
		
	}
	
	@Bean
	@Scope("prototype")
	public Jedis jedis(JedisSentinelPool pool) {
		return pool.getResource();
	}
	*/

	/*
	@Value("${redis.nodes}")
	String nodes;

	@Bean
	@Scope("prototype")
	public ShardedJedis shardedJedis() {
		List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
		String[] redisNodes = nodes.split(",");
		for (String s : redisNodes) {
			String[] hostAndPort = s.split(":");
			String host = hostAndPort[0];
			Integer port = Integer.parseInt(hostAndPort[1]);

			JedisShardInfo e = new JedisShardInfo(host, port);
			shards.add(e);
		}

		return new ShardedJedis(shards);
	}*/

	/*
	 * @Value("${redis.host}") private String host;
	 * 
	 * @Value("${redis.port}") private Integer port;
	 * 
	 * @Bean//让spring容器管理
	 * 
	 * @Scope("prototype")//默认是单例的 设置为多例 当用户使用时创建 public Jedis jedis() {
	 * 
	 * return new Jedis(host, port); }
	 */

