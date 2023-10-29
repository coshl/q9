package com.summer.util;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Desc:
 * Created by tl on 2018/9/21
 */
public class Constant {
    /**
     * 等待风控审核的借款申请对应的状态
     */
    public static final Integer RISK_STATUS_WAIT = 1;
    /**
     * 风控审核通过的状态
     */
    public static final Integer RISK_STATUS_SUC = 10;
    /**
     * 风控审核不通过的状态
     */
    public static final Integer RISK_STATUS_FAIL = 12;
    /**
     * 等待新模型反馈结果
     */
    public static final Integer RISK_STATUS_WAIT_NEW = 14;
    /**
     * 当前页数
     */
    public final static String CURRENT_PAGE = "pageNum";
    /**
     * 每页显示多少条
     */
    public static final String PAGE_SIZE = "numPerPage";
    /**
     * 命名空间编码
     */
    public static final String NAME_SPACE = "nameSpace";
    //用户token的redis前缀
    public static final String TOKEN = "USER_";

    //默认密码
    public static final String DEFAULT_PASSWORD = "qwer1234";
    //TODO 放在配置文件中
    //默认投放链接
    public static final String DEFAULT_LINK = "http://www.yuansuqianbao.com/";
    //默认渠道后台
    public static final String DEFAULT_BACKSTAGE = "http://back.yuansuqianbao.com/backcashman/back/indexBack";

    public static final byte COLLECTOR_OPERAT_STATUS_NO = 0;
    public static final byte CUSTOMER_TYPE_NEW = 0;
    public static final byte CUSTOMER_TYPE_OLD = 1;
    public static final byte BORROW_STATUS_INIT = 0;
    public static final byte BORROW_STATUS_TRAIL_REJECT = 1;
    public static final byte BORROW_STATUS_TRAIL_PASS = 2;
    public static final byte BORROW_STATUS_REVIEW_PERSON = 3;
    // 复审机审驳回
    public static final byte BORROW_STATUS_REVIEW_REJECT = 4;
    public static final byte BORROW_STATUS_REVIEW_PASS = 5;
    public static final byte BORROW_STATUS_FKZ = 6;
    public static final byte BORROW_STATUS_FKSB = 7;
    public static final byte BORROW_STATUS_QX = -1;
    public static final byte BORROW_STATUS_PAID_OVERDUE = 13;
    public static final byte BORROW_STATUS_DIRTY = 12;
    public static final byte BORROW_STATUS_OVERDUE = 11;
    public static final byte BORROW_STATUS_PAID = 10;
    public static final byte BORROW_STATUS_PART_PAID = 9;
    public static final byte BORROW_STATUS_HKZ = 8;

    public static final byte REPAYMENT_STATUS_PAID = 2;
    public static final byte REPAYMENT_STATUS_PART_PAID = 1;
    public static final byte REPAYMENT_STATUS_INIT = 0;
    public static final byte REPAYMENT_STATUS_OVERDUE = 3;
    public static final byte REPAYMENT_STATUS_PAID_OVERDUE = 4;
    public static final byte REPAYMENT_STATUS_DIRTY = 5;
    public static final byte REPAYMENT_STATUS_PAID_FARWORD = 6;

    public static final byte RENEWAL_STATUS_PAID = 2;
    public static final byte RENEWAL_STATUS_PAING = 1;
    public static final byte RENEWAL_STATUS_DEFEAT = 3;
    public static final byte REPAYMENTDETAIL_STATUS_PAID = 2;
    public static final byte REPAYMENTDETAIL_STATUS_PAYING = 1;
    public static final byte REPAYMENTDETAIL_STATUS_DEFEAT = 3;
    public static final byte REPAYMENTDETAIL_STATUS_INIT = 0;
    public static final byte REPAYMENTDETAIL_TYPE_NORMAL = 0;
    public static final byte PAY_TYPE_UNKNOWN = 0;
    public static final byte PAY_TYPE_AUTO = 1;
    public static final byte PAY_TYPE_ONLINE_WX = 2;
    public static final byte PAY_TYPE_ONLINE_BANK = 3;
    public static final byte PAY_TYPE_ONLINE_ZFB = 4;
    public static final byte PAY_TYPE_OFFLINE_WX = 5;
    public static final byte PAY_TYPE_OFFLINE_BANK = 6;
    public static final byte PAY_TYPE_OFFLINE_ZFB = 7;
    public static final byte PAY_TYPE_ONLINE_OSDT = 8;
    public static final byte REPAYMENTDETAIL_TYPE_OVERDUE = 1;
    public static final byte REPAYMENTDETAIL_TYPE_FORWARD = 2;
    public static final byte USER_UNAUTHENTIC = 0;
    public static final byte USER_AUTHENTIC = 1;
    public static final byte UNKNOWN = 0;
    public static final byte FIRST_CONTACT_FATHER = 1;
    public static final byte FIRST_CONTACT_MOTHER = 2;
    public static final byte FIRST_CONTACT_SON = 3;
    public static final byte FIRST_CONTACT_DAUGHTER = 4;
    public static final byte FIRST_CONTACT_SPOUSE = 5;
    public static final byte FIRST_CONTACT_BROTHER = 6;
    public static final byte FIRST_CONTACT_SISTER = 7;

    public static final byte SECOND_CONTACT_STUDENT = 1;
    public static final byte SECOND_CONTACT_RELATIVE = 2;
    public static final byte SECOND_CONTACT_COLLEAGUE = 3;
    public static final byte SECOND_CONTACT_FRIEND = 4;
    public static final byte SECOND_CONTACT_OTHER = 5;
    public static final String CACHE_KEY = "CACHE_KEY";
    public static Map<Byte, String> BORROW_STATUS_MAP = new HashMap<>();
    public static Map<Byte, String> LOAN_STATUS_MAP = new HashMap<>();
    public static Map<Byte, String> USER_FRONT_MAP = new HashMap<>();
    public static Map<Byte, String> NEW_USER_FRONT_MAP = new HashMap<>();
    public static Map<Byte, String> BORROW_FRONT_MAP = new HashMap<>();
    public static Map<Byte, String> REPAY_STATUS_MAP = new HashMap<>();
    public static Map<Byte, String> AUTHENTIC_STATUS_MAP = new HashMap<>();
    public static Map<Byte, String> AUDIT_STATUS_MAP = new HashMap<>();
    public static Map<Byte, String> PAY_TYPE_MAP = new HashMap<>();
    public static Map<Byte, String> FIRST_CONTACT_TYPE_MAP = new HashMap<>();
    public static Map<Byte, String> SECOND_CONTACT_TYPE_MAP = new HashMap<>();
    public static Map<Byte, String> COLLECT_CONTACT_TYPE_MAP = new HashMap<>();
    public static Map<Byte, String> COLLECT_RELATION_TYPE_MAP = new HashMap<>();
    public static final String RELATIVIZE_TYPE_GXBTB = "GXBTB";
    public static final String RELATIVIZE_TYPE_GXBTBRPT = "GXBTBRPT";
    public static final String RELATIVIZE_TYPE_GXBZM = "GXBZM";
    public static final String RELATIVIZE_TYPE_GXBDEBIT = "GXBDEBIT";
    public static final String REPAYMENT_TYPE_OVERDUE = "逾期待还";
    public static final String REPAYMENT_TYPE_NORMAL = "正常待还";
    public static final String REPAYMENT_TYPE_RENEWAL = "续期待还";
    public static final byte REPAYMENT_TYPE_OFFLINE = 1;
    public static final byte REPAYMENT_TYPE_FORWARD = 1;

