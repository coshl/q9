package com.summer.service.sms;

import java.util.Map;

public interface ISmsService {
    String SMS_LIMIT_KEY = "SMS_LIMIT_KEY_";

    Boolean batchSend(String phones, String content);

    Boolean batchSendRemind(String phones, String contentInfo);

    Boolean sendCode(String phones, String code, boolean isCheckPhoneNum, Integer type);

    Map<String, String> getRedisMap();
}
