package com.slljr.finance.common.utils.oss;

public class OSSResult {
    public OSSResult(String msg) {
        this.status = false;
        this.msg = msg;
    }

    public OSSResult(String fileKey, String fileType, Long fileSize, String fileUrl) {
        this.status = true;
        this.msg = "SUCCESS";
        this.fileKey = fileKey;
        this.fileType = fileType;
        this.fileSize = fileSize;
        this.fileUrl = fileUrl;
    }

    private boolean status;
    private String msg;
    private String fileKey;
    private String fileType;
    private Long fileSize;
    private String fileUrl;

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

    public String getFileKey() {
        return fileKey;
    }

    public void setFileKey(String fileKey) {
        this.fileKey = fileKey;
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

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }
}
