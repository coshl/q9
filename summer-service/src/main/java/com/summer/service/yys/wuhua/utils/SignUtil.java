package com.summer.service.yys.wuhua.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.*;
import java.util.Map.Entry;

/**
 * 签名和验证签名工具
 */
public class SignUtil {
	private static final String charsetName = "utf-8";
	public static final String signName="sign";
	private static final String resultName="result";

	/**
	 * 判断签名是否正确，必须包含sign字段
	 *
	 * @param text
	 *            JSONMap 请求字符串
	 * @param key 公钥
	 * @return boolean 验证结果，成功返回true,失败返回false
	 * @throws UnsupportedEncodingException
	 * @throws SignatureException
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeySpecException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 * @author wangzhongbin
	 * @since 1.0.0
	 */
	public static boolean isSignatureValid(String text, String key) throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, SignatureException, UnsupportedEncodingException  {
		HashMap<String,String> map = getValue(text);
		String signValue = map.get(resultName);
		String sign = map.get(signName);
		return RsaUtil.verify(signValue.getBytes(charsetName), key, sign);
//		return true;
	}

	/**
	 * 生成签名
	 * @param text
	 *            JSONMap 请求字符串
	 * @param key 公钥
	 * @return String 签名结果
	 * @throws SignatureException
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeySpecException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 * @throws UnsupportedEncodingException
	 * @since 1.0.0
	 * @author wangzhongbin
	 */
	public static String generateSignature(String text, String key) throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, SignatureException, UnsupportedEncodingException {
		HashMap<String,String> map = getValue(text);
		String signValue = map.get(resultName);
		return RsaUtil.sign(signValue.getBytes(charsetName), key);
	}

	/**
	 * convert String to TreeMap
	 *
	 * @param text
	 *            JSONMap
	 * @return TreeMap <String, String>
	 * @author wangzhongbin
	 * @since 1.0.0
	 */
	private static TreeMap<String, String> covertToTreeMap(String text) {
		TreeMap<String, String> textTree = new TreeMap<String, String>();
		Object object = JSON.parse(text);
		if (object instanceof JSONObject) {
			JSONObject jo = (JSONObject) object;
			Set<Entry<String, Object>> set = jo.entrySet();
			for (Entry<String, Object> str : set) {
				Object value = str.getValue();
				if (value != null && value instanceof String) {
					textTree.put(str.getKey(), value.toString());
				} else if (value instanceof JSONArray) {
					textTree.put(str.getKey(), JSON.toJSONString(formatJSONArray(JSON.toJSONString(value))));
				} else if (value instanceof JSONObject) {
					textTree.put(str.getKey(), JSON.toJSONString(covertToTreeMap(JSON.toJSONString(value))));
				}
			}
		}

		return textTree;
	}

	/**
	 * convert each element to TreeMap
	 *
	 * @param value
	 *            JSONList
	 * @return List<TreeMap<String, String>>
	 * @author wangzhongbin
	 * @since 1.0.0
	 */
	private static List<TreeMap<String, String>> formatJSONArray(String value) {
		JSONArray ja = JSON.parseArray(value);
		List<TreeMap<String, String>> arrayList = new ArrayList<TreeMap<String,String>>();
		for (int i = 0; i < ja.size(); i++) {
			arrayList.add(covertToTreeMap(JSON.toJSONString(ja.get(i))));
		}
		return arrayList;
	}

	/**
	 * 获取参与签名的字符串
	 *
	 * @param text
	 * @return String
	 */
	private static HashMap<String,String> getValue(String text){
		HashMap<String,String> resultMap = new HashMap<String,String>();
		TreeMap<String, String> map = covertToTreeMap(text);
		resultMap.put(signName, map.get(signName));
		map.remove(signName);// 移除上送上来的sign字段
		StringBuilder sb = new StringBuilder();
		Iterator<Entry<String, String>> iter = map.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, String> entry = iter.next();
			sb.append(entry.getValue() == null ? "" : entry.getValue());
		}
		String result = sb.toString();
		resultMap.put(resultName, result);
		return resultMap;
	}

}
