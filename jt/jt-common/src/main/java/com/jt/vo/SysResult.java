package com.jt.vo;
/**
 * 该类是系统级别的VO对象
 * @author 86540
 *
 */

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class SysResult implements Serializable {
	private Integer status;
	private String msg;
	private Object data;

	public static SysResult success() {
		return new SysResult(200, null, null);
	}

	public static SysResult success(Object data) {
		return new SysResult(200, null, data);
	}

	public static SysResult success(String msg, Object data) {
		return new SysResult(200, msg, data);
	}

	public static SysResult fail() {
		return new SysResult(201, "业务执行失败！", null);
	}

}
