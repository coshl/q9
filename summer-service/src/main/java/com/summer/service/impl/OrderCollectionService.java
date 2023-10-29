package com.summer.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.summer.dao.entity.*;
import com.summer.dao.mapper.*;
import com.summer.api.service.CollectedUserService;
import com.summer.api.service.IOrderCollectionService;
import com.summer.api.service.IUserInfoService;

import com.summer.pojo.vo.BackConfigParamsVo;
import com.summer.util.CallBackResult;
import com.summer.util.Constant;
import com.summer.util.DateUtil;
import com.summer.util.IdGen;
import com.summer.util.encrypt.AESDecrypt;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Desc:
 * Created by tl on 2019/1/5
 */
@Service
public class OrderCollectionService implements IOrderCollectionService {
    private static Logger log = LoggerFactory.getLogger(OrderCollectionService.class);
    @Resource
    private OrderRepaymentMapper orderRepaymentMapper;
    @Resource
    private OrderCollectionReductionDAO orderCollectionReductionDAO;
    @Resource
    private OrderCollectionDetailDAO orderCollectionDetailDAO;
    @Resource
    private OrderCollectionDAO orderCollectionDAO;
    @Resource
    private IMmanLoanCollectionRuleDao mmanLoanCollectionRuleDao;
    @Resource
    private PlatformUserMapper platformUserMapper;
    @Resource
    private IMmanLoanCollectionStatusChangeLogDao mmanLoanCollectionStatusChangeLogDao;
    @Resource
    private CollectedUserService collectedUserService;
    @Resource
    private IUserInfoService userInfoService;
    @Resource
    private OrderCollectionCallerDAO orderCollectionCallerDAO;
    @Resource
    private BackConfigParamsService backConfigParamsService;

    @Resource
    private UserBlackListDAO userBlackListDAO;


    @Override
    public void reduce(String remark, Integer userId, Integer collectionId, Integer repaymentId, int reductionAmount, Integer backId) {

        Date now = new Date();
        OrderCollectionReduction newReduction = new OrderCollectionReduction(collectionId, backId,
                userId, reductionAmount, Constant.COLLECTION_AUDIT_TODO, now, now, "", repaymentId);
        if (StringUtils.isNotBlank(remark)) {
            newReduction.setRemark(remark);
        }
        //新系统v0.2不需要审核(老系统需要保持一致)，所以默认为审核通过，之前的减免金额为现应还金额
        newReduction.setAuditStatus(Byte.parseByte("2"));
        orderCollectionReductionDAO.insertSelective(newReduction);

        /**直接修改应还款金额，不需要审核通过*/
        OrderRepayment orderRepayment = new OrderRepayment();
        orderRepayment.setRepaymentAmount(reductionAmount);
        orderRepayment.setId(repaymentId);
        orderRepaymentMapper.updateByPrimaryKeySelective(orderRepayment);

    }

