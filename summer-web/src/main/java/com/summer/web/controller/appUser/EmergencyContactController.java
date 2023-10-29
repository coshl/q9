package com.summer.web.controller.appUser;

import com.summer.api.service.*;
import com.summer.dao.mapper.PlateformChannelMapper;
import com.summer.dao.mapper.UserBlackListDAO;
import com.summer.service.impl.UserBlackListService;
import com.summer.service.impl.risk.BlackApi;
import com.summer.web.controller.BaseController;
import com.summer.dao.entity.*;
import com.summer.pojo.dto.ContactParamDto;
import com.summer.pojo.dto.ContactsParamDto;
import com.summer.pojo.dto.ShortMessageDto;
import com.summer.pojo.dto.UserAppSoftwareDto;
import com.summer.api.service.channel.IChannelAsyncCountService;
import com.summer.util.CallBackResult;
import com.summer.util.Constant;
import com.summer.util.UserRelationUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 紧急联系人认证、通讯录上传、短信上传、用户应用上传
 */
@Slf4j
@RestController
@RequestMapping("v1.0/api/app")
public class EmergencyContactController extends BaseController {

    @Resource
    private IUserInfoService userInfoService;
    @Resource
    private IInfoIndexInfoService infoIndexInfoService;
    @Resource
    private IUserContactsService userContactsService;
    @Resource
    private IUserShortMessageService userShortMessageService;
    @Resource
    private IUserAppSoftwareService userAppSoftwareService;
    @Resource
    private IChannelAsyncCountService channelAsyncCountService;
    @Resource
    UserBlackListDAO userBlackListDAO;
    @Resource
    PlateformChannelMapper plateformChannelMapper;

