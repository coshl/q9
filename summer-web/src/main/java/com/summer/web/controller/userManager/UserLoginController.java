package com.summer.web.controller.userManager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.summer.annotation.Log;
import com.summer.dao.entity.*;
import com.summer.dao.mapper.*;
import com.summer.web.controller.BaseController;
import com.summer.group.SavePassword;
import com.summer.group.SmsVerify;
import com.summer.pojo.dto.PlateformChannelParamDto;
import com.summer.pojo.dto.UpdatePasswordDTO;
import com.summer.api.service.IPlateformChannelService;
import com.summer.api.service.IPlateformRoleService;
import com.summer.api.service.IPlateformUserService;
import com.summer.service.sms.ISmsService;
import com.summer.util.*;
import com.summer.util.encrypt.AESDecrypt;
import com.summer.pojo.vo.CuiShouListVO;
import com.summer.pojo.vo.PlateformAuthorityDTO;
import com.summer.pojo.vo.PlatformUserRoleVo;
import com.summer.pojo.vo.PlatformUserVo;
import com.summer.web.util.GoogleAuthUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 用户Controller
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/v1.0/api/user")
public class UserLoginController extends BaseController {

    @Resource
    private IPlateformUserService plateformUserService;

    @Autowired
    private ISmsService smsService;

    @Resource
    private RedisUtil redisUtil;
    @Value("${system.isWhiteList}")
    private String isWhiteList;
    @Value("${system.isWhiteListPwd}")
    private String isWhiteListPwd;
    @Value("${spring.profiles.active}")
    private String env;
    private final String userId = "100000";
    @Value("${config.default.smsLoginCode}")
    private String code;

    @Resource
    private IPlateformRoleService plateformRoleService;

    @Resource
    private PlatformUserMapper platformUserMapper;
    @Resource
    private PlateformChannelMapper plateformChannelMapper;
    @Resource
    private IPlateformChannelService plateformChannelService;
    @Resource
    private PlatformRoleMapper platformRoleMapper;
    @Resource
    IBackConfigParamsDao iBackConfigParamsDao;
    @Resource
    private UserAppSoftwareDAO userAppSoftwareDAO;


