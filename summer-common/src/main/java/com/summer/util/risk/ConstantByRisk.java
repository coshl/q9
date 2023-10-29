package com.summer.util.risk;

/**
 * 风控规则的相关常量类
 */
public class ConstantByRisk {
    /**
     * 通讯录联系人数量低于20个
     */
    public static final String R_TXL_KEY1 = "r_txl001";
    /**
     * 通讯录命中敏感性词汇（借、贷相关个人或平台）大于10个
     */
    public static final String R_TXL_KEY2 = "r_txl002";
    /**
     * 通讯录涉及：警官  警察  警方 法官
     */
    public static final String R_TXL_KEY3 = "r_txl003";
    /**
     * 年龄小于18，大于45岁
     */
    public static final String R_LN = "r_ln001";
    /**
     * 身份证号码不合法
     */
    public static final String R_YYS1 = "r_yys001";
    /**
     * 申请人姓名+身份证出现在法院黑名单
     */
    public static final String R_YYS2 = "r_yys002";
    /**
     * 号码使用时长低于6个月
     */
    public static final String R_YYS3 = "r_yys003";
    /**
     * 近5个月话费充值都低于30元
     */
    public static final String R_YYS4 = "r_yys004";
    /**
     * 芝麻分低于570
     */
    public static final String R_ZMF = "r_zmf001";
    /**
     * 借款限制的地区
     */
    public static final String R_DQ = "r_dq001";
    /**
     * 用户未黑名单状态
     */
    public static final byte USER_STATUS_BLANK = 1;
    /**
     * 话费余额低于5元
     */
    public static final String R_YYS5 = "r_yys005";
    /**
     * 身份证号码是否与运营商数据匹配
     */
    public static final String R_YYS6 = "r_yys006";
    /**
     * 姓名是否与运营商数据匹配
     */
    public static final String R_YYS7 = "r_yys007";
    /**
     * 运营商入网时长匹配
     * */
    public static final String R_YYS8 = "r_yys008";

    public static final String R_BLACK001 = "r_black001";


}
