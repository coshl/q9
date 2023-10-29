package com.summer.web.controller.plateformChannel;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.summer.annotation.Log;
import com.summer.web.controller.BaseController;
import com.summer.dao.entity.LoanRuleConfig;
import com.summer.dao.entity.PlateformChannel;
import com.summer.dao.entity.PlatformChannelStaff;
import com.summer.dao.entity.PlatformUser;
import com.summer.dao.mapper.*;
import com.summer.dao.entity.dailystatics.DailyStatisticsChannelProduct;
import com.summer.dao.mapper.dailystatistics.DailyStatisticsChannelProductDAO;
import com.summer.pojo.dto.PlateformChannelDTO;
import com.summer.pojo.dto.PlateformChannelParamDto;
import com.summer.api.service.IPlateformChannelService;
import com.summer.api.service.IUserMoneyRateService;
import com.summer.util.*;
import com.summer.util.encrypt.AESDecrypt;
import com.summer.pojo.vo.BackConfigParamsVo;
import com.summer.pojo.vo.ChannelNameListVo;
import com.summer.pojo.vo.ChannelStaffVo;
import com.summer.pojo.vo.PlateformChannelListVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.*;

/**
 * 渠道商Controller
 */
@Slf4j
@RestController
@RequestMapping("/v1.0/api/platformChannel")
public class PlateformChannelController extends BaseController {


    @Resource
    private IPlateformChannelService plateformChannelService;

    @Resource
    private PlateformChannelMapper plateformChannelMapper;
    @Resource
    private PlatformUserMapper platformUserMapper;
    @Resource
    private PlatformChannelStaffDAO platformChannelStaffDAO;

    @Resource
    private IBackConfigParamsDao backConfigParamsDao;
    @Resource
    private LoanRuleConfigDAO loanRuleConfigDAO;

    @Resource
    private IUserMoneyRateService userMoneyRateService;

    @Resource
    private RedisUtil redisUtil;

    /**
     * 进量统计
     **/
    @Resource
    private DailyStatisticsChannelProductDAO dailyStatisticsChannelProductDAO;


