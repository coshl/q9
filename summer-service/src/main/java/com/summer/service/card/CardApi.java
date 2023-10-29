package com.summer.service.card;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.util.HashMap;
import java.util.Map;

public class CardApi {

    //{"code":"0000","msg":"处理成功","result":"2","resultMsg":"认证信息不匹配","requestId":"20210820020152296_8owjb1c4"}
    //  "code": "I005",
    //  "msg": "业务异常 ",
    //  "result": "-1",
    //  "resultMsg": "请求身份证号不标准：身份证号为空或者不符合身份证校验规范",
    //  "requestId": "20200720113810361_le805776"
    //}
    public static String aliCard(HashMap map){
        String host = "http://ediscard.market.alicloudapi.com";
        String path = "/comms/zszn/nameIDCardPhoneAccountVerify";
        String method = "POST";
        String appcode = "967694ef0d7e44e6b7553f884e35a89d";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        //根据API的要求，定义相对应的Content-Type
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        Map<String, String> querys = new HashMap<String, String>();
        Map<String, String> bodys = new HashMap<String, String>();
        bodys.put("accountNo", map.get("card_number").toString());
        bodys.put("idName", map.get("name").toString());
        bodys.put("idNumber", map.get("id_number").toString());
        bodys.put("mobile", map.get("phone_number").toString());
        try {
            /**
             * 重要提示如下:
             * HttpUtils请从
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
             * 下载
             * 相应的依赖请参照
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
             */
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            //获取response的body
            String result = EntityUtils.toString(response.getEntity());
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



}
