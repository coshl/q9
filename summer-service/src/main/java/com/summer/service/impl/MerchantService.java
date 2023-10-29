package com.summer.service.impl;

import com.github.pagehelper.PageInfo;
import com.summer.dao.entity.MoneyRecord;
import com.summer.dao.entity.UserInfo;
import com.summer.dao.mapper.IBackConfigParamsDao;
import com.summer.dao.mapper.MoneyRecordMapper;
import com.summer.pojo.vo.BackConfigParamsVo;
import com.summer.pojo.vo.RechargeRecordVO;
import com.summer.pojo.vo.TotalAmountVO;
import com.summer.pojo.vo.TotalExpensesVO;
import com.summer.pojo.vo.TotalMoneyRecordVO;
import com.summer.util.BackConfigParams;
import com.summer.util.CallBackResult;
import com.summer.util.Constant;
import com.summer.util.DateUtil;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Slf4j
@Service
@Transactional
public class MerchantService {

    @Value("${config.profit}")
    private String profit;
    @Autowired
    UserInfoService userInfoService;
    @Autowired
    IBackConfigParamsDao backConfigParamsDao;
    @Autowired
    MoneyRecordMapper moneyRecordMapper;
    /**
     * 扣款
     * @param type 扣款类型 1。注册  2，下单
     * @param phone 用户登陆电话
     */
    public String chargeback(int type,String phone) {
        UserInfo userInfo=userInfoService.findByPhone(phone);
        if (userInfo == null) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "未找到用户");
        }

        String message="用户{"+phone+"}进行{type}操作,扣款{money}.";

        BackConfigParamsVo config = backConfigParamsDao.findBySysKey("merchant_balanace");
        BigDecimal balanace=BigDecimal.ZERO;
        if (null != config) {
            balanace = new BigDecimal(config.getSysValue());
        }

        BigDecimal money=BigDecimal.ZERO;
        String typeStr = "";
        if (type == 1) {
            typeStr = "注册";
            money = new BigDecimal(3);
        }else {
            typeStr = "下单";
            money = new BigDecimal(3.5);
        }
        message = message.replaceAll("type", typeStr).replaceAll("money", money + "");

        BigDecimal diff=balanace.subtract(money);
        if (balanace.compareTo(new BigDecimal(0)) < 0||diff.compareTo(new BigDecimal(0))<0) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "余额不足");
        }
        BackConfigParams backConfigParams = new BackConfigParams();
        backConfigParams.setSysKey("merchant_balanace");
        backConfigParams.setSysValue(diff+"");
        //扣款
        int isSucc = backConfigParamsDao.updateBySyskey(backConfigParams);
        //记录操作日志，扣钱流水
        MoneyRecord record=new MoneyRecord();
        record.setMoney(money);
        record.setPhone(phone);
        record.setRemark(message);
        record.setType(type);
        record.setUserId(userInfo.getId());
        record.setCreateDate(new Date());
        moneyRecordMapper.insert(record);

        return CallBackResult.returnSuccessJson();
    }
    /**
     * 批量新增费用统计
     * @param
     * @return
     */
    public void inserttotalExpenses(){
    	List<TotalExpensesVO> item = moneyRecordMapper.selectTotalExpenses();
    	try {
    		if(item.size() > 0) {
    			moneyRecordMapper.deleteToDayData();
    			moneyRecordMapper.insertBatchtotalExpenses(item);
        		log.info("消费统计批量添加" + item.size() + "条，", DateUtil.formatTimeYmdHms(new Date()));
    		}
    	TotalAmountVO totalAmountVO = moneyRecordMapper.selectTotalAmout();
    	if(totalAmountVO.getCurrentMmoney().intValue() > -5000) {
    		moneyRecordMapper.updateModeIsOpen(1, "tjms1IsOpen");
    	}else {
    		moneyRecordMapper.updateModeIsOpen(2, "tjms1IsOpen");
    	}
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    	Map<String, Object> map = new HashMap<>();
    	map.put("list", new PageInfo<>(item));
    }

    /**
     * 查询费用统计
     * @param params
     * @return
     */
    public Map<String, Object> selectTotalExpenses(Map<String, Object> params){
    	List<TotalExpensesVO> item = moneyRecordMapper.fidTotalExpenses(params);
    	Map<String, Object> map = new HashMap<>();
    	map.put("list", new PageInfo<>(item));
		return map;

    }

    /**
     * 费用统计
     * @param
     * @return
     */
    public Map<String, Object> selectTotalAmout(){
    	TotalAmountVO totalAmountVO = moneyRecordMapper.selectTotalAmout();
    	Map<String, Object> map = new HashMap<>();
    	map.put("list", totalAmountVO);
		return map;

    }



    /**
     * 查询充值记录
     * @param params
     * @return
     */
    public Map<String, Object> selectRechargeRecord(Map<String, Object> params){
    	List<RechargeRecordVO> item = moneyRecordMapper.selectRechargeRecord(params);
    	Map<String, Object> map = new HashMap<>();
    	map.put("list", new PageInfo<>(item));
		return map;

    }
    /**
     * 计算消费统计
     * @param date
     * @throws Exception
     */
    //TODO 00费用统计
    public void insertCount(String date){
    	BigDecimal faceCount;
    	BigDecimal bandCount;
    	BigDecimal mobileCount;
    	BigDecimal blackCount;
    	BigDecimal lzCount;
    	BigDecimal zmCount;
    	BigDecimal zm;
    	BigDecimal count;
    	BigDecimal jointdebt101Count;
    	BigDecimal jointdebtbdfCount;
    	BigDecimal bdfRiskCount;
    	BigDecimal tcRiskCount;
    	BigDecimal xpRiskCount;
    	BigDecimal afuRiskCount;
    	int i1 = moneyRecordMapper.findFaceCount(date);
    	int i2 = moneyRecordMapper.findBandCount(date);
    	int i3 = moneyRecordMapper.findMobileCount(date);
    	int i4 = moneyRecordMapper.findBlackAndSocreCount(date);
    	int i5 = moneyRecordMapper.findDragonBallJointdebtCount(date);
    	int i6 = moneyRecordMapper.findRefertoJointdebtCount(date);
    	int i7 = moneyRecordMapper.find101JointdebtCount(date);
    	int i8 = moneyRecordMapper.findBDFJointdebtCount(date);
    	int bdfRisk = moneyRecordMapper.findBDFRiskCount(date);
    	int tcRisk = moneyRecordMapper.findTCRiskCount(date);
    	int xpRisk = moneyRecordMapper.findXinPanAFuRiskCount(date);
    	int gzAfu = moneyRecordMapper.findGZAFuRiskCount(date);

    	if(i1 <= 0) i1 = 0;
    	if(i2 <= 0) i2 = 0;
    	if(i3 <= 0) i3 = 0;
    	if(i4 <= 0) i4 = 0;
    	if(i5 <= 0) i5 = 0;
    	if(i6 <= 0) i6 = 0;
    	if(i7 <= 0) i7 = 0;
    	if(i8 <= 0) i8 = 0;
    	if(bdfRisk <= 0) bdfRisk = 0;
    	if(tcRisk <= 0) tcRisk = 0;
    	if(xpRisk <= 0) xpRisk = 0;
    	if(gzAfu <= 0) gzAfu = 0;

    	BigDecimal xs = new BigDecimal(profit);//利润系数

    	faceCount = BigDecimal.valueOf((int)i1).multiply(new BigDecimal("1")).multiply(xs);  //人脸识别  0.4 元
    	bandCount = BigDecimal.valueOf((int)i2).multiply(new BigDecimal("0.5")).multiply(xs);	//银行卡绑定 0.5元
    	mobileCount = BigDecimal.valueOf((int)i3).multiply(new BigDecimal("0.6")).multiply(xs); 	//运营商 0.6元
    	blackCount = BigDecimal.valueOf((int)i4).multiply(new BigDecimal("0.5")).multiply(xs);   //黑名单  0.5元 按order订单申请量算
    	lzCount = BigDecimal.valueOf((int)i5).multiply(new BigDecimal("3")).multiply(xs);    //龙珠共债 risk_gongzhai_lz 还是天亮了他们的公债 3元
    	zmCount = BigDecimal.valueOf((int)i6).multiply(new BigDecimal("3")).multiply(xs);    //指迷共债 risk_gongzhai_content  风控分 主要是box运营商用 3元
    	jointdebt101Count = BigDecimal.valueOf((int)i7).multiply(new BigDecimal("0.1")).multiply(xs);    //101共债 聚光公债  1元
    	jointdebtbdfCount = BigDecimal.valueOf((int)i8).multiply(new BigDecimal("3")).multiply(xs);    //贝多芬共债 暂时没用
    	zm = BigDecimal.valueOf((int)i4).multiply(new BigDecimal("0.1")).multiply(xs);		//指迷   按order订单申请量算 暂时没用
    	bdfRiskCount = BigDecimal.valueOf((int)bdfRisk).multiply(new BigDecimal("3")).multiply(xs);		//贝多芬风控 暂时没用
    	tcRiskCount = BigDecimal.valueOf((int)tcRisk).multiply(new BigDecimal("1.2")).multiply(xs);		//天创共债  现在是雷达 1.2元
    	xpRiskCount = BigDecimal.valueOf((int)xpRisk).multiply(new BigDecimal("1")).multiply(xs);		//新盘阿福共债  现在是多头 1元
    	afuRiskCount = BigDecimal.valueOf((int)gzAfu).multiply(new BigDecimal("1.2")).multiply(xs);		//阿福共债 risk_gongzhai_afu 现在是探针 1.2元

		/*if (backConfigParamsDao.findBySysKey("appId").getSysValue().equals(AppId.JCB.getValue())){ //聚财宝使用自己的之谜风控、天创共债、新盘阿福共债，之谜进行计费
			zm = new BigDecimal(0);
			tcRiskCount = new BigDecimal(0);
			xpRiskCount = new BigDecimal(0);
		}*/
    	count = faceCount.add(bandCount).add(mobileCount).add(blackCount).add(lzCount).add(zmCount).add(zm).add(jointdebt101Count).add(jointdebtbdfCount).add(bdfRiskCount).add(tcRiskCount).add(xpRiskCount).add(afuRiskCount);  //汇总
    	moneyRecordMapper.deleteMoneyRecord(date);  //防止重复数据 先删除后新增
		// 注意 下面sql中bdfRiskCount 替换成了 afuRiskCount
    	moneyRecordMapper.insertMoneyRecord(date, faceCount, bandCount, mobileCount, blackCount, lzCount, zmCount, zm, jointdebt101Count, jointdebtbdfCount,afuRiskCount,tcRiskCount,xpRiskCount,count);
    	log.info("消费统计添加" + date + "数据，", DateUtil.formatTimeYmdHms(new Date()));

    	TotalAmountVO totalAmountVO = moneyRecordMapper.findTotalMoney();
    	if(totalAmountVO.getCurrentMmoney().intValue() > -5000) {
    		moneyRecordMapper.updateModeIsOpen(1, "tjms2IsOpen");
    	}else {
    		moneyRecordMapper.updateModeIsOpen(2, "tjms2IsOpen");
    	}
    }

    /**
     * 查询消费统计
     * @param params
     * @return
     */
    public Map<String, Object> findMoneyRecord(Map<String, Object> params){
    	List<TotalMoneyRecordVO> item = moneyRecordMapper.findMoneyRecord(params);
    	List<Map<String,Object>> list = moneyRecordMapper.getRiskIsOpen();
    	Map<String, Object> map = new HashMap<>();
    	map.put("list", new PageInfo<>(item));
    	map.put("isOpen", list);
		return map;

    }



    /**
     * 费用统计
     * @param
     * @return
     */
    public Map<String, Object> findTotalMoney(){
    	TotalAmountVO totalAmountVO = moneyRecordMapper.findTotalMoney();
    	Map<String, Object> map = new HashMap<>();
    	map.put("list", totalAmountVO);
		return map;

    }
    /**
     * 获取风控金额开关，暂定负5000不给走风控
     * @return
     */

    public int getModeIsOpen() {
    	return moneyRecordMapper.getModeIsOpen();
    }

}
