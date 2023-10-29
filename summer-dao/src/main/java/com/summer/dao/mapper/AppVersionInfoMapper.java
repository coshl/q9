package com.summer.dao.mapper;

import com.summer.dao.entity.AppVersionInfo;
import com.summer.pojo.vo.AppVersionInfoVo;

import java.util.List;
import java.util.Map;

public interface AppVersionInfoMapper {
    /**
     * 根据参数查找
     *
     * @return
     */
    AppVersionInfo findByParam(Map<String, Object> map);


    int deleteByPrimaryKey(Integer id);

    int insert(AppVersionInfo record);

    int insertSelective(AppVersionInfo record);

    AppVersionInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AppVersionInfo record);

    int updateByPrimaryKeyWithBLOBs(AppVersionInfo record);

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