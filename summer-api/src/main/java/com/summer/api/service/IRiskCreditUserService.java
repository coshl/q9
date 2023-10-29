package com.summer.api.service;


import com.summer.dao.entity.RiskCreditUser;
import com.summer.dao.entity.RiskRuleProperty;

import java.util.HashMap;
import java.util.List;

public interface IRiskCreditUserService {
    /**
     * 更新用户表芝麻分
     *
     * @param riskCreditUser 需要传入userId和zmScore
     * @return
     */
    int updateZmScore(RiskCreditUser riskCreditUser);

    /**
     * 获得所有的规则标识与征信对象的属性对应关系
     *
     * @param params
     * @return
     */
    List<RiskRuleProperty> findRuleProperty(
            HashMap<String, Object> params);

    /**
     * 更新同盾详情及更新时间
     *
     * @param riskCreditUser 需要传入userId、tdScore、tdPhoneBlack、tdCardNumBlack、tdMonth1Borrow、
     *                       tdDay7Borrow
     *                       、tdMonth1CardNumDeviceBorrow、tdDay7DeviceCardOrPhoneBorrow
     *                       、tdDay7CardDevice、tdMonth3ApplyCard、tdMonth3CardApply
     * @return
     */
    int updateTdDetail(RiskCreditUser riskCreditUser);

    /**
     * 更新白骑士黑名单情况及更新时间
     *
     * @param riskCreditUser 需要传入userId,bqsBack(1.通过；2.拒绝（命中黑名单）；3.建议人工审核(命中灰名单))
     * @return
     */
    int updateBqs(RiskCreditUser riskCreditUser);

    /**
     * 更细91征信数据及更新时间
     *
     * @param riskCreditUser 需要传入userId,jyLoanNum,jyJdNum,jyJdBl,jyOverNum,jyOverBl,,
     * @return
     */
    int updateJy(RiskCreditUser riskCreditUser);

    /**
     * 更新密罐数据及更新时间
     *
     * @param riskCreditUser 需要传入userId,userId,mgBlackScore, mgDay7Num,mgMonth1Num,mgBlack
     * @return
     */
    int updateMg(RiskCreditUser riskCreditUser);

    /**
     * 更新用户表聚信立分析数据及更新时间
     *
     * @param riskCreditUser 需要传入userId,jxlZjDkNum,jxlBjDkNum,
     *                       jxlYjHf,jxlLink2Days,jxlLink1days
     *                       ,jxlLink2Num,jxlLink1Num,jxlLink2Order
     *                       ,jxlLink1Order,jxlGjTs,jxl_ht_phone_num、jxlAmthNum
     * @return
     */
    int updateJxl(RiskCreditUser riskCreditUser);

    /**
     * 根据用户ID查询出现黑名单、逾期情况的记录数
     *
     * @param userId
     * @return
     */
    List<RiskCreditUser> findByUserId(Integer userId);

    /**
     * 调用所有征信接口并更新相关信息
     *
     * @param id      征信表主键ID，更新征信表使用
     * @param userId  用户表主键ID(更新用户表专用)
     * @param assetId 借款信息表的主键ID(订单表id)
     */
    void review(Integer id, Integer userId, Integer assetId);

    Integer findRiskStatus(Integer orderId);

}
