package com.summer.service.impl.risk;

import com.alibaba.fastjson.JSONObject;
import com.summer.api.service.IBackConfigParamsService;
import com.summer.pojo.vo.BackConfigParamsVo;
import com.summer.service.impl.thirdpart.zhimi.HttpClientUtil;
import com.summer.util.SHA256Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

//外部黑名单接口
@Slf4j
@Service
public class BlackApi {
    //宁远科技
    @Value("${nykj.url}")
    String nykjUrl;
    //孟华
    @Value("${mh.url}")
    String mhUrl;
    @Value("${mh.account}")
    String mhAccount;
    @Value("${mh.secret}")
    String mhSecret;
    //昆仑山
    @Value("${kls.url}")
    String klsUrl;
    @Value("${kls.appCode}")
    String appCode;
    @Value("${kls.authCode}")
    String authCode;
    @Resource
    private IBackConfigParamsService backConfigParamsService;


    //孟华黑名单,身份证与手机号至少二选一，其他可传null.
    public boolean menghuaBlack(String idcardName, String idcardNo, String phone, String bankNumbers, String ips, String deviceIds) throws Exception {
      /*  String url = "https://api.jygzy.com/api/blacklist-query";
        String account = "673181939";
        String secret = "6172276b15c84e37bb425a90f022df0e";*/
        String url = mhUrl;
        String account = mhAccount;
        String secret = mhSecret;
        Map params = new TreeMap();
        if (StringUtils.isNotEmpty(idcardName)) {
            params.put("name", idcardName);
        }
        if (StringUtils.isNotEmpty(idcardNo)) {
            params.put("identityCard", idcardNo);
        }

        if (StringUtils.isNotEmpty(phone)) {
            params.put("mobile", phone);
        }
        if (StringUtils.isNotEmpty(bankNumbers)) {
            String[] banks = bankNumbers.split(",");
            params.put("bankCards", banks);
        }
        if (StringUtils.isNotEmpty(ips)) {
            String[] ip = ips.split(",");
            params.put("ips", ip);
        }
        if (StringUtils.isNotEmpty(deviceIds)) {
            String[] devices = deviceIds.split(",");
            params.put("deviceIds", devices);
        }

        Map heards = new TreeMap();
        heards.put("AppName", account);
        heards.put("Content-Type", "application/json;charset=UTF-8");
        String data = JSONObject.toJSONString(params);
        String sign = SHA256Util.HMACSHA256(data, secret);
        heards.put("Sign", sign);
        String contentType = "application/json;charset=UTF-8";
        String result = HttpClientUtil.post(url, heards, data, contentType);
        log.info("孟华风控黑名单请求参数:{},返回结果:{}", JSONObject.toJSONString(params), result);
        if (StringUtils.isNotEmpty(result)) {
            JSONObject json = JSONObject.parseObject(result);
            Integer code = json.getInteger("code");
            if (code == 0) {
                JSONObject data1 = json.getJSONObject("data").getJSONObject("data");
                Boolean flag = data1.getBoolean("flag");
                if (flag) {
                    return true;
                }
            }
        }
        return false;
    }

    //昆仑山黑名单
    public boolean kunLsBlack(String idcardNo, String phone) throws Exception {
       /* String url = "http://8.129.168.128:8081/black/blackList/";
        String authCode = "be2GkKS*#nFk7xMn";
        String appCode = "gtrh";*/
        String url = klsUrl;
        String authCode = this.authCode;
        String appCode = this.appCode;

        url = url + "?idCard=" + idcardNo + "&mobile=" + phone;

        Map heards = new HashMap();
        heards.put("authCode", authCode);
        heards.put("appCode", appCode);
        String result = HttpClientUtil.get(url, heards);
        log.info("昆仑山风控黑名单请求参数:{},返回结果:{}", idcardNo + "," + phone, result);
        if (StringUtils.isNotEmpty(result)) {
            JSONObject json = JSONObject.parseObject(result);
            Integer code = json.getInteger("code");
            String success = json.getString("success");
            if (code == 200 && success.equals("success")) {
                boolean isBlack = json.getBoolean("data");
                return isBlack;// true为命中黑名单，false为未命中
            }
        }
        return false;
    }

    //宁远科技山黑名单
    public boolean ningYkjBlack(String idcardNo, String phone) throws Exception {
//        String url = "https://blacklist.ningyukeji.top:9006/ext/queryBlacklist";
        // String url = "http://8.210.117.248/user/black/queryThirdBlack?idcardNo=IDCARDNO&telephone=PHONE";
        String url = nykjUrl;
        url = url.replaceAll("IDCARDNO", idcardNo).replaceAll("PHONE", phone);
//        String merCode ="me-a7283c5ce5994ea5be67bbf805a3a";//商户号

        Map params = new HashMap();
        params.put("mobile", phone);
        params.put("idCard", idcardNo);
//        params.put("merCode",merCode);
//        String result = HttpClientUtil.post(url,null,params,HttpClientUtil.CONTENT_TYPE);
        String result = HttpClientUtil.get(url);
        log.info("宁远科技黑名单请求参数:{},返回结果:{}", JSONObject.toJSONString(params), result);
        if (StringUtils.isNotEmpty(result)) {
            JSONObject json = JSONObject.parseObject(result);
            String code = json.getString("code");
            boolean isBlack = json.getBoolean("data");
            if (code != null && code.equals("0") && isBlack) {//命中黑名单
                return true;
            }
        }
        return false;
    }


    /*
     * 是否命中外部黑名单
     *
     * @return
     * null - 未命中
     * 1 - 命中孟华黑名单
     * 2 - 命中昆仑山黑名单
     * 3 - 命中宁远黑名单
     * */
    public String isOutsidBlack(String idcardNo, String phone,Integer userId) {
        try {
            BackConfigParamsVo mhvo = backConfigParamsService.findBySysKey("menghua_black_switch");
            BackConfigParamsVo nyvo = backConfigParamsService.findBySysKey("ningyuan_black_switch");

            if (mhvo.getSysValue() == 2) {
                boolean mhBlack = this.menghuaBlack(null, idcardNo, phone, null, null, null);
                if (mhBlack)
                    return "命中孟华黑名单";
            }
            if (nyvo.getSysValue() == 2) {
                boolean nykjBlack = this.ningYkjBlack(idcardNo, phone);
                if (nykjBlack)
                    return "命中宁远黑名单";
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getLocalizedMessage());

        }
        return null;
    }


    public static void main(String[] args) throws Exception {
        //boolean flag= BlackApi.menghuaBlack("","321102199110141010","18758055912","","","");
        // boolean flag  = BlackApi.kunLsBlack("321102199110141010","18758055912");
        /*this.ningYkjBlack("321102199110141010","18758055912");*/
        //System.out.println("请求的用户是否为黑名单用户："+flag);
    }
}
