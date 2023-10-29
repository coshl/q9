package com.summer.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.summer.api.service.IUserInfoService;
import com.summer.api.service.RiskGongzhaiContentService;
import com.summer.dao.entity.*;
import com.summer.dao.mapper.RiskGongzhaiContentMapper;
import com.summer.enums.YesOrNo;
import com.summer.service.IRiskGongzhaiContentService;
import com.summer.service.fk.QingTianZhuApi;
import com.summer.service.impl.black.JuGuangApi;
import com.summer.service.impl.thirdpart.RadarApi;
import com.summer.service.yys.BoxApi;
import com.summer.util.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * 共债服务实现类
 * </p>
 *
 * @author jobob
 * @since 2020-07-31
 */
@Service
@Slf4j
public class RiskGongzhaiContentServiceImpl extends ServiceImpl<RiskGongzhaiContentMapper, RiskGongzhaiContent> implements IRiskGongzhaiContentService, RiskGongzhaiContentService {
    @Autowired
    private IUserInfoService userInfoService;
    @Autowired
    private RiskGz101ServiceImpl riskGz101Service;
    @Autowired
    private RiskGzTcServiceImpl riskGzTcService;
    @Autowired
    private RiskGzXpafServiceImpl riskGzXpafService;
    @Autowired
    private RiskGongzhaiLzServiceImpl riskGongzhaiLzService;
    @Autowired
    private RiskGongZhaiAFuServiceImpl riskGongZhaiAFuService;
    @Autowired
    private RadarApi radarApi;
    @Autowired
    private QingTianZhuApi qingTianZhuApi;

    public CallBackResult queryLeiDa(Integer userId, Integer status) {
        UserInfo userInfo = userInfoService.selectByPrimaryKey(userId);
        if (Objects.isNull(userInfo))
            return CallBackResult.fail("用户信息为空");
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id", userId);
        queryWrapper.orderByDesc("id");
        queryWrapper.last("limit 1");
        RiskGzTc riskGzTc = riskGzTcService.getOne(queryWrapper);

        if (Objects.nonNull(riskGzTc) && status == 1) {
//            if (halfHourTime(riskGzTc.getCreateTime().toString())) {
            return CallBackResult.ok(riskGzTc.getReport());
//            }

        }

        Map params = new HashMap<>();
        params.put("name", userInfo.getRealName());
        params.put("phone", userInfo.getPhone());
        params.put("identNumber", userInfo.getIdCard());
        String result = null;
        try {
            result = qingTianZhuApi.leiDa(params);
            log.info(userInfo.getRealName() + "全景雷达查询结果" + result);
            JSONObject jsonResult = JSON.parseObject(result);
            if ("success".equals(jsonResult.get("respCode"))) {
                JSONObject jsResult = jsonResult.getJSONObject("attachment");
                RiskGzTc saveRiskGzTc = new RiskGzTc();
                saveRiskGzTc.setCreateTime(new Date());
                saveRiskGzTc.setReport(jsResult.toJSONString());
                saveRiskGzTc.setStatus(YesOrNo.YES.getValue());
                saveRiskGzTc.setUserId(Long.valueOf(userInfo.getId()));
                riskGzTcService.save(saveRiskGzTc);
                return CallBackResult.ok(jsResult.toJSONString());
            }
            return CallBackResult.fail(jsonResult.getString("respDesc"));
        } catch (Exception e) {
            log.error("获取GK全景雷达查询数据共债失败");
            e.printStackTrace();
            return CallBackResult.fail(e.getMessage());
        }
    }

