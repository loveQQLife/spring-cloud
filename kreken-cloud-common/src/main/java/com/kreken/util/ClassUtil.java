package com.kreken.util;

import com.alibaba.fastjson.JSONObject;
import com.kreken.exception.ExcelException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 反射工具类.
 */
public class ClassUtil {

	private static Logger logger = LoggerFactory.getLogger(ClassUtil.class);

	/**
	 * 获取类加载器
	 */
	public static ClassLoader overridenClassLoader;

	public static ClassLoader getContextClassLoader() {
		return overridenClassLoader != null ?
				overridenClassLoader : Thread.currentThread().getContextClassLoader();
	}

	/**
	 * 获取指定类的全部属性字段
	 *
	 * @param className    需要获取的类名
	 * @param extendsField 是否获取接口或父类中的公共属性
	 * @return 属性字段数组
	 */
	public final static String[] getField(String className, boolean extendsField) {
		Class classz = loadClass(className);
		Field[] fields = classz.getFields();
		Set<String> set = new HashSet<>();
		if (fields != null) {
			for (Field f : fields) {
				set.add(f.getName());
			}
		}
		if (extendsField) {
			Field[] fieldz = classz.getDeclaredFields();
			if (fieldz != null) {
				for (Field f : fieldz) {
					set.add(f.getName());
				}
			}
		}
		return set.toArray(new String[set.size()]);
	}

	/**
	 * 获取类中的公共属性
	 *
	 * @param className    需要获取的类名
	 * @param extendsField 是否获取接口或父类中的公共属性
	 * @return 属性字段数组
	 */
	public final static String[] getPublicField(String className, boolean extendsField) {
		Class classz = loadClass(className);
		Set<String> set = new HashSet<>();
		Field[] fields = classz.getDeclaredFields();
		if (fields != null) {
			for (Field f : fields) {
				String modifier = Modifier.toString(f.getModifiers());
				if (modifier.startsWith("public")) {
					set.add(f.getName());
				}
			}
		}
		if (extendsField) {
			Field[] fieldz = classz.getFields();
			if (fieldz != null) {
				for (Field f : fieldz) {
					set.add(f.getName());
				}
			}
		}
		return set.toArray(new String[set.size()]);
	}

	/**
	 * 获取类中定义的protected类型的属性字段
	 *
	 * @param className 需要获取的类名
	 * @return protected类型的属性字段数组
	 */
	public final static String[] getProtectedField(String className) {
		Class classz = loadClass(className);
		Set<String> set = new HashSet<>();
		Field[] fields = classz.getDeclaredFields();
		if (fields != null) {
			for (Field f : fields) {
				String modifier = Modifier.toString(f.getModifiers());
				if (modifier.startsWith("protected")) {
					set.add(f.getName());
				}
			}
		}
		return set.toArray(new String[set.size()]);
	}

	/**
	 * 获取类中定义的private类型的属性字段
	 *
	 * @param className 需要获取的类名
	 * @return private类型的属性字段数组
	 */
	public final static String[] getPrivateField(String className) {
		Class classz = loadClass(className);
		Set<String> set = new HashSet<>();
		Field[] fields = classz.getDeclaredFields();
		if (fields != null) {
			for (Field f : fields) {
				String modifier = Modifier.toString(f.getModifiers());
				if (modifier.startsWith("private")) {
					set.add(f.getName());
				}
			}
		}
		return set.toArray(new String[set.size()]);
	}

	/**
	 * 获取对象的全部public类型方法
	 *
	 * @param className     需要获取的类名
	 * @param extendsMethod 是否获取继承来的方法
	 * @return 方法名数组
	 */
	public final static String[] getPublicMethod(String className, boolean extendsMethod) {
		Class classz = loadClass(className);
		Method[] methods;
		if (extendsMethod) {
			methods = classz.getMethods();
		} else {
			methods = classz.getDeclaredMethods();
		}
		Set<String> set = new HashSet<>();
		if (methods != null) {
			for (Method f : methods) {
				String modifier = Modifier.toString(f.getModifiers());
				if (modifier.startsWith("public")) {
					set.add(f.getName());
				}
			}
		}
		return set.toArray(new String[set.size()]);
	}


