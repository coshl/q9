package com.summer;

import com.alibaba.fastjson.JSONObject;
import com.summer.dao.entity.*;
import com.summer.dao.mapper.*;
import com.summer.api.service.channel.IChannelAsyncCountService;
import com.summer.enums.MobileAuthStatus;
import com.summer.service.impl.*;
import com.summer.api.service.risk.IFirstTrialCpsService;
import com.summer.service.impl.thirdpart.zhimi.HttpClientUtil;
import com.summer.service.mq.OrderProducer;
import com.summer.util.DateUtil;
import com.summer.util.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.*;

/**
 * @ClassName ： CountTest
 * @Description ：
 * @Author：
 * @Date ：2019/10/29 9:41
 * @Version ：V1.0
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SummerApplication.class})
@Slf4j

public class CountTest {
    @Resource
    OrderRepaymentService repaymentService;

    @Resource
    UserMoXieDataDAO userMoXieDataDAO;
    @Resource
    private OrderProducer orderProducer;
    @Resource
    IBackConfigParamsDao backConfigParamsDao;

    @Test
    public void tset() {
        // 原始数据
        String rawdata = null;
        // 报告数据
        String reportdata = null;
                /*获取通话记录（原数据）接口：
                http://[address]/api/crawler/records
                获取通话报告接口：
                http://[address]/api/crawler/report
                获取通话报告 url 接口：
                http://[address]/api/crawler/report_url
                获取通话记录 url 接口：
                http://[address]/api/crawler/data_url*/
        Map headers = new HashMap();
        //TODO 测试完之后更换正式账户
        headers.put("appId","8031307");
        headers.put("appKey","400f8951d15100a56797b3e6a599918a");
        Map body = new HashMap();
        body.put("token","934987c4ebd3476ba99c7549c03f13de");
        //原始数据
        String rawdataString = HttpClientUtil.postForm("http://xpapi.dongyafuwu.com/api/crawler/records",headers,body);
        JSONObject rawdataJsonObject = JSONObject.parseObject(rawdataString);
        rawdata = rawdataJsonObject.getString("data");
        //log.info("开始调用星盘运营商接口获取运营商rawdata数据:{}", rawdata);
        //报告拉取
        String reportdataString = HttpClientUtil.postForm("http://xpapi.dongyafuwu.com/api/crawler/report",headers,body);
        JSONObject reportdataJsonObject = JSONObject.parseObject(reportdataString);
        reportdata = reportdataJsonObject.getString("data");
        //log.info("开始调用星盘运营商接口获取运营商reportdata数据:{}", reportdata);
        // TODO 上传阿里云获得存储路径，格式：商户pid+userid+爬虫id+后缀名
        UserMoXieDataWithBLOBs updateDataPath = new UserMoXieDataWithBLOBs();
        updateDataPath.setId(5618);
        updateDataPath.setMxRaw(rawdata);
        updateDataPath.setMxReport(reportdata);
        updateDataPath.setMxAuthStatus(MobileAuthStatus.AUTH_SUCCESS.getValue());
        userMoXieDataDAO.updateByPrimaryKeySelective(updateDataPath);
    }
    @Autowired
    LoanReportService loanReportService;
    @Test
    public void test2() {
        loanReportService.getReport("2","2");
    }

    @Resource
    private OrderRepaymentService orderRepaymentService;

    @Test
    public void test8() {
        UserInfo userInfo = new UserInfo();
        OrderBorrow orderBorrow = new OrderBorrow();
        OrderRepayment orderRepayment = new OrderRepayment();

        userInfo.setPhone("18822986645");
        userInfo.setRealName("晓明");
        orderBorrow.setApplyAmount(300000);
        orderBorrow.setCreateTime(new Date());

        orderRepayment.setPaidAmount(300000);
        orderRepayment.setPaidTime(new Date());
        orderRepaymentService.pushWhite(userInfo, orderBorrow, orderRepayment);
    }

    @Test
    public void test9() {
        String result = HttpUtil.doGet("http://localhost:8086/v1.0/api/user/findWhiteUser?phone=18822986645");
        System.out.println(result);
    }

    @Resource
    private OrderRenewalMapper orderRenewalMapper;

    @Test
    public void test10() {
        Date now = new Date();
        String firstRepaymentTime = DateUtil.getDateFormat(now, "yyyy-MM-dd");
        Integer renewalNumber = orderRenewalMapper.countRenewalNum(firstRepaymentTime);
        //续期金额 (当日续期金额=当日续期成功订单利息之和+当日续期手续费之和)
        Long renewalServiceFee = orderRenewalMapper.countRenewalServiceFee(firstRepaymentTime);
        Long renewalAmount = orderRenewalMapper.countRenewalFree(firstRepaymentTime) + renewalServiceFee;
    }

    @Resource
    private ReportRepaymentDAO reportRepaymentDAO;

    @Test
    public void test11() {
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
    }

    @Test
    public void test12() throws Exception {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id_no", "420821199001105001");
        jsonObject.put("name", "柳书兵");
        jsonObject.put("mobilePhone", "13738034183");
        jsonObject.put("pid", "10001");
        jsonObject.put("userId", 28489);

        String s = HttpUtil.doGet("http://116.62.65.37:8086/v1.0/api/risk/afuFraud?id_no=420117199304062318&name=李盼&mobilePhone=18672330798&pid=10001&userId=28501");
        JSONObject parseObject = JSONObject.parseObject(s);
        System.out.println(JSONObject.parseObject(parseObject.getString("data")));
        System.out.println(JSONObject.parseObject(s));
    }

    @Resource
    IFirstTrialCpsService firstTrialCpsService;
    @Resource
    OrderBorrowMapper orderBorrowMapper;

    @Test
    public void test13() {
        OrderBorrow orderBorrow = orderBorrowMapper.selectByPrimaryKey(28);
        String s = JSONObject.toJSONString(orderBorrow);
        firstTrialCpsService.riskFirstTrial(s);
    }

    @Resource
    private RiskCreditUserService riskCreditUserService;
    @Test
    public void test14() {
        riskCreditUserService.reviewMq(String.valueOf(2));
    }

    @Test
    public void  test18()
    {
        repaymentService.upUserLimit(333);
    }
}
