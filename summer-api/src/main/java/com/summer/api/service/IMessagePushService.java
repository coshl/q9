package com.summer.api.service;

import com.summer.dao.entity.MessagePush;

import java.util.List;
import java.util.Map;

/**
 * @author ls
 * @Title:
 * @date 2019/2/19 11:00
 */

public interface IMessagePushService {
    int insert(MessagePush data);

    int deleteByPrimaryKey(int id);

    List<MessagePush> selectByParams(Map<String, Object> param);
}
