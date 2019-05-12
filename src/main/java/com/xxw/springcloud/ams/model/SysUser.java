package com.xxw.springcloud.ams.model;

import java.io.Serializable;

public class SysUser implements Serializable {

	private static final long serialVersionUID = -3638380273919111505L;
	
	private Long id;
	
	private String userName;
	
	private String name;
	
	private String password;
	
	private Long deptId;
	
	private String email;
	
	private String mobile;
	
	private int status;
	
	private Long sex;
	
	private String birth;
	
	private String userCreate;
	
	private String userModified;
	
	private String gmtCreate;
	
	private String gmtModified;

	public SysUser(Long id, String userName, String name, String password, Long deptId, String email, String mobile,
			int status, Long sex, String birth, String userCreate, String userModified, String gmtCreate,
			String gmtModified) {
		super();
		this.id = id;
		this.userName = userName;
		this.name = name;
		this.password = password;
		this.deptId = deptId;
		this.email = email;
		this.mobile = mobile;
		this.status = status;
		this.sex = sex;
		this.birth = birth;
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Long getDeptId() {
		return deptId;
	}

	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Long getSex() {
		return sex;
	}

	public void setSex(Long sex) {
		this.sex = sex;
	}

	public String getBirth() {
		return birth;
	}

	public void setBirth(String birth) {
		this.birth = birth;
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
