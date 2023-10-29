# API 说明文档 测试机器:47.88.224.71:8088
## 二.主页
#### 1.POST 
       URL:
           /v1.0/api/index/borrow/statistic
       
       RES:
           1.all 累计放款金额 loanedMoney double
            new 新用户 
              笔数 loanedNum
              放款率 loanedRate
            old 老用户 
              笔数
              放款率
           2.累计还款金额 paidMoney int
            新用户 
              金额
              笔数 paidNum
              还款率 paidRate
            老用户 
              金额
              笔数
              还款率
           4.累计续期金额 renewalMoney double
            新用户 
              金额
              笔数 renewalNum
              续期率 renewalRate
            老用户 
              金额
              笔数
              续期率
           4.正常待还金额 payMoney int
            新用户 
              金额
              笔数 payNum
              正常待还率 payRate
            老用户 
              金额
              笔数
              正常待还率
           4.逾期待还金额 overdueMoney double
            新用户 
              金额
              笔数 overdueNum
              逾期率 overdueRate
            老用户 
              金额
              笔数
              逾期率

           1.矩形图 7天列表 list
             2.日期 reportDate String
             4.其他同上 
           1.复贷率 reloanRate
           1.累计注册用户数 total
           1.累计放款用户数 loan
           1.累计逾期用户数 overdue
           1.曲线图 7天列表 indexReports
             2.日期 reportDate String
             2.当日注册用户数 registerNum String
             2.当日放款成功用户数 loanedNum String
             2.当日逾期用户数 overdueNum String

## 一.客户管理 /api/userInfo
### 一.客户列表 /queryWithBorrow 入参json post 分页pageSize pageNum
#### 1.查询
    1.客户姓名 realName String
    2.手机号 phone StringT
    3.注册时间 开始:sCreateime 结束:eCreateTime String "2018-12-24"
    4.渠道来源 channelId int (查数据库)
    5.认证状态 authenticStatus int 0、未认证，1、部分认证 2、已认证
    6.申请时间 开始:sApplyTime 结束:eApplyTime String "2018-12-24"
    7.订单进度 astatus int 0:待初审(待机审);1:初审驳回;2初审通过,代放款;3待人工复审;4复审驳回;5复审通过,待放款;6:放款中;7:放款失败;8已放款，还款中;9:部分还款;10:已还款;11:已逾期;
    12:已坏账，13逾期已还款；
    8.是否续借 reloanable int 1是0否
#### 2.列表
    9.渠道来源 channelName String
    9.注册时间 createTimeChg String
    9.可借金额 amountAvailable int
    9.申请时间 applyTimeChg String
    10.申请金额 applyAmount int
    11.借款期限 loanTerm int
    12.续借次数 reloanCount int
#### 3.加入黑名单 /addBlackUser post json数组方式传参ids(用户id)

### 二.未认证列表 /queryUnauthentic
#### 1.查询
    1.客户姓名 realName String
    2.手机号 phone String
    3.注册时间 开始:sCreateTime 结束:eCreateTime String "2018-12-24"
    4.渠道来源 channelId int 
    5.已认证至 authenticStatus int 0 未认证 1、身份认证 2、个人信息认证,3运营商认证 4 银行卡绑定 
#### 2.列表
    3.注册时间 createTimeChg String
    9.渠道来源 channelName String
    6.可用额度 amountAvailable int
    7.个人信息 basicAuthentic int 1是0否
    8.紧急联系人 emergencyAuthentic int
    9.运营商认证 mobileAuthentic int
    10.芝麻授信 zmStatus int

### 三.黑白名单列表 /queryBlackUser
#### 1.查询
    1.客户姓名 realName String
    2.手机号 phone String
    3.注册时间 开始:sCreateime 结束:eCreateTime String "2018-12-24"
    4.渠道来源 channelId int (查数据库)
    5.是否续借 reloanable int 1是0否
    6.状态 status int 0、正常，1、黑名单
#### 2.列表
    7.续借次数 reloanCount int
    8.原因 blackReason String
    (数据库中添加了该字段`black_reason` varchar(100) NOT NULL DEFAULT '' COMMENT '加入黑名单的理由')
#### 3.删除 /deleteBlackUser post json方式传参id(用户id)

