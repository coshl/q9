package com.summer.api.service.risk;

/**
 * @ClassName ： IFirstTrialCpsService
 * @Description ：操盘手风控
 * @Author：
 * @Date ：2019/10/18 14:50
 * @Version ：V1.0
 */
public interface IFirstTrialCpsService {
    /**
     * 风控初审的方法(操盘手)
     */
    void riskFirstTrial(String jsonData);
}
