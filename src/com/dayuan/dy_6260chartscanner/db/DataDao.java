 package com.dayuan.dy_6260chartscanner.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DataDao {

	/**
	 * ���ݿ�򿪵İ�����
	 */
	private MyDBOpenHelper helper;
	/**
	 * �ڹ��췽��������� ����Ҫ�õ���ĳ�ʼ��
	 * @param context
	 */
	public DataDao(Context context) {
		helper = new MyDBOpenHelper(context);
	}
	public long update(int id,String samplename){
		SQLiteDatabase db = helper.getWritableDatabase();
		//db.execSQL("insert into contactinfo (name,phone) values (?,?)", new Object[]{name,phone});
		ContentValues values = new ContentValues();
		values.put("samplename",samplename);
//		values.put("checkedunit",checkedunit);
//		values.put("license", license);
		long rowid= db.update("datamanager",values, "_id=?",new String[]{Integer.toString(id)});
		//�ǵ��ͷ����ݿ���Դ
		db.close();
		return rowid;
	}
	public int update(int id,String checkedunit,String license){
		SQLiteDatabase db = helper.getWritableDatabase();
		//db.execSQL("insert into contactinfo (name,phone) values (?,?)", new Object[]{name,phone});
		ContentValues values = new ContentValues();
		values.put("checkedunit",checkedunit);
		values.put("license", license);
		int rowid = db.update("datamanager",values, "_id=?",new String[]{Integer.toString(id)});
		//�ǵ��ͷ����ݿ���Դ
		db.close();
		return rowid;
	}
	
	public long add(String checknumber,String taskorigin, String taskclass,
			String checkunit,String phone,String address,String checkperson,String checkdate,
			String samplename,String name,String checkquantity,String checkaddress,String checkedunit,
			String checkedphone,String checkedaddress,String license,String sentperson,
			String sentdate,String remark){
		SQLiteDatabase db = helper.getWritableDatabase();
		//db.execSQL("insert into contactinfo (name,phone) values (?,?)", new Object[]{name,phone});
		ContentValues values = new ContentValues();
		values.put("checknumber", checknumber);
		values.put("taskorigin", taskorigin);
		values.put("taskclass", taskclass);
		values.put("checkunit", checkunit);
		values.put("phone", phone);
		values.put("address", address);
		values.put("checkperson", checkperson);
		values.put("checkdate",checkdate);
		values.put("samplename",samplename);
		values.put("name",name);
		values.put("checkquantity",checkquantity);
		values.put("checkaddress",checkaddress);
		values.put("checkedunit",checkedunit);
		values.put("checkedphone",checkedphone);
		values.put("checkedaddress",checkedaddress);
		values.put("license", license);
		values.put("sentperson",sentperson);
		values.put("sentdate", sentdate);
		values.put("remark", remark);
		//�ڲ�����ƴsql���ʵ�ֵ�.
		long rowid = db.insert("datamanager", null, values);
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
		int rowcount = db.delete("datamanager", "_id=?",new String[]{Integer.toString(id)}
);
		db.close();
		//�ٴ����ݿ������ѯһ��,��name�Ƿ���
		return rowcount;
	}
	public int update(int id,String newsamplename,String name,String result){
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("samplename", newsamplename);
		values.put("name",name);
		values.put("result",result);
		int rowcount= db.update("datamanager",values, "_id=?",new String[]{Integer.toString(id)});
		db.close();
		return rowcount;	
	}
//	public int update(int id,String newname,String newmethod,String newvalueC,
//		    String newvalueT,String newvalueTL,String  newconstant0,
//			String newconstant1,String newconstant2,String newconstant3
//			,String newunit,String newlimitV){
//		SQLiteDatabase db = helper.getWritableDatabase();
//		ContentValues values = new ContentValues();
//		values.put("name", newname);
//		values.put("method", newmethod);
//		values.put("valueC", newvalueC);
//		values.put("valueT", newvalueT);
//		values.put("valueTL", newvalueTL);
//		values.put("constant0", newconstant0);
//		values.put("constant1", newconstant1);
//		values.put("constant2", newconstant2);
//		values.put("constant3", newconstant3);
//		values.put("unit", newunit);
//		values.put("limitV", newlimitV);
//		int rowcount= db.update("info", values, "_id=?", new String[]{Integer.toString(id)});
//		db.close();
//		return rowcount;
//	}
}
