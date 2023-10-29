package com.summer.web.controller.dailystatistics;

import com.summer.web.controller.BaseController;
import com.summer.dao.entity.PlatformUser;
import com.summer.dao.entity.UserInfo;
import com.summer.api.service.channel.IChannelAsyncCountService;
import com.summer.api.service.channel.IChannelCountRepaymentService;
import com.summer.util.CallBackResult;
import com.summer.util.Constant;
import com.summer.util.DateUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@RestController
@RequestMapping("/v1.0/api")
public class ChannelReCountController extends BaseController {
    @Resource
    private IChannelAsyncCountService channelAsyncCountService;
    @Resource
    private IChannelCountRepaymentService channelCountRepaymentService;

    /**
     * 重新统计放款人数
     *
     * @param day
     * @param channelId
     * @return
     */
    @GetMapping("/loan/recount")
    public String loanRePush(Integer day, Integer channelId, HttpServletRequest request) {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        // 超管
        if (platformUser.getRoleId() != Constant.ROLEID_SUPER) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "无权操作");
        }
        Date date = new Date();
        //获取过去多少天的时间
        Date time = DateUtil.dateSubtraction(date, day);
        UserInfo userInfo = new UserInfo();
        userInfo.setChannelId(channelId);
        userInfo.setCreateTime(time);
        channelAsyncCountService.loanIsSuccCount(userInfo, date, 0);
        return CallBackResult.returnSuccessJson();
    }

    /**
     * @return
     */
    @GetMapping("/overdue/repush")
    public String overduePush(Integer day, HttpServletRequest request) {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        // 超管
        if (platformUser.getRoleId() != Constant.ROLEID_SUPER) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "无权操作");
        }
        Date date = new Date();
        channelCountRepaymentService.overdueTotal(date, day);
        return CallBackResult.returnSuccessJson();
    }

}
