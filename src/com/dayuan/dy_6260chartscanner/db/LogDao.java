 package com.dayuan.dy_6260chartscanner.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class LogDao {

	/**
	 * 数据库打开的帮助类
	 */
	private MyDBOpenHelper helper;
	/**
	 * 在构造方法里面完成 必须要用的类的初始化
	 * @param context
	 */
	public LogDao(Context context) {
		helper = new MyDBOpenHelper(context);
	}
	public long add(int number,String samplename,String name,String result,String date,String density,String data
			
			){
		SQLiteDatabase db = helper.getWritableDatabase();
		//db.execSQL("insert into contactinfo (name,phone) values (?,?)", new Object[]{name,phone});
		ContentValues values = new ContentValues();
		values.put("sampleid", number);
		values.put("samplename", samplename);
		values.put("name", name);
		values.put("density", density);
		values.put("result", result);
		values.put("date", date);
		values.put("data", data);
		//内部是组拼sql语句实现的.
		long rowid = db.insert("log", null, values);
		//记得释放数据库资源
		db.close();
		
		return rowid;
	}
	public long add(int number, String samplename,String name,String result,String date,String type,String density,String data
			){
		SQLiteDatabase db = helper.getWritableDatabase();
		//db.execSQL("insert into contactinfo (name,phone) values (?,?)", new Object[]{name,phone});
		ContentValues values = new ContentValues();
		values.put("sampleid", number);
		values.put("samplename", samplename);
		values.put("name", name);
		values.put("result", result);
		values.put("date", date);
		values.put("type", type);
		values.put("density", density);
		values.put("data", data);
		//内部是组拼sql语句实现的.
		long rowid = db.insert("log", null, values);
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
		int rowcount = db.delete("log", "_id=?",new String[]{Integer.toString(id)}
);
		db.close();
		//再从数据库里面查询一遍,看name是否还在
		return rowcount;
	}
	public int deleteAll(){
		//判断这个数据是否存在.
		SQLiteDatabase db = helper.getWritableDatabase();
		//db.execSQL("delete from contactinfo where name=?", new Object[]{name});
		int countRow= db.delete("log", null,null);
		db.close();
		//再从数据库里面查询一遍,看name是否还在
		return countRow;
	}
	public int update(int id,String newnumber, String newsamplename,String newname,
			String newresult,String newdate,String newtype
			 ){
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("sampleid", newnumber);
		values.put("samplename", newsamplename);
		values.put("name", newname);
		values.put("result", newresult);
		values.put("date", newdate);
		values.put("type", newtype);
		int rowcount= db.update("info", values, "_id=?", new String[]{Integer.toString(id)});
		db.close();
		return rowcount;
	}
}
