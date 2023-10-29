package com.summer.web.controller;

import cn.hutool.core.util.IdcardUtil;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.summer.annotation.Log;
import com.summer.dao.entity.*;
import com.summer.dao.mapper.*;
import com.summer.pojo.dto.OrderPushRiskDTO;
import com.summer.pojo.vo.*;
import com.summer.api.service.IOrderBorrowService;
import com.summer.api.service.IUserShortMessageService;
import com.summer.service.mq.OrderProducer;
import com.summer.util.*;
import com.summer.web.util.ExcelExportUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Desc:
 * Created by tl on 2018/12/20
 */
@Controller
@RequestMapping("/v1.0/api/borrowOrder")
public class OrderBorrowController extends BaseController {
    private static final String MXRPT_SHOW_URL = "https://tenant.51datakey.com/carrier/report_data?data=";
    private static Logger log = LoggerFactory.getLogger(OrderBorrowController.class);
    @Resource
    private IOrderBorrowService orderBorrowService;
    @Resource
    private PlateformChannelMapper plateformChannelMapper;
    @Resource
    private UserContactsDAO userContactsDAO;
    @Resource
    private UserInfoMapper userInfoMapper;
    @Resource
    private IBackConfigParamsDao backConfigParamsDao;
    @Resource
    private PlatformUserMapper platformUserMapper;
    @Resource
    private OrderBorrowMapper orderBorrowMapper;
    @Resource
    private OrderProducer orderProducer;

    @Resource
    private IUserCardInfoDao userCardInfoDao;
    @Resource
    private IRiskCreditUserDao riskCreditUserDao;
    @Resource
    private OrderCollectionDAO orderCollectionDAO;
    @Resource
    private OrderCollectionReductionDAO orderCollectionReductionDAO;
    @Resource
    private UserMoXieDataDAO userMoXieDataDAO;
    @Resource
    private OrderCollectionDetailDAO orderCollectionDetailDAO;
    @Resource
    private OrderRepaymentDetailDAO orderRepaymentDetailDAO;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private IUserShortMessageService userShortMessageService;
    @Resource
    private WhiteUserDAO whiteUserDAO;
    @Resource
    private OrderRepaymentMapper orderRepaymentMapper;
    @Resource
    private InfoIndexInfoDao infoIndexInfoDAO;
    private static String WITHDRAW_KEYS = "LOAN_WITHDRAW";
    private static String SEND_LOAN = "BORROW";


    /**
     * 审核订单列表
     *
     * @param jsonData
     * @return
     */
    @ResponseBody
    @PostMapping("/queryWithUser")
    public String queryWithUser(@RequestBody String jsonData, HttpServletRequest request) {
        long l = System.currentTimeMillis();
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, "参数非法");
        }
        // 超管和审核人员才能查询
        if (platformUser.getRoleId() != Constant.ROLEID_SUPER && platformUser.getRoleId() != Constant.ROLEID_REVIEWER) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "没有权限");
        }
        Map<String, Object> params = JSONObject.parseObject(jsonData);
        //审核人员只能看到分配给自己的订单
        PageHelper.startPage(params);
        long l1 = System.currentTimeMillis();
        List<BorrowUserVO> list = orderBorrowMapper.orderList(params);
        long l2 = System.currentTimeMillis();
        if (CollectionUtils.isNotEmpty(list)) {
            for (BorrowUserVO borrowUserVO : list) {

                if (Constant.ROLEID_REVIEWER == platformUser.getRoleId()) {
                    params.put("reviewUserId", platformUser.getId());
                }
//                log.info("OrderBorrowController queryWithUser params=" + params.toString());
                params.put("review", "true");
                if (borrowUserVO.getIsBlack() == 0) {
                    List<WhiteUser> whiteUsers = whiteUserDAO.selectByPhone(borrowUserVO.getPhone());
                    if (CollectionUtils.isNotEmpty(whiteUsers)) {
                        borrowUserVO.setIsWhite(1);
                    } else {
                        borrowUserVO.setIsWhite(0);
                    }
                } else {
                    borrowUserVO.setIsWhite(2);
                }
                //borrowUserVO.setPhone(PhoneUtil.maskPhoneNum(borrowUserVO.getPhone()));
                borrowUserVO.setIdCard(IdcardUtil.hide(borrowUserVO.getIdCard(), 5, 15));
            }
        }
        long l3 = System.currentTimeMillis();
        List<PlateformChannel> plateformChannels = plateformChannelMapper.selectSimple(new HashMap<>());
        long l4 = System.currentTimeMillis();
        Map<String, Object> res = new HashMap<>();
        res.put("page", new PageInfo<>(list));
        res.put("channel", plateformChannels);
        long e = System.currentTimeMillis();
        log.info("审核列表params={} 总耗时ms={},db={},hd={},ch={}", params.toString(), (e - l), (l2 - l1), (l3 - l2), (l4 - l3));
        return CallBackResult.returnJson(res);
    }

    /**
     * 查询订单列表
     *
     * @param jsonData
     * @return
     */
    @ResponseBody
    @PostMapping("/orderList")
    public String orderList(@RequestBody String jsonData, HttpServletRequest request) {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, "参数非法");
        }
        // 超管和审核人员才能查询
        if (platformUser.getRoleId() != Constant.ROLEID_SUPER && platformUser.getRoleId() != Constant.ROLEID_REVIEWER) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "没有权限");
        }
        Map<String, Object> params = JSONObject.parseObject(jsonData);
        //审核人员只能看到分配给自己的订单
        if (Constant.ROLEID_REVIEWER == platformUser.getRoleId()) {
            params.put("reviewUserId", platformUser.getId());
        }
