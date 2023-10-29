### 客户相关接口
1、修改身份信息
   * URL ：POST /v1.0/api/userInfo/updateUserInformation
   * Param:
       
          Authorization:""                //token值(放请求头中)
                     
   ``` json         
    {
        "id":28448,                     //用户id
        "realName":"修改信息测试",      //用户姓名
        "idCard":"420821199301196011"   //用户身份证号
    }
   ``` 
   * Result
   ``` json  
    {
        "code": 200,
        "data": null,
        "msg": "身份信息修改成功！"
    }
   ```  

2、新系统客户列表查询
   * URL ：POST /v1.0/api/userInfo/queryWithBorrowNew
   * Param:
                     
   ``` json         
    {
        "astatus": 13,              //订单状态
        "channelId": 47,            //渠道id
        "eCreateTime": "2019-06-10",//注册时间，结束
        "isAuthentic": 2,           //认证状态
        "pageNum": 1,               
        "pageSize": 10,             
        "phone": "13738031111",     //手机号
        "realName": "柳客户",       //客户姓名
        "customerType": 1,          //新/老客：0：新用户；1：老用户
        "sCreateTime": "2019-06-10" //注册时间，开始
    }   
   ``` 
   * Result
   ``` json  
        {
            "code": 200,
            "data": {
                "channel": [......],
                "loanAmount": 1500,
                "page": {
                    "endRow": 1,
                    "firstPage": 1,
                    "hasNextPage": false,
                    "hasPreviousPage": false,
                    "isFirstPage": true,
                    "isLastPage": true,
                    "lastPage": 1,
                    "list": [
                        {
                            "advice": null,
                            "amountAvailable": 150000,          //可借金额
                            "amountAvailableNormal": "1500.00",
                            "applyAmount": 200000,
                            "applyAmountNormal": "2000.00",
                            "applyTimeChg": "2019-06-12 13:09:57",
                            "auditStatus": 13,
                            "auditStatusName": "审核通过,",     //订单状态
                            "authenticStatus": 4,               //认证项：1：身份认证，2：身份认证、个人认证，3：身份认证、个人认证、运营商认证，4：身份认证、个人认证、运营商认证、银行卡
                            "authenticStatusName": "已认证",
                            "bankAccount": null,
                            "bankCardId": null,
                            "blackReason": "",
                            "borrowId": 205,
                            "borrowStatus": 13,
                            "borrowStatusName": "已逾期还款,",
                            "channelName": "qqq",           //渠道
                            "clientType": 1,                //手机型号：客户端类型：0表示未知，1表示安卓，2表示IOS
                            "createTimeChg": "2019-06-10 18:38:45", //注册时间
                            "creditLevel": null,
                            "customerType": 1,              //新/老客：0：新用户；1：老用户
                            "flowNo": null,
                            "hitRiskTimes": null,
                            "id": 28442,
                            "idCard": "111121199301191111", //身份证号
                            "increaseStatus": 3,
                            "interest": null,
                            "interestNormal": 0,
                            "intoMoney": null,
                            "intoMoneyNormal": null,
                            "loanAmount": null,
                            "loanFee": null,
                            "loanReviewRemark": "",
                            "loanReviewUser": null,
                            "loanStatusName": "放款成功",
                            "loanTerm": 7,
                            "loanTimeChg": null,
                            "orderStatusName": "逾期已还",
                            "outTradeNo": "1215603161967421",
                            "overdueCount": 2,              //逾期次数
                            "payRemark": null,
                            "phone": "13738031111",         //手机号
                            "phoneMd5": "137****4182",
                            "pid": null,
                            "realName": "柳客户",          //客户姓名
                            "reloanCount": 5,
                            "renewalCount": null,
                            "reviewRemark": null,
                            "reviewer": null,
                            "serviceCharge": null,
                            "status": 0,
                            "statusNormal": null,
                            "trialRemark": "审核通过",
                            "trialTimeChg": null,
                            "userId": null
                        }
                    ],
                    "navigateFirstPage": 1,
                    "navigateLastPage": 1,
                    "navigatePages": 8,
                    "navigatepageNums": [
                        1
                    ],
                    "nextPage": 0,
                    "pageNum": 1,
                    "pageSize": 10,
                    "pages": 1,
                    "prePage": 0,
                    "size": 1,
                    "startRow": 1,
                    "total": 1
                }
            },
            "msg": "成功"
        }
   ```

3、查询系统是新系统还是老系统
   * URL ：POST /v1.0/api/userInfo/getVersion
   * Param:
       
          Authorization:""                //token值(放请求头中)
                     
   * Result
   ``` json  
        {
            "code": 200,
            "data": {
                "id": 712,
                "sysValue": 0   //老系统0新系统1
            },
            "msg": "成功"
        }
   ```  
   
4、安卓用户7天内的短信
   * URL ：POST /v1.0/api/borrowOrder/querySms
   * Param:
                     
   ``` json         
    {
        "phone":"13738034183",
        "pageNum":1,
        "pageSize":10
    }
   ``` 
   * Result
   ``` json  
    {
        "code": 200,
        "data": {
            "endRow": 5,
            "firstPage": 1,
            "hasNextPage": false,
            "hasPreviousPage": false,
            "isFirstPage": true,
            "isLastPage": true,
            "lastPage": 1,
            "list": [
                {
                    "id": 3,
                    "messageContent": "此儲值卡將於今天 23:59失效，請以增值券增值可享額外儲值額。",
                    "messageDate": "2019-01-24 09:32:57",
                    "phone": "13738034183"
                },
                {
                    "id": 4,
                    "messageContent": "此儲值卡尚有1天有效24/01/2019 23:59，請儘快增值以便保留現有流動電話號碼，以增值券增值可享額外儲值額。",
                    "messageDate": "2019-01-23 09:32:44",
                    "phone": "13738034183"
                },
                {
                    "id": 5,
                    "messageContent": "此儲值卡將於今天23:59到期，請儘快增值以便保留現有流動電話號碼，以增值券增值可享額外儲值額。",
                    "messageDate": "2019-01-23 09:32:41",
                    "phone": "13738034183"
                },
                {
                    "id": 6,
                    "messageContent": "此儲值卡尚有1天有效(23/01/2019 23:59)。請儘快增值以便保留現有流動電話號碼，以增值券增值可享額外儲值額。",
                    "messageDate": "2019-01-22 09:33:17",
                    "phone": "13738034183"
                },
                {
                    "id": 7,
                    "messageContent": "此儲值卡尚有3天有效24/01/2019 23:59，請儘快增值以便保留現有流動電話號碼，以增值券增值可享額外儲值額。",
                    "messageDate": "2019-01-21 09:32:37",
                    "phone": "13738034183"
                }
            ],
            "navigateFirstPage": 1,
            "navigateLastPage": 1,
            "navigatePages": 8,
            "navigatepageNums": [
                1
            ],
            "nextPage": 0,
            "pageNum": 1,
            "pageSize": 10,
            "pages": 1,
            "prePage": 0,
            "size": 5,
            "startRow": 1,
            "total": 5
        },
        "msg": "成功"
    }
   ```
   
5、重置APP用户登录密码为abcd1234
   * URL ：POST /v1.0/api/userInfo/updatePassword
   * Param:
       
          Authorization:""                //token值(放请求头中)
                     
       ``` json         
        {
            "id":1      //用户id
        }
       ``` 
   * Result
   
   成功：
   ``` json  
    {
        "code": 200,
        "data": null,
        "message": "重置密码成功！"
    }
   ```  
   失败：
   ``` json  
    {
        "code": -2,
        "data": null,
        "message": "参数非法"
    }
   ``` 