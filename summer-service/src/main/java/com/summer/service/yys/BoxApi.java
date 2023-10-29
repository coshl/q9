package com.summer.service.yys;

import com.alibaba.fastjson.JSONObject;
import com.summer.service.card.HttpUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class BoxApi {
    public static final String appName = "483609995";
    public static final String secret = "062690e5d3f24fefb0c1053635d4ab3d";
    public static final String host = "https://api.datariskcontrol.com";
    public static final String method = "POST";

    public static String boxGetUrl(Map bodys){
        String  path = "/access-platform/carrier";
        // 当前时间戳
        long timestamp = System.currentTimeMillis();
        //签名
        String sign = Box.signBodyWithAppSecret(JSONObject.toJSONString(bodys), timestamp, secret);
        try {
            String result = Box.request(host+path, appName, sign, timestamp, JSONObject.toJSONString(bodys));
            System.out.println("box请求结果==========================================="+result);
            if(result.isEmpty()||"Unauthorized".equals(result)){
                return null;
            }
            JSONObject jsonResult = JSONObject.parseObject(result);
            if(jsonResult.containsKey("code")&&jsonResult.getString("message").equals("success")){
                return jsonResult.getJSONObject("data").toJSONString();
            }
            //System.out.println(result.concat("&redirectUrl=www.baidu.com"));
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

   /* {
        "code": 0,
            "message": "success",
            "data": true   或者 false
    }*/
    public static String boxResetCallback(Map bodys){
        String path = "/access-platform/carrier/reset-callback-state";
        // 当前时间戳
        long timestamp = System.currentTimeMillis();
        //签名
        String sign = Box.signBodyWithAppSecret(JSONObject.toJSONString(bodys), timestamp, secret);
        try {
            String result = Box.request(host+path, appName, sign, timestamp, JSONObject.toJSONString(bodys));

            return result;
            //System.out.println(result.concat("&redirectUrl=www.baidu.com"));
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /*{
        "code": 0,
            "message": "success",
            "requestId": "f00eb48f6f9745ff94520968e1a4b685",
            "time": "2021-10-23 23:30:26",
            "data": {
        "task_id": "18d4021c-c684-11e9-a893-7675bfa22ea1",
                "time": "2021-10-23 23:30:26",
                "version": "1.26",
                "status": 0
    }
    }*/
    public static String boxQueryModel(Map bodys){
        String path = "/model-score/query/simple-url/1.26";
        // 当前时间戳
        long timestamp = System.currentTimeMillis();
        //签名
        String sign = Box.signBodyWithAppSecret(JSONObject.toJSONString(bodys), timestamp, secret);
        try {
            String result = Box.request(host + path, appName, sign, timestamp, JSONObject.toJSONString(bodys));
            return result;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

   /* {
        "code": -1,
            "message": "模型分计算中",
            "requestId": "ef6f936a2b6d47fba18f623a3a82367c",
            "time": "2021-08-24 23:57:12"
    }*/
   /*{
       "code": 0,
           "message": "success",
           "requestId": "ef6f936a2b6d47fba18f623a3a82367c",
           "time": "2021-08-24 23:57:12",
           "data": {
               "score": 495,
               "task_id": "18d4021c-c684-11e9-a893-7675bfa22ea1",
               "time": "2019-08-24 23:30:27",
               "version": "1.26"
   }
   }*/
    public static String boxQueryScore(String taskId){
        String path = "/model-score/async-query-result/1.26?taskId=";
        // 当前时间戳
        long timestamp = System.currentTimeMillis();
        //签名
        String sign = Box.signBodyWithAppSecret("", timestamp, secret);
        try {
            Thread.sleep(3000);
            String result = Box.getRequest(host + path + taskId , appName, sign, timestamp);
            System.out.println("BOX模型分======================="+result);
            if(!result.isEmpty()){
                JSONObject resultJson = JSONObject.parseObject(result);
                if(resultJson.containsKey("code")&&resultJson.getIntValue("code")==0){
                    String score = String.valueOf(resultJson.getJSONObject("data").getIntValue("score"));
                    return score;
                }else {
                    return "0";
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return "0";
    }

    public static String boxJoinDebt(Map bodys){
        String path = "/my-debtors/debtor/statistic";
        // 当前时间戳
        long timestamp = System.currentTimeMillis();
        //签名
        String sign = Box.signBodyWithAppSecret(JSONObject.toJSONString(bodys), timestamp, secret);
        try {
            String result = Box.request(host + path, appName, sign, timestamp, JSONObject.toJSONString(bodys));
            return result;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /*接口文档地址
    https://docs.datariskcontrol.com/
    用户名：view
    密码：aiw4ahf0equuphash9t*/
    public static void main(String[] args) {
        Map bodys = new HashMap();
        //bodys.put("userId", "9999");
        //bodys.put("name", "阮景辉");
        bodys.put("idNo", "130527198910130326");
        bodys.put("phone", "18632960161");
        //System.out.println(boxQueryScore("95e4d13e-c48b-11ec-9181-9a34483c5e7d"));

        System.out.println(boxJoinDebt(bodys));
    }
}
