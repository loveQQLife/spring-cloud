package com.kreken.util;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Writer;

public class XmlUtil {

	private static Logger logger = LoggerFactory.getLogger(XmlUtil.class);

	private static String PREFIX_CDATA    = "<![CDATA[";
	private static String SUFFIX_CDATA    = "]]>";


	/**
	 * 将XML转为实体类
	 * 
	 * @param xml
	 *            XML原文
	 * @param clazz
	 *            需要转换的类(包含子类)
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T parseObject(String xml, Class<?>... clazz) {
		try {
			XStream xStream = new XStream(new XppDriver(new XStreamNameCoder()));
			xStream.autodetectAnnotations(true);
			xStream.processAnnotations(clazz);
			return (T) xStream.fromXML(xml);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("XML转为实体类发生错误！", ex);
		}
		return null;
	}

	/**
	 * 将实体类转为XML
	 * 
	 * @param t
	 *            实体类对象
	 * @param clazz
	 *            需要转换的类(包含子类)
	 * @return
	 */
	public static <T> String toXML(T t, Class<?>... clazz) {
		try {
			XStream xStream = new XStream(new XppDriver(new XStreamNameCoder()));
			xStream.autodetectAnnotations(true);
			xStream.processAnnotations(clazz);
			return xStream.toXML(t);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("实体类转为XML发生错误！", ex);
		}
		return null;
	}

	/**
	 * 将实体类转为XML(对转义标签不会序列化)
	 * <![CDATA[{这是转移标签}]]>
	 *
	 * @param t
	 *            实体类对象
	 * @param clazz
	 *            需要转换的类(包含子类)
	 * @return
	 */
	public static <T> String toXMLWithOutEscape(T t, Class<?>... clazz) {
		try {
			XStream xStream =  new XStream(
					new XppDriver() {
						public HierarchicalStreamWriter createWriter(Writer out) {
							return new PrettyPrintWriter(out) {
								protected void writeText(QuickWriter writer, String text) {
									if(text.startsWith(PREFIX_CDATA)
											&& text.endsWith(SUFFIX_CDATA)) {
										writer.write(text);
									}else{
										super.writeText(writer, text);
									}
								}
							};
						};
					}
			);
			xStream.autodetectAnnotations(true);
			xStream.processAnnotations(clazz);
			return xStream.toXML(t);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("实体类转为XML发生错误！", ex);
		}
		return null;
	}

}
