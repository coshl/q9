package com.summer.service.impl.thirdpart;

import com.alibaba.fastjson.JSON;
import com.summer.service.impl.black.HttpClientUtils;
import com.summer.util.OkHttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Component
@Slf4j
public class RadarApi {

//http://122.226.223.231:5555/ext/v2/queryRadar
//mercode ： me-3NeuEpsHDznhz5tNWTCUkcNyM4CtEybGPq
//mercode ：
public  static  String getRadar(String mobile,String idCard,String name){

    com.alibaba.fastjson.JSONObject request = new com.alibaba.fastjson.JSONObject();
    request.put("mobile", mobile);//商户
    request.put("idCard", idCard);//应用ID
    request.put("name", name);//商户订单号
    request.put("merCode", "me-1e73ec5fd48828c7ed5dd7b6e8fb8");//支付方式
    Map map=new HashMap();
    map.put("appCode","b809fe01f3c64c5284c06ff3c1fabe50");
    String result = HttpClientUtils.postSsl("http://122.226.223.231:5555/ext/v2/queryRadar", request.toString(),map);

//    String result = OkHttpUtils.builder().url("http://122.226.223.231:5555/ext/v2/queryRadar").
//            addHeader("Content-Type", "application/json; charset=utf-8").
//            addHeader("appCode", "b809fe01f3c64c5284c06ff3c1fabe50").
//            addParam("mobile", mobile).
//            addParam("idCard", idCard).
//            addParam("name", name).
//            addParam("merCode", "me-1e73ec5fd48828c7ed5dd7b6e8fb8").
//            post(true).sync();
    //System.out.println("getRadar==========="+result);
    log.info("五花共债getRadar==========="+result);
    return result;

}

    public static void main(String[] args) {
        String result = OkHttpUtils.builder().url("http://122.226.223.231:5555/ext/v2/queryRadar").
                addHeader("Content-Type", "application/json; charset=utf-8").
                addHeader("appCode", "b809fe01f3c64c5284c06ff3c1fabe50").
                addParam("mobile", "18640356167").
                addParam("idCard", "210114199702195425").
                addParam("name", "白桂莲").
                addParam("merCode", "me-1e73ec5fd48828c7ed5dd7b6e8fb8").
                post(true).sync();
        System.out.println(result);
    }


}
