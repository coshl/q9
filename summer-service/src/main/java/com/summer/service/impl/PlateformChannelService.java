package com.summer.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.common.util.Md5Utils;
import com.github.pagehelper.PageInfo;
import com.summer.dao.entity.LoanRuleConfig;
import com.summer.dao.entity.PlateformChannel;
import com.summer.dao.entity.PlatformUser;
import com.summer.dao.mapper.LoanRuleConfigDAO;
import com.summer.dao.mapper.PlateformChannelMapper;
import com.summer.dao.mapper.PlatformChannelStaffDAO;
import com.summer.dao.mapper.PlatformUserMapper;
import com.summer.pojo.dto.PlateformChannelDTO;
import com.summer.pojo.dto.PlateformChannelParamDto;
import com.summer.api.service.IPlateformChannelService;
import com.summer.api.service.channel.IChannelAsyncCountService;
import com.summer.util.*;
import com.summer.pojo.vo.ChannelLinkVO;
import com.summer.pojo.vo.ChannelStaffVo;
import com.summer.pojo.vo.PlateformChannelListVo;
import com.summer.util.log.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
 * 渠道service
 */
@Slf4j
@Service
public class PlateformChannelService implements IPlateformChannelService {

    @Value("${channelLink}")
    private String channelLink;
    @Value("${channelCode}")
    private String code;
    @Value("${backStage}")
    private String backStage;
    @Value("${suowo.apiUrl}")
    private String suowoUrl;
    @Value("${suowo.key}")
    private String suowoKey;

    @Resource
    private PlateformChannelMapper plateformChannelMapper;
    @Resource
    private PlatformUserMapper platformUserMapper;
    @Resource
    private PlatformChannelStaffDAO platformChannelStaffDAO;
    @Resource
    private IChannelAsyncCountService channelAsyncCountService;
    @Resource
    private LoanRuleConfigDAO loanRuleConfigDAO;

    /**
     * 渠道列表（暂时未使用）
     *
     * @param params
     * @return
     */
    @Override
    public PageInfo<PlateformChannel> query(Map<String, Object> params) {
        List<PlateformChannel> result = plateformChannelMapper.findParams(params);
        return new PageInfo<>(result);
    }

    /**
     * 修改渠道状态
     *
     * @param plateformChannel
     */
    @Override
    public int updateByStatus(PlateformChannel plateformChannel) {
        return plateformChannelMapper.updateByStatus(plateformChannel.getId(), plateformChannel.getStatus());
    }

