package com.kreken.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @category spring 上传组件
 * @author kenshinhu
 */

public class UploadTools {

	private Logger log = LoggerFactory.getLogger(UploadTools.class);

	public final static String Validate_PASS = "PASS"; // 验证成功

	public final static String Validate_Error_MAXSIZE = "MAXSIZE"; // 验证失败，文件容量起出

	public final static String Validate_Error_Ext = "EXTNOTPASS"; // 验证失败，文件格式不对

	public static final String FILE_SEPARATOR = System.getProperties().getProperty("file.separator");

	// 文件大小
	private int MaxSize;

	public int getMaxSize() {
		return MaxSize;
	}

	public void setMaxSize(int maxSizeKB) {
		MaxSize = maxSizeKB * 1024;
	}

	/**
	 * @category 文件上传方式(指定文件名)
	 * @param file
	 *            文件实例
	 * @param path
	 *            文件路径
	 * @param name
	 *            文件名
	 * @throws Exception
	 */
	public String upload(MultipartFile file, String path, String name) throws Exception {

		log.info("FileUpload is Start");

		String filename = file.getOriginalFilename();

		String extName = filename.substring(filename.lastIndexOf(".")).toLowerCase();

		String lastFileName = name + extName;

		// 图片存储的相对路径
		String fileFullPath = path + FILE_SEPARATOR + lastFileName;

		FileCopyUtils.copy(file.getBytes(), new File(fileFullPath));

		log.info("local:" + fileFullPath);

		return lastFileName;

	}

	/**
	 * @category 上传图片方法 ,自动生成名字, 返回完整相对路径
	 * @param file
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public String upload_pic(MultipartFile file, String path) throws Exception {

		log.info("FileUpload is Start");

		String ValidateResult = validatePICTURE(file);

		// 当难验证不通过时对外抛出异常
		if (!ValidateResult.equals("PASS")) {
			throw new Exception(ValidateResult);
		}

		String filename = file.getOriginalFilename();

		String extName = filename.substring(filename.lastIndexOf(".")).toLowerCase();

		String lastFileName = System.currentTimeMillis() + extName;

		// 图片存储的相对路径

		checkDir(path, true);

		String fileFullPath = path + FILE_SEPARATOR + lastFileName;

		FileCopyUtils.copy(file.getBytes(), new File(fileFullPath));

		log.info("local:" + fileFullPath);

		return fileFullPath;

	}

	/**
	 * @category 上传图片方法 ,自动生成名字
	 * @param file
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public String upload_PICTURE(MultipartFile file,String path) throws Exception {

		log.info("FileUpload is Start");

		String ValidateResult = validatePICTURE(file);

		// 当难验证不通过时对外抛出异常
		if (!ValidateResult.equals("PASS")) {
			throw new Exception(ValidateResult);
		}

		String filename = file.getOriginalFilename();

		// String extName =
		// filename.substring(filename.lastIndexOf(".")).toLowerCase();

		String lastFileName = System.currentTimeMillis() + ".jpg";

		// 图片存储的相对路径

		checkDir(path, true);

		String fileFullPath = path + FILE_SEPARATOR + lastFileName;

		FileCopyUtils.copy(file.getBytes(), new File(fileFullPath));

		log.info("local:" + fileFullPath);

		return lastFileName;

	}

	/**
	 * @category 上传图片方法 ,生成指定名字
	 * @param file
	 * @param path
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("unused")
	public String upload_PICTURE(MultipartFile file, String path, String lastFileName) throws Exception {

		log.info("FileUpload is Start");

		String ValidateResult = validatePICTURE(file);

		// 当难验证不通过时对外抛出异常
		if (!ValidateResult.equals("PASS")) {
			throw new Exception(ValidateResult);
		}

		String filename = file.getOriginalFilename();

		// String extName =
		// filename.substring(filename.lastIndexOf(".")).toLowerCase();

		lastFileName = lastFileName + ".jpg";

		// 图片存储的相对路径
		String fileFullPath = path + FILE_SEPARATOR + lastFileName;

		FileCopyUtils.copy(file.getBytes(), new File(fileFullPath));

		log.info("local:" + fileFullPath);

		return lastFileName;

	}

	/**
	 * @category 上传并生成缩略图
	 * @param file
	 * @param path
	 * @param width
	 * @param height
	 * @return
	 * @throws Exception
	 */
	public String upload_PICTURE(MultipartFile file, String path, int width, int height) throws Exception {

		log.info("FileUpload is Start");

		String ValidateResult = validatePICTURE(file);

		// 当难验证不通过时对外抛出异常
		if (!ValidateResult.equals("PASS")) {
			throw new Exception(ValidateResult);
		}

		String filename = file.getOriginalFilename();

		String extName = filename.substring(filename.lastIndexOf(".")).toLowerCase();

		String lastFileName = System.currentTimeMillis() + extName;

		// 缩略图片存储的相对路径
		String fileFullPath = path + FILE_SEPARATOR + lastFileName;

		ScaleImage si = new ScaleImage();

		try {
			si.saveImage(file, fileFullPath, extName, width, height);
		} catch (Exception e) {
			e.printStackTrace();
			log.info("生成缩略图失败，转用直接上传");

		}
		// String pic = this.upload_PICTURE(file, path,lastFileName+"_big");
		log.info("local:" + fileFullPath);

		return lastFileName;

	}

