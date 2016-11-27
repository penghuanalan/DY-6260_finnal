package com.dayuan.dy_6260chartscanner.entity;

import android.widget.CheckBox;

public class ReportData {
	int id;
    String checkNumber ;
    String projectName;
	String cpsdate ;
	String cptproperty ;
	String cpfrom ;
	String cpeditor ;
	String checkUnit ;
	String editTime ;
	String cpmemo ;
	String plandetail;
	int plandcount ;
	String udate ;
	String reportNumber;
	String result;
	private CheckBox check;
	public boolean isCheck;
	public ReportData(CheckBox check) {
		this.check = check;
	}
	public ReportData() {
		super();
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
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getCpsdate() {
		return cpsdate;
	}
	public void setCpsdate(String cpsdate) {
		this.cpsdate = cpsdate;
	}
	public String getCptproperty() {
		return cptproperty;
	}
	public void setCptproperty(String cptproperty) {
		this.cptproperty = cptproperty;
	}
	public String getCpfrom() {
		return cpfrom;
	}
	public void setCpfrom(String cpfrom) {
		this.cpfrom = cpfrom;
	}
	public String getCpeditor() {
		return cpeditor;
	}
	public void setCpeditor(String cpeditor) {
		this.cpeditor = cpeditor;
	}
	public String getCheckUnit() {
		return checkUnit;
	}
	public void setCheckUnit(String checkUnit) {
		this.checkUnit = checkUnit;
	}
	public String getEditTime() {
		return editTime;
	}
	public void setEditTime(String editTime) {
		this.editTime = editTime;
	}
	public String getCpmemo() {
		return cpmemo;
	}
	public void setCpmemo(String cpmemo) {
		this.cpmemo = cpmemo;
	}
	public String getPlandetail() {
		return plandetail;
	}
	public void setPlandetail(String plandetail) {
		this.plandetail = plandetail;
	}
	public int getPlandcount() {
		return plandcount;
	}
	public void setPlandcount(int plandcount) {
		this.plandcount = plandcount;
	}
	public String getUdate() {
		return udate;
	}
	public void setUdate(String udate) {
		this.udate = udate;
	}
	public String getReportNumber() {
		return reportNumber;
	}
	public void setReportNumber(String reportNumber) {
		this.reportNumber = reportNumber;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public CheckBox getCheck() {
		return check;
	}
	public void setCheck(CheckBox check) {
		this.check = check;
	}
	
}