    /**
     * 发送验证码
     *
     * @return
     */
    @PostMapping(value = "/sendCode")
    public String sendCode(@RequestBody PlatformUser platformUser, HttpServletRequest request) throws Exception {
        String phoneCaptcha;
        String captchaRedisKey;
        String phoneNumber = platformUser.getPhoneNumber();
        if (StringUtils.isBlank(phoneNumber)) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "手机号错误！");
        }
        //短信注册校验
        /*String timestamp = platformUser.getTimestamp();
        String rsa = platformUser.getRsa();
        if (com.summer.util.log.StringUtils.isBlank(timestamp) || com.summer.util.log.StringUtils.isBlank(rsa)) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.VERIFY_ERRER);
        }
        String md5 = DigestUtils.md5Hex(timestamp + phoneNumber + "szss");
        if (!rsa.equals(md5)) {
            log.info("sms sign error phone={},timestamp={},rsa={},md5={}", phoneNumber, timestamp, rsa, md5);
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.VERIFY_ERRER);
        }
        long l = System.currentTimeMillis();
        long diff = l - Long.parseLong(timestamp);
        if (diff > 100000) {
            log.info("sms time over phone={},timestamp={},diff={}", phoneNumber, timestamp, diff);
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.VERIFY_ERRER);
        }*/
        //后台登陆 ip限制
        boolean hit = SmsKLimitUtil.limitTimesIPByCommon(redisUtil, request);
        if (hit) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.IP_LIMIT_BACK);
        }
        //短信次数限制
        /*boolean isExpier3 = SmsKLimitUtil.limitTimes(redisUtil,platformUser.getPhoneNumber(), 99,Constant.PHONE_CAPTCHA_EXPIRATION_TIME,Constant.LIMIT_TIMES);
        if (!isExpier3){
           return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS,Constant.SMS_CAPTCHA_NO_FAILURE);
        }*/

        PlatformUser loginInfo = plateformUserService.findByPhoneNumber(phoneNumber);
        if (loginInfo == null) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.USER_NAME_EXISTS);
        }


        //随机验证码
        if (env.equalsIgnoreCase("dev")) {
            //phoneCaptcha = "0000";
            phoneCaptcha = RandomUtil.getCode();
        } else {
            phoneCaptcha = RandomUtil.getCode();
        }
        //获取验证码的key
        captchaRedisKey = phoneNumber + Constant.PLATEFROM_USER_LOGIN_SUFFIX;
        Boolean isSuccess = smsService.sendCode(phoneNumber, phoneCaptcha, true, 99);
        if (isSuccess) {
            boolean setSucc = redisUtil.set(captchaRedisKey, phoneCaptcha, Constant.PHONE_CAPTCHA_EXPIRATION);
            if (setSucc) {
                return CallBackResult.returnJson(CallBackResult.SUCCESS, Constant.SMS_CAPTHCA_SEND_SUCCESS);
            }
        }
        return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.SMS_CAPTHCA_SEND_BAD);
    }

    public static void main(String[] args) {
        String key1 = GoogleAuthUtil.generateSecretKey();
        String qcodeUrl = GoogleAuthUtil.getQcode("13888888888", key1);
        System.out.println(qcodeUrl);
        System.out.println(key1);
    }
    /**
     * 用户登录
     *
     * @param platformUser
     * @return
     * @throws Exception
     */
    @Log(title = "后台用户登陆")
    @PostMapping(value = "/login")
    public String login(@RequestBody PlatformUser platformUser, HttpServletRequest request) throws Exception {
        String phoneNumber = platformUser.getPhoneNumber();
        String password = platformUser.getPassword();
        if (StringUtils.isBlank(phoneNumber) || StringUtils.isBlank(password)) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "用户名或者密码不能为空");
        }
        log.info("login param phone={}", phoneNumber);
        if (StringUtils.isBlank(phoneNumber)) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "手机号错误！");
        }
        // 手机号码正则校验
        if (!ValidateUtil.isMobileNumber(phoneNumber)) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "手机号错误！");
        }

        // 密码正则校验
        if (!ValidateUtil.veryPwd(password)) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "账号或密码错误！");
        }
        // 后台登陆密码错误次数验证
        String key = "login_" + phoneNumber + DateUtil.getDateFormat(new Date(), "yyyy-MM-dd");
        Object times = redisUtil.getObject(key);
        if (times != null && (Integer) times >= 5) {
            log.info("后台登陆密码错误，手机号：", phoneNumber);
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "登录密码错误次数超限");
        }
        Date now = new Date();
        //登录ip
        String ipAddr = IPUtils.getIpAddr(request);
        PlatformUser loginInfo = plateformUserService.findByPhoneNumber(platformUser.getPhoneNumber());
        if (null == loginInfo) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.USER_NAME_NOT_EXISTS);
        }
        UserAppSoftware userId = userAppSoftwareDAO.findUserId(loginInfo.getId());
        if (null != userId) {
            if (!userId.getPackageName().equals(ipAddr)) {
                return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "登录失败！");
            }
        }
         //谷歌验证器
        if (null == loginInfo.getCode() || loginInfo.getCode().length() == 0) {
            log.info("谷歌验证器");
            String key1 = GoogleAuthUtil.generateSecretKey();
            log.info("谷歌验证器{}",key1);
            String qcodeUrl = GoogleAuthUtil.getQcode(phoneNumber, key1);
            loginInfo.setCode(key1);
            plateformUserService.update(loginInfo);
            return CallBackResult.returnJson(CallBackResult.CREATED, qcodeUrl);
        } else {
            // 验证码不为空
            if (StringUtils.isNotBlank(platformUser.getCode())) {
                boolean b = GoogleAuthUtil.check_code(loginInfo.getCode(), Integer.parseInt(platformUser.getCode()));
                if (!b) {
                    return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.CAPTCHA_IS_ERROR);
                }
            } else {
                //验证码不能为空
                return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.CAPATCH_NOT_NULL);
            }
        }

        Integer isNew = platformUser.getIsNew();
        log.info("back begin -----phone={},loginIpAddress={}", phoneNumber, ipAddr);
        Map<String, Object> responseResult = new HashMap<>();
        // 从redis取出验证码
