package com.summer.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.summer.dao.entity.PlateformChannel;
import com.summer.dao.entity.UserInfo;
import com.summer.service.impl.black.UnionAPI;
import com.summer.util.RSANewUtil;
import com.summer.util.UnionDESUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

//撞库
@RestController
@RequestMapping("/api/v1/unionLogin")
@Log4j2
public class UnionController {

    private static final String SUCCESS = "成功";
    private static final String FAIL = "失败";

    @Autowired
    private UnionAPI unionAPI;

    @Value(value = "${nacos.config.namespace}")
    private String appIdSystem;

    @PostMapping("/checkUser")
    @ResponseBody
    public String checkUser(HttpServletRequest request, @RequestBody String jsonData) throws Exception {
        JSONObject backJson = new JSONObject();
        backJson.put("code",1);
        JSONObject params = JSONObject.parseObject(jsonData);
        //{"data":"qZ/+RUcJ/Vv2kDUN3eFx+Miey92VqD4PTH70B2CNt2R8UfyjW9CxeU5xteAHe3Ed+nzrS147GAcSzeH4gG9ctw==","channel":"1"}
        String channel = params.getString("channel");
        String data = UnionDESUtil.decrypt(params.getString("data"),"af4bd50a21fe0851a24c7bf3");
        String phone = JSONObject.parseObject(data).getString("phone_md5");
        log.info("unionCheckUserAppId============================"+channel);
        log.info("unionCheckUserPhone============================"+phone);
        if(unionAPI.unionCheckUser(phone)){
            backJson.put("msg",FAIL);
        }else {
            backJson.put("code",0);
            backJson.put("msg",SUCCESS);
        }
        return backJson.toJSONString();
    }

    //提交申请/进件
    @PostMapping("/data")
    @ResponseBody
    public String data(HttpServletRequest request, @RequestBody String jsonData) throws Exception {
        JSONObject backJson = new JSONObject();
        backJson.put("code",1);
        backJson.put("url","");
        PlateformChannel plateformChannel = unionAPI.unionRegister();
        if(Objects.isNull(plateformChannel)){
            backJson.put("msg","没有开放链接");
        }else {
            backJson.put("code",0);
            backJson.put("msg",SUCCESS);
            backJson.put("url",plateformChannel.getLink());
        }
        return backJson.toJSONString();
    }


    @PostMapping("/unionCheckUser")
    @ResponseBody
    public String unionCheckUser(HttpServletRequest request, @RequestBody String jsonData) throws Exception {
        JSONObject backJson = new JSONObject();
        backJson.put("code",201);
        backJson.put("data",false);
        JSONObject params = JSONObject.parseObject(jsonData);
        String appId = params.getString("appId");
        String phone = params.getString("data");
        log.info("unionCheckUserAppId============================"+appId);
        try {
            phone = RSANewUtil.decrypt(phone,RSANewUtil.getPrivateKey(UnionAPI.PRIVATE_KEY));
            log.info("unionCheckUserPhone============================"+phone);
        } catch (Exception exception) {
            backJson.put("msg","秘钥错误");
            return backJson.toJSONString();
        }

        if (!appId.equals(appIdSystem) && !appId.equals("appIdSystem11223")){
            backJson.put("msg","无效的appId");
            return backJson.toJSONString();
        }

        if(unionAPI.unionCheckUser(phone)){
            backJson.put("msg",FAIL);
        }else {
            backJson.put("code",200);
            backJson.put("msg",SUCCESS);
            backJson.put("data",true);
        }
        return backJson.toJSONString();

    }

    @PostMapping("/unionRegister")
    @ResponseBody
    public String unionRegister(HttpServletRequest request, @RequestBody String jsonData) throws Exception {
        JSONObject backJson = new JSONObject();
        backJson.put("code",201);
        backJson.put("url","");
        JSONObject params = JSONObject.parseObject(jsonData);
        String appId = params.getString("appId");
        String phone = params.getString("data");
        log.info("unionRegisterAppId============================"+appId);
        try {
            phone = RSANewUtil.decrypt(phone,RSANewUtil.getPrivateKey(UnionAPI.PRIVATE_KEY));
            log.info("unionRegisterPhone============================"+phone);
        } catch (Exception exception) {
            backJson.put("msg","秘钥错误");
            return backJson.toJSONString();
        }

        if (!appId.equals(appIdSystem) && !appId.equals("appIdSystem11223")){
            backJson.put("msg","无效的appId");
            return backJson.toJSONString();
        }

        PlateformChannel plateformChannel = unionAPI.unionRegister();
        if(Objects.isNull(plateformChannel)){
            backJson.put("msg","没有开放链接");
        }else {
            backJson.put("code",200);
            backJson.put("msg",SUCCESS);
            backJson.put("url",plateformChannel.getLink());
        }
        return backJson.toJSONString();

    }

}
