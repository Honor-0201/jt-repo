package com.jt.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jt.mapper.CartMapper;
import com.jt.pojo.Cart;

@Service
public class DubboCartServiceImpl implements DubboCartService {

	@Autowired
	private CartMapper cartMapper;

	@Override
	public List<Cart> findCartListByUserId(Long userId) {
		QueryWrapper<Cart> queryWrapper = new QueryWrapper<Cart>();
		queryWrapper.eq("user_id", userId);
		return cartMapper.selectList(queryWrapper);

	}

	/*
	 * sql:update tb_cart set num=#{num},updated=#{updated} where user_id=#{} and
	 * item_id=#{}
	 */
	@Override
	public void updateCartNum(Cart cart) {
		Cart cartTemp = new Cart();
		cartTemp.setNum(cart.getNum()).setUpdated(new Date());
		UpdateWrapper<Cart> updateWrapper = new UpdateWrapper<Cart>();
		updateWrapper.eq("user_id", cart.getUserId());
		updateWrapper.eq("item_id", cart.getItemId());
		cartMapper.update(cartTemp, updateWrapper);

	}

	@Override
	public void deleteCart(Cart cart) {
		QueryWrapper<Cart> wrapper = new QueryWrapper<>(cart);
		cartMapper.delete(wrapper);

	}

	/**
	 * 根据userId和itemId查询数据库 有就更新 没有就插入
	 */
	@Override
	public void insertCart(Cart cart) {

		QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("user_id", cart.getUserId());
		queryWrapper.eq("item_id", cart.getItemId());
		Cart cartDB = cartMapper.selectOne(queryWrapper);

		if (cartDB == null) {
			cart.setCreated(new Date()).setUpdated(cart.getCreated());
			cartMapper.insert(cart);
		} else {
			int num = cart.getNum() + cartDB.getNum();
//			cartDB.setNum(num).setUpdated(new Date());
//			cartMapper.updateById(cartDB);
			// 此处只需要更新2个数据，没必要对所有更新
			Cart cartTemp = new Cart();
			cartTemp.setNum(num).setId(cartDB.getId()).setUpdated(new Date());
			cartMapper.updateById(cartTemp);
		}

	}

}
