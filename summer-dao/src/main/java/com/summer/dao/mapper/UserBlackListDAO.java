/* https://github.com/12641561 */
package com.summer.dao.mapper;

import com.summer.dao.entity.UserBlackList;
import com.summer.pojo.vo.UserBlackListVo;

import java.util.List;
import java.util.Map;

/**
 * 本文件由 https://github.com/12641561/mybatis-generator-core-chinese-1.3.5 自动生成
 *
 * @author tang code generator
 */
public interface UserBlackListDAO {
    /**
     * 根据ID删除
     *
     * @param id 主键ID
     * @return 返回删除成功的数量
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * 添加对象所有字段
     *
     * @param record 插入字段对象(必须含ID）
     * @return 返回添加成功的数量
     */
    int insert(UserBlackList record);

    /**
     * 添加对象对应字段
     *
     * @param record 插入字段对象(必须含ID）
     * @return 返回添加成功的数量
     */
    int insertSelective(UserBlackList record);

    /**
     * 根据ID查询
     *
     * @param id 主键ID
     * @return 返回查询的结果
     */
    UserBlackList selectByPrimaryKey(Integer id);

    /**
     * 根据ID修改对应字段
     *
     * @param record 修改字段对象(必须含ID）
     * @return 返回更新成功的数量
     */
    int updateByPrimaryKeySelective(UserBlackList record);

    /**
     * 根据ID修改所有字段(必须含ID）
     *
     * @param record 修改字段对象(必须含ID）
     * @return 返回更新成功的数量
     */
    int updateByPrimaryKey(UserBlackList record);

    /**
     * 通过手机号查询黑名单
     *
     * @param phone
     * @return
     */
    UserBlackList findByPhone(String phone);

    /**
     * 根据参数查询
     *
     * @param param
     * @return
     */
    List<UserBlackListVo> findByParam(Map<String, Object> param);

    int deleteBlackUser(Integer day);

    //List<RiskBlackLists> SyncBlackData();

    //List<RiskOrderList> synchronizeOrderData();


}
