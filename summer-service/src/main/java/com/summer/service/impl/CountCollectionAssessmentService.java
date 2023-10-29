package com.summer.service.impl;

import com.summer.dao.entity.CountCollectionAssessment;
import com.summer.dao.mapper.CountCollectionAssessmentDAO;
import com.summer.api.service.ICountCollectionAssessmentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * CountCollectionAssessmentService
 *
 * @author : GeZhuo
 * Date: 2019/2/25
 */

@Service
public class CountCollectionAssessmentService implements ICountCollectionAssessmentService {

    @Resource
    private CountCollectionAssessmentDAO countCollectionAssessmentDAO;

    @Override
    public List<CountCollectionAssessment> findParams(Map<String, Object> params) {
        return countCollectionAssessmentDAO.findParams(params);
    }

}