## 二.贷前管理 /api/borrowOrder
### 一.机审未通过列表 /queryDenied
#### 1.查询
    1.客户姓名 realName String
    2.手机号 phone String
    4.渠道来源 channelId int
    4.机审时间 开始:sTrialTime 结束:eTrialTime String "2018-12-24"
    5.机审状态 status int 1:初审驳回;2初审通过,待放款;3待人工复审;
    6.是否续借 renewable int 1是0否
    
#### 2.列表
    8.机审时间 trialTimeChg String
    8.可借金额 amountAvailable int
    9.申请金额 applyAmount int
    
    10.借款期限 loanTerm int
    11.命中数 hitRiskTimes int
    12.续借次数 renewalCount int
    13.操作 重新审核 /toReview 入参订单id int
### 二.审核订单列表 
#### 1.查询/queryWithUser 
    转派/redistribute 入参订单id  int 平台用户platformUserId  int
    导出/downloadOrderReview 入参非json
    1.客户姓名 realName String
    2.手机号 phone String
    3.申请时间 开始:sApplyTime 结束:eApplyTime String "2018-12-24"
    4.渠道来源 channelId int
    5.审核状态 status int 3待人工复审;4复审驳回;5复审通过,待放款;
    6.是否续借 renewable int
    7.审核人 platformUserId int(查询数据库)
#### 2.列表
    9.申请时间 createTimeChg String
    8.可借金额 amountAvailable int
    9.申请金额 applyAmount int
    10.借款期限 loanTerm int
    9.利息 interest double
    9.服务费 feeAmount int
    4.渠道来源 channelName String
    11.不通过原因 reviewRemark String
    12.续借次数 renewalCount int
    13.审核人 reviewer String
    14.订单号 outTradeNo String
    15.操作
    通过:/review 入参status=5 int
    拒绝:/review 入参status=4 int remark="理由"
#### 2.详情
        URL:
            /v1.0/api/borrowOrder/details
        REQ: 
            phone String 手机号
        RES:
            通讯录 userContactsList 
                姓名 contactName
                电话 contactPhone
            借款记录 orderBorrows
            还款记录 orderRepayments
            催收记录 collectionUserVOS
                    13.催收人员 userName String
                    1.时间 createTimeChg String
                    1.操作 operate String
                    1.详情 detail String
## 二.贷中管理
### 一.放款记录/api/borrowOrder
#### 1.查询/queryWithUserPass 导出/downloadOrderPass
    1.客户姓名 realName String
    2.手机号 phone String
    3.申请时间 开始:sApplyTime 结束:eApplyTime String "2018-12-24"
    4.放款时间 开始:sLoanTime 结束:eLoanTime String "2018-12-24"
    5.审核状态 status int 5复审通过,待放款;6:放款中;7:放款失败;8已放款，还款中;
    6.是否续借 renewable int
    7.审核人 platformUserId int
    
#### 2.列表
    9.申请金额 applyAmount int
    9.申请时间 createTimeChg String
    9.放款时间 loanTimeChg String
    10.借款期限 loanTerm int
    11.批复金额 loanAmount int
    12.实际到账金额 intoMoney int
    13.失败原因 payRemark String
    13.放款账户 bankAccount String
    13.订单号 outTradeNo String
    13.交易号 flowNo String
    15.操作
        重新放款:/reloan 入参订单id int
### 二.展期记录列表/api/renewalOrder
#### 1.查询/queryWithUser 导出/downloadOrderRenewal
    1.客户姓名 realName String
    2.手机号 phone String
    3.订单号 outTradeNo String
    5.支付方式 payType int 0未知 1支付宝 2微信 3银行卡 4其他
    6.展期时间 开始:sRenewalTime 结束:eRenewalTime String "2018-12-24"
    7.审核人 platformUserId int
    7.展期状态 status int 0未知 1付款中 2付款成功 3付款失败
#### 2.列表
    8.应还金额 repaymentAmount int
    9.展期费 renewalFee int 
    10.展期期限 renewalDay int
    10.应还时间 repaymentTimeChg String
    10.展期时间 createTimeChg String
    11.失败原因 remark String
    12.展期次数 renewalNo int
    13.审核人 reviewer String
### 三.待还记录列表/api/repaymentOrder
#### 1.查询/queryToPay 导出/downloadOrderRepayment
    1.客户姓名 realName String
    2.手机号 phone String
    3.待还类型 status int 0:逾期待还;1:正常待还;2:展期待还;
    3.订单号 outTradeNo String
    6.应还时间 开始:sRepaymentTime 结束:eRepaymentTime String "2018-12-24"
    7.审核人 platformUserId int
