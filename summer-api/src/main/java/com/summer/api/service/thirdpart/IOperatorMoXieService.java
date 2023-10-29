package com.summer.api.service.thirdpart;

import com.alibaba.fastjson.JSONObject;
import com.summer.dao.entity.UserMoXieDataWithBLOBs;

import java.util.Map;

/**
 * 解析魔蝎运营商报告
 */
public interface IOperatorMoXieService {
    /**
     * 解析魔蝎运营商报告
     *
     * @return
     */
    Map<String, Object> getMoXieDetail(Integer userId);

    UserMoXieDataWithBLOBs getUserMoXieData(Integer userId);

}