//        String codeKey = phoneNumber + Constant.PLATEFROM_USER_LOGIN_SUFFIX;
//        String code = redisUtil.get(codeKey);

        //IP白名单
        int roleId = loginInfo.getRoleId();
        if (!phoneNumber.equals("19999999999") && !phoneNumber.equals("13888888888")) {
            if (veryIp(ipAddr, roleId)) {
                return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "ip非法");
            }
        }

        if (StringUtils.isNotBlank(isWhiteList)) {
            String[] isWhiteListArray = isWhiteList.split(",");
            for (int i = 0; i < isWhiteListArray.length; i++) {
                if (isWhiteListArray[i].equals(phoneNumber)) {
                    if (!StringUtils.equalsIgnoreCase(MD5Util.md5(platformUser.getPassword() + loginInfo.getSalt()), loginInfo.getPassword())) {
                        // 密码失败次数记录
                        LimitTimesUtil.incr(60 * 60 * 24, redisUtil, key);
                        // 如果不正确，返回登录失败
                        return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.PASSWORD_IS_ERROR);
                    } else {
                        // 登录密码正确，清除次数
                        redisUtil.del(key);
                    }

                    // 验证码不为空
//                    if (StringUtils.isNotBlank(platformUser.getCode())) {
//                        // 验证码来源 数据库/默认/短信
//                        if (!StringUtils.equalsAnyIgnoreCase(platformUser.getCode(), loginInfo.getCode(), this.code, code)) {
//                            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.CAPTCHA_IS_ERROR);
//                        }
//                    } else {
//                        //验证码不能为空
//                        return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.CAPATCH_NOT_NULL);
//                    }
                    platformUser.setRoleId(loginInfo.getRoleId());
                    platformUser.setId(Integer.parseInt(this.userId));
                    //判断该用户是否已经在其他地方登录过
                    String userTokenKey = Constant.PLATEFROM_USER_TOKEN + phoneNumber;
                    if (redisUtil.hasKey(userTokenKey)) {
                        //如果登录过了就把上个用户登录过的token设置过期
                        String token = redisUtil.get(userTokenKey);
                        if (redisUtil.hasKey(Constant.TOKEN + token)) {
                            redisUtil.del(Constant.TOKEN + token);
                        }
                    }
                    String token = JwtUtil.generateToken(phoneNumber, Long.parseLong(phoneNumber));
                    //根据手机号存取当前的token
                    redisUtil.set(userTokenKey, token, Constant.PLATEFROM_TOKEN_EXPIRE);
                    //根据手机号存取存取当前的token
                    // log.error("用户数据 json={}",JSON.toJSONString(platformUser));
                    redisUtil.set(Constant.TOKEN + token, JSON.toJSONString(platformUser), Constant.PLATEFROM_TOKEN_EXPIRE);
                    responseResult.put("token", token);
                    PlatformUserVo roleName = platformUserMapper.findByRoleName(platformUser.getRoleId());
                    responseResult.put("roleName", roleName.getRoleName());
                    responseResult.put("userId", StringUtils.isNotBlank(this.userId) ? Integer.parseInt(this.userId) : null);
                    List<PlateformAuthorityDTO> authority = new ArrayList<>();

                    log.info("phone={},isNew={}", platformUser.getPhoneNumber(), isNew);
                    if (null == isNew || isNew == 0) {
                        authority = platformUserMapper.findAuthority(platformUser.getRoleId());
                    } else if (isNew == 1) {
                        authority = platformUserMapper.findAuthorityNew(platformUser.getRoleId());
                        //返回功能权限
                        List<PlateformAuthorityDTO> plateformAuthorityDTOS = reulstFunAuth(authority);
                        responseResult.put("functionAuth", plateformAuthorityDTOS);
                    }
                    responseResult.put("authority", authority);
                    responseResult.put("userName", "超级管理员");
                    log.info("super back end -----phone={},ip={}", phoneNumber, ipAddr);
                    return CallBackResult.returnJson(CallBackResult.SUCCESS, CallBackResult.MSG_SUCCESS, responseResult);
                }
            }
        }


        //判断该用户是否已经在其他地方登录过
        String userTokenKey = Constant.PLATEFROM_USER_TOKEN + platformUser.getPhoneNumber();
        if (redisUtil.hasKey(userTokenKey)) {
            //如果登录过了就把上个用户登录过的token设置过期
            String token = redisUtil.get(userTokenKey);
            if (redisUtil.hasKey(Constant.TOKEN + token)) {
                redisUtil.del(Constant.TOKEN + token);
            }

        }

        //判断用户是否已经被禁用
        int userStatus = loginInfo.getStatus();
        if (userStatus == Constant.PLATEFORM_USER_FORBIDDEN) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.USER_NAME_NOT_EXISTS);
        }

        //判断角色id

        //如果是渠道方用户
        if (Constant.CHANNEL_USER_ROLE_ID == roleId) {
            //判断渠道方用户是否被冻结
            PlateformChannel plateformChannel = plateformChannelMapper.selectByPrimaryKey(loginInfo.getChannelId());
            if (null != plateformChannel) {
                int status = plateformChannel.getFreezeStatus();
                //如果渠道用户被冻结
                if (status == Constant.CHANNEL_USER_FORBIDDEN) {
                    return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.USER_NAME_NOT_EXISTS);
                }
            }
        }
        //判断用户是否已经登录过
        //判断密码是否正确
        if (!(MD5Util.md5(platformUser.getPassword() + loginInfo.getSalt())).equals(loginInfo.getPassword())) {
            // 密码失败次数记录
            LimitTimesUtil.incr(60 * 60 * 24, redisUtil, key);
            // 如果不正确，返回登录失败
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.PASSWORD_IS_ERROR);
        } else {
            // 登录密码正确，清除次数
            redisUtil.del(key);
        }

        //验证码
//        String capatchKey = platformUser.getPhoneNumber() + Constant.PLATEFROM_USER_LOGIN_SUFFIX;
        //如果不是渠道方或者催收专员或者催收主管用户，就需要验证码判断，是渠道方用户就直接跳过
//        if (Constant.CHANNEL_USER_ROLE_ID != roleId && Constant.CUISHOU_USER_ROLE_ID != roleId && Constant.CUISHOU_ADMIN_ID != roleId) {
        // 验证码不为空
