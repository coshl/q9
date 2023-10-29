package com.summer.web.controller.thirdpart;

import com.alibaba.fastjson.JSONObject;
import com.summer.dao.entity.*;
import com.summer.dao.entity.mongo.Report;
import com.summer.dao.mapper.mongo.ReportRepository;
import com.summer.enums.*;
import com.summer.service.IUserMobileDataRecordService;
import com.summer.service.impl.InfoIndexInfoService;
import com.summer.service.yys.BoxApi;
import com.summer.service.yys.wuhua.WuHuaApi;
import com.summer.util.*;
import com.summer.web.controller.BaseController;
import com.summer.dao.mapper.IBackConfigParamsDao;
import com.summer.dao.mapper.UserMoXieDataDAO;
import com.summer.api.service.IInfoIndexInfoService;
import com.summer.api.service.channel.IChannelAsyncCountService;
import com.summer.service.impl.UserInfoService;
import com.summer.service.job.RedissLockUtil;
import com.summer.api.service.thirdpart.IOperatorMoXieService;
import com.summer.pojo.vo.BackConfigParamsVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * 用户运营商认证
 *
 * @param
 * @author 2016年12月9日 16:51:09
 */
@RestController
@RequestMapping("v1.0/api/moxie")
@Slf4j
public class MxController extends BaseController {

    private static String YYS_KEYS = "YYS_KEYS";
    @Value("${system.prefix}")
    private String PREFIX;
    @Value("${mobile.xinPan.appId}")
    private String xinPanAppId;
    @Value("${mobile.xinPan.appKey}")
    private String xinPanAppKey;
    @Value("${mobile.xinPan.h5Uri}")
    private String xinPanH5Uri;
    @Value("${mobile.xinPan.backUrl}")
    private String xinPanBackUrl;
    @Value(value = "${server.port}")
    private String port;


    @Resource
    private IChannelAsyncCountService channelAsyncCountService;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private IBackConfigParamsDao backConfigParamsDao;
    @Autowired
    private IInfoIndexInfoService indexInfoService;
    @Resource
    private UserMoXieDataDAO userMoXieDataDAO;
    @Autowired
    private IOperatorMoXieService operatorMoXieService;
    @Resource
    private UserInfoService userInfoService;
    @Autowired
    private InfoIndexInfoService infoIndexInfoService;
    @Autowired
    private IUserMobileDataRecordService userMobileDataRecordService;
    @Resource
    private ReportRepository reportRepository;
    @Resource
    private WuHuaApi wuHuaApi;

