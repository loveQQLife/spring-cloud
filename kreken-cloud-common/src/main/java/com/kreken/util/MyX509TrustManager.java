package com.kreken.util;

import com.mysql.jdbc.StringUtils;

import javax.net.ssl.X509TrustManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 证书信任管理器（用于https请求）
 * 
 * @author zengxiangxin
 */

public class MyX509TrustManager implements X509TrustManager {

	private List<X509Certificate> x509Certificates;

	public MyX509TrustManager() {

	}

	public MyX509TrustManager(String sslFilePath) {

		if (!StringUtils.isNullOrEmpty(sslFilePath)
				&& new File(sslFilePath).exists()) {

			try {
				InputStream inStream = new FileInputStream(sslFilePath);
				CertificateFactory cf = CertificateFactory.getInstance("X.509");
				X509Certificate cert = (X509Certificate) cf
						.generateCertificate(inStream);
				inStream.close();

				if (x509Certificates == null) {
					x509Certificates = new ArrayList<X509Certificate>();
				}

				x509Certificates.add(cert);

			} catch (CertificateException e) {
				e.printStackTrace();
			} catch( IOException ex){
				ex.printStackTrace();
			}
		}
	}

	public void checkClientTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {
	}

	public void checkServerTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {
	}

	public X509Certificate[] getAcceptedIssuers() {

		if (this.x509Certificates != null && this.x509Certificates.size() > 0) {
			X509Certificate[] result = new X509Certificate[] {};
			return this.x509Certificates.toArray(result);
		}

		return null;
	}

}
