## 样例
    REQ:{
          "mobilePhone": ""
        }
    RES:{"code":200,"data":{
    "mobilePhone":""},"msg":"成功"}
## 一.畅捷支付
入参不为json,为普通请求
### 1.1 GET 绑卡发短信
    URL:
        /v1.0/api/changJiePay/bindMsg
    REQ: 
        acct_name String 开户名
        card_no String 银行卡号
        id_no String 身份证号
        mobilePhone String 手机号
        user_id String 用户id
    RES:
        code Integer 609失败 200成功 
        msg String 信息
        orderNo String 订单号 code为200时返回
### 1.2 GET 提交绑卡
    URL:
        /v1.0/api/changJiePay/bindSub
    REQ: 
        msg String 短信验证码
        user_id String 用户id
        orderNo String 绑卡发短信接口返回值
        acct_name String 开户名
        card_no String 银行卡号
        id_no String 身份证号
        mobilePhone String 手机号
        
    RES:
        code Integer 609失败 200成功 
        msg String 信息
### 1.3 GET 解绑
    URL:
        /v1.0/api/changJiePay/unbindCj
    REQ: 
        userid String 用户id
    RES:
        code Integer 609失败 200成功 
        msg String 信息
### 1.3 GET 还款短信
    URL:
        /v1.0/api/changJiePay/repayment-checkPasInfo
    REQ: 
        uid String 用户id
        id Integer 借款borrow订单id
    RES:
        code Integer 609失败 200成功 
        msg String 信息
        data Map 
            orderNum  String 订单号 code为200时返回
### 1.3 GET 还款提交
    URL:
        /v1.0/api/changJiePay/orderPay
    REQ: 
        msg String 短信验证码
        id Integer 借款borrow订单id
        orderNum String 还款短信接口返回的订单号
    RES:
        code Integer 609失败 200成功 
        msg String 信息
### 1.3 GET 续期短信
    URL:
        /v1.0/api/changJiePay/renewal-check
    REQ: 
        money Integer 续期总费用
        id Integer 借款borrow订单id
        uid String 用户id
    RES:
        code Integer 609失败 200成功 
        msg String 信息
        data Map 
            orderNum  String 订单号 code为200时返回
### 1.3 GET 续期提交
    URL:
        /v1.0/api/changJiePay/renewal-payment
    REQ: 
        msg String 短信验证码
        id String 借款borrow订单id
        orderNum String 还款短信接口返回的订单号
    RES:
        code Integer 609失败 200成功 
        msg String 信息
       
## 二.魔蝎运营商
入参不为json,为普通请求
### 1.1 POST 运营商认证
    URL:
        /v1.0/api/moxie/mobileApproveMX
    REQ: 
        mobilePhone String 手机号
        identityName String 身份证姓名
        identityNo String 身份证号码
    RES:
        uri String 运营商认证url 

### 1.2 GET 获取银行卡信息
    URL:
        /v1.0/api/changJiePay/getBankCardInfo
    REQ: 
        token 
    RES:
       name         姓名
       phone        手机号
       icCard       身份证
       bankName     开户行
       cardNum String 银行卡号
       cardType String 1:信用卡   2:借记卡,3:对公账号
       bindStatus Integer 状态 (0:未生效   1:已生效)