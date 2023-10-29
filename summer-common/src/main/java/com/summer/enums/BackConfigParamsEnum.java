package com.summer.enums;

/**
 * back_config_params表的配置key
 */
public enum BackConfigParamsEnum {

    MOBILE_SWITCH("mobile_switch", "运营商开关:1-fuygs"),
    SCORE_GATEWAY("score_gateway", "风控模型分阀值"),
    OLD_PASS("old_pass", "老用户直接放款，0表示否，1表示是"),
    REVIEW_JS("REVIEW_JS", "机审通过人工审核控制:0-机审通过直接放款;1-机审通过后需要人工复审"),
    AUTO_SCORE_GATEWAY("auto_score_gateway", "机审通过自动放款条件所需分（需要REVIEW_JS为开启状态）"),
    APPID("appId", "商户区分标识"),
    OLD_CUSTOMER_CHECK("OLD_CUSTOMER_CHECK","老客是否需要过风控 1 - 需要 0 - 不需要");

    private String key;
    private String desc;

    BackConfigParamsEnum(String key, String desc) {
        this.key = key;
        this.desc = desc;
    }

    public String getKey() {
        return key;
    }

    public String getDesc() {
        return desc;
    }


}
