package com.summer.service.yys.wuhua;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.summer.service.yys.wuhua.dto.OperatorAuthRequestDto;
import com.summer.service.yys.wuhua.dto.OperatorQueryDto;
import com.summer.service.yys.wuhua.utils.HttpGzipUtil;
import com.summer.service.yys.wuhua.utils.SignUtil;
import com.summer.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

@Component
@Slf4j
public class WuHuaApi {

    @Value("${mobile.wuHua.merchantId}")
    private String merchantId;

    @Value("${mobile.wuHua.privateKey}")
    private String privateKey;

    @Value("${mobile.wuHua.publicKey}")
    private String publicKey;

    @Value("${mobile.wuHua.url}")
    private String url;

    @Value("${mobile.wuHua.notifyUrl}")
    private String notifyUrl;

    @Value("${mobile.wuHua.backUrl}")
    private String backUrl;

    /**
     * 获取url
     * @return
     * @throws Exception
     */
    public String getOperatorUrl(Map<String,String>  map) throws Exception {
        OperatorAuthRequestDto dto = new OperatorAuthRequestDto();
        dto.setMerchantId(merchantId);
        dto.setRequestId(map.get("phone")+ RandomUtil.getRandomSalt(4));
        dto.setTimestamp(System.currentTimeMillis()+"");
        dto.setName(map.get("name"));
        dto.setIdNumber(map.get("idNumber"));
        dto.setMobile(map.get("phone"));
        dto.setCallbackUrl(backUrl);
        dto.setNotifyUrl(notifyUrl);
        dto.setShowNavbar("false");
        TreeMap<String,Object> paramMap =  dto.toMap();
        paramMap.put("sign", SignUtil.generateSignature(JSON.toJSONString(dto),privateKey));
        //post请求
        return getUrl(url+"/whitewhale/authorization",paramMap);
    }

    /**
     * 获取运营商报告
     * @return
     * @throws Exception
     */
    public  String doQueryUserReport(String authId) throws Exception {
        OperatorQueryDto requestDto = new OperatorQueryDto();
        requestDto.setMerchantId(merchantId);
        requestDto.setAuthId(authId);
        requestDto.setTimestamp(System.currentTimeMillis()+"");
        String requestUrl = url+"/whitewhale/user/report";
        requestDto.setSign( SignUtil.generateSignature(JSON.toJSONString(requestDto),privateKey));
        //接口返回报文为Gip压缩
        String result = HttpGzipUtil.httpPostJsonReceiveGzip(requestUrl, JSONObject.toJSONString(requestDto));
        //验签
        // 返回报文的data 属性不参与签名，排除data
        JSONObject resultMap = JSON.parseObject(result);

        if("000000".equals(resultMap.getString("returnCode"))){
            log.info("五花拉取报告成功========================================"+authId);
            return String.valueOf(resultMap.getJSONObject("data"));
        }else {
            log.info("五花拉取报告失败========================================"+result);
        }
        return null;
       /* resultMap.remove("data");
        //验签
        boolean isSignatureValid =  SignUtil.isSignatureValid(JSON.toJSONString(resultMap), publicKey);
        if(isSignatureValid) {
            return result;
        }else {
            return null;
        }*/
    }

    /**
     * 获取原始数据
     * @param requestDto
     * @return
     * @throws Exception
     */
    public  String doQueryUserData(OperatorQueryDto requestDto) throws Exception {
        requestDto.setMerchantId(merchantId);
        String requestUrl = url+"/whitewhale/user/data";
        requestDto.setSign( SignUtil.generateSignature(JSON.toJSONString(requestDto),privateKey));
        //接口返回报文为Gip压缩
        String result = 	HttpGzipUtil.httpPostJsonReceiveGzip(requestUrl, JSONObject.toJSONString(requestDto));
        //验签
        // 返回报文的data 属性不参与签名，排除data
        JSONObject resultMap = JSON.parseObject(result);
        resultMap.remove("data");
        //验签
        boolean isSignatureValid =  SignUtil.isSignatureValid(JSON.toJSONString(resultMap), publicKey);
        if(isSignatureValid) {
            return result;
        }else {
            return null;
        }
    }

    /*
     * 拼装参数
     */
    private static String getUrl(String url, TreeMap<String, Object> params) throws UnsupportedEncodingException {
        // 添加url参数
        if (params != null) {
            Iterator<String> it = params.keySet().iterator();
            StringBuffer sb = null;
            while (it.hasNext()) {
                String key = it.next();
                String value = params.get(key).toString();
                value = URLEncoder.encode(value, "UTF-8");
                if (sb == null) {
                    sb = new StringBuffer();
                    sb.append("?");
                } else {
                    sb.append("&");
                }
                sb.append(key);
                sb.append("=");
                sb.append(value);
            }
            url += sb.toString();
        }
        return url;
    }

    public static void main(String[] args) throws Exception {
        /*String url = getOperatorUrl("徐帅", "412724199402013719", "13592106271", "https://www.baidu.com/", "MUh5MWIw6t3");
        System.out.println(url);*/
        System.out.println("15171410013jsk".substring(0,11));
    }
}
