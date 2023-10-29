package com.summer.service.card;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * @Description: HTTP工具类
 * @Company: Risk-FINTECH
 * @ProjectName: Demo
 * @Author: Risk
 * @GmtCreate: 2019-09-22 20:51:11
 */
public class HttpClientUtil {

    public static String CONTENT_TYPE = "application/json; charset=UTF-8";

    /**
     * get请求
     *
     * @param url
     * @return
     */
    public static String get(String url) {
        return get(url, null);
    }

    public static String get(String url, Map<String, String> header) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        if (header != null) {
            Header[] heads = getHeader(header);
            httpGet.setHeaders(heads);
        }
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(10000).build();
        httpGet.setConfig(requestConfig);
        CloseableHttpResponse httpResponse = null;

        String var6;
        try {
            httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            var6 = EntityUtils.toString(httpEntity, "UTF-8");
            return var6;
        } catch (Exception var10) {
            var10.printStackTrace();
            var6 = null;
        } finally {
            httpClose(httpResponse, httpClient);
        }

        return var6;
    }

    /**
     * post请求
     *
     * @param url
     * @param o
     * @return
     */
    public static JSONObject postAndGetStatus(String url, Object o) {
        return postAndGetStatus(url, (Map) null, o);
    }

    public static JSONObject postAndGetStatus(String url, Map<String, String> header, Object o) {
        return postAndGetStatus(url, header, o, CONTENT_TYPE);
    }

    public static JSONObject postAndGetStatus(String url, Map<String, String> header, Object o, String contentType) {
        String data = JSONObject.toJSONString(o);
        return postAndGetStatus(url, header, data, contentType);
    }

    public static JSONObject postAndGetStatus(String url, Map<String, String> header, String data, String contentType) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        if (header != null) {
            Header[] heads = getHeader(header);
            httpPost.setHeaders(heads);
        }

        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(10000).build();
        httpPost.setConfig(requestConfig);
        StringEntity postEntity = new StringEntity(data, "UTF-8");
        postEntity.setContentType(contentType);
        httpPost.setEntity(postEntity);
        CloseableHttpResponse httpResponse = null;

        JSONObject result = new JSONObject();
        Integer code = 500;
        String var10;
        try {
            httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            var10 = EntityUtils.toString(httpEntity, "UTF-8");
            code = httpResponse.getStatusLine().getStatusCode();
        } catch (Exception var14) {
            var14.printStackTrace();
            var10 = null;
        } finally {
            httpClose(httpResponse, httpClient);
        }
        result.put("code", code);
        result.put("data", var10);
        return result;
    }

    /**
     * post请求
     *
     * @param url
     * @param o
     * @return
     */
    public static String post(String url, Object o) {
        return post(url, (Map) null, o);
    }

    public static String post(String url, Map<String, String> header, Object o) {
        return post(url, header, o, CONTENT_TYPE);
    }

    public static String post(String url, Map<String, String> header, Object o, String contentType) {
        String data = JSONObject.toJSONString(o);
        return post(url, header, data, contentType);
    }

    public static String post(String url, Map<String, String> header, String data, String contentType) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        if (header != null) {
            Header[] heads = getHeader(header);
            httpPost.setHeaders(heads);
        }

        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(10000).build();
        httpPost.setConfig(requestConfig);
        StringEntity postEntity = new StringEntity(data, "UTF-8");
        postEntity.setContentType(contentType);
        httpPost.setEntity(postEntity);
        CloseableHttpResponse httpResponse = null;

        String var10;
        try {
            httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            var10 = EntityUtils.toString(httpEntity, "UTF-8");
        } catch (Exception var14) {
            var14.printStackTrace();
            var10 = null;
        } finally {
            httpClose(httpResponse, httpClient);
        }
        return var10;
    }

    private static Header[] getHeader(Map<String, String> params) {
        List<Header> heads = new ArrayList();
        Set<String> keys = params.keySet();
        Iterator var3 = keys.iterator();

        while (var3.hasNext()) {
            String key = (String) var3.next();
            Header head = new BasicHeader(key, (String) params.get(key));
            heads.add(head);
        }

        return (Header[]) heads.toArray(new Header[heads.size()]);
    }

    private static void httpClose(CloseableHttpResponse httpResponse, CloseableHttpClient httpClient) {
        if (httpResponse != null) {
            try {
                httpResponse.close();
            } catch (IOException var4) {
                var4.printStackTrace();
            }
        }

        if (httpClient != null) {
            try {
                httpClient.close();
            } catch (IOException var3) {
                var3.printStackTrace();
            }
        }

    }


    public static String postForm(String url, Map<String, String> params) {
        return postForm(url, (Map) null, params);
    }

    public static String postForm(String url, Map<String, String> header, Map<String, String> params) {
        List<NameValuePair> formparams = null;
        if (params != null) {
            formparams = getParams(params);
        }

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        if (header != null) {
            Header[] heads = getHeader(header);
            httpPost.setHeaders(heads);
        }

        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(20000).setConnectTimeout(20000).build();
        httpPost.setConfig(requestConfig);
        CloseableHttpResponse httpResponse = null;

        HttpEntity httpEntity;
        try {
            StringEntity postEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
            postEntity.setContentType("application/x-www-form-urlencoded;charset=UTF-8");
            httpPost.setEntity(postEntity);
            httpResponse = httpClient.execute(httpPost);
            httpEntity = httpResponse.getEntity();
            String var10 = EntityUtils.toString(httpEntity, "UTF-8");
            return var10;
        } catch (Exception var14) {
            var14.printStackTrace();
            httpEntity = null;
        } finally {
            httpClose(httpResponse, httpClient);
        }

        return null;
    }

    private static List<NameValuePair> getParams(Map<String, String> params) {
        List<NameValuePair> formparams = new ArrayList();
        Set<String> keys = params.keySet();
        Iterator var3 = keys.iterator();

        while (var3.hasNext()) {
            String key = (String) var3.next();
            String val = (String) params.get(key);
            if (!StringUtils.isEmpty(val)) {
                formparams.add(new BasicNameValuePair(key, val));
            }
        }

        return formparams;
    }


    public static String post(String url, Map<String, String> header, byte[] body) {
        StringBuilder sb = new StringBuilder();
        try {
            HttpClient client = HttpClientBuilder.create().build();
            HttpPost post = new HttpPost(url);
            post.setEntity(new ByteArrayEntity(body));
            if (Objects.nonNull(header)) {
                for (String key : header.keySet()) {
                    post.setHeader(key, header.get(key));
                }
            }
            HttpResponse response = client.execute(post);
            InputStream inputStream = response.getEntity().getContent();
            String line;
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
