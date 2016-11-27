package com.dayuan.dy_6260chartscanner.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class FoodClassDao {
	/**
	 * ���ݿ�򿪵İ�����
	 */
	private DBOpenHelper helper;
	/**
	 * �ڹ��췽��������� ����Ҫ�õ���ĳ�ʼ��
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
		//�ڲ�����ƴsql���ʵ�ֵ�.
		long rowid = db.insert("foodclass", null, values);
		//�ǵ��ͷ����ݿ���Դ
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
		//�ǵ��ͷ����ݿ���Դ
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
		//�ڲ�����ƴsql���ʵ�ֵ�.
		long rowid = db.insert("foodclass", null, values);
		//�ǵ��ͷ����ݿ���Դ
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
		//�ڲ�����ƴsql���ʵ�ֵ�.
		long rowid= db.update("foodclass",values, "syscode=?",new String[]{syscode});
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
		int rowcount = db.delete("foodclass", "_id=?",new String[]{Integer.toString(id)}
);
		db.close();
		//�ٴ����ݿ������ѯһ��,��name�Ƿ���
		return rowcount;
	}
	public String getSysCode(String syscode){
		String sysCode=null;
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor  cursor = db.rawQuery("select syscode from foodclass where syscode=?", new String[]{syscode});
		if(cursor.moveToNext()){//����������ƶ�����һλ,������ǲ�ѯ��������
			sysCode = cursor.getString(0);
		}
		cursor.close();//�رյ��α�,�ͷ���Դ
		db.close();//�ر����ݿ�,�ͷ���Դ
		return sysCode;
	}
}