#### 2.列表
    10.应还时间 repaymentTimeChg String
    8.应还金额 repaymentAmount int
    10.借款期限 loanTerm int
    9.距离还款日 repaymentInterval int
    10.待还类型 repaymentType String
    11.逾期次数 overdueCount int
    13.审核人 reviewer String
#### 3.线下还款 /offlinePay
    订单id int 
    还款金额 paidAmount String 单位分
    还款时间 payTime String
    支付方式 payType int  0未知 1支付宝 2微信 3银行卡 4其他
    还款订单号 thirdOrderNo String
    备注 remark String
### 四.已还记录列表/api/repaymentOrder
#### 1.查询/queryPaid 导出/downloadOrderPaid

    1.客户姓名 realName String
    2.手机号 phone String
    3.还款状态 status int 0未知 1付款中 2付款成功 3付款失败
    3.支付方式 payType int 付款方式 0未知 1代扣 2微信线上 3银行卡线上 4支付宝线上 5微信线下 6银行卡线下 7支付宝线下
    6.实际还款时间 开始:sPayTime 结束:ePayTime String "2018-12-24"
    7.还款类型 overdue int 0正常 1逾期 2提前
    7.订单号 thirdOrderNo String
    7.审核人 platformUserId int
#### 2.列表
    10.实际还款时间 payTimeChg String
    8.应还金额 repaymentAmount int
    10.借款期限 loanTerm int
    10.应还时间 repaymentTime String
    9.实际还款金额 paidAmount int
    9.还款状态 status int
    9.失败原因 payTip String
    11.逾期次数 overdueCount int
    11.提前还款次数 forwardCount int
    13.审核人 reviewer String
## 二.贷后管理
### 一.减免订单/api/reductionOrder
#### 1.查询/queryWithUser 导出/downloadOrderReduction
    1.客户姓名 realName String
    2.手机号 phone String
    3.催收人员 platformUserId int
    4.逾期状态 payStatus int 0:待还款;1:部分还款;2:已还款;3:已逾期;4:逾期已还款，5:已坏账；
    5.审核状态 auditStatus int 0待审核、1拒绝、2通过;
    
#### 2.列表
    9.借款金额 applyAmount int
    10.借款期限 loanTerm int
    11.应还本金 principalAmount int
    12.滞纳金 lateFee int
    12.减免滞纳金 reductionAmount int
    13.逾期天数 lateDay int
    13.催收小组 groupLevel String
    13.催收人员 userName String
    11.逾期次数 overdueCount int
    4.逾期状态 payStatus int 0未还1部分还款2已还
    5.审核状态 auditStatus int 0待审核、1拒绝、2通过;
    15.操作
            通过/拒绝:/reduction 入参订单id int 
            auditStatus String 1拒绝、2通过
            remark String 理由
### 二.逾期管理/api/overdueOrder
#### 1.查询/queryWithUser 导出/downloadOrderOverdue
    1.客户姓名 realName String
    2.手机号 phone String
    3.催收人员 platformUserId int
    4.逾期状态 payStatus int 0未还1部分还款2已还
    5.放款日期 开始:sLoanTime 结束:eLoanTime String "2018-12-24"
    
#### 2.列表
    9.借款金额 applyAmount int
    10.借款期限 loanTerm int
    11.应还本金 principalAmount int
    12.滞纳金 lateFee int
    12.已还金额 paidAmount int
    5.放款日期 loanTimeChg  String 
    5.预计还款日期 repaymentTimeChg  String 
    13.逾期天数 lateDay int
    13.催收小组 groupLevel String
    13.催收人员 userName String
    11.逾期次数 overdueCount int
    4.逾期状态 payStatus int 0未还1部分还款2已还
#### 3.转派 /dispatch
    入参订单id int 
    3.催收人员 platformUserId int
#### 4.统计 /statistic
    3.num=1 今日催回逾期 笔数 todaySuccessCount 金额 todaySuccessAmount
    3.num=2 当月催回逾期 笔数 monthSuccessCount 金额 monthSuccessAmount
    3.num=3 当日新增逾期 笔数 todayNewCount 金额 todayNewAmount
    3.num=4 三日内逾期 笔数 overdueCount3 金额 overdueAmount3
    <!-- 3.num=5 4-10日内逾期 笔数 overdueCount4 金额 overdueAmount4 -->
    <!-- 3.num=6 11-30日内逾期 笔数 overdueCount11 金额 overdueAmount11 -->
    <!-- 3.num=7 31-90日内逾期 笔数 overdueCount31 金额 overdueAmount31 -->
    <!-- 3.num=8 90日以上逾期 笔数 overdueCount90 金额 overdueAmount90 -->
    <!-- 3.num=9 所有逾期 笔数 overdueCount 金额 overdueAmount -->
    3.num=5 3-7日内逾期 笔数 overdueCount7 金额 overdueAmount7
    3.num=6 7-30日内逾期 笔数 overdueCount30 金额 overdueAmount30
    3.num=7 30日以上逾期 笔数 overdueCount31 金额 overdueAmount31
