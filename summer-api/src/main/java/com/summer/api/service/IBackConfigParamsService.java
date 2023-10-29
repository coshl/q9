package com.summer.api.service;

import com.summer.util.BackConfigParams;
import com.summer.pojo.vo.BackConfigParamsVo;

import java.util.HashMap;
import java.util.List;

/**
 * 系统配置Service接口类
 */
public interface IBackConfigParamsService {
    /**
     * @param params sysType参数分类
     * @return
     */
    public List<BackConfigParams> findParams(HashMap<String, Object> params);

    /**
     * 更新
     *
     * @param list
     * @return
     */
    public int updateValue(List<BackConfigParams> list);

    /**
     * 通过key查询对应配置规则
     *
     * @param syskey
     * @return
     */
    BackConfigParamsVo findBySysKey(String syskey);

    /**
     * 根据SysKey更新对应的值
     *
     * @param backConfigParams
     * @return
     */
    int updateBySyskey(BackConfigParams backConfigParams);
}
