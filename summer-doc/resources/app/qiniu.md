# 七牛获取token
1、七牛图片上传获取token
   * URL GET:/v1.0/api/qiniu/token
   * param 
   
        Authorization:""                   //token值(放请求头中)
        
   * Result:
   ``` json 
    {
        "code": 200,
        "data": {
            "token": "",    //上传七牛云前获取的token
            "userId": 89    //用户id
        },
        "msg": "成功"
    }
   
   ```