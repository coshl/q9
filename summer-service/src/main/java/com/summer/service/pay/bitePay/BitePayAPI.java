package com.summer.service.pay.bitePay;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.summer.service.pay.bitePay.dto.*;
import com.summer.util.OkHttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * rest 接口调用工具类
 *
 *
 */
@Component
@Slf4j
public class BitePayAPI {

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 是否限制重试(成功则不重新发起请求), true: 不限制，false: 限制
     */
    private static final Boolean constraint = Boolean.FALSE;

    /**
     * 重试次数
     */
    private static final int TRY_NUM = 3;

    /**
     * 出账类下单
     *
     * @param paramDTO 参数：不包含sign, type, appid, appkey
     * @return
     */
    public OutAccountResultDTO OutAccount(OutAccountParamDTO paramDTO) {
        String url = paramDTO.getWeburl();
        // 返回信息
        OutAccountResultDTO resultDTO = null;
        // 当前请求接口次数
        int tryCount = 1;

        // sign用MD5加密
        String sign = this.getSign(paramDTO);
        paramDTO.setSign(sign);
        // 身份证号处理
        paramDTO.setPidnum(this.getPidnum(paramDTO.getPidnum()));
        // 参数转json保留下划线, 不包含apikey
        JSONObject paramJson = this.paramToJson(paramDTO);
        // 设置Headers
        HttpHeaders headers = getHttpHeaders();
        url = url + "?" + toUrlParams(paramJson) + "&key=" + paramDTO.getApikey();
        String paramJsonStr = JSONUtil.toJsonStr(paramJson);
        while (constraint || tryCount <= TRY_NUM) {
            try {
                // apikey不转json
                paramDTO.setApikey(null);
                // 开始POST请求
                //String resultStr = this.restPost(url, paramJsonStr, headers);
                String resultStr = OkHttpUtils.builder().url(url)
                        // 有参数的话添加参数，可多个
                        .addParam("jsonObjectStringJson", paramJsonStr)
                        // 也可以添加多个
                        .addHeader("Content-Type", "application/json; charset=utf-8")
                        // 如果是true的话，会类似于postman中post提交方式的raw，用json的方式提交，不是表单
                        // 如果是false的话传统的表单提交
                        .post(true)
                        .sync();
                resultDTO = JSONUtil.toBean(resultStr, OutAccountResultDTO.class);
                // 请求成功标志
                String successStatus = "1";
                if (Objects.nonNull(resultDTO) && successStatus.equals(resultDTO.getStatus())) {
                    log.info("出账类下单接口: 第{}次请求成功，返回数据为：{}", tryCount, resultStr);
                    // 请求成功
                    return resultDTO;
                } else {
                    log.error("出账类下单接口: 第{}次请求失败，返回数据为：{}", tryCount, resultStr);
                }
            } catch (Exception e) {
                log.error("出账类下单接口: 第{}次请求失败，失败原因：{}", tryCount, e.getMessage());
            }
            tryCount ++;
        }
        return resultDTO;
    }



    /**
     * 入账类下单
     *
     * @param paramDTO 参数：不包含sign, type, appid, appkey
     * @return
     */
    public InAccountResultDTO InAccount(InAccountParamDTO paramDTO) {
        String url = paramDTO.getWeburl();
        // 返回信息
        InAccountResultDTO resultDTO = null;
        // 当前请求接口次数
        int tryCount = 1;

        // sign用MD5加密
        String sign = this.getSign(paramDTO);
        paramDTO.setSign(sign);
        // 身份证号处理
        paramDTO.setPidnum(this.getPidnum(paramDTO.getPidnum()));
        // 参数转json保留下划线, 不包含apikey
        JSONObject paramJson = this.paramToJson(paramDTO);

        // 设置Headers
        HttpHeaders headers = getHttpHeaders();

        url = url + "?" + toUrlParams(paramJson) + "&key=" + paramDTO.getApikey();
        String paramJsonStr = JSONUtil.toJsonStr(paramJson);
        while (constraint || tryCount <= TRY_NUM) {
            try {
                // apikey不转json
                paramDTO.setApikey(null);
                // 开始POST请求
                //String resultStr = this.restPost(url, paramJsonStr, headers);
                String resultStr = OkHttpUtils.builder().url(url)
                        // 有参数的话添加参数，可多个
                        .addParam("jsonObjectStringJson", paramJsonStr)
                        // 也可以添加多个
                        .addHeader("Content-Type", "application/json; charset=utf-8")
                        // 如果是true的话，会类似于postman中post提交方式的raw，用json的方式提交，不是表单
                        // 如果是false的话传统的表单提交
                        .post(true)
                        .sync();
                resultDTO = JSONUtil.toBean(resultStr, InAccountResultDTO.class);
                // 请求成功标志
                String successStatus = "1";
                if (Objects.nonNull(resultDTO) && successStatus.equals(resultDTO.getStatus())) {
                    log.info("入账类下单接口: 第{}次请求成功，返回数据为：{}", tryCount, resultStr);
                    // 请求成功
                    return resultDTO;
                } else {
                    log.error("入账类下单接口: 第{}次请求失败，返回数据为：{}", tryCount, resultStr);
                }
            } catch (Exception e) {
                log.error("入账类下单接口: 第{}次请求失败，失败原因：{}", tryCount, e.getMessage());
            }
            tryCount ++;
        }
        return resultDTO;
    }

