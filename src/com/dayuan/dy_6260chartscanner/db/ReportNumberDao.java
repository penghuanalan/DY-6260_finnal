 package com.dayuan.dy_6260chartscanner.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ReportNumberDao {

	/**
	 * ���ݿ�򿪵İ�����
	 */
	private TheDBOpenHelper helper;
	/**
	 * �ڹ��췽��������� ����Ҫ�õ���ĳ�ʼ��
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
		//�ڲ�����ƴsql���ʵ�ֵ�.
		long rowid = db.insert("reportnumber", null, values);
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
		int rowcount = db.delete("reportnumber", "_id=?",new String[]{Integer.toString(id)}
);
		db.close();
		//�ٴ����ݿ������ѯһ��,��name�Ƿ���
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
		if(cursor.moveToNext()){//����������ƶ�����һλ,������ǲ�ѯ��������
			reportNumber = cursor.getInt(1);
		}
		cursor.close();//�رյ��α�,�ͷ���Դ
		db.close();//�ر����ݿ�,�ͷ���Դ
		return reportNumber;
	}
}
