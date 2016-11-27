package com.dayuan.dy_6260chartscanner.entity;

public class Project {
    private int id;
    private int sampleid;
	private String name;
	private String samplename;
	private String method;
	private String valueC;
	private String unit;
	private String limitV;
	private String valueT;
	private String valueTL;
	private String constant0;
    private	String constant1;
    private String constant2;
    private String constant3;
    private String valueOne;
    private String valueTwo;
    private String valueThree;
    private String result;
	public Project() {
		super();
	}
	
	public Project(String name) {
		super();
		this.name = name;
	}

	public Project(int id){
		this.id=id;
	}
	

	public Project(int id, int sampleid, String name, String samplename,
			String method, String valueC, String unit, String limitV,
			String valueT, String valueTL, String constant0, String constant1,
			String constant2, String constant3, String valueOne,
			String valueTwo, String valueThree, String result) {
		super();
		this.id = id;
		this.sampleid = sampleid;
		this.name = name;
		this.samplename = samplename;
		this.method = method;
		this.valueC = valueC;
		this.unit = unit;
		this.limitV = limitV;
		this.valueT = valueT;
		this.valueTL = valueTL;
		this.constant0 = constant0;
		this.constant1 = constant1;
		this.constant2 = constant2;
		this.constant3 = constant3;
		this.valueOne = valueOne;
		this.valueTwo = valueTwo;
		this.valueThree = valueThree;
		this.result = result;
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSampleid() {
		return sampleid;
	}

	public void setSampleid(int sampleid) {
		this.sampleid = sampleid;
	}

	public  String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getSamplename() {
		return samplename;
	}

	public void setSamplename(String samplename) {
		this.samplename = samplename;
	}

	public  String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getValueC() {
		return valueC;
	}
	public void setValueC(String valueC) {
		this.valueC = valueC;
	}
	
	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getLimitV() {
		return limitV;
	}

	public void setLimitV(String limitV) {
		this.limitV = limitV;
	}

	public String getValueT() {
		return valueT;
	}
	public void setValueT(String valueT) {
		this.valueT = valueT;
	}
	public String getValueTL() {
		return valueTL;
	}
	public void setValueTL(String valueTL) {
		this.valueTL = valueTL;
	}
	public String getConstant0() {
		return constant0;
	}
	public void setConstant0(String constant0) {
		this.constant0 = constant0;
	}
	public String getConstant1() {
		return constant1;
	}
	public void setConstant1(String constant1) {
		this.constant1 = constant1;
	}
	public String getConstant2() {
		return constant2;
	}
	public void setConstant2(String constant2) {
		this.constant2 = constant2;
	}
	public String getConstant3() {
		return constant3;
	}
	public void setConstant3(String constant3) {
		this.constant3 = constant3;
	}

	public String getValueOne() {
		return valueOne;
	}

	public void setValueOne(String valueOne) {
		this.valueOne = valueOne;
	}

	public String getValueTwo() {
		return valueTwo;
	}

	public void setValueTwo(String valueTwo) {
		this.valueTwo = valueTwo;
	}

	public String getValueThree() {
		return valueThree;
	}

	public void setValueThree(String valueThree) {
		this.valueThree = valueThree;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	@Override
	public String toString() {
		return "Project [id=" + id + ", name=" + name + ", method=" + method
				+ ", valueC=" + valueC + ", unit=" + unit + ", limitV="
				+ limitV + ", valueT=" + valueT + ", valueTL=" + valueTL
				+ ", constant0=" + constant0 + ", constant1=" + constant1
				+ ", constant2=" + constant2 + ", constant3=" + constant3 + "]";
	}


	
    
}
