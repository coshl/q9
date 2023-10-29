package com.summer.web.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.summer.dao.entity.*;
import com.summer.dao.mapper.IUserCardInfoDao;
import com.summer.dao.mapper.OrderBorrowMapper;
import com.summer.dao.mapper.ProtocolDAO;
import com.summer.enums.MobileSwitch;
import com.summer.enums.PayChannelEnum;
import com.summer.pojo.vo.BackConfigParamsVo;
import com.summer.service.impl.BackConfigParamsService;
import com.summer.util.BackConfigParams;
import com.summer.util.CallBackResult;
import com.summer.util.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author ls
 * @version V1.0
 * @Title: 协议
 * @Description:
 * @date 2018/12/24 9:54
 */
@Slf4j
@RestController
@RequestMapping("/v1.0/api/agreement")
public class AgreementController extends BaseController {
    @Resource
    private OrderBorrowMapper orderBorrowMapper;
    @Resource
    private ProtocolDAO protocolDAO;
    @Resource
    private IUserCardInfoDao userBankCardInfoMapper;
    @Resource
    private BackConfigParamsService backConfigParamsService;
    @Value("${payChannel.inuse}")
    private String inuse;


    /**
     * 支付切换
     */
    @PostMapping("/payChannelUpdate")
    public String payChannelUpdate(HttpServletRequest request,@RequestBody String jsonData) throws Exception {
        log.info("支付通道修改开始================="+jsonData);
        Map<String, Object> params = JSONObject.parseObject(jsonData);
        Integer code = (Integer) params.get("code");
        if(null == code) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "请输入选择通道！");
        }
        List payList = JSONArray.parseArray(inuse);
        if(payList.contains(code)){
            //代付
            BackConfigParams repayBf = new BackConfigParams();
            repayBf.setSysKey("repay_bf");
            repayBf.setSysValue(String.valueOf(code));
            backConfigParamsService.updateBySyskey(repayBf);
            //代收
            BackConfigParams usdtDf = new BackConfigParams();
            usdtDf.setSysKey("usdt_df");
            usdtDf.setSysValue(String.valueOf(code));
            backConfigParamsService.updateBySyskey(usdtDf);
        }else {
            return CallBackResult.returnJson(CallBackResult.INVALID_REQUEST, "请开通支付后台再使用该通道！");
        }
        return CallBackResult.returnJson("ok");
    }

    /**
     * 支付查询
     */
    @PostMapping("/payChannelSelect")
    public String payChannelSelect(HttpServletRequest request) throws Exception {
        log.info("支付通道查询开始=================");
        //JSONObject json = new JSONObject();
        List list = new ArrayList();
        Map map1 = new HashMap();
        map1.put("code",PayChannelEnum.BITE.getValue());
        map1.put("name",PayChannelEnum.BITE.getDesc());
        list.add(map1);
        Map map2 = new HashMap();
        map2.put("code",PayChannelEnum.AOCHUANG.getValue());
        map2.put("name",PayChannelEnum.AOCHUANG.getDesc());
        list.add(map2);
        Map map3 = new HashMap();
        map3.put("code",PayChannelEnum.FUGUI.getValue());
        map3.put("name",PayChannelEnum.FUGUI.getDesc());
        list.add(map3);
        Map map4 = new HashMap();
        map4.put("code",PayChannelEnum.MAYA.getValue());
        map4.put("name",PayChannelEnum.MAYA.getDesc());
        list.add(map4);
        //json.put("date", list);
        return CallBackResult.returnJson(list);
    }

    /**
     * 运营商切换
     */
    @PostMapping("/mobileChannelUpdate")
    public String mobileChannelUpdate(HttpServletRequest request, @RequestBody String jsonData) throws Exception {
        log.info("运营商通道修改开始================="+jsonData);
        Map<String, Object> params = JSONObject.parseObject(jsonData);
        Integer code = (Integer) params.get("code");
        if(null == code) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "请输入选择通道！");
        }
            BackConfigParams mobileSwitch = new BackConfigParams();
            mobileSwitch.setSysKey("mobile_switch");
            mobileSwitch.setSysValue(String.valueOf(code));
            backConfigParamsService.updateBySyskey(mobileSwitch);
        return CallBackResult.returnJson("ok");
    }

    /**
     * 运营商查询
     */
    @PostMapping("/mobileChannelSelect")
    public String mobileChannelSelect(HttpServletRequest request) throws Exception {
        log.info("运营商通道查询开始=================");
        //JSONObject json = new JSONObject();
        List list = new ArrayList();

        Map map1 = new HashMap();
        map1.put("code", MobileSwitch.XINGPAN.getValue());
        map1.put("name",MobileSwitch.XINGPAN.getDesc());
        list.add(map1);

        Map map2 = new HashMap();
        map2.put("code",MobileSwitch.BOX.getValue());
        map2.put("name",MobileSwitch.BOX.getDesc());
        list.add(map2);

        Map map3 = new HashMap();
        map3.put("code",MobileSwitch.WUHUA.getValue());
        map3.put("name",MobileSwitch.WUHUA.getDesc());
        list.add(map3);
        /*Map map3 = new HashMap();
        map3.put("code",PayChannelEnum.FUGUI.getValue());
        map3.put("name",PayChannelEnum.FUGUI.getDesc());
        list.add(map3);*/
        //json.put("date", list);
        return CallBackResult.returnJson(list);
    }

    /**
     * 借款协议
     */
    @PostMapping("/borrowAgreement")
    public String borrowAgreement(HttpServletRequest request) throws Exception {
        UserInfo userInfo = redisUser(request);
        if (null == userInfo){
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST,Constant.USER_MSG_NOT_EXIST);
        }

        OrderBorrow orderBorrow = orderBorrowMapper.selectByUserId(userInfo.getId());
        String realName = userInfo.getRealName();
        String idCard = userInfo.getIdCard();
        String shadowIdCard = idCard.substring(0, 5) + "****" + idCard.substring(idCard.length() - 4, idCard.length());
        Protocol protocol = protocolDAO.selectByPrimaryKey(1);
        String partyC = protocol.getPartyC();

        Integer applyAmount = orderBorrow.getApplyAmount();
        Integer feeApr = orderBorrow.getFeeApr();
        Date loanEndTime = orderBorrow.getLoanEndTime();
        Date loanTime = orderBorrow.getLoanTime();
        String orderId = orderBorrow.getOutTradeNo();
        HashMap<String, String> map = new HashMap<>();
        map.put("id", orderId);
        map.put("realName", realName);
        map.put("idCard", shadowIdCard);
        map.put("partyC", partyC);

        map.put("applyAmount", applyAmount + "");
        map.put("feeApr", feeApr + "");
        map.put("loanTime", loanTime + "");
        map.put("loanEndTime", loanEndTime + "");
        return CallBackResult.returnJson(map);
    }


    /**
     * 平台服务协议
     *
     * @param request
     * @return
     */
    @PostMapping("/platformService")
    public String platformService(HttpServletRequest request) throws Exception {
        UserInfo userInfo = redisUser(request);
        if (null == userInfo){
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST,Constant.USER_MSG_NOT_EXIST);
        }

        OrderBorrow orderBorrow = orderBorrowMapper.selectByUserId(userInfo.getId());
        String realName = userInfo.getRealName();
        String idCard = userInfo.getIdCard();
        String shadowIdCard = idCard.substring(0, 5) + "****" + idCard.substring(idCard.length() - 4, idCard.length());

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("phone", userInfo.getPhone());
        hashMap.put("name", realName);
        hashMap.put("id", shadowIdCard);
        hashMap.put("idCard", userInfo.getIdCard());
        // 利率
        hashMap.put("lateFeeApr", orderBorrow.getLateFeeApr() + "%");
        hashMap.put("applyAmount", orderBorrow.getApplyAmount() + "");
        // 借款时间
        hashMap.put("loanTime", orderBorrow.getLoanTime() + "");
        // 需要还款时间
        hashMap.put("loanEndTime", orderBorrow.getLoanEndTime() + "");

        return CallBackResult.returnJson(hashMap);
    }

    /**
     * 授权扣款委托书
     *
     * @param request
     * @return
     */
    @PostMapping("/authorized")
    public String authorized(HttpServletRequest request) throws Exception {
        UserInfo userInfo = redisUser(request);
        if (null == userInfo){
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST,Constant.USER_MSG_NOT_EXIST);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("userId", userInfo.getId());
        HashMap<String, String> hashMap = new HashMap<>();
        String idCard = userInfo.getIdCard();
        String shadowIdCard = idCard.substring(0, 5) + "****" + idCard.substring(idCard.length() - 4, idCard.length());
        hashMap.put("name", userInfo.getRealName());
        hashMap.put("idCard", shadowIdCard);
        hashMap.put("phone", userInfo.getPhone());
        //根据用户id和银行卡有效状态查询银行卡（user_card_info）
        Map<String, Object> findCardParam = new HashMap<String, Object>();
        findCardParam.put("userId", userInfo.getId());
        findCardParam.put("status", Constant.USER_BANKCARD_STATUS);
        findCardParam.put("limit", 1);
        List<UserCardInfo> userCardByUserId = userBankCardInfoMapper.findUserCardByUserId(findCardParam);

        if (null != userCardByUserId && userCardByUserId.size() > 0) {
            hashMap.put("bankName", userCardByUserId.get(0).getBankName());
            hashMap.put("bankNo", userCardByUserId.get(0).getCardNo() + "");
        }
        return CallBackResult.returnJson(hashMap);
    }


}
