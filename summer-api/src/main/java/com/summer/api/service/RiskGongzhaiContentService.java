package com.summer.api.service;

import com.alibaba.fastjson.JSONObject;
import com.summer.dao.entity.RiskGongzhaiContent;
import com.summer.dao.entity.UserInfo;
import com.summer.util.CallBackResult;

/**
 * 业务相关的接口
 */
//TODO 00 共债
public interface RiskGongzhaiContentService {
    //获取全景雷达
    CallBackResult queryLeiDa(Integer userId,Integer status);

    //获取duotou
    CallBackResult getQinTianDuoTou(Integer userId,Integer status);

    //获取公债
    CallBackResult getJuGuangGongZhai(Integer userId,Integer status);

    //获取Radar公债
    CallBackResult getRadarGongZhai(Integer userId,Integer status);


    CallBackResult getTanZhenC(Integer userId,Integer status);


    CallBackResult getPayOrderDept(Integer userId,Integer status);


}