    /**
     * 保存紧急联系人
     *
     * @return
     */
    @PostMapping("/save/contact")
    public String saveContact(HttpServletRequest request, @Validated @RequestBody ContactParamDto contactParamDto) throws Exception {
        Date nowTime = new Date();
        UserInfo userInfo = redisUser(request);
        if (null != userInfo) {
            UserInfo newUserInfo = new UserInfo();
            newUserInfo.setId(userInfo.getId());
            //现居住地址
            newUserInfo.setPresentAddress(contactParamDto.getPresentAddress());
            //现居住详细地址
            newUserInfo.setPresentAddressDetail(contactParamDto.getPresentAddressDetail());
            //第一联系人关系
            newUserInfo.setFirstContactRelation(Byte.parseByte(contactParamDto.getFirstContactRelation()));
            //第一联系人姓名
            newUserInfo.setFirstContactName(contactParamDto.getFirstContactName() );
            //第一联系人手机号
            newUserInfo.setFirstContactPhone(contactParamDto.getFirstContactPhone() );
            //第二联系人关系
            newUserInfo.setSecondContactRelation(Byte.parseByte(contactParamDto.getSecondContactRelation()));
            //第二联系人姓名
            newUserInfo.setSecondContactName( contactParamDto.getSecondContactName() );
            //第二联系人手机号
            newUserInfo.setSecondContactPhone(contactParamDto.getSecondContactPhone());
            //紧急联系人认证状态
            newUserInfo.setEmergencyAuthentic(Constant.CONTACT_AUTH_STATUS);
            //现居地经度
            newUserInfo.setPresentLongitude(contactParamDto.getPresentLongitude());
            //现居地维度
            newUserInfo.setPresentLatitude(contactParamDto.getPresentLatitude());
            //email地址
//            newUserInfo.setEmailAddress(contactParamDto.getEmailAddress());
            //手机基本信息
            /*if (null!=contactParamDto.getPhoneBaseInfo()){
                newUserInfo.setPhoneBaseInfo(JSONObject.toJSONString(contactParamDto.getPhoneBaseInfo()));
            }*/
            //认证状态（0 未认证 1、身份认证 2、个人信息认证,3运营商认证 4 银行卡绑定 ）'
           /* PlateformChannel plateformChannel =plateformChannelMapper.selectByPrimaryKey(userInfo.getChannelId());
            if (plateformChannel.getChannelName().equals("黑名单"))
            {
                //加入内部黑名单
                UserBlackList userBlackList = new UserBlackList();
                userBlackList.setIdCard(userInfo.getIdCard());
                userBlackList.setPhone(userInfo.getPhone());
                userBlackList.setUserName(userInfo.getRealName());
                userBlackList.setUserName(userInfo.getRealName());
                userBlackList.setStatus(0);
                userBlackList.setRemark("黑名单链接注册进来");
                userBlackList.setCreateTime(new Date());
                userBlackListDAO.insert(userBlackList);
                //推送到中央黑名单
                *//*BlackList blackList = new BlackList();
                blackList.setCreateTime(new Date());
                blackList.setIdCard(userInfo.getIdCard());
                blackList.setPhone(userInfo.getPhone());
                blackList.setUpdateTime(new Date());
                blackListServer.create(blackList);*//*
                return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "认证失败了");
            }*/

            userInfo.setAuthenticStatus(Constant.CONTACTS_AUTH_STATUS);
            userInfoService.updateAuthenticStatus(userInfo);
            //newUserInfo.setAuthenticStatus(Constant.CONTACTS_AUTH_STATUS);
            int isSucc = userInfoService.updateByPrimaryKeySelective(newUserInfo);
            if (isSucc > 0) {
                InfoIndexInfo infoIndexInfo = new InfoIndexInfo();
                infoIndexInfo.setUserId(userInfo.getId());
                //认证状态
                infoIndexInfo.setAuthContacts(Integer.valueOf(Constant.CONTACT_AUTH_STATUS));
                //认证时间
                infoIndexInfo.setAuthContactsTime(nowTime);
                int sucState = infoIndexInfoService.updateByPrimaryKeySelective(infoIndexInfo);
                if (sucState > 0) {
                    /**异步统计紧急联系人认证人数*/
                    channelAsyncCountService.contactsAuthIsSuccCount(userInfo, nowTime);
                    return CallBackResult.returnJson(CallBackResult.SUCCESS, CallBackResult.MSG_SUCCESS);
                }
            }
            //认证失败
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.AUTH_IS_BAD);
        }
        //登录失效
        return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
    }
    /**
     * 上传通讯录
     *
     * @return
     */
    @PostMapping("/save/contacts")
    public String saveContacts(HttpServletRequest request, @RequestBody List<ContactsParamDto> contactsParamDton) throws Exception {
        UserInfo userInfo = redisUser(request);
        if (null != userInfo) {
            UserInfo newPhone = userInfoService.selectByPrimaryKey(userInfo.getId());
            if (null != newPhone) {
                List<UserContacts> userContactlist = userContactsService.findContatsByUserId(userInfo.getId());
                if (null != userContactlist && userContactlist.size() > Constant.LIST_SIZE_LENTH_ZORE) {
                    userContactsService.deleteByUserId(userInfo.getId());
                }
                int isSucc = 0;
                List<UserContacts> userContactsParam = new ArrayList<>();
                Date nowTime = new Date();
                for (ContactsParamDto contacts : contactsParamDton) {
                    UserContacts userContacts = new UserContacts();
                    //用户id
                    userContacts.setUserId(newPhone.getId());
                    //用户姓名
                    userContacts.setUserName(newPhone.getPhone() );
                    //联系人姓名
                    userContacts.setContactName(contacts.getContactName() );
                    //联系人手机号
                    userContacts.setContactPhone(contacts.getContactPhone());
                    //当前时间
                    userContacts.setCreateTime(nowTime);
                    userContactsParam.add(userContacts);
                }
                isSucc = userContactsService.insertBatchSelective(userContactsParam);

                if (isSucc > 0) {
                    return CallBackResult.returnJson(CallBackResult.SUCCESS, CallBackResult.MSG_SUCCESS);
                }
                //认证失败
                return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.AUTH_IS_BAD);
            }
        }
        //登录失效
        return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
    }

    /**
     * 上传用户短信信息
     *
     * @param request
     * @param shortMessageDtos
     * @return
     */
    @PostMapping("/save/shortmsg")
    public String saveShortMessage(HttpServletRequest request, @RequestBody List<ShortMessageDto> shortMessageDtos) throws Exception {
        UserInfo userInfo = redisUser(request);
        if (null != userInfo) {

            int isSucc = 0;
            List<UserShortMessage> userShortMessagess = new ArrayList<>();
            Date nowTime = new Date();
            if (null != shortMessageDtos && shortMessageDtos.size() > 0) {
                List<UserShortMessage> userShortMessages = userShortMessageService.findByUserId(userInfo.getId());
                if (null != userShortMessages && userShortMessages.size() > Constant.LIST_SIZE_LENTH_ZORE) {
                    userShortMessageService.deleteByUserId(userInfo.getId());
                }
                for (ShortMessageDto shortMessageDto : shortMessageDtos) {
                    UserShortMessage userShortMessage = new UserShortMessage();
                    //用户id
                    userShortMessage.setUserId(userInfo.getId());
                    //添加时间
                    userShortMessage.setCreatTime(nowTime);
                    //短息时间
                    userShortMessage.setMessageDate(shortMessageDto.getMessageDate());
                    //短信内容
                    userShortMessage.setMessageContent(shortMessageDto.getMessageContent());
                    //手机号码
                    String phone = shortMessageDto.getPhone();
                    if (StringUtils.isNotBlank(phone) && phone.length() > 255) {
                        log.info(phone);
                        userShortMessage.setPhone(phone.substring(0, 100));
                    } else {
                        userShortMessage.setPhone(phone);
                    }
                    userShortMessagess.add(userShortMessage);
                }
                isSucc = userShortMessageService.insertBatchSelective(userShortMessagess);
                if (isSucc > 0) {
                    return CallBackResult.returnJson(CallBackResult.SUCCESS, CallBackResult.MSG_SUCCESS);
                }
            }
            //认证失败
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.AUTH_IS_BAD);
        }
        //登录失效
        return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
    }

    /**
     * 上传用户手机APP应用
     *
     * @param request
     * @return
     */
    @PostMapping("/save/software")
    public String saveUserAppSoftware(HttpServletRequest request, @RequestBody List<UserAppSoftwareDto> userAppSoftwareDto) throws Exception {
        UserInfo userInfo = redisUser(request);
        if (null != userInfo) {
            List<UserAppSoftware> userAppSoftwares = userAppSoftwareService.findByUserId(userInfo.getId());
            if (null != userAppSoftwares && userAppSoftwares.size() > Constant.LIST_SIZE_LENTH_ZORE) {
                userAppSoftwareService.deleteByUserId(userInfo.getId());
            }
            List<UserAppSoftware> uploadUserAppSoftware = new ArrayList<>();
            for (UserAppSoftwareDto userAppSoftware : userAppSoftwareDto) {
                UserAppSoftware newUserAppSoftware = new UserAppSoftware();
                newUserAppSoftware.setUserId(userInfo.getId());
                newUserAppSoftware.setAppName(userAppSoftware.getAppName());
                newUserAppSoftware.setPackageName(userAppSoftware.getPackageName());
                newUserAppSoftware.setVersionCode(userAppSoftware.getVersionCode());
                newUserAppSoftware.setVersionName(userAppSoftware.getVersionName());
                uploadUserAppSoftware.add(newUserAppSoftware);
            }
            int isSucc = userAppSoftwareService.insertBatchSelective(uploadUserAppSoftware);
            if (isSucc > 0) {
                return CallBackResult.returnJson(CallBackResult.SUCCESS, CallBackResult.MSG_SUCCESS);
            }
            //认证失败
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.AUTH_IS_BAD);

        }
        //登录失效
        return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
    }

    /**
     * 获取紧急联系人
     *
     * @param request
     * @return
     */
    @GetMapping("/contacts")
    public String getContacts(HttpServletRequest request) throws Exception {
        UserInfo userInfo = redisUser(request);

        if (null != userInfo) {
            UserInfo newUser = userInfoService.selectByPrimaryKey(userInfo.getId());
            if (null != newUser) {
                Map<String, Object> resultMap = new HashMap<String, Object>();
                //现居住地址
                resultMap.put("presentAddress", newUser.getPresentAddress());
                //现居住地详细地址
                resultMap.put("presentAddressDetail", newUser.getPresentAddressDetail());
                //第一联系关系
                resultMap.put("firstContactRelation", newUser.getFirstContactRelation());
                //第一联系人电话
                resultMap.put("firstContactPhone", newUser.getFirstContactPhone());
                //第一联系人姓名
                resultMap.put("firstContactName", newUser.getFirstContactName());
                //第二联系人关系
                resultMap.put("secondContactRelation", newUser.getSecondContactRelation());
                //第二联系人电话
                resultMap.put("secondContactPhone", newUser.getSecondContactPhone());
                //第二联系人姓名
                resultMap.put("secondContactName", newUser.getSecondContactName());
                //返回对应关系
                relation(resultMap);
                return CallBackResult.returnJson(CallBackResult.SUCCESS, CallBackResult.MSG_SUCCESS, resultMap);
            }
        }
        return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
    }

    public void relation(Map<String, Object> resultMap) {
        List lineal_list = new ArrayList();
        List other_list = new ArrayList();
        // 直系亲属联系人
        for (Map.Entry<String, String> entry : UserRelationUtil.CONTACTS_FAMILY.entrySet()) {
            Map map = new HashMap<String, String>();
            map.put("type", entry.getKey());
            map.put("name", entry.getValue());
            lineal_list.add(map);
            resultMap.put("lineal_list", lineal_list);
        }
        // 其他联系人
        for (Map.Entry<String, String> entry : UserRelationUtil.CONTACTS_OTHER.entrySet()) {
            Map map = new HashMap<String, String>();
            map.put("type", entry.getKey());
            map.put("name", entry.getValue());
            other_list.add(map);
            resultMap.put("other_list", other_list);
        }
    }

}
