package com.xxw.springcloud.ams.model;

public class Header {
	
	public String reqUserId;
	
	public String reqSerialNumber;
	
	public String rspReturnCode;
	
	public String rspReturnMsg;
	
	public int pageSize;
	
	public int pageIndex;
	
	public int rspPageCount;

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

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public int getRspPageCount() {
		return rspPageCount;
	}

	public void setRspPageCount(int rspPageCount) {
		this.rspPageCount = rspPageCount;
	}
}
