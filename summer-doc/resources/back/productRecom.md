#产品推荐接口
1.新增推荐产品
   * URL POST:/v1.0/api/product/addProduct
   * param json
        {
          "appName":"随便花",                 //APP名称
          "logoUrl":"http://www.a.png",      //产品logo地址
          "appDescription":"最高额度1",      //产品描述
          "downUrl":"http://www.abc.com/c/12wqe" //产品下载地址（落地页注册页地址） 
         }
        
   * Result:
   ``` json 
    
        "code": 200,
        "data": {          
        },
        "msg": "添加成功"
    }
   
   ```    

2.删除推荐产品
   * URL GET:/v1.0/api/product/deleteProduct
   * param 
        id                   //产品id
        
   * Result:
   ``` json 
    
        "code": 200,
        "data": {          
        },
        "msg": "删除成功"
    }
   
   ```    

3.编辑推荐产品
   * URL POST:/v1.0/api/product/updateProduct
   * param 
               {
                  "id":1，                          //主键id
                  "appName":"随便花",                //APP名称
                  "logoUrl":"http://www.a.png",      //产品logo地址
                  "appDescription":"最高额度1",      //产品描述
                  "downUrl":"http://www.abc.com/c/12wqe" //产品下载地址（落地页注册页地址） 
                 }
        
   * Result:
   ``` json 
    
        "code": 200,
        "data": {          
        },
        "msg": "修改成功"
    }
   
   ```  

4.后台查询推荐产品
   * URL GET:/v1.0/api/product/selectProduct
   * param 
         
        
   * Result:
   ``` json 
    
        "code": 200,
        "data": [{    //数组
           "id":1，                          //主键id
           "appName":"随便花",                //APP名称
           "logoUrl":"http://www.a.png",      //产品logo地址
           "appDescription":"最高额度1",      //产品描述
           "downUrl":"http://www.abc.com/c/12wqe" //产品下载地址（落地页注册页地址）         
        }]
        "msg": "成功"
    }
   
   ```  

5.APP查询推荐产品
   * URL GET:/v1.0/api/product/selectAppProduct
   * param 
         
        
   * Result:
   ``` json 
    
        "code": 200,
        "data": [{                            //数组
           "id":1，                           //主键id
           "appName":"随便花",                //APP名称
           "logoUrl":"http://www.a.png",      //产品logo地址
           "appDescription":"最高额度1",      //产品描述
           "downUrl":"http://www.abc.com/c/12wqe" //产品下载地址（落地页注册页地址）         
        }]
        "msg": "成功"
    }
   
   ```  