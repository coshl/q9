package com.summer.service.yys.wuhua.dto;

import java.io.Serializable;


public class OperatorAuthNotificationDto implements Serializable{

	/**
	 *
	 */
	protected static final long serialVersionUID = -985046796434298807L;
	private String merchantId;//机构id
	private String requestId;//传递的请求唯一标识
	private String authId;//授权id
	private String type;//通知类型
	private String status;//状态
	private String sign;//签名
	public String getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public String getAuthId() {
		return authId;
	}
	public void setAuthId(String authId) {
		this.authId = authId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}

}
