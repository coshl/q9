package com.summer.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.summer.dao.entity.*;
import com.summer.dao.mapper.*;
import com.summer.pojo.dto.PlateformChannelParamDto;
import com.summer.api.service.IReportRepaymentService;
import com.summer.api.service.ITaskjobService;
import com.summer.api.service.channel.IChannelAsyncCountService;
import com.summer.service.impl.OrderRepaymentService;
import com.summer.service.mq.OrderProducer;
import com.summer.util.*;
import com.summer.util.encrypt.AESDecrypt;
import com.summer.pojo.vo.CountLoanByUserTypeVO;
import com.summer.pojo.vo.UserPhoneInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Desc:
 * Created by tl on 2019/3/25
 */
@Slf4j
@RestController
@RequestMapping("/v1.0/api/schedule")
public class TestScheduleController extends BaseController {
    @Resource
    private ITaskjobService taskjobService;
    @Resource
    private OrderProducer orderProducer;
    @Resource
    private OrderBorrowMapper orderBorrowMapper;
    @Resource
    private IChannelAsyncCountService channelAsyncCountService;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private OrderRepaymentMapper orderRepaymentMapper;
    @Resource
    private IUserCardInfoDao userCardInfoDao;
    @Resource
    private OrderRepaymentService orderRepaymentService;
    @Resource
    private ReportRepaymentDAO reportRepaymentDAO;
    @Resource
    private OrderRenewalMapper orderRenewalMapper;
    @Resource
    private UserInfoMapper userInfoMapper;
    @Resource
    private InfoIndexInfoDao infoIndexInfoDao;
    @Resource
    private LoanReportDAO loanReportDAO;
    @Resource
    private IReportRepaymentService reportRepaymentService;
    @Value("${app.pid}")
    private String pid;
    @Resource
    private PlatformUserMapper platformUserMapper;
    @Resource
    private PlateformChannelMapper plateformChannelMapper;

