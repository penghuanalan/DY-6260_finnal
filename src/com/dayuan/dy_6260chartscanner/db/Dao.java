 package com.dayuan.dy_6260chartscanner.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Dao {

	/**
	 * 数据库打开的帮助类
	 */
	private MyDBOpenHelper helper;
	/**
	 * 在构造方法里面完成 必须要用的类的初始化
	 * @param context
	 */
	public Dao(Context context) {
		helper = new MyDBOpenHelper(context);
	}
	public long add(int sampleid,String name, String method,
			String valueC,
			String valueT,String valueTL,String constant0,
			String constant1,String constant2,
			String constant3,
			String unit,String limitV){
		SQLiteDatabase db = helper.getWritableDatabase();
		//db.execSQL("insert into contactinfo (name,phone) values (?,?)", new Object[]{name,phone});
		ContentValues values = new ContentValues();
		values.put("sampleid", sampleid);
		values.put("name", name);
		//values.put("samplename", samplename);
		values.put("method", method);
		values.put("valueC", valueC);
		values.put("valueT", valueT);
		values.put("valueTL", valueTL);
		values.put("constant0", constant0);
		values.put("constant1", constant1);
		values.put("constant2", constant2);
		values.put("constant3", constant3);
		values.put("unit", unit);
		values.put("limitV", limitV);
		//内部是组拼sql语句实现的.
		long rowid = db.insert("info", null, values);
		//记得释放数据库资源
		db.close();
		
		return rowid;
	}
	public long add(int sampleid,String name, String method,
			String valueC,
			String valueT,String valueTL,String constant0,
			String constant1,String constant2,
			String constant3,
			String unit,String limitV,String valueOne,String valueTwo,String valueThree,String result){
		SQLiteDatabase db = helper.getWritableDatabase();
		//db.execSQL("insert into contactinfo (name,phone) values (?,?)", new Object[]{name,phone});
		ContentValues values = new ContentValues();
		values.put("sampleid", sampleid);
		values.put("name", name);
		//values.put("samplename", samplename);
		values.put("method", method);
		values.put("valueC", valueC);
		values.put("valueT", valueT);
		values.put("valueTL", valueTL);
		values.put("constant0", constant0);
		values.put("constant1", constant1);
		values.put("constant2", constant2);
		values.put("constant3", constant3);
		values.put("unit", unit);
		values.put("limitV", limitV);
		values.put("valueone", valueOne);
		values.put("valuetwo", valueTwo);
		values.put("valuethree",valueThree);
		values.put("result", result);
		//内部是组拼sql语句实现的.
		long rowid = db.insert("info", null, values);
		//记得释放数据库资源
		db.close();
		
		return rowid;
	}
	public long add(int sampleid,String name, String method,
			String valueC,
			String valueT,String valueTL,String constant0,
			String constant1,String constant2,
			String constant3,
			String unit,String limitV,String samplename){
		SQLiteDatabase db = helper.getWritableDatabase();
		//db.execSQL("insert into contactinfo (name,phone) values (?,?)", new Object[]{name,phone});
		ContentValues values = new ContentValues();
		values.put("sampleid", sampleid);
		values.put("name", name);
		//values.put("samplename", samplename);
		values.put("method", method);
		values.put("valueC", valueC);
		values.put("valueT", valueT);
		values.put("valueTL", valueTL);
		values.put("constant0", constant0);
		values.put("constant1", constant1);
		values.put("constant2", constant2);
		values.put("constant3", constant3);
		values.put("unit", unit);
		values.put("limitV", limitV);
		values.put("samplename", samplename);
		//内部是组拼sql语句实现的.
		long rowid = db.insert("info", null, values);
		//记得释放数据库资源
		db.close();
		
		return rowid;
	}
	/**
	 * 根据姓名删除一条记录
	 * @param name 要删除的联系人的姓名
	 * @return 返回0代表的是没有删除任何的记录 返回整数int值代表删除了几条数据
	 */
	public int delete(int id){
		//判断这个数据是否存在.
		SQLiteDatabase db = helper.getWritableDatabase();
		//db.execSQL("delete from contactinfo where name=?", new Object[]{name});
		int rowcount = db.delete("info", "_id=?",new String[]{Integer.toString(id)}
);
		db.close();
		//再从数据库里面查询一遍,看name是否还在
		return rowcount;
	}
	public int deleteAll(){
		//判断这个数据是否存在.
		SQLiteDatabase db = helper.getWritableDatabase();
		//db.execSQL("delete from contactinfo where name=?", new Object[]{name});
		int countRow= db.delete("info", null,null);
		db.close();
		//再从数据库里面查询一遍,看name是否还在
		return countRow;
	}
	public int update(String name,int newsampleid,String newsamplename){
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("sampleid",newsampleid);
		values.put("samplename", newsamplename);
		int rowcount= db.update("info", values, "name=?", new String[]{name});
		db.close();
		return rowcount;	
	}
	public int update(int newsampleid,String name){
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("sampleid",newsampleid);
		//values.put("samplename", newsamplename);
		int rowcount= db.update("info", values, "name=?", new String[]{name});
		db.close();
		return rowcount;	
	}
	public int update(int id,String newname,String newmethod,String newvalueC,
		    String newvalueT,String newvalueTL,String  newconstant0,
			String newconstant1,String newconstant2,String newconstant3
			,String newunit,String newlimitV){
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("name", newname);
		values.put("method", newmethod);
		values.put("valueC", newvalueC);
		values.put("valueT", newvalueT);
		values.put("valueTL", newvalueTL);
		values.put("constant0", newconstant0);
		values.put("constant1", newconstant1);
		values.put("constant2", newconstant2);
		values.put("constant3", newconstant3);
		values.put("unit", newunit);
		values.put("limitV", newlimitV);
		int rowcount= db.update("info", values, "_id=?", new String[]{Integer.toString(id)});
		db.close();
		return rowcount;
	}
	public int update(int id,String newname,String newmethod,String newvalueC,
			String newvalueT,String newvalueTL,String  newconstant0,
			String newconstant1,String newconstant2,String newconstant3
			,String newunit,String newlimitV,String newvalueOne,String newvalueTwo,
			String newvalueThree,String newresult){
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("name", newname);
		values.put("method", newmethod);
		values.put("valueC", newvalueC);
		values.put("valueT", newvalueT);
		values.put("valueTL", newvalueTL);
		values.put("constant0", newconstant0);
		values.put("constant1", newconstant1);
		values.put("constant2", newconstant2);
		values.put("constant3", newconstant3);
		values.put("unit", newunit);
		values.put("limitV", newlimitV);
		values.put("valueone", newvalueOne);
		values.put("valuetwo", newvalueTwo);
		values.put("valuethree",newvalueThree);
		values.put("result", newresult);
		int rowcount= db.update("info", values, "_id=?", new String[]{Integer.toString(id)});
		db.close();
		return rowcount;
	}
}
