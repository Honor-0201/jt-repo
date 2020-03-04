package com.jt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jt.service.FileService;
import com.jt.vo.EasyUIFile;

@RestController
public class FileController {
	@Autowired
	private FileService fileService;

	/**
	 * 实现商品文件上传
	 */
	@RequestMapping("/pic/upload")
	public EasyUIFile fileUpload(MultipartFile uploadFile) {
		System.err.println("fileUpload");
		return fileService.fileUpload(uploadFile);

	}

}
