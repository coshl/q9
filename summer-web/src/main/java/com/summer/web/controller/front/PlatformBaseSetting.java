package com.summer.web.controller.front;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.summer.annotation.Log;
import com.summer.web.controller.BaseController;
import com.summer.dao.entity.*;
import com.summer.dao.mapper.*;
import com.summer.group.SaveAppBaseSetting;
import com.summer.pojo.dto.BaseSettingDTO;
import com.summer.api.service.IInfoNoticeService;
import com.summer.util.*;
import com.summer.util.log.StringUtils;
import com.summer.pojo.vo.InfoNoticeVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ls
 * @Title: app基础配置
 * @date 2019/1/25 18:55
 */
@RestController
@Slf4j
@RequestMapping("/v1.0/api/app/base")
public class PlatformBaseSetting extends BaseController {
    @Resource
    private AppBaseDAO appBaseDAO;
    @Resource
    private IBackConfigParamsDao backConfigParamsDao;
    @Resource
    private IInfoNoticeService infoNoticeService;
    @Resource
    private UserInfoMapper userInfoMapper;
    @Resource
    private IUserCardInfoDao userCardInfoDao;

    /**
     * 查询app基础资料
     *
     * @return
     */
    @GetMapping("/getAppBaseSetting")
    public String getAppBaseSetting(HttpServletRequest request) {
        PlatformUser platformUser = redisPlatformUser(request);
        UserInfo userInfo = redisUser(request);
        if (null == platformUser&&null==userInfo) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        // 超管才能查询
        if (null != platformUser&&platformUser.getRoleId() != Constant.ROLEID_SUPER) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "没有权限");
        }
        AppBase appBase = appBaseDAO.selectByPrimaryKey(1);
        String ag_company = backConfigParamsDao.findStrValue("ag_company");
        String ag_code = backConfigParamsDao.findStrValue("ag_code");
        Map<String, Object> map = new HashMap<>();
        if (null != appBase) {
            map.put("name", appBase.getName());
            map.put("descript", appBase.getAppDesc());
            map.put("servicePhone", appBase.getServicePhone());
            map.put("logo", appBase.getImgUrl());
            map.put("weixin", appBase.getWeixin());
            map.put("qq", appBase.getQq());
            map.put("id", appBase.getId());
            map.put("company", ag_company);
            map.put("code", ag_code);
            // 查询需要展示的公告
            List<InfoNoticeVo> notice = infoNoticeService.findNotice();
            map.put("notice", notice);
            //线下支付信息
            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put("userId", 0);
            params.put("limit", 1);
            List<UserCardInfo> userCardInfoList = userCardInfoDao.findUserCardByUserId(params);
            JSONArray pay = new JSONArray();
            JSONObject bank = new JSONObject();
            JSONObject zfb = new JSONObject();

            if (!CollectionUtils.isEmpty(userCardInfoList)) {
                UserCardInfo cardInfo = userCardInfoList.get(0);
                //银行卡号
                // (1:信用卡   2:借记卡,3:对公账号)

                bank.put("acct_name", cardInfo.getOpenName());
                bank.put("card_no", cardInfo.getCardNo());
                bank.put("id_no", cardInfo.getAgreeno());

                bank.put("bank", cardInfo.getBankName());
                bank.put("branch", cardInfo.getBankAddress());

            }
            UserInfo user = userInfoMapper.findByPhone("0");
            if (user != null) {
                zfb.put("card_no", user.getCompanyName());
                zfb.put("acct_name", user.getCompanyPhone());
                zfb.put("name", user.getRealName());

            }
            bank.put("type", "1");
            pay.add(bank);
            zfb.put("type", "2");
            pay.add(zfb);
            map.put("pay", pay);
        }
        return CallBackResult.returnJson(map);
    }

    /**
     * 添加公告
     *
     * @param jsonData
     * @param request
     * @return
     */
    @PostMapping("/addNotice")
    public String addNotice(@RequestBody String jsonData, HttpServletRequest request) {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        // 超管才能添加
        if (platformUser.getRoleId() != Constant.ROLEID_SUPER) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "没有权限");
        }
        Map<String, Object> params = JSONObject.parseObject(jsonData);
        InfoNotice notice = new InfoNotice((String) params.get("noticeTitle"), (String) params.get("noticeContent"), (String) params.get("linkUrl"), 1, (Integer) params.get("noticeType"));
        if (infoNoticeService.insertSelect(notice) > 0) {
            return CallBackResult.returnSuccessJson();
        } else {
            return CallBackResult.returnJson(CallBackResult.INVALID_REQUEST, "新增公告失败");
        }
    }

    /**
     * 修改公告内容
     *
     * @param jsonData
     * @param request
     * @return
     */
    @PostMapping("/updateNotice")
    public String updateNotice(@RequestBody String jsonData, HttpServletRequest request) {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        // 超管才能修改
        if (platformUser.getRoleId() != Constant.ROLEID_SUPER) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "没有权限");
        }
        Map<String, Object> params = JSONObject.parseObject(jsonData);
        Integer id = (Integer) params.get("id");
        if (null == id) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "参数非法");
        }
        InfoNotice notice = new InfoNotice();
        notice.setId(id);
        notice.setStatus((Integer) params.get("status"));
        // 公告标题
        notice.setNoticeTitle((String) params.get("noticeTitle"));
        // 公告内容
        notice.setNoticeContent((String) params.get("noticeContent"));
        // 公告链接
        notice.setLinkUrl((String) params.get("linkUrl"));
        // 公告类型(1,消息中心公告，2,APP首页轮播公告，3，弹框公告)
        notice.setNoticeType((Integer) params.get("noticeType"));
        if (infoNoticeService.updateSelect(notice) > 0) {
            return CallBackResult.returnSuccessJson();
        } else {
            return CallBackResult.returnJson(CallBackResult.INVALID_REQUEST, "修改公告失败");
        }
    }

    /**
     * 修改app基础资料设置
     *
     * @param baseSetting
     * @return
     */
    @Log(title = "编辑产品基础资料设置")
    @PostMapping("/saveAppBaseSetting")
    @Transactional
    public String saveAppBaseSetting(@Validated(SaveAppBaseSetting.class)
                                     @RequestBody BaseSettingDTO baseSetting, HttpServletRequest request) {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        // 超管才能修改
        if (platformUser.getRoleId() != Constant.ROLEID_SUPER) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "没有权限");
        }
        AppBase record = new AppBase();
        //APP名称
        record.setName(baseSetting.getName());
        //APP描述
        record.setAppDesc(baseSetting.getDescript());
        //客服电话
        record.setServicePhone(baseSetting.getServicePhone());
        //客服微信
        record.setWeixin(baseSetting.getWeixin());
        //客服QQ
        record.setQq(baseSetting.getQq());
        record.setId(1);
        record.setImgUrl(baseSetting.getLogo());
        AppBase appBase = appBaseDAO.selectByPrimaryKey(1);
        if (null == appBase) {
            int status = appBaseDAO.insertSelective(record);
            if (status > 0) {
                return CallBackResult.returnSuccessJson();
            }
        } else {
            int update = appBaseDAO.updateByPrimaryKeySelective(record);
            if (update > 0) {
                return CallBackResult.returnSuccessJson();
            }
        }
        return CallBackResult.returnErrorJson();
    }

    /**
     * 编辑公司信息
     *
     * @return
     */
    @Log(title = "编辑公司信息")
    @PostMapping("/updateAg")
    public String updateAg(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        // 超管才能修改
        if (platformUser.getRoleId() != Constant.ROLEID_SUPER) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "没有权限");
        }
        Object companyObj = params.get("company");
        Object codeObj = params.get("code");
        if (null == companyObj || null == codeObj) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "参数非法");
        }
        BackConfigParams com = new BackConfigParams();
        String company = companyObj.toString();
        com.setSysValue(company);
        com.setSysKey("ag_company");
        backConfigParamsDao.updateBySyskey(com);
        String code = codeObj.toString();
        com.setSysValue(code);
        com.setSysKey("ag_code");
        backConfigParamsDao.updateBySyskey(com);
