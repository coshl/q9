package com.summer.service.impl.thirdpart;

import cn.hutool.core.lang.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.jfinal.json.Json;
import com.summer.dao.entity.UserContacts;
import com.summer.dao.entity.UserMoXieDataWithBLOBs;
import com.summer.dao.entity.mongo.Report;
import com.summer.dao.mapper.UserContactsDAO;
import com.summer.dao.mapper.UserMoXieDataDAO;
import com.summer.api.service.thirdpart.IOperatorMoXieService;
import com.summer.pojo.dto.YunYingShangDTO;
import com.summer.util.AliOssUtils;
import com.summer.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;


/**
 * 解析魔蝎的运营商报告
 */
@Slf4j
@Service
public class OperatorMoXieService implements IOperatorMoXieService {
    @Resource
    private UserMoXieDataDAO userMoXieDataDAO;
    @Resource
    private MongoTemplate mongoTemplate;

    @Resource
    private UserContactsDAO userContactsDAO;

    @Override
    public Map<String, Object> getMoXieDetail(Integer userId) {
        Map<String, Object> resultMap = new HashMap<>();
        if (userId == null || userId < 0) {
            return null;
        }
        try {
            UserMoXieDataWithBLOBs userMoXieDataWithBLOBs = userMoXieDataDAO.selectDataByUserId(userId);
            if (null == userMoXieDataWithBLOBs) {
                return null;
            } else if (userMoXieDataWithBLOBs.getTaskId().equals("手动跳过")) {
                return null;
            }
            byte status = userMoXieDataWithBLOBs.getStatus();
            if (status == 2 || status == 3) {
                // 报告内容
                //resultMap.put("mobileReport", JSONObject.parseObject(OkHttpUtils.builder().url(userMoXieDataWithBLOBs.getMxRaw()).addHeader("Content-Type", "application/json; charset=utf-8").sync()));
                resultMap.put("mobileReport", JSONObject.parseObject(userMoXieDataWithBLOBs.getMxReport()));
                // 运营商通道：1-fuygs
                resultMap.put("mobileSwitch", userMoXieDataWithBLOBs.getStatus());
            } else if (status == 4) {
                cn.hutool.json.JSONObject jsonObject = new cn.hutool.json.JSONObject(userMoXieDataWithBLOBs.getMxReport());
                cn.hutool.json.JSONArray call_contact_detail = jsonObject.getJSONArray("call_contact_detail");
                String js = JSONObject.toJSONString(call_contact_detail, SerializerFeature.WriteClassName);
                YunYingShangDTO[] array = new Gson().fromJson(js,YunYingShangDTO[].class);
                List<YunYingShangDTO> list = Arrays.asList(array);
                List<YunYingShangDTO> list1 = new ArrayList<>();
                Map<String, Object> params = new HashMap<>();
                params.put("userId", userId);
                List<UserContacts> userContactsList = userContactsDAO.selectUserContacts(params);
                Map<String, String> userVOMap = new HashMap<>();
                for (UserContacts userContacts : userContactsList) {
                    String myString = userContacts.getContactPhone().replaceAll("[^0-9\\\\.]", "");
                    userVOMap.put(myString, userContacts.getContactName());
                }
                for (YunYingShangDTO yunYingShangDTO : list) {
                    if (!StringUtils.isBlank(yunYingShangDTO.getPeer_num())) {
                        String s = userVOMap.get(yunYingShangDTO.getPeer_num());
                        if (!StringUtils.isBlank(s)) {
                            yunYingShangDTO.setP_relation(s);
                        }
                        list1.add(yunYingShangDTO);
                    }

                }
                jsonObject.put("call_contact_detail", list1);
                resultMap.put("mobileReport", jsonObject.toString());
//                resultMap.put("mobileReport", userMoXieDataWithBLOBs.getMxReport());
                // 运营商通道：1-fuygs
                resultMap.put("mobileSwitch", userMoXieDataWithBLOBs.getStatus());
            }
                /*else if(status == 3){
                Report report = mongoTemplate.findById(userId,Report.class);
                JSONObject reportJson = JSONObject.parseObject(report.getReport());
               */
            /* List<UserContacts> userContactsList = userContactsDAO.findContatsByUserId(userId);
                Iterator<UserContacts> itUserContacts = userContactsList.iterator();
                //先去掉小于11位数的号码
                while(itUserContacts.hasNext()){
                    UserContacts userContacts = itUserContacts.next();
                    userContacts.setContactPhone(PhoneUtil.getNoWith86Number(userContacts.getContactPhone()));
                    //userContactsList.removeIf(userContact -> userContact.getContactPhone().length()<11);
                    if (userContacts.getContactPhone().length()<11) {
                        itUserContacts.remove();
                    }
                }
               *//**//* for (UserContacts userContacts : userContactsList){
                    new Thread(()->{
                    }).start();
                }*//**//*
                //转成map
                Map<String, String> map = userContactsList.stream().collect(Collectors.toMap(UserContacts::getContactPhone,UserContacts::getContactName,(oldValue,newValue)->newValue));
                //最后排序
                JSONArray callArrays = reportJson.getJSONArray("calls");
                for(Object callJson : callArrays){
                    JSONObject callArray = (JSONObject) JSONObject.toJSON(callJson);
                    JSONArray itemsArray = callArray.getJSONArray("items");
                    *//**//*for (Object itemsJson : itemsArray){
                        JSONObject itemArray = (JSONObject) JSONObject.toJSON(itemsJson);
                        itemArray.put("peer_number",PhoneUtil.getNoWith86Number(itemArray.getString("peer_number")));
                        itemsArray.removeIf(itemArrayOne -> itemArray.getString("peer_number").length()<11);
                        itemArray.put("location",map.get(itemArray.getString("peer_number")));
                    }*//**//*

                    Iterator<Object> itemsJson = itemsArray.iterator();
                    while(itemsJson.hasNext()){
                        JSONObject itemArray = (JSONObject)itemsJson.next();
                        itemArray.put("peer_number",PhoneUtil.getNoWith86Number(itemArray.getString("peer_number")));
                        //itemsArray.removeIf(itemArrayOne -> itemArray.getString("peer_number").length()<11);
                        itemArray.put("fee",map.get(itemArray.getString("peer_number")));
                        if (itemArray.getString("peer_number").length()<11) {
                            itemsJson.remove();
                        }
                    }

                    itemsArray = this.sort(itemsArray,"duration",false);
                    callArray.put("items",itemsArray);
                }*//*
                // 报告内容
                resultMap.put("mobileReport", reportJson);
                // 运营商通道：1-fuygs
                resultMap.put("mobileSwitch", report.getType());
            }*//*else if (status == 4){

                resultMap.put("mobileReport", AliOssUtils.readReport(userMoXieDataWithBLOBs.getMxReport()));
                // 运营商通道：1-fuygs
                resultMap.put("mobileSwitch", userMoXieDataWithBLOBs.getStatus());

            }*/
        } catch (Exception e) {
            log.error("OperatorMoXieService.getMoXieDetail ----------------->运营商解析报告异常 userId={},e={}", userId, e);
            e.printStackTrace();
            return null;
        }
        return resultMap;
    }

