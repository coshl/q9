package com.summer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.summer.dao.entity.UserMobileDataRecord;

/**
 * <p>
 * 运营商行为日志记录表 服务类
 * </p>
 *
 * @author jobob
 * @since 2020-09-13
 */
public interface IUserMobileDataRecordService extends IService<UserMobileDataRecord> {

    void insertRecord(Long userId, String logId, String mobile, String h5Url);

    UserMobileDataRecord findUserMobileDataRecordByUserIdAndLogId(Long userId,String logId);

    void updateRecord(Long userId, String logId, Integer status);

    void updateRecordByUserIdAndMxId(Long userId, Long mxId, Integer status);

    void updateRecord(Long userId, String logId, Long mxId, Integer status, String notifyContent);
}
