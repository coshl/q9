package com.summer.util;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.*;

public class WebClient {
    public static WebClient webClient;

    public static WebClient getInstance() {
        if (webClient == null) {
            webClient = new WebClient();
        }
        return webClient;
    }

    /**
     * 通过get来访问url
     *
     * @param src 需要访问的url
     * @return
     * @throws Exception
     */
    public String doGet(String src) throws Exception {
        return doGet(src, "utf-8");
    }

    /**
     * 通过get来访问url
     *
     * @param src          需要访问的地址
     * @param outputencode 获取输出时的编码
     * @return
     * @throws Exception
     */
    public String doGet(String src, String outputencode) throws Exception {
        return doGet(src, outputencode, null);
    }

    /**
     * 通过get来访问url
     *
     * @param src          需要访问的地址
     * @param outputencode 获取输出时的编码
     * @param headers      需要添加的访问头信息
     * @return
     * @throws Exception
     */
    public String doGet(String src, String outputencode,
                        HashMap<String, String> headers) throws Exception {
        StringBuffer result = new StringBuffer();
        URL url = new URL(src);
        URLConnection connection = url.openConnection();
        BufferedReader in = null;
        try {
            if (headers != null) {
                Iterator<String> iterators = headers.keySet().iterator();
                while (iterators.hasNext()) {
                    String key = iterators.next();
                    connection.setRequestProperty(key, headers.get(key));
                }
            }
            connection.connect();
            in = new BufferedReader(new InputStreamReader(connection
                    .getInputStream(), outputencode));
            String line;
            while ((line = in.readLine()) != null) {
                result.append("\n" + line);
            }
            return result.toString();
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (in != null) {
                in.close();
            }
        }

    }

    public List<NameValuePair> buildNameValuePair(Map<String, String> params) {
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                nvps.add(new BasicNameValuePair(entry.getKey(), entry
                        .getValue()));
            }
        }

        return nvps;
    }

    /**
     * 通过post来访问url
     *
     * @param url
     * @param params
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public String doPost(String url, Map<String, String> params)
            throws ClientProtocolException, IOException {
        String result = null;
        List<NameValuePair> nvps = buildNameValuePair(params);
        CloseableHttpClient httpclient = HttpClients.createDefault();
        EntityBuilder builder = EntityBuilder.create();
        try {
            HttpPost httpPost = new HttpPost(url);
            builder.setParameters(nvps);
            httpPost.setEntity(builder.build());
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
            RequestConfig requestConfig = RequestConfig.custom()
                    .setSocketTimeout(20000).setConnectTimeout(20000).build();// 设置请求和传输超时时间
            httpPost.setConfig(requestConfig);
            CloseableHttpResponse response = httpclient.execute(httpPost);
            try {
                HttpEntity entity = response.getEntity();
                if ("OK".equals(response.getStatusLine().getReasonPhrase())
                        && response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    result = EntityUtils.toString(entity, "UTF-8");
                }
                EntityUtils.consume(entity);
            } finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }
        return result;
    }

    /**
     * 通过post来访问url:https
     *
     * @param url
     * @param map
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public String doPostSLL(String url, Map<String, String> map, String charset) {
        SSLClient httpClient = null;
        HttpPost httpPost = null;
        String result = null;
        try {
            httpClient = new SSLClient();
            httpPost = new HttpPost(url);
            //设置参数
            List<BasicNameValuePair> list = new ArrayList<>();
            Iterator iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> elem = (Map.Entry<String, String>) iterator.next();
                list.add(new BasicNameValuePair(elem.getKey(), elem.getValue()));
            }
            if (list.size() > 0) {
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, charset);
                httpPost.setEntity(entity);
            }
            HttpResponse response = httpClient.execute(httpPost);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    result = EntityUtils.toString(resEntity, charset);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }


    public String postJsonData(String url, String jsonStrData,
                               HashMap<String, Object> params) {
        HttpPost post = new HttpPost(url);
        CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
        String msg = null;
        try {
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
            RequestConfig requestConfig = RequestConfig.custom()
                    .setSocketTimeout(soketOut).setConnectTimeout(connOut)
                    .build();// 设置请求和传输超时时间
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
//				System.out.println(result);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return msg;
    }
}
