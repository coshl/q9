package com.summer.util;

import cn.hutool.json.JSONObject;
import cn.hutool.json.XML;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class XmlUtil {

    public static String XmlToJson(String xml){
        JSONObject jsonObject = XML.toJSONObject(xml);
        //数据前标签
        String request = jsonObject.getJSONObject("soap:Envelope").getJSONObject("soap:Body").getJSONObject("a:queryResponse").getJSONObject("request").toString();
        return request;
    }

}