    @Override
    public CallBackResult getQinTianDuoTou(Integer userId, Integer status) {
        UserInfo userInfo = userInfoService.selectByPrimaryKey(userId);
        if (Objects.isNull(userInfo))
            return CallBackResult.fail("用户信息为空");
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id", userId);
        queryWrapper.orderByDesc("id");
        queryWrapper.last("limit 1");
        RiskGzXpaf riskGzXpaf = riskGzXpafService.getOne(queryWrapper);

        if (Objects.nonNull(riskGzXpaf) && status == 1) {
//            if (halfHourTime(riskGzXpaf.getCreateTime().toString())) {
            return CallBackResult.ok(riskGzXpaf.getReport());
//            }
        }

        Map params = new HashMap<>();
        params.put("name", userInfo.getRealName());
        params.put("phone", userInfo.getPhone());
        params.put("identNumber", userInfo.getIdCard());
        String result = null;
        try {
            result = qingTianZhuApi.duoTou(params);
            JSONObject jsonResult = JSON.parseObject(result);
            log.info(userInfo.getRealName() + "多头查询结果" + jsonResult.get("respDesc"));
            if ("success".equals(jsonResult.get("respCode"))) {
                JSONObject jsResult = jsonResult.getJSONObject("attachment");
                RiskGzXpaf saveRiskGzXpaf = new RiskGzXpaf();
                saveRiskGzXpaf.setCreateTime(new Date());
                saveRiskGzXpaf.setReport(jsResult.toJSONString());
                saveRiskGzXpaf.setStatus(YesOrNo.YES.getValue());
                saveRiskGzXpaf.setUserId(Long.valueOf(userInfo.getId()));
                riskGzXpafService.save(saveRiskGzXpaf);
                return CallBackResult.ok(jsResult.toJSONString());
            }
            return CallBackResult.fail(jsonResult.getString("respDesc"));
        } catch (Exception e) {
            log.error("获取多头查询数据失败");
            e.printStackTrace();
            return CallBackResult.fail(e.getMessage());
        }
    }


