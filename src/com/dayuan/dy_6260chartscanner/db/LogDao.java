 package com.dayuan.dy_6260chartscanner.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class LogDao {

	/**
	 * ���ݿ�򿪵İ�����
	 */
	private MyDBOpenHelper helper;
	/**
	 * �ڹ��췽��������� ����Ҫ�õ���ĳ�ʼ��
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
		//�ڲ�����ƴsql���ʵ�ֵ�.
		long rowid = db.insert("log", null, values);
		//�ǵ��ͷ����ݿ���Դ
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
		//�ڲ�����ƴsql���ʵ�ֵ�.
		long rowid = db.insert("log", null, values);
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
		int rowcount = db.delete("log", "_id=?",new String[]{Integer.toString(id)}
);
		db.close();
		//�ٴ����ݿ������ѯһ��,��name�Ƿ���
		return rowcount;
	}
	public int deleteAll(){
		//�ж���������Ƿ����.
		SQLiteDatabase db = helper.getWritableDatabase();
		//db.execSQL("delete from contactinfo where name=?", new Object[]{name});
		int countRow= db.delete("log", null,null);
		db.close();
		//�ٴ����ݿ������ѯһ��,��name�Ƿ���
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
