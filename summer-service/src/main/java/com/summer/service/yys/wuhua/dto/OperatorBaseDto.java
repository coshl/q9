package com.summer.service.yys.wuhua.dto;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.TreeMap;


@SuppressWarnings("serial")
public class OperatorBaseDto implements Serializable{


	private String merchantId;//机构id
	private String requestId;//请求id
	private String timestamp;//时间戳
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

	public boolean isObject(Class thisClass) {
		return thisClass.getName().equals("java.lang.Object");
	}
	final public TreeMap<String, Object> toMap() {
		TreeMap<String, Object> map = new TreeMap<String, Object>();
		Class thisClass = this.getClass();
		Field[] fields = new Field[0];
		while (!isObject(thisClass)) {
			Field[] fieldTemp = thisClass.getDeclaredFields();
			thisClass = thisClass.getSuperclass();
			fields = ArrayUtils.addAll(fields, fieldTemp);

		}
		for (Field field : fields) {
			Object obj;
			try {
				obj = field.get(this);
				if (obj != null && StringUtils.isNotEmpty(obj.toString())) {
					map.put(field.getName(), obj.toString());
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return map;
	}


}
