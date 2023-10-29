package com.summer.dao.entity;

import java.io.Serializable;
import java.util.Date;


/**
 * 首页信息
 */
public class InfoIndex implements Serializable {

    private static final long serialVersionUID = 7510444030131070135L;
    private Integer id;             //自增id
    private String productName;    //产品的名字
    private Integer loanAmount;        //借款金额
    private Integer expire;            //借款期限
    private Integer status;            //状态 （0,表示无效，1，有效）
    private Integer inquire;        //审查费
    private Integer borrowInterest;    // 借款利息
    private Integer accountManagement;    //账号管理费
    private Integer actualPay;            //到期应还
    private Date createTime;            //创建时间
    private Date updateTime;            //更新时间

    public InfoIndex() {
    }

    public InfoIndex(Integer id, String productName, Integer loanAmount, Integer expire, Integer status, Integer inquire, Integer borrowInterest, Integer accountManagement, Integer actualPay, Date createTime, Date updateTime) {
        this.id = id;
        this.productName = productName;
        this.loanAmount = loanAmount;
        this.expire = expire;
        this.status = status;
        this.inquire = inquire;
        this.borrowInterest = borrowInterest;
        this.accountManagement = accountManagement;
        this.actualPay = actualPay;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(Integer loanAmount) {
        this.loanAmount = loanAmount;
    }

    public Integer getExpire() {
        return expire;
    }

    public void setExpire(Integer expire) {
        this.expire = expire;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getInquire() {
        return inquire;
    }

    public void setInquire(Integer inquire) {
        this.inquire = inquire;
    }

    public Integer getBorrowInterest() {
        return borrowInterest;
    }

    public void setBorrowInterest(Integer borrowInterest) {
        this.borrowInterest = borrowInterest;
    }

    public Integer getAccountManagement() {
        return accountManagement;
    }

    public void setAccountManagement(Integer accountManagement) {
        this.accountManagement = accountManagement;
    }

    public Integer getActualPay() {
        return actualPay;
    }

    public void setActualPay(Integer actualPay) {
        this.actualPay = actualPay;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "InfoIndex{" +
                "id=" + id +
                ", productName='" + productName + '\'' +
                ", loanAmount=" + loanAmount +
                ", expire=" + expire +
                ", status=" + status +
                ", inquire=" + inquire +
                ", borrowInterest=" + borrowInterest +
                ", accountManagement=" + accountManagement +
                ", actualPay=" + actualPay +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
