# 上传用户APP、手机短信、通讯录
1、上传用户APP应用
 * URL ：POST  /v1.0/api/app/save/software
 * Param:
 
         Authorization:""                //token值(放请求头中)
``` jsonArray  
      [
      	{
      		"appName":"",               //应用软件名称
      		"packageName":"",           //应用包名
      		"versionName":"",           应用版本名
      		"versionCode":""            应用版本号
      	},{
      		"appName":"",
      		"packageName":"",
      		"versionName":"",
      		"versionCode":""
      	}
      ]
   
```            
 * Result
 ``` json  
    {
        "code": 200,
        "data": null,
        "msg": "成功"
    }
 
 ```   
 
2、上传手机短信
 1、上传用户APP应用
  * URL ：POST  /v1.0/api/app/save/shortmsg
  * Param:
  
          Authorization:""                //token值(放请求头中)
 ``` jsonArray   
    [
    	{
    		"messagecontent":"你好，你的余额不足",    //短信内容
    		"messagedate":"2019-8-9",                //短信时间
    		"phone":"10086"                          //手机号
    	},{
    		"messagecontent":"中国移动",
    		"messagedate":"2019-8-7",
    		"phone":"10086"
    	}
    ]
 ``` 
 * Result
  ``` json 
  
  ```
  
3、上传手机通讯录
 1、上传用户APP应用
  * URL ：POST  /v1.0/api/app/save/contacts
  * Param:
  
          Authorization:""                //token值(放请求头中)
 ``` jsonArray   
    [
    	{
    		"contactName":"张三",          //联系人姓名
    		"contactPhone":"10086"          //联系人手机号码
    	},{
    	    "contactName":"李四",
    		"contactPhone":"1008611"
    	}
    ]
 ``` 
 * Result
  ``` json 
  {
      "code": 200,
      "data": null,
      "msg": "成功"
  }
  ```