package com.summer.web.controller;

import cn.hutool.core.util.IdcardUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.summer.annotation.Log;
import com.summer.api.service.*;
import com.summer.dao.entity.*;
import com.summer.dao.mapper.*;
import com.summer.api.service.channel.IChannelAsyncCountService;
import com.summer.service.sms.ISmsService;
import com.summer.util.*;
import com.summer.util.encrypt.AESDecrypt;
import com.summer.pojo.vo.BackConfigParamsVo;
import com.summer.pojo.vo.BorrowUserVO;
import com.summer.pojo.vo.UserBlackListVo;
import com.summer.web.util.ExcelExportUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

/**
 * Desc:
 * Created by tl on 2018/12/20
 */
@RestController
@RequestMapping("/v1.0/api/userInfo")
public class UserInfoController extends BaseController {
    private static Logger log = LoggerFactory.getLogger(UserInfoController.class);
    @Resource
    private IUserInfoService userInfoService;
    @Resource
    private UserInfoMapper userInfoMapper;
    @Resource
    private PlateformChannelMapper plateformChannelMapper;
    @Autowired
    private ISmsService smsService;
    @Resource
    private ILoanRuleConfigService loanRuleConfigService;
    @Resource
    private IUserMoneyRateService userMoneyRateService;
    @Resource
    private IUserBlackListService userBlackListService;
    @Resource
    private IBackConfigParamsDao backConfigParamsDao;
    @Resource
    private IUserCardInfoDao userCardInfoDao;
    @Resource
    private IChannelAsyncCountService channelAsyncCountService;
    @Resource
    private IInfoIndexService infoIndexService;
    @Resource
    private RedisUtil redisUtil;

    @Resource
    private UserSealDAO userSealDAO;

    @Resource
    private OrderBorrowMapper orderBorrowMapper;

    private static final String SMS_LIMIT_KEY = "SMS_LIMIT_KEY_";

