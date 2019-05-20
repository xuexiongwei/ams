package com.xxw.springcloud.ams.model;

import java.io.Serializable;

/**
 * 项目基本信息
 * @author uisftech
 *
 */
public class Xmjbxx implements Serializable {

	private static final long serialVersionUID = -3638380273919111505L;
	
	private Long id;
	//许可证号
	private String prjSN;
	//建设单位
	private String prjUnit;
	//建设位置
	private String prjAdr;
	//工程名称
	private String prjName;
	//建设类型
	private String prjType;
	//联系人
	private String contacts;
	//联系方式
	private String contactInf;
	//附带临建批号
	private String prjTemSN;
	//特别告知事项
	private String specialNotifi;
	//发件日期
	private String noticeTime;
	//有效时间
	private String effectiveTime;
	//备注
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
	public String getPrjSN() {
		return prjSN;
	}
	public void setPrjSN(String prjSN) {
		this.prjSN = prjSN;
	}
	public String getPrjUnit() {
		return prjUnit;
	}
	public void setPrjUnit(String prjUnit) {
		this.prjUnit = prjUnit;
	}
	public String getPrjAdr() {
		return prjAdr;
	}
	public void setPrjAdr(String prjAdr) {
		this.prjAdr = prjAdr;
	}
	public String getPrjName() {
		return prjName;
	}
	public void setPrjName(String prjName) {
		this.prjName = prjName;
	}
	public String getPrjType() {
		return prjType;
	}
	public void setPrjType(String prjType) {
		this.prjType = prjType;
	}
	public String getContacts() {
		return contacts;
	}
	public void setContacts(String contacts) {
		this.contacts = contacts;
	}
	public String getContactInf() {
		return contactInf;
	}
	public void setContactInf(String contactInf) {
		this.contactInf = contactInf;
	}
	public String getPrjTemSN() {
		return prjTemSN;
	}
	public void setPrjTemSN(String prjTemSN) {
		this.prjTemSN = prjTemSN;
	}
	public String getSpecialNotifi() {
		return specialNotifi;
	}
	public void setSpecialNotifi(String specialNotifi) {
		this.specialNotifi = specialNotifi;
	}
	public String getNoticeTime() {
		return noticeTime;
	}
	public void setNoticeTime(String noticeTime) {
		this.noticeTime = noticeTime;
	}
	public String getEffectiveTime() {
		return effectiveTime;
	}
	public void setEffectiveTime(String effectiveTime) {
		this.effectiveTime = effectiveTime;
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