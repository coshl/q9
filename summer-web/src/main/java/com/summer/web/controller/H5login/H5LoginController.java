package com.summer.web.controller.H5login;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.common.util.Md5Utils;
import com.summer.api.service.*;
import com.summer.pojo.vo.AppVersionInfoVo;
import com.summer.web.controller.BaseController;
import com.summer.dao.entity.InfoIndexInfo;
import com.summer.dao.entity.IpAddressLog;
import com.summer.dao.entity.PlateformChannel;
import com.summer.dao.entity.UserInfo;
import com.summer.dao.mapper.IBackConfigParamsDao;
import com.summer.pojo.dto.H5RegisterDto;
import com.summer.api.service.channel.IChannelAsyncCountService;
import com.summer.util.*;
import com.summer.util.log.StringUtils;
import com.summer.pojo.vo.BackConfigParamsVo;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 落地页H5登录
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/v1.0/api/h5")
public class H5LoginController extends BaseController {

    @Resource
    private RedisUtil redisUtil;
    @Autowired
    private IUserInfoService userInfoService;
    @Resource
    private SourceUserLoginInfoService sourceUserLoginInfoService;
    @Resource
    private IAppVersionInfoService appVersionInfoService;
    @Resource
    private IInfoIndexInfoService infoIndexInfoService;
    @Resource
    private IPlateformChannelService plateformChannelService;
    @Resource
    private IChannelAsyncCountService channelAsyncCountService;
    @Resource
    private IIpAddressLogService iIpAddressLogService;

    @Resource
    private IBackConfigParamsDao backConfigParamsDao;

    public static  final String CHANNEL_SWITCH = "CHANNEL_SWITCH";

