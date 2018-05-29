package com.kreken.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class ZXingUtil {

	/**
	 * 将内容生成条码
	 * 
	 * @param content
	 *            条码内容
	 * @param barcodeFormat
	 *            QR_CODE
	 * @param width
	 * @param height
	 * @param imageType
	 * @return
	 */
	public static byte[] generate(String content, BarcodeFormat barcodeFormat,
			int width, int height, String imageType) {

		QRCodeWriter writer = new QRCodeWriter();
		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		int white = 255 << 16 | 255 << 8 | 255;
		int black = 0;
		try {

			if (!StringUtil.isNullOrEmpty(content)) {
				content = new String(content.getBytes("UTF-8"), "ISO-8859-1");
			}

			BitMatrix bitMatrix = writer.encode(content, barcodeFormat, width,
					height);
			for (int i = 0; i < width; i++) {
				for (int j = 0; j < height; j++) {
					image.setRGB(i, j, bitMatrix.get(i, j) ? black : white);
				}
			}

			try {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				ImageIO.write(image, imageType, out);
				return out.toByteArray();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (WriterException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return null;
	}
}
