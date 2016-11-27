package com.dayuan.dy_6260chartscanner.biz;

import java.util.ArrayList;
import java.util.List;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dayuan.dy_6260chartscanner.db.MyDBOpenHelper;
import com.dayuan.dy_6260chartscanner.entity.QueryLog;

public class QueryLogBiz {
	
	private String table="log";
	 Context context;
	 public QueryLogBiz(Context context){
		 super();
		 this.context=context;
	 }
	public List<QueryLog>getQueryLog(){
		List<QueryLog> queryLog=new ArrayList<QueryLog>();
		SQLiteDatabase db=new MyDBOpenHelper(context).
				getReadableDatabase();
		Cursor c=db.query(table, null, null, null, null,null,"_id desc");
		for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
			QueryLog log=new QueryLog();
			int id=c.getInt(0);
			log.setId(id);
			log.setNumber(c.getInt(1));
			log.setSamplename(c.getString(2));
			log.setName(c.getString(3));
			log.setResult(c.getString(4));
			log.setDate(c.getString(5));
			log.setType(c.getString(6));
			log.setDensity(c.getString(7));
			log.setData(c.getString(8));
			queryLog.add(log);
		}
		c.close();
		db.close();
		return queryLog;
	}
}