    /**
     * h5登录注册
     *
     * 目前只使用这一个 2022.8.15记录
     *
     * @return
     */
    @PostMapping("/login")
    public String login(HttpServletRequest request, @Validated @RequestBody H5RegisterDto h5RegisterDto) throws Exception {
        Date nowTime = new Date();
        //注册渠道
        PlateformChannel plateformChannel = cacheChannel(h5RegisterDto.getChannelCode());
        if (null == plateformChannel) {
            //未在我们的渠道列表中
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.CHANNEL_CODE_ERRER);
        }
        //渠道被禁用(不能被注册)
        int status = plateformChannel.getForbiddenStatus();
        if (status == Constant.CHANNEL_FORBIDDEN) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.CHANNEL_CODE_ERRER);
        }
        //用户名已经存在
        UserInfo userInfo = userInfoService.findByPhone(h5RegisterDto.getPhoneNumber());
        UserInfo userInfoNew = userInfoService.findByPhone(h5RegisterDto.getPhoneNumber());
        if (null != userInfo || null != userInfoNew) {
            byte statusInfo = userInfo.getStatus();
            if (statusInfo == Constant.DELETE_TYPE) {
                return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.FOR_REGISTER_INFO);
            }
            Map<String, Object> map = new HashMap<>();
            map.put("appType", h5RegisterDto.getClientType());
            List<AppVersionInfoVo> appVersionInfoVos = appVersionInfoService.selectByParam(map);
            try {
                String ipAdrress = IpAdrressUtil.getIpAdrress(request);
                if (redisUtil.setIfAbsent("sys_donw_lock_" + h5RegisterDto.getPhoneNumber() + "_" + ipAdrress, "1", 1, TimeUnit.DAYS)) {
                    channelAsyncCountService.appDownloadCount(userInfo, new Date());
                }
            } catch (Exception e) {
                log.error("渠道登录率统计异常");
            }
            map.put("apk",appVersionInfoVos.get(0));
            return CallBackResult.returnJson(CallBackResult.SUCCESS, Constant.H5_REGISTER_SUCC, map);
        }
        //校验验证码失效状态
        /*if (!redisUtil.hasKey(h5RegisterDto.getPhoneNumber() + Constant.SQUEEZE_PAGE_REGISTER_SUFFIX)) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.CAPTCHA_IS_NOT_EXIST);
        }
        //校验验证码
        if (!h5RegisterDto.getSmsCode().equals(redisUtil.get(h5RegisterDto.getPhoneNumber() + Constant.SQUEEZE_PAGE_REGISTER_SUFFIX))) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.CAPTCHA_IS_ERROR);
        }*/
        Map<Object, Object> resultMap = new HashMap<>();
        //用户信息不存在，走注册
        UserInfo newUserInfo = new UserInfo();
        //设置手机号码
        newUserInfo.setPhone(h5RegisterDto.getPhoneNumber());
        //注册ip
        newUserInfo.setCreateIp(IPUtils.getIpAddr(request));
        //渠道id
        newUserInfo.setChannelId(plateformChannel.getId());
        //注册时间
        newUserInfo.setCreateTime(nowTime);
        //客户端类型（客户端类型：0表示未知，1表示安卓，2表示IOS）
        newUserInfo.setClientType(h5RegisterDto.getClientType());
        int saveState = userInfoService.insertSelective(newUserInfo);
        if (saveState > 0) {
            //查询刚注册成的用户信息
            UserInfo newestUserInfo = userInfoService.findByPhone(h5RegisterDto.getPhoneNumber());
            //如果查出用户为空
            if (null == newestUserInfo) {
                //注册失败
                return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.REGISTER_IS_BAD);
            }
            //插入用户不为空，插入表info_index_info数据
            InfoIndexInfo infoIndexInfo = new InfoIndexInfo();
            infoIndexInfo.setUserId(newestUserInfo.getId());
            infoIndexInfo.setAuthCount(Constant.AUTH_COUNT);
            infoIndexInfoService.insertSelective(infoIndexInfo);
            /**异步统计注册量*/
            channelAsyncCountService.registerIsSuccCount(newestUserInfo.getId(), newUserInfo.getChannelId(), newUserInfo.getCreateTime());
            //注册成功的标记
            resultMap.put("flag", Constant.REGISTER_SUCC_FLAG);


            Map<String, Object> map = new HashMap<>();
            map.put("appType", h5RegisterDto.getClientType());
            List<AppVersionInfoVo> appVersionInfoVos = appVersionInfoService.selectByParam(map);
            try {
                String ipAdrress = IpAdrressUtil.getIpAdrress(request);
                if (redisUtil.setIfAbsent("sys_donw_lock_" + h5RegisterDto.getPhoneNumber() + "_" + ipAdrress, "1", 1, TimeUnit.DAYS)) {
                     userInfo = userInfoService.findByPhone(h5RegisterDto.getPhoneNumber());
                    if (null != userInfo)
                        channelAsyncCountService.appDownloadCount(userInfo, new Date());
                }
            } catch (Exception e) {
                log.error("渠道登录率统计异常");
            }
            resultMap.put("apk",appVersionInfoVos.get(0));
            return CallBackResult.returnJson(CallBackResult.SUCCESS, Constant.H5_REGISTER_SUCC, resultMap);
        } else {//注册失败
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.REGISTER_IS_BAD);
        }
    }

    /**
     * 防鲁
     * @param request
     * @param h5RegisterDto
     * @return
     * @throws Exception
     */
    @PostMapping("/loginH5")
    public String loginH5(HttpServletRequest request, @Validated @RequestBody H5RegisterDto h5RegisterDto) throws Exception {
        log.info("loginH5 参数={} " ,h5RegisterDto);
        String phoneNumber = h5RegisterDto.getPhoneNumber();
        String channelCode = h5RegisterDto.getChannelCode();
        PlateformChannel plateformChannel = cacheChannel(channelCode);
        Date nowTime = new Date();
        //注册渠道
        //PlateformChannel plateformChannel = plateformChannelService.findByChannelCode(channelCode);

        if (null == plateformChannel) {
            //未在我们的渠道列表中
            log.error("渠道code={}",channelCode);
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.CHANNEL_CODE_ERRER);
        }
        //渠道被禁用(不能被注册)
        int status = plateformChannel.getForbiddenStatus();
        if (status == Constant.CHANNEL_FORBIDDEN) {
            log.error("渠道1code={}",channelCode);
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.CHANNEL_CODE_ERRER);
        }
        //用户名已经存在
        UserInfo userInfo = userInfoService.findByPhone(phoneNumber);
        UserInfo userInfoNew = userInfoService.findByPhone(h5RegisterDto.getPhoneNumber());
        if (null != userInfo || null != userInfoNew) {
            byte statusInfo = userInfo.getStatus();
            if (statusInfo == Constant.DELETE_TYPE) {
                return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.FOR_REGISTER_INFO);
            }
            return CallBackResult.returnJson(Constant.H5_OlD_USER, Constant.H5_USER_REGISTER);
        }
        //校验验证码失效状态 TODO 注册验证码去掉 8月15日
        /*if (!redisUtil.hasKey(phoneNumber + Constant.SQUEEZE_PAGE_REGISTER_SUFFIX)) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.CAPTCHA_IS_NOT_EXIST);
        }
        //校验验证码
        if (!h5RegisterDto.getSmsCode().equals(redisUtil.get(phoneNumber + Constant.SQUEEZE_PAGE_REGISTER_SUFFIX))) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.CAPTCHA_IS_ERROR);
        }*/
        Map<Object, Object> resultMap = new HashMap<>();
        //用户信息不存在，走注册
        UserInfo newUserInfo = new UserInfo();
        //设置手机号码
        newUserInfo.setPhone(h5RegisterDto.getPhoneNumber());
        //注册ip
        newUserInfo.setCreateIp(IPUtils.getIpAddr(request));
        //渠道id
        newUserInfo.setChannelId(plateformChannel.getId());
        //注册时间
        newUserInfo.setCreateTime(nowTime);
        //newUserInfo.setEmailAddress();//防鲁唯一值，暂时关闭

        //客户端类型（客户端类型：0表示未知，1表示安卓，2表示IOS）
        newUserInfo.setClientType(h5RegisterDto.getClientType());
        int saveState = userInfoService.insertSelective(newUserInfo);
        if (saveState > 0) {
            //查询刚注册成的用户信息
            UserInfo newestUserInfo = userInfoService.findByPhone(phoneNumber);
            //如果查出用户为空
            if (null == newestUserInfo) {
                //注册失败
                return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.REGISTER_IS_BAD);
            }
            //插入用户不为空，插入表info_index_info数据
            InfoIndexInfo infoIndexInfo = new InfoIndexInfo();
            infoIndexInfo.setUserId(newestUserInfo.getId());
            infoIndexInfo.setAuthCount(Constant.AUTH_COUNT);
            infoIndexInfoService.insertSelective(infoIndexInfo);
            /**异步统计注册量*/
            channelAsyncCountService.registerIsSuccCount(newestUserInfo.getId(), newUserInfo.getChannelId(), newUserInfo.getCreateTime());
            //注册成功的标记
            resultMap.put("flag", Constant.REGISTER_SUCC_FLAG);
            return CallBackResult.returnJson(CallBackResult.SUCCESS, Constant.H5_REGISTER_SUCC, resultMap);
        } else {//注册失败
            log.info("loginH5 phone={} row=0" ,phoneNumber);
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.REGISTER_IS_BAD);
        }
    }

    /**
     * h5注册，注册时填写密码
     *
     * @return
     */
    @PostMapping("/register")
    public String register(HttpServletRequest request, @Validated @RequestBody H5RegisterDto h5RegisterDto) throws Exception {
        Date nowTime = new Date();
        //注册渠道
       // PlateformChannel plateformChannel = plateformChannelService.findByChannelCode(h5RegisterDto.getChannelCode());
        //获取缓存的渠道信息
        PlateformChannel plateformChannel = cacheChannel(h5RegisterDto.getChannelCode());
        if (null == plateformChannel) {
            //未在我们的渠道列表中
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.CHANNEL_CODE_ERRER);
        }
        //渠道被禁用(不能被注册)
        int status = plateformChannel.getForbiddenStatus();
        if (status == Constant.CHANNEL_FORBIDDEN) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.CHANNEL_CODE_ERRER);
        }
        // 密码不能为空
        if (!ValidateUtil.veryPwd(h5RegisterDto.getPassword())) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "密码格式错误，请重新填写");
        }
        String phoneNumber = h5RegisterDto.getPhoneNumber();

        //用户名已经存在
        UserInfo userInfo = userInfoService.findByPhone(phoneNumber);
        UserInfo userInfoNew = userInfoService.findByPhone(h5RegisterDto.getPhoneNumber());

        if (null != userInfo || null != userInfoNew) {
            byte statusInfo = userInfo.getStatus();
            if (statusInfo == Constant.DELETE_TYPE) {
                return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.FOR_REGISTER_INFO);
            }
            return CallBackResult.returnJson(Constant.H5_OlD_USER, Constant.H5_USER_REGISTER);
        }
        //校验验证码失效状态
        if (!redisUtil.hasKey(phoneNumber + Constant.SQUEEZE_PAGE_REGISTER_SUFFIX)) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.CAPTCHA_IS_NOT_EXIST);
        }
        //校验验证码
        if (!h5RegisterDto.getSmsCode().equals(redisUtil.get(phoneNumber + Constant.SQUEEZE_PAGE_REGISTER_SUFFIX))) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.CAPTCHA_IS_ERROR);
        }
        Map<Object, Object> resultMap = new HashMap<>();
        //用户信息不存在，走注册
        UserInfo newUserInfo = new UserInfo();
        //设置手机号码
        newUserInfo.setPhone(h5RegisterDto.getPhoneNumber());
        //注册ip
        newUserInfo.setCreateIp(IPUtils.getIpAddr(request));
        //渠道id
        newUserInfo.setChannelId(plateformChannel.getId());
        //注册时间
        newUserInfo.setCreateTime(nowTime);
        //客户端类型（客户端类型：0表示未知，1表示安卓，2表示IOS）
        newUserInfo.setClientType(h5RegisterDto.getClientType());
        // 用户APP登录密码的盐
        String salt = RandomUtil.getRandomSalt(6);
        newUserInfo.setLoginPwdSalt(salt);
        // 用户APP登录密码
        newUserInfo.setPassword(MD5Util.md5(h5RegisterDto.getPassword() + salt));
        int saveState = userInfoService.insertSelective(newUserInfo);
        if (saveState > 0) {
            //查询刚注册成的用户信息
            UserInfo newestUserInfo = userInfoService.findByPhone(phoneNumber);
            //如果查出用户为空
            if (null == newestUserInfo) {
                //注册失败
                return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.REGISTER_IS_BAD);
            }
            //插入用户不为空，插入表info_index_info数据
            InfoIndexInfo infoIndexInfo = new InfoIndexInfo();
            infoIndexInfo.setUserId(newestUserInfo.getId());
            infoIndexInfo.setAuthCount(Constant.AUTH_COUNT);
            infoIndexInfoService.insertSelective(infoIndexInfo);
            /**异步统计注册量*/
            channelAsyncCountService.registerIsSuccCount(newestUserInfo.getId(), newUserInfo.getChannelId(), newUserInfo.getCreateTime());
            //注册成功的标记
            resultMap.put("flag", Constant.REGISTER_SUCC_FLAG);
            return CallBackResult.returnJson(CallBackResult.SUCCESS, Constant.H5_REGISTER_SUCC, resultMap);
        } else {//注册失败
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.REGISTER_IS_BAD);
        }
    }

    /**
     * 访问刷新接口,做pv、uv使用
     *
     * @param request
     * @param channelCode
     */
    @GetMapping("/visit")
    public CallBackResult addressCount(HttpServletRequest request,
                               String channelCode) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("regist",false);
        String appid = backConfigParamsDao.findStrValue("appId");
        jsonObject.put("appId",appid);
        //注册渠道
        if (org.apache.commons.lang3.StringUtils.isBlank(channelCode)) {
            //渠道编码为空，不用记录
            return CallBackResult.ok(CallBackResult.SUCCESS, Constant.FLUSH_IS_SUCC);
        }
        //获取缓存的渠道信息
        PlateformChannel plateformChannel = cacheChannel(channelCode);
        if (null != plateformChannel) {
            int status = plateformChannel.getStatus();
            int forbiddenStatus = plateformChannel.getForbiddenStatus();
            //如果渠道下线，或者被禁用了 渠道数据就不在增加了
            if (status == 0 || forbiddenStatus == 1) {
                return CallBackResult.ok(CallBackResult.SUCCESS, Constant.FLUSH_IS_SUCC);
            }
            Date nowTime = new Date();
            //插入访问日志
            IpAddressLog ipAddressLog = new IpAddressLog();
            ipAddressLog.setChannelCode(plateformChannel.getChannelCode());
            ipAddressLog.setUrl(Constant.PU_UV_URL);
            ipAddressLog.setCreateTime(nowTime);
            ipAddressLog.setIp(IPUtils.getIpAddr(request));
            int state = iIpAddressLogService.insertSelective(ipAddressLog);
            if (state >0){
                /**异步统计pvUv*/
                channelAsyncCountService.pvUvAsyncCount(plateformChannel, nowTime);
              /*  if (appid.equals("101"))
                {
                    try {
                        //解忧贷根据用户ip对库操作
                        String ip = IPUtils.getIpAddr(request);
                        String key = "jieyou|"+ip+"|322";

                        String res =  HttpUtil.doGet("http://api.hn9zong.com/api_ip.php?ip="+ip+"&Uid=322&key="+ Md5Utils.getMD5(key.getBytes()),2000);
                        log.info("解忧请求结果:{}",res);
                        JSONObject object =   JSONObject.parseObject(res);
                        if (object.getString("code").equals("200"))
                        {
                            boolean flag =   redisUtil.set("jieyou_number_"+object.getJSONObject("data").getString("moblie"),channelCode,60*60*2);
                            if (flag)
                                jsonObject.put("regist",true);
                        }
                    }catch (Exception e)
                    {
                        log.info("visit异常请求");
                    }
                }*/
            }
        }

        return CallBackResult.ok(jsonObject);
    }

    /**
     * APP下載量
     *
     * @param phoneNumber
     * @return
     */
    @GetMapping("/appDownload")
    public String appDownloadCount(String phoneNumber) throws Exception {
        //注册渠道
        if (null == phoneNumber) {
            //用户为空，不用记录
            return CallBackResult.returnJson(CallBackResult.SUCCESS, Constant.FLUSH_IS_SUCC);
        }
        UserInfo userInfo = userInfoService.findByPhone(phoneNumber);
        if (null != userInfo && null != userInfo.getAppDownloadTimes()) {
            int appDownloadTimes = userInfo.getAppDownloadTimes();
            //紧紧首次下载才能
            if (appDownloadTimes == 0) {
                appDownloadTimes = appDownloadTimes + 1;
                UserInfo newUserInfo = new UserInfo();
                newUserInfo.setId(userInfo.getId());
                newUserInfo.setAppDownloadTimes(appDownloadTimes);
                userInfoService.updateByPrimaryKeySelective(newUserInfo);
                /**异步统计下载量*/
                channelAsyncCountService.appDownloadCount(userInfo, new Date());
            } else {
                //叠加APP下载量
                appDownloadTimes = appDownloadTimes + 1;
                UserInfo newUserInfo = new UserInfo();
                newUserInfo.setId(userInfo.getId());
                newUserInfo.setAppDownloadTimes(appDownloadTimes);
                userInfoService.updateByPrimaryKeySelective(newUserInfo);
            }
        }
        return CallBackResult.returnJson(CallBackResult.SUCCESS, CallBackResult.MSG_SUCCESS);
    }

    /**
     * 链接打开判断
     *
     * @param channelCode
     * @return
     */
    @GetMapping("/linkSwitch")
    public String linkSwitch(@NotBlank(message = "参数错误") String channelCode) throws Exception {
        //获取缓存的渠道信息
        PlateformChannel plateformChannel = cacheChannel(channelCode);
        if (null != plateformChannel) {
            Map<String, Object> resultMap = new HashMap<>();
            //微信打开链接开关：0表示允许，1表示禁用
            resultMap.put("wechatSwitch", plateformChannel.getWechatSwitch());
            //贷超打开链接开关：0表示允许，1表示禁用
            resultMap.put("creditSuperSwitch", plateformChannel.getCreditSuperSwitch());
            // 浏览器打开链接开关：0表示允许，1表示禁用
            resultMap.put("browserSwitch", plateformChannel.getBrowserSwitch());
            //pc浏览器打开开关
            resultMap.put("pcSwitch", plateformChannel.getPcSwitch());

           /* BackConfigParamsVo channelStripSwitch = backConfigParamsDao.findBySysKey(Constant.CHANNEL_STRIP_SWITCH);
            if (null != channelStripSwitch) {
                //如果未开启防撸，直接返回1 防撸关闭状态
                if (channelStripSwitch.getSysValue() == 0){
                    resultMap.put("channelStripSwitch", 1);
                }else {
                 //如果开启了防撸 再看这条渠道是否已开启了防撸 '防撸开关 0=开启，1关闭
                    resultMap.put("channelStripSwitch", plateformChannel.getStripSwitch());

                }

            }else {
                //如果配置为空，默认关闭的
                resultMap.put("channelStripSwitch", 1);
            }*/

            return CallBackResult.returnJson(CallBackResult.SUCCESS, CallBackResult.MSG_SUCCESS, resultMap);
        }
        return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.CHANNEL_CODE_ERRER);
    }

    /**
     * 缓存渠道信息
     * @param channelCode
     * @return
     */
    public PlateformChannel cacheChannel(String channelCode){
        PlateformChannel plateformChannel;
        String key = Constant.PLATEFORM_CHANNEL + channelCode;
        if (redisUtil.hasKey(key)){
            String data = redisUtil.get(key);
            plateformChannel = JSONObject.parseObject(data, PlateformChannel.class);
        }else {
            plateformChannel = plateformChannelService.findByChannelCode(channelCode);
            if (null != plateformChannel){
                redisUtil.set(key, JSONObject.toJSONString(plateformChannel));
            }
        }
        return plateformChannel;
    }


}
