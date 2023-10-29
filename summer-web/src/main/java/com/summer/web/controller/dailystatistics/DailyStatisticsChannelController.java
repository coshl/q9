package com.summer.web.controller.dailystatistics;


import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.summer.annotation.Log;
import com.summer.web.controller.BaseController;
import com.summer.dao.entity.PlatformUser;
import com.summer.dao.mapper.PlateformChannelReportDAO;
import com.summer.dao.mapper.dailystatistics.DailyStatisticsChannelProductDAO;
import com.summer.pojo.dto.ChannelLinkDto;
import com.summer.pojo.dto.PlateformChannelParamDto;
import com.summer.pojo.vo.*;
import com.summer.api.service.IPlateformChannelService;
import com.summer.api.service.IPlateformUserService;
import com.summer.api.service.channel.IChannelCountRepaymentService;
import com.summer.api.service.dailystatistics.IDailyStatisticsChannelService;
import com.summer.util.*;
import com.summer.util.encrypt.AESDecrypt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.*;

/**
 * 渠道统计
 */
@Controller
@RequestMapping("v1.0/api/dailystatics")
public class DailyStatisticsChannelController extends BaseController {

    @Resource
    private IDailyStatisticsChannelService statisticsChannelService;

    @Resource
    private DailyStatisticsChannelProductDAO dailyStatisticsChannelDAO;

    @Resource
    private PlateformChannelReportDAO plateformChannelReportDAO;
    @Resource
    private IPlateformUserService plateformUserService;
    @Resource
    private IPlateformChannelService plateformChannelService;
    @Resource
    private IChannelCountRepaymentService channelCountRepaymentService;
    @Value("${serviceUrl}")
    private String serviceUrl;

    /**
     * 渠道数据-进量统计
     *
     * @param jsonData
     * @return
     * @throws Exception
     */
    @PostMapping("/channelFlow")
    @ResponseBody
    public String channelFlow(HttpServletRequest request, @RequestBody String jsonData) throws Exception {
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
        PageHelper.startPage(params);
        Map<String, Object> map = statisticsChannelService.findParams(params);
        return CallBackResult.returnJson(map);
    }

    /**
     * 渠道数据-还款统计
     *
     * @param jsonData
     * @return
     * @throws Exception
     */
    @PostMapping("/chaneelLend")
    @ResponseBody
    public String chaneelLend(HttpServletRequest request, @RequestBody String jsonData) throws Exception {
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
        //如果角色为渠道专员，就得根据对应渠道专员去查询
        if (roleId == Constant.OPERATOR_ROLE_ID) {
            params.put("plateformUserId", platformUser.getId());
        }
        PageHelper.startPage(params);
        Map<String, Object> map = statisticsChannelService.findChannelLend(params);
        return CallBackResult.returnJson(map);
    }

    /**
     * 渠道转化-我方
     *
     * @param jsonData
     * @return
     * @throws Exception
     */
    @PostMapping("/findMyChannelStatistics")
    @ResponseBody
    public String findMyChannelStatistics(HttpServletRequest request, @RequestBody String jsonData) throws Exception {
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
        PageHelper.startPage(params);
        Map<String, Object> myChannelStatistics = statisticsChannelService.findMyChannelStatistics(params);
        return CallBackResult.returnJson(myChannelStatistics);
    }

