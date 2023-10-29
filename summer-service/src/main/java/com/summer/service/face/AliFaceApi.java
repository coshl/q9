package com.summer.service.face;

import com.aliyun.cloudauth20190307.Client;
import com.aliyun.cloudauth20190307.models.*;
import com.aliyun.tea.TeaException;
import com.aliyun.tea.TeaUnretryableException;
import com.aliyun.tearpc.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;
import com.summer.dao.entity.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class AliFaceApi {

    public static String initFaceVerify(UserInfo userInfo, String metaInfo){
        InitFaceVerifyRequest request = new InitFaceVerifyRequest();
        // 请输入场景ID+L。
        request.setSceneId(Long.valueOf("1000007593"));
//        request.setSceneId(Long.valueOf("1000001977"));
        // 设置商户请求的唯一标识。
        request.setOuterOrderNo("BjtOcdY3RJBtm81IsgDO5aSxduyIjydk");
        // 认证方案。
        request.setProductCode("ID_PLUS");
        request.setCertType("IDENTITY_CARD");
        request.setModel("LIVENESS");
        request.setCertName(userInfo.getRealName());
        request.setCertNo(userInfo.getIdCard());
        // MetaInfo环境参数。
        request.setMetaInfo(metaInfo);
        // 推荐，支持服务路由。
        InitFaceVerifyResponse response = initFaceVerifyAutoRoute(request);
        // 不支持服务自动路由。
        //InitFaceVerifyResponse response = initFaceVerify("cloudauth.cn-shanghai.aliyuncs.com", request);
        log.info("人脸通知返回结果:{}",response.getMessage() + response.getCode());
        if("200".equals(response.getCode()) && "success".equals(response.getMessage())){
            return response.getResultObject() == null ? null
                    : response.getResultObject().getCertifyId();
        }else {
            log.info("人脸错误返回结果:{}",response.getMessage());
            return null;
        }
    }

    private static InitFaceVerifyResponse initFaceVerifyAutoRoute(InitFaceVerifyRequest request) {
        // 第一个为主区域Endpoint，第二个为备区域Endpoint。
        List<String> endpoints = Arrays.asList("cloudauth.cn-shanghai.aliyuncs.com",
                "cloudauth.cn-beijing.aliyuncs.com");
        InitFaceVerifyResponse lastResponse = null;
        for (String endpoint : endpoints) {
            try {
                InitFaceVerifyResponse response = initFaceVerify(endpoint, request);
                lastResponse = response;

                // 服务端错误，切换到下个区域调用。
                if (response != null && "500".equals(response.getCode())) {
                    continue;
                }

                return response;
            } catch (Exception e) {
                // 网络异常，切换到下个区域调用。
                if (e.getCause() instanceof TeaException) {
                    TeaException teaException = ((TeaException)e.getCause());
                    if (teaException.getData() != null && "ServiceUnavailable".equals(
                            teaException.getData().get("Code"))) {
                        continue;
                    }
                }
                if (e.getCause() instanceof TeaUnretryableException) {
                    continue;
                }
            }
        }
        return lastResponse;
    }

    private static InitFaceVerifyResponse initFaceVerify(String endpoint, InitFaceVerifyRequest request)
            throws Exception {
        Config config = new Config();
//        config.setAccessKeyId("LTAI5t5pZsewqsFu8BaCDbJW");LTAI5tNBLDa8LrLAt1QfTfdR
        config.setAccessKeyId("LTAI5tNBLDa8LrLAt1QfTfdR");
//        config.setAccessKeySecret("tVmydwsv62OmlyXHsFrqxYyZysbzh5");
        config.setAccessKeySecret("dkOmu8NzXHfG0kZ7zI9kvghZkKC7Pf");

        config.setEndpoint(endpoint);
        // 设置http代理。
        //config.setHttpProxy("http://xx.xx.xx.xx:xxxx");
        // 设置https代理。
        //config.setHttpsProxy("https://xx.xx.xx.xx:xxxx");
        Client client = new Client(config);

        // 创建RuntimeObject实例并设置运行参数。
        RuntimeOptions runtime = new RuntimeOptions();
        runtime.readTimeout = 10000;
        runtime.connectTimeout = 10000;

        return client.initFaceVerify(request, runtime);
    }

    public static void main(String[] args) {
        DescribeFaceVerifyResponse response = describeFaceVerify("sha4b883cc2d4c31134ba2acebccb027");
        String materialInfo = response.getResultObject() == null ? null : response.getResultObject().getMaterialInfo();
        System.out.println(materialInfo);
    }

    public static DescribeFaceVerifyResponse describeFaceVerify(String certifyId){
        // 通过以下代码创建API请求并设置参数。
        DescribeFaceVerifyRequest request = new DescribeFaceVerifyRequest();
        // 请输入场景ID+L。
//        request.setSceneId(Long.valueOf("1000001977"));
        request.setSceneId(Long.valueOf("1000007593"));
        request.setCertifyId(certifyId);
        // 推荐，支持服务路由。
        DescribeFaceVerifyResponse response = describeFaceVerifyAutoRoute(request);
        // 不支持服务自动路由。
        //DescribeFaceVerifyResponse response = describeFaceVerify("cloudauth.cn-shanghai.aliyuncs.com", request);
        log.info("ali人脸通知返回结果:{},{}",certifyId,response.getMessage() == null ? null : response.getMessage() );
        return response;
    }

    private static DescribeFaceVerifyResponse describeFaceVerifyAutoRoute(DescribeFaceVerifyRequest request) {
        // 第一个为主区域Endpoint，第二个为备区域Endpoint。
        List<String> endpoints = Arrays.asList("cloudauth.cn-shanghai.aliyuncs.com",
                "cloudauth.cn-beijing.aliyuncs.com");
        DescribeFaceVerifyResponse lastResponse = null;
        for (String endpoint : endpoints) {
            try {
                DescribeFaceVerifyResponse response = describeFaceVerify(endpoint, request);
                lastResponse = response;
                // 服务端错误，切换到下个区域调用。
                if (response != null && "500".equals(response.getCode())) {
                    continue;
                }
                return response;
            } catch (Exception e) {
                // 网络异常，切换到下个区域调用。
                if (e.getCause() instanceof TeaException) {
                    TeaException teaException = ((TeaException)e.getCause());
                    if (teaException.getData() != null && "ServiceUnavailable".equals(
                            teaException.getData().get("Code"))) {
                        continue;
                    }
                }

                if (e.getCause() instanceof TeaUnretryableException) {
                    continue;
                }
            }
        }

        return lastResponse;
    }

    //{"faceAge":34,"faceAttack":"F","faceOcclusion":"F","facialPictureFront":{"faceAttackScore":0.001270582666620612,"ossBucketName":"cn-shanghai-aliyun-cloudauth-1413929175248673",
    // "ossObjectName":"verify/1413929175248673/sha8994dad44d2e93597583a8048c7eb_0.jpeg","pictureUrl":"https://cn-shanghai-aliyun-cloudauth-1413929175248673.oss-cn-shanghai.aliyuncs
    // .com/verify/1413929175248673/sha8994dad44d2e93597583a8048c7eb_0.jpeg?Expires=1660917570&OSSAccessKeyId=STS
    // .NTjm41w1RS9854uAyNfVcEaa1&Signature=nD2QQdIv07T7FNgBBefcJ7KKDKM%3D&security-token=CAISjgJ1q6Ft5B2yfSjIr5ffJo6Fmu5z5PvTNxLEpXkbatlPqqTK0zz2IHtOfHVuA
    // %2BgYsP01mWBV6v8alrkqF8YeFBaVNJQttMgHr1z6O2twXlJoqu5qsoasPETOITyZtZagToeUZdfZfejXGDKgvyRvwLz8WCy%2FVli%2BS%2FOggoJmadJlNWvRL0AxZrFsKxBltdUROFbIKP%2BpKWSKuGfLC1dysQcO4gEWq4bHm5HFsUuE3QSll7VJ99queaLJNZc8YM1NNP6ux
    // %2FFze6b71ypd1gNH7q8ejtYfom2f4o%2FHWwcPuk%2FbY7eIq4V1WwZ9Z7knHaVAq%2BXwnOBkuuXYnIns0BdLMuZOSD7YQI2wYWEwgBcxt78agAFxIJN1aLBBU%2FZBH%2FlB1ORNNbnO7DhAQPNF2HGbyp3czENc8rHszmnQusiA1kuAtnsWhTuqxhyLWsTY2zbqfsqbDBj
    // %2Fh62UF4WySy5D3GHfddeXaVuP%2BXup0iLCfRGl2tXxNF5zRBExzL%2BbOcZt7Kab6JXv%2Bn%2BVk4DDJKi5JvD%2Bvw%3D%3D","qualityScore":96.0854721069336,"verifyScore":98.99892560460579}}
    private static DescribeFaceVerifyResponse describeFaceVerify(String endpoint, DescribeFaceVerifyRequest request)
            throws Exception {
        Config config = new Config();
        config.setAccessKeyId("LTAI5tNBLDa8LrLAt1QfTfdR");
        config.setAccessKeySecret("dkOmu8NzXHfG0kZ7zI9kvghZkKC7Pf");
        config.setEndpoint(endpoint);
        // 设置http代理。
        //config.setHttpProxy("http://xx.xx.xx.xx:xxxx");
        // 设置https代理。
        //config.setHttpsProxy("http://xx.xx.xx.xx:xxxx");
        Client client = new Client(config);

        // 创建RuntimeObject实例并设置运行参数。
        RuntimeOptions runtime = new RuntimeOptions();
        runtime.readTimeout = 10000;
        runtime.connectTimeout = 10000;

        return client.describeFaceVerify(request, runtime);
    }


    public static String faceEnums(String code){
        switch (code) {
            case "201":
               return FaceEnums.FAIL201.getMassage();
            case "202":
                return FaceEnums.FAIL202.getMassage();
            case "203":
                return FaceEnums.FAIL203.getMassage();
            case "204":
                return FaceEnums.FAIL204.getMassage();
            case "205":
                return FaceEnums.FAIL205.getMassage();
            case "206":
                return FaceEnums.FAIL206.getMassage();
            case "207":
                return FaceEnums.FAIL207.getMassage();
            default:
                break;
        }
        return "认证失败";

    }

    /**
     * 去掉url中的路径，留下请求参数部分
     * @param strURL url地址
     * @return url请求参数部分
     * @author lzf
     */
    private static String TruncateUrlPage(String strURL){
        String strAllParam=null;
        String[] arrSplit=null;
        strURL=strURL.trim().toLowerCase();
        arrSplit=strURL.split("[?]");
        if(strURL.length()>1){
            if(arrSplit.length>1){
                for (int i=1;i<arrSplit.length;i++){
                    strAllParam = arrSplit[i];
                }
            }
        }
        return strAllParam;
    }

   /* public static void main(String[] args) {
        String url = "https://cn-shanghai-aliyun-cloudauth-1413929175248673.oss-cn-shanghai.aliyuncs.com/verify/1413929175248673/sha9f90b2bcf8bd9b0f4829c9a285ce5_ocridnationalemblem_9b1f.jpeg?Expires=1661143113&OSSAccessKeyId=STS.NTsR12wqBHrtCBdNkJijCNDZR&Signature=nQDg5DaB%2Fk6jA71%2FCOcNQl3pkKI%3D&security-token=CAISjgJ1q6Ft5B2yfSjIr5fGGYuGmq5j%2F7CfQWTVqmsfZeVvoYHxsDz2IHtOfHVuA%2BgYsP01mWBV6v8alrkqF8YeFBaVNJQttMgHr1z6O198AEVrqu5qsoasPETOITyZtZagToeUZdfZfejXGDKgvyRvwLz8WCy%2FVli%2BS%2FOggoJmadJlNWvRL0AxZrFsKxBltdUROFbIKP%2BpKWSKuGfLC1dysQcO4gEWq4bHm5HFsUuE3QSll7VJ99queaLJNZc8YM1NNP6ux%2FFze6b71ypd1gNH7q8ejtYfom2f4o%2FHWwcPuk%2FbY7eIq4V1WwZ9Z7knHaVAq%2BXwnOBkuuXYnIns0BdLMuZOSD7YQI2wYWEwgBcxt78agAGafFZp8rBpAY%2BpWjJDLRVQ726Q%2BdhV2j6QeWaEDI0d0xO1empx0n8PY%2ByLFajc563yqKUq55PcEjtXaRHGnt4ERbcbUAoBiJfPJspuVtvioGX5Xgh5GMrPAyDV%2BZzWckuiXhYy2EvGsVuwoJf6t5TpQt5bF9iv8Cqr5vKk0ANCvw%3D%3D";
        String str = url.substring(0,url.indexOf("?"));
        System.out.println(str);
    }*/
}