### 二.我的催收/api/collectOrder
#### 1.查询/queryWithUser 导出/downloadOrderCollect
    1.客户姓名 realName String
    2.手机号 phone String
    4.逾期状态 payStatus int 0未还1部分还款2已还
    4.还款方式 payType int 0未知 1代扣 2微信线上 3银行卡线上 4支付宝线上 5微信线下 6银行卡线下 7支付宝线下
    5.应还日期 开始:sRepaymentTime 结束:eRepaymentTime String "2018-12-24"
    
#### 2.列表
    9.借款金额 applyAmount int
    10.借款期限 loanTerm int
    11.应还本金 principalAmount int
    12.滞纳金 lateFee int
    12.已还金额 paidAmount int
    5.放款日期 loanTimeChg  String 
    5.预计还款日期 repaymentTimeChg  String 
    13.逾期天数 lateDay int
    13.催收小组 groupLevel String
    13.催收人员 userName String
    11.逾期次数 overdueCount int
    4.逾期状态 payStatus int 0未还1部分还款2已还
    4.还款方式 payType int 0未知 1代扣 2微信线上 3银行卡线上 4支付宝线上 5微信线下 6银行卡线下 7支付宝线下
#### 3.减免滞纳金 /reduce
    入参订单id int 
    3.减免金额 reductionAmount int 单位分
    3.减免原因 remark String
#### 4.催收情况标签 /collectTag
    出参 name id
#### 5.催收登记 /addCollection
    入参订单id int 
    3.联系人电话 contactPhone String
    3.联系人名称 contactName String 
    3.联系人类型 contactType int 0未知 1: 紧急联系人 2:通讯录联系人
    3.联系人关系 relation int 0未知1父亲,2母亲,3本人,4亲人,5朋友,6同事,7其他
    3.催收类型 collectionType int (0待催收 1电话催收、2短信催收)
    3.承诺还款时间 promiseRepaymentTime String "2018-12-24"
    3.备注 remark String 
    3.催收情况标签 names String 逗号分隔

#### 6.详情 /detail
    入参订单id int 
    出参
    13.催收人员 userName String
    1.时间 createTimeChg String
    1.操作 operate String
    1.详情 detail String

## 四.统计分析
### 三.催收业务统计/v1.0/api/collectionReport
#### 1.查询/queryCollectionReport 导出/downloadCollectionReport
    1.开始时间 beginTime 结束时间 endTime String "2019-1-1"
#### 2.列表
    1.催收人 personName String "2019-1-1"
    2.催收任务数 orderTotal Integer
    3.逾期本金(元) loanMoney BigDecimal
    4.催回率(%) repaymentOrderRate BigDecimal(默认为null)
    5.催回本金(元) repaymentMoney BigDecimal
    6.催回息费(元) repaymentInterest BigDecimal
    7.催回滞纳金(元) repaymentPenalty BigDecimal
    注（通过pageInfo获取列表对象，params获取查询条件）

### 五.每日放款统计/v1.0/api/loanReport
#### 1.查询/queryLoan 导出/downloadOrderOverdue
    1.开始时间 beginTime 结束时间 endTime String "2019-1-1"
#### 2.列表
    1.日期 reportDate String "2019-1-1"
    2.注册人数 registerCount Integer
    3.申请人数 borrowApplyCount Integer
    4.申请率(%)（申请人数/注册人数） applyRate Float(若注册人数为0则为null)
    5.放款人数 loanOrderCount Integer
    6.放款金额（元）moneyAmountCount BigDecimal
    7.放款率（放款成功人数/申请人数） borrowRate Float(若申请人数为0则为null)
    注（通过pageInfo获取列表对象，moneyCount获取左上角总房款金额（元），params获取查询条件）
  
