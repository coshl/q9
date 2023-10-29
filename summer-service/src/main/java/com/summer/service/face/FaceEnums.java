package com.summer.service.face;

public enum FaceEnums {


    SUCCESS200("认证通过", "200"),
    FAIL201("姓名和身份证不一致", "201"),
    FAIL202("查询不到身份信息", "202"),
    FAIL203("查询不到照片或照片不可用", "203"),
    FAIL204("人脸比对不一致", "204"),
    FAIL205("活体检测存在风险", "205"),
    FAIL206("业务策略限制", "206"),
    FAIL207("人脸与身份证人脸比对不一致", "207"),
    SUCCESS210("认证通过", "210");

    private final String massage;

    private final String subCode;

    FaceEnums(String massage, String subCode) {
        this.massage = massage;
        this.subCode = subCode;
    }

    public String getMassage() {
        return massage;
    }

    public String getSubCode() {
        return subCode;
    }

}
