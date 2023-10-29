#登录注册、发送验证码
1、 登陆注册接口：
   * URL: （POST）v1.0/api/app/login
   * Param:         
             
   ```json         
          {
            "phoneNumber":"" ,   //校验正则表达式("^((\\+?86)|(\\(\\+86\\)))?(13[0-9][0-9]{8}|14[0-9]{9}|15[0-9][0-9]{8}|17[0-9][0-9]{8}|18[0-9][0-9]{8})$")
            "smsCode":"1234" ,               //验证码
            "appMarket": "lyb-self",        //app应用市场
            "appVersion": "1.0.0",          //App版本号:1.1.0（安卓格式），App版本号110（IOS格式）
            "phoneNumber": "",              //手机号码
            "clientType": 1,                //客户端类型：1表示安卓，2表示IOS
            "osVersion": "8.1.0",           //os版本号
            "appName": "ysqb",              //APP名称
            "GPS": "",                      //GPS定位信息
            "deviceId": "866479048830961",  //设备Id
            "deviceName": "SM-G925F",       //设备名称：
            "channelCode": 1                //渠道编码 ：APP端固定值1
           }
   ```  
         
   * Result:  
   
   ```json    
       {
           "code": 200,
            "msg": "登录成功",
            "data": {
                    "realName": "",                 //真实姓名
                    "phoneNumber": "18722986431",  //手机号码
                    "sex": 0,                       //'性别 0未知 1男 2女 3其他
                    "token": "App_UserMWWY eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjE1NDY5NTc2MDMsInVzZXJJZCI6MjksInVzZXJuYW1lIjoiMTg3MjI5ODY0MzAifQ.dR4MHqbTmCLxz618-OrsvvvNpaqgOgaQDKlc3lMpbGtKx0Mouk                                    EbnEy_9zCq2-e2xGmOnli0TsG7T7iXGr8pHA"
                     //token有效期：7天， 7天后需要重新登录
               }
        }
   ```
 
2、 获取短信的接口
   * URL: GET v1.0/api/sms/{phoneNumber}/{type}
   * Param:  
    
                phoneNumber=18722986431  //正则表达式("^((\\+?86)|(\\(\\+86\\)))?(13[0-9][0-9]{8}|14[0-9]{9}|15[0-9][0-9]{8}|17[0-9][0-9]{8}|18[0-9][0-9]{8})$")
                type=1                   //发送短信的类型 （1、表示登录时发送的短信）
   * Result:  
   ```json
       {
            "code": 200,
            "msg": "短信验证码发送成功！",
            "data": null
        }
   ```
   
   新短信接口：
      APP: /v1.0/api/sms/mobile/{phoneNumber} 参数： timestamp  rsa 
       rsa = MD5(timestamp + phoneNumber + "szss") 
       timestamp=当前时间戳
      落地页: /v1.0/api/sms/ldy/{phoneNumber}  timestamp rsa channelCode
      修改密码发送验证码：
       /v1.0/api/sms/pwd/{phoneNumber}  timestamp rsa 
   
3、注销
   * URL:POST /v1.0/api/app/loginout
   * Param:
    
          Authorization:""    //token值
          
   * Result:  
   ```json
           {
                "code": 200,
                "msg": "注销成功！",
                "data": null
            }
   ```       
 
4、APP密码登录接口：
   * URL: （POST）v1.0/api/app/loginNew
   * Param:         
             
   ```json         
          {
            "phoneNumber":"" ,   //校验正则表达式("^((\\+?86)|(\\(\\+86\\)))?(13[0-9][0-9]{8}|14[0-9]{9}|15[0-9][0-9]{8}|17[0-9][0-9]{8}|18[0-9][0-9]{8})$")
            "password":"zxcv1234" ,         //app登录密码
            "appMarket": "lyb-self",        //app应用市场
            "appVersion": "1.0.0",          //App版本号:1.1.0（安卓格式），App版本号110（IOS格式）
            "phoneNumber": "",              //手机号码
            "clientType": 1,                //客户端类型：1表示安卓，2表示IOS
            "osVersion": "8.1.0",           //os版本号
            "appName": "ysqb",              //APP名称
            "GPS": "",                      //GPS定位信息
            "deviceId": "866479048830961",  //设备Id
            "deviceName": "SM-G925F",       //设备名称：
            "channelCode": 1                //渠道编码 ：APP端固定值1
           }
   ```  
         
   * Result:  
   
   ```json    
       {
           "code": 200,
            "msg": "登录成功",
            "data": {
                    "realName": "",                 //真实姓名
                    "phoneNumber": "18722986431",  //手机号码
                    "sex": 0,                       //'性别 0未知 1男 2女 3其他
                    "token": "App_UserMWWY eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjE1NDY5NTc2MDMsInVzZXJJZCI6MjksInVzZXJuYW1lIjoiMTg3MjI5ODY0MzAifQ.dR4MHqbTmCLxz618-OrsvvvNpaqgOgaQDKlc3lMpbGtKx0Mouk                                    EbnEy_9zCq2-e2xGmOnli0TsG7T7iXGr8pHA"
                     //token有效期：7天， 7天后需要重新登录
               }
        }
   ```
   
5、APP用户修改密码：
   * URL: （POST）v1.0/api/app/updatePassword
   * Param:         
             
   ```json         
          {
            "phoneNumber":"" ,   //用户手机号
            "password":"zxcv1234" ,         //app登录密码
            "passwordType": "1.0.0",        //修改密码类型，0：忘记密码，1：APP用户登录进去后直接修改密码
            "smsCode": "1234"               //验证码，忘记密码时填写
           }
   ```  
         
   * Result:  
   
   成功：
   ```json    
       {
           "code": 200,
            "msg": "密码修改成功",
            "data": null
        }
   ```
   失败：
   ```json    
       {
           "code": -1,
            "msg": "参数有误",
            "data": null
        }
   ```
   
   