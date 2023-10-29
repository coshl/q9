package com.summer.web.controller;

import com.summer.dao.entity.Configuration;
import com.summer.dao.mapper.ConfigurationMapper;
import com.summer.util.CallBackResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author ls
 * @version V1.0
 * @Title:
 * @Description: 借款利率
 * @date 2019/1/1 14:26
 */
@Slf4j
@RestController
@RequestMapping("/v1.0/api/user")
public class BorrowFeeController extends BaseController {
    @Resource
    private ConfigurationMapper configurationMapper;

    /**
     * 查看借款利率
     *
     * @return
     */
    @ResponseBody
    @PostMapping("/borrowFee")
    public String queryBorrowFeeWithUser() {
        Configuration configuration = configurationMapper.selectByPrimaryKey(1);
        HashMap<String, Object> hashMap = new HashMap<>();
        if (null != configuration) {
            hashMap.put("amount", configuration.getBorrowAmount());
            hashMap.put("borrowPeriod", configuration.getBorrowPeriod());
            hashMap.put("inquire", configuration.getInquire());
            hashMap.put("borrowInterest", configuration.getBorrowInterest());
            hashMap.put("accountManager", configuration.getAccountManagement());
            hashMap.put("actualPay", configuration.getActualPay());
        }
        return CallBackResult.returnJson(hashMap);
    }
}
