package com.jt.service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jt.mapper.ItemDescMapper;
import com.jt.mapper.ItemMapper;
import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.vo.EasyUITable;

@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private ItemMapper itemMapper;
	@Autowired
	private ItemDescMapper itemDescMapper;

	@Override
	public EasyUITable findItemByPage(Integer page, Integer rows) {
		// int total = itemMapper.selectCount(null);
		// int start = (page - 1) * rows;
		// List<Item> userList =
		// itemMapper.findItemByPage(start,rows);

		/*
		 * mybatis-plus分页说明 1.new Page<>(current, size); current:当前页数 size: 每页条数
		 * 
		 * 此分页使用在旧版本时候需要添加一项配置：已写 新版本无需做此项配置，但是效率很慢
		 */
		IPage<Item> tempPage = new Page<>(page, rows);
		QueryWrapper<Item> queryWrapper = new QueryWrapper<>();
		queryWrapper.orderByDesc("updated");
		// 当前分页查询的结果对象
		IPage<Item> iPage = itemMapper.selectPage(tempPage, queryWrapper);
		int total = (int) iPage.getTotal();
		// 获取分页的结果
		List<Item> userList = iPage.getRecords();
		return new EasyUITable(total, userList);
	}

	@Override
	@Transactional
	public void saveItem(Item item, ItemDesc itemDesc) {
		item.setStatus(1).setCreated(new Date()).setUpdated(item.getCreated());
		itemMapper.insert(item);

		itemDesc.setItemId(item.getId()).setCreated(item.getCreated()).setUpdated(item.getCreated());
		itemDescMapper.insert(itemDesc);
	}

	@Override
	@Transactional
	public void updateItem(Item item,ItemDesc itemDesc) {
		item.setUpdated(new Date());
		itemMapper.updateById(item);// 此条数据全部更新
		
		itemDesc.setItemId(item.getId()).setUpdated(item.getUpdated());
		itemDescMapper.updateById(itemDesc);
	}

	@Override
	public void updateStatus(Long[] ids, Integer status) {
		Item item = new Item();
		item.setStatus(status).setUpdated(new Date());

		UpdateWrapper<Item> updateWrapper = new UpdateWrapper<>();
		List idList = Arrays.asList(ids);
		updateWrapper.in("id", idList);
		itemMapper.update(item, updateWrapper);

	}

	@SuppressWarnings("unchecked")
	@Override
	public void deleteItem(Long[] ids) {
		List idList = Arrays.asList(ids);
		itemMapper.deleteBatchIds(idList);

	}

	@Override
	public ItemDesc findItemDescById(ItemDesc itemDesc) {

		return itemDescMapper.selectById(itemDesc.getItemId());
	}

	@Override
	public Item findItemById(Long itemId) {
		return itemMapper.selectById(itemId);
	}

}