    public static final byte COLLECTION_AUDIT_PASS = 2;
    public static final byte COLLECTION_AUDIT_REJECT = 1;
    public static final byte COLLECTION_AUDIT_TODO = 0;

    public static final byte COLLECTION_TYPE_PHONE = 1;
    public static final byte COLLECTION_TYPE_MSG = 2;

    public static final byte COLLECTION_CONTACT_TYPE_EMERGE = 1;
    public static final byte COLLECTION_CONTACT_TYPE_BOOK = 2;

    public static final byte COLLECTION_CONTACT_RELATION_FATHER = 1;
    public static final byte COLLECTION_CONTACT_TYPE_MOTHER = 2;
    public static final byte COLLECTION_CONTACT_TYPE_SELF = 3;
    public static final byte COLLECTION_CONTACT_TYPE_RELATIVE = 4;
    public static final byte COLLECTION_CONTACT_TYPE_FRIEND = 5;
    public static final byte COLLECTION_CONTACT_TYPE_COLLEAGUE = 6;
    public static final byte COLLECTION_CONTACT_TYPE_OTHER = 7;

    public static final byte AUTHENTIC_INIT = 0;
    public static final byte AUTHENTIC_IDCARD = 1;
    public static final byte AUTHENTIC_BASIC = 2;
    public static final byte AUTHENTIC_MOBILE = 3;
    public static final byte AUTHENTIC_BANK = 4;
    public static final byte COLLECT_LEVEL_DAY = 3;


    public final static String XJX_OVERDUE_LEVEL_S1 = "3";// 零用宝逾期等级或催收组S1

    public final static String XJX_OVERDUE_LEVEL_S2 = "4";// 零用宝逾期等级或催收组S2

    public final static String XJX_OVERDUE_LEVEL_M1_M2 = "5";// 零用宝逾期等级或催收组M1_M2

    public final static String XJX_OVERDUE_LEVEL_M2_M3 = "6";// 零用宝逾期等级或催收组M2_M3

    public final static String XJX_OVERDUE_LEVEL_M3P = "7";// 零用宝逾期等级或催收组M3+
    public final static byte XJX_COLLECTION_ORDER_STATE_SUCCESS = 4;// 零用宝催收状态催收成功

    public final static byte XJX_COLLECTION_ORDER_STATE_PAYING = 5;// 零用宝催收状态续期（续期中不管之前怎样现在不能操作）

    public final static byte XJX_COLLECTION_ORDER_STATE_ING = 1;// 零用宝催收状态催收中

    public final static byte XJX_COLLECTION_ORDER_STATE_PROMISE = 2;// 零用宝催收状态承诺还款
    public final static byte XJX_COLLECTION_ORDER_STATE_OUTSIDE = 3;// 零用宝催收状态委外中

    public final static byte XJX_COLLECTION_ORDER_STATE_WAIT = 0;// 零用宝催收状态待催收
    public final static String XJX_COLLECTION_STATUS_MOVE_TYPE_IN = "1";// 零用宝催收状态流转类型入催

    public final static String XJX_COLLECTION_STATUS_MOVE_TYPE_CONVERT = "2";// 零用宝催收状态流转类型逾期等级转换

    public final static String XJX_COLLECTION_STATUS_MOVE_TYPE_OTHER = "3";// 零用宝催收状态流转类型转单

    public final static String XJX_COLLECTION_STATUS_MOVE_TYPE_OUTSIDE = "4";// 零用宝催收状态流转类型委外

    public final static String XJX_COLLECTION_STATUS_MOVE_TYPE_SUCCESS = "5";// 零用宝催收状态流转类型催收完成
    public static final Map<String, String> groupNameMap = new LinkedHashMap<String, String>();
    public static final Map<Byte, String> authenticNameMap = new LinkedHashMap<>();

