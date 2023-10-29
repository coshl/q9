package com.summer.dao.entity;

import com.summer.pojo.dto.BaseUserDecrypt;
import com.summer.util.Constant;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class UserInfo extends BaseUserDecrypt implements Serializable {

    private static final long serialVersionUID = 268947397845093024L;

    private Integer id;

    private String password;

    private String payPassword;

    private String authenticStatusName;

    private Byte realAuthentic;

    private Byte authenticStatus;

    private Byte bankAuthentic;

    private Byte emergencyAuthentic;

    private Byte mobileAuthentic;

    private Date realAuthenticTime;

    private Byte realCount;

    private Date lastRealTime;

   // private String idCard;

    private Byte sex;

    private Integer age;

    private Byte education;

    private Byte maritalStatus;

    private String presentAddress;

    private String presentAddressDetail;

    private String companyName;

    private String companyPhone;

    private String companyAddress;

    private String companyAddressDetail;

    private Byte firstContactRelation;

    private Byte secondContactRelation;

    private Date createTime;

    private String createTimeChg;

    private String applyTimeChg;

    private String createIp;

    private Date updateTime;

    private Byte status;

    private String blackReason;

    /**
     * APP下载次数
     */
    private Integer appDownloadTimes;
    /**
     * email地址
     */
    private String emailAddress;
    /**
     * 手机基础信息
     */
    private String phoneBaseInfo;
    
    /*
     * 用户设备型号
     */
    private String deviceModel;
    
    /**
     * 用户系统版本
     */
    private String systemVersion;
    
    /**
     * App版本号
     */
    private String appVersion;

    public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	public String getDeviceModel() {
		return deviceModel;
	}

	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}

	public String getSystemVersion() {
		return systemVersion;
	}

	public void setSystemVersion(String systemVersion) {
		this.systemVersion = systemVersion;
	}

	public String getAuthenticStatusName() {
        return authenticStatusName;
    }

    public String getApplyTimeChg() {
        return applyTimeChg;
    }

    public void setApplyTimeChg(String applyTimeChg) {
        this.applyTimeChg = applyTimeChg;
    }

    public String getBlackReason() {
        return blackReason;
    }

    public void setBlackReason(String blackReason) {
        this.blackReason = blackReason;
    }

    private Integer inviteUserId;

    private Byte customerType;

    private Integer amountMin;

    private Integer amountMax;

    private Integer applyAmount;

    private Integer amountAvailable;

    private Integer amountAddSum;

    private Date addAmountTime;

    private String equipmentNumber;

    private Short zmScore;

    private Date zmLastTime;

    private Byte zmIndustyBlack;

    private Byte zmOverNum;

    private Byte zmUnpayOverNum;

    private Date zmIndustyLastTime;

    private Byte zmStatus;

    private Integer myHb;

    private Date myHbTime;

    private Integer channelId;

    private Integer reloanCount;

    private String province;

    private String channelName;

    private String presentAddressDistinct;

    private String presentLatitude;

    private String presentLongitude;

    private List<OrderBorrow> orderBorrowList;

    private String loginPwdSalt; //登录密码的盐

    private String payPwdSalt;   //支付密码的盐
    //客户端类型：0表示未知，1表示安卓，2表示IOS
    private Integer clientType;

    private Integer loanTerm;
    //身份证有效期
    private String idCardPeriod;
    //智趣OCR人脸识别订单号
    private String ocrOrder;
    /**
     * 魔杖认证状态
     */
    private Integer mzStatus;
    /**
     * 历史逾期总记录数，逾期并还款也纳为逾期记录
     */
    private Integer historyOverNum;
    /**
     * 最近一次逾期总天数，逾期并还款也算
     */
    private Integer lastOverDays;
    /**
     * 有的花订单号
     */
    private String ydhOrderNo;

    public String getYdhOrderNo() {
        return ydhOrderNo;
    }

    public void setYdhOrderNo(String ydhOrderNo) {
        this.ydhOrderNo = ydhOrderNo;
    }

    public String getCreateTimeChg() {
        return createTimeChg;
    }

    public void setCreateTimeChg(String createTimeChg) {
        this.createTimeChg = createTimeChg;
    }

    public Integer getLoanTerm() {
        return loanTerm;
    }

    public void setLoanTerm(Integer loanTerm) {
        this.loanTerm = loanTerm;
    }

    public Integer getApplyAmount() {
        return applyAmount;
    }

    public void setApplyAmount(Integer applyAmount) {
        this.applyAmount = applyAmount;
    }

    public Byte getZmIndustyBlack() {
        return zmIndustyBlack;
    }

    public void setZmIndustyBlack(Byte zmIndustyBlack) {
        this.zmIndustyBlack = zmIndustyBlack;
    }

    public Integer getReloanCount() {
        return reloanCount;
    }

    public void setReloanCount(Integer reloanCount) {
        this.reloanCount = reloanCount;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public List<OrderBorrow> getOrderBorrowList() {
        return orderBorrowList;
    }

    public void setOrderBorrowList(List<OrderBorrow> orderBorrowList) {
        this.orderBorrowList = orderBorrowList;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getPayPassword() {
        return payPassword;
    }

    public void setPayPassword(String payPassword) {
        this.payPassword = payPassword == null ? null : payPassword.trim();
    }

    public Byte getRealAuthentic() {
        return realAuthentic;
    }

    public void setRealAuthentic(Byte realAuthentic) {
        this.realAuthentic = realAuthentic;
    }

    public Byte getAuthenticStatus() {
        return authenticStatus;
    }

    public void setAuthenticStatus(Byte authenticStatus) {
        this.authenticStatus = authenticStatus;
        this.authenticStatusName = Constant.authenticNameMap.get(authenticStatus);
    }

    public Byte getBankAuthentic() {
        return bankAuthentic;
    }

    public void setBankAuthentic(Byte bankAuthentic) {
        this.bankAuthentic = bankAuthentic;
    }

    public Byte getEmergencyAuthentic() {
        return emergencyAuthentic;
    }

    public void setEmergencyAuthentic(Byte emergencyAuthentic) {
        this.emergencyAuthentic = emergencyAuthentic;
    }

    public Byte getMobileAuthentic() {
        return mobileAuthentic;
    }

    public void setMobileAuthentic(Byte mobileAuthentic) {
        this.mobileAuthentic = mobileAuthentic;
    }

    public Date getRealAuthenticTime() {
        return realAuthenticTime;
    }

    public void setRealAuthenticTime(Date realAuthenticTime) {
        this.realAuthenticTime = realAuthenticTime;
    }

    public Byte getRealCount() {
        return realCount;
    }

    public void setRealCount(Byte realCount) {
        this.realCount = realCount;
    }

    public Date getLastRealTime() {
        return lastRealTime;
    }

    public void setLastRealTime(Date lastRealTime) {
        this.lastRealTime = lastRealTime;
    }

   /* public String getIdCard() {
        return idCard;
    }*/

   /* public void setIdCard(String idCard) {
        this.idCard = idCard == null ? null : idCard.trim();
    }*/

    public Byte getSex() {
        return sex;
    }

    public void setSex(Byte sex) {
        this.sex = sex;
    }


    public Byte getEducation() {
        return education;
    }

    public void setEducation(Byte education) {
        this.education = education;
    }

    public Byte getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(Byte maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getPresentAddress() {
        return presentAddress;
    }

    public void setPresentAddress(String presentAddress) {
        this.presentAddress = presentAddress == null ? null : presentAddress.trim();
    }

    public String getPresentAddressDetail() {
        return presentAddressDetail;
    }

    public void setPresentAddressDetail(String presentAddressDetail) {
        this.presentAddressDetail = presentAddressDetail == null ? null :
                presentAddressDetail.trim();
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName == null ? null : companyName.trim();
    }

    public String getCompanyPhone() {
        return companyPhone;
    }

    public void setCompanyPhone(String companyPhone) {
        this.companyPhone = companyPhone == null ? null : companyPhone.trim();
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress == null ? null : companyAddress.trim();
    }

    public String getCompanyAddressDetail() {
        return companyAddressDetail;
    }

    public void setCompanyAddressDetail(String companyAddressDetail) {
        this.companyAddressDetail = companyAddressDetail == null ? null : companyAddressDetail.trim();
    }



    public Byte getFirstContactRelation() {
        return firstContactRelation;
    }

    public void setFirstContactRelation(Byte firstContactRelation) {
        this.firstContactRelation = firstContactRelation;
    }



    public Byte getSecondContactRelation() {
        return secondContactRelation;
    }

    public void setSecondContactRelation(Byte secondContactRelation) {
        this.secondContactRelation = secondContactRelation;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateIp() {
        return createIp;
    }

    public void setCreateIp(String createIp) {
        this.createIp = createIp == null ? null : createIp.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Integer getInviteUserId() {
        return inviteUserId;
    }

    public void setInviteUserId(Integer inviteUserId) {
        this.inviteUserId = inviteUserId;
    }



    public Byte getCustomerType() {
        return customerType;
    }

    public void setCustomerType(Byte customerType) {
        this.customerType = customerType;
    }

    public Integer getAmountMin() {
        return amountMin;
    }

    public void setAmountMin(Integer amountMin) {
        this.amountMin = amountMin;
    }

    public Integer getAmountMax() {
        return amountMax;
    }

    public void setAmountMax(Integer amountMax) {
        this.amountMax = amountMax;
    }

    public Integer getAmountAvailable() {
        return amountAvailable;
    }

    public void setAmountAvailable(Integer amountAvailable) {
        this.amountAvailable = amountAvailable;
    }

    public Integer getAmountAddSum() {
        return amountAddSum;
    }

    public void setAmountAddSum(Integer amountAddSum) {
        this.amountAddSum = amountAddSum;
    }

    public Date getAddAmountTime() {
        return addAmountTime;
    }

    public void setAddAmountTime(Date addAmountTime) {
        this.addAmountTime = addAmountTime;
    }

    public String getEquipmentNumber() {
        return equipmentNumber;
    }

    public void setEquipmentNumber(String equipmentNumber) {
        this.equipmentNumber = equipmentNumber == null ? null : equipmentNumber.trim();
    }

    public Short getZmScore() {
        return zmScore;
    }

    public void setZmScore(Short zmScore) {
        this.zmScore = zmScore;
    }

    public Date getZmLastTime() {
        return zmLastTime;
    }

    public void setZmLastTime(Date zmLastTime) {
        this.zmLastTime = zmLastTime;
    }


    public Byte getZmOverNum() {
        return zmOverNum;
    }

    public void setZmOverNum(Byte zmOverNum) {
        this.zmOverNum = zmOverNum;
    }

    public Byte getZmUnpayOverNum() {
        return zmUnpayOverNum;
    }

    public void setZmUnpayOverNum(Byte zmUnpayOverNum) {
        this.zmUnpayOverNum = zmUnpayOverNum;
    }

    public Date getZmIndustyLastTime() {
        return zmIndustyLastTime;
    }

    public void setZmIndustyLastTime(Date zmIndustyLastTime) {
        this.zmIndustyLastTime = zmIndustyLastTime;
    }

    public Byte getZmStatus() {
        return zmStatus;
    }

    public void setZmStatus(Byte zmStatus) {
        this.zmStatus = zmStatus;
    }

    public Integer getMyHb() {
        return myHb;
    }

    public void setMyHb(Integer myHb) {
        this.myHb = myHb;
    }

    public Date getMyHbTime() {
        return myHbTime;
    }

    public void setMyHbTime(Date myHbTime) {
        this.myHbTime = myHbTime;
    }

    public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province == null ? null : province.trim();
    }

    public String getLoginPwdSalt() {
        return loginPwdSalt;
    }

    public void setLoginPwdSalt(String loginPwdSalt) {
        this.loginPwdSalt = loginPwdSalt;
    }

    public String getPayPwdSalt() {
        return payPwdSalt;
    }

    public void setPayPwdSalt(String payPwdSalt) {
        this.payPwdSalt = payPwdSalt;
    }

    public String getPresentAddressDistinct() {
        return presentAddressDistinct;
    }

    public void setPresentAddressDistinct(String presentAddressDistinct) {
        this.presentAddressDistinct = presentAddressDistinct;
    }

    public String getPresentLatitude() {
        return presentLatitude;
    }

    public void setPresentLatitude(String presentLatitude) {
        this.presentLatitude = presentLatitude;
    }

    public String getPresentLongitude() {
        return presentLongitude;
    }

    public void setPresentLongitude(String presentLongitude) {
        this.presentLongitude = presentLongitude;
    }

    public Integer getClientType() {
        return clientType;
    }

    public void setClientType(Integer clientType) {
        this.clientType = clientType;
    }



    public String getIdCardPeriod() {
        return idCardPeriod;
    }

    public void setIdCardPeriod(String idCardPeriod) {
        this.idCardPeriod = idCardPeriod;
    }

    public String getOcrOrder() {
        return ocrOrder;
    }

    public void setOcrOrder(String ocrOrder) {
        this.ocrOrder = ocrOrder;
    }

    public Integer getHistoryOverNum() {
        return historyOverNum;
    }

    public void setHistoryOverNum(Integer historyOverNum) {
        this.historyOverNum = historyOverNum;
    }

    public Integer getLastOverDays() {
        return lastOverDays;
    }

    public void setLastOverDays(Integer lastOverDays) {
        this.lastOverDays = lastOverDays;
    }

    public Integer getAppDownloadTimes() {
        return appDownloadTimes;
    }

    public void setAppDownloadTimes(Integer appDownloadTimes) {
        this.appDownloadTimes = appDownloadTimes;
    }


    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPhoneBaseInfo() {
        return phoneBaseInfo;
    }

    public void setPhoneBaseInfo(String phoneBaseInfo) {
        this.phoneBaseInfo = phoneBaseInfo;
    }

    public Integer getMzStatus() {
        return mzStatus;
    }

    public void setMzStatus(Integer mzStatus) {
        this.mzStatus = mzStatus;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}