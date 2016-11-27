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
	 * ���ݿ�򿪵İ�����
	 */
	private TheDBOpenHelper helper;
	/**
	 * �ڹ��췽��������� ����Ҫ�õ���ĳ�ʼ��
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
		//�ڲ�����ƴsql���ʵ�ֵ�.
		long rowid = db.insert("connect", null, values);
		//�ǵ��ͷ����ݿ���Դ
		db.close();
		
		return rowid;
	}
	/**
	 * ��������ɾ��һ����¼
	 * @param name Ҫɾ������ϵ�˵�����
	 * @return ����0�������û��ɾ���κεļ�¼ ��������intֵ����ɾ���˼�������
	 */
	public int delete(int id){
		//�ж���������Ƿ����.
		SQLiteDatabase db = helper.getWritableDatabase();
		//db.execSQL("delete from contactinfo where name=?", new Object[]{name});
		int rowcount = db.delete("connect", "_id=?",new String[]{Integer.toString(id)}
);
		db.close();
		//�ٴ����ݿ������ѯһ��,��name�Ƿ���
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
		if(cursor.moveToNext()){//����������ƶ�����һλ,������ǲ�ѯ��������
			Server server=new Server();
			int id=cursor.getInt(0);
			server.setId(id);
			server.setAddress(cursor.getString(1));
			server.setUser(cursor.getString(2));
			server.setPassword(cursor.getString(3));
			serverLists.add(server);
		}
		cursor.close();//�رյ��α�,�ͷ���Դ
		db.close();//�ر����ݿ�,�ͷ���Դ
		return serverLists;
	}
}
