package com.slljr.finance.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonResult {
    private boolean status = true;
    private int code = 200;
    private Object data = null;
    private String msg = "SUCCESS";
    
    public JsonResult() {
		super();
	}
	public JsonResult(boolean status, int code, Object data, String msg) {
		super();
		this.status = status;
		this.code = code;
		this.data = data;
		this.msg = msg;
	}
	public void setData(Object data) {
        this.data = data;
    }
    public void setSuccess(){
        this.status = true;
        this.code = 200;
        this.msg = "SUCCESS";
    }
    public void setFail(){
        setFail(-1, "FAIL");
    }
    public void setFail(int code, String msg){
        this.status = false;
        this.code = code;
        this.msg = msg;
    }


    public boolean isStatus() {
        return status;
    }
    public void setStatus(boolean status) {
        this.status = status;
    }
    public Object getData() {
        return data;
    }
    public int getCode() {
        return code;
    }
    public void setCode(int code) {
        this.code = code;
    }
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        JsonResult result = new JsonResult();
        result.setFail();
        System.out.println(result.toString());
    }
}
