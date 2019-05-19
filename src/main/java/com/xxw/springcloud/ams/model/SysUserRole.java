package com.xxw.springcloud.ams.model;

import java.io.Serializable;

public class SysUserRole implements Serializable {

	private static final long serialVersionUID = 2258621026852858873L;

	private Long id;
	private Long userId;
	private Long roleId;
	private String userCreate;
	private String userModified;
	private String gmtCreate;
	private String gmtModified;
	public SysUserRole(Long id, Long userId, Long roleId, String userCreate, String userModified, String gmtCreate,
			String gmtModified) {
		super();
		this.id = id;
		this.userId = userId;
		this.roleId = roleId;
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
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getRoleId() {
		return roleId;
	}
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
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
}
