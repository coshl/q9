#APP首页配置、APP版本更新接口
1、 未登陆时首页默认数据
   * URL：GET v1.0/api/app/index
   * Param:
   
                Authorization:""                   //token值(放请求头中)
               
   * Result:
   ``` json 
      {
        "code": 200,
        "data": {
            "bannerList": [                                         //banner图集合
                {
                    "reurl": "本地图片上传获得的地址",               //本地上传的地址  
                    "sort": "1",                                    //排序
                    "title": "title Test标题",                      //标题
                    "url": "www.google.com链接地址"                 //图片超链接
                },
                {
                    "reurl": "本地图片上传获得的地址",              
                    "sort": "2",
                    "title": "title Test标题",
                    "url": "www.baidu.com链接地址"
                }
            ],
            "dayInterest": 0.1,                                //日利率                           
            "expire": 7,                                       //借款期限
            "indexflag": 2,                                    //未登录时默认展示默认数据的标记                                                                                                                         
            "loanAmount": 1500,
            "messageNum": 1,
            "userLoanLogList": [
                {
                    "noticeContent": "尾号2269，正常还款，成功提额至1050元"
                },
                {
                    "noticeContent": "尾号6547，成功借款1000元，申请至放款耗时3分钟"
                },
                {
                    "noticeContent": "尾号2265，成功借款1000元，申请至放款耗时4分钟"
                },
                {
                    "noticeContent": "尾号1225，正常还款，成功提额至1050元"
                },
                {
                    "noticeContent": "尾号6681，成功借款1000元，申请至放款耗时3分钟"
                },
                {
                    "noticeContent": "尾号5423，成功借款1000元，申请至放款耗时4分钟"
                },
                {
                    "noticeContent": "尾号3212，正常还款，成功提额至1050元"
                },
                {
                    "noticeContent": "尾号7634，成功借款1000元，申请至放款耗时5分钟"
                }
            ]
        },
        "msg": "首页访问成功"
    }
   ```   
   
2、登陆过（未借款）
   ``` json
    "code": 200,
    "data": {
        "authCount": null,                          //认证总数
        "bannerList": [                            //banner图结合
            {
                "reurl": "本地图片上传获得的地址",
                "sort": "1",                                                
                "title": "title Test标题",
                "url": "www.google.com链接地址"
            },
            {
                "reurl": "本地图片上传获得的地址",
                "sort": "2",
                "title": "title Test标题",
                "url": "www.baidu.com链接地址"
            }
        ],
        "dayInterest": 0.1,                                     //日利息0.1%
        "expire": 7,                                            //借款期限
        "indexflag": 0,                                         //用户登录过但没有借款订单的标记
        "loanAmount": 1500,                                     //借款金额
        "messageNum": 1,                                        //APP右上角消息的条数
        "userLoanLogList": [                                    //轮播公告集合（）
            {
                "noticeContent": "尾号2269，正常还款，成功提额至1050元"
            },
            {
                "noticeContent": "尾号6547，成功借款1000元，申请至放款耗时3分钟"
            },
            {
                "noticeContent": "尾号2265，成功借款1000元，申请至放款耗时4分钟"
            },
            {
                "noticeContent": "尾号1225，正常还款，成功提额至1050元"
            },
            {
                "noticeContent": "尾号6681，成功借款1000元，申请至放款耗时3分钟"
            },
            {
                "noticeContent": "尾号5423，成功借款1000元，申请至放款耗时4分钟"
            },
            {
                "noticeContent": "尾号3212，正常还款，成功提额至1050元"
            },
            {
                "noticeContent": "尾号7634，成功借款1000元，申请至放款耗时5分钟"
            }
        ]
    },
    "msg": "首页访问成功"
}
   ```     
