package com.summer.api.service;

import com.summer.dao.entity.SourceUserLoginInfo;

import java.util.List;
import java.util.Map;

/**
 * Service - 借款用户登陆信息表
 */
public interface SourceUserLoginInfoService {

    /**
     * 根据手机号和 sourceH5Code 查询
     *
     * @param phoneNumber
     * @param sourceH5Code
     * @return
     * @throws Exception
     */
    SourceUserLoginInfo findByPhoneAndH5Code(String phoneNumber, String sourceH5Code) throws Exception;

    /**
     * 将用户信息添加注册表
     *
     * @param sourceUserLoginInfo
     * @return
     * @throws Exception
     */
    void save(SourceUserLoginInfo sourceUserLoginInfo) throws Exception;

    /**
     * 更新用户注册信息
     *
     * @param sourceUserLoginInfo
     * @return
     * @throws Exception
     */
    void update(SourceUserLoginInfo sourceUserLoginInfo) throws Exception;

    /**
     * 根据id查找
     *
     * @return
     */
    SourceUserLoginInfo findById(Long id);

    /**
     * 通过手机号码查询
     *
     * @return
     */
    SourceUserLoginInfo findByPhoneNumber(Map<String, Object> map);

    /**
     * 批量删除
     *
     * @return
     */
    int deleteAll(String[] ids);

    /**
     * 统计
     *
     * @param map
     * @return
     */
    Long count(Map<String, Object> map);

    /**
     * 根据手机号码更新
     *
     * @param sourceUserLoginInfo
     * @return
     */
    int updateByPhoneNumber(SourceUserLoginInfo sourceUserLoginInfo);

    /**
     * 通过手机号码查找
     *
     * @param phone
     * @return
     */
    List<SourceUserLoginInfo> findByPhone(String phone);

    /**
     * 根据渠道id查询
     *
     * @param sourceUserLoginInfo
     * @return
     */
    Integer findBySourceChannelCode(SourceUserLoginInfo sourceUserLoginInfo);
}