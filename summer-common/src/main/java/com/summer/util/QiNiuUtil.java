package com.summer.util;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.qiniu.util.UrlSafeBase64;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Component
@Slf4j
public class QiNiuUtil {
    private static String accessKey = "N_JHcKG7PJBiJjBXRMoszp4jV2MFIKB56zHdt3SS";
    private static String secretKey = "NdzHBF_SqE_rjNghpn9SIUkkXSrBSERtwR00VnrG";
    private static String bucket;

    @Value("${qiniu.bucket}")
    public void setBucket(String buc) {
        bucket = buc;
    }

    private static Zone zone = Zone.zone0();
    private static String domainOfBucket = "qiniu.51hongwen.com";
    private static long expireInSeconds = -1;

    /**
     * 上传本地文件
     *
     * @param localFilePath 本地文件完整路径
     * @param key           文件云端存储的名称
     * @param override      是否覆盖同名同位置文件
     * @return
     */
    public static boolean upload(String localFilePath, String key, boolean override) {
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(zone);
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //...生成上传凭证，然后准备上传
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken;
        if (override) {
            upToken = auth.uploadToken(bucket, key);//覆盖上传凭证
        } else {
            upToken = auth.uploadToken(bucket);
        }
        try {
            Response response = uploadManager.put(localFilePath, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);
            return true;
        } catch (QiniuException ex) {
            log.error("QiNiuUtil.upload", ex);
            return false;
        }
    }

    /**
     * 上传内存内容
     *
     * @param content  内存内容
     * @param key      文件云端存储的名称
     * @param override 是否覆盖同名同位置文件
     * @return
     */
    public static boolean uploadContent(String content, String key, boolean override) {
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(zone);
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //...生成上传凭证，然后准备上传
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken;
        if (override) {
            upToken = auth.uploadToken(bucket, key);//覆盖上传凭证
        } else {
            upToken = auth.uploadToken(bucket);
        }
        try {
            byte[] uploadBytes = content.getBytes("utf-8");
            Response response = uploadManager.put(uploadBytes, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * 上传Base64图片
     *
     * @param l        图片没经过base64处理的原图字节大小，获取文件大小的时候，切记要通过文件流的方式获取。而不是通过图片标签然后转换后获取。
     * @param file64   图片base64字符串
     * @param key      文件云端存储的名称
     * @param override 是否覆盖同名同位置文件
     * @return
     * @throws IOException
     */
    public static boolean uploadBase64(int l, String file64, String key, boolean override) throws IOException {
        /*FileInputStream fis = null;
        int l = (int) (new File(localFilePath).length());
        byte[] src = new byte[l];
        try {
            fis = new FileInputStream(new File(localFilePath));
            fis.read(src);
        }catch (FileNotFoundException e){
            e.printStackTrace();
            log.error(e.getMsg());
            log.error("图片文件读取失败");
            return false;
        }
        String file64 = Base64.encodeToString(src, 0);*/

        Auth auth = getAuth();
        String upToken;
        if (override) {
            upToken = auth.uploadToken(bucket, key);//覆盖上传凭证
        } else {
            upToken = auth.uploadToken(bucket);
        }

        String url =
                "http://upload.qiniup.com/putb64/" + l + "/key/" + UrlSafeBase64.encodeToString(key);
        //非华东空间需要根据注意事项 1 修改上传域名
        RequestBody rb = RequestBody.create(null, file64);
        Request request = new Request.Builder().
                url(url).
                addHeader("Content-Type", "application/octet-stream")
                .addHeader("Authorization", "UpToken " + upToken)
                .post(rb).build();
        //System.out.println(request.headers());
        OkHttpClient client = new OkHttpClient();
        okhttp3.Response response = client.newCall(request).execute();
        //System.out.println(response);
        return response.isSuccessful();
    }

    /**
     * 获取文件访问地址
     *
     * @param fileName 文件云端存储的名称
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String fileUrl(String fileName) throws UnsupportedEncodingException {
        String encodedFileName = URLEncoder.encode(fileName, "utf-8");
        String publicUrl = String.format("%s/%s", domainOfBucket, encodedFileName);
        Auth auth = getAuth();
        if (-1 == expireInSeconds) {
            return auth.privateDownloadUrl(publicUrl);
        }
        return auth.privateDownloadUrl(publicUrl, expireInSeconds);
    }

    public static Auth getAuth() {
        Auth auth = Auth.create(accessKey, secretKey);
        return auth;
    }
}