    /**
        wuhuayys
    * */
    @PostMapping(value = "WHCallBack")
    public Object WHCallBack(HttpServletRequest request) {
        InputStream is = null;
        final StringBuilder stringBuilder = new StringBuilder();
        JSONObject jsonObjectData = null;
        //获取回调流
        try {
            is = request.getInputStream();
            byte[] b = new byte[4096];
            for (int n; (n = is.read(b)) != -1; ) {
                stringBuilder.append(new String(b, 0, n));
            }
            jsonObjectData = JSONObject.parseObject(stringBuilder.toString());
            log.info("五花营商回调数据=====================:" + jsonObjectData);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String requestId = jsonObjectData.getString("requestId");
        String authId = jsonObjectData.getString("authId");
        String type = jsonObjectData.getString("type");
        String status = jsonObjectData.getString("status");

        // 调用具体的运营商api获取原始数据
        String mobileSwitch = backConfigParamsDao.findStrValue(BackConfigParamsEnum.MOBILE_SWITCH.getKey());
        UserMoXieDataWithBLOBs isExist;
        if(status.equals("FAIL") || type.equals("data")){
            return "OK";
        }
        UserInfo userInfo = userInfoService.findByPhone(requestId.substring(0,11));
        // 初始化运营商认证表
        Date now = new Date();
        isExist = userMoXieDataDAO.selectDataByUserId(userInfo.getId());
        if (Objects.isNull(isExist)) {
            isExist = new UserMoXieDataWithBLOBs(userInfo.getId(), userInfo.getPhone(), authId, now, Integer.valueOf(mobileSwitch).byteValue(),MobileAuthStatus.AUTH_RUN.getValue(),now);
            userMoXieDataDAO.insertSelective(isExist);
        }else {
            isExist.setTaskId(authId);
            userMoXieDataDAO.updateByPrimaryKey(isExist);
        }
        // 用于拉取运营商数据的爬虫id
        String lockKey = "WHCallBack_" + authId;
        try {
            // 锁用户
            RedissLockUtil.tryLock(lockKey, 10, 5);
            isExist = userMoXieDataDAO.selectByTaskId(authId);
            InfoIndexInfo infoIndexInfo = indexInfoService.selectByPrimaryKey(userInfo.getId());
            if (Objects.equals(infoIndexInfo.getAuthMobile(), YesOrNo.YES.getValue())) {
                log.info("运营商已认证通过:{}", authId);
                return "OK";
            }
            log.info("userMoXieData to json:{}", JSONObject.toJSONString(isExist));

            // 同时获取报告数据
            String reportResult = wuHuaApi.doQueryUserReport(authId);
            if(StringUtils.isNotBlank(reportResult)){
                //String urlName = AliOssUtils.uploadReport(null,"report/" +  userInfo.getId()+authId + "_wuhua.txt",reportResult);
                // TODO 上传阿里云获得存储路径，格式：商户pid+userid+爬虫id+后缀名
                UserMoXieDataWithBLOBs updateDataPath = new UserMoXieDataWithBLOBs();
                updateDataPath.setId(isExist.getId());
                updateDataPath.setMxRaw("reportResult");
                updateDataPath.setMxReport(reportResult);
                updateDataPath.setMxAuthStatus(MobileAuthStatus.AUTH_SUCCESS.getValue());
                userMoXieDataDAO.updateByPrimaryKeySelective(updateDataPath);
                log.info("五花运营商认证拉取报告后日志更新:{}", requestId);
                userMobileDataRecordService.updateRecordByUserIdAndMxId(Long.valueOf(userInfo.getId()), Long.valueOf(isExist.getId()), 5);
            }

            if (Objects.equals(mobileSwitch, MobileSwitch.WUHUA.getValue())) {
                log.info("回调修改{}运营商认证状态,userId:{}", MobileSwitch.WUHUA.getDesc(), userInfo.getId());
                // 改用户运营商认证成功
                userInfoService.updateMobileAuthenticStatusAndData(
                        userInfo.getId(),
                        YesOrNo.YES.getValue(),
                        Constant.AUTHENTIC_MOBILE,
                        isExist.getId(),
                        MobileAuthStatus.AUTH_SUCCESS.getValue(), authId);
            }
            log.info("运营商回调日志更新:{}", userInfo.getId());
            try {
                //异步统计运营商认证率
                channelAsyncCountService.mobileAuthIsSuccCount(userInfo, new Date());
                userMobileDataRecordService.updateRecord(userInfo.getId().longValue(), "1234", isExist.getId().longValue(), 4, jsonObjectData.toJSONString());
            } catch (Exception e) {
                log.error("运营商辅助性业务逻辑失败:{}", e);
            }
            return "OK";
        } catch (Exception e) {
            log.error("运营商认证error:{}", e);
            return "OK";
        } finally {
            redisUtil.del(lockKey);
        }
    }

    /**
     * XP运营商回调
     */
    //TODO 00运营商回调
    @PostMapping(value = "XPCallBack")
    public Object XPCallBack(HttpServletRequest request) {
        InputStream is = null;
        final StringBuilder stringBuilder = new StringBuilder();
        JSONObject jsonObjectData = null;
        //获取回调流
        try {
            is = request.getInputStream();
            byte[] b = new byte[4096];
            for (int n; (n = is.read(b)) != -1; ) {
                stringBuilder.append(new String(b, 0, n));
            }
            jsonObjectData = JSONObject.parseObject(stringBuilder.toString());
            log.info("天创营商回调数据:" + jsonObjectData);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String code = jsonObjectData.getString("code");
        String token = jsonObjectData.getString("token");

        // 调用具体的运营商api获取原始数据
        String mobileSwitch = backConfigParamsDao.findStrValue(BackConfigParamsEnum.MOBILE_SWITCH.getKey());
        UserMoXieDataWithBLOBs isExist;
        if(code.equals("START") || code.equals("FAIL")){
            String phone = jsonObjectData.getString("phone");
            UserInfo userInfo = userInfoService.findByPhone(phone);
            // 初始化运营商认证表
            Date now = new Date();
            isExist = userMoXieDataDAO.selectDataByUserId(userInfo.getId());
            if (Objects.isNull(isExist)) {
                isExist = new UserMoXieDataWithBLOBs(userInfo.getId(), userInfo.getPhone(), token, now, Integer.valueOf(mobileSwitch).byteValue(),MobileAuthStatus.AUTH_RUN.getValue(),now);
                userMoXieDataDAO.insertSelective(isExist);
            }else {
                isExist.setTaskId(token);
                userMoXieDataDAO.updateByPrimaryKey(isExist);
            }
            return errorMsg("获取报告中");
        }
        // 用于拉取运营商数据的爬虫id
        String lockKey = "XPCallBack_" + token;
        try {
            // 锁用户
            RedissLockUtil.tryLock(lockKey, 10, 5);
            isExist = userMoXieDataDAO.selectByTaskId(token);

            UserInfo userInfo = userInfoService.findByPhone(isExist.getPhone());
            InfoIndexInfo infoIndexInfo = indexInfoService.selectByPrimaryKey(userInfo.getId());
            if (Objects.equals(infoIndexInfo.getAuthMobile(), YesOrNo.YES.getValue())) {
                log.info("运营商已认证通过:{}", token);
                return successMsg("已认证成功");
            }
            log.info("userMoXieData to json:{}", JSONObject.toJSONString(isExist));
            if (Objects.equals(mobileSwitch, MobileSwitch.XINGPAN.getValue())) {
                log.info("回调修改{}运营商认证状态,userId:{}", MobileSwitch.XINGPAN.getDesc(), userInfo.getId());
                // 改用户运营商认证成功
                userInfoService.updateMobileAuthenticStatusAndData(
                        userInfo.getId(),
                        YesOrNo.YES.getValue(),
                        Constant.AUTHENTIC_MOBILE,
                        isExist.getId(),
                        MobileAuthStatus.AUTH_SUCCESS.getValue(), token);
            }
            log.info("运营商回调日志更新:{}", userInfo.getId());
            try {
                //异步统计运营商认证率
                channelAsyncCountService.mobileAuthIsSuccCount(userInfo, new Date());
                userMobileDataRecordService.updateRecord(userInfo.getId().longValue(), "1234", isExist.getId().longValue(), 4, jsonObjectData.toJSONString());
            } catch (Exception e) {
                log.error("运营商辅助性业务逻辑失败:{}", e);
            }
            return successMsg("保存成功");
        } catch (Exception e) {
            log.error("运营商认证error:{}", e);
            return errorMsg("获取失败");
        } finally {
            redisUtil.del(lockKey);
        }
    }

    //重置box运营商回调 http://q9.q9999.top:8088/v1.0/api/moxie/boxResetCallback
    @PostMapping(value = "/boxResetCallback")
    public ApiResult boxResetCallback(HttpServletRequest request) {
        String requestId = request.getParameter("requestId");
        Map map = new HashMap();
        map.put("requestId",requestId);
        String result =  BoxApi.boxResetCallback(map);
        return new ApiResult("ok", "查询成功===="+result);
    }

    /**
     *   {
     *             "carrierUrl": "https://report.example.com/report.json",
     *                 "requestId": "7cbe04a1a5f948259c0eb754c7419287",
     *                 "userId":"2342151251512512525",
     *                 "status": "success",
     *                 "name":"张三",
     *                 "idcard":"340************78",
     *                 "phone":"138*****678"
     *         }
     *         http://q9.q9999.top:8088/v1.0/api/moxie/boxCallBack
     */
    //TODO 00运营商回调
    @PostMapping(value = "boxCallBack")
    @Transactional
    public Object boxCallBack(HttpServletRequest request) {
        InputStream is = null;
        final StringBuilder stringBuilder = new StringBuilder();
        JSONObject jsonObjectData = null;
        //获取回调流
        try {
            is = request.getInputStream();
            byte[] b = new byte[4096];
            for (int n; (n = is.read(b)) != -1; ) {
                stringBuilder.append(new String(b, 0, n));
            }
            jsonObjectData = JSONObject.parseObject(stringBuilder.toString());
            log.info("box营商回调数据:" + jsonObjectData);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //String token = jsonObjectData.getString("token");
        String status = jsonObjectData.getString("status");
        //String userId = jsonObjectData.getString("userId");
        String requestId = jsonObjectData.getString("requestId");
        UserMoXieDataWithBLOBs isExist;
        // 调用具体的运营商api获取原始数据
        String mobileSwitch = backConfigParamsDao.findStrValue(BackConfigParamsEnum.MOBILE_SWITCH.getKey());
        String phone = jsonObjectData.getString("phone");
        if(status.equals("crawlStart") || status.equals("fail")){
                 return errorMsg("获取报告中");
        }
        UserInfo userInfo = userInfoService.findByPhone(phone);
        // 初始化运营商认证表
        Date now = new Date();
        isExist = userMoXieDataDAO.selectDataByUserId(userInfo.getId());
        if (Objects.isNull(isExist)) {
            isExist = new UserMoXieDataWithBLOBs(userInfo.getId(), userInfo.getPhone(), requestId, now, Integer.valueOf(mobileSwitch).byteValue(),MobileAuthStatus.AUTH_RUN.getValue(),now);
            userMoXieDataDAO.insertSelective(isExist);
        }else {
            isExist.setTaskId(requestId);
            userMoXieDataDAO.updateByPrimaryKey(isExist);
        }

        // 用于拉取运营商数据的爬虫id
        String lockKey = "BoxCallBack_" + requestId;
        try {
            // 锁用户
            RedissLockUtil.tryLock(lockKey, 10, 5);
            isExist = userMoXieDataDAO.selectByTaskId(requestId);
            //UserInfo userInfo = userInfoService.findByPhone(phone);
            InfoIndexInfo infoIndexInfo = indexInfoService.selectByPrimaryKey(userInfo.getId());
            if (Objects.equals(infoIndexInfo.getAuthMobile(), YesOrNo.YES.getValue())) {
                log.info("运营商已认证通过:{}", requestId);
                return successMsg("已认证成功");
            }
            log.info("userMoXieData to json:{}", JSONObject.toJSONString(isExist));

            // 同时获取报告数据
            String carrierUrl = jsonObjectData.getString("carrierUrl");
            String allData = HttpUtil.doGet(carrierUrl);
            /*JSONObject allDataJson = JSONObject.parseObject(allData);
            try {
                Report report = new Report();
                report.setId(userInfo.getId());
                report.setReport(allData);
                report.setType(Integer.valueOf(MobileSwitch.BOX.getValue()));
                reportRepository.save(report);
            } catch (Exception e) {
                log.error("运营商报告放入MongoDB error:{}", e);
            }*/

            // TODO 上传阿里云获得存储路径，格式：商户pid+userid+爬虫id+后缀名
            UserMoXieDataWithBLOBs updateDataPath = new UserMoXieDataWithBLOBs();
            updateDataPath.setId(isExist.getId());
            updateDataPath.setMxRaw(carrierUrl);
            updateDataPath.setMxReport(allData);
            updateDataPath.setMxAuthStatus(MobileAuthStatus.AUTH_SUCCESS.getValue());
            userMoXieDataDAO.updateByPrimaryKeySelective(updateDataPath);
            log.info("运营商认证拉取报告后日志更新:{}", requestId);
            userMobileDataRecordService.updateRecordByUserIdAndMxId(Long.valueOf(userInfo.getId()), Long.valueOf(isExist.getId()), 5);

            if (Objects.equals(mobileSwitch, MobileSwitch.BOX.getValue())) {
                log.info("回调修改{}运营商认证状态,userId:{}", MobileSwitch.BOX.getDesc(), userInfo.getId());
                // 改用户运营商认证成功
                userInfoService.updateMobileAuthenticStatusAndData(
                        userInfo.getId(),
                        YesOrNo.YES.getValue(),
                        Constant.AUTHENTIC_MOBILE,
                        isExist.getId(),
                        MobileAuthStatus.AUTH_SUCCESS.getValue(),
                        requestId);
            }
            log.info("运营商回调日志更新:{}", userInfo.getId());
            try {
                //异步统计运营商认证率
                channelAsyncCountService.mobileAuthIsSuccCount(userInfo, new Date());
                userMobileDataRecordService.updateRecord(userInfo.getId().longValue(), "1234", isExist.getId().longValue(), 4, jsonObjectData.toJSONString());
            } catch (Exception e) {
                log.error("运营商辅助性业务逻辑失败:{}", e);
            }
            return successMsg("保存成功");
        } catch (Exception e) {
            log.error("运营商认证error:{}", e);
            return errorMsg("获取失败");
        } finally {
            redisUtil.del(lockKey);
        }
    }



    /**
     * 人脸/身份认证和运营商认证的一些配置
     */
    @RequestMapping(value = "/mobileSelect", method = {RequestMethod.POST, RequestMethod.GET})
    public Object mobileSelect(HttpServletRequest request) throws Exception {
        // 根据pid查询商户后台地址
        String switchM = backConfigParamsDao.findStrValue("mobile_switch");
        // ocr开关：1、ocr
        BackConfigParamsVo ocr = backConfigParamsDao.findBySysKey("ocr_switch");
        // 运营商用户修改权限:0不可,1手机号,2身份证姓名,3修改所有
        BackConfigParamsVo info_update = backConfigParamsDao.findBySysKey("info_update");
        // 自动申请借款,0手动,1自动
        String auto_borrow = backConfigParamsDao.findStrValue("auto_borrow");
        // 客服url
        String wait_url = backConfigParamsDao.findStrValue("wait_url");
        // 老用户自动申请借款,0手动,1自动
        String old_auto = backConfigParamsDao.findStrValue("old_auto");
        if (StringUtils.isBlank(old_auto)) {
            old_auto = YesOrNo.No.getStrValue();
        }
        UserInfo userInfo = redisUser(request);
        if (null != userInfo) {
            /*String riskType = userInfoMapper.selectChannelById(userInfo.getId());
            if (StringUtils.isNotBlank(riskType) && !"5".equals(riskType) && !"0".equals(riskType)) {
                //按渠道
                switchM = riskType;
            }*/
            if (userInfo.getCustomerType() == YesOrNo.YES.getValue().byteValue() && Objects.equals(YesOrNo.No.getStrValue(), old_auto)) {
                //老用户不自动申请
                log.info("old_auto=" + old_auto);
                auto_borrow = YesOrNo.No.getStrValue();
            }
        }
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("mobile_switch", StringUtils.isBlank(switchM) ? 0 : Integer.parseInt(switchM));
        data.put("ocr_switch", null == ocr ? 0 : ocr.getSysValue());
        data.put("info_update", Objects.isNull(info_update) ? YesOrNo.No.getValue() : info_update.getSysValue());
        data.put("wait_url", StringUtils.isBlank(wait_url) ? "#" : wait_url);
        data.put("auto_borrow", StringUtils.isBlank(auto_borrow) ? YesOrNo.No.getValue() : Double.parseDouble(auto_borrow));
        return successMsg("成功", data);
    }

    /**
     * 运营商认证
     */
    //TODO 00运营商 获取Url
    @PostMapping(value = "/mobileApproveMX")
    public CallBackResult mobileApproveMX(@RequestBody String body, HttpServletRequest request) {
        // 当前登陆人身份信息校验 登陆人校验
        /*JSONObject parameters = JSONObject.parseObject(body);
        String mobilePhone = parameters.getString("mobilePhone");
        if (StringUtils.isBlank(mobilePhone)) {
            log.info("运营商认证h5手机号不能为空:{}", body);
            return CallBackResult.fail("手机号不能为空");
        }*/
        UserInfo loginUserInfo = redisUser(request);
        String lockey = PREFIX + YYS_KEYS + "_" + loginUserInfo.getId();
        log.info("运营商认证h5的lockey:{}", lockey);
        if (redisUtil.hasKey(lockey)) {
            log.info("运营商认证h5请求过于频繁:{}", lockey);
            return CallBackResult.fail(String.format("运营商认证请求频繁,请等待%s秒后重刷新", redisUtil.getExpire(lockey)));
        }
        RLock lock = RedissLockUtil.lock(lockey, 30);
        log.info("收到运营商用户信息提交资料:{}", body);
        try {
            InfoIndexInfo infoIndexInfo = infoIndexInfoService.selectByPrimaryKey(loginUserInfo.getId());
            if (YesOrNo.YES.getValue() != infoIndexInfo.getAuthInfo()) {
                log.info("请先过身份认证");
                return CallBackResult.fail(301, "身份认证数据校验中，30s后请重试");
            }
            if (YesOrNo.YES.getValue() != infoIndexInfo.getAuthContacts()) {
                log.info("请先过个人信息认证");
                return CallBackResult.fail(301, "个人信息认证数据校验中，30s后请重试");
            }
            if (StringUtils.isAnyEmpty(loginUserInfo.getRealName(), loginUserInfo.getIdCard())) {
                log.info("请先过个人信息认证");
                return CallBackResult.fail(301, "个人信息认证数据校验中，30s后请重试");
            }
           /* if (!StringUtils.equals(loginUserInfo.getPhone(), mobilePhone)) {
                log.info("登录手机号和认证手机号码不一致");
                return CallBackResult.fail(301, "登录手机号和认证手机号码不一致");
            }*/
            /*UserMoXieDataWithBLOBs isExist = userMoXieDataDAO.selectDataByUserId(loginUserInfo.getId());
            if (Objects.nonNull(isExist)) {
                log.info("请等待运营商认证处理完成");
                return CallBackResult.fail("请等待运营商认证处理完成");
            }*/
            if (Objects.equals(infoIndexInfo.getAuthMobile(), Constant.STATUS_INT_VALID)) {
                log.info("运营商已认证,请勿重复认证");
                return CallBackResult.fail(301, "运营商已认证,请勿重复认证");
            }

            if (Objects.equals(infoIndexInfo.getAuthMobile(), Constant.STATUS_INT_WAIT)) {
                log.info("运营商正在认证中，请等待:{}", infoIndexInfo.getUserId());
                return CallBackResult.fail("运营商正在认证中，请等待");
            }

            if (redisUtil.hasKey(Constant.USER_INDEX_AUTHMOBILE + infoIndexInfo.getUserId())) {
                log.info("运营商认证状态命中缓存,请等待缓存释放:{}", infoIndexInfo.getUserId());
                return CallBackResult.fail("运营商正在认证中，请等待" + redisUtil.getExpire(Constant.USER_INDEX_AUTHMOBILE + infoIndexInfo.getUserId()) + "s");
            }
            String mobileSwitch = backConfigParamsDao.findStrValue(BackConfigParamsEnum.MOBILE_SWITCH.getKey());
            Map<String, Object> data = new HashMap<>();
            //运营商通道
            data.put("operatorChannel", mobileSwitch);
            String logId = RandomUtil.getCode();
            if (Objects.equals(mobileSwitch, MobileSwitch.FUYGS.getValue())) {
                return null;
            }else if(Objects.equals(mobileSwitch, MobileSwitch.XINGPAN.getValue())){
                Map heards = new TreeMap();
                heards.put("appId", xinPanAppId);//"8008869";// 商户 id
                heards.put("appKey", xinPanAppKey);//"33db31946cb94b0b671b6d1985245646";// 商户key
                heards.put("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
                Map params = new TreeMap();
                params.put("userName", loginUserInfo.getPhone());
                params.put("identityNo", loginUserInfo.getIdCard());
                params.put("identityName", loginUserInfo.getRealName());
                params.put("backUrl", xinPanBackUrl);
                String yysResult = GZHttpUtils.doPostHttp(xinPanH5Uri,heards,params);
                //{"msg":null,"code":"SUCCESS","data":"http://xpapi1.cczmy.cn/crawler/sdk/v2?uuid=e631cd8e-ca41-4de0-9584-fbf4f6359911"}
                log.info("星盘运营商返回参数:{}",yysResult);
                JSONObject yysJsonObject = JSONObject.parseObject(yysResult);
                if(yysJsonObject.containsKey("code")  && ("SUCCESS").equals(yysJsonObject.getString("code"))){
                    data.put("uri", yysJsonObject.getString("data"));
                    data.put("userId", loginUserInfo.getId());
                    data.put("realName", loginUserInfo.getRealName());
                    data.put("idCard", loginUserInfo.getIdCard());
                    userMobileDataRecordService.insertRecord(loginUserInfo.getId().longValue(), logId, loginUserInfo.getPhone(), yysJsonObject.getString("data"));
                    log.info("插入日志，星盘运营商认证返回参数:{}", data);
                }else if("FAIL".equals(yysJsonObject.getString("code"))){
                    //有多个运营商可用暂时注释
                   /* BackConfigParams backConfigParams = new BackConfigParams();
                    backConfigParams.setSysKey(BackConfigParamsEnum.MOBILE_SWITCH.getKey());
                    backConfigParams.setSysValue("3");
                    //更换成运营商
                    backConfigParamsDao.updateBySyskey(backConfigParams);
                    return CallBackResult.fail("请重新获取运营商");*/
                }
            }else if(Objects.equals(mobileSwitch, MobileSwitch.BOX.getValue())){
                Map bodys = new HashMap();
                bodys.put("userId", PREFIX+port);
                bodys.put("name", loginUserInfo.getRealName());
                bodys.put("idcard", loginUserInfo.getIdCard());
                bodys.put("phone", loginUserInfo.getPhone());
                String result = BoxApi.boxGetUrl(bodys);
                log.info("box运营商返回参数:{}",result);
                if (!result.isEmpty()){
                    String authUrl =  JSONObject.parseObject(result).getString("authUrl").concat("&redirectUrl=http://8.209.212.16:8076/otheryys/callPageBack");
                    //String requestId =  JSONObject.parseObject(result).getString("requestId");
                    data.put("uri", authUrl);
                    data.put("userId", loginUserInfo.getId());
                    data.put("realName", loginUserInfo.getRealName());
                    data.put("idCard", loginUserInfo.getIdCard());
                    userMobileDataRecordService.insertRecord(loginUserInfo.getId().longValue(), logId, loginUserInfo.getPhone(), result);
                    log.info("插入日志，box运营商认证返回参数:{}", data);
                }else {
                    return CallBackResult.fail("获取运营商失败,请重新获取运营商");
                }
            }else if(Objects.equals(mobileSwitch, MobileSwitch.WUHUA.getValue())){
                Map<String,String> map = new HashMap();
                map.put("name", loginUserInfo.getRealName());
                map.put("idNumber", loginUserInfo.getIdCard());
                map.put("phone", loginUserInfo.getPhone());
                String url = wuHuaApi.getOperatorUrl(map);
                log.info("五花运营商返回参数:{}",url);
                if (!url.isEmpty()){
                    data.put("uri", url);
                    data.put("userId", loginUserInfo.getId());
                    data.put("realName", loginUserInfo.getRealName());
                    data.put("idCard", loginUserInfo.getIdCard());
                    userMobileDataRecordService.insertRecord(loginUserInfo.getId().longValue(), logId, loginUserInfo.getPhone(), url);
                    log.info("插入日志，wuhua运营商认证返回参数:{}", data);
                }else {
                    return CallBackResult.fail("获取运营商失败,请重新获取运营商");
                }
            }
            return CallBackResult.ok(data);
        } catch (Exception e) {
            log.error("获取运营商url失败:{}", e);
            return CallBackResult.fail(e.getMessage());
        } finally {
            lock.unlock();
        }
    }




    /**
     * 运营商跳过认证状态
     */
    @GetMapping("/jumpOperator/{userId}")
    public Object jumpOperator(@PathVariable Integer userId) {
        log.info("跳过运营商:{}", userId);
        String lockKey = "callBack_" + userId;
        RedissLockUtil.tryLock(lockKey, 10, 3);
        try {
            UserInfo userInfo = userInfoService.selectByPrimaryKey(userId);
            if(userInfo.getMobileAuthentic() == 1){
                return CallBackResult.fail(userInfo.getRealName() + "运营商已认证完成,不需要跳过");
            }else if(userInfo.getAuthenticStatus() != 2){
                return CallBackResult.fail(userInfo.getRealName() + "未认证到运营商,不能跳过");
            }
            // 调用具体的运营商api获取原始数据
            String mobileSwitch = backConfigParamsDao.findStrValue(BackConfigParamsEnum.MOBILE_SWITCH.getKey());
            if(mobileSwitch.equals("3")){
                return CallBackResult.fail(userInfo.getRealName() + "当前运营商有风控不能跳过");
            }
            // 初始化运营商认证表
            Date now = new Date();
            UserMoXieDataWithBLOBs isExist = userMoXieDataDAO.selectDataByUserId(userId);
            if (Objects.isNull(isExist)) {
                isExist = new UserMoXieDataWithBLOBs(userId, userInfo.getPhone(), "99999", now, Integer.valueOf(mobileSwitch).byteValue(),MobileAuthStatus.AUTH_SUCCESS.getValue(),now);
                isExist.setStatus(Byte.parseByte(mobileSwitch));
                isExist.setMxRaw("手动跳过");
                isExist.setMxReport("手动跳过");
                userMoXieDataDAO.insertSelective(isExist);
                isExist = userMoXieDataDAO.selectDataByUserId(userId);
            }else {
                UserMoXieDataWithBLOBs updateDataPath = new UserMoXieDataWithBLOBs();
                updateDataPath.setId(isExist.getId());
                updateDataPath.setMxRaw("手动跳过");
                updateDataPath.setMxReport("手动跳过");
                updateDataPath.setMxAuthStatus(MobileAuthStatus.AUTH_SUCCESS.getValue());
                userMoXieDataDAO.updateByPrimaryKeySelective(updateDataPath);
            }
            // 改用户运营商认证成功
            userInfoService.updateMobileAuthenticStatusAndData(
                    userInfo.getId(),
                    YesOrNo.YES.getValue(),
                    Constant.AUTHENTIC_MOBILE,
                    isExist.getId(),
                    MobileAuthStatus.AUTH_SUCCESS.getValue(),
                    "手动跳过");
            try {
                //异步统计运营商认证率
                channelAsyncCountService.mobileAuthIsSuccCount(userInfo, new Date());
                userMobileDataRecordService.updateRecord(userInfo.getId().longValue(), "1234", isExist.getId().longValue(), 4, "手动跳过");
            } catch (Exception e) {
                log.error("运营商辅助性业务逻辑失败:{}", e);
            }
        } finally {
            redisUtil.del(lockKey);
        }
        return CallBackResult.ok("用户运营商跳过成功");
    }


    /**
     * 修改运营商认证状态
     */
    @GetMapping("/failureUrl/{userId}/{logId}")
    public Object failureUrl(@PathVariable Integer userId, @PathVariable String logId) {
        log.info("运营商认证失败:{}", userId);
        String lockKey = "callBack_" + userId;
        RedissLockUtil.tryLock(lockKey, 10, 3);
        try {
            log.info("failureUrl修改日志记录userId:{},logId:{}", userId, logId);
            userMobileDataRecordService.updateRecord(userId.longValue(), logId, 3);
        } finally {
            redisUtil.del(lockKey);
        }
        return CallBackResult.fail("运营商认证失败");
    }

    @GetMapping("/successUrl/{userId}/{logId}")
    public CallBackResult mobileChange(@PathVariable Integer userId, @PathVariable String logId) {
        log.info("successUrl修改日志记录userId:{},logId:{}", userId, logId);
        String lockKey = "callBack_" + userId;
        RedissLockUtil.tryLock(lockKey, 10, 3);
        try {
            InfoIndexInfo infoIndexInfo = indexInfoService.selectByPrimaryKey(userId);
            if (Objects.equals(infoIndexInfo.getAuthMobile(), YesOrNo.No.getValue())) {
                log.info("successUrl运营商认证中,userId:{}", userId);
                redisUtil.set(Constant.USER_INDEX_AUTHMOBILE + userId, Constant.STATUS_INT_WAIT, 120);
                // 修改运营商状态为认证中
                userInfoService.updateMobileAuthenticStatus(userId, Constant.STATUS_INT_WAIT);
                return CallBackResult.ok("运营商认证处理中，请耐心等待");
            } else if (Objects.equals(infoIndexInfo.getAuthMobile(), YesOrNo.YES.getValue())) {
                return CallBackResult.ok("运营商认证成功");
            }
            UserMobileDataRecord isExist = userMobileDataRecordService.findUserMobileDataRecordByUserIdAndLogId(userId.longValue(), logId);
            if (Objects.nonNull(isExist) && isExist.getStatus() == 1) {
                userMobileDataRecordService.updateRecord(userId.longValue(), logId, 2);
            }
        } finally {
            redisUtil.del(lockKey);
        }
        return CallBackResult.ok("运营商认证处理中，请耐心等待");
    }

    //查询运营商认证状态
    @GetMapping("/getMobileChange")
    public CallBackResult getMobileChange(HttpServletRequest request) {
        UserInfo userInfo = redisUser(request);
        if (null == userInfo) {
            return CallBackResult.fail(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        log.info("getMobileChange运营商接口调用:{}", userInfo.getId());
        String lockKey = "callBack_" + userInfo.getId();
        RedissLockUtil.tryLock(lockKey, 10, 3);
        try {
            InfoIndexInfo infoIndexInfo = indexInfoService.selectByPrimaryKey(userInfo.getId());
            if (Objects.equals(infoIndexInfo.getAuthMobile(), YesOrNo.No.getValue())) {
                log.info("successUrl运营商认证中,userId:{}", userInfo.getId());
                redisUtil.set(Constant.USER_INDEX_AUTHMOBILE + userInfo.getId(), Constant.STATUS_INT_WAIT, 120);
                // 修改运营商状态为认证中
                userInfoService.updateMobileAuthenticStatus(userInfo.getId(), Constant.STATUS_INT_WAIT);
                return CallBackResult.ok("运营商认证处理中，请耐心等待");
            } else if (Objects.equals(infoIndexInfo.getAuthMobile(), YesOrNo.YES.getValue())) {
                return CallBackResult.ok("运营商认证成功");
            }
        } finally {
            redisUtil.del(lockKey);
        }
        return CallBackResult.ok("运营商认证中");
    }

    /**
     * 前端获取运营商报告
     *
     * @param userId 借款人用户id
     * @return
     */
    @GetMapping("/queryMobileReport/{userId}")
    public Object queryReportAndRawData(@PathVariable Integer userId) {
        UserInfo isExist = userInfoService.selectByPrimaryKey(userId);
        if (Objects.isNull(isExist)) {
            return errorMsg("此用户不存在");
        }
        Map<String, Object> moXieDetail = operatorMoXieService.getMoXieDetail(userId);

        if (Objects.isNull(moXieDetail)) {
            return errorMsg("暂无报告");
        }
        return successMsg("成功", moXieDetail);
    }

    /**
     * 前端获取运营商报告
     *
     * @param userId 借款人用户id
     * @return
     */
    @GetMapping("/queryMobileHtmlReport/{userId}")
    public Object queryMobileHtmlReport(@PathVariable Integer userId) {
        UserInfo isExist = userInfoService.selectByPrimaryKey(userId);
        if (Objects.isNull(isExist)) {
            return errorMsg("此用户不存在");
        }
        Map<String, Object> moXieDetail = operatorMoXieService.getMoXieDetail(userId);
        if (Objects.isNull(moXieDetail)) {
            return errorMsg("暂无报告");
        }
        return successMsg("成功", moXieDetail);
    }

    /**
     * 前端上传taskId
     *
     * @param
     * @return
     */
    @PostMapping("/back12")
    public Object getTaskId(@RequestParam String taskId,@RequestParam String data, HttpServletRequest request) {
        UserInfo userInfo = redisUser(request);
        if (userInfo == null)
            return CallBackResult.fail("未登录");
        log.info("运营商认证前端上传爬虫id,taskId:{},userId:{},phone:{},原始数据:{}", taskId, userInfo.getId(), userInfo.getPhone(),data);
        if (org.springframework.util.StringUtils.isEmpty(taskId))
            return CallBackResult.fail("缺少taskId");
        //运营商通道
        String mobileSwitch = backConfigParamsDao.findStrValue(BackConfigParamsEnum.MOBILE_SWITCH.getKey());
        UserMoXieDataWithBLOBs userMoXieData1 = new UserMoXieDataWithBLOBs(userInfo.getId(), userInfo.getPhone(), taskId, new Date(), Integer.valueOf(mobileSwitch).byteValue(), MobileAuthStatus.AUTH_SUCCESS.getValue(), new Date());
        int res = 0;
        UserMoXieDataWithBLOBs userMoXieData = userMoXieDataDAO.selectDataByUserId(userInfo.getId());
        if (userMoXieData == null) {
            res = userMoXieDataDAO.insertSelective(userMoXieData1);
            userInfoService.updateMobileAuthenticStatusAndData(
                    userInfo.getId(),
                    YesOrNo.YES.getValue(),
                    Constant.AUTHENTIC_MOBILE,
                    userMoXieData1.getId(),
                    MobileAuthStatus.AUTH_RUN.getValue(), taskId);

        } else {
            userMoXieData.setUpdateTime(new Date());
            userMoXieData.setTaskId(taskId);
            userMoXieData.setStatus(Integer.valueOf(mobileSwitch).byteValue());
            userMoXieData.setMxAuthStatus(MobileAuthStatus.AUTH_SUCCESS.getValue());
            res = userMoXieDataDAO.updateByPrimaryKeySelective(userMoXieData);
            userInfoService.updateMobileAuthenticStatusAndData(
                    userInfo.getId(),
                    YesOrNo.YES.getValue(),
                    Constant.AUTHENTIC_MOBILE,
                    userMoXieData.getId(),
                    MobileAuthStatus.AUTH_RUN.getValue(), taskId);
        }
        if (res != 1)
            return CallBackResult.fail("认证失败");
        channelAsyncCountService.mobileAuthIsSuccCount(userInfo, new Date());
        return CallBackResult.ok("认证成功");
    }

    /**
     * 前端上传taskId
     *
     * @param
     * @return
     */
    @PostMapping("/back22")
    public Object getTaskId2(@RequestBody String body, HttpServletRequest request) {
        JSONObject parameters = JSONObject.parseObject(body);
        String taskId = parameters.getString("taskId");
        String data = parameters.getString("data");
        UserInfo userInfo = redisUser(request);
        if (userInfo == null)
            return CallBackResult.fail("未登录");
        log.info("运营商认证前端上传爬虫back2接口id,taskId:{},userId:{},phone:{},原始数据:{}", taskId, userInfo.getId(), userInfo.getPhone(),data);
        if (StringUtils.isEmpty(taskId))
            return CallBackResult.fail("缺少taskId");
        //运营商通道
        String mobileSwitch = backConfigParamsDao.findStrValue(BackConfigParamsEnum.MOBILE_SWITCH.getKey());
        UserMoXieDataWithBLOBs userMoXieData1 = new UserMoXieDataWithBLOBs(userInfo.getId(), userInfo.getPhone(), taskId, new Date(), Integer.valueOf(mobileSwitch).byteValue(), MobileAuthStatus.AUTH_SUCCESS.getValue(), new Date());
        int res = 0;
        UserMoXieDataWithBLOBs userMoXieData = userMoXieDataDAO.selectDataByUserId(userInfo.getId());
        if (userMoXieData == null) {
            res = userMoXieDataDAO.insertSelective(userMoXieData1);
            userInfoService.updateMobileAuthenticStatusAndData(
                    userInfo.getId(),
                    YesOrNo.YES.getValue(),
                    Constant.AUTHENTIC_MOBILE,
                    userMoXieData1.getId(),
                    MobileAuthStatus.AUTH_RUN.getValue(), taskId);

        } else {
            userMoXieData.setUpdateTime(new Date());
            userMoXieData.setTaskId(taskId);
            userMoXieData.setStatus(Integer.valueOf(mobileSwitch).byteValue());
            userMoXieData.setMxAuthStatus(MobileAuthStatus.AUTH_SUCCESS.getValue());
            res = userMoXieDataDAO.updateByPrimaryKeySelective(userMoXieData);
            userInfoService.updateMobileAuthenticStatusAndData(
                    userInfo.getId(),
                    YesOrNo.YES.getValue(),
                    Constant.AUTHENTIC_MOBILE,
                    userMoXieData.getId(),
                    MobileAuthStatus.AUTH_RUN.getValue(), taskId);
        }
        if (res != 1)
            return CallBackResult.fail("认证失败");
        channelAsyncCountService.mobileAuthIsSuccCount(userInfo, new Date());
        return CallBackResult.ok("认证成功");
    }


    /**
     * 获取用户运营商认证信息
     * */
    @PostMapping(value = "/getMobileInfo")
    public CallBackResult operatorCreateToken(@RequestParam Integer userId, HttpServletRequest request) {
        UserInfo userInfo = userInfoService.selectByPrimaryKey(userId);
        if (Objects.isNull(userInfo))
            return CallBackResult.fail("用户不存在");
        // 当前登陆人身份信息校验 登陆人校验
        JSONObject res = new JSONObject();
        res.put("name",userInfo.getRealName());
        res.put("idNo",userInfo.getIdCard());
        res.put("phone",userInfo.getPhone());
       return CallBackResult.ok(res);
    }


}
