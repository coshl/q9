package com.summer.dao.mapper;

import com.summer.dao.entity.InfoIndexInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

public interface InfoIndexInfoDao {

    /**
     * 处理button按钮事件
     *
     * @param map
     */
    void updateInfoUserInfoBorrowStatus(HashMap<String, Object> map);

    int updateIndexInfoByUserId(InfoIndexInfo indexInfo);

    int updateIndexInfoByUserIdOld(InfoIndexInfo indexInfo);

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
     * 解绑更新银行卡认证状态
     *
     * @param map
     */
    void updateInfoUserInfoBankStatus(HashMap<String, Object> map);

    int updateBorrow(Map<String, Object> map);
    
    @Insert("insert into bank_bind_info(user_id,user_name,message,crate_time,code) values(#{userId},#{userName},#{info},DATE_FORMAT(now(),'%Y-%m-%d'),#{code})")
    int insertBankBindFailureInfo(@Param("userId")int userId,@Param("userName")String userName,@Param("info")String info,@Param("code")String code);
    
    String selectMerchantFlag();
}