	/**
	 * 获取对象的全部protected类型方法
	 *
	 * @param className     需要获取的类名
	 * @param extendsMethod 是否获取继承来的方法
	 * @return 方法名数组
	 */
	public final static String[] getProtectedMethod(String className, boolean extendsMethod) {
		Class classz = loadClass(className);
		Method[] methods;
		if (extendsMethod) {
			methods = classz.getMethods();
		} else {
			methods = classz.getDeclaredMethods();
		}
		Set<String> set = new HashSet<>();
		if (methods != null) {
			for (Method f : methods) {
				String modifier = Modifier.toString(f.getModifiers());
				if (modifier.startsWith("protected")) {
					set.add(f.getName());
				}
			}
		}
		return set.toArray(new String[set.size()]);
	}

	/**
	 * 获取对象的全部private类型方法
	 *
	 * @param className 需要获取的类名
	 * @return 方法名数组
	 */
	public final static String[] getPrivateMethod(String className) {
		Class classz = loadClass(className);
		Method[] methods = classz.getDeclaredMethods();
		Set<String> set = new HashSet<>();
		if (methods != null) {
			for (Method f : methods) {
				String modifier = Modifier.toString(f.getModifiers());
				if (modifier.startsWith("private")) {
					set.add(f.getName());
				}
			}
		}
		return set.toArray(new String[set.size()]);
	}

	/**
	 * 获取对象的全部方法
	 *
	 * @param className     需要获取的类名
	 * @param extendsMethod 是否获取继承来的方法
	 * @return 方法名数组
	 */
	public final static String[] getMethod(String className, boolean extendsMethod) {
		Class classz = loadClass(className);
		Method[] methods;
		if (extendsMethod) {
			methods = classz.getMethods();
		} else {
			methods = classz.getDeclaredMethods();
		}
		Set<String> set = new HashSet<>();
		if (methods != null) {
			for (Method f : methods) {
				set.add(f.getName());
			}
		}
		return set.toArray(new String[set.size()]);
	}


	/**
	 * 调用对象的setter方法
	 *
	 * @param obj   对象
	 * @param att   属性名
	 * @param value 属性值
	 * @param type  属性类型
	 */
	public final static void setter(Object obj, String att, Object value, Class<?> type)
			throws InvocationTargetException, IllegalAccessException {
		try {
			String name = att.substring(0, 1).toUpperCase() + att.substring(1);
			Method met = obj.getClass().getMethod("set" + name, type);
			met.invoke(obj, value);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}

	}


	/**
	 * 获取指定目录下所有的类名
	 *
	 * @param path         包名
	 * @param childPackage 是否获取子包
	 */
	public final static List<String> getClassName(String path, boolean childPackage) {
		List<String> fileNames = new ArrayList<>();
		if (path.endsWith(".jar")) {
			fileNames.addAll(getClassNameByJar(path));
		} else {
			fileNames = getClassNameByFile(path, childPackage);
		}
		return fileNames;
	}

	/**
	 * 从项目文件获取某包下所有类
	 *
	 * @param filePath     文件路径
	 * @param childPackage 是否遍历子包
	 * @return 类的完整名称
	 */
	public final static List<String> getClassNameByFile(String filePath, boolean childPackage) {
		List<String> myClassName = new ArrayList<>();
		List<File> files = FileUtil.listFile(filePath, childPackage);
		for (File file : files) {
			if (file.getName().endsWith(".class")) {
				String childFilePath = file.getPath();
				int index = filePath.replaceAll("\\\\", ".").length();
				childFilePath = childFilePath.replaceAll("\\\\", ".").substring(index, childFilePath.length());
				myClassName.add(childFilePath);
			}
		}
		return myClassName;
	}

