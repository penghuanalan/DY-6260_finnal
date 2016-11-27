 package com.dayuan.dy_6260chartscanner.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class OrderDao {

	/**
	 * ���ݿ�򿪵İ�����
	 */
	private TheDBOpenHelper helper;
	/**
	 * �ڹ��췽��������� ����Ҫ�õ���ĳ�ʼ��
	 * @param context
	 */
	public OrderDao(Context context) {
		helper = new TheDBOpenHelper(context);
	}
	public long add(String open
			){
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("open", open);
		//�ڲ�����ƴsql���ʵ�ֵ�.
		long rowid = db.insert("autosetting", null, values);
		//�ǵ��ͷ����ݿ���Դ
		db.close();
		return rowid;
	}
	public long add02(String uploadOrder
			){
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("autoupload", uploadOrder);
		//�ڲ�����ƴsql���ʵ�ֵ�.
		long rowid = db.insert("autosetting", null, values);
		//�ǵ��ͷ����ݿ���Դ
		db.close();
		return rowid;
	}
	public long add03(String locateOrder
			){
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("locate", locateOrder);
		//�ڲ�����ƴsql���ʵ�ֵ�.
		long rowid = db.insert("autosetting", null, values);
		//�ǵ��ͷ����ݿ���Դ
		System.out.println("���ݿ�洢������");
		db.close();
		return rowid;
	}
	public long add04(String outprint
			){
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("outprint", outprint);
		//�ڲ�����ƴsql���ʵ�ֵ�.
		long rowid = db.insert("autosetting", null, values);
		//�ǵ��ͷ����ݿ���Դ
		db.close();
		return rowid;
	}
	/**
	 * ��������ɾ��һ����¼
	 * @param name Ҫɾ������ϵ�˵�����
	 * @return ����0�������û��ɾ���κεļ�¼ ��������intֵ����ɾ���˼�������
	 */
//	public int delete(int id){
//		//�ж���������Ƿ����.
//		SQLiteDatabase db = helper.getWritableDatabase();
//		//db.execSQL("delete from contactinfo where name=?", new Object[]{name});
//		int rowcount = db.delete("autosetting", "_id=?",new String[]{Integer.toString(id)}
//);
//		db.close();
//		//�ٴ����ݿ������ѯһ��,��name�Ƿ���
//		return rowcount;
//	}
	 public void delete(String openOrder) {  
	        SQLiteDatabase db = helper.getWritableDatabase();  
	        db.execSQL("delete from autosetting where open=?", new Object[] { openOrder });  
	        db.close();  
	    }  
	 public void delete02(String uploadOrder) {  
		 SQLiteDatabase db = helper.getWritableDatabase();  
		 db.execSQL("delete from autosetting where autoupload=?", new Object[] { uploadOrder });  
		 db.close();  
	 }  
	 public void delete03(String locateOrder) {  
		 SQLiteDatabase db = helper.getWritableDatabase();  
		 db.execSQL("delete from autosetting where locate=?", new Object[] { locateOrder });  
		 db.close();  
	 }  
	 public void delete04(String outprint) {  
		 SQLiteDatabase db = helper.getWritableDatabase();  
		 db.execSQL("delete from autosetting where outprint=?", new Object[] { outprint });  
		 db.close();  
	 }  
	public int update(String openOrder){
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("open",openOrder);
		int rowcount= db.update("autosetting", values, "open=?", new String[]{openOrder});
		db.close();
		return rowcount;	
	}
	public String getOrder(String openOrder){
		String order=null;
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor  cursor = db.rawQuery("select open from autosetting where open=?", new String[]{openOrder});
		if(cursor.moveToNext()){//����������ƶ�����һλ,������ǲ�ѯ��������
			order = cursor.getString(0);
		}
		cursor.close();//�رյ��α�,�ͷ���Դ
		db.close();//�ر����ݿ�,�ͷ���Դ
		return order;
	}
	public String getUploadOrder(String uploadOrder){
		String uploadorder=null;
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor  cursor = db.rawQuery("select autoupload from autosetting where autoupload=?", new String[]{uploadOrder});
		if(cursor.moveToNext()){//����������ƶ�����һλ,������ǲ�ѯ��������
			uploadorder = cursor.getString(0);
		}
		cursor.close();//�رյ��α�,�ͷ���Դ
		db.close();//�ر����ݿ�,�ͷ���Դ
		return uploadorder;
	}
	public String getLocateOrder(String locateOrder){
		String locateorder=null;
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor  cursor = db.rawQuery("select locate from autosetting where locate=?", new String[]{locateOrder});
		if(cursor.moveToNext()){//����������ƶ�����һλ,������ǲ�ѯ��������
			locateorder = cursor.getString(0);
		}
		cursor.close();//�رյ��α�,�ͷ���Դ
		db.close();//�ر����ݿ�,�ͷ���Դ
		return locateorder;
	}
	
	public String getOutPrint(String outPrint){
		String outprint=null;
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor  cursor = db.rawQuery("select outprint from autosetting where outprint=?", new String[]{outPrint});
		if(cursor.moveToNext()){//����������ƶ�����һλ,������ǲ�ѯ��������
			outprint = cursor.getString(0);
		}
		cursor.close();//�رյ��α�,�ͷ���Դ
		db.close();//�ر����ݿ�,�ͷ���Դ
		return outprint;
	}
}
