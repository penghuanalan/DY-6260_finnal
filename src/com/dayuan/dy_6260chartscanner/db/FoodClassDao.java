package com.dayuan.dy_6260chartscanner.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class FoodClassDao {
	/**
	 * 数据库打开的帮助类
	 */
	private DBOpenHelper helper;
	/**
	 * 在构造方法里面完成 必须要用的类的初始化
	 * @param context
	 */
	public FoodClassDao(Context context) {
		helper = new DBOpenHelper(context);
	}
	
	public long add(String syscode, String samplename
			
			){
		SQLiteDatabase db = helper.getWritableDatabase();
		//db.execSQL("insert into contactinfo (name,phone) values (?,?)", new Object[]{name,phone});
		ContentValues values = new ContentValues();
		values.put("syscode", syscode);
		values.put("samplename", samplename);
		//内部是组拼sql语句实现的.
		long rowid = db.insert("foodclass", null, values);
		//记得释放数据库资源
		db.close();
		
		return rowid;
	}
	public long update(String syscode,String samplename){
		SQLiteDatabase db = helper.getWritableDatabase();
		//db.execSQL("insert into contactinfo (name,phone) values (?,?)", new Object[]{name,phone});
		ContentValues values = new ContentValues();
		values.put("samplename",samplename);
//		values.put("checkedunit",checkedunit);
//		values.put("license", license);
		long rowid= db.update("foodclass",values, "syscode=?",new String[]{syscode});
		//记得释放数据库资源
		db.close();
		return rowid;
	}
	public long add(String syscode,String stdcode,String samplename, String shortcut,
			String checklevel,String checkitemcodes,String checkitemvalue,int number,int num,
			String date,String remark
			){
		SQLiteDatabase db = helper.getWritableDatabase();
		//db.execSQL("insert into contactinfo (name,phone) values (?,?)", new Object[]{name,phone});
		ContentValues values = new ContentValues();
		values.put("syscode", syscode);
		values.put("stdcode", stdcode);
		values.put("samplename", samplename);
		values.put("shortcut", shortcut);
		values.put("checklevel", checklevel);
		values.put("checkitemcodes", checkitemcodes);
		values.put("checkitemvalue", checkitemvalue);
		values.put("number", number);
		values.put("num",num);
		values.put("date",date);
		values.put("remark",remark);
		//内部是组拼sql语句实现的.
		long rowid = db.insert("foodclass", null, values);
		//记得释放数据库资源
		db.close();
		
		return rowid;
	}
	public long update(String syscode,String stdcode,String samplename, String shortcut,
			String checklevel,String checkitemcodes,String checkitemvalue,int number,int num,
			String date,String remark
			){
		SQLiteDatabase db = helper.getWritableDatabase();
		//db.execSQL("insert into contactinfo (name,phone) values (?,?)", new Object[]{name,phone});
		ContentValues values = new ContentValues();
		values.put("syscode", syscode);
		values.put("stdcode", stdcode);
		values.put("samplename", samplename);
		values.put("shortcut", shortcut);
		values.put("checklevel", checklevel);
		values.put("checkitemcodes", checkitemcodes);
		values.put("checkitemvalue", checkitemvalue);
		values.put("number", number);
		values.put("num",num);
		values.put("date",date);
		values.put("remark",remark);
		//内部是组拼sql语句实现的.
		long rowid= db.update("foodclass",values, "syscode=?",new String[]{syscode});
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
		int rowcount = db.delete("foodclass", "_id=?",new String[]{Integer.toString(id)}
);
		db.close();
		//再从数据库里面查询一遍,看name是否还在
		return rowcount;
	}
	public String getSysCode(String syscode){
		String sysCode=null;
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor  cursor = db.rawQuery("select syscode from foodclass where syscode=?", new String[]{syscode});
		if(cursor.moveToNext()){//如果光标可以移动到下一位,代表就是查询到了数据
			sysCode = cursor.getString(0);
		}
		cursor.close();//关闭掉游标,释放资源
		db.close();//关闭数据库,释放资源
		return sysCode;
	}
}