//        generateCompanySeal(company, code);
        return CallBackResult.returnSuccessJson();
    }

//    @Async
//    public void generateCompanySeal(String company, String code) {
//        // 获取已初始化的客户端以便可以正常调用SDK提供的各种电子签名服务,做全局使用只获取一次即可
//        ServiceClient serviceClient = null;
//        String personAccountId = null;
//        String personSealData = null;
//        try {
//            serviceClient = InitClientHelper.doGetServiceClient(DemoConfig.PROJECT_ID);
//            // 个人客户账户AccountId,可将该AccountId保存到贵司数据库以便日后直接使用,只创建一次即可
//            personAccountId = RunTSignDemo.addOrganizeAccount(serviceClient, company, code);
//
//            // 个人客户印章SealData,可将该SealData保存到贵司数据库以便日后直接使用,只创建一次即可
//            personSealData = RunTSignDemo.addOrganizeTemplateSeal(serviceClient, personAccountId);
//            UserSeal userSeal = userSealDAO.selectByPhone(Pid);
//
//            if (userSeal == null) {
//                userSeal = new UserSeal(0, Pid, personAccountId, personSealData);
//                userSealDAO.insertSelective(userSeal);
//            } else if (StringUtils.isBlank(userSeal.getAccountId())) {
//                userSeal.setAccountId(personAccountId);
//                userSeal.setSealData(personSealData);
//                userSealDAO.updateByPrimaryKeySelective(userSeal);
//            }
//        } catch (DemoException e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * 查询app客服信息
     *
     * @return
     */
    @GetMapping("/getServicePhone")
    public String getServicePhone(HttpServletRequest request,String timestamp, String rsa) {

        //道用户异步校验
        if (com.summer.util.log.StringUtils.isBlank(timestamp) || com.summer.util.log.StringUtils.isBlank(rsa)) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.VERIFY_ERRER);
        }
        String param = "wpsk_servicePhone";
        String md5 = DigestUtils.md5Hex(timestamp + param );
        if (!rsa.equals(md5)) {
            log.info("【查询app客服信息】 error param={},timestamp={},rsa={},md5={}", param, timestamp, rsa, md5);
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.VERIFY_ERRER);
        }

        long l = System.currentTimeMillis();
        long diff = l - Long.parseLong(timestamp);
        /*if (diff > 15 * 60000) {
            log.info("【查询app客服信息】 time over param={},timestamp={},diff={}", param, timestamp, diff);
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.VERIFY_ERRER);
        }*/
        log.info("【查询app客服信息】md5 pass  param={},timestamp={},rsa={},ip={}", param, timestamp, rsa, IpAdrressUtil.getIpAdrress(request));

        AppBase appBase = appBaseDAO.selectByPrimaryKey(1);
        Map<String, Object> map = new HashMap<>();
        if (null != appBase) {
            map.put("servicePhone", appBase.getServicePhone());
            map.put("appLogo", appBase.getImgUrl());
            map.put("weixin", appBase.getWeixin());
            map.put("qq", appBase.getQq());
            map.put("appName", appBase.getName());
        } else {
            map.put("servicePhone", "");
            map.put("appLogo", "");
            map.put("weixin", "");
            map.put("qq", "");
            map.put("appName", "");
        }
        return CallBackResult.returnJson(map);
    }


    private String getReason(String reason) {
        if ("Input parameter error".equals(reason)) {
            reason = "姓名输入错误";
        } else if ("Invalid id_number".equals(reason)) {
            reason = "身份证号错误";
        } else if ("Invalid card_number".equals(reason)) {
            reason = "银行卡号错误";
        }
        return reason;
    }

    private String getStatus(String status) {
        if ("RATE_LIMIT_EXCEEDED".equals(status)) {
            status = "调用频率过高";
        } else if ("OUT_OF_QUOTA".equals(status)) {
            status = "调用次数超出限额";
        } else if ("DATA_SERVER_ERROR".equals(status)) {
            status = "数据服务异常";
        } else if ("INTERNAL_ERROR".equals(status)) {
            status = "内部服务异常";
        } else if ("INVALID_ARGUMENT".equals(status)) {
            status = "请求参数错误";
        }
        return status;
    }

    /**
     * 线下还款银行卡,支付宝,微信
     *
     * @return
     */
    @RequestMapping(value = "/offPayUpdate", method = RequestMethod.POST)
    public String offPayUpdate(@RequestBody Map<String, String> request, HttpServletRequest re) {
        PlatformUser platformUser = redisPlatformUser(re);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        if (platformUser.getRoleId() != Constant.ROLEID_SUPER) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "无权操作");
        }

        CallBackResult callBackResult = new CallBackResult(CallBackResult.BUSINESS_ERROR, "修改失败,请求参数不符合要求");
        try {

            String typeStr = request.get("type");
            if (StringUtils.isBlank(typeStr)) {
                return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "参数非法");
            }
            Integer type = Integer.parseInt(typeStr);
            String acct_name = request.get("name");
            String card_no = request.get("card_no");
            String id_no = request.get("id_no");
            String bank = request.get("bank");
            String branch = request.get("branch");
            String zfb_no = request.get("zfb_no");
            String zfb_pic = request.get("zfb_pic");

            if (type == 1) {
               /* if (StringUtils.isNotBlank(card_no) && StringUtils.isNotBlank(id_no) && StringUtils.isNotBlank(acct_name)) {
                    boolean b = false;
                    String resultInfo = HttpUtil.doGet(centerUrl + "moxie/bankCheck?name=" + acct_name + "&cardNumber=" + card_no + "&idNumber=" + id_no);
                    Map<String, Object> dataMap = null;
                    String message = "系统异常";
                    log.info("银行卡校验----返回结果-------resultInfo={}", resultInfo);
                    if (StringUtils.isNotBlank(resultInfo)) {
                        JSONObject resultMap = JSONObject.parseObject(resultInfo);
                        String code = resultMap.getString("code");
                        message = resultMap.getString("message");
                        if ("200".equals(code)) {
                            b = true;
                        }
                    }
                    if (!b) {
                        callBackResult.setMessage("绑卡失败," + message);
                        return JSONObject.toJSONString(callBackResult);
                    }
                }*/
                HashMap<String, Object> params = new HashMap<String, Object>();
                String user_id = "0";
                params.put("userId", user_id);
                params.put("limit", 1);
                List<UserCardInfo> userCardInfoList = userCardInfoDao.findUserCardByUserId(params);
                if (CollectionUtils.isEmpty(userCardInfoList)) {
                    callBackResult = saveCard(user_id, acct_name, "0", card_no, id_no, bank,
                            "", callBackResult, branch, zfb_no, zfb_pic, "", "", "usdt_tag");
                } else {
                    UserCardInfo cardInfo = userCardInfoList.get(0);
                    //银行卡号
                    cardInfo.setCardNo(card_no);
                    //手机号
                    cardInfo.setPhone("0");
                    cardInfo.setBindingStatus(0);
                    cardInfo.setAgreeno(id_no);
                    cardInfo.setOpenName(org.apache.commons.lang3.StringUtils.isBlank(acct_name) ? "" : acct_name);
                    //用户id
                    cardInfo.setUserId(0);
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

                }
                callBackResult.setMessage("修改成功");
                callBackResult.setCode(CallBackResult.SUCCESS);
                return JSONObject.toJSONString(callBackResult);
            } else if (type == 2) {

                UserInfo user = new UserInfo();
                user.setCompanyName(zfb_no);
                user.setRealName(acct_name);
                user.setCompanyPhone(zfb_pic);
                user.setStatus((byte) 2);
                user.setPhone("0");
                UserInfo u = userInfoMapper.findByPhone("0");
                if (u == null) {
                    userInfoMapper.insertSelective(user);
                } else {
                    user.setId(u.getId());
                    userInfoMapper.updateByPrimaryKeySelective(user);
                }
                callBackResult.setMessage("修改成功");
                callBackResult.setCode(CallBackResult.SUCCESS);
                return JSONObject.toJSONString(callBackResult);
            }
        } catch (Exception e) {
            log.info("修改失败e=" + e.getMessage());
            e.printStackTrace();
            callBackResult.setMessage(e.getMessage());
        }
        return JSONObject.toJSONString(callBackResult);
    }

    private CallBackResult saveCard(String user_id, String acct_name, String mobilePhone, String card_no, String id_no
            , String value, String orderTrxid, CallBackResult callBackResult, String branch, String zfb_no, String zfb_pic, String wx_no, String wx_pic, String bindType) {
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
            callBackResult.setMessage("绑卡成功，请返回首页发起借款");
            callBackResult.setCode(CallBackResult.SUCCESS);
            return callBackResult;
        } else {
            callBackResult.setMessage("绑卡失败");
            return callBackResult;
        }
    }

    private boolean tempBindCardProcedure(Map<String, String> reqParam) {
        try {
            String userId = reqParam.get("userId");
            String bankName = reqParam.get("bankName");
            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put("userId", Integer.parseInt(userId));
            params.put("limit", 1);
            List<UserCardInfo> userCardInfoList = userCardInfoDao.findUserCardByUserId(params);

            //所属银行名称
            // 银行id
            int bankId = 0;
            if (userCardInfoList == null || userCardInfoList.isEmpty()) {
                bankId = 0;

//                if (reqParam.get("cardNo").length()<15) {
//                    return false;
//                }
//                //通过银行卡号获得银行卡名称
//                if (bankName == null || "".equals(bankName)) {
//                    return false;
//                }
                //BankAllInfo bankAllInfo = userBankDao.selectByBankName(bankName.substring(0,6));
                // bankId=bankAllInfo.getBankId();
            } else {
                bankId = userCardInfoList.get(0).getBank_id();
            }
            reqParam.put("bankName", bankName);
            reqParam.put("bankId", bankId + "");

            boolean res = (userCardInfoList == null || userCardInfoList.isEmpty())
                    ? saveFirstTimeBindCardProcedure(reqParam)
                    : false;
            return res;

        } catch (Exception e) {
            log.info("bindingCardController2 error" + e);
            return false;
        }
    }

    private UserCardInfo buildUserCardInfo(Map<String, String> param) {
        UserCardInfo cardInfo = new UserCardInfo();
        //银行卡编号
        cardInfo.setBankId(Integer.parseInt(param.get("bankId")));
        //银行卡号
        cardInfo.setCardNo(param.get("cardNo"));
        //手机号
        String mobilePhone = param.get("mobilePhone");
        String acctName = param.get("acctName");
        cardInfo.setPhone(org.apache.commons.lang3.StringUtils.isBlank(mobilePhone) ? "" : mobilePhone);
        String orderTrxId = param.get("OrderTrxId");
        String bindType = param.get("bindType");
        cardInfo.setBindingStatus(0);

        cardInfo.setAgreeno(param.get("idNo"));
        cardInfo.setBindingStatus(1);
        cardInfo.setOpenName(org.apache.commons.lang3.StringUtils.isBlank(acctName) ? "" : acctName);
        //用户id
        cardInfo.setUserId(0);
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

    private boolean saveFirstTimeBindCardProcedure(Map<String, String> param) {
        try {

            UserCardInfo cardInfo = buildUserCardInfo(param);
            int flag = userCardInfoDao.saveUserCardInfo(cardInfo);
            if (flag > 0) {
                log.info("绑卡成功");

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
}
