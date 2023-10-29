package com.summer.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.summer.dao.entity.PlatformUser;
import com.summer.dao.mapper.CollectionStatisticsMapper;
import com.summer.api.service.CollectionStatisticsService;
import com.summer.api.service.IPlateformUserService;
import com.summer.util.CallBackResult;
import com.summer.pojo.vo.CollectionStatisticsVo;
import com.summer.pojo.vo.OperatorVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 催收业务统计
 */
@Service
public class CollectionStatisticsServiceImpl implements CollectionStatisticsService {

    @Resource
    private CollectionStatisticsMapper statisticsDAO;

    @Resource
    private IPlateformUserService plateformUserService;

    @Override
    public String queryCollectionStatistics(String jsonData, PlatformUser platformUser) {
        //从jsonData获取数据
        Map<String, Object> params = JSONObject.parseObject(jsonData);
        //催收专员、当日催收员只能看到自己的
        if (platformUser.getRoleId() == 8 || platformUser.getRoleId() == 10) {
            params.put("userName", platformUser.getUserName());
        }
        PageHelper.startPage(params);
        List<CollectionStatisticsVo> list = statisticsDAO.findByParams(params);
        PageInfo<CollectionStatisticsVo> pageInfo = new PageInfo<>(list);
        Map<String, Object> map = new HashMap<>();
        //催收专员、当日催收列表
        List<OperatorVo> users = plateformUserService.findByCuishouRoleId(810);
        map.put("pageInfo", pageInfo);
        map.put("collection", users);
        return CallBackResult.returnJson(map);
    }

    @Override
    public List<CollectionStatisticsVo> findByParams(Map<String, Object> params) {
        return statisticsDAO.findByParams(params);
    }
}
