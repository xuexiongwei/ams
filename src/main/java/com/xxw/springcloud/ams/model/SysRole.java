package com.xxw.springcloud.ams.model;

import java.io.Serializable;

public class SysRole implements Serializable {

	private static final long serialVersionUID = -4289559485950836165L;

	private Long id;

	private String roleName;

	private String roleSign;

	private String remark;

	private String userCreate;

	private String userModified;

	private String gmtCreate;

	private String gmtModified;

	public SysRole(Long id, String roleName, String roleSign, String remark, String userCreate, String userModified,
			String gmtCreate, String gmtModified) {
		super();
		this.id = id;
		this.roleName = roleName;
		this.roleSign = roleSign;
		this.remark = remark;
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

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleSign() {
		return roleSign;
	}

	public void setRoleSign(String roleSign) {
		this.roleSign = roleSign;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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
