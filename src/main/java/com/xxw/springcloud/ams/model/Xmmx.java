package com.xxw.springcloud.ams.model;

import java.io.Serializable;

/**
 * 项目明细表
 * 
 * @author uisftech
 *
 */
public class Xmmx implements Serializable {

	private static final long serialVersionUID = -3638380273919111505L;

	private Long id;
	// 许可证号
	private String prjSN;
	// 建筑序号
	private Long serialNumber;
	// 建筑功能
	private String serialFunct;
	// 地上建筑面积（平方米）
	private Double aboveGroundArea;
	// 地下建筑面积（平方米）
	private Double underGroundArea;
	// 混合建筑面积（平方米）
	private Double blendArea;
	// 地上建筑长度（米）
	private Double aboveGroundLen;
	// 分类代码
	private String prjClasfiCode;
	// 五级分类名称
	private String prjClasfiName1;
	private String prjClasfiName2;
	private String prjClasfiName3;
	private String prjClasfiName4;
	private String prjClasfiName5;

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

	public String getPrjSN() {
		return prjSN;
	}

	public void setPrjSN(String prjSN) {
		this.prjSN = prjSN;
	}

	public Long getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(Long serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getSerialFunct() {
		return serialFunct;
	}

	public void setSerialFunct(String serialFunct) {
		this.serialFunct = serialFunct;
	}

	public Double getAboveGroundArea() {
		return aboveGroundArea;
	}

	public void setAboveGroundArea(Double aboveGroundArea) {
		this.aboveGroundArea = aboveGroundArea;
	}

	public Double getUnderGroundArea() {
		return underGroundArea;
	}

	public void setUnderGroundArea(Double underGroundArea) {
		this.underGroundArea = underGroundArea;
	}

	public Double getBlendArea() {
		return blendArea;
	}

	public void setBlendArea(Double blendArea) {
		this.blendArea = blendArea;
	}

	public Double getAboveGroundLen() {
		return aboveGroundLen;
	}

	public void setAboveGroundLen(Double aboveGroundLen) {
		this.aboveGroundLen = aboveGroundLen;
	}

	public String getPrjClasfiCode() {
		return prjClasfiCode;
	}

	public void setPrjClasfiCode(String prjClasfiCode) {
		this.prjClasfiCode = prjClasfiCode;
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

	public String getPrjClasfiName1() {
		return prjClasfiName1;
	}

	public void setPrjClasfiName1(String prjClasfiName1) {
		this.prjClasfiName1 = prjClasfiName1;
	}

	public String getPrjClasfiName2() {
		return prjClasfiName2;
	}

	public void setPrjClasfiName2(String prjClasfiName2) {
		this.prjClasfiName2 = prjClasfiName2;
	}

	public String getPrjClasfiName3() {
		return prjClasfiName3;
	}

	public void setPrjClasfiName3(String prjClasfiName3) {
		this.prjClasfiName3 = prjClasfiName3;
	}

	public String getPrjClasfiName4() {
		return prjClasfiName4;
	}

	public void setPrjClasfiName4(String prjClasfiName4) {
		this.prjClasfiName4 = prjClasfiName4;
	}

	public String getPrjClasfiName5() {
		return prjClasfiName5;
	}

	public void setPrjClasfiName5(String prjClasfiName5) {
		this.prjClasfiName5 = prjClasfiName5;
	}
}
