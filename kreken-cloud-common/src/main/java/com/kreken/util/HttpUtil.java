package com.kreken.util;

import com.alibaba.fastjson.JSONObject;
import com.mysql.jdbc.StringUtils;
import net.sf.jmimemagic.Magic;
import net.sf.jmimemagic.MagicMatch;
import org.apache.http.conn.ssl.SSLContexts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyStore;
import java.util.Iterator;
import java.util.Map;

/**
 * 
 * 公众平台通用接口工具类
 * 
 */

@SuppressWarnings("deprecation")
public class HttpUtil {

	private static Logger log = LoggerFactory.getLogger(HttpUtil.class);

	/**
	 * 获取客户端请求的 IP地址
	 * 
	 * @param request
	 * @return
	 */
	public static String getClientIP(HttpServletRequest request) {

		String ip = request.getHeader("x-forwarded-for");

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}

		return ip;
	}

	/**
	 * 判断是否为Ajax请求
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @return 是true, 否false
	 */
	public static boolean isAjaxRequest(HttpServletRequest request) {
		String requestType = request.getHeader("X-Requested-With");
		if (requestType != null && requestType.equals("XMLHttpRequest")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 发起http请求并获取结果
	 * 
	 * @param requestUrl
	 *            请求地址
	 * 
	 * @param requestMethod
	 *            请求方式（GET、POST）
	 * 
	 * @param paramData
	 *            提交的数据
	 * 
	 * @return 返回响应的String
	 */
	public static String httpRequestString(String requestUrl,
			String requestMethod, String paramData) {
		if (requestUrl.startsWith("https://")) {
			return httpsRequestString(requestUrl, requestMethod, paramData);
		}

		String response = null;
		StringBuffer buffer = new StringBuffer();

		try {
			URL url = new URL(requestUrl);
			HttpURLConnection httpUrlConn = (HttpURLConnection) url
					.openConnection();

			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);

			// 设置请求方式（GET/POST）
			httpUrlConn.setRequestMethod(requestMethod);
			if ("GET".equalsIgnoreCase(requestMethod)) {
				httpUrlConn.connect();
			}

			// 当有数据需要提交时
			if (null != paramData) {
				OutputStream outputStream = httpUrlConn.getOutputStream();
				// 注意编码格式，防止中文乱码
				outputStream.write(paramData.getBytes("utf8"));
				outputStream.close();
			}

			// 将返回的输入流转换成字符串
			InputStream inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(
					inputStream, "utf8");
			BufferedReader bufferedReader = new BufferedReader(
					inputStreamReader);

			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}

			bufferedReader.close();
			inputStreamReader.close();

			// 释放资源
			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();
			response = buffer.toString();
		} catch (ConnectException ce) {
			log.error("Weixin server connection timed out.");
		} catch (Exception e) {
			log.error("https request error:{}", e);
		}

		return response;
	}

	/**
	 * 发起https请求并获取结果(不带SSL请求证书)
	 * 
	 * @param requestUrl
	 *            请求地址
	 * 
	 * @param requestMethod
	 *            请求方式（GET、POST）
	 * 
	 * @param paramData
	 *            提交的数据
	 * 
	 * @return 返回响应的String
	 */
	private static String httpsRequestString(String requestUrl,
			String requestMethod, String paramData) {
		String response = null;
		StringBuffer buffer = new StringBuffer();

		try {
			// 创建SSLContext对象，并使用我们指定的信任管理器初始化
			TrustManager[] tm = { new MyX509TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());

			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();
			URL url = new URL(requestUrl);
			HttpsURLConnection httpUrlConn = (HttpsURLConnection) url
					.openConnection();

			httpUrlConn.setSSLSocketFactory(ssf);
			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);

			// 设置请求方式（GET/POST）
			httpUrlConn.setRequestMethod(requestMethod);
			if ("GET".equalsIgnoreCase(requestMethod)) {
				httpUrlConn.connect();
			}

			// 当有数据需要提交时
			if (null != paramData) {
				OutputStream outputStream = httpUrlConn.getOutputStream();
				// 注意编码格式，防止中文乱码
				outputStream.write(paramData.getBytes("utf8"));
				outputStream.close();
			}

			// 将返回的输入流转换成字符串
			InputStream inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(
					inputStream, "utf8");
			BufferedReader bufferedReader = new BufferedReader(
					inputStreamReader);

			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}

			bufferedReader.close();
			inputStreamReader.close();

			// 释放资源
			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();
			response = buffer.toString();
		} catch (ConnectException ce) {
			log.error("Weixin server connection timed out.");
		} catch (Exception e) {
			log.error("https request error:{}", e);
		}

		return response;
	}

	/**
	 * 发起http请求并获取结果
	 * 
	 * @param requestUrl
	 *            请求地址
	 * 
	 * @param requestMethod
	 *            请求方式（GET、POST）
	 * 
	 * @param paramData
	 *            提交的数据
	 * 
	 * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
	 */
	public static JSONObject httpRequest(String requestUrl,
			String requestMethod, String paramData) {
		if (requestUrl.startsWith("https://")) {
			return httpsRequest(requestUrl, requestMethod, paramData);
		}

		JSONObject jsonObject = null;
		StringBuffer buffer = new StringBuffer();

		try {
			URL url = new URL(requestUrl);
			HttpURLConnection httpUrlConn = (HttpURLConnection) url
					.openConnection();

			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);

			// 设置请求方式（GET/POST）
			httpUrlConn.setRequestMethod(requestMethod);
			if ("GET".equalsIgnoreCase(requestMethod)) {
				httpUrlConn.connect();
			}

			// 当有数据需要提交时
			if (null != paramData) {
				OutputStream outputStream = httpUrlConn.getOutputStream();
				// 注意编码格式，防止中文乱码
				outputStream.write(paramData.getBytes("utf8"));
				outputStream.close();
			}

			// 将返回的输入流转换成字符串
			InputStream inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(
					inputStream, "utf8");
			BufferedReader bufferedReader = new BufferedReader(
					inputStreamReader);

			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}

			bufferedReader.close();
			inputStreamReader.close();

			// 释放资源
			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();
			jsonObject = JSONObject.parseObject(buffer.toString());
		} catch (ConnectException ce) {
			log.error("Weixin server connection timed out.");
		} catch (Exception e) {
			log.error("https request error:{}", e);
		}

		return jsonObject;
	}

	/**
	 * 发起https请求并获取结果(不带SSL请求证书)
	 * 
	 * @param requestUrl
	 *            请求地址
	 * 
	 * @param requestMethod
	 *            请求方式（GET、POST）
	 * 
	 * @param paramData
	 *            提交的数据
	 * 
	 * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
	 */
	private static JSONObject httpsRequest(String requestUrl,
			String requestMethod, String paramData) {
		JSONObject jsonObject = null;
		StringBuffer buffer = new StringBuffer();

		try {
			// 创建SSLContext对象，并使用我们指定的信任管理器初始化
			TrustManager[] tm = { new MyX509TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());

			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();
			URL url = new URL(requestUrl);
			HttpsURLConnection httpUrlConn = (HttpsURLConnection) url
					.openConnection();

			httpUrlConn.setSSLSocketFactory(ssf);
			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);

			// 设置请求方式（GET/POST）
			httpUrlConn.setRequestMethod(requestMethod);
			if ("GET".equalsIgnoreCase(requestMethod)) {
				httpUrlConn.connect();
			}

			// 当有数据需要提交时
			if (null != paramData) {
				OutputStream outputStream = httpUrlConn.getOutputStream();
				// 注意编码格式，防止中文乱码
				outputStream.write(paramData.getBytes("utf8"));
				outputStream.close();
			}

			// 将返回的输入流转换成字符串
			InputStream inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(
					inputStream, "utf8");
			BufferedReader bufferedReader = new BufferedReader(
					inputStreamReader);

			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}

			bufferedReader.close();
			inputStreamReader.close();

			// 释放资源
			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();
			jsonObject = JSONObject.parseObject(buffer.toString());
		} catch (ConnectException ce) {
			log.error("Weixin server connection timed out.");
		} catch (Exception e) {
			log.error("https request error:{}", e);
		}

		return jsonObject;
	}

	/**
	 * 发起https请求并获取结果(不带SSL请求证书)
	 * 
	 * @param <T>
	 * 
	 * @param requestUrl
	 *            请求地址
	 * 
	 * @param requestMethod
	 *            请求方式（GET、POST）
	 * 
	 * @param paramData
	 *            提交的数据
	 * 
	 * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
	 */
	public static <T> T httpRequest(String requestUrl, String requestMethod,
			String paramData, Class<T> clazz) {

		if (requestUrl.startsWith("https://")) {
			return httpsRequest(requestUrl, requestMethod, paramData, clazz);
		}

		T jsonObject = null;
		StringBuffer buffer = new StringBuffer();

		try {
			URL url = new URL(requestUrl);
			HttpURLConnection httpUrlConn = (HttpURLConnection) url
					.openConnection();

			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);

			// 设置请求方式（GET/POST）
			httpUrlConn.setRequestMethod(requestMethod);
			if ("GET".equalsIgnoreCase(requestMethod)) {
				httpUrlConn.connect();
			}

			// 当有数据需要提交时
			if (null != paramData) {
				OutputStream outputStream = httpUrlConn.getOutputStream();
				// 注意编码格式，防止中文乱码
				outputStream.write(paramData.getBytes("utf8"));
				outputStream.close();
			}

			// 将返回的输入流转换成字符串
			InputStream inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(
					inputStream, "utf8");
			BufferedReader bufferedReader = new BufferedReader(
					inputStreamReader);

			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}

			bufferedReader.close();
			inputStreamReader.close();

			// 释放资源
			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();

			if (clazz != null) {
				jsonObject = JSONObject.parseObject(buffer.toString(), clazz);
			}
		} catch (ConnectException ce) {
			log.error("Weixin server connection timed out.");
		} catch (Exception e) {
			log.error("https request error:{}", e);
		}

		return jsonObject;
	}

	/**
	 * 发起https请求并获取结果(不带SSL请求证书)
	 * 
	 * @param <T>
	 * 
	 * @param requestUrl
	 *            请求地址
	 * 
	 * @param requestMethod
	 *            请求方式（GET、POST）
	 * 
	 * @param paramData
	 *            提交的数据
	 * 
	 * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
	 */
	private static <T> T httpsRequest(String requestUrl, String requestMethod,
			String paramData, Class<T> clazz) {
		T jsonObject = null;
		StringBuffer buffer = new StringBuffer();

		try {
			// 创建SSLContext对象，并使用我们指定的信任管理器初始化
			TrustManager[] tm = { new MyX509TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());

			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();
			URL url = new URL(requestUrl);
			HttpsURLConnection httpUrlConn = (HttpsURLConnection) url
					.openConnection();

			httpUrlConn.setSSLSocketFactory(ssf);
			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);

			// 设置请求方式（GET/POST）
			httpUrlConn.setRequestMethod(requestMethod);
			if ("GET".equalsIgnoreCase(requestMethod)) {
				httpUrlConn.connect();
			}

			// 当有数据需要提交时
			if (null != paramData) {
				OutputStream outputStream = httpUrlConn.getOutputStream();
				// 注意编码格式，防止中文乱码
				outputStream.write(paramData.getBytes("utf8"));
				outputStream.close();
			}

			// 将返回的输入流转换成字符串
			InputStream inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(
					inputStream, "utf8");
			BufferedReader bufferedReader = new BufferedReader(
					inputStreamReader);

			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}

			bufferedReader.close();
			inputStreamReader.close();

			// 释放资源
			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();
			if (clazz != null) {
				jsonObject = JSONObject.parseObject(buffer.toString(), clazz);
			}
		} catch (ConnectException ce) {
			log.error("Weixin server connection timed out.");
		} catch (Exception e) {
			log.error("https request error:{}", e);
		}

		return jsonObject;
	}

	/**
	 * 发起https请求并获取结果(带SSL请求证书)
	 * 
	 * @param requestUrl
	 *            请求地址
	 * 
	 * @param requestMethod
	 *            请求方式（GET、POST）
	 * 
	 * @param paramData
	 *            提交的数据
	 * 
	 * @return 返回的响应String
	 */
	public static String httpRequestStringWithSSLCert(String requestUrl,
			String requestMethod, String paramData, String sslCertFile,
			String sslCertPassword) {
		String response = null;
		StringBuffer buffer = new StringBuffer();

		try {
			KeyStore ks = KeyStore.getInstance("PKCS12");
			FileInputStream instream = new FileInputStream(
					new File(sslCertFile));
			try {
				ks.load(instream, sslCertPassword.toCharArray());
			} finally {
				instream.close();
			}
			// 创建SSLContext对象，并使用我们指定的信任管理器初始化
			SSLContext sslContext = SSLContexts.custom()
					.loadKeyMaterial(ks, sslCertPassword.toCharArray()).build();

			// 从上述SSLContext对象中得到SSLSocketFactory对象
			URL url = new URL(requestUrl);
			HttpsURLConnection httpUrlConn = (HttpsURLConnection) url
					.openConnection();

			httpUrlConn.setSSLSocketFactory(sslContext.getSocketFactory());
			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);

			// 设置请求方式（GET/POST）
			httpUrlConn.setRequestMethod(requestMethod);
			if ("GET".equalsIgnoreCase(requestMethod)) {
				httpUrlConn.connect();
			}

			// 当有数据需要提交时
			if (null != paramData) {
				OutputStream outputStream = httpUrlConn.getOutputStream();
				// 注意编码格式，防止中文乱码
				outputStream.write(paramData.getBytes("utf8"));
				outputStream.close();
			}

			// 将返回的输入流转换成字符串
			InputStream inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(
					inputStream, "utf8");
			BufferedReader bufferedReader = new BufferedReader(
					inputStreamReader);

			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}

			bufferedReader.close();
			inputStreamReader.close();

			// 释放资源
			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();
			response = buffer.toString();
		} catch (ConnectException ce) {
			log.error("Weixin server connection timed out.");
		} catch (Exception e) {
			log.error("https request error:{}", e);
		}

		return response;
	}

	/**
	 * 发起https请求并获取结果(带SSL请求证书)
	 * 
	 * @param requestUrl
	 *            请求地址
	 * 
	 * @param requestMethod
	 *            请求方式（GET、POST）
	 * 
	 * @param paramData
	 *            提交的数据
	 * 
	 * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
	 */
	public static JSONObject httpRequestWithSSLCert(String requestUrl,
			String requestMethod, String paramData, String sslCertPath,
			String sslCertPassword) {
		JSONObject jsonObject = null;
		StringBuffer buffer = new StringBuffer();

		try {
			// 创建SSLContext对象，并使用我们指定的信任管理器初始化
			TrustManager[] tm = { new MyX509TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");

			if (!StringUtils.isNullOrEmpty(sslCertPath)) {
				String sslKeySotrePath = sslCertPath.substring(0,
						sslCertPath.lastIndexOf("\\.") + 1)
						+ ".keystore";
				KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
				FileInputStream is = new FileInputStream(sslKeySotrePath);
				ks.load(is, sslCertPassword.toCharArray());
				is.close();
				KeyManagerFactory keyManagerFactory = KeyManagerFactory
						.getInstance(KeyManagerFactory.getDefaultAlgorithm());// Sunx509
				keyManagerFactory.init(ks, sslCertPassword.toCharArray());
				sslContext.init(keyManagerFactory.getKeyManagers(), tm,
						new java.security.SecureRandom());
			} else {
				sslContext.init(null, tm, new java.security.SecureRandom());
			}

			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();
			URL url = new URL(requestUrl);
			HttpsURLConnection httpUrlConn = (HttpsURLConnection) url
					.openConnection();

			httpUrlConn.setSSLSocketFactory(ssf);
			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);

			// 设置请求方式（GET/POST）
			httpUrlConn.setRequestMethod(requestMethod);
			if ("GET".equalsIgnoreCase(requestMethod)) {
				httpUrlConn.connect();
			}

			// 当有数据需要提交时
			if (null != paramData) {
				OutputStream outputStream = httpUrlConn.getOutputStream();
				// 注意编码格式，防止中文乱码
				outputStream.write(paramData.getBytes("utf8"));
				outputStream.close();
			}

			// 将返回的输入流转换成字符串
			InputStream inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(
					inputStream, "utf8");
			BufferedReader bufferedReader = new BufferedReader(
					inputStreamReader);

			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}

			bufferedReader.close();
			inputStreamReader.close();

			// 释放资源
			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();
			jsonObject = JSONObject.parseObject(buffer.toString());
		} catch (ConnectException ce) {
			log.error("Weixin server connection timed out.");
		} catch (Exception e) {
			log.error("https request error:{}", e);
		}

		return jsonObject;
	}

	/**
	 * 发起https请求并获取结果(带SSL请求证书)
	 * 
	 * @param <T>
	 * 
	 * @param requestUrl
	 *            请求地址
	 * 
	 * @param requestMethod
	 *            请求方式（GET、POST）
	 * 
	 * @param paramData
	 *            提交的数据
	 * 
	 * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
	 */
	public static <T> T httpRequestWithSSLCert(String requestUrl,
			String requestMethod, String paramData, Class<T> clazz,
			String sslCertPath, String sslCertPassword) {
		T jsonObject = null;
		StringBuffer buffer = new StringBuffer();

		try {
			// 创建SSLContext对象，并使用我们指定的信任管理器初始化
			TrustManager[] tm = { new MyX509TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");

			if (!StringUtils.isNullOrEmpty(sslCertPath)) {
				String sslKeySotrePath = sslCertPath.substring(0,
						sslCertPath.lastIndexOf("\\.") + 1)
						+ ".keystore";
				KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
				FileInputStream is = new FileInputStream(sslKeySotrePath);
				ks.load(is, sslCertPassword.toCharArray());
				is.close();
				KeyManagerFactory keyManagerFactory = KeyManagerFactory
						.getInstance(KeyManagerFactory.getDefaultAlgorithm());// Sunx509
				keyManagerFactory.init(ks, sslCertPassword.toCharArray());
				sslContext.init(keyManagerFactory.getKeyManagers(), tm,
						new java.security.SecureRandom());
			} else {
				sslContext.init(null, tm, new java.security.SecureRandom());
			}

			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();
			URL url = new URL(requestUrl);
			HttpsURLConnection httpUrlConn = (HttpsURLConnection) url
					.openConnection();

			httpUrlConn.setSSLSocketFactory(ssf);
			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);

			// 设置请求方式（GET/POST）
			httpUrlConn.setRequestMethod(requestMethod);
			if ("GET".equalsIgnoreCase(requestMethod)) {
				httpUrlConn.connect();
			}

			// 当有数据需要提交时
			if (null != paramData) {
				OutputStream outputStream = httpUrlConn.getOutputStream();
				// 注意编码格式，防止中文乱码
				outputStream.write(paramData.getBytes("utf8"));
				outputStream.close();
			}

			// 将返回的输入流转换成字符串
			InputStream inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(
					inputStream, "utf8");
			BufferedReader bufferedReader = new BufferedReader(
					inputStreamReader);

			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}

			bufferedReader.close();
			inputStreamReader.close();

			// 释放资源
			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();

			if (clazz != null) {
				jsonObject = JSONObject.parseObject(buffer.toString(), clazz);
			}
		} catch (ConnectException ce) {
			log.error("Weixin server connection timed out.");
		} catch (Exception e) {
			log.error("https request error:{}", e);
		}

		return jsonObject;
	}

	/**
	 * 上传图片
	 * 
	 * @param requestUrl
	 * @param paramData
	 * @param fileMap
	 * @return
	 */
	public static JSONObject httpRequestWithFile(String requestUrl,
			String paramData, Map<String, String> fileMap) {

		if (requestUrl != null && requestUrl.startsWith("https://")) {
			return httpsRequestWithFile(requestUrl, paramData, fileMap);
		}

		JSONObject res = null;
		HttpURLConnection conn = null;
		String BOUNDARY = "---------------------------"
				+ System.currentTimeMillis(); // boundary就是request头和上传文件内容的分隔符
		try {
			URL url = new URL(requestUrl);
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			conn.setReadTimeout(30000);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
			conn.setRequestProperty("Content-Type",
					"multipart/form-data; boundary=" + BOUNDARY);

			OutputStream out = new DataOutputStream(conn.getOutputStream());
			// text
			if (!StringUtils.isNullOrEmpty(paramData)) {
				StringBuffer strBuf = new StringBuffer();
				for (String param : paramData.split("\\&")) {
					String[] formDatas = param.split("\\=");
					String inputName = formDatas[0];
					String inputValue = formDatas[1];
					if (inputValue == null) {
						continue;
					}
					strBuf.append("\r\n").append("--").append(BOUNDARY)
							.append("\r\n");
					strBuf.append("Content-Disposition: form-data; name=\""
							+ inputName + "\"\r\n\r\n");
					strBuf.append(inputValue);
				}
				out.write(strBuf.toString().getBytes("utf8"));
			}

			// file
			if (fileMap != null) {
				Iterator<Map.Entry<String, String>> iter = fileMap.entrySet()
						.iterator();
				while (iter.hasNext()) {
					Map.Entry<String, String> entry = iter.next();
					String inputName = (String) entry.getKey();
					String inputValue = (String) entry.getValue();
					if (inputValue == null) {
						continue;
					}
					File file = new File(inputValue);
					String filename = file.getName();
					MagicMatch match = Magic.getMagicMatch(file, false, true);
					String contentType = match.getMimeType();

					StringBuffer strBuf = new StringBuffer();
					strBuf.append("\r\n").append("--").append(BOUNDARY)
							.append("\r\n");
					strBuf.append("Content-Disposition: form-data; name=\""
							+ inputName + "\"; filename=\"" + filename
							+ "\"\r\n");
					strBuf.append("Content-Type:" + contentType + "\r\n\r\n");

					out.write(strBuf.toString().getBytes("utf8"));

					DataInputStream in = new DataInputStream(
							new FileInputStream(file));
					int bytes = 0;
					byte[] bufferOut = new byte[1024];
					while ((bytes = in.read(bufferOut)) != -1) {
						out.write(bufferOut, 0, bytes);
					}
					in.close();
				}
			}

			byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("UTF-8");
			out.write(endData);
			out.flush();
			out.close();

			// 读取返回数据
			StringBuffer strBuf = new StringBuffer();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					conn.getInputStream(), "utf8"));
			String line = null;
			while ((line = reader.readLine()) != null) {
				strBuf.append(line).append("\n");
			}
			res = JSONObject.parseObject(strBuf.toString());
			reader.close();
			reader = null;
		} catch (Exception e) {
			System.out.println("发送POST请求出错。" + requestUrl);
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.disconnect();
				conn = null;
			}
		}
		return res;
	}

	/**
	 * 上传图片
	 * 
	 * @param requestUrl
	 * @param paramData
	 * @param fileMap
	 * @return
	 */
	public static <T> T httpRequestWithFile(String requestUrl,
			String paramData, Map<String, String> fileMap, Class<T> clazz) {

		if (requestUrl != null && requestUrl.startsWith("https://")) {
			return httpsRequestWithFile(requestUrl, paramData, fileMap, clazz);
		}

		T res = null;
		HttpURLConnection conn = null;
		String BOUNDARY = "---------------------------"
				+ System.currentTimeMillis(); // boundary就是request头和上传文件内容的分隔符
		try {
			URL url = new URL(requestUrl);
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			conn.setReadTimeout(30000);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
			conn.setRequestProperty("Content-Type",
					"multipart/form-data; boundary=" + BOUNDARY);

			OutputStream out = new DataOutputStream(conn.getOutputStream());
			// text
			if (!StringUtils.isNullOrEmpty(paramData)) {
				StringBuffer strBuf = new StringBuffer();
				for (String param : paramData.split("\\&")) {
					String[] formDatas = param.split("\\=");
					String inputName = formDatas[0];
					String inputValue = formDatas[1];
					if (inputValue == null) {
						continue;
					}
					strBuf.append("\r\n").append("--").append(BOUNDARY)
							.append("\r\n");
					strBuf.append("Content-Disposition: form-data; name=\""
							+ inputName + "\"\r\n\r\n");
					strBuf.append(inputValue);
				}
				out.write(strBuf.toString().getBytes("utf8"));
			}

			// file
			if (fileMap != null) {
				Iterator<Map.Entry<String, String>> iter = fileMap.entrySet()
						.iterator();
				while (iter.hasNext()) {
					Map.Entry<String, String> entry = iter.next();
					String inputName = (String) entry.getKey();
					String inputValue = (String) entry.getValue();
					if (inputValue == null) {
						continue;
					}
					File file = new File(inputValue);
					String filename = file.getName();
					MagicMatch match = Magic.getMagicMatch(file, false, true);
					String contentType = match.getMimeType();

					StringBuffer strBuf = new StringBuffer();
					strBuf.append("\r\n").append("--").append(BOUNDARY)
							.append("\r\n");
					strBuf.append("Content-Disposition: form-data; name=\""
							+ inputName + "\"; filename=\"" + filename
							+ "\"\r\n");
					strBuf.append("Content-Type:" + contentType + "\r\n\r\n");

					out.write(strBuf.toString().getBytes("utf8"));

					DataInputStream in = new DataInputStream(
							new FileInputStream(file));
					int bytes = 0;
					byte[] bufferOut = new byte[1024];
					while ((bytes = in.read(bufferOut)) != -1) {
						out.write(bufferOut, 0, bytes);
					}
					in.close();
				}
			}

			byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("UTF-8");
			out.write(endData);
			out.flush();
			out.close();

			// 读取返回数据
			StringBuffer strBuf = new StringBuffer();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					conn.getInputStream(), "utf8"));
			String line = null;
			while ((line = reader.readLine()) != null) {
				strBuf.append(line).append("\n");
			}
			res = JSONObject.parseObject(strBuf.toString(), clazz);
			reader.close();
			reader = null;
		} catch (Exception e) {
			System.out.println("发送POST请求出错。" + requestUrl);
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.disconnect();
				conn = null;
			}
		}
		return res;
	}

	/**
	 * 上传图片(HTTPS)
	 * 
	 * @param requestUrl
	 * @param paramData
	 * @param fileMap
	 * @return
	 */
	private static JSONObject httpsRequestWithFile(String requestUrl,
			String paramData, Map<String, String> fileMap) {
		JSONObject res = null;
		HttpsURLConnection conn = null;
		String BOUNDARY = "---------------------------"
				+ System.currentTimeMillis(); // boundary就是request头和上传文件内容的分隔符
		try {
			URL url = new URL(requestUrl);
			conn = (HttpsURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			conn.setReadTimeout(30000);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
			conn.setRequestProperty("Content-Type",
					"multipart/form-data; boundary=" + BOUNDARY);

			OutputStream out = new DataOutputStream(conn.getOutputStream());
			// text
			if (!StringUtils.isNullOrEmpty(paramData)) {
				StringBuffer strBuf = new StringBuffer();
				for (String param : paramData.split("\\&")) {
					String[] formDatas = param.split("\\=");
					String inputName = formDatas[0];
					String inputValue = formDatas[1];
					if (inputValue == null) {
						continue;
					}
					strBuf.append("\r\n").append("--").append(BOUNDARY)
							.append("\r\n");
					strBuf.append("Content-Disposition: form-data; name=\""
							+ inputName + "\"\r\n\r\n");
					strBuf.append(inputValue);
				}
				out.write(strBuf.toString().getBytes("utf8"));
			}

			// file
			if (fileMap != null) {
				Iterator<Map.Entry<String, String>> iter = fileMap.entrySet()
						.iterator();
				while (iter.hasNext()) {
					Map.Entry<String, String> entry = iter.next();
					String inputName = (String) entry.getKey();
					String inputValue = (String) entry.getValue();
					if (inputValue == null) {
						continue;
					}
					File file = new File(inputValue);
					String filename = file.getName();
					MagicMatch match = Magic.getMagicMatch(file, false, true);
					String contentType = match.getMimeType();

					StringBuffer strBuf = new StringBuffer();
					strBuf.append("\r\n").append("--").append(BOUNDARY)
							.append("\r\n");
					strBuf.append("Content-Disposition: form-data; name=\""
							+ inputName + "\"; filename=\"" + filename
							+ "\"\r\n");
					strBuf.append("Content-Type:" + contentType + "\r\n\r\n");

					out.write(strBuf.toString().getBytes("utf8"));

					DataInputStream in = new DataInputStream(
							new FileInputStream(file));
					int bytes = 0;
					byte[] bufferOut = new byte[1024];
					while ((bytes = in.read(bufferOut)) != -1) {
						out.write(bufferOut, 0, bytes);
					}
					in.close();
				}
			}

			byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
			out.write(endData);
			out.flush();
			out.close();

			// 读取返回数据
			StringBuffer strBuf = new StringBuffer();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					conn.getInputStream(), "utf8"));
			String line = null;
			while ((line = reader.readLine()) != null) {
				strBuf.append(line).append("\n");
			}
			res = JSONObject.parseObject(strBuf.toString());
			reader.close();
			reader = null;
		} catch (Exception e) {
			System.out.println("发送POST请求出错。" + requestUrl);
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.disconnect();
				conn = null;
			}
		}
		return res;
	}

	/**
	 * 上传图片(HTTPS)
	 * 
	 * @param requestUrl
	 * @param paramData
	 * @param fileMap
	 * @return
	 */
	private static <T> T httpsRequestWithFile(String requestUrl,
			String paramData, Map<String, String> fileMap, Class<T> clazz) {
		T res = null;
		HttpsURLConnection conn = null;
		String BOUNDARY = "---------------------------"
				+ System.currentTimeMillis(); // boundary就是request头和上传文件内容的分隔符
		try {
			URL url = new URL(requestUrl);
			conn = (HttpsURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			conn.setReadTimeout(30000);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
			conn.setRequestProperty("Content-Type",
					"multipart/form-data; boundary=" + BOUNDARY);

			OutputStream out = new DataOutputStream(conn.getOutputStream());
			// text
			if (!StringUtils.isNullOrEmpty(paramData)) {
				StringBuffer strBuf = new StringBuffer();
				for (String param : paramData.split("\\&")) {
					String[] formDatas = param.split("\\=");
					String inputName = formDatas[0];
					String inputValue = formDatas[1];
					if (inputValue == null) {
						continue;
					}
					strBuf.append("\r\n").append("--").append(BOUNDARY)
							.append("\r\n");
					strBuf.append("Content-Disposition: form-data; name=\""
							+ inputName + "\"\r\n\r\n");
					strBuf.append(inputValue);
				}
				out.write(strBuf.toString().getBytes("utf8"));
			}

			// file
			if (fileMap != null) {
				Iterator<Map.Entry<String, String>> iter = fileMap.entrySet()
						.iterator();
				while (iter.hasNext()) {
					Map.Entry<String, String> entry = iter.next();
					String inputName = (String) entry.getKey();
					String inputValue = (String) entry.getValue();
					if (inputValue == null) {
						continue;
					}
					File file = new File(inputValue);
					String filename = file.getName();
					MagicMatch match = Magic.getMagicMatch(file, false, true);
					String contentType = match.getMimeType();

					StringBuffer strBuf = new StringBuffer();
					strBuf.append("\r\n").append("--").append(BOUNDARY)
							.append("\r\n");
					strBuf.append("Content-Disposition: form-data; name=\""
							+ inputName + "\"; filename=\"" + filename
							+ "\"\r\n");
					strBuf.append("Content-Type:" + contentType + "\r\n\r\n");

					out.write(strBuf.toString().getBytes("utf8"));

					DataInputStream in = new DataInputStream(
							new FileInputStream(file));
					int bytes = 0;
					byte[] bufferOut = new byte[1024];
					while ((bytes = in.read(bufferOut)) != -1) {
						out.write(bufferOut, 0, bytes);
					}
					in.close();
				}
			}

			byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
			out.write(endData);
			out.flush();
			out.close();

			// 读取返回数据
			StringBuffer strBuf = new StringBuffer();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					conn.getInputStream(), "utf8"));
			String line = null;
			while ((line = reader.readLine()) != null) {
				strBuf.append(line).append("\n");
			}
			res = JSONObject.parseObject(strBuf.toString(), clazz);
			reader.close();
			reader = null;
		} catch (Exception e) {
			System.out.println("发送POST请求出错。" + requestUrl);
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.disconnect();
				conn = null;
			}
		}
		return res;
	}


	/**
	 * 发起https请求并获取结果(不带SSL请求证书)
	 *
	 * @param <T>
	 *
	 * @param requestUrl
	 *            请求地址
	 *
	 * @param requestMethod
	 *            请求方式（GET、POST）
	 *
	 * @param paramData
	 *            提交的数据
	 *
	 * @return XMLUtil转换为对应的实体类
	 */
	public static <T> T httpRequestXMLToClazz(String requestUrl, String requestMethod,
									String paramData, Class<T> clazz) {

		if (requestUrl.startsWith("https://")) {
			return httpsRequestXMLToClazz(requestUrl, requestMethod, paramData, clazz);
		}

		T jsonObject = null;
		StringBuffer buffer = new StringBuffer();

		try {
			URL url = new URL(requestUrl);
			HttpURLConnection httpUrlConn = (HttpURLConnection) url
					.openConnection();

			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);

			// 设置请求方式（GET/POST）
			httpUrlConn.setRequestMethod(requestMethod);
			if ("GET".equalsIgnoreCase(requestMethod)) {
				httpUrlConn.connect();
			}

			// 当有数据需要提交时
			if (null != paramData) {
				OutputStream outputStream = httpUrlConn.getOutputStream();
				// 注意编码格式，防止中文乱码
				outputStream.write(paramData.getBytes("utf8"));
				outputStream.close();
			}

			// 将返回的输入流转换成字符串
			InputStream inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(
					inputStream, "utf8");
			BufferedReader bufferedReader = new BufferedReader(
					inputStreamReader);

			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}

			bufferedReader.close();
			inputStreamReader.close();

			// 释放资源
			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();

			if (clazz != null) {
				jsonObject = XmlUtil.parseObject(buffer.toString(),clazz);
			}
		} catch (ConnectException ce) {
			log.error("Weixin server connection timed out.");
		} catch (Exception e) {
			log.error("https request error:{}", e);
		}

		return jsonObject;
	}

	/**
	 * 发起https请求并获取结果(不带SSL请求证书)
	 *
	 * @param <T>
	 *
	 * @param requestUrl
	 *            请求地址
	 *
	 * @param requestMethod
	 *            请求方式（GET、POST）
	 *
	 * @param paramData
	 *            提交的数据
	 *
	 * @return XMLUtil转换为对应的实体类
	 */
	private static <T> T httpsRequestXMLToClazz(String requestUrl, String requestMethod,
									  String paramData, Class<T> clazz) {
		T jsonObject = null;
		StringBuffer buffer = new StringBuffer();

		try {
			// 创建SSLContext对象，并使用我们指定的信任管理器初始化
			TrustManager[] tm = { new MyX509TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());

			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();
			URL url = new URL(requestUrl);
			HttpsURLConnection httpUrlConn = (HttpsURLConnection) url
					.openConnection();

			httpUrlConn.setSSLSocketFactory(ssf);
			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);

			// 设置请求方式（GET/POST）
			httpUrlConn.setRequestMethod(requestMethod);
			if ("GET".equalsIgnoreCase(requestMethod)) {
				httpUrlConn.connect();
			}

			// 当有数据需要提交时
			if (null != paramData) {
				OutputStream outputStream = httpUrlConn.getOutputStream();
				// 注意编码格式，防止中文乱码
				outputStream.write(paramData.getBytes("utf8"));
				outputStream.close();
			}

			// 将返回的输入流转换成字符串
			InputStream inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(
					inputStream, "utf8");
			BufferedReader bufferedReader = new BufferedReader(
					inputStreamReader);

			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}

			bufferedReader.close();
			inputStreamReader.close();

			// 释放资源
			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();
			if (clazz != null) {
				jsonObject = XmlUtil.parseObject(buffer.toString(), clazz);
			}
		} catch (ConnectException ce) {
			log.error("Weixin server connection timed out.");
		} catch (Exception e) {
			log.error("https request error:{}", e);
		}

		return jsonObject;
	}

}