    static {
        COLLECT_CONTACT_TYPE_MAP.put(UNKNOWN, "未知");
        COLLECT_CONTACT_TYPE_MAP.put(COLLECTION_CONTACT_TYPE_EMERGE, "紧急联系人");
        COLLECT_CONTACT_TYPE_MAP.put(COLLECTION_CONTACT_TYPE_BOOK, "通讯录联系人");

        COLLECT_RELATION_TYPE_MAP.put(UNKNOWN, "未知");
        COLLECT_RELATION_TYPE_MAP.put(COLLECTION_CONTACT_RELATION_FATHER, "父亲");
        COLLECT_RELATION_TYPE_MAP.put(COLLECTION_CONTACT_TYPE_MOTHER, "母亲");
        COLLECT_RELATION_TYPE_MAP.put(COLLECTION_CONTACT_TYPE_SELF, "本人");

        COLLECT_RELATION_TYPE_MAP.put(COLLECTION_CONTACT_TYPE_RELATIVE, "亲人");
        COLLECT_RELATION_TYPE_MAP.put(COLLECTION_CONTACT_TYPE_FRIEND, "朋友");
        COLLECT_RELATION_TYPE_MAP.put(COLLECTION_CONTACT_TYPE_COLLEAGUE, "同事");
        COLLECT_RELATION_TYPE_MAP.put(COLLECTION_CONTACT_TYPE_OTHER, "其他");


        AUDIT_STATUS_MAP.put((byte) -1, "");
        AUDIT_STATUS_MAP.put(COLLECTION_AUDIT_TODO, "待审核");
        AUDIT_STATUS_MAP.put(COLLECTION_AUDIT_REJECT, "拒绝");
        AUDIT_STATUS_MAP.put(COLLECTION_AUDIT_PASS, "通过");

        BORROW_FRONT_MAP.put((byte) -2, "待签约");
        BORROW_FRONT_MAP.put((byte) -3, "待绑卡");
        BORROW_FRONT_MAP.put((byte) -1, "取消放款");
        BORROW_FRONT_MAP.put(BORROW_STATUS_INIT, "待机审");
        BORROW_FRONT_MAP.put(BORROW_STATUS_TRAIL_PASS, "待机审");
        BORROW_FRONT_MAP.put(BORROW_STATUS_REVIEW_PERSON, "待人审");

        BORROW_FRONT_MAP.put(BORROW_STATUS_TRAIL_REJECT, "机审拒绝");
        BORROW_FRONT_MAP.put(BORROW_STATUS_REVIEW_REJECT, "机审拒绝");
        BORROW_FRONT_MAP.put(BORROW_STATUS_REVIEW_PASS, "人审拒绝");
        BORROW_FRONT_MAP.put(BORROW_STATUS_FKZ, "审核通过");

        BORROW_FRONT_MAP.put(BORROW_STATUS_FKSB, "审核通过");
        BORROW_FRONT_MAP.put(BORROW_STATUS_HKZ, "审核通过");
        BORROW_FRONT_MAP.put(BORROW_STATUS_PART_PAID, "审核通过");
        BORROW_FRONT_MAP.put(BORROW_STATUS_PAID, "审核通过");
        BORROW_FRONT_MAP.put(BORROW_STATUS_OVERDUE, "审核通过");
        BORROW_FRONT_MAP.put(BORROW_STATUS_DIRTY, "审核通过");
        BORROW_FRONT_MAP.put(BORROW_STATUS_PAID_OVERDUE, "审核通过");

        USER_FRONT_MAP.put((byte) -1, "取消放款");
        USER_FRONT_MAP.put((byte) -66, "");
        USER_FRONT_MAP.put((byte) -2, "待签约");
        USER_FRONT_MAP.put((byte) -3, "待绑卡");
        USER_FRONT_MAP.put(BORROW_STATUS_INIT, "待审核");
        USER_FRONT_MAP.put(BORROW_STATUS_TRAIL_REJECT, "审核失败");
        USER_FRONT_MAP.put(BORROW_STATUS_TRAIL_PASS, "审核中");

        USER_FRONT_MAP.put(BORROW_STATUS_REVIEW_PERSON, "待人工审核");

        USER_FRONT_MAP.put(BORROW_STATUS_REVIEW_REJECT, "审核失败");
        USER_FRONT_MAP.put(BORROW_STATUS_REVIEW_PASS, "审核失败");
        USER_FRONT_MAP.put(BORROW_STATUS_FKZ, "放款中");

        USER_FRONT_MAP.put(BORROW_STATUS_FKSB, "放款失败");

        USER_FRONT_MAP.put(BORROW_STATUS_HKZ, "待还款");

        USER_FRONT_MAP.put(BORROW_STATUS_PART_PAID, "部分还款");
        USER_FRONT_MAP.put(BORROW_STATUS_PAID, "已还款");
        USER_FRONT_MAP.put(BORROW_STATUS_OVERDUE, "逾期中");

        USER_FRONT_MAP.put(BORROW_STATUS_DIRTY, "坏账");

        USER_FRONT_MAP.put(BORROW_STATUS_PAID_OVERDUE, "已逾期还款");


        LOAN_STATUS_MAP.put((byte) -1, "取消放款");
        LOAN_STATUS_MAP.put(BORROW_STATUS_FKZ, "放款中");
        LOAN_STATUS_MAP.put(BORROW_STATUS_FKSB, "放款失败");

        NEW_USER_FRONT_MAP.put((byte) -2, "待签约");
        NEW_USER_FRONT_MAP.put((byte) -3, "待绑卡");
        NEW_USER_FRONT_MAP.put((byte) -1, "取消放款");
        NEW_USER_FRONT_MAP.put(BORROW_STATUS_INIT, "待机审");
        NEW_USER_FRONT_MAP.put(BORROW_STATUS_TRAIL_REJECT, "机审拒绝");
        NEW_USER_FRONT_MAP.put(BORROW_STATUS_TRAIL_PASS, "待机审");

        NEW_USER_FRONT_MAP.put(BORROW_STATUS_REVIEW_PERSON, "待人审");

        NEW_USER_FRONT_MAP.put(BORROW_STATUS_REVIEW_REJECT, "机审拒绝");
        NEW_USER_FRONT_MAP.put(BORROW_STATUS_REVIEW_PASS, "人审拒绝");
        NEW_USER_FRONT_MAP.put(BORROW_STATUS_FKZ, "放款中");

        NEW_USER_FRONT_MAP.put(BORROW_STATUS_FKSB, "放款失败");

        NEW_USER_FRONT_MAP.put(BORROW_STATUS_HKZ, "待还款");

        NEW_USER_FRONT_MAP.put(BORROW_STATUS_PART_PAID, "部分还款");
        NEW_USER_FRONT_MAP.put(BORROW_STATUS_PAID, "正常已还");
        NEW_USER_FRONT_MAP.put(BORROW_STATUS_OVERDUE, "已逾期");

        NEW_USER_FRONT_MAP.put(BORROW_STATUS_DIRTY, "坏账");

        NEW_USER_FRONT_MAP.put(BORROW_STATUS_PAID_OVERDUE, "逾期已还");


        BORROW_STATUS_MAP.put(BORROW_STATUS_REVIEW_PERSON, "待人工复审");
        BORROW_STATUS_MAP.put(BORROW_STATUS_REVIEW_REJECT, "复审驳回");
        BORROW_STATUS_MAP.put(BORROW_STATUS_REVIEW_PASS, "复审驳回");

        AUTHENTIC_STATUS_MAP.put(AUTHENTIC_BANK, "已认证");

        AUTHENTIC_STATUS_MAP.put(AUTHENTIC_MOBILE, "部分认证");
        AUTHENTIC_STATUS_MAP.put(AUTHENTIC_BASIC, "部分认证");
        AUTHENTIC_STATUS_MAP.put(AUTHENTIC_IDCARD, "部分认证");

        AUTHENTIC_STATUS_MAP.put(AUTHENTIC_INIT, "未认证");

        REPAY_STATUS_MAP.put(REPAYMENT_STATUS_PART_PAID, "部分还款");

        REPAY_STATUS_MAP.put(REPAYMENT_STATUS_PAID, "已还款");
        REPAY_STATUS_MAP.put(REPAYMENT_STATUS_PAID_OVERDUE, "已还款");
        REPAY_STATUS_MAP.put(REPAYMENT_STATUS_PAID_FARWORD, "已还款");

        REPAY_STATUS_MAP.put(REPAYMENT_STATUS_OVERDUE, "未还款");
        REPAY_STATUS_MAP.put(REPAYMENT_STATUS_INIT, "未还款");
        REPAY_STATUS_MAP.put(REPAYMENT_STATUS_DIRTY, "未还款");

        PAY_TYPE_MAP.put(PAY_TYPE_UNKNOWN, "未知");
        PAY_TYPE_MAP.put(PAY_TYPE_AUTO, "代扣");
        PAY_TYPE_MAP.put(PAY_TYPE_ONLINE_WX, "微信线上");

        PAY_TYPE_MAP.put(PAY_TYPE_ONLINE_BANK, "银行卡线上");
        PAY_TYPE_MAP.put(PAY_TYPE_ONLINE_ZFB, "支付宝线上");
        PAY_TYPE_MAP.put(PAY_TYPE_OFFLINE_WX, "微信线下");
        PAY_TYPE_MAP.put(PAY_TYPE_OFFLINE_BANK, "银行卡线下");
        PAY_TYPE_MAP.put(PAY_TYPE_OFFLINE_ZFB, "支付宝线下");

        groupNameMap.put(XJX_OVERDUE_LEVEL_S1, "S1");
        groupNameMap.put(XJX_OVERDUE_LEVEL_S2, "S2");
        groupNameMap.put(XJX_OVERDUE_LEVEL_M1_M2, "M1-M2");
        groupNameMap.put(XJX_OVERDUE_LEVEL_M2_M3, "M2-M3");
        groupNameMap.put(XJX_OVERDUE_LEVEL_M3P, "M3+");
        authenticNameMap.put(AUTHENTIC_INIT, "未认证");
        authenticNameMap.put(AUTHENTIC_IDCARD, "身份认证");
        authenticNameMap.put(AUTHENTIC_BASIC, "个人信息认证");
        authenticNameMap.put(AUTHENTIC_MOBILE, "运营商认证");
        authenticNameMap.put(AUTHENTIC_BANK, "银行卡绑定");

        SECOND_CONTACT_TYPE_MAP.put(UNKNOWN, "未知");
        FIRST_CONTACT_TYPE_MAP.put(UNKNOWN, "未知");
        FIRST_CONTACT_TYPE_MAP.put(FIRST_CONTACT_FATHER, "父亲");
        FIRST_CONTACT_TYPE_MAP.put(FIRST_CONTACT_MOTHER, "母亲");
        FIRST_CONTACT_TYPE_MAP.put(FIRST_CONTACT_SON, "儿子");

        FIRST_CONTACT_TYPE_MAP.put(FIRST_CONTACT_DAUGHTER, "女儿");
        FIRST_CONTACT_TYPE_MAP.put(FIRST_CONTACT_SPOUSE, "配偶");
        FIRST_CONTACT_TYPE_MAP.put(FIRST_CONTACT_BROTHER, "兄弟");
        FIRST_CONTACT_TYPE_MAP.put(FIRST_CONTACT_SISTER, "姐妹");

        SECOND_CONTACT_TYPE_MAP.put(SECOND_CONTACT_STUDENT, "同学");

        SECOND_CONTACT_TYPE_MAP.put(SECOND_CONTACT_RELATIVE, "亲戚");
        SECOND_CONTACT_TYPE_MAP.put(SECOND_CONTACT_COLLEAGUE, "同事");
        SECOND_CONTACT_TYPE_MAP.put(SECOND_CONTACT_FRIEND, "朋友");
        SECOND_CONTACT_TYPE_MAP.put(SECOND_CONTACT_OTHER, "其他");
    }

