#APP版本更新接口
1、安卓APP获取版本信息
   * URL ：GET /v1.0/api/app/returnAndroidAppHao/{appVersion}
   * Param:
         
         appVersion:"1.0.1"   //版本号（格式：1.0.1）携带在URL地址后面 
         
   * Result      
   ```json
      {
       "apkDownloadUr":"",       //下载地址
       "isForceUpdate":"",       //是否强制更新(0非强制更新，1强制更新)
       "isUpdate":"",            //是否提示更新(0,不更新，1更新)
       "description":""          //更新说明                    
       }
   ```   
2、IOS更新访问接口获取版本信息
   * URL ：POST /v1.0/api/app/returnIosAppHao/{appVersion}
   * Param:
         
           appVersion:100  //版本号（格式：100）携带在URL地址后面       
   * Result         
   ```json
   {
    "apkDownloadUr":"",       //下载地址
    "isForceUpdate":"",       //是否强制更新(0非强制更新，1强制更新)
    "isUpdate":"",            //是否提示更新(0,不更新，1更新)
    "description":""          //更新说明                
    }
   ```  