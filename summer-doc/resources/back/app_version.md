##APP版本更新功能
1.获取版本更新数据

   * URL GET:/v1.0/api/version/info
   * param 
        
   * Result:
   ``` json 
    {
        "code": 200,
        "data": {
            id                 //id
            appType            //APP类型
            currentVersion    //当前版本
            isUpdate          //是否提示更新(0,不更新，1更新)
            apkDownloadUrl    //下载地址
            forceUpdate       //是否强制更新(0非强制更新，1强制更新)
            updateDescription  //更新文案
            updateTime         //更新时间
        },
        "msg": "成功"
    }
   
   ```


2.修改版本更新数据

   * URL POST:/v1.0/api/version/update
   * param 
        id                 //id
        currentVersion    //当前版本
        isUpdate          //是否提示更新(0,不更新，1更新)
        forceUpdate       //是否强制更新(0非强制更新，1强制更新)
        apkDownloadUrl    //下载地址
        updateDescription  //更新文案
   * Result:
   ``` json 
    {
        "code": 200,
        "data": {        
        },
        "msg": "成功"
    }
   
   ```