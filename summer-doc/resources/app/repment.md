#还款记录、还款详情接口
1、获取还款记录
  * URL ：GET v1.0/api/app/record/{type}
  * Param:
    
         Authorization:""                //token值(放请求头中)
         type=1                          //1表示查询待还款订单、2表示已还款订单
  * Result
  ``` json 
   type=1 时返回应获取的值     
        {
            "code": 200,
            "data": [
                {
                    "id": 12,                          //还款订单id
                    "lateDay": 0,                      //逾期天数（status:3、5时获取该值）
                    "loanTime": "2019-03-12 16:23",    //借款时间              
                    "repaymentAmount": "1500.00",     //借款金额
                    "repaymentCountDown": 6,          //还款倒计时（status：0，1时获取该值）
                    "status": 0                       //0:待还款;1:部分还款;3:已逾期;5:已坏账
                },
                {
                    "id": 13,
                    "lateDay": 15,
                    "loanTime": "2019-03-18 17:53",
                    "repaymentAmount": "700.00",
                    "repaymentCountDown": 7,
                    "status": 3
                }
                
            ],
            "msg": "成功"
        }
   ```    
   ``` json     
        type=2 时返回应获取的值     
        {
            "code": 200,
            "data": [
                {
                    "id": 15,                        //还款订单id
                    "paidTime": "1970-01-01 08:00",  //还款时间
                    "repaymentAmount": "1000.00",    //还款金额
                    "status": 4                      //2:已还款;4:逾期已还款,6提前还款
                },
                {
                    "id": 16,
                    "paidTime": "1970-01-01 08:00",
                    "repaymentAmount": "1500.00",
                    "status": 2
                }
            ],
            "msg": "成功"
        }
     
  ``` 
  
2、还款详情
  * URL ：GET v1.0/api/app/repayment/detail
  * Param:
  
         Authorization:""                //token值(放请求头中)
         orderId=1                       //订单id
  * Result
  ``` json
    {
        "code": 200,
        "data": {
            "accrual": 0,                 //服务费
            "lateDay": 0,                //逾期天数
            "lateFee": 0,                // 罚息
            "loanTime": 1552378986000,   //借款时间
            "orderNum": "1015523789485609", //订单号
            "principalAmount": 975,         //本金
            "reOrderId": 12,                //还款订单号
            "renewalsTimes": null,          //延期次数
            "repaymentAmount": 150000,      //应还金额
            "repaymentDownTime": 6,         //待还款天数
            "serviceCharge": 343,          //服务费
            "loanTerm":7,                   //借款天数
            "status": 1,                    //订单状态(0:待还款;2:已还款;3:已逾期;4:逾期已还款，5:已坏账(也属于逾期))
            "repaymentTime":""              //还款时间,
             renewal_switch                 //续期开关，0表示开启，1表示关闭
        },
        "msg": "成功"
    }
  ```   
3、续期详情
   * URL ：GET v1.0/api/app/renewal/detail
   * Param:
    
           Authorization:""                //token值(放请求头中)
           orderId=1                       //还款中订单id
           
   * Result
   
   ``` json
        {
            "code": 200,
            "data": {
                "feeAmount": "525.00",                  //服务费              
                "newRepaymentTime": "1970-01-08 08:00",  //新的还款时间
                "orderRepaymentId": 16,                  //还款订单id
                "renewalExpire": 7,                     //延期
                "renewalFee": "20.00",                  //续期费
                "repaymentAmount": 1500.00,             //到期应还
                "repaymentTime": "1970-01-01 08:00"     //原还款时间
            },
            "msg": "成功"
        }
   ```      