    /**
     * 客户列表
     *
     * @param params
     * @return
     */
    @PostMapping("/queryWithBorrow")
    public String queryWithBorrow(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, "参数非法");
        }
        Integer roleId = platformUser.getRoleId();
        // 超管和客服才能查看客户列表
        if (roleId != Constant.ROLEID_SUPER && roleId != Constant.ROLEID_WAITER) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "没有权限");
        }
        //等修复完分页后再校验入参
        params.put("status", "0");
        log.info("UserInfoController query params=" + params.toString());
        PageHelper.startPage(params);
        PageInfo<BorrowUserVO> pageInfo = userInfoService.queryWithBorrow(params);
        List<PlateformChannel> plateformChannels = plateformChannelMapper.selectSimple(new HashMap<>());
        Map<String, Object> res = new HashMap<>();
        res.put("page", pageInfo);
        if (Constant.ROLEID_WAITER == platformUser.getRoleId() && params.size() == 2) {
            res.put("page", new PageInfo<>(new ArrayList<BorrowUserVO>()));
        }
        res.put("channel", plateformChannels);
      /*  Map<String, Object> loanParams = new HashMap<>();
        params.put("status",Constant.INCRESE_MONNEY_CONFIG_STATUS);
        //查询贷款规则
        LoanRuleConfig loanConfig = loanRuleConfigService.findLoanConfigByParams(loanParams);*/
        LoanRuleConfig loanConfig = loanRuleConfigService.findByChannelId(0);
        if (null != loanConfig && null != loanConfig.getLoanAmount()) {
            res.put("loanAmount", loanConfig.getLoanAmount() / Constant.CENT_CHANGE_DOLLAR);
        } else {
            res.put("loanAmount", Constant.APPLY_MIN_MONEY);
        }
        return CallBackResult.returnJson(res);
    }

    /**
     * 未认证列表
     *
     * @param jsonData
     * @return
     */
    @PostMapping("/queryUnauthentic")
    public String queryUnauthentic(@RequestBody String jsonData, HttpServletRequest request) {

        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        Integer roleId = platformUser.getRoleId();
        // 超管和客服才能查看客户列表
        if (roleId != Constant.ROLEID_SUPER && roleId != Constant.ROLEID_WAITER) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "没有权限");
        }
        Map<String, Object> params = JSONObject.parseObject(jsonData);
        log.info("UserInfoController queryUnauthentic params=" + params.toString());
        PageHelper.startPage(params);
        Object authenticObj = params.get("authentic");
        if (null == authenticObj) {
            //未认证
            params.put("unauthentic", "true");
        }
        //查询正常的用户
        params.put("userStatus", 0);
        List<UserInfo> list = userInfoMapper.findParams(params);
        List<PlateformChannel> plateformChannels = plateformChannelMapper.selectSimple(new HashMap<>());
        Map<String, Object> res = new HashMap<>();
        res.put("page", new PageInfo<>(list));
        res.put("channel", plateformChannels);
        return CallBackResult.returnJson(res);
    }

    /**
     * ios短信列表
     *
     * @param params
     * @return
     */
    @PostMapping("/iosUser")
    public String iosUser(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        Map<String, Object> res = new HashMap<>();
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, "参数非法");
        }

        if (Constant.ROLEID_WAITER != platformUser.getRoleId() && Constant.ROLEID_SUPER != platformUser.getRoleId()) {
            res.put("page", new PageInfo<>(new ArrayList<BorrowUserVO>()));
            return CallBackResult.returnJson(res);
        }
        params.put("clientType", 2);
        PageHelper.startPage(params);
        List<BorrowUserVO> list = userInfoMapper.queryIos(params);
        res.put("page", new PageInfo<>(list));
        return CallBackResult.returnJson(res);
    }

    /**
     * ios短信
     *
     * @param params
     * @return
     */
    @PostMapping("/sendMsg")
    public String sendMsg(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, "参数非法");
        }
        if (Constant.ROLEID_WAITER != platformUser.getRoleId() && Constant.ROLEID_SUPER != platformUser.getRoleId()) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "没有权限");
        }
        Object phone = params.get("phone");
        Object url = params.get("url");
        if (phone == null || url == null) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "参数非法");
        }
        // String content = yunFengMsgUtil.getIosUpdate();
        Map<String, String> redisMap = smsService.getRedisMap();
        String content = redisMap.get("sms.service.iosUpdate");
        content = content.replace("#url#", url.toString());
        Boolean success = smsService.batchSend(phone.toString(), content);
        if (success) {
            return CallBackResult.returnJson(CallBackResult.SUCCESS, "发送成功");
        }
        return CallBackResult.returnJson(CallBackResult.BUSINESS_DEFEAT, "发送失败");
    }

    /**
     * 添加黑名单
     *
     * @param params
     * @return
     * @throws Exception
     */
    @Log(title = "添加黑名单")
    @PostMapping("/addBlackUser")
    public String addBlackUser(@RequestBody HashMap<String, Object> params, HttpServletRequest request) throws Exception {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, "登录超时");
        }
        if (Constant.ROLEID_WAITER != platformUser.getRoleId() && Constant.ROLEID_SUPER != platformUser.getRoleId()) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "没有权限");
        }
        int affectedRow = userInfoService.addBlackUser(params);
        if (affectedRow > 0) {
            Object idsObj = params.get("ids");
            if (null != idsObj) {
                List<UserBlackList> userBlackLists = new ArrayList<>();
                List<Integer> ids = JSONArray.parseArray(idsObj.toString(), Integer.class);
                for (Integer id : ids) {
                    UserInfo userInfo = userInfoService.selectByPrimaryKey(id);
                    if (null != userInfo) {
                        UserBlackList userBlackList = new UserBlackList();
                        //手机号
                        userBlackList.setPhone(userInfo.getPhone());
                        //用户姓名
                        userBlackList.setUserName(userInfo.getRealName());
                        //身份证号
                        userBlackList.setIdCard(userInfo.getIdCard());
                        //用户注册时间
                        userBlackList.setUserCreateTime(userInfo.getCreateTime());
                        //添加时间
                        userBlackList.setCreateTime(new Date());
                        Object blackReason = params.get("blackReason");
                        if (null != blackReason) {
                            //原因
                            userBlackList.setRemark(blackReason.toString());
                        }
                        //状态0黑名单，1移除黑名单
                        userBlackList.setStatus(0);
                        UserBlackList byPhone = userBlackListService.findByPhone(userInfo.getPhone());
                        if (null != byPhone) {
                            userBlackList.setId(byPhone.getId());
                            userBlackListService.updateByPrimaryKeySelective(userBlackList);
                        } else {
                            userBlackListService.insertSelective(userBlackList);
                        }
                        userBlackList.setId(null);
                        userBlackLists.add(userBlackList);
                    }
                }
                /**商户添加黑名单，同时向数据中心推送*/
//                blackUserService.addBlackUser(userBlackLists);
            }
            return CallBackResult.returnJson(CallBackResult.SUCCESS, CallBackResult.MSG_SUCCESS, null);
        } else {
            return CallBackResult.returnErrorJson();
        }
    }

    /**
     * 删除黑名单
     *
     * @param params
     * @return
     * @throws Exception
     */
    @Log(title = "删除黑名单")
    @PostMapping("/deleteBlackUser")
    public String deleteBlackUser(@RequestBody HashMap<String, Object> params, HttpServletRequest request) throws Exception {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, "登录超时");
        }
        if (Constant.ROLEID_WAITER != platformUser.getRoleId() && Constant.ROLEID_SUPER != platformUser.getRoleId()) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "没有权限");
        }
        Integer id = (Integer) params.get("id");
        //修改状态为移除黑名单
        UserBlackList userBlackList = userBlackListService.selectByPrimaryKey(id);
        if (null != userBlackList) {
            UserBlackList userBlackListUp = new UserBlackList();
            //黑名单表的主键Id
            userBlackListUp.setId(userBlackList.getId());
            //状态
            userBlackListUp.setStatus(1);
            userBlackListUp.setUpdateTime(new Date());
            userBlackListService.updateByPrimaryKeySelective(userBlackListUp);
            UserInfo userInfo = userInfoService.findByPhone(userBlackList.getPhone());
            if (null != userInfo) {
                userInfoService.deleteBlackUser(userInfo.getId());
            }
        }
        return CallBackResult.returnJson(CallBackResult.SUCCESS, CallBackResult.MSG_SUCCESS, null);
    }

    /**
     * 手动为用户提额
     *
     * @param params
     * @return
     * @throws Exception
     */
    @Log(title = "手动提额")
    @PostMapping("/update/money")
    public String updateUserInfo(@RequestBody HashMap<String, Object> params, HttpServletRequest request) throws Exception {
        PlatformUser platformUser = redisPlatformUser(request);
        // 登录失效
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        // 超管和客服可以查询
        Integer roleId = platformUser.getRoleId();
        if (roleId != Constant.ROLEID_SUPER && roleId != Constant.ROLEID_WAITER) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "没有权限");
        }
        Object id = params.get("id");
        Object amountAvailable = params.get("amountAvailable");
        if (null == id) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, CallBackResult.PARAM_IS_EXCEPTION_MSG);
        }
        params.put("userId", id);
        if (null != amountAvailable) {
            int maxAmount = Integer.parseInt(amountAvailable.toString());
            params.put("maxAmount", maxAmount * Constant.DOLLAR_CHANGE_PENNY);
            // 借款金额限制判断
            String moneylimit = moneylimit(maxAmount, (Integer) id);
            if (null != moneylimit) {
                return moneylimit;
            }
        }
        int isSucc = userMoneyRateService.updateByUserId(params);
        if (isSucc > 0) {
            return CallBackResult.returnJson(CallBackResult.SUCCESS, Constant.INCREASE_IS_SUCC);
        }
        return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.INCREASE_IS_FAILD);
    }

    //
    @PostMapping("/updateAmount")
    @ResponseBody
    public Object updateApplicationAmount(HttpServletRequest request, Integer id, Integer applyAmount, Integer loanTerm, Integer feeApr){
        if (null == applyAmount || applyAmount <= 0) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "申请金额不能为空且不能为0！");
        }
        if (null == loanTerm || loanTerm <= 0) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "借款天数不能为空且不能为0！");
        }
        if (null == feeApr || feeApr <= 0) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "费用利率不能为空且不能为0！");
        }

        Double rate = new BigDecimal(feeApr).divide(BigDecimal.valueOf(Constant.DOLLAR_CHANGE_PENNY)).doubleValue();
        UserMoneyRate byUserId = userMoneyRateService.findByUserId(id);
        int count = 0;
        if (null != byUserId) {
            byUserId.setUserId(id);
            byUserId.setMaxAmount(applyAmount * Constant.DOLLAR_CHANGE_PENNY);
            byUserId.setLoanTerm(loanTerm);
            byUserId.setServiceCharge(rate - 0.007);
            count = userMoneyRateService.update(byUserId);
        }
        return count > 0 ? CallBackResult.ok("修改成功") : CallBackResult.fail();
    }


    /**
     * 查詢黑名单用户列表，包括催收被拉黑的用户
     *
     * @param params
     * @param request
     * @return
     */
    @PostMapping("/queryBlackUser")
    public String blackListUser(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, "参数非法");
        }
        // 超管才能查询
        if (platformUser.getRoleId() != Constant.ROLEID_SUPER) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "没有权限");
        }
        Map<String, Object> res = new HashMap<>();
        PageHelper.startPage(params);

        params.put("status", 0);
        // AESDecrypt.encryptMapPhone(params);
        List<UserBlackListVo> userBlackListVos = userBlackListService.findByParam(params);
      /*  for (UserBlackListVo userBlackListVo : userBlackListVos) {
            userBlackListVo.setPhone(PhoneUtil.maskPhoneNum(userBlackListVo.getPhone()));
            userBlackListVo.setIdCard(IdcardUtil.hide(userBlackListVo.getIdCard(), 4, 15));
        }*/
        res.put("page", new PageInfo<>(userBlackListVos));
        return CallBackResult.returnJson(res);
    }

    /**
     * 修改身份信息
     *
     * @param params
     * @return
     * @throws Exception
     */
    @Log(title = "修改身份证信息")
    @PostMapping("/updateUserInformation")
    public String updateUserInformation(@RequestBody HashMap<String, Object> params, HttpServletRequest request) throws Exception {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }

        if (platformUser.getRoleId() != Constant.ROLEID_SUPER) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.UN_AUTH_VIST);
        }

        Object id = params.get("id");
        if (null == id) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, CallBackResult.PARAM_IS_EXCEPTION_MSG);
        }
        int isSucc = userInfoService.updateByUserId(params);
        if (isSucc > 0) {
//            generateUserSeal(id.toString());
            return CallBackResult.returnJson(CallBackResult.SUCCESS, "身份信息修改成功！");
        }
        return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "身份信息修改失败！");
    }

    @RequestMapping(value = "/bindUpdate", method = RequestMethod.POST)
    public String bindUpdate(@RequestBody HashMap<String, String> params, HttpServletRequest request) {
        CallBackResult callBackResult = new CallBackResult(CallBackResult.BUSINESS_DEFEAT, "修改失败,请求参数不符合要求");
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        // 超管和客服才能修改绑卡
        if (Constant.ROLEID_SUPER != platformUser.getRoleId() && Constant.ROLEID_WAITER != platformUser.getRoleId()) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "无权操作");
        }
        String rsaSign = "";
        try {

            String user_id = params.get("user_id");
            String acct_name = params.get("acct_name");
            String mobilePhone = params.get("mobilePhone");
            String card_no = params.get("card_no");
            String id_no = params.get("id_no");


            String bank = params.get("bank");
            String branch = params.get("branch");
            String zfb_no = params.get("zfb_no");
            String zfb_pic = params.get("zfb_pic");
            String wx_no = params.get("wx_no");
            String wx_pic = params.get("wx_pic");

            UserInfo user = userInfoMapper.selectByPrimaryKey(Integer.parseInt(user_id));
            if (user == null) {
                return JSONObject.toJSONString(callBackResult);
            }
            if (StringUtils.isBlank(card_no)) {
                UserInfo userInfo = new UserInfo();
                userInfo.setId(user.getId());
                userInfo.setRealName(acct_name);
                userInfo.setIdCard(id_no);
                userInfoMapper.updateByPrimaryKeySelective(userInfo);
            } else {

                HashMap<String, Object> param = new HashMap<String, Object>();
                param.put("userId", Integer.parseInt(user_id));
                param.put("limit", 1);
                List<UserCardInfo> userCardInfoList = userCardInfoDao.findUserCardByUserId(param);
                if (CollectionUtils.isEmpty(userCardInfoList)) {
                    callBackResult = saveCard(user_id, acct_name, mobilePhone, card_no, id_no, user, bank,
                            "", callBackResult, branch, zfb_no, zfb_pic, wx_no, wx_pic, "usdt_tag");
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

                    UserInfo userInfo2 = new UserInfo();
                    userInfo2.setId(user.getId());
                    userInfo2.setCompanyName(zfb_no);
                    userInfo2.setRealName(acct_name);
                    userInfo2.setIdCard(id_no);
                    userInfo2.setCompanyPhone(zfb_pic);
                    userInfo2.setCompanyAddress(wx_no);
                    userInfo2.setCompanyAddressDetail(wx_pic);
                    userInfoMapper.updateByPrimaryKeySelective(userInfo2);
                }
            }

            callBackResult.setMessage("修改成功");
            callBackResult.setCode(CallBackResult.SUCCESS);
            return JSONObject.toJSONString(callBackResult);

        } catch (Exception e) {
            log.info("修改失败e=" + e.getMessage());
            e.printStackTrace();
            callBackResult.setMessage(e.getMessage());
            return JSONObject.toJSONString(callBackResult);
        }
    }

    private CallBackResult saveCard(String user_id, String acct_name, String mobilePhone, String card_no, String id_no
            , UserInfo user, String value, String orderTrxid, CallBackResult callBackResult, String branch, String zfb_no, String zfb_pic, String wx_no, String wx_pic, String bindType) {
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
        boolean saveCardInfoRes = tempBindCardProcedure(params);
        if (saveCardInfoRes) {

            String tel = user.getPhone();
            if (StringUtils.isNoneBlank(tel)) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        //  String content = yunFengMsgUtil.getBankBindSucc();
                        //绑卡成功短信
                        Map<String, String> redisMap = smsService.getRedisMap();
                        String content = redisMap.get("sms.service.bankBindSucc");
                        smsService.batchSend(tel, content);
                        // yunFengMsgUtil.batchSend(tel, "尊敬的用户您好！系统升级完成，银行卡已成功绑定，请继续补充完善资料，获得额度，马上到账");
                    }
                }).start();
            }
            callBackResult.setMessage("绑卡成功，请返回首页发起借款");
            callBackResult.setCode(CallBackResult.SUCCESS);
            /**绑卡成功异步统计*/
            channelAsyncCountService.bankAuthIsSuccCount(user, new Date());
            return callBackResult;
        } else {
            callBackResult.setMessage("绑卡失败");
            return callBackResult;
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
                user.setIdCard(param.get("idNo"));
                user.setRealName(param.get("acctName"));
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
        String orderTrxId = param.get("OrderTrxId");
        String bindType = param.get("bindType");
        cardInfo.setBindingStatus(0);
        if ("cj_tag".equals(bindType)) {
            cardInfo.setAgreeno("cj_tag");
            cardInfo.setMchntSsn(orderTrxId);
        } else if ("usdt_tag".equals(bindType)) {
            cardInfo.setAgreeno("usdt_tag");
        }
        cardInfo.setBindingStatus(1);
        cardInfo.setOpenName(StringUtils.isBlank(acctName) ? u.getRealName() : acctName);
        //用户id
        cardInfo.setUserId(u.getId());
        //开户姓名
        //银行名称
        cardInfo.setBankName(param.get("bankName"));
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
                bankId = 0;


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

    /**
     * 查询绑卡信息
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/bindInfo", method = RequestMethod.GET)
    public String bindInfo(HttpServletRequest request) {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        // 超管和客服才能查询
        if (Constant.ROLEID_SUPER != platformUser.getRoleId() && Constant.ROLEID_WAITER != platformUser.getRoleId()) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "无权操作");
        }
        String rsaSign = "";
        CallBackResult callBackResult = new CallBackResult(CallBackResult.BUSINESS_DEFEAT, "查询失败,请求参数不符合要求");
        try {
            String user_id = request.getParameter("user_id");
            UserInfo user = userInfoMapper.selectByPrimaryKey(Integer.parseInt(user_id));
            if (user == null) {
                return JSONObject.toJSONString(callBackResult);
            }


            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put("userId", user_id);
            params.put("limit", 1);
            List<UserCardInfo> userCardInfoList = userCardInfoDao.findUserCardByUserId(params);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("acct_name", user.getRealName());
            jsonObject.put("phone", user.getPhone());
            jsonObject.put("icCard", user.getIdCard());
            if (CollectionUtils.isEmpty(userCardInfoList)) {
                jsonObject.put("bindStatus", 0);
                jsonObject.put("cardType", "");
                // 状态 (0:未生效   1:已生效)

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
                jsonObject.put("zfb_no", user.getCompanyName());
                jsonObject.put("zfb_pic", user.getCompanyPhone());
                jsonObject.put("wx_no", user.getCompanyAddress());
                jsonObject.put("wx_pic", user.getCompanyAddressDetail());
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


//    @Async
//    public void generateUserSeal(String id) {
//        UserInfo user = userInfoMapper.selectByPrimaryKey(Integer.parseInt(id));
//        if (user == null) {
//            return;
//        }
//        // 获取已初始化的客户端以便可以正常调用SDK提供的各种电子签名服务,做全局使用只获取一次即可
//        ServiceClient serviceClient = null;
//        String personAccountId = "";
//        String personSealData = "";
//        try {
//            serviceClient = InitClientHelper.doGetServiceClient(DemoConfig.PROJECT_ID);
//            // 个人客户账户AccountId,可将该AccountId保存到贵司数据库以便日后直接使用,只创建一次即可
//            personAccountId = RunTSignDemo.addPersonAccount(serviceClient, user.getRealName(), user.getIdCard());
//
//            // 个人客户印章SealData,可将该SealData保存到贵司数据库以便日后直接使用,只创建一次即可
//            personSealData = RunTSignDemo.addPersonTemplateSeal(serviceClient, personAccountId);
//        } catch (DemoException e) {
//            e.printStackTrace();
//        }
//        UserSeal userSeal = userSealDAO.selectByPhone(user.getPhone());
//
//        if (userSeal == null) {
//            userSeal = new UserSeal(user.getId(), user.getPhone(), personAccountId, personSealData);
//            userSealDAO.insertSelective(userSeal);
//        } else {
//            userSeal.setAccountId(personAccountId);
//            userSeal.setSealData(personSealData);
//            userSealDAO.updateByPrimaryKeySelective(userSeal);
//        }
//    }

    /**
     * 查询系统是新系统还是老系统
     *
     * @return
     */
    @PostMapping("/getVersion")
    public String getVersion(HttpServletRequest request) {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        //通过key查询系统是新系统还是老系统
        BackConfigParamsVo scoreType = backConfigParamsDao.findBySysKey("env_new");
        return CallBackResult.returnJson(scoreType);
    }

    /**
     * 新系统客户列表
     *
     * @param params
     * @return
     */
    @PostMapping("/queryWithBorrowNew")
    public String queryWithBorrowNew(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        Integer roleId = platformUser.getRoleId();
        // 超管和客服才能查看客户列表
        if (roleId != Constant.ROLEID_SUPER && roleId != Constant.ROLEID_WAITER) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "没有权限");
        }
        //等修复完分页后再校验入参
        params.put("ustatus", "0");
        log.info("UserInfoController query params=" + params.toString());
        PageHelper.startPage(params);
        long l = System.currentTimeMillis();
        PageInfo<BorrowUserVO> pageInfo = userInfoService.queryWithBorrowNew(params);
       /* for (BorrowUserVO borrowUserVO : pageInfo.getList()) {
            String phone = PhoneUtil.maskPhoneNum(borrowUserVO.getPhone());
            borrowUserVO.setPhone(phone);
            borrowUserVO.setPhone1(phone);
            borrowUserVO.setIdCard(IdcardUtil.hide(borrowUserVO.getIdCard(), 5, 15));
        }*/
        long l1 = System.currentTimeMillis();
        List<PlateformChannel> plateformChannels = plateformChannelMapper.selectSimple(new HashMap<>());
        Map<String, Object> res = new HashMap<>();
        res.put("page", pageInfo);
        if (Constant.ROLEID_WAITER == roleId && null == params.get("phone") && null == params.get("realName")) {
            res.put("page", new PageInfo<>(new ArrayList<BorrowUserVO>()));
        }
        res.put("channel", plateformChannels);
       /* Map<String, Object> loanParams = new HashMap<>();
        params.put("status",Constant.INCRESE_MONNEY_CONFIG_STATUS);*/
        //查询贷款规则
        LoanRuleConfig loanConfig = loanRuleConfigService.findByChannelId(0);
        if (null != loanConfig && null != loanConfig.getLoanAmount()) {
            res.put("loanAmount", loanConfig.getLoanAmount() / Constant.CENT_CHANGE_DOLLAR);
        } else {
            res.put("loanAmount", Constant.APPLY_MIN_MONEY);
        }
        log.info("用户列表params={} db={}", params.toString(), (l1 - l));
        return CallBackResult.returnJson(res);
    }

    /**
     * 删除用户（做逻辑删除，非特殊情况不做物理删除，直接删除不利于统计bug相关排查）
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Log(title = "删除用户")
    @RequestMapping("/clean")
    public Object deleteUserInfo(Integer id, HttpServletRequest request) throws Exception {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, "登录超时");
        }
        //必须是超级管理员才能删除
        if (platformUser.getRoleId() != 1) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "对不起，您没有该权限！");
        }
        if (null == id) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "参数非法");
        }
        UserInfo userInfo = new UserInfo();
        userInfo.setId(id);
        userInfo.setStatus(Byte.parseByte("2"));
        userInfoMapper.updateByPrimaryKeySelective(userInfo);
        return CallBackResult.returnJson(CallBackResult.SUCCESS, "删除成功");
    }

    /**
     * 借款金额限制
     *
     * @param loanMoney
     * @return
     */
    public String moneylimit(Integer loanMoney, Integer userId) {
        Integer limitStart = 0;
        Integer limitend = 10000;
        //最小金额
        LoanRuleConfig loanRuleConfig = loanRuleConfigService.findByUserId(userId);
        if (null != loanRuleConfig) {
            limitStart = loanRuleConfig.getLoanAmount() / 100;
        }
        //最大金额
        BackConfigParamsVo loanLimitEnd = backConfigParamsDao.findBySysKey("loan_limitEnd");
        if (null != loanLimitEnd) {
            limitend = loanLimitEnd.getSysValue();
        }

        if (FigureUtil.isGthundred(loanMoney, limitStart, limitend)) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "保存失败，借款金额必须大于" + limitStart + "，小于" + limitend + "元！");
        }
        return null;
    }

    /**
     * 重置APP用户登录密码
     *
     * @param jsonData
     * @return
     * @throws Exception
     */
    @Log(title = "重置APP用户登录密码")
    @RequestMapping("/updatePassword")
    public Object updatePassword(@RequestBody String jsonData, HttpServletRequest request) throws Exception {
        Map<String, Object> params = JSONObject.parseObject(jsonData);
        Integer id = (Integer) params.get("id");
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, "参数非法");
        }
        Integer roleId = platformUser.getRoleId();
        // 必须是超级管理员才能重置密码
        if (null == roleId || roleId != 1) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "对不起，您没有该权限！");
        }
        if (null == id) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "参数非法");
        }
        UserInfo userInfo = userInfoMapper.selectByPrimaryKey(id);
        if (userInfo == null) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "参数非法！");
        }
        UserInfo newUserInfo = new UserInfo();
        newUserInfo.setId(id);
        // 随机盐
        /*String randomSalt = RandomUtil.getRandomSalt(6);
        // 随机密码
        String newPassword = RandomUtil.getRandomLetter(2) + RandomUtil.getRandomSalt(6);
        // 密码：MD5（密码+盐）
        newUserInfo.setPassword(MD5Util.md5(newPassword + randomSalt));
        //密码base64加密
        String encodePwd = Base64Util.encode(newPassword);
        newUserInfo.setEquipmentNumber(encodePwd);
        // 设置盐
        newUserInfo.setLoginPwdSalt(randomSalt);*/
        newUserInfo.setPassword("ff72cee0f1da0eed904407e177f1688b");
        newUserInfo.setLoginPwdSalt("IPg5ue");
        newUserInfo.setEquipmentNumber("MTIzNDU2");

        if (userInfoMapper.updateByPrimaryKeySelective(newUserInfo) > 0) {
            // yunFengMsgUtil.batchSend(userInfo.getPhone(), "尊敬的用户，您的APP登录密码是：" + newPassword);
            return CallBackResult.returnJson(CallBackResult.SUCCESS, "重置密码成功！");
        }
        return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "重置密码失败！");
    }


    /**
     * 查询用户密码，需要密码才能查看
     *
     * @return
     */
    @RequestMapping("/userAuth")
    public Object findPwd(HttpServletRequest request, @RequestBody String jsonData) {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }

        if (platformUser.getRoleId() != Constant.ROLEID_SUPER) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.UN_AUTH_VIST);
        }
        Map<String, Object> params = JSONObject.parseObject(jsonData);
        log.info("【商户查询用户密码】，phone={},params={}", platformUser.getPhoneNumber(), params);
        Object id = params.get("id");
        Object pwd = params.get("password");
        if (null == id || null == pwd) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "参数错误");
        }
        int userId = Integer.parseInt(id.toString());
        String password = pwd.toString();

        // log.info("【保存贷款规则】-----phone={},ip={},param={}",platformUser.getPhoneNumber(),IpAdrressUtil.getIpAdrress(request), JSONObject.toJSONString(loanRuleConfigDto));

        // 密码错误次数验证
        String key = "findUserPwd_" + platformUser.getPhoneNumber() + "_" + userId + "_" + DateUtil.getDateFormat(new Date(), "yyyy-MM-dd");
        Object times = redisUtil.getObject(key);
        if (times != null && (Integer) times > 5) {
            log.info("【查询用户密码次数超限】，phone={},password={},times={}", platformUser.getPhoneNumber(), password, times);
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "密码错误次数超限");
        }
        // 超管密码
        String pass = backConfigParamsDao.findStrValue("password");
        //密码校验
        if (org.apache.commons.lang3.StringUtils.isBlank(password) || !password.equals(pass)) {
            log.info("查询用户密码,输入密码错误，phone={},password={}，times={}", platformUser.getPhoneNumber(), password, times);
            // 密码失败次数记录
            LimitTimesUtil.incr(60 * 60 * 24, redisUtil, key);
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "账号或密码错误！");
        } else {
            redisUtil.del(key);
        }

        UserInfo userInfo = userInfoMapper.findPwdById(userId);
        if (null != userInfo) {
            Map<String, Object> resultMap = new HashMap<>();
            String equipmentNumber = userInfo.getEquipmentNumber();
            if (StringUtils.isNotBlank(equipmentNumber)) {
                resultMap.put("userAuth", equipmentNumber.substring(0, 4));
                resultMap.put("userName", equipmentNumber.substring(4, equipmentNumber.length()));
                return CallBackResult.returnJson(CallBackResult.SUCCESS, "成功", resultMap);
            }

        }
        return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "该用户暂无密码");
    }

    /**
     * 查询用户联系密码，需要密码才能查看
     *
     * @return
     */
    @RequestMapping("/getContactsPwd")
    public Object getContactsPwd(HttpServletRequest request, @RequestBody String jsonData) {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        if (platformUser.getRoleId() != Constant.ROLEID_SUPER) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.UN_AUTH_VIST);
        }
        Map<String, Object> params = JSONObject.parseObject(jsonData);
        log.info("【商户查询用户联系手机号】，phone={},params={}", platformUser.getPhoneNumber(), params);
        Object id = params.get("id");
        Object pwd = params.get("contactsPwd");
        if (null == id || null == pwd) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "参数错误");
        }
        int userId = Integer.parseInt(id.toString());
        String password = pwd.toString();

        // 密码错误次数验证
        String key = "getContactsPwd_" + platformUser.getPhoneNumber() + "_" + userId + "_" + DateUtil.getDateFormat(new Date(), "yyyy-MM-dd");
        Object times = redisUtil.getObject(key);
        if (times != null && (Integer) times > 5) {
            log.info("【查询用户联系密码次数超限】，phone={},password={},times={}", platformUser.getPhoneNumber(), password, times);
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "密码错误次数超限");
        }
        // 超管密码
        String pass = backConfigParamsDao.findStrValue("contactsPwd");
        //密码校验
        if (org.apache.commons.lang3.StringUtils.isBlank(password) || !password.equals(pass)) {
            log.info("查询用户密码,输入密码错误，phone={},password={}，times={}", platformUser.getPhoneNumber(), password, times);
            // 密码失败次数记录
            LimitTimesUtil.incr(60 * 60 * 24, redisUtil, key);
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "账号或密码错误！");
        } else {
            redisUtil.del(key);
        }
        UserInfo userInfo = userInfoMapper.selectByUserId(userId);
        if (null != userInfo) {
            Map<String, Object> resultMap = new HashMap<>();
            String phone = userInfo.getPhone();
            if (StringUtils.isNotBlank(phone)) {
                resultMap.put("userPhone", phone);
                return CallBackResult.returnJson(CallBackResult.SUCCESS, "成功", resultMap);
            }

        }
        return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "查询联系手机号出错");
    }


    /**
     * 导出用户列表Excel
     *
     * @param
     * @return
     */
    @PostMapping("/userInfoX")
    public String exportUserXls(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String, Object> params) throws Exception {
        /*PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        Integer roleId = platformUser.getRoleId();
        if (roleId != Constant.ROLEID_SUPER || isExporExcel(platformUser.getPhoneNumber())) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "没有权限");
        }*/
        params.put("ustatus", "0");
        List<BorrowUserVO> borrowUserVOList = userInfoService.queryWithBorrowNew(params).getList();
        List<Map> map = new ArrayList<>();
        for (BorrowUserVO vo : borrowUserVOList) {
            Map<String, Object> param = new HashMap<>();
            param.put("realName", vo.getRealName());
            param.put("phone", vo.getPhone());
            param.put("idCard", vo.getIdCard());
            param.put("channelName", vo.getChannelName());
            param.put("appType", ExcelExportUtil.changApptype(vo.getClientType()));
            param.put("createIp", vo.getCreateIp());
            param.put("createTimeChg", vo.getCreateTimeChg());
            param.put("authenticStatusName", ExcelExportUtil.changeStatusName(vo.getAuthenticStatus()));
            param.put("borrowStatusName", vo.getBorrowStatusName());
            param.put("amountAvailable", vo.getAmountAvailable() / 100);
            param.put("overdueCount", vo.getOverdueCount());
            map.add(param);
        }
        String[] title = new String[]{"客户姓名", "手机号码", "身份证号", "渠道来源", "手机系统", "IP地址", "注册时间", "认证项", "订单状态", "可借金额/元", "逾期次数"};
        String[] properties = new String[]{"realName", "phone", "idCard", "channelName", "appType", "createIp", "createTimeChg", "authenticStatusName", "borrowStatusName", "amountAvailable", "overdueCount"};
        String sheetName = "用户列表";
        ExcelExportUtil.exportExcel(response, map, title, properties, sheetName);
        return CallBackResult.returnJson(CallBackResult.SUCCESS, "导出成功");
    }


    /**
     * 重置APP用户登录密码
     *
     * @param jsonData
     * @return
     * @throws Exception
     */
    @Log(title = "清除手机短信锁")
    @RequestMapping("/cleanLockPhone")
    public Object cleanLockPhone(@RequestBody String jsonData, HttpServletRequest request) throws Exception {
        Map<String, Object> params = JSONObject.parseObject(jsonData);
        String phone = params.get("phone").toString();
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, "参数非法");
        }
        if (null == phone) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "参数非法");
        }
        UserInfo userInfo = userInfoMapper.findByPhone(phone);
        if (userInfo == null) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "无此用户！");
        }
        String limitKey = SMS_LIMIT_KEY + phone + "_4";
        if (!redisUtil.hasKey(limitKey)) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "无此手机号！");
        }
        redisUtil.del(limitKey);
        return CallBackResult.returnJson(CallBackResult.SUCCESS, "清除成功！");

    }

}
