package com.summer.service.fk;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.summer.service.impl.black.HttpClientUtils;
import com.summer.util.AesUtils;
import com.summer.util.OkHttpUtils;
import com.summer.util.encrypt.AESDecrypt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class QingTianZhuApi {

    //https://one.linkingv.cn/api/risk/interface
    private static final String host = "https://one.linkingv.cn/api/risk/interface";
    private static final String merchantId = "13624876933535072421986409621224";
    private static final String aesKey = "QeCExKpWWtXEqwjQ";
    private static final String productId = "lksProduct1022";

    //测试环境
    //请求地址: https://one.linkingv.cn/test/risk/interface
    //公司账号(institution_id): test123456test
    //aes密钥: 1111111111111111
    /*请求地址: 请求地址: https://one.linkingv.cn/api/risk/interface
    公司账号(institution_id): 13624876933535072421986409621224
    aes密钥: QeCExKpWWtXEqwjQ
    */
    /*// 明⽂
        JSONObject data = new JSONObject();
        data.put("name", "张丽红");
        data.put("phone", "15002489056");
        data.put("identNumber", "211225197609180620");
        data.put("encryption", "plaintext");
        // 转json字符串
        String dataStr = data.toString();*/
    public  String leiDa(Map user){
        user.put("encryption", "plaintext");
        // AES加密
        String aesStr = AesUtils.encrypt(JSON.toJSONString(user), aesKey);
        JSONObject param = new JSONObject();
        param.put("merchantId", merchantId);
        param.put("productId", productId);
        param.put("txnId", "559d176a");
        param.put("riskData", aesStr);

        String result = HttpClientUtils.postSsl(host, param.toString(), null);
//        String result = OkHttpUtils.builder().
//                url(host).
//                addParam("jsonObjectStringJson", param.toJSONString()).
//                post(true).sync();
        return result;
    }

    public static void main(String[] args) {
        Map params = new HashMap<>();
        params.put("name", "白桂莲");
        params.put("phone", "18640356167");
        params.put("identNumber", "210114199702195425");
        params.put("encryption", "plaintext");
        // AES加密
        String aesStr = AesUtils.encrypt(JSON.toJSONString(params), aesKey);
        JSONObject param = new JSONObject();
        param.put("merchantId", merchantId);
        param.put("productId", productId);
        param.put("txnId", "559d176a");
        param.put("riskData", aesStr);
        String s = HttpClientUtils.postSsl(host, param.toString(), null);
        System.out.println(s);
    }


    public  String tanZhen(Map user){
        user.put("encryption", "plaintext");
        String aesStr = AesUtils.encrypt(JSON.toJSONString(user), aesKey);
        JSONObject param = new JSONObject();
        param.put("merchantId", merchantId);
        param.put("productId", productId);
        param.put("txnId", "7da49d68649");
        param.put("riskData", aesStr);
        String result = HttpClientUtils.postSsl(host, param.toString(), null);
//        String result = OkHttpUtils.builder().
//                url(host).
//                addParam("jsonObjectStringJson", param.toJSONString()).
//                post(true).sync();
        return result;
    }


    public  String duoTou(Map user){
        String aesStr = AesUtils.encrypt(JSON.toJSONString(user), aesKey);
        JSONObject param = new JSONObject();
        param.put("merchantId", merchantId);
        param.put("productId", productId);
        param.put("txnId", "cd04e520");
        param.put("riskData", aesStr);
        String result = HttpClientUtils.postSsl(host, param.toString(), null);
//        String result = OkHttpUtils.builder().
//                url(host).
//                addParam("jsonObjectStringJson", param.toJSONString()).
//                post(true).sync();
        return result;
    }

    public  String black(Map user){
        // AES加密
        String aesStr = AesUtils.encrypt(JSON.toJSONString(user), aesKey);
        JSONObject param = new JSONObject();
        param.put("merchantId", merchantId);
        param.put("productId", productId);
        param.put("txnId", "14d74f6a");
        param.put("riskData", aesStr);
        String result = HttpClientUtils.postSsl(host, param.toString(), null);
//        String result = OkHttpUtils.builder().
//                url(host).
//                addParam("jsonObjectStringJson", param.toJSONString()).
//                post(true).sync();
        return result;
    }

   /* public static void main(String[] args) {
        // 明⽂
        Map data = new HashMap();
        data.put("name", "詹鹏");
        data.put("phone", "18386684519");
        data.put("identNumber", "522627199310224411");
        System.out.println(black(data));
    }*/
}
