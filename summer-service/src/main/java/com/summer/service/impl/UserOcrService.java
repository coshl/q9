package com.summer.service.impl;

import cn.hutool.core.util.IdcardUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.cloudauth20190307.models.DescribeFaceVerifyResponse;
import com.summer.api.service.IUserInfoService;
import com.summer.api.service.IUserOcrService;
import com.summer.api.service.channel.IChannelAsyncCountService;
import com.summer.dao.entity.InfoIndexInfo;
import com.summer.dao.entity.UserInfo;
import com.summer.dao.entity.UserOcrData;
import com.summer.dao.entity.UserOcrDataWithBLOBs;
import com.summer.dao.mapper.UserOcrDataDAO;
import com.summer.enums.UserAuthStatus;
import com.summer.service.face.AliFaceApi;
import com.summer.service.manager.UserOcrDataManager;
import com.summer.util.*;
import com.summer.util.log.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class UserOcrService implements IUserOcrService {

    @Resource
    private UserOcrDataDAO userOcrDataDAO;
    @Autowired
    private UserOcrDataManager userOcrDataManager;
    @Resource
    private IChannelAsyncCountService channelAsyncCountService;
    @Resource
    private IUserInfoService userInfoService;
    @Resource
    private AliFaceApi aliFaceApi;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return userOcrDataDAO.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(UserOcrDataWithBLOBs record) {
        return userOcrDataDAO.insert(record);
    }

    @Override
    public int insertSelective(UserOcrDataWithBLOBs record) {
        return userOcrDataDAO.insertSelective(record);
    }

    @Override
    public UserOcrDataWithBLOBs selectByPrimaryKey(Integer id) {
        return userOcrDataDAO.selectByPrimaryKey(id);
    }

    @Override
    public UserOcrDataWithBLOBs selectByUserId(Integer userId) {
        return userOcrDataDAO.selectByUserId(userId);
    }

    @Override
    public int updateByPrimaryKeySelective(UserOcrDataWithBLOBs record) {
        return userOcrDataDAO.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKeyWithBLOBs(UserOcrDataWithBLOBs record) {
        return userOcrDataDAO.updateByPrimaryKeyWithBLOBs(record);
    }

    @Override
    public int updateByPrimaryKey(UserOcrData record) {
        return userOcrDataDAO.updateByPrimaryKey(record);
    }

    @Override
    public UserOcrDataWithBLOBs selectByUserIdAndToken(Integer userId, String token) {
        try {
            Assert.notNull(userId, "id不能为空");
            Assert.notNull(token, "token不能为空");
            return userOcrDataDAO.selectByUserIdAndToken(userId, token);
        } catch (IllegalArgumentException e) {
            log.error("参数错误:{}", e.getMessage());
            return null;
        } catch (Exception e) {
            log.error("查询ocr认证记录失败:{}", e);
            return null;
        }
    }

    @Override
    public Boolean updateStatusById(Integer id, Integer status) {
        try {
            Assert.notNull(id, "id不能为空");
            Assert.notNull(status, "status不能为空");
            UserOcrDataWithBLOBs userOcrData = new UserOcrDataWithBLOBs();
            userOcrData.setId(id);
            userOcrData.setStatus(status);
            return userOcrDataDAO.updateByPrimaryKeySelective(userOcrData) > 0;
        } catch (IllegalArgumentException e) {
            log.error("参数错误:{}", e.getMessage());
            return null;
        } catch (Exception e) {
            log.error("状态更新异常", e);
            return false;
        }
    }

    @Override
    public List<UserOcrData> listByStatus(Integer status) {
        try {
            Assert.notNull(status, "status不能为空");
            return userOcrDataDAO.listByStatus(status);
        } catch (IllegalArgumentException e) {
            log.error("参数错误:{}", e.getMessage());
        } catch (Exception e) {
            log.error("状态更新异常", e);
        }
        return new ArrayList<>();
    }

    /**
     * @param userId       用户id
     * @param targetStatus 要修改的目标状态
     * @param sourceStatus 原状态，用于查询
     */
    @Override
    public Boolean updateStatusByUserIdAndStatus(Integer userId, Integer sourceStatus, Integer targetStatus) {
        try {
            Assert.notNull(userId, "userId不能为空");
            Assert.notNull(sourceStatus, "sourceStatus不能为空");
            Assert.notNull(targetStatus, "targetStatus不能为空");
            return userOcrDataDAO.updateStatusByUserIdAndStatus(userId, sourceStatus, targetStatus) > 0;
        } catch (IllegalArgumentException e) {
            log.error("参数错误:{}", e.getMessage());
            return null;
        } catch (Exception e) {
            log.error("状态更新异常", e);
            return null;
        }
    }

    @Override
    public Boolean updateStatusAndFailCauseById(Integer id, Integer status, String failCause) {
        try {
            Assert.notNull(id, "id不能为空");
            Assert.notNull(status, "status不能为空");
            Assert.notNull(failCause, "failCause不能为空");
            return userOcrDataDAO.updateStatusAndFailCauseById(id, status, failCause) > 0;
        } catch (IllegalArgumentException e) {
            log.error("参数错误:{}", e.getMessage());
        } catch (Exception e) {
            log.error("更新异常", e);
        }
        return false;
    }

    /**
     * @return 返回一个Boolean类型，true代表有报告生成,但不表示认证成功；false无报告
     */
    @Override
    public Boolean ocrAuth(UserOcrData userOcrData) {
        try {
            DescribeFaceVerifyResponse response = aliFaceApi.describeFaceVerify(userOcrData.getToken());
            //log.info("OCR获取报告获取请求参数:{}", response);
            if(!"200".equals(response.getCode())){
                return false;
            }
            String materialInfo = response.getResultObject() == null ? null : response.getResultObject().getMaterialInfo();
            if(materialInfo.isEmpty()) {
                return false;
            }
            JSONObject jsonObjectFace = JSON.parseObject(materialInfo);
            JSONObject jsonObjectOcrIdCardInfo = jsonObjectFace.getJSONObject("ocrIdCardInfo");
            String humanFaceImg = jsonObjectFace.getJSONObject("facialPictureFront").getString("pictureUrl");
            String cardPositiveImg = jsonObjectFace.getJSONObject("ocrPictureFront").getString("ossIdFaceUrl");
            String cardAntiImg = jsonObjectFace.getJSONObject("ocrPictureFront").getString("ossIdNationalEmblemUrl");

            UserInfo userInfo = userInfoService.selectByPrimaryKey(userOcrData.getUserId());
            UserInfo newUserInfo = new UserInfo();
            newUserInfo.setId(userOcrData.getUserId());
            //身份证正面地址
            newUserInfo.setIdcardImgZ(cardPositiveImg.substring(0,cardPositiveImg.indexOf("?")));
            //身份证反面图片地址
            newUserInfo.setIdcardImgF(cardAntiImg.substring(0,cardAntiImg.indexOf("?")));
            //人脸图片
            newUserInfo.setHeadPortrait(humanFaceImg.substring(0,humanFaceImg.indexOf("?")));
            //真实姓名
            newUserInfo.setRealName(jsonObjectOcrIdCardInfo.getString("certName"));
            //身份证号
            newUserInfo.setIdCard(jsonObjectOcrIdCardInfo.getString("certNo"));
            //身份证地址
            newUserInfo.setIdCardAddress(jsonObjectOcrIdCardInfo.getString("address"));
            //身份证有效期
            newUserInfo.setIdCardPeriod(jsonObjectOcrIdCardInfo.getString("endDate"));
            //OCR人脸识别订单号
            newUserInfo.setOcrOrder(userOcrData.getToken());
            //年龄
            newUserInfo.setAge(IdcardUtil.getAgeByIdCard(jsonObjectOcrIdCardInfo.getString("certNo")));
            //性别
            int sex = IdcardUtil.getGenderByIdCard(jsonObjectOcrIdCardInfo.getString("certNo")) == 0 ? 2:1;
            newUserInfo.setSex((byte)sex);
            //成功后，修改info_index_info 个人信息认证状态
            InfoIndexInfo infoIndexInfo = new InfoIndexInfo();
            infoIndexInfo.setUserId(userOcrData.getUserId());
            infoIndexInfo.setAuthInfoTime(new Date());
            //是否为攻击：攻击为T，非攻击为F。
            String faceAttack = jsonObjectFace.getString("faceAttack");
            // 是否有脸部遮挡：有脸部遮挡为T，否则为F。
            String faceOcclusion = jsonObjectFace.getString("faceOcclusion");
            if (StringUtils.equalsIgnoreCase(faceAttack, "T") && StringUtils.equalsIgnoreCase(faceOcclusion, "T")) {
                // 对比失败，非本人
                log.info("人脸对比失败，实名结果非本人,userId:{},ocrDateTableId:{}", userOcrData.getUserId(), userOcrData.getId());
                //实名认证状态
                newUserInfo.setRealAuthentic(UserAuthStatus.FAIL_AUTH.getValue().byteValue());
                infoIndexInfo.setAuthInfo(UserAuthStatus.FAIL_AUTH.getValue());
                // 认证失败时事务
                userOcrDataManager.authFail(userOcrData.getId(), "有脸部遮挡行为，双T状态拒绝", newUserInfo, infoIndexInfo);
                return true;
            } else {
                byte realCount = userInfo.getRealCount();
                if (realCount == 0) {
                    //认证次数(设置成1，表示第一次认证，渠道统计的时候只在第一次统计：防止用户多次认证，渠道显示认证人数大于注册人数)
                    newUserInfo.setRealCount(Constant.AUTH_COUNT_TIMES);
                } else {
                    //原来的次数上认证次数+1
                    realCount ++;
                    newUserInfo.setRealCount(realCount);
                }
                // 实名认证时间
                newUserInfo.setRealAuthenticTime(new Date());
                newUserInfo.setRealAuthentic(UserAuthStatus.SUCCESS_AUTH.getValue().byteValue());
                newUserInfo.setAuthenticStatus(Constant.REAL_AUTH_STATUS);
                infoIndexInfo.setAuthInfo(UserAuthStatus.SUCCESS_AUTH.getValue());
                // 认证成功时事务
                userOcrDataManager.authSuccess(userOcrData.getUserId(), userOcrData.getId(), newUserInfo, infoIndexInfo ,jsonObjectFace.toJSONString());
                /**异步统计认证个人信息认证量*/
                channelAsyncCountService.personInfoAuthIsSuccCount(userInfo, new Date());
                return true;
            }
        } catch (Exception e) {
            log.error("OCR报告获取异常:{}", e);
            return false;
        }
    }
}
