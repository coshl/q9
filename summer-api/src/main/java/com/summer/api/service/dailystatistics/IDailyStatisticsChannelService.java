package com.summer.api.service.dailystatistics;

import com.github.pagehelper.PageInfo;
import com.summer.pojo.vo.PlateformChannelSideVo;

import java.math.BigDecimal;
import java.util.Map;

public interface IDailyStatisticsChannelService {

    /**
     * 渠道进量统计
     *
     * @param params
     * @return
     */
    Map<String, Object> findParams(Map<String, Object> params);

    /**
     * 渠道放款统计
     *
     * @param params
     * @return
     */
    Map<String, Object> findChannelLend(Map<String, Object> params);

    /**
     * 我方渠道统计
     *
     * @param params
     * @return
     */
    Map<String, Object> findMyChannelStatistics(Map<String, Object> params);

    /**
     * 渠道方统计
     *
     * @param params
     * @return
     */
    PageInfo<PlateformChannelSideVo> findChannelStatistics(Map<String, Object> params);

    /**
     * 修改系数
     *
     * @param id
     * @return
     */
    int updateCoefficient(Integer id, BigDecimal dedutionCoefficient);


}