//        params.put("review", "true");
        PageHelper.startPage(params);
        List<BorrowUserVO> list = orderBorrowMapper.orderList(params);
     /*   for (BorrowUserVO vo : list) {
            vo.setPhone(PhoneUtil.maskPhoneNum(vo.getPhone()));
            vo.setIdCard(IdcardUtil.hide(vo.getIdCard(), 4, 15));
        }*/
        List<PlateformChannel> plateformChannels = plateformChannelMapper.selectSimple(new HashMap<>());
        Map<String, Object> res = new HashMap<>();
        res.put("page", new PageInfo<>(list));
        res.put("channel", plateformChannels);
        return CallBackResult.returnJson(res);
    }


    @Log(title = "下载订单列表")
    @PostMapping("/downDistribution")
    public void downDistribution(@RequestBody String jsonData, HttpServletRequest request, HttpServletResponse response) {
        try {

            PlatformUser platformUser = redisPlatformUser(request);
            if (null == platformUser) {
                returnLoginBad(response);
                return;
            }

            Map<String, Object> params = JSONObject.parseObject(jsonData);
            //获取当前用户信息
            //审核人员只能看到分配给自己的订单
            if (Constant.ROLEID_REVIEWER == platformUser.getRoleId()) {
                params.put("reviewUserId", platformUser.getId());
            }

            List<BorrowUserVO> list = orderBorrowMapper.orderList(params);
            String title = "订单列表";
            BuildXLSX.setFileDownloadHeader(request, response, title);
            LinkedHashMap<String, String> titleMap = new LinkedHashMap<>();
            titleMap.put("realName", "客户姓名");
            titleMap.put("phone", "手机号码");
            titleMap.put("channelName", "渠道名称");
            titleMap.put("clientType", "手机系统1：安卓 2：ios");
            titleMap.put("orderStatusName", "审核状态");
            titleMap.put("createTimeChg", "申请时间");
            titleMap.put("loanTimeChg", "放款时间");
            titleMap.put("amountAvailable", "可借金额/分");
            titleMap.put("applyAmount", "申请金额/分");
            titleMap.put("serviceCharge", "服务费/分");
            titleMap.put("interest", "利息/分");
            titleMap.put("loanTerm", "借款期限/天");
            titleMap.put("customerType", "新/老客 0：新客 1：老客");
            titleMap.put("loanReviewUser", "审核人");
            XlsxParam xlsxParam = new XlsxParam(list, title, titleMap);
            OutputStream os = response.getOutputStream();
            BuildXLSX.buildExcel(xlsxParam, os);
        } catch (Exception e) {
            log.error("downDistribution error", e);
        }
    }


    /**
     * 人工复审
     *
     * @return
     */
    @Log(title = "人工复审")
    @ResponseBody
    @PostMapping("/audit")
    public String audit(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        log.info("人工复审请求参数:{}", JSONObject.toJSONString(params));
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, "参数非法");
        }
        if (platformUser.getRoleId() != Constant.ROLEID_SUPER && platformUser.getRoleId() != Constant.ROLEID_REVIEWER) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "无权操作");
        }
        // 超管密码
        String pass = backConfigParamsDao.findStrValue("password");
        // 密码错误次数验证
        String key = "audit_" + platformUser.getPhoneNumber() + pass + DateUtil.getDateFormat(new Date(), "yyyy-MM-dd");
        Object times = redisUtil.getObject(key);
        if (times != null && (Integer) times > 5) {
            log.info("人工复审密码错误，phone={},times={}", platformUser.getPhoneNumber(), times);
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "密码错误次数超限");
        }
        //密码校验
        String password = (String) params.get("password");
        if (org.apache.commons.lang3.StringUtils.isBlank(password) || !password.equals(pass)) {
            log.info("人工复审密码错误，phone={},times={}", platformUser.getPhoneNumber(), times);
            // 密码失败次数记录
            LimitTimesUtil.incr(60 * 60 * 24, redisUtil, key);
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "账号或密码错误！");
        } else {
            redisUtil.del(key);
        }

        Object orderNoObj = params.get("out_trade_no");
        Object statusObj = params.get("status");
        if (null == orderNoObj || null == statusObj) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "参数非法");
        }

        String lockKey = "audit_lock_" + orderNoObj;
        if (!redisUtil.setIfAbsent(lockKey, "1", 300, TimeUnit.SECONDS)) {
            Long seconds =redisUtil.getExpire(lockKey);
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "您已操作人工复审,无需重复点击!订单操作锁定期" + DateUtil.getDate(seconds.intValue()));
        }

        params.remove("status");
        List<OrderBorrow> orderBorrows = orderBorrowMapper.selectSimple(params);
        byte status = Byte.parseByte(statusObj.toString());
        if ((Constant.BORROW_STATUS_REVIEW_REJECT != status && Constant.BORROW_STATUS_FKZ != status) || CollectionUtils.isEmpty(orderBorrows) || null == orderBorrows.get(0)) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "参数非法");
        }
        OrderBorrow orderBorrow = orderBorrows.get(0);
        //已分配的订单（reviewUserId!=0），审核人员审核时只能审核分配给自己的
        if (platformUser.getRoleId() == Constant.ROLEID_REVIEWER && orderBorrow.getReviewUserId() != 0 && !platformUser.getId().equals(orderBorrow.getReviewUserId())) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "无权操作");
        }

        // 增加一次订单状态结果的判断，已经给出结果的，不允许再操作
        Byte borrowStatus = orderBorrow.getStatus();
        if (borrowStatus == Constant.BORROW_STATUS_TRAIL_PASS
                || Constant.BORROW_STATUS_FKZ == borrowStatus) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "已经人工复审操作过了,请刷新列表");
        }

        String loanReviewRemark = orderBorrow.getLoanReviewRemark();
        int credit_level = 3;
        if (borrowStatus < 6) {
            if (Constant.BORROW_STATUS_REVIEW_REJECT == status) {
                // 如果给的状态是复审机审驳回，就改为复审人审拒绝
                status = 5;
                loanReviewRemark = loanReviewRemark.replace("待复审", "人工复审驳回");
            } else {
                loanReviewRemark = loanReviewRemark.replace("待复审", "人工复审通过");
                credit_level = 1;
            }
            HashMap<String, Object> params2 = new HashMap<String, Object>();
            params2.put("remark", loanReviewRemark);
            params2.put("id", orderBorrow.getId());
            params2.put("status", status);
            params2.put("credit_level", credit_level);
            params2.put("reviewId", platformUser.getId());
            int i = riskCreditUserDao.updateReview(params2);//borrow表
            if (status == 6) {
                //
                sendLoan(orderBorrow);
            }
            log.info("mbid={},orderBorrow={}", orderBorrow.getId(), orderBorrow.toString());
            if (i > 0) {

                return CallBackResult.returnJson("操作成功");
            }
        } else {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_DEFEAT, "操作失败,当前状态无法操作");
        }

        return CallBackResult.returnJson(CallBackResult.BUSINESS_DEFEAT, "操作失败");
    }

    /*@ResponseBody
    @PostMapping("/auditlocalhost")
    @Transactional
    public String auditlocalhost(Integer id, HttpServletRequest request) {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, "参数非法");
        }
        if (platformUser.getRoleId() != Constant.ROLEID_SUPER && platformUser.getRoleId() != Constant.ROLEID_REVIEWER) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "无权操作");
        }

        OrderBorrow orderBorrow = orderBorrowMapper.selectByPrimaryKey(id);

        Byte borrowStatus = orderBorrow.getStatus();
        String loanReviewRemark = orderBorrow.getLoanReviewRemark();
        int credit_level = 3;
        if (borrowStatus < 6) {

            loanReviewRemark = loanReviewRemark.replace("待复审", "人工复审通过");
            credit_level = 1;


            HashMap<String, Object> params2 = new HashMap<String, Object>();
            params2.put("remark", loanReviewRemark);
            params2.put("id", orderBorrow.getId());
            params2.put("status", 6);
            params2.put("credit_level", credit_level);
            params2.put("reviewId", 0);
            int i = riskCreditUserDao.updateReview(params2);//borrow表
            orderBorrowService.autoLoan(orderBorrow.getId() + "");
            if (i > 0) {

                return CallBackResult.returnJson("操作成功");
            }
        } else {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_DEFEAT, "操作失败,当前状态无法操作");
        }

        return CallBackResult.returnJson(CallBackResult.BUSINESS_DEFEAT, "操作失败");
    }
*/

    /**
     * 重新放款
     *
     * @return
     */
    @Log(title = "重新放款")
    @ResponseBody
    @PostMapping("/reLoan")
    @Transactional
    public String reLoan(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, "参数非法");
        }
        if (platformUser.getRoleId() != Constant.ROLEID_SUPER) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "无权操作");
        }
        Object idsObj = params.get("ids");
        if (null == idsObj) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "参数非法");
        }
        String[] split = idsObj.toString().split(",");
        if (ArrayUtils.isNotEmpty(split)) {
            orderBorrowMapper.updateLoan(split);
            orderBorrowMapper.updateLoanFail(split);
            List<OrderBorrow> orderBorrows = orderBorrowMapper.selectForLoan(split);
            sendLoanList(orderBorrows);
            return CallBackResult.returnJson("操作成功");
        }

        return CallBackResult.returnJson(CallBackResult.BUSINESS_DEFEAT, "操作失败");
    }

    /**
     * 手动放款
     *
     * @return
     */
    @Log(title = "手动放款")
    @ResponseBody
    @PostMapping("/manualLoan")
