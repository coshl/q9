package com.summer.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.summer.api.service.IAppVersionInfoService;
import com.summer.dao.entity.AppVersionInfo;
import com.summer.util.AliOssUtils;
import com.summer.util.CallBackResult;
import com.summer.web.util.NewPlist;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
@Slf4j
@RestController
@RequestMapping(value = "/v1.0/api/common")
public class CommonController {

    @Value("${oss.bucket_name}")
    private String bucketName;
    @Value("${oss.endpoint}")
    private String endpoint;


    @Value("${oss.access_key_id}")
    private String accessKeyId;
    @Value("${oss.access_key_secret}")
    private String accessKeySecret;

    @Resource
    private IAppVersionInfoService appVersionInfoService;

    /**
     * 上传文件到阿里云
     */
    @PostMapping("/upload/file")
    public CallBackResult uploadFile(@RequestParam("file") MultipartFile file) {
        String suffix = null;
        String fileName = file.getOriginalFilename();
        if (fileName.contains(".")) {
            suffix = fileName.substring(fileName.lastIndexOf("."));
        }

        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        if (inputStream == null)
            return CallBackResult.fail("上传失败");

        String finalFileName = AliOssUtils.upload(null, "img_", suffix, inputStream);
        String url = "https://" + bucketName + "." + endpoint + "/" + finalFileName;
        return CallBackResult.ok(url);
    }
    /**
     * 上传文件到阿里云
     */
    @PostMapping("/upload/fileBase64")
    public CallBackResult fileBase64(HttpServletRequest request) {



        String file_base64 = null;

        try {
            BufferedReader streamReader = new BufferedReader( new InputStreamReader(request.getInputStream(), "UTF-8"));
            StringBuilder responseStrBuilder = new StringBuilder();
            String inputStr;
            while ((inputStr = streamReader.readLine()) != null)
                responseStrBuilder.append(inputStr);

            JSONObject jsonObject = JSONObject.parseObject(responseStrBuilder.toString());
            file_base64 = jsonObject.getString("file_base64");
        } catch (Exception e) {
            e.printStackTrace();
        }




        ByteArrayInputStream stream = null;
        try {
            BASE64Decoder decoder = new BASE64Decoder();
            byte[] bytes1 = decoder.decodeBuffer(file_base64);
            stream = new ByteArrayInputStream(bytes1);
        } catch (Exception e) {
            // TODO: handle exception
        }

        if (stream == null)
            return CallBackResult.fail("上传失败");

        String finalFileName = AliOssUtils.upload(null, "img_", ".jpg", stream);
        String url = "https://" + bucketName + "." + endpoint + "/" + finalFileName;
        return CallBackResult.ok(url);
    }


    /**
     * 请求oss文件访问路径
     */
    @GetMapping("/getFileUrl")
    public CallBackResult getFileUrl(@RequestParam("fileName") String fileName) {
        String url = AliOssUtils.getAuthUrl(null, fileName, 60 * 60 * 24, false);
        Map<String, String> map = new HashMap<>();
        map.put("url", url);
        return CallBackResult.ok(map);
    }

    /**
     * oss参数配置
     */
    @GetMapping("/ossConfig")
    public CallBackResult ossConfigossConfig() {
        JSONObject res = new JSONObject();
        res.put("bucketName",bucketName);
        res.put("endpoint",endpoint);
        res.put("accessKeyId",accessKeyId);
        res.put("accessKeySecret",accessKeySecret);
        return CallBackResult.ok(res);
    }



    /**
     * 上传APP安装包
     */

    @PostMapping("/uploadAPP")
    public CallBackResult uploadAPP(@RequestParam("file") MultipartFile file,HttpServletRequest request) throws Exception {
        Integer appType =  Integer.valueOf(request.getParameter("appType"));
        String title = request.getParameter("title");
        String bundleId = request.getParameter("bundleId");
        String versionCode =  request.getParameter("versionCode");
        String suffix = null;
        String fileName = file.getOriginalFilename();

        if (fileName.contains(".")) {
            suffix = fileName.substring(fileName.lastIndexOf("."));
        }

        String packName = "Android";
        if(appType.equals(1)){
            if (!suffix.contains(".apk")) {
                return CallBackResult.fail("上传文件不匹配,应为apk文件");
            }
        } else if (appType.equals(2)) {
            if(bundleId == null || bundleId.isEmpty()){
                return CallBackResult.fail("包名不能为空");
            } else if(versionCode == null || versionCode.isEmpty()){
                return CallBackResult.fail("版本号不能为空");
            } else if(title == null || title.isEmpty()){
                return CallBackResult.fail("标题不能为空");
            }
            packName = "Ios";
            if (!suffix.contains(".ipa")) {
                return CallBackResult.fail("上传文件不匹配,应为ipa文件");
            }
        }
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        if (inputStream == null)
            return CallBackResult.fail("上传失败");


        String finalFileName = AliOssUtils.uploadBig(null, "package/" + packName + "Pack_", suffix, inputStream,multipartFileToFile(file));
        String path = "https://" + bucketName + "." + endpoint + "/" + finalFileName;
        AppVersionInfo appVersionInfo = appVersionInfoService.selectAppVersionByType(appType);
        if (appType.equals(2)) {
            try {

                String url = path.substring(0,path.lastIndexOf("/"));
                inputStream = NewPlist.createPlist(title,path,bundleId,versionCode);
                String iosPlistUrl =  AliOssUtils.upload(null, "package/IosPower_" , ".plist", inputStream);
                path = "https://" + bucketName + "." + endpoint + "/" + iosPlistUrl;
                appVersionInfo.setTitleName(title);
                appVersionInfo.setBundleId(bundleId);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        appVersionInfo.setUpdateTime(new Date());
        appVersionInfo.setApkDownloadUrl(path);
        if(versionCode!=null&&versionCode!=""){
            appVersionInfo.setCurrentVersion(versionCode);
        }
        int isTrue = appVersionInfoService.updateByPrimaryKey(appVersionInfo);
        if (isTrue > 0) {
            return CallBackResult.ok(path);
        } else {
            return CallBackResult.fail("上传失败");
        }
    }


    public static File multipartFileToFile(MultipartFile file) throws Exception {

        File toFile = null;
        if (file.equals("") || file.getSize() <= 0) {
            file = null;
        } else {
            InputStream ins = null;
            ins = file.getInputStream();
            toFile = new File(file.getOriginalFilename());
            inputStreamToFile(ins, toFile);
            ins.close();
        }
        return toFile;
    }


    //获取流文件
    private static void inputStreamToFile(InputStream ins, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
