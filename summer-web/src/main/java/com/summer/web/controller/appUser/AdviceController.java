package com.summer.web.controller.appUser;

import com.summer.web.controller.BaseController;
import com.summer.dao.entity.Advice;
import com.summer.dao.entity.UserInfo;
import com.summer.group.SaveAdvice;
import com.summer.pojo.dto.SaveAdviceDTO;
import com.summer.api.service.IAdviceService;
import com.summer.util.CallBackResult;
import com.summer.util.Constant;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author ls
 * @Title: 意见反馈
 * @date 2019/3/18 10:11
 */
@RestController
@RequestMapping("/v1.0/api/user")
public class AdviceController extends BaseController {
    @Resource
    private IAdviceService iAdviceService;


    /**
     * 保存app意见反馈
     *
     * @param
     * @return
     */
    @PostMapping("/saveAdvice")
    public String saveAdvice(@Validated(SaveAdvice.class)
                             @RequestBody SaveAdviceDTO saveAdviceDTO, HttpServletRequest request) {
        UserInfo userInfo = redisUser(request);
        if (null == userInfo){
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST,Constant.USER_MSG_NOT_EXIST);
        }


        Advice record = new Advice();
        record.setDeviceId(saveAdviceDTO.getDeviceId());
        record.setAdviceInfo(saveAdviceDTO.getAdviceInfo());
        record.setOsVersion(saveAdviceDTO.getOsVersion());
        String userPhone = saveAdviceDTO.getUserPhone();
        if (userInfo != null) {
            userPhone = userInfo.getPhone();
        } else {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "账号异常");
        }
        record.setUserPhone(userPhone);
        record.setDeviceName(saveAdviceDTO.getDeviceName());
        record.setAppVersion(saveAdviceDTO.getAppVersion());
        int i = iAdviceService.insertSelective(record);
        if (i > 0) {
            return CallBackResult.returnJson(CallBackResult.SUCCESS, Constant.ONLINE_FEED_SUCCESS);
        }
        return CallBackResult.returnErrorJson();
    }
}