    /**
     * 系统参数中返回list时使用的key的后缀
     */
    public static final String SYS_CONFIG_LIST = "_LIST";
    /**
     * 基础规则对应的集合
     */
    public static String BASE_RULE = "BASE_RULE";
    /**
     * 提交服务密码标识
     */
    public static final String SUB_PASSWORD = "SUB_PASSWORD";
    /**
     * (提交查询密码)北京移动提交查询密码标识
     */
    public static final String SUB_QUERY_PASSWORD = "SUB_QUERY_PASSWORD";
    /**
     * 提交登录(或查询详单)短信验证码接口
     */
    public static final String SUB_CAPTCHA = "CAPTCHA";
    /*** UTF-8编码 */
    public static final String UTF8 = "UTF-8";
    /**
     * 提交成功，需要查询结果
     **/
    public static final Integer STATUS_INT_VALID = 1;// 有效
    public static final Integer STATUS_INT_WAIT = 3;// 处理中
    public static final byte BORROW_PAY_STATUS_DEFEAT = 2;
    public static final byte BORROW_PAY_STATUS_SUCCESS = 4;
    public static final byte BORROW_PAY_STATUS_PART_SUCCESS = 1;
    public static final byte BORROW_PAY_STATUS_WAIT = 3;
    public static final byte BORROW_PAY_STATUS_INIT = 0;
    public static final String TYPE_CLIENT_IOS = "ios";
    public static final String TYPE_CLIENT_ANDROID = "andriod";
    public static final long TOKEN_EXPIRATION_TIME = 7 * 24 * 60 * 60; //token过期时间（单位：秒）
    public static final long PHONE_CAPTCHA_EXPIRATION_TIME =  60 * 60;//手机验证码过期时间
    public static final long PHONE_CAPTCHA_EXPIRATION = 300;//手机验证码过期时间
    public static final Integer RESULT_BAD_STATUS = -1; //返回失败的code状态
    public static final String RISK_RE_PUSH_ = "RISK_RE_PUSH_"; // 重推锁
    public static final byte PHONE_CAPTCHA = 4; //手机验证码4位

    public static final byte PASSWORD_LOWEST = 6; //登录密码最低
    public static final byte PARSSWORD_HIGHEST = 20; //最高20位
    public static final byte USER_STATUS = 0; //用户状态为0时 才可用

    public static final String USER_FORBIDDEN = "用户被禁用";
    public static final String PHONE_IS_ILLEGAL = "请输入正确手机号";
    public static final String PARAM_IS_NOT_BALANK = "参数不能为空";
    public static final String PASSWORD_AUTHORITY = "请输入6-20位登录密码";
    public static final String CAPTCHA_AUTHORITY = "请输入4位手机验证码";
    public static final String CAPTCHA_IS_NOT_EXIST = "验证码失效，请重新获取验证码！";
    /**
     * 用户注册时发送验证码的后缀，APP用户修改密码时的后缀
     */
    public static final String PHONE_CAPTCHA_SUFFIX = "phoneCaptchaKeySuffix";
    public static final String APP_TOKEN_PREFIX = "App_User";
    public static final String OCR_PREFIX = "OCR_TABLE";

    public static final String PHONE_REGULAR = "^((\\+?86)|(\\(\\+86\\)))?(13[0-9][0-9]{8}|14[0-9]{9}|15[0-9][0-9]{8}|16[0-9][0-9]{8}|17[0-9][0-9]{8}|18[0-9][0-9]{8}|19[0-9][0-9]{8})$";
    public static final String PASSWORD_REGULAR = "^[a-zA-Z0-9]\\w{5,17}$";
    public static final String USER_MSG_NOT_EXIST = "登录失效，请重新登录";
    public static final String PARAM_ERROR = "参数有误";

    public static final byte AUTHENTIC_UNCOMPLETED_STATUS = 0;//未认证状态
    public static final byte AUTHENTIC_PARTCOMPLETE_STATUS = 1;//部分完成状态
    public static final byte AUTHENTIC_COMPLETED_STATUS = 2;//已完成状态
    public static final byte BORROW_ORDER_REPAYMENT_STATUS = 10; //借款订单已还款的状态
    public static final byte BORROW_ORDER_OVERDUE_REPAYMENT_STATUS = 13;//逾期还款的状态
    public static final Integer PARAM_IS_ZERO = 0; //参数为0

    public static final int BORROW_LOWEST_MONEY = 1000;//最低借款金额
    public static final int BORROW_LOWEST_PERIOD = 7; //最低借款期限
    public static final int LIST_SIZE_LENTH_ZORE = 0;//集合的长度为0
    public static final int BANK_VALID_STATUS = 1;//银行卡生效状态

    public static final Double APR_FEE = 0.3; //服务费率（如果UserMoneyRate表里没有该用户的信息）默认按这个平台配置传进来的可借额度的0.3计算服务费
    public static final Integer DOLLAR_CHANGE_PENNY = 100;//元到分的转换
    public static final String REDIS_LOAN_DATA_PREFIX = "USER_LOAN_DATA_"; //允许借款后的借款数据，临时存入Redis的key前缀
    public static final long LOAN_DATA_EXPIRATION_TIME = 30 * 60;//允许借款后的借款数据，临时存入Redis的过期时间

    public static final Integer LOAN_APR_FEE = 1000; //借款服务费率转换元
    public static final Integer ARP_FEE = 10000; //费率万分之一
    public static final int PHONE_CAPTCHA_REGISTER_TYPE = 1; //注册时发送的验证码类型
    public static final int PHONE_CAPTCHA_UPDATE_PASSWORD_TYPE = 2;//修改密码时，发送的验证码类型
    public static final int PHONE_CAPTCHA_UPDATE_PAY_PASSWORD_TYPE = 3;//忘记交易密码时的
    //落地页注册验证码
    public static final int PHONE_CAPTCHA_LUODIYE_TYPE = 4;


    public static final String PHONE_CAPTCHA_UPDATE_PASSWORD_SUFFIX = "phoneUpdatePasswordCaptchaKeySuffix";//Redis获取验证码的key的后缀（修改密码时）
    public static final String PHONE_CAPTCHA_UPDATE_PAY_PASSWORD_SUFFIX = "phoneRegisterCaptchaKeySuffix";//Redis获取验证码的key的后缀（忘记交易密码修改交易密码时）

    public static final String PLEASE_INPUT_FEEDBACK_CONTENT = "请输入反馈内容";


    public static final String INDEX_VISIT_SUCCESS = "首页访问成功";
    public static final Byte INDEX_DATA_STATUS = 1;//APP首页有效数据的状态
    public static final Byte INDEX_NOTICE_STATUS = 1;//首页公告的有效状态
    public static final Byte INDEX_NOTICE_BOX_TYPE = 3;// 弹框公告
    public static final Byte INDEX_NOTICE_CAROUSEL_TYPE = 2;// 首页借款记录类型
    public static final Byte INDEX_NOTICE_MSG_TYPE = 1;//消息中心的公告
    public static final Byte BANNER_EQUEMENT_TYPE = 1;//'设备类型，0表示PC端，1移动端，2表示其他'
    public static final Byte BANNER_CHANNEL_ID = 0;//类型(0表示banner,1表示启动页)'
    public static final Byte BANNER_STATUS = 0;//正常（0）禁用（1）

