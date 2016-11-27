package com.dayuan.dy_6260chartscanner.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {

	public DBOpenHelper(Context context) {
		super(context, "dayuan02.db", null, 1);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table verifydata"
				+ " (_id integer primary key autoincrement,"
				+ "pointnum varchar(40),pointname varchar(40),"
				+ "pointtype varchar(40),"
				+ "orgnum varchar(40),"
				+ "orgname varchar(40))"
				);
		db.execSQL("create table foodclass"
				+ " (_id integer primary key autoincrement,"
				+ "syscode varchar(40),stdcode varchar(40),"
				+ "samplename varchar(40),"
				+ "shortcut varchar(40),"
				+ "checklevel varchar(40),"
				+ "checkitemcodes varchar(60),"
				+ "checkitemvalue varchar(40),"
				+ "number int(20),"
				+ "num int(20),"
				+ "date datetime,"
				+ "remark varchar(100))"
				);
		db.execSQL("create table company "
				+ "(_id integer primary key autoincrement,"
				+ " syscode varchar(40),stdcode varchar(40), "
				+ "companyid varchar(40),unitname varchar(50),displayname varchar(50),"
				+ "property varchar(50),kindcode varchar(40),regcapital varchar(40),"
				+"capitalunit varchar(40),incorporator varchar(40),regdate varchar(40)," +
				"districtcode varchar(40),postcode varchar(40),address varchar(40)," +
				"linkman varchar(40),linkinfo varchar(40),creditlevel varchar(40)," +
				"creditrecord varchar(40),productinfo varchar(40),otherInfo varchar(40)," +
				"checklevel varchar(40),foodsaferecord varchar(40),number int(20)," +
				"num int(20),date datetime,remark varchar(100),sign varchar(40))");
		db.execSQL("create table selectitem"
				+ " (_id integer primary key autoincrement,"
				+ "samplename varchar(40),samplenum varchar(40),"
				+ "name varchar(40),"
				+ "itemdes varchar(40),"
				+ "standardvalue varchar(40),"
				+ "demarcate varchar(40),"
				+ "unit varchar(40))"
				);
		db.execSQL("create table taskclass"
				+ " (_id integer primary key autoincrement,"
				+ "cpcode varchar(100),cptitle varchar(100),"//任务编号,任务主题
				+ "cpsdate datetime,"//起始日期,检测日期
				+ "cpedate datetime,"//结束日期
				+ "cptproperty varchar(40),"//任务性质
				+ "cpfrom varchar(40),"//任务来源
				+ "cpeditor varchar(40),"//编制人
				+ "cpporgid varchar(40),"//机构编码
				+ "cpporg varchar(100),"//检测单位
				+ "cpeddate datetime,"//编制日期
				+ "cpmemo text,plandetail text,plandcount int(20)," //备注,任务详细内容,计划抽检数量
				+"baojingtime datetime,udate datetime,reportnumber varchar(40),result varchar(40))" //检测结果
				);
		db.execSQL("create table reportdata"
				+ " (_id integer primary key autoincrement,"
				+"checkedunit varchar(100)," //被检单位
				+"projectname varchar(40),"//检测项目
				+ "cpcode varchar(100),cptitle varchar(100),"//任务编号(检测编号),任务主题
				+ "cpsdate datetime,"//起始日期,检测日期
				+ "cptproperty varchar(40),"//任务性质
				+ "cpfrom varchar(40),"//任务来源
				+ "cpeditor varchar(40),"//编制人
				+ "cpporgid varchar(40),"//机构编码
				+ "cpporg varchar(100),"//检测单位
				+ "cpeddate datetime,"//编制日期
				+ "cpmemo text," //备注,计划抽检数量
				+"checkunitphone varchar(40),"//抽检单位电话
				+"checkedunitphone varchar(40),"//被抽样单位电话
				+"checkperson varchar(40),"//抽检人
				+"sendperson varchar(40)," //送检人
				+"plandetail text,"//任务详细内容
				+"plandcount int(20),"//抽检数量
				+"checkaddress varchar(100),reportnumber varchar(40),"//抽样地点，送检日期，检测报告编号
				+"checkedaddress varchar(100),"
				+"checkstandard varchar(40),"
			    +"result varchar(40))" //检测结果
			    
				);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		 //db.execSQL("alter table info add samplename varchar(50)" );

	}

}
