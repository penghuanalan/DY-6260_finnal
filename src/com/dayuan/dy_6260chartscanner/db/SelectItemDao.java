package com.dayuan.dy_6260chartscanner.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SelectItemDao {
	/**
	 * ���ݿ�򿪵İ�����
	 */
	private DBOpenHelper helper;
	/**
	 * �ڹ��췽��������� ����Ҫ�õ���ĳ�ʼ��
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
//		//�ǵ��ͷ����ݿ���Դ
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
		//�ڲ�����ƴsql���ʵ�ֵ�.
		long rowid = db.insert("selectitem", null, values);
		//�ǵ��ͷ����ݿ���Դ
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
		//�ڲ�����ƴsql���ʵ�ֵ�.
		long rowid = db.insert("selectitem", null, values);
		//�ǵ��ͷ����ݿ���Դ
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
		//�ڲ�����ƴsql���ʵ�ֵ�.
		long rowid = db.update("selectitem",values, "_id=?",new String[]{String.valueOf(id)});
		//�ǵ��ͷ����ݿ���Դ
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
		//�ڲ�����ƴsql���ʵ�ֵ�.
		long rowid = db.update("selectitem",values, "samplenum=?",new String[]{samplenum});
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
		int rowcount = db.delete("selectitem", "_id=?",new String[]{Integer.toString(id)}
);
		db.close();
		//�ٴ����ݿ������ѯһ��,��name�Ƿ���
		return rowcount;
	}
	public int deleteAll(){
		//�ж���������Ƿ����.
		SQLiteDatabase db = helper.getWritableDatabase();
		//db.execSQL("delete from contactinfo where name=?", new Object[]{name});
		int countRow= db.delete("selectitem", null,null);
		db.close();
		//�ٴ����ݿ������ѯһ��,��name�Ƿ���
		return countRow;
	}
	public String getSampleNum(String samplenum){
		String sampleNum=null;
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor  cursor = db.rawQuery("select samplenum from selectitem where samplenum=?", new String[]{samplenum});
		if(cursor.moveToNext()){//����������ƶ�����һλ,������ǲ�ѯ��������
			sampleNum = cursor.getString(0);
		}
		cursor.close();//�رյ��α�,�ͷ���Դ
		db.close();//�ر����ݿ�,�ͷ���Դ
		return sampleNum;
	}
//	public String getSampleNum(String samplenum){
//		String sampleNum=null;
//		SQLiteDatabase db = helper.getReadableDatabase();
//		Cursor  cursor = db.rawQuery("select samplenum from selectitem where samplenum=?", new String[]{samplenum});
//		if(cursor.moveToNext()){//����������ƶ�����һλ,������ǲ�ѯ��������
//			sampleNum = cursor.getString(0);
//		}
//		cursor.close();//�رյ��α�,�ͷ���Դ
//		db.close();//�ر����ݿ�,�ͷ���Դ
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
//		cursor.close();//�رյ��α�,�ͷ���Դ
//		db.close();//�ر����ݿ�,�ͷ���Դ
//		return samples;
//	}
}

