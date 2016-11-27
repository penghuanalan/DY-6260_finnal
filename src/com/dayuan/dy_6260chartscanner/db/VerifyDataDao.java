package com.dayuan.dy_6260chartscanner.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class VerifyDataDao {
	/**
	 * ���ݿ�򿪵İ�����
	 */
	private DBOpenHelper helper;
	/**
	 * �ڹ��췽��������� ����Ҫ�õ���ĳ�ʼ��
	 * @param context
	 */
	public VerifyDataDao(Context context) {
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
//		//�ǵ��ͷ����ݿ���Դ
//		db.close();
//		return rowid;
//	}
	public long add(String pointnum,String pointname,String pointtype, String orgnum,
			String orgname
			){
		SQLiteDatabase db = helper.getWritableDatabase();
		//db.execSQL("insert into contactinfo (name,phone) values (?,?)", new Object[]{name,phone});
		ContentValues values = new ContentValues();
		values.put("pointnum", pointnum);
		values.put("pointname", pointname);
		values.put("pointtype", pointtype);
		values.put("orgnum", orgnum);
		values.put("orgname", orgname);
		//�ڲ�����ƴsql���ʵ�ֵ�.
		long rowid = db.insert("verifydata", null, values);
		//�ǵ��ͷ����ݿ���Դ
		db.close();
		
		return rowid;
	}
	public long update(String pointnum,String pointname,String pointtype, String orgnum,
			String orgname
			){
		SQLiteDatabase db = helper.getWritableDatabase();
		//db.execSQL("insert into contactinfo (name,phone) values (?,?)", new Object[]{name,phone});
		ContentValues values = new ContentValues();
		values.put("pointnum", pointnum);
		values.put("pointname", pointname);
		values.put("pointtype", pointtype);
		values.put("orgnum", orgnum);
		values.put("orgname", orgname);
		//�ڲ�����ƴsql���ʵ�ֵ�.
		long rowid= db.update("verifydata",values, "pointnum=?",new String[]{pointnum});
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
		int rowcount = db.delete("verifydata", "_id=?",new String[]{Integer.toString(id)}
);
		db.close();
		//�ٴ����ݿ������ѯһ��,��name�Ƿ���
		return rowcount;
	}
	public String getPointNum(String pointnum){
		String pointNum=null;
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor  cursor = db.rawQuery("select pointnum from verifydata where pointnum=?", new String[]{pointnum});
		if(cursor.moveToNext()){//����������ƶ�����һλ,������ǲ�ѯ��������
			pointNum = cursor.getString(0);
		}
		cursor.close();//�رյ��α�,�ͷ���Դ
		db.close();//�ر����ݿ�,�ͷ���Դ
		return pointNum;
	}
}

