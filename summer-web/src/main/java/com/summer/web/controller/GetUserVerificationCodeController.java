package com.summer.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.summer.dao.entity.PhoneCodeSend;
import com.summer.dao.mapper.PhoneCodeSendDAO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.summer.dao.entity.PlatformUser;
import com.summer.util.CallBackResult;
import com.summer.util.Constant;
import com.summer.util.RedisUtil;

@Validated
@RestController
@RequestMapping("/v1.0/api/verificationCode")
public class GetUserVerificationCodeController extends BaseController {

	@Resource
	private PhoneCodeSendDAO phoneCodeSendDAO;

	@GetMapping("/getUsersCodeByPhone")
	@ResponseBody
	public Object getUsersCodeByPhone(HttpServletRequest request,@RequestParam String phone,String flag) {
        /*PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        // 超管、渠道主管、渠道专员可以查询
        Integer roleId = platformUser.getRoleId();
        if (roleId != Constant.ROLEID_SUPER && roleId != Constant.CHANNEL_ADMIN_ROLE_ID && roleId != Constant.OPERATOR_ROLE_ID) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "没有权限");
        }*/
		if(null == phone || "".equals(phone)) {
			return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "请输入手机号码！");
		}
		//JSONObject json = new JSONObject();
		List<PhoneCodeSend> phoneCodeSendsList = phoneCodeSendDAO.selectCode(phone);
		//json.put("date", phoneCodeSendsList);
		return CallBackResult.returnJson(phoneCodeSendsList);
	}
}
