package com.summer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.summer.dao.entity.UserMobileDataRecord;
import com.summer.dao.mapper.UserMobileDataRecordMapper;
import com.summer.service.IUserMobileDataRecordService;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 运营商行为日志记录表 服务实现类
 * </p>
 *
 * @author jobob
 * @since 2020-09-13
 */
@Service
public class UserMobileDataRecordServiceImpl extends ServiceImpl<UserMobileDataRecordMapper, UserMobileDataRecord> implements IUserMobileDataRecordService {


    @Override
    public void insertRecord(Long userId, String logId, String mobile, String h5Url) {
        UserMobileDataRecord userMobileDataRecord = new UserMobileDataRecord();
        userMobileDataRecord.setUserId(userId);
        userMobileDataRecord.setMobile(mobile);
        userMobileDataRecord.setH5Url(h5Url);
        userMobileDataRecord.setLogId(logId);
        userMobileDataRecord.setStatus(1);
        userMobileDataRecord.setCreateTime(new Date());
        userMobileDataRecord.setUpdateTime(new Date());
        save(userMobileDataRecord);
    }

    @Override
    public UserMobileDataRecord findUserMobileDataRecordByUserIdAndLogId(Long userId, String logId) {
        LambdaQueryWrapper<UserMobileDataRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserMobileDataRecord::getLogId, logId);
        wrapper.eq(UserMobileDataRecord::getUserId, userId);
        return getOne(wrapper);
    }

    @Override
    public void updateRecord(Long userId, String logId, Integer status) {
        LambdaUpdateWrapper<UserMobileDataRecord> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(UserMobileDataRecord::getUpdateTime, new Date())
                .set(UserMobileDataRecord::getStatus, status);
        wrapper.eq(UserMobileDataRecord::getUserId, userId).eq(UserMobileDataRecord::getLogId, logId);
        update(wrapper);
    }

    @Override
    public void updateRecordByUserIdAndMxId(Long userId, Long mxId, Integer status) {
        LambdaUpdateWrapper<UserMobileDataRecord> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(UserMobileDataRecord::getUpdateTime, new Date())
                .set(UserMobileDataRecord::getStatus, status);
        wrapper.eq(UserMobileDataRecord::getUserId, userId).eq(UserMobileDataRecord::getMxId, mxId);
        update(wrapper);
    }

    @Override
    public void updateRecord(Long userId, String logId, Long mxId, Integer status, String notifyContent) {
        LambdaUpdateWrapper<UserMobileDataRecord> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(UserMobileDataRecord::getUpdateTime, new Date())
                .set(UserMobileDataRecord::getStatus, status)
                .set(UserMobileDataRecord::getMxId, mxId)
                .set(UserMobileDataRecord::getStatus, status)
                .set(UserMobileDataRecord::getNotifyContent, notifyContent)
        ;
        wrapper.eq(UserMobileDataRecord::getUserId, userId);//.eq(UserMobileDataRecord::getLogId, logId);
        update(wrapper);
    }
}
