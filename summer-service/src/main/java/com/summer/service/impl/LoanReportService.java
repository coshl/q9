package com.summer.service.impl;

import com.summer.dao.entity.LoanReport;
import com.summer.dao.mapper.LoanReportDAO;
import com.summer.dao.mapper.OrderBorrowMapper;
import com.summer.dao.mapper.OrderRepaymentMapper;
import com.summer.dao.mapper.UserInfoMapper;
import com.summer.api.service.ILoanReportService;
import com.summer.util.DateUtil;
import com.summer.pojo.vo.CountLoanByUserTypeVO;
import com.summer.pojo.vo.LoanReportVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * LoanReportService
 *
 * @author : GeZhuo
 * Date: 2019/2/25
 */
@Slf4j
@Service
public class LoanReportService implements ILoanReportService {

    @Resource
    private LoanReportDAO loanReportDAO;

    @Resource
    private OrderBorrowMapper orderBorrowMapper;
    @Resource
    private OrderRepaymentMapper orderRepaymentMapper;
    @Resource
    private UserInfoMapper userInfoMapper;

    @Override
    public List<LoanReportVO> findParams(Map<String, Object> params) {
        return loanReportDAO.findParams(params);
    }

    @Override
    public Map<String, Object> findLoanMoneySum(Map<String, Object> params) {
        return loanReportDAO.findLoanMoneySum(params);
    }

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public int statisticLoanReport(Integer number) {
        try {
            Long applyNum = orderBorrowMapper.statisticByDay(number);
            Long registerNum = userInfoMapper.statisticByDay(number);
            Map<String, Object> rep = orderRepaymentMapper.countByDay(number);
            List<CountLoanByUserTypeVO> vos = orderRepaymentMapper.countUserByDay(number);
            Date now = new Date();
            String beginTime = DateUtil.getDateFormat(now, "yyyy-MM-dd") + " 00:00:00";
            LoanReport loanReport = new LoanReport(Integer.parseInt(rep.get("num").toString()), Long.parseLong(rep.get("loanAmount").toString()),
                    simpleDateFormat.parse(beginTime), now, registerNum.intValue(), applyNum.intValue(), vos);
            //判断今日放款统计是否已存在
            LoanReport loanReportOld = loanReportDAO.selectByReportDate(DateUtil.getDateFormat(now, "yyyy-MM-dd"));
            if (loanReportOld != null && loanReportOld.getId() != null) {
                loanReport.setId(loanReportOld.getId());
                return loanReportDAO.updateByPrimaryKeySelective(loanReport);
            } else {
                return loanReportDAO.insertSelective(loanReport);
            }
        } catch (Exception e) {
            log.error("statisticLoan error", e.toString());
            return 0;
        }
    }

    @Override
    public Map<String, Object> getReport(String begin,String end) {
        return loanReportDAO.getReport(begin,end);
    }
}