## 二.统计分析
### 一.财务统计
入参为json
#### 1.POST 借款对账查询
       URL:
           /v1.0/api/financeStatistic/borrow
       REQ: 
           1.客户姓名 realName String
           2.手机号 phone String
           4.订单号 orderNo String
           4.放款日期 开始:sLoanTime 结束:eLoanTime String "2018-12-24"
           4.下单日期 开始:sApplyTime 结束:eApplyTime String "2018-12-24"
       RES:
           1.批复总额 totalApplyMoney double
           2.笔数 totalApplyCount int
           4.应放款金额 dueIntoMoney double
           4.笔数 dueIntoCount int
           4.实际放款金额 actualIntoMoney double
           4.笔数 actualIntoCount int
           
           1.客户姓名 realName String
           2.手机号 phone String
           4.申请时间 applyTime String
           4.放款时间 loanTime String
           4.申请金额 applyMoney double
           4.实际到账金额 intoMoney double
           4.借款期限 loanTerm int
           4.利息 interest double
           4.复贷次数 reloanTime int
           4.交易号 orderNo String
#### 1.POST 借款对账导出
       URL:
           /v1.0/api/financeStatistic/borrowDown
       REQ: 
           1.客户姓名 realName String
           2.手机号 phone String
           4.订单号 orderNo String
           4.放款日期 开始:sLoanTime 结束:eLoanTime String "2018-12-24"
           4.下单日期 开始:sApplyTime 结束:eApplyTime String "2018-12-24"
#### 1.POST 还款对账查询
       URL:
           /v1.0/api/financeStatistic/repay
       REQ: 
           2.手机号 phone String
           4.订单号 orderNo String
           4.还款方式 repayType int
           4.还款时间 开始:sRepayTime 结束:eRepayTime String "2018-12-24"
       RES:
           1.还款总额 totalRepayMoney double
           2.笔数 totalRepayCount int
           4.逾期还款金额 overdueRepayMoney double
           4.笔数 overdueRepayCount int
           4.正常还款金额 normalRepayMoney double
           4.笔数 normalRepayCount int
           
           1.客户姓名 realName String
           2.手机号 phone String
           4.还款时间 repayTime String
           4.待还金额 topayMoney double
           4.已还金额 paidMoney double
           4.还款方式 repayTypeName String
           4.交易号 orderNo String
#### 1.POST 还款对账导出
       URL:
           /v1.0/api/financeStatistic/repayDown
       REQ: 
          2.手机号 phone String
          4.订单号 orderNo String
          4.还款方式 repayType int
          4.还款时间 开始:sRepayTime 结束:eRepayTime String "2018-12-24"
#### 1.POST 续期对账查询
       URL:
           /v1.0/api/financeStatistic/renewal
       REQ: 
           2.手机号 phone String
           4.订单号 orderNo String
           4.续期方式 renewalType int
           4.续期时间 开始:sRenewalTime 结束:eRenewalTime String "2018-12-24"
       RES:
           1.续期总额 totalRenewalMoney double
           2.笔数 totalRenewalCount int
           
           1.客户姓名 realName String
           2.手机号 phone String
           4.续期时间 renewalTime String
           4.续期期限 renewalTerm int
           4.续期费 renewalFee double
           4.续期前应还时间 oldRepayTime String
           4.续期后应还时间 newRepayTime String
           4.续期方式 renewalTypeName String
           4.交易号 orderNo String
#### 1.POST 续期对账导出
       URL:
           /v1.0/api/financeStatistic/renewalDown
       REQ: 
          2.手机号 phone String
          4.订单号 orderNo String
          4.续期方式 renewalType int
          4.续期时间 开始:sRenewalTime 结束:eRenewalTime String "2018-12-24"
#### 1.POST 渠道对账查询
       URL:
           /v1.0/api/financeStatistic/channel
       REQ: 
           2.运营人员 backuserId int
           4.日期 开始:sQueryTime 结束:eQueryTime String "2018-12-24"
       RES:
           1.结算总额 totalExpenseMoney int
           2.笔数 totalExpenseCount int
           
           1.日期 statisticTime String
           1.运营人员 backuserName String
           2.渠道名称 channelName String
           4.合作价格 price double
           4.渠道注册数 registerNum int
           4.结算金额 expenseAmount double
#### 1.POST 渠道对账导出
       URL:
           /v1.0/api/financeStatistic/channelDown
       REQ: 
          2.运营人员 backuserId int
          4.日期 开始:sQueryTime 结束:eQueryTime String "2018-12-24"
       
