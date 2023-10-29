package com.summer.service.impl;

import com.summer.dao.entity.OrderCollectionReduction;
import com.summer.dao.entity.OrderRepaymentDetail;
import com.summer.dao.mapper.OrderCollectionReductionDAO;
import com.summer.dao.mapper.OrderRepaymentDetailDAO;
import com.summer.api.service.IOrderRepaymentDetailService;
import com.summer.pojo.vo.FinanceStatisticVO;
import com.summer.pojo.vo.PaymentDetailUserVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class OrderRepaymentDetailService implements IOrderRepaymentDetailService {

    @Resource
    private OrderRepaymentDetailDAO orderRepaymentDetailDAO;

    @Resource
    private OrderCollectionReductionDAO orderCollectionReductionDAO;

    @Override
    public int deleteByPrimaryKey(Long id) {
        return orderRepaymentDetailDAO.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(OrderRepaymentDetail record) {
        return orderRepaymentDetailDAO.insert(record);
    }

    @Override
    public int insertSelective(OrderRepaymentDetail record) {
        return orderRepaymentDetailDAO.insertSelective(record);
    }

    @Override
    public OrderRepaymentDetail selectByPrimaryKey(Long id) {
        return orderRepaymentDetailDAO.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(OrderRepaymentDetail record) {
        return orderRepaymentDetailDAO.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(OrderRepaymentDetail record) {
        return orderRepaymentDetailDAO.updateByPrimaryKey(record);
    }

    @Override
    public List<PaymentDetailUserVO> findParams(Map<String, Object> params) {
        List<PaymentDetailUserVO> paymentDetailUserVOs = orderRepaymentDetailDAO.findParams(params);
        for (PaymentDetailUserVO paymentUserVO : paymentDetailUserVOs) {
            paymentUserVO.setAuditStatus(Byte.parseByte("3"));
            OrderCollectionReduction byRepaymentId = orderCollectionReductionDAO.findByRepaymentId(paymentUserVO.getRepaymentId());
            if (null != byRepaymentId) {
                paymentUserVO.setAuditStatus(byRepaymentId.getAuditStatus());
                String remark = byRepaymentId.getRemark();
                if (null != remark) {
                    if (remark.contains("_")) {
                        String[] strings = remark.split("_");
                        if(null !=paymentUserVO.getRemark() && !"用户退款".equals(paymentUserVO.getRemark())){
                            paymentUserVO.setRemark(strings[0]);
                        }

                        String beforeRepayAmount = strings[1];
                        //上一次的应还金额
                        paymentUserVO.setBeforeRepayAmount(beforeRepayAmount);
                    }
                }
            }

            OrderCollectionReduction rderCollectionReduction = orderCollectionReductionDAO.findByRepayIdAsc(paymentUserVO.getRepaymentId());
            if (null != rderCollectionReduction) {
                String remarkStart = rderCollectionReduction.getRemark();
                if (null != remarkStart) {
                    if (remarkStart.contains("_")) {
                        String[] string = remarkStart.split("_");
                        //paymentUserVO.setRemark(strings[0]);
                        String beforeRepayAmount = string[1];
                        //最早的应还金额
                        paymentUserVO.setStartRepayAmount(beforeRepayAmount);
                    }
                }
            }
        }
        return paymentDetailUserVOs;
    }

    @Override
    public List<OrderRepaymentDetail> selectSimple(Map<String, Object> params) {
        return orderRepaymentDetailDAO.selectSimple(params);
    }

    @Override
    public List<FinanceStatisticVO> findStatistic(Map<String, Object> params) {
        return orderRepaymentDetailDAO.findStatistic(params);
    }

    @Override
    public List<Map<String, Object>> countStatistic() {
        return orderRepaymentDetailDAO.countStatistic();
    }

    @Override
    public void updatePaying(OrderRepaymentDetail orderRepaymentDetail) {
        orderRepaymentDetailDAO.updatePaying(orderRepaymentDetail);
    }
}
