package com.jt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.service.ItemService;
import com.jt.vo.EasyUITable;
import com.jt.vo.SysResult;

@RestController
@RequestMapping("/item")
public class ItemController {

	@Autowired
	private ItemService itemService;

	/**
	 * 根据条件查询数据信息 url:http://localhost:8091/item/query?page=1&rows=20
	 */
	@RequestMapping("/query")
	public EasyUITable findItemAll(Integer page, Integer rows) {
		return itemService.findItemByPage(page, rows);

	}

	/**
	 * 实现商品的新增
	 */
	@RequestMapping("/save") 

	public SysResult saveItem(Item item,ItemDesc itemDesc) {

		itemService.saveItem(item,itemDesc);
		return SysResult.success();
	}

	/**
	 * 实现商品的更新
	 */
	@RequestMapping("/update")
	public SysResult updateItem(Item item,ItemDesc itemDesc) {
		itemService.updateItem(item,itemDesc);
		return SysResult.success();

	}

	/**
	 * 商品的上架和下架
	 */
	@RequestMapping("/instock")
	public SysResult instockItem(Long[] ids) {
		itemService.updateStatus(ids, 2);
		return SysResult.success();

	}

	@RequestMapping("/reshelf")
	public SysResult reshelfItem(Long[] ids) {
		itemService.updateStatus(ids, 1);
		return SysResult.success();

	}

	@RequestMapping("/delete")
	public SysResult deleteItem(Long[] ids) {
		itemService.deleteItem(ids);
		return SysResult.success();

	}
	/**
	 * 根据id查询商品的详情信息
	 */
	@RequestMapping("/query/item/desc/{itemId}")
	public SysResult findItemDescById(ItemDesc itemDesc){//@PathVariable Long itemId也可以
		itemDesc = itemService.findItemDescById(itemDesc);
		return SysResult.success(itemDesc);
		
	}

}
