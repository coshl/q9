package com.summer.pojo.dto;


/**
 * 用户关键数据加密解密
 */
public class BaseUserDecrypt {
    public String phone;
    public String idCard;
    public String firstContactName;
    public String firstContactPhone;
    public String secondContactPhone;
    public String secondContactName;
    public String realName;
    private String headPortrait;
    private String idcardImgZ;
    private String idcardImgF;
    //身份证地址
    private String idCardAddress;

    public String getPhone() {
       return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * 不加密
     * */
    public void setPhone1(String phone) {
        this.phone =phone;

    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        //解密处理
        this.idCard = idCard;
        //如果包含=就解密否则就加密
      /*  if (StringUtils.isNotBlank(idCard)){
            if (idCard.contains("=")){
                //解密手机号
                this.idCard =   AESDecrypt.decrypt(idCard);
            }else {
                this.idCard =   AESDecrypt.encrypt(idCard);
            }
        }*/
    }

    public String getFirstContactPhone() {
        return firstContactPhone;
    }

    public void setFirstContactPhone(String firstContactPhone) {
        this.firstContactPhone = firstContactPhone;
        //如果包含=就解密否则就加密
       /* if (StringUtils.isNotBlank(firstContactPhone)){
            if (firstContactPhone.contains("=")){
                //解密手机号
                this.firstContactPhone =   AESDecrypt.decrypt(firstContactPhone);
            }else {
                this.firstContactPhone =   AESDecrypt.encrypt(firstContactPhone);
            }
        }*/
    }

    public String getSecondContactPhone() {
        return secondContactPhone;
    }

    public void setSecondContactPhone(String secondContactPhone) {
        this.secondContactPhone = secondContactPhone;
        //如果包含=就解密否则就加密
       /* if (StringUtils.isNotBlank(secondContactPhone)){
            if (secondContactPhone.contains("=")){
                //解密手机号
                this.secondContactPhone =   AESDecrypt.decrypt(secondContactPhone);
            }else {
                this.secondContactPhone =   AESDecrypt.encrypt(secondContactPhone);
            }

        }*/
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getFirstContactName() {
        return firstContactName;
    }

    public void setFirstContactName(String firstContactName) {
        this.firstContactName = firstContactName;

        //如果包含=就解密否则就加密
      /*  if (StringUtils.isNotBlank(firstContactName)){
            if (firstContactName.contains("=")){
                //解密手机号
                this.firstContactName =   AESDecrypt.decrypt(firstContactName);
            }else {
                this.firstContactName =   AESDecrypt.encrypt(firstContactName);
            }

        }*/
    }

    public String getSecondContactName() {
        return secondContactName;
    }

    public void setSecondContactName(String secondContactName) {
        this.secondContactName = secondContactName;

        //如果包含=就解密否则就加密
       /* if (StringUtils.isNotBlank(secondContactName) ){
            if (secondContactName.contains("=")){
                //解密手机号
                this.secondContactName =   AESDecrypt.decrypt(secondContactName);
            }else {
                this.secondContactName =   AESDecrypt.encrypt(secondContactName);
            }

        }*/
    }

    public String getHeadPortrait() {
        return headPortrait;
    }

    public void setHeadPortrait(String headPortrait) {
        this.headPortrait = headPortrait;
        //如果包含=就解密否则就加密
       /* if (StringUtils.isNotBlank(headPortrait) && headPortrait.contains("=")){
            //解密手机号
            this.headPortrait =   AESDecrypt.decrypt(headPortrait);
        }else {
            this.headPortrait =   AESDecrypt.encrypt(headPortrait);
        }*/
    }

    public String getIdcardImgZ() {
        return idcardImgZ;
    }

    public void setIdcardImgZ(String idcardImgZ) {
        this.idcardImgZ = idcardImgZ;

        //如果包含=就解密否则就加密
      /*  if (StringUtils.isNotBlank(idcardImgZ) && idcardImgZ.contains("=")){
            //解密手机号
            this.idcardImgZ =   AESDecrypt.decrypt(idcardImgZ);
        }else {
            this.idcardImgZ =   AESDecrypt.encrypt(idcardImgZ);
        }*/
    }

    public String getIdcardImgF() {
        return idcardImgF;
    }

    public void setIdcardImgF(String idcardImgF) {
        this.idcardImgF = idcardImgF;

        //如果包含=就解密否则就加密
       /* if (StringUtils.isNotBlank(idcardImgF) && idcardImgF.contains("=")){
            //解密手机号
            this.idcardImgF =   AESDecrypt.decrypt(idcardImgF);
        }else {
            this.idcardImgF =   AESDecrypt.encrypt(idcardImgF);
        }*/
    }

    public String getIdCardAddress() {
        return idCardAddress;
    }

    public void setIdCardAddress(String idCardAddress) {
        this.idCardAddress = idCardAddress;
        //如果包含=就解密否则就加密
       /* if (StringUtils.isNotBlank(idCardAddress) && idCardAddress.contains("=")){
            //解密手机号
            this.idCardAddress =   AESDecrypt.decrypt(idCardAddress);
        }else {
            this.idCardAddress =   AESDecrypt.encrypt(idCardAddress);
        }*/
    }
}
