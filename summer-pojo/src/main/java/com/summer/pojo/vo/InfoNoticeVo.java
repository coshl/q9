package com.summer.pojo.vo;

/**
 * app公告消息
 */
public class InfoNoticeVo {

    private Integer id;
    private String noticeTitle;    //公告标题
    private String noticeContent;  //公告内容
    private String linkUrl;          //公告链接
    private Integer status;        //状态（0，不显示；1，显示；）
    private Integer noticeType;   //公告类型(1,消息中心公告，2,APP首页轮播公告，3，弹框公告)
    private String noticeTypeStr;

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
        //公告类型(1,消息中心公告，2,APP首页轮播公告，3，弹框公告)
        if (noticeType == 1) {
            this.noticeTypeStr = "消息中心公告";
        } else if (noticeType == 2) {
            this.noticeTypeStr = "APP首页轮播公告";
        } else if (noticeType == 3) {
            this.noticeTypeStr = "弹框公告";
        }
    }

    public String getNoticeTypeStr() {
        return noticeTypeStr;
    }
}