    /**
     * 渠道列表
     *
     * @param jsonData
     * @return
     */
    @PostMapping("/query")
    public String query(@RequestBody String jsonData, javax.servlet.http.HttpServletRequest request) {
        Map<String, Object> params = JSONObject.parseObject(jsonData);

        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        // 超管、渠道主管、渠道专员可以查询
        Integer roleId = platformUser.getRoleId();
        if (roleId != Constant.ROLEID_SUPER && roleId != Constant.CHANNEL_ADMIN_ROLE_ID && roleId != Constant.OPERATOR_ROLE_ID) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "没有权限");
        }
        //如果角色为运营人人员，就得根据对应运营人员去查询
        if (roleId == Constant.OPERATOR_ROLE_ID) {
            params.put("plateformUserId", platformUser.getId());
        }
        //开启分页
        PageHelper.startPage(params);
        PageInfo<PlateformChannelListVo> listParams = plateformChannelService.findListParams(params);
        return CallBackResult.returnJson(listParams);
    }

    /**
     * 更新渠道状态
     *
     * @param channelStatusDto
     * @return
     * @throws Exception
     */
    @Log(title = "修改渠道信息")
    @PostMapping("/updateByStatus")
    @Transactional
    public String updateByStatus(@RequestBody PlateformChannelParamDto channelStatusDto, HttpServletRequest request) throws Exception {
        PlatformUser platformUserInfo = redisPlatformUser(request);
        if (null == platformUserInfo) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        if (null == channelStatusDto.getId()) {
            return CallBackResult.returnJson(CallBackResult.PARAM_IS_ERROR, CallBackResult.PARAM_IS_EXCEPTION_MSG);
        }
        log.info("渠道号channelStatusDto.getId()============================================="+channelStatusDto.getId());
        PlateformChannel plateformChannel = plateformChannelMapper.selectByPrimaryKeyToUpdate(channelStatusDto.getId());
        if (null == plateformChannel) {
            return CallBackResult.returnJson(CallBackResult.PARAM_IS_ERROR, CallBackResult.PARAM_IS_EXCEPTION_MSG);
        }
        String channelName = channelStatusDto.getChannelName();

        if (null != channelStatusDto.getPrice()) {
            BigDecimal price = new BigDecimal((channelStatusDto.getPrice() * Constant.CENT_CHANGE_DOLLAR));
            channelStatusDto.setPriceInt(price.intValue());
        }
        //判断是不是100的整数倍
        if (!FigureUtil.isMulByhundred(channelStatusDto.getLoanAmount())) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "借款金额必须是100的整数倍");
        }
        //借款金额下线上线限制
        String moneylimit = moneylimit(channelStatusDto.getLoanAmount());
        if (null != moneylimit) {
            return moneylimit;
        }


        if (StringUtils.isNotBlank(channelName)) {
            //判断是否与之前的渠道名称相同
            if (!channelName.equals(plateformChannel.getChannelName())) {
                //判断渠道是否已经存在
                //如果传进来得渠道名称不同，说明渠道名被修改了，就得通过渠道名去查看是否已经存在了
                PlateformChannel plateformChannelInfo = plateformChannelService.selectByChannelName(channelName);
                if (null != plateformChannelInfo) {
                    return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.CHANNELNAME_EXIST);
                }
            }
        }
        //该渠道的手机账户id
        Integer plateUserId = null;

        String managerPhone = channelStatusDto.getManagerPhone();
        //加密手机号
        if (StringUtils.isNotBlank(managerPhone)) {
            channelStatusDto.setAccount(managerPhone);
            //如果账户与之前得账户相等，就不用再更新了
            if (!managerPhone.equals(plateformChannel.getAccount())) {
                //如果原账户与现在的账户不相等，就看是否已经存在了，如果存在了就不能再添加了
                PlatformUser byPhoneNumber = platformUserMapper.findByPhoneNumber(managerPhone);
                if (null != byPhoneNumber) {
                    return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.PLATRFROM_USER_EXIST);
                } else {
                    //如果新账户不存在，就把原来的账户的用户Id查询，通过Id进行修改用户信息
                    PlatformUser byPhoneNumberInfo = platformUserMapper.findByPhoneNumber(plateformChannel.getAccount());
                    if (null != byPhoneNumberInfo) {
                        plateUserId = byPhoneNumberInfo.getId();
                    }
                }
            }
        }
        //如果风控分数为null就修改分数为0
        String riskScore = channelStatusDto.getRiskScore();
        if (StringUtils.isBlank(riskScore)) {
            channelStatusDto.setRiskScore("0");
        }
        // channelStatusDto.setPlateformUserId(platformUserInfo.getId());

        //如果渠道开启直接拒绝的，同时下线,不再统计进量
        Integer riskSwitch = channelStatusDto.getRiskSwitch();
        if (null != riskSwitch) {
            if (riskSwitch == 1) {
                channelStatusDto.setStatus(0);
            } else {
                channelStatusDto.setStatus(1);
            }
        }
        int isSucc = plateformChannelService.updateByPrimaryKeySelective(channelStatusDto);
        if (isSucc > 0) {
            if (null != plateUserId) {
                String password = channelStatusDto.getPassword();
                PlatformUser platformUser = new PlatformUser();
                platformUser.setId(plateUserId);
                //账户
                platformUser.setPhoneNumber(managerPhone);
                if (StringUtils.isNotBlank(password)) {
                    //随机盐
                    String randomSalt = RandomUtil.getRandomSalt(6);
                    //密码：MD5（密码+盐）
                    platformUser.setPassword(MD5Util.md5(password + randomSalt));
                    //设置盐
                    platformUser.setSalt(randomSalt);
                }
                //ip地址
                platformUser.setIpAddress(IPUtils.getIpAddr(request));
                //用户名，就是渠道名称
                platformUser.setUserName(channelStatusDto.getChannelName());
                platformUserMapper.updateByPrimaryKeySelective(platformUser);
            }

            //合作模式
            Integer cooperationMode = channelStatusDto.getCooperationMode();
            //如果价格修改的话，我们需要修改
            if (null != channelStatusDto.getPriceInt() || null != cooperationMode) {
                Date date = new Date();
                //渠道人员统计价格更新
                PlatformChannelStaff platformChannelStaff = findPlatformChannelStaff(channelStatusDto.getChannelName(), date);
                if (null != platformChannelStaff) {
                    PlatformChannelStaff platformChannelStaffS = new PlatformChannelStaff();
                    platformChannelStaffS.setId(platformChannelStaff.getId());
                    //合作价格
                    platformChannelStaffS.setPrice(channelStatusDto.getPriceInt());
                    platformChannelStaffDAO.updateByPrimaryKeySelective(platformChannelStaffS);
                }
                //进量统计价格和合作模式
                DailyStatisticsChannelProduct channelProductExist = isChannelProductExist(channelStatusDto.getId(), date);
                if (null != channelProductExist) {
                    DailyStatisticsChannelProduct dailyStatisticsChannelProduct = new DailyStatisticsChannelProduct();
                    dailyStatisticsChannelProduct.setId(channelProductExist.getId());
                    //合作价格
                    dailyStatisticsChannelProduct.setPrice(channelStatusDto.getPriceInt());
                    if (null != cooperationMode) {
                        //合作模式
                        dailyStatisticsChannelProduct.setCooperationMode(cooperationMode);
                    }
                    dailyStatisticsChannelProductDAO.updateByPrimaryKeySelective(dailyStatisticsChannelProduct);
                }
            }
            /**TODO 00 根据渠道id修改对应费率  （该表并没有渠道id字段，无效功能）*/
            updateLoanRule(channelStatusDto);
            return CallBackResult.returnJson(CallBackResult.CREATED, Constant.UPDATE_SUCC);

        }
        return CallBackResult.returnJson(CallBackResult.INVALID_REQUEST, Constant.UPDATE_FAILD);
    }

    /**
     * 根据渠道名称，和时间查询
     *
     * @param channelName
     * @param nowTime
     * @return
     */
    public PlatformChannelStaff findPlatformChannelStaff(String channelName, Date nowTime) {
        Map<String, Object> param = new HashMap<>();
        param.put("nowTime", nowTime);
        param.put("channelName", channelName);
        return platformChannelStaffDAO.selectByReportTimeChannelId(param);
    }

    /**
     * 查询传进来的时间，和渠道id，查询该渠道的进量统计数据
     *
     * @param channelId
     * @param nowTime
     * @return
     */
    public DailyStatisticsChannelProduct isChannelProductExist(Integer channelId, Date nowTime) {
        Map<String, Object> param = new HashMap<>();
        param.put("nowTime", nowTime);
        param.put("channelId", channelId);
        return dailyStatisticsChannelProductDAO.selectByParams(param);
    }

    /**
     * 添加渠道
     *
     * @param plateformChannelDTO
     * @return
     * @throws Exception
     */
    @Log(title = "渠道添加")
    @PostMapping("/createChannel")
    public String createChannel(HttpServletRequest request, @Validated @RequestBody PlateformChannelDTO plateformChannelDTO) throws Exception {
        //获取当前用户信息
        PlatformUser platformUser = redisPlatformUser(request);
        if (null != platformUser) {

            if (platformUser.getRoleId() != Constant.ROLEID_SUPER && platformUser.getRoleId() != Constant.CHANNEL_ADMIN_ROLE_ID  ) {
                return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.UN_AUTH_VIST);
            }

            log.info("【渠道添加】-----phone={},ip={},param={}", platformUser.getPhoneNumber(), IpAdrressUtil.getIpAdrress(request), JSONObject.toJSONString(plateformChannelDTO));

            String channelName = plateformChannelDTO.getChannelName();
            if (StringUtils.isBlank(channelName)){
                return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "渠道名称不能为空！");
            }
            channelName = channelName.replace(" ","");
            //判断渠道是否已经存在
            PlateformChannel plateformChannel1 = plateformChannelService.selectByChannelName(channelName);
            if (null != plateformChannel1) {
                return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.CHANNELNAME_EXIST);
            }
            //管理人姓名不能为空
            String managerName = plateformChannelDTO.getManagerName();
            if (StringUtils.isNotBlank(managerName)){
                managerName = managerName.replace(" ","");
                plateformChannelDTO.setManagerName(managerName);
            }

            //风控分数不能小于总的系统默认分数
           /* String riskScore = plateformChannelDTO.getRiskScore();
            if (StringUtils.isNotBlank(riskScore)) {
                int risk = Integer.parseInt(riskScore);
                BackConfigParamsVo scoreGateway = backConfigParamsDao.findBySysKey(Constant.SCORE_GATEWAY);
                if (null != scoreGateway) {
                    if (risk < scoreGateway.getSysValue()) {
                        return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "修改的风控分数不能低于系统默认分数！");
                    }
                } else {
                    if (risk < 550) {
                        return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "修改的风控分数不能低于系统默认分数！");
                    }
                }
            }*/

            //判断是不是100的整数倍
            if (!FigureUtil.isMulByhundred(plateformChannelDTO.getLoanAmount())) {
                return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "借款金额必须是100的整数倍");
            }
            //借款金额下线上线限制
            String moneylimit = moneylimit(plateformChannelDTO.getLoanAmount());
            if (null != moneylimit) {
                return moneylimit;
            }
            //防撸开启检查
            /*Integer stripSwitch = plateformChannelDTO.getStripSwitch();
            if (null !=stripSwitch && stripSwitch ==0){
                BackConfigParamsVo channelStripSwitch = backConfigParamsDao.findBySysKey(Constant.CHANNEL_STRIP_SWITCH);
                if (null != channelStripSwitch){
                    //如果未开启防撸
                    if (channelStripSwitch.getSysValue() == 0 ){
                        return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "当前防撸未开通，请先申请防撸账号！！");
                    }
                }
            }*/

            //账户已经存在
            PlatformUser byPhoneNumber = platformUserMapper.findByPhoneNumber(plateformChannelDTO.getManagerPhone());
            if (null != byPhoneNumber) {
                return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.PLATRFROM_USER_EXIST);
            }
            plateformChannelDTO.setPlatefromUserId(platformUser.getId());
            int isSucc = plateformChannelService.insertChannel(plateformChannelDTO, request);
            if (isSucc > 0) {

                return CallBackResult.returnJson(CallBackResult.SUCCESS, Constant.CHANNEL_ADD_SUCC);
            }
            return CallBackResult.returnJson(CallBackResult.INVALID_REQUEST, Constant.CHANNEL_ADD_FAILD);
        }
        return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);

    }

    /**
     * 渠道人员统计
     *
     * @param jsonData
     * @return
     * @throws Exception
     */
    @PostMapping("/channelUser")
    public String channelUser(@RequestBody String jsonData, HttpServletRequest request) throws Exception {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        //超级管理员 渠道主管 渠道专员
        Integer roleId = platformUser.getRoleId();
        if (roleId !=  Constant.ADMIN_ROLE_ID && roleId != Constant.CHANNEL_ADMIN_ROLE_ID && roleId != Constant.CHANNEL_ID ){
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS,"您无权操作！");
        }

        Map<String, Object> params = JSONObject.parseObject(jsonData);
        //开启分页
        PageHelper.startPage(params);
        PageInfo<ChannelStaffVo> pageInfo = plateformChannelService.findParams(params);
        return CallBackResult.returnJson(pageInfo);
    }

    @Log(title = "渠道人员统计")
    @RequestMapping("/downloadChannelUserList")
    public void putChannelUserList(javax.servlet.http.HttpServletRequest request, HttpServletResponse response,
                                   @RequestBody String jsonData) throws Exception {

        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            returnLoginBad(response);
            return;
        }
        Integer roleId = platformUser.getRoleId();
        if (Constant.ROLEID_SUPER != roleId) {
            returnErrorAuth(response);
            return;
        }

        //判断下载通道开启关闭
        Integer downloadSwitch = downloadSwitch();
        if (null != downloadSwitch){
            downloadBad(response);
            return;
        }

        Map<String, Object> params = JSONObject.parseObject(jsonData);
        List<ChannelStaffVo> channelStaffList = platformChannelStaffDAO.findParams(params);
        String title = "渠道人员统计";
        BuildXLSX.setFileDownloadHeader(request, response, title);
        LinkedHashMap<String, String> titleMap = new LinkedHashMap<>();
        titleMap.put("reportTime", "日期");
        titleMap.put("userName", "运营人员");
        titleMap.put("deliveryConnection", "投放连接数");
        titleMap.put("reallyRegister", "真实注册数");
        titleMap.put("channelRegister", "渠道注册数");
        titleMap.put("loanCost", "放款成本/元");
        titleMap.put("registerCost", "注册成本/元");
        XlsxParam xlsxParam = new XlsxParam(channelStaffList, title, titleMap);
        OutputStream os = response.getOutputStream();
        BuildXLSX.buildExcel(xlsxParam, os);
    }

    /**
     * 根据请求id查询渠道
     *
     * @param params
     * @return
     */
    private PlateformChannel checkId(Map<String, Object> params) {
        Object idObj = params.get("id");
        if (null == idObj) {
            return null;
        }
        return plateformChannelMapper.selectByPrimaryKey(Integer.parseInt(idObj.toString()));
    }

    /**
     * 获取所有的渠道名称
     *
     * @return
     */
    @GetMapping("/getChannelName")
    public Object getChannelName(HttpServletRequest request) {
        PlatformUser platformUserInfo = redisPlatformUser(request);
        if (null == platformUserInfo) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }

        List<ChannelNameListVo> byChannelName = plateformChannelMapper.findByChannelName(null);
        return CallBackResult.returnJson(CallBackResult.SUCCESS, CallBackResult.MSG_SUCCESS, byChannelName);

    }

    /**
     * 获取全局分数
     * @return
     */
    @RequestMapping("/score")
    public Object getScore(HttpServletRequest request) {
        PlatformUser platformUserInfo = redisPlatformUser(request);
        if (null == platformUserInfo) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }

        if (platformUserInfo.getRoleId() != Constant.ROLEID_SUPER && platformUserInfo.getRoleId() != Constant.CHANNEL_ADMIN_ROLE_ID) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.UN_AUTH_VIST);
        }
        BackConfigParamsVo scoreGateway = backConfigParamsDao.findBySysKey("score_gateway");
        Map<String, Object> resultMap = new HashMap<>();
        if (null != scoreGateway) {
            resultMap.put("pbScore", scoreGateway.getSysValue());
        }
        BackConfigParamsVo scoreType = backConfigParamsDao.findBySysKey("mobile_switch");
        if (null != scoreType) {
            resultMap.put("scoreType", scoreType.getSysValue());
        }
        /*BackConfigParamsVo channelStripSwitch = backConfigParamsDao.findBySysKey(Constant.CHANNEL_STRIP_SWITCH);
        if (null != channelStripSwitch) {
            resultMap.put("channelStripSwitch", channelStripSwitch.getSysValue());
        }*/

        return CallBackResult.returnJson(CallBackResult.SUCCESS, CallBackResult.MSG_SUCCESS, resultMap);
    }

    /**
     * 更新渠道状态
     *
     * @param channelStatusDto
     * @return
     * @throws Exception
     */
    @Log(title = "修改渠道信息")
    @PostMapping("/updateChannel")
    @Transactional
    public String updateChannel(@RequestBody PlateformChannelParamDto channelStatusDto, HttpServletRequest request) throws Exception {
        PlatformUser platformUserInfo = redisPlatformUser(request);
        if (null == platformUserInfo) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }

        if (platformUserInfo.getRoleId() != Constant.ROLEID_SUPER && platformUserInfo.getRoleId() != Constant.CHANNEL_ADMIN_ROLE_ID  ) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.UN_AUTH_VIST);
        }

       log.info("【修改渠道信息】-----phone={},ip={},param={}", platformUserInfo.getPhoneNumber(), IpAdrressUtil.getIpAdrress(request), JSONObject.toJSONString(channelStatusDto));
        if (null == channelStatusDto.getId()) {
            return CallBackResult.returnJson(CallBackResult.PARAM_IS_ERROR, CallBackResult.PARAM_IS_EXCEPTION_MSG);
        }
        PlateformChannel plateformChannel = plateformChannelMapper.selectByPrimaryKeyToUpdate(channelStatusDto.getId());
        if (null == plateformChannel) {
            return CallBackResult.returnJson(CallBackResult.PARAM_IS_ERROR, CallBackResult.PARAM_IS_EXCEPTION_MSG);
        }

        //判断是不是100的整数倍
        if (!FigureUtil.isMulByhundred(channelStatusDto.getLoanAmount())) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "借款金额必须是100的整数倍");
        }
        //金额限制 上下限
        String moneylimit = moneylimit(channelStatusDto.getLoanAmount());
        if (null != moneylimit) {
            return moneylimit;
        }
        //风控分数不能小于总的系统默认分数
        /*String riskScore = channelStatusDto.getRiskScore();
        if (StringUtils.isNotBlank(riskScore)) {
            int risk = Integer.parseInt(riskScore);
            BackConfigParamsVo scoreGateway = backConfigParamsDao.findBySysKey(Constant.SCORE_GATEWAY);
            if (null != scoreGateway) {
                if (risk < scoreGateway.getSysValue()) {
                    return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "修改的风控分数不能低于系统默认分数！");
                }
            } else {
                if (risk < 550) {
                    return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "修改的风控分数不能低于系统默认分数！");
                }
            }
        }*/
        String channelName = channelStatusDto.getChannelName();
        if (null != channelStatusDto.getPrice()) {
            BigDecimal price = new BigDecimal((channelStatusDto.getPrice() * Constant.CENT_CHANGE_DOLLAR));
            channelStatusDto.setPriceInt(price.intValue());
        }
        if (StringUtils.isNotBlank(channelName)) {
            channelName = channelName.replace(" ","");
            //判断是否与之前的渠道名称相同
            if (!channelName.equals(plateformChannel.getChannelName())) {
                //判断渠道是否已经存在
                //如果传进来得渠道名称不同，说明渠道名被修改了，就得通过渠道名去查看是否已经存在了
                PlateformChannel plateformChannelInfo = plateformChannelService.selectByChannelName(channelName);
                if (null != plateformChannelInfo) {
                    return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.CHANNELNAME_EXIST);
                }
            }
        }

        //防撸开启检查
       /* Integer stripSwitch = channelStatusDto.getStripSwitch();
        if (null !=stripSwitch && stripSwitch ==0){
            BackConfigParamsVo channelStripSwitch = backConfigParamsDao.findBySysKey(Constant.CHANNEL_STRIP_SWITCH);
            if (null != channelStripSwitch){
                //如果未开启防撸
                if (channelStripSwitch.getSysValue() == 0 ){
                    return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "当前防撸未开通，请先申请防撸账号！！");
                }
            }
        }*/

        //该渠道的手机账户id
        Integer plateUserId = null;
        String managerPhone = channelStatusDto.getManagerPhone();
        if (StringUtils.isNotBlank(managerPhone)) {
            channelStatusDto.setAccount(managerPhone);
            channelStatusDto.setManagerPhone(managerPhone);
            //如果账户与之前得账户相等，就不用再更新了
            if (!managerPhone.equals(plateformChannel.getAccount())) {
                PlatformUser byPhoneNumber = platformUserMapper.findByPhoneNumber(managerPhone);
                if (null != byPhoneNumber) {
                    return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.PLATRFROM_USER_EXIST);
                } else {
                    PlatformUser byPhoneNumberInfo = platformUserMapper.findByPhoneNumber(plateformChannel.getAccount());
                    if (null != byPhoneNumberInfo) {
                        plateUserId = byPhoneNumberInfo.getId();
                    }
                }
            }
        }
        String managerName = channelStatusDto.getManagerName();
        if (StringUtils.isNotBlank(managerName)){
            managerName = managerName.replace(" ","");
            channelStatusDto.setManagerName(managerName);
        }


        // channelStatusDto.setPlateformUserId(platformUserInfo.getId());
        //v1.0.2版本利用上线状态，合并禁用状态和冻结状态
        Integer status = channelStatusDto.getStatus();
        if (null != status) {
            //如果渠道下线，同时渠道链接被禁用，账号被冻结
            if (status == 0) {
                // channelStatusDto.setFreezeStatus(1);
                channelStatusDto.setForbiddenStatus(1);
                //如果渠道上线，同时渠道链接被解禁，账号被解冻
            } else if (status == 1) {
                // channelStatusDto.setFreezeStatus(0);
                channelStatusDto.setForbiddenStatus(0);
            }
        }
        //如果渠道开启直接拒绝的，同时下线,不再统计进量
        Integer riskSwitch = channelStatusDto.getRiskSwitch();
        if (null != riskSwitch) {
            if (riskSwitch == 1) {
                channelStatusDto.setStatus(0);
            } else {
                channelStatusDto.setStatus(1);
            }
        }
        int isSucc = plateformChannelService.updateByPrimaryKeySelective(channelStatusDto);
        if (isSucc > 0) {
            if (null != plateUserId) {
                String password = channelStatusDto.getPassword();
                PlatformUser platformUser = new PlatformUser();
                platformUser.setId(plateUserId);
                //账户
                platformUser.setPhoneNumber(managerPhone);
                if (StringUtils.isNotBlank(password)) {
                    //随机盐
                    String randomSalt = RandomUtil.getRandomSalt(6);
                    //密码：MD5（密码+盐）
                    platformUser.setPassword(MD5Util.md5(password + randomSalt));
                    //设置盐
                    platformUser.setSalt(randomSalt);
                }
                //ip地址
                platformUser.setIpAddress(IPUtils.getIpAddr(request));
                //用户名，就是渠道名称
                platformUser.setUserName(channelStatusDto.getChannelName());
                platformUserMapper.updateByPrimaryKeySelective(platformUser);
            }

            //合作模式
            Integer cooperationMode = channelStatusDto.getCooperationMode();
            //如果价格修改的话，我们需要修改
            if (null != channelStatusDto.getPriceInt() || null != cooperationMode) {
                Date date = new Date();
                //渠道人员统计价格更新
                PlatformChannelStaff platformChannelStaff = findPlatformChannelStaff(channelStatusDto.getChannelName(), date);
                if (null != platformChannelStaff) {
                    PlatformChannelStaff platformChannelStaffS = new PlatformChannelStaff();
                    platformChannelStaffS.setId(platformChannelStaff.getId());
                    //合作价格
                    platformChannelStaffS.setPrice(channelStatusDto.getPriceInt());
                    platformChannelStaffDAO.updateByPrimaryKeySelective(platformChannelStaffS);
                }
                //进量统计价格和合作模式
                DailyStatisticsChannelProduct channelProductExist = isChannelProductExist(channelStatusDto.getId(), date);
                if (null != channelProductExist) {
                    DailyStatisticsChannelProduct dailyStatisticsChannelProduct = new DailyStatisticsChannelProduct();
                    dailyStatisticsChannelProduct.setId(channelProductExist.getId());
                    //合作价格
                    dailyStatisticsChannelProduct.setPrice(channelStatusDto.getPriceInt());
                    if (null != cooperationMode) {
                        //合作模式
                        dailyStatisticsChannelProduct.setCooperationMode(cooperationMode);
                    }
                    dailyStatisticsChannelProductDAO.updateByPrimaryKeySelective(dailyStatisticsChannelProduct);
                }
            }
            /**根据渠道id修改对应费率*/
            updateLoanRule(channelStatusDto);
            //如果有修改，就删除缓存的渠道信息
            String key = Constant.PLATEFORM_CHANNEL + plateformChannel.getChannelCode();
            if (redisUtil.hasKey(key)){
                redisUtil.del(key);
            }
            return CallBackResult.returnJson(CallBackResult.CREATED, Constant.UPDATE_SUCC);

        }
        return CallBackResult.returnJson(CallBackResult.INVALID_REQUEST, Constant.UPDATE_FAILD);
    }

    /**
     * 根据渠道id修改对应费率
     *
     * @param channelStatusDto
     */
    public void updateLoanRule(PlateformChannelParamDto channelStatusDto) {
        /**如果有了就更新*/
        LoanRuleConfig byChannelId = loanRuleConfigDAO.findByChannelId(channelStatusDto.getId());
        Integer loanAmount = channelStatusDto.getLoanAmount();
        if (null != byChannelId) {

            LoanRuleConfig loanRuleConfig = new LoanRuleConfig();
            //借款金额
            if (null != loanAmount) {
                loanRuleConfig.setLoanAmount(loanAmount * Constant.CENT_CHANGE_DOLLAR);
            }
            //替换原来的期限
            loanRuleConfig.setExpire(channelStatusDto.getExpire());
            //替换借款服务费
            if (null != channelStatusDto.getServiceCharge()) {
                loanRuleConfig.setServiceCharge(DataTransform.changeSmallDouble(channelStatusDto.getServiceCharge()));
            }
            if (null != channelStatusDto.getBorrowInterest()) {
                //替换借款利息
                loanRuleConfig.setBorrowInterest(DataTransform.changeSmallDouble(channelStatusDto.getBorrowInterest()));
            }
            //替换续期期限
            loanRuleConfig.setRenewalExpire(channelStatusDto.getRenewalExpire());
            //渠道id
            loanRuleConfig.setChannelId(channelStatusDto.getId());
            loanRuleConfigDAO.updateByChannelIdSelective(loanRuleConfig);
        } else {
            /**没有就插入*/
            plateformChannelService.updateChannelRule(channelStatusDto);
        }
        /**如果金额不能为空，就需要修改小于当前金额的用户，不然申请的时候会提示：不能低于系统配置金额*/
        if (null != loanAmount) {
            userMoneyRateService.updateMaxAmountByChannel(loanAmount, channelStatusDto.getId());
        }
    }

    /**
     * 借款金额限制
     *
     * @param loanMoney
     * @return
     */
    public String moneylimit(Integer loanMoney) {
        Integer limitStart = 0;
        Integer limitend = 100000;
        //最小金额
        BackConfigParamsVo loanLimitStart = backConfigParamsDao.findBySysKey("loan_limitStart");
        if (null != loanLimitStart) {
            limitStart = loanLimitStart.getSysValue();
        }
        //最大金额
        BackConfigParamsVo loanLimitEnd = backConfigParamsDao.findBySysKey("loan_limitEnd");
        if (null != loanLimitEnd) {
            limitend = loanLimitEnd.getSysValue();
        }

        if (FigureUtil.isGthundred(loanMoney, limitStart, limitend)) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "保存失败，借款金额必须大于" + limitStart + "，小于" + limitend + "元！");
        }
        return null;
    }

}
