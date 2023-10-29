#后台用户修改密码
1、发送短信验证码
   * URL  GET /v1.0/api/sms/{phoneNumber}/5
   * Params
   * Result
   
    ``` json  
    {
        "code": 200,                   //200表示发送成功，0表示用户不存在
        "data": null,
        "msg": "短信验证码发送成功！"
    }
    ```
2、校验验证码
   * URL  POST /v1.0/api/user/sms/verify
   * Params
    
    {
    	"phoneNumber":"",
    	"smsCode":"9932"
    }
        
   * Result
   
   ``` json 
   {
       "code": 200,                //200校验成功 -1表示验证码失效或错误
       "data": null,
       "msg": "成功"
   }
   ```
   
3、修改密码
   * URL POST /v1.0/api/user/update/password
   * Params
      
    {
   	"phoneNumber":"",
   	"password":"qwer12345"
    }
    
   * Result
   ``` json
   {
       "code": 200,                    //200表示修改成功，-1表示失败
       "data": null,
       "msg": "密码修改成功"
   }
   ```
   
4、后台用户校验

  * URL GET   /v1.0/api/user/channelUser
  * Params 
      
        "phoneNumber":"18722986431"
      
   * Result
      ``` json
      {
          "code": 200,                    //200表示成功，-1表示用户不存在
          "data":{
             "type":1                      //1表示渠道方用户，2表示非渠道方用户
              }
          "msg": "成功"
      }
      ```   
5、获取催收分组列表
  * URL GET /v1.0/api/user/find/cuishou
  * Param 
  * Result
         {
             "code": 200,
             "data": [
                 {
                     "groupName": "S1",  //分组名
                     "id": 3    //id
                 },
                 {
                     "groupName": "S2",
                     "id": 4
                 }
             ],
             "msg": "成功"
         }
 
 6.删除用户信息
     * URL GET /v1.0/api/userInfo/clean    
     * Param 
        id    //用户id
     
     * Result    
     
     { "code": 200,
       "data": {}
       "msg": "删除成功"
     }
         
         
 7、修改角色登录ip
    * URL POST /v1.0/api/user/updateIp
    * Params
       
     {
    	"id":1,角色id
    	"ip":"127.0.0.1"
     }
     
    * Result
    ``` json
    {
        "code": 200,                    //200表示修改成功，-1表示失败
        "data": null,
        "msg": "修改成功"
    }
    ```
 8、查询用户密码
    * URL POST /v1.0/api/userInfo/userAuth
    * Params
        Authorization //用户token
     {
    	"id":1,             //用户id
    	"password":"123456" //系统密码
     }
     
    * Result
    ``` json
    {
        "code": 200,                      //200表示修改成功，-1表示失败
        "data": {
              "userAuth":"MTIzNH" ,  //base64加密过的密码。需要前端进行base64解密(userAuth+userName)过后展示给商户
              "userName":"F3ZXRx"
             },
        "msg": "修改成功"
    }
    ```    