package com.dayuan.dy_6260chartscanner.entity;

import android.widget.CheckBox;

public class SelectItem {
    private int id;
	private String sampleName;
	private String sampleNumber;
	private String name;
	private String checkStandard;
	private String standardValue;
	private String demarcate;
	private String unit;
	private CheckBox check;
	public boolean isCheck;
	public boolean ischecked = false;

	public SelectItem(CheckBox check) {
		this.check = check;
	}
	public SelectItem() {
		super();
	}
	public SelectItem(int id, String sampleName, String sampleNumber,
			String name, String checkStandard, String standardValue,
			String demarcate, String unit) {
		super();
		this.id = id;
		this.sampleName = sampleName;
		this.sampleNumber = sampleNumber;
		this.name = name;
		this.checkStandard = checkStandard;
		this.standardValue = standardValue;
		this.demarcate = demarcate;
		this.unit = unit;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSampleName() {
		return sampleName;
	}
	public void setSampleName(String sampleName) {
		this.sampleName = sampleName;
	}
	public String getSampleNumber() {
		return sampleNumber;
	}
	public void setSampleNumber(String sampleNumber) {
		this.sampleNumber = sampleNumber;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCheckStandard() {
		return checkStandard;
	}
	public void setCheckStandard(String checkStandard) {
		this.checkStandard = checkStandard;
	}
	public String getStandardValue() {
		return standardValue;
	}
	public void setStandardValue(String standardValue) {
		this.standardValue = standardValue;
	}
	public String getDemarcate() {
		return demarcate;
	}
	public void setDemarcate(String demarcate) {
		this.demarcate = demarcate;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	
}
