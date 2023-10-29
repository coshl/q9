# 费用
1、商户后台查询自己的充值记录或退费记录
   * URL ：POST /v1.0/api/billing/queryPlateformRecharge
   * Param:
    
   ``` json  
    {
        "type":0,               //类型：0：查询充值，1：查询退费
        "beginTime":"2019-3-21 10:10:10",   //开始时间
        "endTime":"2019-3-29 22:10:10",     //结束时间
        "pageNum":1,
        "pageSize":10
    }
   ```

   * Result
   ``` json  
    {
        "code": 200,
        "data": {
            "count": {
                "balance": 19500,
                "callTimes": 50,
                "consumeMoney": 500,
                "reportPrice": 10,
                "riskPrice": 10,
                "totalMoney": 20000
            },
            "pageInfo": {
                "endRow": 2,
                "firstPage": 1,
                "hasNextPage": false,
                "hasPreviousPage": false,
                "isFirstPage": true,
                "isLastPage": true,
                "lastPage": 1,
                "list": [
                    {
                        "businessName": "向钱冲",
                        "certificate": "http://qiniu.youjing.tech/suibianbg.png",
                        "id": 1,
                        "outOrder": "11111",
                        "payMoney": 10000,
                        "payTime": "2019-07-02 22:35:03",
                        "pid": 10001,
                        "plateformName": "士强",
                        "rechargeWay": 0,
                        "status": 1
                    },
                    {
                        "businessName": "向钱冲",
                        "certificate": "http://qiniu.youjing.tech/随便花logo.png",
                        "id": 2,
                        "outOrder": "879879kjj",
                        "payMoney": 11000,
                        "payTime": "2019-07-02 22:36:40",
                        "pid": 10001,
                        "plateformName": "士强",
                        "rechargeWay": 2,
                        "status": 1
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
                "size": 2,
                "startRow": 1,
                "total": 2
            }
        },
        "msg": "成功"
    }
   ```
   
2、商户后台查询自己的消费记录
   * URL ：POST /v1.0/api/billing/queryPlateformConsumption
   * Param:
    
   ``` json  
    {
        "beginTime":"2019-3-21 10:10:10",   //开始时间
        "endTime":"2019-3-29 22:10:10",     //结束时间
        "pageNum":1,
        "pageSize":10
    }
   ```

   * Result
   ``` json  
    {
        "code": 200,
        "data": {
            "count": {
                "balance": 19500,
                "callTimes": 50,
                "consumeMoney": 500,
                "reportPrice": 10,
                "riskPrice": 10,
                "totalMoney": 20000
            },
            "pageInfo": {
                "endRow": 2,
                "firstPage": 1,
                "hasNextPage": false,
                "hasPreviousPage": false,
                "isFirstPage": true,
                "isLastPage": true,
                "lastPage": 1,
                "list": [
                    {
                        "balance": 19700,
                        "businessName": "向钱冲",
                        "consume": 300,
                        "discount": 1,
                        "id": 1,
                        "pid": 10001,
                        "reportCallTimes": 10,
                        "reportConsume": 100,
                        "reportPrice": 10,
                        "reportTime": "2019-07-01",
                        "riskCallTimes": 20,
                        "riskConsume": 200,
                        "riskPrice": 10
                    },
                    {
                        "balance": 19500,
                        "businessName": "向钱冲",
                        "consume": 200,
                        "discount": 1,
                        "id": 2,
                        "pid": 10001,
                        "reportCallTimes": 10,
                        "reportConsume": 100,
                        "reportPrice": 10,
                        "reportTime": "2019-07-02",
                        "riskCallTimes": 10,
                        "riskConsume": 100,
                        "riskPrice": 10
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
                "size": 2,
                "startRow": 1,
                "total": 2
            }
        },
        "msg": "成功"
    }
   ```