package com.jt.vo;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class EasyUIFile implements Serializable {

	private Integer error = 0;
	private String url;
	private Integer width;
	private Integer height;

	public static EasyUIFile file() {
		return new EasyUIFile(1, null, null, null);
	}

}
