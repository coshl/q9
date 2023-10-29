package com.summer.service.impl.channel;

import com.summer.dao.entity.*;
import com.summer.dao.mapper.*;
import com.summer.dao.entity.dailystatics.DailyStatisticsChannelProduct;
import com.summer.dao.mapper.dailystatistics.DailyStatisticsChannelProductDAO;
import com.summer.api.service.SourceUserLoginInfoService;
import com.summer.api.service.channel.IChannelAsyncCountService;
import com.summer.service.job.RedissLockUtil;
import com.summer.util.Constant;
import com.summer.util.DateUtil;
import com.summer.util.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * 渠道相关统计的实现类（数据生成）
 */
@Configuration
@Service
public class ChannelAsyncCountService implements IChannelAsyncCountService {

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Value("${system.prefix}")
    private String PREFIX;
    /**
     * 进量统计
     **/
    @Resource
    private DailyStatisticsChannelProductDAO dailyStatisticsChannelProductDAO;
    /**
     * 渠道统计
     */
    @Resource
    private PlateformChannelReportDAO plateformChannelReportDAO;
    /**
     * 渠道商
     */
    @Resource
    private PlateformChannelMapper plateformChannelMapper;
    /**
     * 渠道人员统计
     */
    @Resource
    private PlatformChannelStaffDAO platformChannelStaffDAO;
    @Resource
    private IpAddressLogDAO ipAddressLogDAO;
    @Resource
    private OrderBorrowMapper orderBorrowMapper;
    @Resource
    private SourceUserLoginInfoService sourceUserLoginInfoService;
    @Resource
    private UserInfoMapper userInfoMapper;
    @Resource
    private OrderRepaymentMapper orderRepaymentMapper;

    @Value("${system.centerUrl}")
    private String dataUrl;
    @Value("${app.pid}")
    private String pid;

    @Async
    @Transactional
    @Override
    public void registerIsSuccCount(Integer userId, Integer channelId, Date nowTime) {
        log.info("start invoke  ChannelSyncCountService.registerIsSuccCount --------------> userId={},channelId={},nowTime={}", userId, channelId, DateUtil.formatTimeYmdHms(nowTime));
        boolean getLock = false;
        try {
            //todo 若任务执行时间过短，则有可能在等锁的过程中2个服务任务都会获取到锁，这与实际需要的功能不一致，故需要将waitTime设置为0
            if (getLock = RedissLockUtil.tryLock(PREFIX + "registerIsSuccCount", 0, 60)) {
                log.info("registerIsSuccCount start");
                /**每日渠道进量统计:注册人数统计：及数据生产*/
                statisticsChannelProduct(channelId, nowTime);
                /**渠道人员统计，注册数统计及数据生产*/
                platformChannelStaffCount(channelId, nowTime);
                /**渠道统计：注册人数增加*/
                UserInfo pwdById = userInfoMapper.selectByUserId(userId);
                plateformChannelReportCount(channelId, nowTime, pwdById);
            }
        } catch (Exception e) {
            log.error("registerIsSuccCount userId={},channelId={},error={}", userId, channelId, e);
        } finally {
            if (getLock) {
                RedissLockUtil.unlock(PREFIX + "registerIsSuccCount");
            }
        }
    }

    @Async
    @Override
    public void loginIsSuccCount(UserInfo userInfo, Date nowTime) {
        log.info("start invoke ChannelSyncCountService.loginIsSuccCount phone={},channelId={}", userInfo.phone, userInfo.getChannelId());
        boolean getLock = false;
        try {
            //todo 若任务执行时间过短，则有可能在等锁的过程中2个服务任务都会获取到锁，这与实际需要的功能不一致，故需要将waitTime设置为0
            if (getLock = RedissLockUtil.tryLock(PREFIX + "loginIsSuccCount", 0, 60)) {
                log.info("loginIsSuccCount start");
                PlateformChannelReport plateformChannelReport = findPlateformChannelReport(userInfo.getChannelId(), userInfo.getCreateTime());
                if (null != plateformChannelReport) {
                    PlateformChannelReport newPlateformChannelReport = new PlateformChannelReport();
                    newPlateformChannelReport.setId(plateformChannelReport.getId());
                    //该渠道的登录量
//                    SourceUserLoginInfo sourceUserLoginInfo = new SourceUserLoginInfo();
//                    sourceUserLoginInfo.setSourceChannelCode(userInfo.getChannelId());
//                    sourceUserLoginInfo.setUserCreatTime(userInfo.getCreateTime());
//                    int loginCount = sourceUserLoginInfoService.findBySourceChannelCode(sourceUserLoginInfo);
                    newPlateformChannelReport.setLoginCount(plateformChannelReport.getLoginCount() + 1);
                    //更新时间
                    newPlateformChannelReport.setUpdateTime(nowTime);
                    plateformChannelReportDAO.updateByPrimaryKeySelective(newPlateformChannelReport);
                    log.info("ChannelSyncCountService.loginIsSuccCount 渠道统计：登录成功人数统计------------->更新成功 phone={},channelId={}", userInfo.phone, userInfo.getChannelId());
                } else {
                    //如果该情况出现频率过高，说明在注册时未生成该数据，这里可以重新根据当前传进来的用户的注册时间，和渠道统计出总数， 然后生成一条以该注册时间为统计时间的渠道数据,（其他地方类似）
                    log.info("ChannelSyncCountService.loginIsSuccCount  渠道统计：登录成功人数统计-------------->未查询到该用户注册当天，生成的渠道统计数据 phone={},channelId={},createTime={}", userInfo.getPhone(), userInfo.getChannelId(), DateUtil.formatTimeYmdHms(userInfo.getCreateTime()));
                }
            }
        } catch (Exception e) {
            log.error("ChannelSyncCountService.loginIsSuccCount  渠道统计：登录成功人数统计------------>更新失败 err={} ,phone={},channelId={},createTime={}", e, userInfo.phone, userInfo.getChannelId(), DateUtil.formatTimeYmdHms(userInfo.getCreateTime()));
            e.printStackTrace();
        } finally {
            if (getLock) {
                RedissLockUtil.unlock(PREFIX + "loginIsSuccCount");
            }
        }
    }

    @Async
    @Override
    public void personInfoAuthIsSuccCount(UserInfo userInfo, Date nowTime) {
        log.info("start invoke ChannelSyncCountService.personInfoAuthIsSuccCount phone={},channelId={}", userInfo.getPhone(), userInfo.getChannelId());
        boolean getLock = false;
        try {
            //todo 若任务执行时间过短，则有可能在等锁的过程中2个服务任务都会获取到锁，这与实际需要的功能不一致，故需要将waitTime设置为0
            if (getLock = RedissLockUtil.tryLock(PREFIX + "personInfoAuthIsSuccCount", 0, 30)) {
                log.info("personInfoAuthIsSuccCount start");
                PlateformChannelReport plateformChannelReport = findPlateformChannelReport(userInfo.getChannelId(), userInfo.getCreateTime());
                if (null != plateformChannelReport) {
                    // UserInfo userbyPhone = userInfoMapper.findByPhone(userInfo.getPhone());
                    UserInfo userbyPhone = userInfoMapper.selectByPrimaryKey(userInfo.getId());
                    if (null != userbyPhone && null != userbyPhone.getRealCount()) {
                        //只在第一次认证就可以了 ，后面认证多次不再统计，不然会导致认证人数超过注册人数
                        byte realCounnt = userbyPhone.getRealCount();
                        if (Constant.AUTH_COUNT_TIMES == realCounnt) {
                            PlateformChannelReport newPlateformChannelReport = new PlateformChannelReport();
                            newPlateformChannelReport.setId(plateformChannelReport.getId());
                            //身份认证信息+1
                            newPlateformChannelReport.setIdcardCertificationCount(plateformChannelReport.getIdcardCertificationCount() + 1);
                            //更新时间
                            newPlateformChannelReport.setUpdateTime(nowTime);
                            plateformChannelReportDAO.updateByPrimaryKeySelective(newPlateformChannelReport);
                            log.info("ChannelSyncCountService.personInfoAuthIsSuccCount 渠道统计：身份证个人信息认证成功人数统计-------------->更新成功 phone={},channelId={}", userInfo.getPhone(), userInfo.getChannelId());
                        }
                    }
                } else {
                    //如果该情况出现频率过高，说明在注册时未生成该数据，这里可以重新根据当前传进来的用户的注册时间，和渠道统计出总数， 然后生成一条以该注册时间为统计时间的渠道数据,（其他地方类似）
                    log.info("ChannelSyncCountService.personInfoAuthIsSuccCount  渠道统计：身份证个人信息认证成功人数统计----->未查询到该用户注册当天，生成的渠道统计数据 phone={},channelId={}", userInfo.getPhone(), userInfo.getChannelId());
                }
            }
        } catch (Exception e) {
            log.error("ChannelSyncCountService.personInfoAuthIsSuccCount  渠道统计：身份证个人信息认证成功人数统计----->更新失败 err={},phone={},channelId={}", e, userInfo.getPhone(), userInfo.getChannelId());
            e.printStackTrace();
        } finally {
            if (getLock) {
                RedissLockUtil.unlock(PREFIX + "personInfoAuthIsSuccCount");
            }
        }
    }

