# 阿福
1、查询用户阿福信息并保存入数据库
   * URL ：POST /v1.0/api/afu/insertAfu
   * Param:
       
        ``` json         
        {
            "id_no":"142402199302221510",       //身份证号（必传）
            "name":"张兴盛",                    //用户姓名（必传）
            "amount_business":"0",              //机构产品类型，参考1.机构产品类型（必传）
            "mobile":"15113113377",             //用户手机号：多个用逗号分隔，至多 2 个；（必传）
            "query_reason":"LOAN_AUDIT",        //查询原，因参考 3.查询原因（必传）
            "bank_no":"6222021001128740153",    //银行卡号（非必传）
            "corp_addr_province":"山西省",      //工作单位地址：省（非必传）
            "corp_addr_city":"晋中市",         //工作单位地址：市（非必传）
            "corp_addr_county":"介休市",       //工作单位地址：县、区（非必传）
            "corp_addr_address":"义安镇田李村前街13号",  //工作单位地址：详细地址（非必传）
            "corp_name":"宜信",                   //单位名称（非必传）
            "corp_tel":"010-61300110",            //单位固话：需有-，且-前为 3-4 位数字，-后有 7-8 位数字（非必传）
            "contact_id_no":"370633197005042513", //联系人身份证号（非必传）
            "contact_mobile":"13546734637",       //联系人手机号（非必传）
            "contact_name":"啊啊",                //联系人姓名（非必传）
            "contact_type":"PARENTS",             //联系人类别，参考2.联系人类别（非必传）
            "email":"532537296qq@qq.com",         //用户电子邮箱（非必传）
            "family_province":"山西省",           //家庭地址：省（非必传）
            "family_city":"晋中市",               //家庭地址：市（非必传）
            "family_county":"介休市",             //家庭地址：县、区（非必传）
            "family_address":"义安镇田李村前街13号",//家庭地址：详细地址（非必传）
            "family_tel":"010-61300110",            //家庭固话：需有-，且-前为 3-4 位数字，-后有 7-8 位数字（非必传）
            "qq":"532537296",                       //QQ 号码（非必传）
            "pid":10001,                            //平台标识（必传）
            "uid":"15113113377"                     //客户手机号（必传）
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
2、查询用户阿福原始数据
   * URL ：GET /v1.0/api/afu/queryUserAfuData
   * Param:
   
             pid:10001          //平台标识（Long型）
             uid:13020162121    //客户手机号（Long型）
                   
   * Result
   ``` json  
    {
        "code": 200,
        "data": {
            "afReportData": "{\"code\":\"10000\",\"data\":{\"loanRecords\":[],\"queriedHistory\":{\"checkedRecords\":[{\"orgName\":\"134\",\"orgType\":\"NONE_LICENSED_CASH_LOAN\",\"queryReason\":\"LOAN_AUDIT\",\"time\":\"2019-05-13\"},{\"orgName\":\"205\",\"orgType\":\"NONE_LICENSED_CASH_LOAN\",\"queryReason\":\"LOAN_AUDIT\",\"time\":\"2019-05-10\"},{\"orgName\":\"438\",\"orgType\":\"NONE_LICENSED_CASH_LOAN\",\"queryReason\":\"LOAN_AUDIT\",\"time\":\"2019-05-09\"},{\"orgName\":\"103\",\"orgType\":\"NONE_LICENSED_CASH_LOAN\",\"queryReason\":\"LOAN_AUDIT\",\"time\":\"2019-05-07\"},{\"orgName\":\"106\",\"orgType\":\"NONE_LICENSED_CASH_LOAN\",\"queryReason\":\"LOAN_AUDIT\",\"time\":\"2019-05-06\"},{\"orgName\":\"32\",\"orgType\":\"NONE_LICENSED_CASH_LOAN\",\"queryReason\":\"LOAN_AUDIT\",\"time\":\"2019-05-03\"},{\"orgName\":\"220\",\"orgType\":\"NONE_LICENSED_CASH_LOAN\",\"queryReason\":\"LOAN_AUDIT\",\"time\":\"2019-05-02\"},{\"orgName\":\"357\",\"orgType\":\"NONE_LICENSED_CONSUMPTION_PERIOD\",\"queryReason\":\"LOAN_AUDIT\",\"time\":\"2019-05-02\"},{\"orgName\":\"480\",\"orgType\":\"NONE_LICENSED_CASH_LOAN\",\"queryReason\":\"LOAN_MANAGE\",\"time\":\"2019-04-30\"},{\"orgName\":\"381\",\"orgType\":\"NONE_LICENSED_CASH_LOAN\",\"queryReason\":\"LOAN_AUDIT\",\"time\":\"2019-04-11\"},{\"orgName\":\"283\",\"orgType\":\"NONE_LICENSED_CASH_LOAN\",\"queryReason\":\"LOAN_AUDIT\",\"time\":\"2019-04-08\"},{\"orgName\":\"25\",\"orgType\":\"NONE_LICENSED_CASH_LOAN\",\"queryReason\":\"LOAN_AUDIT\",\"time\":\"2019-04-06\"},{\"orgName\":\"468\",\"orgType\":\"NONE_LICENSED_CASH_LOAN\",\"queryReason\":\"LOAN_AUDIT\",\"time\":\"2019-04-05\"},{\"orgName\":\"195\",\"orgType\":\"NONE_LICENSED_CASH_LOAN\",\"queryReason\":\"LOAN_AUDIT\",\"time\":\"2019-03-09\"},{\"orgName\":\"386\",\"orgType\":\"NONE_LICENSED_CASH_LOAN\",\"queryReason\":\"LOAN_AUDIT\",\"time\":\"2018-12-29\"},{\"orgName\":\"318\",\"orgType\":\"NONE_LICENSED_CASH_LOAN\",\"queryReason\":\"LOAN_AUDIT\",\"time\":\"2018-12-14\"},{\"orgName\":\"487\",\"orgType\":\"NONE_LICENSED_CONSUMPTION_PERIOD\",\"queryReason\":\"LOAN_AUDIT\",\"time\":\"2018-12-13\"},{\"orgName\":\"426\",\"orgType\":\"NONE_LICENSED_CASH_LOAN\",\"queryReason\":\"LOAN_AUDIT\",\"time\":\"2018-12-11\"},{\"orgName\":\"243\",\"orgType\":\"NONE_LICENSED_CASH_LOAN\",\"queryReason\":\"LOAN_AUDIT\",\"time\":\"2018-12-05\"},{\"orgName\":\"339\",\"orgType\":\"NONE_LICENSED_CONSUMPTION_PERIOD\",\"queryReason\":\"LOAN_AUDIT\",\"time\":\"2018-12-05\"},{\"orgName\":\"199\",\"orgType\":\"P2P\",\"queryReason\":\"LOAN_AUDIT\",\"time\":\"2018-12-04\"},{\"orgName\":\"395\",\"orgType\":\"NONE_LICENSED_CASH_LOAN\",\"queryReason\":\"LOAN_AUDIT\",\"time\":\"2018-12-03\"},{\"orgName\":\"219\",\"orgType\":\"NONE_LICENSED_CASH_LOAN\",\"queryReason\":\"LOAN_AUDIT\",\"time\":\"2018-12-02\"},{\"orgName\":\"249\",\"orgType\":\"NONE_LICENSED_CASH_LOAN\",\"queryReason\":\"LOAN_AUDIT\",\"time\":\"2018-12-02\"},{\"orgName\":\"312\",\"orgType\":\"NONE_LICENSED_CONSUMER_FINANCE\",\"queryReason\":\"LOAN_AUDIT\",\"time\":\"2018-12-02\"},{\"orgName\":\"292\",\"orgType\":\"NONE_LICENSED_CASH_LOAN\",\"queryReason\":\"LOAN_AUDIT\",\"time\":\"2018-12-01\"},{\"orgName\":\"122\",\"orgType\":\"P2P\",\"queryReason\":\"LOAN_AUDIT\",\"time\":\"2018-11-29\"},{\"orgName\":\"470\",\"orgType\":\"NONE_LICENSED_CASH_LOAN\",\"queryReason\":\"LOAN_AUDIT\",\"time\":\"2018-11-29\"},{\"orgName\":\"285\",\"orgType\":\"NONE_LICENSED_CASH_LOAN\",\"queryReason\":\"LOAN_AUDIT\",\"time\":\"2018-11-25\"},{\"orgName\":\"404\",\"orgType\":\"NONE_LICENSED_CASH_LOAN\",\"queryReason\":\"LOAN_AUDIT\",\"time\":\"2018-11-25\"},{\"orgName\":\"262\",\"orgType\":\"NONE_LICENSED_CASH_LOAN\",\"queryReason\":\"LOAN_AUDIT\",\"time\":\"2018-11-24\"},{\"orgName\":\"208\",\"orgType\":\"NONE_LICENSED_CASH_LOAN\",\"queryReason\":\"LOAN_AUDIT\",\"time\":\"2018-11-22\"},{\"orgName\":\"168\",\"orgType\":\"NONE_LICENSED_CASH_LOAN\",\"queryReason\":\"LOAN_AUDIT\",\"time\":\"2018-11-20\"},{\"orgName\":\"39\",\"orgType\":\"NONE_LICENSED_CASH_LOAN\",\"queryReason\":\"LOAN_AUDIT\",\"time\":\"2018-11-19\"},{\"orgName\":\"324\",\"orgType\":\"NONE_LICENSED_CONSUMPTION_PERIOD\",\"queryReason\":\"LOAN_AUDIT\",\"time\":\"2018-11-18\"},{\"orgName\":\"43\",\"orgType\":\"NONE_LICENSED_CASH_LOAN\",\"queryReason\":\"LOAN_AUDIT\",\"time\":\"2018-11-18\"},{\"orgName\":\"364\",\"orgType\":\"P2P\",\"queryReason\":\"LOAN_AUDIT\",\"time\":\"2018-11-12\"},{\"orgName\":\"436\",\"orgType\":\"MICRO_FINANCE\",\"queryReason\":\"LOAN_AUDIT\",\"time\":\"2018-11-11\"},{\"orgName\":\"73\",\"orgType\":\"NONE_LICENSED_CASH_LOAN\",\"queryReason\":\"LOAN_AUDIT\",\"time\":\"2018-11-07\"},{\"orgName\":\"320\",\"orgType\":\"NONE_LICENSED_CONSUMER_FINANCE\",\"queryReason\":\"LOAN_AUDIT\",\"time\":\"2018-11-07\"},{\"orgName\":\"57\",\"orgType\":\"NONE_LICENSED_CASH_LOAN\",\"queryReason\":\"LOAN_AUDIT\",\"time\":\"2018-11-07\"},{\"orgName\":\"376\",\"orgType\":\"P2P\",\"queryReason\":\"LOAN_AUDIT\",\"time\":\"2018-11-06\"},{\"orgName\":\"336\",\"orgType\":\"NONE_LICENSED_CASH_LOAN\",\"queryReason\":\"LOAN_AUDIT\",\"time\":\"2018-11-06\"},{\"orgName\":\"223\",\"orgType\":\"NONE_LICENSED_CASH_LOAN\",\"queryReason\":\"LOAN_AUDIT\",\"time\":\"2018-10-30\"},{\"orgName\":\"183\",\"orgType\":\"NONE_LICENSED_CONSUMER_FINANCE\",\"queryReason\":\"LOAN_MANAGE\",\"time\":\"2018-10-29\"},{\"orgName\":\"317\",\"orgType\":\"NONE_LICENSED_CASH_LOAN\",\"queryReason\":\"LOAN_AUDIT\",\"time\":\"2018-10-29\"},{\"orgName\":\"46\",\"orgType\":\"NONE_LICENSED_CASH_LOAN\",\"queryReason\":\"LOAN_AUDIT\",\"time\":\"2018-10-24\"},{\"orgName\":\"231\",\"orgType\":\"NONE_LICENSED_CONSUMPTION_PERIOD\",\"queryReason\":\"LOAN_AUDIT\",\"time\":\"2018-10-19\"},{\"orgName\":\"444\",\"orgType\":\"NONE_LICENSED_CASH_LOAN\",\"queryReason\":\"LOAN_AUDIT\",\"time\":\"2018-10-18\"},{\"orgName\":\"255\",\"orgType\":\"NONE_LICENSED_CASH_LOAN\",\"queryReason\":\"LOAN_AUDIT\",\"time\":\"2018-10-18\"},{\"orgName\":\"0\",\"orgType\":\"NONE_LICENSED_CASH_LOAN\",\"queryReason\":\"LOAN_AUDIT\",\"time\":\"2018-10-03\"},{\"orgName\":\"82\",\"orgType\":\"P2P_CAR_LOAN\",\"queryReason\":\"LOAN_AUDIT\",\"time\":\"2018-07-06\"}],\"orgCountTotal\":114,\"otherOrgCount\":52,\"timesByCurrentOrg\":0},\"riskResults\":[]},\"flowId\":\"10484207441555456\",\"msg\":\"请求成功\",\"success\":true}",
            "afScoreData": "{\"code\":\"10000\",\"data\":{\"compositeScore\":\"\",\"decisionSuggest\":\"0\"},\"flowId\":\"10484207494246400\",\"msg\":\"请求成功\",\"success\":true}",
            "createTime": 1560095607000,
            "id": 2,
            "pid": 10001,
            "uid": 13020162121,
            "updateTime": 1560154621000
        },
        "msg": "成功"
    }
   ```

1.机构产品类型：
机构产品类型 说明
     0       小额信贷（泛指小额信贷、现金分期等小额业务，额度一般在 5 千元以下）

2.联系人类别：
风险明细 说明
SPOUSE 配偶（至多 1 个）
CHILD 子女
PARENTS 父母（至多 2 个）
COLLEAGUE 同事
RELATIVES 亲属
FRIEND 朋友
OTHER_CONTACTS 其他联系人

3.查询原因
查询原因 说明
LOAN_AUDIT 贷款审批
LOAN_MANAGE 贷后管理
CREDIT_CARD_AUDIT 信用卡审批
GUARANTEE_AUDIT 担保资格审查
PRE_GUARANTEE_AUDIT 保前审查