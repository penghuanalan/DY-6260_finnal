package com.dayuan.dy_6260chartscanner.entity;

public class VerifyData {
    int id;
	String pointnum;
	String ponitname;
	String pointtype;
	String orgnum;
	String orgname;
	
	public VerifyData() {
		super();
	}

	public VerifyData(int id, String pointnum, String ponitname,
			String pointtype, String orgnum, String orgname) {
		super();
		this.id = id;
		this.pointnum = pointnum;
		this.ponitname = ponitname;
		this.pointtype = pointtype;
		this.orgnum = orgnum;
		this.orgname = orgname;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPointnum() {
		return pointnum;
	}

	public void setPointnum(String pointnum) {
		this.pointnum = pointnum;
	}

	public String getPonitname() {
		return ponitname;
	}

	public void setPonitname(String ponitname) {
		this.ponitname = ponitname;
	}

	public String getPointtype() {
		return pointtype;
	}

	public void setPointtype(String pointtype) {
		this.pointtype = pointtype;
	}

	public String getOrgnum() {
		return orgnum;
	}

	public void setOrgnum(String orgnum) {
		this.orgnum = orgnum;
	}

	public String getOrgname() {
		return orgname;
	}

	public void setOrgname(String orgname) {
		this.orgname = orgname;
	}
	
	
}
