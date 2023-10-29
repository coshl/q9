package com.summer.service.sms.mch;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

import com.alibaba.fastjson.JSONObject;
import com.summer.util.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.alibaba.nacos.api.config.annotation.NacosValue;

import lombok.extern.slf4j.Slf4j;

/**
 * 第三方聚光发送短信
 *
 * @author Administrator
 */
@Slf4j
@Component
public class SmsJuGuangMsg {
    @Value(value = "${spring.profiles.active}")
    private String env;
    /**
     * 聚光短信发送url地址
     */
    @NacosValue(autoRefreshed = true, value = "${sms.service.juguang.url}")
    private String url;
    @NacosValue(autoRefreshed = true, value = "${sms.service.juguang.userid}")
    private String userid;
    @NacosValue(autoRefreshed = true, value = "${sms.service.juguang.account}")
    private String account;
    @NacosValue(autoRefreshed = true, value = "${sms.service.juguang.password}")
    private String password;

    public Boolean sendJuGuangSms(String phones, String title, String content) {
        if (StringUtils.containsNone(title, "【")) {
            title = "【" + title;
        }
        if (StringUtils.containsNone(title, "】")) {
            title = title + "】";
        }
        if (content.contains("#title#"))
            content = content.replace("#title#", title);
        else
            content = title + content;
        content = content + "退订回T";
        Map<String, Object> jsonObject = new HashMap();
        jsonObject.put("userid", userid);
        jsonObject.put("account", account);
        jsonObject.put("password", password);
        jsonObject.put("mobile", phones);
        jsonObject.put("content", content);
        jsonObject.put("sendTime", "");
        jsonObject.put("action", "send");
        jsonObject.put("extno", "");
        if (!StringUtils.equalsIgnoreCase(env, "dev")) {
            String res = HttpUtil.doPost(url, jsonObject);
            log.info(phones + "短信通知发送返回结果：{}", res);
            return true;
        }
        return false;
    }

    /**
     * <returnstatus>Success</returnstatus>
     * <message>ok</message>
     * <remainpoint>13087</remainpoint>
     * <taskID>448740</taskID>
     * <successCounts>1</successCounts></returnsms>
     */


    public Boolean sendHttpSms(String phones, String title, String content) {
        if (StringUtils.containsNone(title, "【")) {
            title = "【" + title;
        }
        if (StringUtils.containsNone(title, "】")) {
            title = title + "】";
        }
        if (content.contains("#title#"))
            content = content.replace("#title#", title);
        else
            content = title + content;
        content = content + "退订回T";
        Map<String, Object> jsonObject = new HashMap();
        //HTTP短信接口 网关地址 http://120.26.214.187:8001/sms 帐号 yzm1005 密码 123456
        jsonObject.put("userName", "yzm8008");
        jsonObject.put("content", content);
        ArrayList phoneList = new ArrayList<>();
        phoneList.add(phones);
        jsonObject.put("phoneList", phoneList);
        long timestamp = System.currentTimeMillis();
        jsonObject.put("timestamp", timestamp);
        //MD5(userName + content + timestamp + MD5(password))
        String sign = MD5.sign("yzm8008" + content + timestamp + MD5.sign("123456789", "", "utf8"), "", "utf8");
        jsonObject.put("sign", sign);
        jsonObject.put("sendTime", "");
        jsonObject.put("extcode", "");
        jsonObject.put("callData", "");

        String res = OkHttpUtils.builder().url("http://120.26.214.187:8001/sms/api/sendMessage")
                // 有参数的话添加参数，可多个
                .addParam("jsonObjectStringJson", JSONObject.toJSONString(jsonObject))
                // 也可以添加多个
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json;charset=utf-8")
                // 如果是true的话，会类似于postman中post提交方式的raw，用json的方式提交，不是表单
                // 如果是false的话传统的表单提交
                .post(true)
                .sync();
        log.info(phones + "Http短信通知发送返回结果：{}", res);
        return true;
        //return false;
    }



    //todo https://dx.ipyy.net/smsJson.aspx
    //【瓷行天下】您的验证码为:（@），5分钟有效！
    public Boolean sendHuaXinSms(String phones, String title, String code) throws Exception {
        String content = "您的验证码为:" + code + "，5分钟有效！【瓷行天下】";
        CloseableHttpClient httpclient = SSLClientSMS.createSSLClientDefault();
        String url = "https://dx.ipyy.net/sms.aspx";  //接口地址
        String userid = "OA00381";        //用户ID。
        String account = "OA00381";        //用户账号名
        String password = "5sufdhp2";        //接口密码
        //String mobile="15171410013";	     //多个手机号用逗号分隔
        //String text = content;
        String sendTime = "";
        String extno = "";        //扩展号，没有请留空
        HttpPost post = new HttpPost(url);
        post.setHeader("Content-type", "application/x-www-form-urlencoded");
        List nvps = new ArrayList();
        nvps.add(new BasicNameValuePair("action", "send"));
        nvps.add(new BasicNameValuePair("userid", userid));
        nvps.add(new BasicNameValuePair("account", account));
        nvps.add(new BasicNameValuePair("password", MD5SMS.GetMD5Code(password)));
        nvps.add(new BasicNameValuePair("mobile", phones));
        nvps.add(new BasicNameValuePair("content", content));
        nvps.add(new BasicNameValuePair("sendTime", sendTime));
        nvps.add(new BasicNameValuePair("extno", extno));
        post.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
        HttpResponse response = httpclient.execute(post);
        HttpEntity entity = response.getEntity();
        String returnString = EntityUtils.toString(entity);
        //System.out.println(returnString);
        EntityUtils.consume(entity);
        log.info(phones + "华信云短信通知发送返回结果：{}", returnString);
        return true;
    }

