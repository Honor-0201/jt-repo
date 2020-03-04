package com.jt.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.service.ItemService;

@RestController
@RequestMapping("/web/item")//http://manage.jt.com/web/testJSONP
public class WebItemController {

	@Autowired
	private ItemService itemService;

	@GetMapping("/findItemById")
	public Item findItemById(Long itemId) {
		return itemService.findItemById(itemId);

	}
	
	@GetMapping("/findItemDescById")
	public ItemDesc findItemDescById(ItemDesc itemDesc) {
		return itemService.findItemDescById(itemDesc);
	}

}
