package com.summer.util;

/**
 * 关于借款订单的常量定义类
 */
public class ConstantByOrderState {

    //0:待初审(待机审)
    public static final byte ZEROSTATUS = 0;
    //1:初审驳回
    public static final byte FIRSTSTATUS = 1;
    //2初审通过,代放款
    public static final byte TWOSTATUS = 2;
    //3待人工复审
    public static final byte THREESTATUS = 3;
    //4复审驳回
    public static final byte FOURSTATUS = 4;
    //5复审通过,待放款
    public static final byte FIVESTATUS = 5;
    //6:放款中
    public static final byte SIXSTATUS = 6;
    //7:放款失败
    public static final byte SEVENSTATUS = 7;
    //8已放款，还款中
    public static final byte EIGHTSTATUS = 8;
    //9:部分还款
    public static final byte NINESTATUS = 9;
    //10:已还款
    public static final byte TENSTATUS = 10;
    //11:已逾期
    public static final byte ELEVENSTATUS = 11;
    //12:已坏账
    public static final byte TWELVESTATUS = 12;
    //13逾期已还款
    public static final byte THIRTEENSTATUS = 13;

    //0:待签约
    public static final byte FU_TWOSTATUS = -2;
    //1:待绑卡
    public static final byte FU_THREESTATUS = -3;

    public static final String BEGING_AUDIT_STATUS = "审核中";
    public static final String FIRST_AUDIT_STATUS_IS_BAN = "初审未通过";
    public static final String FIRST_AUDIT_STATUS_IS_SUCCESS = "审核中";
    public static final String REVIEW_AUDIT_STATUS = "审核中";
    public static final String REVIEW_AUDIT_STATUS_IS_SUCCESS = "审核通过";

    public static final String REVIEW_AUDIT_STATUS_IS_BAD = "审核未通过";
    public static final String OVERDUE_STATUS = "已逾期";
    public static final String OVERDUE_INFO = "已逾期";
    public static final String MAKEING_LOANS_STATUS = "打款中";
    public static final String MAKE_LOANS_STATUS_IS_BAD = "打款失败";
    public static final String MAKE_LOANS_STATUS_IS_SUCCESS = "待还款";
    public static final String MAKE_LOANS_STATUS_IS_SUCC = "打款成功";
    public static final String AUDIT_INFO_IS_SUCCESS = "审核通过";
    public static final String BEGING_AUDIT_INFO = "您已进入风控审核状态";
    /**
     * 初审驳回原因优化：不显示实际驳回原因
     */
    public static final String TRIAL_REULT_REASON = "审核未通过";
    /**
     * 初审通过信息
     */
    public static final String TRIAL_PASS_INFO = "您已进入风控复审状态";
    /**
     * 待人工复审信息
     */
    public static final String REVIEW_INFO = "您已进入风控复审状态";
    /**
     * 复审驳回信息
     */
    public static final String REVIEW_REULT_REASON = "未通过风控复审";
    /**
     * 复审通过信息
     */
    public static final String REVIEW_PASS_INFO = "恭喜您已通过复审，请耐心等待放款";
    /**
     * 复审通过,打款中
     */
    public static final String REMITTANCE_INFO = "2小时内到账指定账户，请耐心等待";
    /**
     * 复审通过,放款失败
     */
    public static final String REMITTANCE_FAILD_INFO = "";
    /**
     * 已放款，还款中
     */
    public static final String REMITTANCE_SUCC_INFO = "按时还款会提高您的专属额度，恶意逾期将收录至征信系统，记得准时还款哦！目前您距离还款日还有";
    /**
     * 待还款状态
     */
    public static final byte REPAYMENT_STATUS_ZERO = 0;
    /**
     * 部分还款
     */
    public static final byte REPAYMENT_STATUS_ONE = 1;
    /**
     * 已还款
     */
    public static final byte REPAYMENT_STATUS_SECOND = 2;
    /**
     * 已逾期
     */
    public static final byte REPAYMENT_STATUS_THREE = 3;
    /**
     * 已逾期还款
     */
    public static final byte REPAYMENT_STATUS_FORU = 4;
    /**
     * 已坏账
     */
    public static final byte REPAYMENT_STATUS_FIF = 5;
    /**
     * 已逾期还款
     */
    public static final byte REPAYMENT_STATUS_AHEAD = 6;
    //借款允许的标记
    public static final String LOAN_ALLOW_FLAG = "0";
    //个人信息未完成的标记
    public static final String BASE_UNCOMPLETE_FLAG = "1";
    //紧急联系人未完成的标记
    public static final String CONTACTS_UNAUTH_FLAG = "2";
    //运营商未认证
    public static final String MOBILE_UNAUTH = "3";
    //芝麻未认证
    public static final String ZHIMA_UNAUTHTH = "4";
    //银行卡未认证
    public static final String BANK_UNAUTHTH = "5";
    //有未完成的订单标记
    public static final String HAS_ORDER = "6";
    //默认总费率(生成订单时)
    public static final Double DEFAULT_ARP_FEE = 0.35;
    //默认服务费（生成订单时）
    public static final int DEFAULT_SERVICECHARGE = 3430;
    //默认借款利息（生成订单时）
    public static final int DEFAULT_ACCRUAL_ = 70;
    //打款成功信息
    public static final String MAKE_LOANS_STATUS_SUCC_INFO = "已成功到账";
    //订单插入数据库失败
    public static final String ORDER_PRODUCT_FAILD = "7";
    //订单插入数据库成功
    public static final String ORDER_PRODUCT_SUCC = "8";

}
