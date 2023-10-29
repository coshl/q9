package com.summer.service.impl;

import com.summer.dao.entity.OrderRenewal;
import com.summer.dao.mapper.OrderRenewalMapper;
import com.summer.api.service.IOrderRenewalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class OrderRenewalService implements IOrderRenewalService {
    @Resource
    private OrderRenewalMapper orderRenewalMapper;

    @Override
    public List<OrderRenewal> findByRepaymentId(Integer repaymentId) {
        return orderRenewalMapper.findByRepaymentId(repaymentId);
    }

    @Override
    public List<OrderRenewal> findByBorrowId(Integer borrowId) {
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("borrowId", borrowId);
            return orderRenewalMapper.selectSimple(param);
        } catch (Exception e) {
            log.error("查询账期记录失败:{}", e);
        }
        return null;
    }
}
