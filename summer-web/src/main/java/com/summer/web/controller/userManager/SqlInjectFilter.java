package com.summer.web.controller.userManager;


import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * sql注入过滤器
 */
@Slf4j
//@Component
//@WebFilter(urlPatterns = "/*", filterName = "SQLInjection", initParams = {@WebInitParam(name = "regx", value = "")})
public class SqlInjectFilter implements Filter {
    private static String regx = "(?:!)|(?:@)|(?:#)|(?:\\$)|(?:%)|(?:\\^)|(?:&)|(?:\\*)|(?:')|(?:--)|(?:;)|(?:\\\\)|" +
            "(?:select)|(?:update)|(?:and)|(?:or)|(?:delete)|(?:insert)|(?:trancate)|(?:char)|(?:into)|(?:substr)|" +
            "(?:ascii)|(?:declare)|(?:exec)|(?:count)|(?:master)|(?:drop)|(?:execute)";

    //表示忽略大小写
    private static Pattern sqlPattern = Pattern.compile(regx, Pattern.CASE_INSENSITIVE);


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        /**获取json数据*/
        Map<String, Object> jsonMap = getparamJSON((HttpServletRequest) servletRequest);
        if (null != jsonMap){
            Iterator itjson = jsonMap.entrySet().iterator();
            while (itjson.hasNext()) {
                Map.Entry entry = (Map.Entry) itjson.next();
                //校验字符串，数字不用校验
                Object valueJson = entry.getValue();
                if (null != valueJson){
                    String value = valueJson.toString();
                    if (isSqlValid(value)) {
                        log.error("---------输入的参数有非法字符！param={}",value);
                        response.sendError(-1, "请求参数错误！");
                        return;
                    }
                }

            }
        }

        /**获取url上的参数**/
        Map parametersMap = request.getParameterMap();
        Iterator it = parametersMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String[] value = (String[]) entry.getValue();
            for (int i = 0; i < value.length; i++) {
                if (isSqlValid(value[i])) {
                    log.error(value[i] + "---------输入的参数有非法字符！");
                    response.sendError(-1, "请求参数错误！");
                    return;
                }
            }
        }
        filterChain.doFilter(request, response);
    }

    private boolean isSqlValid(String str) {
        Matcher matcher = sqlPattern.matcher(str);
        if (matcher.find()) {
            log.info("参数存在非法字符，请确认：" + matcher.group());
            return true;
        }
        return false;
    }

    @Override
    public void destroy() {

    }


    /**
     * 获取json数据
     * @param request
     * @return
     */
   public Map<String,Object> getparamJSON(HttpServletRequest request) {
       Map<String,Object>  param= null;
        try {
            BufferedReader streamReader = new BufferedReader( new InputStreamReader(request.getInputStream(), "UTF-8"));
            StringBuilder responseStrBuilder = new StringBuilder();
            String inputStr;
            while ((inputStr = streamReader.readLine()) != null)
                responseStrBuilder.append(inputStr);
            param = JSONObject.parseObject(responseStrBuilder.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return param;
    }


}
