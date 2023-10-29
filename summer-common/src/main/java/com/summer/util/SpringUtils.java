package com.summer.util;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * json 格式输入 公用
 *
 * @author cjx SpringUtils.java 2013-12-6
 */
public class SpringUtils {

    // header 常量定义
    private static final String ENCODING_PREFIX = "encoding";
    private static final String NOCACHE_PREFIX = "no-cache";
    private static final boolean NOCACHE_DEFAULT = true;
    private static Logger logger = LoggerFactory.getLogger(SpringUtils.class);

    // 绕过jsp直接输出文本的函数 //

    /**
     * 直接输出内容的简便函数.
     *
     */
    public static void render(HttpServletResponse response,
                              final String contentType, final String content,
                              final String... headers) {
        try {
            // 分析headers参数
            String encoding = Constant.UTF8;
            boolean noCache = NOCACHE_DEFAULT;
            for (String header : headers) {
                String headerName = StringUtils.substringBefore(header, ":");
                String headerValue = StringUtils.substringAfter(header, ":");

                if (StringUtils.equalsIgnoreCase(headerName, ENCODING_PREFIX)) {
                    encoding = headerValue;
                } else if (StringUtils.equalsIgnoreCase(headerName,
                        NOCACHE_PREFIX)) {
                    noCache = Boolean.parseBoolean(headerValue);
                } else
                    throw new IllegalArgumentException(headerName
                            + "不是一个合法的header类型");
            }
            // 设置headers参数
            String fullContentType = contentType + ";charset=" + encoding;
            response.setContentType(fullContentType);
            if (noCache) {
                response.setHeader("Pragma", "No-cache");
                response.setHeader("Cache-Control", "no-cache");
                response.setDateHeader("Expires", 0);
            }

            response.getWriter().write(content);
            response.getWriter().flush();

        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 直接输出文本.
     *
     */
    public static void renderText(HttpServletResponse response,
                                  final String text, final String... headers) {
        render(response, "text/plain", text, headers);
    }

    /**
     * 直接输出HTML.
     *
     */
    public static void renderHtml(HttpServletResponse response,
                                  final String html, final String... headers) {
        render(response, "text/html", html, headers);
    }

    /**
     * 直接输出XML.
     *
     */
    public static void renderXml(HttpServletResponse response,
                                 final String xml, final String... headers) {
        render(response, "text/xml", xml, headers);
    }

    /**
     * 直接输出JSON格式
     *
     * @param string
     *            json字符串.
     */
    public static void renderJson(HttpServletResponse response,
                                  final String string, final String... headers) {
        render(response, "application/json", string, headers);
    }

    /**
     * 直接输出JSON. Java对象,将被转化为json字符串.
     *
     */
    public static void renderJson(HttpServletResponse response,
                                  final Object object, final String... headers) {
        String jsonString = fromObject(object);
        renderJson(response, jsonString, headers);
    }

    /**
     * 对象转换json格式字符串
     *
     * @param obj
     * @return
     */
    public static String fromObject(Object obj) {
        return JSONObject.toJSONString(obj);
    }

    public static void renderJsonResult(HttpServletResponse response,
                                        String code, String message) {
        ServiceResult rest = new ServiceResult(code, message);

        SpringUtils.renderJson(response, rest);
    }
    /**
     * 根据身份证号取年龄
     * @param birthday
     * @param format
     * @return
     */
    public static int calculateAge(String birthday,String format){
        int age=0;
        try {
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            Date today = sdf.parse(birthday);
            age=date.getYear()-today.getYear();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return age;
    }
}
