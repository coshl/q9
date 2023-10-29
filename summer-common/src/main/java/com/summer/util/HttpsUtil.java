package com.summer.util;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.*;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * HTTP工具类 发送http/https协议get/post请求，发送map，json，xml，txt数据
 *
 * @author happyqing 2016-5-20
 */
public final class HttpsUtil {
    private static Log log = LogFactory.getLog(HttpUtil.class);

    /**
     * 执行一个http/https get请求，返回请求响应的文本数据
     *
     * @param url         请求的URL地址
     * @param queryString 请求的查询参数,可以为null
     * @param charset     字符集
     * @param pretty      是否美化
     * @return 返回请求响应的文本数据
     */
    public static String doGet(String url, String queryString, String charset,
                               boolean pretty) throws IOException {
        StringBuffer response = new StringBuffer();
        HttpClient client = new HttpClient();
        if (url.startsWith("https")) {
            // https请求
            Protocol myhttps = new Protocol("https",
                    new MySSLProtocolSocketFactory(), 443);
            Protocol.registerProtocol("https", myhttps);
        }
        HttpMethod method = new GetMethod(url);
        try {
            if (StringUtils.isNotBlank(queryString))
            // 对get请求参数编码，汉字编码后，就成为%式样的字符串
            {
                method.setQueryString(URIUtil.encodeQuery(queryString));
            }
            client.executeMethod(method);
            if (method.getStatusCode() == HttpStatus.SC_OK) {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(method.getResponseBodyAsStream(),
                                charset));
                String line;
                while ((line = reader.readLine()) != null) {
                    if (pretty) {
                        response.append(line).append(
                                System.getProperty("line.separator"));
                    } else {
                        response.append(line);
                    }
                }
                reader.close();
            }
        } catch (IOException e) {
            log.error("执行Get请求" + url + "时，发生异常！", e);
            throw new IOException();
        } finally {
            method.releaseConnection();
        }
        return response.toString();
    }

    /**
     * 执行一个http/https post请求，返回请求响应的文本数据
     *
     * @param url     请求的URL地址
     * @param params  请求的查询参数,可以为null
     * @param charset 字符集
     * @param pretty  是否美化
     * @return 返回请求响应的文本数据
     */
    public static String doPost(String url, Map<String, String> params,
                                String charset, boolean pretty) {
        StringBuffer response = new StringBuffer();
        HttpClient client = new HttpClient();
        if (url.startsWith("https")) {
            // https请求
            Protocol myhttps = new Protocol("https",
                    new MySSLProtocolSocketFactory(), 443);
            Protocol.registerProtocol("https", myhttps);
        }
        PostMethod method = new PostMethod(url);
        // 设置参数的字符集
        method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,
                charset);
        try {
            // 设置post数据
            if (params != null) {
                // HttpMethodParams p = new HttpMethodParams();
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    // p.setParameter(entry.getKey(), entry.getValue());
                    method.setParameter(entry.getKey(), URLEncoder.encode(entry.getValue(), charset));
                }
                // method.setParams(p);
            }
            client.executeMethod(method);
            if (method.getStatusCode() == HttpStatus.SC_OK) {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(method.getResponseBodyAsStream(),
                                charset));
                String line;
                while ((line = reader.readLine()) != null) {
                    if (pretty) {
                        response.append(line).append(
                                System.getProperty("line.separator"));
                    } else {
                        response.append(line);
                    }
                }
                reader.close();
            }
        } catch (IOException e) {
            log.error("执行Post请求" + url + "时，发生异常！", e);
        } finally {
            method.releaseConnection();
        }
        return response.toString();
    }

    public static String httpRequest(String requestUrl, String requestMethod, String outputStr) {
        try {
            URL url = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            conn.setRequestMethod(requestMethod);
//            conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("content-type", "application/json");
            // 当outputStr不为null时向输出流写数据
            if (null != outputStr) {
                OutputStream outputStream = conn.getOutputStream();
                // 注意编码格式
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }
            // 从输入流读取返回内容
            InputStream inputStream = conn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String str = null;
            StringBuffer buffer = new StringBuffer();
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            // 释放资源
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            inputStream = null;
            conn.disconnect();
            return buffer.toString();
        } catch (ConnectException ce) {
            System.out.println("连接超时：{}");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("http请求异常：{}");
        }
        return null;
    }

    /**
     * 执行一个http/https post请求， 直接写数据 json,xml,txt
     *
     * @param url     请求的URL地址
     * @param charset 字符集
     * @param pretty  是否美化
     * @return 返回请求响应的文本数据
     */
    public static String writePost(String url, String content, String charset,
                                   boolean pretty) {
        StringBuffer response = new StringBuffer();
        HttpClient client = new HttpClient();
        if (url.startsWith("https")) {
            // https请求
            Protocol myhttps = new Protocol("https",
                    new MySSLProtocolSocketFactory(), 443);
            Protocol.registerProtocol("https", myhttps);
        }
        PostMethod method = new PostMethod(url);
        try {
            // 设置请求头部类型参数
            // method.setRequestHeader("Content-Type","text/plain; charset=utf-8");//application/json,text/xml,
            // text/plain
            // method.setRequestBody(content);
            // //InputStream,NameValuePair[],String
            // RequestEntity是个接口，有很多实现类，发送不同类型的数据
            RequestEntity requestEntity = new StringRequestEntity(content,
                    "text/plain", charset);// application/json,text/xml,text/plain
            method.setRequestEntity(requestEntity);
            client.executeMethod(method);
            if (method.getStatusCode() == HttpStatus.SC_OK) {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(method.getResponseBodyAsStream(),
                                charset));
                String line;
                while ((line = reader.readLine()) != null) {
                    if (pretty) {
                        response.append(line).append(
                                System.getProperty("line.separator"));
                    } else {
                        response.append(line);
                    }
                }
                reader.close();
            }
        } catch (Exception e) {
            log.error("执行Post请求" + url + "时，发生异常！", e);
        } finally {
            method.releaseConnection();
        }
        return response.toString();
    }

    public static String postJsonData(String url, String jsonStrData) throws IOException {
        return postJsonData(url, jsonStrData, null);
    }

    public static String postJsonData(String url, String jsonStrData,
                                      HashMap<String, Object> params) throws IOException {
        HttpPost post = new HttpPost(url);
        CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
        String msg = null;

        Integer soketOut = 20000;
        Integer connOut = 20000;
        if (params != null) {
            if (params.containsKey("soketOut")) {
                soketOut = Integer.valueOf(params.get("soketOut") + "");
            }
            if (params.containsKey("connOut")) {
                connOut = Integer.valueOf(params.get("connOut") + "");
            }
            if (params.containsKey("Authorization")) {
                post.setHeader("Authorization", params.get("Authorization") + "");
            }

        }
        RequestConfig requestConfig =
                RequestConfig.custom().setSocketTimeout(soketOut).setConnectTimeout(connOut).build();// 设置请求和传输超时时间
        post.setConfig(requestConfig);
        // 修复 POST json 导致中文乱码
        HttpEntity entity = new StringEntity(jsonStrData, "UTF-8");
        post.setEntity(entity);
        post.setHeader("Content-type", "application/json");
        HttpResponse resp = closeableHttpClient.execute(post);
        InputStream respIs = resp.getEntity().getContent();
        byte[] respBytes = IOUtils.toByteArray(respIs);
        String result = new String(respBytes, Charset.forName("UTF-8"));
        if (null == result || result.length() == 0) {
            System.err.println("无响应");
        } else {
            msg = result;
            // System.out.println(result);
        }
        return msg;
    }

}
