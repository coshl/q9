package com.summer.web.util;
import com.summer.util.AliOssUtils;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateFormatUtils;

import java.io.*;
import java.util.Date;

@Slf4j
public class NewPlist {

        public static InputStream createPlist(String title, String path, String bundleId,
                                         String versionCode) throws Exception {

            log.info("==========开始创建plist文件{},标题为",title);
            String plist = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<!DOCTYPE plist PUBLIC \"-//Apple//DTD PLIST 1.0//EN\" \"http://www.apple.com/DTDs/PropertyList-1.0.dtd\">\n" +
                    "<plist version=\"1.0\">\n" +
                    "    <dict>\n" +
                    "        <key>items</key>\n" +
                    "        <array>\n" +
                    "            <dict>\n" +
                    "                <key>assets</key>\n" +
                    "                <array>\n" +
                    "                    <dict>\n" +
                    "                        <key>kind</key>\n" +
                    "                        <string>software-package</string>\n" +
                    "                        <key>url</key>\n" +
                    "                        <string>"+path+"</string>\n" +
                    "                    </dict>\n" +
                    "                </array>\n" +
                    "                <key>metadata</key>\n" +
                    "                <dict>\n" +
                    "                    <key>bundle-identifier</key>\n" +
                    "                    <string>"+bundleId+"</string>\n" +
                    "                    <key>bundle-version</key>\n" +
                    "                    <string>"+versionCode+"</string>\n" +
                    "                    <key>kind</key>\n" +
                    "                    <string>software</string>\n" +
                    "                    <key>title</key>\n" +
                    "                    <string>"+title+"</string>\n" +
                    "                </dict>\n" +
                    "            </dict>\n" +
                    "        </array>\n" +
                    "    </dict>\n" +
                    "</plist>";
            InputStream inputStream =  new ByteArrayInputStream(plist.getBytes());
            return inputStream;
        }

}
