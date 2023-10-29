package com.summer.dao.mapper;

import com.summer.dao.entity.RiskCreditUser;
import com.summer.dao.entity.RiskRuleProperty;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public interface IRiskCreditUserDao {

    public Integer findWaitToGetAnalyseRiskCreditUserCount();

    /**
     * 更新新风控模型的结果
     *
     * @param riskCreditUser
     * @return
     */
    public int updateNewRisk(RiskCreditUser riskCreditUser);

    /**
     * 申请额度与可放额度不符，更新原借款金额为当次判定的可借额度
     *
     * @param riskCreditUser
     * @return
     */
    public int updateBorrowMoney(RiskCreditUser riskCreditUser);

    /**
     * 查询某个待计算额度的用户，大于0是有待查记录
     *
     * @param params
     * @return
     */
    public int findEdHelp(RiskCreditUser riskCreditUser);

    /**
     * 查询某个借款待机审的记录数，大于0是有待查记录
     *
     * @param params
     * @return
     */
    public int findAnalysisHelp(String assetId);

    /**
     * 查询某个用户待查询聚信立报告的记录数，大于0是有待查记录
     *
     * @param params
     * @return
     */
    public int findJxlHelp(HashMap<String, Object> params);

    /**
     * 更新中智诚黑名单信息
     *
     * @param riskCreditUser
     * @return
     */
    public int updateZzc(RiskCreditUser riskCreditUser);

    /**
     * 根据用户主键或者身份证号码或者手机号码查询黑名单库，如果有则拒绝
     *
     * @param riskCreditUser
     * @return
     */
    public int findBlack(RiskCreditUser riskCreditUser);

    /**
     * 根据主键更新过程数据后缀
     *
     * @param riskCreditUser id主键<br>
     *                       riskCalSuf后缀<br>
     * @return
     */
    public int updateSuf(RiskCreditUser riskCreditUser);

    public int updateNewFlag(Integer userId);

    /**
     * 更新蚂蚁花呗信息
     *
     * @param riskCreditUser 包含userId、myHb两个信息
     * @return
     */
    public int updateUserMyHb(RiskCreditUser riskCreditUser);

    /**
     * 更新用户表的芝麻分
     *
     * @param riskCreditUser
     * @return
     */
    public int updateUserZmScore(RiskCreditUser riskCreditUser);

    /**
     * 更新用户表的关注度信息
     *
     * @param riskCreditUser
     * @return
     */
    public int updateUserZmIndusty(RiskCreditUser riskCreditUser);

    /**
     * 根据征信表主键更新征信表芝麻所有信息
     *
     * @param riskCreditUser
     * @return
     */
    public int updateZm(RiskCreditUser riskCreditUser);

    /**
     * 运行决策树是更新用户表芝麻的所有信息
     *
     * @param riskCreditUser
     * @return
     */
    public int updateUserZm(RiskCreditUser riskCreditUser);

    /**
     * 后台分析时，以用户表信息为准
     *
     * @param id
     * @return 包含个人基本信息、聚信立、芝麻、后台统计数据
     */
    public RiskCreditUser findUserDetail(Integer id);

    /**
     * 根据征信表ID更新同盾详情和更新状态为成功
     *
     * @param riskCreditUser
     * @return
     */
    public int updateTdDetail(RiskCreditUser riskCreditUser);

    /**
     * 更新白骑士相关信息及接口状态
     *
     * @param riskCreditUser
     * @return
     */
    public int updateBqs(RiskCreditUser riskCreditUser);

    /**
     * 更新91征信数据及接口状态
     *
     * @param riskCreditUser
     * @return
     */
    public int updateJy(RiskCreditUser riskCreditUser);

    /**
     * 更新密罐数据
     *
     * @param riskCreditUser
     * @return
     */
    public int updateMg(RiskCreditUser riskCreditUser);

    /**
     * 更新算话数据
     *
     * @param riskCreditUser
     * @return
     */
    public int updateSh(RiskCreditUser riskCreditUser);

    /**
     * 更新宜信逾期记录数和更新时间
     *
     * @param riskCreditUser
     * @return
     */
    public int updateYx(RiskCreditUser riskCreditUser);

    /**
     * 根据征信表主键更新征信表聚信立所有数据
     *
     * @param riskCreditUser
     * @return
     */
    public int updateJxlAndDays(RiskCreditUser riskCreditUser);

    /**
     * 更新用户表的聚信立详情
     *
     * @param riskCreditUser
     * @return
     */
    public int updateUserJxlDetail(RiskCreditUser riskCreditUser);

    /**
     * 更新用户表的聚信立token及状态为已认证
     *
     * @param riskCreditUser
     * @return
     */
    public int updateUserJxlToken(RiskCreditUser riskCreditUser);

    /**
     * 更新用户表的聚信立分析数据
     *
     * @param riskCreditUser
     * @return
     */
    public int updateUserJxl(RiskCreditUser riskCreditUser);

    public List<RiskRuleProperty> findRuleProperty(HashMap<String, Object> params);

    public Integer findJxlWaitReportCount(HashMap<String, Object> params);

    //获取不到报告信息处理
    public int findJxlEndReportInfo();

    public Integer findWaitAnalysisCount(HashMap<String, Object> params);

    public Integer findCalMoneyCount(HashMap<String, Object> params);

    /**
     * 获得某个用户最近一次被拒绝的时间
     *
     * @param Id
     * @return
     */
    public Date findLastFail(Integer Id);

    /**
     * 根据主键查询该待审记录的接口状态
     *
     * @param id
     * @return
     */
    public RiskCreditUser findInterfaceStatus(Integer id);

    /**
     * 根据主键更新所有接口状态
     *
     * @param riskCreditUser 包含芝麻、同盾报告ID，同盾状态、白骑士、91、密罐的接口状态
     * @return
     */
    public int updateStatus(RiskCreditUser riskCreditUser);

    /**
     * 更新借款申请为机审不通过
     *
     * @param params remark 机审备注<br>
     *               id借款申请主键ID<br>
     * @return
     */
    public int updateAssetsFail(HashMap<String, Object> params);

    public int updateLoanFail(HashMap<String, Object> params);

    /**
     * 更新征信待审核表的状态和备注
     *
     * @param riskCreditUser
     * @return
     */
    public int updateRiskStatus(RiskCreditUser riskCreditUser);

    /**
     * 根据主键查询征信详情
     *
     * @return
     */
    public RiskCreditUser findById(Integer id);

    /**
     * 根据用户主键查询用户表的聚信立详情
     *
     * @param id
     * @return
     */
    public String findDetailById(Integer id);

    /**
     * 查询当前地址出现的次数(包括被查用户的那一次)
     *
     * @param presentAddress 用户当前住址
     * @return
     */
    public Integer findRepeatZz(String presentAddress);

    /**
     * 查询公司地址出现的次数(包括被查用户的那一次)
     *
     * @param presentAddress 用户公司地址
     * @return
     */
    public Integer findRepeatGsdz(String companyAddress);

    /**
     * 查询公司名称出现的次数(包括被查用户的那一次)
     *
     * @param companyName 用户公司名称
     * @return
     */
    public Integer findRepeatGsmc(String companyName);

    /**
     * 查询使用同一个设备注册的用户数量，不能排除当前用户
     *
     * @param equipmentNumber 用户注册设备号
     * @return
     */
    public Integer findDifPhoneByEqNum(String equipmentNumber);

    /**
     * 1.查询公司电话相同但公司名称或者公司地址不同的记录数，减1是排除当前用户
     * 2.查询公司地址相同但公司名称或者公司电话不同的记录数，减1是排除当前用户
     * 3.查询公司名称相同但公司号码或者公司地址不同的记录数，减1是排除当前用户 <br>
     * 4.查询使用同一个用户使用不同设备登录的用户数量，不能排除当前用户 <br>
     * 5.查询某个用户的第一二联系人在平台的申请记录
     *
     * @param riskCreditUser
     * @return
     */
    public List<Integer> findAllCount(RiskCreditUser riskCreditUser);

    /**
     * 更新聚类信息
     *
     * @param riskCreditUser
     * @return
     */
    public int updateJl(RiskCreditUser riskCreditUser);

    /**
     * 查询出实名认证通过、聚信立认证通过并且返回报告信息的、芝麻认证通过的，状态是未计算的用户列表，用于计算用户最新额度
     *
     * @return
     */
    public List<RiskCreditUser> findCalMoney();

    /**
     * 根据用户主键把用户表的额度、剩余可用额度更新为机身额度、时间更新为当前时间，是否计算过借款额度更新为已更新
     *
     * @param userId
     * @return
     */
    public int updateUserMoney(RiskCreditUser riskCreditUser);

    /**
     * 插入一条征信记录，辅助用户额度的计算
     */
    public int insertCalMoney(RiskCreditUser riskCreditUser);

    /**
     * 更新征信表的机审额度
     *
     * @param riskCreditUser
     * @return
     */
    public int updateMoney(RiskCreditUser riskCreditUser);

    /**
     * 主键ID
     *
     * @param id
     * @return
     */
    public RiskCreditUser findBorrowMoney(Integer id);

    /**
     * 更新用户表的可用金额
     *
     * @param riskCreditUser 用户主键、可用金额（在sql中乘以100）
     * @return
     */
    public int updateUserAvailable(RiskCreditUser riskCreditUser);


    /**
     * 根据用户ID查询出现黑名单、逾期情况的记录数
     *
     * @param userId
     * @return
     */
    public List<RiskCreditUser> findByUserId(Integer userId);

    /**
     * 查询某个用户是否有计算额度的记录
     *
     * @param riskCreditUser userId和assetId（固定传入0）
     * @return
     */
    public Integer findOneCal(RiskCreditUser riskCreditUser);

    public Integer findOneCal2(RiskCreditUser riskCreditUser);

    /**
     * 查询用户最新一条审核的某个接口信息
     *
     * @param params sql动态的sql<br>
     *               userId是用户主键
     * @return
     */
    public RiskCreditUser findLastInterface(HashMap<String, Object> params);

    /**
     * 根据手机号查询是否是灰名单(他库逾期用户)
     */
    public Integer findGray(RiskCreditUser riskCreditUser);

    public int findCalH5MoneyCount(HashMap<String, Object> params2);

    public void updateUserNewflag(HashMap<String, Object> params2);

    public List<RiskCreditUser> findRiskCreditUser(HashMap<String, Object> params);

    public List<RiskCreditUser> findRiskby14(HashMap<String, Object> params);

    public int cleanUserLimit(HashMap<String, Object> params);

    List<HashMap<String, String>> findWaitToGetAnalyseRiskCreditUser(HashMap<String, Object> params2);

    HashMap<String, Object> findForRisk(String borrowId);

    /**
     * 更新风控状态()
     *
     * @return
     */
    int updateFirstRiskStatus(RiskCreditUser riskCreditUser);

    int updateReview(HashMap<String, Object> params2);

    RiskCreditUser findByBorrowId(Integer borrowId);
}