3、登录过了有借款订单
   ```json
      {
      	"code": 200,
      	"data": {
            "applyAmount": 1000.00,
      		"bannerList": [{
      				"reurl": "http://img.redocn.com/sheying/20141015/qingcheshuimiandexiaomuzhou_3237569.jpg",
      				"sort": 1,
      				"title": "title Test标题",
      				"url": "http://img.redocn.com/sheying/20141015/qingcheshuimiandexiaomuzhou_3237569.jpg"
      			},
      			{
      				"reurl": "http://img.daimg.com/uploads/allimg/121227/1-12122H31938.jpg",
      				"sort": 1,
      				"title": "title Test标题",
      				"url": "http://img.daimg.com/uploads/allimg/121227/1-12122H31938.jpg"
      			},
      			{
      				"reurl": "http://img.daimg.com/uploads/allimg/121227/1-12122H31938.jpg",
      				"sort": 1,
      				"title": "11标题",
      				"url": "http://img.daimg.com/uploads/allimg/121227/1-12122H31938.jpg"
      			}
      		],
      		"indexflag": 1,                    //有订单的标记（0表示用户登录过无订单，1表示有订单显示订单状态，2表示未登录默认首页数据，3表示命中风控过后显示审核未通过页面）
      		"messageNum": 1,
      		"orderStateList": [{
      				"nextLoanTime": null,
      				"orderInfo": "申请金额: 1500.00元 期限: 7天  服务费: 514.50元 利息： 10.50元",
      				"orderState": "申请提交 ",
      				"orderTime": "2019-03-14 11:47",
      				"sort": "1"
      			},
      			{
      				"nextLoanTime": null,               //还款倒计时、逾期天数、（有就显示，没有就不显示）
      				"orderInfo": "恭喜你通过风控审核",   //审核信息
      				"orderState": "审核通过",           //审核状态
      				"orderTime": "2019-03-09 08:00",    //时间      （有就显示，没有就不显示）
      				"sort": "2"
      			},
      			{
      				"nextLoanTime": null,
      				"orderInfo": "已成功到账光大银行(7777)",
      				"orderState": "打款成功",
      				"orderTime": "2019-03-18 18:00",
      				"sort": "3"
      			},
      			{
      				"nextLoanTime": "7",
      				"orderInfo": "距离还款日还有",
      				"orderState": "待还款",
      				"orderTime": null,
      				"sort": "4"
      			}
      		],
      		"userLoanLogList": [{
      				"noticeContent": "尾号2269，正常还款，成功提额至1050元"
      			},
      			{
      				"noticeContent": "尾号6547，成功借款1000元，申请至放款耗时3分钟"
      			},
      			{
      				"noticeContent": "尾号2265，成功借款1000元，申请至放款耗时4分钟"
      			},
      			{
      				"noticeContent": "尾号1225，正常还款，成功提额至1050元"
      			},
      			{
      				"noticeContent": "尾号6681，成功借款1000元，申请至放款耗时3分钟"
      			},
      			{
      				"noticeContent": "尾号5423，成功借款1000元，申请至放款耗时4分钟"
      			},
      			{
      				"noticeContent": "尾号3212，正常还款，成功提额至1050元"
      			},
      			{
      				"noticeContent": "尾号7634，成功借款1000元，申请至放款耗时5分钟"
      			}
      		]
      	},
      	"msg": "首页访问成功"
      }
   ```
3、订单命中风控过后
   ```json
        {
            "code": 200,
            "data": {
                "bannerList": [
                    {
                        "reurl": "http://img.redocn.com/sheying/20141015/qingcheshuimiandexiaomuzhou_3237569.jpg",
                        "sort": 1,
                        "title": "title Test标题",
                        "url": "http://img.redocn.com/sheying/20141015/qingcheshuimiandexiaomuzhou_3237569.jpg"
                    },
                    {
                        "reurl": "http://img.daimg.com/uploads/allimg/121227/1-12122H31938.jpg",
                        "sort": 1,
                        "title": "title Test标题",
                        "url": "http://img.daimg.com/uploads/allimg/121227/1-12122H31938.jpg"
                    },
                    {
                        "reurl": "http://img.daimg.com/uploads/allimg/121227/1-12122H31938.jpg",
                        "sort": 1,
                        "title": "11标题",
                        "url": "http://img.daimg.com/uploads/allimg/121227/1-12122H31938.jpg"
                    }
                ],
                "indexflag": 3,            //表示显示审核未通过标记
                "messageNum": 1,           //右上角信息条数
                "nextLoanTime": 2,         //距离下次借款申请倒计时
                "userLoanLogList": [
                    {
                        "noticeContent": "尾号2269，正常还款，成功提额至1050元"
                    },
                    {
                        "noticeContent": "尾号6547，成功借款1000元，申请至放款耗时3分钟"
                    },
                    {
                        "noticeContent": "尾号2265，成功借款1000元，申请至放款耗时4分钟"
                    },
                    {
                        "noticeContent": "尾号1225，正常还款，成功提额至1050元"
                    },
                    {
                        "noticeContent": "尾号6681，成功借款1000元，申请至放款耗时3分钟"
                    },
                    {
                        "noticeContent": "尾号5423，成功借款1000元，申请至放款耗时4分钟"
                    },
                    {
                        "noticeContent": "尾号3212，正常还款，成功提额至1050元"
                    },
                    {
                        "noticeContent": "尾号7634，成功借款1000元，申请至放款耗时5分钟"
                    }
                ]
            },
            "msg": "首页访问成功"
        } 
   ```
   

4、我的界面
   * URL :GET v1.0/api/app/myIndex
   * Param
    
        Authorization:""                   //token值(放请求头中)
   * Result
   ```json
      {
          "code": 200,
          "data": {
                  "amountAvailable": 5000,    //剩余可借金额
                  "amountMax": 10000,         //总额度
                  "phone": "尾号6430"         //手机号
                  "myFlag":1                  //1：银行卡未绑定（去绑定银行卡）2：去个人中心，3去获取银行卡信息页面
          },
          "msg": "成功"
      }
   ```  
        
   