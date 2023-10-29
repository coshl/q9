package com.summer.web.controller.BorrowRuleConfig;

import com.summer.web.controller.BaseController;
import com.summer.dao.entity.AuthRuleConfig;
import com.summer.dao.entity.PlatformUser;
import com.summer.api.service.IAuthRuleConfigService;
import com.summer.util.CallBackResult;
import com.summer.util.Constant;
import com.summer.util.StatusResultUtil;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 认证规则配置控制器
 */
@Validated
@RestController
@RequestMapping("/v1.0/api")
public class AuthRuleConfigController extends BaseController {

    @Resource
    private IAuthRuleConfigService authRuleConfigService;

    /**
     * 获取认证规则的列表
     *
     * @return
     */
    @GetMapping("/auth/rule")
    public Object getAuthRule(HttpServletRequest request) throws Exception {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        if (platformUser.getRoleId() != Constant.ROLEID_SUPER ) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.UN_AUTH_VIST);
        }

        List<AuthRuleConfig> allAuthRule = authRuleConfigService.findAllAuthRule();
        return CallBackResult.returnJson(CallBackResult.SUCCESS, CallBackResult.MSG_SUCCESS, allAuthRule);

    }

    /**
     * 更新认证规则
     *
     * @param
     * @return
     */
    @PutMapping("/update/auth/rule")
    public Object updateAuthRule(@NotNull(message = "认证规则的id不能为空") Integer id, @NotNull(message = "认证规则的状态不能为空") Integer status, HttpServletRequest request) throws Exception {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        if (platformUser.getRoleId() != Constant.ROLEID_SUPER ) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.UN_AUTH_VIST);
        }

        AuthRuleConfig authRuleConfig = new AuthRuleConfig();
        authRuleConfig.setId(id);
        byte state = Byte.parseByte(status.toString());
        authRuleConfig.setStatus(state);
        int isSucc = authRuleConfigService.updateByPrimaryKeySelective(authRuleConfig);
        if (isSucc > 0) {
            return StatusResultUtil.isSucc(state);
        }
        return StatusResultUtil.isBad(state);
    }

}
