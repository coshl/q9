package com.summer.web.controller.front;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.summer.web.controller.BaseController;
import com.summer.dao.entity.IpAddressLog;
import com.summer.dao.entity.MessagePush;
import com.summer.dao.entity.PlatformUser;
import com.summer.group.AddMessagePush;
import com.summer.group.GetMessagePush;
import com.summer.pojo.dto.MessagePushDTO;
import com.summer.api.service.IIpAddressLogService;
import com.summer.api.service.IMessagePushService;
import com.summer.util.CallBackResult;
import com.summer.util.Constant;
import com.summer.util.IPUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author ls
 * @Title: 消息推送
 * @date 2019/2/19 10:09
 */
@RestController
@RequestMapping("/v1.0/api/manage")
public class MessagePushController extends BaseController {
    @Resource
    private IMessagePushService iMessagePushService;
    @Resource
    private IIpAddressLogService iIpAddressLogService;

    /**
     * 添加消息推送
     *
     * @return
     */
    @PostMapping("/addMessagePush")
    public String addMessagePush(@Validated(AddMessagePush.class) @RequestBody MessagePushDTO messagePushDTO, HttpServletRequest request) {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "参数非法");
        }

        // 超管
        Integer roleId = platformUser.getRoleId();
        if (roleId != Constant.ROLEID_SUPER ) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "没有权限");
        }
        MessagePush record = new MessagePush();
        record.setObject(messagePushDTO.getObject());
        record.setTitle(messagePushDTO.getTitle());
        record.setAddPerson(platformUser.getUserName());
        record.setType(messagePushDTO.getType());
        record.setContent(messagePushDTO.getContent());
        int insert = iMessagePushService.insert(record);
        if (insert > 0) {
            return CallBackResult.returnSuccessJson();
        }
        return CallBackResult.returnJson(CallBackResult.ABRUPT_ABNORMALITY, CallBackResult.MSG_ERROR);
    }

    /**
     * 删除消息推送
     *
     * @return
     */
    @GetMapping("/delMessagePush")
    public String delMessagePush(Integer id,HttpServletRequest request) {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "参数非法");
        }

        // 超管
        Integer roleId = platformUser.getRoleId();
        if (roleId != Constant.ROLEID_SUPER ) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "没有权限");
        }
        int i = iMessagePushService.deleteByPrimaryKey(id);
        if (i > 0) {
            return CallBackResult.returnSuccessJson();
        }
        return CallBackResult.returnJson(CallBackResult.ABRUPT_ABNORMALITY, CallBackResult.MSG_ERROR);
    }


    /**
     * 根据条件查询
     *
     * @param messagePushDTO
     * @return
     */
    @PostMapping("/getMessagePush")
    public String getInformationManagement(@Validated(GetMessagePush.class) @RequestBody MessagePushDTO messagePushDTO) {
        Map<String, Object> map = new HashMap<>();
        map.put("title", messagePushDTO.getTitle());
        map.put("content", messagePushDTO.getContent());
        map.put("addPerson", messagePushDTO.getAddPerson());
        map.put("type", messagePushDTO.getType());
        map.put("startTime", messagePushDTO.getStartTime());
        map.put("endTime", messagePushDTO.getEndTime());
        map.put("pageSize", messagePushDTO.getPageSize());
        map.put("pageNum", messagePushDTO.getPageNum());
        PageHelper.startPage(map);
        List<MessagePush> messagePushList = iMessagePushService.selectByParams(map);
        return CallBackResult.returnJson(new PageInfo<>(messagePushList));
    }


    /**
     * 访问app,做pv、uv使用
     *
     * @param request
     */
    @GetMapping("/visit")
    public String addressCount(HttpServletRequest request) throws Exception {
        Date nowTime = new Date();
        //插入访问日志
        IpAddressLog ipAddressLog = new IpAddressLog();
        ipAddressLog.setUrl(Constant.PU_UV_APP);
        ipAddressLog.setCreateTime(nowTime);
        ipAddressLog.setIp(IPUtils.getIpAddr(request));
        iIpAddressLogService.insertSelective(ipAddressLog);

        return CallBackResult.returnJson(CallBackResult.SUCCESS, Constant.FLUSH_IS_SUCC);
    }
}
