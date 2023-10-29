/*
 Navicat Premium Data Transfer

 Source Server         : 9999
 Source Server Type    : MySQL
 Source Server Version : 80025
 Source Host           : rm-6wen0d3sib377ypldjo.mysql.japan.rds.aliyuncs.com:3306
 Source Schema         : b_xiaojuzi

 Target Server Type    : MySQL
 Target Server Version : 80025
 File Encoding         : 65001

 Date: 05/07/2022 21:22:24
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for app_version_info
-- ----------------------------
DROP TABLE IF EXISTS `app_version_info`;
CREATE TABLE `app_version_info`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `current_version` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '当前版本信息',
  `app_type` int(0) NOT NULL COMMENT 'APP类型（1表示安卓，2表示IOS）',
  `is_update` int(0) NOT NULL DEFAULT 0 COMMENT '是否提示更新(0,不更新，1更新)',
  `apk_download_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'apk下载地址',
  `update_description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '更新文案',
  `force_update` int(0) NOT NULL DEFAULT 0 COMMENT '是否强制更新(0非强制更新，1强制更新)',
  `apk_md5` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'md5值',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `title_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标题',
  `bundle_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '包名',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of app_version_info
-- ----------------------------
INSERT INTO `app_version_info` VALUES (1, '110', 1, 0, 'http://qr.hotlnk.cn/79837224975', '1.优化低版本还款功能', 1, 'adfcvasd', '2019-01-05 11:50:23', '2022-07-01 14:19:44', '小橘子', 'com.summer.jyh');
INSERT INTO `app_version_info` VALUES (2, '110', 2, 0, 'http://qr.hotlnk.cn/79837224975', 'ios', 0, 'asdd', '2019-03-14 12:56:44', '2022-07-01 14:19:52', '小橘子', 'com.summer.jyh');
INSERT INTO `app_version_info` VALUES (3, '110', 3, 0, 'http://qr.hotlnk.cn/79837224975', '合并链接', 0, '12', '2019-12-25 12:40:31', '2022-06-25 13:50:21', '标题头', 'com.java.tool');

-- ----------------------------
-- Table structure for auth_rule_config
-- ----------------------------
DROP TABLE IF EXISTS `auth_rule_config`;
CREATE TABLE `auth_rule_config`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '认证规则主键',
  `auth_rule_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '认证规则的key',
  `auth_rule_descript` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '认证规则的描述（对应前端的认证名称）',
  `auth_rule_type` tinyint(0) NOT NULL DEFAULT 0 COMMENT '认证类型（0表示必要认证，1表示补充认证）',
  `add_user` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '添加的人',
  `create_time` timestamp(0) NOT NULL DEFAULT '1970-01-01 08:00:01' COMMENT '添加的时间',
  `update_time` datetime(0) NOT NULL DEFAULT '1970-01-01 08:00:01' COMMENT '更新时间',
  `status` tinyint(0) NOT NULL DEFAULT 0 COMMENT '规则的状态（0表示启用，1表示关闭）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '认证规则配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of auth_rule_config
-- ----------------------------
INSERT INTO `auth_rule_config` VALUES (1, 'basicAuthentic', '个人信息认证', 0, '', '2019-02-27 08:00:01', '2019-03-29 14:36:21', 0);
INSERT INTO `auth_rule_config` VALUES (2, 'emergencyAuthentic', '紧急联系人认证', 0, '', '2019-03-05 08:00:01', '2019-04-15 17:30:00', 0);
INSERT INTO `auth_rule_config` VALUES (3, 'mobileAuthentic', '运营商认证', 0, '', '2019-03-05 08:00:01', '2019-04-15 19:54:01', 0);
INSERT INTO `auth_rule_config` VALUES (4, 'zmAuthentic', '芝麻认证', 0, '', '2019-03-05 08:00:01', '2019-04-14 15:42:33', 1);
INSERT INTO `auth_rule_config` VALUES (5, 'bankAuthentic', '银行卡认证', 0, NULL, '2019-03-05 08:00:01', '2019-04-12 17:38:27', 0);

-- ----------------------------
-- Table structure for back_config_params
-- ----------------------------
DROP TABLE IF EXISTS `back_config_params`;
CREATE TABLE `back_config_params`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `sys_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '参数名称',
  `sys_value` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '参数值',
  `sys_value_big` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '文本类型值',
  `sys_key` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '参数键',
  `sys_type` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '参数类型',
  `input_type` enum('text','textarea','password','image') CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '页面输入类型',
  `remark` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '参数说明',
  `limit_code` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `desc` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '该字段用户各项说明，不用做前台显示',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `unique`(`sys_key`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 996 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of back_config_params
-- ----------------------------
INSERT INTO `back_config_params` VALUES (1, '代付余额不足通知手机', '186', NULL, 'charge_notify_phones', 'PAY_CHANGJIE', 'text', '号码', 'class=\"required digits\" min=\"0\"', NULL);
INSERT INTO `back_config_params` VALUES (661, '机审通过人工审核控制', '1', '', 'REVIEW_JS', 'REVIEW_JS', 'text', '', '', '');
INSERT INTO `back_config_params` VALUES (694, '风控模型分阀值', '50', NULL, 'score_gateway', 'RISK_PAIXU', 'text', '分', 'class=\"required digits\" min=\"0\"', NULL);
INSERT INTO `back_config_params` VALUES (695, '老用户逾期天数阀值', '1', NULL, 'overdueDays', 'RISK_PAIXU', 'text', '天', 'class=\"required digits\" min=\"0\"', NULL);
INSERT INTO `back_config_params` VALUES (696, '老用户申请时间阀值', '2018-01-01', NULL, 'applyTime', 'RISK_PAIXU', 'text', '天', 'class=\"required digits\" min=\"0\"', NULL);
INSERT INTO `back_config_params` VALUES (697, '自然渠道模型分阀值', '750', NULL, '自然流量', 'RISK_PAIXU', 'text', '分', 'class=\"required digits\" min=\"0\"', NULL);
INSERT INTO `back_config_params` VALUES (704, '项目环境', 'offline', NULL, 'environment', 'XMHJ', 'text', NULL, NULL, NULL);
INSERT INTO `back_config_params` VALUES (705, '风控分数类型0佩奇分1排序4mb新客5pb6mb老客', '5', NULL, 'score_type', 'RISK_PAIXU', 'text', '分', 'class=\"required digits\" min=\"0\"', NULL);
INSERT INTO `back_config_params` VALUES (706, '支付通道切换1畅捷2块钱', '1', NULL, 'repayment_change', 'PAY_CHANGE', 'text', '支付通道切换，0表示关闭，1表示畅捷，2表示快钱', NULL, NULL);
INSERT INTO `back_config_params` VALUES (707, '逾期是否允许复贷', '1', NULL, 'overdue_repayment', 'RISK_FIRST', 'text', '逾期是否可以复贷，0表示否，1表示是', NULL, NULL);
INSERT INTO `back_config_params` VALUES (710, '魔杖认证开关', '1', NULL, 'mz_switch', 'RISK_DATA', 'text', '是否关闭魔杖认证：0表示否，1表示是', NULL, NULL);
INSERT INTO `back_config_params` VALUES (711, '达到逾期自动导入黑名单', '7', NULL, 'overdue_day_insert', 'RISK_OVERDUE', 'text', '根据配置的逾期天数，每天定时自动导入黑名单库', NULL, NULL);
INSERT INTO `back_config_params` VALUES (712, '老用户直接放款', '1', NULL, 'old_pass', 'RISK_PAIXU', 'text', '老用户直接放款，0表示否，1表示是', NULL, NULL);
INSERT INTO `back_config_params` VALUES (713, '放款延迟时间单位分钟', '1', NULL, 'late_min', 'RISK_PAIXU', 'text', '放款延迟时间', NULL, NULL);
INSERT INTO `back_config_params` VALUES (714, '老系统0新系统1', '0', NULL, 'env_new', 'RISK_PAIXU', 'text', '分', 'class=\"required digits\" min=\"0\"', NULL);
INSERT INTO `back_config_params` VALUES (715, '签约公司主体', '公司', NULL, 'ag_company', 'ag_type', 'text', '签约公司主体', NULL, NULL);
INSERT INTO `back_config_params` VALUES (716, '签约公司信用代码', 'asdfjkl', NULL, 'ag_code', 'ag_type', 'text', '签约公司信用代码', NULL, NULL);
INSERT INTO `back_config_params` VALUES (718, '续期开关', '0', NULL, 'renewal_switch', 'LOAN_TYPE', 'text', '续期开关，0表示开启，1表示禁用', NULL, NULL);
INSERT INTO `back_config_params` VALUES (719, '支付开关', '1', NULL, 'repay_switch', 'LOAN_TYPE', 'text', '支付开关，0=银行卡，1=支付宝', NULL, NULL);
INSERT INTO `back_config_params` VALUES (720, '还款金额计算方式', '0', NULL, 'ct_switch', 'LOAN_TYPE', 'text', '还款金额计算方式，0表示：申请金额+服务费+利息，1表示：还款金额=申请金额', NULL, NULL);
INSERT INTO `back_config_params` VALUES (721, '线下收款银行', '杭州_阿里_巴巴_网络科技_有限公司', NULL, 'pay_bank', 'pay_type', 'text', '线下收款银行', NULL, NULL);
INSERT INTO `back_config_params` VALUES (723, '小贷开关', '0', NULL, 'xd_switch', 'LOAN_TYPE_XD', 'text', '小贷开关 0=开启，1=关闭', NULL, NULL);
INSERT INTO `back_config_params` VALUES (725, '银行卡解绑权限开关', '1', NULL, 'bind_card', 'BIND_SWITCH', 'text', '银行卡解绑权限开关0=开启，1=关闭', NULL, NULL);
INSERT INTO `back_config_params` VALUES (726, '利息前置/后置开关', '0', NULL, 'interest', 'LOAN_TYPE', 'text', '0：利息前置，1：利息后置', NULL, NULL);
INSERT INTO `back_config_params` VALUES (728, '是否查询魔杖数据开关', '1', NULL, 'mz_status', 'mz_status', 'text', '魔杖数据查询开关 0=要查询 1=不查询', NULL, NULL);
INSERT INTO `back_config_params` VALUES (730, '借款规则金额下限', '100', NULL, 'loan_limitStart', 'LOAN_RULE', 'text', '控制渠道添加、贷款规则的修改 最小金额 ', NULL, NULL);
INSERT INTO `back_config_params` VALUES (732, '借款规则金额上限', '300000', NULL, 'loan_limitEnd', 'LOan_RULE', 'text', '控制渠道添加、贷款规则的修改 最大金额 ', NULL, NULL);
INSERT INTO `back_config_params` VALUES (733, '放款金额上限限制(单位分)', '30000000', NULL, 'loan_amount_limit', 'LOAN_RULE', 'text', '放款金额上限限制', NULL, NULL);
INSERT INTO `back_config_params` VALUES (734, '密码', '123456', NULL, 'password', 'password', 'text', '密码', NULL, NULL);
INSERT INTO `back_config_params` VALUES (735, '首页日利息', '5', NULL, 'dayInterest', 'dayInterest', 'text', NULL, NULL, NULL);
INSERT INTO `back_config_params` VALUES (736, '代付选择,0随机,1bt,2ac,3db(火牛),4极及支付,5pinbo,6卓和', '2', NULL, 'repay_bf', 'repay', 'text', NULL, NULL, NULL);
INSERT INTO `back_config_params` VALUES (737, '还款通道选择 0随机,1bt,2ac3db(火牛),4极及支付,5pinbo,6卓和', '2', NULL, 'usdt_df', 'usdt', 'text', NULL, NULL, NULL);
INSERT INTO `back_config_params` VALUES (738, '运营商开关:1-fuygs2bob3box', '3', NULL, 'mobile_switch', 'mobile', 'text', '分', 'class=\"required digits\" min=\"0\"', NULL);
INSERT INTO `back_config_params` VALUES (739, 'ocr开关1ocr', '1', NULL, 'ocr_switch', 'ocr', 'text', '分', 'class=\"required digits\" min=\"0\"', NULL);
INSERT INTO `back_config_params` VALUES (740, '运营商用户修改权限:0不可,1手机号,2身份证姓名,3修改所有', '0', NULL, 'info_update', 'info_update', 'text', '分', 'class=\"required digits\" min=\"0\"', NULL);
INSERT INTO `back_config_params` VALUES (742, '自动申请借款,0手动,1自动', '1', NULL, 'auto_borrow', 'auto_borrow', 'text', '自动申请借款', NULL, NULL);
INSERT INTO `back_config_params` VALUES (743, '客服url', 'http://47.74.48.135:7088/chatIndex?kefu_id=kefu2&refer=', NULL, 'wait_url', 'wait_url', 'text', '客服url', NULL, NULL);
INSERT INTO `back_config_params` VALUES (763, '是否开启防撸 0未开启，1开启了', '1', NULL, 'channel_strip_switch', 'channel_strip_switch', NULL, NULL, NULL, NULL);
INSERT INTO `back_config_params` VALUES (766, 'APP用户重置密码开关 1开启，2关闭', '1', NULL, 'app_update_password', 'app_update_password', NULL, NULL, NULL, NULL);
INSERT INTO `back_config_params` VALUES (842, '在提醒还款时，提示提升的假额度', '1000.00', NULL, 'sms.service.increaseMoney', 'sms.service', NULL, NULL, NULL, NULL);
INSERT INTO `back_config_params` VALUES (843, '到期前一天提醒短信', '尊敬的#userName#，您在#appName#申请#money#元还有1天到期，请按时归还。如有联系您线下还款，本金销账，一律为假冒，如有疑问请认准APP-（我的）唯一官方客服核实。', NULL, 'sms.service.repayRemind', 'sms.service', NULL, NULL, NULL, NULL);
INSERT INTO `back_config_params` VALUES (844, '到期当天短信', '尊敬的#userName#，您在#appName#申请#money#元已到期，请按时还款。如有联系您线下还款，本金销账，一律为假冒，如有疑问请认准APP-（我的）唯一官方客服核实。', NULL, 'sms.service.repayRemindNow', 'sms.service', NULL, NULL, NULL, NULL);
INSERT INTO `back_config_params` VALUES (845, '逾期短信', '尊敬的#userName#，您在#appName#申请#money#元已超时#day#天，请尽快还款以免影响您的信誉。平台仅支持线上还款，如有疑问请认准APP-（我的）唯一官方客服。', NULL, 'sms.service.repayOverdue', 'sms.service', NULL, NULL, NULL, NULL);
INSERT INTO `back_config_params` VALUES (846, '银行卡绑定成功', '尊敬的#userName#您好！您在#appName#APP上银行卡已绑定成功。您的资质良好可获得最高20000额度，赶紧点击申请完成吓款吧～', NULL, 'sms.service.bankBindSucc', 'sms.service', NULL, NULL, NULL, NULL);
INSERT INTO `back_config_params` VALUES (847, '提额成功', '恭喜您已经成功归还，额度将提高到#MaxAmount#元，请继续保持良好的归还习惯！', NULL, 'sms.service.increaseSucc', 'sms.service', NULL, NULL, NULL, NULL);
INSERT INTO `back_config_params` VALUES (848, '商户余额不足', '商户号(#CorpAcctNo#)余额不足，请赶快充值！', NULL, 'sms.service.moneyInsufficient', 'sms.service', NULL, NULL, NULL, NULL);
INSERT INTO `back_config_params` VALUES (849, '打款成功', '尊敬的#RealName#，您在#appName#申请的(#applyAmount#)元借款，已经成功发放至您的银行卡，请注意查收。温馨提示：请认准app（我的）中官方客服账号，其他账号均为假冒，谨防上当受骗。', NULL, 'sms.service.remitIsSucc', 'sms.service', NULL, NULL, NULL, NULL);
INSERT INTO `back_config_params` VALUES (850, '打款失败', '尊敬的#RealName#，您在#appName#申请的(#applyAmount#)元借款，打款失败原因(#reason#)，请及时更换其他银行卡。', NULL, 'sms.service.remitIsFail', 'sms.service', NULL, NULL, NULL, NULL);
INSERT INTO `back_config_params` VALUES (851, 'ios更新短信', '您好,APP已成功升级,最新版本下载地址:#url#,请删除老版本,下载新版本噢。还款续借请认准官方APP,以防被骗,索要验证码和公众号还款等行为全部属于诈骗行为!', NULL, 'sms.service.iosUpdate', 'sms.service', NULL, NULL, NULL, NULL);
INSERT INTO `back_config_params` VALUES (852, '第三方支付商户名', '畅捷', NULL, 'sms.service.commercialName', 'sms.service', NULL, NULL, NULL, NULL);
INSERT INTO `back_config_params` VALUES (853, '商户余额不足', '#commercialName#商户余额不足，请赶快充值！', NULL, 'sms.service.commercialMoney', 'sms.service', NULL, NULL, NULL, NULL);
INSERT INTO `back_config_params` VALUES (854, '放款异常', '请求#commercialName#放款失败！请尽快处理！', NULL, 'sms.service.loanException', 'sms.service', NULL, NULL, NULL, NULL);
INSERT INTO `back_config_params` VALUES (855, '支付失败', '支付失败,失败原因:#reason#！请尽快处理！', NULL, 'sms.service.payError', 'sms.service', NULL, NULL, NULL, NULL);
INSERT INTO `back_config_params` VALUES (856, '#APP、落地页注册、后台登录,短信验证码，修改密码等', '#title#验证码：#code#，请勿泄露。你正在授权本平台为你提供服务，如非本人操作，请忽略。', NULL, 'sms.service.sendContent', 'sms.service', NULL, NULL, NULL, NULL);
INSERT INTO `back_config_params` VALUES (857, '签名', '【小橘子】', NULL, 'sms.service.title', 'sms.service', NULL, NULL, NULL, NULL);
INSERT INTO `back_config_params` VALUES (858, '回T退订', '回T退订', NULL, 'sms.service.suffix', 'sms.service', NULL, NULL, NULL, NULL);
INSERT INTO `back_config_params` VALUES (859, '风控余额不足', '风控余额不足,请及时充值', NULL, 'sms.service.risk', 'sms.service', NULL, NULL, NULL, NULL);
INSERT INTO `back_config_params` VALUES (860, '老用户自动申请借款,0手动,1自动', '0', NULL, 'old_auto', 'old_auto', 'text', '老用户自动申请借款', NULL, NULL);
INSERT INTO `back_config_params` VALUES (861, '安心放开关 0关闭 1开启', '0', NULL, 'axf_switch', 'axf_switch', 'text', '分', 'class=\"required digits\" min=\"0\"', NULL);
INSERT INTO `back_config_params` VALUES (862, '商户内部黑名单开关1 关闭黑名单 2开启黑名单', '2', NULL, 'user_Black_List', 'user_Black_List', NULL, NULL, NULL, NULL);
INSERT INTO `back_config_params` VALUES (863, '短信切换开关,1-萌萌 2-smgw3-海鱼4-聚光', '4', NULL, 'sms.switch', 'sms.service', NULL, NULL, NULL, NULL);
INSERT INTO `back_config_params` VALUES (864, '商户余额', '92.5', NULL, 'merchant_balanace', 'merchant_balanace', NULL, '商家余额', NULL, NULL);
INSERT INTO `back_config_params` VALUES (877, '费用统计模式1、2', '2', NULL, 'tjms', 'tjms', 'text', '费用统计模式', NULL, NULL);
INSERT INTO `back_config_params` VALUES (878, 'APP聊天URL', 'https://kefu.easemob.com/webim/im.html?configId=50adf881-40f4-44bd-88b5-06f53075f944', NULL, 'app_chatUrl', 'app_chatUrl', 'text', 'APP聊天URL', '', NULL);
INSERT INTO `back_config_params` VALUES (879, '财神黑名单开关 1 关闭黑名单 2开启黑名单', '1', NULL, 'caishen_black_switch', 'caishen_black_switch', 'text', '财神黑名单开关,2=开启，1=关闭:', NULL, NULL);
INSERT INTO `back_config_params` VALUES (880, '宁远黑名单开关 1 关闭黑名单 2开启黑名单', '1', NULL, 'ningyuan_black_switch', 'ningyuan_black_switch', 'text', '宁远黑名单开关,2=开启，1=关闭:', NULL, NULL);
INSERT INTO `back_config_params` VALUES (881, '梦华黑名单开关 1 关闭黑名单 2开启黑名单', '1', NULL, 'menghua_black_switch', 'menghua_black_switch', 'text', '梦华黑名单开关,2=开启，1=关闭:', NULL, NULL);
INSERT INTO `back_config_params` VALUES (882, '费用统计模式二开关（1开、2关）', '1', NULL, 'tjms2IsOpen', 'tjms2IsOpen', 'text', '风控金额低于5000自动关闭风控接口，不走机审', NULL, NULL);
INSERT INTO `back_config_params` VALUES (883, '费用统计模式一开关（1开、2关）', '1', NULL, 'tjms1IsOpen', 'tjms1IsOpen', 'text', '风控金额低于5000自动关闭风控接口，不走机审', NULL, NULL);
INSERT INTO `back_config_params` VALUES (886, '短信通知切换开关,1-萌萌 2-smgw ', '1', NULL, 'smsnotice.switch', 'sms.service', NULL, NULL, NULL, NULL);
INSERT INTO `back_config_params` VALUES (887, '机审通过自动放款条件所需分（需要REVIEW_JS为0）', '5650', NULL, 'auto_score_gateway', 'auto_score_gateway', NULL, '分', NULL, NULL);
INSERT INTO `back_config_params` VALUES (888, '金大鑫', '109', NULL, 'appId', 'axd_type', 'text', NULL, NULL, NULL);
INSERT INTO `back_config_params` VALUES (889, '退款代付选择,0随机,1极及支付', '8000', NULL, 'retreat_Money', 'retreat_Money', 'text', NULL, NULL, NULL);
INSERT INTO `back_config_params` VALUES (890, '老客是否需要过风控k开关', '0', NULL, 'OLD_CUSTOMER_CHECK', 'OLD_CUSTOMER_CHECK', NULL, NULL, NULL, NULL);
INSERT INTO `back_config_params` VALUES (900, '天创共债开关 0 - 关  1-开', '0', NULL, 'tcgz', '', NULL, NULL, NULL, NULL);
INSERT INTO `back_config_params` VALUES (901, '贝多芬共债开关1 0 - 关  1-开', '0', NULL, 'gzbdf', '', NULL, NULL, NULL, NULL);
INSERT INTO `back_config_params` VALUES (991, '龙珠共债开关 0 - 关  1-开 ', '1', NULL, 'lzgz', '', NULL, NULL, NULL, NULL);
INSERT INTO `back_config_params` VALUES (992, '之谜共债开关 0 - 关  1-开', '1', NULL, 'zmgz', '', NULL, NULL, NULL, NULL);
INSERT INTO `back_config_params` VALUES (993, '101共债开关 0 - 关  1-开', '0', NULL, 'gz101', '', NULL, NULL, NULL, NULL);
INSERT INTO `back_config_params` VALUES (994, '贝多芬共债开关 0 - 关  1-开', '0', NULL, 'bdf', '', NULL, NULL, NULL, NULL);
INSERT INTO `back_config_params` VALUES (995, '人脸活体开关', '1', NULL, 'ocr_selfile', '', NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for bank_bind_info
-- ----------------------------
DROP TABLE IF EXISTS `bank_bind_info`;
CREATE TABLE `bank_bind_info`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `user_id` int(0) NULL DEFAULT NULL COMMENT '用户id',
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户名称',
  `message` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '返回信息',
  `crate_time` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建时间',
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'code',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for collected_user
-- ----------------------------
DROP TABLE IF EXISTS `collected_user`;
CREATE TABLE `collected_user`  (
  `id` int(0) UNSIGNED NOT NULL COMMENT '自增ID',
  `realname` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '真实姓名',
  `id_number` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '身份证号码',
  `user_phone` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号码',
  `overdueDays` int(0) NULL DEFAULT NULL COMMENT '逾期天数',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `status` int(0) NULL DEFAULT 0 COMMENT '状态：0：正常，1：移出黑名单',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_phone`(`user_phone`) USING BTREE,
  UNIQUE INDEX `uk_id_num`(`id_number`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '逾期用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for collection_company
-- ----------------------------
DROP TABLE IF EXISTS `collection_company`;
CREATE TABLE `collection_company`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '催单公司名称',
  `create_date` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `priority` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '1' COMMENT '优先级，优先级越高，优先分配订单（暂时不提供）',
  `status` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '1' COMMENT '状态，1启用，0禁用',
  `self_business` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '是否是自营团队，1是，0不是',
  `region` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '归属地',
  `update_date` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '催收机构（公司、本公司不可删除）' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of collection_company
-- ----------------------------
INSERT INTO `collection_company` VALUES ('1', 'cuishou_comp', '2018-10-31 14:59:02', '1', '1', '1', '上海', NULL);

-- ----------------------------
-- Table structure for collection_statistics
-- ----------------------------
DROP TABLE IF EXISTS `collection_statistics`;
CREATE TABLE `collection_statistics`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `count_date` date NULL DEFAULT NULL COMMENT '统计时间',
  `user_id` int(0) NULL DEFAULT NULL COMMENT '催收员角色id',
  `distribution_number` int(0) NULL DEFAULT 0 COMMENT '分配订单数',
  `repayment_number` int(0) NULL DEFAULT 0 COMMENT '催回订单数',
  `repayment_number_rate` decimal(11, 2) NULL DEFAULT 0.00 COMMENT '订单还款率',
  `distribution_amount` decimal(11, 2) NULL DEFAULT 0.00 COMMENT '应还金额',
  `repayment_amount` decimal(11, 2) NULL DEFAULT 0.00 COMMENT '催回金额',
  `repayment_amount_rate` decimal(11, 2) NULL DEFAULT 0.00 COMMENT '金额催回率',
  `update_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_count_date`(`count_date`) USING BTREE,
  INDEX `index_user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '催收统计总报表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for count_collection_assessment
-- ----------------------------
DROP TABLE IF EXISTS `count_collection_assessment`;
CREATE TABLE `count_collection_assessment`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `person_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '催收员编号',
  `person_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '催收员姓名',
  `company_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '催单公司编号',
  `company_title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '催单公司名称',
  `group_id` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '催收员分组（3，4，5，6，7对应S1，S2，M1-M2，M2-M3，M3+对应1-10,11-30（1），1个月-2个月，2个月-3个月，3个月+）',
  `group_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '催收员分组name',
  `order_group_id` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分组id',
  `order_group_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '订单分组name',
  `loan_money` decimal(11, 2) NULL DEFAULT 0.00 COMMENT '本金金额，元',
  `repayment_money` decimal(11, 2) NULL DEFAULT 0.00 COMMENT '已还金额，元',
  `not_yet_repayment_money` decimal(11, 2) NULL DEFAULT 0.00 COMMENT '未还金额，元',
  `repayment_rate` decimal(11, 4) NULL DEFAULT NULL COMMENT '本金还款率，元',
  `migrate_rate` decimal(11, 2) NULL DEFAULT -1.00 COMMENT '迁徙率，元',
  `penalty` decimal(11, 2) NULL DEFAULT 0.00 COMMENT '滞纳金总额，元',
  `repayment_penalty` decimal(11, 2) NULL DEFAULT 0.00 COMMENT '滞纳金已还款，元',
  `not_repayment_penalty` decimal(11, 2) NULL DEFAULT 0.00 COMMENT '滞纳金未还款，元',
  `penalty_repayment_rate` decimal(11, 4) NULL DEFAULT NULL COMMENT '滞纳金回款率，元',
  `interest` decimal(11, 2) NULL DEFAULT 0.00 COMMENT '利息总金额，元',
  `repayment_interest` decimal(11, 2) NULL DEFAULT 0.00 COMMENT '利息已还款，元',
  `not_repayment_interest` decimal(11, 2) NULL DEFAULT 0.00 COMMENT '利息未还款，元',
  `interest_repayment_rate` decimal(11, 4) NULL DEFAULT NULL COMMENT '利息回款率，元',
  `order_total` int(0) NULL DEFAULT 0 COMMENT '订单总数',
  `dispose_order_num` int(0) NULL DEFAULT 0 COMMENT '已处理订单数',
  `risk_order_num` int(0) NULL DEFAULT 0 COMMENT '风控标记订单量',
  `repayment_order_num` int(0) NULL DEFAULT 0 COMMENT '已还款订单数',
  `repayment_order_rate` decimal(11, 4) NULL DEFAULT NULL COMMENT '订单还款率',
  `count_date` date NULL DEFAULT NULL COMMENT '统计时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `uk_date`(`person_id`, `count_date`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '每日催收统计报表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for daily_statistics_channel_lend
-- ----------------------------
DROP TABLE IF EXISTS `daily_statistics_channel_lend`;
CREATE TABLE `daily_statistics_channel_lend`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `channel_id` int(0) NOT NULL COMMENT '渠道id',
  `repayment_time` date NULL DEFAULT NULL COMMENT '还款时间',
  `sum_number` int(0) NULL DEFAULT 0 COMMENT '总单量',
  `repayment_number` int(0) NULL DEFAULT 0 COMMENT '已还款',
  `overdue_number` int(0) NULL DEFAULT 0 COMMENT '逾期量',
  `first_borrow` int(0) NULL DEFAULT 0 COMMENT '新用户待还笔数',
  `first_borrow_overdue` int(0) NULL DEFAULT 0 COMMENT '新用户逾期量',
  `old_number` int(0) NULL DEFAULT 0 COMMENT '老用户待还笔数',
  `old_overdue_number` int(0) NULL DEFAULT 0 COMMENT '老用户逾期笔数',
  `total_overdue_count` int(0) NULL DEFAULT 0 COMMENT '总的逾期数',
  `new_total_overdue_count` int(0) NULL DEFAULT 0 COMMENT '新用户总逾期数',
  `old_total_overdue_count` int(0) NULL DEFAULT 0 COMMENT '老用户总逾期数',
  `new_repayment_number` int(0) NULL DEFAULT 0 COMMENT '新用户已还数量',
  `old_repayment_number` int(0) NULL DEFAULT 0 COMMENT '老用户已还数量',
  `renewal_number` int(0) NULL DEFAULT 0 COMMENT '总续期数量',
  `new_renewal_number` int(0) NULL DEFAULT 0 COMMENT '新用户续期数量',
  `old_renewal_number` int(0) NULL DEFAULT 0 COMMENT '老用户续期数量',
  `today_renewal_number` int(0) NULL DEFAULT 0 COMMENT '今日续期数量',
  `ahead_renewal_number` int(0) NULL DEFAULT 0 COMMENT '提前续期数量',
  `new_today_renewal_number` int(0) NULL DEFAULT 0 COMMENT '新用户今日续期数量',
  `old_today_renewal_number` int(0) NULL DEFAULT 0 COMMENT '老用户今日续期数量',
  `new_ahead_renewal_number` int(0) NULL DEFAULT 0 COMMENT '新用户提前续期数量',
  `old_ahead_renewal_number` int(0) NULL DEFAULT 0 COMMENT '老用户提前续期数量',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_chanelId_repayTime`(`channel_id`, `repayment_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 269 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '每日还款统计' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of daily_statistics_channel_lend
-- ----------------------------
INSERT INTO `daily_statistics_channel_lend` VALUES (267, 11, '2022-07-04', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `daily_statistics_channel_lend` VALUES (268, 12, '2022-07-04', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `daily_statistics_channel_lend` VALUES (269, 11, '2022-07-05', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `daily_statistics_channel_lend` VALUES (270, 12, '2022-07-05', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for daily_statistics_channel_product
-- ----------------------------
DROP TABLE IF EXISTS `daily_statistics_channel_product`;
CREATE TABLE `daily_statistics_channel_product`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `product_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '产品名称',
  `report_time` date NULL DEFAULT NULL COMMENT '统计时间',
  `channel_id` int(0) NULL DEFAULT NULL COMMENT '渠道id',
  `plateform_user_id` int(0) NULL DEFAULT NULL COMMENT '渠道运营人员id',
  `uv` int(0) NOT NULL DEFAULT 0 COMMENT '独立访问量',
  `pv` int(0) NOT NULL DEFAULT 0 COMMENT '点击量',
  `register_number` int(0) NOT NULL DEFAULT 0 COMMENT '注册量',
  `register_number_channel` int(0) NOT NULL DEFAULT 0 COMMENT '扣量后的注册数',
  `factor` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '当日扣量系数',
  `pplication_number` int(0) NOT NULL DEFAULT 0 COMMENT '申请人数',
  `sum_channel_cost` int(0) NOT NULL DEFAULT 0 COMMENT '渠道结算费用 单位为分',
  `loan_number_channel` int(0) NOT NULL DEFAULT 0 COMMENT '放款量',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `price` int(0) NULL DEFAULT NULL COMMENT '合作价格 单位分',
  `cooperation_mode` int(0) NULL DEFAULT NULL COMMENT '合作模式：0 cpa, 1 cps ,2 其他,3,uv',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_reporttime_channelid`(`report_time`, `channel_id`) USING BTREE,
  INDEX `code`(`create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 83 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '每日渠道进量统计' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of daily_statistics_channel_product
-- ----------------------------
INSERT INTO `daily_statistics_channel_product` VALUES (83, '', '2022-07-04', 12, 1, 1, 1, 0, 0, 0.00, 0, 0, 0, '2022-07-04 22:14:13', '2022-07-04 22:14:14', 2000, 2);
INSERT INTO `daily_statistics_channel_product` VALUES (84, '', '2022-07-05', 12, 1, 2, 2, 1, 1, 0.00, 0, 0, 0, '2022-07-05 19:01:39', '2022-07-05 20:07:57', 2000, 2);

-- ----------------------------
-- Table structure for increase_money_config
-- ----------------------------
DROP TABLE IF EXISTS `increase_money_config`;
CREATE TABLE `increase_money_config`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `product_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '名称',
  `achieve_times` int(0) NOT NULL COMMENT '次数达到XX次数',
  `increase_money` double NOT NULL DEFAULT 0 COMMENT '还款次数达到XX次提额，提升额度的费率（占总金额的百分比）',
  `reduction_money` double NOT NULL DEFAULT 0 COMMENT '还款次数达到XX次降息，降息的费用的费率（占总金额的百分比）',
  `repetition_inrease_money` int(0) NOT NULL DEFAULT 0 COMMENT '复贷次数达到XX次提升金额为（在贷款规则表中最多借款金额上减或者加；提额类型2时使用）',
  `reduce_interest` double NOT NULL DEFAULT 0 COMMENT '改变利息为（在贷款规则中的利息上减或加 ；类型为2时使用）',
  `increase_type` tinyint(0) NOT NULL DEFAULT 2 COMMENT '提额类型，（0表示正常还款次数，1表示逾期还款次数,2表示复贷次数）',
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT '1970-01-01 08:00:01' COMMENT '更新时间',
  `status` int(0) NOT NULL DEFAULT 1 COMMENT '0: 无效,1:有效',
  `risk_score` int(0) NOT NULL DEFAULT 600 COMMENT '风控分数',
  `service_money` double NOT NULL DEFAULT 0 COMMENT '服务费率',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `id`(`id`) USING BTREE,
  INDEX `times`(`achieve_times`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 44 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '提额降息规则配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of increase_money_config
-- ----------------------------
INSERT INTO `increase_money_config` VALUES (1, '', 1, 0, 0, 200000, 0.007, 2, NULL, '2022-06-26 20:09:28', 1, 0, 0.343);
INSERT INTO `increase_money_config` VALUES (2, '', 2, 0, 0, 200000, 0.007, 2, NULL, '2022-06-26 20:09:28', 1, 0, 0.343);
INSERT INTO `increase_money_config` VALUES (3, '', 3, 0, 0, 300000, 0.007, 2, NULL, '2022-06-26 20:09:28', 1, 0, 0.343);
INSERT INTO `increase_money_config` VALUES (34, '', 4, 0, 0, 200000, 0.007, 2, NULL, '2022-06-26 20:09:28', 1, 0, 0.293);
INSERT INTO `increase_money_config` VALUES (35, '', 5, 0, 0, 300000, 0.007, 2, NULL, '2022-06-26 20:09:28', 1, 0, 0.293);
INSERT INTO `increase_money_config` VALUES (36, '', 6, 0, 0, 200000, 0.007, 2, NULL, '2022-06-26 20:09:28', 1, 0, 0.293);
INSERT INTO `increase_money_config` VALUES (37, '', 7, 0, 0, 300000, 0.007, 2, NULL, '2022-06-26 20:09:28', 1, 0, 0.293);
INSERT INTO `increase_money_config` VALUES (43, '', 0, 0, 0, 30000, 0.007, 3, '2019-05-13 08:00:01', '1970-01-01 08:00:01', 1, 600, 0);

-- ----------------------------
-- Table structure for index_report
-- ----------------------------
DROP TABLE IF EXISTS `index_report`;
CREATE TABLE `index_report`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `report_date` char(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '报告针对数据的日期',
  `loaned_money` decimal(20, 2) NOT NULL DEFAULT 0.00 COMMENT '当日放款成功总额',
  `paid_money` decimal(20, 2) NOT NULL DEFAULT 0.00 COMMENT '当日还款成功总额',
  `renewal_money` decimal(20, 2) NOT NULL DEFAULT 0.00 COMMENT '当日续期成功总额',
  `pay_money` decimal(20, 2) NOT NULL DEFAULT 0.00 COMMENT '当日待还总额',
  `overdue_money` decimal(20, 2) NOT NULL DEFAULT 0.00 COMMENT '当日逾期总额',
  `register_num` int(0) NOT NULL DEFAULT 0 COMMENT '当日注册用户数',
  `loaned_num` int(0) NOT NULL DEFAULT 0 COMMENT '当日放款成功用户数',
  `overdue_num` int(0) NOT NULL DEFAULT 0 COMMENT '当日逾期用户数',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '添加时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '最后更新时间',
  `status` tinyint(0) NOT NULL DEFAULT 1 COMMENT '1.有效数据；2；无效数据',
  `customer_type` tinyint(0) NOT NULL DEFAULT 0 COMMENT '0未知,1.新用户；2；老用户,3全部',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_date`(`report_date`, `customer_type`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 109 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of index_report
-- ----------------------------
INSERT INTO `index_report` VALUES (106, '2022-07-01', 0.00, 0.00, 0.00, 0.00, 0.00, 0, 0, 0, '2022-07-02 04:00:01', '2022-07-02 04:00:01', 1, 3);
INSERT INTO `index_report` VALUES (107, '2022-07-02', 0.00, 0.00, 0.00, 0.00, 0.00, 0, 0, 0, '2022-07-03 04:00:01', '2022-07-03 04:00:01', 1, 3);
INSERT INTO `index_report` VALUES (108, '2022-07-03', 0.00, 0.00, 0.00, 0.00, 0.00, 0, 0, 0, '2022-07-04 04:00:01', '2022-07-04 04:00:01', 1, 3);
INSERT INTO `index_report` VALUES (109, '2022-07-04', 0.00, 0.00, 0.00, 0.00, 0.00, 0, 0, 0, '2022-07-05 04:00:01', '2022-07-05 04:00:01', 1, 3);

-- ----------------------------
-- Table structure for info_index_info
-- ----------------------------
DROP TABLE IF EXISTS `info_index_info`;
CREATE TABLE `info_index_info`  (
  `user_id` int(0) NOT NULL,
  `card_amount` varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL DEFAULT '150000' COMMENT '最大额度',
  `amount_min` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '150000' COMMENT '最小额度',
  `rate` varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL DEFAULT '0.098,0.15' COMMENT '利率',
  `auth_info` int(0) NOT NULL DEFAULT 0 COMMENT '个人信息',
  `auth_info_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '个人信息认证时间',
  `auth_contacts` int(0) NOT NULL DEFAULT 0 COMMENT '联系人',
  `auth_contacts_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '紧急联系人认证时间',
  `auth_bank` int(0) NOT NULL DEFAULT 0 COMMENT '银行卡',
  `auth_bank_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '绑卡时间',
  `auth_sesame` int(0) NOT NULL DEFAULT 0 COMMENT '芝麻信用',
  `auth_sesame_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '芝麻认证时间',
  `auth_mobile` int(0) NOT NULL DEFAULT 0 COMMENT '手机运营商',
  `auth_count` int(0) NOT NULL DEFAULT 5 COMMENT '认证总数',
  `borrow_status` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '借款状态,1：存在借款 0：不存在',
  `bank_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '银行卡号',
  `status` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '1' COMMENT '0:无效 1:有效',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of info_index_info
-- ----------------------------
INSERT INTO `info_index_info` VALUES (0, '150000', '150000', '0.098,0.15', 0, '2020-08-28 01:57:51', 0, '2020-08-28 01:57:51', 0, '2020-08-28 01:57:51', 0, '2020-08-28 01:57:51', 0, 0, '0', NULL, '1');

-- ----------------------------
-- Table structure for info_notice
-- ----------------------------
DROP TABLE IF EXISTS `info_notice`;
CREATE TABLE `info_notice`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `notice_title` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '公告标题',
  `notice_type` int(0) NOT NULL COMMENT '公告类型(1,消息中心公告，2,APP首页轮播公告)',
  `notice_content` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '公告内容',
  `link_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL DEFAULT '' COMMENT '跳转url',
  `status` int(0) NOT NULL COMMENT '0: 不显示, 1:显示',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of info_notice
-- ----------------------------
INSERT INTO `info_notice` VALUES (1, '', 2, '尾号2269，正常还款，成功提额至20000元', '', 1);
INSERT INTO `info_notice` VALUES (2, '', 2, '尾号6547，成功借款15000元，申请至放款耗时3分钟', '', 1);
INSERT INTO `info_notice` VALUES (3, '', 2, '尾号2265，成功借款15000元，申请至放款耗时4分钟', '', 1);
INSERT INTO `info_notice` VALUES (4, '', 2, '尾号1225，正常还款，成功提额至25000元', '', 1);
INSERT INTO `info_notice` VALUES (5, '', 2, '尾号6681，成功借款15000元，申请至放款耗时3分钟', '', 1);
INSERT INTO `info_notice` VALUES (6, '', 2, '尾号5423，成功借款15000元，申请至放款耗时4分钟', '', 1);
INSERT INTO `info_notice` VALUES (7, '', 2, '尾号3212，正常还款，成功提额至30000元', '', 1);
INSERT INTO `info_notice` VALUES (8, '', 2, '尾号7634，成功借款20000元，申请至放款耗时4分钟', '', 1);
INSERT INTO `info_notice` VALUES (9, '客服联系方式与工作时间', 1, '1. 工作时间：\n    审核时间  周一到周日 9：30-21：00\n    非工作时间顺延到第二天审核~\n\n2. 贷前咨询：\n\n    客服微信号：xxx 工作时间同上~非工作时间会联系不上哦~', '', 1);
INSERT INTO `info_notice` VALUES (10, '公告', 3, '', '', 0);

-- ----------------------------
-- Table structure for ip_address_log
-- ----------------------------
DROP TABLE IF EXISTS `ip_address_log`;
CREATE TABLE `ip_address_log`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `ip` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '访问ip',
  `url` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '访问连接',
  `channel_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '渠道编码名称',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `time_index`(`create_time`) USING BTREE,
  INDEX `channel_h5_index`(`channel_code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'ip访问日志' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ip_address_log
-- ----------------------------
INSERT INTO `ip_address_log` VALUES (1, '111.48.134.10', '/h5/login', 'ZmWyx12', '2022-07-04 22:14:13');
INSERT INTO `ip_address_log` VALUES (2, '122.96.31.97', '/h5/login', 'ZmWyx12', '2022-07-05 19:01:39');
INSERT INTO `ip_address_log` VALUES (3, '111.48.134.10', '/h5/login', 'ZmWyx12', '2022-07-05 20:07:57');

-- ----------------------------
-- Table structure for linkface_compareresults
-- ----------------------------
DROP TABLE IF EXISTS `linkface_compareresults`;
CREATE TABLE `linkface_compareresults`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `id_card` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '身份证号码',
  `compare_results` longblob NULL COMMENT '对比结果',
  `compare_status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '对比状态',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for loan_report
-- ----------------------------
DROP TABLE IF EXISTS `loan_report`;
CREATE TABLE `loan_report`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `loan_order_count` int(0) NULL DEFAULT 0 COMMENT '放款总单数',
  `money_amount_count` bigint(0) NULL DEFAULT 0 COMMENT '放款总额',
  `old_loan_order_count` int(0) NULL DEFAULT 0 COMMENT '老用户放款单数',
  `old_loan_money_count` bigint(0) NULL DEFAULT 0 COMMENT '老用户放款总额',
  `new_loan_order_count` int(0) NULL DEFAULT 0 COMMENT '新用户放款单数',
  `new_loan_money_count` bigint(0) NULL DEFAULT 0 COMMENT '新用户放款金额',
  `loan_sevenday_count` int(0) NULL DEFAULT 0 COMMENT '7天期限放款单数',
  `loan_fourteenday_count` int(0) NULL DEFAULT 0 COMMENT '14天期限放款单数',
  `loan_twentyOne_count` int(0) NULL DEFAULT 0 COMMENT '21天期限放款单数',
  `sevenday_moeny_count` bigint(0) NULL DEFAULT 0 COMMENT '7天期限放款总额',
  `fourteenday_money_count` bigint(0) NULL DEFAULT 0 COMMENT '14天期限放款总额',
  `twentyOneday_money_count` int(0) NULL DEFAULT 0 COMMENT '21天期限放款总额',
  `report_date` date NULL DEFAULT NULL COMMENT '报表日期',
  `created_at` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '建表时间',
  `borrow_suc_count` int(0) NULL DEFAULT 0 COMMENT '借款成功人数',
  `register_count` int(0) NULL DEFAULT 0 COMMENT '注册人数',
  `borrow_apply_count` int(0) NULL DEFAULT 0 COMMENT '借款人数',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_date`(`report_date`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 127 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci COMMENT = '每日放款统计报表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of loan_report
-- ----------------------------
INSERT INTO `loan_report` VALUES (126, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, '2022-07-04', '2022-07-04 23:50:01', 0, 0, 0);
INSERT INTO `loan_report` VALUES (127, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, '2022-07-05', '2022-07-05 21:20:01', 0, 1, 0);

-- ----------------------------
-- Table structure for loan_rule_config
-- ----------------------------
DROP TABLE IF EXISTS `loan_rule_config`;
CREATE TABLE `loan_rule_config`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `product_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '产品名称',
  `loan_amount` int(0) NOT NULL DEFAULT 150000 COMMENT '借款金额',
  `expire` int(0) NOT NULL DEFAULT 7 COMMENT '借款期限（天）',
  `inquire` double NOT NULL DEFAULT 0.2 COMMENT '信审查费率（占总金额的百分比 :暂未使用）',
  `account_management` double NOT NULL DEFAULT 0.143 COMMENT '账户管理费率（占总金额的百分比：暂未使用）',
  `service_charge` double NOT NULL DEFAULT 0.343 COMMENT '服务费（占总金额的百分比：服务费取代了之前的信审查费率+账户管理费）',
  `borrow_interest` double NOT NULL DEFAULT 0.007 COMMENT '借款利息利率（占总金额的百分比）',
  `overdue_rate` double NOT NULL COMMENT '逾期一天的利率（占总金额的百分比）用在滞纳金额计算：滞纳金额=借款金额*逾期一天的利率*逾期天数',
  `highest_overdue_rate` double NOT NULL COMMENT '最高滞纳金额的（滞纳金额 < 最高滞纳金额）',
  `renewal_expire` int(0) NOT NULL DEFAULT 6 COMMENT '续期期限（天）',
  `renewal_fee` double NOT NULL COMMENT '续期费服务费（这里按元存的：因为后期可能改成占用百分比）',
  `hightest_renewal` int(0) NOT NULL DEFAULT 0 COMMENT '最多续期',
  `hit_risk_allow_borrow_day` int(0) NOT NULL COMMENT '命中风控多少天后才能再借（天）0表示永远不能再借',
  `hit_black_allow_borrow_day` int(0) NOT NULL COMMENT '命中黑明单后多少天后才能再借（天）0表示久不能再借',
  `normal_repayment_repetition_loan` int(0) NOT NULL DEFAULT 0 COMMENT '正常还款是否可以复贷：0表示\"是\" 1表示不能否',
  `overdue_repayment_repetition_loan` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '逾期7天以下还款是否可以复贷 {\"overdueDay\":\"7\",\"isAllowLoan\":\"1\" }（0表示否不能再借，1表示是允许再借）',
  `create_time` timestamp(0) NOT NULL DEFAULT '1970-01-01 08:00:01' COMMENT '创建时间',
  `update_time` timestamp(0) NULL DEFAULT NULL COMMENT '更新时间',
  `status` int(0) NOT NULL DEFAULT 1 COMMENT '0: 无效,1:有效',
  `channel_id` int(0) NOT NULL COMMENT '渠道id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `id`(`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 34 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '贷款规则配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of loan_rule_config
-- ----------------------------
INSERT INTO `loan_rule_config` VALUES (1, '让我花', 300000, 6, 0.2, 0.143, 0.343, 0.007, 0.2, 2000000, 6, 0, 0, 0, 0, 0, '{\"overdueDay\":7,\"isAllowLoan\":1}', '2019-01-13 17:51:34', '2022-06-26 20:09:28', 1, 0);
INSERT INTO `loan_rule_config` VALUES (2, '让我花', 200000, 7, 0.2, 0.143, 0.433, 0.007, 0.2, 2000000, 6, 20, 0, 0, 0, 0, '{\"overdueDay\":7,\"isAllowLoan\":1}', '2020-08-02 14:19:10', '2020-08-22 16:56:08', 1, 6);
INSERT INTO `loan_rule_config` VALUES (27, '让我花', 300000, 6, 0.2, 0.143, 0.393, 0.007, 0.2, 2000000, 5, 20, 0, 0, 0, 0, '{\"overdueDay\":7,\"isAllowLoan\":1}', '2020-08-28 01:55:43', NULL, 1, 1);
INSERT INTO `loan_rule_config` VALUES (28, '让我花', 300000, 6, 0.2, 0.143, 0.393, 0.007, 0.2, 2000000, 5, 20, 0, 0, 0, 0, '{\"overdueDay\":7,\"isAllowLoan\":1}', '2020-09-14 16:49:37', NULL, 1, 7);
INSERT INTO `loan_rule_config` VALUES (29, '让我花', 200000, 5, 0.2, 0.143, 0.493, 0.007, 0.2, 2000000, 4, 0, 0, 0, 0, 0, '{\"overdueDay\":7,\"isAllowLoan\":1}', '2020-09-17 22:02:42', '2020-10-21 16:49:45', 1, 8);
INSERT INTO `loan_rule_config` VALUES (30, '让我花', 300000, 5, 0.2, 0.143, 0.393, 0.007, 0.2, 2000000, 4, 0, 0, 0, 0, 0, '{\"overdueDay\":7,\"isAllowLoan\":1}', '2022-02-18 22:23:55', '2022-02-20 21:28:22', 1, 9);
INSERT INTO `loan_rule_config` VALUES (31, '让我花', 300000, 5, 0.2, 0.143, 0.393, 0.007, 0.2, 2000000, 4, 0, 0, 0, 0, 0, '{\"overdueDay\":7,\"isAllowLoan\":1}', '2022-06-23 16:18:36', NULL, 1, 10);
INSERT INTO `loan_rule_config` VALUES (32, '让我花', 300000, 5, 0.2, 0.143, 0.393, 0.007, 0.2, 2000000, 4, 0, 0, 0, 0, 0, '{\"overdueDay\":7,\"isAllowLoan\":1}', '2022-06-25 13:09:04', NULL, 1, 11);
INSERT INTO `loan_rule_config` VALUES (33, '让我花', 300000, 6, 0.2, 0.143, 0.343, 0.007, 0.2, 2000000, 6, 0, 0, 0, 0, 0, '{\"overdueDay\":7,\"isAllowLoan\":1}', '2022-06-30 12:55:12', '2022-06-30 13:36:39', 1, 12);

-- ----------------------------
-- Table structure for mman_loan_collection_rule
-- ----------------------------
DROP TABLE IF EXISTS `mman_loan_collection_rule`;
CREATE TABLE `mman_loan_collection_rule`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `company_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '催收公司id',
  `collection_group` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '催收组',
  `every_limit` int(0) NULL DEFAULT 0 COMMENT '每人每天单数上限(单数平均分配，0代表无上限)',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `rule_company_group`(`company_id`, `collection_group`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '催收规则分配表，和催收员表中数据一并显示，关联催收公司表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of mman_loan_collection_rule
-- ----------------------------
INSERT INTO `mman_loan_collection_rule` VALUES ('1', '1', '3', 1000000);
INSERT INTO `mman_loan_collection_rule` VALUES ('2', '1', '4', 1000000);

-- ----------------------------
-- Table structure for mman_loan_collection_status_change_log
-- ----------------------------
DROP TABLE IF EXISTS `mman_loan_collection_status_change_log`;
CREATE TABLE `mman_loan_collection_status_change_log`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `loan_collection_order_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '催收订单ID',
  `before_status` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作前状态',
  `after_status` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作后状态',
  `type` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作类型(操作类型  1入催 2逾期等级转换 3转单 4委外 5催收成功 字典)',
  `create_date` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `operator_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作人',
  `remark` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '操作备注',
  `company_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '催收公司ID',
  `current_collection_user_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '当前催收员UUID',
  `current_collection_user_level` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '当前催收员等级',
  `current_collection_Order_Level` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '当前订单组的等级',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '状态流转表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of mman_loan_collection_status_change_log
-- ----------------------------
INSERT INTO `mman_loan_collection_status_change_log` VALUES ('13c6f115fb664a3bba1ff4e7e2d1c982', 'J20200808180403474786316', NULL, '0', '1', '2020-08-14 01:35:00', '系统', '系统派单，催收人：催1，手机：17828483004', '1', '29', '3', '3');

-- ----------------------------
-- Table structure for money_record
-- ----------------------------
DROP TABLE IF EXISTS `money_record`;
CREATE TABLE `money_record`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `create_date` timestamp(0) NULL DEFAULT NULL,
  `update_date` timestamp(0) NULL DEFAULT NULL,
  `type` tinyint(0) NULL DEFAULT NULL COMMENT '1.注册。2。下单。3。充值。4。其他',
  `phone` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `user_id` int(0) NULL DEFAULT NULL,
  `money` decimal(15, 2) NULL DEFAULT NULL,
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for money_total_census
-- ----------------------------
DROP TABLE IF EXISTS `money_total_census`;
CREATE TABLE `money_total_census`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `current_money` decimal(15, 2) NOT NULL COMMENT '当前余额',
  `total_money` decimal(15, 2) NOT NULL COMMENT '充值余额',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for order_borrow
-- ----------------------------
DROP TABLE IF EXISTS `order_borrow`;
CREATE TABLE `order_borrow`  (
  `id` int(0) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` int(0) UNSIGNED NOT NULL COMMENT '用户id',
  `apply_amount` int(0) NOT NULL DEFAULT 0 COMMENT '申请金额，单位为分',
  `fee_apr` int(0) NOT NULL DEFAULT 0 COMMENT '借款总服务费利率(万分之一)（总服务费率：服务费率+利息）',
  `loan_fee` int(0) NOT NULL DEFAULT 0 COMMENT '借款服务费',
  `into_money` int(0) NOT NULL DEFAULT 0 COMMENT '实际到账金额，单位为分',
  `loan_term_type` tinyint(0) NOT NULL DEFAULT 0 COMMENT '0:按天，1,：按月，2：按年',
  `loan_term` mediumint(0) UNSIGNED NOT NULL COMMENT '根据loan_method确定，几天、几月、几年',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `loan_time` timestamp(0) NULL DEFAULT NULL COMMENT '放款时间，用于计算利息的起止时间',
  `loan_end_time` timestamp(0) NULL DEFAULT NULL COMMENT '需要还款时间',
  `late_fee_apr` smallint(0) NOT NULL DEFAULT 0 COMMENT '滞纳金利率，单位为万分之几',
  `bank_card_id` int(0) NOT NULL DEFAULT 0 COMMENT '银行卡ID',
  `trial_time` timestamp(0) NULL DEFAULT NULL COMMENT '初审时间',
  `trial_remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '初审备注',
  `trial_user_id` mediumint(0) NOT NULL DEFAULT 0 COMMENT '初审用户',
  `review_time` timestamp(0) NULL DEFAULT NULL COMMENT '复审时间',
  `review_remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '复审备注',
  `review_user_id` mediumint(0) NOT NULL DEFAULT 0 COMMENT '复审用户',
  `loan_review_time` timestamp(0) NULL DEFAULT NULL COMMENT '放款审核时间',
  `loan_review_remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '放款审核备注',
  `loan_review_user_id` mediumint(0) NOT NULL DEFAULT 0 COMMENT '放款审核用户',
  `credit_level` tinyint(0) NOT NULL DEFAULT 0 COMMENT '人审机审区分,0:未知,1:人审通过2:机审通过,人审拒绝3,机审拒绝4',
  `hit_risk_times` tinyint(0) NOT NULL DEFAULT 0 COMMENT '命中风险策略次数',
  `customer_type` tinyint(0) NOT NULL DEFAULT 0 COMMENT '是否是老用户：0、新用户；1；老用户',
  `status` tinyint(0) NOT NULL DEFAULT 0 COMMENT '状态：-1取消放款,0:待初审(待机审);1:初审驳回;2初审通过;3待人工审核4复审机审驳回,5复审人审拒绝;;6:放款中;7:放款失败;8已放款，还款中;9:部分还款;10:已还款;11:已逾期;12:已坏账，13逾期已还款；',
  `pay_remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '付款备注',
  `pay_status` tinyint(0) NOT NULL DEFAULT 0 COMMENT '付款状态： 0：初始状态，1：成功请求到银行，2：请求失败，3：请求成功等待结果；4放款成功',
  `out_trade_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '我方订单流水号',
  `flow_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '第三方订单流水号',
  `service_charge` int(0) NOT NULL DEFAULT 0 COMMENT '服务费率(万分之一)',
  `accrual` int(0) NOT NULL DEFAULT 0 COMMENT '利息(万分之一)',
  `create_date` date NULL DEFAULT NULL COMMENT '申请时间年月日',
  `card_no` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT 'usdt提现链接',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `out_trade_no`(`out_trade_no`) USING BTREE,
  UNIQUE INDEX `uk_createTime_uid`(`user_id`, `create_date`) USING BTREE,
  UNIQUE INDEX `flow_no`(`flow_no`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  INDEX `idx_create`(`create_time`) USING BTREE,
  INDEX `idx__customer`(`customer_type`) USING BTREE,
  INDEX `idx_status`(`status`, `pay_status`) USING BTREE,
  INDEX `idx_review_user`(`loan_review_user_id`, `review_user_id`) USING BTREE,
  INDEX `idx_loan_time`(`loan_time`, `loan_end_time`) USING BTREE,
  INDEX `idx_trail_time`(`trial_time`) USING BTREE,
  INDEX `index_level`(`credit_level`) USING BTREE,
  INDEX `index_review`(`review_user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1157 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '借款订单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of order_borrow
-- ----------------------------
INSERT INTO `order_borrow` VALUES (1147, 2, 300000, 4000, 120000, 180000, 0, 5, '2022-04-04 16:31:07', '2022-07-05 21:21:13', NULL, NULL, 1200, 7, '2022-04-04 16:31:18', '未命中风控规则，初审通过', 0, NULL, '', 0, '2022-04-04 16:31:17', '通过机审,需人工复审待复审人工审核', 0, 2, 0, 0, 3, '', 0, '1216490610671571', NULL, 117900, 2100, '2022-04-04', NULL);

-- ----------------------------
-- Table structure for order_collection
-- ----------------------------
DROP TABLE IF EXISTS `order_collection`;
CREATE TABLE `order_collection`  (
  `id` int(0) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `user_id` int(0) UNSIGNED NOT NULL COMMENT '借款用户ID',
  `borrow_id` int(0) UNSIGNED NOT NULL COMMENT '借款订单id',
  `repayment_id` int(0) UNSIGNED NOT NULL COMMENT '总还款记录表ID',
  `last_collection_user_id` mediumint(0) NOT NULL DEFAULT 0 COMMENT '上一催收员ID',
  `current_collection_user_id` mediumint(0) NOT NULL DEFAULT 0 COMMENT '当前催收员ID',
  `current_overdue_level` tinyint(0) NOT NULL DEFAULT 0 COMMENT '当前逾期等级（即逾期字典分组）',
  `overdue_day` int(0) NOT NULL DEFAULT 0 COMMENT '逾期天数',
  `m1_approve_id` mediumint(0) NOT NULL DEFAULT 0 COMMENT 's1审批人ID',
  `m1_operate_status` tinyint(0) NOT NULL DEFAULT 0 COMMENT 's1审批人操作状态（1，已操作过，0未操作过）',
  `m2_approve_id` mediumint(0) NOT NULL DEFAULT 0 COMMENT 's2审批人ID',
  `m2_operate_status` tinyint(0) NOT NULL DEFAULT 0 COMMENT 's2审批人操作状态（1，已操作过，0未操作过）',
  `m3_approve_id` mediumint(0) NOT NULL DEFAULT 0 COMMENT 'm1-m2审批人ID',
  `m3_operate_status` tinyint(0) NOT NULL DEFAULT 0 COMMENT 'm1-m2审批人操作状态（1，已操作过，0未操作过）',
  `m4_approve_id` mediumint(0) NOT NULL DEFAULT 0 COMMENT 'm2-m3审批人ID',
  `m4_operate_status` tinyint(0) NOT NULL DEFAULT 0 COMMENT 'm2-m3审批人操作状态（1，已操作过，0未操作过）',
  `m5_approve_id` mediumint(0) NOT NULL DEFAULT 0 COMMENT 'm3+审批人ID',
  `m5_operate_status` tinyint(0) NOT NULL DEFAULT 0 COMMENT 'm3+审批人操作状态（1，已操作过，0未操作过）',
  `status` tinyint(0) NOT NULL DEFAULT 0 COMMENT '催收状态 0待催收 1催收中 2承诺还款3 待催收（外派）4催收成功 6减免申请 7审核成功 8审核拒绝',
  `promise_repayment_time` timestamp(0) NULL DEFAULT NULL COMMENT '承诺还款时间',
  `last_collection_time` timestamp(0) NULL DEFAULT NULL COMMENT '最后催收时间',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `operator_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '操作人',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '备注',
  `outside_company_id` smallint(0) NOT NULL DEFAULT 0 COMMENT '委外机构ID',
  `deleted` tinyint(0) NOT NULL DEFAULT 0 COMMENT '标记删除 1删除 0有效',
  `important_weight` tinyint(0) NOT NULL DEFAULT 0 COMMENT '标记重要程度 越大越重',
  `loan_real_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '借款人姓名',
  `loan_user_phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '借款人手机号',
  `real_money` int(0) NOT NULL DEFAULT 0 COMMENT '累计实收金额',
  `reduction_money` int(0) NOT NULL DEFAULT 0 COMMENT '累计减免滞纳金',
  `order_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'borrow订单号',
  `dispatch_time` timestamp(0) NULL DEFAULT NULL COMMENT '派单时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_borrow_id`(`borrow_id`) USING BTREE,
  UNIQUE INDEX `uk_repayment_id`(`repayment_id`) USING BTREE,
  INDEX `create_time`(`create_time`) USING BTREE,
  INDEX `status`(`status`) USING BTREE,
  INDEX `current_overdue_level`(`current_overdue_level`) USING BTREE,
  INDEX `current_collection_user_id`(`current_collection_user_id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '催收订单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for order_collection_caller
-- ----------------------------
DROP TABLE IF EXISTS `order_collection_caller`;
CREATE TABLE `order_collection_caller`  (
  `id` int(0) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `user_id` int(0) UNSIGNED NOT NULL COMMENT '用户id',
  `repayment_id` int(0) UNSIGNED NOT NULL COMMENT '总还款记录表ID',
  `last_collection_user_id` mediumint(0) NOT NULL DEFAULT 0 COMMENT '上一催收员ID',
  `current_collection_user_id` mediumint(0) NOT NULL DEFAULT 0 COMMENT '当前催收员ID',
  `status` tinyint(0) NULL DEFAULT 0 COMMENT '催收状态 0：催收中 1：已还款（不再显示）',
  `promise_repayment_time` timestamp(0) NULL DEFAULT NULL COMMENT '承诺还款时间',
  `last_collection_time` timestamp(0) NULL DEFAULT NULL COMMENT '最后催收时间',
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `operator_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '操作人',
  `remark` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '催收情况',
  `deleted` tinyint(0) NULL DEFAULT 0 COMMENT '标记删除 1删除 0有效',
  `dispatch_time` timestamp(0) NULL DEFAULT NULL COMMENT '派单时间',
  `allocation_number` int(0) NULL DEFAULT 1 COMMENT '分配次数（续借客户直接让以前的客服跟进）',
  `annotation` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `create_time`(`create_time`) USING BTREE,
  INDEX `status`(`status`) USING BTREE,
  INDEX `current_collection_user_id`(`current_collection_user_id`) USING BTREE,
  INDEX `repayment_id`(`repayment_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '客服订单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for order_collection_detail
-- ----------------------------
DROP TABLE IF EXISTS `order_collection_detail`;
CREATE TABLE `order_collection_detail`  (
  `id` bigint(0) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `collection_id` int(0) UNSIGNED NOT NULL COMMENT '催收订单ID（借款编号）',
  `collection_user_id` mediumint(0) NOT NULL DEFAULT 0 COMMENT '催收员ID',
  `user_id` int(0) UNSIGNED NOT NULL COMMENT '借款用户ID',
  `contact_type` tinyint(0) NOT NULL DEFAULT 0 COMMENT '联系人类型 0未知 1: 紧急联系人 2:通讯录联系人',
  `contact_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '联系人姓名',
  `relation` tinyint(0) NOT NULL DEFAULT 0 COMMENT '联系人关系 0未知1父亲,2母亲,3本人,4亲人,5朋友,6同事,7其他',
  `contact_phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '联系人电话',
  `press_level` tinyint(0) NOT NULL DEFAULT 0 COMMENT '施压等级',
  `status` tinyint(0) NOT NULL DEFAULT 0 COMMENT '当前催收状态 0待催收、1催收中、2承诺还款、3委外中、4委外成功、5催收成功 字典',
  `collection_type` tinyint(0) NOT NULL DEFAULT 0 COMMENT '催收类型(0待催收 1电话催收、2短信催收)',
  `collected_amount` int(0) NOT NULL DEFAULT 0 COMMENT '催收到的金额',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `collection_tag` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '催收情况',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_collection_user_id`(`collection_user_id`) USING BTREE,
  INDEX `idx_collection_id`(`collection_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '催收记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for order_collection_reduction
-- ----------------------------
DROP TABLE IF EXISTS `order_collection_reduction`;
CREATE TABLE `order_collection_reduction`  (
  `id` bigint(0) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `collection_id` int(0) UNSIGNED NOT NULL COMMENT '催收订单ID（借款编号）',
  `operator_user_id` mediumint(0) NOT NULL DEFAULT 0 COMMENT '减免人员ID',
  `user_id` int(0) UNSIGNED NOT NULL COMMENT '借款用户ID',
  `reduction_amount` mediumint(0) NOT NULL DEFAULT 0 COMMENT '减免金额',
  `audit_status` tinyint(0) NOT NULL DEFAULT 0 COMMENT '审核状态 0待审核、1拒绝、2通过',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '备注',
  `repayment_id` int(0) UNSIGNED NOT NULL COMMENT '还款订单ID',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `collection_id`(`collection_id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  INDEX `audit_status`(`audit_status`) USING BTREE,
  INDEX `repayment_id`(`repayment_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for order_renewal
-- ----------------------------
DROP TABLE IF EXISTS `order_renewal`;
CREATE TABLE `order_renewal`  (
  `id` bigint(0) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `user_id` int(0) UNSIGNED NOT NULL DEFAULT 0 COMMENT '用户ID',
  `repayment_id` int(0) UNSIGNED NOT NULL DEFAULT 0 COMMENT '总还款记录表ID',
  `borrow_id` int(0) UNSIGNED NOT NULL DEFAULT 0 COMMENT '借款记录表ID',
  `repayment_principal` int(0) UNSIGNED NOT NULL DEFAULT 0 COMMENT '待还本金,若部分还款,则小于money_amount借款金额',
  `repayment_fee` int(0) NOT NULL DEFAULT 0 COMMENT '服务费',
  `late_fee` int(0) NOT NULL DEFAULT 0 COMMENT '滞纳金',
  `renewal_fee` int(0) NOT NULL DEFAULT 0 COMMENT '续期费',
  `old_repayment_time` timestamp(0) NULL DEFAULT NULL COMMENT '续期前预期还款时间',
  `renewal_day` tinyint(0) UNSIGNED NOT NULL DEFAULT 0 COMMENT '续期天数',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '备注',
  `status` tinyint(0) NOT NULL DEFAULT 0 COMMENT '付款状态 0未知 1付款中 2付款成功 3付款失败 ',
  `pay_type` tinyint(0) NOT NULL DEFAULT 0 COMMENT '付款方式 0未知 1支付宝 2微信 3银行卡 4其他',
  `renewal_no` tinyint(0) UNSIGNED NOT NULL DEFAULT 0 COMMENT '客户的第几次续借',
  `money_amount` int(0) UNSIGNED NOT NULL DEFAULT 0 COMMENT '借款总金额',
  `repayment_time` timestamp(0) NULL DEFAULT NULL COMMENT '续期后预期还款时间',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `late_day` int(0) NOT NULL DEFAULT 0 COMMENT '滞纳天数',
  `out_trade_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '第三方订单流水号',
  `sign` tinyint(0) NULL DEFAULT 0 COMMENT '标识',
  `renewal_type` tinyint(0) NOT NULL DEFAULT 0 COMMENT '续期类型：0=未知，1=线上续期，2=线下续期',
  `third` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '第三方订单流水号',
  `pay_url` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付链接',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_orderno`(`out_trade_no`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  INDEX `repayment_id`(`repayment_id`) USING BTREE,
  INDEX `status`(`status`) USING BTREE,
  INDEX `create_time`(`create_time`) USING BTREE,
  INDEX `borrow_id`(`borrow_id`) USING BTREE,
  INDEX `repayTime`(`repayment_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 78 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '展期记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for order_repayment
-- ----------------------------
DROP TABLE IF EXISTS `order_repayment`;
CREATE TABLE `order_repayment`  (
  `id` int(0) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `user_id` int(0) UNSIGNED NOT NULL COMMENT '用户ID',
  `borrow_id` int(0) UNSIGNED NOT NULL COMMENT '借款订单id',
  `repayment_amount` int(0) NOT NULL DEFAULT 0 COMMENT '应还金额',
  `paid_amount` int(0) NOT NULL DEFAULT 0 COMMENT '已还金额',
  `principal_amount` int(0) NOT NULL DEFAULT 0 COMMENT '本金',
  `fee_amount` int(0) NOT NULL DEFAULT 0 COMMENT '服务费150',
  `late_fee` int(0) NOT NULL DEFAULT 0 COMMENT '滞纳金',
  `late_fee_apr` smallint(0) NOT NULL DEFAULT 0 COMMENT '逾期利率，单位为万分之几',
  `repayment_time` timestamp(0) NULL DEFAULT NULL COMMENT '预期还款时间',
  `paid_time` timestamp(0) NULL DEFAULT NULL COMMENT '实际还款日期',
  `late_start_time` timestamp(0) NULL DEFAULT NULL COMMENT '滞纳金计算开始时间,预期还款时间的后一天',
  `late_update_time` timestamp(0) NULL DEFAULT NULL COMMENT '滞纳金更新时间',
  `late_day` int(0) NOT NULL DEFAULT 0 COMMENT '滞纳天数',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '备注',
  `status` tinyint(0) NOT NULL DEFAULT 0 COMMENT '总还款状态0:待还款;1:部分还款;2:已还款;3:已逾期;4:逾期已还款，5:已坏账,6提前还款；',
  `collected` tinyint(0) NOT NULL DEFAULT 0 COMMENT '是否进入催收 0否1是',
  `paid_forward` tinyint(0) NOT NULL DEFAULT 0 COMMENT '是否提前还款 0否1是',
  `pay_type` tinyint(0) NOT NULL DEFAULT 0 COMMENT '付款方式 0未知 1支付宝 2微信 3银行卡 4其他',
  `first_repayment_time` timestamp(0) NULL DEFAULT NULL COMMENT '首次预期还款时间',
  `true_late_fee` int(0) NOT NULL DEFAULT 0 COMMENT '已还滞纳金',
  `true_principal` int(0) NOT NULL DEFAULT 0 COMMENT '已还本金',
  `reduce_amount` int(0) NOT NULL DEFAULT 0 COMMENT '减免金额',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `borrow_id`(`borrow_id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  INDEX `status`(`status`) USING BTREE,
  INDEX `create_time`(`create_time`) USING BTREE,
  INDEX `repay_time`(`repayment_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '总还款记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for order_repayment_detail
-- ----------------------------
DROP TABLE IF EXISTS `order_repayment_detail`;
CREATE TABLE `order_repayment_detail`  (
  `id` bigint(0) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `borrow_id` int(0) UNSIGNED NOT NULL COMMENT '原订单表ID',
  `repayment_id` int(0) UNSIGNED NOT NULL COMMENT '总还款记录表ID',
  `user_id` int(0) UNSIGNED NOT NULL COMMENT '借款用户id',
  `paid_amount` int(0) UNSIGNED NOT NULL COMMENT '还款金额',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '备注',
  `third_order_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '三方订单流水',
  `order_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '我方订单号',
  `pay_time` timestamp(0) NULL DEFAULT NULL COMMENT '支付时间',
  `pay_tip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '支付失败原因',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `operator_user_id` mediumint(0) NOT NULL DEFAULT 0 COMMENT '操作人',
  `status` tinyint(0) NOT NULL DEFAULT 0 COMMENT '付款状态 0未知 1付款中 2付款成功 3付款失败 ',
  `overdue` tinyint(0) NOT NULL DEFAULT 0 COMMENT '还款类型 0正常 1逾期 2提前',
  `pay_type` tinyint(0) NOT NULL DEFAULT 0 COMMENT '付款方式 0未知 1代扣 2微信线上 3银行卡线上 4支付宝线上 5微信线下 6银行卡线下 7支付宝线下',
  `pay_url` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付链接',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_orderno`(`order_no`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  INDEX `status`(`status`) USING BTREE,
  INDEX `repayment_id`(`repayment_id`) USING BTREE,
  INDEX `create_time`(`create_time`) USING BTREE,
  INDEX `uk_create_time`(`repayment_id`, `create_time`) USING BTREE,
  INDEX `borrow_id`(`borrow_id`) USING BTREE,
  INDEX `order_no`(`third_order_no`) USING BTREE,
  INDEX `pay_time`(`pay_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '还款记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for phone_code_send
-- ----------------------------
DROP TABLE IF EXISTS `phone_code_send`;
CREATE TABLE `phone_code_send`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机验证码',
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机验证码',
  `create_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for phone_device_info
-- ----------------------------
DROP TABLE IF EXISTS `phone_device_info`;
CREATE TABLE `phone_device_info`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `user_id` int(0) NOT NULL COMMENT '用户ID',
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '手机号',
  `orderId` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '唯一订单号，保证和提交风控请求的订单号一致',
  `merchCode` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '柏特提供唯一商户号',
  `tranTime` datetime(0) NULL DEFAULT NULL COMMENT '系统时间，yyyy-mm-dd hh:mm:ss',
  `timestamp` datetime(0) NULL DEFAULT NULL COMMENT '(当先系统时间戳，也就是sign中使用的时间戳',
  `stepId` tinyint(0) NULL DEFAULT NULL COMMENT '获取设备信息的节点，1：注册 2：登录 3：贷款申请',
  `sign` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '签名串：MD5(username/merchCode + \"-\" + timestamp + \"-\" + userKey + \"-\" + orderId)',
  `phone_device_data` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '手机设备信息',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '手机设备信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for plateform_channel
-- ----------------------------
DROP TABLE IF EXISTS `plateform_channel`;
CREATE TABLE `plateform_channel`  (
  `id` int(0) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `channel_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '渠道商名称',
  `account` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `password` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'qwer1234' COMMENT '密码 默认 qwer1234',
  `salt` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '盐',
  `manager_phone` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `manager_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '渠道商经理名字',
  `plateform_user_id` int(0) NULL DEFAULT 0 COMMENT '操作人',
  `company_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '默认公司' COMMENT '公司名称',
  `price` int(0) NULL DEFAULT NULL COMMENT '合作价格 单位为分',
  `payment_mode` int(0) NULL DEFAULT NULL COMMENT '结算模式：0 对公, 1 对私',
  `payment_type` int(0) NULL DEFAULT NULL COMMENT '结算方式：0 日结,1 月结 2 周结 3 其他，4预付',
  `cooperation_mode` int(0) NULL DEFAULT NULL COMMENT '合作模式：0 cpa, 1 cps ,2 其他，3 uv',
  `decrease_percentage` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '扣量比例',
  `status` tinyint(1) NULL DEFAULT 1 COMMENT '状态：0下线，1上线',
  `link` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '投放链接',
  `short_url` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '短链接',
  `back_stage` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '渠道后天链接',
  `remark` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '备注',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `freeze_status` int(0) NOT NULL DEFAULT 0 COMMENT '冻结状态：0表示未冻结，1表示冻结',
  `forbidden_status` int(0) NOT NULL DEFAULT 0 COMMENT '禁用状态：0表示未禁用，1表示禁用',
  `channel_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '渠道编码',
  `wechat_switch` int(0) NOT NULL DEFAULT 1 COMMENT '微信打开链接开关：0表示允许，1表示禁用',
  `credit_super_switch` int(0) NOT NULL DEFAULT 0 COMMENT '贷超打开链接开关：0表示允许，1表示禁用',
  `browser_switch` int(0) NOT NULL DEFAULT 0 COMMENT '浏览器打开链接开关：0表示允许，1表示禁用',
  `risk_type` tinyint(0) NOT NULL DEFAULT 0 COMMENT '\r\n\r\n该渠道风控类型0=未知4=mb风控，5=pb风控 ',
  `risk_score` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '\r\n风控分数',
  `pc_switch` tinyint(0) NOT NULL DEFAULT 0 COMMENT ' 电脑浏览器开关 0=允许，1=禁用',
  `risk_switch` tinyint(0) NOT NULL DEFAULT 0 COMMENT '风控拒绝开关，0=允许，1=拒绝',
  `strip_switch` tinyint(0) NOT NULL DEFAULT 1 COMMENT '防撸开关 0=开启，1关闭',
  `audit_switch` tinyint(0) NOT NULL DEFAULT 0 COMMENT '审核开关 0=总开关，1手动待人工复审，2自动',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_channel_code`(`channel_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '渠道商' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of plateform_channel
-- ----------------------------
INSERT INTO `plateform_channel` VALUES (11, 'dof', '13115457896', 'ae9brSiA', '', '13115457896', 'asdasd', 1, '默认公司', 100, 0, 0, 3, 0.00, 1, 'http://xiaojuzi.live:8087/index.html?channel_code=Ilpby11', NULL, ' http://xiaojuzi.live:9090/#/login', '', '2022-06-25 13:09:03', '2022-07-05 19:56:37', 0, 0, 'Ilpby11', 1, 0, 0, 1, '400', 0, 0, 0, 0);
INSERT INTO `plateform_channel` VALUES (12, '秒选', '18055557777', 'KbD2RXcF', '', '18055557777', 'aa', 1, '默认公司', 2000, 0, 0, 2, 0.00, 1, 'http://xiaojuzi.live:8087/index.html?channel_code=ZmWyx12', NULL, ' http://xiaojuzi.live:9090/#/login', '', '2022-06-30 12:55:11', '2022-07-05 19:56:15', 0, 0, 'ZmWyx12', 0, 0, 0, 1, '50', 0, 0, 0, 0);

-- ----------------------------
-- Table structure for plateform_channel_report
-- ----------------------------
DROP TABLE IF EXISTS `plateform_channel_report`;
CREATE TABLE `plateform_channel_report`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `report_time` date NULL DEFAULT NULL COMMENT '统计时间',
  `channelid` int(0) NULL DEFAULT NULL COMMENT '渠道id',
  `down_count` int(0) NULL DEFAULT 0 COMMENT '下载量',
  `login_count` int(0) NULL DEFAULT 0 COMMENT '登录人数',
  `register_count` int(0) NULL DEFAULT 0 COMMENT '注册量',
  `idcard_certification_count` int(0) NULL DEFAULT 0 COMMENT '身份认证（对应个人信息认证）',
  `personal_information_count` int(0) NULL DEFAULT 0 COMMENT '个人信息认证（对应紧急联系人认证）',
  `sesame_count` int(0) NULL DEFAULT 0 COMMENT '芝麻分认证人数',
  `operator_count` int(0) NULL DEFAULT 0 COMMENT '运营商认证人数',
  `auth_bank_count` int(0) NULL DEFAULT 0 COMMENT '银行卡认证人数',
  `company_count` int(0) NULL DEFAULT 0 COMMENT '工作信息人数',
  `borrow_apply_count` int(0) NULL DEFAULT 0 COMMENT '申请借款人数',
  `borrow_suc_count` int(0) NULL DEFAULT 0 COMMENT '申请成功人数',
  `loan_count` int(0) NULL DEFAULT 0 COMMENT '放款人数',
  `dedution_coefficient` decimal(11, 4) UNSIGNED NOT NULL COMMENT '系数',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `channel_register_count` int(0) NULL DEFAULT 0 COMMENT '渠道注册量',
  `channel_apply_count` int(0) NULL DEFAULT 0 COMMENT '渠道方的申请量',
  `channel_loan_count` int(0) NULL DEFAULT 0 COMMENT '渠道方的放款数量',
  `hit_system_black` int(0) NOT NULL DEFAULT 0 COMMENT '我方_命中系统黑名单数量',
  `hit_out_black` int(0) NOT NULL DEFAULT 0 COMMENT '我方_命中外部黑名单数量',
  `channel_system_black` int(0) NOT NULL DEFAULT 0 COMMENT '渠道方_命中系统黑名单数量',
  `channel_out_black` int(0) NOT NULL DEFAULT 0 COMMENT '渠道方_命中外部黑名单数量',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_date`(`report_time`, `channelid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 56 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '渠道统计表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of plateform_channel_report
-- ----------------------------
INSERT INTO `plateform_channel_report` VALUES (56, '2022-07-05', 12, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0.0000, '2022-07-05 19:02:04', '2022-07-05 21:09:12', 1, 0, 0, 0, 0, 0, 0);

-- ----------------------------
-- Table structure for platform_advice
-- ----------------------------
DROP TABLE IF EXISTS `platform_advice`;
CREATE TABLE `platform_advice`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机号码',
  `advice_info` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '建议内容',
  `os_version` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '系统版本',
  `app_version` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'app版本',
  `device_id` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备id',
  `device_name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备名',
  `advice_addtime` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '添加时间',
  `handle_status` int(0) NULL DEFAULT 0 COMMENT '处理状态(0:未处理,1:已处理)',
  `handle_date` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '处理时间',
  `hidden` int(0) NULL DEFAULT 0 COMMENT '显示处理(0:显示,1:不显示)',
  `handle_person` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '处理人',
  `handle_status_info` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '处理情况',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for platform_app_base
-- ----------------------------
DROP TABLE IF EXISTS `platform_app_base`;
CREATE TABLE `platform_app_base`  (
  `id` int(0) NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'app名称',
  `app_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'app描述',
  `service_phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '客服电话',
  `img_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图片',
  `weixin` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '客服微信',
  `qq` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '客服QQ',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of platform_app_base
-- ----------------------------
INSERT INTO `platform_app_base` VALUES (1, '小橘子', 'APP介绍', '18888888888', 'https://q9xiaojuzi.oss-cn-hangzhou.aliyuncs.com/img_20220705195839179937250.jpg', '没有', '11111111');

-- ----------------------------
-- Table structure for platform_app_exception
-- ----------------------------
DROP TABLE IF EXISTS `platform_app_exception`;
CREATE TABLE `platform_app_exception`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `user_id` int(0) NOT NULL COMMENT '用户ID',
  `sys_type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '系统(android,ios)',
  `os_version` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '系统版本',
  `app_verion` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'app版本',
  `device_name` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '设备名',
  `device_id` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '设备号',
  `exception` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '异常信息',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for platform_authority
-- ----------------------------
DROP TABLE IF EXISTS `platform_authority`;
CREATE TABLE `platform_authority`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '名称',
  `code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代码',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `parent_id` int(0) NOT NULL DEFAULT 0 COMMENT '父权限节点id',
  `create_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建日期',
  `modify_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改日期',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_code`(`code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 49 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of platform_authority
-- ----------------------------
INSERT INTO `platform_authority` VALUES (1, '主页', 'P1001', NULL, 0, '2018-12-24 18:53:50', '2018-12-25 14:04:38');
INSERT INTO `platform_authority` VALUES (2, '客户管理', 'P1002', NULL, 0, '2018-12-24 18:53:50', '2018-12-25 14:04:38');
INSERT INTO `platform_authority` VALUES (3, '贷前管理', 'P1003', NULL, 0, '2018-12-24 18:53:50', '2018-12-25 14:04:38');
INSERT INTO `platform_authority` VALUES (4, '贷中管理', 'P1004', NULL, 0, '2018-12-24 18:53:50', '2018-12-25 14:04:38');
INSERT INTO `platform_authority` VALUES (5, '贷后管理', 'P1005', NULL, 0, '2018-12-24 18:53:50', '2018-12-25 14:04:38');
INSERT INTO `platform_authority` VALUES (6, '渠道管理', 'P1006', NULL, 0, '2018-12-24 18:53:50', '2018-12-25 14:04:38');
INSERT INTO `platform_authority` VALUES (7, '统计分析', 'P1007', NULL, 0, '2018-12-24 18:53:50', '2018-12-25 14:04:38');
INSERT INTO `platform_authority` VALUES (8, '配置管理', 'P1008', NULL, 0, '2018-12-24 18:53:50', '2018-12-25 14:04:38');
INSERT INTO `platform_authority` VALUES (9, 'APP管理', 'P1009', NULL, 0, '2018-12-24 18:53:50', '2018-12-25 14:04:38');
INSERT INTO `platform_authority` VALUES (10, '客户列表', 'C1001', NULL, 2, '2018-12-24 18:53:50', '2018-12-25 14:04:38');
INSERT INTO `platform_authority` VALUES (11, '未认证列表', 'C1002', NULL, 2, '2018-12-24 18:53:50', '2018-12-25 14:04:38');
INSERT INTO `platform_authority` VALUES (12, '黑白名单列表', 'C1003', NULL, 2, '2018-12-24 18:53:50', '2018-12-25 14:04:38');
INSERT INTO `platform_authority` VALUES (13, '审核列表', 'C1004', NULL, 3, '2018-12-24 18:53:50', '2018-12-25 14:04:38');
INSERT INTO `platform_authority` VALUES (14, '放款记录', 'C1005', NULL, 4, '2018-12-24 18:53:50', '2018-12-25 14:04:38');
INSERT INTO `platform_authority` VALUES (15, '续期记录', 'C1006', NULL, 4, '2018-12-24 18:53:50', '2018-12-25 14:04:38');
INSERT INTO `platform_authority` VALUES (16, '待还记录', 'C1007', NULL, 4, '2018-12-24 18:53:50', '2018-12-25 14:04:38');
INSERT INTO `platform_authority` VALUES (17, '已还记录', 'C1008', NULL, 4, '2018-12-24 18:53:50', '2018-12-25 14:04:38');
INSERT INTO `platform_authority` VALUES (18, '减免订单', 'C1009', NULL, 5, '2018-12-24 18:53:50', '2018-12-25 14:04:38');
INSERT INTO `platform_authority` VALUES (19, '逾期管理', 'C1010', NULL, 5, '2018-12-24 18:53:50', '2018-12-25 14:04:38');
INSERT INTO `platform_authority` VALUES (20, '我的催收', 'C1011', NULL, 5, '2018-12-24 18:53:50', '2018-12-25 14:04:38');
INSERT INTO `platform_authority` VALUES (21, '渠道列表', 'C1012', NULL, 6, '2018-12-24 18:53:50', '2018-12-25 14:04:38');
INSERT INTO `platform_authority` VALUES (22, '渠道数据', 'C1013', NULL, 6, '2018-12-24 18:53:50', '2018-12-25 14:04:38');
INSERT INTO `platform_authority` VALUES (23, '渠道人员统计', 'C1014', NULL, 6, '2018-12-24 18:53:50', '2018-12-25 14:04:38');
INSERT INTO `platform_authority` VALUES (24, '财务统计', 'C1015', NULL, 7, '2018-12-24 18:53:50', '2018-12-25 14:04:38');
INSERT INTO `platform_authority` VALUES (25, '渠道统计', 'C1016', NULL, 7, '2018-12-24 18:53:50', '2018-12-25 14:04:38');
INSERT INTO `platform_authority` VALUES (26, '催收业务统计', 'C1017', NULL, 7, '2018-12-24 18:53:50', '2018-12-25 14:04:38');
INSERT INTO `platform_authority` VALUES (27, '每日放款统计', 'C1018', NULL, 7, '2018-12-24 18:53:50', '2018-12-25 14:04:38');
INSERT INTO `platform_authority` VALUES (28, '每日还款统计', 'C1019', NULL, 7, '2018-12-24 18:53:50', '2018-12-25 14:04:38');
INSERT INTO `platform_authority` VALUES (29, '风控规则配置', 'C1020', NULL, 8, '2018-12-24 18:53:50', '2018-12-25 14:04:38');
INSERT INTO `platform_authority` VALUES (30, '认证规则配置', 'C1021', NULL, 8, '2018-12-24 18:53:50', '2018-12-25 14:04:38');
INSERT INTO `platform_authority` VALUES (31, '贷款规则配置', 'C1022', NULL, 8, '2018-12-24 18:53:50', '2018-12-25 14:04:38');
INSERT INTO `platform_authority` VALUES (32, '账户管理', 'C1023', NULL, 8, '2018-12-24 18:53:50', '2018-12-25 14:04:38');
INSERT INTO `platform_authority` VALUES (33, '角色管理', 'C1024', NULL, 8, '2018-12-24 18:53:50', '2018-12-25 14:04:38');
INSERT INTO `platform_authority` VALUES (34, '基础设置', 'C1025', NULL, 9, '2018-12-24 18:53:50', '2018-12-25 14:04:38');
INSERT INTO `platform_authority` VALUES (35, 'BANNER启动页管理', 'C1026', NULL, 9, '2018-12-24 18:53:50', '2018-12-25 14:04:38');
INSERT INTO `platform_authority` VALUES (36, '信息管理', 'C1027', NULL, 9, '2018-12-24 18:53:50', '2018-12-25 14:04:38');
INSERT INTO `platform_authority` VALUES (37, '消息推送', 'C1028', NULL, 9, '2018-12-24 18:53:50', '2018-12-25 14:04:38');
INSERT INTO `platform_authority` VALUES (38, '意见反馈', 'C1029', NULL, 9, '2018-12-24 18:53:50', '2018-12-25 14:04:38');
INSERT INTO `platform_authority` VALUES (41, '渠道添加', 'C1032', NULL, 21, '2019-04-14 16:02:46', '2019-04-14 16:02:52');
INSERT INTO `platform_authority` VALUES (42, '短信管理', 'C1030', NULL, 9, '2018-12-24 18:53:50', '2018-12-25 14:04:38');
INSERT INTO `platform_authority` VALUES (43, 'APP版本管理', 'C1033', NULL, 9, '2019-05-23 12:09:35', '2019-06-16 21:46:34');
INSERT INTO `platform_authority` VALUES (44, '当日催收', 'C1034', NULL, 5, '2019-06-28 17:53:54', '2019-06-28 17:53:54');
INSERT INTO `platform_authority` VALUES (45, '费用统计', 'C1035', NULL, 7, '2019-07-09 19:22:58', '2019-07-09 19:22:58');
INSERT INTO `platform_authority` VALUES (47, '订单列表', 'C1036', NULL, 4, '2019-07-09 19:22:58', '2019-07-11 11:40:40');
INSERT INTO `platform_authority` VALUES (48, '命中规则统计', 'C1037', NULL, 7, '2019-10-21 11:00:36', '2019-10-21 11:00:58');

-- ----------------------------
-- Table structure for platform_authority_new
-- ----------------------------
DROP TABLE IF EXISTS `platform_authority_new`;
CREATE TABLE `platform_authority_new`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '名称',
  `code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代码',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `parent_id` int(0) NOT NULL DEFAULT 0 COMMENT '父权限节点id',
  `create_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建日期',
  `modify_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改日期',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_code`(`code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 61 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of platform_authority_new
-- ----------------------------
INSERT INTO `platform_authority_new` VALUES (1, '主页', 'P1001', NULL, 0, '2018-12-24 18:53:50', '2018-12-25 14:04:38');
INSERT INTO `platform_authority_new` VALUES (2, '用户信息', 'P1002', NULL, 0, '2018-12-24 18:53:50', '2018-12-25 14:04:38');
INSERT INTO `platform_authority_new` VALUES (3, '借款管理', 'P1003', NULL, 0, '2018-12-24 18:53:50', '2018-12-25 14:04:38');
INSERT INTO `platform_authority_new` VALUES (4, '还款管理', 'P1004', NULL, 0, '2018-12-24 18:53:50', '2018-12-25 14:04:38');
INSERT INTO `platform_authority_new` VALUES (5, '催收管理', 'P1005', NULL, 0, '2018-12-24 18:53:50', '2018-12-25 14:04:38');
INSERT INTO `platform_authority_new` VALUES (6, '渠道管理', 'P1006', NULL, 0, '2018-12-24 18:53:50', '2018-12-25 14:04:38');
INSERT INTO `platform_authority_new` VALUES (7, '统计分析', 'P1007', NULL, 0, '2018-12-24 18:53:50', '2018-12-25 14:04:38');
INSERT INTO `platform_authority_new` VALUES (8, '基础设置', 'P1008', NULL, 0, '2018-12-24 18:53:50', '2018-12-25 14:04:38');
INSERT INTO `platform_authority_new` VALUES (10, '用户列表', 'C1001', NULL, 2, '2018-12-24 18:53:50', '2018-12-25 14:04:38');
INSERT INTO `platform_authority_new` VALUES (12, '黑白名单管理', 'C1003', NULL, 2, '2018-12-24 18:53:50', '2018-12-25 14:04:38');
INSERT INTO `platform_authority_new` VALUES (13, '审核列表', 'C1004', NULL, 3, '2018-12-24 18:53:50', '2018-12-25 14:04:38');
INSERT INTO `platform_authority_new` VALUES (14, '放款记录', 'C1005', NULL, 3, '2018-12-24 18:53:50', '2018-12-25 14:04:38');
INSERT INTO `platform_authority_new` VALUES (15, '续期记录', 'C1006', NULL, 4, '2018-12-24 18:53:50', '2018-12-25 14:04:38');
INSERT INTO `platform_authority_new` VALUES (16, '待还记录', 'C1007', NULL, 4, '2018-12-24 18:53:50', '2018-12-25 14:04:38');
INSERT INTO `platform_authority_new` VALUES (17, '已还记录', 'C1008', NULL, 4, '2018-12-24 18:53:50', '2018-12-25 14:04:38');
INSERT INTO `platform_authority_new` VALUES (19, '逾期订单', 'C1010', NULL, 5, '2018-12-24 18:53:50', '2018-12-25 14:04:38');
INSERT INTO `platform_authority_new` VALUES (20, '我的催收', 'C1011', NULL, 5, '2018-12-24 18:53:50', '2018-12-25 14:04:38');
INSERT INTO `platform_authority_new` VALUES (21, '渠道列表', 'C1012', NULL, 6, '2018-12-24 18:53:50', '2018-12-25 14:04:38');
INSERT INTO `platform_authority_new` VALUES (22, '渠道数据', 'C1013', NULL, 6, '2018-12-24 18:53:50', '2018-12-25 14:04:38');
INSERT INTO `platform_authority_new` VALUES (23, '渠道转化', 'C1014', NULL, 6, '2018-12-24 18:53:50', '2018-12-25 14:04:38');
INSERT INTO `platform_authority_new` VALUES (24, '财务统计', 'C1015', NULL, 7, '2018-12-24 18:53:50', '2018-12-25 14:04:38');
INSERT INTO `platform_authority_new` VALUES (26, '催收统计', 'C1017', NULL, 5, '2018-12-24 18:53:50', '2018-12-25 14:04:38');
INSERT INTO `platform_authority_new` VALUES (27, '当日放款统计', 'C1018', NULL, 7, '2018-12-24 18:53:50', '2018-12-25 14:04:38');
INSERT INTO `platform_authority_new` VALUES (28, '当日还款统计', 'C1019', NULL, 7, '2018-12-24 18:53:50', '2018-12-25 14:04:38');
INSERT INTO `platform_authority_new` VALUES (29, '风控规则设置', 'C1020', NULL, 8, '2018-12-24 18:53:50', '2018-12-25 14:04:38');
INSERT INTO `platform_authority_new` VALUES (31, '贷款规则设置', 'C1022', NULL, 8, '2018-12-24 18:53:50', '2018-12-25 14:04:38');
INSERT INTO `platform_authority_new` VALUES (32, '账号设置', 'C1023', NULL, 8, '2018-12-24 18:53:50', '2018-12-25 14:04:38');
INSERT INTO `platform_authority_new` VALUES (33, '角色设置', 'C1024', NULL, 8, '2018-12-24 18:53:50', '2018-12-25 14:04:38');
INSERT INTO `platform_authority_new` VALUES (38, '意见反馈', 'C1029', NULL, 2, '2018-12-24 18:53:50', '2018-12-25 14:04:38');
INSERT INTO `platform_authority_new` VALUES (41, '新增渠道', 'C1032', NULL, 21, '2019-04-14 16:02:46', '2019-04-14 16:02:52');
INSERT INTO `platform_authority_new` VALUES (44, '当日到期', 'C1034', NULL, 5, '2019-06-28 17:53:54', '2019-06-28 17:53:54');
INSERT INTO `platform_authority_new` VALUES (45, '费用统计', 'C1035', NULL, 7, '2019-07-09 19:22:58', '2019-07-09 19:22:58');
INSERT INTO `platform_authority_new` VALUES (46, '订单管理', 'P1010', NULL, 0, '2018-12-24 18:53:50', '2018-12-25 14:04:38');
INSERT INTO `platform_authority_new` VALUES (47, '订单列表', 'C1036', NULL, 46, '2019-07-09 19:22:58', '2019-07-09 19:22:58');
INSERT INTO `platform_authority_new` VALUES (48, 'app管理', 'C1039', NULL, 8, '2019-07-09 19:22:58', '2019-07-09 19:22:58');
INSERT INTO `platform_authority_new` VALUES (49, '审核列表搜索', 'S1001', '新配的功能权限code，都以S开头，因为要重新封装一个功能权限对象 给前端，除功能级别权限，其他任何code不能以S开头', 13, '2019-07-20 11:01:13', '2019-07-20 16:47:10');
INSERT INTO `platform_authority_new` VALUES (50, '审核列表重新机审', 'S1002', NULL, 13, '2019-07-20 11:08:57', '2019-07-20 12:09:58');
INSERT INTO `platform_authority_new` VALUES (51, '审核列表人工复审', 'S1003', NULL, 13, '2019-07-20 11:09:25', '2019-07-20 12:10:01');
INSERT INTO `platform_authority_new` VALUES (54, '用户列表搜索', 'S1004', NULL, 10, '2019-07-20 15:51:57', '2019-07-20 16:10:32');
INSERT INTO `platform_authority_new` VALUES (55, '用户列表手动提额', 'S1005', NULL, 10, '2019-07-20 15:52:13', '2019-07-20 16:10:29');
INSERT INTO `platform_authority_new` VALUES (56, '意见反馈搜索', 'S1006', NULL, 38, '2019-07-20 15:52:49', '2019-07-20 16:10:26');
INSERT INTO `platform_authority_new` VALUES (57, '意见反馈处理', 'S1007', NULL, 38, '2019-07-20 16:01:06', '2019-07-20 16:03:08');
INSERT INTO `platform_authority_new` VALUES (58, '当日催收转派', 'S1008', NULL, 44, '2019-07-24 16:10:36', '2019-07-24 16:10:36');
INSERT INTO `platform_authority_new` VALUES (59, '帮助中心', 'S1009', NULL, 0, '2019-08-06 10:33:23', '2019-08-06 10:33:23');
INSERT INTO `platform_authority_new` VALUES (60, '规则命中统计', 'C1040', NULL, 7, '2019-10-21 10:55:30', '2019-10-21 10:56:45');

-- ----------------------------
-- Table structure for platform_banner
-- ----------------------------
DROP TABLE IF EXISTS `platform_banner`;
CREATE TABLE `platform_banner`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `equement_type` tinyint(0) NULL DEFAULT 0 COMMENT '设备类型，0表示PC端，1移动端，2表示其他',
  `type` tinyint(0) NULL DEFAULT NULL COMMENT '类型(0表示banner,1表示启动页)',
  `add_person` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '添加人员',
  `channel_id` tinyint(0) NULL DEFAULT 0 COMMENT '渠道id',
  `user_level` tinyint(0) NULL DEFAULT 0 COMMENT '预留字段',
  `present_way` tinyint(0) NULL DEFAULT 0 COMMENT '发布方式，0表示立即发布，1表示定时发布',
  `start_time` datetime(0) NULL DEFAULT NULL COMMENT '活动开始时间',
  `end_time` datetime(0) NULL DEFAULT NULL COMMENT '活动结束时间',
  `sort` tinyint(0) NULL DEFAULT NULL COMMENT '排序',
  `status` tinyint(1) UNSIGNED ZEROFILL NULL DEFAULT NULL COMMENT '正常（0）禁用（1）',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '链接地址',
  `reurl` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '本地图片上传获得的地址',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标题',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of platform_banner
-- ----------------------------
INSERT INTO `platform_banner` VALUES (1, 1, 0, NULL, 0, 0, 0, NULL, '2019-04-09 11:27:18', 1, 0, 'https://q9bucket.oss-cn-hangzhou.aliyuncs.com/app/banner/banner_1.png', 'https://qiniu.51hongwen.com/banner1%20%E5%89%AF%E6%9C%AC.png', 'bannel1', '2019-04-09 11:30:15', '2020-08-15 00:49:54');
INSERT INTO `platform_banner` VALUES (2, 1, 0, NULL, 0, 0, 0, NULL, '2019-04-09 11:27:18', 2, 0, 'https://q9bucket.oss-cn-hangzhou.aliyuncs.com/app/banner/banner_2.png', 'https://qiniu.51hongwen.com/banner2%20%E5%89%AF%E6%9C%AC.png', 'bannel1', '2019-04-09 11:30:15', '2020-08-15 00:49:54');
INSERT INTO `platform_banner` VALUES (3, 1, 0, NULL, 0, 0, 0, NULL, '2019-04-09 11:27:18', 3, 0, 'https://q9bucket.oss-cn-hangzhou.aliyuncs.com/app/banner/banner_3.png', 'https://qiniu.51hongwen.com/banner2%20%E5%89%AF%E6%9C%AC.png', 'bannel1', '2019-04-09 11:30:15', '2020-08-15 00:49:55');

-- ----------------------------
-- Table structure for platform_base_configuration
-- ----------------------------
DROP TABLE IF EXISTS `platform_base_configuration`;
CREATE TABLE `platform_base_configuration`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `sys_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '参数名称',
  `sys_value` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '参数值',
  `sys_value_big` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '文本类型值',
  `sys_key` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '参数键',
  `sys_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '参数类型',
  `input_type` enum('text','textarea','password','image') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '页面输入类型',
  `remark` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '参数说明',
  `limit_code` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `desc` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '该字段用户各项说明，不用做前台显示',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `unique`(`sys_key`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of platform_base_configuration
-- ----------------------------
INSERT INTO `platform_base_configuration` VALUES (1, '默认展示的借款金额', '100000', NULL, '0', '0', NULL, NULL, NULL, NULL);
INSERT INTO `platform_base_configuration` VALUES (2, '默认展示借款期限', '7', NULL, '1', '0', 'text', NULL, NULL, NULL);

-- ----------------------------
-- Table structure for platform_channel_staff
-- ----------------------------
DROP TABLE IF EXISTS `platform_channel_staff`;
CREATE TABLE `platform_channel_staff`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `report_time` timestamp(0) NULL DEFAULT NULL COMMENT '统计时间',
  `plateform_user_id` int(0) NULL DEFAULT NULL COMMENT '运营人员',
  `delivery_connection` int(0) NOT NULL DEFAULT 0 COMMENT '投放连接数',
  `really_register` int(0) NOT NULL DEFAULT 0 COMMENT '真实注册数',
  `channel_register` int(0) NOT NULL DEFAULT 0 COMMENT '渠道注册数',
  `loan_cost` int(0) NOT NULL DEFAULT 0 COMMENT '放款人数：用于计算放款成本（投放金额/放款人数）',
  `register_cost` int(0) NOT NULL DEFAULT 0 COMMENT '注册成本，以分为单位',
  `channel_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '渠道名',
  `price` int(0) NOT NULL DEFAULT 0 COMMENT '合作价格，以分为单位',
  `expense` int(0) NOT NULL DEFAULT 0 COMMENT '结算金额，以分为单位',
  `decrease_percentage` decimal(11, 4) NOT NULL COMMENT '扣量系数',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `channel_name`(`channel_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 71 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of platform_channel_staff
-- ----------------------------
INSERT INTO `platform_channel_staff` VALUES (71, '2022-07-05 19:02:04', 1, 0, 1, 1, 0, 0, '秒选', 2000, 0, 0.0000);

-- ----------------------------
-- Table structure for platform_configuration
-- ----------------------------
DROP TABLE IF EXISTS `platform_configuration`;
CREATE TABLE `platform_configuration`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `user_id` int(0) NOT NULL COMMENT '用户id',
  `borrow_amount` int(0) NOT NULL DEFAULT 0 COMMENT '申请金额，单位为分',
  `borrow_period` int(0) NOT NULL DEFAULT 0 COMMENT '借款期限',
  `inquire` int(0) NOT NULL DEFAULT 0 COMMENT '信审查询费',
  `borrow_interest` int(0) NOT NULL DEFAULT 0 COMMENT '借款利息',
  `account_management` int(0) NOT NULL DEFAULT 0 COMMENT '账户管理费',
  `actual` int(0) NOT NULL DEFAULT 0 COMMENT '实际利息利率',
  `actual_pay` int(0) NOT NULL DEFAULT 0 COMMENT '到期应还',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `normal_repayment_times` int(0) NOT NULL DEFAULT 0 COMMENT '正常还款次数',
  `overdue_repayment_times` int(0) NOT NULL COMMENT '逾期还款次数',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for platform_help_center
-- ----------------------------
DROP TABLE IF EXISTS `platform_help_center`;
CREATE TABLE `platform_help_center`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '内容',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for platform_information_management
-- ----------------------------
DROP TABLE IF EXISTS `platform_information_management`;
CREATE TABLE `platform_information_management`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '内容',
  `add_person` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '添加人员',
  `type` tinyint(1) NULL DEFAULT NULL COMMENT '归属类目（1表示帮助中心，2表示公告）',
  `modify_person` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人员',
  `views` int(0) NULL DEFAULT NULL COMMENT '浏览量',
  `order_by` tinyint(1) NULL DEFAULT NULL COMMENT '排序',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for platform_message_push
-- ----------------------------
DROP TABLE IF EXISTS `platform_message_push`;
CREATE TABLE `platform_message_push`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '标题',
  `content` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '内容',
  `add_person` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '添加人员',
  `type` int(0) NOT NULL COMMENT '推送类型（1、短信、2表示PUSH手机端，3表示站内信）',
  `object` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '对象',
  `send_nums` int(0) NULL DEFAULT NULL COMMENT '发送数量',
  `pv` int(0) NULL DEFAULT NULL COMMENT 'pv',
  `uv` int(0) NULL DEFAULT NULL COMMENT 'uv',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建日期',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改日期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '消息推送' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for platform_protocol
-- ----------------------------
DROP TABLE IF EXISTS `platform_protocol`;
CREATE TABLE `platform_protocol`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '姓名',
  `phone` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `id_card` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '身份证号',
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '标题',
  `content` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '内容',
  `type` tinyint(1) NULL DEFAULT NULL COMMENT '协议类型（）',
  `status` tinyint(1) NULL DEFAULT NULL COMMENT '状态（0正常，1禁用）',
  `ip` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'ip地址',
  `party_b` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '乙方',
  `party_c` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '丙方',
  `party_d` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '丁方',
  `add_person` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '添加人员',
  `image_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图片地址',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建日期',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改日期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '协议' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of platform_protocol
-- ----------------------------
INSERT INTO `platform_protocol` VALUES (1, '修改人员111', '1381888888', '45226+626231221', '协议标题111', '内容111', 0, 1, '192.168.1.1', NULL, NULL, NULL, NULL, NULL, '2019-02-27 14:25:08', '2019-02-28 15:49:06');
INSERT INTO `platform_protocol` VALUES (3, '接口', '13838647899', '412722189503045274', '平台服务协议', ' <h4>第一条 协议成立</h4>\r\n        <p>乙方在甲方或甲方合作方平台上通过类似点击确认按钮后，本协议及附件一《授权扣款委托书》、附件二《法律文书送达地址确认书》即时成立并生效。本协议以数据电文的形式存储于甲方或甲方合作方服务器中，乙方对此予以确认并同意。</p>\r\n        <br />\r\n        <h4>第二条	定义</h4>\r\n        <p>除本协议另有规定外，本协议中下列用语的定义如下：</p>\r\n        <p>1、借款（居间）协议：指乙方通过甲方平台签订的《借款（居间）协议》，《借款（居间）协议》与本协议同时签订；</p>\r\n        <p>2、平台服务：指乙方同意甲方及其合作方对乙方在《借款（居间）协议》项下款项的获取与偿还进行相关管理并提供服务，包括但不限于媒介服务、贷后管理、逾期管理、提前结清等；</p>\r\n        <p>3、出借人：指本条第一款《借款（居间）协议》项下的出借人；</p>\r\n        <p>4、工作日：指除国家法定节假日、公休日以外的甲方对外办理业务的任何一日。</p>\r\n        <br />\r\n        <h4> 第三条	服务内容</h4>\r\n        <p>一、居间服务</p>\r\n        <p>甲方及甲方合作方为乙方提供居间服务并收取一定数额的综合服务费</p>\r\n        <p>1、媒介服务：甲方利用自有资源和平台优势，为乙方在甲方平台上发起的借款寻找合适出借人，促成双方签订《借款（居间）协议》，实现乙方获得出借人出借款项；甲方有权根据业务经营情况为乙方选择介绍合适出借人的合作方居间平台，乙方同意并授权甲方于借款起息日预先收取综合服务费，并在发放借款时即从本金金额中扣除。</p>\r\n        <p>2、贷后管理：《借款（居间）协议》生效后，甲方对《借款（居间）协议》项下借款及乙方进行监督和督促，并有权收取分期服务费，贷后管理服务内容包括：</p>\r\n        <p><span>（1）对乙方的履约情况、经营及财务状况等进行监督检查；</span></p>\r\n        <p><span>（2）对乙方在《借款（居间）协议》项下的借款资金的使用情况进行监督检查；</span></p>\r\n        <p><span>（3）督促乙方根据《借款（居间）协议》的约定按时还款；</span></p>\r\n        <p><span>（4）与银行和/或第三方支付机构开展合作，对《借款（居间）协议》项下各类金额提供电子数据信息计算与统计，对法院需要认定的必要费用提供财务凭证。</span></p>\r\n        <p>3、甲方无法也没有义务保证乙方在发出借款要约意向后，能够实际成功获得借款，由此可能导致的损失由乙方自行承担，甲方不承担任何责任。</p>\r\n        <br />\r\n        <p>二、逾期管理</p>\r\n        <p>乙方未能在《借款（居间）协议》任何一个还款日24:00前足额支付还款金额至乙方还款账户的，视作乙方逾期还款，甲方即对乙方的借款进行逾期管理，逾期管理服务及相关内容包括：</p>\r\n        <p>1、依照各方其他协议使用保证金对逾期借款在约定范围内进行代偿；</p>\r\n        <p>2、直接或间接向乙方发送通知进行款项追偿，包括但不限于以手机短信、电话、信函、电子邮件、网站通知或其他合法方式提醒或催告乙方履行《借款（居间）协议》项下的还款义务；</p>\r\n        <p>3、逾期管理款项的范围包括但不限于本金、利息、综合服务费、逾期管理费等。</p>\r\n        <P>4、乙方发生逾期还款的，自还款日后第1日起向甲方支付逾期管理费，至乙方全额支付应付款项为止。</P>\r\n        <br />\r\n        <p>三、提前结清</p>\r\n        <p>1、乙方可以发起提前结清服务申请，服务是否可用以操作界面展示为准（受银行结算等影响，部分日期可能无法发起申请）。</p>\r\n        <p>2、乙方选择提前结清的：乙方应偿还未到期全部款项。</p>\r\n        <br />\r\n        <h4>第四条	费用</h4>\r\n        <p>1、综合服务费：指乙方因甲方或其合作方所提供的媒介服务、贷后管理等服务向甲方支付的费用；甲方在借款起息日收取。（详见附表）</p>\r\n        <p>2、逾期管理费：以逾期未还金额为基数按日收取，按照逾期未还金额计算，包括逾期未还本金、利息之和，以当日0:00至24:00为一日，丙方每日收取用户逾期未还金额的2%作为逾期管理费。</p>\r\n        <p>3、其他甲方可能发生的，且须由乙方承担的费用，包括但不限于：</p>\r\n        <p><span>（1）因代偿债务发生的税费；</span></p>\r\n        <p><span>（2）诉讼、仲裁过程中支出的费用，包括法院、仲裁机构收取的费用等；</span></p>\r\n        <p><span>（3）审计费、拍卖费、律师费等中介费用；</span></p>\r\n        <p><span>（4）包括但不限于交通费、餐费等必要差旅费用。</span></p>\r\n        <br />\r\n        <h4>第五条 服务期限</h4>\r\n        <p>本协议项下服务期限自本协议生效之日起，至乙方在《借款（居间）协议》项下的所有义务全部履行完毕，以及乙方支付本协议项下的全部费用完毕之日止。</p>', 0, 0, '192.168.1.168', NULL, NULL, NULL, NULL, NULL, '2019-02-27 14:30:59', '2019-02-28 15:49:15');

-- ----------------------------
-- Table structure for platform_role
-- ----------------------------
DROP TABLE IF EXISTS `platform_role`;
CREATE TABLE `platform_role`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '名称',
  `code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代码',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `role_super_id` int(0) NOT NULL COMMENT '角色上级id',
  `status` tinyint(1) NULL DEFAULT 0 COMMENT '角色状态 0：正常 1：禁用；2：删除',
  `create_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建日期',
  `modify_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改日期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of platform_role
-- ----------------------------
INSERT INTO `platform_role` VALUES (1, 'admin', '', '超级管理员', 737, 0, '2018-12-22 11:32:40', '2019-03-05 10:18:45');
INSERT INTO `platform_role` VALUES (2, 'cuishou_admin', '', '逾期主管', 1, 0, '2018-12-22 11:32:40', '2019-07-26 15:53:40');
INSERT INTO `platform_role` VALUES (3, 'channel_admin', '', '渠道主管', 742, 0, '2018-12-22 11:32:40', '2019-04-14 15:51:24');
INSERT INTO `platform_role` VALUES (4, 'auditor', '', '审核', 1, 0, '2018-12-22 11:32:40', '2019-03-05 10:18:45');
INSERT INTO `platform_role` VALUES (5, 'waiter', '', '客服', 737, 0, '2018-12-22 11:32:40', '2019-03-05 10:18:45');
INSERT INTO `platform_role` VALUES (6, 'finance', '', '财务', 737, 0, '2018-12-22 11:32:40', '2019-04-17 10:22:46');
INSERT INTO `platform_role` VALUES (7, 'channel', '', '渠道专员', 3, 0, '2018-12-22 11:32:40', '2019-03-05 10:18:45');
INSERT INTO `platform_role` VALUES (8, 'cuishou', '', '逾期专员', 2, 0, '2018-12-22 11:32:40', '2019-07-26 15:53:49');
INSERT INTO `platform_role` VALUES (9, 'channel_user', NULL, '渠道方用户', 760, 0, '2019-04-07 18:37:12', '2019-04-08 17:21:00');
INSERT INTO `platform_role` VALUES (10, 'collection_today', NULL, '当日催收专员', 1, 0, '2019-07-10 10:23:43', '2019-07-26 15:53:27');
INSERT INTO `platform_role` VALUES (11, 'collection_today_admin', NULL, '当日催收主管', 1, 0, '2019-07-24 15:44:05', '2019-07-24 15:44:36');
INSERT INTO `platform_role` VALUES (13, 'waiter_today', NULL, '当日客服', 1, 0, '2019-08-23 16:30:01', '2019-08-23 16:31:18');

-- ----------------------------
-- Table structure for platform_role_authority
-- ----------------------------
DROP TABLE IF EXISTS `platform_role_authority`;
CREATE TABLE `platform_role_authority`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `role_Id` int(0) NOT NULL COMMENT '角色',
  `authority_Id` int(0) NULL DEFAULT NULL COMMENT '权限',
  `create_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建日期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 137 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of platform_role_authority
-- ----------------------------
INSERT INTO `platform_role_authority` VALUES (1, 1, 1, '2019-03-04 21:36:12');
INSERT INTO `platform_role_authority` VALUES (2, 1, 2, '2019-03-04 21:36:12');
INSERT INTO `platform_role_authority` VALUES (3, 1, 3, '2019-03-04 21:36:12');
INSERT INTO `platform_role_authority` VALUES (4, 1, 4, '2019-03-04 21:36:12');
INSERT INTO `platform_role_authority` VALUES (5, 1, 5, '2019-03-04 21:36:12');
INSERT INTO `platform_role_authority` VALUES (6, 1, 6, '2019-03-04 21:36:12');
INSERT INTO `platform_role_authority` VALUES (7, 1, 7, '2019-03-04 21:36:12');
INSERT INTO `platform_role_authority` VALUES (8, 1, 8, '2019-03-04 21:36:12');
INSERT INTO `platform_role_authority` VALUES (9, 1, 9, '2019-03-04 21:36:12');
INSERT INTO `platform_role_authority` VALUES (10, 1, 10, '2019-03-04 21:36:12');
INSERT INTO `platform_role_authority` VALUES (11, 1, 11, '2019-03-04 21:36:12');
INSERT INTO `platform_role_authority` VALUES (12, 1, 12, '2019-03-04 21:36:12');
INSERT INTO `platform_role_authority` VALUES (13, 1, 13, '2019-03-04 21:36:12');
INSERT INTO `platform_role_authority` VALUES (14, 1, 14, '2019-03-04 21:36:12');
INSERT INTO `platform_role_authority` VALUES (15, 1, 15, '2019-03-04 21:36:12');
INSERT INTO `platform_role_authority` VALUES (16, 1, 16, '2019-03-04 21:36:12');
INSERT INTO `platform_role_authority` VALUES (17, 1, 17, '2019-03-04 21:36:12');
INSERT INTO `platform_role_authority` VALUES (18, 1, 18, '2019-03-04 21:36:12');
INSERT INTO `platform_role_authority` VALUES (19, 1, 19, '2019-03-04 21:36:12');
INSERT INTO `platform_role_authority` VALUES (20, 1, 20, '2019-03-04 21:36:12');
INSERT INTO `platform_role_authority` VALUES (21, 1, 21, '2019-03-04 21:36:12');
INSERT INTO `platform_role_authority` VALUES (22, 1, 22, '2019-03-04 21:36:12');
INSERT INTO `platform_role_authority` VALUES (23, 1, 23, '2019-03-04 21:36:12');
INSERT INTO `platform_role_authority` VALUES (24, 1, 24, '2019-03-04 21:36:12');
INSERT INTO `platform_role_authority` VALUES (25, 1, 25, '2019-03-04 21:36:12');
INSERT INTO `platform_role_authority` VALUES (26, 1, 26, '2019-03-04 21:36:12');
INSERT INTO `platform_role_authority` VALUES (27, 1, 27, '2019-03-04 21:36:12');
INSERT INTO `platform_role_authority` VALUES (28, 1, 28, '2019-03-04 21:36:12');
INSERT INTO `platform_role_authority` VALUES (29, 1, 29, '2019-03-04 21:36:12');
INSERT INTO `platform_role_authority` VALUES (30, 1, 30, '2019-03-04 21:36:12');
INSERT INTO `platform_role_authority` VALUES (31, 1, 31, '2019-03-04 21:36:12');
INSERT INTO `platform_role_authority` VALUES (32, 1, 32, '2019-03-04 21:36:12');
INSERT INTO `platform_role_authority` VALUES (33, 1, 33, '2019-03-04 21:36:12');
INSERT INTO `platform_role_authority` VALUES (34, 1, 34, '2019-03-04 21:36:12');
INSERT INTO `platform_role_authority` VALUES (35, 1, 35, '2019-03-04 21:36:12');
INSERT INTO `platform_role_authority` VALUES (36, 1, 36, '2019-03-04 21:36:12');
INSERT INTO `platform_role_authority` VALUES (37, 1, 37, '2019-03-04 21:36:12');
INSERT INTO `platform_role_authority` VALUES (38, 1, 38, '2019-03-04 21:36:12');
INSERT INTO `platform_role_authority` VALUES (39, 2, 18, '2019-03-04 21:36:12');
INSERT INTO `platform_role_authority` VALUES (40, 2, 19, '2019-03-04 21:36:12');
INSERT INTO `platform_role_authority` VALUES (41, 2, 32, '2019-03-04 21:36:12');
INSERT INTO `platform_role_authority` VALUES (48, 4, 13, '2019-03-04 21:36:12');
INSERT INTO `platform_role_authority` VALUES (49, 5, 10, '2019-03-04 21:36:12');
INSERT INTO `platform_role_authority` VALUES (50, 5, 12, '2019-03-04 21:36:12');
INSERT INTO `platform_role_authority` VALUES (51, 6, 14, '2019-03-04 21:36:12');
INSERT INTO `platform_role_authority` VALUES (52, 6, 15, '2019-03-04 21:36:12');
INSERT INTO `platform_role_authority` VALUES (53, 6, 16, '2019-03-04 21:36:12');
INSERT INTO `platform_role_authority` VALUES (54, 6, 17, '2019-03-04 21:36:12');
INSERT INTO `platform_role_authority` VALUES (55, 6, 24, '2019-03-04 21:36:12');
INSERT INTO `platform_role_authority` VALUES (56, 6, 27, '2019-03-04 21:36:12');
INSERT INTO `platform_role_authority` VALUES (57, 6, 28, '2019-03-04 21:36:12');
INSERT INTO `platform_role_authority` VALUES (58, 7, 21, '2019-03-04 21:36:12');
INSERT INTO `platform_role_authority` VALUES (59, 7, 22, '2019-03-04 21:36:12');
INSERT INTO `platform_role_authority` VALUES (60, 8, 20, '2019-03-04 21:36:12');
INSERT INTO `platform_role_authority` VALUES (61, 19, 1, '2019-04-14 00:36:12');
INSERT INTO `platform_role_authority` VALUES (69, 20, 2, '2019-04-14 10:05:24');
INSERT INTO `platform_role_authority` VALUES (70, 20, 10, '2019-04-14 10:05:24');
INSERT INTO `platform_role_authority` VALUES (71, 20, 11, '2019-04-14 10:05:24');
INSERT INTO `platform_role_authority` VALUES (72, 20, 12, '2019-04-14 10:05:24');
INSERT INTO `platform_role_authority` VALUES (73, 1, 41, '2019-04-14 16:04:15');
INSERT INTO `platform_role_authority` VALUES (74, 7, 25, '2019-04-14 23:14:58');
INSERT INTO `platform_role_authority` VALUES (76, 1, 39, '2019-04-15 21:47:43');
INSERT INTO `platform_role_authority` VALUES (77, 1, 40, '2019-04-15 21:47:53');
INSERT INTO `platform_role_authority` VALUES (80, 3, 21, '2019-04-15 23:06:23');
INSERT INTO `platform_role_authority` VALUES (81, 3, 22, '2019-04-15 23:06:23');
INSERT INTO `platform_role_authority` VALUES (82, 3, 25, '2019-04-15 23:06:23');
INSERT INTO `platform_role_authority` VALUES (83, 3, 26, '2019-04-15 23:06:23');
INSERT INTO `platform_role_authority` VALUES (84, 3, 6, '2019-04-15 23:06:23');
INSERT INTO `platform_role_authority` VALUES (85, 3, 7, '2019-04-15 23:06:23');
INSERT INTO `platform_role_authority` VALUES (97, 9, 25, '2019-04-17 13:29:18');
INSERT INTO `platform_role_authority` VALUES (98, 9, 7, '2019-04-17 13:29:18');
INSERT INTO `platform_role_authority` VALUES (99, 2, 20, '2019-05-05 19:48:00');
INSERT INTO `platform_role_authority` VALUES (100, 5, 42, '2019-03-04 21:36:12');
INSERT INTO `platform_role_authority` VALUES (101, 1, 42, '2019-03-04 21:36:12');
INSERT INTO `platform_role_authority` VALUES (102, 1, 43, '2019-05-23 12:11:06');
INSERT INTO `platform_role_authority` VALUES (104, 1, 44, '2019-07-07 12:01:10');
INSERT INTO `platform_role_authority` VALUES (105, 1, 45, '2019-07-07 12:01:21');
INSERT INTO `platform_role_authority` VALUES (106, 10, 5, '2019-07-07 12:02:44');
INSERT INTO `platform_role_authority` VALUES (107, 10, 44, '2019-07-07 12:02:48');
INSERT INTO `platform_role_authority` VALUES (116, 1, 47, '2019-05-23 12:11:06');
INSERT INTO `platform_role_authority` VALUES (118, 11, 5, '2019-07-24 15:44:58');
INSERT INTO `platform_role_authority` VALUES (119, 11, 44, '2019-07-24 15:45:02');
INSERT INTO `platform_role_authority` VALUES (120, 11, 26, '2019-08-12 13:42:50');
INSERT INTO `platform_role_authority` VALUES (136, 1, 48, '2019-10-21 11:01:12');

-- ----------------------------
-- Table structure for platform_role_authority_new
-- ----------------------------
DROP TABLE IF EXISTS `platform_role_authority_new`;
CREATE TABLE `platform_role_authority_new`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `role_Id` int(0) NOT NULL COMMENT '角色',
  `authority_Id` int(0) NULL DEFAULT NULL COMMENT '权限',
  `create_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建日期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 145 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of platform_role_authority_new
-- ----------------------------
INSERT INTO `platform_role_authority_new` VALUES (1, 1, 1, '2019-03-04 21:36:12');
INSERT INTO `platform_role_authority_new` VALUES (2, 1, 2, '2019-03-04 21:36:12');
INSERT INTO `platform_role_authority_new` VALUES (3, 1, 3, '2019-03-04 21:36:12');
INSERT INTO `platform_role_authority_new` VALUES (4, 1, 4, '2019-03-04 21:36:12');
INSERT INTO `platform_role_authority_new` VALUES (5, 1, 5, '2019-03-04 21:36:12');
INSERT INTO `platform_role_authority_new` VALUES (6, 1, 6, '2019-03-04 21:36:12');
INSERT INTO `platform_role_authority_new` VALUES (7, 1, 7, '2019-03-04 21:36:12');
INSERT INTO `platform_role_authority_new` VALUES (8, 1, 8, '2019-03-04 21:36:12');
INSERT INTO `platform_role_authority_new` VALUES (10, 1, 10, '2019-03-04 21:36:12');
INSERT INTO `platform_role_authority_new` VALUES (12, 1, 12, '2019-03-04 21:36:12');
INSERT INTO `platform_role_authority_new` VALUES (13, 1, 13, '2019-03-04 21:36:12');
INSERT INTO `platform_role_authority_new` VALUES (14, 1, 14, '2019-03-04 21:36:12');
INSERT INTO `platform_role_authority_new` VALUES (15, 1, 15, '2019-03-04 21:36:12');
INSERT INTO `platform_role_authority_new` VALUES (16, 1, 16, '2019-03-04 21:36:12');
INSERT INTO `platform_role_authority_new` VALUES (17, 1, 17, '2019-03-04 21:36:12');
INSERT INTO `platform_role_authority_new` VALUES (19, 1, 19, '2019-03-04 21:36:12');
INSERT INTO `platform_role_authority_new` VALUES (20, 1, 20, '2019-03-04 21:36:12');
INSERT INTO `platform_role_authority_new` VALUES (21, 1, 21, '2019-03-04 21:36:12');
INSERT INTO `platform_role_authority_new` VALUES (22, 1, 22, '2019-03-04 21:36:12');
INSERT INTO `platform_role_authority_new` VALUES (23, 1, 23, '2019-03-04 21:36:12');
INSERT INTO `platform_role_authority_new` VALUES (24, 1, 24, '2019-03-04 21:36:12');
INSERT INTO `platform_role_authority_new` VALUES (26, 1, 26, '2019-03-04 21:36:12');
INSERT INTO `platform_role_authority_new` VALUES (27, 1, 27, '2019-03-04 21:36:12');
INSERT INTO `platform_role_authority_new` VALUES (28, 1, 28, '2019-03-04 21:36:12');
INSERT INTO `platform_role_authority_new` VALUES (29, 1, 29, '2019-03-04 21:36:12');
INSERT INTO `platform_role_authority_new` VALUES (31, 1, 31, '2019-03-04 21:36:12');
INSERT INTO `platform_role_authority_new` VALUES (32, 1, 32, '2019-03-04 21:36:12');
INSERT INTO `platform_role_authority_new` VALUES (33, 1, 33, '2019-03-04 21:36:12');
INSERT INTO `platform_role_authority_new` VALUES (38, 1, 38, '2019-03-04 21:36:12');
INSERT INTO `platform_role_authority_new` VALUES (40, 2, 19, '2019-03-04 21:36:12');
INSERT INTO `platform_role_authority_new` VALUES (41, 2, 26, '2019-03-04 21:36:12');
INSERT INTO `platform_role_authority_new` VALUES (48, 4, 13, '2019-03-04 21:36:12');
INSERT INTO `platform_role_authority_new` VALUES (49, 5, 10, '2019-03-04 21:36:12');
INSERT INTO `platform_role_authority_new` VALUES (50, 5, 38, '2019-03-04 21:36:12');
INSERT INTO `platform_role_authority_new` VALUES (51, 6, 14, '2019-03-04 21:36:12');
INSERT INTO `platform_role_authority_new` VALUES (52, 6, 15, '2019-03-04 21:36:12');
INSERT INTO `platform_role_authority_new` VALUES (53, 6, 16, '2019-03-04 21:36:12');
INSERT INTO `platform_role_authority_new` VALUES (54, 6, 17, '2019-03-04 21:36:12');
INSERT INTO `platform_role_authority_new` VALUES (55, 6, 24, '2019-03-04 21:36:12');
INSERT INTO `platform_role_authority_new` VALUES (56, 6, 27, '2019-03-04 21:36:12');
INSERT INTO `platform_role_authority_new` VALUES (57, 6, 28, '2019-03-04 21:36:12');
INSERT INTO `platform_role_authority_new` VALUES (58, 7, 21, '2019-03-04 21:36:12');
INSERT INTO `platform_role_authority_new` VALUES (59, 7, 22, '2019-03-04 21:36:12');
INSERT INTO `platform_role_authority_new` VALUES (60, 8, 20, '2019-03-04 21:36:12');
INSERT INTO `platform_role_authority_new` VALUES (61, 19, 1, '2019-04-14 00:36:12');
INSERT INTO `platform_role_authority_new` VALUES (69, 20, 2, '2019-04-14 10:05:24');
INSERT INTO `platform_role_authority_new` VALUES (70, 20, 10, '2019-04-14 10:05:24');
INSERT INTO `platform_role_authority_new` VALUES (71, 20, 11, '2019-04-14 10:05:24');
INSERT INTO `platform_role_authority_new` VALUES (72, 20, 12, '2019-04-14 10:05:24');
INSERT INTO `platform_role_authority_new` VALUES (73, 1, 41, '2019-04-14 16:04:15');
INSERT INTO `platform_role_authority_new` VALUES (74, 7, 23, '2019-04-14 23:14:58');
INSERT INTO `platform_role_authority_new` VALUES (80, 3, 21, '2019-04-15 23:06:23');
INSERT INTO `platform_role_authority_new` VALUES (81, 3, 22, '2019-04-15 23:06:23');
INSERT INTO `platform_role_authority_new` VALUES (82, 3, 23, '2019-04-15 23:06:23');
INSERT INTO `platform_role_authority_new` VALUES (104, 1, 44, '2019-06-28 17:54:08');
INSERT INTO `platform_role_authority_new` VALUES (107, 1, 45, '2019-07-09 21:22:01');
INSERT INTO `platform_role_authority_new` VALUES (114, 10, 26, '2019-07-10 14:26:27');
INSERT INTO `platform_role_authority_new` VALUES (115, 10, 44, '2019-07-10 14:26:32');
INSERT INTO `platform_role_authority_new` VALUES (116, 1, 46, '2019-05-23 12:11:06');
INSERT INTO `platform_role_authority_new` VALUES (117, 1, 47, '2019-06-28 17:54:08');
INSERT INTO `platform_role_authority_new` VALUES (118, 6, 45, '2019-06-28 17:54:08');
INSERT INTO `platform_role_authority_new` VALUES (120, 8, 26, '2019-06-28 17:54:08');
INSERT INTO `platform_role_authority_new` VALUES (121, 1, 48, '2019-06-28 17:54:08');
INSERT INTO `platform_role_authority_new` VALUES (122, 4, 49, '2019-07-20 12:09:01');
INSERT INTO `platform_role_authority_new` VALUES (123, 4, 50, '2019-07-20 12:09:08');
INSERT INTO `platform_role_authority_new` VALUES (124, 4, 51, '2019-07-20 12:09:18');
INSERT INTO `platform_role_authority_new` VALUES (125, 1, 49, '2019-07-20 12:09:31');
INSERT INTO `platform_role_authority_new` VALUES (126, 1, 50, '2019-07-20 12:09:36');
INSERT INTO `platform_role_authority_new` VALUES (127, 1, 51, '2019-07-20 12:09:42');
INSERT INTO `platform_role_authority_new` VALUES (128, 1, 54, '2019-07-20 16:09:49');
INSERT INTO `platform_role_authority_new` VALUES (129, 1, 55, '2019-07-20 16:10:48');
INSERT INTO `platform_role_authority_new` VALUES (130, 1, 56, '2019-07-20 16:10:52');
INSERT INTO `platform_role_authority_new` VALUES (131, 1, 57, '2019-07-20 16:10:56');
INSERT INTO `platform_role_authority_new` VALUES (132, 5, 54, '2019-07-20 16:11:22');
INSERT INTO `platform_role_authority_new` VALUES (133, 5, 55, '2019-07-20 16:11:28');
INSERT INTO `platform_role_authority_new` VALUES (134, 5, 56, '2019-07-20 16:11:33');
INSERT INTO `platform_role_authority_new` VALUES (135, 5, 57, '2019-07-20 16:11:37');
INSERT INTO `platform_role_authority_new` VALUES (136, 1, 58, '2019-07-24 16:11:44');
INSERT INTO `platform_role_authority_new` VALUES (137, 11, 5, '2019-07-24 16:12:06');
INSERT INTO `platform_role_authority_new` VALUES (138, 11, 44, '2019-07-24 16:12:10');
INSERT INTO `platform_role_authority_new` VALUES (139, 11, 58, '2019-07-24 16:12:15');
INSERT INTO `platform_role_authority_new` VALUES (140, 1, 59, '2019-08-06 10:33:51');
INSERT INTO `platform_role_authority_new` VALUES (141, 11, 26, '2019-08-12 13:43:13');
INSERT INTO `platform_role_authority_new` VALUES (142, 9, 23, '2019-08-22 16:27:58');
INSERT INTO `platform_role_authority_new` VALUES (144, 1, 60, '2019-10-21 10:58:59');

-- ----------------------------
-- Table structure for platform_user
-- ----------------------------
DROP TABLE IF EXISTS `platform_user`;
CREATE TABLE `platform_user`  (
  `id` int(0) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `phone_number` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `user_name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户密码',
  `role_id` int(0) NOT NULL COMMENT '用户角色id',
  `salt` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '盐',
  `code` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '验证码',
  `last_login_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '访问时间',
  `ip_address` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'ip地址',
  `user_super_id` int(0) NOT NULL COMMENT '添加人',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '1删除0正常',
  `last_code_time` timestamp(0) NOT NULL DEFAULT '1970-01-01 08:00:01' COMMENT '上一次获取验证码的时间',
  `group_level` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '催收分组',
  `channel_id` int(0) NOT NULL DEFAULT 0 COMMENT '渠道id',
  `company_id` int(0) NOT NULL DEFAULT 0 COMMENT '催收公司id',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `phone_number`(`phone_number`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '平台用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of platform_user
-- ----------------------------
INSERT INTO `platform_user` VALUES (1, '19999999999', 'admin', 'ff72cee0f1da0eed904407e177f1688b', 1, 'IPg5ue', '1234', '2022-02-13 21:08:19', '20.78.152.19', 1, '2020-07-23 23:52:10', '2020-07-23 23:52:10', 0, '1970-01-01 08:00:01', '0', 0, 1);
INSERT INTO `platform_user` VALUES (15, '13888888888', '12345', 'ff72cee0f1da0eed904407e177f1688b', 1, 'IPg5ue', '1234', '2022-02-21 19:21:51', '223.104.253.60', 1, '2020-09-17 22:02:41', '2020-09-17 22:02:42', 0, '1970-01-01 08:00:01', '0', 0, 1);
INSERT INTO `platform_user` VALUES (18, '13199999999', '逾期小于3', 'e5d27323e70daf8203d9bd72b57cc474', 8, 'pIOXwZ', NULL, '2022-06-25 13:01:16', '47.57.184.89', 1, '2022-06-25 13:01:16', '2022-06-25 13:01:17', 0, '1970-01-01 08:00:01', '3', 0, 1);

-- ----------------------------
-- Table structure for product_recommend
-- ----------------------------
DROP TABLE IF EXISTS `product_recommend`;
CREATE TABLE `product_recommend`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `app_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'app名称',
  `logo_url` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '产品logo地址',
  `app_descriptoin` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'app描述',
  `down_url` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '下载地址（落地页注册地址）',
  `remark` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '产品推荐表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for recharge_record
-- ----------------------------
DROP TABLE IF EXISTS `recharge_record`;
CREATE TABLE `recharge_record`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `recharge_time` date NULL DEFAULT NULL COMMENT '充值时间',
  `recharge_amount` decimal(18, 4) NULL DEFAULT NULL COMMENT '充值金额',
  `recharge_staff` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '充值人员',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of recharge_record
-- ----------------------------
INSERT INTO `recharge_record` VALUES (1, '2020-08-28', 300.0000, '天道（赠送测试）');

-- ----------------------------
-- Table structure for report_repayment
-- ----------------------------
DROP TABLE IF EXISTS `report_repayment`;
CREATE TABLE `report_repayment`  (
  `id` int(0) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `report_date` varchar(25) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '统计日期',
  `all_repayment_count` bigint(0) NULL DEFAULT 0 COMMENT '当前应还款笔总数',
  `expire_count_old` bigint(0) NULL DEFAULT 0 COMMENT '当前老用户应还笔数',
  `expire_count_new` bigint(0) NULL DEFAULT 0 COMMENT '当前新用户应还笔数',
  `normal_repayment_count` bigint(0) NULL DEFAULT 0 COMMENT '当前用户正常还款笔数',
  `normal_count_new` bigint(0) NULL DEFAULT 0 COMMENT '当前新用户正常还款笔数',
  `normal_count_old` bigint(0) NULL DEFAULT 0 COMMENT '当前老用户正常还款笔数',
  `repay_rate` bigint(0) NULL DEFAULT 0 COMMENT '当前正常还款率',
  `repay_rate_old` bigint(0) NULL DEFAULT 0 COMMENT '老用户正常还款率',
  `repay_rate_new` bigint(0) NULL DEFAULT 0 COMMENT '新用户正常还款率',
  `repayment_rate_s1_count_all` bigint(0) NULL DEFAULT 0 COMMENT '逾期3天内总还款率',
  `repayment_rate_s1_count_new` bigint(0) NULL DEFAULT 0 COMMENT '新用户逾期3天内还款率',
  `repayment_rate_s1_count_old` bigint(0) NULL DEFAULT 0 COMMENT '老用户逾期3天内还款率',
  `repayment_rate_s2_count_all` bigint(0) NULL DEFAULT 0 COMMENT '逾期7天内总还款率',
  `repayment_rate_s2_count_new` bigint(0) NULL DEFAULT 0 COMMENT '新用户逾期7天内还款率',
  `repayment_rate_s2_count_old` bigint(0) NULL DEFAULT 0 COMMENT '老用户逾期7天内还款率',
  `repayment_rate_s3_count_all` bigint(0) NULL DEFAULT 0 COMMENT '逾期30天内总还款率',
  `repayment_rate_s3_count_new` bigint(0) NULL DEFAULT 0 COMMENT '新用户逾期30天内还款率',
  `repayment_rate_s3_count_old` bigint(0) NULL DEFAULT 0 COMMENT '老用户逾期30天内还款率',
  `all_repayment_amount` bigint(0) NULL DEFAULT 0 COMMENT '当前应还款总金额',
  `expire_amount_old` bigint(0) NULL DEFAULT 0 COMMENT '当前老用户应还金额',
  `expire_amount_new` bigint(0) NULL DEFAULT 0 COMMENT '当前新用户应还金额',
  `normal_repayment_amount` bigint(0) NULL DEFAULT 0 COMMENT '当前用户正常还款金额',
  `normal_amount_new` bigint(0) NULL DEFAULT 0 COMMENT '当前新用户正常还款金额',
  `normal_amount_old` bigint(0) NULL DEFAULT 0 COMMENT '当前老用户正常还款金额',
  `repay_amount_rate` bigint(0) NULL DEFAULT 0 COMMENT '当前正常还款率 按金额',
  `repay_amount_rate_old` bigint(0) NULL DEFAULT 0 COMMENT '老用户正常还款率 按金额',
  `repay_amount_rate_new` bigint(0) NULL DEFAULT 0 COMMENT '新用户正常还款率 按金额',
  `repayment_rate_s1_amount_all` bigint(0) NULL DEFAULT 0 COMMENT '用户逾期3天内还款率 按金额',
  `repayment_rate_s1_amount_new` bigint(0) NULL DEFAULT 0 COMMENT '新用户逾期3天内还款率 按金额',
  `repayment_rate_s1_amount_old` bigint(0) NULL DEFAULT 0 COMMENT '老用户逾期3天内还款率 按金额',
  `repayment_rate_s2_amount_all` bigint(0) NULL DEFAULT 0 COMMENT '用户逾期7天内还款率 按金额',
  `repayment_rate_s2_amount_new` bigint(0) NULL DEFAULT 0 COMMENT '新用户逾期7天内还款率 按金额',
  `repayment_rate_s2_amount_old` bigint(0) NULL DEFAULT 0 COMMENT '老用户逾期7天内还款率 按金额',
  `repayment_rate_s3_amount_new` bigint(0) NULL DEFAULT 0 COMMENT '新用户逾期21天内还款率 按金额',
  `repayment_rate_s3_amount_old` bigint(0) NULL DEFAULT 0 COMMENT '老用户逾期21天内还款率 按金额',
  `repayment_rate_s3_amount_all` bigint(0) NULL DEFAULT 0 COMMENT '用户逾期30天内还款率 按金额 ',
  `channel_id` int(0) NULL DEFAULT 0 COMMENT '渠道名称',
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `all_repay_count` bigint(0) NULL DEFAULT 0 COMMENT '已经还款数量',
  `all_repay_amount` bigint(0) NULL DEFAULT 0 COMMENT '已经还款总额',
  `all_overdue_count` bigint(0) NULL DEFAULT 0 COMMENT '生息中数量',
  `all_overdue_amount` bigint(0) NULL DEFAULT 0 COMMENT '生息中总额',
  `overdue_rate_s1_amount` bigint(0) NULL DEFAULT 0 COMMENT 'S1级逾期率金额',
  `overdue_rate_s2_amount` bigint(0) NULL DEFAULT 0 COMMENT 'S2级逾期率金额',
  `overdue_rate_s3_amount` bigint(0) NULL DEFAULT 0 COMMENT 'S3级逾期率金额',
  `overdue_rate_s2_count` bigint(0) NULL DEFAULT 0 COMMENT 'S2级逾期率数量',
  `overdue_rate_s3_count` bigint(0) NULL DEFAULT 0 COMMENT 'S3级逾期率数量',
  `overdue_rate_m3_amount` bigint(0) NULL DEFAULT 0 COMMENT 'M3级逾期率金额',
  `overdue_rate_m3_count` bigint(0) NULL DEFAULT 0 COMMENT 'M3级逾期率数量',
  `renewal_number` int(0) NULL DEFAULT 0 COMMENT '续期数目',
  `renewal_amount` bigint(0) NULL DEFAULT 0 COMMENT '续期金额',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 103 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of report_repayment
-- ----------------------------
INSERT INTO `report_repayment` VALUES (99, '2022-07-01', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, '2022-07-01 00:00:00', '2022-07-01 00:00:00', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
INSERT INTO `report_repayment` VALUES (100, '2022-07-02', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, '2022-07-02 00:00:02', '2022-07-02 00:00:02', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
INSERT INTO `report_repayment` VALUES (101, '2022-07-03', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, '2022-07-03 00:00:02', '2022-07-03 00:00:02', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
INSERT INTO `report_repayment` VALUES (102, '2022-07-04', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, '2022-07-04 00:00:02', '2022-07-04 00:00:02', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
INSERT INTO `report_repayment` VALUES (103, '2022-07-05', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, '2022-07-05 20:30:02', '2022-07-05 20:30:02', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);

-- ----------------------------
-- Table structure for risk_bdf_content
-- ----------------------------
DROP TABLE IF EXISTS `risk_bdf_content`;
CREATE TABLE `risk_bdf_content`  (
  `id` bigint(0) UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_id` bigint(0) NULL DEFAULT NULL COMMENT '用户id',
  `score` decimal(8, 2) NULL DEFAULT NULL COMMENT '分',
  `report_content` longblob NULL COMMENT '报告内容',
  `status` int(0) NULL DEFAULT NULL COMMENT '0-待获取;1-获取成功',
  `fail_cause` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '失败原因',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '风控描述',
  `resp_order` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '请求订单号',
  `type` int(0) NULL DEFAULT NULL COMMENT '风控类型：1-fygsh',
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  INDEX `status`(`status`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for risk_bdf_dt
-- ----------------------------
DROP TABLE IF EXISTS `risk_bdf_dt`;
CREATE TABLE `risk_bdf_dt`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(0) NULL DEFAULT NULL,
  `type` int(0) NULL DEFAULT NULL COMMENT '多头服务商类型1-risk',
  `resp_order` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '请求订单号',
  `status` int(0) NULL DEFAULT NULL COMMENT '1-成功;0-失败',
  `fail_cause` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `report` longblob NULL COMMENT '多头报告内容',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '时间',
  `update_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for risk_credit_user
-- ----------------------------
DROP TABLE IF EXISTS `risk_credit_user`;
CREATE TABLE `risk_credit_user`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` int(0) NOT NULL COMMENT '用户主键',
  `asset_id` int(0) NOT NULL COMMENT '借款信息申请表主键ID',
  `user_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  `card_num` varchar(20) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL COMMENT '用户身份证号码',
  `user_phone` varchar(20) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL COMMENT '用手机号',
  `age` int(0) NOT NULL DEFAULT 18 COMMENT '年龄，本表在实名认证通过才插入，通过用户表获得',
  `sex` int(0) NOT NULL DEFAULT 1 COMMENT '1男2女',
  `education` int(0) NOT NULL DEFAULT 7 COMMENT '学历（0未知1博士、2硕士、3本科、4大专、5中专、6高中、7初中及以下）',
  `amount_addsum` decimal(20, 2) NOT NULL DEFAULT 0.00 COMMENT '后台累计增加的额度',
  `amount_max` decimal(20, 2) NOT NULL DEFAULT 0.00 COMMENT '机审额度',
  `js_amount_time` timestamp(0) NULL DEFAULT NULL COMMENT '机审额度更新时间',
  `zm_score` decimal(20, 2) NOT NULL DEFAULT 0.00 COMMENT '芝麻积分',
  `zm_score_time` timestamp(0) NULL DEFAULT NULL COMMENT '芝麻分上次更新时间',
  `zm_industy_black` int(0) NOT NULL DEFAULT 2 COMMENT '芝麻行业关注度黑名单1.是；2否',
  `zm_over` int(0) NOT NULL DEFAULT 0 COMMENT '行业关注度接口中返回的借贷逾期记录数AA001借贷逾期的记录数',
  `zm_no_pay_over` int(0) NOT NULL DEFAULT 0 COMMENT '行业关注度接口中返回的逾期未支付记录数，包括AD001 逾期未支付、AE001 逾期未支付的记录总数',
  `zm_industy_time` timestamp(0) NULL DEFAULT NULL COMMENT '行业关注度上次更新时间',
  `zm_status` int(0) NOT NULL DEFAULT 1 COMMENT '芝麻数据统计状态，1失败；2成功',
  `my_hb` decimal(20, 2) NOT NULL DEFAULT 0.00 COMMENT '蚂蚁花呗额度',
  `my_hb_time` timestamp(0) NULL DEFAULT NULL COMMENT '蚂蚁花呗额度更新时间',
  `td_report_id` varchar(100) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL COMMENT '同盾贷前审核报告标识',
  `td_report_time` timestamp(0) NULL DEFAULT NULL COMMENT '同盾贷前审核报告上次生成时间',
  `td_score` decimal(20, 2) NOT NULL DEFAULT 0.00 COMMENT '同盾分，分值越高风险越大',
  `td_phone_black` int(0) NOT NULL DEFAULT 0 COMMENT '同盾手机网贷黑名单个数',
  `td_card_num_black` int(0) NOT NULL DEFAULT 0 COMMENT '同盾身份证号网贷黑名单个数',
  `td_month1_borrow` int(0) NOT NULL DEFAULT 0 COMMENT '同盾1月内多平台申请借款',
  `td_day7_borrow` int(0) NOT NULL DEFAULT 0 COMMENT '同盾7天内多平台申请借款',
  `td_month1_card_num_device_borrow` int(0) NOT NULL DEFAULT 0 COMMENT '同盾1月内身份证使用多设备申请',
  `td_day7_device_card_or_phone_borrow` int(0) NOT NULL DEFAULT 0 COMMENT '同盾7天内设备使用多身份证或手机号申请',
  `td_day7_card_device` int(0) NOT NULL DEFAULT 0 COMMENT '7天内身份证关联设备数',
  `td_month3_apply_card` int(0) NOT NULL DEFAULT 0 COMMENT '3个月内申请信息关联多个身份证',
  `td_month3_card_apply` int(0) NOT NULL DEFAULT 0 COMMENT '3个月内身份证关联多个申请信息',
  `td_detail_time` timestamp(0) NULL DEFAULT NULL COMMENT '同盾详情上次更新时间',
  `td_status` int(0) NOT NULL DEFAULT 1 COMMENT '同盾接口状态，默认1失败；成功是2，生成报告接口状态以reportId为准，查询报告以此为准',
  `bqs_black` int(0) NOT NULL DEFAULT 1 COMMENT '白骑士1.通过；2.拒绝（命中黑名单）；3.建议人工审核(命中灰名单)',
  `bqs_black_time` timestamp(0) NULL DEFAULT NULL COMMENT '白骑士更新时间',
  `bqs_status` int(0) NOT NULL DEFAULT 1 COMMENT '白骑士接口状态，默认1失败；成功是2',
  `zzc_black` int(0) NOT NULL DEFAULT 2 COMMENT '是否是中智诚黑名单：1.是2否',
  `zzc_black_num` int(0) NOT NULL DEFAULT 0 COMMENT '命中中智诚黑名单的记录数',
  `zzc_fqz` int(0) NOT NULL DEFAULT 3 COMMENT '中智诚反欺诈风险级别1.高(最差)；2中(中等)；3低(最好)',
  `zzc_fqz_id` varchar(100) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL COMMENT '中智诚反欺诈ID号',
  `zzc_black_time` timestamp(0) NULL DEFAULT NULL COMMENT '中智诚黑名单更新时间',
  `zzc_status` int(0) NOT NULL DEFAULT 1 COMMENT '中智诚接口状态，默认1失败；成功是2',
  `jy_loan_num` int(0) NOT NULL DEFAULT 0 COMMENT '91征信借款总数',
  `jy_jd_num` int(0) NOT NULL DEFAULT 0 COMMENT '91征信拒单记录数',
  `jy_jd_bl` decimal(5, 2) NOT NULL DEFAULT 0.00 COMMENT '91征信拒单记录占比',
  `jy_over_num` int(0) NOT NULL DEFAULT 0 COMMENT '91征信逾期记录数、逾期金额大于0或者状态是逾期',
  `jy_over_bl` decimal(5, 2) NOT NULL DEFAULT 0.00 COMMENT '91征信逾期记录占比',
  `jy_fk_num` int(0) NOT NULL DEFAULT 0 COMMENT '91征信已放款总数',
  `jy_hk_num` int(0) NOT NULL DEFAULT 0 COMMENT '91征信已还款总数',
  `jy_hk_bl` decimal(5, 2) NOT NULL DEFAULT 1.00 COMMENT '91征信已还款总数除以放款总数',
  `jy_time` timestamp(0) NULL DEFAULT NULL COMMENT '91数据更新时间',
  `jy_status` int(0) NOT NULL DEFAULT 1 COMMENT '91征信接口状态，默认1失败；成功是2',
  `mg_black_score` decimal(20, 2) NOT NULL DEFAULT 0.00 COMMENT '密罐黑中介分,默认100，分值越小风险越大',
  `mg_day7_num` int(0) NOT NULL DEFAULT 0 COMMENT '蜜罐近7天查询次数',
  `mg_month1_num` int(0) NOT NULL DEFAULT 0 COMMENT '蜜罐近1个月查询次数',
  `mg_black` int(0) NOT NULL DEFAULT 2 COMMENT '蜜罐黑名单1是2否；身份证和姓名是否在黑名单或者手机和姓名是否在黑名单',
  `mg_time` timestamp(0) NULL DEFAULT NULL COMMENT '密罐更新时间',
  `mg_status` int(0) NOT NULL DEFAULT 1 COMMENT '密罐接口状态，默认1失败；成功是2',
  `jxl_token` varchar(100) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL COMMENT '聚信立开始采集数据时存入token',
  `jxl_token_time` timestamp(0) NULL DEFAULT NULL COMMENT '聚信立token入库时间',
  `jxl_detail` longtext CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL COMMENT '聚信立成功采集后返回的数据',
  `jxl_detail_time` timestamp(0) NULL DEFAULT NULL COMMENT '聚信立成功采集详情数据的时间',
  `jxl_zj_dk_num` int(0) NOT NULL DEFAULT 0 COMMENT '聚信立贷款类号码主叫个数',
  `jxl_bj_dk_num` int(0) NOT NULL DEFAULT 0 COMMENT '聚信立贷款类号码被叫个数',
  `jxl_yj_hf` decimal(20, 2) NOT NULL DEFAULT 0.00 COMMENT '聚信立月均话费',
  `jxl_link2_days` int(0) NOT NULL DEFAULT 0 COMMENT '聚信立通话详单和用户第二联系人最晚联系日期到目前的天数',
  `jxl_link1_days` int(0) NOT NULL DEFAULT 0 COMMENT '聚信立通话详单和用户第一联系人最晚联系日期到目前的天数',
  `jxl_link2_num` int(0) NOT NULL DEFAULT 0 COMMENT '聚信立通话详单和用户第二联系人的通话次数',
  `jxl_link1_num` int(0) NOT NULL DEFAULT 0 COMMENT '聚信立通话详单和用户第一联系人的通话次数',
  `jxl_link2_order` int(0) NOT NULL DEFAULT 0 COMMENT '聚信立第二联系人通话次数排名',
  `jxl_link1_order` int(0) NOT NULL DEFAULT 0 COMMENT '聚信立第一联系人通话次数排名',
  `jxl_gj_ts` int(0) NOT NULL DEFAULT 0 COMMENT '聚信立关机天数，手机静默情况',
  `jxl_total` int(0) NOT NULL DEFAULT 0 COMMENT '聚信立统计关机天数的周期（天数）',
  `jxl_gj_bl` decimal(5, 2) NOT NULL DEFAULT 0.00 COMMENT '聚信立关机天数比例',
  `jxl_lx_gj_ts` int(0) NOT NULL DEFAULT 0 COMMENT '聚信立最大连续关机天数',
  `jxl_ht_phone_num` int(0) NOT NULL DEFAULT 0 COMMENT '聚信立互通电话数量',
  `jxl_amth_num` int(0) NOT NULL DEFAULT 0 COMMENT '聚信立澳门通话次数',
  `jxl_phone_reg_days` int(0) NOT NULL DEFAULT 0 COMMENT '聚信立手机开户时间距离当前的天数',
  `jxl_time` timestamp(0) NULL DEFAULT NULL COMMENT '聚信立分析数据更新时间',
  `jxl_realName` int(0) NOT NULL DEFAULT 2 COMMENT '运营商是否已实名1已实名；2未实名，默认2',
  `go_am_num` int(0) NOT NULL DEFAULT 0 COMMENT '去澳门次数；默认0次，目标地为澳门则+1',
  `jxl_same_phone_card` int(0) NOT NULL DEFAULT 1 COMMENT '1.匹配；2.不匹配；3.运营商未提供身份证号码',
  `zjlxr_black` int(0) NOT NULL DEFAULT 0 COMMENT '直接联系人黑名单数',
  `jjlxr_black` int(0) NOT NULL DEFAULT 0 COMMENT '间接联系人黑名单数',
  `yqjj_black` int(0) NOT NULL DEFAULT 0 COMMENT '引起间接联系人黑名单数',
  `yx_fx_num` int(0) NOT NULL DEFAULT 0 COMMENT '宜信命中风险名单的记录数',
  `yx_month3_over` int(0) NOT NULL DEFAULT 0 COMMENT '宜信三个月以上逾期发生的次数',
  `yx_less_month3_over` int(0) NOT NULL DEFAULT 0 COMMENT '宜信三个月以内逾期发生的次数，历史逾期次数减三个月以上的逾期次数',
  `yx_status` int(0) NOT NULL DEFAULT 1 COMMENT '宜信接口状态，默认1失败；成功是2',
  `yx_time` timestamp(0) NULL DEFAULT NULL COMMENT '宜信入库时间',
  `risk_status` int(0) NOT NULL DEFAULT 1 COMMENT '当前风控运算状态1.待机审；2.机审通过(接口成功)；3机审不通过(接口成功)4，机审建议复审(接口成功，规则计算出需要复审)5.辅助用户初次额度计算.6已人工审核；7人工强制通过，8.未知异常；9.征信异常,10.复审通过，11.复审不通过,12.放款驳回,13，人为，14.新模型风控中',
  `risk_remark` varchar(250) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '机审备注',
  `risk_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '风控审核时间',
  `history_over_num` int(0) NOT NULL DEFAULT 0 COMMENT '历史逾期总记录数，逾期并还款也纳为逾期记录',
  `last_over_days` int(0) NOT NULL DEFAULT 0 COMMENT '最近一次逾期总天数，逾期并还款也算',
  `last_days` int(0) NOT NULL DEFAULT -1 COMMENT '上一次被拒距离当前的天数',
  `present_add_num` int(0) NOT NULL DEFAULT 0 COMMENT '常用地址重复次数',
  `company_add_num` int(0) NOT NULL DEFAULT 0 COMMENT '公司地址重复次数',
  `many_device_login_num` int(0) NOT NULL DEFAULT 0 COMMENT '多设备登录同一个账号次数',
  `one_device_reg_many_phone_num` int(0) NOT NULL DEFAULT 0 COMMENT '一个设备注册多个用户次数',
  `company_name_num` int(0) NOT NULL DEFAULT 0 COMMENT '单位名称出现次数',
  `company_phone_diff_name_or_add` int(0) NOT NULL DEFAULT 0 COMMENT '同一个单位电话关联不同单位名称或地址的次数',
  `company_add_diff_name_or_phone` int(0) NOT NULL DEFAULT 0 COMMENT '同一个单位地址关联不同单位号码或者名称',
  `company_name_diff_add_or_phone` int(0) NOT NULL DEFAULT 0 COMMENT '同一个单位名称关联不同地址或电话',
  `concat_apply_num` int(0) NOT NULL DEFAULT 0 COMMENT '联系人申请借款数',
  `csjy` int(0) NOT NULL DEFAULT 3 COMMENT '1通过；2拒绝；3无建议',
  `customer_type` int(0) NOT NULL DEFAULT 0 COMMENT '是否是老用户：0、新用户；1；老用户',
  `user_contact_size` int(0) NOT NULL DEFAULT 0 COMMENT '用户通讯录的联系人数量',
  `money_amount` decimal(20, 2) NOT NULL DEFAULT 0.00 COMMENT '借款金额',
  `gray_num` int(0) NOT NULL DEFAULT 0 COMMENT '命中他库黑名单的次数',
  `add_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '数据插入时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '最后更新时间',
  `risk_cal_suf` varchar(100) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL COMMENT '风控过程数据所在表的后缀',
  `to_new_risk` varchar(500) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL COMMENT '新风控模型响应的数据',
  `new_risk_score` decimal(20, 2) NOT NULL DEFAULT 0.00 COMMENT '新风控模型响应的分值',
  `new_risk_flag` int(0) NOT NULL DEFAULT 3 COMMENT '新风控模型响应的结果1通过；2不通过；3默认值',
  `sh_fqz` int(0) NOT NULL DEFAULT 1 COMMENT '1.低风险2中风险3高风险',
  `sh_score` decimal(20, 2) NOT NULL DEFAULT 0.00 COMMENT '算话评分530分(含)以下为高欺诈风险，530-570分(含)为中欺诈风险，570分以上为低欺诈风险',
  `sh_status` int(0) NOT NULL DEFAULT 1 COMMENT '1.失败；2.成功',
  `sh_time` timestamp(0) NULL DEFAULT NULL COMMENT '算话入库时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_risk_status`(`risk_status`) USING BTREE,
  INDEX `index_uid_asset_id`(`user_id`, `asset_id`) USING BTREE,
  INDEX `idne_addtime`(`add_time`) USING BTREE,
  INDEX `index_asset_id`(`asset_id`) USING BTREE,
  INDEX `index_rtime_status`(`risk_time`, `risk_status`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 22 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for risk_fk_content
-- ----------------------------
DROP TABLE IF EXISTS `risk_fk_content`;
CREATE TABLE `risk_fk_content`  (
  `id` bigint(0) UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_id` bigint(0) NULL DEFAULT NULL COMMENT '用户id',
  `score` decimal(8, 2) NULL DEFAULT NULL COMMENT '分',
  `report_content` longblob NULL COMMENT '报告内容',
  `status` int(0) NULL DEFAULT NULL COMMENT '0-待获取;1-获取成功',
  `fail_cause` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '失败原因',
  `task_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '风控唯一id',
  `type` int(0) NULL DEFAULT NULL COMMENT '风控类型：1-fygsh',
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  INDEX `status`(`status`) USING BTREE,
  INDEX `task_id`(`task_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for risk_gongzhai_afu
-- ----------------------------
DROP TABLE IF EXISTS `risk_gongzhai_afu`;
CREATE TABLE `risk_gongzhai_afu`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(0) NULL DEFAULT NULL,
  `type` int(0) NULL DEFAULT NULL COMMENT '共债服务商类型1-risk',
  `fk_notify_result` int(0) NULL DEFAULT NULL COMMENT '放款通知结果',
  `request_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '共债放款上传返回的字段值',
  `status` int(0) NULL DEFAULT NULL COMMENT '1-成功;0-失败',
  `fail_cause` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `report` longblob NULL COMMENT '共债报告内容',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '时间',
  `update_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for risk_gongzhai_content
-- ----------------------------
DROP TABLE IF EXISTS `risk_gongzhai_content`;
CREATE TABLE `risk_gongzhai_content`  (
  `id` bigint(0) UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_id` bigint(0) NULL DEFAULT NULL,
  `type` int(0) NULL DEFAULT NULL COMMENT '共债服务商类型1-risk',
  `fk_notify_result` int(0) NULL DEFAULT NULL COMMENT '放款通知结果',
  `request_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '共债放款上传返回的字段值',
  `status` int(0) NULL DEFAULT NULL COMMENT '1-成功;0-失败',
  `fail_cause` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `report` longblob NULL COMMENT '共债报告内容',
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  INDEX `type`(`type`) USING BTREE,
  INDEX `request_id`(`request_id`) USING BTREE,
  INDEX `status`(`status`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for risk_gongzhai_lz
-- ----------------------------
DROP TABLE IF EXISTS `risk_gongzhai_lz`;
CREATE TABLE `risk_gongzhai_lz`  (
  `id` bigint(0) UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_id` bigint(0) NULL DEFAULT NULL,
  `type` int(0) NULL DEFAULT NULL COMMENT '共债服务商类型1-risk',
  `fk_notify_result` int(0) NULL DEFAULT NULL COMMENT '放款通知结果',
  `request_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '共债放款上传返回的字段值',
  `status` int(0) NULL DEFAULT NULL COMMENT '1-成功;0-失败',
  `fail_cause` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `report` longblob NULL COMMENT '共债报告内容',
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  INDEX `type`(`type`) USING BTREE,
  INDEX `request_id`(`request_id`) USING BTREE,
  INDEX `status`(`status`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for risk_gz101
-- ----------------------------
DROP TABLE IF EXISTS `risk_gz101`;
CREATE TABLE `risk_gz101`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(0) NULL DEFAULT NULL,
  `type` int(0) NULL DEFAULT NULL COMMENT '共债服务商类型1-risk',
  `fk_notify_result` int(0) NULL DEFAULT NULL COMMENT '放款通知结果',
  `request_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '共债放款上传返回的字段值',
  `status` int(0) NULL DEFAULT NULL COMMENT '1-成功;0-失败',
  `fail_cause` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `report` longblob NULL COMMENT '共债报告内容',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '时间',
  `update_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for risk_gz_tc
-- ----------------------------
DROP TABLE IF EXISTS `risk_gz_tc`;
CREATE TABLE `risk_gz_tc`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(0) NULL DEFAULT NULL,
  `type` int(0) NULL DEFAULT NULL COMMENT '共债服务商类型1-risk',
  `fk_notify_result` int(0) NULL DEFAULT NULL COMMENT '放款通知结果',
  `request_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '共债放款上传返回的字段值',
  `status` int(0) NULL DEFAULT NULL COMMENT '1-成功;0-失败',
  `fail_cause` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `report` longblob NULL COMMENT '共债报告内容',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '时间',
  `update_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1342 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '雷达报告' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for risk_gz_xpaf
-- ----------------------------
DROP TABLE IF EXISTS `risk_gz_xpaf`;
CREATE TABLE `risk_gz_xpaf`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(0) NULL DEFAULT NULL,
  `type` int(0) NULL DEFAULT NULL COMMENT '共债服务商类型1-risk',
  `fk_notify_result` int(0) NULL DEFAULT NULL COMMENT '放款通知结果',
  `request_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '共债放款上传返回的字段值',
  `status` int(0) NULL DEFAULT NULL COMMENT '1-成功;0-失败',
  `fail_cause` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `report` longblob NULL COMMENT '共债报告内容',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '时间',
  `update_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 399 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for risk_rule_config
-- ----------------------------
DROP TABLE IF EXISTS `risk_rule_config`;
CREATE TABLE `risk_rule_config`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `rule_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '规则名称key(r:表示规则首页字母，txl:表示通讯录，不同规则的首写字母来定义，00X表示 该规则下的编号 )',
  `rule_value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '条件值(JSON数据 数值型判断时：type 0表示小于，1表示等于，2表示大于)',
  `risk_decision` tinyint(0) NOT NULL DEFAULT 0 COMMENT '风控决策（0拒绝，1通过）',
  `rule_descript` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '规则描述',
  `status` tinyint(0) NOT NULL DEFAULT 0 COMMENT '0启用 ，1关闭 2逻辑删除',
  `rule_type` tinyint(0) NOT NULL DEFAULT 0 COMMENT '（0表示存储单数值型，1表示存储数值范围型，2表示存储数组形式存储型(比如比如涉及敏感词汇：警官，警察),3表示直接等于strValue，4，不同类型的值的范围型：(比如通讯录与通话记录中前10位匹配号码数量小于3),   5，表示包含某些关键字超过某个指标时:比如通讯录命中敏感性词汇（借、贷相关个人或平台）大于10个 ）',
  `create_time` timestamp(0) NOT NULL DEFAULT '1970-01-01 08:00:01' COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT '1970-01-01 08:00:01' ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_ruleKey`(`rule_key`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '风控规则配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of risk_rule_config
-- ----------------------------
INSERT INTO `risk_rule_config` VALUES (1, 'r_txl001', '{\"type\":0,\"intValue\":5}', 1, '通讯录联系人数量低于5个', 1, 0, '2019-01-25 08:00:01', '2022-06-30 16:42:15');
INSERT INTO `risk_rule_config` VALUES (2, 'r_txl002', '{\"type\":2,\"intValue\":10,\"strValueArray\":\"借，贷\"}', 1, '通讯录命中敏感性词汇（借、贷相关个人或平台）大于10个', 1, 5, '2019-01-25 08:00:01', '2020-09-14 22:39:55');
INSERT INTO `risk_rule_config` VALUES (3, 'r_txl003', '{\"strValueArray\":\"警官，警察，警方，法官\"}', 1, '通讯录涉及：警官  警察  警方 法官', 1, 2, '2019-01-25 08:00:01', '2019-10-24 14:08:09');
INSERT INTO `risk_rule_config` VALUES (4, 'r_ln001', '{\"maxValue\":55,\"minValue\":18}', 1, '年龄大于18或小于55岁', 0, 1, '1970-01-01 08:00:01', '2020-08-30 22:34:18');
INSERT INTO `risk_rule_config` VALUES (10, 'r_dq001', '{\"strValueArray\":{\"西藏自治区\": \"\",\"黑龙江\": \"双鸭山|齐齐哈尔\",\"山东\": \"临沂|烟台\",\"云南\": \"红河\",\"广西\": \"北海\",\"内蒙古\": \"赤峰|包头\"}}', 1, '借款限制的地区', 1, 2, '1970-01-01 08:00:01', '2020-08-06 12:54:56');
INSERT INTO `risk_rule_config` VALUES (12, 'r_yys006', '{\"strValue\":\"匹配失败\"}', 1, '身份证号码与运营商数据不匹配', 1, 3, '2019-04-30 08:00:01', '2022-02-20 23:16:25');
INSERT INTO `risk_rule_config` VALUES (13, 'r_yys007', '{\"strValue\":\"匹配失败\"}', 1, '姓名与运营商数据不匹配', 1, 3, '2019-04-30 08:00:01', '2022-02-20 23:16:23');
INSERT INTO `risk_rule_config` VALUES (14, 'r_black001', '{\"intValue\":1}', 1, '黑名单用户', 0, 1, '1970-01-01 08:00:01', '2020-08-09 12:10:56');
INSERT INTO `risk_rule_config` VALUES (15, 'r_yys008', '{\"minValue\":8}', 1, '运营商入网时长小于指定阈值', 1, 0, '1970-01-01 08:00:01', '2022-02-20 23:15:38');

-- ----------------------------
-- Table structure for risk_trial_count
-- ----------------------------
DROP TABLE IF EXISTS `risk_trial_count`;
CREATE TABLE `risk_trial_count`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `rule_id` int(0) NOT NULL COMMENT '规则的id',
  `hit_count` int(0) NOT NULL COMMENT '数量',
  `count_type` tinyint(1) UNSIGNED ZEROFILL NOT NULL COMMENT '(0表示命中规则的统计，1表示通过该规则的统计)',
  `rule_descript` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '规则的描述',
  `create_time` timestamp(0) NOT NULL DEFAULT '1970-01-01 08:00:01' ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT '1970-01-01 08:00:01' ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `status` tinyint(0) NULL DEFAULT NULL COMMENT '状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 80 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of risk_trial_count
-- ----------------------------
INSERT INTO `risk_trial_count` VALUES (49, 14, 1, 1, '黑名单用户', '2020-08-28 10:27:55', '2020-08-28 10:27:55', NULL);
INSERT INTO `risk_trial_count` VALUES (50, 1, 1, 0, '通讯录联系人数量低于5个', '2020-08-28 10:27:55', '2020-08-28 10:27:55', NULL);
INSERT INTO `risk_trial_count` VALUES (51, 14, 1, 1, '黑名单用户', '2020-09-15 16:08:01', '2020-09-15 16:08:01', NULL);
INSERT INTO `risk_trial_count` VALUES (52, 1, 1, 1, '通讯录联系人数量低于5个', '2020-09-15 16:08:02', '2020-09-15 16:08:02', NULL);
INSERT INTO `risk_trial_count` VALUES (53, 4, 1, 1, '年龄大于18或小于55岁', '2020-09-15 16:08:02', '2020-09-15 16:08:02', NULL);
INSERT INTO `risk_trial_count` VALUES (54, 13, 1, 1, '姓名与运营商数据不匹配', '2020-09-15 16:08:02', '2020-09-15 16:08:02', NULL);
INSERT INTO `risk_trial_count` VALUES (55, 15, 1, 1, '运营商入网时长小于指定阈值', '2020-09-15 16:08:02', '2020-09-15 16:08:02', NULL);
INSERT INTO `risk_trial_count` VALUES (56, 14, 1, 1, '黑名单用户', '2020-10-21 14:55:48', '2020-10-21 14:55:48', NULL);
INSERT INTO `risk_trial_count` VALUES (57, 1, 1, 0, '通讯录联系人数量低于5个', '2020-10-21 14:55:48', '2020-10-21 14:55:48', NULL);
INSERT INTO `risk_trial_count` VALUES (58, 14, 4, 1, '黑名单用户', '2022-02-20 21:18:23', '2022-02-20 21:18:23', NULL);
INSERT INTO `risk_trial_count` VALUES (59, 1, 3, 1, '通讯录联系人数量低于5个', '2022-02-20 23:10:08', '2022-02-20 23:10:08', NULL);
INSERT INTO `risk_trial_count` VALUES (60, 4, 3, 1, '年龄大于18或小于55岁', '2022-02-20 23:10:08', '2022-02-20 23:10:08', NULL);
INSERT INTO `risk_trial_count` VALUES (61, 13, 1, 1, '姓名与运营商数据不匹配', '2022-02-20 23:10:08', '2022-02-20 23:10:08', NULL);
INSERT INTO `risk_trial_count` VALUES (62, 15, 1, 0, '运营商入网时长小于指定阈值', '2022-02-20 23:10:08', '2022-02-20 23:10:08', NULL);
INSERT INTO `risk_trial_count` VALUES (63, 14, 1, 1, '黑名单用户', '2022-02-21 00:04:03', '2022-02-21 00:04:03', NULL);
INSERT INTO `risk_trial_count` VALUES (64, 1, 1, 1, '通讯录联系人数量低于5个', '2022-02-21 00:04:03', '2022-02-21 00:04:03', NULL);
INSERT INTO `risk_trial_count` VALUES (65, 4, 1, 1, '年龄大于18或小于55岁', '2022-02-21 00:04:03', '2022-02-21 00:04:03', NULL);
INSERT INTO `risk_trial_count` VALUES (66, 14, 1, 1, '黑名单用户', '2022-02-24 16:44:26', '2022-02-24 16:44:26', NULL);
INSERT INTO `risk_trial_count` VALUES (67, 14, 1, 1, '黑名单用户', '2022-04-04 16:31:17', '2022-04-04 16:31:17', NULL);
INSERT INTO `risk_trial_count` VALUES (68, 1, 1, 1, '通讯录联系人数量低于5个', '2022-04-04 16:31:18', '2022-04-04 16:31:18', NULL);
INSERT INTO `risk_trial_count` VALUES (69, 4, 1, 1, '年龄大于18或小于55岁', '2022-04-04 16:31:18', '2022-04-04 16:31:18', NULL);
INSERT INTO `risk_trial_count` VALUES (70, 14, 4, 1, '黑名单用户', '2022-06-23 16:31:33', '2022-06-23 16:31:33', NULL);
INSERT INTO `risk_trial_count` VALUES (71, 1, 4, 1, '通讯录联系人数量低于5个', '2022-06-23 16:31:33', '2022-06-23 16:31:33', NULL);
INSERT INTO `risk_trial_count` VALUES (72, 4, 4, 1, '年龄大于18或小于55岁', '2022-06-23 16:31:33', '2022-06-23 16:31:33', NULL);
INSERT INTO `risk_trial_count` VALUES (73, 14, 1, 1, '黑名单用户', '2022-06-25 13:35:08', '2022-06-25 13:35:08', NULL);
INSERT INTO `risk_trial_count` VALUES (74, 1, 1, 0, '通讯录联系人数量低于5个', '2022-06-25 13:35:08', '2022-06-25 13:35:08', NULL);
INSERT INTO `risk_trial_count` VALUES (75, 14, 2, 1, '黑名单用户', '2022-06-30 14:25:20', '2022-06-30 14:25:20', NULL);
INSERT INTO `risk_trial_count` VALUES (76, 1, 1, 0, '通讯录联系人数量低于5个', '2022-06-30 14:25:20', '2022-06-30 14:25:20', NULL);
INSERT INTO `risk_trial_count` VALUES (77, 4, 1, 1, '年龄大于18或小于55岁', '2022-06-30 16:54:16', '2022-06-30 16:54:16', NULL);
INSERT INTO `risk_trial_count` VALUES (78, 14, 2, 1, '黑名单用户', '2022-07-01 12:10:11', '2022-07-01 12:10:11', NULL);
INSERT INTO `risk_trial_count` VALUES (79, 4, 2, 1, '年龄大于18或小于55岁', '2022-07-01 12:10:11', '2022-07-01 12:10:11', NULL);

-- ----------------------------
-- Table structure for source_user_login_info
-- ----------------------------
DROP TABLE IF EXISTS `source_user_login_info`;
CREATE TABLE `source_user_login_info`  (
  `id` int(0) UNSIGNED NOT NULL COMMENT 'id',
  `phone_number` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '登陆用户的手机号',
  `source_h5_code` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户访问的h5页渠道编码',
  `login_times` int(0) NOT NULL DEFAULT 0 COMMENT '访问次数',
  `ip_address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'ip地址',
  `source_channel_code` int(0) NOT NULL COMMENT '注册时的渠道id',
  `login_time` datetime(0) NOT NULL COMMENT '登录时间',
  `client_type` int(0) NOT NULL DEFAULT 0 COMMENT '客户端类型：0表示未知，1表示安卓，2表示IOS',
  `user_creat_time` datetime(0) NULL DEFAULT NULL COMMENT '用户注册时间（用于计算登录量有用）',
  `user_login_info` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '登录时手机基本信息JSON存储,字段解释对应AppUserLoginDto对象属性',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `login_infor`(`phone_number`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户登陆信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of source_user_login_info
-- ----------------------------
INSERT INTO `source_user_login_info` VALUES (0, '13795460108', NULL, 2, '119.62.209.129', 1, '2020-08-28 10:21:20', 1, '2020-08-28 01:57:52', '{\"appMarket\":\"xqc-self\",\"appName\":\"b_android\",\"appVersion\":\"1.0.3\",\"channelCode\":\"自然流量\",\"clientType\":\"1\",\"deviceId\":\"82a5868da1bcae94\",\"deviceName\":\"Redmi K30\",\"osVersion\":\"10\",\"phoneNumber\":\"13795460108\",\"smsCode\":\"1756\"}');

-- ----------------------------
-- Table structure for sys_oper_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_oper_log`;
CREATE TABLE `sys_oper_log`  (
  `oper_id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '日志主键',
  `title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '模块标题',
  `business_type` int(0) NULL DEFAULT 0 COMMENT '业务类型（0其它 1新增 2修改 3删除）',
  `method` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '方法名称',
  `request_method` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '请求方式',
  `operator_type` int(0) NULL DEFAULT 0 COMMENT '操作类别（0其它 1后台用户 2手机端用户）',
  `oper_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '操作人员',
  `dept_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '部门名称',
  `oper_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '请求URL',
  `oper_ip` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '主机地址',
  `oper_location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '操作地点',
  `oper_param` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '请求参数',
  `json_result` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '返回参数',
  `status` int(0) NULL DEFAULT 0 COMMENT '操作状态（0正常 1异常）',
  `error_msg` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '错误消息',
  `oper_time` datetime(0) NULL DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`oper_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '操作日志记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_oper_log
-- ----------------------------
INSERT INTO `sys_oper_log` VALUES (1, '后台用户登陆', 0, 'com.summer.web.controller.userManager.UserLoginController.login()', 'POST', 1, '', '', 'http://q9.q9999.top:8077/v1.0/api/user/login', '111.48.114.64', '', '{\"commonParam\":{},\"jsonParam\":[{\"code\":\"1234\",\"id\":100000,\"isNew\":1,\"password\":\"123456\",\"phoneNumber\":\"19999999999\",\"roleId\":1}]}', '\"{\\\"code\\\":200,\\\"data\\\":{\\\"functionAuth\\\":[{\\\"code\\\":\\\"S1001\\\"},{\\\"code\\\":\\\"S1002\\\"},{\\\"code\\\":\\\"S1003\\\"},{\\\"code\\\":\\\"S1004\\\"},{\\\"code\\\":\\\"S1005\\\"},{\\\"code\\\":\\\"S1006\\\"},{\\\"code\\\":\\\"S1007\\\"},{\\\"code\\\":\\\"S1008\\\"},{\\\"code\\\":\\\"S1009\\\"}],\\\"authority\\\":[{\\\"code\\\":\\\"P1002\\\"},{\\\"code\\\":\\\"P1003\\\"},{\\\"code\\\":\\\"P1004\\\"},{\\\"code\\\":\\\"P1005\\\"},{\\\"code\\\":\\\"P1006\\\"},{\\\"code\\\":\\\"P1007\\\"},{\\\"code\\\":\\\"P1008\\\"},{\\\"code\\\":\\\"C1012\\\"},{\\\"code\\\":\\\"P1010\\\"},{\\\"code\\\":\\\"C1004\\\"},{\\\"code\\\":\\\"C1001\\\"},{\\\"code\\\":\\\"C1029\\\"},{\\\"code\\\":\\\"C1034\\\"},{\\\"code\\\":\\\"P1001\\\"},{\\\"code\\\":\\\"C1003\\\"},{\\\"code\\\":\\\"C1005\\\"},{\\\"code\\\":\\\"C1006\\\"},{\\\"code\\\":\\\"C1007\\\"},{\\\"code\\\":\\\"C1008\\\"},{\\\"code\\\":\\\"C1010\\\"},{\\\"code\\\":\\\"C1011\\\"},{\\\"code\\\":\\\"C1013\\\"},{\\\"code\\\":\\\"C1014\\\"},{\\\"code\\\":\\\"C1015\\\"},{\\\"code\\\":\\\"C1017\\\"},{\\\"code\\\":\\\"C1018\\\"},{\\\"code\\\":\\\"C1019\\\"},{\\\"code\\\":\\\"C1020\\\"},{\\\"code\\\":\\\"C1022\\\"},{\\\"code\\\":\\\"C1023\\\"},{\\\"code\\\":\\\"C1024\\\"},{\\\"code\\\":\\\"C1032\\\"},{\\\"code\\\":\\\"C1035\\\"},{\\\"code\\\":\\\"C1036\\\"},{\\\"code\\\":\\\"C1039\\\"},{\\\"code\\\":\\\"S1001\\\"},{\\\"code\\\":\\\"S1002\\\"},{\\\"code\\\":\\\"S1003\\\"},{\\\"code\\\":\\\"S1004\\\"},{\\\"code\\\":\\\"S1005\\\"},{\\\"code\\\":\\\"S1006\\\"},{\\\"code\\\":\\\"S1007\\\"},{\\\"code\\\":\\\"S1008\\\"},{\\\"code\\\":\\\"S1009\\\"},{\\\"code\\\":\\\"C1040\\\"}],\\\"roleName\\\":\\\"admin\\\",\\\"userName\\\":\\\"超级管理员\\\",\\\"userId\\\":100000,\\\"token\\\":\\\"MWWY eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjE2NTY2NjI0NzgsInVzZXJJZCI6MTk5OTk5OTk5OTksInVzZXJuYW1lIjoiMTk5OTk5OTk5OTkifQ.lL0MWbSQepkT87OGGBXFyyy7U4We1JoldsswrTAD6DhxiNWif9APO-aVZ5Tofx2HRoyuS7bvHY-CNVjRYr8NxA\\\"},\\\"message\\\":\\\"成功\\\",\\\"success\\\":null}\"', 0, '', '2022-07-01 15:51:19');
INSERT INTO `sys_oper_log` VALUES (2, '后台用户登陆', 0, 'com.summer.web.controller.userManager.UserLoginController.login()', 'POST', 1, '', '', 'http://q9.q9999.top:8077/v1.0/api/user/login', '111.48.134.10', '', '{\"commonParam\":{},\"jsonParam\":[{\"code\":\"1234\",\"id\":100000,\"isNew\":1,\"password\":\"123456\",\"phoneNumber\":\"19999999999\",\"roleId\":1}]}', '\"{\\\"code\\\":200,\\\"data\\\":{\\\"functionAuth\\\":[{\\\"code\\\":\\\"S1001\\\"},{\\\"code\\\":\\\"S1002\\\"},{\\\"code\\\":\\\"S1003\\\"},{\\\"code\\\":\\\"S1004\\\"},{\\\"code\\\":\\\"S1005\\\"},{\\\"code\\\":\\\"S1006\\\"},{\\\"code\\\":\\\"S1007\\\"},{\\\"code\\\":\\\"S1008\\\"},{\\\"code\\\":\\\"S1009\\\"}],\\\"authority\\\":[{\\\"code\\\":\\\"P1002\\\"},{\\\"code\\\":\\\"P1003\\\"},{\\\"code\\\":\\\"P1004\\\"},{\\\"code\\\":\\\"P1005\\\"},{\\\"code\\\":\\\"P1006\\\"},{\\\"code\\\":\\\"P1007\\\"},{\\\"code\\\":\\\"P1008\\\"},{\\\"code\\\":\\\"C1012\\\"},{\\\"code\\\":\\\"P1010\\\"},{\\\"code\\\":\\\"C1004\\\"},{\\\"code\\\":\\\"C1001\\\"},{\\\"code\\\":\\\"C1029\\\"},{\\\"code\\\":\\\"C1034\\\"},{\\\"code\\\":\\\"P1001\\\"},{\\\"code\\\":\\\"C1003\\\"},{\\\"code\\\":\\\"C1005\\\"},{\\\"code\\\":\\\"C1006\\\"},{\\\"code\\\":\\\"C1007\\\"},{\\\"code\\\":\\\"C1008\\\"},{\\\"code\\\":\\\"C1010\\\"},{\\\"code\\\":\\\"C1011\\\"},{\\\"code\\\":\\\"C1013\\\"},{\\\"code\\\":\\\"C1014\\\"},{\\\"code\\\":\\\"C1015\\\"},{\\\"code\\\":\\\"C1017\\\"},{\\\"code\\\":\\\"C1018\\\"},{\\\"code\\\":\\\"C1019\\\"},{\\\"code\\\":\\\"C1020\\\"},{\\\"code\\\":\\\"C1022\\\"},{\\\"code\\\":\\\"C1023\\\"},{\\\"code\\\":\\\"C1024\\\"},{\\\"code\\\":\\\"C1032\\\"},{\\\"code\\\":\\\"C1035\\\"},{\\\"code\\\":\\\"C1036\\\"},{\\\"code\\\":\\\"C1039\\\"},{\\\"code\\\":\\\"S1001\\\"},{\\\"code\\\":\\\"S1002\\\"},{\\\"code\\\":\\\"S1003\\\"},{\\\"code\\\":\\\"S1004\\\"},{\\\"code\\\":\\\"S1005\\\"},{\\\"code\\\":\\\"S1006\\\"},{\\\"code\\\":\\\"S1007\\\"},{\\\"code\\\":\\\"S1008\\\"},{\\\"code\\\":\\\"S1009\\\"},{\\\"code\\\":\\\"C1040\\\"}],\\\"roleName\\\":\\\"admin\\\",\\\"userName\\\":\\\"超级管理员\\\",\\\"userId\\\":100000,\\\"token\\\":\\\"MWWY eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjE2NTcwMjI4OTgsInVzZXJJZCI6MTk5OTk5OTk5OTksInVzZXJuYW1lIjoiMTk5OTk5OTk5OTkifQ.E1JL8vGfCJPjGOjCiypaPzGKId6blrXu-a3NChswL_tatBJq6QirrZuslRbMdSNNRbUR14Q9EZLHNKzyPL7k8A\\\"},\\\"message\\\":\\\"成功\\\",\\\"success\\\":null}\"', 0, '', '2022-07-05 19:58:19');
INSERT INTO `sys_oper_log` VALUES (3, '编辑产品基础资料设置', 0, 'com.summer.web.controller.front.PlatformBaseSetting.saveAppBaseSetting()', 'POST', 1, '19999999999', '', 'http://q9.q9999.top:8077/v1.0/api/app/base/saveAppBaseSetting', '111.48.134.10', '', '{\"commonParam\":{},\"jsonParam\":[{\"descript\":\"APP介绍\",\"id\":1,\"logo\":\"https://q9xiaojuzi.oss-cn-hangzhou.aliyuncs.com/img_20220705195839179937250.jpg\",\"name\":\"小橘子\",\"qq\":\"11111111\",\"servicePhone\":\"18888888888\",\"weixin\":\"没有\"}]}', '\"{\\\"code\\\":200,\\\"data\\\":\\\"\\\",\\\"message\\\":\\\"成功\\\",\\\"success\\\":true}\"', 0, '', '2022-07-05 19:58:41');
INSERT INTO `sys_oper_log` VALUES (4, '后台用户登陆', 0, 'com.summer.web.controller.userManager.UserLoginController.login()', 'POST', 1, '', '', 'http://q9.q9999.top:8077/v1.0/api/user/login', '111.48.134.10', '', '{\"commonParam\":{},\"jsonParam\":[{\"code\":\"1234\",\"id\":100000,\"isNew\":1,\"password\":\"123456\",\"phoneNumber\":\"19999999999\",\"roleId\":1}]}', '\"{\\\"code\\\":200,\\\"data\\\":{\\\"functionAuth\\\":[{\\\"code\\\":\\\"S1001\\\"},{\\\"code\\\":\\\"S1002\\\"},{\\\"code\\\":\\\"S1003\\\"},{\\\"code\\\":\\\"S1004\\\"},{\\\"code\\\":\\\"S1005\\\"},{\\\"code\\\":\\\"S1006\\\"},{\\\"code\\\":\\\"S1007\\\"},{\\\"code\\\":\\\"S1008\\\"},{\\\"code\\\":\\\"S1009\\\"}],\\\"authority\\\":[{\\\"code\\\":\\\"P1002\\\"},{\\\"code\\\":\\\"P1003\\\"},{\\\"code\\\":\\\"P1004\\\"},{\\\"code\\\":\\\"P1005\\\"},{\\\"code\\\":\\\"P1006\\\"},{\\\"code\\\":\\\"P1007\\\"},{\\\"code\\\":\\\"P1008\\\"},{\\\"code\\\":\\\"C1012\\\"},{\\\"code\\\":\\\"P1010\\\"},{\\\"code\\\":\\\"C1004\\\"},{\\\"code\\\":\\\"C1001\\\"},{\\\"code\\\":\\\"C1029\\\"},{\\\"code\\\":\\\"C1034\\\"},{\\\"code\\\":\\\"P1001\\\"},{\\\"code\\\":\\\"C1003\\\"},{\\\"code\\\":\\\"C1005\\\"},{\\\"code\\\":\\\"C1006\\\"},{\\\"code\\\":\\\"C1007\\\"},{\\\"code\\\":\\\"C1008\\\"},{\\\"code\\\":\\\"C1010\\\"},{\\\"code\\\":\\\"C1011\\\"},{\\\"code\\\":\\\"C1013\\\"},{\\\"code\\\":\\\"C1014\\\"},{\\\"code\\\":\\\"C1015\\\"},{\\\"code\\\":\\\"C1017\\\"},{\\\"code\\\":\\\"C1018\\\"},{\\\"code\\\":\\\"C1019\\\"},{\\\"code\\\":\\\"C1020\\\"},{\\\"code\\\":\\\"C1022\\\"},{\\\"code\\\":\\\"C1023\\\"},{\\\"code\\\":\\\"C1024\\\"},{\\\"code\\\":\\\"C1032\\\"},{\\\"code\\\":\\\"C1035\\\"},{\\\"code\\\":\\\"C1036\\\"},{\\\"code\\\":\\\"C1039\\\"},{\\\"code\\\":\\\"S1001\\\"},{\\\"code\\\":\\\"S1002\\\"},{\\\"code\\\":\\\"S1003\\\"},{\\\"code\\\":\\\"S1004\\\"},{\\\"code\\\":\\\"S1005\\\"},{\\\"code\\\":\\\"S1006\\\"},{\\\"code\\\":\\\"S1007\\\"},{\\\"code\\\":\\\"S1008\\\"},{\\\"code\\\":\\\"S1009\\\"},{\\\"code\\\":\\\"C1040\\\"}],\\\"roleName\\\":\\\"admin\\\",\\\"userName\\\":\\\"超级管理员\\\",\\\"userId\\\":100000,\\\"token\\\":\\\"MWWY eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjE2NTcwMjQyNDksInVzZXJJZCI6MTk5OTk5OTk5OTksInVzZXJuYW1lIjoiMTk5OTk5OTk5OTkifQ.pq7SQ1ZaZsxjvqz7kHkzsW2WEHI0qqxEDs3MarHkwqqdVeHzyRLDpUVhswHHhLX2sUyG1hjshIExdYF_29m7Vw\\\"},\\\"message\\\":\\\"成功\\\",\\\"success\\\":null}\"', 0, '', '2022-07-05 20:20:51');

-- ----------------------------
-- Table structure for total_expenses
-- ----------------------------
DROP TABLE IF EXISTS `total_expenses`;
CREATE TABLE `total_expenses`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `time` date NOT NULL COMMENT '日期',
  `registered_Number` int(0) NULL DEFAULT NULL COMMENT '注册人数',
  `registered_Amount` decimal(18, 4) NULL DEFAULT NULL COMMENT '注册单价',
  `registeredConsumption_Amount` decimal(18, 4) NULL DEFAULT NULL COMMENT '注册消费金额',
  `wind_Control_Number` int(0) NULL DEFAULT NULL COMMENT '风控人数',
  `wind_Control_Amout` decimal(18, 4) NULL DEFAULT NULL COMMENT '风控单价',
  `wind_ControlConsumption_Amout` decimal(18, 4) NULL DEFAULT NULL COMMENT '风控消费金额',
  `totalConsumption` decimal(18, 4) NULL DEFAULT NULL COMMENT '总消费',
  PRIMARY KEY (`id`, `time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for total_money
-- ----------------------------
DROP TABLE IF EXISTS `total_money`;
CREATE TABLE `total_money`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `time` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `faceCount` decimal(18, 4) NULL DEFAULT NULL COMMENT '人脸识别认证',
  `bandCount` decimal(18, 4) NULL DEFAULT NULL COMMENT '银行卡绑定',
  `mobileCount` decimal(18, 4) NULL DEFAULT NULL COMMENT '运营商认证',
  `blackCount` decimal(18, 4) NULL DEFAULT NULL COMMENT '黑名单',
  `lzCount` decimal(18, 4) NULL DEFAULT NULL COMMENT '龙珠共债',
  `zmCount` decimal(18, 4) NULL DEFAULT NULL COMMENT '指迷共债',
  `zm` decimal(18, 4) NULL DEFAULT NULL COMMENT '指迷分',
  `oneZeroOneCount` decimal(18, 4) NULL DEFAULT NULL COMMENT '101共债',
  `bdfCount` decimal(18, 4) NULL DEFAULT NULL COMMENT '贝多芬共债',
  `bdfRiskCount` decimal(18, 4) NULL DEFAULT NULL COMMENT '贝多芬风控',
  `tcRiskCount` decimal(18, 4) NULL DEFAULT NULL COMMENT '天创风控',
  `xinPanRiskCount` decimal(18, 4) NULL DEFAULT NULL COMMENT '星盘风控',
  `totalMoney` decimal(18, 4) NULL DEFAULT NULL COMMENT '总消费金额',
  PRIMARY KEY (`id`, `time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1985 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of total_money
-- ----------------------------
INSERT INTO `total_money` VALUES (1938, '2022-07-02', 0.0000, 0.0000, 0.0000, 0.0000, 0.0000, 0.0000, 0.0000, 0.0000, 0.0000, 0.0000, 0.0000, 0.0000, 0.0000);
INSERT INTO `total_money` VALUES (1962, '2022-07-03', 0.0000, 0.0000, 0.0000, 0.0000, 0.0000, 0.0000, 0.0000, 0.0000, 0.0000, 0.0000, 0.0000, 0.0000, 0.0000);
INSERT INTO `total_money` VALUES (1986, '2022-07-04', 0.0000, 0.0000, 0.0000, 0.0000, 0.0000, 0.0000, 0.0000, 0.0000, 0.0000, 0.0000, 0.0000, 0.0000, 0.0000);
INSERT INTO `total_money` VALUES (2007, '2022-07-05', 0.0000, 0.0000, 0.0000, 0.0000, 0.0000, 0.0000, 0.0000, 0.0000, 0.0000, 0.0000, 0.0000, 0.0000, 0.0000);

-- ----------------------------
-- Table structure for user_afu_data
-- ----------------------------
DROP TABLE IF EXISTS `user_afu_data`;
CREATE TABLE `user_afu_data`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `pid` bigint(0) NULL DEFAULT NULL COMMENT '平台标识',
  `uid` bigint(0) NULL DEFAULT NULL COMMENT '用户手机号',
  `af_report_data` longblob NULL COMMENT '阿福风险报告原始数据',
  `af_score_data` longblob NULL COMMENT '阿福评分原始数据',
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户阿福原始数据' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_app_software
-- ----------------------------
DROP TABLE IF EXISTS `user_app_software`;
CREATE TABLE `user_app_software`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '序列号',
  `app_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '应用软件名称',
  `package_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '应用包名',
  `version_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '应用版本名',
  `version_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '应用版本号',
  `user_id` int(0) NOT NULL COMMENT '用户Id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  INDEX `id`(`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci COMMENT = '用户手机APP应用表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_black_list
-- ----------------------------
DROP TABLE IF EXISTS `user_black_list`;
CREATE TABLE `user_black_list`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `phone` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '手机号吗',
  `user_name` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户姓名',
  `id_card` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '身份证号码',
  `user_create_time` datetime(0) NULL DEFAULT NULL COMMENT '用户注册时间',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `status` int(0) NOT NULL DEFAULT 0 COMMENT '状态0黑名单，1移除黑名单',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `overdue_day` int(0) NULL DEFAULT NULL COMMENT '逾期天数',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_phone`(`phone`) USING BTREE,
  INDEX `idx_card`(`id_card`) USING BTREE,
  INDEX `idx_time`(`user_create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户黑名单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_card_info
-- ----------------------------
DROP TABLE IF EXISTS `user_card_info`;
CREATE TABLE `user_card_info`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `user_id` int(0) NOT NULL COMMENT '用户编号',
  `bank_id` int(0) NOT NULL COMMENT '银行卡编号',
  `card_no` varchar(64) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  `credit_amount` varchar(11) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '信用卡额度',
  `valid_period` varchar(11) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '有效期',
  `phone` varchar(32) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  `status` int(0) NOT NULL DEFAULT 0 COMMENT '状态 0无效，1：生效',
  `main_card` int(0) NULL DEFAULT 0 COMMENT '是否为主卡 0是 1 不是',
  `open_name` varchar(32) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  `type` int(0) NULL DEFAULT NULL COMMENT '银行卡类型(1:信用卡   2:借记卡,3:对公账号)',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '添加时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  `agreeno` varchar(32) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '签约协议号',
  `mchntssn` varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '签约流水号',
  `binding_status` int(0) NOT NULL DEFAULT 0 COMMENT '是否绑卡成功 0：否，1：是',
  `bank_name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '银行名称',
  `bank_address` varchar(64) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '开户分行',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `IDX_UID`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_contacts
-- ----------------------------
DROP TABLE IF EXISTS `user_contacts`;
CREATE TABLE `user_contacts`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '序列号',
  `user_id` int(0) NOT NULL COMMENT '用户id',
  `user_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `contact_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `contact_phone` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime(0) NOT NULL COMMENT '添加时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  INDEX `id`(`id`) USING BTREE,
  INDEX `idx_username`(`user_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户通讯录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_info
-- ----------------------------
DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info`  (
  `id` int(0) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `phone` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `login_pwd_salt` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '登录密码的盐',
  `password` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '密码',
  `pay_pwd_salt` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '支付密码的盐',
  `pay_password` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '交易密码',
  `real_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '真实姓名',
  `real_authentic` tinyint(0) NOT NULL DEFAULT 0 COMMENT '实名认证状态（0、未认证，1、已认证）',
  `authentic_status` tinyint(0) NOT NULL DEFAULT 0 COMMENT '认证状态（0 未认证 1、身份认证 2、个人信息认证,3运营商认证 4 银行卡绑定 ）',
  `emergency_authentic` tinyint(0) NOT NULL DEFAULT 0 COMMENT '紧急联系人认证状态(0、未认证，1、已认证)',
  `mobile_authentic` tinyint(0) NOT NULL DEFAULT 0 COMMENT '运营商认证认证状态(0、未认证，1、已认证)',
  `real_authentic_time` timestamp(0) NULL DEFAULT NULL COMMENT '实名认证时间',
  `real_count` tinyint(0) UNSIGNED NOT NULL COMMENT '已认证次数',
  `last_real_time` date NULL DEFAULT NULL COMMENT '认证操作时间',
  `id_card` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `sex` tinyint(0) NOT NULL DEFAULT 0 COMMENT '性别 0未知 1男 2女 3其他',
  `age` tinyint(0) UNSIGNED NOT NULL COMMENT '年龄',
  `education` tinyint(0) NOT NULL DEFAULT 0 COMMENT '学历（0未知1博士、2硕士、3本科、4大专、5中专、6高中、7初中及以下）',
  `marital_status` tinyint(0) NOT NULL DEFAULT 0 COMMENT '婚姻状况:0未知 1未婚,2已婚未育,3,未婚已育,4离异,5其他',
  `present_address` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '现居地',
  `present_address_detail` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '现居地详细信息',
  `first_contact_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `first_contact_phone` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `first_contact_relation` tinyint(0) NOT NULL DEFAULT 0 COMMENT '与第一联系人的关系(0未知1父亲、2母亲、3儿子、4女儿、5配偶、6兄弟、7姐妹)',
  `second_contact_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `second_contact_phone` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `second_contact_relation` tinyint(0) NOT NULL DEFAULT 0 COMMENT '与第二联系人的关系(0未知1.同学2.亲戚3.同事4.朋友5.其他)',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '注册的时间',
  `create_ip` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '注册的IP',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改的时间',
  `status` tinyint(0) NOT NULL DEFAULT 0 COMMENT '用户状态(0,正常 1,禁用:黑名单，2删除的用户：在客户列表不展示，并且不能登录)',
  `invite_user_id` int(0) UNSIGNED NOT NULL COMMENT '被谁邀请的',
  `head_portrait` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '头像地址',
  `idcard_img_z` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '身份证正面',
  `idcard_img_f` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '身份证反面',
  `customer_type` tinyint(0) NOT NULL DEFAULT 0 COMMENT '是否是老用户：0、新用户；1；老用户',
  `amount_min` int(0) NOT NULL DEFAULT 100000 COMMENT '最小额度(单位分)',
  `amount_max` int(0) NOT NULL DEFAULT 1000000 COMMENT '最大额度(单位分)',
  `amount_available` int(0) NOT NULL DEFAULT 150000 COMMENT '用户剩余额度',
  `amount_add_sum` int(0) NOT NULL DEFAULT 0 COMMENT '累计增加额度',
  `add_amount_time` timestamp(0) NULL DEFAULT NULL COMMENT '机审更新用户额度的时间',
  `equipment_number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户注册的手机设备号',
  `zm_score` smallint(0) NOT NULL DEFAULT 0 COMMENT '芝麻分',
  `zm_last_time` timestamp(0) NULL DEFAULT NULL COMMENT '芝麻分上次更新时间',
  `zm_over_num` tinyint(0) UNSIGNED NOT NULL COMMENT '行业关注度接口中返回的借贷逾期记录数AA001借贷逾期的记录数',
  `zm_unpay_over_num` tinyint(0) UNSIGNED NOT NULL COMMENT '行业关注度接口中返回的逾期未支付记录数，包括AD001 逾期未支付、AE001 逾期未支付的记录总数',
  `zm_industy_last_time` timestamp(0) NULL DEFAULT NULL COMMENT '行业关注度上次更新时间',
  `zm_status` tinyint(0) NOT NULL DEFAULT 0 COMMENT '芝麻信用认证状态0.未认证；1已认证',
  `my_hb` int(0) NOT NULL DEFAULT 0 COMMENT '蚂蚁花呗额度',
  `my_hb_time` timestamp(0) NULL DEFAULT NULL COMMENT '蚂蚁花呗额度更新时间',
  `channel_id` int(0) NOT NULL DEFAULT 0 COMMENT '渠道',
  `province` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '省份，根据身份证号前两位统计',
  `present_address_distinct` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '现居地详细信息',
  `present_latitude` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '现居地地图的纬度',
  `present_longitude` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '现居地址经度',
  `client_type` int(0) NOT NULL DEFAULT 0 COMMENT '客户端类型：0表示未知，1表示安卓，2表示IOS',
  `black_reason` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '加入黑名单的理由',
  `id_card_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '身份证地址',
  `id_card_period` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '身份证有效期限',
  `ocr_order` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'orc人脸识别订单号',
  `zm_industy_black` tinyint(1) NULL DEFAULT NULL COMMENT '芝麻认证行业关注度黑名单',
  `history_over_num` int(0) NOT NULL DEFAULT 0 COMMENT '历史逾期总记录数，逾期并还款也纳为逾期记录',
  `last_over_days` int(0) NOT NULL DEFAULT 0 COMMENT '最近一次逾期总天数，逾期并还款也算',
  `bank_authentic` tinyint(0) NOT NULL DEFAULT 0 COMMENT '银行绑卡（0、未认证，1、已认证）',
  `app_download_times` int(0) NOT NULL DEFAULT 0 COMMENT 'APP下载次数',
  `email_address` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '防撸唯一值',
  `phone_base_info` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '手机基本信息',
  `mz_status` int(0) NOT NULL DEFAULT 0 COMMENT '魔杖认证状态，0表示未认证，1表示已认证',
  `person_info` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT 'API有的花使用，用户个人信息认证JSON数据',
  `company_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '支付宝账号',
  `company_phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '支付宝二维码地址',
  `company_address` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '微信账号',
  `company_address_detail` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '微信二维码地址',
  `app_Version` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '版本号',
  `device_Model` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备型号',
  `system_Version` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '系统版本',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_phone`(`phone`) USING BTREE,
  UNIQUE INDEX `uk_add`(`email_address`) USING BTREE,
  INDEX `index_create_time`(`create_time`) USING BTREE,
  INDEX `idx_authentic`(`authentic_status`) USING BTREE,
  INDEX `idx_status_channel`(`status`, `channel_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 28 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '目标用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_info
-- ----------------------------
INSERT INTO `user_info` VALUES (2, '15906278728', 'rLo09u', '3d7bf678ee3d73810ae36946d9577c44', '', '', '陈小玲', 0, 0, 0, 0, NULL, 0, NULL, '320621198404065720', 0, 0, 0, 0, '', '', NULL, NULL, 0, NULL, NULL, 0, '2020-10-21 18:41:42', '124.160.215.70', '2022-07-05 21:21:02', 0, 0, '', '', '', 0, 100000, 1000000, 150000, 0, NULL, 'cndNMGpUQWk=', 0, NULL, 0, 0, NULL, 0, 0, NULL, 8, '', '', '', '', 2, '', '', '', '', NULL, 0, 0, 0, 0, NULL, '', 0, NULL, '', '', '', '', '2.1.0', 'Redmi 7', '9');

-- ----------------------------
-- Table structure for user_mobile_data_record
-- ----------------------------
DROP TABLE IF EXISTS `user_mobile_data_record`;
CREATE TABLE `user_mobile_data_record`  (
  `id` bigint(0) UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_id` bigint(0) NULL DEFAULT NULL,
  `mobile` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户手机号码',
  `mx_id` bigint(0) NULL DEFAULT NULL COMMENT '运营商报告表user_moxie_data.id',
  `log_id` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户本此认证唯一校验id',
  `status` int(0) NULL DEFAULT NULL COMMENT '当前状态:1-获取h5链接；2-跳到了成功url；3-跳失败url；4-收到了回调；5-拉完了报告',
  `h5_url` varchar(450) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'h5链接',
  `notify_content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '回调内容',
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  INDEX `mobile`(`mobile`) USING BTREE,
  INDEX `mx_id`(`mx_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 48 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '运营商行为日志记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_money_rate
-- ----------------------------
DROP TABLE IF EXISTS `user_money_rate`;
CREATE TABLE `user_money_rate`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `user_id` int(0) NOT NULL COMMENT '用户id',
  `max_amount` int(0) NOT NULL DEFAULT 150000 COMMENT '单位为分',
  `credit_vet` double NOT NULL DEFAULT 0.2 COMMENT '信审查询利率（暂未使用）',
  `account_manage` double NOT NULL DEFAULT 0.143 COMMENT '账户管理费利率（暂未使用）',
  `service_charge` double NOT NULL DEFAULT 0.343 COMMENT '服务费（占总金额的百分比：服务费取代了之前的信审查费率+账户管理费）',
  `accrual` double NOT NULL DEFAULT 0.007 COMMENT '实际利息利率',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `normal_repayment_times` int(0) NOT NULL DEFAULT 0 COMMENT '正常还款次数',
  `overdue_repayment_times` int(0) NOT NULL DEFAULT 0 COMMENT '逾期还款次数',
  `repetition_times` int(0) NOT NULL DEFAULT 0 COMMENT '复贷次数',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `xuser_id`(`user_id`) USING BTREE,
  INDEX `times`(`repetition_times`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 23 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户费率表，（用户登录成功过后访问首页时）生成该表数据' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_money_rate
-- ----------------------------
INSERT INTO `user_money_rate` VALUES (1, 0, 300000, 0.2, 0.143, 0.393, 0.007, '2020-08-28 01:59:33', NULL, 0, 0, 0);
INSERT INTO `user_money_rate` VALUES (2, 2, 300000, 0.2, 0.143, 0.393, 0.007, '2020-08-28 02:06:21', NULL, 0, 0, 0);

-- ----------------------------
-- Table structure for user_moxie_data
-- ----------------------------
DROP TABLE IF EXISTS `user_moxie_data`;
CREATE TABLE `user_moxie_data`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `user_id` int(0) UNSIGNED NOT NULL COMMENT '借款用户ID',
  `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '手机号码',
  `task_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '运营商认证平台返回的唯一标识',
  `mx_raw` longblob NULL COMMENT '成功采集后返回的原始数据',
  `mx_report` longblob NULL COMMENT '成功采集后返回的报告数据',
  `status` tinyint(0) NOT NULL DEFAULT 3 COMMENT '本此使用的运营商类型:1-fuygs',
  `msg_show` longblob NULL COMMENT '用于展示魔蝎报告',
  `mx_auth_status` int(0) NOT NULL DEFAULT -1 COMMENT '运营商认证状态:-1-待认证;0-认证中;1-认证成功;2-认证失败',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_userid`(`user_id`) USING BTREE,
  INDEX `phone`(`phone`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '运营商数据表(原魔蝎运营商数据表，现改为通用的运营商数据表)' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_moxie_data
-- ----------------------------
INSERT INTO `user_moxie_data` VALUES (1, 1, '13795460108', '1598581528120649256', 0x30830200789CED7D4B8F654B76D67F29CFAC2E6BC53BA266B86160092C44D7CCEA41F5AD82BEA8BA6EEBDE6AC06AF5048107C8209090DA16AF096280641E12962D33E0CF702FF2BF60AD887DF639D5B922F63EF9EDD895CE76AABB5DCECC135FEE1D11EBBDBEF5F3175F7CF5F6DD8B572F88BF5E7CEFC5DB371FDFBC78F5F3175F7CFDEECDC777AFBFFC89FCCC92A597945FDAFCDAD02B1B5ED9FC5B86F2F2EB3FF8F8E6EB8F9FFCA67B4966F9E1DFFAF0F6C122FCA31FBDF9E6CB2F7EE7C3DFFF4AB07EFAE3AF3EC86F18977E93BFA82DFDD54FDF7DFDE6E3575FF30FBEFFE32F3FBCF93B5FFDE8CBF7EFF807F5D7FFF6575FBCF9F8E5571FF8A7FFF7CFFFC5777FFA67FC837FF8D5971FFE26FFD5158CC24B2A2F8DACF4C5D76FFEF1FB776F7FF74DFD3B7EF337BFFDD7FFEEFADDDF79FBEEC3C72F3FFEFEEFF29FF2E167EFDFAF3FF81B6FDF7EFDEE9B6FF8137FF9CB3FFFF60F7FC97F99F9F6BFFD67FEE4971F7EFAB38FCB6ADFFEC91F7FFB7F7ED956ACDFBF5DEF8533FC30C6949CC89A4CC1D53FE7CDFB2F7EF69EFFCEB73F78F74FF897FEDFBFFDB34FBEFBDB5F7EFDF1C76FDFFCBEBC11FEE44BB2CB63ACBFF13B6F1F3CFEB77FFE4FFFF27FFFB3EFFEF4BF7CFB877FF1E217F597DFFFBD775F7CF5F55B7E82DFFBF98B9F7CF5E1E38FAFBBB0EC4EFBD9C75FD9A0F8DAC45736F146FF563B14EF6FD0FEE2DF7CFB2F7FF9DD1FFFF7EFFED77F6D7FD3FBD7BFFF53F9F86FBFFB3EFF3FEFDECAD23FFB7AF97D5B78233FFEF8DDD77F77D963A21CAD29C187627CE4BF5485F7AFA8BCF20686D7D183B1C51B0A2A7A10746F5E91C31F3E2AF02638177C4939943EBE71AF88607C1354FC50720914B255F1DD6B1318FC95CB30BED35EBFC9C15071C1918E6F5E1B23BB6F3C8C1F9D86EF7249E4AC757CFA7E287FC1A75723F5AF467A697DDD1DF3CADFF5D7C9DFF6E9CE64ED2F4BD92591184579338C6D5F9B2CD7D2E0F7C2A6CEB564B96982722D1B3C9F4AF3CA45ECD1BD7E2B22CBC8EC9D516EC50ACEA7F2805B691E816F44F1397A15FAF87FF407DFFDD1BFDA732A15F890F8DD27CB5B603AF0945F39FB2A1418DE698F6F8DB581FF91920A4FAFAD7BE5F32BB2F7C03FD87A6BD5FBE84ACEE4A2D38F5DC31ECAE35DD8DA89DF856D591361D84E97433BB0F9C2BBBEA4D9831D34199C93892917E7BCEF60B300E6DB161449F3DD9FFEF177FFF33F7DF71FFEE4DB3FFB8F3B8E9BFADA13256FBDF19A01905E9A22828E4D10AF1CB73EFC83478FFA490F31B2FA4F7900CDA74DD17E773EB9D11F3D669333BF7DF5A20B7E7AE559FBDDF5E82ABE574F1D3F7D29AC6682BAF30D9F9F5F3B7577E24755CEEF797EC6378A92B9135F57B19BF0BCF7B203CAA5BBB8199B1BAFCB390A7CEE2D1B1E2A7216B3CBB1CDAD997D7B91ADF6CC55AD6713A32ADB4D9437CE9ACD6A56C55EE06054A59A0D1BBA31F8A8DFF320D02163D0D6F79E99E13BA73CC8CB66E0A03938BB81D5FBCD52958C3336A9764443B69E771A4056059B67CB2D96C05F7D60B15D3575B2FB64937AB0ADF17CE61DA9C06C318B2BC7D60B02ACCB92CC1A34B039AF3F327B32964D16095600EF5AD7DD3E5B1FAC2BAE872C22543795F7223B5D80057EDDD665D26FB311FDC5CFACE98FFD27BB63B08C1F9A0D1623AA4BD3DA1AF443CF403B5D637D4D4542157CC02C22B053D4AFF2507A09344B4DF10A9057ADBBE825FBCCEE80533799AAAE20FF2A2042C47AF57C6D3C74AEB11916D9C8C956F5A335D1F8644CE93E33BB3FFCCC90FC2AE5313B5DB1456E230FDD0B84591742D6715335C3236BAAA3F5A3182336B1CBA74A4E8AE26D7B508AE8323B86E86330CEAADA8282208BAB8D1CEDA09F2F568EBAC321A8F6950FD855F6BAF8EAA3BA2ABB48F7F0F6A2663DC8B871A29D5C630B2A2875835308D1B03DE0936A7A510B62053DBCB97B83F5C0E2F8998DBCEE905870224A99B487B66CF1B147C5F86A4831F6438AF1A52DB2172EEAA1A5FD3E876E06BBEC7DA2AC28CF58B3397C00931E55C2CC94D15E34644922F19D9962F9DB6235CB3FD638A21160C826D43DFB91D2BE208BAB05D984DDB8AD2FA4F9B40D98E491212D9274CF523229D918458BC4EA4C8757DE81B66827545D91C9A9BB2CC85602485A1467FF2EEBDECEF864B70882B85AC836EBA619E5121D9BFFDDB74DACF9306DD2CF0B74AF548B5C78026326EA95A2E002DBFE9D579DAA17EFC027565D9D24CE744E292A564AAC5E16BFEBC2FB8C6CB21E9EAC3998A2A5212EC0CEF13E234224761CAD7A9983AA2B1AB2E9642020C38C5D5AD6532953175972C1E065764E73B4240E2EB85E7369A3B8B4128DB57C4820A7A3172762039C95730FD9D52C28642669974AE244D48913C5EAD5B25AF698AFE354D350CE36F5619DB8B46AC9C17E21A2472187AF7A81767AB901E6C76F23F3D9B260E855DDE58D776D242267900B15D41DA6189D4BA4053F2B309F6807060F826A892CC09A231D9780098B4D357FB93B12D8F3E08396C1BBA0121820F29D9CA931AC90A3E6BFC7EABF7B39D298FFDEF367ADC94173DF6375A4B338D20611987C5DF4DB34B4BC04DB8801A4266B77DBB95A725EC2AD2E49AC5905F6F2D092C6406216C695BB1DF9866D6A581F72E49DEE49458AD915A3FA33E2C837B30F129B510F35A7644AF1AE63EB366C036287AC9EB26D6876201D3B9F53CC6CBE5A5AB4A802B3F00CBA99FD29E483C0FAAF5E64C3E799EAFFE8B7D85E2C2E4470195D43C843A6E23B6FD7D48C20DB5CD055529179572D8B2D4F2EA8419AD00FD28497CED42A14B64F20C33BA9B1D8919550A1455D5BCC1434DA35F3351D9E8C57AC5F06A6D7B686BD216047BAFD3BBA6615DBF06937A0E57DB76C69C87C045971434131BDC86D0F34F10B874EBF8DEA29DBC68E92BD234891E83527DBD03540E5A19C5248BA7339C4B6E5B565919A50E35FAFF8D8C496E7F658E0A4AB49D8B1354AC026D4E0AB445E31B743BDD541CA288DE1F7A1B8F20D3949092976C075BD2275B32168F593A19694D7B809661BEA69A52170A8A5F4117BD7513D5DBCBF642554A5EF72B591088C74EB624C2A99AC7541D55796AA85E4B1EBACD613552F2FD9A4A596820422AD91122AE82EEB091E935D620123D1A20EB4186722BC8F3FD825A46883516F94C4409398A3EE6819124C9152B910936A1CB4DA2DB64A1C5442F58857ED6BD437614104A3B62688C7C35658B1FA23D7DA7F395FC823AB71C866FA7BEB74E09A280E527C7F74845D9ED818B68D75605B2BA8021895D39319B5062005556CB668331156D6A3CB90AA1B53D6A526550D65F4FA5BCCEE9457CD92CB44D518A054E3EB01BBC8410FD978A2589690AFE2F9F8BEE7E397CE0B071A86BAB160B3CD29154D8FF89A57922E24D00BD5C5B94B2C67A2CF9A85E46B4DAC17EF03CAD55A55794AA900D9C41BD243AEE105B7F3AA3FF0F54D271C1CA5E32A92A245FC22CCA5E407795ED5B995AC52669F5B6BF66AC0ED7021464AB7E8D9E7E215F9E2AB28971A732C75A81FEA940B9B2846CB4CFB2ACA9D147B405659EED4CFF1894E4E2B3FF04B496AEDAB84049B5ED9CE87DAC5A095A4FA2589C6EE1D1406779DD2E3E0020B55CDC16AC8B5CA04CA96C6D4413636B9E4F50B454B852654CDDF49675148D986A2955B37642F9D62D0DBD68BCA364E584516D1B5D3587820BAF41CADD43065E7A316ABF43705C03B45D7438149BAB6DE426DED8088FC489DB7CCEA3A5BD233B4FE9245038B0FD426C8EA5F89A6D01FBA768A788FFA57F78640FD92DAB184153C77921C52DB22FF5081AB9E08190C0F69C02199CC568071515514926748B524157236EECE873764A92202FD773506C9878B1D78721D6453534A0133BE03A9F166DE65FE2F4B4F5578510DB23B07F68AF49287ECD879D967C5F8767DE3DBD5D87F93A9586A51DD8B280D34FCA59CBF8A4C2254B14C4CAFA8CB84404649735D706B5B1882AB9EFB148AC46449338257648FB9F749CF290E916D0BE2580CB9E3ED0656DAC53A45BCB985CDC08195209DA6C3682C1B304ED161AE162B467962AC9AAC631EB16126B6990ADCFC7BDE64282ADA091E9910BD21C5B16BC0B16A6DE8893BA1146B8CB75A0CC72DE599686555678F59B215228DAEA2014BBB0ED671D82D6E4A2578D37D62279972E889C7C08AFE6AC016EF6DD0CD5171678D295D6053BBA6770AEB0746B09AB7B386F5A5E505E937B2B37E04BD3F99F4105ABF4CFBA0A9A62C1FFBD49D8AAE68534A6CA9FC866A2CB825DAEF029840537DE95674E3BD88AF1FFEE287DF7BF1CD4FBE792C8D53FD338D6EBC7EFB07FFFCDB7FFF3FF87B1FDB9FC818EFBEFC47C2AEF569A508C562B2E7D35784C92A66A3C54D1B9695ACB966AEEEC70AD9582B15AA430CAD81E7580CA7463BF763644AC4E7D6D8C2AF2DAA4EE48A65D5CEE839583AAFC2142C17D564D81C2CA75EC543CF8490EF2862FD500C5BD47A976331F470D8B118AC7EA173B60F43D3EF876218BD5479CA19AE0907484EDB58FC1E2C239D2BD873DD8395D462E0A3B1AA0B2509430C8B5211268B0B560FCA4B7C648F8AF8C1BB0F6F7F15472C76B5507D5D5B6F693A646D5B103126CBF36E08435D705149AD34F6C6203098060DD96A46F6BA7E5643AFF73C461076980E0761CDFF75C2BBE836AC6BEFB8ED8F589B2A6100F86E8A78B29EC4D121D5A1AD581220D2DBA9EFD8674FCEF1E715437BC5888836D9DCEBDAB260756FF1286D72128698AD6ACEFA8E77C5478BAC7EB34F41A8155C9A1F8BDE8D75ED09773AD47BA717E8DC75EFACF859449AC39B255C273990A81607DCE3D0B17075DE65D3F4E9004B68C12063712F9624A669D7EBBB776B7C0D061535D278C0DAA61677820E9CC4879D29C6256F7B068EAF19C7A8160B4FD80F927210405C2DC73907678CA58E25E51775A5C5448F7F2671808BDAB77307968F145349022317551763AE16953B95ACF628717FC1D05A1A8FC5D05B8EFECA61B8AC32931D7DCE1A56522B9AEEC0722EE51D58924529F019E899DA6DFDAC925D1F657EB945F6EFD169F7CA677791FD13CC095783AB06915D8B37E528FAD82563A7FCAA66338E30B2BB4F927BC57F47ED72AD661003097D5BB6C460A2772959D2AAA5CFC76A96CB8EFDB9F784D989A7B7D57804DC60A5E432F9A87510E6857A4818751F2FA0DAD14AC1F2F1920E4E7D6843AD1C219D31FA1E49D87B5FB50EC34730953276762F185AADC751F7D0D47D4FEA7087635CB815017B8A3142655802C3A526069B9D187154A868ACCCEB300A3D798EDEC1B6B6DEBA73C0DAD21D8F58BB8B76F2D105637C705A41F305C915B55EE768DBCA2C3E1096ECD98BC55A3138B5FEE9AEE0B561184A867D3CE73A7696593430ECD70BB78F646B93099174915C9BBA59CA6089C0B1BDDD30825ACF742C8641B4FCEEB350B1F88C03326DB74D4F8B5F87DA95AC3243209B73879FE63341617AE7AFA1F64446612851AE76972D75AF5EBAAC3D43E7551A017E4580D4A9AF281B6F13996092294ECF7D7C0E285093EF82A2CA1DAF355DDE973D7254C6D9A336E723845D06F59D07615D7B8F91FB88B5AB91AE91F5DEF78E52666D109C6AE32E30A4F60F1D10D959D7477DE481AF616A9F8ADD97277FA4CF74C500FDD88DE7C83DEE88FB7C6517A5A4D168A30C6E714067F9A9E0E4E51A4E48B85DD79E203E6AF9B00B489A73CB7A3D0BC348193420AAAA0743493A176CA428A34CF57B9807E4D6F7C5DC9C340BFB988C670139C682B3534F11CB221EE7722F8370BB45EF0275869C2E15CCFB8A5036B02A8D9C0BBAECAC38A49369DDFDFE3AA4B5B9B2A0989A18471315690303739C2F616537421122993245B05DD6DE939ABA77EDB8F0B561C2264BEB4E0A8512D52FDD386AEC0C010C97F48DA3CBFA405A7FD3A8A813566CDC25581EB1176DED0967A8AE2D9974E4A6C9F1D7DD820BFD056E6B25F642F400720331FA78EBFB4072715D717E160AD5CA322C34354C105E31C04D19DD8720FC9A3271142D4E75A1441B785993B461B60DCA8265C97740C9E03BC4796F1D38C20720952789F7298CB00C92DAB9034BD26DFA4CBF03A4C0BA3C629D0C9777758EF68E0370AF7C5CD73E3E21DCD6A67D3193C73A0D2BC6BC5E1753A76D898D83C9AC1D18194CDFECC0F0683F4D316CF684E86428F2F0C6D91A06C4FA36EEC0B23353440B86019F67078654FCCE2B915B30E2FCFBD29959758C386CCB237EE7D6F2A0BADDB1FC345544955F1F4CFD6567B3951A16C906EBAE5F8322382E179290F052499D6219066ACE3FA4BDABB726F37382B16A6580C0E45ABF8419243687948A0CBD95E48536832A0B9F5BA367072B3C9D2B62FEA4928AD0D4A819F4862593BAB1B8DA82452109E7681FAB1650ED89A18EDEA1F00D1662F3DBDA3196CC3EC79ECBFB188512CE0747032C5339ACD0C61EA9AF907959CE5A951BE1060A4DA049797E24BDAAA2811074FE169068BC9C3D5D1A9D0CB3274BFB08993A7F792F94EE58DBE938EF74C198993FF31757E2F8FACFEBDA137233B5ABC3EE0B333E6E7BDBF273CC157FE1E1C50C6DB6AD7D609BCEF9A6F27FFDB0A4A61F0D7DC9C879D6FB2E90EBD832BE26181C9E8F1F46E5DDD2733CA12DFBBAF68468AD5BFA1201D3FEA656810DCAD2AB5B33AD1FE28016480A83DBB9A2A061C2A781422283B13AE6B1EBB8624C744FEDEB4ABF8A08E47A017D64B3DB672AD1B365DCC5CA952A03AC2CED5D98BABEF48EA0C6E270E7571438CBF16B8412844B4EA3ED3B22E577597F62EF45C3F0FAF80854D05FD69E91366E6B43F4349BC9F50BC69EBF1FC2107D0826489A32F4436D58FBC20C547BB3E7791A061C2FDAC0209DF2F920792BAD9F07685A49924A894D07462A1F02DA68DE127E918A6FDBDFC7B250CFDBA6FBD9300811873BC4552BDD01C341BB1C86150B0CDF3D352C5327C3062C725FD10C1F71329D6AE10A630B1650B985D183AC6651327B84DABD4AE6B2F61E1A8047ACEDC4F5451B9C8B75C5C6483EEA05AFB5F5D143EC4B9BC6F60563224F60C3A8C4D073847E6B7C2C7065CC762F8BA99CDEC23082D9F39565B9F7B24E40C8355D830A11CB97DB47A36B7A5AAA6226F6ECAD18DAE48AE330A476645F64F3896390A829A806469274E4CAA072F6B9C190D01DEF3289EFD52297B5F7D85C77AE4D65A93805CCEC2D53FE3C0C0735448EDCF4757D28B530B67B05C3544222D07563B330E6ECA85B1DD8909C3E5DF168A4C6048E354B0FAB44AF18F3C882A8B62E793BE37EAF6BEFA97ABA77EDDAAE242A7C9EC2B862CCB30CD7E99813ABB6CEC208202FCD13C2D8D726FA8833DBC265B3D6A67D54C48F5D7BCF3D78ECDA7B8ACD1EB1762D2B9A247FDADAC78787292FB13534D93B36ED4E8741DBED77C074263EDD9935D9EAF5A3B4B4AC83BDE443E57FC1409BE346CA3FC921F6FB62C5F71EE2B6B63E60F080B54D6D6D41CBC483CDC25415321F2D351048ADC30F3F569982C454A814DE7235CC7D85822FCA5382AAAD7F07B43A15EB285832FA36C5451CCF386D97B5F7307F3D62EDDA5401C6BAA4C9C8D6AA4DDDAA69300714D70D3CD5733028D53E4C9C4F25C5125D8746F98A830608377042F5BEE3AE63FB18EF7E5D7F1E990785E57ACC88E2AC6B1F9FE0A75AB3EF136C97F0D50BB678DFDFE31371D85B05FBB34659D22BC6BC3EEE862113A8610AB0BE1CA98D1452F7043D873159AD5D97E5654823621F2E3A8FA440DE8E50F451A37B2E476BEA66FB5357DCAD79C1A399D63EEDE82908E6808C7BE6E54D25A82F7C093B2AD62FF6EE8CC0C165ED0975EBCB3C6EB8F7C6C8602E937DCA36CBC40D15CB5D9CE609E2DC4D74C82F45BF6890BCB03560BB6C71571894AE660B461452DE35BF632C9DAC37032FB9E140B33C6E2B913A96FF3928B5D24D141F9867ED196A97F527124250653F77790659CD75ED0937BB56B449C21E8DEC8CAFC56930627A146494E7A5F680F75AB70C1A064A923136052F18A82B363AB16639557B9EE3DE5375597B86BEA8E53E324E131C09296FDFE4624AA0906C17ABA63C617E1F632586208308BB9B713210DC92B90D5487258185F617FBF38782F1E934E9F4A23B4D3A2DACAA52FF00BA095D4FE70603BEA62760B037621CB96CB596E05B9C599E61C388D51C864BC328F8624D2CA1943CC68218A83B1D1EEBF2E03DDA7A531EAF01AD86574C8AE1B5A258D053DFB3E7765EABC48A41B8DAEF73979D8B62C55EC55C4616CFC619FE4A39BB211644DB7C55060DAD871364380570EFFB06FEEDFAA8ACEF994B0DA3D659C1FEA331328AAF885A8B1B50E8E57F6A5054ED66AC502605B10AA289148A24ADFB58044FF511769044BD76EEB4F079877D743B77D9B9B76B1F6D9F5FD636102DFCD887B9C5987A291B670FD81C6359131B27AD24D1B370EE9D2929A17E7CCEDDB0C7A1EFC685B5F9844BF839A06056851D50FB8B6DEEBD2AEBDA47D767A5CAD12C73C1708E66C77EAC373ED9C46758E9866A585E861C81359DC32BBF62CCCA88A60B2BB43DBE18E176ED09E7A831118314E67D7FE442DF0BD73A0C8DD3B350641BF64DC4BC7F1B96B5271C9FCA03EB0C6686F6BD9CBABC0579A8363CA90B952D5879DFB5D4D7F567E5B8D3424C682143F352AC980305EBB3D6839616624299D13D27C4D096DFD7BFFAE8E5C10AD5EDE567F068AECB5BAC19656B79B34B5ADF2F26DADA87D7F45CD6165244B0E649EA2F62624D1F43D0EA30D2C2972912096550B2365A1F0C256B4B275055B1247D84051086C6CB8A81B6F90E4553B500609B7819309C938BFAC3F8DAC96F614BACB03F14C42FEAB02F3E7F2854978F978766D70D974F52FC83978B8AEF553A0DEBB74070D6E0C901C1932B9F10104944191E20412C9E9D8C50D3859B5F025687D793DEAE7D7427D3656D7F8054D9B21757209CA7EF2901398B8F413142999D6D312C98FBDB146B4FFE2C89DC969F2491DD4483725DFBE8EAC8CBDAD8BCF4CB51DA7A3BE7A1E013EB0628B5DA4C8843C086E09E0B7D597F1A9956C330C2D08496895F0488EB0990F381F0BDDF0164322A7B7B043B2721CC132897B5F778A88F5ADBEF5BFBB111B2D330A4B308AE93C921791624FC951512B00B94D937D8EFB19EF58A3131E8672ED9D20931E375EDA35BDD53651AAA0CC10764C98AC992272BEA1CCA06E5EBD091894194156316AB4AC568A1BF195B4D17E974FC5637FE1C56DC8737B1DCACFDD876A5F1DA8D326E0FD7E14693717728EE2D0C2CF49E024C967BCD2E18CECC3D74BA17FA198727F44736ED7343F152018DCD79EADAE7D7F5E78940AAB515326AE9F8DAA0EBDAC77BAAB236550AB159DC1E3718A8A5BFF1FEA59D68176B0174529F0D4A5A4ED59E4EB27B4F559A7862537D3B11E1005DDE4E34A5C321B1C280DDBA7D6D9416026E542665BE16E483F551CD09361C490BE2BDD9C197187B5AEF5C1C8827C83422F66412AFEA92B121A89E18B559E2734CCFCBDA878F106E6B57F981D35577471ADDA2C09D7D4F01A5DD46A083AD5EF6ACCB927579B034E2B92E9F2AE909DCFFD6E704BA8541734A9B30ADB9069BB729C3016CF2255016B34A2D205DE86EA07A8FA1DD7C591F95E923BBAD8DD9862AF43E6D77117A7D3FC2DA47A27AAFCC6DE3A8CBAEFE86C7AD9DF1C968E3A620C131D2C70B87FEBD0F8CE22C9FDFE0BBCF54C32368D0F60962891AC18319BBB01AD512FC5C0C13C959EA147B9D0A942B3D2A96A91B0544AF18B3B82752659991E8DCF1DC13EBDAFBAAE21EB1B69378CF01333E5949B2FE2A8E4CD0B58B5FB800A15ADD4D2FB7A21C5077C547978ACFBE36397737FD3C2839C0E600F6572B4DE8AEA3105C2D882A33BA2FAE6B4F083FB489A8FBC891C6F9589569EB1601CEF80E107C55FAB318F76F312606E62ACB93D93797691CE828C5DB5874A1DE0A495086BB1D1AEA34202BA36EC82113E73E35248857EE208942F1685783D1F3D9EBF2B0BEEDA7E7AF18F36813160C8B8F0DEF2E2F510E9CA68C02158D2869C5D8C7FC70AFCCB50BB9F684723A59DB4ABAFF8062B4422546ABCDA7BBE0987D93E1BB1493CE4AE63F77E2B1E6B51025ED6357DEF277D93CC8311A977509F239A04056C55D50D2AF16B050DD8E22EDE70C04F7BA6C03F9CACB024E2EE905A6EAFA1E2F4E1DD92D66194880566C8F548AB97467C33BBF8D0135086F2C2F7CF78750F07468915614B449B5B031EC4D4AC696C1C995690B6867C63690845402D27CDEB44D0C91C8F1CB4BCEB9DE1ED57007D2A6BCE94AAC1847D55828F472F145975E2EBE7424F699C1C5E8209E703E0A3EDD658C5207FAA174F9C50712E64139F3A4585615AA1D0F3C8D47B927EC1A4CAD57827349D6F1ADF2BC86A681E24B5B84695B4A34E0E0159B8AD63BA728A10623CCF0336364E7A3CCBC39B60D7884EB107A8199B3102A31366A16145F6C6643CDDBD4395F751C191B39B05EED67D72F28B61C5145B8CEAAB24EF1AF1A9697342F80D5373DD7F523D24B34363D2B8624DC21F2BCB1BEBEC1985693C718E952C7067532B065186BC384C9817CD10280154B78C760EE83C1AD9C8F10973E32D086FADC08B5DD0008638D3378B718B3782118231C45CB46C9663286CF704762D58CB1A4DD60194CA5968CA885360C54A78378C2BBAA873698F50B512E100BDAA1E74F4681A72D6CA084DA053B49675DD6871BE406F7A5658EF7857F37F222C23C43CE2AD9848A238E444027530CE5CB8A31AB3BAE6164D18D7048DB90EB04E29E2B0A5C84324271AF2D1FE2232CE27E33D20A63B15A81ADE527D2E09F862136173A30905D0619D514A51E25F9907429692BDD7E84AD8AB1AC7F5E28754A363CD081EDA314A4A2D698425EDF1D239A18E3021BEAC8CBFA10D9C886CCAF7352E4D263B5AEEC04D992F84C87E09D56207481B2FBF80236CC57F6B83DBBDC1D98652A0E96E8196AE30B061A9D1AED0C09868D7037CEF8BE9C8C32D3326EC349D832C6769E25B34DC1A5ECA2D07DAB4AC0948BBB8795B98F0ED98A318DC2E582E1F6B1718D37866FBFB1319AEED6541306675BF5ECE727ADF920DECC29801CFD1B9EEC507250630A5728A83F782F14E134FCC599445602255270A28DC3B940D10153539D217EAED87B2089F31D107BBD4E3FD4C82C6265970F5200044F8DB4BC3D1D66F953616CCD6BC339913A67516D178A574A7E2099B0F7689F0895AA3145F83CF14CE243453514704541B7E869A0841A9B9B13025AD747EB1A46FA2D2D814CC0E6ACEE395B9C4BA83FC7D8113669A13A069AC0376D821563D6EC8458E9FB930C42C55AF86A02BE995131276DC075BC4C0A887881A913622332A50A00FDB83D632CAA54637395CF6930CD36C075DCC8533819656675810932C25D7AAF2614D3ADCB07F8550D426A2761B4518CA8E4DC309EDA740FBFAB85687C4D122B1CD6CF2E198D43F20265C114CAF085CD5F1EE480DBB13C3A5862BC3C74B5B79787C7627CC6E51D1253DC5EDE62EEE3489E8A7D05B51E5D35B70DACBD5D29DAB4B85B2C6C32DD53C4A2EAE1A3FAD50AA05891D678AD41265E06A5C40304FB58CB2E283859FA6747A963253CD4BAB4FB243C7BAC09539D6E979F23E3EAF22242E731A93F3F142FC10AA03671180CB9AC8F32818E1CFB5A0F43061F76AEEBE5469E90F062086B838BA1FB0C959B07E29D196EF3E4E5E990EE06CB4BDA124DF7259D0A03F3EB3D1918E916823DB76D9BE55C2CE9EA2A782CCA164A94BCD7F3EE0D47E2D21353A2972A3854C48CA4A45D9A138F49EDAA155D2B069A0EDD780E12698F568FEC3963E76249854FDA356A1910077F0DF3081823E5DC47E48FC94BDF3D1BE0C174EA7C04CBC85C55D0C7EC0D38793E082408701DAC9374BE49D175ACBFDA6C6BC36433FC39A1D4865A7430F05343999AEAA09A310E7821F4A8DCFA6C14B839611385A096BAA1DB7A591F1E213A3064DAB415C2278BB864A85B94544B142921E5C99BE6EB8A312F7B2F335E42E536C37DBFE0D8D83736E9C5626723C9299E19E2BDA2CC0CBE9E8C02F33E6EA118712DC156B4BEF9F28C10123E027DC75E9C8632FBF49E8612B0691AADF734F8121C6519A2D795CA460AE0F192575663B438C83AAFC9D958541B218E70FC69E8F853962A112948407D581FBCF48A5B753867055A66A7A2C7AF9557EA3DD66D02948435B1D2FB81E03905412821F0373532629F250ADAA1B8894287CC481B768C9E0E031335ED8031B83DBB2D5FA8921E40F4779BC1B8E70A3335822930F99533B8B35909D349250F8C75929724B4401A9AAE5F7E5D7FA2A75907AA997DCCBD83720B9B626661963C1B1972797A58F2B2F043B67537CF079A6DCDD4E964D263336B7EC52DC6BC560E99BA55B91660A7C0B2A1193A2F2B2EB126A01DD6DCB443877E3B7483F207844C87ACED0D2889DD0C9724CB19F35E9BCB701A4A58A6038072737898578C5923A6E232FEE788C1BFED30EB566058BA39617225E37C0C26A96D625714D86E1EA32CE3BCD117B6755B4E03AA155F069C00DCB7662FCB4346733F78D296F77877C068798B15AD0C97B735108F56A8F796A75ADE31B1988F2AD5CD0159AA31862947E4A836300258B4BB0F431A27E765A904C3568C79A5496D6E8D60CC2B1B5A301C1E15352658B64D7BF2E93C1C6A2529709477183C5861E0E2D7270293EB419B68CF373EA82358FA4741FE935166B2E8084A6537830354352EADCE46BAA058871BF2DEDA62C8874EC5500392A24178F447B07CD08AD1260C559C7EFDC015419D9DE3F84DD5FFD1A32CEBCA404B558826133B3DD47B490B041064D903E1A146CFDD10400C6203C22C6DD053C7133C33146955B6E0088EC27B42997DF6E87DA4A053300B9693E0CD01550FFA24B186517B37E089B0647C8CD95BAFB332B6B922FCE2D099C4557019C30EBC53678B8417DDD9224178284D94570AB20BF478B3CF43208FD2B3504A22186C2E3144A7D1D9342C7602223C475668B4828C2EECD06835A84A3C0CD99C1DEB6C5D9E3097A9BFBC15AFCF647CE4769F04ACA17870CCC3B8CD62C508F37CCB86E16A20040FB25DE2459F1B4724DB01A6AC35DE26F24E339E1A9011122BD46196CA74DF49E83D5F183469308631CBC4129C16B26B803C3F144981E342731FCABC62FED350A8B2B51E50003F48E8DFC24CEC1E7D8E3041E6494D995874BBFEACE075C310AE69BC68DC91CCA93642C1E675D3F27340C10CADFBA06CC175CDC2D9AB5B9B6D0A5F417839C719DD5B8C59C379C242711D2CC26156DF1525A11D61034D66676655740A4D6FAED53D309D7649750ABBFE4C8D50DB1BB44DBD57407A1E025B4C708DA22929CAA5A937A6BB2FCF15EB18A1F3EB8E45F8C023292C72914C54459DA945F226CE6CF73C1F655E53694591D2E2740035FD134299E9EB2CACDDF1801AD9DB6BA31A21CF18AB71C3E2ED5A89820F54D428DBC2145EE6F4FFDEAE3FAB56AE61F89AA7C082C4369442263919D36A8CED3E8F95CE33E89A7643AB8DBE5B0ACCB1ED1819BB578C79BE95892299D9F8C025B33739242381627D43622D413A6270C848A29D8C32553A57FA76B10DE7D5223C3F147B0083683FCF75E58387E7046E1B9A2BD6098EC1B95854A3920085D1257F47557E5A76B6F5EDAA15E0C120FEF5A6985E31266ACE2007DB253C2E55A9DFBC73A9FB2C75A8363C44AC5F025E51A88E3987AA38FBBA999737AFBCC34B8EF5E57D8D74409DD24BBD3F7F79993E5ED9699F0E1646BABB174BDADA418AC142963DE820D3AF7D2951B73B2AD3201F3678DCC5F0489F83D24AAC0B5ECD9982E50DE22F72D62845380DCBCB5C67A0E764E8705CD6B7B34AEB2B46F5681041B3559A70C598F81CB6C64F8E28881A594FCF1265AA1F50CB528403000E7AF7ECCD5310AC684B3C7E5172922889AE35D77A07A8DFAF9868638ABC4229266B935BC2429169A0E1913B64F18928E4400EDBDBBA3A97C9E9C69F916494540BC029E9E2FAE6D39928EE80B6CC0D945A780BC6190749AFF90835836FA0EA8DA19E277C0ACBA67EA4CAC512A0F2FDAB9EEF3E47C598198DA365223D3C6DC5FB289521D185A85B7715A9B24BCDB38C568C796F8C5ADA3960A7AB7BC11B03978358372FC17D9BA54E231996271DA8D6AE8CF5E18A55AF9EDFB63EEB79B475B2B8646DE1DBAE1EAC4655E5270E6DBFC1403B0147072B2FB39A81FAACA6DC5364BB8864F69B517B2AC295160927C327993337C6803B4E471872840D5E67C4A64A91DB22FD287D241FF1DEFE9179FFDC505AD0159D87A20BCAB45C973921BDD3969FD287D148758EA8F2F24644AF5E7F774581DB3D3750A4DA134F140CFA968390D0084524D490B9C32B6D30A68031CF4C9E859523165BC6F1BBD3B5D68A05A58C9F28169F08B8C7B1CFEA5D51A47E08D22D773CD133C6A284B367F4A6F3DE6000B3E047EEE4B23E9EA81CD97E6D427641327A754F923484CADC749B42FF589F0725012B0B4E831FBA7C2B06DC503BDC1D3E5ED272708ACC5EB04E91A3E761D53CAFCF07F0F06FFAB50D0A9FEA1AF860674325D98EE469A36AE3713C8CFCA506653F07126CD36D23352F044E608E0DAEE705532796CA080898CF38F10E055E36E85AAFE6E6A56862DE14885B9499AD8FE7A1D49B038752657C990D648AD5D5917B6D9DB45903476DAF063F154AF20E69565660593FC2A34047864225260B70EE848F408A914C2E7D25578B009CC329E7C7F2E602838B812702932BFBC52C3EE31B0C348E303A67B5D6C041B5064B8ED6F3B53452D5DEABA216AAAD58B35033EB1ACE469959454DB582221C308FBB9193695C6B0D8524BB02650707D99BD3D687C2C3DBEB036181B6D762C53E151CBEF37018749C4DBB14991C3463AC63FB1F56CAF204506AE18767618C5DF77EC1D415013B62638496439D57B5BE62A0F39E468AB162980306F1D45EFA502FA52FFA853C19CB56C26F98217FD3B0AC7385E984E88959A6B2C086D288C3F306676A23D0D928307BC38AA29007FA175DF240BF34F0198800BDA9A14C127E702D68A8C8A5D3B184B230E0FE7B7F07CF4799674E5F50AC45A2927577820CF0F1C9BBE0C6BB7336165451B41B2B48360ED0EDADFC8E0F7724C7FF91D0B8A21DFD4DCB1DCC036824026F899F8DB432E2CF85057371ECC2AAFD6A1367A59E8F326F56EAB92836E39470CEB2CBED82B72507C590F5B565B10EAFC62AE43B91CA9BF5D161A55D43D92F2DA41EEE8EEEB9146721B03BECC0B46CA1188DB1896C3229B15DDCC322894EC391232939895EEFBEA8388D1F1AAD5DAF0EBE3E0AEBF9A1D49A6FE09C8D5DD75B8C59B98386214DFE182F52A74AA62D4F92C9812A7EB796F778E6A33AC33E29C51EB72870226F8452D385ACB060A24CB252DA18240B3E000227553D35A0580964E04895DA3C7416421DE13D7120CBF928C7C50B741473C4C89F8D9A91CF8334B70EA62155C90C9B16FDC6C1F351E63541FA85FA800CCCCDCFA7BA0EE8714197664EE2EE36E255F4C3A75951D093F6345002CE7E73899C18EF4288462B83B9C1823CBEED4799BAFC9C411C37CBCF20A3BB2CEFF3ECE5A18BB7B57C82657BB72AFD7961B85A230CA7839E024684470DEFC180339C7B3030AE7013B3F1D6259B5A08648C859519EDC46A63DEB0D2AC6272B0C55A4F640A69154D2B94C5CB0286718915051E1EFAF9516C153406619F1C463C2FEB03DCA69BF195CA3AE308E7511C94185E60D8DB8623F79B7EC367404203227B90EADC08D84A1DFAAB27A34CCDE1341448AF99A503379363BC6483EDC8E873B124C803CDC15BF3A28E55818B3125D7130D8C65FD41D4247A0879C59898D431CBFE0061BEEA7D191B8CE757E63B55420D8AE47140E28541186E45C002165B0847042B36C51AD5AE9D80D5BAB6D690E03D495D957E6B48A2A3014EB90C5EDA8A3067EACA5908757EE4B46AC75B8C8929A38A21D1DD39218A3AC4C9830D4DBCBC56C07ECAF20452C3D41B57A4768CBCB57D6158A1843AE2044122914F8FD7813A5F3C05E36CD6379FEA9C28A9D645639F8EAC8D3AC942838995AB6DCA115E963747B058F7A39E0D45580E90FDDF5A1E6CC91D2EEF652E20640D6F2CCF6630EC6DF74962FC429B261EEA7CD3F47C2CB2C8E8DE25A0CE169D0B64721092AB1E16550F19EE5B1A9E8686312F0C771E860D68B88AF8104802CA9BAE765968F40C9E8934D66586735137271A90F06F43EE436605E6F871C891CFA9E811A5CF8075408796C999B1D88D5461F26BDB1A27E0DCA7C93679CAA5572526D47A45D41A5C5CCBEA3F957EA9A30005715C409AD35EE06F5D7F62E08FF225F23FB3E2FE79A2CCEC1E58F8210B1E90DDB29A17BEC304F33FAC9D465957D67991D43315E85F63DC89E1F0B24D562CC17AFE8F466D7C368E783370DD4B4A2576ED805C09170B1244DA0A8FAC1868BDE848F0A76AA7DB0378DFEBF83F22DE9B3E90C1E58BCC1AB0AC2AF5D4D279304D5AE2B4CD5B318624FC637444F2D251081D13A3F17A7A98BA799CF73B1D666A1673E1887438C3E6AF198CD065CC2C63B9624CF46D1B06E1F3133E3B868C33064BECF66CFBB930736F7E6D3E13B10CE55DFB1E5F5B3F2201AD4DC5DF08203D3884ABE641A97E4576C4D3008B0532EC91652276F8F564C61505F5C83650AAD587F7D16E47302E5070BFD04E28B667D0B16FC312A9F35032C8A9BF699A5F30A6CDBA600C5FAB21A0B0B6E41C7A07B94EFAF350EDC0E66B5A31260AB2CA27680D5C505AC885C2FFD00FD5F3425982E530096788219233DA38D1CF81030F3819E3D89AEE4D07D4C48A2B6693AEFC2BFF9ECB4705E23AB7BF55931282D262E3269BE88CB1FCBFBD9CD98A8595633F452C2F3CC32821E7E64E091F185A81D52FF5390781846A07DD95C000644CE8119B7B617F92B917782A5D3685227544C18A72C0251DA1488392DFD5C9F983771FDE3E7867C15AFEAF64CF07EB3B840169E8C7B4F5ED0113B4FAEADF2C2D6840D9FD8E3CCCB34499D955DD98FF88905EDA565A9613DF124962A7549C9E533235508267FFC782D85C464D626526D6593ECC299178993E749FA87219E2F40AC3408659922568D3C2D68B9332F588CC50DCF434568CA3E67D2B947FEE4597F2CFBD74F5C0878206543C8BD3246A8E3C5B1E8A5CBD8502EBFEF741994326F68A47128276819F1D8ADCA9B22B0CA52AED24AE9BC807E536F1FA243C2512809A27BE9F2BCA3C828F8652631D84293C92E2434AE46D431C63417C4E7BB11A573FA8F65C6EE48BC95876B2BD7A776CA9F327A04AE7BE417AB33E4A47DC35481B863904A3AF532B0A55AA0A20A132D6A9B71847E95405A316B7E135A2ECE6B0A563A850CF5F3F1F4BD81C0FA092F12E181FA32D496360B98582A3EA7BA064A2E4AC7EB3B3106ACB29905569667C92DEBF988C31E4B27F3A5860446F1F96147365DC3DE9B78CAC28119DE072D13C948D61CDA06B9E24B15D9710FFE16A5227E76C61D9A08BEE0A255360E74D0DBB459937CFEB4C947404B564A53165A3471D1FB2E2105E32EE828C632FC91BDDC3BFC142FB123BF6C1BA3CF2CA86CBC71A9984D901B297324B4B7C5D9482CE06556D5D2C903418907A8B31ABF1BC61B89A963EAAC04A37DA9E1D4C1D010FF1438C044BD330788F888B29190A294917D9D3C182F5F32EAC2C5D4A30DDE42054C93051049AC7A7BB748BE14EC6C0D3BB4F00C3C2BDCF3DEAF2B31048BA7AB1FCD78E64F1E7C2C24251FBB1C8CCEC83BA459997193B11A5394F403A64ABC8E71605DE9727826203529B7EC7797EE6589883BB0BAB8EB59010E88C8ED8DBF52786711B869BC90A7E8B328F65ED44140917A3E1D571487AC59835389831BC348E7B77C0744F6F3DDB1F4663546B3875D83648BE6303BF2E9362B229DB527427DAD7398C09663DB3BCFD8C5639053A46955F0A3F604ABF5AB56E8CE93E512D5A43D3ED3EF29BA362BC0C958DFAC93E1F8B66CD31BD206074293BA4C18A326FA2D5F3433162B5E3D55FD92796A356579E7E193506E6245AEE58A6B1766A312F50168C71B63084A7BE2B7A8181DB4A8646E8F34211B33DE095F914591794C4687A9CD85D78B3B05907948D64C3198EA4844085B295EFA5A0EA7411D2C6E71859C5E927AE6165F40EB92294AB36956E7163C30A4201388557FA667D8C65683D6F6C8EC894E659509BD6DB8A31AB336BC5803826362DDD0B06DCF7CF179492759DB0EA0A03F7E83C1D1842CD5CCB5ADAF2E5673D6A2599AB62B5026703F6CF5DB072483986D8B9384761D9A6B4738FB498A12AB3AC9F4870778B31ABB1FD8261F795616EA4250CB1F034A118FDA6521D459A71EA346B5D098946CED5A5F00FA3551C27432F18E89B1B4950AA167540291BF7D805B43058034D286BD9BE21199A5179083B580B89164C4378E944D4CDAA46713B71A8CDA9180794AE79613D95588B3673D1BD3495A937447CD6BB8CA6D5897A1B8A90591FD0DF2EFA605444D4A044BC611A61B176C9DAE492D7080E2F5832190DF649D85B60412AFFA3CB85150A2DD1BF14CFAA3A6E414998A7DD75E4AFCBA354C7A3E5A109067B963FE810AB9C20A7C248441AE703AB9413492F536928D2B38DCF2EE0CB614D645B50D5300BD20155246C6A38363462F7B5F9AA34619AA66022B1636834B28E06E48476EA801EEA4D2023B219F7409C8F3A15F59928122046DAF6F69A340D4B9A9D61E1CFD6209BD17AA0C854FEE180535D0E2B8A9F1F8A0BB8DB41255187B1B9C1E4AACC8E897D6BECB33728A818785228C7D4447750722D5887CBFD5D2EC61B9F5CBFEBE374283AC04E67491359BB1589A9E84234D70AE9889113742BA4AFCB83D35F87CBBB23E874794956007D9BBC415924A1DB5A23A264748B64ABC750060B0CF54DDBBA3CEAD27618B5D6E50D3CCE8825B267FD6F5CC79469388487B6E49290B082D3E092488F21CEDC67729049E2EAF8810B8E4CD2C127AFB3FDEC82CB7A006D01320714AC797E756CA1915343B65720389BBB0DE46BCC695E97E682010ED1AA450985464F0173B66E21187C5E76FFF20B97DE016467BC0D7C231395D1733C3B20214D98DA4B7285813D992702E38EC8730E6B52AE28F366699C8F32B3527D41F158F5E050CCCC5FDE62849D9F777907D28DEE587EE2BBB70E754806D6691D2509CBA561686A4581E7486DA090D83C20B15C9F25E08A306776EF05C1599CB5741C586B33643CDE6D2E95FFECBBC5642C1FE13E56B546D1474AD1F566BC349CFC8AA0AE93AD2CF815635EA5B949751A1AC45BD742F9D9591B258A6752D2CD850645F8D648A6B0382799C921103C377E37105C5CBA0D2413054052A29DBB24B3BE086B6FE86B984A0A20D523B01BD7136A672138BC648048987C9C2F9185A74A73D2B0EC11ADCD238B714599D99DF3FC50841D13EB2F1BD55F5E31E02E894421492B0BFBC13A9DCE150B0EE68E8CA6C350FAF33156148F5527EC7C168FCF93A928EA548C0351C636C082714094A53B17EF509C4C2EBBDEC4B25B24D8B0DD7B734EC642C354FBEE0F8AE232F9946D27B07F36CC54A7734599EA74AE28784D4C14FAC0A04D483D10E78E9B7A88DED992A2F018A39D120E48535F64811FD91D47200C5DF6A36006C3051A4A1D2E80918474FB8A96F5F1CAAE9DA2F920AC51C9C55F35943BDEDBD45295E350EE78A25923272E182E1FC0519AFA4E6E43C0DCA84D840398FF59930D76428632EC38C71A0FFA75F9AEB887D6DFFEE3D98F45EB907620ECB08C1EFD7A80F5AF7FFC0F7FF143FECDAF3EBE79FFFDAF7EF6E1E38B573F7FF1C59BF7EF5FDF7CC704F7BD17DFFCE49B4FBEE7297CEFFA9BAFEBDFF7E167EFDFFF4256FBA9A1EFF38F96DFFDBD9FBFF8E9825B633FD9F8222DEC6C552ED0BC1EB5D55EBCE27FFCE89DFCF3DD5BF9B63CFD279F4EC994E21DD970FD74513F5C1E7C361747C9069FE8FAD9A87E363EC48D42D71D5336D7CF3AF5B3EEC1679D2B99CDB5E8A2F25977FBD987CFEB7C16BBC81577FDAC5571EDC3BFF93A6CE0DECF5A63A3B17CB8E837B2B39E9405ECF00FB7A6046B0BBFEDFBFFEE5033E336BAB4FDD91FDE1CB8760C7FF5BC6927C6382A493F72F2938727473DB36C84758E2DFFE4E10B8939FAE46E1FAA50D68F2E65ED7D52B879849CF4E3C7DF7FF859CFFFC7522D375E17E017AC2EC0DF7FF0FC144C6D22BA3948AC7DF4AB931E1E62F5E285CEE7C3C3CFDF8CBA5A3F1F48BFF6FCFD87CFEF7276526A75FDB877EBABAFFF1C1E657EFAC236C3CDD3BBA8FFF5FCFDF548FEE0CB0FFFE0FDBBFEC1FCF450D55FE1371436445EFD3D47E6A178537E8FEFD04371A4FD5E488AF8D07ECF3EFC3DE539F8835B97A8FD9E2B8AA87D88CBD2C8EF592F3F3CBB9FDEBBF5B7D64DFAC9370FF5D4AF84A9AF57DE1B5685A25065D7BF5E6C03F9F62DEA2736E68DB8D03F4B0F3ECB7FF48D8808974FC9BFD68FD143C0A515F37A419C8617DCA79FFC950F79D23EE43F816B4F67794B3EB9503B3FB906E6AF77A9689F74E501A678A3373750FD547CF878E5DE0FAD39AAEBE7D4BD7386EDA75FFC7FA811845D, 0x83160200789CED7D5D735CC5B5F65F39A573472157AFFE6E5DBCF7EF3D776F512A210F58157DB8A4313914451584986FC7388118838943020787130CC4E6D8D818FF98A3194957E72FBC6BF59E19CFECDD2D4DCF6E293D521CEC4833FBA39FD5DD6B3DEBA3BB5F5D78616D7D7D796DF3C5AD85A5FFF7EAC2C6D666F7C2C2D202679C2D32B3F0ECC2EAD6E6CEA58D8BDDB5ADCDE5958DAD4B9BDD8525768EBDF6FCB30B2B172FAEAF75B60777BFBAF05267F37C671BEFDEFBE8BEBF73E3E2CAE62BCB2BE7CF6F7776769637562E2EE30F97D6BB74F18B5BDB1B2BDD6EE7FCF07BBC0F6F5AEFBCDC591FFCB8D25DEB5E3ADF19FCB6B5F9D2E8D7D71A4FAF2EBAB0B5D14978DFC1F507BD0FAE3FF3CC33D0BBF3E52C2FEF76D63B172F6C6D7696372F6DBCE0B1E3D56BE75757B6CF2FAF6FADAE90D8F0C3DD07EFF57FBCDF7BF09B8347BFEDFFF89FBD0F1ED61A1B684CF514FC4200C3FF81B38671B04C098BDF6EAE6C50637ADFDEE83DB9DEFBF033FCA8D61010061FF60CDE6B872F0B3777E5257C9210086B67630725B6BA458D5F798144814362750507C8F0D3B59DEEF25AB7B3E1FB7C67F43D4A75B51A1AF06C75C34E17B1EF74D756AB8BBA5BDD95F565FFCDE042F6DAB3C7F631D0E8AC01B51A9856467165C1BA05BA6220C3FD4F7FB3FFE91BBD5FEEECDFBBBF400F4C43CC4B40CC238899D586B171B4BB0FBEED7DF678EFEB47BDF76ECF8016E431F5B0097FAC64F873CB627DEC98D24E2935D1C7BB0FBF3C78FDD3833FFCF7FE171FCC82FA44FB186257C7105BC610B110CD7EEE3D7AB87FE7970AF7FE6FBED87DF4B87FF3DD831B1FF62EFFBD77F5C10C9210BAA52878F8631D1185887CAE02C2A0096E3944877BEFAD7FF47FFCA6F7DEDFFAEFBE7FEA06C150B519A1CD84007EFEE4E0F2FDDECD5BFB3FBC350368258E0735C43E8F0C046EE2C069B6AB71CCFDF73FEB7D3C0BDA63021BF93832B68F9AE7DA360778FFFB6B7B1FDDCA38CF9D9A561491090D91CFA58A0823D2F13169A0651B1743EFCBAFF79FFCA9FFC907FD7FDC3DF8EBADDE1FDE682900C94E76BA47EC1F1741FCCD21FFF84AEFF3F7761FDE3EF8F4ADBD4757F7EF3D6889BFAD9ECFF2B13E4ADBD911B2319555829E96874FE270CB65092D8F0D38DFE4B101F7D177BB8F3EE9DDF9BC78CA1C5144871148ECA10932D5FBE91EE9D92FBFDE7DF26639CC214A1152F002D849A81F3CDAFBE3F5DE1B5FF51ECEE20D150BD5772CFA427AC22FD87B7C6DEFD1CDBD8F7EDCFF6E96815C2C25042ED8847D3CF8C31BFBF7BFEBDDF94BFF9DFBFD9B577AEF7D4103FAC99BF4C9CD2B330BC09560255C7C260BE5F007189784D0ACF7FAADFEE75FCCC20F8FA9C3631C2A3610628899FF67623A5FBE4DF4F0C7370EDEBEDABFFE676404F843EFCADDDEF76F1DBC7D65EFAF97FB9F7FB5FF976F4FD5D8176862FD3F714FF0F2E5DEEB3FEFBDF9EDDEC327B340676DC147483044A21D32C632E2912E0B4A323E61B16F7EB377EBAB63A5C236C9B9E751CA9FA2D51129E75A4E76F6CC58455B8D1699CAB10E8F7D1E727709AC6442E0C03679C01E13C987C870E5D1D910F3715C169CDCB4C419999611073E320BE090202DE39007E93179AD31673EAAC66216993BD1F45D4B439B289B433C09678DB3D2E6810B6DA76BD4E34B51594768265D06D44485151DDFD171CC19E7C82DF358583859CF3F2685305C9CAF35D77F6698C7154C4EA3CF313D8CBD2A24CBA79EA088D460D4EC68279524CBC3F14FA60E3E61A7203953068CCC8FC8A4A48A303F108B76700B16B400E0F82F833CFCB860CCE3D9DF4CE3B9F0F00638833DCB99514832F250E59345EC12E7AF32540BE3F24DE193851B6119D1D4A60321B50273CA073322B54C72C6504B71060289551EFFD69680D81EDEBB3C13632E1C2B47CB2B25E4F172DB628DE45AF324194059A491C202FEA259A6B054DB00469A20D232483E1BAB14E3D6E653CCC70438550EF1A9CB35139C399367441F93BBDB3EC0EAE15A64CD19FDA262C152CF0A65A4E25C0B893318324DDF938D662454D278C400E8177185D44A38E74E39E2C1FC251EC91C79BF3A4F08A7EC7A995124320FB13A26B06951E6437C40EB84346563CDD6B186A15764B9C1316D6516C45317504522C499041171140CB3820377AEE257278B380BB4A8D8A2E49994B412C67290E284FBF89F83781899B49087729C6C7C3D416179036C1D489086531C47E521946D01A7A58A5233F6B5E2DD39E9D6686FC746B1C05ECD97242A1C2C80E2C89CA5167388369962709CAAF9C863C16029FB97C7C8160CB24A1C58C570FC166E72D23470B43ECC0147D2A818383E8F4338413B0D48A322FE2434DA585D86853DE6E12CA542C75E708913589E7A6BEB34874C19CE634219EDC0C47E1506C730389127DD576C97FA210C9C5B2610B6E1D9F224C54E5B40C56419EA6596C7FE1C53AD4D7B0FA00A292333360C14E41BCB27BB78332A9EC3D622644B8F9C6C2155FA0A2A0AC0492632D56497DAB155C99811D28036942D10992270C5221E45CF39C3FFA4912C537142E1DA8AF9B9CB9C138C15ADAD72CD60720E7C4546C96073CD60867F2432C82A0136878863EBC70E490719F40D10761EE7AFF07D132443D79E0191E7B2676F3605ADB4D24CC05C0EE684EE1D0632A8C4D56A0DC2E6F1ED8B053CF0017DC61E9CCF179C720D3D2C1FB31CC98675D9AA21CB261C7E258240A364205B22BBF02E46E717FD7C9169AD54D96069FB12A6D95CAAE7E48E15DE59005FA45EF4DCCD3692C7CBF2CF0462C6295AA728637F16E0726E8413C2E9D3EE1E8D32BB127038B34CB581270B37EA11C723ECC02464CA1215DEB51462775C176D7373512A263479BC8513C86C3A4A68830359192354A622F5C2BDA241025FE44AE04F0D377580B61DCE83924099BD0CB2D8213D2CCC07298D144AF05CEB2E4E360A1B8DDEC5DD046D8139C333ADBC28B6830733188906071079147411ABEFE3340334E71C58267FB7F02D15413027AD942E5BC95CB1BD5BE53F81362875E8E36B26F2846F8A053C98BB8CD2F99A96D4C84C7E42B1882B7EC5D12B621209A5AF612802719ADD49DE3D83196199D4B20CB0A9BA2915ACAF61C8B462B9D8A13C2C5477922910DCE6DA99AB68BC0A4955D98338938E02C51C114896710798C2A72D58C59D9468774B469BA77F85B382736DE87C20634E39CD18CEDB538F1247AFD6863307E40CD536AC2E0D71FB695B4D5ACB9942B26C0C13E8E8978C389BCD452A652CD7790A424BF7FFFCE230996DE3CFB2D172660387D2CC07D8644F57D176C40C55950FC1168138B61F64165D25681D51EEFD7D8AD5CE035DA59CE68AF1E269462673A42D282DC0B0B3638E7CFC469A79B446C958B9140A90647167D43C4623A3FA2D5E30887D9B2BA55DBCF155D291669EC79E4DCF225826AC382B911B6649519D7E9C67C5D31DEE7C22E65615A74FD8D1CE8145A04D254CA9CAD869055A0A633813A73CE25A21A66D8B32AE462F1C2D47BB6384356761EAD2B6458CCDE3204EE7505C092925705376794226FF167D796DAD60C5BBB7D9C271B4664A00949DDDCC96C955741A1C5A21C5A09008D5713BF312FD9F8CEB770BEF60F46C65BEB59D85CF5D5F97A033D53F163B8EAB427C4BBB0C28511D09308F88D32D2FAD971A6CF539971EE00CB6C8C2E9AFB3F19B4870B4B906685DA7A5ED7A4F3D621F48975C186EBCE63A13C16544CBFD71172583CD667985D1B40CEEB457DB0CD01AAD1958976FCFAAC275B34055853E117057F4D44D2D7B8D3B0AFED46871262291CA28D0CCE9D2BDC04C39305A72AF2C3850452BE63C76572A4B26D76F6B24CE44C2C431C32D6D34904F379FEC5996E9CACA4AA395E499CE752C152C28CEF12F6D025A04D0D422A2348B3BD4522ED78ABF62FB756C5F1F34407974F2099F3E9B1CB90167B91422D399D12570A9E8F264668C618C6B86C8014E78EA669AA34988C14A708AB6E5A27C42A6D04DB103DAF7B121A082566533E21C738838DDE86A4D4B767365778BEDDF21A702C184E599621985A3A554369531E39F5CE7DD970D58D211E1CC496ECA3649B9F00A2A8104A3C55C72ABF4F1CC95D0994219C5AAE551E9B2704CE04016C29EB0F7F7CF201BCC38E05C0C777F29B98BDBF3C96109337534883CB1AAC2C1A2BBEBB3F7E8FA66DA4BA1585D35D8994B69846B9DA0197CCAB57395491016AC62D2394B398539449CCC9D05E35C675B85502CD8A19B00DC306EA8A20CE6311E3983B3C02DBAFA56F34C85BEC5E21DECCC859E8206CD94A3D3D1E71071EA98A64580C08058A52B9B7364B24A48AE2C7A485A4AEC655EB4DACACDB28A0EE6E4095102FA46163B992B8BF3F794F76E95CC77B4CD8271866A25CB56D299BAD85863859286A3EACAB554BB589D355C7183DEB09132D3FE4665A345C26114553903FA49F24CF42F73066D30F2CB33412A87A798521D7011788F2DFD3B70FB01A8840147B5106723C7305EF05C36EDC843A3051268E6FF68324FF3189E4D74952C8D6A6D042256C8B5E610706A173B29B4E6C671F4FE5DA6133F4B565CDE0CE3C8A67260C973ED465E3262078A767EA59CBF013A6AAD08C4C75BD221357A0ED232A7253A1265F4F1711B27740F0D9DA03E977013D596E75AE80A93F232A73E25ECD5162A69A21E422B043E8F6987E420FCD3D31233D54117ABA54727614AC968A76E0699D68716DBC35514DEE03436C085DFF3E994ABAD81934847185107673A7DAC58333C3CD9879676675AB4516CD78E569759430EB182C223F0D9E23BB4319D955C9A799CBB33302C85FC4A304B9577454FDF7C1698395AA842C9A5D38D776C2F6B90566BC1339DCC55BC82467BA499D4F358459AD6BFE8050BC1E633B5923C73A94098C886CDB4236CE1704787C8CFE3304EF6048196D3A1AFEF903F976D7AB3A929666897D432DCC0631ECB823846B6B3C70A07CB01A8639168E84CA70B14EEE423C9100A001DDFB9CC2025D625D1BEE4DEDFA5D2B34CFBF3150D9849C79DA1EC9192622EA770EACA3AED2820893E8296CC9EF6E2C2E11103B4BB265A5F505295A1B58ED531E2CAA1AF6FD02A0901504875C3B18E6A893D5C45DBB535994E352ABA8BE9E06DF48F14A34AFF5CC7AF154B2A07A149CA8A3A708A29C3E7711AA707EC34B87C3B6D163BA0871B8BE258367066520BB48F9B34F64CF8FBC8B18446CA7116C00E4F5657761E7554FAB435F294DBDBB1E2672944A65DBC8B453B50C59C36AB9330AF5B5EA5AE991C6D5A4EC17559B41F9875C5065229264B5FB191CD0FA4AD62A5025346D4F9982357DC2AEFFD95BD9E2ECB68B640AB61156968FFE784B3F927BD4DC32032C9D03950E2D4AF7936B4369476FC2A63D61E37CDE0C2A14EF61B7A9FF6AA94412043724595B04E305065E8AA632DEF967416B5A3FC188393D755FF9478A4E04A329AC2E8159DFA08ECF0546A5F880374966FD1743257AC0A8C63DC725AD83E9F9E6F7A7D9901848A6E12D510CE21E0C4598C60399A275A9B725616C44A728D142B7C53B74C0BDA2D6D7566A5B1E838B8B2330AF922759C8E5344158D5CEB4CB8C0602D3AC1C69C72B0C34C37A7C38B8DD5AAF09DECB2790F16E8AC416D802B75CAFDA551BD8AA235FCF35AAF32431CDA02174AD8B9F40E6750588ACE4EC975A455E184038454409B339EF2B93B5CAEA0A4F45B9A17ED2F64AA5F30F88F63A024E7A507DC33A51894D35C69E664B54D52C988B3520E62D11C4CD97D9CC90053924133C12C005AA4A2E7719E80E5B06214E6B66234594FD3422BF4FCB5507A1E8774FACE5F5A31A9B89C4BB433641CD0451282D3598B678276300E92532D47214BCB8E7B0583605AEAB3B020141C9DEACC8860CD69D94A22606668B37EC6ADD3A89ACFC09A5072F229C1CF65C683528B26D1A49C29B3021C141855F41AA43C88896DA0E2E2520956BAA3942F8AC5FC6267EEE633219C5ED9CEE900283197519D74C281FE20B22BCE9061153D9C732DFF05814EFF3CEF8C949E2954CC72457358967D3E5FBE2E96B4D9D759C88E0EEBDCDDE9AFC81AD4C95A0712A441278941D1A3399BC7EF9CE4DACD6369FF0C67B7499AB6C0D45C528DF4F88641EDAC907014525C78CC7AEA69F7968C3697C36F8D56868A65CBD90DF9B8C9A4065457E8128AA2DDFD3C75FDD8B1E02B659DA3C2869201E719D17EA35CCD04A554D0433AE541BB01DDF0552BAAFCDDAFF21DC1E898D465D78C66F313AA7372CA3E5B331BB5A21D273968A79C2BBB1A29EB2148737C06526AF8991B41A74E7021DD7C1E5497CE37A4D50C59D619C04AF5C0CA02528D796492E931D8AA67CF8283EF7B16D0272A6467FAE3E590CAD272D87948E9E78B4692975046DF1EF7B26E5A9CC1E673014EBAF1315ACC6D254EF249E5DC01385B36D64CA5A1169D3E4EC6960EA53BE5DC62B8CC1924FE2794D2C0E7D1AF4FE717CA31A7892B978C369F536FA1107671BCA793D1D9C6824C10A3138EE77124A7469935D7672C08E90F56601CBD5DDAB27C0E11A7AFBC21B70FA88F4F7D05CE68DB0D3A7E8E76C66573E903A65A60EE146D953ABF3BABA4B3673ACFF9F42FF61DBA0A4C49C5DC3CF6EC0CEB04A9C04A9B79049BDEB34833905A49476BF5CF80A61AABEA1696897934BF898869090A114A2B8BAFEACE177CA53D271908055ACEA3824E4F8E09A654D92B0433F98216AC413709F83C2AAB19FB751E6338C99CD91940BF888BB3B0BDC678E79E019F576097826452CCAD099AAD6F79D9079265C3EA8F44A18563F3A8A8921380C8942D778236203C1BBB0F72A1292756FA51289974D560D30534435A695934A5CA368101345556D1565EF308387D4511A7BD34A52A7C794D260750F9AD7041B133607D2BC0A49C8D36DCA0A276F3E800A6C77134ED86ABE6764BDC19E2AF128484F95CE53AC3BA22CD69F732838AEB8C042669B363344CCAC13C06E9663E23C5582BE6F2DCAA194A184058A6A5507496E21C229E218D424BD5F55C2AE8D4822B8B8CD21F2AA8181DCE3D8788D3BB5772BF2A5FCC63FF26A3052E2C70161BCEF8C9EAD6C6C6A5CDB5556CFED6E6F2C6D666F782071BC22A9F5DF017E01338E36C9189856717BABFDE5AFEF5CA2BCBE3EF5FBE486D60E7D8403AC30798671776367686BF72C907DF4FDC3BBC58555FAE6DBEB47CFED276D5BEEE1AB51FF440ECD8B6DA57D8C14F6F1B3C490CBBF529140E7528320D0A979350183B040A872814E5540C8A64C24E85C5D4B1A8442C26010BB82816164342265334A0B02614A1EB50741A145183A20E1B617C16285411D4800201288DC9628E822226B1D0EC987E88C5B108A3636894D1BA01C636C134BAC5A6758B9E408214EA10247A864EB110185DA8DCBA5B17814DA8CAA652ABEB28D650A50CED24BE423AA395D434228F1EC96EF2A92EFC5083CE3E6D4FC7D5540FD50DA9065BEA04BA204A1A36D5436BA34E445A4A393DAB8D85A91E5A9F96E187FA23F8D03889804C438AAEA6B6220F9516BD302E9C98AAA5D33D74EC08AE19E0871FCA816B7FCA24FB772BB80CF4169FB1B91C9C421F14C74046112867A5355C0B137A284EB69DADD53522265B9B9B9D559A9D3BFFD7F38857078F1B4E30A2116B9BDDCEF68BEB5BBF5E1ED30363946A6BE3E2CA6A77B3B3B333C97FF0ABB5EDD5F5CEF2D68BCB2F6EAF7536CFEF2CAF6F55E4A5625EC3DFB0D9BB0FDEEBFF781F5B8B4A69B5E3EF3FA7BD4CC72EEAFFE36EEFCAF573076F5EEBDDFFD3E4B5CCD4AFFDF146FF875BE7FA9F7FDBBC5691083A172F74363ADB357A8643B946B3486D2DBF7069F5579D6E8C6C8DD4507848E1AF630FA13E5A626C51E03F0BE9FAB34999C2633AA941D496459DB541D5FBE9929DA773882BFCC198FFFDF9D3A16A9A60E78DA135AEEBEB6DA6E62EBAD9DA2C423628D8E8B1A984ADE68D564B5E6BB694356660EA0DA7362F023FB4E5026C9407D8003F5331916BABA51191C68B7AE345DD16A87AE37DBB17411DDA7AF44FA344D9D8268B1111D93FB5E3D87CD594BDAB37BF66C95563A0FB862F823DB4F930647701F682C3B7297D98A6FDA239E2EB43BED67CE08DE653C317391CDAFCB8C3E5049FBEF14F0D7EB0F1A2D6F61ADFB1F5A6FB462F7279B8E455B4ED423B1718F6441CB7575EEEAC3FD5CDE8DE6F7707770D3D934546A31F1F4937775FB9485FF5EE7FB5FBF866FFFA57F8CD76A77B697B337C5367A7BBB65935E5E2FACA2A5D503DABFAECFCCA2B3B5E3D9DEF5CC4175FDAEE8C2E1B18351270AE4699457091468D4C68AD69D286DA166BD5F00593ADBAFAC1EECFD70E6B1567915655767864866B6DE3E9621BBE2B516C1C62627B780D4945FF93B7FA9FFC6ECA064E608A3733BD77ED22D7CD3690A71D9052D5EE1BDFF5EF7D33880E6D769188210B5BEBBE120D8219599B45433750916AC03BC747D2C48CE6E1A939BC5FC6BCDAE105E38AB336B92D3793FA65D42819D5C4DAE926F508F8C3BC6EEBC7FDDE0AEF20C456A3D6115534BADD1E819745E1B23058888697B80D84CAE29EEC61506B14BAE6F81E06586407CC4CB4770573CDD04D0071FDA200E29A23308E38F2FEA300CD0E38DAC360229434E8FE35E08AB1C9EBF5C2B991424BC11B8B7D0E2FE0F1382FB79129CCE2265D35A9A06A628E29ACA75DDCFBE9C9EE839BE7F0FF9A5D1C5120C7D7C571EE289B7013BAD84F9601DE0FAEEF3EFC7B0D68E4BDC70734DAAFC09A633910758D027D3A757FB8B5FFFED573FDBBDF37FBF5086B749270A70A971F8DF6E0ED2BBD876F25C23C6AC666C4295833C53113D0DE679FF5EE7F7E0EB554EFF2E562BB55D681CC887660717BD76EF7AEFEB558B46D95D308AE646CEFA31FF7BFFBBC5CA830D5389E823656EA09FF36D5D351E4F1E4E036BDE636607B0F1E940C762ACE7834D8DE0F3F10A1D87D70AFFF8FDF140B964F35658F467BF0F9EFD1DD3B77F0E8B7A8A88A45ABA732B353F4ED5B97F77F7FA377F5CB73BDCBF77A77DEDDFBEC56B198C7EB2A0E8DF61FC5180746A8FFC7EF777FFA22335C1EC53B16C46E7446106FD3C90DF0C6A97555FFFE1DECDF623B37AF62DEFBECC3DEADBABF570E58C88476FF97C7BD3B6F9CDBFFF0AB269B2A086D2E5D5539B6FDFB3FF46EDE2D176DC80E3DFFECC2CB9DED9D2A190BE7F8398ABF52C2F5A5CEE6EA2BCB8360E5306A8F57AFAFBC301EC5AF890A1BD559D95E5FEBEC34A3E8F239B04BCA2E71738E55D985C1C3C76E3EB24CA47AFD204E3B0A0856B785F2D43B5BDBD4FAF39DFF1864C1AB570E45436FD5CA0C9F3B59CB479F761B48CC22E8E7C0A09FB5C46188E4C841E46282518BDC3DC7D992344B12C28299A6D2654AB9D4E38AE3E2E141F15411BEE9C543E992E7402D493686E748F1E89878A47F1CCA462D091516CF34353B6D868D08C94553D232452AC03C0CBEC4C5D4521131A950F6871E0780B33A2C95698A8EA61D34B5D0ECB8706448380E12279423D58060989D5A36514D4389A7E7B858E26E498A24D94CD44E4D3B626A61DC71D9A8906C283F93229B0A8CB44B8C876513AEEA8A8E1B205133BE043C2C9B694AC0DACC261D120A773A71C488E7A8C40167D3F423262A15B128E039E69640A29A4992CA440D5B9A0A1E059AC68563822A5826096784C6A0B16D3F9DC422D8E748CF8C9BBB698413A8C56B336E6C50D1244DA61114FCCF8425132E0F8C511AD25B6C49F12521D32433564BD846262E241395A47CF522B764AEB9458BDD7E2AE1E33889181FF774664E3595266A21A7144A3D423D41F65848383251FB32DFC348CF4406E120D5D3F43821A29629366080E198519060B4C30C384881AD4D118AF223C62CB1F82C4A118AF4CC482E093E26E3E984322C9A6B2B9420F1752C492AD29B24B124748A493A84C4A054382C49243149869A83D29A3B69122C52582A41DA6B6663767A0C451B854BD3116D3E4F36454E31D570930EDEBEB6FFA72BFD9B0FFB7FFA6DD0520FB14DC825C8787DB9E18460F66EFCD27BF89FD52BE2AE92178F944B5C4F3D66E010E2CB690832D23069BA575BC3AC14BEA42C03F18520F3059DE8670FE0004EAAB0740251C7E88C925ED87A09DC98B0A71B3CC25A21356B3098BD8F6EF7DFB9DFFFF3DBFB77BE9F7E5E0509B014756D73F0CDA7FD77BF1FE5E8223AC7AB4EF40F9EFAC6B34F2FB4768AA617B9DA899A186D934C709AC2132B487E1B0CAF12EAC1AD1FF77FF965FFDED7FB4F3EECDF7CF7E0C687BDCB7FEF5D7D1037E48AC61219DF0C364B2D821A78F3B1A04464A659619421CB95408747050713D20AF2619166B4141162B432C2A4C46AA20AC82E7245E351A2231F51401051404228E98C55090AA89EFC9B904E9019F3243F2A88A69570C473BEAE7D49449871443856017342095F863CAD7086A5471301BE2025A66AFC24A93461CC2E95CA5F60E42FA824A920CF01EE68EB97963C870729B1499A4741146D84C2348D3C26A2519A9850D0B1C4F1C2DB3ADB3C1C0B4E0C790650CC2E143988A0A28C63D629327F9812CA189914260F19281E64C4224D2A41186D381F592274DFE598619B462A92313A52523559715C2AE3354313820952629DEA2A3481B4910B03627B0AFDD4486C26368504A02D02994285835328C881054B0C740660B49D43A8BC9133A6CE2163B9722241B184E75090FD429A5B1984D14A2AC287055157B934A9E068115AB1840844582AE1B8AF480A560561B432428EC2C8C246FDA5C80C625A0B81FE64824350AF0C9F104E90E9AA44031D00D34636608739CEC41183B688BBA4547678C404092E24666A032866178ACF3B0CBCD1245304426BDAFECBDAB6F1701124B8500FCD1C9D3EA9C3985D2ACA67DC682539F6593297D3C6B2D64209973D2469DC208A364385469E5C5212B555AA508C43F3DC360B29820437AD102488A2AD50C492421947820A4708A575A14390E0CE22941A8AF6C97CB0D11C5B5828745882B14E0899327F0E89698A20C95549CE7310CD34859451DD224981233B4C34D1A09934CCF1D6D3281CE84D0B290461B41A3292A6255221993464D006092328D3961068A957EC4E48274C7693984B104D9B2143C93BCAF546532861E1284A120070C5534A1D6A35AF13C20973DEC4911340D32A44A7C9DBE266060D2C9583941878783E05B92E4F8DD03551CC3E9D7C3D1F526795C275698353E6FF49B049C198A50812DD7AC865F7C1B7BDCF1EEF7DFDA8F7DEEDDEE5CBBDD77FDE7BF3DBBD874FE2EE4013D3EC93AACAE301458A212D883970954C4AD8FB10574906F9AF4A0A3904C1B40A5079274398C49083E7BF16B46E9BDE9741FECB13B57000455BAA57053012158D65DC9F5DD7562841FE9B2694208A366118AAF4A55AAA2599C46640E028C159245A57FACA2001A68CFFA4B679F8779C82BD27FF75F0FAEFA3C198009856C198AAAC4A2CF16487893202AD2D930CD25F3E4369780D452BD7DACB98A9C45904B403376746A5D44087A230324C7F5373B04D14ED73B0BE0E242D07AB9CA532199B92660C2ED29B10519803AB5932B113985A99241F28C5B929D2148D518CD3F6CF2901DFF06C0A92DF460DC81445D035186D6D92F145D08999582A2092C0DA664C6490FCCA24F21B44D146C5A0C34E1A2BD526796667B83109D18743995D38DE9B18C40B80699562F3E12F969C20C001230D806B3B8B5490EEF2D412DF268A5624067C4C5D25A6EE41718E7FD136B5964A90EF2A96348F82385AF11739C84DC5D62BC52355DA0A3A0BA5A55422ABDBEADCEE2802D384D18AC0F8D80E955F25C71B38483A3DA4A554C2250D49D63988A2556E8DF9B24FB7044904862A1A58A68A06150EF6A6A6D79A40DA567A18BF80362972377001A0F56A131564BB3C69060551B452B7CA9B34869D932C144E3FB4154A90DFA6A995208A5633C8F878BA48CC9980A1A2206B8C6EEB48AB20BFE54989D8208C56F307D594A3B89648B3CCC63A6B9575A66DA9A10A125C485DFAD884D1C60451B61BA8923951DBFA95C406648209AA6F0A31219B30C14D1A314130ADEA82BCAE421D9E48FEE93C42C9920A76C36B658304D7242A97008AF60587909ABAA76229099CB72E21D3916D1C66A936843CA9FBCA89C051A7925D21344342A9945AA9F04809AF614BF7846A2066CF1B49BF4D860FF68944BEA2A9D85DA7ACA90ECB241CCD4D637141186D524555218D4252984EF91973494BB4825209525B99B8683880A28D116255D0C625D7BB6B668C04DDD632EB481D439AF509A068657D2A5F5327B2384584C57083BE6D82A6AD6DC535219C20C54D0C2704C0B462B8CE4727D21748F855232C4534E1011326B8CD7CC83B4F761F7EBDFBE0DBDD47EFC72753134CABA082F5B96DB3C4D2CBC83447EAD2563619686E1045DBCA0E2A8C8E6EB274D88091C05A6B9820BF6D4CA2A3874B104AABA904833D09D42C530954DBE162C2ABD274EAFE4A4D186D150C10174A8B5702B34ED3C1BE6D171B9920CB958955EF01146D3344A8C1D1C0A556BD7300C953F60E0A0B255CB53B43D5420D455B2751D0E35862DA0C099DB4A85BDACE9F20CD4DDDBCA209A2ED40E14B5C25E6A02B9900B8B6F127138EDF262E0408A0683550BCDE56909875A615698C1BA6DB327F1389DF2619E6208C5689213B5C4E9C56AE20048E13AE746BCB6CC21B3224651183305A8D15EBAD994D2DE2104683D5D2425B826BC2E5B969CB1783385AAE3062B47FC7924C5B61C49400A518B45D766582D4367181440044AB09043EA1625265E270F6702699684D55C2A1DBE4B2D3268EB6FB0AD00E55C9DA964C906329FB6386F7AB0BD3DAC414620045AB6C59B50A3239F844D52C14964BA9E63EA49AC5864B7213170107C0B45F1A4D5B38A4A58798F20798CB947511A1696483E4569B19B617A8C168C9E448C82A39E94C751B529AB6150A361CC44D5B1A1D84D12AC462FC1E0EC498D342738EB6DF553A452CF5133D26A413E4B9493A2608A655C59CDFFE99C537808CCF23C379928E09CFA320CD4D63B94114AD9266DE1167267DBD3818C6054F599116144AB80C37B5562E00A365E889C2583AD175A6B59D96292B9336233E646DA70D97E3A6D54105E1B4CA291A7A1CD3893B23550689AB14C7283C66C28BD112EB7D0228DA58E96A4F400A5424D13A1489E5D61897A45E6A07474D0867BA78EEE1B63A00A66DE9295008344DF32AAA68E742483D43C26878E6DFC4B6C441EA9BE83906C0B48F72035F52690AD8288E4E35699B9644C685CB7213C30C0118AD548CF05B5DA42E84304A6900097286F2F6D1212913C20957E7A6EE95D444D3BED04521934E5B5D44074F80401DDC3660E782DC376DCD551046CB85680323971EDA352E6917B6B0508294373DDAD084D166AC7034FD96F6B04E532F1C1998C21F4CDBF27617DE6D2CC9A30EA268E33B563B1788E455D1A081838194C0545828E1B32612D3450114ADDC23E517E7EB44871A2C03C6A9443745E58656D4BB20D9D549862808A3AD7F445E45F2AA19AD9D1346A414B71F769C8D0BAF414B4A1C05D1B41A32D5210432D179AC7690C271D3FA988920D74D8C320440CC5E5C2806890514314BACFBD7DC18F41FF5BFB75F69052C4C746758B15883327B8121EAAB2A9CC393D71429F4AA1D176D8B96AB63C55BAE550CC268950E705E8BA31B9AB433356DFE2EA561B6F5710A2C4C71D3DCE9208E56814CE1B717A55DF653C3BB8066BAF56950C0C2F1DDD410661346FB9C34FA9E2C79FB659572785830EC022CBC9D6E5DABF43E7BBCFBE48BFDEFDEE8BD13DBF63E88A45584C15771A22F915645A70C58C368A3C74C661A5886B56841382D9D004A76B3441D83D271CA703AE527977482C437D19B0EA069251C4E3B75713AB37086BA77247AAD354DB864778633496A305A691ABFF04FF1E881508784EC84A17569794276C0DA573604D1B4AAD9F521407F305F5A118C758609CE535471FD70F549E10409B04EF20A82705A4D28E603EB383FD31C49C38CE41264D2812D87699BF0496BA9F3AA89A655605353645DC68FB98C08472B2DB5DF1DA8AD150F1FB5961CD26CE268B5CEC6F8F0B14ACC5B73908CA1A39DB6B83E72025DB88637A9CA2108A4655DCCC0D625EEAE8572B19C65914BF8BCB5B4336B82485A957FF833B37872DAC4E720A92EA6F5340A57F2263A93011875A93C8F2FEE6CEEAC75D75EEEF8E3CD97373BBFF6479C6FAFEDFC6A79BDF372679DECC45F7F7770E36FFFFBF3A7BB8F298BBC7AA1B3FAABE5EDCECEA5757CCA42F5C3F2F94E77656D7048FAB30B6BDDCEC6F2DA79FC1A5D63461C2F70D9D2AB0B6BE75757B6CF2F6FAC74572F60D32A54FB6F3EDEFFDDFDDED53FEE3FF990164E7FFC56FFEEEDDE5B1FEC7DFADBFD87FFB5FBE831390057EFEFFDF90D7C2E0963FCFEDD07BF6BDED6FBFA0FBD0FAFFCCFEB1F3EF34CEFC3CFFEE7F56B076FDEEEBDF356EF839F0E2E5F59786DD0DE815C47B7F72EDF3DF8E80E3A1CFD37BE1B5D9A5B363C2A9BCD4EF7D75BDBF4AACD97BA1796FDA3C907BAFCD5DEE36BFDEBFF7DF0F193DD070F81EF3EF8A67FF39DDD475FE124AB6319BFB8FFD7D7FB385E1221607FF4AE5EDBFBDDF78D968B237A954439EC2E7ACACDDB078FAE6137F4AE7C4C7B97BEFDB07A686D2AF5DF7DBF7FF321766FF4961AC4BD8FEEEE7F777FF7E1C3D1A5B981CA85DA1BEF7DDD7FFDABFD373EDABBFBE838DFAB6AEF1DBDE8E0F54FC90DBEF37DEFF1C7C7302475B463038DAE4F9ED104BD7C1725544D1E12CC8FEF375B3A4B03F9A1FA84541C3E6A750B95CAFADA4ED77FB1832AEDF95A3BFBDF5F3BB87EAFF7FAADFD7BF7F3352C3E99A30D0B7DE59F39F1FDC4017DFE869D2EDA859DEEDA6A755177ABBBB2BEECBF791A481EFB7068470656C25BC47FDE3DFCB07BAC1133BDE9F9A8335D9D831A327FA1834B5FAB8F95893353B38D95B8FA9C7A10E3ECDAFBE876EFCA17B9DB26DBB7ED22BF58CDAEDE4F777B57BFCFD73695416E0F7F4FDAFBF1B5DEF757B3CEFFB8E69CBA6DBD5FDEEF3D78275F934C065D79F7E3831B7FC9D724DBBE49E880E76B8FCBD01E9EAF3DFE4C93B65DF6CBDD83EBB72B9EB0408A6EBB73B1723106867CA29D0FBE2517049FDAA15D3148CDD6A2383E5BC7A9C4FEDCB0FBFCE336B70657A14768D1FC296E9D9314A273440CBA5B178155DABA7AE2D0DAD57437084619FA60E4C6003E51548B8B871ABCAEFA9F1DDAC8D07387BE5FE34BA655E4A516A4335A55475625BF949E1B7BA93F613D9C5C7A7AB07BEA1BF1A1B1F7F933BAA7B28AD3BE6C609D432FF38EFBD12183D437E263636FF42B458E5AA092FA3E7C68F47D2AF23EAA7F335C49C36679A18ABED06F611A2C0219DF3A35F585F8D4D80BFDA1DC91A4F0E844F0C8FBFCBDE1374601FA3D0F8EDA6A21151F3EB419DBF8575CE35F718D7FC535FE15D73881B8C62C8221E207877748B617B1FA8B0E8F86B412FC14619111938D873BC6B57F48F10F984EC89D0F3BFEFEC52F5C5AFD5567ECD5D5947D1A6DA8051F06970F5E825A77FFC9352ED187443D84BA77F0A6F1769228531F0A8BA6F7E5DFF23DCF2C0A36FD0361F2813CF040C1165DF489037A98DA48C716C11ED2CCB490CCACD342D4A6C591F195595F54D7BC47064B667D515DD51E19F998F545BAF6A2581863D6E79BBAC68AC424667DBEAD3D3F186098F5E1AEFEF050B46056A3C1EA921973FD87A6F4F9A12FBE837A70BD33854B8E9E8C3AD2398E927A0611B765CC9D8FDDCCB508FBAC53DDAC68BFAC996FE6B3DF8C171C1D4D88DE2C5CD8759EEA664EFB9BCED86C1BF1D9A77971ECDEB1608637B55B1B1B9736D7AAA4371180CE6AA5D65F5DD8594327A0DB00446B93BB173ACBEB2B480CC2D73C2DA291FF363C2AF4DF0E3EBF71F0E813329FE02AB38C7363E5E5CEF6CA4B9DD87348E37EFE76FFBBABFDBF7F11BC7FD490B14933B12FE1E4B19383D73C35A7EB5B483D069F36DA5E3FE674FCE2A854E2D72CD4DABEB9F6D285EEF20ACAFB6574843B3B684ACF3134D0B53E595DEBBE122360403BD84F30A8E58BD4D3EC9C3FD009EF5C182BEF18B7F320220EFAF0017E8D45C8B51F5EA06DD4F7377E87D6B1960EEFB12E1A159000B6C10647A1BAA790EB170D1FEDCB550680FDCAFAFE8DEFFAF7BEA9C33E0235832350FBF28F306AD0328C9AE9186ACE5D03336F62E611CCDCD4310F371318C71C79FBA87547210EB0FC21620911C410ED67D3002C9B805504307B0AB8AA001C15006604CC5414B08EE19531BC23111DBE7C861D0DB83E925B829C227037D99268F43080CFE734562FAC6CBF341EDE1B7EB2B2319C8CFEB1A3CF278B7717C6BE798A79F4D1C6CA7F0C9E406FDBC256AE74B7B647BEEBC6CADA66A55BC3A56E2BE7CF237DDB216A77FD41EF83EBCF3CF30CF4EE7C89DFF83B87B754F9FBD003F085AB1DDF75E78C57538DAD3D460A68EC52CEEB99F060EB26DAB03CF12A1A0FDBAB4813B75E5C7E717BADB3797E27ADB59AD75A3B3831FCE0CD6BD5541ABB96D591D5EB6EC7AF55046DE5E595B5F59517D6C77A994ADCC458170D0AEFAAC3917D2FBFB4B6D31D8CA90BF8E3D6368D7790BD9FEE015481C68551F8C007539B113C1006BBF09981B3F0F2DAC5117527DBDD30AA7E2A8C0672CCBA924D99B8921EB778489AC438130DEB1BDB547D10D07DA46FEB2F558B704868DFEF0B137EABB450D7A541FDA39AEF04B6C867499888D02B03459E019838EFC52CAFE45A37B550E0950194822DEA595E09DCC83AF78AD5B2D6DFF97F667A63E5F435C365AFFD7F39C104EA, 1, NULL, 1, '2020-08-28 10:25:21', '2020-08-28 10:27:11');

-- ----------------------------
-- Table structure for user_ocr_data
-- ----------------------------
DROP TABLE IF EXISTS `user_ocr_data`;
CREATE TABLE `user_ocr_data`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `user_id` int(0) NOT NULL COMMENT '用户ID',
  `status` int(0) NOT NULL COMMENT '-1-初始化，获取token未录人脸阶段;0-认证中(sdk回调操作成功)；1-认证成功(拿到报告个人信息)；2-作废，不再获取报告；3-失败（活体攻击等非本人冒名实名情况）',
  `fail_cause` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '失败原因',
  `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '手机号码',
  `token` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '获取OCR的token',
  `ocr_detail` longblob NULL COMMENT '实名认证报告',
  `ocr_face_indicators` longblob NULL COMMENT '欺诈报告',
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `phone`(`phone`) USING BTREE,
  INDEX `userid`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '人脸认证数据表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_risk_data
-- ----------------------------
DROP TABLE IF EXISTS `user_risk_data`;
CREATE TABLE `user_risk_data`  (
  `id` int(0) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `borrow_id` int(0) UNSIGNED NOT NULL COMMENT '借款订单ID',
  `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '手机号码',
  `score` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '模型分数',
  `rsp_msg` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '返回信息',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '模型描述信息',
  `order_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '订单号',
  `rsp_code` char(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '返回码,000000成功,010033 参数错误,010045  系统错误',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `status` tinyint(0) NOT NULL DEFAULT 0 COMMENT '风控建议(0,未知 1,通过,2人工审核,3拒绝)',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  `black` tinyint(0) NOT NULL DEFAULT 0 COMMENT '是否命中黑名单(0,未知 1,命中,2未命中)',
  `custom_type` tinyint(0) NOT NULL DEFAULT 0 COMMENT '是否老用户(0,未知 1,是,2新用户)',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_borrow_id`(`borrow_id`) USING BTREE,
  INDEX `phone`(`phone`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '排序风控数据表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_seal
-- ----------------------------
DROP TABLE IF EXISTS `user_seal`;
CREATE TABLE `user_seal`  (
  `id` int(0) UNSIGNED NOT NULL COMMENT '自增ID',
  `user_id` int(0) UNSIGNED NOT NULL COMMENT '用户id',
  `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '手机号码',
  `account_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'e签宝account_id',
  `seal_data` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'e签宝seal_data',
  `status` tinyint(0) NOT NULL DEFAULT 0 COMMENT '状态',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_phone`(`phone`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户协议表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_short_message
-- ----------------------------
DROP TABLE IF EXISTS `user_short_message`;
CREATE TABLE `user_short_message`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '序列号',
  `message_content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '短信内容',
  `message_date` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '短信时间',
  `phone` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '手机号',
  `user_id` int(0) NOT NULL COMMENT '用户ID',
  `creat_time` datetime(0) NULL DEFAULT NULL COMMENT '上传时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `id`(`id`) USING BTREE,
  INDEX `userId`(`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_unicode_ci COMMENT = '用户短信表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for white_user
-- ----------------------------
DROP TABLE IF EXISTS `white_user`;
CREATE TABLE `white_user`  (
  `id` int(0) UNSIGNED NOT NULL COMMENT '自增ID',
  `phone` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号码',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `uk_user_phone`(`phone`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '白名单用户表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
