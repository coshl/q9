/*
 *
 *
 *
 */
package com.summer.service.impl;

import com.summer.dao.entity.SourceUserLoginInfo;
import com.summer.dao.mapper.SourceUserLoginInfoMapper;
import com.summer.api.service.SourceUserLoginInfoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Service - 借款用户登陆信息表
 */
@Service
public class SourceUserLoginInfoServiceImpl implements SourceUserLoginInfoService {

    @Resource
    private SourceUserLoginInfoMapper sourceUserLoginInfoMapper;


    @Override
    public SourceUserLoginInfo findByPhoneAndH5Code(String phoneNumber, String sourceH5Code) throws Exception {
        return sourceUserLoginInfoMapper.findByPhoneAndH5Code(phoneNumber, sourceH5Code);
    }

    @Transactional
    @Override
    public void save(SourceUserLoginInfo sourceUserLoginInfo) throws Exception {
        sourceUserLoginInfoMapper.save(sourceUserLoginInfo);
    }

    @Transactional
    @Override
    public void update(SourceUserLoginInfo sourceUserLoginInfo) throws Exception {
        sourceUserLoginInfoMapper.update(sourceUserLoginInfo);
    }

    @Override
    public SourceUserLoginInfo findById(Long id) {
        return sourceUserLoginInfoMapper.findById(id);
    }

    @Override
    public SourceUserLoginInfo findByPhoneNumber(Map<String, Object> map) {
        return sourceUserLoginInfoMapper.findByPhoneNumber(map);
    }

    @Override
    public int deleteAll(String[] ids) {
        return sourceUserLoginInfoMapper.deleteAll(ids);
    }

    @Override
    public Long count(Map<String, Object> map) {
        return sourceUserLoginInfoMapper.count(map);
    }

    @Override
    public int updateByPhoneNumber(SourceUserLoginInfo sourceUserLoginInfo) {
        return sourceUserLoginInfoMapper.updateByPhoneNumber(sourceUserLoginInfo);
    }

    @Override
    public List<SourceUserLoginInfo> findByPhone(String phone) {
        return sourceUserLoginInfoMapper.findByPhone(phone);
    }

    @Override
    public Integer findBySourceChannelCode(SourceUserLoginInfo sourceUserLoginInfo) {
        return sourceUserLoginInfoMapper.findBySourceChannelCode(sourceUserLoginInfo);
    }
}