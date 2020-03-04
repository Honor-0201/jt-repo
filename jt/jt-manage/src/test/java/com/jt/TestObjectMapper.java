package com.jt;

import java.io.IOException;
import java.util.Date;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.pojo.ItemDesc;

public class TestObjectMapper {
	
	private static final ObjectMapper MAPPER = new ObjectMapper();
	/**
	 * 实现对象和json数据之间的转换
	 * @throws IOException 
	 */
	@Test
	public void object2JSON() throws IOException {
		ItemDesc itemDesc = new ItemDesc();
		itemDesc.setItemId(1000L).setItemDesc("<html>").setCreated(new Date()).setUpdated(itemDesc.getCreated());
		String json = MAPPER.writeValueAsString(itemDesc);
		System.out.println(json);
		ItemDesc itemDesc2 = MAPPER.readValue(json, ItemDesc.class);
		System.out.println(itemDesc2);
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
