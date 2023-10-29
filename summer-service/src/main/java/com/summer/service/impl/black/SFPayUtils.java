package com.summer.service.impl.black;


import okhttp3.*;
import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SFPayUtils {
    private static OkHttpClient client = new OkHttpClient();


    public static String post(String url, Map params) {
        FormBody.Builder builder = new FormBody.Builder();
        for (Object key : params.keySet()) {
            builder.add(key.toString(), params.get(key).toString());
        }

        RequestBody formBody = builder.build();
        Request request = new Request.Builder().url(url).post(formBody).build();
        String result = null;
        try {
            Response response = client.newCall(request).execute();
            int code = response.code();
            System.err.println("状态码:" + code);
            result = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String get(String url) {
        Request request = new Request.Builder().url(url).build();
        String json = null;
        Response response = null;
        try {
            response = client.newCall(request).execute();
            json = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }



        public static Boolean reg(String phone) {
            String regex = "^((13[0-9])|(14[0-9])|(15[0-9])|(16[0-9])|(17[0-9])|(18[0-9])|(19[0-9]))" +
                    "\\d{8}$";
                Pattern p = Pattern.compile(regex);
                Matcher m = p.matcher(phone);
                boolean isMatch = m.matches();
                if (!isMatch) {
                    return false;
                }
            return true;
        }




}
