package com.summer.pojo.vo;

import java.math.BigDecimal;

public class TotalExpensesVO {
	private Integer id;
	private Integer registerCount;    //注册人数
	private Integer windControlCount;	//风控人数
	private String reportTime;		//统计时间	
	private BigDecimal registrationUnitPrice;	//注册单价
	private BigDecimal registeredConsumptionAmount;	//注册消费金额
	private BigDecimal unitPriceOfRiskControl;	//风控单价
	private BigDecimal riskControlConsumptionAmount;  //风控消费金额
	private BigDecimal totalConsumption;  //总消费
	public Integer getRegisterCount() {
		return registerCount;
	}
	public void setRegisterCount(Integer registerCount) {
		this.registerCount = registerCount;
	}
	public Integer getWindControlCount() {
		return windControlCount;
	}
	public void setWindControlCount(Integer windControlCount) {
		this.windControlCount = windControlCount;
	}
	public String getReportTime() {
		return reportTime;
	}
	public void setReportTime(String reportTime) {
		this.reportTime = reportTime;
	}
	public BigDecimal getRegistrationUnitPrice() {
		return registrationUnitPrice;
	}
	public void setRegistrationUnitPrice(BigDecimal registrationUnitPrice) {
		this.registrationUnitPrice = registrationUnitPrice;
	}
	public BigDecimal getRegisteredConsumptionAmount() {
		return registeredConsumptionAmount;
	}
	public void setRegisteredConsumptionAmount(BigDecimal registeredConsumptionAmount) {
		this.registeredConsumptionAmount = registeredConsumptionAmount;
	}
	public BigDecimal getUnitPriceOfRiskControl() {
		return unitPriceOfRiskControl;
	}
	public void setUnitPriceOfRiskControl(BigDecimal unitPriceOfRiskControl) {
		this.unitPriceOfRiskControl = unitPriceOfRiskControl;
	}
	public BigDecimal getRiskControlConsumptionAmount() {
		return riskControlConsumptionAmount;
	}
	public void setRiskControlConsumptionAmount(BigDecimal riskControlConsumptionAmount) {
		this.riskControlConsumptionAmount = riskControlConsumptionAmount;
	}
	
	public BigDecimal getTotalConsumption() {
		return totalConsumption;
	}
	public void setTotalConsumption(BigDecimal totalConsumption) {
		this.totalConsumption = totalConsumption;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	
}
