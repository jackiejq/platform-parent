package com.slljr.finance.common.utils.oss;

public class OSSForumResult {
    public OSSForumResult(String msg) {
        this.status = false;
        this.msg = msg;
    }

    public OSSForumResult(String title, String fileType, Long fileSize, String src) {
        this.status = true;
        this.msg = "SUCCESS";
        this.title = title;
        this.fileType = fileType;
        this.fileSize = fileSize;
        this.src = src;
    }

    private boolean status;
    private String msg;
    private String title;
    private String fileType;
    private Long fileSize;
    private String src;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String gettitle() {
        return title;
    }

    public void settitle(String title) {
        this.title = title;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getsrc() {
        return src;
    }

    public void setsrc(String src) {
        this.src = src;
    }
}
