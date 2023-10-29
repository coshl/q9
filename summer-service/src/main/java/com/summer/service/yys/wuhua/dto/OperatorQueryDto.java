package com.summer.service.yys.wuhua.dto;

import java.io.Serializable;


@SuppressWarnings("serial")
public class OperatorQueryDto implements Serializable{

	private String merchantId;//机构id
	private String authId;//授权id
	private String timestamp;//时间戳
	private String sign;//签名
	public String getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}
	public String getAuthId() {
		return authId;
	}
	public void setAuthId(String authId) {
		this.authId = authId;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}


}
