package com.jt.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.pojo.Cart;
import com.jt.pojo.Order;
import com.jt.service.DubboCartService;
import com.jt.service.DubboOrderService;
import com.jt.util.UserThreadLocal;
import com.jt.vo.SysResult;

@Controller
@RequestMapping("/order")
public class OrderController {
	@Reference(check = false)
	private DubboCartService cartService;

	@Reference(check = false)
	private DubboOrderService orderService;

	/**
	 * 订单确认页，需要获取订单记录 通过userId获取
	 * 
	 * @return
	 */
	@RequestMapping("/create")
	public String create(Model model) {
		// 在拦截器中定义的，所以不会为空
		Long userId = UserThreadLocal.getUser().getId();
		List<Cart> cartList = cartService.findCartListByUserId(userId);
		model.addAttribute("carts", cartList);

		return "order-cart";// 执行视图解析器，所以使用model
	}

	@RequestMapping("submit")
	@ResponseBody
	public SysResult indertOrder(Order order) {
		Long userId = UserThreadLocal.getUser().getId();
		order.setUserId(userId);

		String orderId = orderService.insertOrder(order);

		if (StringUtils.isEmpty(orderId))
			return SysResult.fail();

		return SysResult.success(orderId);

	}

	/**
	 * 根绝orderId查询数据库记录
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping("/success")
	public String findOrderById(String id, Model model) {

		Order order = orderService.findOrderById(id);
		model.addAttribute("order", order);

		return "success";
	}

}
