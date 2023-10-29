package com.summer.service.yys;


import com.alibaba.fastjson.JSONObject;
import com.summer.util.log.StringUtils;
import org.apache.commons.codec.digest.HmacUtils;
import org.apache.commons.httpclient.URI;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.nio.charset.StandardCharsets;

public class Box {
    /**
     添加依赖
     <dependency>
     <groupId>org.apache.commons</groupId>
     <artifactId>commons-lang3</artifactId>
     <version><选择适合的版本></version>
     </dependency>
     <dependency>
     <groupId>commons-codec</groupId>
     <artifactId>commons-codec</artifactId>
     <version><选择适合的版本></version>
     </dependency>
     <dependency>
     <groupId>org.apache.httpcomponents</groupId>
     <artifactId>httpclient</artifactId>
     <version><选择适合的版本></version>
     </dependency>
     */
    /**
     * 示例代码，仅供参考。请勿在生产环境下直接使用
     *
     */

        /**
         * 对请求体签名签名
         * @param reqBody
         * @param timestamp
         * @param appSecret
         * @return
         */
        public static String signBodyWithAppSecret(String reqBody, long timestamp, String appSecret) {
            final byte[] keyBytes = StringUtils.getBytes(appSecret, String.valueOf(StandardCharsets.UTF_8));
            HmacUtils hmacUtils = new HmacUtils();
            final byte[] contentBytes = StringUtils.getBytes(reqBody + timestamp, String.valueOf(StandardCharsets.UTF_8));
            return hmacUtils.hmacSha256Hex(keyBytes,contentBytes);
        }

        /**
         * 发起请求请求
         * @param url
         * @param appName
         * @param sign
         * @param timestamp
         * @param reqBody
         * @return
         * @throws Exception
         */
        public static String request(String url, String appName, String sign, long timestamp, String reqBody) throws Exception {
            URI uri = new URI(url);
            HttpPost httpPost = new HttpPost(String.valueOf(uri));
            httpPost.setEntity(
                    EntityBuilder.create()
                            .setContentType(ContentType.APPLICATION_JSON.withCharset(StandardCharsets.UTF_8))
                            .setText(reqBody).build()
            );
            /*header: {
                "Content-Encoding": "gzip"
            }*/
            // 构建请求头
            httpPost.addHeader("AppName", appName);
            httpPost.addHeader("Sign", sign);
            httpPost.addHeader("SignVersion", "v2");
            httpPost.addHeader("Timestamp", String.valueOf(timestamp));
            // 发起请求
            //"{"code":0,"message":"success","data":{"authUrl":"http://35.71.161.248:30080/#/v1/Carrier?loginParams=%7B%22name%22:%22%E5%BC%A0%E4%B8%89%22,%22phone%22:%2217777777777%22,%22nonce%22:%229e5919ce63784c1cb70c061795034574%22,%22idcard%22:%22000000000000000000%22%7D","requestId":"9e5919ce63784c1cb70c061795034574"}}"
            CloseableHttpResponse httpResponse = HttpClients.createDefault().execute(httpPost);
            HttpEntity entity = httpResponse.getEntity();
            return EntityUtils.toString(entity);
        }

    public static String getRequest(String url, String appName, String sign, long timestamp) throws Exception {
        URI uri = new URI(url);
        HttpGet httpGet = new HttpGet(String.valueOf(uri));
        // 构建请求头
        httpGet.addHeader("ContentType", "application/json;charset=utf-8");
        httpGet.addHeader("AppName", appName);
        httpGet.addHeader("Sign", sign);
        httpGet.addHeader("SignVersion", "v2");
        httpGet.addHeader("Timestamp", String.valueOf(timestamp));
        // 发起请求
        //"{"code":0,"message":"success","data":{"authUrl":"http://35.71.161.248:30080/#/v1/Carrier?loginParams=%7B%22name%22:%22%E5%BC%A0%E4%B8%89%22,%22phone%22:%2217777777777%22,%22nonce%22:%229e5919ce63784c1cb70c061795034574%22,%22idcard%22:%22000000000000000000%22%7D","requestId":"9e5919ce63784c1cb70c061795034574"}}"
        CloseableHttpResponse httpResponse = HttpClients.createDefault().execute(httpGet);
        HttpEntity entity = httpResponse.getEntity();
        return EntityUtils.toString(entity);
    }

        public static void main(String[] args) {
            // 当前时间戳
            long timestamp = System.currentTimeMillis();
            // 请求体
            String body = "{\"idcard\":\"000000000000000000\",\"phone\":\"17777777777\",\"name\":\"张三\",\"userId\":\"17777777777\"}";
            String secret = "062690e5d3f24fefb0c1053635d4ab3d";
            // 签名
            String sign = signBodyWithAppSecret(JSONObject.toJSONString(body), timestamp, secret);
            /*Map body = new TreeMap();
            body.put("idcard", "000000000000000000");
            body.put("phone", "17777777777");
            body.put("name", "张三");
            body.put("userId", "17777777777");
            System.out.println(JSONObject.toJSONString(body));
            String sign = signBodyWithAppSecret(JSONObject.toJSONString(body), timestamp, secret);
            System.out.println(sign);
            Map head = new TreeMap();
            head.put("AppName", "483609995");
            head.put("Sign", sign);
            head.put("SignVersion", "v2");
            head.put("Timestamp", String.valueOf(timestamp));*/
            // 发起请求
            //String responseBody = HttpUtils.doPost("https://api.datariskcontrol.com/access-platform/carrier",head,body);
            try {
                String responseBody = request("https://api.datariskcontrol.com/access-platform/carrier", "483609995", sign, timestamp, body);
                System.out.println(responseBody);
            } catch (Exception exception) {
                exception.printStackTrace();
            }


        }
    }

