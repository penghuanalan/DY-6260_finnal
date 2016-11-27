package com.dayuan.dy_6260chartscanner.biz;

import java.util.ArrayList;
import java.util.List;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dayuan.dy_6260chartscanner.db.MyDBOpenHelper;
import com.dayuan.dy_6260chartscanner.entity.CheckNumber;
import com.dayuan.dy_6260chartscanner.entity.QueryLog;

public class DataManageBiz {
	
	private String table="datamanager";
	 Context context;
	 public DataManageBiz(Context context){
		 super();
		 this.context=context;
	 }
	public List<CheckNumber>getCheckNumber(){
		List<CheckNumber> numbers=new ArrayList<CheckNumber>();
		SQLiteDatabase db=new MyDBOpenHelper(context).getReadableDatabase();
		Cursor c=db.query(table, null, null, null, null,null,"_id desc");
		for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
			CheckNumber number=new CheckNumber();
			int id=c.getInt(0);
			number.setId(id);
			number.setCheckUnit(c.getString(4));
			number.setCheckedUnit(c.getString(13));
//			number.setCheckAddress(c.getString(12));
//			number.setCheckPerson(c.getString(7));
//			number.setSentPerson(c.getString(17));
			numbers.add(number);
		}
		c.close();
		db.close();
		return numbers;
	}
}
