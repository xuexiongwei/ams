package com.xxw.springcloud.ams.model;

import java.io.Serializable;

/**
 * 项目属性表
 * @author uisftech
 *
 */
public class Xmsx implements Serializable {

	private static final long serialVersionUID = -3638380273919111505L;
	
	private Long id;
	//许可证号
	private String prjSN;
	//建筑序号
	private Long serialNumber;
	//项目性质
	private String prjNature;
	//规划项目性质/人防工程情况
	private String prjAttr;
	//结构类型
	private String strucType;
	//平时用途
	private String peacetimeUses;
	//地上层数
	private Long aboveGroundLev;
	//地下层数
	private Long underGroundLev;
	//地上高度（米）
	private Double aboveGroundHet;
	//地下高度（米）
	private Double underGroundHet;
	//栋数
	private Long buildings;
	//住房套数
	private Long housingStockNum;
	//验线文号
	private String checkDocSN;
	//验线日期
	private String checkDocDate;
	//验收文号
	private String checkSN;
	//验收日期
	private String checkDate;
	//延期文号
	private String delaySN;
	//延长期
	private String delayCountDay;
	//撤（注）销证号
	private String cancelSN;
	//撤（注）销日期
	private String cancelDate;
	//补正证号
	private String correctionSN;
	//补正日期
	private String correctionDate;
	//影像判读结果
	private String imgJudgeRes;
	//代征用地情况
	private String exproprInfo;
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
	public Long getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(Long serialNumber) {
		this.serialNumber = serialNumber;
	}
	public String getPrjNature() {
		return prjNature;
	}
	public void setPrjNature(String prjNature) {
		this.prjNature = prjNature;
	}
	public String getPrjAttr() {
		return prjAttr;
	}
	public void setPrjAttr(String prjAttr) {
		this.prjAttr = prjAttr;
	}
	public String getStrucType() {
		return strucType;
	}
	public void setStrucType(String strucType) {
		this.strucType = strucType;
	}
	public String getPeacetimeUses() {
		return peacetimeUses;
	}
	public void setPeacetimeUses(String peacetimeUses) {
		this.peacetimeUses = peacetimeUses;
	}
	public Long getAboveGroundLev() {
		return aboveGroundLev;
	}
	public void setAboveGroundLev(Long aboveGroundLev) {
		this.aboveGroundLev = aboveGroundLev;
	}
	public Long getUnderGroundLev() {
		return underGroundLev;
	}
	public void setUnderGroundLev(Long underGroundLev) {
		this.underGroundLev = underGroundLev;
	}
	public Double getAboveGroundHet() {
		return aboveGroundHet;
	}
	public void setAboveGroundHet(Double aboveGroundHet) {
		this.aboveGroundHet = aboveGroundHet;
	}
	public Double getUnderGroundHet() {
		return underGroundHet;
	}
	public void setUnderGroundHet(Double underGroundHet) {
		this.underGroundHet = underGroundHet;
	}
	public Long getBuildings() {
		return buildings;
	}
	public void setBuildings(Long buildings) {
		this.buildings = buildings;
	}
	public Long getHousingStockNum() {
		return housingStockNum;
	}
	public void setHousingStockNum(Long housingStockNum) {
		this.housingStockNum = housingStockNum;
	}
	public String getCheckDocSN() {
		return checkDocSN;
	}
	public void setCheckDocSN(String checkDocSN) {
		this.checkDocSN = checkDocSN;
	}
	public String getCheckDocDate() {
		return checkDocDate;
	}
	public void setCheckDocDate(String checkDocDate) {
		this.checkDocDate = checkDocDate;
	}
	public String getCheckSN() {
		return checkSN;
	}
	public void setCheckSN(String checkSN) {
		this.checkSN = checkSN;
	}
	public String getCheckDate() {
		return checkDate;
	}
	public void setCheckDate(String checkDate) {
		this.checkDate = checkDate;
	}
	public String getDelaySN() {
		return delaySN;
	}
	public void setDelaySN(String delaySN) {
		this.delaySN = delaySN;
	}
	public String getDelayCountDay() {
		return delayCountDay;
	}
	public void setDelayCountDay(String delayCountDay) {
		this.delayCountDay = delayCountDay;
	}
	public String getCancelSN() {
		return cancelSN;
	}
	public void setCancelSN(String cancelSN) {
		this.cancelSN = cancelSN;
	}
	public String getCancelDate() {
		return cancelDate;
	}
	public void setCancelDate(String cancelDate) {
		this.cancelDate = cancelDate;
	}
	public String getCorrectionSN() {
		return correctionSN;
	}
	public void setCorrectionSN(String correctionSN) {
		this.correctionSN = correctionSN;
	}
	public String getCorrectionDate() {
		return correctionDate;
	}
	public void setCorrectionDate(String correctionDate) {
		this.correctionDate = correctionDate;
	}
	public String getImgJudgeRes() {
		return imgJudgeRes;
	}
	public void setImgJudgeRes(String imgJudgeRes) {
		this.imgJudgeRes = imgJudgeRes;
	}
	public String getExproprInfo() {
		return exproprInfo;
	}
	public void setExproprInfo(String exproprInfo) {
		this.exproprInfo = exproprInfo;
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
