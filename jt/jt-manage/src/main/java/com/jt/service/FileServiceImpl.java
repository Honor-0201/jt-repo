package com.jt.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jt.vo.EasyUIFile;

/** 文件上传业务层 */
@Service
//@PropertySources({@PropertySource("classpath:/properties/image.properties"),@PropertySource("classpath:/properties/image.properties")})
@PropertySource("classpath:/properties/image.properties")
public class FileServiceImpl implements FileService {
	
	@Value("${image.localDir}")
	private String localDir;// = "D:/Java_software/sts-workspace/JT-iamges/";
	@Value("${image.localDirUrl}")
	private String localDirUrl;// = "http://image.jt.com/";

	/**
	 * 1.校验：判断文件是否为图片 jpg|png|gif 
	 * 2.防止恶意程序 -是否有宽高 
	 * 3.图片分文件保存 yyyy/MM/dd/HH/mm/ss
	 * 4.修改文件名字防止重名 UUID 几乎不重名 
	 * 5.实现文件上传
	 */
	@Override
	public EasyUIFile fileUpload(MultipartFile uploadFile) {
		EasyUIFile easyUIFile = new EasyUIFile();
		String fileName = uploadFile.getOriginalFilename();
		fileName = fileName.toLowerCase();

		String regex = "^.+\\.(jpg|png|gif)$";
		if (!fileName.matches(regex)) {
			return EasyUIFile.file();
		}
		// 判断是否为恶意程序
		try {
			BufferedImage image = ImageIO.read(uploadFile.getInputStream());
			int width = image.getWidth();
			int height = image.getHeight();

			if (width == 0 || height == 0) {
				return EasyUIFile.file();
			}

			// 实现分文件存储 yyyy/MM/dd
			String dateDir = new SimpleDateFormat("yyyy/MM/dd/").format(new Date());

			String fileDirPath = localDir + dateDir;

			File dirFile = new File(fileDirPath);
			if (!dirFile.exists()) {
				dirFile.mkdirs();
			}

			// 实现文件名称重命名
			String fileType = fileName.substring(fileName.lastIndexOf("."));
			String uuid = UUID.randomUUID().toString();

			String realFileName = uuid + fileType;

			// 实现文件上传
			uploadFile.transferTo(new File(fileDirPath + realFileName));
			String url = localDirUrl + dateDir + realFileName;
			System.err.println(url);
			easyUIFile.setHeight(height).setWidth(width).setUrl(url);

		} catch (IOException e) {
			e.printStackTrace();
			return EasyUIFile.file();
		}

		return easyUIFile;
	}

}
