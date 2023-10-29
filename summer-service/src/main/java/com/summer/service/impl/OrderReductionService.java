package com.summer.service.impl;

import com.summer.dao.entity.OrderCollection;
import com.summer.dao.entity.OrderCollectionReduction;
import com.summer.dao.entity.OrderRepayment;
import com.summer.dao.mapper.OrderCollectionDAO;
import com.summer.dao.mapper.OrderCollectionReductionDAO;
import com.summer.dao.mapper.OrderRepaymentMapper;
import com.summer.api.service.IOrderReductionService;
import com.summer.util.Constant;
import com.summer.util.changjiepay.ChanPayUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Desc:
 * Created by tl on 2019/1/5
 */
@Service
public class OrderReductionService implements IOrderReductionService {
    @Resource
    private OrderCollectionReductionDAO orderCollectionReductionDAO;
    @Resource
    private OrderCollectionDAO orderCollectionDAO;
    @Resource
    private OrderRepaymentMapper orderRepaymentMapper;
    @Resource
    private OrderRepaymentService orderRepaymentService;

    @Override
    @Transactional
    public void reductConfirm(Map<String, Object> params, OrderCollectionReduction orderCollectionReduction, Integer platformUserId) {
        if (orderCollectionReduction.getCollectionId() == 0) {
            //非催收减免金额
            reductRepay(params, orderCollectionReduction, platformUserId);
            return;
        }
        OrderCollection orderCollection =
                orderCollectionDAO.selectByPrimaryKey(orderCollectionReduction.getCollectionId());
        Byte auditStatus = params.get("auditStatus") == null ? (byte) 0 :
                Byte.parseByte(params.get("auditStatus").toString());
        OrderCollectionReduction newReduction = new OrderCollectionReduction();
        OrderRepayment orderRepayment = orderRepaymentMapper.selectByPrimaryKey(orderCollection.getRepaymentId());
        OrderRepayment repayment = new OrderRepayment();
        repayment.setId(orderRepayment.getId());
        OrderCollection newOrder = new OrderCollection();
        newOrder.setId(orderCollection.getId());
        newReduction.setId(orderCollectionReduction.getId());
        if (Constant.COLLECTION_AUDIT_PASS == auditStatus) {
            newReduction.setAuditStatus(Constant.COLLECTION_AUDIT_PASS);
            Integer reductionAmount = orderCollectionReduction.getReductionAmount();
            newOrder.setReductionMoney(orderCollection.getReductionMoney() + reductionAmount);
            repayment.setReduceAmount(orderRepayment.getReduceAmount() + reductionAmount);
            orderCollectionDAO.updateByPrimaryKeySelective(newOrder);
            orderRepaymentMapper.updateByPrimaryKeySelective(repayment);
            if (orderRepayment.getRepaymentAmount() - orderRepayment.getPaidAmount() - repayment.getReduceAmount() == 0) {
                orderRepaymentService.payOffline(orderRepayment, null, "减免到0", ChanPayUtil.generateOutTradeNo(), null, reductionAmount,
                        platformUserId);
            }

        } else if (Constant.COLLECTION_AUDIT_REJECT == auditStatus) {
            newReduction.setAuditStatus(Constant.COLLECTION_AUDIT_REJECT);
        }
        Object remarkObj = params.get("remark");
        if (remarkObj != null) {
            newReduction.setRemark(remarkObj.toString());
        }
        orderCollectionReductionDAO.updateByPrimaryKeySelective(newReduction);

    }

    @Override
    @Transactional
    public void reductRepay(Map<String, Object> params, OrderCollectionReduction orderCollectionReduction, Integer platformUserId) {
        OrderRepayment orderRepayment =
                orderRepaymentMapper.selectByPrimaryKey(orderCollectionReduction.getRepaymentId());
        Byte auditStatus = params.get("auditStatus") == null ? (byte) 0 :
                Byte.parseByte(params.get("auditStatus").toString());
        OrderCollectionReduction newReduction = new OrderCollectionReduction();
        OrderRepayment repayment = new OrderRepayment();
        repayment.setId(orderRepayment.getId());
        newReduction.setId(orderCollectionReduction.getId());
        if (Constant.COLLECTION_AUDIT_PASS == auditStatus) {
            newReduction.setAuditStatus(Constant.COLLECTION_AUDIT_PASS);
            Integer reductionAmount = orderCollectionReduction.getReductionAmount();
            repayment.setReduceAmount(orderRepayment.getReduceAmount() + reductionAmount);
            orderRepaymentMapper.updateByPrimaryKeySelective(repayment);
            if (orderRepayment.getRepaymentAmount() - orderRepayment.getPaidAmount() - repayment.getReduceAmount() == 0) {
                orderRepaymentService.payOffline(orderRepayment, null, "减免到0", ChanPayUtil.generateOutTradeNo(), null, reductionAmount,
                        platformUserId);
            }
        } else if (Constant.COLLECTION_AUDIT_REJECT == auditStatus) {
            newReduction.setAuditStatus(Constant.COLLECTION_AUDIT_REJECT);
        }
        Object remarkObj = params.get("remark");
        if (remarkObj != null) {
            newReduction.setRemark(remarkObj.toString());
        }
        orderCollectionReductionDAO.updateByPrimaryKeySelective(newReduction);

    }
}