    public static final String INDEX_REDIS_DATA_KEY = "INDEX_REDIS_DATA_KEY"; //首页缓存缓存的key
    public static final Integer CENT_CHANGE_DOLLAR = 100;//分转元
    public static final String APP_VERSION = "v1.0"; //app 版本号


    public static final boolean IS_UPDATE_STATE = true;//APP版本更新的状态
    public static final boolean IS_NOT_UPDATE_STATE = false;//APP版本更新的状态

    public static final String USER_LOGINED_INDEX_CACHE_KEY = "userLoginIndexCacheKey";  //用户登录过后(没有借款订单)的首页缓存数据的key前缀
    public static final byte PART_AUTHENTIC_COMPLETED_STATUS = 1;//芝麻，个人信息、运营商、紧急联系人认证完成的状态

    /**
     * 首页未登录过默认显示标记
     */
    public static final byte INDEX_UN_LOGON_SHOW_FLAG = 2;
    /**
     * 首页用户登录过显示标记
     */
    public static final byte INDEX_LOGON_SHOW_FLAG = 1;
    /**
     * 首页用户登录过有借款订单的标记
     */
    public static final byte INDEX_LOGON_SHOW_HAS_ORDER_FLAG = 2;
    /**
     * 首页默认数量
     */
    public static final byte NOTICE_DEFAULT_COUNT = 0;
    public static final String REMIT_TIME = "打款时间";
    public static final String LAST_REPAYMENT_TIME = "上次还款时间";


    //没有有交易密码的状态
    public static final byte HAS_NOT_PAY_PASSWORD = 0;
    //有交易密码的状态
    public static final byte HAS_PAY_PASSWORD = 1;
    public static final String ALLOW_LOAN_INFO = "允许申请借款";
    public static final String BANK_IS_NOT_EXISTS = "银行卡不存在";
    public static final String PAY_PASSWORD_ERROR = "交易密码错误";
    public static final String LOAN_DATA_IS_NOT_INCONFORMITY = "数据不一致，请重新点击借款";
    public static final String LOAN_APPLY_SUCCESS = "申请成功";
    public static final String LOAN_APPLY_BAD = "申请失败";

    public static final String USER_NAME_EXISTS = "该账号未注册，请注册后再登录";
    public static final String USER_NAME_NOT_EXISTS = "账号或密码错误，请重新输入！";

    public static final String REGISTER_IS_SUCCESS = "注册成功";
    public static final String REGISTER_IS_BAD = "注册失败";
    public static final String CAPTCHA_IS_ERROR = "验证码错误";
    public static final String LOGIN_IS_SUCCESS = "登录成功";
    public static final String LOGIN_IS_BAD = "登录失败";
    public static final String PASSWORD_IS_ERROR = "账号或密码错误";
    public static final String PLEASE_BIND_BANK_CARD = "请绑定银行卡";
    public static final String HAS_UNCOMPLETE_ORDER_INFO = "您有一笔借款订单正在审核中";//"您有未完成的借款,暂时不能再借款！";
    public static final String HAS_OVERDUE_ORDER_INFO = "系统查询到您有不良记录，暂不能借款！";

    public static final String PLEASE_COMPLETE_PERSON_INFO = "请先填写个人信息";
    public static final String WITHOUT_USER_RATE_INFO = "没有该用户对应的费率信息，暂不能借款";
    public static final String LOAN_MONEY_LIMIT = "借款金额超限，您当前最高可借款金额为";
    public static final String UNCOMPLETE_REAL_AUTH = "未完成个人实名认证";
    public static final String UNCOMPLETE_PERSON_AUTH = "未完成个人信息认证";
    public static final String UNCOMPLETE_MOBILE_AUTH = "未完成手机运营商认证";
    public static final String UNCOMPLETE_EMERGENCY_AUTH = "未完成紧急联系人认证";
    public static final String ZM_IS_BLANK = "芝麻行业关注度黑名单";
    public static final String UNCOMPLETE_ZM_AUTH = "未完成芝麻分认证";
    public static final String UNCOMPLETE_ZLX_AUTH = "未完成聚信立认证";
    public static final String AUTH_INFO_AUDIT = "认证信息审核中";

    public static final String SMS_CAPTHCA_SEND_BAD = "短信验证码发送失败！";
    public static final String SMS_CAPTHCA_SEND_SUCCESS = "短信验证码发送成功！";

    public static final String SMS_CAPTHCA_TYPE_IS_ERROR = "发送短信类型错误";
    /**
     * 短信达到上限提示
     */
    public static final String SMS_CAPTCHA_NO_FAILURE = "连续发送短信次数达到上限，请24小时后重试";
    /**
     * 复贷次数类型
     */
    public static final byte REPETITION_TYPE = 2;
    /**
     * 正常还款类型
     */
    public static final byte NORMAL_INCREASE_TYPE = 0;
    /**
     * 逾期还款类型
     */
    public static final byte OVERDUE_INCREASE_TYPE = 1;

    /**
     * （0表示存储单数值型，
     */
    public static final byte SINGLE_SHUZHI_TYPE = 0;
    /**
     * 1表示存储数值范围型，
     */
    public static final byte SHUZHI_RANGE_TYPE = 1;
    /**
     * 2表示存储数组形式存储型(比如涉及敏感词汇：警官，警察),
     */
    public static final byte ARRAY_TYPE = 2;
    /**
     * 3表示直接等于strValue，
     */
    public static final byte IS_STRVALUE_TYPE = 3;
    /**
     * 4，不同类型的值的范围型：(比如通讯录与通话记录中前10位匹配号码数量小于3),
     */
    public static final byte DIFFirent_VALUE_TYPE = 4;
    /**
     * 5，表示包含某些关键字超过某个指标时 :比如通讯录命中敏感性词汇（借、贷相关个人或平台）大于10个）
     */
    public static final byte OVERPROOF_TYPE = 5;

    public static final int LESSTHAN_TYPE = 0; //小于的类型
    public static final int EQ_TYPE = 1; //等于的类型
    public static final int GREATERTHAN_TYPE = 2; //大于的类型
    /**
     * 初审驳回状态
     */
    public static final byte TRIAL_REJECT_STATUS = 1;
    /**
     * 初审通过的状态
     */
    public static final byte TRIAL_PASS_STATUS = 2;
    /**
     * 规则有效的状态
     */
    public static final byte RULE_EFFECTIVE_STATUS = 0;
    /**
     * 规则统计中命中规则的的类型
     */
    public static final byte HIT_RULE_COUNTS_TYPE = 0;
    /**
     * 规则统计通过该规则的类型
     */
    public static final byte PASS_COUNT_TYPE = 1;
    /**
     * 放款中状态
     */
    public static final byte TRIAL_LOAN_STATUS = 6;
    /**
     * 表示正常还款类型
     */
    public static final byte INCREASE_TYPE = 0;
    /**
     * 表示逾期还款类型
     */
    public static final byte OVERDUE_TYPE = 1;
    /**
     * 复贷次数类型
     */
    public static final byte REPETIT_TYPE = 2;
    public static final String PLEASE_INPUT_FEEDBACK_STATE_INFO = "请输入处理情况";

