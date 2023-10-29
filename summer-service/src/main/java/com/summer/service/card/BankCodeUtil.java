package com.summer.service.card;


import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dhl on 2017/5/16.
 */

public class BankCodeUtil {
    public final static Map<String, String> bankKV = new HashMap<String, String>();

    static {
        bankKV.put("中国工商银行", "ICBC");
        bankKV.put("中国农业银行", "ABC");
        bankKV.put("中国建设银行", "CCB");
        bankKV.put("中国银行", "BOC");
        bankKV.put("中国交通银行", "BCOM");
        bankKV.put("兴业银行", "CIB");
        bankKV.put("中信银行", "CITIC");
        bankKV.put("中国光大银行", "CEB");
        bankKV.put("平安银行", "PAB");
        bankKV.put("中国邮政储蓄银行", "PSBC");
        bankKV.put("上海银行", "SHB");
        bankKV.put("浦东发展银行", "SPDB");
        bankKV.put("民生银行", "CMBC");
        bankKV.put("招商银行", "CMB");
        bankKV.put("广发银行", "GDB");
        bankKV.put("华夏银行", "HXB");
        bankKV.put("杭州银行", "HZB");
        bankKV.put("北京银行", "BOB");
        bankKV.put("宁波银行", "NBCB");
        bankKV.put("江苏银行", "JSB");
        bankKV.put("浙商银行", "ZSB");
    }

    //获取fengfu支付的银行代码
    public static String getBaofooBankCode(String number) {
        String codeName = GetBank.getname(number);
        for (String code : BankCodeUtil.bankKV.keySet()
                ) {
            if (code.contains(codeName) || codeName.contains(code)) {
                return BankCodeUtil.bankKV.get(code);
            }
        }
        return null;
    }

    public static void main(String[] args) throws IOException, URISyntaxException {
        System.out.println(BankCodeUtil.getBaofooBankCode("6236681300002011488 "));


		/*System.out.println( BaofooAuthUtil.api(
				"6222020111122220000",
				"320301198502169142",
				"张宝",
				"13061966998",
				"123123412",
				"0.01",
				"",
				"",
				"1"
		) );*/
		/*System.out.println( BaofooCutPayUtil.cutApi(
				"6222020111122220000",
				"320301198502169142",
				"张宝",
				"13061966998",
				"12123123412",
				"0.01"
		) );*/
	/*	System.out.println(BaofooCutPayUtil.transApi(
"123123412",
"20170517182514",
"",
"",
""
		));*/
    }
}
