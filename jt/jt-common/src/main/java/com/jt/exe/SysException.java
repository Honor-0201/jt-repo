package com.jt.exe;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.alibaba.druid.util.StringUtils;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.jt.vo.SysResult;

import lombok.extern.slf4j.Slf4j;

//@ControllerAdvice//异常通知
@RestControllerAdvice
@Slf4j
public class SysException {
	

	@ExceptionHandler(RuntimeException.class)
//	@ResponseBody
	/*
	 * 当系统中出现运行时异常时生效
	 * 区分 系统正常异常和跨域异常 
	 * 说明:跨域访问时用户一定会添加callback参数
	 */
	public Object error(Exception exception,HttpServletRequest request) {
		String callback = request.getParameter("callback");
		 System.out.println(callback);
		if (StringUtils.isEmpty(callback)) {
			
			exception.printStackTrace();
			log.error(exception.getMessage());
			return SysResult.fail();
		}else {
			exception.printStackTrace();
			log.error(exception.getMessage());
			return new JSONPObject(callback, SysResult.fail());
			
		}
		

	}

}
