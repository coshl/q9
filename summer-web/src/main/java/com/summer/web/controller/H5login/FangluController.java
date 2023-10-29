package com.summer.web.controller.H5login;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.common.util.Md5Utils;
import com.summer.api.service.*;
import com.summer.api.service.channel.IChannelAsyncCountService;
import com.summer.dao.entity.InfoIndexInfo;
import com.summer.dao.entity.IpAddressLog;
import com.summer.dao.entity.PlateformChannel;
import com.summer.dao.entity.UserInfo;
import com.summer.dao.mapper.IBackConfigParamsDao;
import com.summer.dao.mapper.PlateformChannelMapper;
import com.summer.pojo.dto.H5RegisterDto;
import com.summer.pojo.vo.BackConfigParamsVo;
import com.summer.util.*;
import com.summer.util.log.StringUtils;
import com.summer.web.controller.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 落地页H5登录
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/v1.0/api")
public class FangluController extends BaseController {
    @Resource
    RedisUtil redisUtil;
    @Resource
    PlateformChannelMapper plateformChannelMapper;
    @Value("${config.channelBlackLikn}")
    private String blackLink;
    static String channelCode =  "channel_code_";

    /**
     * 防鲁跳转
     *
     * @return
     */
    @GetMapping("/wall/{code}")
    public void login(HttpServletRequest request, HttpServletResponse response, @PathVariable String code) throws Exception {

        PlateformChannel channel = getChannel(code);
        //sing =  md5+时间戳+uuid
        String sign = Md5Utils.getMD5("暂时预留".getBytes()) + "&" + Calendar.getInstance().getTimeInMillis() / 1000 + "&" + UUID.randomUUID();

        if (channel == null)
            response.sendRedirect(blackLink + "&sign=" +  Base64Util.encode(sign));
        else
            response.sendRedirect(channel.getLink() + "&sign=" + Base64Util.encode(sign));
    }

    private PlateformChannel getChannel(String code) {
       String tempCode = channelCode+code;
        PlateformChannel channel = null;
        if (redisUtil.hasKey(tempCode)) {
            JSONObject jsonObject = (JSONObject) redisUtil.getObject(tempCode);
            channel = JSONObject.toJavaObject(jsonObject, PlateformChannel.class);
        } else {
            channel = plateformChannelMapper.findByChannelCode(code);
            if (channel != null)
                redisUtil.set(tempCode, JSON.toJSON(channel),60*60*24);
        }
        return channel;
    }


}
