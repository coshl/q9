package com.summer.dao.mapper;

import com.summer.dao.entity.MoneyRecord;
import com.summer.pojo.vo.RechargeRecordVO;
import com.summer.pojo.vo.TotalAmountVO;
import com.summer.pojo.vo.TotalExpensesVO;
import com.summer.pojo.vo.TotalMoneyRecordVO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface MoneyRecordMapper {

    void insert(MoneyRecord record);

    List<MoneyRecord> pageByCondition(Map<String, Object> params);

    List<TotalExpensesVO> selectTotalExpenses();

    List<TotalExpensesVO> fidTotalExpenses(Map<String, Object> params);

    TotalAmountVO selectTotalAmout();

    int insertBatchtotalExpenses(List<TotalExpensesVO> item);

    List<RechargeRecordVO> selectRechargeRecord(Map<String, Object> params);

    @Select("SELECT COUNT(0) AS s FROM info_index_info where auth_info = 1 and TO_DAYS( #{date} ) - TO_DAYS(DATE_FORMAT(auth_info_time,'%Y-%m-%d')) = 0 ")
    int findFaceCount(@Param("date")String date);

    @Select("SELECT COUNT(0) as s FROM bank_bind_info where TO_DAYS( #{date} ) - TO_DAYS(DATE_FORMAT(crate_time,'%Y-%m-%d')) = 0 ")
    int findBandCount(@Param("date")String date);

    @Select("SELECT COUNT(0) as s FROM info_index_info where auth_mobile = 1 and TO_DAYS( #{date} ) - TO_DAYS(DATE_FORMAT(auth_info_time,'%Y-%m-%d')) = 0")
    int findMobileCount(@Param("date")String date);

    @Select("SELECT COUNT(0) as s FROM order_borrow where customer_type = 0 and TO_DAYS( #{date} ) - TO_DAYS(DATE_FORMAT(create_date,'%Y-%m-%d')) = 0 ")
    int findBlackAndSocreCount(@Param("date")String date);

    @Select("SELECT COUNT(0) as s FROM risk_gongzhai_content where TO_DAYS( #{date} ) - TO_DAYS(DATE_FORMAT(create_time,'%Y-%m-%d')) = 0 ")
    int findRefertoJointdebtCount(@Param("date")String date);

    @Select("SELECT COUNT(0) as s FROM risk_gongzhai_lz where TO_DAYS( #{date} ) - TO_DAYS(DATE_FORMAT(create_time,'%Y-%m-%d')) = 0 ")
    int findDragonBallJointdebtCount(@Param("date")String date);

    @Select("SELECT COUNT(0) as s FROM risk_gz101 where TO_DAYS( #{date} ) - TO_DAYS(DATE_FORMAT(create_time,'%Y-%m-%d')) = 0 ")
    int find101JointdebtCount(@Param("date")String date);

    @Select("SELECT COUNT(0) as s FROM risk_bdf_dt where TO_DAYS( #{date} ) - TO_DAYS(DATE_FORMAT(create_time,'%Y-%m-%d')) = 0 ")
    int findBDFJointdebtCount(@Param("date")String date);

    @Select("SELECT COUNT(0) as s FROM risk_bdf_content where TO_DAYS( #{date} ) - TO_DAYS(DATE_FORMAT(create_time,'%Y-%m-%d')) = 0 ")
    int findBDFRiskCount(@Param("date")String date);

    @Select("SELECT COUNT(0) as s FROM risk_gz_tc where TO_DAYS( #{date} ) - TO_DAYS(DATE_FORMAT(create_time,'%Y-%m-%d')) = 0 ")
    int findTCRiskCount(@Param("date")String date);

    @Select("SELECT COUNT(0) as s FROM risk_gz_xpaf where TO_DAYS( #{date} ) - TO_DAYS(DATE_FORMAT(create_time,'%Y-%m-%d')) = 0 ")
    int findXinPanAFuRiskCount(@Param("date")String date);

    @Select("SELECT COUNT(0) as s FROM risk_gongzhai_afu where TO_DAYS( #{date} ) - TO_DAYS(DATE_FORMAT(create_time,'%Y-%m-%d')) = 0 ")
    int findGZAFuRiskCount(@Param("date")String date);

    @Insert("insert into total_money(time,faceCount,bandCount,mobileCount,blackCount,lzCount,zmCount,zm,oneZeroOneCount,bdfCount,bdfRiskCount,tcRiskCount,xinPanRiskCount,totalMoney) values(#{date},#{faceCount},#{bandCount},#{mobileCount},#{blackCount},#{lzCount},#{zmCount},#{zm},#{oneZeroOneCount},#{jointdebtbdfCount},#{bdfRiskCount},#{tcRiskCount},#{xpRiskCount},#{count})")
    int insertMoneyRecord(@Param("date")String date,@Param("faceCount")BigDecimal faceCount,@Param("bandCount")BigDecimal bandCount,@Param("mobileCount")BigDecimal mobileCount,@Param("blackCount")BigDecimal blackCount,@Param("lzCount")BigDecimal lzCount,@Param("zmCount")BigDecimal zmCount,@Param("zm")BigDecimal zm,@Param("oneZeroOneCount")BigDecimal oneZeroOneCount,@Param("jointdebtbdfCount")BigDecimal jointdebtbdfCount,@Param("bdfRiskCount")BigDecimal bdfRiskCount,@Param("tcRiskCount")BigDecimal tcRiskCount,@Param("xpRiskCount")BigDecimal xpRiskCount,@Param("count")BigDecimal count);

    @Delete("delete from total_money where time = #{date}")
    int deleteMoneyRecord(@Param("date")String date);

    List<TotalMoneyRecordVO> findMoneyRecord(Map<String, Object> params);

    TotalAmountVO findTotalMoney();

    @Delete("delete from total_expenses where time = DATE_FORMAT(NOW(),'%Y-%m-%d')")
    int deleteToDayData();

    @Update("update back_config_params set sys_value = #{isOpen} where sys_key = #{key}")
    void updateModeIsOpen(@Param("isOpen")int isOpen,@Param("key")String key);

    @Select("select sys_value as isOpen from back_config_params where sys_key = (select case when sys_value = 1 then 'tjms1IsOpen' else 'tjms2IsOpen' end from back_config_params where sys_key = 'tjms')")
    int getModeIsOpen();

    @Select("select sys_value as flag  from back_config_params where sys_key = 'tjms'")
    String getMode();

    @Select("select sys_name,sys_key,sys_value from back_config_params where sys_key in ('lzgz','zmgz','101gz','bdf','tcgz','xpafgz','bdffk')")
    List<Map<String,Object>> getRiskIsOpen();
}
