package com.summer.api.service;

import com.summer.dao.entity.AppVersionInfo;
import com.summer.pojo.vo.AppVersionInfoVo;

import java.util.List;
import java.util.Map;

/**
 * APP版本信息的service接口
 */
public interface IAppVersionInfoService {

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
    int insert(AppVersionInfo record);

    /**
     * 添加对象对应字段
     *
     * @param record 插入字段对象(必须含ID）
     * @return 返回添加成功的数量
     */
    int insertSelective(AppVersionInfo record);

    /**
     * 根据ID查询
     *
     * @param id 主键ID
     * @return 返回查询的结果
     */
    AppVersionInfo selectByPrimaryKey(Integer id);

    /**
     * 根据ID修改对应字段
     *
     * @param record 修改字段对象(必须含ID）
     * @return 返回更新成功的数量
     */
    int updateByPrimaryKeySelective(AppVersionInfo record);

    /**
     * 根据ID修改字段（包含二进制大对象）
     *
     * @param record 修改字段对象(必须含ID）
     * @return 返回更新成功的数量
     */
    int updateByPrimaryKeyWithBLOBs(AppVersionInfo record);

    /**
     * 根据ID修改所有字段(必须含ID）
     *
     * @param record 修改字段对象(必须含ID）
     * @return 返回更新成功的数量
     */
    int updateByPrimaryKey(AppVersionInfo record);

    /**
     * 根据类型查找APP
     *
     * @param appType
     * @return
     */
    AppVersionInfo selectAppVersionByType(Integer appType);

    /**
     * 根据参数查找
     *
     * @param param
     * @return
     */
    List<AppVersionInfoVo> selectByParam(Map<String, Object> param);
    
    
    List<Map<String,Object>> getAppVersionInfo(Integer appType);

}
