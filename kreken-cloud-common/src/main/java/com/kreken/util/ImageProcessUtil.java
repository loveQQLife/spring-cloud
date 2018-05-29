package com.kreken.util;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ImageProcessUtil {

	private static Font font = null;// 添加字体的属性设置

	public ImageProcessUtil() {

	}

	// 初始化的时候必须穿入font的参数
	public ImageProcessUtil(int fontSize, String fontFileUrl) {
		setFont(fontSize, fontFileUrl);
	}

	/**
	 * 设定文字的字体等
	 */
	public void setFont(int fontSize, String fontFileUrl) {
		try {
			if (font == null) {
				font = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream(fontFileUrl));
			}

			font = font.deriveFont(Font.TRUETYPE_FONT, fontSize);
		} catch (FontFormatException e) {
			e.printStackTrace();
		}catch( IOException ex){
			ex.printStackTrace();
		}
	}

	/**
	 * 导入本地图片到缓冲区
	 */
	public BufferedImage loadImageLocal(String imgName) {
		try {
			return ImageIO.read(new File(imgName));
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	/**
	 * 生成新图片到本地
	 */
	public void writeImageLocal(String newImage, BufferedImage img) {
		if (newImage != null && img != null) {
			try {
				File outputfile = new File(newImage);
				ImageIO.write(img, "png", outputfile);
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}
	}

	// 把图片修饰圆角 720°时候为圆形
	public BufferedImage makeRoundedCorner(BufferedImage image, int cornerRadius) {
		int w = image.getWidth();
		int h = image.getHeight();
		BufferedImage output = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

		Graphics2D g2 = output.createGraphics();
		output = g2.getDeviceConfiguration().createCompatibleImage(w, h, Transparency.TRANSLUCENT);
		g2.dispose();
		g2 = output.createGraphics();

		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.fillRoundRect(0, 0, w, h, cornerRadius, cornerRadius);

		g2.setComposite(AlphaComposite.SrcIn);

		g2.drawImage(image, 0, 0, w, h, null);

		g2.dispose();

		return output;
	}

	// 缩放
	public BufferedImage createResizedCopy(Image originalImage, int scaledWidth, int scaledHeight,
			boolean preserveAlpha) {
		int imageType = preserveAlpha ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
		BufferedImage scaledBI = new BufferedImage(scaledWidth, scaledHeight, imageType);
		Graphics2D g = scaledBI.createGraphics();
		if (preserveAlpha) {
			g.setComposite(AlphaComposite.Src);
		}
		g.drawImage(originalImage, 0, 0, scaledWidth, scaledHeight, null);
		g.dispose();
		return scaledBI;
	}

	/**
	 * 合并图层（目前只支持两张）
	 * 
	 * @param bg
	 *            背景图
	 * @param otherPhoto
	 *            其他图片
	 * @param x
	 *            其他图片要插入的x轴坐标
	 * @param y
	 *            其他图片要插入的y轴坐标
	 * @return
	 */
	public BufferedImage mergeLayers(BufferedImage bg, BufferedImage otherPhoto, int x, int y) {
		Graphics g = bg.getGraphics();
		g.drawImage(otherPhoto, x, y, otherPhoto.getWidth(), otherPhoto.getHeight(), null);
		g.dispose();
		return bg;
	}

	/**
	 * 在图片上添加文字
	 */
	public BufferedImage modifyImage(BufferedImage img, String content, int x, int y) {
		// 图片长宽
		int w = img.getWidth();
		// int h = img.getHeight();//可以不要
		// 文字的长宽
		if (font == null) {
			setFont(36, getClass().getResource("/").getFile().toString() + new File("/check/MSYH.TTC"));
		}
		FontMetrics fm = new JLabel().getFontMetrics(font);

		int stringWidth = fm.stringWidth(content);
		int leading = 4;// stringWidth/content.toString().length()/5;
		int w2 = stringWidth + (leading * (content.length() - 1));

		int h2 = fm.getHeight();
		// 若想文字居中,就要图片长/2= X + w2/2
		int realWidth = (w - w2) / 2;
		Graphics g = img.getGraphics();
		g.setColor(Color.white);// 设置字体颜色
		if (font != null)
			g.setFont(font);

		// 获取每个字符的长度
		char[] str = content.toCharArray();
		int temp = 0;
		int realWidth2 = realWidth;
		for (int i = 0; i < str.length; i++) {
			int cW = fm.charWidth(str[i]); // 每个字长度
			int cW2 = leading; // 间距
			if (i != 0) {
				realWidth2 = realWidth2 + temp;
			}

			g.drawString(String.valueOf(str[i]), realWidth2, y + h2);
			temp = (cW + cW2);
			// System.out.println("间距:" + cW2 + "，字("+str[i]+")的长度:"+cW);
		}
		g.dispose();
		return img;
	}

	/**
	 * 生层微信贺卡
	 * 
	 * @param bgPicUrl
	 *            背景图路径
	 * @param userHeadUrl
	 *            用户头像路径
	 * @param headWidth
	 *            头像缩放长度
	 * @param headHeight
	 *            头像缩放高度
	 * @param cornerRadius
	 *            图片圆角弧度
	 * @param headX
	 *            头像坐标
	 * @param headY
	 *            头像坐标
	 * @param content
	 *            文字
	 * @param contentX
	 *            文字坐标
	 * @param contentY
	 *            文字坐标
	 * @return
	 */
	public BufferedImage wechatNewYearCard(String bgPicUrl, String userHeadUrl, int headWidth, int headHeight,
			int cornerRadius, int headX, int headY, String content, int contentX, int contentY) {
		BufferedImage bg = loadImageLocal(bgPicUrl);
		BufferedImage pic = loadImageLocal(userHeadUrl);
		BufferedImage resized = createResizedCopy(pic, headWidth, headHeight, true);
		BufferedImage rounded = makeRoundedCorner(resized, cornerRadius);
		BufferedImage add = mergeLayers(bg, rounded, headX, headY);
		BufferedImage suc = modifyImage(add, content, contentX, contentY);
		return suc;
	}

	public byte[] wechatNewYearCardBytes(String bgPicUrl, String userHeadUrl, int headWidth, int headHeight,
			int cornerRadius, int headX, int headY, String content, int contentX, int contentY) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		BufferedImage bimage = wechatNewYearCard(bgPicUrl, userHeadUrl, headWidth, headHeight, cornerRadius, headX,
				headY, content, contentX, contentY);
		try {
			ImageIO.write(bimage, "JPEG", out);
			return out.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

}
