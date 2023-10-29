package com.summer.service.impl.black;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.summer.util.OkHttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class JuGuangApi {
    @Value("${sms.service.appName}")
    private String name;

    private static final String host = "http://47.74.48.135:81";
    //private static final String host = "http://47.57.187.27:81";
    //   /user/black/queryBlack
    //  {
    //  "code": 0,
    //  "msg": "success",
    //  "data": false
    //}
    public static Boolean queryBlack(String idcardNo,String telephone){
        Map map  = new HashMap();
        map.put("idcardNo",idcardNo);
        map.put("telephone",telephone);
        String result = HttpUtils.doGet(host+"/user/black/queryBlack", map);
        JSONObject jsonObject = JSON.parseObject(result);
        return jsonObject.getBoolean("data");
    }

    //   /data/upload/uploadJointDebt
    public void uploadJointDebt(List<Map<String, String>> userList, Integer type, Integer overdue) {
        List<Object> data = new ArrayList<>();
        for (Map<String, String> order : userList) {
            Map map = new HashMap();
            map.put("idcardNo",order.get("idNo"));
            log.info("上传name===================="+name);
            if(StringUtils.isNotBlank(name)){
                map.put("platformName",name);
            }else {
                map.put("platformName","巨时惠");
            }
            map.put("overdue",overdue);
            map.put("name",order.get("name"));
            map.put("status", type);
            map.put("telephone",order.get("phone"));
            data.add(JSON.toJSON(map));
        }
        Map dataMap = new HashMap();
        dataMap.put("data",data);
        //addHeader("Content-Type", "application/json; charset=utf-8")
        String result = OkHttpUtils.builder().url(host+"/data/upload/uploadJointDebt").addParam("json",JSON.toJSONString(dataMap)).post(false).sync();
        System.out.println("聚光公债上传======================================================"+result);
        //String result = HttpUtils.doPostSSL(host+"/data/upload/uploadJointDebt", null, paramMap);
        //JSONObject jsonObject = JSON.parseObject(result);
        //return jsonObject.getBoolean("data");
    }

    //   /data/upload/getJoinDebt
    public static String getJoinDebt(String idcardNo){
        Map map  = new HashMap();
        map.put("idcardNo",idcardNo);
        String result = HttpUtils.doGet(host+"/data/upload/getJoinDebt", map);
        if(result.isEmpty()){
            return "";
        }
        JSONObject jsonObject = JSON.parseObject(result);
        JSONArray JsonArray = JSON.parseArray(jsonObject.getString("data"));
        //[{"onGoingOrder":0,"sumOverdue":1,"todayApply":0,"todayRepay":0,"repayPlatform":0,"refuseOrder":0,"telephone":"13508042328","sumApply":0,"idcardNo":"511026197802140029","sumLoan":0,"todayBecame":0,"loanPlatform":0,"sumRenew":1,"todayLoan":0,"sumPlatform":1,"name":"周霁","sumRepay":0,"overduePlatform":1}]
        return JsonArray.get(0).toString();
    }

    //   /payOrder/getPayOrder
    public static String getPayOrder(String name){
        Map map  = new HashMap();
        map.put("name",name);
        String result = HttpUtils.doGet(host+"/payOrder/getPayOrder", map);
        if(result.isEmpty()){
            return "";
        }
        return result;
    }

    //   /user/black/insertBlack
    public static void insertBlack(List<Map<String, String>> userList){
        List<Object> data = new ArrayList<>();
        for (Map<String, String> order : userList) {
            Map map = new HashMap();
            map.put("idcardNo",order.get("idNo"));
            map.put("name",order.get("name"));
            map.put("telephone",order.get("phone"));
            data.add(JSON.toJSON(map));
        }
        Map dataMap = new HashMap();
        dataMap.put("data",data);
        //addHeader("Content-Type", "application/json; charset=utf-8").
        String result = OkHttpUtils.builder().url(host+"/user/black/insertBlack").
                addParam("json",JSON.toJSONString(dataMap)).post(false).sync();
        System.out.println("聚光黑名单上传======================================================"+result);
    }


    /* todo 黑名单
    public static void main(String[] args) {
        List<Object> data = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("idcardNo", "222222222222");
        map.put("name", "fs");
        map.put("telephone", "11111111111");
        data.add(JSON.toJSON(map));
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("data", data);
        insertBlack(dataMap);
        System.out.println("ok");
    }*/

    public static void main(String[] args) {
        //System.out.println(getJoinDebt("511026197802140029"));
        List<Map<String, String>> userList = new ArrayList<>();
        Map map = new HashMap();
        map.put("name","test");
        map.put("phone","15037203227");
        map.put("idNo","210122198010090011");
        userList.add(map);
        new JuGuangApi().uploadJointDebt(userList,99,0);
        System.out.println("=====");
    }
}
