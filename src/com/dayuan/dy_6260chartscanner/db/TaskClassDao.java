package com.dayuan.dy_6260chartscanner.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class TaskClassDao {
	/**
	 * 数据库打开的帮助类
	 */
	private  DBOpenHelper helper;
	/**
	 * 在构造方法里面完成 必须要用的类的初始化
	 * @param context
	 */
	public TaskClassDao(Context context) {
		helper = new DBOpenHelper(context);
	}
	
	public  long update(String CPCODE, String reportNumber,String result
			
			){
		SQLiteDatabase db = helper.getWritableDatabase();
		//db.execSQL("insert into contactinfo (name,phone) values (?,?)", new Object[]{name,phone});
		ContentValues values = new ContentValues();
		values.put("reportnumber", reportNumber);
		values.put("result", result);
		//内部是组拼sql语句实现的.
		long rowid = db.update("taskclass",values,"cpcode=?",new String[]{CPCODE});
		//记得释放数据库资源
		db.close();
		
		return rowid;
	}
	public long update(String CPCODE,String CPTITLE,String CPSDATE, String CPEDATE,
			String CPTPROPERTY,String CPFROM,String CPEDITOR,String CPPORGID,
			String CPPORG,String CPEDDATE,String CPMEMO,String PLANDETAIL,int PLANDCOUNT,
			String BAOJINGTIME,String UDATE
			){
		SQLiteDatabase db = helper.getWritableDatabase();
		//db.execSQL("insert into contactinfo (name,phone) values (?,?)", new Object[]{name,phone});
		ContentValues values = new ContentValues();
//		values.put("cpcode", CPCODE);
		values.put("cptitle", CPTITLE);
		values.put("cpsdate", CPSDATE);
		values.put("cpedate", CPEDATE);
		values.put("cptproperty", CPTPROPERTY);
		values.put("cpfrom", CPFROM);
		values.put("cpeditor", CPEDITOR);
		values.put("cpporgid", CPPORGID);
		values.put("cpporg",CPPORG);
		values.put("cpeddate",CPEDDATE);
		values.put("cpmemo",CPMEMO);
		values.put("plandetail",PLANDETAIL);
		values.put("plandcount",PLANDCOUNT);
		values.put("baojingtime",BAOJINGTIME);
		values.put("udate",UDATE);
		long rowid= db.update("taskclass",values, "cpcode=?",new String[]{CPCODE});
		//记得释放数据库资源
		db.close();
		return rowid;
	}
	public long add(String CPCODE,String CPTITLE,String CPSDATE, String CPEDATE,
			String CPTPROPERTY,String CPFROM,String CPEDITOR,String CPPORGID,
			String CPPORG,String CPEDDATE,String CPMEMO,String PLANDETAIL,int PLANDCOUNT,
			String BAOJINGTIME,String UDATE
			){
		SQLiteDatabase db = helper.getWritableDatabase();
		//db.execSQL("insert into contactinfo (name,phone) values (?,?)", new Object[]{name,phone});
		ContentValues values = new ContentValues();
		values.put("cpcode", CPCODE);
		values.put("cptitle", CPTITLE);
		values.put("cpsdate", CPSDATE);
		values.put("cpedate", CPEDATE);
		values.put("cptproperty", CPTPROPERTY);
		values.put("cpfrom", CPFROM);
		values.put("cpeditor", CPEDITOR);
		values.put("cpporgid", CPPORGID);
		values.put("cpporg",CPPORG);
		values.put("cpeddate",CPEDDATE);
		values.put("cpmemo",CPMEMO);
		values.put("plandetail",PLANDETAIL);
		values.put("plandcount",PLANDCOUNT);
		values.put("baojingtime",BAOJINGTIME);
		values.put("udate",UDATE);
		//内部是组拼sql语句实现的.
		long rowid = db.insert("taskclass", null, values);
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
		int rowcount = db.delete("taskclass", "_id=?",new String[]{Integer.toString(id)}
);
		db.close();
		//再从数据库里面查询一遍,看name是否还在
		return rowcount;
	}
	public String getCheckNumber(String CPCODE){
		String checkNumber=null;
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor  cursor = db.rawQuery("select cpcode from taskclass where cpcode=?", new String[]{CPCODE});
		if(cursor.moveToNext()){//如果光标可以移动到下一位,代表就是查询到了数据
			checkNumber = cursor.getString(0);
		}
		cursor.close();//关闭掉游标,释放资源
		db.close();//关闭数据库,释放资源
		return checkNumber;
	}
}

