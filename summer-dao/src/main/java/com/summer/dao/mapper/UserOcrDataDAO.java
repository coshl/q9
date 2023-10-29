package com.summer.dao.mapper;

import com.summer.dao.entity.UserOcrData;
import com.summer.dao.entity.UserOcrDataWithBLOBs;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserOcrDataDAO {
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

    UserOcrDataWithBLOBs selectByUserIdAndToken(@Param("userId") Integer userId, @Param("token") String token);

    List<UserOcrData> listByStatus(Integer status);

    int updateStatusByUserIdAndStatus(@Param("userId") Integer userId, @Param("sourceStatus") Integer sourceStatus, @Param("targetStatus") Integer targetStatus);

    int updateStatusAndFailCauseById(Integer id, Integer status, String failCause);
}