	/**
	 * 从jar获取某包下所有类
	 *
	 * @param jarPath jar文件路径
	 * @return 类的完整名称
	 */
	public final static List<String> getClassNameByJar(String jarPath) {
		List<String> myClassName = new ArrayList<>();
		try (JarFile jarFile = new JarFile(jarPath)) {
			Enumeration<JarEntry> entrys = jarFile.entries();
			while (entrys.hasMoreElements()) {
				JarEntry jarEntry = entrys.nextElement();
				String entryName = jarEntry.getName();
				if (entryName.endsWith(".class")) {
					entryName = entryName.replace("/", ".").substring(0, entryName.lastIndexOf("."));
					myClassName.add(entryName);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return myClassName;
	}


	/**
	 * 加载指定的类
	 *
	 * @param className 需要加载的类
	 * @return 加载后的类
	 */
	public final static Class loadClass(String className) {
		Class theClass = null;
		try {
			theClass = Class.forName(className);
		} catch (ClassNotFoundException e1) {
			logger.error("load class error:" + e1.getMessage());
			e1.printStackTrace();
		}
		return theClass;
	}

	/**
	 * 获取jar包中的非*.class外的全部资源文件名字
	 *
	 * @param jarPath jar文件路径
	 * @return 返回资源名称数组
	 */
	public final static List<String> getResourceNameByJar(String jarPath) {
		List<String> resource = new ArrayList<>();
		try (JarFile jarFile = new JarFile(jarPath)) {
			Enumeration<JarEntry> entrys = jarFile.entries();
			while (entrys.hasMoreElements()) {
				JarEntry jarEntry = entrys.nextElement();
				String entryName = jarEntry.getName();
				if (!entryName.endsWith(".class") && !entryName.endsWith("/")) {
					resource.add(FileUtil.commandPath(jarPath) + "!" + entryName);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resource;
	}

	/**
	 * 获取jar包中的非*.class外的全部的以suffix结尾的资源文件
	 *
	 * @param jarPath jar包的路径
	 * @param suffix  后缀名称
	 * @return 返回资源名称数组
	 */
	public final static List<String> getResourceNameByJar(String jarPath, String suffix) {
		List<String> resource = new ArrayList<>();
		try (JarFile jarFile = new JarFile(jarPath)) {
			Enumeration<JarEntry> entrys = jarFile.entries();
			while (entrys.hasMoreElements()) {
				JarEntry jarEntry = entrys.nextElement();
				String entryName = jarEntry.getName();
				if (entryName.endsWith(suffix)) {
					resource.add(FileUtil.commandPath(jarPath) + "!" + entryName);
				}
			}
		} catch (IOException e) {
			logger.error(JSONObject.toJSONString(e));
		}
		return resource;
	}

	/**
	 * 获取一个类的父类
	 *
	 * @param className 需要获取的类
	 * @return 父类的名称
	 */
	public final static String getSuperClass(String className) {
		Class classz = loadClass(className);
		Class superclass = classz.getSuperclass();
		return superclass.getName();
	}

	/**
	 * 获取一个雷的继承链
	 *
	 * @param className 需要获取的类
	 * @return 继承类名的数组
	 */
	public final static String[] getSuperClassChian(String className) {
		Class classz = loadClass(className);
		List<String> list = new ArrayList<>();
		Class superclass = classz.getSuperclass();
		String superName = superclass.getName();
		if (!"java.lang.Object".equals(superName)) {
			list.add(superName);
			list.addAll(Arrays.asList(getSuperClassChian(superName)));
		} else {
			list.add(superName);
		}
		return list.toArray(new String[list.size()]);
	}

	/**
	 * 获取一类实现的全部接口
	 *
	 * @param className         需要获取的类
	 * @param extendsInterfaces 话说getInterfaces能全部获取到才对，然而测试的时候父类的接口并没有
	 *                          因此就多除了这参数
	 * @return 实现接口名称的数组
	 */
	public final static String[] getInterfaces(String className, boolean extendsInterfaces) {
		Class classz = loadClass(className);
		List<String> list = new ArrayList<>();
		Class[] interfaces = classz.getInterfaces();
		if (interfaces != null) {
			for (Class inter : interfaces) {
				list.add(inter.getName());
			}
		}
		if (extendsInterfaces) {
			String[] superClass = getSuperClassChian(className);
			for (String c : superClass) {
				list.addAll(Arrays.asList(getInterfaces(c, false)));
			}
		}
		return list.toArray(new String[list.size()]);
	}

	/**
	 * @MethodName : getFieldByName
	 * @Description : 根据字段名获取字段
	 * @param fieldName
	 *            字段名
	 * @param clazz
	 *            包含该字段的类
	 * @return 字段
	 */
	public static Field getFieldByName(String fieldName, Class<?> clazz){
		// 拿到本类的所有字段
		Field[] selfFields = clazz.getDeclaredFields();

		// 如果本类中存在该字段，则返回
		for (Field field : selfFields) {
			if (field.getName().equals(fieldName)) {
				return field;
			}
		}

		// 否则，查看父类中是否存在此字段，如果有则返回
		Class<?> superClazz = clazz.getSuperclass();
		if (superClazz != null && superClazz != Object.class) {
			return getFieldByName(fieldName, superClazz);
		}

		// 如果本类和父类都没有，则返回空
		return null;
	}

	/**
	 * @MethodName : setFieldValueByName
	 * @Description : 根据字段名给对象的字段赋值
	 * @param fieldName
	 *            字段名
	 * @param fieldValue
	 *            字段值
	 * @param o
	 *            对象
	 */
	public static void setFieldValueByName(String fieldName, Object fieldValue, Object o) throws Exception {

		Field field = getFieldByName(fieldName, o.getClass());
		if (field != null) {
			field.setAccessible(true);
			// 获取字段类型
			Class<?> fieldType = field.getType();

			// 根据字段类型给字段赋值
			if (String.class == fieldType) {
				field.set(o, String.valueOf(fieldValue));
			} else if ((Integer.TYPE == fieldType) || (Integer.class == fieldType)) {
				field.set(o, Integer.parseInt(fieldValue.toString()));
			} else if ((Long.TYPE == fieldType) || (Long.class == fieldType)) {
				field.set(o, Long.valueOf(fieldValue.toString()));
			} else if ((Float.TYPE == fieldType) || (Float.class == fieldType)) {
				field.set(o, Float.valueOf(fieldValue.toString()));
			} else if ((Short.TYPE == fieldType) || (Short.class == fieldType)) {
				field.set(o, Short.valueOf(fieldValue.toString()));
			} else if ((Double.TYPE == fieldType) || (Double.class == fieldType)) {
				field.set(o, Double.valueOf(fieldValue.toString()));
			} else if (Character.TYPE == fieldType) {
				if ((fieldValue != null) && (fieldValue.toString().length() > 0)) {
					field.set(o, Character.valueOf(fieldValue.toString().charAt(0)));
				}
			} else if (Date.class == fieldType) {
				field.set(o, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(fieldValue.toString()));
			} else {
				field.set(o, fieldValue);
			}
		} else {
			throw new ExcelException(o.getClass().getSimpleName() + "类不存在字段名 " + fieldName);
		}
	}

	/**
	 * @MethodName : getFieldValueByName
	 * @Description : 根据字段名获取字段值
	 * @param fieldName
	 *            字段名
	 * @param o
	 *            对象
	 * @return 字段值
	 */
	public static Object getFieldValueByName(String fieldName, Object o) throws Exception {

		Object value = null;
		Field field = getFieldByName(fieldName, o.getClass());

		if (field != null) {
			field.setAccessible(true);
			value = field.get(o);
		} else {
			throw new ExcelException(o.getClass().getSimpleName() + "类不存在字段名 " + fieldName);
		}

		return value;
	}

	/**
	 * @MethodName : getFieldValueByNameSequence
	 * @Description : 根据带路径或不带路径的属性名获取属性值
	 *              即接受简单属性名，如userName等，又接受带路径的属性名，如student.department.name等
	 *
	 * @param fieldNameSequence
	 *            带路径的属性名或简单属性名
	 * @param o
	 *            对象
	 * @return 属性值
	 * @throws Exception
	 */
	public static Object getFieldValueByNameSequence(String fieldNameSequence, Object o) throws Exception {

		Object value = null;

		// 将fieldNameSequence进行拆分
		String[] attributes = fieldNameSequence.split("\\.");
		if (attributes.length == 1) {
			value = getFieldValueByName(fieldNameSequence, o);
		} else {
			// 根据属性名获取属性对象
			Object fieldObj = getFieldValueByName(attributes[0], o);
			String subFieldNameSequence = fieldNameSequence.substring(fieldNameSequence.indexOf(".") + 1);
			value = getFieldValueByNameSequence(subFieldNameSequence, fieldObj);
		}
		return value;

	}
}