    /**
     * 添加渠道
     *
     * @param plateformChannelDTO
     */
    @Transactional
    @Override
    public int insertChannel(PlateformChannelDTO plateformChannelDTO, HttpServletRequest request) {

        String randomSalt = RandomUtil.getRandomSalt(6);
        //乙方随机生成的密码
        Date date = new Date();
        String password = GenerateNo.generatePassword(6).get(0);
        //账户名：就合作公司的手机号
        String account = plateformChannelDTO.getManagerPhone();
        PlateformChannelParamDto plateformChannel = new PlateformChannelParamDto();

        plateformChannel.setStripSwitch(plateformChannelDTO.getStripSwitch());
        plateformChannel.setAuditSwitch(plateformChannelDTO.getAuditSwitch());
        //合作方手机号码
        plateformChannel.setManagerPhone(plateformChannelDTO.getManagerPhone());
        //渠道名
        plateformChannel.setChannelName(plateformChannelDTO.getChannelName());
        //合作联系人
        plateformChannel.setManagerName(plateformChannelDTO.getManagerName());
        //扣量比例
        plateformChannel.setDecreasePercentage(plateformChannelDTO.getDecreasePercentage());
        //结算方式
        plateformChannel.setPaymentType(plateformChannelDTO.getPaymentType());
        //结算模式
        plateformChannel.setPaymentMode(plateformChannelDTO.getPaymentMode());
        //结算模式
        plateformChannel.setCooperationMode(plateformChannelDTO.getCooperationMode());
        //公司名称
        plateformChannel.setCompanyName(plateformChannelDTO.getCompanyName());
        //备注
        plateformChannel.setRemark(plateformChannelDTO.getRemark());
        //乙方账户
        plateformChannel.setAccount(plateformChannelDTO.getManagerPhone());
        //乙方密码
        // TODO 此处查验一下登录逻辑，把加密规则改下
        plateformChannel.setPassword(password);
        //默认上线
        plateformChannel.setStatus(Constant.CHANNEL_ONLINE);
        //后台链接
        plateformChannel.setBackStage(backStage);

        //风控类型
        plateformChannel.setRiskType(plateformChannelDTO.getRiskType());
        //风控分数
        plateformChannel.setRiskScore(plateformChannelDTO.getRiskScore());
        //操作人ID
        plateformChannel.setPlateformUserId(plateformChannelDTO.getPlatefromUserId());
        //价格 * 100 按分存
        if (null != plateformChannelDTO.getPrice()) {
            BigDecimal price = new BigDecimal((plateformChannelDTO.getPrice() * Constant.CENT_CHANGE_DOLLAR));
            plateformChannel.setPriceInt(price.intValue());
        }

        /**插入（渠道信息表plateform_channel表）*/
        int isSuccState = plateformChannelMapper.insertSelective(plateformChannel);
        if (isSuccState > 0) {
            //渠道编码
            String channelCode = GenerateNo.generateChannelCode(5, plateformChannel.getId());
            plateformChannel.setChannelCode(channelCode);
            //渠道链接
            plateformChannel.setLink(channelLink + "?channel_code=" + plateformChannel.getChannelCode()+"&code="+code);
            isSuccState = plateformChannelMapper.updateByPrimaryKeySelective(plateformChannel);
            PlateformChannel plateformChannel2 = selectByChannelName(plateformChannel.getChannelName());
            if (null != plateformChannel2) {
                PlatformUser platformUser = new PlatformUser();
                //账户
                platformUser.setPhoneNumber(account);
                //随机盐

                //密码：MD5（密码+盐）
                platformUser.setPassword(MD5Util.md5(password + randomSalt));
                //设置盐
                platformUser.setSalt(randomSalt);
                //渠道id
                platformUser.setChannelId(plateformChannel2.getId());
                //角色id
                platformUser.setRoleId(Constant.CHANNEL_USER_ROLE_ID);
                //添加人id
                platformUser.setUserSuperId(plateformChannel2.getPlateformUserId());
                //添加时间
                platformUser.setCreateTime(date);
                //ip地址
                platformUser.setIpAddress(IPUtils.getIpAddr(request));
                //
                platformUser.setUserName(plateformChannel.getManagerName());
                isSuccState = platformUserMapper.insertSelective(platformUser);
                /**渠道个性化贷款规则*/
                saveChannelRule(plateformChannelDTO, plateformChannel2.getId());
            }
            if (isSuccState > 0) {
                /**异步统计投放连接数*/
                channelAsyncCountService.deliveryConnectionCount(date, plateformChannel.getPlateformUserId(), plateformChannel2.getId());
                return isSuccState;
            } else {
                return 0;
            }
        }
        return 0;
    }

    /**
     * 渠道个性借款期限等
     *
     * @param plateformChannelDTO
     * @param channelId
     * @return
     */
    @Override
    public int saveChannelRule(PlateformChannelDTO plateformChannelDTO, Integer channelId) {
        int status = 0;
        //v1.0.3个性化渠道 【借款期限】、【服务费率】、【利息】、【续期期限】
        LoanRuleConfig sysRuleConfig = loanRuleConfigDAO.findByChannelId(0);
        if (null != sysRuleConfig) {
            //把id设为空，当成一个新的贷款规则插入
            sysRuleConfig.setId(null);

            if (null != plateformChannelDTO.getLoanAmount()) {
                sysRuleConfig.setLoanAmount(plateformChannelDTO.getLoanAmount() * Constant.DOLLAR_CHANGE_PENNY);
            }
            //替换原来的期限
            sysRuleConfig.setExpire(plateformChannelDTO.getExpire());
            //替换借款服务费
            if (null != plateformChannelDTO.getServiceCharge()) {
                sysRuleConfig.setServiceCharge(DataTransform.changeSmallDouble(plateformChannelDTO.getServiceCharge()));
            }
            if (null != plateformChannelDTO.getBorrowInterest()) {
                //替换借款利息
                sysRuleConfig.setBorrowInterest(DataTransform.changeSmallDouble(plateformChannelDTO.getBorrowInterest()));
            }

            //替换续期期限
            sysRuleConfig.setRenewalExpire(plateformChannelDTO.getRenewalExpire());
            //渠道id
            sysRuleConfig.setChannelId(channelId);
            //创建时间
            sysRuleConfig.setCreateTime(new Date());
            //更新时间为null
            sysRuleConfig.setUpdateTime(null);
            status = loanRuleConfigDAO.insertSelective(sysRuleConfig);
        }
        return status;
    }

