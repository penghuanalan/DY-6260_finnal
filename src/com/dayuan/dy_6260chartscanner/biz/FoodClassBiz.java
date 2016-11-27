package com.dayuan.dy_6260chartscanner.biz;

import java.util.ArrayList;
import java.util.List;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dayuan.dy_6260chartscanner.db.MyDBOpenHelper;
import com.dayuan.dy_6260chartscanner.entity.CheckNumber;
import com.dayuan.dy_6260chartscanner.entity.FoodClass;
import com.dayuan.dy_6260chartscanner.entity.QueryLog;

public class FoodClassBiz {
	
	private String table="foodclass";
	 Context context;
	 public FoodClassBiz(Context context){
		 super();
		 this.context=context;
	 }
	public List<FoodClass>getFoodClass(){
		List<FoodClass> foodclasses=new ArrayList<FoodClass>();
		SQLiteDatabase db=new MyDBOpenHelper(context).getReadableDatabase();
		Cursor c=db.query(table, null, null, null, null,null,null);
		for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
			FoodClass foodclass=new FoodClass();
			int id=c.getInt(0);
			foodclass.setId(id);
			foodclass.setSysCode(c.getString(1));
			foodclass.setSampleName(c.getString(3));
//			foodclass.setCheckedUnit(c.getString(13));
//			foodclass.setCheckAddress(c.getString(12));
//			foodclass.setCheckPerson(c.getString(7));
//			foodclass.setSentPerson(c.getString(17));
			foodclasses.add(foodclass);
		}
		c.close();
		db.close();
		return foodclasses;
	}
}
