package com.dayuan.dy_6260chartscanner.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CompanyDao {
	/**
	 * 数据库打开的帮助类
	 */
	private DBOpenHelper helper;
	/**
	 * 在构造方法里面完成 必须要用的类的初始化
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
		//内部是组拼sql语句实现的.
		long rowid = db.insert("company", null, values);
		//记得释放数据库资源
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
		//记得释放数据库资源
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
		//内部是组拼sql语句实现的.
		long rowid = db.insert("company", null, values);
		//记得释放数据库资源
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
		//内部是组拼sql语句实现的.
		long rowid= db.update("company",values, "syscode=?",new String[]{syscode});
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
		int rowcount = db.delete("company", "_id=?",new String[]{Integer.toString(id)}
);
		db.close();
		//再从数据库里面查询一遍,看name是否还在
		return rowcount;
	}
	public String getSysCode(String syscode){
		String sysCode=null;
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor  cursor = db.rawQuery("select syscode from company where syscode=?", new String[]{syscode});
		if(cursor.moveToNext()){//如果光标可以移动到下一位,代表就是查询到了数据
			sysCode = cursor.getString(0);
		}
		cursor.close();//关闭掉游标,释放资源
		db.close();//关闭数据库,释放资源
		return sysCode;
	}
}

