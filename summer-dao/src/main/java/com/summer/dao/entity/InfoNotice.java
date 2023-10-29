package com.summer.dao.entity;

import java.io.Serializable;

/**
 * app公告消息
 */
public class InfoNotice implements Serializable {

    private static final long serialVersionUID = -3407022019464823576L;
    private Integer id;
    private String noticeTitle;    //公告标题
    private String noticeContent;  //公告内容
    private String linkUrl;          //公告链接
    private Integer status;        //状态（0，不显示；1，显示；）
    private Integer noticeType;//公告类型(1,消息中心公告，2,APP首页轮播公告)

    public InfoNotice() {
    }

    public InfoNotice(String noticeTitle, String noticeContent, String linkUrl, Integer status, Integer noticeType) {
//		this.id = id;
        this.noticeTitle = noticeTitle;
        this.noticeContent = noticeContent;
        this.linkUrl = linkUrl;
        this.status = status;
        this.noticeType = noticeType;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNoticeTitle() {
        return noticeTitle;
    }

    public void setNoticeTitle(String noticeTitle) {
        this.noticeTitle = noticeTitle;
    }

    public String getNoticeContent() {
        return noticeContent;
    }

    public void setNoticeContent(String noticeContent) {
        this.noticeContent = noticeContent;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getNoticeType() {
        return noticeType;
    }

    public void setNoticeType(Integer noticeType) {
        this.noticeType = noticeType;
    }

    @Override
    public String toString() {
        return "InfoNotice{" +
                "id=" + id +
                ", noticeTitle='" + noticeTitle + '\'' +
                ", noticeContent='" + noticeContent + '\'' +
                ", linkUrl='" + linkUrl + '\'' +
                ", status='" + status + '\'' +
                ", noticeType=" + noticeType +
                '}';
    }
}
