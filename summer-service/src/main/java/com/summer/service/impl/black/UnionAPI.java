package com.summer.service.impl.black;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.summer.api.service.IUserInfoService;
import com.summer.dao.entity.PlateformChannel;
import com.summer.dao.entity.UserInfo;
import com.summer.dao.mapper.PlateformChannelMapper;
import com.summer.service.yys.wuhua.utils.RsaUtil;
import com.summer.util.RSANewUtil;
import com.summer.util.risk.RsaUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
@Slf4j
public class UnionAPI {

    @Autowired
    private IUserInfoService userInfoService;

    @Autowired
    private PlateformChannelMapper plateformChannelMapper;

    public static final String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC+mLrQeU/p2ltcLALYubn7Ut6u\n" +
            "T/NBDR6rqY+yVZChkMvnJn0n6NRg57NwhnnBlZA+puvKzx0qkul9lYylJZ0QJ87a\n" +
            "f0UAvIk7YPqoHPhEJ6AC5J8YZ7Uu/j5yIJDKNn7PvUf5BBJMzgm5MZiDcwusFLxg\n" +
            "JOMyCVaA+0NVL1IqsQIDAQAB";//公钥
    public static final String PRIVATE_KEY = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAL6YutB5T+naW1ws\n" +
            "Ati5uftS3q5P80ENHqupj7JVkKGQy+cmfSfo1GDns3CGecGVkD6m68rPHSqS6X2V\n" +
            "jKUlnRAnztp/RQC8iTtg+qgc+EQnoALknxhntS7+PnIgkMo2fs+9R/kEEkzOCbkx\n" +
            "mINzC6wUvGAk4zIJVoD7Q1UvUiqxAgMBAAECgYAHrMDBClGEei15nbATqA7sJt8e\n" +
            "p8uE0BFvGinWJYnoZ5s+WJdrlv2OVWYtXsziVbJ0Q2Z1STfEchwC2ZXLRKN5ZDky\n" +
            "oKlZP19Squo2dbfQz3HYXXpL4CciNLJsEtbRj9ucNZoErGIF3q9dUwfOeWAoskVT\n" +
            "JNTxC1wLTfeLgEOMEQJBAO6nZFDJX0X7HrCASYJ9J7ATs/lOZ0/HaACIJx105ZJs\n" +
            "j3iGEAkVStUjB+j7Mlp32+K3G2dsjb0JRp07BsRuFb0CQQDMcyQgtdfpKVGI6TYo\n" +
            "kZDAoJnftQ8laWz8SwaxuyOIT413FOvBM7Hfj0nu98Zb73o0vBXe7CDsEAytG3fb\n" +
            "m5YFAkEAn+NPxvwSs2zZk8Bu/1hgNYwNwJIxjvZ2jByrsAV4JNBMkp+msJ8uQns0\n" +
            "undItemlTFAGsRS+JQ7I22AHWaSzNQJBAKKzNpr5W1tn0ET+ImVigilpJUsL0YPa\n" +
            "RCr6odcqr/o4JMIRWhQkSXc5loKfl7FuAHia/WSr0P9LPms8IsQXKukCQQC1Qa9+\n" +
            "45iRUPJXF/XXJQzjlE53qSGp4fEQ0js7WwDb+nFm+k2/nFZJbSLVwQevCMoZlCXO\n" +
            "pX8suJ1A7fcKtQVD";//私钥


    public Boolean unionCheckUser(String data){
        /*try {
            String phone = RSANewUtil.decrypt(data,RSANewUtil.getPrivateKey(PRIVATE_KEY));
            log.info("unionCheckUserPhone============================"+phone);

        } catch (Exception exception) {
            log.info("秘钥错误========================");
            return false;
        }
        return true;*/
        UserInfo userInfo = userInfoService.findByPhone(data);
        if(Objects.isNull(userInfo)){
            return false;
        }
        return true;
    }

    public  PlateformChannel unionRegister(){
            PlateformChannel plateformChannel = plateformChannelMapper.findUnionLink();
            return plateformChannel;
    }

    /*public static void main(String[] args) {
        try {
           *//* Map map = RsaUtils.initKey();
            String publicKey = RsaUtils.getPublicKey(map);
            String privateKey = RsaUtils.getPrivateKey(map);
            System.out.println("publicKey========================================"+publicKey);
            System.out.println("privateKey========================================"+privateKey);*//*
            String data = RSANewUtil.encrypt("15171410013",RSANewUtil.getPublicKey(PUBLIC_KEY));
            System.out.println("data======================================"+data);
            String phone = RSANewUtil.decrypt(data,RSANewUtil.getPrivateKey(PRIVATE_KEY));
            System.out.println("phone======================================"+phone);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }*/
}