    public static final String PLEASE_INPUT_PROTOCOL_TITLE = "请输入协议标题";
    public static final String PLEASE_INPUT_PROTOCOL_CONTENT = "请输入协议内容";
    public static final String PLEASE_INPUT_PROTOCOL_NAME = "请输入姓名";
    public static final String PLEASE_INPUT_PROTOCOL_IDCARD = "请输入身份证";
    public static final String PLEASE_INPUT_PROTOCOL_PHONE = "请输入手机号";
    public static final String PLEASE_INPUT_APP_NAME = "请输入APP名称";
    public static final String PLEASE_INPUT_APP_DESCRIPT = "请输入APP描述";
    public static final String PLEASE_INPUT_SERVICE_PHONE = "请输入客服电话";
    public static final String ONLINE_FEED_SUCCESS = "在线反馈成功";
    public static final String INVALID_INSERT = "此序号banner已存在，请重新选择";

    /**
     * 提额配置有效数据的状态
     */
    public static final byte INCRESE_MONNEY_CONFIG_STATUS = 1;
    /**
     * 借款规则有效的状态
     */
    public static final int LOAN_RULE_STATUS = 1;
    /**
     * 有效的银行卡状态
     */
    public static final int USER_BANKCARD_STATUS = 1;
    /**
     * 费率乘100 借款金额除100时使用，防止直接double类型 * int 精度损失
     */
    public static final int CHANGE_MULTIPLE = 100;

    /**
     * 认证规则状态为0 表示启用该规则
     */
    public static final byte AUTH_RULE_STATUS = 0;
    /**
     * 用户黑名单状态
     */
    public static final byte USER_BLANK_STATUS = 1;
    /**
     * 正常还款的状态
     */
    public static final byte NORMAL_REPAYMENT_STATUS = 10;
    /**
     * 逾期还款天数
     */
    public static final int ORDER_LATE_DAY = 7;

    /**
     * 命中黑明单是否允许借款的状态
     */
    public static final int HIT_BLACK_STATE = 0;
    /**
     * 正常还款是否可以复贷
     */
    public static final int NOMAL_REPAYMENT_STATE = 1;

    /**
     * 还款表中正常还款状态
     */
    public static final byte NOMAL_REPAYMENT_STATES = 2;
    /**
     * 还款表中逾期还款状态
     */
    public static final int OVERDUE_REPAYMENT_STATE = 4;
    /**
     * 逾期还款，允许再借的状态
     */
    public static final int IS_ALLOW_LOAN_STATE = 1;
    /**
     * 逾期还款永远不能再借的状态
     */
    public static final int NOT_IS_ALLOW_LOAN_STATE = 0;
    /**
     * 命中风控间隔天数
     */
    public static final int HIT_RISK_INTERVAL_DAY = 30;

    /**
     * 命中风控，15天后设置用户所有的认证状态为0：未认证
     */
    public static final byte CHANGE_UNAUTH_STATE = 0;
    /**
     * 修改认证统计里的认证状态为0
     */
    public static final int CHANGE_INFOINDEX_UNAUTH_STATE = 0;
    /**
     * 修改用户未知属性默认状态0
     */
    public static final byte USER_UNKONWN_STATE = 0;

    /**
     * 修改用户未知属性默认状态0
     */
    public static final String CHANGE_AUTH_INFO_NULL = "";
    /**
     * 修改芝麻分为0
     */
    public static final short ZMSCORE = 0;
    /**
     * 芝麻行业关注度黑名单 0：否
     */
    public static final byte ZM_INDUSTY_BLACK = 0;
    /**
     * 借贷逾期记录数AA001借贷逾期的记录数
     */
    public static final byte ZM_OVER_NUM = 0;
    /**
     * 逾期未支付记录数，包括AD001 逾期未支付、AE001 逾期未支付的记录总数
     */
    public static final byte ZM_UNPAY_OVER_NUM = 0;
    /**
     * 花呗额度
     */
    public static final int MY_HB = 0;
    /**
     * 还款表中待还款状态
     */
    public static final byte WAIT_REPAYMENT_STATUS = 0;
    /**
     * 还款表中部分还款状态
     */
    public static final byte PART_REPAYMENT_STATUS = 1;

    /**
     * 日利率转换百分比
     */
    public static final int DAY_INTEREST_CHANGE = 100;
    public static final String LOGIN_OUT_IS_SUCCESS = "注销成功";
    /**
     * 存在借款的状态
     */
    public static final String EXIST_BORROW_STATUS = "1";

    /**
     * 存在借款的状态
     */
    public static final String NOT_EXIST_BORROW_STATUS = "0";
    /**
     * info_index_info表中的认证总数为0
     */
    public static final int AUTH_COUNT = 0;
    /**
     * 安卓类型
     */
    public static final int ANDROID_APP_TYPE = 1;
    /**
     * IOS类型
     */
    public static final int IOS_APP_TYPE = 2;
    /**
     * 认证失败
     */
    public static final String AUTH_IS_BAD = "认证失败";

    /**
     * 人脸认证次数失败5次
     */
    public static final String AUTH_FAILURE_COUNT = "人脸认证次数失败过多,请联系系统管理员";
    /**
     * 修改认证统计里的认证状态为1
     */
    public static final int CHANGE_INFOINDEX_AUTH_STATE = 1;
    /**
     * 实名状态
     */
    public static final byte REAL_AUTH_STATUS = 1;
    /**
     * 紧急联系人认证
     */
    public static final byte CONTACTS_AUTH_STATUS = 2;
    public static final byte MOBILE_AUTH_STATUS = 3;
    /**
     * APP版本信息key
     */
    public static final String APP_VERSION_INFO_KEY = "APP_VERSION_INFO_KEY_";
    /**
     * 当贷款规则中的最多借款金额为空时，默认返回去借款页面的最低金额
     */
    public static final int APPLY_MIN_MONEY = 1500;
    /**
     * app版本需要更新的标记
     */
    public static final int VERSION_UPDATE_NEED_FLAG = 1;
    /**
     * APP不需要版本更新的标记
     */
    public static final int VERSION_UPDATE_NOTNEED_FLAG = 0;
    /**
     * 联系人认证状态
     */
    public static final byte CONTACT_AUTH_STATUS = 1;

    /**
     * APP查询待还款订单的类型
     */
    public static final int FIND_WAIT_ORDER_TYPE = 1;
    /**
     * APP查询已还款订单的类型
     */
    public static final int FIND_REPAYMENT_ORDER_TYPE = 2;

    /**
     * 无借款订单
     */
    public static final String WITHOUT_LOAN_ORDER = "暂无借款信息";
    /**
     * 还款记录查询
     */
    public static final String WITHOUT_LOAN_TYPE = "类型错误";

