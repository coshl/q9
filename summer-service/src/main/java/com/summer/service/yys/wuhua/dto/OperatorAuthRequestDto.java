package com.summer.service.yys.wuhua.dto;

import java.io.Serializable;


public class OperatorAuthRequestDto extends OperatorBaseDto implements Serializable{

	/**
	 *
	 */
	protected static final long serialVersionUID = -985046796434298807L;
	protected String name;//姓名
	protected String idNumber;//身份证
	protected String mobile;//手机号
	protected String showNavbar;//是否显示页面导航条
	protected String hideName;//是否显示传递的姓名
	protected String hideIdNumber;//是否显示传递的身份证
	protected String hideMobile;//是否显示传递的手机号
	protected String callbackUrl;//页面回调URL
	protected String notifyUrl;//服务端回调URL
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIdNumber() {
		return idNumber;
	}
	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getShowNavbar() {
		return showNavbar;
	}
	public void setShowNavbar(String showNavbar) {
		this.showNavbar = showNavbar;
	}
	public String getHideName() {
		return hideName;
	}
	public void setHideName(String hideName) {
		this.hideName = hideName;
	}

	public String getHideIdNumber() {
		return hideIdNumber;
	}
	public void setHideIdNumber(String hideIdNumber) {
		this.hideIdNumber = hideIdNumber;
	}
	public String getHideMobile() {
		return hideMobile;
	}
	public void setHideMobile(String hideMobile) {
		this.hideMobile = hideMobile;
	}
	public String getCallbackUrl() {
		return callbackUrl;
	}
	public void setCallbackUrl(String callbackUrl) {
		this.callbackUrl = callbackUrl;
	}
	public String getNotifyUrl() {
		return notifyUrl;
	}
	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

}
