package com.summer.service.pay.aochPay;


import com.alibaba.fastjson.JSON;
import com.summer.service.pay.bitePay.dto.OutAccountParamDTO;
import com.summer.service.pay.bitePay.dto.OutAccountResultDTO;
import com.summer.util.OkHttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class AochPayAPI {

    public String aochRepay(Map<String,String> param) {
        //奥创还款
        String userId = param.get("userId");
        String apiKey = param.get("apiKey");
        String notifyUrl = param.get("notifyUrl");
        String orderNo = param.get("orderNo");
        String amount = param.get("amount");
        String name = param.get("name");
        String sign = new MD5codingLowCase().MD5(userId + orderNo + amount + notifyUrl + apiKey);
        String resultStr = OkHttpUtils.builder().url("http://ultrapay.cc/Apipay")
                // 有参数的话添加参数，可多个
                .addParam("userid", userId)
                .addParam("orderno", orderNo)
                .addParam("desc", "数字")
                .addParam("amount", amount)
                .addParam("notifyurl", notifyUrl)
                .addParam("backurl", "http://8.209.212.16:8081/borrowUser/payBack")
                .addParam("paytype", "bank_auto")
                .addParam("userip", "127.0.0.1")
                .addParam("currency", "CNY")
                .addParam("sign", sign)
                .addParam("acname", name)
                // 也可以添加多个
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                // 如果是true的话，会类似于postman中post提交方式的raw，用json的方式提交，不是表单
                // 如果是false的话传统的表单提交
                .post(false)
                .sync();
        return resultStr;
    }


    public String aochPayment(Map<String,String> param) {
        //奥创代付
        String userId = param.get("userId");
        String apiKey = param.get("apiKey");
        String notifyUrl = param.get("notifyUrl");
        String orderNo = param.get("orderNo");
        String amount = param.get("amount");
        String name = param.get("name");
        String bankName = param.get("bankName");
        String bankCode = param.get("bankCode");

        String date = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());// 订单日期
        Map content = new HashMap();
        content.put("orderno",orderNo);
        content.put("date", date);
        content.put("amount", amount);
        content.put("account", bankCode);
        content.put("name", name);
        content.put("bank", bankName);
        content.put("subbranch", "无");
        String contentJson = JSON.toJSONString(content);
        String sign = new MD5codingLowCase().MD5(userId + "withdraw" +"["+ contentJson+"]" + apiKey);
        String resultStr = OkHttpUtils.builder().url("http://ultrapay.cc/Apipay")
                // 有参数的话添加参数，可多个
                .addParam("userid", userId)
                .addParam("action", "withdraw")
                .addParam("notifyurl", notifyUrl)
                .addParam("content",  "["+contentJson+"]")
                .addParam("sign", sign)
                // 也可以添加多个
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                // 如果是true的话，会类似于postman中post提交方式的raw，用json的方式提交，不是表单
                // 如果是false的话传统的表单提交
                .post(false)
                .sync();
        return resultStr;
    }
}
