package com.summer.api.service;

import com.summer.dao.entity.InfoIndexInfo;
import com.summer.dao.entity.UserInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import java.util.List;
import java.util.Map;

/**
 * 用户认证统计的Service接口类
 */
public interface IInfoIndexInfoService {
    /**
     * 根据ID删除
     *
     * @param userId 主键ID
     * @return 返回删除成功的数量
     */
    int deleteByPrimaryKey(Integer userId);

    /**
     * 添加对象所有字段
     *
     * @param record 插入字段对象(必须含ID）
     * @return 返回添加成功的数量
     */
    int insert(InfoIndexInfo record);

    /**
     * 添加对象对应字段
     *
     * @param record 插入字段对象(必须含ID）
     * @return 返回添加成功的数量
     */
    int insertSelective(InfoIndexInfo record);

    /**
     * 根据ID查询
     *
     * @param userId 主键ID
     * @return 返回查询的结果
     */
    InfoIndexInfo selectByPrimaryKey(Integer userId);

    /**
     * 根据ID修改对应字段
     *
     * @param record 修改字段对象(必须含ID）
     * @return 返回更新成功的数量
     */
    int updateByPrimaryKeySelective(InfoIndexInfo record);

    /**
     * 根据ID修改所有字段(必须含ID）
     *
     * @param record 修改字段对象(必须含ID）
     * @return 返回更新成功的数量
     */
    int updateByPrimaryKey(InfoIndexInfo record);

    /**
     * 根据参数查询
     *
     * @param params
     * @return
     */
    List<InfoIndexInfo> selectByParam(Map<String, Object> params);

    /**
     * 修改用户表和用户认证统计表的认证状态为0及清空之前认证信息
     *
     * @param userInfo
     */
    void updateUserInfoAndInfoIndexInfo(UserInfo userInfo, HttpServletRequest request, HttpServletResponse response);
    
    /**
     * 插入银行卡认证失败
     * @return
     */
   int insertBankBindFailureInfo(int userId,String userName,String info,String code);
   
   String selectMerchantFlag();
}
