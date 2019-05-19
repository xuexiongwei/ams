package com.xxw.springcloud.ams.model;

import java.io.Serializable;

/**
 * 图元表 此对象只针对DXF
 * 
 * @author uisftech
 *
 */
public class DxfEntity implements Serializable {

	private static final long serialVersionUID = -3638380273919111505L;

	private Long id;
	// 图元ID
	private Long constrID;
	// 许可证号
	private String prjSN;
	// 提取文件名
	private String fileName;
	// 经纬度
	private String longlatV;
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

	public Long getConstrID() {
		return constrID;
	}

	public void setConstrID(Long constrID) {
		this.constrID = constrID;
	}

	public String getPrjSN() {
		return prjSN;
	}

	public void setPrjSN(String prjSN) {
		this.prjSN = prjSN;
	}

	public String getLonglatV() {
		return longlatV;
	}

	public void setLonglatV(String longlatV) {
		this.longlatV = longlatV;
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

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