    /**
     * 首页
     *
     * @return
     */
    @GetMapping("/index")
    public String statistic(HttpServletRequest request) {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        if (Constant.ROLEID_SUPER != platformUser.getRoleId()) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, "没有该权限");
        }
        taskjobService.statisticIndex(new Date(), true);
        return CallBackResult.returnJson("成功");
    }

    @GetMapping("/test")
    public String test(HttpServletRequest request) {
        //获取当前用户信息
        PlatformUser platformUser = redisPlatformUser(request);
        // 登录失效
        if (null == platformUser || platformUser.getRoleId() != 1) {
            return "无此权限";
        }
        CallBackResult callBackResult = new CallBackResult(CallBackResult.SUCCESS, "测试");
        log.info("os=" + SmsKLimitUtil.getOsAndBrowserInfo(request));
        return JSONObject.toJSONString(callBackResult);
    }

    @GetMapping("/ag")
    public String ag(Integer bId, HttpServletRequest request) {
        //获取当前用户信息
        PlatformUser platformUser = redisPlatformUser(request);
        // 登录失效
        if (null == platformUser || platformUser.getRoleId() != 1) {
            return "无此权限";
        }
        String rootFolder = new File("").getAbsolutePath();
        log.info("rootFolder=" + rootFolder);
        OrderBorrow bo = orderBorrowMapper.selectByPrimaryKey(bId);
        if (null == bo) {
            return CallBackResult.returnErrorJson();
        }
        //userAgreeService.getSign(bo, 0);
        //userAgreeService.getSign(bo, 1);
        CallBackResult callBackResult = new CallBackResult(CallBackResult.SUCCESS, "成功");
        return JSONObject.toJSONString(callBackResult);
    }


    private int getBorrowStatus(Byte boStatus) {
        int res = 0;
        int stutus = boStatus;
        switch (stutus) {
            case -3:
                res = 22;
                break;
            case -2:
                res = 4;
                break;
            case -1:
                res = 1;
                break;
            case 1:
            case 4:
            case 5:
                res = 2;
                break;
            case 6:
                res = 6;
                break;
            case 8:
                res = 7;
                break;
            case 10:
                res = 8;
                break;
            case 13:
                res = 10;
                break;
            case 7:
                res = 15;
                break;
            case 0:
                res = 0;
                break;
            default:
                break;
        }
        return res;
    }

    private String getMoneyFormat(Integer applyAmount) {
        return new BigDecimal(applyAmount / 100.0).setScale(2, BigDecimal.ROUND_HALF_DOWN).toString();
    }

    /**
     * 派单
     *
     * @return
     */
    @GetMapping("/autoDispatch")
    public String autoDispatch(HttpServletRequest request) {
        //获取当前用户信息
       /* PlatformUser platformUser = redisPlatformUser(request);
        // 登录失效
        if (null == platformUser || platformUser.getRoleId() != 1) {
            return "无此权限";
        }*/
        taskjobService.handleDispatchTask();
        return CallBackResult.returnJson("成功");
    }

    /**
     * 催收统计
     *
     * @return
     */
    @GetMapping("/collectCount")
    public String collectCount(HttpServletRequest request) {
        //获取当前用户信息
        /*PlatformUser platformUser = redisPlatformUser(request);
        // 登录失效
        if (null == platformUser || platformUser.getRoleId() != 1) {
            return "无此权限";
        }*/
        taskjobService.statisticCollect(DateUtil.addDay(new Date(), -1));
        return CallBackResult.returnJson("成功");
    }

    /**
     * 逾期更新
     *
     * @return
     */
    @GetMapping("/overdue")
    public String overdue(HttpServletRequest request) {
        //获取当前用户信息
        /*PlatformUser platformUser = redisPlatformUser(request);
        // 登录失效
        if (null == platformUser || platformUser.getRoleId() != 1) {
            return "无此权限";
        }*/
        taskjobService.overdue();
        return CallBackResult.returnJson("成功");
    }

    /**
     * 复审队列
     *
     * @return
     */
    @GetMapping("/sendRisk")
    public String sendRisk(HttpServletRequest request) {
       /* //获取当前用户信息
        PlatformUser platformUser = redisPlatformUser(request);
        // 登录失效
        if (null == platformUser || platformUser.getRoleId() != 1) {
            return "无此权限";
        }*/
        List<OrderBorrow> orderBorrows = orderBorrowMapper.selectRisk();
        if (CollectionUtils.isNotEmpty(orderBorrows)) {
            for (OrderBorrow orderBorrow : orderBorrows) {
                Integer borrowId = orderBorrow.getId();
                orderProducer.sendRisk(borrowId + "");
            }
        }
        log.info("----------riskQueue sendRisk end：影响条数=" + orderBorrows.size());
        return CallBackResult.returnJson("成功");
    }

    @RequestMapping("/channelData")
    public Object channelData(Integer day, HttpServletRequest request) {
        //获取当前用户信息
        PlatformUser platformUser = redisPlatformUser(request);
        // 登录失效
        if (null == platformUser || platformUser.getRoleId() != 1) {
            return "无此权限";
        }
        channelAsyncCountService.channelData(day);
        return CallBackResult.returnJson();
    }

    /**
     * 清除验证码过期时间
     *
     * @return
     */
    @RequestMapping("/cpc")
    public Object cleanPhoneCaptcha(String phone, Integer type, HttpServletRequest request) {
        //获取当前用户信息
        /*PlatformUser platformUser = redisPlatformUser(request);
        // 登录失效
        if (null == platformUser || platformUser.getRoleId() != 1) {
            return "无此权限";
        }*/
        return SmsKLimitUtil.captchExpire(redisUtil, phone, type);
    }

    @RequestMapping("/pull")
    public Object rePushOrder(String url, String ids, HttpServletRequest request) {
        //获取当前用户信息
        PlatformUser platformUser = redisPlatformUser(request);
        // 登录失效
        if (null == platformUser || platformUser.getRoleId() != 1) {
            return "无此权限";
        }
        int sum = 0;
        if (StringUtils.isNoneBlank(url) && StringUtils.isNoneBlank(ids)) {
            String[] split = ids.split(",");
            for (int i = 0; i < split.length; i++) {
                String reqUrl = url + "/v1.0/api/schedule/apiNotify?bId=" + split[i];
                try {
                    String result = HttpUtil.doGet(reqUrl);
                    log.info("推送api订单状态----i={},result={},url={}", i, result, reqUrl);
                    sum = i;
                    Thread.sleep(300);
                } catch (Exception e) {
                    continue;
                }

            }
        }
        return CallBackResult.returnJson(CallBackResult.SUCCESS, "成功", sum);
    }


    /**
     * 渠道命中黑名单统计
     *
     * @return
     */
    @RequestMapping("/black")
    public Object black(Integer day, Integer channelId, HttpServletRequest request) {
        //获取当前用户信息
        PlatformUser platformUser = redisPlatformUser(request);
        // 登录失效
        if (null == platformUser || platformUser.getRoleId() != 1) {
            return "无此权限";
        }
        Date nowDate = new Date();
        Date date = DateUtil.dateSubtraction(nowDate, day);
        UserInfo userInfo = new UserInfo();
        userInfo.setCreateTime(date);
        userInfo.setChannelId(channelId);
        //统计命中系统黑名单
        channelAsyncCountService.hitSystemBlackRe(userInfo, nowDate);
        //统计命中第三方黑名单
        channelAsyncCountService.hitOutBlackRe(userInfo, nowDate);

        return CallBackResult.returnJson();
    }

    /**
     * 续期金额重新统计
     *
     * @return
     */
    @RequestMapping("/countRenewal")
    public Object renewal(HttpServletRequest request) {
        //获取当前用户信息
        PlatformUser platformUser = redisPlatformUser(request);
        // 登录失效
        if (null == platformUser || platformUser.getRoleId() != 1) {
            return "无此权限";
        }
        List<ReportRepayment> byReportTime = reportRepaymentDAO.findByReportTime();
        for (ReportRepayment reportRepayment : byReportTime) {
            try {
                String firstRepaymentTime = reportRepayment.getReportDate();
                //续期数目（成功数量）
                Integer renewalNumber = orderRenewalMapper.countRenewalNum(firstRepaymentTime);
                //Integer renewalNumber = 0;
                //续期金额 (当日续期金额=当日续期成功订单利息之和+当日续期手续费之和)
                Long renewalServiceFee = orderRenewalMapper.countRenewalServiceFee(firstRepaymentTime);
                Long renewalAmount = orderRenewalMapper.countRenewalFree(firstRepaymentTime) + renewalServiceFee;
                reportRepayment.setRenewalNumber(renewalNumber);
                reportRepayment.setRenewalAmount(renewalAmount);
                reportRepaymentDAO.updateByPrimaryKeySelective(reportRepayment);
                Thread.sleep(200);
            } catch (Exception e) {
                log.error("统计续期异常----id={},firstRepaymentTime={},error={}", reportRepayment.getId(), reportRepayment.getReportDate(), e);
                continue;
            }

        }
        return CallBackResult.returnJson("成功");
    }

    /**
     * 修改认证状态
     *
     * @param phone
     * @param type
     * @return
     */
