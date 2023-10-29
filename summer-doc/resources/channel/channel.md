#渠道管理 
###统一前缀：/v1.0/api/platformChannel

1、列表查询
   * 路径：/query
   * 参数： 
   
          channelName 渠道名称  非必填  string
          plateformUserId 运营人员ID 非必填 int
          startTime 开始时间 非必填  date
          endTime  结束时间  非必填  date
          pageNum 当前页码  必填  int
          pageSize 条数     必填   int
    
       
   * 返回：  
 
         channelName 渠道名称   string
         price  合作价格  int
         managerName	合作联系人 string
	     managerPhone    合作联系人电话 string
	     decreasePercentage 扣量比例 BigDecimal
         paymentType   结算方式  int                 //结算方式：0 日结,1 月结 2 周结 3 其他
         cooperationMode  合作模式		int         //合作模式：0 cpa, 1 cps ,2 其他'
	     paymentMode  结算模式  int                 //结算模式：0 对公, 1 对私
	     companyName  公司名称  string
         createTime    创建时间  
         status       状态 0表示下线，1表示上线
         freezeStatus 冻结状态：冻结状态：0表示未冻结，1表示冻结
         forbiddenStatus 禁用状态：0表示未禁用，1表示禁用
         
         wechatSwitch; 微信打开链接开关：0表示允许，1表示禁用
         creditSuperSwitch;贷超打开链接开关：0表示允许，1表示禁用
         browserSwitch;浏览器打开链接开关：0表示允许，1表示禁用
         riskType : 该渠道风控类型0=未知4=pb风控，5=mb风控 
         riskScore : 风控分数
          expire ：借款期限
          serviceCharge ：借款服务费
          borrowInterest ： 借款利息
          renewalExpire ： 续期期限
          pcSwitch :pc浏览器开关
         
	    
2、添加渠道
   * 路径：/createChannel
   * 参数 ： 
   
         channelName 渠道名称   string
         price  合作价格  int
         managerName	合作联系人 string
         managerPhone    合作联系人电话 string
         decreasePercentage 扣量比例 BigDecimal
         paymentType   结算方式  int
         cooperationMode  合作模式		int
         paymentMode  结算模式  int
         companyName  公司名称  string
         remark       备注   string
         riskType : 该渠道风控类型0=未知4=pb风控，5=mb风控 
         riskScore : 风控分数
         expire ：借款期限
         serviceCharge ：借款服务费
         borrowInterest ： 借款利息
         renewalExpire ： 续期期限

		
         code : code=201;//新建或修改数据成功    code=400 ;//新建或修改数据的操作失败
		
3、更新渠道状态
   * 路径：/updateByStatus
   * 参数：
   
                  id  int  必填   渠道id
                  channelName 渠道名称   string
                  price  合作价格  int
                  managerName	合作联系人 string
                  managerPhone    合作联系人电话 string
                  decreasePercentage 扣量比例 BigDecimal
                  paymentType   结算方式  int
                  cooperationMode  合作模式		int
                  paymentMode  结算模式  int
                  companyName  公司名称  string
                  remark       备注   string
                  riskType : 该渠道风控类型0=未知4=pb风控，5=mb风控 
                  riskScore : 风控分数
                  expire ：借款期限
                  serviceCharge ：借款服务费
                  borrowInterest ： 借款利息
                  renewalExpire ： 续期期限
                  pcSwitch :pc浏览器开关 
               
	  
4、渠道人员统计
   * 路径:/channelUser
   * 参数：
   
         userName 运营人员名称  string  非必填
         startTime  开始时间
	     endTime    结束时间
	     pageNUm    当前页码   必填
	     pageSize   条数       必填
   * 返回：
   
         reportTime  日期
	     userName  运营人员
	     deliveryConnection 投放连接数
	     reallyRegister 真实注册数
	     channelRegister 渠道注册数
	     loanCost  放款成本
	     registerCost 注册成本
	 
	 
