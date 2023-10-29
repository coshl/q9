package com.summer.util;

import cn.hutool.core.io.IoUtil;
import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.*;
import com.jfinal.kit.PathKit;
import com.jfinal.kit.Ret;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

/**
 * 阿里云oss
 */
@Component
@Slf4j
public class AliOssUtils {

    private static String endpoint;
    private static String accessKeyId;
    private static String accessKeySecret;
    private static String bucketName;

    @Value("${oss.endpoint}")
    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    @Value("${oss.access_key_id}")
    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    @Value("${oss.access_key_secret}")
    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

    @Value("${oss.bucket_name}")
    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    /**
     * 上传
     *
     * @param prefix
     * @param file
     * @return
     */
    public static String upload(String bucketName, String prefix, File file) {
        if (file == null || file.isDirectory() || !file.exists()) {
            throw new IllegalArgumentException("参数不正确");
        }
        if (StringUtils.isBlank(bucketName)) {
            bucketName = AliOssUtils.bucketName;
        }
        // 创建OSSClient实例。
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        try {
            String suffix = ".jpg";
            if (file.getName().contains(".")) {
                suffix = file.getName().substring(file.getName().lastIndexOf("."));
            }
            String fileName = prefix + DateFormatUtils.format(new Date(), "yyyyMMddHHmmssS") + RandomStringUtils.randomNumeric(6) + suffix;
            ossClient.putObject(bucketName, fileName, file);
            return fileName;
        } catch (Exception e) {
            log.error("上传阿里云oss出错", e);
            throw new RuntimeException(e);
        } finally {
            // 关闭Client。
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

    /**
     * 上传
     *
     * @param prefix
     * @param suffix      后缀 eg: .jpg
     * @param inputStream
     * @return
     */
    public static String upload(String bucketName, String prefix, String suffix, InputStream inputStream) {
        if (inputStream == null) {
            throw new IllegalArgumentException("参数不正确");
        }
        if (!StringUtils.startsWith(suffix, ".")) {
            throw new IllegalArgumentException("参数不正确");
        }
        if (StringUtils.isBlank(bucketName)) {
            bucketName = AliOssUtils.bucketName;
        }
        // 创建OSSClient实例。
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        try {
            String fileName = prefix + DateFormatUtils.format(new Date(), "yyyyMMddHHmmssS") + RandomStringUtils.randomNumeric(6) + suffix;
            ossClient.putObject(bucketName, fileName, inputStream);
            return fileName;
        } catch (Exception e) {
            log.error("上传阿里云oss出错", e);
            throw new RuntimeException(e);
        } finally {
            // 关闭Client。
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }



    /**
     * 上传量大文件
     *
     * @param prefix
     * @param suffix
     * @param inputStream
     * @return
     */
    public static String uploadBig(String bucketName, String prefix, String suffix, InputStream inputStream,File sampleFile) {
        if (inputStream == null) {
            throw new IllegalArgumentException("参数不正确");
        }
        if (!StringUtils.startsWith(suffix, ".")) {
            throw new IllegalArgumentException("参数不正确");
        }
        if (StringUtils.isBlank(bucketName)) {
            bucketName = AliOssUtils.bucketName;
        }

        // 创建OSSClient实例。
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        try {
            String fileName = prefix + DateFormatUtils.format(new Date(), "ddHHmm") + RandomStringUtils.randomNumeric(6) + suffix;
            InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest(bucketName, fileName);
            InitiateMultipartUploadResult upresult = ossClient.initiateMultipartUpload(request);
            String uploadId = upresult.getUploadId();
            List<PartETag> partETags =  new ArrayList<>();
            final long partSize = 1 * 1024 * 1024L;   // 1MB

            long fileLength = sampleFile.length();
            int partCount = (int) (fileLength / partSize);
            if (fileLength % partSize != 0) {
                partCount++;
            }
            for (int i = 0; i < partCount; i++) {
                long startPos = i * partSize;
                long curPartSize = (i + 1 == partCount) ? (fileLength - startPos) : partSize;
                InputStream instream = new FileInputStream(sampleFile);
                // 跳过已经上传的分片。
                instream.skip(startPos);
                UploadPartRequest uploadPartRequest = new UploadPartRequest();
                uploadPartRequest.setBucketName(bucketName);
                uploadPartRequest.setKey(fileName);
                uploadPartRequest.setUploadId(uploadId);
                uploadPartRequest.setInputStream(instream);
                uploadPartRequest.setPartSize(curPartSize);
                uploadPartRequest.setPartNumber( i + 1);
                UploadPartResult uploadPartResult = ossClient.uploadPart(uploadPartRequest);
                partETags.add(uploadPartResult.getPartETag());
            }
            CompleteMultipartUploadRequest completeMultipartUploadRequest =
                    new CompleteMultipartUploadRequest(bucketName, fileName, uploadId, partETags);// 完成上传。
            CompleteMultipartUploadResult completeMultipartUploadResult = ossClient.completeMultipartUpload(completeMultipartUploadRequest);
            log.error("上传阿里云oss成功");

            return fileName;
        } catch (Exception e) {
            log.error("上传阿里云oss出错", e);
            throw new RuntimeException(e);
        } finally {
            // 关闭Client。
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }









    /**
     * 上传
     *
     * @param prefix
     * @param suffix 后缀 eg: .jpg
     * @param bytes
     * @return
     */
    public static String upload(String bucketName, String prefix, String suffix, byte[] bytes) {
        if (bytes == null) {
            throw new IllegalArgumentException("参数不正确");
        }
        if (StringUtils.isBlank(bucketName)) {
            bucketName = AliOssUtils.bucketName;
        }
        // 创建OSSClient实例。
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        try {
            String fileName = prefix + DateFormatUtils.format(new Date(), "yyyyMMddHHmmss") + RandomStringUtils.randomNumeric(6) + suffix;
            ossClient.putObject(bucketName, fileName, new ByteArrayInputStream(bytes));
            return fileName;
        } catch (Exception e) {
            log.error("上传阿里云oss出错", e);
            throw new RuntimeException(e);
        } finally {
            // 关闭Client。
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

    /**
     * 上传APP日志
     *
     * @param bytes
     * @return
     */
    public static String uploadAppLog(String bucketName, String fileName, byte[] bytes) {
        if (bytes == null) {
            throw new IllegalArgumentException("参数不正确");
        }
        if (StringUtils.isBlank(bucketName)) {
            bucketName = AliOssUtils.bucketName;
        }
        // 创建OSSClient实例。
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);

        try {
            ossClient.putObject(bucketName, fileName, new ByteArrayInputStream(bytes));
            return fileName;
        } catch (Exception e) {
            log.error("上传阿里云oss出错", e);
            throw new RuntimeException(e);
        } finally {
            // 关闭Client。
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

    /**
     * 授权访问
     *
     * @param bucketName
     * @param fileName
     * @param expirationSecond 有效时间（秒）
     * @param isWater          是否生成水印
     * @return
     */
    public static String getAuthUrl(String bucketName, String fileName, long expirationSecond, boolean isWater) {
        if (StringUtils.isBlank(bucketName)) {
            bucketName = AliOssUtils.bucketName;
        }
        // 创建OSSClient实例。
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        try {
            // 设置URL过期时间
            Date expiration = new Date(new Date().getTime() + expirationSecond * 1000);
            GeneratePresignedUrlRequest req = new GeneratePresignedUrlRequest(bucketName, fileName, HttpMethod.GET);
            req.setExpiration(expiration);
            if (isWater) {
                // 图片处理样式-设置水印文字（资付）
                String style = "image/watermark,text_6LWE5LuY,type_d3F5LXplbmhlaQ,size_20,t_100,shadow_100,color_fdfdfd,g_se,y_10,x_10";
                req.setProcess(style);
            }
            // 生成URL。
            URL url = ossClient.generatePresignedUrl(req);
            return url.toString();
        } catch (Exception e) {
            log.error("上传阿里云oss出错", e);
            throw new RuntimeException(e);
        } finally {
            // 关闭Client。
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

    /**
     * 上传报告
     *
     * @param bucketName
     * @param fileName
     * @param content
     * @return
     */
    public static String uploadReport(String bucketName, String fileName, String content) {
        if (StringUtils.isBlank(content) || StringUtils.isBlank(fileName)) {
            throw new IllegalArgumentException("参数不正确");
        }
        if (StringUtils.isBlank(bucketName)) {
            bucketName = AliOssUtils.bucketName;
        }
        // 创建OSSClient实例。
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        try {
            InputStream is = new ByteArrayInputStream(content.getBytes("UTF-8"));
            ossClient.putObject(bucketName, fileName, is);
            //String url = "https://" + bucketName + "." + endpoint + "/" + fileName;
            //https://q9juehui.oss-cn-hongkong.aliyuncs.com/report/29AUTH_202208181560103875953213440_wuhua.txt
            return fileName;
        } catch (Exception e) {
            log.error("上传阿里云oss出错", e);
            throw new RuntimeException(e);
        } finally {
            // 关闭Client。
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

    /**
     * 下载
     *
     * @param fileName
     * @return
     */
    public static Ret downloadFile(String fileName) {
        try {
            OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);

            OSSObject ossObject = ossClient.getObject(AliOssUtils.bucketName, fileName);
            if (ossObject == null) {
                return null;
            }
            InputStream is = ossObject.getObjectContent();
            byte[] bytes = IoUtil.readBytes(is);
            generateFile(bytes, getTempFilePath(), fileName.replace("/", "_"));
            return Ret.ok("msg", "用户证件照下载成功");
        } catch (Exception e) {
            log.error("用户证件照转换出错", e);
        }
        return Ret.fail("msg", "用户证件照下载失败");
    }

    /**
     * 下载
     *
     * @param fileName
     * @return
     */
    public static InputStream downloadFileInputStream(String fileName) {
        InputStream is = null;
        try {
            OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
            fileName = fileName.substring(fileName.lastIndexOf("/")+1);
            OSSObject ossObject = ossClient.getObject(AliOssUtils.bucketName, fileName);
            if (ossObject == null) {
                return null;
            }
            is = ossObject.getObjectContent();

        } catch (Exception e) {
            log.error("用户证件照转换出错", e);
        }
      return is;
    }


    public static String getTempFilePath() {
        return PathKit.getRootClassPath() + "/tempFile";
    }

    /**
     * 上传
     *
     * @param prefix
     * @param suffix 后缀 eg: .jpg
     * @param bytes
     * @return
     */
    public static String uploadPub(String bucketName, String prefix, String suffix, byte[] bytes) {
        if (bytes == null) {
            throw new IllegalArgumentException("参数不正确");
        }
        if (StringUtils.isBlank(bucketName)) {
            bucketName = AliOssUtils.bucketName;
        }
        // 创建OSSClient实例。
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        try {
            String fileName = prefix + DateFormatUtils.format(new Date(), "yyyyMMddHHmmssS") + RandomStringUtils.randomNumeric(6) + suffix;
            ossClient.putObject(bucketName, fileName, new ByteArrayInputStream(bytes));
            return fileName;
        } catch (Exception e) {
            log.error("上传阿里云oss出错", e);
            throw new RuntimeException(e);
        } finally {
            // 关闭Client。
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

    /**
     * 根据byte数组，生成文件
     */
    private static void generateFile(byte[] bfile, String filePath, String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        try {
            File dir = new File(filePath);
            if (!dir.exists()) {//判断文件目录是否存在
                dir.mkdirs();
            }
            fos = new FileOutputStream(new File(filePath + "/" + fileName));
            bos = new BufferedOutputStream(fos);
            bos.write(bfile);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }


    /**
     * 读取阿里云oss 报告
     *
     * @param fileName
     * @return
     */
    public static String readReport(String fileName) {
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        try {
            OSSObject ossObject = ossClient.getObject(AliOssUtils.bucketName, fileName);
            if (ossObject == null) {
                return "";
            }
            return IoUtil.read(ossObject.getObjectContent(), "UTF-8");
        } catch (Exception e) {
            log.error("读取阿里云oss报告出错", e);
            return "";
        } finally {
            // 关闭Client。
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }



}
