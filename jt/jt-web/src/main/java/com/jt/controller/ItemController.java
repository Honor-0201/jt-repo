package com.jt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.service.ItemService;

/** 需要跳转页面 */
@Controller
@RequestMapping("/items")
public class ItemController {
	
	@Autowired
	private ItemService itemService;

	@RequestMapping("/{itemId}")
	public String toItems(@PathVariable Long itemId,Model model) {
		//根据商品的ID号查询后台商品数据
//		System.out.println("当前商品ID为："+itemId);
		Item item = itemService.findItemById(itemId);
//		System.out.println(item);
		model.addAttribute("item",item);
		
		ItemDesc itemDesc = itemService.findItemDescById(itemId);
		model.addAttribute("itemDesc",itemDesc);
		
		return "item";// 动态的商品展现页面

	}

}