//    @Transactional(propagation = Propagation.REQUIRED)
    public String manualLoan(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        String phoneNumber = platformUser.getPhoneNumber();
        log.info("【手动放款】-----phone={},ip={},param={}", phoneNumber, IpAdrressUtil.getIpAdrress(request), params);
        if (platformUser.getRoleId() != Constant.ROLEID_SUPER) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "无权操作");
        }
        // 超管密码
        String pass = backConfigParamsDao.findStrValue("password");
        // 密码错误次数验证
        String key = "manualLoan_" + phoneNumber + pass + DateUtil.getDateFormat(new Date(), "yyyy-MM-dd");
        Object times = redisUtil.getObject(key);
        if (times != null && (Integer) times > 5) {
            log.info("手动放款密码错误，phone={},times={}", phoneNumber, times);
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "密码错误次数超限");
        }
        String password = (String) params.get("password");
        if (org.apache.commons.lang3.StringUtils.isBlank(password) || !password.equals(pass)) {
            log.info("手动放款密码错误，phone={},times={}", phoneNumber, times);
            // 密码失败次数记录
            LimitTimesUtil.incr(60 * 60 * 24, redisUtil, key);
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "账号或密码错误！");
        } else {
            redisUtil.del(key);
        }
        Object idsObj = params.get("ids");
        if (null == idsObj) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "参数非法");
        }
        String[] split = idsObj.toString().split(",");
        if (ArrayUtils.isNotEmpty(split)) {
            List<OrderBorrow> orderBorrows = orderBorrowMapper.selectForLoan(split);
            //loanManual(orderBorrows);
            //发送消息队列进行处理
            orderProducer.sendmanualLoan(JSONObject.toJSONString(orderBorrows));
            return CallBackResult.returnJson("操作成功");
        }

        return CallBackResult.returnJson(CallBackResult.BUSINESS_DEFEAT, "操作失败");
    }

    @Async
    public void sendLoanList(List<OrderBorrow> orderBorrows) {
        if (CollectionUtils.isNotEmpty(orderBorrows)) {
            for (OrderBorrow orderBorrow : orderBorrows) {
                doSend(orderBorrow);
            }
        }
    }

    @Async
    public void sendLoan(OrderBorrow orderBorrow) {
        doSend(orderBorrow);
    }

    private void doSend(OrderBorrow orderBorrow) {
        Integer id = orderBorrow.getId();
        String dateFormatMin = DateUtil.getDateFormat(new Date(), "yyyy-MM-dd HH:mm");
        String minkey = SEND_LOAN + dateFormatMin + id;
        long minIn = redisUtil.incr(minkey, 1);
        int timeIn = 60;
        boolean expireIn = redisUtil.expire(minkey, timeIn);
        if (!expireIn) {
            boolean expire1 = redisUtil.expire(minkey, timeIn);
            if (!expire1) {
                log.error("BORROW SEND_LOAN expireIn error id=" + id);
            }
        }
        if (minIn == 1) {
            log.info("BORROW SEND_LOAN,id={},minIn={}", id, minIn);
            orderProducer.sendLoan(orderBorrow.getId() + "", null);
        }
    }

    /**
     * 取消放款
     *
     * @return
     */
    @Log(title = "取消放款")
    @ResponseBody
    @PostMapping("/cancelLoan")
    public String cancelLoan(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, "参数非法");
        }
        if (platformUser.getRoleId() != Constant.ROLEID_SUPER) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "无权操作");
        }
        Object idsObj = params.get("ids");
        if (null == idsObj) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "参数非法");
        }
        String[] split = idsObj.toString().split(",");


        if (ArrayUtils.isNotEmpty(split)) {
            int i = orderBorrowService.cancelLoan(split);
            if (i > 0) {

                return CallBackResult.returnJson("操作成功");
            } else {
                return CallBackResult.returnJson(CallBackResult.BUSINESS_DEFEAT, "取消失败");
            }
        }

        return CallBackResult.returnJson(CallBackResult.BUSINESS_DEFEAT, "操作失败");
    }

    /**
     * 查看详情通讯录
     *
     * @return
     */
    @ResponseBody
    @PostMapping("/details")
    public String details(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, "参数非法");
        }
        // 超管，审核人员，逾期主管，逾期专员，当日催收主管，当日催收专员才能查询
        Integer roleId = platformUser.getRoleId();
        if (roleId != Constant.ROLEID_SUPER && roleId != Constant.ROLEID_CUISHOUADMIN && roleId != Constant.ROLEID_REVIEWER && roleId != Constant.ROLEID_COLLECTOR && roleId != Constant.ROLEID_COLLECTION_TODAY && roleId != Constant.ROLEID_BOSS_ADMIN_ADMIN) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "没有权限");
        }
        Object userId = params.get("userId");
        if (Objects.isNull(userId)) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "参数非法");
        }
        params.put("userId", userId);
        Object pageSizeObj = params.get("pageSize");
        Object pageNumObj = params.get("pageNum");
        if (null == pageNumObj) {
            params.put("pageNum", 1);
        }
        if (null == pageSizeObj) {
            params.put("pageSize", 10);
        }
        PageHelper.startPage(params);
       /* UserInfo byPhone = userInfoMapper.findByPhone(AESDecrypt.encrypt(phone.toString()));
        if (null != byPhone){
            params.put("userId", byPhone.getId() );
        }*/

        List<UserContacts> userContactsList = userContactsDAO.selectUserContacts(params);
        Map<String, Object> res = new HashMap<>();
        res.put("page", new PageInfo<>(userContactsList));

        return CallBackResult.returnJson(res);
    }

    /**
     * 查看详情报告
     *
     * @return
     */
    @ResponseBody
    @PostMapping("/detailOther")
    public String detailOther(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, "参数非法");
        }
        // 超管，审核人员，逾期主管，逾期专员，当日催收主管，当日催收专员才能查询
        Integer roleId = platformUser.getRoleId();
        if (roleId != Constant.ROLEID_SUPER && roleId != Constant.ROLEID_CUISHOUADMIN && roleId != Constant.ROLEID_REVIEWER && roleId != Constant.ROLEID_COLLECTOR && roleId != Constant.ROLEID_COLLECTION_TODAY && roleId != Constant.ROLEID_BOSS_ADMIN_ADMIN) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "没有权限");
        }
        String userId = String.valueOf(params.get("userId"));
        if (StringUtils.isEmpty(userId)) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "参数非法");
        }
        Integer userIdInt = Integer.valueOf(userId);
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> res = new HashMap<>();
        BackConfigParamsVo reviewSwitch = backConfigParamsDao.findBySysKey("REVIEW_JS");