	/**
	 * @category 文件上传方式(加密文件名)
	 * @param file
	 *            文件实例
	 * @param path
	 *            文件路径
	 * @throws Exception
	 */
	public String upload(MultipartFile file, String path) throws Exception {

		log.info("FileUpload is Start");

		// 当难验证不通过时对外抛出异常
		if (!validateFile(file)) {
			throw new Exception("EXTNOTPASS");
		}

		String filename = file.getOriginalFilename();

		String extName = filename.substring(filename.lastIndexOf(".")).toLowerCase();

		String lastFileName = System.currentTimeMillis() + extName;

		// 图片存储的相对路径
		String fileFullPath = path + FILE_SEPARATOR + lastFileName;

		FileCopyUtils.copy(file.getBytes(), new File(fileFullPath));

		log.info("local:" + fileFullPath);

		return lastFileName;

	}

	/**
	 * @category 文件上传方式(不加密文件名)
	 * @param file
	 *            文件实例
	 * @param path
	 *            文件路径
	 * @throws Exception
	 */
	public String upload1(MultipartFile file, String path) throws Exception {

		log.info("FileUpload is Start");

		// 当难验证不通过时对外抛出异常
		if (!validateFile(file)) {
			throw new Exception("EXTNOTPASS");
		}

		// SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");

		// String time = format.format(new Date());

		String filename = file.getOriginalFilename();

		String extName = filename.substring(filename.lastIndexOf(".")).toLowerCase();

		String firstName = filename.substring(0, filename.indexOf("."));

		// String lastFileName = System.currentTimeMillis() + extName;

		filename = firstName + System.currentTimeMillis() + extName;

		// 图片存储的相对路径
		String fileFullPath = path + FILE_SEPARATOR + filename;

		FileCopyUtils.copy(file.getBytes(), new File(fileFullPath));

		log.info("local:" + fileFullPath);

		return filename;

	}

	/**
	 * @category 文件上传方式(文件名：日期_文件名)
	 * @param file
	 *            文件实例
	 * @param path
	 *            文件路径
	 * @return 本地文件绝对路径
	 * @throws Exception
	 */
	public String upload2(MultipartFile file, String path) throws Exception {
		log.info("FileUpload is Start");
		// 当难验证不通过时对外抛出异常
		if (!validateAcceFile(file)) {
			throw new Exception("EXTNOTPASS");
		}
		String filename = file.getOriginalFilename();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		filename = sdf.format(new Date()) + filename;

		// 文件存储的相对路径
		String fileFullPath = path + FILE_SEPARATOR + filename;

		FileCopyUtils.copy(file.getBytes(), new File(fileFullPath));

		log.info("local:" + fileFullPath);

		return fileFullPath;

	}

	/**
	 * @category 验证文件格式
	 * @param file
	 * @return
	 */
	private String validatePICTURE(MultipartFile file) {
		if (file.getSize() < 0 || file.getSize() > MaxSize) return "MAXSIZE";
		String filename = file.getOriginalFilename();
		String extName = filename.substring(filename.lastIndexOf(".")).toLowerCase();
		if (extName.equals(".jpg") || extName.equals(".gif") || extName.equals(".png") || extName.equals(".bmp") || extName.equals(".jpeg") || extName.equals(".tif") || extName.equals(".ico")) {
			return "PASS";
		} else {
			return "EXTNOTPASS";
		}
	}