    @Override
    @Transactional
    public void addDetail(Map<String, Object> params, OrderCollection orderCollection, Integer backId) {
        Byte contactType = params.get("contactType") == null ? 0 : Byte.parseByte(params.get("contactType").toString());
        Byte relation = params.get("relation") == null ? 0 : Byte.parseByte(params.get("relation").toString());
        Byte collectionType = params.get("collectionType") == null ? 0 :
                Byte.parseByte(params.get("collectionType").toString());
        Object promiseRepaymentTimeObj = params.get("promiseRepaymentTime");
        String remark = params.get("remark") == null ? "" : params.get("remark").toString();
        String names = params.get("names") == null ? "" : params.get("names").toString();
        String contactPhone = params.get("contactPhone") == null ? "" : params.get("contactPhone").toString();
        String contactName = params.get("contactName") == null ? "" : params.get("contactName").toString();

        Date now = new Date();
        Integer id = orderCollection.getId();
        OrderCollectionDetail newOrder = new OrderCollectionDetail(id, backId,
                orderCollection.getUserId(), contactType, contactName, relation, contactPhone, (byte) 0,
                orderCollection.getStatus(), collectionType, 0, now, now, names, remark);
        OrderCollection newCollection = new OrderCollection();
        newCollection.setId(id);
        newCollection.setLastCollectionTime(new Date());
        if (null != promiseRepaymentTimeObj) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date date = sdf.parse(promiseRepaymentTimeObj.toString());
                newCollection.setPromiseRepaymentTime(date);
            } catch (ParseException e) {
                log.error("OrderCollectionService addDetail dateParseError id=" + id);
            }
        }
        orderCollectionDAO.updateByPrimaryKeySelective(newCollection);
        orderCollectionDetailDAO.insertSelective(newOrder);
    }

    @Override
    public void dispatch(PlatformUser byPrimaryKey, OrderCollection orderCollection) {
        OrderCollection newCollection = new OrderCollection();
        newCollection.setId(orderCollection.getId());
        newCollection.setLastCollectionUserId(orderCollection.getCurrentCollectionUserId());
        newCollection.setCurrentCollectionUserId(byPrimaryKey.getId());
        newCollection.setCurrentOverdueLevel(Byte.parseByte(byPrimaryKey.getGroupLevel()));
        orderCollectionDAO.updateByPrimaryKeySelective(newCollection);

        //添加催收流转日志
        MmanLoanCollectionStatusChangeLog mmanLoanCollectionStatusChangeLog = new MmanLoanCollectionStatusChangeLog();
        mmanLoanCollectionStatusChangeLog.setId(IdGen.uuid());
        mmanLoanCollectionStatusChangeLog.setLoanCollectionOrderId(orderCollection.getOrderNo());
        mmanLoanCollectionStatusChangeLog.setOperatorName("系统");
        mmanLoanCollectionStatusChangeLog.setCreateDate(new Date());

        mmanLoanCollectionStatusChangeLog.setCurrentCollectionOrderLevel(byPrimaryKey.getGroupLevel());

        mmanLoanCollectionStatusChangeLog.setRemark(byPrimaryKey.getUserName() + "手动转派" + "，手机：" + byPrimaryKey.getPhoneNumber());

        mmanLoanCollectionStatusChangeLog.setCurrentCollectionUserId(byPrimaryKey.getId() + "");
        mmanLoanCollectionStatusChangeLog.setType(Constant.XJX_COLLECTION_STATUS_MOVE_TYPE_OTHER);//入催

        mmanLoanCollectionStatusChangeLogDao.insert(mmanLoanCollectionStatusChangeLog);
    }

    @Override
    public void assignCollectionOrderToRelatedGroup(
            List<OrderCollection> mmanLoanCollectionOrderList,
            List<MmanLoanCollectionPerson> mmanLoanCollectionPersonList, Date date) {

        if (CollectionUtils.isEmpty(mmanLoanCollectionOrderList)) {
            log.info("mman_loan_collection_order无记录");
            return;
        }
        if (CollectionUtils.isEmpty(mmanLoanCollectionPersonList)) {
            log.info("无催收人员可分配");
            return;
        }

        //2.1 查询当前组中所有非禁用的催收员，按照截止到当前手里未处理的订单数升序排序(前面已查)，并查出他们组每人每天单数上限(上限规则中公司+组唯一)，取出有效催收员
        List<MmanLoanCollectionRule> allRuleList = mmanLoanCollectionRuleDao.findList(new MmanLoanCollectionRule());
        HashMap<String, Integer> allRuleLimitCountMap = new HashMap<String, Integer>();
        if (CollectionUtils.isNotEmpty(allRuleList)) {
            for (MmanLoanCollectionRule ruleOri : allRuleList) {
                allRuleLimitCountMap.put(ruleOri.getCompanyId() + "_" + ruleOri.getCollectionGroup(), ruleOri.getEveryLimit());
            }
        }

        //开始分配前,先筛选出有效催收员(手里单子未超出上限的催收员),查询并设置每个催收员今日派到手里的订单数(包括已完成的)
        String currentCompanyGroup = "";//当前公司_组
        List<MmanLoanCollectionPerson> effectiveCollectionPersonList = new ArrayList<MmanLoanCollectionPerson>();
        for (MmanLoanCollectionPerson person : mmanLoanCollectionPersonList) {
            Integer todayAssignedCount = platformUserMapper.findTodayAssignedCount(person);//查询当前催收员今日派到手里的订单数(包括已完成的)
            person.setTodayAssignedCount(todayAssignedCount);
            Integer limitCount = allRuleLimitCountMap.get(person.getCompanyId() + "_" + person.getGroupLevel());//当前催收组每人每天上限
            currentCompanyGroup = person.getCompanyName() + "_" + Constant.groupNameMap.get(person.getGroupLevel());

            if (limitCount == null) {
                limitCount = 0;
            }

            // 如果催收规则无上限，则都加入
            if (limitCount == 0) {
                effectiveCollectionPersonList.add(person);
                continue;
            }

            if (todayAssignedCount.intValue() < limitCount.intValue()) {
                effectiveCollectionPersonList.add(person);
            }
        }

        //2.2 采用多次均匀涂抹法（将待分配订单数按排好序的催收员，依次分配，最后一次内层循环会优先分配给手里待处理单子少的）派单(最多循环次数：ceilAvgCount * effectivePersonCount)
        if (CollectionUtils.isEmpty(effectiveCollectionPersonList)) {

            log.error("当前" + currentCompanyGroup + "组所有催收员催收规则上限不足，请抓紧调整...");
        } else {

            int orderCount = mmanLoanCollectionOrderList.size();//待分配订单数
            int effectivePersonCount = effectiveCollectionPersonList.size();//当前可用催收员数
            int ceilAvgCount = new BigDecimal(orderCount).divide(new BigDecimal(effectivePersonCount), 0, BigDecimal.ROUND_CEILING).intValue();//平均订单数向上取整数

            int i = 0;//外层循环次数（ceilAvgCount）
            int j = 0;//已分配的订单数（最大为orderCount）
            while (i < ceilAvgCount) {
                for (int t = 0; t < effectivePersonCount; t++) {
                    MmanLoanCollectionPerson effectivePerson = effectiveCollectionPersonList.get(t);//当前催收员

                    //这里再实时查询当前催收员今日派到手里的订单数(包括已完成的)，防止每天第一次派单会超过上限，因为这个时候effectivePerson.getTodayAssignedCount().intValue()一直是0
                    Integer todayAssignedCount = platformUserMapper.findTodayAssignedCount(effectivePerson);
                    Integer limitCount = allRuleLimitCountMap.get(effectivePerson.getCompanyId() + "_" + effectivePerson.getGroupLevel());//当前催收组每人每天上限
                    if (limitCount == null) {
                        limitCount = 0;
                    }

                    // 当催收数量无催收规则上限时 即limitCount = 0
                    if (limitCount == 0) {
                        if (j < orderCount) {
                            OrderCollection order = mmanLoanCollectionOrderList.get(j);
                            try {
                                //派单方法：添加或更新催收订单、添加催收流转日志并更新还款状态
                                addOrUpdateOrderAndAddStatusChangeLogAndUpdatePayStatus(effectivePerson, order, date);
                            } catch (Exception e) {
                                log.error("分配当前催收任务出错，订单ID：" + order.getOrderNo(), e);
                            }
                            j++;
                        } else {
                            return;
                        }
                    }

                    if (todayAssignedCount.intValue() < limitCount.intValue()) {//可以分配
                        if (j < orderCount) {
                            OrderCollection order = mmanLoanCollectionOrderList.get(j);
                            try {
                                //派单方法：添加或更新催收订单、添加催收流转日志并更新还款状态
                                addOrUpdateOrderAndAddStatusChangeLogAndUpdatePayStatus(effectivePerson, order, date);
                            } catch (Exception e) {
                                log.error("分配当前催收任务出错，订单ID：" + order.getOrderNo(), e);
                            }
                            j++;
                        } else {//全部派单完成
                            return;
                        }
                    }
                }
                i++;
            }

            //最终订单数未分配完成，给一个通知
            if (j < orderCount) {
                log.error("当前" + currentCompanyGroup + "组，本次派单后出现催收规则上限不足，剩余" + (orderCount - j) + "单未派送，请及时调整...");
            }
        }
    }

    @Override
    public String updateCollectedUser(String jsonData) {
        Map<String, Object> params = JSONObject.parseObject(jsonData);
        //status 0拉黑，1取消拉黑
        if (null == params.get("phone") || null == params.get("status")) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "参数非法");
        }

        String phone = (String) params.get("phone");
        //逾期天数
        Object lateDay = params.get("lateDay");
        Integer overdue = null;
        if (null != lateDay) {
            overdue = Integer.parseInt(lateDay.toString());
        }
        //原因
        Object blackReason = params.get("blackReason");
        String reason = "";
        if (null != blackReason) {
            reason = blackReason.toString();
        }
        Integer status = (Integer) params.get("status");
        //新增或修改逾期黑名单记录
        collectedUserService.insertOrUpdateCollectedUser(params);
        //修改用户状态
        userInfoService.updateBlackUser(params);

        List<UserBlackList> userBlackLists = new ArrayList<>();

        //加入新建的黑名单表
        UserBlackList userBlackList = userBlackListDAO.findByPhone(phone);
        if (null == userBlackList) {
            UserBlackList userBlackListInfo = saveUserBlackInfo(phone, status, overdue);
            if (null != userBlackListInfo) {
                userBlackListInfo.setRemark(reason);
                userBlackListDAO.insertSelective(userBlackListInfo);
                userBlackListInfo.setId(null);
                userBlackLists.add(userBlackListInfo);
            }
        } else {

            UserBlackList userBlackListUp = new UserBlackList();
            //黑名单表的主键Id
            userBlackListUp.setId(userBlackList.getId());
            //逾期天数
            userBlackListUp.setOverdueDay(overdue);
            //原因
            userBlackListUp.setRemark(reason);
            //状态
            userBlackListUp.setStatus(status);
            userBlackListUp.setUpdateTime(new Date());
            userBlackListDAO.updateByPrimaryKeySelective(userBlackListUp);
            userBlackListUp.setId(null);
            userBlackLists.add(userBlackListUp);
        }
        /**商户添加黑名单，同时向数据中心推送*/
        //blackUserService.addBlackUser(userBlackLists);
        return CallBackResult.returnJson("操作成功");
    }

    @Override
    public void assignforWaiter(List<OrderCollectionCaller> orderCollectionCallers, List<PlatformUser> callers, Date now) {
        if (CollectionUtils.isEmpty(orderCollectionCallers)) {
            log.info("今日无还款订单可分配");
            return;
        }
        if (CollectionUtils.isEmpty(callers)) {
            log.info("无当日催收人员可分配");
            return;
        }
        //关闭自动分配
        BackConfigParamsVo zdht = backConfigParamsService.findBySysKey("zdfp");
        if (null != zdht) {
            //客户订单批量分配
            orderCollectionCallerDAO.insertBatchSelective(orderCollectionCallers);
            log.info("4.客户订单批量分配:{}条,{}", orderCollectionCallers.size(), DateUtil.formatTimeYmdHms(new Date()));
            return;
        }

        List<OrderCollectionCaller> orders = new ArrayList<>();
        List<OrderCollectionCaller> orderOlds = new ArrayList<>();
        //当日催收人员分配数量无上限
        Integer limitCount = 0;
        //2.2 采用多次均匀涂抹法（将待分配订单数按排好序的客服人员，依次分配，最后一次内层循环会优先分配给手里待处理单子少的）派单(最多循环次数：ceilAvgCount * effectivePersonCount)
        if (CollectionUtils.isEmpty(callers)) {
            log.error("所有当日催收人员催收规则上限不足，请抓紧调整...");
        } else {
            int orderCount = orderCollectionCallers.size();//待分配订单数
            int effectivePersonCount = callers.size();//当前可用当日催收人员数
            int ceilAvgCount = new BigDecimal(orderCount).divide(new BigDecimal(effectivePersonCount), 0, BigDecimal.ROUND_CEILING).intValue();//平均订单数向上取整数

            int i = 0;//外层循环次数（ceilAvgCount）
            int j = 0;//已分配的订单数（最大为orderCount）
            //老客不自动分配
            BackConfigParamsVo lkbfp = backConfigParamsService.findBySysKey("lkbfp");
            while (i < ceilAvgCount) {
                for (int t = 0; t < effectivePersonCount; t++) {
                    PlatformUser effectivePerson = callers.get(t);//当前当日催收人员

                    // 当催收数量无催收规则上限时 即limitCount = 0
                    if (limitCount == 0) {
                        if (j < orderCount) {
                            OrderCollectionCaller order = orderCollectionCallers.get(j);
                            try {
                                if (null != lkbfp) {
                                    int old=orderRepaymentMapper.orderId(order.getRepaymentId());
                                    if (old == 1) {
                                        orderOlds.add(order);
                                    } else {
                                        order.setLastCollectionUserId(effectivePerson.getId());
                                        order.setCurrentCollectionUserId(effectivePerson.getId());
                                        orders.add(order);
                                    }
                                } else {
                                    order.setLastCollectionUserId(effectivePerson.getId());
                                    order.setCurrentCollectionUserId(effectivePerson.getId());
                                    orders.add(order);
                                }
                            } catch (Exception e) {
                                log.error("分配当前当日催收人员催收任务出错，还款ID：" + order.getRepaymentId(), e);
                            }
                            j++;
                        } else {
                            //客户订单批量分配
                            if (null != lkbfp && orderOlds.size() >= 1) {
                                orderCollectionCallerDAO.insertBatchSelective(orderCollectionCallers);
                            }
                            if (orders.size() >= 1) {
                                orderCollectionCallerDAO.insertBatchSelective(orders);
                            }
                            log.info("2.客户订单批量分配:{}条，老客户订单不分配:{}条，时间：{}", orders.size(), orderOlds.size(), DateUtil.formatTimeYmdHms(new Date()));
                            return;
                        }
                    }
                }
                i++;
            }
            //客户订单批量分配
            orderCollectionCallerDAO.insertBatchSelective(orders);
            log.info("3.客户订单批量分配" + orders.size() + "条，", DateUtil.formatTimeYmdHms(new Date()));
            //最终订单数未分配完成，给一个通知
            if (j < orderCount) {
                log.error("本次派单后出现当日催收人员规则上限不足，剩余" + (orderCount - j) + "单未派送，请及时调整...");
            }
        }
    }

    private void addOrUpdateOrderAndAddStatusChangeLogAndUpdatePayStatus(MmanLoanCollectionPerson person,
                                                                         OrderCollection mmanLoanCollectionOrder, Date date) {

        //添加催收流转日志
        MmanLoanCollectionStatusChangeLog mmanLoanCollectionStatusChangeLog = new MmanLoanCollectionStatusChangeLog();
        mmanLoanCollectionStatusChangeLog.setId(IdGen.uuid());
        mmanLoanCollectionStatusChangeLog.setLoanCollectionOrderId(mmanLoanCollectionOrder.getOrderNo());
        mmanLoanCollectionStatusChangeLog.setOperatorName("系统");
        mmanLoanCollectionStatusChangeLog.setCreateDate(date);


        mmanLoanCollectionOrder.setCurrentCollectionUserId(Integer.parseInt(person.getId()));
        mmanLoanCollectionOrder.setCurrentOverdueLevel(Byte.parseByte(person.getGroupLevel()));


        //添加或更新催收订单
        //催收公司和状态这里统一设置或统一重置（升级的单子无论原来什么状态，这里都会重置！），根据当前分配到的催收员所在公司而定，状态为本公司待催收，委外公司委外中
        mmanLoanCollectionStatusChangeLog.setCurrentCollectionUserLevel(person.getGroupLevel());
        mmanLoanCollectionOrder.setOutsideCompanyId(Short.parseShort(person.getCompanyId()));
        if (1 == mmanLoanCollectionOrder.getOutsideCompanyId()) {
            mmanLoanCollectionOrder.setStatus(Constant.XJX_COLLECTION_ORDER_STATE_WAIT);
        } else {
            mmanLoanCollectionOrder.setStatus(Constant.XJX_COLLECTION_ORDER_STATE_OUTSIDE);
        }


        Integer id = Integer.parseInt(person.getId());
        if (Constant.XJX_OVERDUE_LEVEL_S1.equals(person.getGroupLevel())) {
            mmanLoanCollectionOrder.setM1ApproveId(id);
            mmanLoanCollectionOrder.setM1OperateStatus(Constant.COLLECTOR_OPERAT_STATUS_NO);
            mmanLoanCollectionStatusChangeLog.setCurrentCollectionOrderLevel(Constant.XJX_OVERDUE_LEVEL_S1);
        } else {

            if (Constant.XJX_OVERDUE_LEVEL_S2.equals(person.getGroupLevel())) {
                mmanLoanCollectionOrder.setM2ApproveId(id);
                mmanLoanCollectionOrder.setM2OperateStatus(Constant.COLLECTOR_OPERAT_STATUS_NO);
                mmanLoanCollectionStatusChangeLog.setCurrentCollectionOrderLevel(Constant.XJX_OVERDUE_LEVEL_S2);

            } else if (Constant.XJX_OVERDUE_LEVEL_M1_M2.equals(person.getGroupLevel())) {
                mmanLoanCollectionOrder.setM3ApproveId(id);
                mmanLoanCollectionOrder.setM3OperateStatus(Constant.COLLECTOR_OPERAT_STATUS_NO);
                mmanLoanCollectionStatusChangeLog.setCurrentCollectionOrderLevel(Constant.XJX_OVERDUE_LEVEL_M1_M2);

            } else if (Constant.XJX_OVERDUE_LEVEL_M2_M3.equals(person.getGroupLevel())) {
                mmanLoanCollectionOrder.setM4ApproveId(id);
                mmanLoanCollectionOrder.setM4OperateStatus(Constant.COLLECTOR_OPERAT_STATUS_NO);
                mmanLoanCollectionStatusChangeLog.setCurrentCollectionOrderLevel(Constant.XJX_OVERDUE_LEVEL_M2_M3);
            } else {
                mmanLoanCollectionOrder.setM5ApproveId(id);
                mmanLoanCollectionOrder.setM5OperateStatus(Constant.COLLECTOR_OPERAT_STATUS_NO);
                mmanLoanCollectionStatusChangeLog.setCurrentCollectionOrderLevel(Constant.XJX_OVERDUE_LEVEL_M3P);
            }
        }

        mmanLoanCollectionStatusChangeLog.setRemark(mmanLoanCollectionStatusChangeLog.getRemark() + ",催收组：" + Constant.groupNameMap.get(mmanLoanCollectionStatusChangeLog.getCurrentCollectionOrderLevel()));
        mmanLoanCollectionStatusChangeLog.setAfterStatus(mmanLoanCollectionOrder.getStatus() + "");
        mmanLoanCollectionStatusChangeLog.setCompanyId(mmanLoanCollectionOrder.getOutsideCompanyId() + "");


        if (null == mmanLoanCollectionOrder.getId()) {
            mmanLoanCollectionStatusChangeLog.setType(Constant.XJX_COLLECTION_STATUS_MOVE_TYPE_IN);//入催
            mmanLoanCollectionStatusChangeLog.setRemark("系统派单，催收人：" + person.getUsername() + "，手机：" + person.getPhone());
            mmanLoanCollectionStatusChangeLog.setCurrentCollectionUserId(person.getId());
            orderCollectionDAO.insertSelective(mmanLoanCollectionOrder);
        } else {
            mmanLoanCollectionStatusChangeLog.setBeforeStatus(mmanLoanCollectionOrder.getStatus() + "");
            mmanLoanCollectionStatusChangeLog.setType(Constant.XJX_COLLECTION_STATUS_MOVE_TYPE_CONVERT);//逾期等级转换
            mmanLoanCollectionStatusChangeLog.setRemark("逾期升级，系统重新派单,当前催收人：" + person.getUsername() + "，手机：" + person.getPhone());
            mmanLoanCollectionStatusChangeLog.setCurrentCollectionUserId(person.getId());
            orderCollectionDAO.updateByPrimaryKeySelective(mmanLoanCollectionOrder);
        }
        mmanLoanCollectionStatusChangeLogDao.insert(mmanLoanCollectionStatusChangeLog);
    }

    /**
     * 设置拉黑的用户信息
     *
     * @param phone
     * @return
     */
    public UserBlackList saveUserBlackInfo(String phone, Integer status, Integer overdue) {
        UserInfo userInfo = userInfoService.findByPhone(phone);
        UserBlackList userBlackList = null;
        if (null != userInfo) {
            userBlackList = new UserBlackList();
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
            //状态0黑名单，1移除黑名单

            userBlackList.setStatus(status);
            //逾期天数
            userBlackList.setOverdueDay(overdue);
        }
        return userBlackList;
    }
}
