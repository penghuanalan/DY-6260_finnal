package com.dayuan.dy_6260chartscanner.entity;

public class FoodClass {
    int id;
	String sysCode;
	String stdCode;
	String sampleName;
	String shortCut;
	String checkLevel;
	String checkItemCodes;
	String checkItemValue;
	Boolean isLock;
	Boolean isReadOnly;
	String date;
	String remark;
	public FoodClass() {
		super();
	}
	
	
	public FoodClass(int id,String sysCode, String stdCode, String sampleName,
			String shortCut, String checkLevel, String checkItemCodes,
			String checkItemValue, Boolean isLock, Boolean isReadOnly,
			String date, String remark) {
		super();
		this.id=id;
		this.sysCode = sysCode;
		this.stdCode = stdCode;
		this.sampleName = sampleName;
		this.shortCut = shortCut;
		this.checkLevel = checkLevel;
		this.checkItemCodes = checkItemCodes;
		this.checkItemValue = checkItemValue;
		this.isLock = isLock;
		this.isReadOnly = isReadOnly;
		this.date = date;
		this.remark = remark;
	}

    
	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getSysCode() {
		return sysCode;
	}
	public void setSysCode(String sysCode) {
		this.sysCode = sysCode;
	}
	public String getStdCode() {
		return stdCode;
	}
	public void setStdCode(String stdCode) {
		this.stdCode = stdCode;
	}
	public String getSampleName() {
		return sampleName;
	}
	public void setSampleName(String sampleName) {
		this.sampleName = sampleName;
	}
	public String getShortCut() {
		return shortCut;
	}
	public void setShortCut(String shortCut) {
		this.shortCut = shortCut;
	}
	public String getCheckLevel() {
		return checkLevel;
	}
	public void setCheckLevel(String checkLevel) {
		this.checkLevel = checkLevel;
	}
	public String getCheckItemCodes() {
		return checkItemCodes;
	}
	public void setCheckItemCodes(String checkItemCodes) {
		this.checkItemCodes = checkItemCodes;
	}
	public String getCheckItemValue() {
		return checkItemValue;
	}
	public void setCheckItemValue(String checkItemValue) {
		this.checkItemValue = checkItemValue;
	}
	public Boolean getIsLock() {
		return isLock;
	}
	public void setIsLock(Boolean isLock) {
		this.isLock = isLock;
	}
	public Boolean getIsReadOnly() {
		return isReadOnly;
	}
	public void setIsReadOnly(Boolean isReadOnly) {
		this.isReadOnly = isReadOnly;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
}
