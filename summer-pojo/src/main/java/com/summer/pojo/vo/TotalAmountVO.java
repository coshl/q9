package com.summer.pojo.vo;

import java.math.BigDecimal;

public class TotalAmountVO {
	
	private BigDecimal currentMmoney;		//当前余额
	
	private BigDecimal totalConsumption;		//总消费金额
	
	private BigDecimal totalMoney;		//总充值金额
	
	private BigDecimal registrationUnitPrice;	//注册单价
	
	private BigDecimal unitPriceOfRiskControl;	//风控单价

	public BigDecimal getCurrentMmoney() {
		return currentMmoney;
	}

	public void setCurrentMmoney(BigDecimal currentMmoney) {
		this.currentMmoney = currentMmoney;
	}

	public BigDecimal getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(BigDecimal totalMoney) {
		this.totalMoney = totalMoney;
	}

	public BigDecimal getRegistrationUnitPrice() {
		return registrationUnitPrice;
	}

	public void setRegistrationUnitPrice(BigDecimal registrationUnitPrice) {
		this.registrationUnitPrice = registrationUnitPrice;
	}

	public BigDecimal getUnitPriceOfRiskControl() {
		return unitPriceOfRiskControl;
	}

	public void setUnitPriceOfRiskControl(BigDecimal unitPriceOfRiskControl) {
		this.unitPriceOfRiskControl = unitPriceOfRiskControl;
	}

	public BigDecimal getTotalConsumption() {
		return totalConsumption;
	}

	public void setTotalConsumption(BigDecimal totalConsumption) {
		this.totalConsumption = totalConsumption;
	}
	
	
	
	

}
