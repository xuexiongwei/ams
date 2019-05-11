package com.xxw.springcloud.ams.model;

public class Header {
	
	public String reqUserId;
	
	public String reqSerialNumber;
	
	public String rspReturnCode;
	
	public String rspReturnMsg;

	public String getReqSerialNumber() {
		return reqSerialNumber;
	}

	public void setReqSerialNumber(String reqSerialNumber) {
		this.reqSerialNumber = reqSerialNumber;
	}

	public String getRspReturnCode() {
		return rspReturnCode;
	}

	public void setRspReturnCode(String rspReturnCode) {
		this.rspReturnCode = rspReturnCode;
	}

	public String getRspReturnMsg() {
		return rspReturnMsg;
	}

	public void setRspReturnMsg(String rspReturnMsg) {
		this.rspReturnMsg = rspReturnMsg;
	}

	public String getReqUserId() {
		return reqUserId;
	}

	public void setReqUserId(String reqUserId) {
		this.reqUserId = reqUserId;
	}
}
