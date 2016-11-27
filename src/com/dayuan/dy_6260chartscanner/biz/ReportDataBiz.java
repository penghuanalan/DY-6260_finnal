package com.dayuan.dy_6260chartscanner.biz;

import java.util.ArrayList;
import java.util.List;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dayuan.dy_6260chartscanner.db.DBOpenHelper;
import com.dayuan.dy_6260chartscanner.db.MyDBOpenHelper;
import com.dayuan.dy_6260chartscanner.entity.CheckNumber;
import com.dayuan.dy_6260chartscanner.entity.FoodClass;
import com.dayuan.dy_6260chartscanner.entity.QueryLog;
import com.dayuan.dy_6260chartscanner.entity.ReportData;
import com.dayuan.dy_6260chartscanner.entity.VerifyData;

public class ReportDataBiz {
	
	private String table="reportdata";
	 Context context;
	public ReportDataBiz(Context context) {
		this.context=context;
	}
	public List<ReportData>getReportDataClass(){
		List<ReportData> datas=new ArrayList<ReportData>();
		SQLiteDatabase db=new DBOpenHelper(context).getReadableDatabase();
		Cursor c=db.query(table, null, null, null, null,null,null);
		for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
			ReportData data=new ReportData();
			int id=c.getInt(0);
			data.setId(id);
			data.setProjectName(c.getString(2));
			data.setCheckNumber(c.getString(3));
			data.setCptproperty(c.getString(6));
			data.setCpfrom(c.getString(7));
			data.setCheckUnit(c.getString(10));
			data.setEditTime(c.getString(11));
			data.setPlandetail(c.getString(17));
			data.setPlandcount(c.getInt(18));
			data.setReportNumber(c.getString(20));
			data.setResult(c.getString(23));
			datas.add(data);
		}
		c.close();
		db.close();
		return datas;
	}
}