	/**
	 * @category 验证文件格式
	 * @param file
	 * @return
	 */
	private boolean validateFile(MultipartFile file) {
		if (file.getSize() < 0 || file.getSize() > MaxSize) return false;
		String filename = file.getOriginalFilename();
		String extName = filename.substring(filename.lastIndexOf(".")).toLowerCase();
		if (extName.equals(".jpg") || extName.equals(".pdf") || extName.equals(".rar") || extName.equals(".zip") || extName.equals(".gif") || extName.equals(".png") || extName.equals(".bmp") || extName.equals(".jpeg") || extName.equals(".doc") || extName.equals(".xls") || extName.equals(".docx") || extName.equals(".xlsx") || extName.equals(".ico")) {
			return true;
		} else {
			return false;
		}
	}

	private boolean validateAcceFile(MultipartFile file) {
		if (file.getSize() < 0 || file.getSize() > MaxSize) return false;
		String filename = file.getOriginalFilename();
		String extName = filename.substring(filename.lastIndexOf(".")).toLowerCase();
		if (extName.equals(".jpg") || extName.equals(".pdf") || extName.equals(".rar") || extName.equals(".zip") || extName.equals(".gif") || extName.equals(".png") || extName.equals(".bmp") || extName.equals(".jpeg") || extName.equalsIgnoreCase(".doc") || extName.equals(".xls") || extName.equals(".docx") || extName.equals(".xlsx") || extName.equals(".ppt") || extName.equals(".pptx") || extName.equals(".ico")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @category 通过对标签查找文件并删除文件
	 * @param body
	 */
	public void DeleteFileByTag(String body, String path) {

		List<String> ImageList = HtmlTagSrcSearch.findImgSrc(body);

		for (String filename : ImageList) {
			String filepath = path + filename;

			File delfile = new File(filepath);

			if (delfile.exists()) {
				delfile.delete();
				log.info("DELETE FILE AT " + filepath);
			} else {
				log.info("DELETE FILE AT " + filepath + " is not exists");
			}

		}
	}

	/**
	 * @category 删除文件
	 * @param path
	 */
	public void DeleteFile(String path) {
		File delfile = new File(path);
		if (delfile.exists()) {
			delfile.delete();
			log.info("DELETE FILE AT " + path);
		} else {
			log.info("DELETE FILE AT " + path + " is not exists");
		}
	}

	/**
	 * @category 上传广告图片
	 * @param file
	 * @param path
	 * @param width
	 * @param height
	 * @return
	 * @throws Exception
	 */
	public String upload_Pic(MultipartFile file, String path, int width, int height, String fileName) throws Exception {

		log.info("FileUpload is Start");

		// 当难验证不通过时对外抛出异常
		if (!validateFile(file)) {
			throw new Exception("EXTNOTPASS");
		}

		// String filename = file.getOriginalFilename();

		// String extName = filename.substring(filename.lastIndexOf("."))
		// .toLowerCase();

		String lastFileName = fileName + ".gif";

		// 图片存储的相对路径
		String fileFullPath = path + FILE_SEPARATOR + lastFileName;

		FileCopyUtils.copy(file.getBytes(), new File(fileFullPath));

		log.info("local:" + fileFullPath);

		return lastFileName;

	}

	public ReturnMessage checkDir(String dir, boolean create) {
		ReturnMessage rm = new ReturnMessage();
		File uploadDir = new File(dir);

		rm.setMessage("OK");
		rm.setResult(true);

		if (!uploadDir.exists()) {
			rm.setMessage("上传目录不存在。");
			rm.setResult(false);
		}

		if (!uploadDir.isDirectory()) {
			rm.setMessage("上传目录不是文件夹。");
			rm.setResult(false);
		}
		// 检查目录写权限
		if (!uploadDir.canWrite()) {
			rm.setMessage("上传目录没有写权限。");
			rm.setResult(false);
		}

		if (create) {
			if (!uploadDir.exists()) {
				uploadDir.mkdirs();
			}
		}

		return rm;
	}
}
