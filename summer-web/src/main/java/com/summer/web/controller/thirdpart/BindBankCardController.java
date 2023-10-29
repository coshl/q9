package com.summer.web.controller.thirdpart;


import com.alibaba.fastjson.JSONObject;
import com.summer.enums.UserAuthStatus;
import com.summer.service.card.CardApi;
import com.summer.util.RedisUtil;
import com.summer.web.controller.BaseController;
import com.summer.dao.entity.InfoIndexInfo;
import com.summer.dao.entity.OrderBorrow;
import com.summer.dao.entity.UserCardInfo;
import com.summer.dao.entity.UserInfo;
import com.summer.dao.mapper.*;
import com.summer.api.service.IInfoIndexService;
import com.summer.api.service.channel.IChannelAsyncCountService;
import com.summer.service.impl.InfoIndexInfoService;
import com.summer.service.mq.OrderProducer;
import com.summer.util.CallBackResult;
import com.summer.util.Constant;
import com.summer.pojo.vo.BackConfigParamsVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 公共绑卡控制类
 *
 * @author
 */
@Slf4j
@Controller
@RequestMapping("v1.0/api/changJiePay")
public class BindBankCardController extends BaseController {
    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private RedisUtil redisUtil;
    @Resource
    private IChannelAsyncCountService channelAsyncCountService;
    @Resource
    private InfoIndexInfoDao infoIndexInfoDao;
    @Resource
    private IInfoIndexService infoIndexService;
    @Resource
    private IUserCardInfoDao userCardInfoDao;
    @Resource
    private OrderBorrowMapper orderBorrowMapper;
    @Resource
    private IBackConfigParamsDao backConfigParamsDao;
    @Autowired
    InfoIndexInfoService infoIndexInfoService;
    @Resource
    private OrderProducer orderProducer;

    private CallBackResult saveCard(String user_id, String acct_name, String mobilePhone, String card_no, String id_no
            , UserInfo user, String value, String orderTrxid, String branch, String zfb_no, String zfb_pic, String wx_no, String wx_pic, String bindType) {
        log.info("进入绑卡方法-----------------------------");
        Map<String, String> params = new HashMap<>();
        params.put("userId", user_id);
        params.put("cardNo", card_no);

        params.put("acctName", acct_name);
        params.put("idNo", id_no);
        params.put("OrderTrxId", orderTrxid);
        params.put("bindType", bindType);
        params.put("mobilePhone", mobilePhone);
        params.put("bankName", value);

        params.put("branch", branch);
        params.put("zfb_no", zfb_no);
        params.put("zfb_pic", zfb_pic);
        params.put("wx_no", wx_no);
        params.put("wx_pic", wx_pic);
        log.info("要绑的卡信息：{}", JSONObject.toJSONString(params));
        boolean saveCardInfoRes = tempBindCardProcedure(params);
        if (saveCardInfoRes) {
            /**绑卡成功异步统计*/
            channelAsyncCountService.bankAuthIsSuccCount(user, new Date());
            return CallBackResult.ok("绑卡成功，请返回首页发起借款");
        } else {
            return CallBackResult.fail("绑卡失败");
        }
    }


    private boolean tempBindCardProcedure(Map<String, String> reqParam) {
        try {
            String userId = reqParam.get("userId");
            String bankName = reqParam.get("bankName");
            UserInfo u = userInfoMapper.selectByPrimaryKey(Integer.parseInt(userId));
            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put("userId", Integer.parseInt(userId));
            params.put("limit", 1);
            List<UserCardInfo> userCardInfoList = userCardInfoDao.findUserCardByUserId(params);
            //所属银行名称
            // 银行id
            int bankId = 0;
            if (userCardInfoList == null || userCardInfoList.isEmpty()) {
                log.info("进行首绑");
                bankId = 0;

                if (reqParam.get("cardNo").length() < 15) {
                    return false;
                }
                //通过银行卡号获得银行卡名称
                if (bankName == null || "".equals(bankName)) {
                    return false;
                }
                //BankAllInfo bankAllInfo = userBankDao.selectByBankName(bankName.substring(0,6));
                // bankId=bankAllInfo.getBankId();
            } else {
                bankId = userCardInfoList.get(0).getBank_id();
            }
            reqParam.put("bankName", bankName);
            reqParam.put("bankId", bankId + "");
            boolean res = (userCardInfoList == null || userCardInfoList.isEmpty())
                    ? saveFirstTimeBindCardProcedure(reqParam, u)
                    : saveReBindCardProcedure(reqParam, u);
            return res;

        } catch (Exception e) {
            log.info("bindingCardController2 error" + e);
            return false;
        }
    }


