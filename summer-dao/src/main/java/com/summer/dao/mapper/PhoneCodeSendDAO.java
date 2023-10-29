package com.summer.dao.mapper;

import com.summer.dao.entity.PhoneCodeSend;

import java.util.List;


public interface PhoneCodeSendDAO {

    /**
     * 添加对象所有字
     * @return 返回添加成功的数量
     */
    int insert(PhoneCodeSend phoneCodeSend);

    List<PhoneCodeSend> selectCode(String phone);
}
