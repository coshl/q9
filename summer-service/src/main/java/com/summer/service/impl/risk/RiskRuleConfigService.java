package com.summer.service.impl.risk;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.summer.dao.entity.Present;
import com.summer.dao.entity.RiskRuleConfig;
import com.summer.dao.mapper.RiskRuleConfigDAO;
import com.summer.api.service.risk.IRiskRuleConfigService;
import com.summer.util.Constant;
import com.summer.util.risk.ConstantByRisk;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 风控规则配置Service接口的实现类
 */
@Service
public class RiskRuleConfigService implements IRiskRuleConfigService {
    @Resource
    private RiskRuleConfigDAO riskRuleConfigDAO;


    @Override
    public int deleteByPrimaryKey(Integer id) {
        return riskRuleConfigDAO.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(RiskRuleConfig record) {
        return riskRuleConfigDAO.insert(record);
    }

    @Override
    public int insertSelective(RiskRuleConfig record) {
        return riskRuleConfigDAO.insertSelective(record);
    }

    @Override
    public RiskRuleConfig selectByPrimaryKey(Integer id) {
        return riskRuleConfigDAO.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(RiskRuleConfig record) {
        return riskRuleConfigDAO.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(RiskRuleConfig record) {
        return riskRuleConfigDAO.updateByPrimaryKey(record);
    }

    @Override
    public List<RiskRuleConfig> findAllRiskRule() {
        List<RiskRuleConfig> allRiskRule = riskRuleConfigDAO.findAllRiskRule();
        List<RiskRuleConfig> riskRuleConfigs = new ArrayList<>();
        if (null != allRiskRule && allRiskRule.size() > Constant.LIST_SIZE_LENTH_ZORE) {
            for (RiskRuleConfig riskRuleConfig : allRiskRule) {
                Map<String, Object> ruleValueMap = JSONObject.parseObject(riskRuleConfig.getRuleValue());
                String ruleKey = riskRuleConfig.getRuleKey();
                switch (ruleKey) {
                    case ConstantByRisk.R_TXL_KEY1:
                        riskRuleConfig.setRuleValue("低于" + ruleValueMap.get("intValue") + "个");
                        riskRuleConfigs.add(riskRuleConfig);
                        continue;
                    case ConstantByRisk.R_TXL_KEY2:
                        riskRuleConfig.setRuleValue("敏感词：" + ruleValueMap.get("strValueArray") + " 大于" + ruleValueMap.get("intValue"));
                        riskRuleConfigs.add(riskRuleConfig);
                        continue;
                    case ConstantByRisk.R_TXL_KEY3:
                        riskRuleConfig.setRuleValue("敏感词：" + ruleValueMap.get("strValueArray"));
                        riskRuleConfigs.add(riskRuleConfig);
                        continue;
                    case ConstantByRisk.R_LN:
                        riskRuleConfig.setRuleValue("小于:" + ruleValueMap.get("minValue") + "岁" + " 或大于：" + ruleValueMap.get("maxValue"));
                        riskRuleConfigs.add(riskRuleConfig);
                        continue;
                    case ConstantByRisk.R_YYS1:
                        riskRuleConfig.setRuleValue(ruleValueMap.get("strValue") + "");
                        riskRuleConfigs.add(riskRuleConfig);
                        continue;
                    case ConstantByRisk.R_YYS2:
                        riskRuleConfig.setRuleValue(ruleValueMap.get("strValue") + "");
                        riskRuleConfigs.add(riskRuleConfig);
                        continue;
                    case ConstantByRisk.R_YYS3:
                        riskRuleConfig.setRuleValue("低于" + ruleValueMap.get("intValue"));
                        riskRuleConfigs.add(riskRuleConfig);
                        continue;
                    case ConstantByRisk.R_YYS4:
                        riskRuleConfig.setRuleValue("近" + ruleValueMap.get("intValue1") + "个月 " + "充值都低于：" + ruleValueMap.get("intValue2"));
                        riskRuleConfigs.add(riskRuleConfig);
                        continue;
                    case ConstantByRisk.R_ZMF:
                        riskRuleConfig.setRuleValue("低于：" + ruleValueMap.get("intValue"));
                        riskRuleConfigs.add(riskRuleConfig);
                        continue;
                    case ConstantByRisk.R_DQ:
                        Object regionObj = ruleValueMap.get("strValueArray");
                        JSONObject regionJson = JSONObject.parseObject(riskRuleConfig.getRuleValue());
                        List<Present> regions = getRegion(regionJson);
                        if (null != regions && regions.size() > Constant.LIST_SIZE_LENTH_ZORE) {
                            StringBuffer presenSb = new StringBuffer();
                            for (Present present : regions) {
                                if (null == present.getCity()) {
                                    presenSb.append(present.getProvince() + " ");
                                } else {
                                    presenSb.append(present.getProvince() + ":" + present.getCity() + " ");
                                }
                            }
                            riskRuleConfig.setRuleValue(presenSb.toString());
                        } else {
                            riskRuleConfig.setRuleValue("");
                        }
                        riskRuleConfigs.add(riskRuleConfig);
                        continue;
                    case ConstantByRisk.R_YYS5:
                        int amount = (int) ruleValueMap.get("intValue");
                        riskRuleConfig.setRuleValue("话费余额低于" + amount / 100 + "元");
                        riskRuleConfigs.add(riskRuleConfig);
                        continue;
                    case ConstantByRisk.R_YYS6:
                        riskRuleConfig.setRuleValue(ruleValueMap.get("strValue") + "");
                        riskRuleConfigs.add(riskRuleConfig);
                        continue;
                    case ConstantByRisk.R_YYS7:
                        riskRuleConfig.setRuleValue(ruleValueMap.get("strValue") + "");
                        riskRuleConfigs.add(riskRuleConfig);
                        continue;
                    case ConstantByRisk.R_YYS8:
                        riskRuleConfig.setRuleValue("运营商入网时长小于" + ruleValueMap.get("minValue") + "个月");
                        riskRuleConfigs.add(riskRuleConfig);
                        continue;
                    case ConstantByRisk.R_BLACK001:
                        riskRuleConfig.setRuleValue("是");
                        riskRuleConfigs.add(riskRuleConfig);
                        continue;
                    default:

                        break;
                }
            }
        }
        return riskRuleConfigs;
    }

    @Override
    public List<RiskRuleConfig> findRiskRuleByStatus(byte status) {
        return riskRuleConfigDAO.findRiskRuleByStatus(status);
    }

    @Override
    public int updateAllState(Byte status) {
        return riskRuleConfigDAO.updateAllState(status);
    }

    /**
     * 获取借款限制的地区
     *
     * @param data
     * @param
     * @param
     * @return
     */
    public List<Present> getRegion(JSONObject data) {
        List<Present> presents = new ArrayList<>();
        Set<String> keySet = data.keySet();
        for (String key : keySet) {
            Object object = data.get(key);
            if (object instanceof JSONObject) {
                JSONObject dataJsonObject = (JSONObject) object;
                Set<String> keySet2 = dataJsonObject.keySet();
                for (String childKey : keySet2) {
                    if ("".equals(dataJsonObject.get(childKey).toString()) || null == dataJsonObject.get(childKey)) {
                        Present present = new Present();
                        present.setProvince(childKey);
                        presents.add(present);
                    } else {
                        String val = dataJsonObject.get(childKey).toString();
                        String[] split = val.split("\\|");
                        Present present = new Present();
                        present.setProvince(childKey);
                        StringBuffer citySb = new StringBuffer();
                        for (String city : split) {
                            citySb.append(city + "、");
                        }
                        citySb.deleteCharAt(citySb.length() - 1);
                        present.setCity(citySb.toString());
                        presents.add(present);
                    }
                }
            } else if (object instanceof JSONArray) {
                JSONArray dataJsonJSONArray = (JSONArray) object;
                for (Object object2 : dataJsonJSONArray) {
                    getRegion((JSONObject) object2);
                }
            }
        }
        return presents;
    }


}