    private boolean saveFirstTimeBindCardProcedure(Map<String, String> param, UserInfo u) {
        try {
            UserCardInfo cardInfo = buildUserCardInfo(param, u);
            int flag = userCardInfoDao.saveUserCardInfo(cardInfo);
            if (flag > 0) {
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("userId", u.getId());
                infoIndexService.authBank(map);//设置状态-第一次绑卡
                log.info("绑卡成功");
                UserInfo user = new UserInfo();
                user.setId(u.getId());
                user.setBankAuthentic(Constant.USER_AUTHENTIC);
                user.setCompanyName(param.get("zfb_no"));
                user.setCompanyPhone(param.get("zfb_pic"));
                user.setCompanyAddress(param.get("wx_no"));
                user.setCompanyAddressDetail(param.get("wx_pic"));
                userInfoMapper.updateByPrimaryKeySelective(user);
                user.setAuthenticStatus(Constant.AUTHENTIC_BANK);
                userInfoMapper.updateAuthenticStatus(user);
                return true;
            } else {
                log.info("账户银行信息保存失败");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private UserCardInfo buildUserCardInfo(Map<String, String> param, UserInfo u) {
        UserCardInfo cardInfo = new UserCardInfo();
        //银行卡编号
        cardInfo.setBankId(Integer.parseInt(param.get("bankId")));
        //银行卡号
        cardInfo.setCardNo(param.get("cardNo"));
        //手机号
        String mobilePhone = param.get("mobilePhone");
        String acctName = param.get("acctName");
        cardInfo.setPhone(StringUtils.isBlank(mobilePhone) ? u.getPhone() : mobilePhone);
        cardInfo.setBindingStatus(1);
        cardInfo.setOpenName(StringUtils.isBlank(acctName) ? u.getRealName() : acctName);
        //用户id
        cardInfo.setUserId(u.getId());
        //开户姓名
        cardInfo.setBankName(param.get("bankName"));
        //银行名称
        cardInfo.setBankAddress(param.get("branch"));
        //状态
        cardInfo.setStatus(UserCardInfo.STATUS_SUCCESS);
        //主副卡
        cardInfo.setMainCard(UserCardInfo.MAINCARD_Z);
        //银行卡类型
        cardInfo.setType(UserCardInfo.TYPE_DEBIT);
        //添加时间
        cardInfo.setCreateTime(new Date());
        return cardInfo;
    }

    /**
     * 重新绑卡
     *
     * @param param
     * @param u
     * @return
     */
    private boolean saveReBindCardProcedure(Map<String, String> param, UserInfo u) {
        UserCardInfo cardInfoSave = buildUserCardInfo(param, u);
        int flag = userCardInfoDao.updateUserCardInfo(cardInfoSave);
        //先根据银行卡号查询user_card_limit  中是否有该银行卡，如果有就说明用户重新绑卡时之前就绑定过，如果没有说明时重新绑定的新卡
        try {
            if (flag > 0) {
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("userId", u.getId());
                infoIndexService.authBankOld(map);//设置状态---重新绑卡
                log.info("重新绑卡成功");
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //TODO 00 绑卡
    @RequestMapping(value = "/bindDirect", method = RequestMethod.GET)
    @ResponseBody
    public String bindDirect(HttpServletRequest request) {

        UserInfo user = redisUser(request);
        if (null == user) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        String s = redisUtil.get("bk_" + user.getId());
        if(!StringUtils.isEmpty(s)){
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, "请勿重复绑定");
        }
        redisUtil.set("bk_"+user.getId(),"银行卡绑定",5);
        List<Integer> statuses = new ArrayList<>();
        statuses.add(UserAuthStatus.FAIL_AUTH.getValue());
        statuses.add(UserAuthStatus.RUN_AUTH.getValue());
        statuses.add(UserAuthStatus.WAIT_AUTH.getValue());
        InfoIndexInfo iii = infoIndexInfoService.selectByPrimaryKey(user.getId());
        iii.setAuthMobile(1);
        if (statuses.contains(iii.getAuthInfo())) {
            log.info("银行卡鉴权:{}", "请先过身份认证");
            return CallBackResult.returnJson(999, "请先过身份认证");
        }
        if (statuses.contains(iii.getAuthContacts())) {
            log.info("银行卡鉴权:{}", "请先过个人信息认证");
            return CallBackResult.returnJson(999, "请先过个人信息认证");
        }
        if (statuses.contains(iii.getAuthMobile())) {
            log.info("银行卡鉴权:{}", "请先过运营商认证");
            return CallBackResult.returnJson(999, "请先过运营商认证");
        }


        String rsaSign = "";
        CallBackResult callBackResult = new CallBackResult(CallBackResult.BUSINESS_DEFEAT, "绑卡失败,请求参数不符合要求");
        try {
            //神逻辑，此USERID字段不能作业务用，用了这后台就废了
            String userId = request.getParameter("user_id");
            //神逻辑，此acct_name字段不能作业务用，用了这实名认证也废了
            String name = user.getRealName();
                    //request.getParameter("acct_name");
            //神逻辑，此mobilePhone字段不能作业务用，用了这帐号都废了
            String mobilePhone = request.getParameter("mobilePhone");
            String card_no = request.getParameter("card_no");//银行卡号

            String bank = request.getParameter("bank");
            String branch = request.getParameter("branch");
            String zfb_no = request.getParameter("zfb_no");
            String zfb_pic = request.getParameter("zfb_pic");
            String wx_no = request.getParameter("wx_no");
            String wx_pic = request.getParameter("wx_pic");


            HashMap aliFaceMap=new HashMap();
            aliFaceMap.put("name",name);
            aliFaceMap.put("id_number",user.getIdCard());
            aliFaceMap.put("card_number",card_no);
            aliFaceMap.put("phone_number",mobilePhone);

            //银行卡校验
            /*String result = CardApi.aliCard(aliFaceMap);
            log.info("绑卡结果" + "================================跳过");
            if (StringUtils.isNotEmpty(result)) {
                JSONObject jsonObjectCard = JSONObject.parseObject(result);
                //{"code":"0000","msg":"处理成功","result":"2","resultMsg":"认证信息不匹配","requestId":"20210820020152296_8owjb1c4"}
                int count = infoIndexInfoService.insertBankBindFailureInfo(user.getId(), name, jsonObjectCard.toJSONString(),jsonObjectCard.get("code").toString());
                if(count > 0) log.info("插入银行卡绑定记录成功：{}" + count);
                else log.info("插入银行卡绑定记录失败：{}" + count);
                if("0000".equals(jsonObjectCard.get("code"))) {
                    //修改认证成功
                    iii.setAuthBank(1);
                    infoIndexInfoService.updateByPrimaryKeySelective(iii);
                    //校验成功，执行原有逻辑
                    String id_no = user.getIdCard();
                    HashMap<String, Object> params = new HashMap<String, Object>();
                    params.put("userId", Integer.parseInt(userId));
                    params.put("limit", 1);
                    List<UserCardInfo> userCardInfoList = userCardInfoDao.findUserCardByUserId(params);
                    log.info("获取到的绑卡信息：{}" + JSONObject.toJSONString(userCardInfoList));
                    if (CollectionUtils.isEmpty(userCardInfoList)) {
                        callBackResult = saveCard(user.getId() + "", name, mobilePhone, card_no, id_no, user, bank,
                                "", branch, zfb_no, zfb_pic, wx_no, wx_pic, "usdt_tag");

                        BackConfigParamsVo auto_borrow = backConfigParamsDao.findBySysKey("auto_borrow");
                        log.info("auto_borrow:{}", auto_borrow.getSysValue());
                        log.info("CallBackResult:{}", callBackResult.getCode());
                        if (null != auto_borrow && callBackResult.getCode().equals(CallBackResult.SUCCESS)) {
                            //自动申请
                            orderProducer.autoBorrowQueue(userId);
                        }
                    } else {
                        callBackResult.setMessage("绑卡失败请勿重复绑卡");
                    }
                    return JSONObject.toJSONString(callBackResult);

                } else {
                    return CallBackResult.returnJson(999, "绑卡失败，请检查您的号码");
                }
            }*/

            int count = infoIndexInfoService.insertBankBindFailureInfo(user.getId(), name, "跳过绑卡","00");
            if(count > 0) log.info("插入银行卡绑定记录成功：{}" + count);
            else log.info("插入银行卡绑定记录失败：{}" + count);
                //修改认证成功
                iii.setAuthBank(1);
                infoIndexInfoService.updateByPrimaryKeySelective(iii);
                //校验成功，执行原有逻辑
                String id_no = user.getIdCard();
                HashMap<String, Object> params = new HashMap<String, Object>();
                params.put("userId", Integer.parseInt(userId));
                params.put("limit", 1);
                List<UserCardInfo> userCardInfoList = userCardInfoDao.findUserCardByUserId(params);
                log.info("获取到的绑卡信息：{}" + JSONObject.toJSONString(userCardInfoList));
                if (CollectionUtils.isEmpty(userCardInfoList)) {
                    callBackResult = saveCard(user.getId() + "", name, mobilePhone, card_no, id_no, user, bank,
                            "", branch, zfb_no, zfb_pic, wx_no, wx_pic, "usdt_tag");
                    BackConfigParamsVo auto_borrow = backConfigParamsDao.findBySysKey("auto_borrow");
                    log.info("auto_borrow:{}", auto_borrow.getSysValue());
                    log.info("CallBackResult:{}", callBackResult.getCode());
                    if (null != auto_borrow && callBackResult.getCode().equals(CallBackResult.SUCCESS)) {
                        //自动申请
                        orderProducer.autoBorrowQueue(userId);
                    }
                } else {
                    callBackResult.setMessage("绑卡失败请勿重复绑卡");
                }
                return JSONObject.toJSONString(callBackResult);

        } catch (Exception e) {
            e.printStackTrace();
            log.info("绑卡失败e=" + e.getMessage());
            callBackResult.setMessage("绑卡失败");
            return JSONObject.toJSONString(callBackResult);
        }

        //return CallBackResult.returnJson(999, "验证失败，请检查您的银行卡");

    }

    @RequestMapping(value = "/bindUpdate", method = RequestMethod.GET)
    @ResponseBody
    public CallBackResult bindUpdate(HttpServletRequest request) {
        log.info("修改银行卡");
        UserInfo userInfo = redisUser(request);
        if (null == userInfo) {
            log.info("用户不存在");
            return CallBackResult.fail(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        try {
            Integer user_id = userInfo.getId();

//          String user_id = request.getParameter("user_id");
            String acct_name = request.getParameter("acct_name");
            String mobilePhone = request.getParameter("mobilePhone");
            String card_no = request.getParameter("card_no");

            String bank = request.getParameter("bank");
            String branch = request.getParameter("branch");
            String zfb_no = request.getParameter("zfb_no");
            String zfb_pic = request.getParameter("zfb_pic");
            String wx_no = request.getParameter("wx_no");
            String wx_pic = request.getParameter("wx_pic");
            UserInfo user = userInfoMapper.selectByPrimaryKey(user_id);
            if (user == null || StringUtils.isBlank(card_no) || StringUtils.isBlank(acct_name)) {
                return CallBackResult.fail(CallBackResult.BUSINESS_DEFEAT, "修改失败,请求参数不符合要求");
            }
            String id_no = user.getIdCard();

            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put("userId", user_id);
            params.put("limit", 1);
            List<UserCardInfo> userCardInfoList = userCardInfoDao.findUserCardByUserId(params);
            if (CollectionUtils.isEmpty(userCardInfoList)) {
                return saveCard(user_id.toString(), acct_name, mobilePhone, card_no, id_no, user, bank,
                        "", branch, zfb_no, zfb_pic, wx_no, wx_pic, "usdt_tag");
            } else {
                UserCardInfo cardInfo = userCardInfoList.get(0);
                if ("db_tag".equals(cardInfo.getAgreeno())) {
                    cardInfo.setAgreeno("usdt_tag");
                }
                //银行卡号
                cardInfo.setCardNo(card_no);
                //手机号
                cardInfo.setPhone(StringUtils.isBlank(mobilePhone) ? user.getPhone() : mobilePhone);
                cardInfo.setBindingStatus(0);

                cardInfo.setOpenName(StringUtils.isBlank(acct_name) ? user.getRealName() : acct_name);
                //用户id
                cardInfo.setUserId(user.getId());
                //开户姓名
                //银行名称
                cardInfo.setBankName(bank);
                cardInfo.setBankAddress(branch);
                //状态
                cardInfo.setStatus(UserCardInfo.STATUS_SUCCESS);
                //主副卡
                cardInfo.setMainCard(UserCardInfo.MAINCARD_Z);
                //银行卡类型
                cardInfo.setType(UserCardInfo.TYPE_DEBIT);
                //添加时间
                userCardInfoDao.updateUserCardInfo(cardInfo);
                user.setCompanyName(zfb_no);
                user.setCompanyPhone(zfb_pic);
                user.setCompanyAddress(wx_no);
                user.setCompanyAddressDetail(wx_pic);
                user.setPhone(null);
                user.setRealName(null);
                userInfoMapper.updateByPrimaryKeySelective(user);
            }
            return CallBackResult.ok("修改成功");

        } catch (Exception e) {
            log.error("修改失败e{}", e);
            return CallBackResult.fail(CallBackResult.BUSINESS_DEFEAT, "修改失败" + e.getMessage());
        }
    }

    /**
     * 移动端查询绑卡信息
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/bindInfo", method = RequestMethod.GET)
    @ResponseBody
    public String bindInfo(HttpServletRequest request) {
//        UserInfo userInfot = redisUser(request);
//        if (null == userInfot){
//            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
//        }
//        String rsaSign = "";
        CallBackResult callBackResult = new CallBackResult(CallBackResult.BUSINESS_DEFEAT, "查询失败,请求参数不符合要求");
        try {
            UserInfo redisUser = redisUser(request);
            if (null == redisUser) {
                callBackResult.setMessage("参数非法");
                callBackResult.setCode(CallBackResult.BUSINESS_ERROR);
                return JSONObject.toJSONString(callBackResult);
            }
            Integer userId = redisUser.getId();
            UserInfo userInfo = userInfoMapper.selectByPrimaryKey(userId);
            if (userInfo == null) {
                callBackResult.setMessage("参数非法");
                callBackResult.setCode(CallBackResult.BUSINESS_ERROR);
                return JSONObject.toJSONString(callBackResult);
            }
            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put("userId", userId);
            params.put("limit", 1);
            List<UserCardInfo> userCardInfoList = userCardInfoDao.findUserCardByUserId(params);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", userInfo.getRealName());
            jsonObject.put("phone", userInfo.getPhone());
            jsonObject.put("icCard", userInfo.getIdCard());
            jsonObject.put("userId", userId);
            if (CollectionUtils.isEmpty(userCardInfoList)) {
                jsonObject.put("bindStatus", 0);
                jsonObject.put("cardType", "");
                // 状态 (0:未生效   1:已生效)

                jsonObject.put("acct_name", "");
                jsonObject.put("card_no", "");

                jsonObject.put("bank", "");
                jsonObject.put("branch", "");
                jsonObject.put("zfb_no", "");
                jsonObject.put("zfb_pic", "");
                jsonObject.put("wx_no", "");
                jsonObject.put("wx_pic", "");
            } else {
                UserCardInfo cardInfo = userCardInfoList.get(0);
                //银行卡号
                // (1:信用卡   2:借记卡,3:对公账号)
                jsonObject.put("cardType", cardInfo.getType());
                // 状态 (0:未生效   1:已生效)
                jsonObject.put("bindStatus", cardInfo.getBindingStatus());

                jsonObject.put("acct_name", cardInfo.getOpenName());
                jsonObject.put("card_no", cardInfo.getCardNo());

                jsonObject.put("bank", cardInfo.getBankName());
                jsonObject.put("branch", cardInfo.getBankAddress());
                jsonObject.put("zfb_no", userInfo.getCompanyName());
                jsonObject.put("zfb_pic", userInfo.getCompanyPhone());
                jsonObject.put("wx_no", userInfo.getCompanyAddress());
                jsonObject.put("wx_pic", userInfo.getCompanyAddressDetail());
            }
            callBackResult.setMessage("查询成功");
            callBackResult.setCode(CallBackResult.SUCCESS);
            callBackResult.setData(jsonObject);
            return JSONObject.toJSONString(callBackResult);

        } catch (Exception e) {
            log.info("查询失败e=" + e.getMessage());
            callBackResult.setMessage(e.getMessage());
            return JSONObject.toJSONString(callBackResult);
        }
    }

}
