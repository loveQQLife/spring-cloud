package com.kreken.util;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmojiUtil {
	private EmojiUtil() {
	}

	enum Type {
		UNICODE, SOFTBANK
	};

	private Map<List<Integer>, String> convertMap;

	public static class Builder {

		private Type from;
		private Type to;

		public Builder from(Type type) {
			this.from = type;
			return this;
		}

		public Builder to(Type type) {
			this.to = type;
			return this;
		}

		public EmojiUtil build() {
			EmojiUtil converter = new EmojiUtil();
			readMap(converter);
			return converter;
		}

		private static final String TRIM_PATTERN = "[^0-9A-F]*";

		public void readMap(EmojiUtil converter) {
			Map<List<Integer>, String> result = new HashMap<List<Integer>, String>();
			converter.convertMap = result;

			XMLEventReader reader = null;
			try {

				XMLInputFactory factory = XMLInputFactory.newInstance();

				InputStream stream = EmojiUtil.class.getClassLoader()
						.getResourceAsStream("emoji4unicode.xml");
				reader = factory.createXMLEventReader(stream);

				while (reader.hasNext()) {
					XMLEvent event = reader.nextEvent();

					if (event.isStartElement()) {
						StartElement element = (StartElement) event;
						if (element.getName().getLocalPart().equals("e")) {

							Attribute fromAttr = element
									.getAttributeByName(new QName(from
											.toString().toLowerCase()));
							Attribute toAttr = element
									.getAttributeByName(new QName(to.toString()
											.toLowerCase()));
							if (fromAttr == null) {
								continue;
							}
							List<Integer> fromCodePoints = new ArrayList<Integer>();
							String from = fromAttr.getValue();
							if (from.length() > 6) {
								String[] froms = from.split("\\+");
								for (String part : froms) {
									fromCodePoints.add(Integer.parseInt(
											part.replaceAll(TRIM_PATTERN, ""),
											16));
								}
							} else {
								fromCodePoints.add(Integer.parseInt(
										from.replaceAll(TRIM_PATTERN, ""), 16));
							}
							if (toAttr == null) {
								result.put(fromCodePoints, null);
							} else {
								String to = toAttr.getValue();
								StringBuilder toBuilder = new StringBuilder();
								if (to.length() > 6) {
									String[] tos = to.split("\\+");
									for (String part : tos) {
										toBuilder.append(Character
												.toChars(Integer.parseInt(part
														.replaceAll(
																TRIM_PATTERN,
																""), 16)));
									}
								} else {
									toBuilder.append(Character.toChars(Integer
											.parseInt(to.replaceAll(
													TRIM_PATTERN, ""), 16)));
								}
								result.put(fromCodePoints, toBuilder.toString());
							}

						}
					}
				}

				reader.close();
			} catch (Exception e) {
				e.printStackTrace();

			} finally {
				if (reader != null) {
					try {
						reader.close();
					} catch (XMLStreamException e) {

					}
				}

			}

		}

	}

	public String convert(int width, int height, String startChar,
			String endChar, String input) {
		StringBuilder result = new StringBuilder();
		int[] codePoints = toCodePointArray(input);
		for (int i = 0; i < codePoints.length; i++) {
			List<Integer> key2 = null;
			if (i + 1 < codePoints.length) {
				key2 = new ArrayList<Integer>();
				key2.add(codePoints[i]);
				key2.add(codePoints[i + 1]);

				if (convertMap.containsKey(key2) || 65039 == codePoints[i + 1]) { // 处理
																					// iphone5
																					// xxxx-fe0f.png
					String aa = Integer.toHexString(codePoints[i]) + "-"
							+ Integer.toHexString(codePoints[i + 1]);
					String value = convertMap.get(key2);
					if (value != null || 65039 == codePoints[i + 1]) {
						result.append(startChar);
						result.append(aa);
						result.append(endChar);
					}
					i++;
					continue;
				}
			}

			List<Integer> key1 = new ArrayList<Integer>();
			key1.add(codePoints[i]);
			if (convertMap.containsKey(key1)) {
				String aa = Integer.toHexString(codePoints[i]);
				String value = convertMap.get(key1);
				if (value != null) {
					result.append(startChar);
					result.append(aa);
					result.append(endChar);
					// System.out.println("key:"+key1);
					// System.out.println("Map:"+value);
				}
				continue;
			}
			if (128529 == codePoints[i]) { // 处理空格
				result.append(" ");
				continue;
			}
			result.append(Character.toChars(codePoints[i]));

		}
		return result.toString();
	}

	public String convert(int width, int height, String imgpath, String input) {
		StringBuilder result = new StringBuilder();
		int[] codePoints = toCodePointArray(input);
		for (int i = 0; i < codePoints.length; i++) {
			List<Integer> key2 = null;
			if (i + 1 < codePoints.length) {
				key2 = new ArrayList<Integer>();
				key2.add(codePoints[i]);
				key2.add(codePoints[i + 1]);

				if (convertMap.containsKey(key2) || 65039 == codePoints[i + 1]) { // 处理
																					// iphone5
																					// xxxx-fe0f.png
					String aa = Integer.toHexString(codePoints[i]) + "-"
							+ Integer.toHexString(codePoints[i + 1]);
					String value = convertMap.get(key2);
					if (value != null || 65039 == codePoints[i + 1]) {
						result.append("<img width=\""
								+ width
								+ "px\" height=\""
								+ height
								+ "px\" style=\"vertical-align: bottom;\" src=\""
								+ imgpath + "");
						result.append(aa);
						result.append(".png\"/>");
					}
					i++;
					continue;
				}
			}

			List<Integer> key1 = new ArrayList<Integer>();
			key1.add(codePoints[i]);
			if (convertMap.containsKey(key1)) {
				String aa = Integer.toHexString(codePoints[i]);
				String value = convertMap.get(key1);
				if (value != null) {
					result.append("<img width=\"" + width + "px\" height=\""
							+ height
							+ "px\" style=\"vertical-align: bottom;\" src=\""
							+ imgpath + "");
					result.append(aa);
					result.append(".png\"/>");
					// System.out.println("key:"+key1);
					// System.out.println("Map:"+value);
				}
				continue;
			}
			if (128529 == codePoints[i]) { // 处理空格
				result.append(" ");
				continue;
			}
			result.append(Character.toChars(codePoints[i]));

		}
		return result.toString();
	}

	int[] toCodePointArray(String str) {
		char[] ach = str.toCharArray();
		int len = ach.length;
		int[] acp = new int[Character.codePointCount(ach, 0, len)];
		int j = 0;

		for (int i = 0, cp; i < len; i += Character.charCount(cp)) {
			cp = Character.codePointAt(ach, i);
			acp[j++] = cp;
		}
		return acp;
	}
}
