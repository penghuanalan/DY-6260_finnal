 package com.dayuan.dy_6260chartscanner.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ReportNumberDao {

	/**
	 * 数据库打开的帮助类
	 */
	private TheDBOpenHelper helper;
	/**
	 * 在构造方法里面完成 必须要用的类的初始化
	 * @param context
	 */
	public ReportNumberDao(Context context) {
		helper = new TheDBOpenHelper(context);
	}
	public long add(int number
			){
		SQLiteDatabase db = helper.getWritableDatabase();
		//db.execSQL("insert into contactinfo (name,phone) values (?,?)", new Object[]{name,phone});
		ContentValues values = new ContentValues();
		values.put("number", number);
		//内部是组拼sql语句实现的.
		long rowid = db.insert("reportnumber", null, values);
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
		int rowcount = db.delete("reportnumber", "_id=?",new String[]{Integer.toString(id)}
);
		db.close();
		//再从数据库里面查询一遍,看name是否还在
		return rowcount;
	}
	public int update(int id,int newnumber){
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("number",newnumber);
		int rowcount= db.update("reportnumber", values, "_id=?", new String[]{Integer.toString(id)});
		db.close();
		return rowcount;	
	}
	public int getReportNumber(){
		int reportNumber=0;
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor  cursor = db.rawQuery("select * from reportnumber", null);
		if(cursor.moveToNext()){//如果光标可以移动到下一位,代表就是查询到了数据
			reportNumber = cursor.getInt(1);
		}
		cursor.close();//关闭掉游标,释放资源
		db.close();//关闭数据库,释放资源
		return reportNumber;
	}
}