    @Async
    @Override
    public void contactsAuthIsSuccCount(UserInfo userInfo, Date nowTime) {
        log.info("start invoke ChannelSyncCountService.contactsAuthIsSuccCount  phone={},channelId={}", userInfo.getPhone(), userInfo.getChannelId());
        boolean getLock = false;
        try {
            //todo 若任务执行时间过短，则有可能在等锁的过程中2个服务任务都会获取到锁，这与实际需要的功能不一致，故需要将waitTime设置为0
            if (getLock = RedissLockUtil.tryLock(PREFIX + "contactsAuthIsSuccCount", 0, 60)) {
                log.info("contactsAuthIsSuccCount start");
                PlateformChannelReport plateformChannelReport = findPlateformChannelReport(userInfo.getChannelId(), userInfo.getCreateTime());
                if (null != plateformChannelReport) {
                    // UserInfo userbyPhone = userInfoMapper.findByPhone(userInfo.getPhone());
                    UserInfo userbyPhone = userInfoMapper.selectByPrimaryKey(userInfo.getId());
                    if (null != userbyPhone && null != userbyPhone.getRealCount()) {
                        //只在第一次认证统计就可以了 ，后面认证多次不再统计，不然会导致认证人数超过注册人数
                        byte realCounnt = userbyPhone.getRealCount();
                        if (Constant.AUTH_COUNT_TIMES == realCounnt) {
                            PlateformChannelReport newPlateformChannelReport = new PlateformChannelReport();
                            newPlateformChannelReport.setId(plateformChannelReport.getId());
                            //紧急联系人个人信息认证数量+1
                            newPlateformChannelReport.setPersonalInformationCount(plateformChannelReport.getPersonalInformationCount() + 1);
                            //更新时间
                            newPlateformChannelReport.setUpdateTime(nowTime);
                            plateformChannelReportDAO.updateByPrimaryKeySelective(newPlateformChannelReport);
                            log.info("ChannelSyncCountService.contactsAuthIsSuccCount 渠道统计：紧急联系人个人信息认证成功人数统计------------->更新成功 phone={},channelId={}", userInfo.getPhone(), userInfo.getChannelId());
                        }
                    }
                } else {
                    //如果该情况出现频率过高，说明在注册时未生成该数据，这里可以重新根据当前传进来的用户的注册时间，和渠道统计出总数， 然后生成一条以该注册时间为统计时间的渠道数据,（其他地方类似）
                    log.info("ChannelSyncCountService.contactsAuthIsSuccCount  渠道统计：紧急联系人个人信息认证成功人数统计-------------->未查询到该用户注册当天，生成的渠道统计数据 phone={},channelId={}", userInfo.getPhone(), userInfo.getChannelId());
                }
            }
        } catch (Exception e) {
            log.error("ChannelSyncCountService.contactsAuthIsSuccCount  渠道统计：紧急联系人个人信息认证成功人数统计------------------------>更新失败 err={},phone={},channelId={}", e, userInfo.getPhone(), userInfo.getChannelId());
            e.printStackTrace();
        } finally {
            if (getLock) {
                RedissLockUtil.unlock(PREFIX + "contactsAuthIsSuccCount");
            }
        }
    }

    @Async
    @Override
    public void mobileAuthIsSuccCount(UserInfo userInfo, Date nowTime) {
        log.info("start invoke ChannelSyncCountService.mobileAuthIsSuccCount phone={},channelId={}", userInfo.getPhone(), userInfo.getChannelId());
        boolean getLock = false;
        try {
            //todo 若任务执行时间过短，则有可能在等锁的过程中2个服务任务都会获取到锁，这与实际需要的功能不一致，故需要将waitTime设置为0
            if (getLock = RedissLockUtil.tryLock(PREFIX + "mobileAuthIsSuccCount", 0, 60)) {
                log.info("mobileAuthIsSuccCount start");
                PlateformChannelReport plateformChannelReport = findPlateformChannelReport(userInfo.getChannelId(), userInfo.getCreateTime());
                if (null != plateformChannelReport) {
                    /// UserInfo userbyPhone = userInfoMapper.findByPhone(userInfo.getPhone());
                    UserInfo userbyPhone = userInfoMapper.selectByPrimaryKey(userInfo.getId());
                    if (null != userbyPhone && null != userbyPhone.getRealCount()) {
                        //只在第一次认证就可以了 ，后面认证多次不再统计，不然会导致认证人数超过注册人数
                        byte realCounnt = userbyPhone.getRealCount();
                        if (Constant.AUTH_COUNT_TIMES == realCounnt) {
                            PlateformChannelReport newPlateformChannelReport = new PlateformChannelReport();
                            newPlateformChannelReport.setId(plateformChannelReport.getId());
                            //运营商认证数量+1
                            newPlateformChannelReport.setOperatorCount(plateformChannelReport.getOperatorCount() + 1);
                            //更新时间
                            newPlateformChannelReport.setUpdateTime(nowTime);
                            plateformChannelReportDAO.updateByPrimaryKeySelective(newPlateformChannelReport);
                            log.info("ChannelSyncCountService.mobileAuthIsSuccCount 渠道统计：运营商认证成功人数统计------------------------>更新成功  phone={},channelId={}", userInfo.getPhone(), userInfo.getChannelId());
                        }
                    }

                } else {
                    //如果该情况出现频率过高，说明在注册时未生成该数据，这里可以重新根据当前传进来的用户的注册时间，和渠道统计出总数， 然后生成一条以该注册时间为统计时间的渠道数据,（其他地方类似）
                    log.info("ChannelSyncCountService.mobileAuthIsSuccCount  渠道统计：运营商认证成功人数统计------------------------>未查询到该用户注册当天，生成的渠道统计数据，phone={},channelId={}", userInfo.getPhone(), userInfo.getChannelId());
                }
            }
        } catch (Exception e) {
            log.error("ChannelSyncCountService.mobileAuthIsSuccCount  渠道统计：运营商认证成功人数统计------------------------>更新失败 err={},phone={},channelId={}", e, userInfo.getPhone(), userInfo.getChannelId());
            e.printStackTrace();
        } finally {
            if (getLock) {
                RedissLockUtil.unlock(PREFIX + "mobileAuthIsSuccCount");
            }
        }
    }

    @Async
    @Override
    public void zhiMaAuthIsSuccCount(UserInfo userInfo, Date nowTime) {
        log.info("start invoke ChannelSyncCountService.zhiMaAuthIsSuccCount phone={},channelId={}", userInfo.getPhone(), userInfo.getChannelId());

        boolean getLock = false;
        try {
            //todo 若任务执行时间过短，则有可能在等锁的过程中2个服务任务都会获取到锁，这与实际需要的功能不一致，故需要将waitTime设置为0
            if (getLock = RedissLockUtil.tryLock(PREFIX + "zhiMaAuthIsSuccCount", 0, 60)) {
                log.info("zhiMaAuthIsSuccCount start");
                PlateformChannelReport plateformChannelReport = findPlateformChannelReport(userInfo.getChannelId(), userInfo.getCreateTime());
                if (null != plateformChannelReport) {

                    UserInfo userbyPhone = userInfoMapper.findByPhone(userInfo.getPhone());
                    if (null != userbyPhone && null != userbyPhone.getRealCount()) {
                        //只在第一次认证就可以了 ，后面认证多次不再统计，不然会导致认证人数超过注册人数
                        byte realCounnt = userbyPhone.getRealCount();
                        if (Constant.AUTH_COUNT_TIMES == realCounnt) {
                            PlateformChannelReport newPlateformChannelReport = new PlateformChannelReport();
                            newPlateformChannelReport.setId(plateformChannelReport.getId());
                            //芝麻认证数量+1
                            newPlateformChannelReport.setSesameCount(plateformChannelReport.getSesameCount() + 1);
                            //更新时间
                            newPlateformChannelReport.setUpdateTime(nowTime);
                            plateformChannelReportDAO.updateByPrimaryKeySelective(newPlateformChannelReport);
                            log.info("ChannelSyncCountService.zhiMaAuthIsSuccCount 渠道统计：芝麻认证成功人数统计---------->更新成功 phone={},channelId={}", userInfo.getPhone(), userInfo.getChannelId());
                        }
                    }

                } else {
                    //如果该情况出现频率过高，说明在注册时未生成该数据，这里可以重新根据当前传进来的用户的注册时间，和渠道统计出总数， 然后生成一条以该注册时间为统计时间的渠道数据,（其他地方类似）
                    log.info("ChannelSyncCountService.zhiMaAuthIsSuccCount  渠道统计：芝麻认证成功人数统计-------------->未查询到该用户注册当天，生成的渠道统计数据 phone={},channelId={}", userInfo.getPhone(), userInfo.getChannelId());
                }
            }
        } catch (Exception e) {
            log.error("ChannelSyncCountService.zhiMaAuthIsSuccCount  渠道统计：芝麻认证成功人数统计------------>更新失败 err={},phone={},channelId={}", e, userInfo.getPhone(), userInfo.getChannelId());
            e.printStackTrace();
        } finally {
            if (getLock) {
                RedissLockUtil.unlock(PREFIX + "zhiMaAuthIsSuccCount");
            }
        }
    }

    @Async
    @Override
    public void bankAuthIsSuccCount(UserInfo userInfo, Date nowTime) {
        log.info("start invoke ChannelSyncCountService.bankAuthIsSuccCount phone={},channelId={}", userInfo.getPhone(), userInfo.getChannelId());
        boolean getLock = false;
        try {
            //todo 若任务执行时间过短，则有可能在等锁的过程中2个服务任务都会获取到锁，这与实际需要的功能不一致，故需要将waitTime设置为0
            if (getLock = RedissLockUtil.tryLock(PREFIX + "bankAuthIsSuccCount", 0, 60)) {
                log.info("bankAuthIsSuccCount start");
                PlateformChannelReport plateformChannelReport = findPlateformChannelReport(userInfo.getChannelId(), userInfo.getCreateTime());
                if (null != plateformChannelReport) {
                    // UserInfo userbyPhone = userInfoMapper.findByPhone(userInfo.getPhone());
                    UserInfo userbyPhone = userInfoMapper.selectByPrimaryKey(userInfo.getId());
                    if (null != userbyPhone && null != userbyPhone.getRealCount()) {
                        //只在第一次认证就可以了 ，后面认证多次不再统计，不然会导致认证人数超过注册人数
                        byte realCounnt = userbyPhone.getRealCount();
                        if (Constant.AUTH_COUNT_TIMES == realCounnt) {
                            PlateformChannelReport newPlateformChannelReport = new PlateformChannelReport();
                            newPlateformChannelReport.setId(plateformChannelReport.getId());
                            //银行卡认证数量+1
                            newPlateformChannelReport.setAuthBankCount(plateformChannelReport.getAuthBankCount() + 1);
                            //更新时间
                            newPlateformChannelReport.setUpdateTime(nowTime);
                            plateformChannelReportDAO.updateByPrimaryKeySelective(newPlateformChannelReport);
                            log.info("ChannelSyncCountService.bankAuthIsSuccCount 渠道统计：银行卡认证成功人数统计------------------------>更新成功 phone={},channelId={}", userInfo.getPhone(), userInfo.getChannelId());
                        }
                    }

                } else {
                    //如果该情况出现频率过高，说明在注册时未生成该数据，这里可以重新根据当前传进来的用户的注册时间，和渠道统计出总数， 然后生成一条以该注册时间为统计时间的渠道数据,（其他地方类似）
                    log.info("ChannelSyncCountService.bankAuthIsSuccCount  渠道统计：银行卡认证成功人数统计------------------------>未查询到该用户注册当天，生成的渠道统计数据 phone={},channelId={}", userInfo.getPhone(), userInfo.getChannelId());
                }
            }
        } catch (Exception e) {
            log.error("ChannelSyncCountService.bankAuthIsSuccCount  渠道统计：银行卡认证成功人数统计------------------------>更新失败 err={},phone={},channelId={}", e, userInfo.getPhone(), userInfo.getChannelId());
            e.printStackTrace();
        } finally {
            if (getLock) {
                RedissLockUtil.unlock(PREFIX + "bankAuthIsSuccCount");
            }
        }


    }

