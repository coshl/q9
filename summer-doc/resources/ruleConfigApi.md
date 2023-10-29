# 配置规则api
1、获取风控配置规则，
   * URL :GET("/v1.0/api/risk/rule")
   * Param
   
              "Authorization":""              //token值
   * Result:    
   ``` json 
   { 
    "code": 200,  
    "msg": "获取成功",
    "data": {
	"code": 200,
	"data": 
	    riskList:[{
		    "id": 1,
		    "riskDecision": 0,                                        //风险决策（0表示通过，1表示不通过）
		    "ruleDescript": "年龄小于18，大于45岁",                    //规则名称
		    "ruleKey": "r_ageScope",                                  //规则的key
		    "ruleType": 1,                                            //（0表示存储单数值型，1表示存储数值范围型，2表示存储数组形式存储型(比如比如涉及敏感词汇：警                                                                                                                         官，警察),3表示直接等于strValue，4，不同类型的值的范围型：(比如通讯录与通话记录中前10位匹配                                                                                                                            号码数量小于3),   5，表示包含某些关键字超过某个指标时:比如通讯录命中敏感性词汇（借、贷相关个                                                                                                                          人或平台）大于10个 ）
		    "ruleValue": "{\"maxValue\":45,\"minValue\":18}",         //条件值条件值(JSON数据 单数值型时：type 0表示小于，1表示大于，2表示等于)
		    "status": 1,                                              //0表示启用,1表示为关闭    
	    }],
	     
	     "risk": //风控分数
	     {
	       "id":1,
           "sysValue"  //分数值
          } 
          "nature": 自然风控分数
          {   
          	"id":1,
          	"sysValue"  //分数值
          	},
          	"oldScore": 老用户风控分数
             {   
             "id":1,
             "sysValue":""  //分数值
              },
     }
     
   ```
   
1.1、修改
   * Url POST /v1.0/api/updateRiskScore
   * Param 
   
   
              "risk": //风控分数
     	     {
     	       "id":1,
                "sysValue":""  //分数值
               } 
               "nature": 自然风控分数
               {   
               	"id":1,
               	"sysValue":""  //分数值
               	},
               	"oldScore": 老用户风控分数
                 {   
                 	"id":1,
                    "sysValue":""  //分数值
                  },
   
   

2、风控配置规则启用/关闭

   * URL :PUT(“/v1.0/api/update/risk/rule”)
   * Param

            "Authorization":""      //token值   
            "id":1,                 //规则的id
            "status":"1"            //0表示启用,1表示为关闭    
    
   * Result:
   ``` json
   {
       "code": 201,
       "data": {},
       "msg": "成功"
   }
   ```   
3、获取认证规则接口
   * URL :GET v1.0/api/auth/rule
   * Param
   
             Authorization:""    //token值
             
   * Result:
   ``` json
   {
    "code": 200,                                     //获取数据成功的状态码
    "msg": "获取成功",
    "data": { 
        "id":"1"                                       //id
        "authKey":"IdCard"                     //认证的key
        "authDescript":"身份认证"           //认证名称
        "authType":"0"                          //认证类型(0必要认证，1补充认证)
        "addUser":"张杰"                       // 添加的用户
        "addTime":"2019-01-20 14:20"  //添加时间      
        "status":"0"                              //0表示启用，1表示关闭
        }
   }

   ```
4、 修改认证规则接口
   * URL :PUT v1.0/api/update/auth/rule
   * Param:
    
            Authorization:""    //token值     
            "id":"1"             //认证规则id
            "status":"0"         //0表示启用，1表示关闭
                 
   * Result:
   ``` json
   {
    "code": 201,                                             //增加或则修改成功的状态码
    "msg": "修改成功",
    "data": { }
   }
   ```
   
