package com.summer.web.controller.risk;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.summer.annotation.Log;
import com.summer.api.service.RiskFkContentService;
import com.summer.dao.entity.RiskFkContent;
import com.summer.enums.MobileSwitch;
import com.summer.enums.YesOrNo;
import com.summer.service.IRiskFkContentService;
import com.summer.service.job.RedissLockUtil;
import com.summer.service.yys.BoxApi;
import com.summer.util.RedisUtil;
import com.summer.web.controller.BaseController;
import com.summer.dao.entity.OrderBorrow;
import com.summer.dao.entity.PlatformUser;
import com.summer.pojo.dto.OrderPushRiskDTO;
import com.summer.api.service.IOrderBorrowService;
import com.summer.service.mq.OrderProducer;
import com.summer.util.CallBackResult;
import com.summer.util.Constant;
import com.summer.util.ConstantByOrderState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 风控魔蝎重推
 */
@RestController
@RequestMapping("/v1.0/api/risk")
@Slf4j
public class RiskMoXieController extends BaseController {

    @Resource
    private OrderProducer orderProducer;
    @Resource
    private IOrderBorrowService orderBorrowService;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private IRiskFkContentService iRiskFkContentService;


    /**
     * 风控重新推送
     *
     * @return
     */
    @Log(title = "重新机审")
    @PostMapping("/push")
    public String riskRePush(@Validated @RequestBody OrderPushRiskDTO orderPushRiskDTO, HttpServletRequest request) {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        if (platformUser.getRoleId() != Constant.ROLEID_SUPER && platformUser.getRoleId() != Constant.ROLEID_REVIEWER) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.UN_AUTH_VIST);
        }
        String riskrepushExKey = "RISKREPUSH_EX" + orderPushRiskDTO.getOrderId();
        Boolean riskrepushEx = redisUtil.setIfAbsent(riskrepushExKey, "0", 5, TimeUnit.SECONDS);
        if (!riskrepushEx) {
            long expire = redisUtil.getExpire(riskrepushExKey);
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "点太快啦！" + expire + "s后再试");
        }
        log.info("重新机审推送:{}", JSONObject.toJSONString(orderPushRiskDTO));
        OrderBorrow orderBorrow = orderBorrowService.findByUserIdOrderId(orderPushRiskDTO);
        int status = orderBorrow.getStatus();

        if (Objects.isNull(orderBorrow)) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.RISK_PUSH_FAILD);
        }
        String lockKey = Constant.RISK_RE_PUSH_ + orderBorrow.getId();
        boolean result = redisUtil.hasKey(lockKey);
        if (result) {
            long expire = redisUtil.getExpire(lockKey);
            // 重复的推送内容
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "机审进行中,请等待" + expire + "s再推送");
        }
        // 0,1,2,3,4,
        if (ConstantByOrderState.FIRSTSTATUS == status || ConstantByOrderState.FOURSTATUS == status || ConstantByOrderState.ZEROSTATUS == status || ConstantByOrderState.THREESTATUS == status || ConstantByOrderState.TWOSTATUS == status) {
            String orderBorrowStr = JSONObject.toJSONString(orderBorrow);
            orderProducer.sendNewOrder(orderBorrowStr);
            //firstTrialMoxieService.riskFirstTrial(orderBorrowStr);
            return CallBackResult.returnJson(CallBackResult.SUCCESS, Constant.RISK_PUSH_SUCC);
        }
        return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.DISPENSE_RISK_PUSH);
    }



    /**
     * 风控获取模型分
     *
     * @return
     */
    @Log(title = "Box风控获取模型分")
    @GetMapping("/riskGetScore/{userId}")
    public Object riskGetScore(@PathVariable Integer userId, HttpServletRequest request) {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        if (platformUser.getRoleId() != Constant.ROLEID_SUPER && platformUser.getRoleId() != Constant.ROLEID_REVIEWER) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.UN_AUTH_VIST);
        }
        log.info("BOX模型分开始:{}", userId);
        RiskFkContent riskFkContent = iRiskFkContentService.getFirstByUserIdAndType(userId, Integer.valueOf(MobileSwitch.BOX.getValue()));
        if(Objects.isNull(riskFkContent) || riskFkContent.getTaskId().isEmpty()){
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "没有BOX风控调用记录，无法获得模型分");
        }
        if(riskFkContent.getScore() != null){
            return CallBackResult.returnJson(CallBackResult.SUCCESS, riskFkContent.getScore());
        }
        if(!riskFkContent.getTaskId().isEmpty()){
            String score = BoxApi.boxQueryScore(riskFkContent.getTaskId());
            return CallBackResult.returnJson(CallBackResult.SUCCESS, score);
        }
        log.info("BOX模型分获取失败:{}", userId);
        return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "BOX模型分获取失败");
    }
}
