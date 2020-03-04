package com.jt.controller.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.jt.pojo.ItemDesc;
import com.jt.util.ObjectMapperUtil;

@RestController
public class JSONPController {
	
	//约定的回调函数名称calllback
//	@RequestMapping("/web/testJSONP")
	public String testJSONPOld(String callback) {
		//http://manage.jt.com/web/testJSONP
		
		ItemDesc itemDesc = new ItemDesc();
		itemDesc.setItemId(10001L).setItemDesc("商品详情信息");
		String json = ObjectMapperUtil.toJson(itemDesc);
		return callback+"("+json+")";
		
	}
	//利用API实现JSONP跨域访问
	@RequestMapping("/web/testJSONP")
	public JSONPObject testJSONP(String callback) {
		//http://manage.jt.com/web/testJSONP
		ItemDesc itemDesc = new ItemDesc();
		itemDesc.setItemId(10001L).setItemDesc("商品详情信息");
		
		return new JSONPObject(callback, itemDesc);
		
	}
	

}