    @Override
    public PageInfo<ChannelStaffVo> findParams(Map<String, Object> params) {
        List<ChannelStaffVo> channelStaffList = platformChannelStaffDAO.findParams(params);
        return new PageInfo<>(channelStaffList);
    }

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return platformChannelStaffDAO.deleteByPrimaryKey(id);
    }

    @Override
    public int insertSelective(PlateformChannelParamDto record) {
        return plateformChannelMapper.insertSelective(record);
    }

    @Override
    public PlateformChannel selectByPrimaryKey(Integer id) {
        return plateformChannelMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(PlateformChannelParamDto channelStatusDto) {
       /* String account = channelStatusDto.getAccount();
        if (StringUtils.isNotBlank(account)){
            String encrypt = AESDecrypt.encrypt(account);
            channelStatusDto.setAccount(encrypt);
            channelStatusDto.setManagerPhone(encrypt);
        }*/
        return plateformChannelMapper.updateByPrimaryKeySelective(channelStatusDto);
    }

    @Override
    public int updateByPrimaryKey(PlateformChannel record) {
        return plateformChannelMapper.updateByPrimaryKey(record);
    }

    @Override
    public int updateByStatus(Integer id, Integer status) {
        return plateformChannelMapper.updateByStatus(id, status);
    }

    @Override
    public void insert(PlateformChannel plateformChannel) {
        plateformChannelMapper.insert(plateformChannel);
    }

    @Override
    public List<PlateformChannel> selectSimple(Map<String, Object> params) {
        return plateformChannelMapper.selectSimple(params);
    }

    @Override
    public PlateformChannel selectByChannelName(String channelName) {
        return plateformChannelMapper.selectByChannelName(channelName);
    }

    @Override
    public PlateformChannel selectByAccount(String account) {
        return plateformChannelMapper.selectByAccount(account);
    }

    @Override
    public ChannelLinkVO findByChannelLink(Integer id) {
        return plateformChannelMapper.findByChannelLink(id);
    }

    @Override
    public String getShortChannelLink(Integer id) {
        ChannelLinkVO channelLinkVO = plateformChannelMapper.findByChannelLink(id);
       if(StringUtils.isNotEmpty(channelLinkVO.getShortUrl())){
            return CallBackResult.returnJson(CallBackResult.ABRUPT_ABNORMALITY,"请勿重复生成", "");
        }
        String sign = Md5Utils.getMD5("暂时预留".getBytes()) + "&" + Calendar.getInstance().getTimeInMillis() / 1000 + "&" + UUID.randomUUID();
        //三维推api接口url地址

        /*Map<String, String> paraMap = new HashMap<String, String>();
        //要缩短的长网址，需要进行urlencode操作
        paraMap.put("url", channelLinkVO.getDropLink() + "&sign=" +  Base64Util.encode(sign));
        paraMap.put("format", "json");
        //用户自己的key
        paraMap.put("key", suowoKey);
        //过期时间，若expireDate为空，默认有效期3个月。若expireDate>=2040-01-01,则永久有效
        paraMap.put("expireDate", "2040-01-01");
        //可选择域名。“0”代表b6i.cn；“1”代表nxw.so。若为空，默认为b6i.cn

        paraMap.put("domain", String.valueOf(i));
        String paraMapJson = JSONObject.toJSONString(paraMap);*/
        int i = (int) (Math.random() * 4);
        String result = OkHttpUtils.builder().url(suowoUrl)
                // 有参数的话添加参数，可多个
                .addParam("url", channelLinkVO.getDropLink() + "&sign=" +  Base64Util.encode(sign))
                .addParam("format", "json")
                .addParam("key", suowoKey)
                .addParam("expireDate", "2040-01-01")
                .addParam("domain", String.valueOf(i))
                // 也可以添加多个
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .get()
                // 可选择是同步请求还是异步请求
                //.async();
                .sync();
        System.out.println(result);
        JSONObject reportResult = JSONObject.parseObject(result);
        //{"err": "","url": "http://mtw.so/5xw4Op"}
        if ("".equals(reportResult.getString("err"))) {
            ChannelLinkVO channelLinkVO1 = new ChannelLinkVO();
            channelLinkVO1.setShortUrl(reportResult.getString("url"));
            channelLinkVO1.setId(id);
            plateformChannelMapper.updateShortLink(channelLinkVO1);
            return CallBackResult.returnJson(CallBackResult.SUCCESS,"", reportResult.getString("url"));
        } else {
            return CallBackResult.returnJson(CallBackResult.ABRUPT_ABNORMALITY,reportResult.getString("err"), "");
        }
    }

    public static void main(String[] args) {
        int i = (int) (Math.random() * 4);
        String sign = Md5Utils.getMD5("暂时预留".getBytes()) + "&" + Calendar.getInstance().getTimeInMillis() / 1000 + "&" + UUID.randomUUID();
        String result = OkHttpUtils.builder().url("http://api.suolink.cn/api.htm")
                // 有参数的话添加参数，可多个
                .addParam("url",  "http://www.jixinbao.monster:6074/index.html?channel_code=KoGrw16&sign=" +  Base64Util.encode(sign))
                .addParam("format", "json")
                .addParam("key", "5f4e3b52b1b63c067174bec3@4206ee1c3ad87b1907913ae2699764aa")
                .addParam("expireDate", "2040-01-01")
                .addParam("domain","suolink.cn")
                // 也可以添加多个
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .get()
                // 可选择是同步请求还是异步请求
                //.async();
                .sync();
        System.out.println(result);
    }

    @Override
    public PageInfo<PlateformChannelListVo> findListParams(Map<String, Object> params) {
        List<PlateformChannelListVo> channelList = plateformChannelMapper.findListParams(params);
        return new PageInfo<>(channelList);
    }

    @Override
    public PlateformChannel findByChannelCode(String channelCode) {
        return plateformChannelMapper.findByChannelCode(channelCode);
    }

    @Override
    public int updateChannelRule(PlateformChannelParamDto plateformChannelDTO) {
        int status = 0;
        //v1.0.3个性化渠道 【借款期限】、【服务费率】、【利息】、【续期期限】
        LoanRuleConfig sysRuleConfig = loanRuleConfigDAO.findByChannelId(0);
        if (null != sysRuleConfig) {
            //把id设为空，当成一个新的贷款规则插入
            sysRuleConfig.setId(null);
            //替换原来的期限
            sysRuleConfig.setExpire(plateformChannelDTO.getExpire());
            //替换借款服务费
            if (null != plateformChannelDTO.getServiceCharge()) {
                sysRuleConfig.setServiceCharge(DataTransform.changeSmallDouble(plateformChannelDTO.getServiceCharge()));
            }
            if (null != plateformChannelDTO.getBorrowInterest()) {
                //替换借款利息
                sysRuleConfig.setBorrowInterest(DataTransform.changeSmallDouble(plateformChannelDTO.getBorrowInterest()));
            }

            //替换续期期限
            sysRuleConfig.setRenewalExpire(plateformChannelDTO.getRenewalExpire());
            //渠道id
            sysRuleConfig.setChannelId(plateformChannelDTO.getId());
            //创建时间
            sysRuleConfig.setCreateTime(new Date());
            //更新时间为null
            sysRuleConfig.setUpdateTime(null);
            status = loanRuleConfigDAO.insertSelective(sysRuleConfig);
        }
        return status;
    }

}
