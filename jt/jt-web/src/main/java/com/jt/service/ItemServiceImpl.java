package com.jt.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jt.anno.CacheFind;
import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.util.HttpClientService;
import com.jt.util.ObjectMapperUtil;

@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private HttpClientService httpClient;

	@Override
	@CacheFind
	public Item findItemById(Long itemId) {
		
		String url = "http://manage.jt.com/web/item/findItemById";
		Map<String, String> params = new HashMap<String, String>();
		params.put("itemId", itemId + "");

		String json = httpClient.doGet(url, params);
//		System.out.println(json);


		return ObjectMapperUtil.toObject(json, Item.class);
	}

	@Override
	@CacheFind
	public ItemDesc findItemDescById(Long itemId) {
		String url = "http://manage.jt.com/web/item/findItemDescById";
		Map<String, String> params = new HashMap<String, String>();
		params.put("itemId", itemId + "");

		String json = httpClient.doGet(url, params);
		System.out.println(json);


		return ObjectMapperUtil.toObject(json, ItemDesc.class);
	}

}
