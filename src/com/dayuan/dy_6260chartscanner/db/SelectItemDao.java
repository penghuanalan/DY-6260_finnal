package com.dayuan.dy_6260chartscanner.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SelectItemDao {
	/**
	 * 数据库打开的帮助类
	 */
	private DBOpenHelper helper;
	/**
	 * 在构造方法里面完成 必须要用的类的初始化
	 * @param context
	 */
	public SelectItemDao(Context context) {
		helper = new DBOpenHelper(context);
	}
	
//	public long update(String syscode,String samplename){
//		SQLiteDatabase db = helper.getWritableDatabase();
//		//db.execSQL("insert into contactinfo (name,phone) values (?,?)", new Object[]{name,phone});
//		ContentValues values = new ContentValues();
//		values.put("samplename",samplename);
////		values.put("checkedunit",checkedunit);
////		values.put("license", license);
//		long rowid= db.update("foodclass",values, "syscode=?",new String[]{syscode});
//		//记得释放数据库资源
//		db.close();
//		return rowid;
//	}
	public long add(String samplename,String samplenum,String name, String itemdes,
			String standardvalue,String demarcate,String unit
			){
		SQLiteDatabase db = helper.getWritableDatabase();
		//db.execSQL("insert into contactinfo (name,phone) values (?,?)", new Object[]{name,phone});
		ContentValues values = new ContentValues();
		values.put("samplename", samplename);
		values.put("samplenum", samplenum);
		values.put("name", name);
		values.put("itemdes", itemdes);
		values.put("standardvalue", standardvalue);
		values.put("demarcate", demarcate);
		values.put("unit", unit);
		//内部是组拼sql语句实现的.
		long rowid = db.insert("selectitem", null, values);
		//记得释放数据库资源
		db.close();
		
		return rowid;
	}
	public long add(String samplename,String name, String itemdes,
			String standardvalue,String demarcate,String unit
			){
		SQLiteDatabase db = helper.getWritableDatabase();
		//db.execSQL("insert into contactinfo (name,phone) values (?,?)", new Object[]{name,phone});
		ContentValues values = new ContentValues();
		values.put("samplename", samplename);
//		values.put("samplenum", samplenum);
		values.put("name", name);
		values.put("itemdes", itemdes);
		values.put("standardvalue", standardvalue);
		values.put("demarcate", demarcate);
		values.put("unit", unit);
		//内部是组拼sql语句实现的.
		long rowid = db.insert("selectitem", null, values);
		//记得释放数据库资源
		db.close();
		
		return rowid;
	}
	public long update(int id,String samplename,String name, String itemdes,
			String standardvalue,String demarcate,String unit
			){
		SQLiteDatabase db = helper.getWritableDatabase();
		//db.execSQL("insert into contactinfo (name,phone) values (?,?)", new Object[]{name,phone});
		ContentValues values = new ContentValues();
		values.put("samplename", samplename);
//		values.put("samplenum", samplenum);
		values.put("name", name);
		values.put("itemdes", itemdes);
		values.put("standardvalue", standardvalue);
		values.put("demarcate", demarcate);
		values.put("unit", unit);
		//内部是组拼sql语句实现的.
		long rowid = db.update("selectitem",values, "_id=?",new String[]{String.valueOf(id)});
		//记得释放数据库资源
		db.close();
		
		return rowid;
	}
	public long update(String samplename,String samplenum,String name, String itemdes,
			String standardvalue,String demarcate,String unit
			){
		SQLiteDatabase db = helper.getWritableDatabase();
		//db.execSQL("insert into contactinfo (name,phone) values (?,?)", new Object[]{name,phone});
		ContentValues values = new ContentValues();
		values.put("samplename", samplename);
//		values.put("samplenum", samplenum);
		values.put("name", name);
		values.put("itemdes", itemdes);
		values.put("standardvalue", standardvalue);
		values.put("demarcate", demarcate);
		values.put("unit", unit);
		//内部是组拼sql语句实现的.
		long rowid = db.update("selectitem",values, "samplenum=?",new String[]{samplenum});
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
		int rowcount = db.delete("selectitem", "_id=?",new String[]{Integer.toString(id)}
);
		db.close();
		//再从数据库里面查询一遍,看name是否还在
		return rowcount;
	}
	public int deleteAll(){
		//判断这个数据是否存在.
		SQLiteDatabase db = helper.getWritableDatabase();
		//db.execSQL("delete from contactinfo where name=?", new Object[]{name});
		int countRow= db.delete("selectitem", null,null);
		db.close();
		//再从数据库里面查询一遍,看name是否还在
		return countRow;
	}
	public String getSampleNum(String samplenum){
		String sampleNum=null;
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor  cursor = db.rawQuery("select samplenum from selectitem where samplenum=?", new String[]{samplenum});
		if(cursor.moveToNext()){//如果光标可以移动到下一位,代表就是查询到了数据
			sampleNum = cursor.getString(0);
		}
		cursor.close();//关闭掉游标,释放资源
		db.close();//关闭数据库,释放资源
		return sampleNum;
	}
//	public String getSampleNum(String samplenum){
//		String sampleNum=null;
//		SQLiteDatabase db = helper.getReadableDatabase();
//		Cursor  cursor = db.rawQuery("select samplenum from selectitem where samplenum=?", new String[]{samplenum});
//		if(cursor.moveToNext()){//如果光标可以移动到下一位,代表就是查询到了数据
//			sampleNum = cursor.getString(0);
//		}
//		cursor.close();//关闭掉游标,释放资源
//		db.close();//关闭数据库,释放资源
//		return sampleNum;
//	}
//	public List getSampleName(String stringSample){
//		List<String> samples=new ArrayList<>();
//		SQLiteDatabase db = helper.getReadableDatabase();
//		String sql="select*from selectitem where samplename like'%,"+stringSample+",%'order by _id";
//		Cursor cursor=db.rawQuery(sql, null);
//		if(cursor.moveToNext()){
//			String sample=cursor.getString(0);
//			samples.add(sample);
//		}
//		cursor.close();//关闭掉游标,释放资源
//		db.close();//关闭数据库,释放资源
//		return samples;
//	}
}

