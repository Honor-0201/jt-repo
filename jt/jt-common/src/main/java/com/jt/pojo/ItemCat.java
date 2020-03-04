package com.jt.pojo;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@TableName("tb_item_cat")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class ItemCat extends BasePojo {
	private Long id;
	private Long parentId;
	private String name;
	private Integer status;
	private Integer sortOrder;// 排序号
	private Boolean isParent;// 是否为父级

}
