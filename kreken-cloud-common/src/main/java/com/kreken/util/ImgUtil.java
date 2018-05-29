package com.kreken.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.*;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ImgUtil {

	/**
	 * HTTP图片地址下载生成图片函数
	 * 
	 * @param imgHttpUrl
	 *            HTTP图片地址
	 * @param fileDir
	 *            保存到目录
	 * @param fileName
	 *            文件名
	 */
	public static String downloadImg(String imgHttpUrl, String fileDir,
			String fileName) {
		try {
			// 创建流
			BufferedInputStream in = new BufferedInputStream(
					new URL(imgHttpUrl).openStream());

			// 存放地址
			String filePath = fileDir + File.separator + fileName;

			File img = new File(filePath);

			File dir = new File(img.getParent());
			if (!dir.isDirectory()) {
				dir.mkdirs();
			}

			// 生成图片
			BufferedOutputStream out = new BufferedOutputStream(
					new FileOutputStream(img));
			byte[] buf = new byte[2048];
			int length = in.read(buf);
			while (length != -1) {
				out.write(buf, 0, length);
				length = in.read(buf);
			}
			in.close();
			out.close();
			return fileName;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * HTTP图片地址下载生成图片函数
	 * 
	 * @param imgHttpUrl
	 *            HTTP图片地址
	 * @param fileDir
	 *            保存到目录
	 *            文件名
	 */
	public static String downloadImg(String imgHttpUrl, String fileDir,
			int width, int height) {
		try {
			// 创建流
			BufferedInputStream in = new BufferedInputStream(
					new URL(imgHttpUrl).openStream());

			InputStream inputStream = scale(in, width, height);

			return FileUtil.saveToFile(fileDir, "jpg", inputStream);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * HTTP图片地址下载生成图片函数
	 * 
	 * @param imgHttpUrl
	 *            HTTP图片地址
	 * @param fileDir
	 *            保存到目录
	 * @param fileName
	 *            文件名
	 */
	public static void downloadImg(String imgHttpUrl, String fileDir,
			String fileName, int width, int height) {
		try {
			// 创建流
			BufferedInputStream in = new BufferedInputStream(
					new URL(imgHttpUrl).openStream());

			InputStream inputStream = scale(in, width, height);

			FileUtil.saveToFile(fileDir + fileName, inputStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 缩放图像
	 * 
	 * @param srcImageFile
	 *            源图像文件地址
	 * @param result
	 *            缩放后的图像地址
	 * @param nWidth
	 *            新文件宽度
	 * @param nHeight
	 *            新文件高度
	 */
	public static String scale(String srcImageFile, String result, int nWidth,
			int nHeight) {
		try {
			BufferedImage src = ImageIO.read(new File(srcImageFile)); // 读入文件
			Image image = src.getScaledInstance(nWidth, nHeight,
					Image.SCALE_DEFAULT);
			BufferedImage tag = new BufferedImage(nWidth, nHeight,
					BufferedImage.TYPE_INT_RGB);
			Graphics g = tag.getGraphics();
			g.drawImage(image, 0, 0, null); // 绘制缩小后的图
			g.dispose();
			ImageIO.write(tag, "JPEG", new File(result));// 输出到文件流
		} catch (IOException e) {
			e.printStackTrace();
		}
		return srcImageFile;
	}

	/**
	 * 缩放图像
	 * 
	 * @param srcImageFile
	 *            源图像文件地址
	 * @param result
	 *            缩放后的图像地址
	 * @param scale
	 *            缩放比例
	 * @param flag
	 *            缩放选择:true 放大; false 缩小;
	 */
	public static String scale(String srcImageFile, String result, int scale,
			boolean flag) {
		try {
			BufferedImage src = ImageIO.read(new File(srcImageFile)); // 读入文件
			int width = src.getWidth(); // 得到源图宽
			int height = src.getHeight(); // 得到源图长
			if (flag) {
				// 放大
				width = width * scale;
				height = height * scale;
			} else {
				// 缩小
				width = width / scale;
				height = height / scale;
			}
			Image image = src.getScaledInstance(width, height,
					Image.SCALE_DEFAULT);
			BufferedImage tag = new BufferedImage(width, height,
					BufferedImage.TYPE_INT_RGB);
			Graphics g = tag.getGraphics();
			g.drawImage(image, 0, 0, null); // 绘制缩小后的图
			g.dispose();
			ImageIO.write(tag, "JPEG", new File(result));// 输出到文件流
		} catch (IOException e) {
			e.printStackTrace();
		}
		return srcImageFile;
	}

	/**
	 * 
	 * @param inputStream
	 * @param nWidth
	 * @param nHeight
	 * @return
	 */
	public static InputStream scale(InputStream inputStream, int nWidth,
			int nHeight) {
		return scale(inputStream, "JPEG", nWidth, nHeight, false);
	}

	/**
	 * 
	 * @param inputStream
	 * @param nWidth
	 * @param nHeight
	 * @param isScale
	 *            是否按比例缩放
	 * @return
	 */
	public static InputStream scale(InputStream inputStream, String fileType,
			int nWidth, int nHeight, boolean isScale) {
		try {

			if (fileType.equalsIgnoreCase("jpg")) {
				fileType = "jpeg";
			}

			BufferedImage src = ImageIO.read(inputStream); // 读入文件

			int width = src.getWidth();
			int height = src.getHeight();

			if (isScale) {
				if (!(nWidth > width && nHeight > height)) {
					//等比例压缩
					if (nWidth * height / width > nHeight) {
						width = nHeight * width / height;
						height = nHeight;
					} else {
						width = nWidth;
						height = nWidth * height / width;
					}
				}
			} else {
				width = nWidth;
				height = nHeight;
			}

			Image image = src.getScaledInstance(width, height,
					Image.SCALE_DEFAULT);
			BufferedImage tag = new BufferedImage(width, height,
					BufferedImage.TYPE_INT_RGB);
			Graphics g = tag.getGraphics();
			g.drawImage(image, 0, 0, null); // 绘制缩小后的图
			g.dispose();
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ImageIO.write(tag, fileType.toUpperCase(), bos);

			ByteArrayInputStream in = new ByteArrayInputStream(
					bos.toByteArray());
			return in;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 *
	 * @param inputStream
	 * @param nWidth
	 * @param nHeight
	 * @param scale  按照比例压缩 80% 0.8f
	 * @param isScale
	 *            是否按比例缩放
	 * @return
	 */
	public static InputStream scale(InputStream inputStream, String fileType,
									int nWidth, int nHeight,int scale, boolean isScale) {
		try {

			if (fileType.equalsIgnoreCase("jpg")) {
				fileType = "jpeg";
			}

			BufferedImage src = ImageIO.read(inputStream); // 读入文件

			int width = src.getWidth();
			int height = src.getHeight();

			if (isScale) {
				if (!(nWidth > width && nHeight > height)) {
					//等比例压缩
					width = width / scale;
					height = height / scale;
				}
			} else {
				width = nWidth;
				height = nHeight;
			}
			Image image = src.getScaledInstance(width, height,
					Image.SCALE_DEFAULT);
			BufferedImage tag = new BufferedImage(width, height,
					BufferedImage.TYPE_INT_RGB);
			Graphics g = tag.getGraphics();
			g.drawImage(image, 0, 0, null); // 绘制缩小后的图
			g.dispose();
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ImageIO.write(tag, fileType.toUpperCase(), bos);

			ByteArrayInputStream in = new ByteArrayInputStream(
					bos.toByteArray());
			return in;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * @param inputStream
	 * @param nWidth
	 * @param nHeight
	 *
	 * @return
	 */
	public static InputStream scale(InputStream inputStream, String fileType,
			int nWidth, int nHeight) {
		return scale(inputStream, fileType, nWidth, nHeight, false);
	}

	/** */
	/**
	 * 图像切割
	 * 
	 * @param srcImageFile
	 *            源图像地址
	 * @param descDir
	 *            切片目标文件夹
	 * @param destWidth
	 *            目标切片宽度
	 * @param destHeight
	 *            目标切片高度
	 */
	public static void cut(String srcImageFile, String descDir, int destWidth,
			int destHeight) {
		try {
			Image img;
			ImageFilter cropFilter;
			// 读取源图像
			BufferedImage bi = ImageIO.read(new File(srcImageFile));
			int srcWidth = bi.getHeight(); // 源图宽度
			int srcHeight = bi.getWidth(); // 源图高度
			if (srcWidth > destWidth && srcHeight > destHeight) {
				Image image = bi.getScaledInstance(srcWidth, srcHeight,
						Image.SCALE_DEFAULT);
				destWidth = 200; // 切片宽度
				destHeight = 150; // 切片高度
				int cols = 0; // 切片横向数量
				int rows = 0; // 切片纵向数量
				// 计算切片的横向和纵向数量
				if (srcWidth % destWidth == 0) {
					cols = srcWidth / destWidth;
				} else {
					cols = (int) Math.floor(srcWidth / destWidth) + 1;
				}
				if (srcHeight % destHeight == 0) {
					rows = srcHeight / destHeight;
				} else {
					rows = (int) Math.floor(srcHeight / destHeight) + 1;
				}
				// 循环建立切片
				// 改进的想法:是否可用多线程加快切割速度
				for (int i = 0; i < rows; i++) {
					for (int j = 0; j < cols; j++) {
						// 四个参数分别为图像起点坐标和宽高
						// 即: CropImageFilter(int x,int y,int width,int height)
						cropFilter = new CropImageFilter(j * 200, i * 150,
								destWidth, destHeight);
						img = Toolkit.getDefaultToolkit().createImage(
								new FilteredImageSource(image.getSource(),
										cropFilter));
						BufferedImage tag = new BufferedImage(destWidth,
								destHeight, BufferedImage.TYPE_INT_RGB);
						Graphics g = tag.getGraphics();
						g.drawImage(img, 0, 0, null); // 绘制缩小后的图
						g.dispose();
						// 输出为文件
						ImageIO.write(tag, "JPEG", new File(descDir
								+ "pre_map_" + i + "_" + j + ".jpg"));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** */
	/**
	 * 图像类型转换 GIF->JPG GIF->PNG PNG->JPG PNG->GIF(X)
	 */
	public static void convert(String source, String result) {
		try {
			File f = new File(source);
			f.canRead();
			f.canWrite();
			BufferedImage src = ImageIO.read(f);
			ImageIO.write(src, "JPG", new File(result));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/** */
	/**
	 * 彩色转为黑白
	 * 
	 * @param source
	 * @param result
	 */
	public static void gray(String source, String result) {
		try {
			BufferedImage src = ImageIO.read(new File(source));
			ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);
			ColorConvertOp op = new ColorConvertOp(cs, null);
			src = op.filter(src, null);
			ImageIO.write(src, "JPEG", new File(result));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 图片拼接
	 * 
	 * @param files
	 *            要拼接的文件列表
	 * @param type
	 *            1 横向拼接， 2 纵向拼接
	 * @return
	 */
	public static InputStream merge(String[] files, int type) {
		int len = files.length;
		if (len < 1) {
			return null;
		}

		File[] src = new File[len];
		BufferedImage[] images = new BufferedImage[len];
		int[][] ImageArrays = new int[len][];
		for (int i = 0; i < len; i++) {
			try {
				src[i] = new File(files[i]);
				images[i] = ImageIO.read(src[i]);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			int width = images[i].getWidth();
			int height = images[i].getHeight();
			ImageArrays[i] = new int[width * height];// 从图片中读取RGB
			ImageArrays[i] = images[i].getRGB(0, 0, width, height,
					ImageArrays[i], 0, width);
		}

		int newHeight = 0;
		int newWidth = 0;
		for (int i = 0; i < images.length; i++) {
			// 横向
			if (type == 1) {
				newHeight = newHeight > images[i].getHeight() ? newHeight
						: images[i].getHeight();
				newWidth += images[i].getWidth();
			} else if (type == 2) {// 纵向
				newWidth = newWidth > images[i].getWidth() ? newWidth
						: images[i].getWidth();
				newHeight += images[i].getHeight();
			}
		}

		if (type == 1 && newWidth < 1) {
			return null;
		}
		if (type == 2 && newHeight < 1) {
			return null;
		}

		// 生成新图片
		try {
			BufferedImage ImageNew = new BufferedImage(newWidth, newHeight,
					BufferedImage.TYPE_INT_RGB);
			int height_i = 0;
			int width_i = 0;
			for (int i = 0; i < images.length; i++) {
				if (type == 1) {
					ImageNew.setRGB(width_i, 0, images[i].getWidth(),
							newHeight, ImageArrays[i], 0, images[i].getWidth());
					width_i += images[i].getWidth();
				} else if (type == 2) {
					ImageNew.setRGB(0, height_i, newWidth,
							images[i].getHeight(), ImageArrays[i], 0, newWidth);
					height_i += images[i].getHeight();
				}
			}

			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ImageIO.write(ImageNew, "jpg", out);// 图片写入到输出流中

			ByteArrayInputStream in = new ByteArrayInputStream(
					out.toByteArray());
			return in;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 图片拼接
	 * 
	 * @param files
	 *            要拼接的文件列表
	 * @param type
	 *            1 横向拼接， 2 纵向拼接
	 * @param newFilename
	 *            新文件路径，*.jpg
	 * @return
	 */
	public static void merge(String[] files, int type, String newFilename) {

		int len = files.length;
		if (len < 1) {
			return;
		}

		File[] src = new File[len];
		BufferedImage[] images = new BufferedImage[len];
		int[][] ImageArrays = new int[len][];
		for (int i = 0; i < len; i++) {
			try {
				src[i] = new File(files[i]);
				images[i] = ImageIO.read(src[i]);
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
			int width = images[i].getWidth();
			int height = images[i].getHeight();
			ImageArrays[i] = new int[width * height];// 从图片中读取RGB
			ImageArrays[i] = images[i].getRGB(0, 0, width, height,
					ImageArrays[i], 0, width);
		}

		int newHeight = 0;
		int newWidth = 0;
		for (int i = 0; i < images.length; i++) {
			// 横向
			if (type == 1) {
				newHeight = newHeight > images[i].getHeight() ? newHeight
						: images[i].getHeight();
				newWidth += images[i].getWidth();
			} else if (type == 2) {// 纵向
				newWidth = newWidth > images[i].getWidth() ? newWidth
						: images[i].getWidth();
				newHeight += images[i].getHeight();
			}
		}

		if (type == 1 && newWidth < 1) {
			return;
		}
		if (type == 2 && newHeight < 1) {
			return;
		}

		// 生成新图片
		try {
			BufferedImage ImageNew = new BufferedImage(newWidth, newHeight,
					BufferedImage.TYPE_INT_RGB);
			int height_i = 0;
			int width_i = 0;
			for (int i = 0; i < images.length; i++) {
				if (type == 1) {
					ImageNew.setRGB(width_i, 0, images[i].getWidth(),
							newHeight, ImageArrays[i], 0, images[i].getWidth());
					width_i += images[i].getWidth();
				} else if (type == 2) {
					ImageNew.setRGB(0, height_i, newWidth,
							images[i].getHeight(), ImageArrays[i], 0, newWidth);
					height_i += images[i].getHeight();
				}
			}

			File outFile = new File(newFilename);
			ImageIO.write(ImageNew, "jpg", outFile);// 写图片到文件路径

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 缩放
	private static void zoomImage(File out, String name, int rtype, int cw,
			int ch, int w, int h) throws Exception {
		BufferedImage bimg0 = ImageIO.read(new File(out, name));
		int bw = bimg0.getWidth();
		int bh = bimg0.getHeight();
		int type = -1;
		String format = null;
		if (name.toLowerCase().endsWith("png")) {
			format = "PNG";
			type = BufferedImage.TYPE_INT_ARGB;

		} else {
			format = "JPG";
			type = BufferedImage.TYPE_INT_RGB;
		}
		int zw = 0;
		int zh = 0;
		switch (rtype) {
		case 1:
			// 宽度ZOOM
			zw = w - 2 * cw;
			zh = ch;
			break;
		case 2:
			// 高度ZOOM
			zw = cw;
			zh = h - 2 * ch;
			break;
		case 3:
			// 高宽ZOOM
			zw = w - 2 * cw;
			zh = h - 2 * ch;
			break;
		}
		BufferedImage image = new BufferedImage(zw, zh, type);
		Graphics g = image.getGraphics();
		g.drawImage(bimg0, 0, 0, zw, zh, 0, 0, bw, bh, null);
		ImageIO.write(image, format, new File(out, name));
	}

	// 合并9图
	private static void mergeImage(File out, String name, String[] images,
			int w, int h) throws Exception {
		BufferedImage bimg0 = ImageIO.read(new File(out.getParentFile(), name));
		int type = -1;
		String format = null;
		if (name.toLowerCase().endsWith("png")) {
			format = "PNG";
			type = BufferedImage.TYPE_INT_ARGB;

		} else {
			format = "JPG";
			type = BufferedImage.TYPE_INT_RGB;
		}

		BufferedImage image = new BufferedImage(w, h, type);
		Graphics g = image.getGraphics();

		int x = 0;
		int y = 0;
		for (int i = 0; i < images.length; i++) {
			BufferedImage bimg = ImageIO.read(new File(out, images[i]));
			g.drawImage(bimg, x, y, bimg.getWidth(), bimg.getHeight(), null);
			if (i % 3 == 0)
				x = x + bimg.getWidth();
			else if (i % 3 == 1)
				x = x + bimg.getWidth();
			else if (i % 3 == 2)
				x = 0;
			// g.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2,
			// observer)

			if (i % 3 == 2)
				y = y + bimg.getHeight();

		}
		ImageIO.write(image, format, new File(out.getParentFile(), name));
	}

	/**
	 * 9Cut(微信朋友圈9小图)切割图片处理
	 * 
	 * @param out
	 *            原图文件路径
	 * @param name
	 *            新图片名称(png、jpg)
	 * @param w
	 *            每张小图的宽度
	 * @param h
	 *            每张小图的高度
	 * @throws Exception
	 */
	public static void cut9Image(File out, String name, int w, int h)
			throws Exception {
		File cache = new File(out.getPath() + "/cache");
		if (!cache.exists())
			cache.mkdir();
		// 原图
		BufferedImage bimg0 = ImageIO.read(new File(out, name));
		String[] images = new String[9];
		List list = new ArrayList();

		int w0 = bimg0.getWidth();
		int h0 = bimg0.getHeight();
		int type = -1;
		String format = null;
		if (name.toLowerCase().endsWith("png")) {
			format = "PNG";
			type = BufferedImage.TYPE_INT_ARGB;

		} else {
			format = "JPG";
			type = BufferedImage.TYPE_INT_RGB;
		}
		// 切割成9部分
		/**
		 * 边角四块的高度和宽度
		 */
		int cornerwidth = 3;
		int cornerheight = 3;
		// 左上
		BufferedImage image1 = new BufferedImage(cornerwidth, cornerheight,
				type);
		// 右上
		BufferedImage image2 = new BufferedImage(cornerwidth, cornerheight,
				type);
		// 左下
		BufferedImage image3 = new BufferedImage(cornerwidth, cornerheight,
				type);
		// 右下
		BufferedImage image4 = new BufferedImage(cornerwidth, cornerheight,
				type);

		int topwidth = w0 - 2 * cornerwidth;
		int topheight = cornerheight;
		// TOP
		BufferedImage topbimg = new BufferedImage(topwidth, topheight, type);
		int leftwidth = cornerwidth;
		int leftheight = h0 - 2 * cornerheight;
		// LEFT
		BufferedImage leftbimg = new BufferedImage(leftwidth, leftheight, type);
		int midwidth = w0 - 2 * cornerwidth;
		int midheight = h0 - 2 * cornerheight;
		// middle
		BufferedImage midbimg = new BufferedImage(midwidth, midheight, type);
		int rightwidth = cornerwidth;
		int rightheight = h0 - 2 * cornerheight;
		// right
		BufferedImage rightbimg = new BufferedImage(rightwidth, rightheight,
				type);
		int bottomwidth = w0 - 2 * cornerwidth;
		int bottomheight = cornerheight;
		// bottom
		BufferedImage bottombimg = new BufferedImage(bottomwidth, bottomheight,
				type);

		Graphics g1 = image1.getGraphics();
		Graphics g2 = image2.getGraphics();
		Graphics g3 = image3.getGraphics();
		Graphics g4 = image4.getGraphics();
		Graphics gtop = topbimg.getGraphics();
		Graphics gleft = leftbimg.getGraphics();
		Graphics gmid = midbimg.getGraphics();
		Graphics gright = rightbimg.getGraphics();
		Graphics gbottom = bottombimg.getGraphics();
		String cutname = "";
		// 左上
		g1.drawImage(bimg0, 0, 0, cornerwidth, cornerheight, 0, 0, cornerwidth,
				cornerheight, null);
		cutname = "image1." + format;
		ImageIO.write(image1, format, new File(cache, cutname));
		images[0] = cutname;
		list.add(image1);
		// top
		gtop.drawImage(bimg0, 0, 0, topwidth, topheight, cornerwidth, 0,
				cornerwidth + topwidth, topheight, null);
		cutname = "top." + format;
		ImageIO.write(topbimg, format, new File(cache, cutname));
		zoomImage(cache, cutname, 1, cornerwidth, cornerheight, w, h);
		images[1] = cutname;
		list.add(topbimg);
		// 右上
		g2.drawImage(bimg0, 0, 0, cornerwidth, cornerheight, cornerwidth
				+ topwidth, 0, 2 * cornerwidth + topwidth, cornerheight, null);
		cutname = "image2." + format;
		ImageIO.write(image2, format, new File(cache, cutname));
		images[2] = cutname;
		list.add(image2);
		// 左
		gleft.drawImage(bimg0, 0, 0, leftwidth, leftheight, 0, cornerheight,
				0 + leftwidth, cornerheight + leftheight, null);
		cutname = "left." + format;
		ImageIO.write(leftbimg, format, new File(cache, cutname));
		zoomImage(cache, cutname, 2, cornerwidth, cornerheight, w, h);
		images[3] = cutname;
		list.add(leftbimg);
		// 中
		gmid.drawImage(bimg0, 0, 0, midwidth, midheight, cornerwidth,
				cornerheight, cornerwidth + midwidth, cornerheight + midheight,
				null);
		cutname = "mid." + format;
		ImageIO.write(midbimg, format, new File(cache, cutname));
		zoomImage(cache, cutname, 3, cornerwidth, cornerheight, w, h);
		images[4] = cutname;
		list.add(midbimg);
		// 右
		gright.drawImage(bimg0, 0, 0, rightwidth, rightheight, cornerwidth
				+ midwidth, cornerheight, 2 * cornerwidth + midwidth,
				cornerheight + midheight, null);
		cutname = "right." + format;
		ImageIO.write(rightbimg, format, new File(cache, cutname));
		zoomImage(cache, cutname, 2, cornerwidth, cornerheight, w, h);
		images[5] = cutname;
		list.add(rightbimg);
		// 左下
		g3.drawImage(bimg0, 0, 0, cornerwidth, cornerheight, 0, cornerheight
				+ leftheight, 0 + cornerwidth, 2 * cornerheight + leftheight,
				null);
		cutname = "image3." + format;
		ImageIO.write(image3, format, new File(cache, cutname));
		images[6] = cutname;
		list.add(image3);
		// 底
		gbottom.drawImage(bimg0, 0, 0, bottomwidth, bottomheight, cornerwidth,
				cornerheight + midheight, cornerwidth + bottomwidth, 2
						* cornerheight + midheight, null);
		cutname = "bottom." + format;
		ImageIO.write(bottombimg, format, new File(cache, cutname));
		zoomImage(cache, cutname, 1, cornerwidth, cornerheight, w, h);
		images[7] = cutname;
		list.add(bottombimg);
		// 右下
		g4.drawImage(bimg0, 0, 0, cornerwidth, cornerheight, cornerwidth
				+ bottomwidth, cornerheight + rightheight, 2 * cornerwidth
				+ bottomwidth, 2 * cornerheight + rightheight, null);
		cutname = "image4." + format;
		ImageIO.write(image4, format, new File(cache, cutname));
		images[8] = cutname;
		list.add(image4);

		mergeImage(cache, name, images, w, h);

		for (int i = 0; i < images.length; i++) {
			File file = new File(cache, images[i]);
			if (file.exists())
				file.delete();
		}
		cache.deleteOnExit();

	}
}
