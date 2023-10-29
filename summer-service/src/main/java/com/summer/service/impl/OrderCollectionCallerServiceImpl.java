package com.summer.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.summer.api.service.OrderCollectionCallerService;
import com.summer.dao.entity.OrderCollectionCaller;
import com.summer.dao.entity.PlatformUser;
import com.summer.enums.RemarkEnum;
import com.summer.dao.mapper.OrderCollectionCallerDAO;
import com.summer.dao.mapper.PlateformChannelMapper;
import com.summer.dao.mapper.PlatformUserMapper;
import com.summer.util.CallBackResult;
import com.summer.util.Constant;
import com.summer.pojo.vo.ChannelNameListVo;
import com.summer.pojo.vo.OrderCollectionCallerVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * 分配
 */
@Service
public class OrderCollectionCallerServiceImpl implements OrderCollectionCallerService {

    @Resource
    private OrderCollectionCallerDAO callerDAO;

    @Resource
    private PlateformChannelMapper plateformChannelMapper;

    @Resource
    private PlatformUserMapper platformUserMapper;

    @Override
    public String selectRepaymentDistribution(Map<String, Object> params) {
        params.put("remark", RemarkEnum.getName((Integer) params.get("remark")));
        PageHelper.startPage(params);
        List<OrderCollectionCallerVo> list = callerDAO.findParams(params);
        PageInfo<OrderCollectionCallerVo> pageInfo = new PageInfo<>(list);
        Map<String, Object> res = new HashMap<>();
        res.put("page", pageInfo);
        //渠道
        List<ChannelNameListVo> plateformChannels = plateformChannelMapper.findByChannelName(null);
        res.put("channel", plateformChannels);
        //客服人员
        HashMap<String, Object> map = new HashMap<>();
        map.put("roleId", Constant.ROLEID_COLLECTION_TODAY);
        map.put("status", 0);
        List<PlatformUser> users = platformUserMapper.selectSimple(map);
        res.put("waiter", users);
        return CallBackResult.returnJson(res);
    }

    @Override
    public List<OrderCollectionCallerVo> findParams(Map<String, Object> params) {
        return callerDAO.findParams(params);
    }

    @Override
    public String updateDistribution(Map<String, Object> params) {
        Integer id = (Integer) params.get("id");
        //催收情况
        String remark = RemarkEnum.getName((Integer) params.get("remark"));
        //备注
        String annotation = (String) params.get("annotation");
        if (id == null || remark == null) {
            return CallBackResult.returnJson(CallBackResult.PARAM_IS_ERROR, CallBackResult.PARAM_IS_EXCEPTION_MSG);
        }
        OrderCollectionCaller caller = new OrderCollectionCaller();
        caller.setId(id);
        caller.setRemark(remark);
        caller.setAnnotation(annotation);
        if (callerDAO.updateByPrimaryKeySelective(caller) > 0) {
            return CallBackResult.returnJson(CallBackResult.SUCCESS, CallBackResult.MSG_CREATED);
        }
        return CallBackResult.returnJson(CallBackResult.INVALID_REQUEST, "更新失败");
    }

    @Override
    public String transfer(String jsonData, PlatformUser platformUser) {
        //从jsonData获取数据
        Map<String, Object> params = JSONObject.parseObject(jsonData);
        String ids = (String) params.get("ids");
        Integer userId = (Integer) params.get("userId");
        if (ids == null) {
            return CallBackResult.returnJson(CallBackResult.PARAM_IS_ERROR, CallBackResult.PARAM_IS_EXCEPTION_MSG);
        }
        String[] split = ids.split(",");
        if (split.length <= 0) {
            return CallBackResult.returnJson(CallBackResult.PARAM_IS_ERROR, CallBackResult.PARAM_IS_EXCEPTION_MSG);
        }
        List<OrderCollectionCaller> lists = new ArrayList<>();
        Date date = new Date();
        for (String id : split) {
            OrderCollectionCaller caller = new OrderCollectionCaller();
            caller.setId(Integer.valueOf(id));
            caller.setCurrentCollectionUserId(userId);
            //分配时间
            caller.setDispatchTime(date);
            //操作人
            caller.setOperatorName(platformUser.getPhoneNumber());
            lists.add(caller);
        }
        if (callerDAO.updateBatch(lists) > 0) {
            return CallBackResult.returnJson(CallBackResult.SUCCESS, "转派成功");
        } else {
            return CallBackResult.returnJson(CallBackResult.INVALID_REQUEST, "转派失败");
        }
    }
}
