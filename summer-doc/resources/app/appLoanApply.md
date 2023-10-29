#借款申请接口
1、 申请借款接口（对应App首页：我要借款 ：需要传金额和借款期限的时候）  暂停使用
   * URL：GET v1.0/api/app/apply/{applyMoney}/{period}
   * Param:
   
                Authorization:""     //token值                
	            "applyMoney":1000,   //申请借款金额
	            "period":7           //借款期限
	            
   * Result:
   ``` json  
   {
       "code": 200,
       "data": {
           "minMoney": 1500,               //允许的最低金额
           "period": 7,                    //借款期限
           "applyMoney": 1500,              //用户的实际最大金额
           "bankInfo": "光大银行(7777)",     //用户银行信息
           "serviceCharge": 0.343，          //服务费率   （服务费计算=：applyMoney * serviceCharge）
           "borrowInterest": 0.007,          //借款利率    (利息计算=：applyMoney * borrowInterest)
           "flag": "0"                       //允许借款的标记 （0：表示允许借款，1表示个人信息、2紧急联系人，3运营商，4表示芝麻认证，5银行卡未认证，6表示有未完成的订单,7表示申请失败）          
       },
       "msg": "允许申请借款"
   }
    
  ``` 
2、确认借款接口
   * URL：GET v1.0/api/app/loan/{applyMoney}/{period}
   * Param:
   
                Authorization:""     //token值
	            "applyMoney":1500,   //申请借款金额
	            "period":7           //借款期限
	            
   * Result:
   ``` json  
   {
       "code": 200,
       "data": null,
       "msg": "申请成功"
   }
   ```
 3、首页申请借款、认证中心申请借款  (正在使用)
    * URL：GET v1.0/api/app/apply2
    * Param:
    
                 Authorization:""     //token值
              
 	            
    * Result:
    ``` json  
    {
        "code": 200,
        "data": {
            "minMoney": 1500,               //允许的最低金额
            "period": 7,                    //借款期限
            "applyMoney": 1500,              //用户的实际最大金额
            "bankInfo": "光大银行(7777)",     //用户银行信息
            "serviceCharge": 0.343，          //服务费率   （服务费计算=：applyMoney * serviceCharge）
            "borrowInterest": 0.007,          //借款利率    (利息计算=：applyMoney * borrowInterest)
            "flag": "0"                       //允许借款的标记 （0：表示允许借款，1表示个人信息、2紧急联系人，3运营商，4表示芝麻认证，5银行卡未认证，6表示有未完成的订单）          
        },
        "msg": "允许申请借款"
    }
     
   ``` 
 