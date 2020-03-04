package com.jt.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.anno.CacheFind;
import com.jt.mapper.ItemCatMapper;
import com.jt.pojo.ItemCat;
import com.jt.util.ObjectMapperUtil;
import com.jt.vo.EasyUITree;

import redis.clients.jedis.Jedis;

@Service
public class ItemCatServiceImpl implements ItemCatService {

	@Autowired
	private ItemCatMapper itemCatMapper;

//	@Autowired
	private Jedis jedis;

	@Override
	public ItemCat findItemCatById(Long itemCatId) {

		return itemCatMapper.selectById(itemCatId);
	}

	@Override
	@CacheFind//(key="天王盖地虎") 测试
	public List<EasyUITree> findItemCatByParentId(Long parentId) {

		List<ItemCat> itemCatList = findItemCatListByParentId(parentId);
		List<EasyUITree> easyUITreeList = new ArrayList<EasyUITree>(itemCatList.size());

		for (ItemCat itemCat : itemCatList) {
			Long id = itemCat.getId();
			String text = itemCat.getName();
			String state = itemCat.getIsParent() ? "closed" : "open";
			EasyUITree easyUITree = new EasyUITree(id, text, state);
			easyUITreeList.add(easyUITree);

		}

		return easyUITreeList;
	}

	private List<ItemCat> findItemCatListByParentId(Long parentId) {
		QueryWrapper<ItemCat> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("parent_id", parentId);
		return itemCatMapper.selectList(queryWrapper);
	}

	/**
	 * 1.先查询缓存-key 写活 2.如果缓存中没数据 第一次查询 查询数据库-转换为json-放入缓存
	 * 3.缓存中有数据，不是第一次，从redis获取数据-转换为对象
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<EasyUITree> findItemCatService(Long parentId) {
		String key = "ITEMCAT::" + parentId;
		List<EasyUITree> treeList = new ArrayList<>();
		String json = jedis.get(key);

		if (StringUtils.isEmpty(json)) {
			// 用户第一次查询
			treeList = findItemCatByParentId(parentId);
			String itemCatJson = ObjectMapperUtil.toJson(treeList);
			jedis.set(key, itemCatJson);
			System.err.println("第一次查询");
		} else {
			treeList = ObjectMapperUtil.toObject(json, treeList.getClass());
			System.err.println("不是第一次查询");
		}

		return treeList;
	}

}
