package com.summer.service.manager;

import com.summer.dao.entity.InfoIndexInfo;
import com.summer.dao.entity.UserInfo;
import com.summer.dao.entity.UserOcrDataWithBLOBs;
import com.summer.dao.mapper.InfoIndexInfoDao;
import com.summer.dao.mapper.UserInfoMapper;
import com.summer.dao.mapper.UserOcrDataDAO;
import com.summer.enums.OcrStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 人脸OCR实名认证相关事务
 */
@Component
public class UserOcrDataManager {
    @Autowired
    private UserOcrDataDAO userOcrDataDAO;
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private InfoIndexInfoDao infoIndexInfoDAO;

    /**
     * 人脸OCR认证失败时更新事务
     */
    @Transactional
    public void authFail(Integer ocrDataId, String ocrFailCause, UserInfo userInfo, InfoIndexInfo infoIndexInfo) {
        userInfoMapper.updateByPrimaryKeySelective(userInfo);
        infoIndexInfoDAO.updateByPrimaryKeySelective(infoIndexInfo);
        // 将该用户其它认证中token置为失效
        userOcrDataDAO.updateStatusAndFailCauseById(ocrDataId, OcrStatusEnum.FAIL_AUTH.getValue(), ocrFailCause);
    }

    /**
     * 人脸OCR认证成功时更新事务
     */
    @Transactional
    public void authSuccess(Integer userId, Integer ocrDataId, UserInfo userInfo, InfoIndexInfo infoIndexInfo ,String jsonObjectFace) {
        // 成功修改用户认证资料
        userInfoMapper.updateByPrimaryKeySelective(userInfo);
        infoIndexInfoDAO.updateByPrimaryKeySelective(infoIndexInfo);
        UserOcrDataWithBLOBs userOcrData = new UserOcrDataWithBLOBs();
        userOcrData.setId(ocrDataId);
        userOcrData.setOcrDetail(jsonObjectFace);
        userOcrData.setStatus(OcrStatusEnum.SUCCESS_AUTH.getValue());
        // 修改OCR记录表认证中为认证成功
        userOcrDataDAO.updateByPrimaryKeySelective(userOcrData);
        // 将该用户其它认证中token置为失效
        userOcrDataDAO.updateStatusByUserIdAndStatus(userId, OcrStatusEnum.WAIT_AUTH.getValue(), OcrStatusEnum.CANCEL_AUTH.getValue());
    }

    /**
     * linkFace人脸OCR认证成功时更新事务
     */
    @Transactional
    public void linkFaceSuccess(UserInfo userInfo,InfoIndexInfo infoIndexInfo) {
        // 成功修改用户认证资料
        userInfoMapper.updateByPrimaryKeySelective(userInfo);
        infoIndexInfoDAO.updateByPrimaryKeySelective(infoIndexInfo);
    }
    /**
     * linkFace人脸OCR认证成功时更新事务
     */
    @Transactional
    public void linkFaceFail(InfoIndexInfo infoIndexInfo) {
        // 成功修改用户认证资料
        infoIndexInfoDAO.updateByPrimaryKeySelective(infoIndexInfo);
    }

    /**
     * 人脸认证中状态 事务更新
     */
    @Transactional
    public void authRunning(Integer ocrId, UserInfo userInfo, InfoIndexInfo infoIndexInfo) {
        userInfoMapper.updateByPrimaryKeySelective(userInfo);
        infoIndexInfoDAO.updateByPrimaryKeySelective(infoIndexInfo);
        UserOcrDataWithBLOBs userOcrData = new UserOcrDataWithBLOBs();
        userOcrData.setId(ocrId);
        userOcrData.setStatus(OcrStatusEnum.WAIT_AUTH.getValue());
        userOcrDataDAO.updateByPrimaryKeySelective(userOcrData);
    }
}