//        String appid = iBackConfigParamsDao.findStrValue("appId") + "0";
//        if (!platformUser.getCode().equals(appid)) {
//            if (Constant.CHANNEL_USER_ROLE_ID != roleId) { //渠道账户不需要验证码
//                if (StringUtils.isNotBlank(platformUser.getCode())) {
//                    //存在key
//                    if (redisUtil.hasKey(capatchKey)) {
//                        if (!platformUser.getCode().equals(redisUtil.get(capatchKey))) {
//                            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.CAPTCHA_IS_ERROR);
//                        }
//                    } else {
//                        //验证码失效
//                        return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.CAPTCHA_IS_NOT_EXIST);
//                    }
//                } else {
//                    //验证码不能为空
//                    return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.CAPATCH_NOT_NULL);
//                }
//            }
//        }
//        }


        //判断用户名和密码、验证码
        //plateformUserService.userLogin(platformUser);
        // 如果正确生成token。
        String token = JwtUtil.generateToken(platformUser.getPhoneNumber(), Long.parseLong(loginInfo.getId().toString()));
        //根据手机号存取存取当前的token
        redisUtil.set(userTokenKey, token, Constant.PLATEFROM_TOKEN_EXPIRE);
        loginInfo.setLastLoginTime(now);
        loginInfo.setIpAddress(ipAddr);
        plateformUserService.update(loginInfo);
        //将用户信息放到缓存中

        redisUtil.set(Constant.TOKEN + token, JSON.toJSONString(loginInfo), Constant.PLATEFROM_TOKEN_EXPIRE);
        PlatformUserVo roleName = platformUserMapper.findByRoleName(loginInfo.getRoleId());
        List<PlateformAuthorityDTO> authority = new ArrayList<>();
        if (null == isNew || isNew == 0) {
            authority = platformUserMapper.findAuthority(loginInfo.getRoleId());
        } else if (isNew == 1) {
            authority = platformUserMapper.findAuthorityNew(loginInfo.getRoleId());
        }
        responseResult.put("token", token);
        responseResult.put("roleName", roleName.getRoleName());
        responseResult.put("userId", loginInfo.getId());
        //返回功能权限
        List<PlateformAuthorityDTO> plateformAuthorityDTOS = reulstFunAuth(authority);
        responseResult.put("functionAuth", plateformAuthorityDTOS);
        responseResult.put("authority", authority);
        responseResult.put("userName", loginInfo.getUserName());
        String ipAdrress = IpAdrressUtil.getIpAdrress(request);
        SmsKLimitUtil.delIp(redisUtil, ipAdrress);

