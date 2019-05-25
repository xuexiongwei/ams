package com.xxw.springcloud.ams.model;

import java.io.Serializable;

/**
 * 用户操作日志
 * 
 * @author uisftech
 *
 */
public class UserOperation implements Serializable {

	private static final long serialVersionUID = -3638380273919111505L;

	public final static String oa_c = "新增";
	public final static String oa_d = "删除";
	public final static String oa_u = "修改";

	private Long id;
	// 用户ID
	private String userID;
	// 用户名字
	private String userName;
	// 操作类型
	private String operAction;
	// 许可证号
	private String prjSN;

	// 用户创建
	private String userCreate;
	// 用户修改
	private String userModified;
	// 记录创建时间
	private String gmtCreate;
	// 记录修改时间
	private String gmtModified;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getOperAction() {
		return operAction;
	}

	public void setOperAction(String operAction) {
		this.operAction = operAction;
	}

	public String getPrjSN() {
		return prjSN;
	}

	public void setPrjSN(String prjSN) {
		this.prjSN = prjSN;
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
