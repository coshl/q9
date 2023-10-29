package com.summer.service.impl;

import cn.hutool.core.util.IdcardUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.base.MoreObjects;
import com.summer.api.service.IUserContactsService;
import com.summer.api.service.IUserInfoService;
import com.summer.api.service.RiskFkContentService;
import com.summer.dao.entity.*;
import com.summer.dao.mapper.IBackConfigParamsDao;
import com.summer.dao.mapper.RiskFkContentMapper;
import com.summer.dao.mapper.UserMoXieDataDAO;
import com.summer.enums.DeviceTypeEnum;
import com.summer.enums.MobileSwitch;
import com.summer.enums.YesOrNo;
import com.summer.service.IRiskFkContentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.summer.service.yys.BoxApi;
import com.summer.util.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.ws.rs.HEAD;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author jobob
 * @since 2020-07-30
 */
@Service
@Slf4j
public class RiskFkContentServiceImpl extends ServiceImpl<RiskFkContentMapper, RiskFkContent> implements IRiskFkContentService, RiskFkContentService {
    @Value("${risk.account-code}")
    private String accountCode;

    @Autowired
    private IUserInfoService userInfoService;
    @Autowired
    private IUserContactsService userContactsService;
    @Autowired
    private UserMoXieDataDAO userMoXieDataDAO;
    @Autowired
    private IBackConfigParamsDao backConfigParamsDao;

