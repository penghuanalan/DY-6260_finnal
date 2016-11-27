package com.dayuan.dy_6260chartscanner.entity;

import android.widget.CheckBox;

public class CheckNumber {
	private int id;
	private String checkNumber;
	private String sampleName;
	private String checkUnit;
	private String checkedUnit;
	private String checktime;
	private CheckBox check;
	public boolean isCheck;

	public CheckNumber() {
		super();
	}

	public CheckNumber(CheckBox check) {
		this.check = check;
	}

	public CheckNumber(int id, String checkNumber, String sampleName,
			String checkUnit, String checkedUnit, String checktime) {
		super();
		this.id = id;
		this.checkNumber = checkNumber;
		this.sampleName = sampleName;
		this.checkUnit = checkUnit;
		this.checkedUnit = checkedUnit;
		this.checktime = checktime;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCheckNumber() {
		return checkNumber;
	}

	public void setCheckNumber(String checkNumber) {
		this.checkNumber = checkNumber;
	}

	public String getSampleName() {
		return sampleName;
	}

	public void setSampleName(String sampleName) {
		this.sampleName = sampleName;
	}

	public String getCheckUnit() {
		return checkUnit;
	}

	public void setCheckUnit(String checkUnit) {
		this.checkUnit = checkUnit;
	}

	public String getCheckedUnit() {
		return checkedUnit;
	}

	public void setCheckedUnit(String checkedUnit) {
		this.checkedUnit = checkedUnit;
	}

	public String getChecktime() {
		return checktime;
	}

	public void setChecktime(String checktime) {
		this.checktime = checktime;
	}

}