//        AESDecrypt.encryptMapPhone(params);
        map.put("userId", userId);
        List<Map<String, Object>> orderBorrows = orderBorrowMapper.selectRecord(map);
        for (Map<String, Object> orderBorrowsMap : orderBorrows) {
            Object phoneMap = orderBorrowsMap.get("phone");
            if (null != phoneMap) {
                orderBorrowsMap.replace("phone", phoneMap.toString());
            }
            Object real_name = orderBorrowsMap.get("real_name");
            if (null != real_name) {
                orderBorrowsMap.replace("real_name", real_name.toString());
            }
        }

        res.put("orderBorrows", orderBorrows);
        List<OrderCollection> orderCollections = orderCollectionDAO.selectSimple(map);
        List<CollectionUserVO> collectionUserVOS = new ArrayList<>();
        for (OrderCollection orderCollection : orderCollections) {
            if (orderCollection == null) {
                continue;
            }
            List<CollectionUserVO> list = collectionDetail(orderCollection.getId());
            collectionUserVOS.addAll(list);
        }

        Collections.sort(collectionUserVOS, new Comparator<CollectionUserVO>() {
            @Override
            public int compare(CollectionUserVO o1, CollectionUserVO o2) {
                return o2.getCreateTimeChg().compareTo(o1.getCreateTimeChg());
            }
        });
        res.put("collectionUserVOS", collectionUserVOS);

        //魔杖开关 （前端根据开关来展示获取报告得按钮）
        BackConfigParamsVo bySysKey = backConfigParamsDao.findBySysKey("mz_switch");
        if (null != bySysKey && null != bySysKey.getSysValue()) {
            res.put("mzSwitch", bySysKey.getSysValue());
        }
        //及用户现居住地址
        UserInfo userInfo = userInfoMapper.selectByPrimaryKey(userIdInt);
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("headPortrait", userInfo.getHeadPortrait());
        userMap.put("idcardImgZ", userInfo.getIdcardImgZ());
        userMap.put("idcardImgF", userInfo.getIdcardImgF());


        HashMap<String, Object> params1 = new HashMap<String, Object>();
        params1.put("userId", userId);
        params1.put("limit", 1);
        List<UserCardInfo> userCardInfoList = userCardInfoDao.findUserCardByUserId(params);
        userMap.put("acct_name", userInfo.getRealName());
        userMap.put("phone", userInfo.getPhone());
        if (CollectionUtils.isEmpty(userCardInfoList)) {
            userMap.put("card_no", "");
            userMap.put("bank", "");
            userMap.put("branch", "");
        } else {
            UserCardInfo cardInfo = userCardInfoList.get(0);
            //银行卡号
            // 状态 (0:未生效   1:已生效)
            userMap.put("acct_name", cardInfo.getOpenName());
            userMap.put("card_no", cardInfo.getCardNo());
            userMap.put("bank", cardInfo.getBankName());
            userMap.put("branch", cardInfo.getBankAddress());
        }


        res.put("idCard", userMap);
        //根据渠道配置来控制审核
        PlateformChannel plateformChannel = plateformChannelMapper.selectByPrimaryKey(userInfo.getChannelId());
        if (null != plateformChannel) {
            Integer auditSwitch = plateformChannel.getAuditSwitch();
            if (null != auditSwitch) {

                if (auditSwitch == 1) {
                    reviewSwitch.setSysValue(1);
                    reviewSwitch.setStrValue("1");
                } else if (auditSwitch == 2) {
                    reviewSwitch.setSysValue(0);
                    reviewSwitch.setStrValue("0");
                }
            }
        }
        res.put("reviewSwitch", reviewSwitch);

        //紧急联系人
        ContactVo contactVo = new ContactVo(userInfo.getFirstContactName(), userInfo.getFirstContactPhone(), userInfo.getFirstContactRelation(), userInfo.getSecondContactName(), userInfo.getSecondContactPhone(), userInfo.getSecondContactRelation(), userInfo.getPresentAddress(), userInfo.getPresentAddressDetail());
        res.put("contactVo", contactVo);
        return CallBackResult.returnJson(res);
    }


    @PostMapping("/rptUrl")
    @ResponseBody
    public Object rptUrl(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, "参数非法");
        }

        // 超管，审核人员，逾期主管，逾期专员，当日催收主管，当日催收专员才能查询
        Integer roleId = platformUser.getRoleId();
        if (roleId != Constant.ROLEID_SUPER && roleId != Constant.ROLEID_CUISHOUADMIN && roleId != Constant.ROLEID_REVIEWER && roleId != Constant.ROLEID_COLLECTOR && roleId != Constant.ROLEID_COLLECTION_TODAY && roleId != Constant.ROLEID_BOSS_ADMIN_ADMIN) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "没有权限");
        }

        Object phone = params.get("phone");
        if (null == phone) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "参数非法");
        }
        Map<String, Object> res = new HashMap<>();
        UserInfo userInfo = userInfoMapper.findByPhone(phone.toString());
        if (null != userInfo) {
            //运营商报告
            String msgShow = "";
            StringBuilder sb = new StringBuilder();
            UserMoXieDataWithBLOBs data = userMoXieDataDAO.selectDataByUserId(userInfo.getId());
            if (null != data && !StringUtils.isEmpty(data.getMsgShow())) {
                msgShow = data.getMsgShow();
            }

            if (org.apache.commons.lang3.StringUtils.isNotBlank(msgShow)) {
                if (data.getMxAuthStatus() == 4) {
                    //新颜
                    res.put("rpt_url", msgShow);
                } else if (data.getMxAuthStatus() == 7) {
                    res.put("rpt_url", "#");

                } else {
                    //魔蝎
                    sb = new StringBuilder(MXRPT_SHOW_URL);
                    //联系号码:姓名:关系,联系号码:姓名:关系
                    sb.append(msgShow).append("&contact=");
                    sb.append(userInfo.getFirstContactPhone()).append(":").append(userInfo.getFirstContactName()).append(":").append(Constant.FIRST_CONTACT_TYPE_MAP.get(userInfo.getFirstContactRelation())).append(",");
                    sb.append(userInfo.getSecondContactPhone()).append(":").append(userInfo.getSecondContactName()).append(":").append(Constant.SECOND_CONTACT_TYPE_MAP.get(userInfo.getSecondContactRelation()));
                    res.put("rpt_url", sb.toString());
                }
            } else {
                res.put("rpt_url", "#");
            }
        }
        return CallBackResult.returnJson(res);
    }

    public List<CollectionUserVO> collectionDetail(Integer id) {
        List<CollectionUserVO> list = new ArrayList<>();
        OrderCollection orderCollection = orderCollectionDAO.selectByPrimaryKey(id);
        if (null == orderCollection) {
            return list;
        }
        Map<String, Object> map = new HashMap<String, Object>(4) {{
            put("collectionId", orderCollection.getId());
            put("auditStatus", Constant.COLLECTION_AUDIT_PASS);
            put("repaymentId", orderCollection.getRepaymentId());
            put("status", Constant.REPAYMENTDETAIL_STATUS_PAID);
        }};
        List<OrderCollectionReduction> orderCollectionReductions = orderCollectionReductionDAO.selectSimple(map);
        PlatformUser platformUser = platformUserMapper.selectByPrimaryKey(orderCollection.getCurrentCollectionUserId());
        String userName = "";
        if (null != platformUser) {
            userName = platformUser.getUserName();
        }
        StringBuilder sb = null;
        if (CollectionUtils.isNotEmpty(orderCollectionReductions)) {
            for (OrderCollectionReduction orderCollectionReduction : orderCollectionReductions) {
                sb = new StringBuilder();
                CollectionUserVO collectionUserVO =
                        new CollectionUserVO(userName,
                                orderCollectionReduction.getCreateTimeChg(), "减免滞纳金",
                                sb.append("减免").append(orderCollectionReduction.getReductionAmount()).toString());
                list.add(collectionUserVO);
                sb = null;
            }
        }
        List<OrderCollectionDetail> orderCollectionDetails = orderCollectionDetailDAO.selectSimple(map);
        if (CollectionUtils.isNotEmpty(orderCollectionDetails)) {
            for (OrderCollectionDetail orderCollectionDetail : orderCollectionDetails) {
                sb = new StringBuilder();
                String collectionType = "";
                if (Constant.COLLECTION_TYPE_PHONE == orderCollectionDetail.getCollectionType()) {
                    collectionType = "电话催收";
                } else if (Constant.COLLECTION_TYPE_MSG == orderCollectionDetail.getCollectionType()) {
                    collectionType = "短信催收";
                }
                CollectionUserVO collectionUserVO =
                        new CollectionUserVO(userName,
                                orderCollectionDetail.getCreateTimeChg(), "催收记录",
                                sb.append(Constant.COLLECT_CONTACT_TYPE_MAP.get(orderCollectionDetail.getContactType())).append("-").append(Constant.COLLECT_RELATION_TYPE_MAP.get(orderCollectionDetail.getRelation())).append("-").append(collectionType).append(
                                        "-").append(orderCollectionDetail.getCollectionTag()).append("-").append(orderCollection.getPromiseRepaymentTimeChg()).append("-").append(orderCollectionDetail.getRemark()).toString());
                list.add(collectionUserVO);
                sb = null;
            }
        }
        List<OrderRepaymentDetail> orderRepaymentDetails = orderRepaymentDetailDAO.selectSimple(map);
        if (CollectionUtils.isNotEmpty(orderRepaymentDetails)) {
            for (OrderRepaymentDetail orderRepaymentDetail : orderRepaymentDetails) {
                sb = new StringBuilder();
                CollectionUserVO collectionUserVO =
                        new CollectionUserVO(userName,
                                orderRepaymentDetail.getCreateTimeChg(),
                                Constant.PAY_TYPE_MAP.get(orderRepaymentDetail.getPayType()) + "还款",
                                sb.append("已还").append(orderRepaymentDetail.getPaidAmount()).toString());
                list.add(collectionUserVO);
                sb = null;
            }
        }
        return list;
    }

    /**
     * 放款记录列表
     *
     * @param jsonData
     * @return
     */
    @ResponseBody
    @PostMapping("/queryWithUserPass")
    public String queryWithUserPass(@RequestBody String jsonData, HttpServletRequest request) {
        long l = System.currentTimeMillis();
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        // 超管和财务可以查询
        Integer roleId = platformUser.getRoleId();
        if (roleId != Constant.ROLEID_SUPER && roleId != Constant.ROLEID_FINANCE) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "没有权限");
        }
        Map<String, Object> params = JSONObject.parseObject(jsonData);