    public Boolean sendxinmaoSms(String phones, String title, String code) throws Exception {
        String content = title+"您的验证码为:" + code + "，5分钟有效！";
        Map<String, String> jsonObject = new HashMap();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String format = df.format(new Date());
        jsonObject.put("userid", "8473");//企业id
        jsonObject.put("timestamp", format);//时间戳
        jsonObject.put("mobile", phones);//号码
        String calculate = HttpUtil.calculate("rongyirongyi123" + format);//账号：rongyifenqi 密码：rongyifenqi123
        jsonObject.put("sign", calculate);//签名
        jsonObject.put("content", content);//URLEncoder.encode(content, "utf-8")
        jsonObject.put("action", "send");//发送任务命令
        jsonObject.put("sendTime", "");// 定时发送时间
        jsonObject.put("extno", "");//扩展子号
        log.info(phones + "新猫短信请求结果：{}", jsonObject);
        String post = HttpUtil.post("http://112.74.163.13:8888/v2sms.aspx", jsonObject);
        log.info(phones + "新猫短信通知发送返回结果：{}", post);
        return true;
    }

    public static void main(String[] args) {
        String content = "【融亿信息】您的验证码为:" + 5689 + "，5分钟有效！";
        Map<String, String> jsonObject = new HashMap();
        //HTTP短信接口 网关地址 http://120.26.214.187:8001/sms 帐号 yzm1005 密码 123456
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String format = df.format(new Date());
        jsonObject.put("userid", "8473");//企业id
        jsonObject.put("timestamp", format);//时间戳
        jsonObject.put("mobile", "17858857869");//号码
        String calculate = HttpUtil.calculate("rongyirongyi123" + format);//账号：rongyifenqi 密码：rongyifenqi123
        jsonObject.put("sign", calculate);//签名
//        jsonObject.put("content", URLEncoder.encode(content, "utf-8"));//URLEncoder.encode(content, "utf-8")
        jsonObject.put("content", content);//URLEncoder.encode(content, "utf-8")
        jsonObject.put("action", "send");//发送任务命令
        jsonObject.put("sendTime", "");// 定时发送时间
        jsonObject.put("extno", "");//扩展子号
        System.out.println(jsonObject);
        String post = HttpUtil.post("http://112.74.163.13:8888/v2sms.aspx", jsonObject);
        String s = XmlUtil.XmlToJson(post);
        log.info(  "华信云短信通知发送返回结果：{}", s);
    }

//    public static void main(String[] args) throws Exception {
//        String content = "您的验证码为:" +"8963" + "，5分钟有效！【瓷行天下】";
//        CloseableHttpClient httpclient = SSLClientSMS.createSSLClientDefault();
//        String url ="https://dx.ipyy.net/sms.aspx";  //接口地址
//        String userid="OA00381";		//用户ID。
//        String account="OA00381";		//用户账号名
//        String password="5sufdhp2";		//接口密码
//        String mobile="15171410013";	     //多个手机号用逗号分隔
//        String text = content;
//        String sendTime="";
//        String extno="";		//扩展号，没有请留空
//        HttpPost post = new HttpPost(url);
//        post.setHeader("Content-type", "application/x-www-form-urlencoded");
//        List nvps = new ArrayList();
//        nvps.add(new BasicNameValuePair("action","send"));
//        nvps.add(new BasicNameValuePair("userid", userid));
//        nvps.add(new BasicNameValuePair("account", account));
//        nvps.add(new BasicNameValuePair("password", MD5SMS.GetMD5Code(password)));
//        nvps.add(new BasicNameValuePair("mobile", mobile));
//        nvps.add(new BasicNameValuePair("content", text));
//        nvps.add(new BasicNameValuePair("sendTime", sendTime));
//        nvps.add(new BasicNameValuePair("extno", extno));
//        post.setEntity(new UrlEncodedFormEntity(nvps,"utf-8"));
//        HttpResponse response = httpclient.execute(post);
//        HttpEntity entity = response.getEntity();
//        String returnString=EntityUtils.toString(entity);
//        System.out.println(returnString);
//        EntityUtils.consume(entity);
//    }

}
