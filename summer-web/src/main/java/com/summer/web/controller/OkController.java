package com.summer.web.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.summer.service.impl.black.JuGuangApi;
import com.summer.service.job.RedissLockUtil;
import com.summer.util.AliOssUtils;
import com.summer.util.OkHttpUtils;
import com.summer.util.RSANewUtil;
import com.summer.util.RedisUtil;
import com.summer.util.risk.RsaUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/ok")
@Log4j2
public class OkController {
    @Autowired
    private RedisUtil redisUtil;
    private static final String lock01 = "lock01";
    @Autowired
    private JuGuangApi juGuangApi;

    private static  String dasda = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC+mLrQeU/p2ltcLALYubn7Ut6uT/NBDR6rqY+yVZChkMvnJn0n6NRg57NwhnnBlZA+puvKzx0qkul9lYylJZ0QJ87af0UAvIk7YPqoHPhEJ6AC5J8YZ7Uu/j5yIJDKNn7PvUf5BBJMzgm5MZiDcwusFLxgJOMyCVaA+0NVL1IqsQIDAQAB";

    public static void main(String[] args) throws Exception {
        String phone =  RSANewUtil.encrypt("13333333333", RSANewUtil.getPublicKey(dasda));
        System.out.println("phone=========================="+phone);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("appId","f5b3d3ec-0d88-4b81-b0ed-d8bf56fc8786");
        jsonObject.put("data", phone);
        System.out.println(jsonObject);
        //撞库
        //String result = OkHttpUtils.builder().url("https://weeb.lcwlkd.top/api/v1/union/unionCheckUser")
        //注册
        String result = OkHttpUtils.builder().url("https://99.necdn.top:8055/api/v1/unionLogin/unionRegister")
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .addParam("jsonObjectStringJson", String.valueOf(jsonObject))
                .post(true).sync();
        System.out.println("result=========================="+result);
    }


    @GetMapping("ok")
    public String ok() {
        //taskjobService.handleDispatchTask();
//        taskjobService.autoDispatchWaiter();
        return "ok";
    }

    @GetMapping("/redisTest")
    public String redisTest() {
        String key = "redisTest";
        redisUtil.set(key, "1");
        return redisUtil.get(key);
    }

    @GetMapping("/juguangTest")
    public String juguangTest() {
        List<Map<String, String>> userList = new ArrayList<>();
        Map map = new HashMap();
        map.put("name","test");
        map.put("phone","15037203227");
        map.put("idNo","210122198010090011");
        userList.add(map);
        juGuangApi.uploadJointDebt(userList,99,0);
        System.out.println("=====");
        return "ok";
    }



    @GetMapping("/lock01")
    public String lock01() {
        System.out.println("lock01进来了");
        long l = System.currentTimeMillis();
        boolean b = RedissLockUtil.tryLock(lock01, TimeUnit.SECONDS, 30, 10);
        long l1 = System.currentTimeMillis();
        System.out.println("lock01出去了");
        long time = l1 - l;
        System.out.println(time);
        redisUtil.del(lock01);
        return "lock01";
    }

    @GetMapping("/yys")
    public String yys(String content) {
        String urlName = AliOssUtils.uploadReport(null,"report/1234567_wuhua.txt",content);
        return urlName;
    }

    @GetMapping("/lock02")
    public String lock02() {
        System.out.println("lock02进来了");
        long l = System.currentTimeMillis();
        boolean b = RedissLockUtil.tryLock(lock01, TimeUnit.SECONDS, 30, 10);
        long l1 = System.currentTimeMillis();
        System.out.println("lock02出去了");
        long time = l1 - l;
        System.out.println(time);
        redisUtil.del(lock01);
        return "lock02";
    }

}