    /**
     * 入账还款查询
     *
     * @param paramDTO 参数：不包含sign, type, appid, appkey
     * @return
     */
    public SearchInAccountResultDTO SearchInAccount(SearchInAccountParamDTO paramDTO) {
        String url = paramDTO.getWeburl();
        // 返回信息
        SearchInAccountResultDTO resultDTO = null;
        // 当前请求接口次数
        int tryCount = 1;

        // sign用MD5加密
        String sign = this.getSign(paramDTO);
        paramDTO.setSign(sign);
        // 参数转json保留下划线, 不包含apikey
        JSONObject paramJson = this.paramToJson(paramDTO);

        // 设置Headers
        HttpHeaders headers = getHttpHeaders();

        url = url + "?" + toUrlParams(paramJson) + "&key=" + paramDTO.getApikey();
        String paramJsonStr = JSONUtil.toJsonStr(paramJson);
        while (constraint || tryCount <= TRY_NUM) {
            try {
                // apikey不转json
                paramDTO.setApikey(null);
                // 开始POST请求
                String resultStr = this.restPost(url, paramJsonStr, headers);
                resultDTO = JSONUtil.toBean(resultStr, SearchInAccountResultDTO.class);
                // 请求成功标志
                String successStatus = "1";
                if (Objects.nonNull(resultDTO) && successStatus.equals(resultDTO.getStatus())) {
                    log.info("入账还款查询接口: 第{}次请求成功，返回数据为：{}", tryCount, resultStr);
                    // 请求成功
                    return resultDTO;
                } else {
                    log.error("入账还款查询接口: 第{}次请求失败，返回数据为：{}", tryCount, resultStr);
                }
            } catch (Exception e) {
                log.error("入账还款查询接口: 第{}次请求失败，失败原因：{}", tryCount, e.getMessage());
            }
            tryCount ++;
        }
        return resultDTO;
    }

    /**
     * 身份证号处理： urlencode(base64_encode('身份证号码'))
     *
     * @param pidnum
     * @return
     */
    public String getPidnum(String pidnum) {
        // base64处理
        String baseStr = Base64.encode(pidnum.getBytes());
        // url处理
        return URLUtil.encode(baseStr);
    }

    /**
     * 处理url
     *
     * @param paramJson json化的参数，不包含apikey
     * @return
     */
    public String toUrlParams(JSONObject paramJson) {
        List<String> skipKey = Arrays.asList("weburl");
        Stream<String> keys = paramJson.keySet().stream().filter(obj -> (!skipKey.contains(obj))).sorted();
        return keys.map(obj -> (obj + "=" + StrUtil.emptyIfNull(paramJson.getStr(obj)))).collect(Collectors.joining("&"));
    }

    /**
     * sign md5加密
     *
     * @param object 参数
     * @return
     */
    public String getSign(Object object) {
        JSONObject signJson = signToJson(object);
        // 排序,并跳过
        List<String> skipKey = Arrays.asList("sign", "notify_type", "notify_url", "apikey", "attach", "weburl");
        Stream<String> signKey = signJson.keySet().stream().filter(obj -> (!skipKey.contains(obj))).sorted();
        // 拼接字符串
        String signJsonStr = signKey.map(obj -> (obj + "=" + StrUtil.emptyIfNull(signJson.getStr(obj)))).collect(Collectors.joining("&"));
        signJsonStr = signJsonStr + "&key=" + StrUtil.emptyIfNull(signJson.getStr("apikey"));
        // md5加密
        return DigestUtils.md5DigestAsHex(signJsonStr.getBytes());
    }

    /**
     * 实体类转JSON字符串保留下划线，并添加key
     *
     * @param obj
     * @return
     * @throws JsonProcessingException
     */
    public JSONObject signToJson(Object obj) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        String jsonStr = null;
        try {
            jsonStr = objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("sign处理出现异常",e);
            throw new RuntimeException("sign处理出现异常",e);
        }
        JSONObject signJson = JSONUtil.parseObj(jsonStr);
        return signJson;
    }

    /**
     * 参数转JSON（保留下划线）, 不包含apikey
     *
     * @param obj
     * @return
     */
    public JSONObject paramToJson(Object obj) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        String jsonStr = null;
        try {
            jsonStr = objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("sign处理出现异常",e);
            throw new RuntimeException("sign处理出现异常",e);
        }
        JSONObject signJson = JSONUtil.parseObj(jsonStr);
        // 不传递apikey值
        signJson.remove("apikey");
        return signJson;
    }

    /**
     * HTTP头部参数对象获取
     *
     * @return HttpHeaders
     */
    private HttpHeaders getHttpHeaders(){
        HttpHeaders headers = new HttpHeaders();
        //默认json交互，后续若有其他访问方式需求，在token或者before方法中重新设置headers参数
        headers.setContentType(MediaType.TEXT_HTML);
        headers.setAccept(Arrays.asList(MediaType.ALL));
        //默认字符集 UTF-8
        headers.set(HttpHeaders.ACCEPT_CHARSET, StandardCharsets.UTF_8.toString());
        return headers;
    }


    /**
     * post请求
     * @param url 请求的url
     * @param jsonData 请求的json
     * @param headers 请求头部信息
     * @return 结果
     */
    public String restPost(String url, String jsonData, HttpHeaders headers){
        log.info("url: {}, jsonData: {}", url, jsonData);
        HttpEntity<String> httpEntity = getHttpEntity(jsonData,headers);
        return restTemplate.postForEntity(url, httpEntity, String.class).getBody();
    }

    /**
     * 获取 HttpEntity
     * @param jsonData 请求携带参数
     * @param headers 请求头信息
     * @return 请求实体
     */
    private HttpEntity<String> getHttpEntity(String jsonData, HttpHeaders headers){
        HttpEntity<String> httpEntity;
        if(StringUtils.hasText(jsonData)){
            httpEntity = new HttpEntity<>(jsonData, headers);
        }else{
            httpEntity = new HttpEntity<>(headers);
        }
        return httpEntity;
    }

}
