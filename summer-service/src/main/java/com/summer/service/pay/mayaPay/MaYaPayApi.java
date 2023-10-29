package com.summer.service.pay.mayaPay;

import com.alibaba.fastjson.JSONObject;
import com.summer.util.MD5;
import com.summer.util.MapToUrlUtil;
import com.summer.util.OkHttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

@Component
@Slf4j
public class MaYaPayApi {
    static final String currency = "CNY";
    static final String version = "1.0";
    static final String signType = "MD5";
    static final String wayCode = "BANKCARD";
    static final String url = "http://payment.maya-pay.com/api";

//    商户号：M1672196335
//    私钥：HexeqISlOwtTVmOvx1lCZCeaYTSAxr4qXdE69gWvmuCHeru69diRj1qMNTCUkSaXSQYDMo2qmQjBTFoGy11vKYelodjvSFuixXPJMfRDnjU4bBguBQtAJlnm7pkc2jei
//    应用AppId：63abb0ef58562bb7a9928b8a
    //
    public  String maYaRepayment(String mchNo, String appId, String apiKey,String amount,String accountName, String notifyUrl, String mchOrderNo) {
        //玛雅还款
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("amount", amount);
        jsonObject.put("appId", appId);
        jsonObject.put("body", "qiantu");
        jsonObject.put("currency", currency);
        jsonObject.put("mchNo", mchNo);
        jsonObject.put("mchOrderNo", mchOrderNo);
        jsonObject.put("notifyUrl", notifyUrl);
        jsonObject.put("reqTime", new Date().getTime());
        jsonObject.put("signType", signType);
        jsonObject.put("subject", accountName);
        jsonObject.put("version", version);
        jsonObject.put("wayCode", wayCode);

        String signstr=MapToUrlUtil.getParmeString(new HashMap(jsonObject))+"&key="+apiKey;
        System.out.println(signstr);
        String signStrs=JeepayKit.md5(signstr,"utf-8");
        jsonObject.put("sign", signStrs);

        System.out.println(signStrs);
        String resultStr = OkHttpUtils.builder().url(url+"/pay/unifiedOrder")
                // 有参数的话添加参数，可多个
                .addParam("jsonObjectStringJson", jsonObject.toJSONString())
                // 如果是true的话，会类似于postman中post提交方式的raw，用json的方式提交，不是表单
                // 如果是false的话传统的表单提交
                .post(true)
                .sync();
        //{"code":0,"data":{"mchOrderNo":"23213123213447","orderState":1,"payData":"http://cashier.maya-pay.com/#/bank?a=P202212291736263900468","payOrderId":"P202212291736263900468"},"msg":"SUCCESS","sign":"A0D7AB2AB4E3B2AEA7BC0B0640CEB55F"}
        return resultStr;
    }





    public  String maYaPayment(String accountName, String accountNo, String amount, String mchNo, String appId, String notifyUrl, String outOrderNo, String apiKey) {
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("mchNo",mchNo);
        jsonObject.put("appId",appId);
        jsonObject.put("mchOrderNo",outOrderNo);
        //jsonObject.put("currency",currency);
        jsonObject.put("refundAccount",accountNo);
        jsonObject.put("accountName",accountName);
        jsonObject.put("refundAmount",amount);
        jsonObject.put("batchNo", new Date().getTime());
        //jsonObject.put("bankName", bankName);
        jsonObject.put("notifyUrl", notifyUrl);
        jsonObject.put("reqTime", new Date().getTime());
        jsonObject.put("version",version);
        jsonObject.put("signType",signType);
        String signstr=MapToUrlUtil.getParmeString(new HashMap(jsonObject))+"&key="+apiKey;
        System.out.println(signstr);
        String signStrs=JeepayKit.md5(signstr,"utf-8");
        jsonObject.put("sign", signStrs);
        String resultStr = OkHttpUtils.builder().url(url+"/refund/batchrefundOrder")
                // 有参数的话添加参数，可多个
                .addParam("jsonObjectStringJson", jsonObject.toJSONString())
                // 也可以添加多个
                .addHeader("Content-Type", "application/json")
                // 如果是true的话，会类似于postman中post提交方式的raw，用json的方式提交，不是表单
                // 如果是false的话传统的表单提交
                .post(true)
                .sync();
        //{"code":0,"data":{"state":1},"msg":"SUCCESS","sign":"89AA85F5E761F2FC918D143E2A686EF8"}
        //state 代付状态 0-订单生成 1-代付中 2-代付成功 3-代付失败 4-代付关闭
        return resultStr;
    }

    //商户号：M1672196335
    //    私钥：HexeqISlOwtTVmOvx1lCZCeaYTSAxr4qXdE69gWvmuCHeru69diRj1qMNTCUkSaXSQYDMo2qmQjBTFoGy11vKYelodjvSFuixXPJMfRDnjU4bBguBQtAJlnm7pkc2jei
//    应用AppId：63abb0ef58562bb7a9928b8a
    public static void main(String[] args) {
        //String result = MaYaRepayment("M1672196335","63abb0ef58562bb7a9928b8a","HexeqISlOwtTVmOvx1lCZCeaYTSAxr4qXdE69gWvmuCHeru69diRj1qMNTCUkSaXSQYDMo2qmQjBTFoGy11vKYelodjvSFuixXPJMfRDnjU4bBguBQtAJlnm7pkc2jei","100000","http://www.baidu.com","23213123213447");
        //String result = tengDaPayment("张延平","6214835715873458","10000","招商银行","550","wwww" ,"2321312321344","HexeqISlOwtTVmOvx1lCZCeaYTSAxr4qXdE69gWvmuCHeru69diRj1qMNTCUkSaXSQYDMo2qmQjBTFoGy11vKYelodjvSFuixXPJMfRDnjU4bBguBQtAJlnm7pkc2jei");
        //System.out.println(result);
    }
}
