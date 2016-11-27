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
import com.dayuan.dy_6260chartscanner.entity.SelectItem;
import com.dayuan.dy_6260chartscanner.entity.TaskClass;
import com.dayuan.dy_6260chartscanner.entity.VerifyData;

public class SelectItemBiz {
	
	private String table="selectitem";
	 Context context;
	 public SelectItemBiz(Context context){
		 super();
		 this.context=context;
	 }
	public List<SelectItem>getItems(){
		List<SelectItem> items=new ArrayList<SelectItem>();
		SQLiteDatabase db=new DBOpenHelper(context).getReadableDatabase();
		Cursor c=db.query(table, null, null, null, null,null,null);
		for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
			SelectItem item=new SelectItem();
			int id=c.getInt(0);
			item.setId(id);
			item.setSampleName(c.getString(1));
			item.setSampleNumber(c.getString(2));
			item.setName(c.getString(3));
			item.setCheckStandard(c.getString(4));
			item.setStandardValue(c.getString(5));
			item.setDemarcate(c.getString(6));
			item.setUnit(c.getString(7));
			items.add(item);
		}
		c.close();
		db.close();
		return items;
	}
	public List<SelectItem>getSelectItems(String sampleName){
		List<SelectItem> items=new ArrayList<SelectItem>();
		SQLiteDatabase db=new DBOpenHelper(context).getReadableDatabase();
		//Cursor c=db.query(table, null, null, null, null,null,null);
		Cursor c=db.query(table, null,  "samplename like '%"+sampleName+"%'", null, null,null,null);
		for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
			SelectItem item=new SelectItem();
			int id=c.getInt(0);
			item.setId(id);
			item.setSampleName(c.getString(1));
			item.setSampleNumber(c.getString(2));
			item.setName(c.getString(3));
			item.setCheckStandard(c.getString(4));
			item.setStandardValue(c.getString(5));
			item.setDemarcate(c.getString(6));
			item.setUnit(c.getString(7));
			items.add(item);
		}
		c.close();
		db.close();
		return items;
	}
	public List<SelectItem>getSelect(String projectName){
		List<SelectItem> items=new ArrayList<SelectItem>();
		SQLiteDatabase db=new DBOpenHelper(context).getReadableDatabase();
		//Cursor c=db.query(table, null, null, null, null,null,null);
		Cursor c=db.query(table, null,  "name like '%"+projectName+"%'", null, null,null,null);
		for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
			SelectItem item=new SelectItem();
			int id=c.getInt(0);
			item.setId(id);
			item.setSampleName(c.getString(1));
			item.setSampleNumber(c.getString(2));
			item.setName(c.getString(3));
			item.setCheckStandard(c.getString(4));
			item.setStandardValue(c.getString(5));
			item.setDemarcate(c.getString(6));
			item.setUnit(c.getString(7));
			items.add(item);
		}
		c.close();
		db.close();
		return items;
	}
	public List<SelectItem>getSelects(String sampleName,String projectName){
		List<SelectItem> items=new ArrayList<SelectItem>();
		SQLiteDatabase db=new DBOpenHelper(context).getReadableDatabase();
		//Cursor c=db.query(table, null, null, null, null,null,null);
		Cursor c=db.query(table, null,  "samplename like '%"+sampleName+"%' and name like '%"+projectName+"%'"
				+"or name like '%"+projectName+"%' and samplename like '%"+sampleName+"%'", 
				null, null,null,null);
		for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
			SelectItem item=new SelectItem();
			int id=c.getInt(0);
			item.setId(id);
			item.setSampleName(c.getString(1));
			item.setSampleNumber(c.getString(2));
			item.setName(c.getString(3));
			item.setCheckStandard(c.getString(4));
			item.setStandardValue(c.getString(5));
			item.setDemarcate(c.getString(6));
			item.setUnit(c.getString(7));
			items.add(item);
		}
		c.close();
		db.close();
		return items;
	}
	public List<SelectItem>getSelectData(String projectName){
		List<SelectItem> items=new ArrayList<SelectItem>();
		SQLiteDatabase db=new DBOpenHelper(context).getReadableDatabase();
		//Cursor c=db.query(table, null, null, null, null,null,null);
		Cursor c=db.query(table, null,  "name like '%"+projectName+"%'", 
				null, null,null,null);
		if(c!=null){
		for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
			SelectItem item=new SelectItem();
			int id=c.getInt(0);
			item.setId(id);
			item.setSampleName(c.getString(1));
			item.setSampleNumber(c.getString(2));
			item.setName(c.getString(3));
			item.setCheckStandard(c.getString(4));
			item.setStandardValue(c.getString(5));
			item.setDemarcate(c.getString(6));
			item.setUnit(c.getString(7));
			items.add(item);
		}
		}
		c.close();
		db.close();
		return items;
	}
}
