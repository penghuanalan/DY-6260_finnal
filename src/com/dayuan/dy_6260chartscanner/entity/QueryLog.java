package com.dayuan.dy_6260chartscanner.entity;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.util.Log;
import android.widget.CheckBox;

public class QueryLog {

	private int id;
	private int number;
	private String samplename;
	private String name;
	private String result;
	private String date;
	private String type;
	private String density;
	private String data;
	private CheckBox selected;
	public boolean isCheck;
	public QueryLog() {
		super();
	}


	public QueryLog(int id, int number, String samplename, String name,
			String result, String date, String type, String density,String data,CheckBox selected) {
		super();
		this.id = id;
		this.number = number;
		this.samplename = samplename;
		this.name = name;
		this.result = result;
		this.date = date;
		this.type = type;
		this.density = density;
		this.data=data;
		this.selected=selected;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public int getNumber() {
		return number;
	}


	public void setNumber(int number) {
		this.number = number;
	}


	public String getSamplename() {
		return samplename;
	}


	public void setSamplename(String samplename) {
		this.samplename = samplename;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getResult() {
		return result;
	}


	public void setResult(String result) {
		this.result = result;
	}


	public String getDate() {
		return date;
	}


	public void setDate(String date) {
		this.date = date;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getDensity() {
		return density;
	}


	public void setDensity(String density) {
		this.density = density;
	}


	public String getData() {
		return data;
	}


	public void setData(String data) {
		this.data = data;
	}


	public CheckBox getSelected() {
		return selected;
	}


	public void setSelected(CheckBox selected) {
		this.selected = selected;
	}
	
	
	
}
