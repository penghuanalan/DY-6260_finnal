package com.dayuan.dy_6260chartscanner.biz;

import java.util.ArrayList;
import java.util.List;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dayuan.dy_6260chartscanner.db.DBOpenHelper;
import com.dayuan.dy_6260chartscanner.entity.VerifyData;

public class VerifyDataBiz {
	
	private String table="verifydata";
	 Context context;
	 public VerifyDataBiz(Context context){
		 super();
		 this.context=context;
	 }
	public List<VerifyData>getVerifyData(){
		List<VerifyData> datas=new ArrayList<VerifyData>();
		SQLiteDatabase db=new DBOpenHelper(context).getReadableDatabase();
		Cursor c=db.query(table, null, null, null, null,null,null);
		for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
			VerifyData data=new VerifyData();
			int id=c.getInt(0);
			data.setId(id);
			data.setPointnum(c.getString(1));
			data.setPonitname(c.getString(2));
			data.setPointtype(c.getString(3));
			data.setOrgnum(c.getString(4));
			data.setOrgname(c.getString(5));
			datas.add(data);
		}
		c.close();
		db.close();
		return datas;
	}
}
