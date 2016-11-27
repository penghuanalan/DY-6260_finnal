 package com.dayuan.dy_6260chartscanner.db;

import java.util.ArrayList;
import java.util.List;

import com.dayuan.dy_6260chartscanner.entity.Server;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ServerDao {

	/**
	 * 数据库打开的帮助类
	 */
	private TheDBOpenHelper helper;
	/**
	 * 在构造方法里面完成 必须要用的类的初始化
	 * @param context
	 */
	public ServerDao(Context context) {
		helper = new TheDBOpenHelper(context);
	}
	public long add(String address,String user,String password
			
			){
		SQLiteDatabase db = helper.getWritableDatabase();
		//db.execSQL("insert into contactinfo (name,phone) values (?,?)", new Object[]{name,phone});
		ContentValues values = new ContentValues();
		values.put("address", address);
		values.put("user", user);
		values.put("password", password);
		//内部是组拼sql语句实现的.
		long rowid = db.insert("connect", null, values);
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
		int rowcount = db.delete("connect", "_id=?",new String[]{Integer.toString(id)}
);
		db.close();
		//再从数据库里面查询一遍,看name是否还在
		return rowcount;
	}
	public int update(int id,String newAddress, String newUser,String newPassword
			 ){
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("address", newAddress);
		values.put("user", newUser);
		values.put("password", newPassword);
		int rowcount= db.update("connect", values, "_id=?", new String[]{Integer.toString(id)});
		db.close();
		return rowcount;
	}
	public List<Server> getServerLists(){
		List<Server> serverLists=new ArrayList<Server>();
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor=db.query("connect", null, null, null, null,null,null);
		if(cursor.moveToNext()){//如果光标可以移动到下一位,代表就是查询到了数据
			Server server=new Server();
			int id=cursor.getInt(0);
			server.setId(id);
			server.setAddress(cursor.getString(1));
			server.setUser(cursor.getString(2));
			server.setPassword(cursor.getString(3));
			serverLists.add(server);
		}
		cursor.close();//关闭掉游标,释放资源
		db.close();//关闭数据库,释放资源
		return serverLists;
	}
}
