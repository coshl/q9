package com.summer.pojo.dto;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 身份认证信息上传
 */
public class PersonInfoDto implements Serializable {

    private static final long serialVersionUID = 4852120072448990657L;
    @NotBlank(message = "身份证正面图片地址不能为空")
    private String idcardImgZ;
    private String idcardImgF;
    @NotBlank(message = "人脸头像地址不为空")
    private String headPortrait;
    @NotBlank(message = "姓名不能为空")
    private String realName;
    @NotBlank(message = "身份证号码不能为空")
    private String idCard;
    @NotBlank(message = "身份证地址不能为空")
    private String idCardAddress;
    private String idCardPeriod;
    /**
     * ocr订单号
     */

    private String ocrOrder;

    public String getIdcardImgZ() {
        return idcardImgZ;
    }

    public void setIdcardImgZ(String idcardImgZ) {
        this.idcardImgZ = idcardImgZ;
    }

    public String getIdcardImgF() {
        return idcardImgF;
    }

    public void setIdcardImgF(String idcardImgF) {
        this.idcardImgF = idcardImgF;
    }

    public String getHeadPortrait() {
        return headPortrait;
    }

    public void setHeadPortrait(String headPortrait) {
        this.headPortrait = headPortrait;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getIdCardAddress() {
        return idCardAddress;
    }

    public void setIdCardAddress(String idCardAddress) {
        this.idCardAddress = idCardAddress;
    }

    public String getIdCardPeriod() {
        return idCardPeriod;
    }

    public void setIdCardPeriod(String idCardPeriod) {
        this.idCardPeriod = idCardPeriod;
    }

    public String getOcrOrder() {
        return ocrOrder;
    }

    public void setOcrOrder(String ocrOrder) {
        this.ocrOrder = ocrOrder;
    }
}