    public static final String LOGIN_OUT_IS_BAD = "注销失败";
    /**
     * APP登录时发送短信限制次数
     */
    public static final int LIMIT_TIMES = 10;
    public static final int ROLEID_REVIEWER = 4;
    public static final int ROLEID_FINANCE = 6;
    public static final int ROLEID_FINANCE_REVIEWER = 10;
    public static final int ROLEID_SUPER = 1;
    public static final int ROLEID_WAITER = 5;
    public static final int ROLEID_CHANNEL = 7;
    public final static int ROLEID_COLLECTOR = 8;
    public final static int ROLEID_CUISHOUADMIN = 2;
    public final static int ROLEID_CHANNELUSER = 9;
    //当日催收
    public final static int ROLEID_COLLECTION_TODAY = 10;
    //boss
    public final static int ROLEID_BOSS_ADMIN = 12;
    // 当日催收主管
    public final static int ROLEID_BOSS_ADMIN_ADMIN = 11;
    //默认期限
    public static final int RENEWAL_EXPIRE = 6;
    /**
     * 默认续期手续费
     */
    public static final double RENEWA_FEE = 20.00;
    /**
     * info_index_info未认证状态
     */
    public static final int INFO_INDEX_UNAUTH = 0;
    /**
     * info_index_info该身份证已认证
     */
    public static final int INFO_INDEX_AUTHED = 2;
    /**
     * 银行卡未认证
     */
    public static final int INDEX_BANK_UNAUTH = 1;
    /**
     * 基本信息未认证
     */
    public static final int INDEX_BASE_UNAUTH = 2;
    /**
     * 金额转换
     */
    public static final Double CHANGE_CENT = 100.0;
    /**
     * 银行卡未认证
     */
    public static final int INDEX_BANK_AUTH = 3;
    /**
     * 首页被拒后显示数据的标记
     */
    public static final int ORDER_REFUSE_INDEX_FLAG = 3;
    /**
     * 运营人员的角色id
     */
    public static final int OPERATOR_ROLE_ID = 7;
    /**
     * 超级管理员角色id
     */
    public static final int ADMIN_ROLE_ID = 1;

    public static final int finance_ROLE_ID = 6;
    /**
     * 渠道主管角色id
     */
    public static final int CHANNEL_ADMIN_ROLE_ID = 3;

    /**
     * 渠道方角色id
     */
    public static final int CHANNEL_ROLE_ID = 9;
    //统计pv,uv的url
    public static final String PU_UV_URL = "/h5/login";
    /**
     * 落地页注册过的标记
     */
    public static final Integer REGISTERED_FLAG = 1;
    /**
     * 落地页注册成功的标记
     */
    public static final Integer REGISTER_SUCC_FLAG = 2;
    /**
     * H5注册成功提示
     */
    public static final String H5_USER_REGISTER = "您已注册过APP，请直接下载APP登录！";
    /**
     * H5已注册过提示
     */
    public static final String H5_REGISTER_SUCC = "恭喜你注册成功，请下载APP登录！";
    public static final String PU_UV_APP = "/app/pvuv";

    //H注册时，老用户返回的code
    public static final int H5_OlD_USER = 0;
    //渠道下线状态
    public static final int CHANNEL_INSERTING_COIL = 0;
    //渠道上线状态
    public static final int CHANNEL_ONLINE = 1;
    /**
     * 后台用户登录，忘记密码时的发送验证码key的后缀
     */
    public static final String BACK_UPDATE_PASSSWORD_SUFFIX = "_backUpdatePasswowrdCaptchaKeySuffix";
    /**
     * 落地页注册时发送验证码key的后缀
     */
    public static final String SQUEEZE_PAGE_REGISTER_SUFFIX = "_squeezePageCaptchaKeySuffix";
    /**
     * 后台用户登录登录时的发送验证码key的后缀
     */
    public static final String PLATEFROM_USER_LOGIN_SUFFIX = "_PLATEFROM_USER_LOGIN_SUFFIX";

    //后台登录忘记密码、修改密码
    public static final int PHONE_CAPTCHA_UPDATE_PASSWORD = 5;
    //渠道包
    public static final int PHONE_CAPTCHA_UPDATE_CHANNEL = 6;

    //忘记密码时发送验证码，用户存在
    public static final int PLATFROM_NOT_EXIST = 0;
    //忘记密码时发送验证码，用户存在
    public static final String PLATFROM_NOT_EXIST_INFO = "账号或密码错误";

    public static final String PASSWORD_UPDATE_SUCC = "密码修改成功";

    public static final String PASSWORD_UPDATE_FAILD = "密码修改失败";

    public static final String UPDATE_SUCC = "修改成功";

    public static final String UPDATE_FAILD = "修改失败";

    public static final String CHANNELNAME_EXIST = "该渠道名已经存在，请勿重复添加";

    public static final String PLATRFROM_USER_EXIST = "账户名已经存在，请重新输入手机号";

    public static final String CHANNEL_ADD_SUCC = "渠道添加成功";

    public static final String CHANNEL_ADD_FAILD = "渠道添加失败";

    public static final String CHANNEL_CODE_ERRER = "渠道信息错误,请重新获取";
    public static final String ERROR_USER_LOGIN = "非注册用户，禁止登录";
    public static final String VERIFY_ERRER = "校验失败";
    public static final String PASSWORD_NULL_ERRER = "密码不能为空，请填写密码";
    public static final String IP_LIMIT = "IP受限，请联系客服进行解除！";

    public static final String IP_LIMIT_BACK = "该ip地址获取验证码次数过多，请联系管理员解封！";
    /**
     * 费率中可借金额为0时，默认返回去借款页面的最低金额
     */
    public static final int ALLOW_TOTAL_MONEY = 1500;
    //落地页刷新成功
    public static final String FLUSH_IS_SUCC = "刷新成功";
    /**
     * 渠道被禁用的状态
     */
    public static final int CHANNEL_FORBIDDEN = 1;
    /**
     * 查询权限列表时使用
     */
    public static final String AUTHORITY_INFO = "对不起，您没有该权限!";
    /**
     * 渠道方用户角色id
     */
    public static final int CHANNEL_USER_ROLE_ID = 9;
    /**
     * 渠道用户被冻结
     */
    public static final int CHANNEL_USER_FORBIDDEN = 1;
    /**
     * 渠道用户被禁用状态
     */
    public static final int PLATEFORM_USER_FORBIDDEN = 1;
    /**
     * 后台用户token有效时长
     */
    public static final long PLATEFROM_TOKEN_EXPIRE = 12 * 60 * 60;
    /**
     * 后台登录时，根据手机号存一个token的key前缀
     */
    public static final String PLATEFROM_USER_TOKEN = "PLATEFROM_USER_TOKEN_";

    public static final String AUTH_UPDATE_SUCC = "权限修改成功";
    //用户认证次数
    public static final Byte AUTH_COUNT_TIMES = 1;

    public static final String ADD_USER_SUCC = "用户添加成功";

    public static final String CAPATCH_NOT_NULL = "验证码不能为空！";
    public static final String PASSWORD_NOT_NULL = "密码不能为空！";

    public static final String RISK_PUSH_SUCC = "重推成功";

    public static final String DISPENSE_RISK_PUSH = "该用户订单状态正常，无需重推！";
    public static final String DISPENSE_RE_RISK_PUSH = "该用户订单状态正常，无需重推！";

    public static final String RISK_PUSH_FAILD = "订单订单信息有误,重推失败！";
    //催收主管角色id
    public static final int CUISHOU_ADMIN_ID = 2;
    //渠道主管角色id
    public static final int CHANNEL_ADMIN_ID = 3;
    //催收专员角色id
    public static final int CUISHOU_ID = 8;
    //渠道专员专员角色id
    public static final int CHANNEL_ID = 7;
    //渠道用户角色id
    public static final int CHANNEL_USER_ID = 9;
    //渠道注册数量、申请数、放款数 大于6开始扣量
    public static final int CHANNEL_COUNT = 6;
    //自然流量注册时，若没有查询到预先存好的自然流量却道，就默认扣量比例
    public static final BigDecimal DECREASE_ERCENTAGE = new BigDecimal(80);
    //自然流量注册时，若没有查询到预先存好的自然流量却道，就默认价格
    public static final Double CHANNEL_PRICE = 12.0;
    //自然流量注册时，若没有查询到预先存好的自然流量渠道 默认结算方式
    public static final int PAYMENTMODE = 0;
    //自然流量注册时，若没有查询到预先存好的自然流量渠道，就默认渠道结算类型
    public static final int PAYMENTTYPE = 1;
    //自然流量注册时，若没有查询到预先存好的自然流量渠道，就默认合作模式：0 cpa
    public static final int COOPERATIONMOD = 0;
    //扣量计算时的5
    public static final int DECREASE_PERCENTAGE_FIVE = 5;

