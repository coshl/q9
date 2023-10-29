package com.summer.util;

import java.util.HashMap;
import java.util.Map;


public class OcrStatusUtils {
    public static String getStatus(String code) {
        switch (code) {
            case "PHOTO_SERVICE_ERROR":
                return "数据源服务服务出错，详情见字段 reason";
            case "ENCODING_ERROR":
                return "参数非UTF-8编码";
            case "DOWNLOAD_TIMEOUT":
                return "网络地址图片获取超时";
            case "DOWNLOAD_ERROR":
                return "网络地址图片获取失败";
            case "IMAGE_ID_NOT_EXIST":
                return "图片不存在";
            case "IMAGE_FILE_SIZE_TOO_BIG":
                return "图片体积过大";
            case "NO_FACE_DETECTED":
                return "上传的图片未检测出人脸";
            case "CORRUPT_IMAGE":
                return "文件不是图片文件或已经损坏";
            case "INVALID_IMAGE_FORMAT_OR_SIZE":
                return "图片大小或格式不符合要求";
            case "INVALID_ARGUMENT":
                return "请求参数错误，具体原因见 reason 字段内容";
            case "UNAUTHORIZED":
                return "账号或密钥错误";
            case "KEY_EXPIRED":
                return "账号过期，具体情况见 reason 字段内容";
            case "RATE_LIMIT_EXCEEDED":
                return "调用频率超出限额";
            case "NO_PERMISSION":
                return "无调用权限";
            case "OUT_OF_QUOTA":
                return "调用次数超出限额";
            case "NOT_FOUND":
                return "请求路径错误";
            case "INTERNAL_ERROR":
                return "服务器内部错误";
            default:
                return code;
        }
    }


    public static String getValidity(String code) {
        switch (code) {
            case "Gongan photo doesn’t exist":
                return "姓名和身份证号匹配，近照不存在";
            case "Name and idcard number doesn’t match":
                return "姓名与身份证号不匹配";
            case "Invalid idcard number":
                return "查无此身份证号";
            default:
                return code;
        }
    }
}

