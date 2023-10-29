package com.summer.util.risk;


import com.summer.util.NativePlace;

import java.util.Map;

/**
 * @ClassName ： PresentUtil
 * @Description ：根据身份证查找省市区
 * @Author：
 * @Date ：2020/1/16 17:19
 * @Version ：V1.0
 **/
public class PresentUtil {

   public void tse(){
       String idNum ="";
       int nativePlaceCode=Integer.parseInt(idNum.substring(0, 6));

       Map<String, String> nativePlace = NativePlace.getNativePlace(nativePlaceCode);

       System.out.println("您所在的地区为：\n" + nativePlace);
   }

    public static void main(String[] args) {
        String idNum ="152601197906173510";
        String provinceName = IDCardUtils.getProvinceName(idNum);
        String cityName = IDCardUtils.getCityName(idNum);
        System.out.println(provinceName+"====="+cityName);


    }
}
