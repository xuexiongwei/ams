package com.xxw.springcloud.ams.model;

import java.io.Serializable;

import com.xxw.springcloud.ams.enums.DicEnum;

/**
 * 字典表
 * 此对象只针对DXF
 * @author uisftech
 *
 */
public class ClassifiDic implements Serializable {

	private static final long serialVersionUID = -3638380273919111505L;
	
	private String id;
	//父级ID
	private String parentID;
	//字典类型
	private DicEnum type;
	//字典值
	private String code;
	//字典名称
	private String name;
	//排序方式
	private Long sort;
	//其他描述信息，分级信息为  当前级数
	private String other;
	
	//用户创建
	private String userCreate;
	//用户修改
	private String userModified;
	//记录创建时间
	private String gmtCreate;
	//记录修改时间
	private String gmtModified;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getParentID() {
		return parentID;
	}
	public void setParentID(String parentID) {
		this.parentID = parentID;
	}

	public DicEnum getType() {
		return type;
	}
	public void setType(DicEnum type) {
		this.type = type;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getSort() {
		return sort;
	}
	public void setSort(Long sort) {
		this.sort = sort;
	}
	public String getOther() {
		return other;
	}
	public void setOther(String other) {
		this.other = other;
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
