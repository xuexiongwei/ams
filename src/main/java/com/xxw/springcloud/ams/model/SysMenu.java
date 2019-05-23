package com.xxw.springcloud.ams.model;

import java.io.Serializable;

public class SysMenu implements Serializable {

	private static final long serialVersionUID = 3106131106176793474L;

	private Long id;

	private String parentId;

	private String name;

	private String url;
	
	private String orderNum;

	private String userCreate;

	private String userModified;

	private String gmtCreate;

	private String gmtModified;
	
	private int isMark;
	public SysMenu(Long id, String parentId, String name, String url, String orderNum, String userCreate,
			String userModified, String gmtCreate, String gmtModified) {
		super();
		this.id = id;
		this.parentId = parentId;
		this.name = name;
		this.url = url;
		this.orderNum = orderNum;
		this.userCreate = userCreate;
		this.userModified = userModified;
		this.gmtCreate = gmtCreate;
		this.gmtModified = gmtModified;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	public String getUserCreate() {
		return userCreate;
	}

	public void setUserCreate(String userCreate) {
		this.userCreate = userCreate;
	}

	public String getUserModified() {
		return userModified;
	}

	public void setUserModified(String userModified) {
		this.userModified = userModified;
	}

	public String getGmtCreate() {
		return gmtCreate;
	}

	public void setGmtCreate(String gmtCreate) {
		this.gmtCreate = gmtCreate;
	}

	public String getGmtModified() {
		return gmtModified;
	}

	public void setGmtModified(String gmtModified) {
		this.gmtModified = gmtModified;
	}

	public int getIsMark() {
		return isMark;
	}

	public void setIsMark(int isMark) {
		this.isMark = isMark;
	}

}