5、 获取贷款规则配置接口
   * URL：GET v1.0/api/loan/rule
   * Param:
    
                Authorization:""    //token值
   * Result:
    
    ``` json  
      {
    "code": 200,
    "data": {
        "LoanRuleConfig": {
              "id": 1,                                       //规则id              
             "expire": 6,                                    //借款期限
            "borrowInterest": 0.007,                          // 借款利息     
             "serviceCharge": 0.343,                         //服务费      
             "loanAmount": 1500,                             //最多借款
             "overdueRate": 0,                                //滞纳金额利率（滞纳金额）
            "highestOverdueRate": 0,                          //最高滞纳金额利率（最高滞纳金额）
            "renewalExpire": 6,                               //续期期限
            "renewalFee": 0,                                   //续期的手续费利率（续期金额）
             “hightestRenewal”:0                             //0表示无限续期  
             "hitRiskAllowBorrowDay": 0,                       //命中风控（0表示永久不能再借：备注{“ 1以上表示命中风控XX天后能再借 ” }）  
            "hitBlackAllowBorrowDay": 0,                       //命中黑名单（0表示永久不能再借 ：备注{“ 1以上表示命中黑名单XX天后能再借 ” }）           
            "normalRepaymentRepetitionLoan": 0,                //正常还款是否可以复贷（0表示是，1表示否）
             "isAllowLoan":1,                                  //逾期还款是否可以复借 （0表示否 1表示是）
             "overdueDay":7                                    //当isAllowLoan=1时，显示该值：小于 7     （7为可调数据）                                                    
        },
        "repetitionIncreaseList": [                                //提额降息规则集合
            {
                "id": 11,                                                     //规则id
                "achieveTimes": 11，                               //次数
                "repetitionInreaseMoney": 1700,                     //提升额度
                "reduceInterest": 0.007,                          //降低利息
                 "increaseType": 2,                                   //提额类型，（0表示正常还款次数，1表示逾期还款次数,2表示复贷次数）
            }
        ]
    },
     "renewalSwitch":{
        id:1           //主键
        sysValue:0     //续期开关，0表示开启，1表示关闭
     }                 
    "msg": "成功"    
    ``` 

6、修改贷款规则配置接口
   * URL：PUT v1.0/api/update/loan/rule
   * Param:
    
           Authorization:""    //token值
           
            {
             "id": 1,                                            //规则id              
             "expire": 6,                                        //借款期限
             "borrowInterest": 0.007,                             // 借款利息     
             "serviceCharge": 0.343,                             //服务费      
             "loanAmount": 1500,                                 //最多借款
             "overdueRate": 0,                                   //滞纳金额利率（滞纳金额）
             "highestOverdueRate": 0,                             //最高滞纳金额利率（最高滞纳金额）
             "renewalExpire": 6,                                  //续期期限
             "renewalFee": 0,                                     //续期的手续费利率（续期金额）
             "hightestRenewal":0                                 // 最多续期  0表示无限续期  
             "hitRiskAllowBorrowDay": 0,                         //命中风控（0表示永久不能再借：备注{“ 1以上表示命中风控XX天后能再借 ” }）  
             "hitBlackAllowBorrowDay": 0,                         //命中黑名单（0表示永久不能再借 ：备注{“ 1以上表示命中黑名单XX天后能再借 ” }）           
             "normalRepaymentRepetitionLoan": 0,                  //正常还款是否可以复贷（0表示是，1表示否）
             "isAllowLoan":1,                                    //逾期还款是否可以复借 （0表示否 1表示是）
             "overdueDay":7                                      //当isAllowLoan=1时，显示该值：小于 7     （7为可调数据）
             "increaseMoneyConfigs": [
            {                                             //（id有值的）代表更新
              
                "id": 11,                                          //规则id
                "achieveTimes": 11，                               //次数
                "repetitionInreaseMoney": 1700,                   //提升额度
                "reduceInterest": 0.007,                          //降低利息
                "increaseType": 2,                               //提额类型，（0表示正常还款次数，1表示逾期还款次数,2表示复贷次数）
            }  ,
            {                                            //id无值代表新增             
               
                "achieveTimes": 12，                               //次数 
                "repetitionInreaseMoney": 1700,                   //提升额度
                "reduceInterest": 0.007,                          //降低利息
                "increaseType": 2,                               //提额类型，（0表示正常还款次数，1表示逾期还款次数,2表示复贷次数）
               
            }     
         ]
        }
   * Result:
   ``` json 
   { 
    "code": 201,  
    "msg": " 成功",
    "data": { }
    }
   ```
   
7、删除提额降息
   * URL :DELETE v1.0/api/delete/increaseMoney/rule
   * Param:
   
         Authorization:""    //token值
         id:1                //提额的的id
    
   * Result:
   ``` json 
       { 
        "code": 204,  //204删除数据成功 402：删除数据失败
        "msg": " 成功",
        "data": { }
        }
   ```   
8、一键开启或关闭风控规则
   * URL :POST /v1.0/api/delete/risk
   * Param:
   
         Authorization:""    //token值
         status:1            // 1表示一键关闭 0表示一键开启
    
   * Result:
   ``` json 
       { 
        "code": 201,   //201表示新建或修改成功，400表示新建或修改失败
        "msg": " 成功",
        "data": { }
        }
   ```     
   
9、手动提额
   * URL    POST /v1.0/api/userInfo/update/money
   * Param 
       id //用户id
       amountAvailable //金额
       
       
10、* URL   POST /v1.0/api/increase/update/status
   * Param 
     id   //提额的id
     status //0表示关，1表示开     
       
        
   
   
    