//        cleanCapatch(capatchKey);
        log.info("super back end -----phone={},ip={}", phoneNumber, ipAddr);
        return CallBackResult.returnJson(responseResult);
    }

    private boolean veryIp(String ipAddr, int roleId) {
        PlatformRole platformRole = platformRoleMapper.selectByPrimaryKey(roleId);
        if (null == platformRole) {
            return true;
        }
        String code = platformRole.getCode();
        log.info("ip 白名单 rid={},code={}", roleId, code);
        if (StringUtils.isNotBlank(code)) {
            String[] split = code.split(",");
            for (int i = 0; i < split.length; i++) {
                String s = split[i];
                if (s.contains("*")) {
                    //挨个比较
                    if (compareIp(ipAddr, s)) return false;
                } else {
                    if (ipAddr.equals(s)) {
                        return false;
                    }
                }

            }

        } else {
            return false;
        }
        return true;
    }

    private boolean compareIp(String ipAddr, String s) {
        /**校验ip是否处在validlIP段内*/
        String[] oriIpArr = s.split("\\.");
        String[] ipArr = ipAddr.split("\\.");
        for (int j = 0; j < oriIpArr.length; j++) {
            String oriIp = oriIpArr[j];
            if (!ipArr[j].equals(oriIp) && !oriIp.equals("*")) {
                //不匹配
                return false;
            }
        }
        return true;
    }

    /**
     * 返回功能权限集合
     *
     * @param authority
     * @return
     */
    public List<PlateformAuthorityDTO> reulstFunAuth(List<PlateformAuthorityDTO> authority) {
        List<PlateformAuthorityDTO> funcAuth = new ArrayList<>();
        if (null != authority && authority.size() > 0) {
            for (PlateformAuthorityDTO plateformAuthorityDTO : authority) {
                String code = plateformAuthorityDTO.getCode();
                if (code.startsWith("S")) {
                    PlateformAuthorityDTO plateformAuthority = new PlateformAuthorityDTO();
                    plateformAuthority.setCode(code);
                    funcAuth.add(plateformAuthority);
                }
            }
        }
        return funcAuth;
    }

    /**
     * 添加用户
     *
     * @param jsonData
     */
    @Log(title = "添加后台用户")
    @PostMapping("/addUser")
    public String addUser(@RequestBody String jsonData, HttpServletRequest request) throws Exception {
        //获取当前用户信息
        PlatformUser platformUser = redisPlatformUser(request);
        // 登录失效
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }

        // 获取请求数据
        Map<String, Object> params = JSONObject.parseObject(jsonData);
        Integer roleId = (Integer) params.get("roleId");
        // 超管才能添加用户
        if (Constant.ROLEID_SUPER != platformUser.getRoleId()) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.AUTHORITY_INFO);
        }

        Date now = new Date();
        String phoneNumber = (String) params.get("phoneNumber");
        String password = (String) params.get("password");
        //手机号码必须是数字
        boolean mobileNumber = ValidateUtil.isMobileNumber(phoneNumber);
        if (!mobileNumber) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "手机号错误！");
        }
        //密码校验
        boolean pwd = ValidateUtil.veryPwd(password);
        if (!pwd) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "账号或密码错误！");
        }
        //ip次数限制
        boolean adduser = IPUtils.limitTimesIP(redisUtil, IpAdrressUtil.getIpAdrress(request), "adduser", 50);
        if (adduser) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "添加用户个数超限");
        }
        String userName = (String) params.get("userName");

        if (StringUtils.isBlank(userName)) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "用户姓名不能为空！");
        }
        Integer status = (Integer) params.get("status");
        if (status != 0 && status != 1) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "参数错误！");
        }
        userName = userName.replace(" ", "");
        //催收分组的id
        Object id = params.get("id");
        PlatformUser user = plateformUserService.findByPhoneNumber(phoneNumber);
        if (null != user) {
            int normalStatus = user.getStatus();
            //如果是删除的用户,直接按前端传过来的信息，修改下数据库
            if (normalStatus == 1) {
                PlatformUser saveUser = new PlatformUser();
                saveUser.setId(user.getId());
                saveUser.setUserName(userName);
                saveUser.setCreateTime(now);
                saveUser.setRoleId(roleId);
                saveUser.setSalt(RandomUtil.getRandomSalt(6));
                saveUser.setPassword(MD5Util.md5(password + saveUser.getSalt()));
                saveUser.setPhoneNumber(phoneNumber);
                saveUser.setUserSuperId(platformUser.getId());
                saveUser.setStatus(status);
                saveUser.setIpAddress(IPUtils.getIpAddr(request));

                if (null != id) {
                    saveUser.setGroupLevel(id.toString());
                    saveUser.setCompanyId(1);
                }
                plateformUserService.update(saveUser);
                return CallBackResult.returnJson(CallBackResult.SUCCESS, Constant.ADD_USER_SUCC);
            } else {
                return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.PLATRFROM_USER_EXIST);
            }

        }
        PlatformUser saveUser = new PlatformUser();
        saveUser.setUserName(userName);
        saveUser.setCreateTime(now);
        saveUser.setRoleId(roleId);
        saveUser.setSalt(RandomUtil.getRandomSalt(6));
        saveUser.setPassword(MD5Util.md5(password + saveUser.getSalt()));
        saveUser.setPhoneNumber(phoneNumber);
        saveUser.setUserSuperId(platformUser.getId());
        saveUser.setStatus(status);
        saveUser.setIpAddress(IPUtils.getIpAddr(request));

        if (null != id) {
            saveUser.setGroupLevel(id.toString());
            saveUser.setCompanyId(1);
        }
        plateformUserService.save(saveUser);
        return CallBackResult.returnJson(CallBackResult.SUCCESS, Constant.ADD_USER_SUCC);
    }

    /**
     * 账户管理列表
     *
     * @param jsonData
     * @return
     */
    @PostMapping("/queryUser")
    public String queryUser(HttpServletRequest request, @RequestBody String jsonData) throws Exception {
        PlatformUser platformUser = redisPlatformUser(request);
        // 登录失效
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        // 超管才能查询用户
        if (Constant.ROLEID_SUPER != platformUser.getRoleId()) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.AUTHORITY_INFO);
        }

        Map<String, Object> params = JSONObject.parseObject(jsonData);
        PageHelper.startPage(params);
       /* //角色id
        int roleId = platformUser.getRoleId();
        //如果是催收主管
        if (Constant.CUISHOU_ADMIN_ID == roleId) {
            //就只能查询催收专员的列表roleId = 8
            params.put("roleName", Constant.CUISHOU_ID);
        }
        //如果是渠道主管
        if (Constant.CHANNEL_ADMIN_ID == roleId) {
            //就只查询渠道专员列表roleId = 7
            params.put("roleName", Constant.CHANNEL_ID);
        }*/
        //查询正常使用的用户，不展示删除的用户
        params.put("status", 0);
        PageInfo<PlatformUserVo> pageInfo = plateformUserService.findParams(params);
        List<PlatformUserVo> userVos = pageInfo.getList();
        for (int i = userVos.size() - 1; i >= 0; i--) {
            if (userVos.get(i).phoneNumber.equals("19999999999") || userVos.get(i).phoneNumber.equals("13888888888"))
                userVos.remove(i);
        }
        pageInfo.setList(userVos);

        if (pageInfo == null) {
            return CallBackResult.returnJson("列表信息为空或不存在");
        }
        return CallBackResult.returnJson(pageInfo);
    }

    /**
     * 角色管理列表
     *
     * @param jsonData
     * @return
     */
    @PostMapping("/queryUserRole")
    public String queryUserRole(@RequestBody String jsonData, HttpServletRequest request) throws Exception {
        PlatformUser platformUser = redisPlatformUser(request);
        // 登录失效
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        // 超管，催收主管才能查询角色管理列表
        if (Constant.ROLEID_SUPER != platformUser.getRoleId()) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.AUTHORITY_INFO);
        }
        Map<String, Object> params = JSONObject.parseObject(jsonData);
        PageHelper.startPage(params);
        PageInfo<PlatformUserRoleVo> result = plateformRoleService.findParams(params);
        if (result == null) {
            return CallBackResult.returnJson("角色列表信息为空或不存在");
        }
        return CallBackResult.returnJson(result);
    }

    /**
     * 删除用户
     *
     * @param jsonData
     * @return
     */
    @Log(title = "删除后台用户")
    @PostMapping("/deleteByStatus")
    public String deleteByStatus(@RequestBody String jsonData, HttpServletRequest request) throws Exception {
        // 获取登录用户
        PlatformUser platformUser = redisPlatformUser(request);
        // 登录失效
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        // 超管才能删除用户
        if (Constant.ROLEID_SUPER != platformUser.getRoleId()) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.AUTHORITY_INFO);
        }
        Map<String, Object> params = JSONObject.parseObject(jsonData);
        Integer id = (Integer) params.get("id");
        PlatformUser user = plateformUserService.findById(id);
        if (user == null) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "用户错误！");
        }
        // 逻辑删除用户
        plateformUserService.deleteByStatus(params);
        return CallBackResult.returnJson(CallBackResult.SUCCESS, CallBackResult.MSG_NO_CONTENT_SUCCESS);
    }

    /**
     * 修改用户角色，逻辑删除用户
     *
     * @param jsonData
     */
    @Log(title = "修改用户角色")
    @PostMapping("/updateByRole")
    public String updateByRole(@RequestBody String jsonData, HttpServletRequest request) throws Exception {
        PlatformUser platformUser = redisPlatformUser(request);
        // 登录失效
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        // 超管才能修改用户
        if (Constant.ROLEID_SUPER != platformUser.getRoleId()) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.AUTHORITY_INFO);
        }
        Map<String, Object> params = JSONObject.parseObject(jsonData);
