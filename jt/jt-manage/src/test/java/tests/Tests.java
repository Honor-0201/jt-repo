package tests;

import org.junit.Test;

import redis.clients.jedis.Jedis;

public class Tests {
	
	@Test
	public void testString() {
		String host = "192.168.160.128";
		int port = 6379;
		Jedis jedis = new Jedis(host, port);
		jedis.set("lxk", "12356");
		System.out.println(jedis.get("lxk"));
		
	}

}
