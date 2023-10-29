package com.summer.util;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;
import java.util.zip.Deflater;

@Slf4j
public class HttpUtil {

    private static final String CHARSET_UTF8 = "UTF-8";

    private static final CloseableHttpClient closeableHttpClient = HttpClients.createDefault();

    /**
     * post形式提交
     *
     * @param url       路径
     * @param paramList 参数集合
     * @return
     */
    public static String post(String url, List<NameValuePair> paramList) {
        CloseableHttpClient client = HttpClients.createDefault();
        String responseText = "";
        CloseableHttpResponse response = null;
        try {
            HttpPost method = new HttpPost(url);
            method.setEntity(new UrlEncodedFormEntity(paramList, CHARSET_UTF8));
            response = client.execute(method);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                responseText = EntityUtils.toString(entity, CHARSET_UTF8);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return responseText;
    }

    public static String doGet(String url) {
        String result = "";
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet(url);
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpget);
            HttpEntity httpEntity = response.getEntity();
            result = EntityUtils.toString(httpEntity);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }


    public static String doGet(String url,int timeOut) {
        String result = "";
        CloseableHttpClient httpclient = HttpClients.createDefault();
        RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(timeOut)
                .setSocketTimeout(timeOut).setConnectTimeout(timeOut).build();
        HttpGet httpget = new HttpGet(url);
        httpget.setConfig(requestConfig);
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpget);
            HttpEntity httpEntity = response.getEntity();
            result = EntityUtils.toString(httpEntity);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static String doPostWithString(String url, Map<String, String> params) {

        String result = "";

        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        if (params != null && params.size() > 0) {
            for (String key : params.keySet()) {
                String value = params.get(key);
                BasicNameValuePair nameValuePair = new BasicNameValuePair(key, value);
                nameValuePairs.add(nameValuePair);
            }
        }

        CloseableHttpResponse response = null;
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, CHARSET_UTF8));
            response = httpclient.execute(httpPost);
            if (response == null) {
                log.error("response==null params={}", params);
            }
            HttpEntity httpEntity = response.getEntity();
            result = EntityUtils.toString(httpEntity);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                if (httpclient != null) {
                    httpclient.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    public static String doPost(String url, Map<String, Object> params) {

        String result = "";

        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        if (params != null && params.size() > 0) {
            for (String key : params.keySet()) {
                String value = params.get(key).toString();
                BasicNameValuePair nameValuePair = new BasicNameValuePair(key, value);
                nameValuePairs.add(nameValuePair);
            }
        }

        CloseableHttpResponse response = null;
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, CHARSET_UTF8));
            response = httpclient.execute(httpPost);
            if (response == null) {
                log.error("response==null params={}", params);
            }
            HttpEntity httpEntity = response.getEntity();
            result = EntityUtils.toString(httpEntity);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                if (httpclient != null) {
                    httpclient.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    public static String doPost(String url, Map<String, String> headers, Map<String, Object> params) {

        String result = "";

        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        if (params != null && params.size() > 0) {
            for (String key : params.keySet()) {
                String value = params.get(key).toString();
                BasicNameValuePair nameValuePair = new BasicNameValuePair(key, value);
                nameValuePairs.add(nameValuePair);
            }
        }
        List<Header> headersList = new ArrayList<>();
        if (!headers.isEmpty()) {
            for (String key : headers.keySet()) {
                Header header = new BasicHeader(key, headers.get(key));
                headersList.add(header);
            }
        }
        CloseableHttpResponse response = null;
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, CHARSET_UTF8));
            Header[] headersArr = new Header[headersList.size()];
            httpPost.setHeaders(headersList.toArray(headersArr));
            response = httpclient.execute(httpPost);
            if (response == null) {
                log.error("response==null params={}", params);
            }
            HttpEntity httpEntity = response.getEntity();
            result = EntityUtils.toString(httpEntity);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                if (httpclient != null) {
                    httpclient.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;
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

    public static String postJsonDataWithHeader(String url, String jsonStrData,
                                      Map<String, String> headers) throws IOException {
        HttpPost post = new HttpPost(url);
        CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
        String msg = null;

        Integer soketOut = 20000;
        Integer connOut = 20000;

        if (!headers.isEmpty()) {
            for (String key : headers.keySet()) {
                post.setHeader(key,headers.get(key));
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

    /**
     * 请求接口
     *
     * @param url
     * @param jsonObj
     */

    public static String httpPostWithJsonDeflate(String jsonObj, String url) throws IOException {
        String body = "";

        HttpPost post = null;
        try {
            HttpClient httpClient = HttpClients.createDefault();

            post = new HttpPost(url);
            // 构造消息头
            post.setHeader("Content-type", "application/json; charset=utf-8");
            post.setHeader("Accept", "application/json");

            byte[] deflateByte = compress(jsonObj.getBytes());
            ByteArrayEntity bEntity = new ByteArrayEntity(deflateByte);
            bEntity.setContentType("application/json");
            //bEntity.setContentEncoding("UTF-8");
            bEntity.setContentEncoding("deflate");
            post.setEntity(bEntity);
            post.setHeader("Content-Encoding", "deflate");

            HttpResponse response = httpClient.execute(post);

            // 检验返回码
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                System.out.println("请求出错: " + statusCode);
            } else {

                //获取结果实体
                HttpEntity entity1 = response.getEntity();
                if (entity1 != null) {
                    //按指定编码转换结果实体为String类型
                    body = EntityUtils.toString(entity1, "UTF-8");
                    System.out.println("请求返回JSON数据 >> " + body);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (post != null) {
                post.releaseConnection();
            }
        }
        return body;
    }

    public static byte[] compress(byte[] inputByte) {
        int len = 0;
        Deflater defl = new Deflater(9, false);
        defl.setInput(inputByte);
        defl.finish();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] outputByte = new byte[1024];
        try {
            while (!defl.finished()) {
                // 压缩并将压缩后的内容输出到字节输出流bos中
                len = defl.deflate(outputByte);
                bos.write(outputByte, 0, len);
            }
            defl.end();
        } finally {
            try {
                bos.close();
            } catch (Exception ex) {
            }
        }
        return bos.toByteArray();
    }

    /**
     * http请求
     *
     * @param url
     * @param jsonObject
     * @return
     * @throws Exception
     */
    public static JSONObject doHttpPost(String url, JSONObject jsonObject) throws Exception {
        //设置传入参数
        StringEntity stringEntity = new StringEntity(jsonObject.toJSONString(), CHARSET_UTF8);
        stringEntity.setContentEncoding(CHARSET_UTF8);
        stringEntity.setContentType("application/json");
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Connection", "close");
        httpPost.setEntity(stringEntity);
        HttpResponse resp = closeableHttpClient.execute(httpPost);
        HttpEntity he = resp.getEntity();
        String respContent = EntityUtils.toString(he, CHARSET_UTF8);
        return (JSONObject) JSONObject.parse(respContent);
    }

    /**
     * http请求
     *
     * @param url
     * @param
     * @return
     * @throws Exception
     */
    public static JSONObject doHttpPost(String url,Map<String,String> headers, String param) throws Exception {
        //设置传入参数
        StringEntity stringEntity = new StringEntity(param, CHARSET_UTF8);
        stringEntity.setContentEncoding(CHARSET_UTF8);
        stringEntity.setContentType("application/json");
        HttpPost httpPost = new HttpPost(url);
        RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(30000)
                .setSocketTimeout(30000).setConnectTimeout(30000).build();
        httpPost.setConfig(requestConfig);
        for (String key : headers.keySet())
        {
            httpPost.setHeader(key,headers.get(key));
        }

        httpPost.setHeader("Connection", "close");
        httpPost.setEntity(stringEntity);
        HttpResponse resp = closeableHttpClient.execute(httpPost);
        HttpEntity he = resp.getEntity();
        String respContent = EntityUtils.toString(he, CHARSET_UTF8);
        return (JSONObject) JSONObject.parse(respContent);
    }

    public static String postJson(String url, String jsonString) throws Exception {
        //设置传入参数
        StringEntity stringEntity = new StringEntity(jsonString, CHARSET_UTF8);
        stringEntity.setContentEncoding(CHARSET_UTF8);
        stringEntity.setContentType("application/json");
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Connection", "close");
        httpPost.setEntity(stringEntity);
        HttpResponse resp = closeableHttpClient.execute(httpPost);
        HttpEntity he = resp.getEntity();
        String respContent = EntityUtils.toString(he, CHARSET_UTF8);
        return respContent;

    }

    private static CloseableHttpClient httpclient = HttpClients.createDefault();

    public static String post(String url, Map<String, String> paramMap) {
        CloseableHttpResponse response = null;
        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");

            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            Iterator<Map.Entry<String, String>> it = paramMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, String> entry = it.next();
                nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));

            //设置连接/读取超时时间
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(60000).setConnectTimeout(60000).build();//设置请求和传输超时时间
            httpPost.setConfig(requestConfig);
            response = httpclient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(response.getEntity(), "UTF-8");
            EntityUtils.consume(entity);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != response) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * MD5鍔犲瘑
     * @param signSource
     * @return
     */
    public static String calculate(String signSource) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(signSource.getBytes(StandardCharsets.UTF_8));
            byte[] b = md.digest();

            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                int i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }

            return buf.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
