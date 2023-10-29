### 角色管理
1. 权限列表接口（用于新增角色时选取权限）
    * URL：POST v1.0/api/role/findByAuthority
    * Param:
            Authorization:""    //token值
    * Result:
    ``` json 
    {
        "code": 200,
        "data": [
            {
                "code": "P1001",
                "createDate": 1545648830000,
                "description": null,
                "id": 1,
                "list": [],
                "modifyDate": 1545717878000,
                "name": "主页",
                "parentId": 0
            },
            {
                "code": "P1002",
                "createDate": 1545648830000,
                "description": null,
                "id": 2,
                "list": [
                    {
                        "code": "C1001",
                        "createDate": 1545648830000,
                        "description": null,
                        "id": 10,
                        "list": [],
                        "modifyDate": 1545717878000,
                        "name": "客户列表",
                        "parentId": 2
                    },
                    {
                        "code": "C1002",
                        "createDate": 1545648830000,
                        "description": null,
                        "id": 11,
                        "list": [],
                        "modifyDate": 1545717878000,
                        "name": "未认证列表",
                        "parentId": 2
                    },
                    {
                        "code": "C1003",
                        "createDate": 1545648830000,
                        "description": null,
                        "id": 12,
                        "list": [],
                        "modifyDate": 1545717878000,
                        "name": "黑白名单列表",
                        "parentId": 2
                    }
                ],
                "modifyDate": 1545717878000,
                "name": "客户管理",
                "parentId": 0
            },
            {
                "code": "P1003",
                "createDate": 1545648830000,
                "description": null,
                "id": 3,
                "list": [
                    {
                        "code": "C1004",
                        "createDate": 1545648830000,
                        "description": null,
                        "id": 13,
                        "list": [],
                        "modifyDate": 1545717878000,
                        "name": "审核列表",
                        "parentId": 3
                    }
                ],
                "modifyDate": 1545717878000,
                "name": "贷前管理",
                "parentId": 0
            },
            {
                "code": "P1004",
                "createDate": 1545648830000,
                "description": null,
                "id": 4,
                "list": [
                    {
                        "code": "C1005",
                        "createDate": 1545648830000,
                        "description": null,
                        "id": 14,
                        "list": [],
                        "modifyDate": 1545717878000,
                        "name": "放款记录",
                        "parentId": 4
                    },
                    {
                        "code": "C1006",
                        "createDate": 1545648830000,
                        "description": null,
                        "id": 15,
                        "list": [],
                        "modifyDate": 1545717878000,
                        "name": "续期记录",
                        "parentId": 4
                    },
                    {
                        "code": "C1007",
                        "createDate": 1545648830000,
                        "description": null,
                        "id": 16,
                        "list": [],
                        "modifyDate": 1545717878000,
                        "name": "待还记录",
                        "parentId": 4
                    },
                    {
                        "code": "C1008",
                        "createDate": 1545648830000,
                        "description": null,
                        "id": 17,
                        "list": [],
                        "modifyDate": 1545717878000,
                        "name": "已还记录",
                        "parentId": 4
                    }
                ],
                "modifyDate": 1545717878000,
                "name": "贷中管理",
                "parentId": 0
            },
            {
                "code": "P1005",
                "createDate": 1545648830000,
                "description": null,
                "id": 5,
                "list": [
                    {
                        "code": "C1009",
                        "createDate": 1545648830000,
                        "description": null,
                        "id": 18,
                        "list": [],
                        "modifyDate": 1545717878000,
                        "name": "减免订单",
                        "parentId": 5
                    },
                    {
                        "code": "C1010",
                        "createDate": 1545648830000,
                        "description": null,
                        "id": 19,
                        "list": [],
                        "modifyDate": 1545717878000,
                        "name": "逾期管理",
                        "parentId": 5
                    },
                    {
                        "code": "C1011",
                        "createDate": 1545648830000,
                        "description": null,
                        "id": 20,
                        "list": [],
                        "modifyDate": 1545717878000,
                        "name": "我的催收",
                        "parentId": 5
                    }
                ],
                "modifyDate": 1545717878000,
                "name": "贷后管理",
                "parentId": 0
            },
            {
                "code": "P1006",
                "createDate": 1545648830000,
                "description": null,
                "id": 6,
                "list": [
                    {
                        "code": "C1012",
                        "createDate": 1545648830000,
                        "description": null,
                        "id": 21,
                        "list": [
                            {
                                "code": "C1032",
                                "createDate": 1555228966000,
                                "description": null,
                                "id": 41,
                                "list": [],
                                "modifyDate": 1555228972000,
                                "name": "渠道添加",
                                "parentId": 21
                            }
                        ],
                        "modifyDate": 1545717878000,
                        "name": "渠道列表",
                        "parentId": 6
                    },
                    {
                        "code": "C1013",
                        "createDate": 1545648830000,
                        "description": null,
                        "id": 22,
                        "list": [],
                        "modifyDate": 1545717878000,
                        "name": "渠道数据",
                        "parentId": 6
                    },
                    {
                        "code": "C1014",
                        "createDate": 1545648830000,
                        "description": null,
                        "id": 23,
                        "list": [],
                        "modifyDate": 1545717878000,
                        "name": "渠道人员统计",
                        "parentId": 6
                    }
                ],
                "modifyDate": 1545717878000,
                "name": "渠道管理",
                "parentId": 0
            },
            {
                "code": "P1007",
                "createDate": 1545648830000,
                "description": null,
                "id": 7,
                "list": [
                    {
                        "code": "C1015",
                        "createDate": 1545648830000,
                        "description": null,
                        "id": 24,
                        "list": [],
                        "modifyDate": 1545717878000,
                        "name": "财务统计",
                        "parentId": 7
                    },
                    {
                        "code": "C1016",
                        "createDate": 1545648830000,
                        "description": null,
                        "id": 25,
                        "list": [],
                        "modifyDate": 1545717878000,
                        "name": "渠道统计",
                        "parentId": 7
                    },
                    {
                        "code": "C1017",
                        "createDate": 1545648830000,
                        "description": null,
                        "id": 26,
                        "list": [],
                        "modifyDate": 1545717878000,
                        "name": "催收业务统计",
                        "parentId": 7
                    },
                    {
                        "code": "C1018",
                        "createDate": 1545648830000,
                        "description": null,
                        "id": 27,
                        "list": [],
                        "modifyDate": 1545717878000,
                        "name": "每日放款统计",
                        "parentId": 7
                    },
                    {
                        "code": "C1019",
                        "createDate": 1545648830000,
                        "description": null,
                        "id": 28,
                        "list": [],
                        "modifyDate": 1545717878000,
                        "name": "每日还款统计",
                        "parentId": 7
                    }
                ],
                "modifyDate": 1545717878000,
                "name": "统计分析",
                "parentId": 0
            },
            {
                "code": "P1008",
                "createDate": 1545648830000,
                "description": null,
                "id": 8,
                "list": [
                    {
                        "code": "C1020",
                        "createDate": 1545648830000,
                        "description": null,
                        "id": 29,
                        "list": [],
                        "modifyDate": 1545717878000,
                        "name": "风控规则配置",
                        "parentId": 8
                    },
                    {
                        "code": "C1021",
                        "createDate": 1545648830000,
                        "description": null,
                        "id": 30,
                        "list": [],
                        "modifyDate": 1545717878000,
                        "name": "认证规则配置",
                        "parentId": 8
                    },
                    {
                        "code": "C1022",
                        "createDate": 1545648830000,
                        "description": null,
                        "id": 31,
                        "list": [],
                        "modifyDate": 1545717878000,
                        "name": "贷款规则配置",
                        "parentId": 8
                    },
                    {
                        "code": "C1023",
                        "createDate": 1545648830000,
                        "description": null,
                        "id": 32,
                        "list": [],
                        "modifyDate": 1545717878000,
                        "name": "账户管理",
                        "parentId": 8
                    },
                    {
                        "code": "C1024",
                        "createDate": 1545648830000,
                        "description": null,
                        "id": 33,
                        "list": [],
                        "modifyDate": 1545717878000,
                        "name": "角色管理",
                        "parentId": 8
                    }
                ],
                "modifyDate": 1545717878000,
                "name": "配置管理",
                "parentId": 0
            },
            {
                "code": "P1009",
                "createDate": 1545648830000,
                "description": null,
                "id": 9,
                "list": [
                    {
                        "code": "C1025",
                        "createDate": 1545648830000,
                        "description": null,
                        "id": 34,
                        "list": [],
                        "modifyDate": 1545717878000,
                        "name": "基础设置",
                        "parentId": 9
                    },
                    {
                        "code": "C1026",
                        "createDate": 1545648830000,
                        "description": null,
                        "id": 35,
                        "list": [],
                        "modifyDate": 1545717878000,
                        "name": "BANNER启动页管理",
                        "parentId": 9
                    },
                    {
                        "code": "C1027",
                        "createDate": 1545648830000,
                        "description": null,
                        "id": 36,
                        "list": [],
                        "modifyDate": 1545717878000,
                        "name": "信息管理",
                        "parentId": 9
                    },
                    {
                        "code": "C1028",
                        "createDate": 1545648830000,
                        "description": null,
                        "id": 37,
                        "list": [],
                        "modifyDate": 1545717878000,
                        "name": "消息推送",
                        "parentId": 9
                    },
                    {
                        "code": "C1029",
                        "createDate": 1545648830000,
                        "description": null,
                        "id": 38,
                        "list": [],
                        "modifyDate": 1545717878000,
                        "name": "意见反馈",
                        "parentId": 9
                    },
                    {
                        "code": "C1030",
                        "createDate": 1545648830000,
                        "description": null,
                        "id": 42,
                        "list": [],
                        "modifyDate": 1545717878000,
                        "name": "短信管理",
                        "parentId": 9
                    },
                    {
                        "code": "C1033",
                        "createDate": 1558584575000,
                        "description": null,
                        "id": 43,
                        "list": [],
                        "modifyDate": 1560692597000,
                        "name": "APP版本管理",
                        "parentId": 9
                    }
                ],
                "modifyDate": 1545717878000,
                "name": "APP管理",
                "parentId": 0
            }
        ],
        "msg": "成功"
    }
  ``` 