    /**
     * 生成风控报告
     *
     * @param userId 用户id
     */
    //TODO 00风控
    @Override
    public CallBackResult initReportByUserId(Integer userId) {
        log.info("获取用户之谜分userId:{}",userId);
        String mobile_switch =  backConfigParamsDao.findStrValue("mobile_switch");
        UserInfo userInfo = userInfoService.selectByPrimaryKey(userId);
        if (Objects.isNull(userInfo)) {
            log.info("用户不存在userId:{}", userId);
            return CallBackResult.fail("用户不存在");
        }
        // 通讯录
        List<UserContacts> userContactsList = userContactsService.findContatsByUserId(userId);
        if (CollectionUtils.isEmpty(userContactsList)) {
            log.info("用户{}通讯录为空", userId);
            return CallBackResult.fail("通讯录为空");
        }
        // 运营商报告
        UserMoXieDataWithBLOBs userMoXieDataWithBLOBs = userMoXieDataDAO.selectDataByUserId(userId);
        if (Objects.isNull(userMoXieDataWithBLOBs)) {
            log.info("请先完成运营商认证");
            return CallBackResult.fail("请先完成运营商认证");
        }
        if (Objects.equals(userMoXieDataWithBLOBs.getStatus(), Byte.parseByte(MobileSwitch.BOX.getValue()))){
            Map<String, Object> bizData = new HashMap<String, Object>() {
                private static final long serialVersionUID = -1L;
                {
                    //产品编号，用以区分产品，用于监控展示和分析，由商户自行设置
                    put("version", "1.26");
                    put("account_code", accountCode);
                    // 申请贷款时间；格式为yyyy-mm-dd HH:mm:ss
                    put("apply_time", DateUtil.formatTimeYmdHms(new Date()));
                    put("phone", userInfo.getPhone());
                    put("name", userInfo.getRealName());
                    put("id_no", userInfo.getIdCard());
                    put("sex", IdcardUtil.getGenderByIdCard(userInfo.getIdCard()) == 1 ? "男":"女");
                    put("age", IdcardUtil.getAgeByIdCard(userInfo.getIdCard()));
                    put("contact_elink_1_name", userInfo.getFirstContactName());
                    put("contact_elink_1", userInfo.getFirstContactPhone());
                    put("contact_elink_2_name", userInfo.getSecondContactName());
                    put("contact_elink_2", userInfo.getSecondContactPhone());
                    //put("phone_os", DeviceTypeEnum.getDescByValue(userInfo.getClientType()));安卓或是ios
                    /*// 两个紧急联系人
                    List<Map<String, Object>> eContactsArr = new ArrayList<>();
                    eContactsArr.add(new HashMap<String, Object>() {{
                        put("contact_name", userInfo.getSecondContactName());
                        put("contact_phone", userInfo.getSecondContactPhone());
                    }});
                    put("e_contacts", eContactsArr);*/
                    put("operatorUrl", userMoXieDataWithBLOBs.getMxRaw());
                    // 通讯录(请确认通讯录不能做任何过滤筛选处理；一个名字下如果有多个号码，请拆分成多条记录)
                    List<Map<String, Object>> contactsArr = new ArrayList<>();
                    for (UserContacts userContacts : userContactsList) {
                        Map<String, Object> eContacts = new HashMap<>();
                        eContacts.put("name", userContacts.getContactName());
                        eContacts.put("mobile", userContacts.getContactPhone());
                        //eContacts.put("update_time", DateUtil.formatTimeYmdHms(userContacts.getCreateTime()));
                        contactsArr.add(eContacts);
                    }
                    put("contacts", contactsArr);
                }
            };
            try {
                //String bizDataJsonStr = JSONObject.toJSONString(bizData);

                /**
                 * 每调用一次获取一条最新的
                 * */
                QueryWrapper<RiskFkContent> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("user_id", userId).eq("status", YesOrNo.YES.getValue());
                RiskFkContent isExist = getOne(queryWrapper);
                if (Objects.isNull(isExist)) {
                    RiskFkContent insertRiskFkContent = new RiskFkContent();
                    insertRiskFkContent.setUserId(userId.longValue());
                    insertRiskFkContent.setStatus(YesOrNo.No.getValue());
                    insertRiskFkContent.setType(userMoXieDataWithBLOBs.getStatus().intValue());
                    insertRiskFkContent.setCreateTime(new Date());
                    insertRiskFkContent.setUpdateTime(new Date());
                    save(insertRiskFkContent);
                    isExist = insertRiskFkContent;
                }
                //String requestParamJsonData = JSONObject.toJSONString(requestParam);
                String result = BoxApi.boxQueryModel(bizData);
                log.info("BOX风控报告请求结果:{}", result);
                    /*"code": 0,
                        "message": "success",
                        "requestId": "f00eb48f6f9745ff94520968e1a4b685",
                        "time": "2021-10-23 23:30:26",
                        "data": {
                    "task_id": "18d4021c-c684-11e9-a893-7675bfa22ea1",
                            "time": "2021-10-23 23:30:26",
                            "version": "1.26",
                            "status": 0*/
                //String result = HttpUtil.postJsonData(riskReportApiUrl, requestParamJsonData);
                if (StringUtils.isNotBlank(result)) {
                    // 存储指迷风控报告
                    JSONObject riskDataJSON = JSONObject.parseObject(result);
                    RiskFkContent riskFkContent = new RiskFkContent();
                    if (riskDataJSON.containsKey("data")) {
                        JSONObject riskData = riskDataJSON.getJSONObject("data");
                        //riskFkContent.setScore(riskData.getBigDecimal("score"));先不放分数后面单独请求
                        riskFkContent.setTaskId(riskData.getString("task_id"));
                        riskFkContent.setReportContent(result);
                        riskFkContent.setStatus(YesOrNo.YES.getValue());
                        riskFkContent.setUpdateTime(new Date());
                        riskFkContent.setId(isExist.getId());
                    } else {
                        riskFkContent.setStatus(YesOrNo.No.getValue());
                        riskFkContent.setUpdateTime(new Date());
                        riskFkContent.setId(isExist.getId());
                        riskFkContent.setFailCause(result);
                    }
                    updateById(riskFkContent);
                    return CallBackResult.ok();
                }
            } catch (Exception e) {
                log.error("BOX风控获取失败:{}", e);
            }
        } if (Objects.equals(userMoXieDataWithBLOBs.getStatus(), Byte.parseByte(MobileSwitch.XINGPAN.getValue())) || Objects.equals(userMoXieDataWithBLOBs.getStatus(), Byte.parseByte(MobileSwitch.WUHUA.getValue()))){
            /**
             * 每调用一次获取一条最新的
             * */
            QueryWrapper<RiskFkContent> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", userId).eq("status", YesOrNo.YES.getValue());
            RiskFkContent isExist = getOne(queryWrapper);
            if (Objects.isNull(isExist)) {
                RiskFkContent insertRiskFkContent = new RiskFkContent();
                insertRiskFkContent.setUserId(userId.longValue());
                insertRiskFkContent.setStatus(YesOrNo.No.getValue());
                insertRiskFkContent.setType(userMoXieDataWithBLOBs.getStatus().intValue());
                insertRiskFkContent.setCreateTime(new Date());
                insertRiskFkContent.setUpdateTime(new Date());
                save(insertRiskFkContent);
                isExist = insertRiskFkContent;
            }
            //{"score":"130.25120682458018","history_apply":{"idcard_3h_cnt":0,"idcard_1d_cnt":0,"idcard_7d_cnt":1,"idcard_60d_cnt":11,"mobile_60d_cnt":3,"idcard_30d_cnt":11,"idcard_14d_cnt":1,"mobile_3d_cnt":0,"mobile_3h_cnt":0,"idcard_3d_cnt":0,"mobile_12h_cnt":0,"mobile_1d_cnt":0,"idcard_12h_cnt":0,"mobile_30d_cnt":3,"mobile_1h_cnt":0,"idcard_1h_cnt":0,"mobile_14d_cnt":1,"mobile_7d_cnt":1},"return_info":"success","return_code":"0","request_id":"20201021150749-20a914d8-136c-11eb-b8f2-06637bb6c7f2"}
            String result = "{\"score\":\"500\",\"history_apply\":{\"idcard_3h_cnt\":0,\"idcard_1d_cnt\":0,\"idcard_7d_cnt\":1,\"idcard_60d_cnt\":11,\"mobile_60d_cnt\":3,\"idcard_30d_cnt\":11," +
                    "\"idcard_14d_cnt\":1,\"mobile_3d_cnt\":0,\"mobile_3h_cnt\":0,\"idcard_3d_cnt\":0,\"mobile_12h_cnt\":0,\"mobile_1d_cnt\":0,\"idcard_12h_cnt\":0,\"mobile_30d_cnt\":3,\"mobile_1h_cnt\":0," +
                    "\"idcard_1h_cnt\":0,\"mobile_14d_cnt\":1,\"mobile_7d_cnt\":1},\"return_info\":\"success\",\"return_code\":\"0\",\"request_id\":\"20201021150749-20a914d8-136c-11eb-b8f2-06637bb6c7f2\"}";
            // 存储指迷风控报告
            JSONObject riskDataJSON = JSONObject.parseObject(result);
            RiskFkContent riskFkContent = new RiskFkContent();
            //JSONObject riskData = riskDataJSON.getJSONObject("data").getJSONObject("risk_data");
            riskFkContent.setScore(riskDataJSON.getBigDecimal("score"));
            riskFkContent.setTaskId(riskDataJSON.getString("request_id"));
            riskFkContent.setReportContent(riskDataJSON.toJSONString());
            riskFkContent.setStatus(YesOrNo.YES.getValue());
            riskFkContent.setUpdateTime(new Date());
            riskFkContent.setId(isExist.getId());
            updateById(riskFkContent);
            return CallBackResult.ok();
        }
        return CallBackResult.fail();
    }

