package com.dayuan.dy_6260chartscanner.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.dayuan.dy_6260chartscanner.biz.TaskClassBiz;

import android.content.Context;
import android.widget.CheckBox;

public class TaskClass {
    int id;
    String checkNumber ;
	String cptitle ;
	String cpsdate ;
	String cpedate ;
	String cptproperty ;
	String cpfrom ;
	String cpeditor ;
	String checkUnitid ;
	String checkUnit ;
	String editTime ;
	String cpmemo ;
	String plandetail;
	int plandcount ;
	String baojingtime ;
	String udate ;
	String reportNumber;
	String result;
	private CheckBox check;
	public boolean isCheck;
	public TaskClass(CheckBox check) {
		this.check = check;
	}
//	private Context context;
//	public List<TaskClass> taskclasses=new TaskClassBiz(context).getTaskClass();
	public TaskClass() {
		super();
	}
	public TaskClass(int id, String checkNumber, String cptitle, String cpsdate,
			String cpedate, String cptproperty, String cpfrom, String cpeditor,
			String checkUnitid, String checkUnit, String editTime, String cpmemo,
			String plandetail, int plandcount, String baojingtime,
			String udate,String reportNumber,String result) {
		super();
		this.id = id;
		this.checkNumber = checkNumber;
		this.cptitle = cptitle;
		this.cpsdate = cpsdate;
		this.cpedate = cpedate;
		this.cptproperty = cptproperty;
		this.cpfrom = cpfrom;
		this.cpeditor = cpeditor;
		this.checkUnitid = checkUnitid;
		this.checkUnit = checkUnit;
		this.editTime = editTime;
		this.cpmemo = cpmemo;
		this.plandetail = plandetail;
		this.plandcount = plandcount;
		this.baojingtime = baojingtime;
		this.udate = udate;
		this.reportNumber=reportNumber;
		this.result=result;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getcheckNumber() {
		return checkNumber;
	}
	public void setcheckNumber(String checkNumber) {
		this.checkNumber = checkNumber;
	}
	public String getCptitle() {
		return cptitle;
	}
	public void setCptitle(String cptitle) {
		this.cptitle = cptitle;
	}
	public String getCpsdate() {
		return cpsdate;
	}
	public void setCpsdate(String cpsdate) {
		this.cpsdate = cpsdate;
	}
	public String getCpedate() {
		return cpedate;
	}
	public void setCpedate(String cpedate) {
		this.cpedate = cpedate;
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
	public String getcheckUnitid() {
		return checkUnitid;
	}
	public void setcheckUnitid(String checkUnitid) {
		this.checkUnitid = checkUnitid;
	}
	public String getcheckUnit() {
		return checkUnit;
	}
	public void setcheckUnit(String checkUnit) {
		this.checkUnit = checkUnit;
	}
	public String geteditTime() {
		return editTime;
	}
	public void seteditTime(String editTime) {
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
	public String getBaojingtime() {
		return baojingtime;
	}
	public void setBaojingtime(String baojingtime) {
		this.baojingtime = baojingtime;
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
//	public TaskClass(Context context){
//		new  TaskClassBiz(context).getTaskClass();
//	}	
}