2. 新增角色接口
    * URL：POST v1.0/api/role/insertRole
    * Param:
                 Authorization:""    //token值
                 {
                     "authorityList":"1,2,3",   //权限id，String类型，中间用“,”隔开
                     "roleName":"XXX",          //角色名称
                     "status":0                 //角色状态 0：正常； 1：禁用；2：删除
                 }
    * Result:
    ``` json 
    {
        "code": 200,
        "data": null,
        "msg": "新建或更新数据成功"
    }

  ``` 

3.删除角色接口
    * URL：POST v1.0/api/role/deleteRole
    * Param:
                Authorization:""    //token值
                {
                     "id":1        //角色id
                }
    * Result:
    ``` json 

    {
        "code": 200,
        "data": null,
        "msg": "删除成功"
    }

  ``` 

4.修改角色接口
    * URL：POST v1.0/api/role/updateRole
    * Param:
                 Authorization:""    //token值
                 {
                     "id":1,                    //角色id
                     "authorityList":"1,2,3",   //权限id，String类型，中间用“,”隔开
                     "roleName":"测试",         //角色名称
                     "status":1                 //角色状态 0：正常； 1：禁用；2：删除
                 }
    * Result:
``` json 
{
    "code": 200,
    "data": null,
    "msg": "新建或更新数据成功"
}
``` 

