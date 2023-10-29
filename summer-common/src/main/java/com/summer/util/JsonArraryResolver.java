package com.summer.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.Set;

/**
 * JSON解析工具类
 */
public class JsonArraryResolver {

    public static Integer resolverInteger(int i, JSONArray createArrayt, String property) {
        return JSONObject.parseObject(JSONObject.toJSONString(createArrayt.get(i))).getInteger(property);
    }

    /**
     * 解析规则里地区的JSON的方法
     *
     * @param data
     * @param keyString
     * @param valString
     * @return
     */
    public static Boolean handJson(JSONObject data, String keyString, String valString) {
        Set<String> keySet = data.keySet();
        for (String key : keySet) {
            Object object = data.get(key);
            if (object instanceof JSONObject) {
                JSONObject dataJsonObject = (JSONObject) object;
                Set<String> keySet2 = dataJsonObject.keySet();
                for (String chilKey : keySet2) {
                    if (chilKey.equals(keyString)) {
                        if ("".equals(dataJsonObject.get(keyString).toString()) || dataJsonObject.get(keyString) == null) {
                            return true;
                        } else {
                            String val = dataJsonObject.get(keyString).toString();
                            String[] split = val.split("\\|");
                            for (String string : split) {
                                if (valString.equals(string)) {
                                    return true;
                                }
                            }
                        }
                    }
                }

            } else if (object instanceof JSONArray) {
                JSONArray dataJsonJSONArray = (JSONArray) object;
                for (Object object2 : dataJsonJSONArray) {
                    handJson((JSONObject) object2, keyString, valString);
                }
            }

        }
        return false;
    }
}
