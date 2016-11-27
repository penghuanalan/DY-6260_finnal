 package com.dayuan.dy_6260chartscanner.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class OrderDao {

	/**
	 * 数据库打开的帮助类
	 */
	private TheDBOpenHelper helper;
	/**
	 * 在构造方法里面完成 必须要用的类的初始化
	 * @param context
	 */
	public OrderDao(Context context) {
		helper = new TheDBOpenHelper(context);
	}
	public long add(String open
			){
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("open", open);
		//内部是组拼sql语句实现的.
		long rowid = db.insert("autosetting", null, values);
		//记得释放数据库资源
		db.close();
		return rowid;
	}
	public long add02(String uploadOrder
			){
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("autoupload", uploadOrder);
		//内部是组拼sql语句实现的.
		long rowid = db.insert("autosetting", null, values);
		//记得释放数据库资源
		db.close();
		return rowid;
	}
	public long add03(String locateOrder
			){
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("locate", locateOrder);
		//内部是组拼sql语句实现的.
		long rowid = db.insert("autosetting", null, values);
		//记得释放数据库资源
		System.out.println("数据库存储了数据");
		db.close();
		return rowid;
	}
	public long add04(String outprint
			){
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("outprint", outprint);
		//内部是组拼sql语句实现的.
		long rowid = db.insert("autosetting", null, values);
		//记得释放数据库资源
		db.close();
		return rowid;
	}
	/**
	 * 根据姓名删除一条记录
	 * @param name 要删除的联系人的姓名
	 * @return 返回0代表的是没有删除任何的记录 返回整数int值代表删除了几条数据
	 */
//	public int delete(int id){
//		//判断这个数据是否存在.
//		SQLiteDatabase db = helper.getWritableDatabase();
//		//db.execSQL("delete from contactinfo where name=?", new Object[]{name});
//		int rowcount = db.delete("autosetting", "_id=?",new String[]{Integer.toString(id)}
//);
//		db.close();
//		//再从数据库里面查询一遍,看name是否还在
//		return rowcount;
//	}
	 public void delete(String openOrder) {  
	        SQLiteDatabase db = helper.getWritableDatabase();  
	        db.execSQL("delete from autosetting where open=?", new Object[] { openOrder });  
	        db.close();  
	    }  
	 public void delete02(String uploadOrder) {  
		 SQLiteDatabase db = helper.getWritableDatabase();  
		 db.execSQL("delete from autosetting where autoupload=?", new Object[] { uploadOrder });  
		 db.close();  
	 }  
	 public void delete03(String locateOrder) {  
		 SQLiteDatabase db = helper.getWritableDatabase();  
		 db.execSQL("delete from autosetting where locate=?", new Object[] { locateOrder });  
		 db.close();  
	 }  
	 public void delete04(String outprint) {  
		 SQLiteDatabase db = helper.getWritableDatabase();  
		 db.execSQL("delete from autosetting where outprint=?", new Object[] { outprint });  
		 db.close();  
	 }  
	public int update(String openOrder){
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("open",openOrder);
		int rowcount= db.update("autosetting", values, "open=?", new String[]{openOrder});
		db.close();
		return rowcount;	
	}
	public String getOrder(String openOrder){
		String order=null;
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor  cursor = db.rawQuery("select open from autosetting where open=?", new String[]{openOrder});
		if(cursor.moveToNext()){//如果光标可以移动到下一位,代表就是查询到了数据
			order = cursor.getString(0);
		}
		cursor.close();//关闭掉游标,释放资源
		db.close();//关闭数据库,释放资源
		return order;
	}
	public String getUploadOrder(String uploadOrder){
		String uploadorder=null;
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor  cursor = db.rawQuery("select autoupload from autosetting where autoupload=?", new String[]{uploadOrder});
		if(cursor.moveToNext()){//如果光标可以移动到下一位,代表就是查询到了数据
			uploadorder = cursor.getString(0);
		}
		cursor.close();//关闭掉游标,释放资源
		db.close();//关闭数据库,释放资源
		return uploadorder;
	}
	public String getLocateOrder(String locateOrder){
		String locateorder=null;
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor  cursor = db.rawQuery("select locate from autosetting where locate=?", new String[]{locateOrder});
		if(cursor.moveToNext()){//如果光标可以移动到下一位,代表就是查询到了数据
			locateorder = cursor.getString(0);
		}
		cursor.close();//关闭掉游标,释放资源
		db.close();//关闭数据库,释放资源
		return locateorder;
	}
	
	public String getOutPrint(String outPrint){
		String outprint=null;
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor  cursor = db.rawQuery("select outprint from autosetting where outprint=?", new String[]{outPrint});
		if(cursor.moveToNext()){//如果光标可以移动到下一位,代表就是查询到了数据
			outprint = cursor.getString(0);
		}
		cursor.close();//关闭掉游标,释放资源
		db.close();//关闭数据库,释放资源
		return outprint;
	}
}