    /**
     * @param jsonArray json数组
     * @param field     要排序的key
     * @param isAsc     是否升序
     */
    private static JSONArray sort(JSONArray jsonArray, final String field, boolean isAsc) {
        List list = new ArrayList<>();
        list = jsonArray.toJavaList(JSONObject.class);
        Collections.sort(list, new Comparator<JSONObject>() {
            @Override
            public int compare(JSONObject o1, JSONObject o2) {
                Object f1 = o1.get(field);
                Object f2 = o2.get(field);
                if (f1 instanceof Number && f2 instanceof Number) {
                    return ((Number) f1).intValue() - ((Number) f2).intValue();
                } else {
                    return f1.toString().compareTo(f2.toString());
                }
            }
        });
        if (!isAsc) {
            Collections.reverse(list);
        }
        return JSONArray.parseArray(list.toString());
    }

    @Override
    public UserMoXieDataWithBLOBs getUserMoXieData(Integer userId) {
        try {
            Assert.notNull(userId, "userId不能为空");
            return userMoXieDataDAO.selectDataByUserId(userId);
        } catch (IllegalArgumentException e) {
            log.error("参数解析异常", e);
            return null;
        } catch (Exception e) {
            log.error("OperatorMoXieService.getMoXieDetail ----------------->运营商报告获取异常 userId={},e={}", userId, e);
            return null;
        }
    }

}