//    @RequestMapping("/updateState")
    public Object updateState(String phone, Integer type, Integer status, HttpServletRequest request) {
        //获取当前用户信息
        PlatformUser platformUser = redisPlatformUser(request);
        // 登录失效
        /*if (null == platformUser || platformUser.getRoleId() != 1) {
            return "无此权限";
        }*/
        if (StringUtils.isBlank(phone) || null == type || null == status) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "手机号或者类型不能为空");
        }
        UserInfo userInfo = userInfoMapper.findByPhone(phone);
        if (null == userInfo) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "账号或密码错误");
        }
        InfoIndexInfo infoIndexInfo = new InfoIndexInfo();
        infoIndexInfo.setUserId(userInfo.getId());
        String msg = "";
        switch (type) {
            case 1:
                //身份认证信息
                infoIndexInfo.setAuthInfo(status);
                msg = "身份认证";
                break;
            case 2:
                //紧急联系人
                infoIndexInfo.setAuthContacts(status);
                msg = "个人信息";
                break;
            case 3:
                //运营商认证
                infoIndexInfo.setAuthMobile(status);
                msg = "运营商";
                break;
            case 4:
                //银行卡认证
                infoIndexInfo.setAuthBank(status);
                UserCardInfo userCardInfo = userCardInfoDao.selectUserBankCard(userInfo.getId());
                if (null != userCardInfo) {
                    userCardInfoDao.deleteUserCardInfo(userInfo.getId());
                }
                msg = "银行卡";
                break;

            case 5:
                infoIndexInfo.setBorrowStatus(status.toString());
                msg = "订单状态";
                break;
            case 0:
                //身份认证信息
                infoIndexInfo.setAuthInfo(status);
                //紧急联系人
                infoIndexInfo.setAuthContacts(status);
                //运营商认证
                infoIndexInfo.setAuthMobile(status);
                //银行卡认证
                infoIndexInfo.setAuthBank(status);
                msg = "四要素";
                break;
            default:
                return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "类型错误，请重新输入！");
        }
        infoIndexInfoDao.updateByPrimaryKeySelective(infoIndexInfo);
        if (5 == type) {
            Date date = new Date();
            OrderBorrow orderBorrow = new OrderBorrow();
            orderBorrow.setUserId(userInfo.getId());
            orderBorrow.setCreateTime(date);
            List<OrderBorrow> byUserOrderParam = orderBorrowMapper.findByUserOrderParam(orderBorrow);
            if (null != byUserOrderParam && byUserOrderParam.size() > 0) {
                OrderBorrow orderBorrowNew = new OrderBorrow();
                orderBorrowNew.setId(byUserOrderParam.get(0).getId());
                orderBorrowNew.setUserId(userInfo.getId());
                int ran2 = (int) (Math.random() * (100 - 1) + 1);
                orderBorrowNew.setCreateDate(DateUtil.dateSubtraction(date, -ran2));
                orderBorrowMapper.updateByPrimaryKeySelective(orderBorrowNew);
            }
        }
        return CallBackResult.returnJson(CallBackResult.SUCCESS, msg + "状态修改成功");
    }

    /**
     * 将今日应还款订单自动派单给当日催收
     *
     * @return
     */
    @GetMapping("/autoDispatchWaiter")
    public String autoDispatchWaiter(HttpServletRequest request) {
       /* //获取当前用户信息
        PlatformUser platformUser = redisPlatformUser(request);
        // 登录失效
        if (null == platformUser || platformUser.getRoleId() != 1) {
            return "失败";
        }*/
        taskjobService.autoDispatchWaiter();
        return "成功";
    }

    /**
     * 前一天当日放款更新
     *
     * @return
     */
    @GetMapping("/statisticLoan")
    public String statisticLoan(HttpServletRequest request) {
        //获取当前用户信息
        PlatformUser platformUser = redisPlatformUser(request);
        // 登录失效
        if (null == platformUser || platformUser.getRoleId() != 1) {
            return "失败";
        }
        Long applyNum = orderBorrowMapper.statisticByDay(1);
        Long registerNum = userInfoMapper.statisticByDay(1);
        Map<String, Object> rep = orderRepaymentMapper.countByDay(1);
        List<CountLoanByUserTypeVO> vos = orderRepaymentMapper.countUserByDay(1);
        Date now = new Date();
        LoanReport loanReport = new LoanReport(Integer.parseInt(rep.get("num").toString()), Long.parseLong(rep.get(
                "loanAmount").toString()), DateUtil.addDay(now, -1), now, registerNum.intValue(), applyNum.intValue(), vos);
        loanReportDAO.insertSelective(loanReport);
        return "成功";
    }

    /**
     * 将所有待还款、部分还款、逾期订单信息推送到数据中心
     *
     * @param request
     * @return
     */
    @GetMapping("/pushAllInformation")
    public String pushAllInformation(HttpServletRequest request) {
        //获取当前用户信息
        PlatformUser platformUser = redisPlatformUser(request);
        // 登录失效
        if (null == platformUser || platformUser.getRoleId() != 1) {
            return "无此权限";
        }
        Map<String, Object> params = new HashMap<>();
        params.put("status", 43210);
        List<OrderRepayment> repayments = orderRepaymentService.findOrderPrepaymentByParam(params);
        for (OrderRepayment repayment : repayments) {
            OrderBorrow orderBorrow = orderBorrowMapper.selectByPrimaryKey(repayment.getBorrowId());
            if (orderBorrow == null) {
                continue;
            }
            UserInfo userInfo = userInfoMapper.selectByPrimaryKey(orderBorrow.getUserId());
            if (userInfo == null) {
                continue;
            }
            params.clear();
            params.put("phone", userInfo.getPhone());
            params.put("idCard", userInfo.getIdCard());
            params.put("realName", userInfo.getRealName());
            params.put("userId", userInfo.getId());
            params.put("pid", pid);
            params.put("borrowId", orderBorrow.getId());
            // 总还款状态0:待还款;1:部分还款;2:已还款;3:已逾期;4:逾期已还款，5:已坏账,6提前还款；
            Byte status = repayment.getStatus();
            // 数据中心订单状态：0：放款成功;1：续期成功;2：还款成功;3：逾期;4：逾期还款;5：部分还款
            if (status == 0) {
                params.put("status", 0);
            } else if (status == 1) {
                params.put("status", 5);
            } else if (status == 2) {
                params.put("status", 2);
            } else if (status == 3) {
                params.put("status", 3);
            } else if (status == 4) {
                params.put("status", 4);
            }
            if (orderBorrow.getLoanTime() != null) {
                params.put("loanTime", DateUtil.getDateFormat(orderBorrow.getLoanTime(), "yyyy-MM-dd HH:mm:ss"));
            } else {
                log.info(repayment.getId() + "_" + repayment.getBorrowId());
                params.put("loanTime", "2220-01-01 00:00:00");
            }
            if (repayment.getRepaymentTime() != null) {
                params.put("loanEndTime", DateUtil.getDateFormat(repayment.getRepaymentTime(), "yyyy-MM-dd HH:mm:ss"));
            } else {
                log.info(repayment.getId() + "_" + repayment.getBorrowId());
                params.put("loanEndTime", "2220-01-01 00:00:00");
            }
            params.put("loanTerm", orderBorrow.getLoanTerm());
            params.put("applyAmount", orderBorrow.getApplyAmount());
            // 将放款成功的用户订单信息推到数据中心
            //String res = HttpUtil.doPost(dataUrl + "borrowInfo/addBorrowInfo", params);
            log.info("将所有待还款、部分还款、逾期订单信息推送到数据中心返回参数------res={}", "ok");
        }
        return "成功";
    }

    /**
     * 将所有续期成功订单信息同步到数据中心
     *
     * @param request
     * @return
     */
    @GetMapping("/pushAllRenewalInformation")
    public String pushAllRenewalInformation(HttpServletRequest request, String createTime) {
        //获取当前用户信息
        PlatformUser platformUser = redisPlatformUser(request);
        // 登录失效
        if (null == platformUser || platformUser.getRoleId() != 1) {
            return "无此权限";
        }
        Map<String, Object> params = new HashMap<>();
        params.put("status", 2);
        params.put("createTime", createTime);
        List<OrderRenewal> orderRenewals = orderRenewalMapper.selectSimple(params);
        for (OrderRenewal orderRenewal : orderRenewals) {
            OrderRepayment repayment = orderRepaymentService.selectByPrimaryKey(orderRenewal.getRepaymentId());
            if (repayment == null || repayment.getStatus() != 0) {
                continue;
            }
            params.clear();
            params.put("pid", pid);
            params.put("borrowId", orderRenewal.getBorrowId());
            // 状态：0：放款成功;1：续期成功;2：还款成功;3：逾期;4：逾期还款;5：部分还款
            params.put("status", 1);
            params.put("remark", "续期成功");
            params.put("loanEndTime", DateUtil.getDateFormat(orderRenewal.getRepaymentTime(), "yyyy-MM-dd HH:mm:ss"));
            // 将续期成功的用户订单信息推到数据中心
            //String res = HttpUtil.doPost(dataUrl + "borrowInfo/updateBorrowInfo", params);
            log.info("续期成功时将订单信息同步到数据中心返回参数------res={}", "res");
        }
        return "成功";
    }

    /**
     * 更新逾期催收统计,当日催收统计（统计之前把原有的统计删除CollectionStatistics）
     *
     * @return
     */
    @GetMapping("/updateCollectionStatistics")
    public Object updateCollectionStatistics(HttpServletRequest request, String beginTime, String endTime) {
        try {
            //获取当前用户信息
            PlatformUser platformUser = redisPlatformUser(request);
            // 登录失效
            if (null == platformUser || platformUser.getRoleId() != 1) {
                return "无此权限";
            }
            log.info("更新逾期催收统计,当日催收统计");
            Date now = DateUtil.parseTimeYmdhms(beginTime);
            // 删除当天
            Integer num = taskjobService.deleteByTime(now);
            log.info("删除当天" + num + "条数据");
            // 逾期催收统计
            taskjobService.updateCollectionStatisticsOverdue(now, beginTime, endTime);
            // 当日催收统计
            taskjobService.updateCollectionStatistics(now, beginTime, endTime);
        } catch (Exception e) {
            log.error("统计异常", e);
        }
        return CallBackResult.returnJson("成功");
    }

    /**
     * 当日还款统计
     *
     * @return
     */
    @GetMapping("/repayCount")
    public Object repayCount(HttpServletRequest request, String date) {
        try {
            //获取当前用户信息
            PlatformUser platformUser = redisPlatformUser(request);
            // 登录失效
            if (null == platformUser || platformUser.getRoleId() != 1) {
                return "无此权限";
            }
            log.info("repayCount start");
            //生成或更新前一天还款统计
            reportRepaymentService.reportRepaymentToday(date, 1);
            //生成或更新本日二次及以上的续期
            reportRepaymentService.renewalToday(date);
        } catch (Exception e) {
            log.error("统计异常", e);
        }
        return CallBackResult.returnJson("成功");
    }

    /**
     * 加密后台手机号 platform_user
     * @param phone
     * @return
     */
    @RequestMapping("/updateUserPhone")
    public Object encryptUserInfo(String phone){
        List<UserPhoneInfo> userPhoneInfos = platformUserMapper.findAllUser(phone);
        Integer status=null;
        if (CollectionUtils.isNotEmpty(userPhoneInfos)){
            for (UserPhoneInfo userPhoneInfo :userPhoneInfos         ) {
                if (userPhoneInfo.getPhoneNumber().contains("==")){
                    //如果包含==就说明更新过来，不再需要更新,防止重复更新
                    continue;
                }
                PlatformUser platformUser = new PlatformUser();
                platformUser.setId(userPhoneInfo.getId());
                platformUser.setPhoneNumber(userPhoneInfo.getPhoneNumber());
                status = platformUserMapper.updateByPrimaryKeySelective(platformUser);
                log.error(" 更新后台---数据 id ={},status={}",platformUser.getId(), status);
            }

        }
        return CallBackResult.returnJson(CallBackResult.SUCCESS,"成功",status);
    }


    /**
     * 加密渠道后台用户 plateform_channel
     * @param phone
     * @return
     */
    @RequestMapping("/updateChannelUser")
    public Object encryptChannelUserInfo(String phone){
       // List<UserPhoneInfo> userPhoneInfos = platformUserMapper.findAllUser(phone);
        List<PlateformChannel> params = plateformChannelMapper.findParams(new HashMap<>());
        Integer status=null;
        if (CollectionUtils.isNotEmpty(params)){
            for (PlateformChannel plateformChannel :params ) {
                try {
                    PlateformChannelParamDto platformUser = new PlateformChannelParamDto();
                    platformUser.setId(plateformChannel.getId());
                    if (!plateformChannel.getAccount().contains("=")){
                        //包含==就不用再更新了
                        platformUser.setAccount(plateformChannel.getAccount());
                        platformUser.setManagerPhone(plateformChannel.getAccount());

                    }
                    if (null != plateformChannel.getPassword() && !plateformChannel.getPassword().contains("=")){
                        platformUser.setPassword(AESDecrypt.encrypt(plateformChannel.getPassword()));
                    }

                    status = plateformChannelMapper.updateByPrimaryKeySelective(platformUser);
                    log.error(" 更新渠道数据 id ={},status={}",platformUser.getId(), status);
                }catch (Exception e){
                    log.error("更新渠道数据异常  ------id={},e={}",plateformChannel.getId(),e);
                    continue;
                }

            }
        }
        return CallBackResult.returnJson(CallBackResult.SUCCESS,"成功",status);
    }


    /**
     * 加密用户手机号 user_info
     * @param phone
     * @return
     */
    @RequestMapping("/updateAppUser")
    public Object encryptUser(String phone){
        Map<String, Object> map = new HashMap<>();
        map.put("idInfo","=");
        List<UserUpdate> updateUser = userInfoMapper.findUpdateUser(map);
        Integer status=null;
        if (CollectionUtils.isNotEmpty(updateUser)){
            for (UserUpdate userInfo :updateUser         ) {
             try {
                 UserInfo platformUser = new UserInfo();
                 platformUser.setId(userInfo.getId());
                 //手机号
                 String phone1 = userInfo.getPhone();
                 if (StringUtils.isNotBlank(phone1) && !phone1.contains("=")){
                     platformUser.setPhone(phone1);
                 }
                 //姓名
                 String realName = userInfo.getRealName();
                 if (StringUtils.isNotBlank(realName) && !realName.contains("=")){
                     platformUser.setRealName(realName);
                 }
                 //身份证号
                /* String idCard = userInfo.getIdCard();
                 if (StringUtils.isNotBlank(idCard) && !idCard.contains("=")){
                     platformUser.setIdCard(idCard);
                 }*/
                 //第一联系人姓名
                /* String firstContactName = userInfo.getFirstContactName();
                 if (StringUtils.isNotBlank(firstContactName) && !firstContactName.contains("=")){
                     platformUser.setFirstContactName(firstContactName);
                 }*/
                 //第一联系人手机号
                 /*String firstContactPhone = userInfo.getFirstContactPhone();
                 if (StringUtils.isNotBlank(firstContactPhone) && !firstContactPhone.contains("=")){
                     platformUser.setFirstContactPhone(firstContactPhone);
                 }*/
                 //第二联系人姓名
                /* String secondContactName = userInfo.getSecondContactName();
                 if (StringUtils.isNotBlank(secondContactName) && !secondContactName.contains("=")){
                     platformUser.setSecondContactName(secondContactName);
                 }*/

                 //第er联系人手机号
                /* String  secondContactPhone = userInfo.getSecondContactPhone();
                 if (StringUtils.isNotBlank(secondContactPhone) && !secondContactPhone.contains("=")){
                     platformUser.setSecondContactPhone(secondContactPhone);
                 }*/
                /* String headPortrait = userInfo.getHeadPortrait();
                 if (StringUtils.isNotBlank(headPortrait) && !headPortrait.contains("=")){
                     platformUser.setHeadPortrait(headPortrait);
                 }

                 String idcardImgZ = userInfo.getIdcardImgZ();
                 if (StringUtils.isNotBlank(idcardImgZ) && !idcardImgZ.contains("=")){
                     platformUser.setIdcardImgZ(idcardImgZ);
                 }

                 String idcardImgF = userInfo.getIdcardImgF();
                 if (StringUtils.isNotBlank(idcardImgZ) && !idcardImgF.contains("=")){
                     platformUser.setIdcardImgF(idcardImgF);
                 }*/

               /*  String idCardAddress = userInfo.getIdCardAddress();
                 if (StringUtils.isNotBlank(idCardAddress) && !idCardAddress.contains("=")){
                     platformUser.setIdCardAddress(idCardAddress);
                 }*/
                 log.error(" 更新APP用户数据 id ={},JSON={}",userInfo.getId(), JSONObject.toJSONString(platformUser));
                 if (StringUtils.isNotBlank(platformUser.getPhone()) || StringUtils.isNotBlank(platformUser.getRealName())){
                     status = userInfoMapper.updateByPrimaryKeySelective(platformUser);
                 }
                 log.error(" 更新APP用户数据 id ={},status={}",userInfo.getId(), status);
             }catch (Exception e) {
                 log.error(" 更新APP用户数据 异常id ={},status={},e={}", userInfo.getId(), status, e);
                 continue;
             }

            }

        }
        return CallBackResult.returnJson(CallBackResult.SUCCESS,"成功",status);
    }




    /**加密银行卡 user_card_info
     *
     * @return
     */
   // @RequestMapping("/encryptUserBank")
    public Object encryptUserBank(){
        Integer status=null;
        List<UserCardInfoUpdate > allBank = userCardInfoDao.findAllBank();
        if (CollectionUtils.isNotEmpty(allBank)){
            for (UserCardInfoUpdate  cardInfo : allBank ) {
                try {
                    UserCardInfo userCardInfo = new UserCardInfo();
                    userCardInfo.setUserId(cardInfo.getUserId());
                    String openName = cardInfo.getOpenName();
                    if (StringUtils.isNotBlank(openName) && !openName.contains("=")){
                        userCardInfo.setOpenName(openName);
                    }
                    String card_no = cardInfo.getCard_no();
                    if (StringUtils.isNotBlank(card_no) && !card_no.contains("=")){
                        userCardInfo.setCardNo(card_no);
                    }
                    String phone = cardInfo.getPhone();
                    if (StringUtils.isNotBlank(phone) && !phone.contains("=")){
                        userCardInfo.setPhone(phone);
                    }
                    log.error(" 更新Ka 用户数据 id ={},JSON={}",userCardInfo.getUserId(), JSONObject.toJSONString(userCardInfo));
                    if (StringUtils.isNotBlank(userCardInfo.getOpenName()) || StringUtils.isNotBlank(userCardInfo.getCardNo())||
                            StringUtils.isNotBlank(userCardInfo.getPhone())){
                        status = userCardInfoDao.updateUserCardInfo(userCardInfo);
                    }


                    log.error(" 更新ka 用户数据 id ={},status={}",userCardInfo.getUserId(), status);
                }catch (Exception e){
                    log.error(" 更新ka 用户数据 异常id ={},status={},e={}", cardInfo.getUserId(),status , e);
                    continue;
                }
            }

        }
        return CallBackResult.returnJson(CallBackResult.SUCCESS,"成功",status);

    }
    @Resource
    private UserContactsDAO userContactsDAO;

    /**
     *加密通讯录 user_contacts
     * @return
     */
   // @RequestMapping("/encryptUserContact")
    public Object encryptUserContact(){
        List<UserContactsUpdate> allContacts = userContactsDAO.findAllContacts();
        Integer status=null;
        if (CollectionUtils.isNotEmpty(allContacts)){
            for ( UserContactsUpdate userContactsUpdate: allContacts){
                try {
                    UserContacts userContacts = new UserContacts();
                    userContacts.setId(userContactsUpdate.getId());

                    String userName = userContactsUpdate.getUserName();
                    if (StringUtils.isNotBlank(userName) && !userName.contains("=")){
                        userContacts.setUserName(userName);
                    }

                    String contactName = userContactsUpdate.getContactName();
                    if (StringUtils.isNotBlank(contactName) && !contactName.contains("=")){
                        userContacts.setContactName(contactName);
                    }


                    String contactPhone = userContactsUpdate.getContactPhone();
                    if (StringUtils.isNotBlank(contactPhone) && !contactPhone.contains("=")){
                        userContacts.setContactPhone(contactPhone);
                    }
                    log.error(" 更新 联系人qian   数据 id ={},status={}",userContacts.getId(), status);
                    if (StringUtils.isNotBlank(userContacts.getUserName()) || StringUtils.isNotBlank(userContacts.getContactName())||
                            StringUtils.isNotBlank(userContacts.getContactPhone())){
                        status = userContactsDAO.updateByPrimaryKeySelective(userContacts);
                    }

                    log.error(" 更新 联系人 数据 id ={},status={}",userContacts.getId(), status);
                }catch (Exception e){
                    log.error(" 更新 联系人数据 异常id ={},status={},e={}", userContactsUpdate.getId(),status , e);
                    continue;
                }

            }
        }
        return CallBackResult.returnJson(CallBackResult.SUCCESS,"成功",status);
    }
}