//        //审核人员只能看到分配给自己的订单
//        if (Constant.ROLEID_REVIEWER == platformUser.getRoleId()) {
//            params.put("reviewUserId", platformUser.getId());
//        }
        log.info("OrderBorrowController queryWithUserPass params=" + params.toString());
        Object statusObj = params.get("status");
        if (null == statusObj) {
            //筛选放款
            params.put("pass", "true");
        }
        PageHelper.startPage(params);
        //TODO CHECK BANK ACCOUNT
        List<BorrowUserVO> list = orderBorrowMapper.orderList(params);
       /* for (BorrowUserVO borrowUserVO : list) {
            borrowUserVO.setPhone(PhoneUtil.maskPhoneNum(borrowUserVO.getPhone()));
            borrowUserVO.setIdCard(IdcardUtil.hide(borrowUserVO.getIdCard(), 5, 15));
        }*/
        params.clear();
        params.put("roleId", 40);
        List<PlatformUser> platformUsers = platformUserMapper.selectSimple(params);
        Map<String, Object> res = new HashMap<>();
        res.put("page", new PageInfo<>(list));
        res.put("platformUsers", platformUsers);
        //渠道
        List<PlateformChannel> plateformChannels = plateformChannelMapper.selectSimple(new HashMap<>());
        res.put("channel", plateformChannels);
        long e = System.currentTimeMillis();
        log.info("放款列表params={} 耗时ms={}", params.toString(), (e - l));
        return CallBackResult.returnJson(res);
    }

    /**
     * 机审未通过列表
     *
     * @param jsonData
     * @return
     */
    @ResponseBody
    @PostMapping("/queryDenied")
    public String queryDenied(@RequestBody String jsonData, HttpServletRequest request) {
        Map<String, Object> params = JSONObject.parseObject(jsonData);
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        //审核人员只能看到分配给自己的订单
        if (Constant.ROLEID_REVIEWER == platformUser.getRoleId()) {
            params.put("reviewUserId", platformUser.getId());
        }
        log.info("OrderBorrowController queryDenied params=" + params.toString());
        Object statusObj = params.get("status");
        if (null == statusObj) {
            //机审未通过
            params.put("denied", "true");
        }
        PageHelper.startPage(params);
        PageInfo<BorrowUserVO> pageInfo = orderBorrowService.queryWithUser(params);
        List<PlateformChannel> plateformChannels = plateformChannelMapper.selectSimple(new HashMap<>());
        Map<String, Object> res = new HashMap<>();
        res.put("page", pageInfo);
        res.put("channel", plateformChannels);
        return CallBackResult.returnJson(res);
    }

    /**
     * 重新审核（重推）
     *
     * @param jsonData
     * @return
     */
    @Log(title = "重新审核（重推）")
    @PostMapping("/toReview")
    @ResponseBody
    public String toReview(@RequestBody String jsonData, HttpServletRequest request) throws Exception {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        // 超管和审核可以查询
        Integer roleId = platformUser.getRoleId();
        if (roleId != Constant.ROLEID_SUPER && roleId != Constant.ROLEID_REVIEWER) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "没有权限");
        }

        Map<String, Object> params = JSONObject.parseObject(jsonData);
        log.info("OrderBorrowController toReview params=" + params.toString());
        OrderBorrow orderBorrow = checkId(params);
        if (null == orderBorrow) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "参数非法");
        }
        //防止重复提交，1秒只能提交一次
        if (redisUtil.hasKey("toReview_" + orderBorrow.getId())) {
            return CallBackResult.returnJson(999, "操作已完成，请勿重复提交");
        } else {
            redisUtil.set("toReview_" + orderBorrow.getId(), "toReview_" + orderBorrow.getId(), 1);
        }
        OrderBorrow newOrder = new OrderBorrow();
        newOrder.setId(orderBorrow.getId());
        newOrder.setStatus(Constant.BORROW_STATUS_REVIEW_PERSON);
        orderBorrowService.updateSelective(newOrder);
        return CallBackResult.returnJson("操作成功");
    }

    /**
     * 转派
     *
     * @param jsonData
     * @return
     */
    @Log(title = "转派")
    @PostMapping("/redistribute")
    @ResponseBody
    public String redistribute(@RequestBody String jsonData, HttpServletRequest request) throws Exception {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }

        Integer roleId = platformUser.getRoleId();
        if (roleId != Constant.ADMIN_ROLE_ID && Constant.CUISHOU_ADMIN_ID != roleId) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "您无权操作！");
        }

        Map<String, Object> params = JSONObject.parseObject(jsonData);
        log.info("OrderBorrowController reDistribute params=" + params.toString());
        OrderBorrow orderBorrow = checkId(params);
        if (null == orderBorrow) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "参数非法");
        }
        //TODO CHECK BACKUSER
        Object platformUserId = params.get("platformUserId");
        if (null == platformUserId) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "参数非法");
        }
        OrderBorrow newOrder = new OrderBorrow();
        newOrder.setId(orderBorrow.getId());

        newOrder.setReviewUserId(Integer.parseInt(platformUserId.toString()));
        orderBorrowService.updateSelective(newOrder);
        return CallBackResult.returnJson("操作成功");
    }

    /**
     * 导出审核订单列表
     *
     * @param request
     * @param response
     * @param jsonData
     * @throws Exception
     */
    @Log(title = "导出审核订单列表")
    @RequestMapping("downloadOrderReview")
    public void downloadOrderReview(HttpServletRequest request, HttpServletResponse response,
                                    @RequestBody String jsonData) throws Exception {

        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            returnLoginBad(response);
            return;
        }
        Integer roleId = platformUser.getRoleId();
        if (Constant.ROLEID_SUPER != roleId) {
            returnErrorAuth(response);
            return;
        }

        //判断下载通道开启关闭
        Integer downloadSwitch = downloadSwitch();
        if (null != downloadSwitch) {
            downloadBad(response);
            return;
        }

        Map<String, Object> params = JSONObject.parseObject(jsonData);
        log.info("OrderBorrowController downloadOrderReview params=" + params.toString());
        Object statusObj = params.get("status");

        List<BorrowUserVO> orderBorrows = orderBorrowMapper.findParams(params);
        log.info("OrderBorrowController downloadOrderReview size=" + orderBorrows.size());
        String title = "审核订单列表";
        BuildXLSX.setFileDownloadHeader(request, response, title);
        LinkedHashMap<String, String> titleMap = new LinkedHashMap<>();
        titleMap.put("createTimeChg", "申请时间");
        titleMap.put("realName", "客户姓名");
        titleMap.put("phone", "手机号码");
        titleMap.put("channelName", "渠道来源");
        titleMap.put("amountAvailable", "可借金额");
        titleMap.put("applyAmount", "申请金额");
        titleMap.put("loanTerm", "借款期限");
        titleMap.put("status", "审批状态");
        titleMap.put("reviewRemark", "不通过原因");
        titleMap.put("renewalCount", "续期次数");
        titleMap.put("reviewer", "审核人");
        titleMap.put("outTradeNo", "订单号");
        XlsxParam xlsxParam = new XlsxParam(orderBorrows, title, titleMap);
        OutputStream os = response.getOutputStream();
        BuildXLSX.buildExcel(xlsxParam, os);
    }

    /**
     * 导出放款订单列表
     *
     * @param request
     * @param response
     * @param jsonData
     * @throws Exception
     */
    @Log(title = "导出放款订单列表")
    @RequestMapping("downloadOrderPass")
    public void downloadOrderPass(HttpServletRequest request, HttpServletResponse response,
                                  @RequestBody String jsonData) throws Exception {

        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            returnLoginBad(response);
            return;
        }
        Integer roleId = platformUser.getRoleId();
        if (Constant.ROLEID_SUPER != roleId) {
            returnErrorAuth(response);
            return;
        }

        //判断下载通道开启关闭
        Integer downloadSwitch = downloadSwitch();
        if (null != downloadSwitch) {
            downloadBad(response);
            return;
        }

        Map<String, Object> params = JSONObject.parseObject(jsonData);
        log.info("OrderBorrowController downloadOrderPass params=" + params.toString());
        Object statusObj = params.get("status");
        if (null == statusObj) {
            //筛选放款
            params.put("pass", "true");
        }
        List<BorrowUserVO> orderBorrows = orderBorrowMapper.orderList(params);
        log.info("OrderBorrowController downloadOrderPass size=" + orderBorrows.size());
        for (BorrowUserVO orderBorrow : orderBorrows) {
            orderBorrow.setLoanAmount(orderBorrow.getIntoMoney() + orderBorrow.getLoanFee());
        }
        String title = "放款记录";
        BuildXLSX.setFileDownloadHeader(request, response, title);
        LinkedHashMap<String, String> titleMap = new LinkedHashMap<>();
        titleMap.put("realName", "客户姓名");
        titleMap.put("phone", "手机号码");
        titleMap.put("borrowStatusName", "放款状态");
        titleMap.put("applyAmountNormal", "申请金额");
        titleMap.put("createTimeChg", "申请时间");
        titleMap.put("loanTimeChg", "放款时间");
        titleMap.put("loanTerm", "借款期限");
        titleMap.put("amountAvailableNormal", "批复金额");
        titleMap.put("intoMoneyNormal", "实际到账金额");
        titleMap.put("reviewer", "审核人");
        titleMap.put("bankAccount", "放款账户");
        titleMap.put("outTradeNo", "订单号");
        titleMap.put("flowNo", "三方订单号");
        XlsxParam xlsxParam = new XlsxParam(orderBorrows, title, titleMap);
        OutputStream os = response.getOutputStream();
        BuildXLSX.buildExcel(xlsxParam, os);
    }

    private OrderBorrow checkId(Map<String, Object> params) {
        Object idObj = params.get("id");
        if (null == idObj) {
            return null;
        }
        return orderBorrowMapper.selectByPrimaryKey(Integer.parseInt(idObj.toString()));
    }


    /**
     * 安卓用户7天内的短信
     *
     * @param jsonData
     * @return
     */
    @ResponseBody
    @PostMapping("/querySms")
    public String querySms(@RequestBody String jsonData, HttpServletRequest request) {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, "参数非法");
        }
        Integer roleId = platformUser.getRoleId();
        // 超管，审核人员，逾期主管，逾期专员，当日催收主管，当日催收专员才能查询
        if (roleId != Constant.ROLEID_SUPER && roleId != Constant.ROLEID_CUISHOUADMIN && roleId != Constant.ROLEID_REVIEWER && roleId != Constant.ROLEID_COLLECTOR && roleId != Constant.ROLEID_COLLECTION_TODAY && roleId != Constant.ROLEID_BOSS_ADMIN_ADMIN) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "没有权限");
        }
        Map<String, Object> params = JSONObject.parseObject(jsonData);
        Object userId = params.get("userId");
        if (null == userId) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "参数非法");
        }
        PageHelper.startPage(params);
        List<UserShortMessageVo> list = userShortMessageService.selectByPhone(params);
        return CallBackResult.returnJson(new PageInfo<>(list));
    }

    /**
     * 修改手动放款密码
     *
     * @param jsonData
     * @param request
     * @return
     */
    @Log(title = "修改手动放款密码")
    @RequestMapping("/updatePwd")
    public @ResponseBody
    Object updatePwd(@RequestBody String jsonData, HttpServletRequest request) {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, "登录失效，请重新登录！");
        }
        //超管才能修改
        if (Constant.ROLEID_SUPER != platformUser.getRoleId()) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "对不起，您没有该权限！");
        }
        // 超管密码
        String pass = backConfigParamsDao.findStrValue("password");
        // 密码错误次数验证
        String key = "updatePwd_" + platformUser.getPhoneNumber() + pass + DateUtil.getDateFormat(new Date(), "yyyy-MM-dd");
        Object times = redisUtil.getObject(key);
        if (times != null && (Integer) times > 5) {
            log.info("更新复审风控规则密码错误，phone={},times={}", platformUser.getPhoneNumber(), times);
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "密码错误次数超限");
        }
        JSONObject params = JSONObject.parseObject(jsonData);
        String oldPwd = params.getString("oldPwd");
        if (org.apache.commons.lang3.StringUtils.isBlank(oldPwd)) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "原密码不能为空！");
        }
        if (org.apache.commons.lang3.StringUtils.isBlank(pass)) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "该系统尚未设置初始密码！");
        }
        if (!pass.equals(oldPwd)) {
            // 密码失败次数记录
            LimitTimesUtil.incr(60 * 60 * 24, redisUtil, key);
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "原密码输入错误，请重新输入！");
        } else {
            redisUtil.del(key);
        }
        String newPwd = params.getString("newPwd");
        if (org.apache.commons.lang3.StringUtils.isBlank(newPwd)) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "新密码不能为空！");
        }

        BackConfigParams backConfigParams = new BackConfigParams();
        backConfigParams.setSysKey("password");
        backConfigParams.setSysValue(newPwd.toString());
        backConfigParamsDao.updateBySyskey(backConfigParams);
        return CallBackResult.returnJson(CallBackResult.SUCCESS, "密码修改成功！");

    }


    /**
     * 修改用户联系密码
     *
     * @param jsonData
     * @param request
     * @return
     */
    @Log(title = "修改手动放款密码")
    @RequestMapping("/upContactsPwd")
    public @ResponseBody
    Object upContactsPwd(@RequestBody String jsonData, HttpServletRequest request) {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, "登录失效，请重新登录！");
        }
        //超管才能修改
        if (Constant.ROLEID_SUPER != platformUser.getRoleId()) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "对不起，您没有该权限！");
        }
        // 超管密码
        String pass = backConfigParamsDao.findStrValue("contactsPwd");
        // 密码错误次数验证
        String key = "upContactsPwd_" + platformUser.getPhoneNumber() + pass + DateUtil.getDateFormat(new Date(), "yyyy-MM-dd");
        Object times = redisUtil.getObject(key);
        if (times != null && (Integer) times > 5) {
            log.info("修改联系人密码错误，phone={},times={}", platformUser.getPhoneNumber(), times);
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "密码错误次数超限");
        }
        JSONObject params = JSONObject.parseObject(jsonData);
        String oldPwd = params.getString("oldContactsPwd");
        if (org.apache.commons.lang3.StringUtils.isBlank(oldPwd)) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "原密码不能为空！");
        }
        if (org.apache.commons.lang3.StringUtils.isBlank(pass)) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "该系统尚未设置初始密码！");
        }
        if (!pass.equals(oldPwd)) {
            // 密码失败次数记录
            LimitTimesUtil.incr(60 * 60 * 24, redisUtil, key);
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "原密码输入错误，请重新输入！");
        } else {
            redisUtil.del(key);
        }
        String newPwd = params.getString("newContactsPwd");
        if (org.apache.commons.lang3.StringUtils.isBlank(newPwd)) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "新密码不能为空！");
        }

        BackConfigParams backConfigParams = new BackConfigParams();
        backConfigParams.setSysKey("contactsPwd");
        backConfigParams.setSysValue(newPwd.toString());
        backConfigParamsDao.updateBySyskey(backConfigParams);
        return CallBackResult.returnJson(CallBackResult.SUCCESS, "密码修改成功！");

    }


    /**
     * 导出放款记录表Excel
     *
     * @param
     * @return
     */
    @PostMapping("/userPassX")
    public String exportWithPsXls(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String, Object> params) throws Exception {
       /* PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        Integer roleId = platformUser.getRoleId();
        // 超管和客服才能查看客户列表
        if (roleId != Constant.ROLEID_SUPER || isExporExcel(platformUser.getPhoneNumber())) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "没有权限");
        }*/
        Object statusObj = params.get("status");
        if (null == statusObj) {
            //筛选放款
            params.put("pass", "true");
        }

        List<BorrowUserVO> VOlist = orderBorrowMapper.orderList(params);

        List<Map> map = new ArrayList<>();
        for (BorrowUserVO vo : VOlist) {
            Map<String, Object> param = new HashMap<>();
            param.put("realName", vo.getRealName());
            param.put("outTradeNo", vo.getOutTradeNo());
            param.put("flowNo", vo.getFlowNo());
            param.put("channelName", vo.getChannelName());
            param.put("phone", vo.getPhone());
            param.put("borrowStatusName", vo.getLoanStatusName());
            param.put("loanReviewTime", vo.getLoanReviewTime());
            param.put("loanTerm", vo.getLoanTerm());
            param.put("loanTimeChg", vo.getLoanTimeChg());
            param.put("applyAmount", vo.getApplyAmountNormal());
            param.put("intoMoney", vo.getIntoMoneyNormal());
            param.put("serviceCharge", vo.getServiceCharge() / 100);
            param.put("interest", vo.getInterestNormal());
            param.put("loanTerm", vo.getLoanTerm());
            param.put("customerType", ExcelExportUtil.changCustomerTypeName(vo.getCustomerType()));
            map.add(param);
        }

        String[] title = new String[]{"客户姓名", "订单号", "第三方支付订单号", "渠道来源", "手机号码", "放款状态", "审核时间", "借款期限", "放款时间", "申请金额/元", "放款本金", "服务费", "利息", "借款期限/天", "新/老客"};

        String[] properties = new String[]{"realName", "outTradeNo", "flowNo", "channelName", "phone", "borrowStatusName", "loanReviewTime", "loanTerm", "loanTimeChg", "applyAmount", "intoMoney", "serviceCharge", "interest", "loanTerm", "customerType"};
        String sheetName = "放款记录";
        ExcelExportUtil.exportExcel(response, map, title, properties, sheetName);
        return CallBackResult.returnJson(CallBackResult.SUCCESS, "导出成功");
    }


    // 修改金额
    @PostMapping("/updateApplicationAmount")
    @ResponseBody
    public Object updateApplicationAmount(HttpServletRequest request, Integer id,Integer userId,Integer applyAmount,Integer loanTerm,Integer feeApr) throws ParseException {
    	//redisPlatformUser
        /*PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        // 超管、渠道主管、渠道专员可以查询
        Integer roleId = platformUser.getRoleId();
        if (roleId != Constant.ROLEID_SUPER && roleId != Constant.CHANNEL_ADMIN_ROLE_ID && roleId != Constant.OPERATOR_ROLE_ID) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "没有权限");
        }*/
        if(null == applyAmount || applyAmount <= 0) {
        	return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "申请金额不能为空且不能为0！");
        }
        if(null == loanTerm || loanTerm <= 0) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "借款天数不能为空且不能为0！");
        }
        if(null == feeApr || feeApr <= 0) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "费用利率不能为空且不能为0！");
        }
        /*if(applyAmount >= 8000) {
        	return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "申请金额单位为元,需要大额操作请联系系统管理员！");
        }*/
        /*UserMoneyRate userMoneyRate = orderBorrowMapper.findUsersRate(userId);
        Double rate = userMoneyRate.getServiceCharge() + userMoneyRate.getAccrual();
        Double loanFee = (applyAmount *  Constant.DOLLAR_CHANGE_PENNY ) * rate;
        Double intoMonet = (applyAmount *  Constant.DOLLAR_CHANGE_PENNY) - loanFee;
        Double serviceCharge = (applyAmount *  Constant.DOLLAR_CHANGE_PENNY) * userMoneyRate.getServiceCharge();
        Double accrual = (applyAmount *  Constant.DOLLAR_CHANGE_PENNY) * userMoneyRate.getAccrual();*/
        Double rate = new BigDecimal(feeApr).divide(BigDecimal.valueOf(Constant.DOLLAR_CHANGE_PENNY)).doubleValue();
        //Double rate = (double) (feeApr / Constant.DOLLAR_CHANGE_PENNY);
        Double loanFee = (applyAmount * Constant.DOLLAR_CHANGE_PENNY ) * rate ;
        Integer intoMonet = (applyAmount * Constant.DOLLAR_CHANGE_PENNY) - loanFee.intValue();
        Double serviceCharge = (applyAmount *  Constant.DOLLAR_CHANGE_PENNY) * (rate - 0.007);
        Double accrual = (applyAmount *  Constant.DOLLAR_CHANGE_PENNY) * 0.007;
        log.info(rate +"================= loanFee" + loanFee + "intoMonet" + intoMonet + "serviceCharge" + serviceCharge + "accrual" + accrual);
        OrderBorrow orderBorrow = new OrderBorrow();
        orderBorrow.setId(id);
        orderBorrow.setLoanTerm(loanTerm);
        orderBorrow.setFeeApr(feeApr * Constant.DOLLAR_CHANGE_PENNY);
        orderBorrow.setApplyAmount(applyAmount *  Constant.DOLLAR_CHANGE_PENNY);
        orderBorrow.setIntoMoney(intoMonet);
        orderBorrow.setLoanFee(loanFee.intValue());
        orderBorrow.setServiceCharge(serviceCharge.intValue());
        orderBorrow.setAccrual(accrual.intValue());
        int count = orderBorrowMapper.updateApplicationAmount(orderBorrow);

        return count > 0 ? CallBackResult.ok("修改成功") : CallBackResult.fail();
    }


    // 删除订单
    @PostMapping("/deleteBorrowOrder")
    @ResponseBody
    @Transactional
    public Object deleteBorrowOrder(HttpServletRequest request, Integer id,Integer userId) throws ParseException {
        //redisPlatformUser
        /*PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        // 超管、渠道主管、渠道专员可以查询
        Integer roleId = platformUser.getRoleId();
        if (roleId != Constant.ROLEID_SUPER && roleId != Constant.CHANNEL_ADMIN_ROLE_ID && roleId != Constant.OPERATOR_ROLE_ID) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "没有权限");
        }*/
        /*OrderPushRiskDTO orderPushRiskDTO = new OrderPushRiskDTO();
        orderPushRiskDTO.setOrderId(id);
        orderPushRiskDTO.setUserId(userId);
        //orderPushRiskDTO.setStatus();
        OrderBorrow orderBorrow = orderBorrowMapper.findByUserIdOrderId(orderPushRiskDTO);*/
        OrderRepayment orderRepayment = orderRepaymentMapper.selectByBorrowId(id);
        if(Objects.isNull(orderRepayment)){
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "已生成还款订单,不能删除");
        }
        orderBorrowMapper.deleteByPrimaryKey(id);
        InfoIndexInfo indexInfo = new InfoIndexInfo();
        indexInfo.setUserId(userId);
        indexInfo.setBorrowStatus("0");
        int count = infoIndexInfoDAO.updateIndexInfoByUserId(indexInfo);

        return count > 0 ? CallBackResult.ok("修改成功") : CallBackResult.fail();
    }

}
