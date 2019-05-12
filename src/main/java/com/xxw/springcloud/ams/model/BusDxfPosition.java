package com.xxw.springcloud.ams.model;

import java.io.Serializable;

/**
 * dxf图元位置信息表
 * 此对象只针对DXF
 * @author uisftech
 *
 */
public class BusDxfPosition implements Serializable {

	private static final long serialVersionUID = -3638380273919111505L;
	
	private Long id;
	//图元ID
	private Long constrID;
	//文档ID
	private Long fileID;
	//经度
	private String longitude;
	//纬度
	private String latitude;
	//用户创建
	private String userCreate;
	//用户修改
	private String userModified;
	//记录创建时间
	private String gmtCreate;
	//记录修改时间
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
	public Long getFileID() {
		return fileID;
	}
	public void setFileID(Long fileID) {
		this.fileID = fileID;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
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
