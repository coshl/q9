package com.summer.util;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipException;

/**
 * gzip压缩工具.
 *
 * @author fyduan
 * @date 16/4/20
 */
public class GzipUtil {

    /**
     * 压缩数据返回byte数组.
     *
     * @param source 待压缩数据字符串.
     * @return 压缩后byte数组
     */
    public static byte[] compress2Bytes(String source) {
        try {
            return compress(source.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public static byte[] compress(byte[] source) {
        if (source == null || source.length == 0) {
            return source;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        GZIPOutputStream gzipOutputStream = null;
        try {
            gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream);
            gzipOutputStream.write(source);
            gzipOutputStream.finish();
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            // TODO LOGGER.error("执行GZip压缩失败", e);
        } finally {
            if (gzipOutputStream != null) {
                try {
                    gzipOutputStream.close();
                } catch (Exception e) {
                    // TODO LOGGER.error("关闭 GZIPInputStream 异常 : ", e);
                }
            }
        }
        return null;
    }

    /**
     * 压缩数据并转换为BASE64编码字符串.
     *
     * @param source 源数据
     * @return 压缩后字符串
     */
    public static String compressToStringForBase64(byte[] source) {
        byte[] bytes = compress(source);
        if (bytes == null || bytes.length == 0) {
            return "";
        }
        return Base64.getEncoder().encodeToString(source);
    }

    /**
     * 解压
     *
     * @param data 源数据
     * @return 解压后数据
     * @throws IOException
     */
    public static String decompress(byte[] data) throws IOException {
        if (data == null || data.length == 0) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        GZIPInputStream gzipInputStream = null;
        try {
            gzipInputStream = new GZIPInputStream(in);
            byte[] buffer = new byte[1024];
            int n;
            while ((n = gzipInputStream.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }
            out.flush();
            return out.toString("UTF-8");
        } catch (ZipException e) {
            // TODO LOGGER.error(String.format("解压缩数据 ZipException 异常, data.length = [%d]", data.length), e);
            throw e;
        } catch (IOException e) {
            // TODO LOGGER.error(String.format("解压缩数据 IOException 异常, data.length = [%d]", data.length), e);
            throw e;
        } finally {
            if (gzipInputStream != null) {
                try {
                    gzipInputStream.close();
                } catch (Exception e) {
                    // TODO LOGGER.error("关闭 GZIPInputStream 异常 : ", e);
                }
            }
            try {
                in.close();
            } catch (Exception e) {
                // TODO LOGGER.error("关闭 ByteArrayInputStream 异常 : ", e);
            }
        }
    }

    /**
     * 解压BASE64编码后的压缩数据.
     *
     * @param source BASE64编码过的压缩数据
     * @return 原始数据
     * @throws IOException
     */
    public static String decompressWithBase64(String source) throws IOException {
        byte[] bytes = Base64.getDecoder().decode(source);
        return decompress(bytes);
    }
}