    @Async
    @Override
    public void workInfoAuthIsSuccCount(UserInfo userInfo, Date nowTime) {
        log.info("start invoke ChannelSyncCountService.workInfoAuthIsSuccCount phone={},channelId={}", userInfo.getPhone(), userInfo.getChannelId());
        boolean getLock = false;
        try {
            //todo 若任务执行时间过短，则有可能在等锁的过程中2个服务任务都会获取到锁，这与实际需要的功能不一致，故需要将waitTime设置为0
            if (getLock = RedissLockUtil.tryLock(PREFIX + "workInfoAuthIsSuccCount", 0, 60)) {
                log.info("workInfoAuthIsSuccCount start phone={},channelId={}", userInfo.getPhone(), userInfo.getChannelId());
                PlateformChannelReport plateformChannelReport = findPlateformChannelReport(userInfo.getChannelId(), userInfo.getCreateTime());
                if (null != plateformChannelReport) {

                    UserInfo userbyPhone = userInfoMapper.findByPhone(userInfo.getPhone());
                    if (null != userbyPhone && null != userbyPhone.getRealCount()) {
                        //只在第一次认证就可以了 ，后面认证多次不再统计，不然会导致认证人数超过注册人数
                        byte realCounnt = userbyPhone.getRealCount();
                        if (Constant.AUTH_COUNT_TIMES == realCounnt) {
                            PlateformChannelReport newPlateformChannelReport = new PlateformChannelReport();
                            newPlateformChannelReport.setId(plateformChannelReport.getId());
                            //工作信息认证数量+1
                            newPlateformChannelReport.setCompanyCount(plateformChannelReport.getCompanyCount() + 1);
                            //更新时间
                            newPlateformChannelReport.setUpdateTime(nowTime);
                            plateformChannelReportDAO.updateByPrimaryKeySelective(newPlateformChannelReport);
                            log.info("ChannelSyncCountService.workInfoAuthIsSuccCount 渠道统计：工作信息认证成功人数统计------------------------>更新成功 phone={},channelId={}", userInfo.getPhone(), userInfo.getChannelId());
                        }
                    }
                } else {
                    //如果该情况出现频率过高，说明在注册时未生成该数据，这里可以重新根据当前传进来的用户的注册时间，和渠道统计出总数， 然后生成一条以该注册时间为统计时间的渠道数据,（其他地方类似）
                    log.info("ChannelSyncCountService.workInfoAuthIsSuccCount  渠道统计：工作信息认证成功人数统计------------------------>未查询到该用户注册当天，生成的渠道统计数据 phone={},channelId={}", userInfo.getPhone(), userInfo.getChannelId());
                }
            }
        } catch (Exception e) {
            log.error("ChannelSyncCountService.workInfoAuthIsSuccCount  渠道统计：工作信息认证成功人数统计------------------------>更新失败 err={},phone={},channelId={}", e, userInfo.getPhone(), userInfo.getChannelId());
            e.printStackTrace();
        } finally {
            if (getLock) {
                RedissLockUtil.unlock(PREFIX + "workInfoAuthIsSuccCount");
            }
        }


    }

