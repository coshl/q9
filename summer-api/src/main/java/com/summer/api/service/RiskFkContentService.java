package com.summer.api.service;

import com.summer.util.CallBackResult;

import java.math.BigDecimal;

public interface RiskFkContentService {
    CallBackResult initReportByUserId(Integer userId);

    CallBackResult getScoreByUserId(Integer userId);

    CallBackResult<BigDecimal> getRiskScoreByUserId(Integer userId);
}