    @Override
    public CallBackResult<BigDecimal> getScoreByUserId(Integer userId) {
        try {
            Assert.notNull(userId, "userId不能为空");
            UserMoXieDataWithBLOBs userMoXieDataWithBLOBs = userMoXieDataDAO.selectDataByUserId(userId);
            if (Objects.isNull(userMoXieDataWithBLOBs)) {
                return CallBackResult.fail("请先完成运营商认证");
            }
            RiskFkContent riskFkContent = getFirstByUserIdAndType(userId, userMoXieDataWithBLOBs.getStatus().intValue());
            return CallBackResult.ok(riskFkContent);
        } catch (IllegalArgumentException e) {
            log.error("参数错误:{}", e.getMessage());
            return CallBackResult.fail(e.getMessage());
        } catch (Exception e) {
            log.error("查询异常:{}", e);
            return CallBackResult.fail("查询异常");
        }
    }

    public static String generateSignature(Map<String, String> data, String userkey) throws Exception {
        Set<String> keySet = data.keySet();
        String[] keyArray = keySet.toArray(new String[keySet.size()]);
        Arrays.sort(keyArray);
        StringBuilder sb = new StringBuilder();
        for (String k : keyArray) {
            if (k.equals("sign")) {
                continue;
            }
            if (data.get(k).trim().length() > 0) // 参数值为空，则不参与签名
            {
                sb.append(data.get(k).trim());
            }
        }
        sb.append(userkey);
        return MD5Util.md5(sb.toString());
    }


    /**
     * 获取指迷分
     */
    @Override
    public CallBackResult<BigDecimal> getRiskScoreByUserId(Integer userId) {
        try {
            QueryWrapper<RiskFkContent> queryWrapper = new QueryWrapper<>();
            queryWrapper.select("score").eq("user_id", userId).orderByDesc("id").last("LIMIT 1");
            RiskFkContent riskFkContent = getOne(queryWrapper);
            return CallBackResult.ok(Objects.nonNull(riskFkContent) ? riskFkContent.getScore() : null);
        } catch (Exception e) {
            log.error("指迷分获取失败:{}", e);
            return CallBackResult.fail("指迷分获取失败");
        }
    }

    /**
     * 获取首个风控内容
     */
    @Override
    public RiskFkContent getFirstByUserIdAndType(Integer userId, Integer type) {
        QueryWrapper<RiskFkContent> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId).eq("type", type);
        queryWrapper.last("LIMIT 1");
        queryWrapper.orderByDesc("id");
        RiskFkContent riskFkContent = getOne(queryWrapper);
        return riskFkContent;
    }

}
