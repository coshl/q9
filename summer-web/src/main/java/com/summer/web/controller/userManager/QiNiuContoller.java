package com.summer.web.controller.userManager;

import com.qiniu.util.Auth;
import com.summer.web.controller.BaseController;
import com.summer.dao.entity.PlatformUser;
import com.summer.dao.entity.UserInfo;
import com.summer.util.CallBackResult;
import com.summer.util.Constant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 七牛图片上传的获取token控制器
 */
@RestController
@RequestMapping("/v1.0/api/qiniu")
public class QiNiuContoller extends BaseController {
    @Value("${qiniu.accessKey}")
    private String accessKey;
    @Value("${qiniu.secretKey}")
    private String secretKey;
    @Value("${qiniu.bucket}")
    private String bucket;
    @Value("${qiniu.pathPrefix}")
    private String pathPrefix;

    /**
     * APP 端生成上传七牛图片的token
     *
     * @param request
     * @return
     */
    @GetMapping("/token")
    public String qiniutoken(HttpServletRequest request) {
        UserInfo userInfo = redisUser(request);
        if (null != userInfo) {
            Auth auth = Auth.create(accessKey, secretKey);
            String token = auth.uploadToken(bucket);
            if (null != token) {
                Map<String, Object> tokenMap = new HashMap<>();
                tokenMap.put("token", token);
                tokenMap.put("userId", userInfo.getId());
                return CallBackResult.returnJson(CallBackResult.SUCCESS, CallBackResult.MSG_SUCCESS, tokenMap);
            }
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "获取失败token");
        }
        return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
    }

    /**
     * APP 端生成上传七牛图片的token
     *
     * @param request
     * @return
     */
    @GetMapping("/tokenpc")
    public String qiniutokenBack(HttpServletRequest request) {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        Auth auth = Auth.create(accessKey, secretKey);
        String token = auth.uploadToken(bucket);
        if (null != token) {
            Map<String, Object> tokenMap = new HashMap<>();
            tokenMap.put("token", token);
            return CallBackResult.returnJson(CallBackResult.SUCCESS, CallBackResult.MSG_SUCCESS, tokenMap);
        }
        return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "获取失败token");

    }

}