##渠道数据 
###统一前缀：v1.0/api/dailystatics
1、进量统计
   * 路径：/channelFlow
   * 参数：
   
         channelName 渠道名称 string
	     userId 运营人员id  int
	     startTime 开始时间
	     endTime   结束时间
	     pageNUm    当前页码   必填
	     pageSize   条数       必填
   * 返回：
   
         reportTime 日期
         channelName 渠道名称
	     plateformUserName 运营人员
	     price  合作价格
	     uv
	     pv
	     uvConversion uv转化率
	     registerNumber 注册用户数
	     registerNumberChannel 渠道注册数
	     pplicationNumber 申请人数
	     pplicationConversion 申请转化率
	     loanNumberChannel 放款量
	     loanConversion  放款转化率
	     registerSum 总注册量
	     aplicationSum 总申请量
	     loanNumberSum 总放款笔数
	     channelRegister 扣量渠道注册数
	     registerSum  注册数
	     aplicationSum 申请人数
	     loanNumberSum  放款人数
	     channelRegister 渠道注册数
	  
2、还款统计
  * 路径：/chaneelLend
  * 参数：
  
        channelName 渠道名称 string
	    userId 运营人员id  int
	    startTime 开始时间
	    endTime   结束时间
	    pageNUm    当前页码   必填
	    pageSize   条数       必填
  * 返回：
        //列表
        overdueRate 总逾期率
        firstOverdueRate 新用户逾期率
        oldOverdueRate 老用户逾期率
	    repaymentTime 还款日期
        channelName 渠道名称
	    sumNumber 总单量
	    repaymentNumber  已还款
	    overdueNumber 逾期量
	    firstBorrow 新用户待还笔数
	    firstBorrowOverdue 新用户逾期量
	    oldNumber 老用户待还笔数
	    oldOverdueNumber 老用户逾期笔数
	    repaymentSum 总待还笔数
	    firstBorrowSum  新用户待还笔数
	    oldNumberSum 老用户待还笔数
	    overdueNumberSum 总逾期笔数
	    firstBorrowOverdueSum 新用户总逾期笔数
	    oldOverdueNumberSum 老用户总逾期笔数
	    
        newRepaymentNumber 新用户已还数量        
        oldRepaymentNumber 老用户已还数量   
        newRepaymentRate 新用户还款率
        oldRepaymentRate   老用户还款率
	  
	   //总的列表上方的数据
	  
	    repaymentSum  总待还笔数
	    firstBorrowSum 新用户待还笔数
	    oldNumberSum 老用户待还笔数
	    overdueNumberSum 总逾期笔数
	    firstBorrowOverdueSum 新用户逾期笔数
	    oldOverdueNumberSum 老用户逾期笔数
	    overdueSumRate  总逾期率
	    firstOverdueSumRate 新用户逾期率
	    oldOverdueSumRate 老用户逾期率
	  
3、我方渠道用户统计
  * 路径：/findMyChannelStatistics
  * 参数：
      
        channelName 渠道名称 string
        startTime  开始时间
	    endTime    结束时间
        pageNUm    当前页码   必填
	    pageSize   条数       必填
  * 返回：
  
        reportTime 日期
	    channelName 渠道名称
	    registerCount 注册量
	    downConversion  下载率
	    loginConversion   登录率
	    idcardCertificationCount  身份认证
	    personalInformationCount 个人信息
	    operatorCount 运营商认证
	    sesameCount  芝麻分认证
	    authBankCount 银行卡认证人数
	    companyCount 工作信息
	    borrowApplyConversion 申请率
	    loanCount  放款人数
	 
        averageApplicationRate 平均申請率
        averageLoanRate 平均还款率
	 
4、渠道方统计
  * 路径：/findChannelStatistics	 
  * 参数：
    	 
        channelName 渠道名称 
        startTime  开始时间
	    endTime    结束时间
        pageNUm    当前页码   必填
	    pageSize   条数       必填
  * 返回： 	  
        
        reportTime 日期
	    channelName 渠道名称
	    deductionBorrowApplyCount 渠道方申请借款人数
	    deductionRegisterCount    渠道方注册量
	    deductionBorrowSucCount   渠道方申请成功人数
	    passRate  通过率
	 
	 
5、修改系数
   * 路径;/updateCoefficient
   * 参数 : 
      
       id 当前点击数据的id
       dedutionCoefficient 扣量比例
	  

	  
#账户管理  
###统一前缀：/v1.0/api/user

1、添加用户 
   * 路径：/addUser
   * 参数：
      
         phoneNumber 手机号码
         roleName  角色名称
	     userName  姓名
	     status    状态  int  0或1

