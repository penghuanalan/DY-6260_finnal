package com.dayuan.dy_6260chartscanner.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CompanyDao {
	/**
	 * ���ݿ�򿪵İ�����
	 */
	private DBOpenHelper helper;
	/**
	 * �ڹ��췽��������� ����Ҫ�õ���ĳ�ʼ��
	 * @param context
	 */
	public CompanyDao(Context context) {
		helper = new DBOpenHelper(context);
	}
	
	public long add(String syscode, String stdcode,String unitname,String address,String linkinfo
			){
		SQLiteDatabase db = helper.getWritableDatabase();
		//db.execSQL("insert into contactinfo (name,phone) values (?,?)", new Object[]{name,phone});
		ContentValues values = new ContentValues();
		values.put("syscode", syscode);
		values.put("stdcode", stdcode);
		values.put("unitname",unitname);
		values.put("address",address);
		values.put("linkinfo",linkinfo);
		//�ڲ�����ƴsql���ʵ�ֵ�.
		long rowid = db.insert("company", null, values);
		//�ǵ��ͷ����ݿ���Դ
		db.close();
		
		return rowid;
	}
	public long update(int id,String samplename){
		SQLiteDatabase db = helper.getWritableDatabase();
		//db.execSQL("insert into contactinfo (name,phone) values (?,?)", new Object[]{name,phone});
		ContentValues values = new ContentValues();
		values.put("samplename",samplename);
//		values.put("checkedunit",checkedunit);
//		values.put("license", license);
		long rowid= db.update("company",values, "_id=?",new String[]{Integer.toString(id)});
		//�ǵ��ͷ����ݿ���Դ
		db.close();
		return rowid;
	}
	public long add(String syscode,String stdcode, String companyid,String unitname,
			String displayname,String property,String kindcode,String regcapital,String capitalunit,
			String incorporator,String regdate,String districtcode,String postcode,String address,
			String linkman,String linkinfo,String creditlevel,String creditrecord,String productinfo,
			String otherInfo,String checklevel,String foodsaferecord,int number,int num,
			String date,String remark,String sign
			){
		SQLiteDatabase db = helper.getWritableDatabase();
		//db.execSQL("insert into contactinfo (name,phone) values (?,?)", new Object[]{name,phone});
		ContentValues values = new ContentValues();
		values.put("syscode", syscode);
		values.put("stdcode", stdcode);
		values.put("companyid", companyid);
		values.put("unitname",unitname);
		values.put("displayname", displayname);
		values.put("property", property);
		values.put("kindcode", kindcode);
		values.put("regcapital",regcapital);
		values.put("capitalunit",capitalunit);
		values.put("incorporator",incorporator);
		values.put("regdate", regdate);
		values.put("districtcode",districtcode);
		values.put("postcode",postcode);
		values.put("address",address);
		values.put("linkman",linkman);
		values.put("linkinfo",linkinfo);
		values.put("creditlevel",creditlevel);
		values.put("creditrecord",creditrecord);
		values.put("productinfo",productinfo);
		values.put("otherInfo",otherInfo);
		values.put("checklevel",checklevel);
		values.put("foodsaferecord",foodsaferecord);
		values.put("number",number);
		values.put("num",num);
		values.put("date",date);
		values.put("remark",remark);
		values.put("sign",sign);
		//�ڲ�����ƴsql���ʵ�ֵ�.
		long rowid = db.insert("company", null, values);
		//�ǵ��ͷ����ݿ���Դ
		db.close();
		
		return rowid;
	}
	public long update(String syscode,String stdcode, String companyid,String unitname,
			String displayname,String property,String kindcode,String regcapital,String capitalunit,
			String incorporator,String regdate,String districtcode,String postcode,String address,
			String linkman,String linkinfo,String creditlevel,String creditrecord,String productinfo,
			String otherInfo,String checklevel,String foodsaferecord,int number,int num,
			String date,String remark,String sign
			){
		SQLiteDatabase db = helper.getWritableDatabase();
		//db.execSQL("insert into contactinfo (name,phone) values (?,?)", new Object[]{name,phone});
		ContentValues values = new ContentValues();
		values.put("syscode", syscode);
		values.put("stdcode", stdcode);
		values.put("companyid", companyid);
		values.put("unitname",unitname);
		values.put("displayname", displayname);
		values.put("property", property);
		values.put("kindcode", kindcode);
		values.put("regcapital",regcapital);
		values.put("capitalunit",capitalunit);
		values.put("incorporator",incorporator);
		values.put("regdate", regdate);
		values.put("districtcode",districtcode);
		values.put("postcode",postcode);
		values.put("address",address);
		values.put("linkman",linkman);
		values.put("linkinfo",linkinfo);
		values.put("creditlevel",creditlevel);
		values.put("creditrecord",creditrecord);
		values.put("productinfo",productinfo);
		values.put("otherInfo",otherInfo);
		values.put("checklevel",checklevel);
		values.put("foodsaferecord",foodsaferecord);
		values.put("number",number);
		values.put("num",num);
		values.put("date",date);
		values.put("remark",remark);
		values.put("sign",sign);
		//�ڲ�����ƴsql���ʵ�ֵ�.
		long rowid= db.update("company",values, "syscode=?",new String[]{syscode});
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
		int rowcount = db.delete("company", "_id=?",new String[]{Integer.toString(id)}
);
		db.close();
		//�ٴ����ݿ������ѯһ��,��name�Ƿ���
		return rowcount;
	}
	public String getSysCode(String syscode){
		String sysCode=null;
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor  cursor = db.rawQuery("select syscode from company where syscode=?", new String[]{syscode});
		if(cursor.moveToNext()){//����������ƶ�����һλ,������ǲ�ѯ��������
			sysCode = cursor.getString(0);
		}
		cursor.close();//�رյ��α�,�ͷ���Դ
		db.close();//�ر����ݿ�,�ͷ���Դ
		return sysCode;
	}
}

