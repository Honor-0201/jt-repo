package com.jt;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;

public class TestShards {
	/**
	 * 测试redis分片
	 */
	@Test
	public void testShards() {
		List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
		shards.add(new JedisShardInfo("192.168.160.128",6379));
		shards.add(new JedisShardInfo("192.168.160.128",6380));
		shards.add(new JedisShardInfo("192.168.160.128",6381));
		ShardedJedis jedis = new ShardedJedis(shards );
		
		jedis.set("JSD","redis分片");
	}

}