5.查询角色下的权限信息接口
    * URL：POST v1.0/api/role/findRoleAuthority
    * Param:
                 Authorization:""    //token值
                 {
                     "id":1,                            //角色id
                 }
                 
    * Result:
``` json 
{
    "code": 200,
    "data": [
        {
            "authorityName": "主页",
            "code": null,
            "id": 1,
            "roleName": "超级管理员",
            "status": 0
        },
        {
            "authorityName": "客户管理",
            "code": null,
            "id": 2,
            "roleName": "超级管理员",
            "status": 0
        },
        {
            "authorityName": "贷前管理",
            "code": null,
            "id": 3,
            "roleName": "超级管理员",
            "status": 0
        },
        {
            "authorityName": "贷中管理",
            "code": null,
            "id": 4,
            "roleName": "超级管理员",
            "status": 0
        },
        {
            "authorityName": "贷后管理",
            "code": null,
            "id": 5,
            "roleName": "超级管理员",
            "status": 0
        },
        {
            "authorityName": "渠道管理",
            "code": null,
            "id": 6,
            "roleName": "超级管理员",
            "status": 0
        },
        {
            "authorityName": "统计分析",
            "code": null,
            "id": 7,
            "roleName": "超级管理员",
            "status": 0
        },
        {
            "authorityName": "配置管理",
            "code": null,
            "id": 8,
            "roleName": "超级管理员",
            "status": 0
        },
        {
            "authorityName": "APP管理",
            "code": null,
            "id": 9,
            "roleName": "超级管理员",
            "status": 0
        },
        {
            "authorityName": "客户列表",
            "code": null,
            "id": 10,
            "roleName": "超级管理员",
            "status": 0
        },
        {
            "authorityName": "未认证列表",
            "code": null,
            "id": 11,
            "roleName": "超级管理员",
            "status": 0
        },
        {
            "authorityName": "黑白名单列表",
            "code": null,
            "id": 12,
            "roleName": "超级管理员",
            "status": 0
        },
        {
            "authorityName": "审核列表",
            "code": null,
            "id": 13,
            "roleName": "超级管理员",
            "status": 0
        },
        {
            "authorityName": "放款记录",
            "code": null,
            "id": 14,
            "roleName": "超级管理员",
            "status": 0
        },
        {
            "authorityName": "续期记录",
            "code": null,
            "id": 15,
            "roleName": "超级管理员",
            "status": 0
        },
        {
            "authorityName": "待还记录",
            "code": null,
            "id": 16,
            "roleName": "超级管理员",
            "status": 0
        },
        {
            "authorityName": "已还记录",
            "code": null,
            "id": 17,
            "roleName": "超级管理员",
            "status": 0
        },
        {
            "authorityName": "减免订单",
            "code": null,
            "id": 18,
            "roleName": "超级管理员",
            "status": 0
        },
        {
            "authorityName": "逾期管理",
            "code": null,
            "id": 19,
            "roleName": "超级管理员",
            "status": 0
        },
        {
            "authorityName": "我的催收",
            "code": null,
            "id": 20,
            "roleName": "超级管理员",
            "status": 0
        },
        {
            "authorityName": "渠道列表",
            "code": null,
            "id": 21,
            "roleName": "超级管理员",
            "status": 0
        },
        {
            "authorityName": "渠道数据",
            "code": null,
            "id": 22,
            "roleName": "超级管理员",
            "status": 0
        },
        {
            "authorityName": "渠道人员统计",
            "code": null,
            "id": 23,
            "roleName": "超级管理员",
            "status": 0
        },
        {
            "authorityName": "财务统计",
            "code": null,
            "id": 24,
            "roleName": "超级管理员",
            "status": 0
        },
        {
            "authorityName": "渠道统计",
            "code": null,
            "id": 25,
            "roleName": "超级管理员",
            "status": 0
        },
        {
            "authorityName": "催收业务统计",
            "code": null,
            "id": 26,
            "roleName": "超级管理员",
            "status": 0
        },
        {
            "authorityName": "每日放款统计",
            "code": null,
            "id": 27,
            "roleName": "超级管理员",
            "status": 0
        },
        {
            "authorityName": "每日还款统计",
            "code": null,
            "id": 28,
            "roleName": "超级管理员",
            "status": 0
        },
        {
            "authorityName": "风控规则配置",
            "code": null,
            "id": 29,
            "roleName": "超级管理员",
            "status": 0
        },
        {
            "authorityName": "认证规则配置",
            "code": null,
            "id": 30,
            "roleName": "超级管理员",
            "status": 0
        },
        {
            "authorityName": "贷款规则配置",
            "code": null,
            "id": 31,
            "roleName": "超级管理员",
            "status": 0
        },
        {
            "authorityName": "账户管理",
            "code": null,
            "id": 32,
            "roleName": "超级管理员",
            "status": 0
        },
        {
            "authorityName": "角色管理",
            "code": null,
            "id": 33,
            "roleName": "超级管理员",
            "status": 0
        },
        {
            "authorityName": "基础设置",
            "code": null,
            "id": 34,
            "roleName": "超级管理员",
            "status": 0
        },
        {
            "authorityName": "BANNER启动页管理",
            "code": null,
            "id": 35,
            "roleName": "超级管理员",
            "status": 0
        },
        {
            "authorityName": "信息管理",
            "code": null,
            "id": 36,
            "roleName": "超级管理员",
            "status": 0
        },
        {
            "authorityName": "消息推送",
            "code": null,
            "id": 37,
            "roleName": "超级管理员",
            "status": 0
        },
        {
            "authorityName": "意见反馈",
            "code": null,
            "id": 38,
            "roleName": "超级管理员",
            "status": 0
        },
        {
            "authorityName": "渠道添加",
            "code": null,
            "id": 41,
            "roleName": "超级管理员",
            "status": 0
        },
        {
            "authorityName": "短信管理",
            "code": null,
            "id": 42,
            "roleName": "超级管理员",
            "status": 0
        },
        {
            "authorityName": "APP版本管理",
            "code": null,
            "id": 43,
            "roleName": "超级管理员",
            "status": 0
        }
    ],
    "msg": "成功"
}
```