    @Transactional
    @Async
    @Override
    public void applyCount(UserInfo userInfo, Date nowTime, Byte customerType) {
        log.info("start invoke ChannelSyncCountService.applyCount phone={},channelId={} nowTime={},customerType={}", userInfo.phone, userInfo.getChannelId(), DateUtil.formatTimeYmdHms(nowTime), customerType);

        boolean getLock = false;
        try {
            //todo 若任务执行时间过短，则有可能在等锁的过程中2个服务任务都会获取到锁，这与实际需要的功能不一致，故需要将waitTime设置为0
            if (getLock = RedissLockUtil.tryLock(PREFIX + "applyCount", 0, 60)) {
                log.info("applyCount start");
                if (0 == customerType) {
                    OrderBorrow orderBorrow = new OrderBorrow();
                    orderBorrow.setUserId(userInfo.getId());
                    orderBorrow.setCustomerType(customerType);
                    List<OrderBorrow> byUserIdCustermType = orderBorrowMapper.findByUserIdCustermType(orderBorrow);
                    //如果该用户是新用户时的生成的订单 ，只记录第一条，如果用户以新用户身份生成了多条订单，就不在计算后面的订单，（避免申请率超过100现象）
                    if (null != byUserIdCustermType && byUserIdCustermType.size() == 1) {
                        /**渠道进量统计，申请人数统计*/
                        channelProductApplyCount(userInfo.getChannelId(), userInfo, nowTime);
                        /**渠道统计：申请人数统计*/
                        channelReportApply(userInfo, nowTime);
                    }
                } else {
                    log.info("ChannelSyncCountService.applyCount ---------------->申请时不是新用户,这里无需统计 phone={},channelId={},customerType={}", userInfo.getPhone(), userInfo.getChannelId(), customerType);
                }
            }
        } catch (Exception e) {
            log.error("applyCount phone={},channelId={},customerType={},error={}", userInfo.getPhone(), userInfo.getChannelId(), customerType, e);
            e.printStackTrace();
        } finally {
            if (getLock) {
                RedissLockUtil.unlock(PREFIX + "applyCount");
            }
        }

    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Async
    @Override
    public void loanIsSuccCount(UserInfo userInfo, Date nowTime, int customerType) {
        log.info("start invoke ChannelSyncCountService.loanIsSuccCount phone={},channelId={},customerType={}", userInfo.getPhone(), userInfo.getChannelId(), customerType);
        try {
            log.info("loanIsSuccCount start phone={},channelId={},customerType={}", userInfo.getPhone(), userInfo.getChannelId(), customerType);
            if (0 == customerType) {
                Map<String, Object> param = new HashMap<>();
                param.put("channelId", userInfo.getChannelId());
                param.put("nowTime", userInfo.getCreateTime());
                //统计当前渠道的放款量
                int loanCount = orderRepaymentMapper.findLoanSuccCount(param);
                /**渠道进量统计：放款人数统计*/
                channelProductLoanCount(userInfo, nowTime, loanCount);
                /**渠道统计：放款成功人数统计*/
                channelReportLoan(userInfo, nowTime, loanCount);
                /**渠道人员放款人数统计*/
                channelStaffLoanCount(userInfo.getChannelId(), nowTime, loanCount);
            } else {
                log.info("ChannelSyncCountService.loanIsSuccCount ---------------->申请时不是新用户,这里无需统计 phone={},channelId={},customerType={}", userInfo.getPhone(), userInfo.getChannelId(), customerType);
            }

        } catch (Exception e) {
            log.error("loanIsSuccCount error={}, phone={},channelId={},customerType={}", e, userInfo.getPhone(), userInfo.getChannelId(), customerType);
        }
    }

    @Async
    @Override
    public void appDownloadCount(UserInfo userInfo, Date nowTime) {
        //log.info("start invoke ChannelSyncCountService.appDownloadCount phone={},channelId={}", userInfo.getPhone(), userInfo.getChannelId());
        boolean getLock = false;
        try {
            //todo 若任务执行时间过短，则有可能在等锁的过程中2个服务任务都会获取到锁，这与实际需要的功能不一致，故需要将waitTime设置为0
            if (getLock = RedissLockUtil.tryLock(PREFIX + "appDownloadCount", 0, 60)) {
                log.info("appDownloadCount start phone={},channelId={}", userInfo.getPhone(), userInfo.getChannelId());
                PlateformChannelReport plateformChannelReport = findPlateformChannelReport(userInfo.getChannelId(), userInfo.getCreateTime());
                if (null != plateformChannelReport) {

                    PlateformChannelReport newPlateformChannelReport = new PlateformChannelReport();
                    newPlateformChannelReport.setId(plateformChannelReport.getId());
                    //下载人数+1
                    int downCount = plateformChannelReport.getDownCount();
                    newPlateformChannelReport.setDownCount(downCount + 1);
                    //更新时间
                    newPlateformChannelReport.setUpdateTime(nowTime);
                    plateformChannelReportDAO.updateByPrimaryKeySelective(newPlateformChannelReport);
                    log.info("ChannelSyncCountService.appDownloadCount 渠道统计：下载APP成功人数统计------------------------>更新成功 phone={},channelId={}", userInfo.getPhone(), userInfo.getChannelId());

                } else {
                    //如果该情况出现频率过高，说明在注册时未生成该数据，这里可以重新根据当前传进来的用户的注册时间，和渠道统计出总数， 然后生成一条以该注册时间为统计时间的渠道数据,（其他地方类似）
                    log.info("ChannelSyncCountService.appDownloadCount  渠道统计：下载APP成功人数统计------------------------>未查询到该用户注册当天，生成的渠道统计数据 phone={},channelId={}", userInfo.getPhone(), userInfo.getChannelId());
                }
            }
        } catch (Exception e) {
            log.error("ChannelSyncCountService.appDownloadCount  渠道统计：下载APP成功人数统计------------------------>更新失败 err={},phone={},channelId={}", e, userInfo.getPhone(), userInfo.getChannelId());
            e.printStackTrace();
        } finally {
            if (getLock) {
                RedissLockUtil.unlock(PREFIX + "appDownloadCount");
            }
        }


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
     * 查询当前时间的该渠道统计数据
     *
     * @param channelId
     * @param nowTime
     * @return
     */
    public PlateformChannelReport isChannelReportExist(Integer channelId, Date nowTime) {
        Map<String, Object> param = new HashMap<>();
        param.put("nowTime", DateUtil.formatTimeYmd(nowTime));
        param.put("channelId", channelId);
        return plateformChannelReportDAO.selectByParams(param);
    }

    /**
     * 渠道数据统计时：设置渠道的基本信息
     *
     * @param channelId
     * @param dailyStatisticsChannelProducts
     */
    public void setChannelInfo(Integer channelId, DailyStatisticsChannelProduct dailyStatisticsChannelProducts) {
        PlateformChannel plateformChannel = plateformChannelMapper.selectByPrimaryKey(channelId);
        if (null != plateformChannel) {
            //合作价格
            dailyStatisticsChannelProducts.setPrice(plateformChannel.getPrice());
            //合作模式
            dailyStatisticsChannelProducts.setCooperationMode(plateformChannel.getCooperationMode());
            //设置扣量比例
            dailyStatisticsChannelProducts.setFactor(plateformChannel.getDecreasePercentage());
            //渠道商表id
            dailyStatisticsChannelProducts.setChannelId(channelId);
            //渠道运营人员Id
            dailyStatisticsChannelProducts.setPlateformUserId(plateformChannel.getPlateformUserId());
        } else {
            log.info("ChannelSyncCountService.setChannelInfo渠道数据统计时：设置渠道的基本信息------------------------>未查询到对应渠道，渠道基本信息设置失败 channelId={}", channelId);
        }
    }

    /**
     * 每日渠道进量（注册人数统计）：数据也从这里生产，其他地方统计都只是更新对应字段
     *
     * @param channelId
     * @param nowTime
     */
    public void statisticsChannelProduct(Integer channelId, Date nowTime) {
        log.info("invoke statisticsChannelProduct 每日进量统计------channelId={},nowTime={}", channelId, DateUtil.formatTimeYmdHms(nowTime));
        DailyStatisticsChannelProduct channelProductExist = isChannelProductExist(channelId, nowTime);
        //已经存在该该时间的渠道进量数据
        if (null != channelProductExist) {
            try {
                DailyStatisticsChannelProduct newDailyStatisticsChannelProduct = new DailyStatisticsChannelProduct();
                newDailyStatisticsChannelProduct.setId(channelProductExist.getId());
                //真实注册量
                newDailyStatisticsChannelProduct.setRegisterNumber(channelProductExist.getRegisterNumber() + 1);
                //下线：链接可以访问。渠道方数据不再增加（点击下线就把此时的渠道方的数据存起来，直到上线前渠道方一直获取这个假数据，上线后再获取真实数据），我方真实数据不变
                PlateformChannel plateformChannel = plateformChannelMapper.selectByPrimaryKey(channelId);
                if (null != plateformChannel && null != plateformChannel.getStatus()) {
                    int status = plateformChannel.getStatus();
                    //当用户下线时，渠道方显示的channel_register_count不再增加
                    if (Constant.CHANNEL_INSERTING_COIL == status) {

                    } else {
                        //如果实际量大于6就要进行扣量计算
                        if (newDailyStatisticsChannelProduct.getRegisterNumber() > Constant.CHANNEL_COUNT) {
                            //渠道扣量后的注册量
                            int registerNumberChannel = getNumberChannel(newDailyStatisticsChannelProduct.getRegisterNumber(), channelProductExist.getFactor());
                            newDailyStatisticsChannelProduct.setRegisterNumberChannel(registerNumberChannel);
                        } else {
                            newDailyStatisticsChannelProduct.setRegisterNumberChannel(newDailyStatisticsChannelProduct.getRegisterNumber());
                        }
                    }
                }
                //更新时间
                newDailyStatisticsChannelProduct.setUpdateTime(nowTime);
                //更新表(daily_statistics_channel_product)
                dailyStatisticsChannelProductDAO.updateByPrimaryKeySelective(newDailyStatisticsChannelProduct);
                log.info("ChannelSyncCountService.registerIsSuccCount 进量统计：注册人数统计------------------------>更新成功 channelId={},nowTime={}", channelId, DateUtil.formatTimeYmdHms(nowTime));
            } catch (Exception e) {
                log.error("ChannelSyncCountService.registerIsSuccCount 进量统计：注册人数统计------------------------>更新失败 err={} ,channelId={},nowTime={}", e, channelId, DateUtil.formatTimeYmdHms(nowTime));
                e.printStackTrace();
            }

        } else {
            try {
                DailyStatisticsChannelProduct dailyStatisticsChannelProduct = new DailyStatisticsChannelProduct();
                //实际注册数量为1
                dailyStatisticsChannelProduct.setRegisterNumber(1);
                //设置渠道注册量
                dailyStatisticsChannelProduct.setRegisterNumberChannel(1);
                //创建时间
                dailyStatisticsChannelProduct.setCreateTime(nowTime);
                //统计时间
                dailyStatisticsChannelProduct.setReportTime(nowTime);
                //设置渠道的基本信息(扣量比例、渠道id、渠道运营人员id)
                setChannelInfo(channelId, dailyStatisticsChannelProduct);
                //插入（daily_statistics_channel_product）
                dailyStatisticsChannelProductDAO.insertSelective(dailyStatisticsChannelProduct);
                log.info("ChannelSyncCountService.registerIsSuccCount 进量统计：注册人数统计--------------->插入成功 channelId={},nowTime={}", channelId, DateUtil.formatTimeYmdHms(nowTime));
            } catch (Exception e) {
                log.error("ChannelSyncCountService.registerIsSuccCount 进量统计：注册人数统计------------>插入失败 err={}, channelId={},nowTime={}", e, channelId, DateUtil.formatTimeYmdHms(nowTime));
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取扣量后的数量 ：渠道方
     *
     * @param realCount          真实数量
     * @param decreasePercentage 扣量系数
     * @return
     */
    public int getNumberChannel(int realCount, BigDecimal decreasePercentage) {
        //如果扣量系数为0，返回真实数据，表示不扣量
        if (decreasePercentage.intValue() == 0) {
            return realCount;
        }
        //如果扣量系数为100，表示全部扣，返回0
        if (decreasePercentage.intValue() == 100) {
            return 0;
        }
        //扣量后的注册量：（真实注册量-5）* 扣量比例（:decreasePercentage）+5
        BigDecimal change = new BigDecimal(100);
        //真实注册量-5
        BigDecimal registerNumber = new BigDecimal((realCount - Constant.DECREASE_PERCENTAGE_FIVE));
        //乘 扣量比例  除100（转换比例）
        BigDecimal registerNumberChannel = registerNumber.multiply(change.subtract(decreasePercentage)).divide(change, 2, BigDecimal.ROUND_HALF_UP);
        return registerNumberChannel.intValue() + Constant.DECREASE_PERCENTAGE_FIVE;
    }

    /**
     * 根据注册时间和渠道id查询对应的渠道人员统计
     *
     * @param channelName
     * @param nowTime
     */
    public PlatformChannelStaff findPlatformChannelStaff(String channelName, Date nowTime) {
        Map<String, Object> param = new HashMap<>();
        param.put("nowTime", nowTime);
        param.put("channelName", channelName);
        return platformChannelStaffDAO.selectByReportTimeChannelId(param);
    }

    /**
     * 人员统计渠道人员统计时：设置渠道基本信息
     */
    public void setChannelStaffInfo(Integer channelId, PlatformChannelStaff platformChannelStaff) {
        PlateformChannel plateformChannel = plateformChannelMapper.selectByPrimaryKey(channelId);
        if (null != plateformChannel) {
            //渠道名称
            platformChannelStaff.setChannelName(plateformChannel.getChannelName());
            //合作价格
            platformChannelStaff.setPrice(plateformChannel.getPrice());
            //运营人员id
            platformChannelStaff.setPlateformUserId(plateformChannel.getPlateformUserId());
            //扣量系数
            platformChannelStaff.setDecreasePercentage(plateformChannel.getDecreasePercentage());
        } else {
            log.info("ChannelSyncCountService.setChannelStaffInfo 人员统计渠道人员统计时：设置渠道基本信息------------------------>未查询到查到对应的渠道，设置失败 channelId={}", channelId);
        }
    }

    /**
     * 渠道运营人员：统计注册数
     *
     * @param channelId
     * @param nowTime
     */
    public void platformChannelStaffCount(Integer channelId, Date nowTime) {
        log.info("ChannelSyncCountService.platformChannelStaffCount 渠道人员统计： channelId={},nowTime={}", channelId, DateUtil.formatTimeYmdHms(nowTime));
        PlateformChannel plateformChannel = plateformChannelMapper.selectByPrimaryKey(channelId);
        if (null != plateformChannel) {
            PlatformChannelStaff platformChannelStaff = findPlatformChannelStaff(plateformChannel.getChannelName(), nowTime);
            //不等于空，就更新注册量
            if (null != platformChannelStaff) {
                try {
                    PlatformChannelStaff newPlatformChannelStaff = new PlatformChannelStaff();
                    newPlatformChannelStaff.setId(platformChannelStaff.getId());
                    //注册人数+1
                    newPlatformChannelStaff.setReallyRegister(platformChannelStaff.getReallyRegister() + 1);

                    //下线：链接可以访问。渠道方数据不再增加（点击下线就把此时的渠道方的数据存起来，直到上线前渠道方一直获取这个假数据，上线后再获取真实数据），我方真实数据不变
                    if (null != plateformChannel.getStatus()) {
                        int status = plateformChannel.getStatus();
                        //当用户下线时，渠道方显示的channel_register_count不再增加
                        if (Constant.CHANNEL_INSERTING_COIL != status) {
                            //大于6，就要开始扣量计算
                            if (newPlatformChannelStaff.getReallyRegister() > Constant.CHANNEL_COUNT) {
                                //计算渠道注册量
                                int registerNumberChannel = getNumberChannel(newPlatformChannelStaff.getReallyRegister(), platformChannelStaff.getDecreasePercentage());
                                newPlatformChannelStaff.setChannelRegister(registerNumberChannel);
                            } else {
                                //如果注册量小于6，就按真实注册量
                                newPlatformChannelStaff.setChannelRegister(newPlatformChannelStaff.getReallyRegister());
                            }
                        }
                    }
                    //注册成本计算（已改为在Sql中计算）
               /* Double recost = null;
                if (null!= platformChannelStaff.getChannelRegister() && null!=platformChannelStaff.getReallyRegister()){
                    BigDecimal price = new BigDecimal(platformChannelStaff.getPrice()/100);
                    //扣量注册数
                    BigDecimal chRegister = new BigDecimal(newPlatformChannelStaff.getChannelRegister());
                    //真实注册数
                    BigDecimal reRegister = new BigDecimal(newPlatformChannelStaff.getReallyRegister());
                    //注册成本（扣量注册数*单价/注册人数）
                    recost = chRegister.multiply(price).divide(reRegister,2,BigDecimal.ROUND_HALF_UP).doubleValue()*Constant.CENT_CHANGE_DOLLAR;
                }
                if (null!=recost){
                    newPlatformChannelStaff.setRegisterCost(recost.intValue());
                }*/
                    platformChannelStaffDAO.updateByPrimaryKeySelective(newPlatformChannelStaff);
                    log.info("ChannelSyncCountService.platformChannelStaffCount 渠道人员统计：注册人数统计------------------------>更新成功 channelId={},nowTime={}", channelId, DateUtil.formatTimeYmdHms(nowTime));
                } catch (Exception e) {
                    log.error("渠道运营人员：统计注册数注册人数,更新失败  err={},channelId={},nowTime={}", e, channelId, DateUtil.formatTimeYmdHms(nowTime));
                    e.printStackTrace();
                }
            } else {
                try {
                    //为空就插入一条
                    platformChannelStaff = new PlatformChannelStaff();
                    //注册量设置为1
                    platformChannelStaff.setReallyRegister(1);
                    //渠道注册量设置为1
                    platformChannelStaff.setChannelRegister(1);
                    //设置渠道相关基本信息
                    setChannelStaffInfo(channelId, platformChannelStaff);
                    //设置统计时间
                    platformChannelStaff.setReportTime(nowTime);
                    //注册成本（扣量注册数*单价/注册人数:已在sql中计算）
                /*Double recost = null;
                if (null!= platformChannelStaff.getChannelRegister() && null!=platformChannelStaff.getReallyRegister()){
                    BigDecimal price = new BigDecimal(platformChannelStaff.getPrice()/100);
                    //扣量注册数
                    BigDecimal chRegister = new BigDecimal(platformChannelStaff.getChannelRegister());
                    //真实注册数
                    BigDecimal reRegister = new BigDecimal(platformChannelStaff.getReallyRegister());
                    //注册成本（扣量注册数*单价/注册人数）
                    recost = chRegister.multiply(price).divide(reRegister,2,BigDecimal.ROUND_HALF_UP).doubleValue()*Constant.CENT_CHANGE_DOLLAR;
                }
                if (null!=recost){
                    platformChannelStaff.setRegisterCost(recost.intValue());
                }*/
                    platformChannelStaffDAO.insertSelective(platformChannelStaff);
                    log.info("ChannelSyncCountService.platformChannelStaffCount 渠道运营人员：统计注册数注册人数------------------------>插入成功 channelId={},nowTime={}", channelId, DateUtil.formatTimeYmdHms(nowTime));

                } catch (Exception e) {
                    log.error("ChannelSyncCountService.platformChannelStaffCount 渠道运营人员：统计注册数注册人数-插入失败 error={},channelId={},nowTime={}", e, channelId, DateUtil.formatTimeYmdHms(nowTime));
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 根据渠道id，和传进来的时间查询
     *
     * @param channelId
     * @param nowTime
     */
    public PlateformChannelReport findPlateformChannelReport(Integer channelId, Date nowTime) {
        Map<String, Object> param = new HashMap<>();
        param.put("nowTime", nowTime);
        param.put("channelId", channelId);
        return plateformChannelReportDAO.selectByParams(param);
    }

    /**
     * 渠道统计：设置渠道基本
     *
     * @param channelId
     * @param plateformChannelReport
     */
    public void setChannelReportInfo(Integer channelId, PlateformChannelReport plateformChannelReport) {
        PlateformChannel plateformChannel = plateformChannelMapper.selectByPrimaryKey(channelId);
        if (null != plateformChannel) {
            //渠道注册量
            if (null != plateformChannel && null != plateformChannel.getStatus()) {
                int status = plateformChannel.getStatus();
                //当用户下线时，渠道方显示的channel_register_count不再增加
                if (Constant.CHANNEL_INSERTING_COIL == status) {

                } else {
                    plateformChannelReport.setChannelRegisterCount(1);
                }
            }
            //设置渠道ID
            plateformChannelReport.setChannelid(plateformChannel.getId());
            //渠道系数
            plateformChannelReport.setDedutionCoefficient(plateformChannel.getDecreasePercentage());
        } else {
            log.info("ChannelSyncCountService.setChannelReportInfo 渠道统计：设置渠道基本------------------------>未查询到对应渠道信息 channelId={}", channelId);
        }
    }

    /**
     * 渠道统计：注册人数统计
     *
     * @param channelId
     * @param nowTime
     */
    public void plateformChannelReportCount(Integer channelId, Date nowTime, UserInfo pwdById) {
        log.info("start invoke ChannelSyncCountService.plateformChannelReportCount  渠道统计：注册人数统计-----channelId={},nowTime={}", channelId, DateUtil.formatTimeYmdHms(nowTime));
        PlateformChannelReport plateformChannelReport = findPlateformChannelReport(channelId, nowTime);
        //存在渠道统计数据
        if (null != plateformChannelReport) {
            try {
                PlateformChannelReport newPlateformChannelReport = new PlateformChannelReport();
                newPlateformChannelReport.setId(plateformChannelReport.getId());
                //安卓+1
                if (null != pwdById && pwdById.getClientType() == 1) {
                    newPlateformChannelReport.setChannelAnZuo(plateformChannelReport.getChannelAnZuo() + 1);
                } else if (null != pwdById && pwdById.getClientType() == 2) {
                    //ios+1
                    newPlateformChannelReport.setChannelIos(plateformChannelReport.getChannelIos() + 1);
                }
                //ios
                //注册量+1
                newPlateformChannelReport.setRegisterCount(plateformChannelReport.getRegisterCount() + 1);
                //更新时间
                newPlateformChannelReport.setUpdateTime(nowTime);
                //渠道注册量
                int channelRegisterCount = plateformChannelReport.getChannelRegisterCount();

                //下线：链接可以访问。渠道方数据不再增加（点击下线就把此时的渠道方的数据存起来，直到上线前渠道方一直获取这个假数据，上线后再获取真实数据），我方真实数据不变
                PlateformChannel plateformChannel = plateformChannelMapper.selectByPrimaryKey(channelId);
                if (null != plateformChannel && null != plateformChannel.getStatus()) {
                    int status = plateformChannel.getStatus();
                    //当用户下线时，渠道方显示的channel_register_count不再增加
                    if (Constant.CHANNEL_INSERTING_COIL != status) {
                        //如果渠道数量>6开始扣量计算
                        channelRegisterCount = channelRegisterCount + 1;
                        newPlateformChannelReport.setChannelRegisterCount(channelRegisterCount);
                    /*
                    if( channelRegisterCount> Constant.CHANNEL_COUNT){
                        //传入真实数量和扣量比例，计算扣量过后的注册量
                        int registerNumberChannel = getNumberChannel(channelRegisterCount, plateformChannelReport.getDedutionCoefficient());
                        newPlateformChannelReport.setChannelRegisterCount(registerNumberChannel);
                      }else {
                        newPlateformChannelReport.setChannelRegisterCount(channelRegisterCount);
                    }*/
                    }
                }
                plateformChannelReportDAO.updateByPrimaryKeySelective(newPlateformChannelReport);
                log.info("ChannelSyncCountService.plateformChannelReportCount  渠道统计：注册人数统计------------------------>更新成功 channelId={},nowTime={}", channelId, DateUtil.formatTimeYmdHms(nowTime));
            } catch (Exception e) {
                log.error("ChannelSyncCountService.plateformChannelReportCount  渠道统计：注册人数统计---->更新失败  channelId={},nowTime={},error={}", channelId, DateUtil.formatTimeYmdHms(nowTime), e);
                e.printStackTrace();
            }

        } else {
            try {
                //不存在渠道统计数据
                plateformChannelReport = new PlateformChannelReport();
                //注册为1
                plateformChannelReport.setRegisterCount(1);
                PlateformChannel plateformChannel = plateformChannelMapper.selectByPrimaryKey(channelId);
                if (null != plateformChannel && null != plateformChannel.getStatus()) {
                    int status = plateformChannel.getStatus();
                    //当用户下线时，渠道方显示的channel_register_count不再增加
                    if (Constant.CHANNEL_INSERTING_COIL == status) {

                    } else {
                        plateformChannelReport.setChannelRegisterCount(1);
                    }
                }
                //创建时间
                plateformChannelReport.setCreateTime(nowTime);
                //设置渠道基本信息
                setChannelReportInfo(channelId, plateformChannelReport);
                //统计时间
                plateformChannelReport.setReportTime(nowTime);
                plateformChannelReportDAO.insertSelective(plateformChannelReport);
                log.info("ChannelSyncCountService.plateformChannelReportCount  渠道统计：注册人数统计------------------------>插入成功 channelId={},nowTime={}", channelId, DateUtil.formatTimeYmdHms(nowTime));
            } catch (Exception e) {
                log.error("ChannelSyncCountService.plateformChannelReportCount  渠道统计：注册人数统计------->插入失败  channelId={},nowTime={},error={}", channelId, DateUtil.formatTimeYmdHms(nowTime), e);
                e.printStackTrace();
            }
        }
    }

    /**
     * 渠道进量统计：申请人数统计
     *
     * @param channelId
     * @param userInfo
     * @param nowTime
     */
    public void channelProductApplyCount(Integer channelId, UserInfo userInfo, Date nowTime) {
        log.info(" start invoke ChannelSyncCountService.channelProductApplyCount ---------------->更新申请人数统计成功 channelId={},phone={}", channelId, userInfo.getPhone());
        //通过该用户渠道id和注册时间，查找
        DailyStatisticsChannelProduct channelProductExist = isChannelProductExist(channelId, userInfo.getCreateTime());
        //如果已经存在该条渠道进量的数据
        if (null != channelProductExist) {
            try {
                DailyStatisticsChannelProduct newChannelProduct = new DailyStatisticsChannelProduct();
                newChannelProduct.setId(channelProductExist.getId());
                //申请人数+1
                newChannelProduct.setPplicationNumber(channelProductExist.getPplicationNumber() + 1);
                //更新时间
                newChannelProduct.setUpdateTime(nowTime);
                dailyStatisticsChannelProductDAO.updateByPrimaryKeySelective(newChannelProduct);
                log.info("ChannelSyncCountService.channelProductApplyCount ---------------->更新申请人数统计成功 channelId={},phone={}", channelId, userInfo.getPhone());
            } catch (Exception e) {
                log.error("ChannelSyncCountService.channelProductApplyCount ------>更新申请人数统计失败  channelId={},phone={},error={}", channelId, userInfo.getPhone(), e);
                e.printStackTrace();
            }
        } else {
            log.info("ChannelSyncCountService.channelProductApplyCount ---------------->未查询该用户注册时生成的 进量统计数据 channelId={},phone={}", channelId, userInfo.getPhone());
        }
    }

    /**
     * 渠道进量统计放款人数统计
     *
     * @param nowtime
     * @param userInfo
     */
    public void channelProductLoanCount(UserInfo userInfo, Date nowtime, int loanCount) {
        log.info("ChannelSyncCountService.channelProductLoanCount ---------------->phone={},channelId={},loanCount={}", userInfo.getPhone(), userInfo.getChannelId(), loanCount);
        //通过该用户渠道id和注册时间，查找
        DailyStatisticsChannelProduct channelProductExist = isChannelProductExist(userInfo.getChannelId(), userInfo.getCreateTime());
        //如果已经存在该条渠道进量的数据
        if (null != channelProductExist) {
            try {
                DailyStatisticsChannelProduct newChannelProduct = new DailyStatisticsChannelProduct();
                newChannelProduct.setId(channelProductExist.getId());
                //放款数量+1
                newChannelProduct.setLoanNumberChannel(loanCount);
                //更新时间
                newChannelProduct.setUpdateTime(nowtime);
                dailyStatisticsChannelProductDAO.updateByPrimaryKeySelective(newChannelProduct);
                log.info("ChannelSyncCountService.channelProductLoanCount ---------------->更新放款人数统计成功 phone={},channelId={},loanCount={}", userInfo.getPhone(), userInfo.getChannelId(), loanCount);
            } catch (Exception e) {
                log.error("ChannelSyncCountService.channelProductLoanCount ---------------->更新放款人数统计失败 phone={},channelId={},loanCount={},error={}", userInfo.getPhone(), userInfo.getChannelId(), loanCount, e);
                e.printStackTrace();
            }
        } else {
            log.info("ChannelSyncCountService.channelProductLoanCount ---------------->未查询该用户注册时生成的 进量统计数据 phone={},channelId={},loanCount={}", userInfo.getPhone(), userInfo.getChannelId(), loanCount);
        }
    }

    /**
     * 渠道统计：申请人数统计
     *
     * @param userInfo
     * @param nowTime
     */
    public void channelReportApply(UserInfo userInfo, Date nowTime) {
        log.info("ChannelSyncCountService.channelReportApply ---------------->未查询该用户注册时生成的 进量统计数据 phone={},channelId={},loanCount={}", userInfo.getPhone(), userInfo.getChannelId());
        PlateformChannelReport plateformChannelReport = findPlateformChannelReport(userInfo.getChannelId(), userInfo.getCreateTime());
        if (null != plateformChannelReport) {
            try {
                PlateformChannelReport newPlateformChannelReport = new PlateformChannelReport();
                newPlateformChannelReport.setId(plateformChannelReport.getId());
                //申请人数+1
                newPlateformChannelReport.setBorrowApplyCount(plateformChannelReport.getBorrowApplyCount() + 1);
                //更新时间
                newPlateformChannelReport.setUpdateTime(nowTime);
                //下线：链接可以访问。渠道方数据不再增加（点击下线就把此时的渠道方的数据存起来，直到上线前渠道方一直获取这个假数据，上线后再获取真实数据），我方真实数据不变
                PlateformChannel plateformChannel = plateformChannelMapper.selectByPrimaryKey(userInfo.getChannelId());
                if (null != plateformChannel && null != plateformChannel.getStatus()) {
                    int status = plateformChannel.getStatus();
                    //当用户下线时，渠道方显示的channel_apply_count不再增加
                    if (Constant.CHANNEL_INSERTING_COIL != status) {
                        //渠道申请人数
                        int channelApplyCount = plateformChannelReport.getChannelApplyCount();
                        /**如果申请当天时间，不在用户注册当天，就不用把渠道方的申请人数记录在过去注册那天的数据上了: 如果两个时间相差为0，就说明注册当天申请的*/
                        int day = DateUtil.daysBetween(userInfo.getCreateTime(), nowTime);
                        if (0 == day) {
                            newPlateformChannelReport.setChannelApplyCount(channelApplyCount + 1);
                        }
                        //如果申请人数大于6，就要开始扣量计算，返回扣量过后的的结果
                        /*if (channelApplyCount> Constant.CHANNEL_COUNT){
                            int applyNumberChannel = getNumberChannel(channelApplyCount, plateformChannelReport.getDedutionCoefficient());
                            //设置成扣量过后的申请数据
                             newPlateformChannelReport.setChannelApplyCount(applyNumberChannel);
                        }else {
                            newPlateformChannelReport.setChannelApplyCount(channelApplyCount);
                        }*/

                    }
                }
                plateformChannelReportDAO.updateByPrimaryKeySelective(newPlateformChannelReport);
                log.info("ChannelSyncCountService.channelReportApply 渠道统计：申请人数统计------------------------>更新成功 phone={},channelId={},loanCount={}", userInfo.getPhone(), userInfo.getChannelId());
            } catch (Exception e) {
                log.error("ChannelSyncCountService.channelReportApply  渠道统计：申请人数统计------------------------>更新失败 phone={},channelId={},loanCount={},error={}", userInfo.getPhone(), userInfo.getChannelId(), e);
                e.printStackTrace();
            }
        } else {
            log.info("ChannelSyncCountService.channelReportApply  渠道统计：申请人数统计------------------------>未查询到该用户注册当天，生成的渠道统计数据 phone={},channelId={}", userInfo.getPhone(), userInfo.getChannelId());
        }
    }

    /**
     * 渠道统计：放款成功人数统计
     *
     * @param userInfo
     * @param nowTime
     */
    public void channelReportLoan(UserInfo userInfo, Date nowTime, int loanCount) {
        log.info("ChannelSyncCountService.channelReportLoan  渠道统计：申请人数统计------------------------>未查询到该用户注册当天，生成的渠道统计数据 phone={},channelId={},loanCount={}", userInfo.getPhone(), userInfo.getChannelId(), loanCount);
        PlateformChannelReport plateformChannelReport = findPlateformChannelReport(userInfo.getChannelId(), userInfo.getCreateTime());
        if (null != plateformChannelReport) {
            try {
                PlateformChannelReport newPlateformChannelReport = new PlateformChannelReport();
                newPlateformChannelReport.setId(plateformChannelReport.getId());
                //放款人数+1
                newPlateformChannelReport.setLoanCount(loanCount);
                //下线：链接可以访问。渠道方数据不再增加（点击下线就把此时的渠道方的数据存起来，直到上线前渠道方一直获取这个假数据，上线后再获取真实数据），我方真实数据不变
                PlateformChannel plateformChannel = plateformChannelMapper.selectByPrimaryKey(userInfo.getChannelId());
                if (null != plateformChannel && null != plateformChannel.getStatus()) {
                    int status = plateformChannel.getStatus();
                    //当用户下线时，渠道方显示的channel_Loan_count不再增加
                    if (Constant.CHANNEL_INSERTING_COIL != status) {
                        /*int channelLoanCount = plateformChannelReport.getChannelLoanCount();
                        channelLoanCount = channelLoanCount + 1;*/
                        /**如果放款当天时间，不在用户注册当天，就不用把渠道方的放款人数记录在过去注册那天的数据上了（我方数据不变）: 如果两个时间相差为0，就说明放款当天放款的*/
                        int day = DateUtil.daysBetween(userInfo.getCreateTime(), nowTime);
                        if (0 == day) {
                            newPlateformChannelReport.setChannelLoanCount(loanCount);
                        }
                        //放款人数大于6,就要同时开始扣量计算
                       /* if (channelLoanCount > Constant.CHANNEL_COUNT) {
                            int numberChannel = getNumberChannel(channelLoanCount, plateformChannelReport.getDedutionCoefficient());
                            //设置扣量过后的放款数量
                            newPlateformChannelReport.setChannelLoanCount(numberChannel);
                        } else {
                            newPlateformChannelReport.setChannelLoanCount(channelLoanCount);
                        }*/

                    }
                }
                //更新时间
                newPlateformChannelReport.setUpdateTime(nowTime);
                plateformChannelReportDAO.updateByPrimaryKeySelective(newPlateformChannelReport);
                log.info("ChannelSyncCountService.channelReportLoan 渠道统计：放款成功人数统计------------------------>更新成功 phone={},channelId={},loanCount={}", userInfo.getPhone(), userInfo.getChannelId(), loanCount);
            } catch (Exception e) {
                log.error("ChannelSyncCountService.channelReportLoan  渠道统计：放款成功人数统计------------------------>更新失败 phone={},channelId={},loanCount={},error={} ", userInfo.getPhone(), userInfo.getChannelId(), loanCount, e);
                e.printStackTrace();
            }
        } else {
            //如果该情况出现频率过高，说明在注册时未生成该数据，这里可以重新根据当前传进来的用户的注册时间，和渠道统计出总数， 然后生成一条以该注册时间为统计时间的渠道数据,（其他地方类似）
            log.info("ChannelSyncCountService.channelReportLoan  渠道统计：申请人数统计------------------------>未查询到该用户注册当天，生成的渠道统计数据 phone={},channelId={},loanCount={}", userInfo.getPhone(), userInfo.getChannelId(), loanCount);
        }
    }

    /**
     * PV uv统计
     *
     * @param time
     */
    @Async
    @Override
    public void pvUvCount(Date time) {
        log.info("ChannelSyncCountService.pvUvCount ---------------->pv、uv统计 time={}", DateUtil.formatTimeYmdHms(time));
        boolean getLock = false;
        try {
            //todo 若任务执行时间过短，则有可能在等锁的过程中2个服务任务都会获取到锁，这与实际需要的功能不一致，故需要将waitTime设置为0
            if (getLock = RedissLockUtil.tryLock(PREFIX + "pvUvCount", 0, 60)) {
                log.info("pvUvCount start");
                //pv,uv（查询IP地址表，pv是登录一次的ip地址，uv是重复的ip地址只记录一次）（后期再改批量进行修改）
                //查询所有的渠道
                List<PlateformChannel> plateformChannels = plateformChannelMapper.findParams(null);
                List<DailyStatisticsChannelProduct> dailyStatisticsChannelProducts = new ArrayList<>();
                IpAddressLogQuery ipAddressLogQuery = new IpAddressLogQuery();
                //访问时间
                ipAddressLogQuery.setCreateTime(DateUtil.formatTimeYmd(time));
                //渠道访问的URL
                ipAddressLogQuery.setUrl(Constant.PU_UV_URL);
                for (PlateformChannel plateformChannel : plateformChannels) {
                    //渠道编码名称
                    ipAddressLogQuery.setChannelCode(plateformChannel.getChannelName());
                    //统计pv、uv
                    DailyStatisticsChannelProduct pvUvChannelProduct = ipAddressLogDAO.getPvUv(ipAddressLogQuery);
                    //设置渠道id
                    pvUvChannelProduct.setChannelId(plateformChannel.getId());
                    //装进集合中
                    dailyStatisticsChannelProducts.add(pvUvChannelProduct);
                }
                for (DailyStatisticsChannelProduct dailyStatisticsChannelProduct : dailyStatisticsChannelProducts) {
                    DailyStatisticsChannelProduct channelProductExist = isChannelProductExist(dailyStatisticsChannelProduct.getChannelId(), time);
                    //如果已经存在该条渠道进量的数据
                    if (null != channelProductExist) {
                        try {
                            DailyStatisticsChannelProduct newChannelProductExist = new DailyStatisticsChannelProduct();
                            newChannelProductExist.setId(channelProductExist.getId());
                            //uv数
                            newChannelProductExist.setUv(dailyStatisticsChannelProduct.getUv());
                            //pv数
                            newChannelProductExist.setPv(dailyStatisticsChannelProduct.getPv());
                            //更新时间
                            newChannelProductExist.setUpdateTime(time);
                            dailyStatisticsChannelProductDAO.updateByPrimaryKeySelective(newChannelProductExist);
                            log.info("ChannelSyncCountService.pvUvCount ---------------->更新uv统计成功 ");
                        } catch (Exception e) {
                            log.error("ChannelSyncCountService.pvUvCount ---------------->更新uv统计失败 err={}", e);
                            e.printStackTrace();
                        }
                    } else {
                        DailyStatisticsChannelProduct newdailyStatisticsChannelProduct = new DailyStatisticsChannelProduct();
                        //pv
                        newdailyStatisticsChannelProduct.setPv(dailyStatisticsChannelProduct.getPv());
                        //设置uv
                        newdailyStatisticsChannelProduct.setUv(dailyStatisticsChannelProduct.getUv());
                        //创建时间
                        newdailyStatisticsChannelProduct.setCreateTime(time);
                        //统计时间
                        newdailyStatisticsChannelProduct.setReportTime(time);
                        //设置渠道的基本信息(扣量比例、渠道id、渠道运营人员id)
                        setChannelInfo(dailyStatisticsChannelProduct.getChannelId(), newdailyStatisticsChannelProduct);
                        //插入（daily_statistics_channel_product）
                        dailyStatisticsChannelProductDAO.insertSelective(newdailyStatisticsChannelProduct);
                        log.info("ChannelSyncCountService.pvUvCount 进量统计：pv、uv数统计------------------------>插入成功 ");
                    }
                }
            }
        } catch (Exception e) {
            log.error("ChannelSyncCountService.pvUvCount 进量统计：pv、uv数统计------------------------>插入失败 err={}", e);
            e.printStackTrace();
        } finally {
            if (getLock) {
                RedissLockUtil.unlock(PREFIX + "pvUvCount");
            }
        }


    }

    @Async
    @Override
    public void deliveryConnectionCount(Date nowTime, Integer plateformUserId, Integer channelId) {
        boolean getLock = false;
        try {
            //todo 若任务执行时间过短，则有可能在等锁的过程中2个服务任务都会获取到锁，这与实际需要的功能不一致，故需要将waitTime设置为0
            if (getLock = RedissLockUtil.tryLock(PREFIX + "deliveryConnectionCount", 0, 30)) {
                PlateformChannel plateformChannel = plateformChannelMapper.selectByPrimaryKey(channelId);
                if (null != plateformChannel) {
                    PlatformChannelStaff platformChannelStaff = findPlatformChannelStaff(plateformChannel.getChannelName(), nowTime);
                    //不等于空，就更新
                    if (null != platformChannelStaff) {
                        PlatformChannelStaff newPlatformChannelStaff = new PlatformChannelStaff();
                        newPlatformChannelStaff.setId(platformChannelStaff.getId());
                        //投放数+1
                        newPlatformChannelStaff.setDeliveryConnection(platformChannelStaff.getDeliveryConnection() + 1);
                        platformChannelStaffDAO.updateByPrimaryKeySelective(newPlatformChannelStaff);
                        log.info("ChannelSyncCountService.deliveryConnectionCount 渠道运营人员:投放连接数统计------------------------>更新成功plateformUserId={},channelId={}", plateformUserId, channelId);

                    } else {
                        //为空就插入一条
                        platformChannelStaff = new PlatformChannelStaff();
                        //投放连接数
                        platformChannelStaff.setDeliveryConnection(1);
                        //设置渠道相关基本信息
                        setChannelStaffInfo(channelId, platformChannelStaff);
                        //设置统计时间
                        platformChannelStaff.setReportTime(nowTime);
                        //注册成本（扣量注册数*单价/注册人数）
                        int registerCost = 0;
                        if (null != platformChannelStaff.getChannelRegister() && null != platformChannelStaff.getReallyRegister()) {
                            registerCost = platformChannelStaff.getChannelRegister() * platformChannelStaff.getPrice() / platformChannelStaff.getReallyRegister();
                        }
                        platformChannelStaff.setRegisterCost(registerCost);
                        platformChannelStaffDAO.insertSelective(platformChannelStaff);
                        log.info("ChannelSyncCountService.deliveryConnectionCount 渠道运营人员:投放连接数统计------------------------>插入成功plateformUserId={},channelId={}", plateformUserId, channelId);
                    }
                }
            }
        } catch (Exception e) {
            log.error("ChannelSyncCountService.deliveryConnectionCount 渠道运营人员:投放连接数统计------------------------>更新失败 err={},plateformUserId={},channelId={}", e, plateformUserId, channelId);
            e.printStackTrace();
        } finally {
            if (getLock) {
                RedissLockUtil.unlock(PREFIX + "deliveryConnectionCount");
            }
        }


    }

    /**
     * 渠道放款统计
     */
    public void channelStaffLoanCount(Integer channelId, Date nowTime, int loanCount) {
        log.info("start invoke ChannelSyncCountService.channelStaffLoanCount 渠道运营人员:放款人数---channelId={},loanCount={},nowTime={}", channelId, loanCount, DateUtil.formatTimeYmdHms(nowTime));
        PlateformChannel plateformChannel = plateformChannelMapper.selectByPrimaryKey(channelId);
        if (null != plateformChannel) {
            PlatformChannelStaff platformChannelStaff = findPlatformChannelStaff(plateformChannel.getChannelName(), nowTime);
            //不等于空，就更新
            if (null != platformChannelStaff) {
                try {
                    PlatformChannelStaff newPlatformChannelStaff = new PlatformChannelStaff();
                    newPlatformChannelStaff.setId(platformChannelStaff.getId());
                    //放款数+1
                    newPlatformChannelStaff.setLoanCost(loanCount);
                    platformChannelStaffDAO.updateByPrimaryKeySelective(newPlatformChannelStaff);
                    log.info("ChannelSyncCountService.channelStaffLoanCount 渠道运营人员:放款人数------------------------>更新成功 channelId={},loanCount={},nowTime={}", channelId, loanCount, DateUtil.formatTimeYmdHms(nowTime));
                } catch (Exception e) {
                    log.error("ChannelSyncCountService.channelStaffLoanCount 渠道运营人员:放款人数------------------------>更新失败 channelId={},loanCount={},nowTime={},error={}", channelId, loanCount, DateUtil.formatTimeYmdHms(nowTime), e);
                    e.printStackTrace();
                }
            } else {
                try {
                    //为空就插入一条
                   /* platformChannelStaff = new PlatformChannelStaff();
                    //投放放款人数
                   // platformChannelStaff.setLoanCost(loanCount);
                    //设置渠道相关基本信息
                    setChannelStaffInfo(channelId, platformChannelStaff);
                    //设置统计时间
                    platformChannelStaff.setReportTime(nowTime);
                    platformChannelStaffDAO.insertSelective(platformChannelStaff);*/
                    //log.info("ChannelSyncCountService.channelStaffLoanCount 渠道运营人员:放款人数------------------------>插入成功");
                } catch (Exception e) {
                    log.error("ChannelSyncCountService.channelStaffLoanCount 渠道运营人员:放款人数------------------------>更新失败 channelId={},loanCount={},nowTime={},error={}", channelId, loanCount, DateUtil.formatTimeYmdHms(nowTime), e);
                    e.printStackTrace();
                }
            }
        }

    }

    @Async
    @Override
    public void pvUvAsyncCount(PlateformChannel plateformChannel, Date time) {
        log.info("ChannelSyncCountService.pvUvCount ---------------->pv、uv统计 channelId={} time={}", plateformChannel.getId(), DateUtil.formatTimeYmdHms(time));
        boolean getLock = false;
        try {
            //todo 若任务执行时间过短，则有可能在等锁的过程中2个服务任务都会获取到锁，这与实际需要的功能不一致，故需要将waitTime设置为0
            if (getLock = RedissLockUtil.tryLock(PREFIX + "pvUvAsyncCount", 0, 60)) {
                log.info("pvUvAsyncCount start");

                IpAddressLogQuery ipAddressLogQuery = new IpAddressLogQuery();
                //访问时间
                ipAddressLogQuery.setCreateTime(DateUtil.formatTimeYmd(time));
                //渠道访问的URL
                ipAddressLogQuery.setUrl(Constant.PU_UV_URL);
                //渠道编码名称
                ipAddressLogQuery.setChannelCode(plateformChannel.getChannelCode());
                //统计pv、uv
                DailyStatisticsChannelProduct pvUvChannelProduct = ipAddressLogDAO.getPvUv(ipAddressLogQuery);
                if (null != pvUvChannelProduct) {
                    //设置渠道id
                    pvUvChannelProduct.setChannelId(plateformChannel.getId());
                    DailyStatisticsChannelProduct channelProductExist = isChannelProductExist(plateformChannel.getId(), time);
                    if (null != channelProductExist) {
                        try {
                            DailyStatisticsChannelProduct newChannelProductExist = new DailyStatisticsChannelProduct();
                            newChannelProductExist.setId(channelProductExist.getId());
                            //uv数
                            newChannelProductExist.setUv(pvUvChannelProduct.getUv());
                            //pv数
                            newChannelProductExist.setPv(pvUvChannelProduct.getPv());
                            //更新时间
                            newChannelProductExist.setUpdateTime(time);
                            dailyStatisticsChannelProductDAO.updateByPrimaryKeySelective(newChannelProductExist);
                            log.info("ChannelSyncCountService.pvUvCount ---------------->更新uv统计成功 ");
                        } catch (Exception e) {
                            log.error("ChannelSyncCountService.pvUvCount ---------------->更新uv统计失败 err={},channelId={},time={}" + e, plateformChannel.getId(), DateUtil.formatTimeYmdHms(time));
                            e.printStackTrace();
                        }
                    } else {
                        DailyStatisticsChannelProduct newdailyStatisticsChannelProduct = new DailyStatisticsChannelProduct();
                        //pv
                        newdailyStatisticsChannelProduct.setPv(pvUvChannelProduct.getPv());
                        //设置uv
                        newdailyStatisticsChannelProduct.setUv(pvUvChannelProduct.getUv());
                        //创建时间
                        newdailyStatisticsChannelProduct.setCreateTime(time);
                        //统计时间
                        newdailyStatisticsChannelProduct.setReportTime(time);
                        //设置渠道的基本信息(扣量比例、渠道id、渠道运营人员id)
                        setChannelInfo(pvUvChannelProduct.getChannelId(), newdailyStatisticsChannelProduct);
                        //插入（daily_statistics_channel_product）
                        dailyStatisticsChannelProductDAO.insertSelective(newdailyStatisticsChannelProduct);
                        log.info("ChannelSyncCountService.pvUvCount 进量统计：pv、uv数统计------------------------>插入成功 ");
                    }
                }
            }
        } catch (Exception e) {
            log.error("ChannelSyncCountService.pvUvCount 进量统计：pv、uv数统计------------------------>插入成功 err={},channelId={},time={}" + e, plateformChannel.getId(), DateUtil.formatTimeYmdHms(time));
            e.printStackTrace();
        } finally {
            if (getLock) {
                RedissLockUtil.unlock(PREFIX + "pvUvAsyncCount");
            }
        }
    }

    /**
     * 补充渠道放款人数数据
     */
    @Override
    public void channelData(Integer day) {
        log.info("补渠道放款统计数据-----day={}", day);
        List<PlateformChannel> params = plateformChannelMapper.findParams(null);
        for (PlateformChannel plateformChannel : params) {
            try {
                Date date1 = new Date();
                UserInfo userInfo = new UserInfo();
                userInfo.setChannelId(plateformChannel.getId());
                Date date = DateUtil.dateSubtraction(date1, day);
                userInfo.setCreateTime(date);
                log.info("注册时间====createTime={},channelId={}", userInfo.getCreateTime(), userInfo.getChannelId());
                Map<String, Object> param = new HashMap<>();
                param.put("channelId", userInfo.getChannelId());
                param.put("nowTime", userInfo.getCreateTime());
                //统计当前渠道的放款量
                int loanCount = orderRepaymentMapper.findLoanSuccCount(param);
                if (0 != loanCount) {
                    /**渠道进量统计：放款人数统计*/
                    channelProductLoanCount(userInfo, date1, loanCount);
                    /**渠道统计：放款成功人数统计*/
                    Date date2 = DateUtil.dateSubtraction(date1, -2);
                    channelReportLoan(userInfo, date2, loanCount);
                    /**渠道人员放款人数统计*/
                    channelStaffLoanCount(userInfo.getChannelId(), date1, loanCount);
                }
            } catch (Exception e) {
                log.error("补渠道放款统计数据---异常--day={}", day);
                e.printStackTrace();
                continue;
            }
        }
    }

    @Async
    @Override
    public void hitSystemBlack(UserInfo userInfo, Date nowTime) {
        PlateformChannelReport channelReport = findPlateformChannelReport(userInfo.getChannelId(), userInfo.getCreateTime());
        if (null != channelReport) {
            Map<String, Object> map = new HashMap<>();
            map.put("channelId", userInfo.getChannelId());
            map.put("createTime", userInfo.getCreateTime());
            map.put("systemBlack", "黑名单");
            map.put("customerType", 0);
            Integer count = plateformChannelReportDAO.countBlackSysout(map);
            channelReport.setHitSystemBlack(count);
            PlateformChannel plateformChannel = plateformChannelMapper.selectByPrimaryKey(userInfo.getChannelId());
            if (null != plateformChannel) {
                if (Constant.CHANNEL_INSERTING_COIL != plateformChannel.getStatus()) {
                    /**如果统计当天时间，不在用户注册当天，就不用把渠道方的人数记录在过去注册那天的数据上了（我方数据不变）: 如果两个时间相差为0，就说明是当天*/
                    int day = DateUtil.daysBetween(nowTime, nowTime);
                    if (0 == day) {
                        channelReport.setChannelSystemBlack(count);
                    }
                }
            }
            plateformChannelReportDAO.updateByPrimaryKeySelective(channelReport);
        }
    }

    @Async
    @Override
    public void hitOutBlack(UserInfo userInfo, Date nowTime) {
        PlateformChannelReport channelReport = findPlateformChannelReport(userInfo.getChannelId(), userInfo.getCreateTime());
        if (null != channelReport) {
            Map<String, Object> map = new HashMap<>();
            map.put("channelId", userInfo.getChannelId());
            map.put("createTime", userInfo.getCreateTime());
            map.put("outBlack", "黑名单");
            map.put("customerType", 0);
            Integer count = plateformChannelReportDAO.countBlackSysout(map);
            channelReport.setHitOutBlack(count);
            PlateformChannel plateformChannel = plateformChannelMapper.selectByPrimaryKey(userInfo.getChannelId());
            if (null != plateformChannel) {
                if (Constant.CHANNEL_INSERTING_COIL != plateformChannel.getStatus()) {
                    /**如果当天时间，不在用户注册当天，就不用把渠道方的人数记录在过去注册那天的数据上了（我方数据不变）: 如果两个时间相差为0，就说明是当天*/
                    int day = DateUtil.daysBetween(nowTime, nowTime);
                    if (0 == day) {
                        channelReport.setChannelOutBlack(count);
                    }
                }
            }
            plateformChannelReportDAO.updateByPrimaryKeySelective(channelReport);
        }
    }

    @Override
    public void hitSystemBlackRe(UserInfo userInfo, Date nowTime) {
        log.info("命中系统黑名单统计------channelId={},createTime={}", userInfo.getChannelId(), DateUtil.formatTimeYmdHms(userInfo.getCreateTime()));
        PlateformChannelReport channelReport = findPlateformChannelReport(userInfo.getChannelId(), userInfo.getCreateTime());
        if (null != channelReport) {
            Map<String, Object> map = new HashMap<>();
            map.put("channelId", userInfo.getChannelId());
            map.put("createTime", userInfo.getCreateTime());
            map.put("systemBlack", "黑名单");
            map.put("customerType", 0);
            Integer count = plateformChannelReportDAO.countBlackSysout(map);
            channelReport.setHitSystemBlack(count);
            PlateformChannel plateformChannel = plateformChannelMapper.selectByPrimaryKey(userInfo.getChannelId());
            if (null != plateformChannel) {
                if (Constant.CHANNEL_INSERTING_COIL != plateformChannel.getStatus()) {
                    channelReport.setChannelSystemBlack(count);
                }
            }
            plateformChannelReportDAO.updateByPrimaryKeySelective(channelReport);
        }
    }

    @Override
    public void hitOutBlackRe(UserInfo userInfo, Date nowTime) {
        log.info("命中【外部】黑名单统计------channelId={},createTime={}", userInfo.getChannelId(), DateUtil.formatTimeYmdHms(userInfo.getCreateTime()));
        PlateformChannelReport channelReport = findPlateformChannelReport(userInfo.getChannelId(), userInfo.getCreateTime());
        if (null != channelReport) {
            Map<String, Object> map = new HashMap<>();
            map.put("channelId", userInfo.getChannelId());
            map.put("createTime", userInfo.getCreateTime());
            map.put("outBlack", "黑名单");
            map.put("customerType", 0);
            Integer count = plateformChannelReportDAO.countBlackSysout(map);
            channelReport.setHitOutBlack(count);
            PlateformChannel plateformChannel = plateformChannelMapper.selectByPrimaryKey(userInfo.getChannelId());
            if (null != plateformChannel) {
                if (Constant.CHANNEL_INSERTING_COIL != plateformChannel.getStatus()) {
                    channelReport.setChannelOutBlack(count);
                }
            }
            plateformChannelReportDAO.updateByPrimaryKeySelective(channelReport);
        }
    }

    @Async
    @Override
    public void pushInformation(UserInfo userInfo, OrderBorrow orderBorrow) {
        // id phone idcard 姓名 update_time create_time pid   订单号 订单状态 放款时间 周期 借款金额
        Map<String, Object> params = new HashMap<>();
        params.put("phone", userInfo.getPhone());
        params.put("idCard", userInfo.getIdCard());
        params.put("realName", userInfo.getRealName());
        params.put("userId", userInfo.getId());
        params.put("pid", pid);
        params.put("borrowId", orderBorrow.getId());
        params.put("status", 0);
        params.put("loanTime", DateUtil.getDateFormat(orderBorrow.getLoanTime(), "yyyy-MM-dd HH:mm:ss"));
        params.put("loanEndTime", DateUtil.getDateFormat(orderBorrow.getLoanEndTime(), "yyyy-MM-dd HH:mm:ss"));
        params.put("loanTerm", orderBorrow.getLoanTerm());
        params.put("applyAmount", orderBorrow.getApplyAmount());
        // 将放款成功的用户订单信息推到数据中心
        //String res = HttpUtil.doPost(dataUrl + "borrowInfo/addBorrowInfo", params);
        log.info("放款成功时将订单信息同步到数据中心返回参数------res={}", "res");
    }

}
