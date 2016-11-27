package com.dayuan.dy_6260chartscanner.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ReportDataDao {
	/**
	 * ���ݿ�򿪵İ�����
	 */
	private  DBOpenHelper helper;
	/**
	 * �ڹ��췽��������� ����Ҫ�õ���ĳ�ʼ��
	 * @param context
	 */
	public ReportDataDao(Context context) {
		helper = new DBOpenHelper(context);
	}

	public  long update(String CPCODE, String reportNumber,String result
			
			){
		SQLiteDatabase db = helper.getWritableDatabase();
		//db.execSQL("insert into contactinfo (name,phone) values (?,?)", new Object[]{name,phone});
		ContentValues values = new ContentValues();
		values.put("reportnumber", reportNumber);
		values.put("result", result);
		//�ڲ�����ƴsql���ʵ�ֵ�.
		long rowid = db.update("reportdata",values,"cpcode=?",new String[]{CPCODE});
		//�ǵ��ͷ����ݿ���Դ
		db.close();
		
		return rowid;
	}
	public long add(String CPCODE,String CPSDATE,
			String CPTPROPERTY,String CPFROM,
			String CPPORG,String CPEDDATE,
			String PLANDETAIL,int PLANDCOUNT
			){
		SQLiteDatabase db = helper.getWritableDatabase();
		//db.execSQL("insert into contactinfo (name,phone) values (?,?)", new Object[]{name,phone});
		ContentValues values = new ContentValues();
		values.put("cpcode", CPCODE);//������(�����)
		values.put("cpsdate", CPSDATE);
		values.put("cptproperty", CPTPROPERTY);//��������
		values.put("cpfrom", CPFROM);//������Դ
		values.put("cpporg",CPPORG);//��ⵥλ
		values.put("cpeddate",CPEDDATE);//��������,�������
		values.put("plandetail",PLANDETAIL);
		values.put("plandcount",PLANDCOUNT);
		//�ڲ�����ƴsql���ʵ�ֵ�.
		long rowid = db.insert("reportdata", null, values);
		//�ǵ��ͷ����ݿ���Դ
		db.close();
		
		return rowid;
	}
	public int deleteAll(){
		//�ж���������Ƿ����.
		SQLiteDatabase db = helper.getWritableDatabase();
		//db.execSQL("delete from contactinfo where name=?", new Object[]{name});
		int countRow= db.delete("reportdata", null,null);
		db.close();
		//�ٴ����ݿ������ѯһ��,��name�Ƿ���
		return countRow;
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
		int rowcount = db.delete("reportdata", "_id=?",new String[]{Integer.toString(id)}
);
		db.close();
		//�ٴ����ݿ������ѯһ��,��name�Ƿ���
		return rowcount;
	}
//	public String getCheckNumber(String CPCODE){
//		String checkNumber=null;
//		SQLiteDatabase db = helper.getReadableDatabase();
//		Cursor  cursor = db.rawQuery("select cpcode from taskclass where cpcode=?", new String[]{CPCODE});
//		if(cursor.moveToNext()){//����������ƶ�����һλ,������ǲ�ѯ��������
//			checkNumber = cursor.getString(0);
//		}
//		cursor.close();//�رյ��α�,�ͷ���Դ
//		db.close();//�ر����ݿ�,�ͷ���Դ
//		return checkNumber;
//	}
}

