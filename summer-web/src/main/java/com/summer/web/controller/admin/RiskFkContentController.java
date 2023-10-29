package com.summer.web.controller.admin;


import com.summer.api.service.RiskFkContentService;
import com.summer.dao.entity.PlatformUser;
import com.summer.util.CallBackResult;
import com.summer.util.Constant;
import com.summer.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 风控相关
 * </p>
 *
 * @author jobob
 * @since 2020-07-30
 */
@RestController
@RequestMapping("/v1.0/api/riskFkContent")
public class RiskFkContentController extends BaseController {

    @Autowired
    private RiskFkContentService riskFkContentService;

    /**
     * 指迷风控分获取
     * @param userId 用户id
     */
    @GetMapping("/getRiskScore")
    public CallBackResult getScore(HttpServletRequest request, @RequestParam Integer userId) {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.fail(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        if (platformUser.getRoleId() != Constant.ROLEID_SUPER && platformUser.getRoleId() != Constant.ROLEID_REVIEWER) {
            return CallBackResult.fail(Constant.RESULT_BAD_STATUS, Constant.UN_AUTH_VIST);
        }
        return riskFkContentService.getScoreByUserId(userId);
    }
}
