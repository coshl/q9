package com.summer.dao.mapper;

import com.summer.util.BackConfigParams;
import com.summer.pojo.vo.BackConfigParamsVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
public interface IBackConfigParamsDao {
    /**
     * @param params sysType参数分类
     * @return
     */
    @Cacheable(value = "IBackConfigParamsDao")
    List<BackConfigParams> findParams(HashMap<String, Object> params);

    List<BackConfigParams> listBySysType(@Param("sysType") String sysType);



    /**
     * 更新
     *
     * @param list
     * @return
     */
    @CacheEvict(value="IBackConfigParamsDao",allEntries=true)// 清空缓存
    public int updateValue(List<BackConfigParams> list);

    /**
     * 通过key查询对应配置规则
     *
     * @param syskey
     * @return
     */
    @Cacheable(value = "IBackConfigParamsDao")
    BackConfigParamsVo findBySysKey(String syskey);

    @Cacheable(value = "IBackConfigParamsDao")
    String findStrValue(String syskey);

    @Cacheable(value = "IBackConfigParamsDao")
    BackConfigParams configParams(String syskey);

    /**
     * 根据SysKey更新对应的值
     *
     * @param backConfigParams
     * @return
     */
    @CacheEvict(value="IBackConfigParamsDao",allEntries=true)// 清空缓存
    int updateBySyskey(BackConfigParams backConfigParams);

    List<BackConfigParamsVo> findByGroup(String group);
    /**
     * 利用ID更新
     *
     * @param backConfigParams
     * @return
     */
    @CacheEvict(value="IBackConfigParamsDao",allEntries=true)// 清空userlist缓存
     int updateById(BackConfigParams backConfigParams);

}