    //风控模型分阀值
    public static final String SCORE_GATEWAY = "score_gateway";
    //自然渠道模型分阀值
    public static final String NATURE_CHANNEL = "自然流量";
    //自然渠道模型分阀值
    public static final String OLD_SCORE = "OLD_SCORE";

    //风控模型分阀值 redis key
    public static final String SCORE_GATEWAY_REDIS_KEY = "SCORE_GATEWAY_KEY_";
    //自然渠道模型分阀值 redis key
    public static final String NATURE_CHANNEL_KEY = "NATURE_CHANNEL_KEY_";
    //查询风控配置的类型
    public static final String BACK_CONFIG_TYPE = "RISK_PAIXU";
    /**
     * 过去5天的逾期数量时使用
     */
    public static final int PASS_DAY = -5;

    public static final String INCREASE_IS_SUCC = "手动提额成功!";
    public static final String INCREASE_IS_FAILD = "手动提额失败!";

    public static final String INCREASE_IS_CLOSE = "自动提额关闭成功!";
    public static final String INCREASE_IS_OPEN = "自动提额开启成功!";
    /**
     * 自动提额开启状态
     */
    public static final int INCREASE_OPEN_STATUS = 1;
    /**
     * 自动提额关闭状态
     */
    public static final int INCREASE_OPEN_CLOSE = 0;
    /**
     * 魔杖认证状态
     */
    public static final int MZ_AUTH_STATUS = 1;
    /**
     * 记录用户当前使用的哪个通道进行的绑卡
     */
    public static final String REPAYMENT_BINDCARD_TYPE = "REPAYMENT_BIND_CARD_TYPE_";
    /**
     * 记录用户畅捷绑卡时的orderNo
     */
    public static final String REPAYMENT_BINDCARD_ORDERNO = "REPAYMENT_BIND_CARD_ORDERNO_";
    /**
     * 记录用户快钱绑卡时的token
     */
    public static final String REPAYMENT_BINDCARD_TOLEN = "REPAYMENT_BIND_CARD_TOLEN_";
    /**
     * 支付通道关闭
     */
    public static final int CLOSE_REPAYMENT_TYPE = 0;
    /**
     * 快钱
     */
    public static final int KUAIQIAN_REPAYMENT_TYPE = 2;
    /**
     * 畅捷
     */
    public static final int CHANGJIE_REPAYMENT_TYPE = 1;
    /**
     * 绑卡时选择的绑卡通道、外部跟踪编号， 缓存过期时间,10分钟
     */
    public static final long REPAYMENT_BINDCARD_EXPIRATION_TIME = 600;
    /**
     * 快钱发送验证码时，外部跟踪编号key前缀
     */
    public static final String KUAIQIAN_EXTERNALREF_NUMBER = "KUAIQIAN_EXTERNALREF_NUMBER_";
    /**
     * 快钱通过银行卡查询银行卡信息，把银行卡名称，卡类型缓存起来，因为后面绑卡成功需要保存，而在验证接口没有返回对应信息，防止第二次调用查询接口
     */
    public static final String KUAIQIAN_USER_CARD_INFO = "KUAIQIAN_USER_CARD_INFO_";

    /**
     * 否
     */
    public static final int MS_ZERO = 0;
    /**
     * 是
     */
    public static final int MS_ONE = 1;
    public static final String REPAYMENT_ERROR = "支付通道异常，请稍后再试！";
    /**
     * 畅捷协议号
     */
    public static final String CHANGJIE_AGREENO = "cj_tag";
    /**
     * APP申请借款时的key
     */
    public static final String APP_LOAN_APPLY = "APP_LOAN_APPLY_";

    public static final long APP_LOAN_TIME = 30;

    /**
     * 老用户类型
     */
    public static final byte OLD_CUSTOMER_TYPE = 1;
    /**
     * 新用户类型
     */
    public static final byte NEW_CUSTOMER_TYPE = 0;
    /**
     * 查询系统配置，逾期是否可以复贷的key
     */
    public static final String OVERDUE_REPAYMENT_KEY = "overdue_repayment";
    /**
     * 逾期不能复贷状态
     */
    public static final int OVERDUE_REPAYMENT_STATUS = 0;

    /**
     * 渠道方用户角色id
     */
    public static final int CUISHOU_USER_ROLE_ID = 8;
    /**
     * 魔杖开关
     */
    public static final int MZ_SWITCH = 1;
    /**
     * 删除的用户注册返回信息
     */
    public static final String FOR_REGISTER_INFO = "对不起！系统检测到您的账号异常，暂无法注册！";
    /**
     * 删除用户的状态
     */
    public static final byte DELETE_TYPE = 2;
    /**
     * 全局的费率渠道id
     */
    public static final int CHANNELRULE_ID = 0;

    public static final String CHANNEL_REQUEST_TOKEN = "请求token获取错误!";

    public static final String CHANNEL_REQUEST_TOKEN_BAD = "请求token获取失败!";
    /**
     * 续期开关的key
     */
    public static final String RENEWAL_SWITCH = "renewal_switch";
    /**
     * 风控余额的key
     */
    public static final String RISK_BALANCE = "RISK_BALANCE_";
    /**
     * 利息前置/后置开关的key
     */
    public static final String INTEREST = "interest";

    /*待审核状态*/
    public static final byte WAIT_REJECT_STATUS = 0;

    public static final String HAS_MORE_ORDER = "普通用户每日只能生成一笔借款订单！";
    /**
     * 支付开关
     */
    public static final String REPAY_SWITCH = "repay_switch";

    public static final Integer AUTH_COMPLATE = 1;

    public static final int WAIT_SIHN = -2;
    public static final int XINYAN_REPORT = 4;
    public static final int JILINCHONG_REPORT = 7;

    public static final String UNDERTHELINE = "under_the_line_";

    public static final String UN_AUTH_VIST = "对不起，您无权操作！";

    /**
     * 缓存渠道信息
     */
    public static final String PLATEFORM_CHANNEL = "PLATEFORM_CHANNEL_";
    public static final int MOBILE_TYPE_BLUE = 6;

    public static final String SMS_SERVICE = "sms.service";
    //渠道防撸开关
    public static final String CHANNEL_STRIP_SWITCH = "channel_strip_switch";

    public static final String APP_UPDATE_PASSWORD = "app_update_password";

    //风控金额开关
    public static final int WIND_CONTROL_MONEY_ISOPEN = 2;  // 2 为关


    /**
     * 用户认证状态rediskey
     */
    public static final String USER_INDEX_AUTHINFO = "USER_INDEX_AUTHINFO_";
    public static final String USER_INDEX_AUTHMOBILE = "USER_INDEX_AUTHMOBILE_";

    /**
     * 客服端标志
     */
    public static final String APP_SWITCH = "appId";
    /**
     * faceId
     */
    public static final String FACE_ID ="face_id";

    public static final String SECRET_ID ="secret_id";

    public static final String RETREAT_MONEY ="retreat_Money";

    public static final String OCR_CONFIDENCE = "置信度低于0.7,活体检测未过";
    //活体开关
    public static final String OCR_SELFILE = "ocr_selfile";

}

