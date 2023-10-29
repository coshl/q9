package com.summer.api.service;

import com.summer.dao.entity.MmanLoanCollectionPerson;
import com.summer.dao.entity.OrderCollection;
import com.summer.dao.entity.OrderCollectionCaller;
import com.summer.dao.entity.PlatformUser;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Desc:
 * Created by tl on 2019/1/5
 */
public interface IOrderCollectionService {
    /**
     * 创建减免订单
     *
     * @param reductionAmount
     */
    void reduce(String remark, Integer userId, Integer collectionId, Integer repaymentId, int reductionAmount, Integer backId);

    /**
     * 添加催收详情
     *
     * @param params
     * @param orderCollection
     */
    void addDetail(Map<String, Object> params, OrderCollection orderCollection, Integer backId);

    /**
     * 添加催收详情
     *
     * @param orderCollection
     */
    void dispatch(PlatformUser byPrimaryKey, OrderCollection orderCollection);

    /**
     * 分配催收订单给对应组的催收员
     *
     * @param mmanLoanCollectionOrderNo131List
     * @param mmanLoanCollectionPersonNo131List
     * @param now
     */
    void assignCollectionOrderToRelatedGroup(List<OrderCollection> mmanLoanCollectionOrderNo131List, List<MmanLoanCollectionPerson> mmanLoanCollectionPersonNo131List, Date now);

    /**
     * 加入/取消黑名单
     *
     * @param jsonData
     * @return
     */
    String updateCollectedUser(String jsonData);

    /**
     * 将订单派到对应分组当日催收人员
     *
     * @param orderCollectionCallers
     * @param callers
     * @param now
     */
    void assignforWaiter(List<OrderCollectionCaller> orderCollectionCallers, List<PlatformUser> callers, Date now);
}
