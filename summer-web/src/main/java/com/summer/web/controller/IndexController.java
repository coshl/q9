package com.summer.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.summer.dao.entity.IndexReport;
import com.summer.dao.entity.PlatformUser;
import com.summer.dao.mapper.*;
import com.summer.api.service.ITaskjobService;
import com.summer.service.impl.BackConfigParamsService;
import com.summer.util.*;
import com.summer.pojo.vo.IndexVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Desc:
 * Created by tl on 2019/3/25
 */
@RestController
@Slf4j
@RequestMapping("/v1.0/api/index")
public class IndexController {

    // 数据中心回调地址
    @Value("${billing.centerUrl}")
    private String centerUrl;
    @Value("${app.pid}")
    private String pid;

    @Resource
    private IndexReportDAO indexReportDAO;
    @Resource
    private UserInfoMapper userInfoMapper;
    @Resource
    private OrderRepaymentMapper orderRepaymentMapper;
    @Resource
    private OrderBorrowMapper orderBorrowMapper;
    @Resource
    private OrderRenewalMapper orderRenewalMapper;
    @Resource
    private ITaskjobService taskjobService;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private BackConfigParamsService backConfigParamsService;

    /**
     * 首页
     *
     * @return
     */
    @PostMapping("/statistic")
    public String statistic(HttpServletRequest request) {
        // 获取登录用户
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        if (platformUser.getRoleId() != Constant.ROLEID_BOSS_ADMIN && platformUser.getRoleId() != Constant.ROLEID_SUPER) {
            log.error("总览越权操作，phone：" + platformUser.getPhoneNumber());
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        Map<String, Object> params = new HashMap<>();
        params.put("startDate", DateUtil.getDateFormat(DateUtil.addDay(new Date(), -6), "yyyy-MM-dd"));
        List<IndexReport> list = indexReportDAO.selectSimple(params);
        IndexReport indexReport = taskjobService.statisticIndex(DateUtil.addDay(new Date(), 1), false);
        list.add(indexReport);
        IndexVO newVo = getINdexVO(params, 0);
        IndexVO oldVo = getINdexVO(params, 1);
        IndexVO allVo = getINdexVO(params, 2);

        Integer newVoApplyNum = newVo.getApplyNum();
        if (newVoApplyNum != 0) {
            newVo.setLoanedRate(new BigDecimal(newVo.getLoanedNum() * 100.0 / newVoApplyNum).setScale(2,
                    BigDecimal.ROUND_HALF_UP));

        }
        Integer loanedNum = oldVo.getApplyNum();
        if (loanedNum != 0) {
            oldVo.setLoanedRate(new BigDecimal(oldVo.getLoanedNum() * 100.0 / loanedNum).setScale(2,
                    BigDecimal.ROUND_HALF_UP));
        }
        Integer newVoLoanedNum = newVo.getLoanedNum();
        if (newVoLoanedNum != 0) {
            newVo.setPaidRate(new BigDecimal(newVo.getPaidNum() * 100.0 / newVoLoanedNum).setScale(2,
                    BigDecimal.ROUND_HALF_UP));

        }
        Integer paidNum = oldVo.getLoanedNum();
        if (paidNum != 0) {
            oldVo.setPaidRate(new BigDecimal(oldVo.getPaidNum() * 100.0 / paidNum).setScale(2,
                    BigDecimal.ROUND_HALF_UP));

        }
        Integer payNum = allVo.getPayNum();
        if (payNum != 0) {

            newVo.setPayRate(new BigDecimal(newVo.getPayNum() * 100.0 / payNum).setScale(2, BigDecimal.ROUND_HALF_UP));
            oldVo.setPayRate(new BigDecimal(100).subtract(newVo.getPayRate()));

        }
        Integer renewalNum = allVo.getRenewalNum();
        if (renewalNum != 0) {

            newVo.setRenewalRate(new BigDecimal(newVo.getRenewalNum() * 100.0 / renewalNum).setScale(2,
                    BigDecimal.ROUND_HALF_UP));
            oldVo.setRenewalRate(new BigDecimal(100).subtract(newVo.getRenewalRate()));
        }
        Integer overdueNum = allVo.getOverdueNum();
        if (overdueNum != 0) {
            newVo.setOverdueRate(new BigDecimal(newVo.getOverdueNum() * 100.0 / overdueNum).setScale(2,
                    BigDecimal.ROUND_HALF_UP));

            oldVo.setOverdueRate(new BigDecimal(100).subtract(newVo.getOverdueRate()));
        }
        
        int flag = indexReportDAO.selectFlag();
        BigDecimal money = new BigDecimal("0");
        if(flag ==  1) {
        	money = indexReportDAO.findTotalMoney();
        }
        if(flag == 2) {
        	money = indexReportDAO.findTotalMoney1();
        }
        Map<String, Object> res = new HashMap<>();
        res.put("list", list);
        res.put("new", newVo);
        res.put("old", oldVo);
        res.put("all", allVo);
        res.put("money",money);
        res.put("appId",backConfigParamsService.findBySysKey("appId"));
        return CallBackResult.returnJson(res);
    }

    @PostMapping("/statistic2")
    public String statistic2(HttpServletRequest request) {
        // 获取登录用户
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        if (platformUser.getRoleId() != Constant.ROLEID_BOSS_ADMIN && platformUser.getRoleId() != Constant.ROLEID_SUPER) {
            log.error("总览越权操作，phone：" + platformUser.getPhoneNumber());
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        Map<String, Object> params = new HashMap<>();
        Map<String, Object> res = new HashMap<>();

        Long total = userInfoMapper.countTotal();
        Long overdue = userInfoMapper.countOverdue();
        Long loanNum = userInfoMapper.countLoan();
        Long countRenewal = userInfoMapper.countRenewal();
//        Long due = userInfoMapper.countDue();
        res.put("reloanRate", 0);
        res.put("overdue", overdue);
        res.put("total", total);
        res.put("loan", loanNum);
        res.put("countRenewal", countRenewal);
        //放款成功的到期的人数
        Long maturityLoanNum = userInfoMapper.countMaturityLoan();
        if (0 != maturityLoanNum) {
            //首次复贷成功人数
            Long reloan = userInfoMapper.countReloan();
            Long renew = userInfoMapper.countRenew();
            Long reOrder = reloan + renew;
            //复贷率=首次复贷成功人数/放款成功的到期的人数
            //新规则 复贷率 = （复贷 + 续期） / 已还人数
            res.put("reloanRate", new BigDecimal(reOrder * 100.0 / maturityLoanNum).setScale(2, BigDecimal.ROUND_HALF_UP));
        }

        return CallBackResult.returnJson(res);
    }

    //@PostMapping("/statistic3")
    public String statistic3(HttpServletRequest request) {
        // 获取登录用户
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        if (platformUser.getRoleId() != Constant.ROLEID_BOSS_ADMIN && platformUser.getRoleId() != Constant.ROLEID_SUPER) {
            log.error("总览越权操作，phone：" + platformUser.getPhoneNumber());
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        Map<String, Object> params = new HashMap<>();


        Map<String, Object> res = new HashMap<>();


        params.put("customerType", 0);
        Long loanNewToday = orderRepaymentMapper.countToday(params);
        params.put("customerType", 1);
        Long loanOldToday = orderRepaymentMapper.countToday(params);
        res.put("loanNewToday", loanNewToday);
        res.put("loanOldToday", loanOldToday);
        // 风控余额
        res.put("balance", 0);
        // 查询费用中心余额，向费用中心发送get请求
        String str = HttpUtil.doGet(centerUrl + "/getBalance?pid=" + pid);
        if (StringUtils.isNotBlank(str) && JSONObject.parseObject(str).get("data") != null) {
            JSONObject resultMap = JSONObject.parseObject(str);
            JSONObject dataMap = (JSONObject) resultMap.get("data");
            res.put("balance", (BigDecimal) dataMap.get("balance"));
            // 更新缓存中的风控余额
            Object code = resultMap.get("code");
            if (null != code && "200".equals(code.toString())) {
                redisUtil.set(Constant.RISK_BALANCE + pid, JSONObject.toJSONString(dataMap));
            }
        }
        return CallBackResult.returnJson(res);
    }

    private IndexVO getINdexVO(Map<String, Object> params, int customerType) {
        params.clear();
        params.put("customerType", customerType);
        Map<String, Object> apply = orderBorrowMapper.countAll(params);
        Map<String, Object> loan = orderRepaymentMapper.countAll(params);
        Map<String, Object> renewal = orderRenewalMapper.countAll(params);
        params.put("over", "true");
        Map<String, Object> over = orderRepaymentMapper.countAll(params);
        params.remove("over");
        params.put("normal", "true");
        Map<String, Object> normal = orderRepaymentMapper.countAll(params);
        BigDecimal zero = BigDecimal.ZERO;
        IndexVO vo = new IndexVO(new BigDecimal(loan.get("loanAmount").toString()), zero,
                Integer.parseInt(loan.get("num").toString()), new BigDecimal(loan.get("paidAmount").toString()), zero,
                Integer.parseInt(loan.get("paidNum").toString()), new BigDecimal(renewal.get("amount").toString()), zero,
                Integer.parseInt(renewal.get("num").toString()), new BigDecimal(normal.get("toPay").toString()), zero,
                Integer.parseInt(normal.get("num").toString()), new BigDecimal(over.get("toPay").toString()), zero,
                Integer.parseInt(over.get("num").toString()), (byte) customerType, Integer.parseInt(apply.get("num").toString()));

        return vo;
    }

    /**
     * 从redis获取到登录对象（后台用户）
     *
     * @return
     */
    public PlatformUser redisPlatformUser(HttpServletRequest request) {
        //判断用户是否登录
        String token = request.getHeader("Authorization");
        PlatformUser platformUser = null;
        if (redisUtil.hasKey(Constant.TOKEN + token)) {
            String user = redisUtil.get(Constant.TOKEN + token);
            platformUser = JSONObject.parseObject(user, PlatformUser.class);
        }
        return platformUser;
    }
}
