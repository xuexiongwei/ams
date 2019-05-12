package com.xxw.springcloud.ams.model;

import java.io.Serializable;

/**
 * 工程图像 标点信息
 * 此对象只针对DXF
 * @author uisftech
 *
 */
public class BusDxfTips implements Serializable {

	private static final long serialVersionUID = -3638380273919111505L;
	
	private Long id;
	//标点所属DXF ID
	private Long dxfID;
	//经度
	private String longitude;
	//纬度
	private String latitude;
	//标点描述
	private String remark;
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
	public Long getDxfID() {
		return dxfID;
	}
	public void setDxfID(Long dxfID) {
		this.dxfID = dxfID;
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