    /**
     * 渠道转化-渠道方
     *
     * @param jsonData
     * @return
     * @throws Exception
     */
    @PostMapping("/findChannelStatistics")
    @ResponseBody
    public String findChannelStatistics(HttpServletRequest request, @RequestBody String jsonData) throws Exception {
        Map<String, Object> params = JSONObject.parseObject(jsonData);
        PlatformUser platformUser = redisPlatformUser(request);

        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        // 超管、渠道主管、渠道专员和渠道方用户可以查询
        Integer roleId = platformUser.getRoleId();
        if (roleId != Constant.ROLEID_SUPER && roleId != Constant.CHANNEL_ADMIN_ROLE_ID && roleId != Constant.OPERATOR_ROLE_ID && roleId != Constant.CHANNEL_ROLE_ID) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "没有权限");
        }
        //如果角色为运营人人员，就得根据对应运营人员去查询
        if (roleId == Constant.OPERATOR_ROLE_ID) {
            params.put("plateformUserId", platformUser.getId());

        }
        //如果是乙方登录
        if (Constant.CHANNEL_USER_ID == roleId) {
            //params.put("roleId",roleId);
            params.put("account", platformUser.getPhoneNumber());
        }

        PageInfo<PlateformChannelSideVo> pageInfo = statisticsChannelService.findChannelStatistics(params);
        return CallBackResult.returnJson(pageInfo);
    }

    /**
     * 修改系数
     *
     * @return
     * @throws Exception
     */
    @PostMapping("/updateCoefficient")
    @ResponseBody
    public String updateCoefficient(@RequestBody String jsonData, HttpServletRequest request) throws Exception {

        PlatformUser platformUserInfo = redisPlatformUser(request);
        if (null == platformUserInfo) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }

        if (platformUserInfo.getRoleId() != Constant.ROLEID_SUPER) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.UN_AUTH_VIST);
        }

        Map<String, Object> params = JSONObject.parseObject(jsonData);
        Integer id = (Integer) params.get("id");
        Object dedutionCoeff = params.get("dedutionCoefficient");
        if (null != dedutionCoeff) {
            BigDecimal dedutionCoefficient = new BigDecimal(dedutionCoeff.toString());
            int isSucc = statisticsChannelService.updateCoefficient(id, dedutionCoefficient);
            if (isSucc > 0) {
                return CallBackResult.returnJson(CallBackResult.SUCCESS, Constant.UPDATE_SUCC);
            }
        }

        return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.UPDATE_FAILD);
    }

    /**
     * 导出渠道进量列表
     *
     * @param request
     * @param response
     * @param jsonData
     * @throws Exception
     */
    @Log(title = "导出渠道进量列表")
    @RequestMapping("downloadChannelIntake")
    public void downloadChannelIntake(HttpServletRequest request, HttpServletResponse response,
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
        List<DailyStatisticsChannelProductVo> channelDAOParams = dailyStatisticsChannelDAO.findParams(params);

        String title = "渠道进量统计";
        BuildXLSX.setFileDownloadHeader(request, response, title);
        LinkedHashMap<String, String> titleMap = new LinkedHashMap<>();
        titleMap.put("channelName", "渠道名称");
        titleMap.put("reportTime", "日期");
        titleMap.put("plateformUserName", "运营人员");
        titleMap.put("price", "合作价格");
        titleMap.put("pv", "PV");
        titleMap.put("uv", "UV");
        titleMap.put("uvConversion", "UV转化率（%）");
        titleMap.put("registerNumber", "注册用户数");
        titleMap.put("registerNumberChannel", "渠道注册数");
        titleMap.put("pplicationNumber", "申请人数");
        titleMap.put("pplicationConversion", "申请转化率（%）");
        titleMap.put("loanNumberChannel", "放款人数");
        titleMap.put("loanConversion", "放款转化率（%）");
        XlsxParam xlsxParam = new XlsxParam(channelDAOParams, title, titleMap);
        OutputStream os = response.getOutputStream();
        BuildXLSX.buildExcel(xlsxParam, os);
    }

    /**
     * 导出我方渠道统计列表
     *
     * @param request
     * @param response
     * @param jsonData
     * @throws Exception
     */
    @Log(title = "导出我方渠道统计列表")
    @RequestMapping("downloadMyChannelStatistics")
    public void downloadMyChannelStatistics(HttpServletRequest request, HttpServletResponse response,
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
        List<PlateformChannelReportVo> channelDAOParams = plateformChannelReportDAO.findParams(params);
        String title = "我方渠道统计";
        BuildXLSX.setFileDownloadHeader(request, response, title);
        LinkedHashMap<String, String> titleMap = new LinkedHashMap<>();
        titleMap.put("reportTime", "日期");
        titleMap.put("channelName", "渠道名称");
        titleMap.put("registerCount", "注册人数");
        titleMap.put("downConversion", "下载率（%）");
        titleMap.put("loginConversion", "登录率（%）");
        titleMap.put("idcardCertificationCount", "身份认证（%）");
        titleMap.put("personalInformationCount", "个人信息（%）");
        titleMap.put("operatorCount", "运营商认证（%）");
        titleMap.put("authBankCount", "银行卡绑定（%）");
        titleMap.put("borrowApplyConversion", "申请率（%）");
        titleMap.put("loanConversion", "放款率（%）");
        XlsxParam xlsxParam = new XlsxParam(channelDAOParams, title, titleMap);
        OutputStream os = response.getOutputStream();
        BuildXLSX.buildExcel(xlsxParam, os);
    }

    /**
     * 获取运营人员下拉框：只允许渠道专员，超级管理员，渠道主管添加渠道
     *
     * @return
     */
    @GetMapping("/getOperatorList")
    public @ResponseBody
    String getOperatorList(HttpServletRequest request) {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }

        // 超管、渠道主管、渠道专员可以查询
        Integer roleId = platformUser.getRoleId();
        if (roleId != Constant.ROLEID_SUPER && roleId != Constant.CHANNEL_ADMIN_ROLE_ID && roleId != Constant.OPERATOR_ROLE_ID) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "没有权限");
        }

        Map<String, Object> param = new HashMap<>();
        //渠道专员（渠道运营人员）：角色id
        param.put("operatorRoleId", Constant.OPERATOR_ROLE_ID);
        //超级管理:角色id
        param.put("adminRoleId", Constant.ADMIN_ROLE_ID);
        //渠道主管:角色id
        param.put("channelAdminRoleId", Constant.CHANNEL_ADMIN_ROLE_ID);
        List<OperatorVo> OperatorVo = plateformUserService.findByRoleId(param);
        return CallBackResult.returnJson(CallBackResult.SUCCESS, CallBackResult.MSG_SUCCESS, OperatorVo);
    }

    @GetMapping("/getLink")
    public @ResponseBody
    String getChannelLink(HttpServletRequest request, @NotNull(message = "id不能为空") Integer id) {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        // 超管、渠道主管、渠道专员可以查询
        Integer roleId = platformUser.getRoleId();
        if (roleId != Constant.ROLEID_SUPER && roleId != Constant.CHANNEL_ADMIN_ROLE_ID && roleId != Constant.OPERATOR_ROLE_ID) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "没有权限");
        }
        ChannelLinkVO byChannelLink = plateformChannelService.findByChannelLink(id);
        byChannelLink.setShortLink(serviceUrl+"/v1.0/api/wall/"+byChannelLink.getChannelCode());
        return CallBackResult.returnJson(byChannelLink);
    }

    @GetMapping("/getShortLink")
    public @ResponseBody
    String getShortChannelLink(HttpServletRequest request, @NotNull(message = "id不能为空") Integer id) {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        // 超管、渠道主管、渠道专员可以
        Integer roleId = platformUser.getRoleId();
        if (roleId != Constant.ROLEID_SUPER && roleId != Constant.CHANNEL_ADMIN_ROLE_ID && roleId != Constant.OPERATOR_ROLE_ID) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "没有权限");
        }
        String result = plateformChannelService.getShortChannelLink(id);
        return result;
    }

    @PostMapping("/saveLink")
    public @ResponseBody
    String updateChannellink(HttpServletRequest request, @Validated @RequestBody ChannelLinkDto channelLinkDto) {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        // 超管、渠道主管、渠道专员可以查询
        Integer roleId = platformUser.getRoleId();
        if (roleId != Constant.ROLEID_SUPER && roleId != Constant.CHANNEL_ADMIN_ROLE_ID && roleId != Constant.OPERATOR_ROLE_ID) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "没有权限");
        }
        PlateformChannelParamDto plateformChannel = new PlateformChannelParamDto();
        plateformChannel.setId(channelLinkDto.getId());
        plateformChannel.setAccount(channelLinkDto.getAccount());
        plateformChannel.setPassword(channelLinkDto.getPassword());
        plateformChannel.setLink(channelLinkDto.getDropLink());
        plateformChannel.setBackStage(channelLinkDto.getBackstageLink());
        int isSucc = plateformChannelService.updateByPrimaryKeySelective(plateformChannel);
        if (isSucc > 0) {
            PlatformUser byPhoneNumber = plateformUserService.findByPhoneNumber(plateformChannel.getAccount());
            if (null != byPhoneNumber) {
                PlatformUser newPlatformUser = new PlatformUser();
                newPlatformUser.setId(byPhoneNumber.getId());
                //随机盐
                String randomSalt = RandomUtil.getRandomSalt(6);
                //密码：MD5（密码+盐）
                newPlatformUser.setPassword(MD5Util.md5(plateformChannel.getPassword() + randomSalt));
                //设置盐
                newPlatformUser.setSalt(randomSalt);
                int status = plateformUserService.update(newPlatformUser);
                if (status > 0) {
                    return CallBackResult.returnJson(CallBackResult.SUCCESS, Constant.UPDATE_SUCC);
                }
            }
        }
        return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.UPDATE_FAILD);
    }

    /**
     * 重新还款统计
     *
     * @return
     */
    @GetMapping("/rePushchaneelLend")
    public @ResponseBody
    String rePushchaneelLend(HttpServletRequest request, Integer day) throws Exception {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        // 超管
        Integer roleId = platformUser.getRoleId();
        if (roleId != Constant.ROLEID_SUPER) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "没有权限");
        }
        Date date = new Date();
        Date newTime = DateUtil.dateSubtraction(date, day);
        channelCountRepaymentService.channelRepaymentCount(newTime);

        return CallBackResult.returnJson(CallBackResult.SUCCESS, CallBackResult.MSG_SUCCESS);
    }

    /**
     * 重新还款统计已还款
     *
     * @return
     */
    @GetMapping("/rePushRepamentLend")
    public @ResponseBody
    String rePushRepaymentLend(HttpServletRequest request, Integer day) throws Exception {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        // 超管
        Integer roleId = platformUser.getRoleId();
        if (roleId != Constant.ROLEID_SUPER) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "没有权限");
        }
        Date date = new Date();
        Date newTime = DateUtil.dateSubtraction(date, day);
        channelCountRepaymentService.repaymentCount(newTime);
        channelCountRepaymentService.overdueCount(newTime);
        return CallBackResult.returnJson(CallBackResult.SUCCESS, CallBackResult.MSG_SUCCESS);
    }

    /**
     * 重新还款统计已还款
     *
     * @return
     */
    @GetMapping("/rePushNewOldRepamentLend")
    public @ResponseBody
    String rePushNewOldRepaymentLend(HttpServletRequest request, Integer day) throws Exception {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        // 超管
        Integer roleId = platformUser.getRoleId();
        if (roleId != Constant.ROLEID_SUPER) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "没有权限");
        }
        Date date = new Date();
        Date newTime = DateUtil.dateSubtraction(date, day);
        channelCountRepaymentService.repaymentCount(newTime);
        return CallBackResult.returnJson(CallBackResult.SUCCESS, CallBackResult.MSG_SUCCESS);
    }


}
