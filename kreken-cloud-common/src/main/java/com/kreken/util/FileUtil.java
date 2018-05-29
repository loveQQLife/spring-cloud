package com.kreken.util;

import javax.swing.*;
import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

	private static String MESSAGE = "";
	public static final String UTF8 = "UTF-8";
	public static final String GBK = "GBK";
	public static final String GB2312 = "GB2312";

	/**
	 * 判断是否符是合法的文件路径
	 *
	 * @param path 需要处理的文件路径
	 */
	public final static boolean legalFile(String path) {
		//下面的正则表达式有问题
		String regex = "[a-zA-Z]:(?:[/][^/:*?\"<>|.][^/:*?\"<>|]{0,254})+";
		//String regex ="^([a-zA-z]:)|(^\\.{0,2}/)|(^\\w*)\\w([^:?*\"><|]){0,250}";
		return RegUtil.isMatche(commandPath(path), regex);
	}

	/**
	 * 返回一个通用的文件路径
	 *
	 * @param file 需要处理的文件路径
	 * @return
	 * Summary windows中路径分隔符是\在linux中是/但windows也支持/方式 故全部使用/
	 */
	public final static String commandPath(String file) {
		return file.replaceAll("\\\\{1,}", "/").replaceAll("\\/{2,}", "/");
	}


	public static void saveToFile(String fileName, InputStream in) throws IOException {
		FileOutputStream fos = null;
		BufferedInputStream bis = null;
		int BUFFER_SIZE = 1024;
		byte[] buf = new byte[BUFFER_SIZE];
		int size = 0;
		// 获取输入流
		bis = new BufferedInputStream(in);
		// //建立文件
		File file = new File(fileName);
		File dir = new File(file.getParent());
		if (!dir.isDirectory()) {
			dir.mkdirs();
		}

		if (file.createNewFile()) {
			fos = new FileOutputStream(fileName, false);

			// //保存文件
			while ((size = bis.read(buf)) != -1)
				fos.write(buf, 0, size);
			fos.close();
		}

		bis.close();
	}

	/**
	 * 
	 * @param fileDir
	 *            原文件名, 例如：test.jpg
	 * @param fileType
	 *            文件类型后缀，例如：jpg、gif
	 * @param in
	 * @return String 新文件名(md5值+文件类型后缀)
	 * @throws IOException
	 */
	public static String saveToFile(String fileDir, String fileType, InputStream in) throws IOException {

		byte[] _tempBytes = input2byte(in);

		String md5FileName = null;

		try {
			md5FileName = MD5Util.MD5(_tempBytes) + "." + fileType;
		} catch (Exception ex) {
			return null;
		}

		in = byte2Input(_tempBytes);

		FileOutputStream fos = null;
		BufferedInputStream bis = null;
		int BUFFER_SIZE = 1024 * 25;// 25M
		byte[] buf = new byte[BUFFER_SIZE];
		int size = 0;

		// 获取输入流
		bis = new BufferedInputStream(in);
		// //建立文件
		String fileName = fileDir + md5FileName;
		File file = new File(fileName);
		File dir = new File(file.getParent());
		if (!dir.isDirectory()) {
			dir.mkdirs();
		}

		if (file.createNewFile()) {
			fos = new FileOutputStream(fileName, false);

			// //保存文件
			while ((size = bis.read(buf)) != -1)
				fos.write(buf, 0, size);
			fos.close();
		}

		bis.close();

		return md5FileName;
	}

	/**
	 * 将 inputStream 转换成 String(字节流)
	 * 
	 * @param is
	 * @param charsetName
	 * @return
	 * @throws IOException
	 */
	public static String inputStreamToString(InputStream is, String charsetName) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int i = -1;
		while ((i = is.read()) != -1) {
			baos.write(i);
		}

		if (!StringUtil.isNullOrEmpty(charsetName) && Charset.forName(charsetName) != null) {
			return baos.toString(charsetName);
		} else {
			return baos.toString();
		}
	}

	/**
	 * 读取文件里面的数据(字符流)
	 *
	 * @param inputStream  文件的路径
	 * @param encoding  文件编码
     * @return
     */
	public String inputStreamToStringReader(InputStream inputStream,String encoding){

		try {
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream,encoding);
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			StringBuffer stringBuffer = new StringBuffer();
			String line = "";
			while ((line  = bufferedReader.readLine()) != null){
				stringBuffer.append(line);
			}
			return stringBuffer.toString();
		}catch (Exception e){
			return null;
		}
	}

	/**
	 * 将 inputStream 转换成 String
	 * 
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public static String inputStreamToString(InputStream is) throws IOException {
		return inputStreamToString(is, null);
	}

	/**
	 * 复制单个文件
	 * 
	 * @param srcFileName
	 *            待复制的文件名
	 * @param destFileName
	 *            目标文件名
	 * @param overlay
	 *            如果目标文件存在，是否覆盖
	 * @return 如果复制成功返回true，否则返回false
	 */
	public static boolean copyFile(String srcFileName, String destFileName, boolean overlay) {
		File srcFile = new File(srcFileName);

		// 判断源文件是否存在
		if (!srcFile.exists()) {
			MESSAGE = "源文件：" + srcFileName + "不存在！";
			JOptionPane.showMessageDialog(null, MESSAGE);
			return false;
		} else if (!srcFile.isFile()) {
			MESSAGE = "复制文件失败，源文件：" + srcFileName + "不是一个文件！";
			JOptionPane.showMessageDialog(null, MESSAGE);
			return false;
		}

		// 判断目标文件是否存在
		File destFile = new File(destFileName);
		if (destFile.exists()) {
			// 如果目标文件存在并允许覆盖
			if (overlay) {
				// 删除已经存在的目标文件，无论目标文件是目录还是单个文件
				new File(destFileName).delete();
			}
		} else {
			// 如果目标文件所在目录不存在，则创建目录
			if (!destFile.getParentFile().exists()) {
				// 目标文件所在目录不存在
				if (!destFile.getParentFile().mkdirs()) {
					// 复制文件失败：创建目标文件所在目录失败
					return false;
				}
			}
		}

		// 复制文件
		int byteread = 0; // 读取的字节数
		InputStream in = null;
		OutputStream out = null;

		try {
			in = new FileInputStream(srcFile);
			out = new FileOutputStream(destFile);
			byte[] buffer = new byte[1024];

			while ((byteread = in.read(buffer)) != -1) {
				out.write(buffer, 0, byteread);
			}
			return true;
		} catch (FileNotFoundException e) {
			return false;
		} catch (IOException e) {
			return false;
		} finally {
			try {
				if (out != null)
					out.close();
				if (in != null)
					in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 复制整个目录的内容
	 * 
	 * @param srcDirName
	 *            待复制目录的目录名
	 * @param destDirName
	 *            目标目录名
	 * @param overlay
	 *            如果目标目录存在，是否覆盖
	 * @return 如果复制成功返回true，否则返回false
	 */
	public static boolean copyDirectory(String srcDirName, String destDirName, boolean overlay) {
		// 判断源目录是否存在
		File srcDir = new File(srcDirName);
		if (!srcDir.exists()) {
			MESSAGE = "复制目录失败：源目录" + srcDirName + "不存在！";
			JOptionPane.showMessageDialog(null, MESSAGE);
			return false;
		} else if (!srcDir.isDirectory()) {
			MESSAGE = "复制目录失败：" + srcDirName + "不是目录！";
			JOptionPane.showMessageDialog(null, MESSAGE);
			return false;
		}

		// 如果目标目录名不是以文件分隔符结尾，则加上文件分隔符
		if (!destDirName.endsWith(File.separator)) {
			destDirName = destDirName + File.separator;
		}
		File destDir = new File(destDirName);
		// 如果目标文件夹存在
		if (destDir.exists()) {
			// 如果允许覆盖则删除已存在的目标目录
			if (overlay) {
				new File(destDirName).delete();
			} else {
				MESSAGE = "复制目录失败：目的目录" + destDirName + "已存在！";
				JOptionPane.showMessageDialog(null, MESSAGE);
				return false;
			}
		} else {
			// 创建目的目录
			System.out.println("目的目录不存在，准备创建。。。");
			if (!destDir.mkdirs()) {
				System.out.println("复制目录失败：创建目的目录失败！");
				return false;
			}
		}

		boolean flag = true;
		File[] files = srcDir.listFiles();
		for (int i = 0; i < files.length; i++) {
			// 复制文件
			if (files[i].isFile()) {
				flag = FileUtil.copyFile(files[i].getAbsolutePath(), destDirName + files[i].getName(), overlay);
				if (!flag)
					break;
			} else if (files[i].isDirectory()) {
				flag = FileUtil.copyDirectory(files[i].getAbsolutePath(), destDirName + files[i].getName(), overlay);
				if (!flag)
					break;
			}
		}
		if (!flag) {
			MESSAGE = "复制目录" + srcDirName + "至" + destDirName + "失败！";
			JOptionPane.showMessageDialog(null, MESSAGE);
			return false;
		} else {
			return true;
		}
	}

	/**
	 *  字节数组转为流
	 *
	 * @param buf
	 * @return
	 */
	public static final InputStream byte2Input(byte[] buf) {
		return new ByteArrayInputStream(buf);
	}

	/**
	 *  流转为字节数组
	 *
	 * @param inStream
	 * @return
	 * @throws IOException
	 */
	public static final byte[] input2byte(InputStream inStream) throws IOException {
		ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
		byte[] buff = new byte[100];
		int rc = 0;
		while ((rc = inStream.read(buff, 0, 100)) > 0) {
			swapStream.write(buff, 0, rc);
		}
		byte[] in2b = swapStream.toByteArray();
		return in2b;
	}

	/**
	 *  将流保存为文件
	 *
	 * @param is
	 * @param outfile
	 */
	public final static void streamSaveAsFile(InputStream is, File outfile) {
		try (FileOutputStream fos = new FileOutputStream(outfile)) {
			byte[] buffer = new byte[1024];
			int len;
			while ((len = is.read(buffer)) > 0) {
				fos.write(buffer, 0, len);
			}

		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * 创建多级目录
	 *
	 * @param paths 需要创建的目录
	 * @return 是否成功
	 */
	public final static boolean createPaths(String paths) {
		File dir = new File(paths);
		return !dir.exists() && dir.mkdir();
	}

	/**
	 * 创建文件支持多级目录
	 *
	 * @param filePath 需要创建的文件
	 * @return 是否成功
	 */
	public final static boolean createFiles(String filePath) {
		File file = new File(filePath);
		File dir = file.getParentFile();
		if (!dir.exists()) {
			if (dir.mkdirs()) {
				try {
					return file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}

	/**
	 * 查找符合正则表达式reg的的文件
	 *
	 * @param dirPath 搜索的目录
	 * @param reg     正则表达式
	 * @return 返回文件列表
	 */
	public final static List<File> searchFileReg(File dirPath, String reg) {
		List<File> list = new ArrayList<>();
		File[] files = dirPath.listFiles();
		if (files != null && files.length > 0 ) {
			for (File file : files) {
				if (file.isDirectory()) {
					list.addAll(searchFile(file, reg));
				} else {
					String Name = file.getName();
					if (RegUtil.isMatche(Name, reg)) {
						list.add(file);
					}
				}
			}
		}
		return list;
	}

	/**
	 * 在指定的目录下搜寻文个文件
	 *
	 * @param dirPath  搜索的目录
	 * @param fileName 搜索的文件名
	 * @return 返回文件列表
	 */
	public final static List<File> searchFile(File dirPath, String fileName) {
		List<File> list = new ArrayList<>();
		File[] files = dirPath.listFiles();
		if (files.length > 0 && files != null) {
			for (File file : files) {
				if (file.isDirectory()) {
					list.addAll(searchFile(file, fileName));
				} else {
					String Name = file.getName();
					if (Name.equals(fileName)) {
						list.add(file);
					}
				}
			}
		}
		return list;
	}

	/**
	 * 罗列指定路径下的全部文件
	 *
	 * @param path 需要处理的文件
	 * @return 包含所有文件的的list
	 */
	public final static List<File> listFile(String path) {
		File file = new File(path);
		return listFile(file);
	}

	/**
	 * 罗列指定路径下的全部文件
	 * @param path 需要处理的文件
	 * @param child 是否罗列子文件
	 * @return 包含所有文件的的list
	 */
	public final static List<File> listFile(String path,boolean child){
		return listFile(new File(path),child);
	}


	/**
	 * 罗列指定路径下的全部文件
	 *
	 * @param path 需要处理的文件
	 * @return 返回文件列表
	 */
	public final static List<File> listFile(File path) {
		List<File> list = new ArrayList<>();
		File[] files = path.listFiles();
		if (files.length > 0 && files != null) {
			for (File file : files) {
				if (file.isDirectory()) {
					list.addAll(listFile(file));
				} else {
					list.add(file);
				}
			}
		}
		return list;
	}

	/**
	 * 罗列指定路径下的全部文件
	 * @param path 指定的路径
	 * @param child 是否罗列子目录
	 * @return
	 */
	public final static List<File> listFile(File path,boolean child){
		List<File> list = new ArrayList<>();
		File[] files = path.listFiles();
		if (files.length > 0 && files != null) {
			for (File file : files) {
				if (child && file.isDirectory()) {
					list.addAll(listFile(file));
				} else {
					list.add(file);
				}
			}
		}
		return list;
	}
}
