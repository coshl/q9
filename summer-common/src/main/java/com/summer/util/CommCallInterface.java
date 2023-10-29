package com.summer.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * @author Administrator 公共调用接口
 */
public class CommCallInterface {
    private static Logger logger = LoggerFactory.getLogger(CommCallInterface.class);
    public static CommCallInterface commCallInterface;

    public static CommCallInterface getInstance() {
        if (commCallInterface == null) {
            commCallInterface = new CommCallInterface();
        }
        return commCallInterface;
    }

    public static String commHttp(String reqPath, String param, String method) {
        URL url;
        String result = "";
        logger.info("===========接口信息============");
        logger.info("reqPath===========" + reqPath);
        logger.info("param===========" + param);
        logger.info("method===========" + method);
        try {

            url = new URL(reqPath);
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setRequestProperty("Content-Type", "application/json");
            http.setDoInput(true);
            http.setDoOutput(true);
            /*
             * System.setProperty("sun.net.client.defaultConnectTimeout",
             * "30000");// 连接超时30秒
             * System.setProperty("sun.net.client.defaultReadTimeout", "30000");
             * // 读取超时30秒
             */

            http.setConnectTimeout(180000);// 连接超时 单位毫秒
            http.setReadTimeout(180000);// 读取超时 单位毫秒
            http.connect();
            if (null != param) {
                OutputStream os = http.getOutputStream();
                os.write(param.getBytes("UTF-8"));// 传入参数
                os.flush();
                os.close();
            }
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(http.getInputStream(), "UTF-8"));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result += line;
            }
//            logger.info("result==========="+result);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            logger.info("result（MalformedURLException）===========" + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            logger.info("result（IOException）===========" + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("result（Exception）===========" + e.getMessage());
        }
        return result;
    }
}
