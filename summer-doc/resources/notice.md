# 公告
1、查询
   * URL ：GET /v1.0/api/app/base/getAppBaseSetting
   * Param:
    

   * Result
   ``` json  
    {
        "code": 200,
        "data": {
            "code": "91320921MA1YUY4J7B",
            "company": "响水勤民电子商务有限公司",
            "descript": "华为口袋",
            "id": 1,
            "logo": "http://qiniu.youjing.tech/app_logo_qianlaiyunzhuan000.png",
            "name": "华为口袋",
            "notice": [
                {
                    "id": 1,
                    "linkUrl": "",
                    "noticeContent": "尾号2269，正常还款，成功提额至1050元",
                    "noticeTitle": "",
                    "noticeType": 2,
                    "noticeTypeStr": "APP首页轮播公告",
                    "status": 1
                },
                {
                    "id": 2,
                    "linkUrl": "",
                    "noticeContent": "尾号6547，成功借款1000元，申请至放款耗时3分钟",
                    "noticeTitle": "",
                    "noticeType": 2,
                    "noticeTypeStr": "APP首页轮播公告",
                    "status": 1
                },
                {
                    "id": 3,
                    "linkUrl": "",
                    "noticeContent": "尾号2265，成功借款1000元，申请至放款耗时4分钟",
                    "noticeTitle": "",
                    "noticeType": 2,
                    "noticeTypeStr": "APP首页轮播公告",
                    "status": 1
                },
                {
                    "id": 4,
                    "linkUrl": "",
                    "noticeContent": "尾号1225，正常还款，成功提额至1050元",
                    "noticeTitle": "",
                    "noticeType": 2,
                    "noticeTypeStr": "APP首页轮播公告",
                    "status": 1
                },
                {
                    "id": 5,
                    "linkUrl": "",
                    "noticeContent": "尾号6681，成功借款1000元，申请至放款耗时3分钟",
                    "noticeTitle": "",
                    "noticeType": 2,
                    "noticeTypeStr": "APP首页轮播公告",
                    "status": 1
                },
                {
                    "id": 6,
                    "linkUrl": "",
                    "noticeContent": "尾号5423，成功借款1000元，申请至放款耗时4分钟",
                    "noticeTitle": "",
                    "noticeType": 2,
                    "noticeTypeStr": "APP首页轮播公告",
                    "status": 1
                },
                {
                    "id": 7,
                    "linkUrl": "",
                    "noticeContent": "尾号3212，正常还款，成功提额至1050元",
                    "noticeTitle": "",
                    "noticeType": 2,
                    "noticeTypeStr": "APP首页轮播公告",
                    "status": 1
                },
                {
                    "id": 8,
                    "linkUrl": "",
                    "noticeContent": "尾号7634，成功借款1000元，申请至放款耗时5分钟",
                    "noticeTitle": "",
                    "noticeType": 2,
                    "noticeTypeStr": "APP首页轮播公告",
                    "status": 1
                },
                {
                    "id": 9,
                    "linkUrl": "",
                    "noticeContent": "1. 工作时间：\n    审核时间  周一到周日 9：30-21：00\n    非工作时间顺延到第二天审核~\n\n2. 贷前咨询：\n\n    客服微信号：abuw66、ss1527007 工作时间同上~非工作时间会联系不上哦~",
                    "noticeTitle": "客服联系方式与工作时间",
                    "noticeType": 1,
                    "noticeTypeStr": "消息中心公告",
                    "status": 1
                }
            ],
            "qq": "123456",
            "servicePhone": "18610075974",
            "weixin": "18610075974"
        },
        "message": "成功"
    }
   ```
   
2、新增公告
   * URL ：POST /v1.0/api/app/base/addNotice
   * Param:
    
   ``` json  
    {
        "noticeTitle":"xx公告",     //公告标题
        "noticeContent":"xxxxx",    //公告内容
        "linkUrl":"xxxxx",          //公告链接
        "noticeType":1              //公告类型(1,消息中心公告，2,APP首页轮播公告，3，弹框公告)
    }
   ```

   * Result
   ``` json  
    {
        "code": 200,
        "data": "",
        "message": "成功"
    }
   ```
   
3、修改公告
   * URL ：POST /v1.0/api/app/base/updateNotice
   * Param:
    
   ``` json  
    {
        "id",                       //公告id
        "status":1,                 //状态（0，不显示；1，显示；）
        "noticeTitle":"xx公告",     //公告标题
        "noticeContent":"xxxxx",    //公告内容
        "linkUrl":"xxxxx",          //公告链接
        "noticeType":1              //公告类型(1,消息中心公告，2,APP首页轮播公告，3，弹框公告)
    }
   ```

   * Result
   ``` json  
        {
            "code": 200,
            "data": null,
            "message": "成功"
        }
   ```
   
4、app查询弹窗公告
   * URL ：POST /v1.0/api/app/getNotice
   * Param:

   * Result
   ``` json  
    {
        "code": 200,
        "data": [
            {
                "id": 10,
                "linkUrl": "xx",                    //公告链接
                "noticeContent": "公告公告公告",    //公告内容
                "noticeTitle": "弹框公告1",         //公告标题
                "noticeType": 3,                    //公告类型(1,消息中心公告，2,APP首页轮播公告，3，弹框公告)
                "status": 1                         //状态（0，不显示；1，显示；）
            }
        ],
        "message": "成功"
    }
   ```