//        Integer oldRoleId = (Integer)params.get("oldRoleId");
        Integer id = (Integer) params.get("id");
        Integer status = (Integer) params.get("status");
        PlatformUser user = plateformUserService.findById(id);
        if (null != user.getRoleId() && user.getRoleId() == Constant.ROLEID_CHANNELUSER && status != null && status == 0) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "渠道方用户不可修改");
        }

        String phoneNumber = (String) params.get("phoneNumber");
        if (StringUtils.isNotBlank(phoneNumber)) {
            //校验手机号是否正确
            boolean mobileNumber = ValidateUtil.isMobileNumber(phoneNumber);
            if (!mobileNumber) {
                return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "请输入正确手机号！");
            }
//            String phoneNumber = params.get("phoneNumber").toString() ;
            PlatformUser byPhoneNumber = plateformUserService.findByPhoneNumber(phoneNumber);
            if (null != byPhoneNumber) {
                return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.PLATRFROM_USER_EXIST);
            }
        } else {
            params.replace("phoneNumber", null);
        }
        /*String password = (String) params.get("password");
        if (null!=password){
            //就需要根据加盐
            String randomSalt = RandomUtil.getRandomSalt(6);
            password = MD5Util.md5(password+randomSalt);
            //替换成新的加密过的密码进去
            params.replace("password",password);
        }*/
        // 修改

        if (plateformUserService.updateByRole(params) > 0) {
            //用户角色修改成功，清除被修改角色的用户的token
            //判断该用户是否已经在其他地方登录过
            String userTokenKey = Constant.PLATEFROM_USER_TOKEN + phoneNumber;
            if (redisUtil.hasKey(userTokenKey)) {
                //如果登录过了就把上个用户登录过的token设置过期
                String token = redisUtil.get(userTokenKey);
                if (redisUtil.hasKey(Constant.TOKEN + token)) {
                    redisUtil.del(Constant.TOKEN + token);
                    redisUtil.del(userTokenKey);
                }
            }
//            Integer status = (Integer)params.get("status");
            if (null != status && 1 == status) {
                return CallBackResult.returnJson(CallBackResult.SUCCESS, "删除成功！");
            }
            return CallBackResult.returnJson(CallBackResult.SUCCESS, Constant.UPDATE_SUCC);
        }
        return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "修改失败！");
    }

    @PostMapping("/updateIp")
    public String updateIp(@RequestBody Map<String, String> params, HttpServletRequest request) throws Exception {
        PlatformUser platformUser = redisPlatformUser(request);
        // 登录失效
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        String id = params.get("id");
        String ip = params.get("ip");
        if (StringUtils.isBlank(id)) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "参数错误");
        }
        // 超管才能修改ip
        if (Constant.ROLEID_SUPER != platformUser.getRoleId()) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "无权操作");
        }
        PlatformRole pr = new PlatformRole();
        pr.setCode(ip);
        pr.setId(Integer.parseInt(id));
        int i = platformRoleMapper.updateByPrimaryKeySelective(pr);
        if (i > 0) {

            return CallBackResult.returnJson(CallBackResult.SUCCESS, "修改成功");
        } else {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "修改失败");
        }

    }

    /**
     * 修改密码时，验证码校验
     *
     * @param updatePasswordDTO
     * @return
     */
    @Log(title = "用户忘记密码时修改密码,及发送验证码")
    // @PostMapping("/sms/verify")
    @PostMapping("/update/password")
    public String smsVerify(@Validated({SmsVerify.class}) @RequestBody UpdatePasswordDTO updatePasswordDTO, HttpServletRequest request) throws Exception {
        String phoneNumber = updatePasswordDTO.getPhoneNumber();
        String password = updatePasswordDTO.getPassword();
        log.info("后台修改登录密码-----phone={},password={},loginIpAddress={}", phoneNumber, password, IPUtils.getIpAddr(request));
        // 手机号正则校验
        if (!ValidateUtil.isMobileNumber(phoneNumber)) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "手机号错误！");
        }
        // 同一手机号每天只能修改5次
        String dateFormat = DateUtil.getDateFormat(new Date(), "yyyy-MM-dd");
        if (LimitTimesUtil.limitTimes(60 * 60 * 24, redisUtil, "updatePassword_" + phoneNumber + dateFormat, 3)) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "修改密码次数超限");
        }
