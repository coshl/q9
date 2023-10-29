package com.summer.service.yys.wuhua.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class HttpGzipUtil {
    private static Logger logger = LoggerFactory.getLogger(HttpGzipUtil.class);
	public static int connectionTimeout = 30000;
	public static int readDataTimeOut = 30000;

	public static int timeout = 30000;
	public static String charset = "UTF-8";
	private static final String APPLICATION_JSON = "application/json";

	public static String postRequest(String str,String url) throws IOException {
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		CloseableHttpClient httpClient = httpClientBuilder.build();
		HttpPost method = new HttpPost(url);
		method.addHeader("Content-type","application/json; charset=ISO-8859-1");
		method.setHeader("Content-Encoding", "gzip");
		method.setEntity(new StringEntity(compress(str), Charset.forName("ISO-8859-1")));
		HttpResponse response = httpClient.execute(method);
		int statusCode = response.getStatusLine().getStatusCode();
		String body = null;
		if(statusCode!=200) {
			logger.error("请求异常" ,statusCode);
		}else {
			body = EntityUtils.toString(response.getEntity());
		}
		return body;
	}

	public static String postJsonRequest(String str,String url) throws IOException {
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		CloseableHttpClient httpClient = httpClientBuilder.build();
		HttpPost method = new HttpPost(url);
		method.addHeader("Content-type","application/json; charset=ISO-8859-1");
		method.setHeader("Content-Encoding", "gzip");
		method.setEntity(new StringEntity(compress(str), Charset.forName("ISO-8859-1")));
		HttpResponse response = httpClient.execute(method);
		int statusCode = response.getStatusLine().getStatusCode();
		String body = null;
		if(statusCode!=200) {
			logger.error("请求异常" ,statusCode);
		}else {
			body = EntityUtils.toString(response.getEntity());
		}
		return body;
	}

	public static String compress(String str) throws IOException {
		if (str == null || str.length() == 0) {
			return str;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		GZIPOutputStream gzip = new GZIPOutputStream(out);
		gzip.write(str.getBytes("UTF-8"));
		gzip.close();
		return out.toString("ISO-8859-1"); // utf-8 ISO-8859-1
	}
	// 解压缩
	public static String uncompress(String str) throws IOException {
		if (str == null || str.length() == 0) {
			return str;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ByteArrayInputStream in = new ByteArrayInputStream(
				str.getBytes("ISO-8859-1"));
		GZIPInputStream gunzip = new GZIPInputStream(in);
		byte[] buffer = new byte[256];
		int n;
		while ((n = gunzip.read(buffer)) >= 0) {
			out.write(buffer, 0, n);
		}
		// toString()使用平台默认编码，也可以显式的指定如toString("GBK")
		return out.toString();
	}

	public static String httpPostJsonReceiveGzip(String url, String str) throws IOException {
		logger.info("URL={},req={}", url, str);
		long start = System.currentTimeMillis();
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		CloseableHttpClient httpClient = httpClientBuilder.build();

		// 配置信息
		RequestConfig requestConfig = RequestConfig.custom()
				// 设置连接超时时间(单位毫秒)
				.setConnectTimeout(connectionTimeout)
				// 设置请求超时时间(单位毫秒)
				.setConnectionRequestTimeout(readDataTimeOut)
				// socket读写超时时间(单位毫秒)
				.setSocketTimeout(timeout)
				// 设置是否允许重定向(默认为true)
				.setRedirectsEnabled(true).build();


		HttpPost post = new HttpPost(url);
		post.setConfig(requestConfig);
		StringBuffer ret = new StringBuffer();
		InputStreamReader read = null;

		try {
			StringEntity sentity = new StringEntity(str, "UTF-8");
			sentity.setContentEncoding(charset);
			sentity.setContentType(APPLICATION_JSON);
			post.setEntity(sentity);

			HttpResponse res = httpClient.execute(post);

			int statusCode = res.getStatusLine().getStatusCode();
			logger.debug("visit url :{}, request status : {}", url.toString(), statusCode);
			BufferedReader br = null;
			HttpEntity entity = res.getEntity();
			read = new InputStreamReader(entity.getContent(), charset);
			br = new BufferedReader(read);// 缓冲


			String line = null;
			while ((line = br.readLine()) != null) {
				ret.append(line);
			}
			if(read != null) {
				read.close();
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			long end = System.currentTimeMillis();
			logger.info("Cost:{},URL={},results={}", end - start, url, e.getMessage());
			throw new RuntimeException(e);
		} finally {
			try {
				if(read != null) {
					read.close();
				}

			} catch (IOException e) {
				logger.error(e.getMessage(),e);
			}
			long end = System.currentTimeMillis();
			logger.info("Cost:{},URL={},req={}", end - start, url, str);
		}
		return ret.toString();
	}
}
