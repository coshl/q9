package com.summer.api.service;

import com.summer.dao.entity.CountCollectionAssessment;

import java.util.List;
import java.util.Map;

/**
 * ICountCollectionAssessmentService
 * 催收报表
 *
 * @author : GeZhuo
 * Date: 2019/2/25
 */
public interface ICountCollectionAssessmentService {

    /**
     * 查询每日贷款报表
     *
     * @param params
     * @return
     */
    public List<CountCollectionAssessment> findParams(Map<String, Object> params);

}
