### 分配
1、查询分配给客服的当日应还订单列表
   * URL ：POST /v1.0/api/distribution/selectRepaymentDistribution
   * Param:
       
          Authorization:""                //token值(放请求头中)
                     
   ``` json         
    {
        "pageNum": 1,
        "pageSize": 10,
        "realName":"马起林",
        "phone":"15840777501",
        "remark":1,
        "beginTime":"2019-06-13",
        "endTime":"2019-06-13"
    }
   ``` 
   * Result
   ``` json  
    {
        "code": 200,
        "data": {
            "endRow": 1,
            "firstPage": 1,
            "hasNextPage": false,
            "hasPreviousPage": false,
            "isFirstPage": true,
            "isLastPage": true,
            "lastPage": 1,
            "list": [
                {
                    "id": 140,
                    "lateFee": 0,                       //滞纳金
                    "loanTerm": 5,                      //借款期限
                    "loanTime": "2019-05-25 17:19:02",  //放款时间
                    "paidAmount": 0,                    //已还款金额
                    "phone": "15840777501",             //手机号
                    "principalAmount": 200000,          //应还款本金
                    "realName": "马起林",               //客户姓名
                    "remark": "未接听",                 //备注
                    "repaymentAmount": 200000,          //借款金额
                    "repaymentTime": "2019-06-13 17:19:02"//应还款时间
                    "idCard": "210882198503050637"      //身份证号码
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
        },
        "msg": "成功"
    }
   ```  

2、下载分配给客服的当日应还订单列表
   * URL ：POST /v1.0/api/distribution/downDistribution
   * Param:
       
          Authorization:""                //token值(放请求头中)
                     
   ``` json         
    {
        "pageNum": 1,
        "pageSize": 10,
        "realName":"马起林",
        "phone":"15840777501",
        "remark":1,
        "beginTime":"2019-06-13",
        "endTime":"2019-06-13"
    }
   ``` 

3、修改备注
   * URL ：POST /v1.0/api/distribution/updateDistribution
   * Param:
   
    Authorization:""                //token值(放请求头中)
                     
   ``` json         
    {
        "id":140,   //id
        "remark":1  //备注
    } 
   ``` 
   * Result
   ``` json  
    {
        "code": 200,
        "data": null,
        "msg": "新建或更新数据成功"
    }
   ```
   
4、每天1点定时分配给客服的当日应还订单列表

5、查询催收统计列表
   * URL ：POST /v1.0/api/collectionStatistics/queryCollectionStatistics
   * Param:
       
          Authorization:""                //token值(放请求头中)
                     
   ``` json         
     {
         "pageNum": 1,
         "pageSize": 10,
         "roleId":8,                //角色id
         "userId":818,              //催收人员账户id
         "beginTime":"2019-07-13",  //开始时间
         "endTime":"2019-07-19"     //结束时间
     }
   ``` 
   * Result
   ``` json  
    {
        "code": 200,
        "data": {
            "collection": [
                {
                    "id": 818,
                    "userName": "毛催"
                },
                {
                    "id": 833,
                    "userName": "毛催2"
                },
                {
                    "id": 834,
                    "userName": "当日催收1"
                },
                {
                    "id": 835,
                    "userName": "当日催收2"
                },
                {
                    "id": 871,
                    "userName": "当日催收3"
                },
                {
                    "id": 872,
                    "userName": "当日催收4"
                }
            ],
            "pageInfo": {
                "endRow": 1,
                "firstPage": 1,
                "hasNextPage": false,
                "hasPreviousPage": false,
                "isFirstPage": true,
                "isLastPage": true,
                "lastPage": 1,
                "list": [
                    {
                        "countDate": "2019-07-19",      //日期
                        "distributionAmount": 1416150,  //应还金额
                        "distributionNumber": 378,      //分配单数
                        "id": 2,
                        "repaymentAmount": 24150,       //催回金额
                        "repaymentAmountRate": 1.71,    //金额催回率
                        "repaymentNumber": 7,           //催回单数
                        "repaymentNumberRate": 1.85,    //笔数催回率
                        "roleId": null,
                        "roleName": "催收专员",         //角色
                        "userName": "毛催"              //催收人员
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

6、催收统计列表导出
   * URL ：POST /v1.0/api/collectionStatistics/collectionStatisticsDown
   * Param:
       
          Authorization:""                //token值(放请求头中)
                     
   ``` json         
     {
         "roleId":8,                //角色id
         "userId":818,              //催收人员账户id
         "beginTime":"2019-07-13",  //开始时间
         "endTime":"2019-07-19"     //结束时间
     }
   ``` 
   * Result

7、转派
   * URL ：POST /v1.0/api/distribution/transfer
   * Param:
   
    Authorization:""                //token值(放请求头中)
                     
   ``` json         
    {
        "ids": "21,22", //id
        "userId": 804   //当日催收员id
    }
   ``` 
   * Result
   ``` json  
    {
        "code": 200,
        "data": null,
        "msg": "转派成功"
    }
   ```
   
   