    public static Boolean halfHourTime(String date) {
        // 按照传入的格式生成一个simpledateformate对象
        long diff = 0;
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long nd = 1000 * 24 * 60 * 60;// 1小时的毫秒数
        log.info("时间判断=======================date=" + date);
        // 获得两个时间的毫秒时间差异
        try {
            Date now = new Date();
            Date start = sd.parse(date);
            diff = now.getTime() - start.getTime();
            log.info("时间判断=======================diff=" + diff);
            log.info("时间判断=======================nd=" + nd);
            if (diff < nd) {
                log.info("没超过24小时,不重新获取");
                return true;
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public CallBackResult getTanZhenC(Integer userId, Integer status) {
        UserInfo userInfo = userInfoService.selectByPrimaryKey(userId);
        if (Objects.isNull(userInfo))
            return CallBackResult.fail("用户信息为空");
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id", userId);
        queryWrapper.orderByDesc("id");
        queryWrapper.last("limit 1");
        RiskGongzhaiAfu riskGongzhaiAfu = riskGongZhaiAFuService.getOne(queryWrapper);

        if (Objects.nonNull(riskGongzhaiAfu) && status == 1) {
//            if (halfHourTime(riskGongzhaiAfu.getCreateTime().toString())) {
            return CallBackResult.ok(riskGongzhaiAfu.getReport());
//            }
        }

        Map params = new HashMap<>();
        params.put("name", userInfo.getRealName());
        params.put("phone", userInfo.getPhone());
        params.put("identNumber", userInfo.getIdCard());
        String result = null;
        try {
            result = qingTianZhuApi.tanZhen(params);
            log.info(userInfo.getRealName() + "探针C查询结果" + result);
            JSONObject jsonResult = JSON.parseObject(result);
            if ("success".equals(jsonResult.get("respCode"))) {
                JSONObject jsResult = jsonResult.getJSONObject("attachment");
                RiskGongzhaiAfu saveRiskGongzhaiAfu = new RiskGongzhaiAfu();
                saveRiskGongzhaiAfu.setCreateTime(new Date());
                saveRiskGongzhaiAfu.setReport(jsResult.toJSONString());
                saveRiskGongzhaiAfu.setStatus(YesOrNo.YES.getValue());
                saveRiskGongzhaiAfu.setUserId(Long.valueOf(userInfo.getId()));
                riskGongZhaiAFuService.save(saveRiskGongzhaiAfu);
                return CallBackResult.ok(jsResult.toJSONString());
            }
            return CallBackResult.fail(jsonResult.getString("respDesc"));
        } catch (Exception e) {
            log.error("获取探针C查询数据失败");
            e.printStackTrace();
            return CallBackResult.fail(e.getMessage());
        }
    }

    @Override
    public CallBackResult getPayOrderDept(Integer userId, Integer status) {
        UserInfo userInfo = userInfoService.selectByPrimaryKey(userId);
        if (Objects.isNull(userInfo))
            return CallBackResult.fail("用户信息为空");

        Map params = new HashMap<>();
        params.put("name", userInfo.getRealName());
        String result = null;
        try {
            result = JuGuangApi.getPayOrder(userInfo.getRealName());
            log.info(userInfo.getRealName() + "支付订单查询结果" + result);
            JSONObject jsonResult = JSON.parseObject(result).getJSONObject("data");
            return CallBackResult.ok(jsonResult);
        } catch (Exception e) {
            log.error("获取支付订单查询结果数据失败");
            e.printStackTrace();
            return CallBackResult.fail(e.getMessage());
        }
    }


    @Override
    public CallBackResult getRadarGongZhai(Integer userId, Integer status) {
        UserInfo userInfo = userInfoService.selectByPrimaryKey(userId);
        if (Objects.isNull(userInfo))
            return CallBackResult.fail("用户信息为空");
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id", userId);
        queryWrapper.orderByDesc("id");
        queryWrapper.last("limit 1");
        RiskGongzhaiLz riskGongzhaiLz = riskGongzhaiLzService.getOne(queryWrapper);
        if (Objects.nonNull(riskGongzhaiLz) && status == 1) {
//            if (halfHourTime(riskGongzhaiLz.getCreateTime().toString())) {
            return CallBackResult.ok(riskGongzhaiLz.getReport());
//            }
        }
        log.info("=====================================hello新的三方请求");
        String result = radarApi.getRadar(userInfo.getPhone(), userInfo.getIdCard(), userInfo.getRealName());
        if (StringUtils.isNotBlank(result)) {
            JSONObject jsonResult = JSON.parseObject(result);
            if (jsonResult.containsKey("code") && jsonResult.getIntValue("code") == 200) {
                RiskGongzhaiLz saveRiskGongzhaiLz = new RiskGongzhaiLz();
                saveRiskGongzhaiLz.setCreateTime(new Date());
                saveRiskGongzhaiLz.setReport(jsonResult.getJSONObject("result").toJSONString());
                saveRiskGongzhaiLz.setStatus(YesOrNo.YES.getValue());
                saveRiskGongzhaiLz.setUserId(Long.valueOf(userInfo.getId()));
                riskGongzhaiLzService.save(saveRiskGongzhaiLz);
                return CallBackResult.ok(jsonResult.getJSONObject("result").toJSONString());
            }
        }
        return CallBackResult.fail("radar共债没有找到该用户的共债记录");
    }


    @Override
    public CallBackResult getJuGuangGongZhai(Integer userId, Integer status) {
        UserInfo userInfo = userInfoService.selectByPrimaryKey(userId);
        if (Objects.isNull(userInfo))
            return CallBackResult.fail("用户信息为空");
        //聚光公债用最新的
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id", userId);
        queryWrapper.last("limit 1");
        RiskGz101 riskGz101 = riskGz101Service.getOne(queryWrapper);
        if (Objects.nonNull(riskGz101) && status == 1) {
//            if (halfHourTime(riskGongzhaiLz.getCreateTime().toString())) {
            Map map = new HashMap();
            map.put("name", userInfo.getRealName());
            map.put("phone", userInfo.getPhone());
            map.put("data", riskGz101.getReport());
            return CallBackResult.ok(map);
//            }
        }
        String result = JuGuangApi.getJoinDebt(userInfo.getIdCard());
        if (StringUtils.isNotBlank(result)) {
            JSONObject jsonResult = JSON.parseObject(result);
            //JSONObject jsResult = jsonResult.getJSONObject("data");
            RiskGz101 saveRisk101 = new RiskGz101();
            saveRisk101.setCreateTime(new Date());
            saveRisk101.setReport(jsonResult.toJSONString());
            saveRisk101.setStatus(YesOrNo.YES.getValue());
            saveRisk101.setUserId(Long.valueOf(userInfo.getId()));
            riskGz101Service.save(saveRisk101);
            Map map = new HashMap();
            map.put("name", userInfo.getRealName());
            map.put("phone", userInfo.getPhone());
            map.put("data", jsonResult);
            return CallBackResult.ok(map);
        }
        return CallBackResult.fail("聚光共债没有找到该用户的共债记录");
    }


}