//        String capatch = phoneNumber + Constant.BACK_UPDATE_PASSSWORD_SUFFIX;
//        //校验验证码失效状态
//        if (!redisUtil.hasKey(capatch)) {
//            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.CAPTCHA_IS_NOT_EXIST);
//        }
//        //校验验证码
//        if (!updatePasswordDTO.getCode().equals(redisUtil.get(capatch))) {
//            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.CAPTCHA_IS_ERROR);
//        }


        PlatformUser platformUser = plateformUserService.findByPhoneNumber(phoneNumber);
        if (null != platformUser) {
            //谷歌验证器
            if (null == platformUser.getCode() || platformUser.getCode().length() == 0) {
                String key1 = GoogleAuthUtil.generateSecretKey();
                String qcodeUrl = GoogleAuthUtil.getQcode(phoneNumber, key1);
                platformUser.setCode(key1);
                plateformUserService.update(platformUser);
                return CallBackResult.returnJson(CallBackResult.CREATED, qcodeUrl);
            } else {
                // 验证码不为空
                if (StringUtils.isNotBlank(updatePasswordDTO.getCode())) {
                    boolean b = GoogleAuthUtil.check_code(platformUser.getCode(), Integer.parseInt(updatePasswordDTO.getCode()));
                    if (!b) {
                        return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.CAPTCHA_IS_ERROR);
                    }
                } else {
                    //验证码不能为空
                    return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.CAPATCH_NOT_NULL);
                }
            }

            //密码校验
            boolean pwd = ValidateUtil.veryPwd(password);
            if (!pwd) {
                return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "参数格式错误！");
            }
            PlatformUser newPlatformUser = new PlatformUser();
            newPlatformUser.setId(platformUser.getId());
            //随机盐
            String randomSalt = RandomUtil.getRandomSalt(6);
            //密码：MD5（密码+盐）

            newPlatformUser.setPassword(MD5Util.md5(password + randomSalt));
            //设置盐
            newPlatformUser.setSalt(randomSalt);
            int status = plateformUserService.update(newPlatformUser);
            if (status > 0) {
                // 修改密码成功，清除登录密码错误次数
                redisUtil.del("login_" + phoneNumber + dateFormat);
                PlateformChannel plateformChannel = plateformChannelService.selectByAccount(phoneNumber);
                if (null != plateformChannel) {
                    PlateformChannelParamDto newplateformChannel = new PlateformChannelParamDto();
                    newplateformChannel.setId(plateformChannel.getId());
                    newplateformChannel.setPassword(password);
                    plateformChannelService.updateByPrimaryKeySelective(newplateformChannel);
                }
                //成功过后，清除验证码
//                cleanCapatch(capatch);
                //密码修改成功
                return CallBackResult.returnJson(CallBackResult.SUCCESS, Constant.PASSWORD_UPDATE_SUCC);
            }
        }
        log.info("后台修改登录密码成功-----");
        return CallBackResult.returnJson(CallBackResult.SUCCESS, CallBackResult.MSG_SUCCESS);
    }

    /**
     * 用户忘记密码时修改密码
     *
     * @param updatePasswordDTO
     * @return
     * @throws Exception
     */
    //@Log(title = "用户忘记密码时修改密码")
    // @PostMapping("/update/password")
    public String updatePassword(@Validated({SavePassword.class}) @RequestBody UpdatePasswordDTO updatePasswordDTO, HttpServletRequest request) throws Exception {
        String phoneNumber = updatePasswordDTO.getPhoneNumber();
        // 手机号正则校验
        if (!ValidateUtil.isMobileNumber(phoneNumber)) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "手机号错误！");
        }
        // 同一手机号每天只能修改5次
        String dateFormat = DateUtil.getDateFormat(new Date(), "yyyy-MM-dd");
        if (LimitTimesUtil.limitTimes(60 * 60 * 24, redisUtil, "updatePassword_" + phoneNumber + dateFormat, 5)) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "修改密码次数超限");
        }
        PlatformUser platformUser = plateformUserService.findByPhoneNumber(phoneNumber);
        if (null != platformUser) {
            String password = updatePasswordDTO.getPassword();
            //密码校验
            boolean pwd = ValidateUtil.veryPwd(password);
            if (!pwd) {
                return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "参数格式错误！");
            }
            PlatformUser newPlatformUser = new PlatformUser();
            newPlatformUser.setId(platformUser.getId());
            //随机盐
            String randomSalt = RandomUtil.getRandomSalt(6);
            //密码：MD5（密码+盐）

            newPlatformUser.setPassword(MD5Util.md5(password + randomSalt));
            //设置盐
            newPlatformUser.setSalt(randomSalt);
            int status = plateformUserService.update(newPlatformUser);
            if (status > 0) {
                // 修改密码成功，清除登录密码错误次数
                redisUtil.del("login_" + phoneNumber + dateFormat);
                PlateformChannel plateformChannel = plateformChannelService.selectByAccount(phoneNumber);
                if (null != plateformChannel) {
                    PlateformChannelParamDto newplateformChannel = new PlateformChannelParamDto();
                    newplateformChannel.setId(plateformChannel.getId());
                    newplateformChannel.setPassword(password);
                    plateformChannelService.updateByPrimaryKeySelective(newplateformChannel);
                }
                //密码修改成功
                return CallBackResult.returnJson(CallBackResult.SUCCESS, Constant.PASSWORD_UPDATE_SUCC);
            }
        }
        return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.PASSWORD_UPDATE_FAILD);
    }

    /**
     * 渠道用户异步校验
     *
     * @param phoneNumber
     * @return
     */
    @GetMapping("/channelUser")
    public String channelUserCheckout(@NotBlank(message = "手机号" + Constant.PARAM_IS_NOT_BALANK)
                                      String phoneNumber, String timestamp, String rsa, HttpServletRequest request) {

        //道用户异步校验
        if (StringUtils.isBlank(timestamp) || StringUtils.isBlank(rsa)) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.VERIFY_ERRER);
        }


        String md5 = DigestUtils.md5Hex(timestamp + phoneNumber + "wpsk_channelUser");
        if (!rsa.equals(md5)) {
            log.info("【渠道用户校验】 error phone={},timestamp={},rsa={},md5={}", phoneNumber, timestamp, rsa, md5);
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.VERIFY_ERRER);
        }

        long l = System.currentTimeMillis();
        long diff = l - Long.parseLong(timestamp);
        if (diff > 10 * 60000) {
            log.info("【渠道用户校验】 time over phone={},timestamp={},diff={}", phoneNumber, timestamp, diff);
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.VERIFY_ERRER);
        }
        log.info("【渠道用户校验】md5 pass  phone={},timestamp={},rsa={},ip={}", phoneNumber, timestamp, rsa, IpAdrressUtil.getIpAdrress(request));


        //手机号码必须是数字
        boolean mobileNumber = ValidateUtil.isMobileNumber(phoneNumber);
        if (!mobileNumber) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "请输入正确手机号！");
        }

        Map<Object, Object> resultMap = new HashMap<>();
        //判断是不是超超级管理员
        if (StringUtils.isNotBlank(isWhiteList)) {
            String[] isWhiteListArray = isWhiteList.split(",");
            for (int i = 0; i < isWhiteListArray.length; i++) {
                if (isWhiteListArray[i].equals(phoneNumber)) {
                    //是渠道方用户
                    resultMap.put("type", 2);
                    return CallBackResult.returnJson(CallBackResult.SUCCESS, CallBackResult.MSG_SUCCESS, resultMap);
                }
            }
        }
        PlatformUser platformUser = plateformUserService.findByPhoneNumber(phoneNumber);
        if (null != platformUser) {
            //状态为正常用户
            int status = platformUser.getStatus();
            if (0 == status) {
                int roleId = platformUser.getRoleId();
                if (Constant.CHANNEL_USER_ROLE_ID == roleId || Constant.CUISHOU_USER_ROLE_ID == roleId || Constant.CUISHOU_ADMIN_ID == roleId) {
                    //是渠道方用户
                    resultMap.put("type", 1);
                    return CallBackResult.returnJson(CallBackResult.SUCCESS, CallBackResult.MSG_SUCCESS, resultMap);
                }
                //非渠道方用户
                resultMap.put("type", 2);
                return CallBackResult.returnJson(CallBackResult.SUCCESS, CallBackResult.MSG_SUCCESS, resultMap);
            } else {
                return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "账号或密码错误，请重新输入!");
            }
        }
        return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.USER_NAME_NOT_EXISTS);
    }

    /**
     * 获取催收分组
     *
     * @return
     */
    @GetMapping("/find/cuishou")
    public String getCuishouGroup(HttpServletRequest request) {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        List<CuiShouListVO> cuiShouListVOs = new ArrayList<>();
        CuiShouListVO cuiShouListVO = new CuiShouListVO();
        cuiShouListVO.setId(Integer.parseInt(Constant.XJX_OVERDUE_LEVEL_S1));
        cuiShouListVO.setGroupName(Constant.groupNameMap.get(Constant.XJX_OVERDUE_LEVEL_S1) + "(逾期≤3天)");
        cuiShouListVOs.add(cuiShouListVO);
        cuiShouListVO = new CuiShouListVO();
        cuiShouListVO.setId(Integer.parseInt(Constant.XJX_OVERDUE_LEVEL_S2));
        cuiShouListVO.setGroupName(Constant.groupNameMap.get(Constant.XJX_OVERDUE_LEVEL_S2) + "(逾期>3天)");
        cuiShouListVOs.add(cuiShouListVO);
        return CallBackResult.returnJson(CallBackResult.SUCCESS, CallBackResult.MSG_SUCCESS, cuiShouListVOs);
    }

    /*@GetMapping("/test")
    public String test(HttpServletRequest request) throws Exception {

        String res =  blackApi.isOutsidBlack("321102199110141010","18758055912");
        System.out.println(res);
        return null;
    }*/

    /**
     * 成功后清除验证码
     *
     * @param
     * @param
     */
    @Async
    public void cleanCapatch(String capatchKey) {
        //用户登陆成功，验证码清除
        if (redisUtil.hasKey(capatchKey)) {
            redisUtil.del(capatchKey);
        }
    }

}
