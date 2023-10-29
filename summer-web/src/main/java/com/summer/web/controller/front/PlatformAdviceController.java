package com.summer.web.controller.front;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.summer.web.controller.BaseController;
import com.summer.dao.entity.Advice;
import com.summer.dao.entity.PlatformUser;
import com.summer.group.GetAdviceByParams;
import com.summer.group.HandleFeedbackInfo;
import com.summer.dao.mapper.AdviceDAO;
import com.summer.pojo.dto.AdviceDTO;
import com.summer.util.CallBackResult;
import com.summer.util.Constant;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ls
 * @Title: 意见反馈
 * @date 2019/1/20 17:56
 */
@RestController
@RequestMapping("/v1.0/api/user")
public class PlatformAdviceController extends BaseController {
    @Resource
    private AdviceDAO adviseDAO;

    /**
     * 根据查询条件查找建议信息
     *
     * @return
     */
    @PostMapping("/getAdviceByParams")
    public String getAdviceInfoByParams(@Validated(GetAdviceByParams.class) @RequestBody AdviceDTO adviceDTO, HttpServletRequest request) {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        // 超管，客服
        if (Constant.ROLEID_SUPER != platformUser.getRoleId() && Constant.ROLEID_WAITER != platformUser.getRoleId()) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, "没有该权限");
        }
        Map<String, Object> map = new HashMap<>();
        map.put("userPhone", adviceDTO.getPhoneNumber());
        map.put("startTime", adviceDTO.getStartTime());
        map.put("endTime", adviceDTO.getEndTime());
        map.put("handleStatus", adviceDTO.getIsHandle());
        map.put("pageSize", adviceDTO.getPageSize());
        map.put("pageNum", adviceDTO.getPageNum());
        PageHelper.startPage(map);
        List<Advice> list = adviseDAO.selectByParams(map);
        return CallBackResult.returnJson(new PageInfo<>(list));
    }

    /**
     * 通过ID获取用户单条意见反馈信息
     *
     * @param id
     * @return
     */
    @GetMapping("/getFeedbackInfoById")
    public String getFeedbackInfoById(HttpServletRequest request, Integer id) {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        // 超管，客服
        if (Constant.ROLEID_SUPER != platformUser.getRoleId() && Constant.ROLEID_WAITER != platformUser.getRoleId()) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, "没有该权限");
        }
        Advice advice = adviseDAO.selectByPrimaryKey(id);
        Map<String, Object> map = new HashMap<>();
        if (advice != null) {
            map.put("userPhone", advice.getUserPhone());
            map.put("handlePerson", advice.getHandlePerson());
            map.put("handleTime", advice.getHandleDate());
            map.put("feedbackContent", advice.getAdviceInfo());
        }
        return CallBackResult.returnJson(map);
    }

    /**
     * 根据ID处理用户反馈信息
     *
     * @return
     */
    @PostMapping("/handleFeedbackInfo")
    public String handleFeedbackInfo(@Validated(HandleFeedbackInfo.class) @RequestBody AdviceDTO adviceDTO, HttpServletRequest request) {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, "参数非法");
        }
        // 超管，客服
        if (Constant.ROLEID_SUPER != platformUser.getRoleId() && Constant.ROLEID_WAITER != platformUser.getRoleId()) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, "没有该权限");
        }
        Advice advice = adviseDAO.selectByPrimaryKey(adviceDTO.getId());
        advice.setHandleStatusInfo(adviceDTO.getHandleStatusInfo());
        advice.setHandlePerson(platformUser.getUserName());
        advice.setHandleStatus(1);
        int update = adviseDAO.updateByPrimaryKey(advice);
        if (update > 0) {
            return CallBackResult.returnJson(CallBackResult.SUCCESS, CallBackResult.MSG_SUCCESS);
        }
        return CallBackResult.returnJson(CallBackResult.ABRUPT_ABNORMALITY, CallBackResult.MSG_ERROR);
    }

}
