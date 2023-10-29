# 认证相关接口
1、认证中心
   * URL ：GET /v1.0/api/app/authcenter
   * Param:
       
          Authorization:""                //token值(放请求头中)
   * Result
   ``` json  
     {
         "code": 200,
         "data": {
             "authBank": 1,             //银行卡认证 （0表示未认证、1表示已认证：以下状态一样）
             "authInfo": 1,             //个人信息认真
             "authMobile": 0,           //运营商认证
             "authSesame": 1,           //芝麻认证
             "suthContacts": 1          //紧急联系人认证
         },
         "msg": "成功"
     }
   ```  
2、获取个人信息
   * URL ：GET /v1.0/api/app/upload/idcard
   * Param:
   
            Authorization:""                   //token值(放请求头中)
        
   * Result
   ``` json 
   {
       "code": 200,
       "data": {
           "headPortrait": "https://www.baidu.com/3",  //人脸头像URL
           "idCard": "123456789",                       // 身份证号
           "idCardAddress": "金绣国际",                 //身份证地址
           "idCardPeriod": "2019-08-02 - 2020-09-02",   //身份证有效期
           "idcardImgF": "https://www.baidu.com/2",     //身份证反面URL
           "idcardImgZ": "https://www.baidu.com/1",     //身份证正面url
           "realName": "张三"                           //姓名
       },
       "msg": "成功"
   }
   ```
3、上传个人信息
    * URL ：POST /v1.0/api/app/upload/idcard
    * Param:
    
       Authorization:""                //token值(放请求头中)
   ``` json  
   {
     "idcardImgZ":"https://www.baidu.com/1",          //身份证正面url            
     "idcardImgF":"https://www.baidu.com/2",          //身份证反面URL         
     "headPortrait":"https://www.baidu.com/3",         //人脸头像URL       
     "realName":"张及",                                //真实姓名
     "idCard":"123456789",                             // 身份证号
     "idCardAddress":"金绣国际",                        //身份证地址
     "idCardPeriod":"2019-08-02 - 2020-09-02",         //身份证有效期  
     "ocrOrder":"79845613"                             //OCROrder订单号
   }                              
   ```        
   * Result
   ``` json  
        {
          "code":200,
          "data":null
          "msg": "成功"
        }
   ```      
   
4、获取紧急联系人页面信息
   * URL ：GET /v1.0/api/app/contacts
   * Param:
          
             Authorization:""                //token值(放请求头中)
             
   * Result
   ``` json
        {
            "code": 200,
            "data": {
                "firstContactPhone": "18722986450",      //第一联系人手机号码
                "firstContactRelation": 1,               //第一联系人关系（0未知1父亲、2母亲、3儿子、4女儿、5配偶、6兄弟、7姐妹）
                "firstContactName":"张三" ,               //第一联系人姓名
                "secondContactPhone": "18777",           //第二联系人手机号
                "secondContactRelation": 1 ,             //与第二联系人关系（0未知1.同学2.亲戚3.同事4.朋友5.其他）
                "secondContactName":"李四",               //第二联系人姓名
                "presentAddress": "金绣",                //现居住地址
                "presentAddressDetail": "1307",          //现居住地详情地址
                "lineal_list":"" ,                       //直系亲属关系对应集合
                "other_list":""                          //其他联系人对应集合
            },
            "msg": "成功"
        }
        
   ```    
5、上传紧急联系人信息
    * URL : POST    /v1.0/api/app/save/contact   
    * Params:
            
             Authorization:""                //token值(放请求头中)
             
   ``` json         
        {
        	"presentAddress":"滨江区",                   //现居住地
        	"presentAddressDetail":"金绣国际",           //现居住地详情
        	"presentLatitude":"12.22",                   //维度
        	"presentLongitude":"13.33",                  //经度
        	"firstContactName":"小",                     //第一联系人姓名
        	"firstContactPhone":"",           //第一联系人手机号码
        	"firstContactRelation":"1",                  //与第一联系人关系（0未知1父亲、2母亲、3儿子、4女儿、5配偶、6兄弟、7姐妹）   
        	"secondContactName":"罗贤",                  //地二联系人姓名
        	"secondContactPhone":"18722986431",         //第二联系人电话
        	"secondContactRelation":"1"                 //与第二联系人关系（0未知1.同学2.亲戚3.同事4.朋友5.其他）
        }
           
   ``` 
   * Result   
    ``` json
            {
                "code": 200,
                "data": null,
                "msg": "成功"
            }
    ```   
    
6、修改认证状态接口
   * URL ：GET /v1.0/api/schedule/updateState
   * Param:
   
            phone            //手机号 String
            type             //类型 int 0 更新四要素状态 1 身份认证 2 个人信息认证 3 运营商认证 4 银行卡认证 5订单状态
            status           //状态 int  0 未认证 1 已认证
            
            
        
   * Result
   ``` json 
   {
       "code": 200,
       "data": {},
       "msg": "成功"
   }
   ```
 7 商户运营商ocr开关接口 
    * URL GET v1.0/api/moxie/mobileSelect
     
    * Result
    ``` json  
     {
         "code": "200",
         "data": "{
           auto_borrow:0.0,    0.0 默认手动   double
           mobile_switch:2,    0 不存在   1 cps ,2 yc
           ocr_switch:1,       0 不存在 1 ocr 目前使用
           }",
         "message": "新建或更新数据成功"
     }
      {
         "code": "1",
         
         "message": "失败"
     }
     ```
 8 绑卡修改接口  测试地址:192.168.3.54:8088
    * URL POST /v1.0/api/userInfo/bindUpdate
      入参非json
      user_id:用户id,列表id
      acct_name:姓名
      id_no:身份证
      mobilePhone:手机号
      card_no:银行卡号
      bank:银行名称
      branch:支行名称
      
     zfb_no:支付宝账号
    zfb_pic:支付宝二维码
       wx_no:微信账号
           wx_pic:微信二维码
            
    * Result
    ``` json  
     {
         "code": 200,
         "message": "新建或更新数据成功"
     }
      {
         "code": 809,
         "message": "失败"
     }
     ```
 9 卡信息接口 
    * URL GET /v1.0/api/userInfo/bindInfo
      入参非json
      user_id:用户id,列表id
     
            
    * Result
    ``` json  
     {
         "code": 200,
         "data":{
          acct_name:姓名
               mobilePhone:手机号
               card_no:银行卡号
               bank:银行名称
               branch:支行名称
               icCard:身份证
              zfb_no:支付宝账号
             zfb_pic:支付宝二维码
                wx_no:微信账号
                    wx_pic:微信二维码
         },
         "message": "新建或更新数据成功"
     }
      {
         "code": 809,
         "message": "失败"
     }
     ```
     