package com.jt.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.pojo.Cart;
import com.jt.service.DubboCartService;
import com.jt.util.UserThreadLocal;
import com.jt.vo.SysResult;

@Controller // 消费者
@RequestMapping("/cart")
public class CartController {

	@Reference
	private DubboCartService cartService;

	@RequestMapping("/show")
	public String show(Model model) {
		/*
		 * 1.获取当前用户信息 uerrId 查询redis 2.页面取值 ${cartList}
		 */

		Long userId = UserThreadLocal.getUser().getId();
		List<Cart> cartList = cartService.findCartListByUserId(userId);
		model.addAttribute("cartList", cartList);

		return "cart";
	}

	@RequestMapping("/update/num/{itemId}/{num}")
	@ResponseBody
	public SysResult updateNum(Cart cart) {
		Long userId = UserThreadLocal.getUser().getId();
		cart.setUserId(userId);
		cartService.updateCartNum(cart);
		return SysResult.success();
	}

	/**
	 * 实现购物车删除
	 * 
	 * @param cart
	 * @return
	 */
	@RequestMapping("/delete/{itemId}")
	public String deleteCart(Cart cart) {
		Long userId = UserThreadLocal.getUser().getId();
		cart.setUserId(userId);
		cartService.deleteCart(cart);
		return "redirect:/cart/show.html";

	}

	@RequestMapping("/add/{itemId}")
	public String insertCart(Cart cart) {
		Long userId = UserThreadLocal.getUser().getId();
		cart.setUserId(userId);
		cartService.insertCart(cart);
		return "redirect:/cart/show.html";
	}

}
