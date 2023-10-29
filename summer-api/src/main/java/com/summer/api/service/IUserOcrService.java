package com.summer.api.service;

import com.summer.dao.entity.UserInfo;
import com.summer.dao.entity.UserMoXieDataWithBLOBs;
import com.summer.dao.entity.UserOcrData;
import com.summer.dao.entity.UserOcrDataWithBLOBs;
import com.summer.util.CallBackResult;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface IUserOcrService {

    /**
     * @mbg.generated 2020-08-05
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * @mbg.generated 2020-08-05
     */
    int insert(UserOcrDataWithBLOBs record);

    /**
     * @mbg.generated 2020-08-05
     */
    int insertSelective(UserOcrDataWithBLOBs record);

    /**
     * @mbg.generated 2020-08-05
     */
    UserOcrDataWithBLOBs selectByPrimaryKey(Integer id);

    /**
     * @mbg.generated 2020-08-05
     */
    UserOcrDataWithBLOBs selectByUserId(Integer userId);

    /**
     * @mbg.generated 2020-08-05
     */
    int updateByPrimaryKeySelective(UserOcrDataWithBLOBs record);

    /**
     * @mbg.generated 2020-08-05
     */
    int updateByPrimaryKeyWithBLOBs(UserOcrDataWithBLOBs record);

    /**
     * @mbg.generated 2020-08-05
     */
    int updateByPrimaryKey(UserOcrData record);

    UserOcrDataWithBLOBs selectByUserIdAndToken(Integer id, String token);

    Boolean updateStatusById(Integer id, Integer value);

    List<UserOcrData> listByStatus(Integer status);

    Boolean updateStatusByUserIdAndStatus(Integer userId, Integer sourceStatus, Integer targetStatus);

    Boolean updateStatusAndFailCauseById(Integer id, Integer status, String failCause);

    Boolean ocrAuth(UserOcrData userOcrData);

}