2、账户管理列表
   * 路径：/queryUser
   * 参数：
      
         status  角色状态
         userName 	 用户姓名
	     phoneNumber 手机号码
	     roleName  角色名称
   * 返回:
   
         userName  姓名
	     phoneNumber 手机号码
	     roleName 角色名称
	     status 状态
	     lastLoginTime 最后登录时间
	     ipAddress 最后登录ip
	     userSuperName 添加人

3、角色管理列表
   * 路径：/queryUserRole
   * 参数： 
       
         roleName 角色名称
         status  角色状态
   * 返回：
    
         roleName 角色名称
         authorityCount 权限数
         status  状态
         createTime 添加时间
         userSuperName  添加人

	 
4、删除用户
   * 路径：/deleteByStatus	 
   * 参数：
       
         id 
         status
	  
5、修改用户角色账户管理
   * 路径：/updateByRole
   * 参数：
       
         id
         roleId 角色id    

6、权限列表
   * 路径：/findByAuthority	  

7、加载角色下的权限信息    
   * 路径：/findRoleAuthority
   * 参数 ： 
     
         id
   * 返回：
   
         authorityName  权限名称
         roleName  角色名称
	     id 
	     status  角色状态
	  
8、添加角色并赋予权限
   * 路径：/insertRole
   * 参数：
     
         authorityList 权限列表  
         roleName  角色名称      
         status  	              
	

9、修改角色名称和权限
   * 路径：/updateRole
   * 参数：
   	 
   	     authorityList 权限列表  
         id                       
	     roleName  角色名称
         status  状态  
	
10、删除角色
   * 路径：/deleteRole	
   * 参数：
      
         id
11、 获取运营人员列表
   * 路径 v1.0/api/dailystatics/getOperatorList
   * 参数：无
   
   * 返回
   
   
     {
         "code": 200,
         "data": [
             {
                 "id": 731,            //运营人员id
                 "userName": "张三"    //运营人员姓名
             },
             {
                 "id": 732,
                 "userName": "张杰"
             },
             {
                 "id": 733,
                 "userName": "admin"
             }
         ],
         "msg": "成功"
     }    
12、 获取获取渠道名称集合
   * 路径 GET /v1.0/api/platformChannel/getChannelName
   * 参数：无
          
   * 返回  
   
   
        {
            "code": 200,
            "data": [
                {
                    "channelId": 1,
                    "channelName": "APP"   //渠道名称
                },
                {
                    "channelId": 2,
                    "channelName": "测试渠道"
                },
                {
                    "channelId": 3,
                    "channelName": "ceshi1"
                },
                {
                    "channelId": 4,
                    "channelName": "测试1"
                },
                {
                    "channelId": 5,
                    "channelName": "测试2"
                },
                {
                    "channelId": 6,
                    "channelName": "测试3"
                },
                {
                    "channelId": 7,
                    "channelName": "测试4"
                },
                {
                    "channelId": 8,
                    "channelName": "测试5"
                }
            ],
            "msg": "成功"
        }
        
        
        
13、 获取获取借款对账手机集合
   * 路径 GET /v1.0/api/financeStatistic/getPhone
   * 参数：无
          
   * 返回  
                 
         {
             "code": 200,
             "data": [
                 {
                     "phone": "",
                     "userId": 89
                 },
                 {
                     "phone": "13554501523",
                     "userId": 91
                 },
                 {
                     "phone": "13738034183",
                     "userId": 93
                 },
                 {
                     "phone": "13173679272",
                     "userId": 94
                 }
             ],
             "msg": "成功"
         }
         
 13、 获取角色集合
   * 路径 GET /v1.0/api/role/getRole
   * 参数：无
           
   * 返回   
   
   
     {
       "code": 200,
       "data": [
           {
               "description": "超级管理员",  //角色描述
               "id": 1                       //角色表id
           },
           {
               "description": "催收主管",
               "id": 2
           },
           {
               "description": "渠道主管",
               "id": 3
           },
           {
               "description": "审核",
               "id": 4
           },
           {
               "description": "客服",
               "id": 5
           },
           {
               "description": "财务",
               "id": 6
           },
           {
               "description": "渠道专员",
               "id": 7
           },
           {
               "description": "催收专员",
               "id": 8
           }
       ],
       "msg": "成功"
   }   
   
       