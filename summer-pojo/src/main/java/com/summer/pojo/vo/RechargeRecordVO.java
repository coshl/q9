package com.summer.pojo.vo;

import java.math.BigDecimal;

public class RechargeRecordVO {
	
	private int id;

	private String rechargetime;
	
	private BigDecimal rechargeAmount;
	
	private String rechargeStaff;

	public String getRechargetime() {
		return rechargetime;
	}

	public void setRechargetime(String rechargetime) {
		this.rechargetime = rechargetime;
	}

	public BigDecimal getRechargeAmount() {
		return rechargeAmount;
	}

	public void setRechargeAmount(BigDecimal rechargeAmount) {
		this.rechargeAmount = rechargeAmount;
	}

	public String getRechargeStaff() {
		return rechargeStaff;
	}

	public void setRechargeStaff(String rechargeStaff) {
		this.rechargeStaff = rechargeStaff;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	
	
}
