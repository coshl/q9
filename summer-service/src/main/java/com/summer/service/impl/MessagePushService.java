package com.summer.service.impl;

import com.summer.dao.entity.MessagePush;
import com.summer.dao.mapper.MessagePushDAO;
import com.summer.api.service.IMessagePushService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author ls
 * @Title:
 * @date 2019/2/19 11:10
 */
@Service
public class MessagePushService implements IMessagePushService {
    @Resource
    private MessagePushDAO messagePushDAO;

    @Override
    public int insert(MessagePush data) {
        return messagePushDAO.insert(data);
    }

    @Override
    public int deleteByPrimaryKey(int id) {
        return messagePushDAO.deleteByPrimaryKey(id);
    }

    @Override
    public List<MessagePush> selectByParams(Map<String, Object> param) {
        return messagePushDAO.selectByParams(param);
    }
}
