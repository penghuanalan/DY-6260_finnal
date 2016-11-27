package com.dayuan.dy_6260chartscanner.db;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.dayuan.dy_6260chartscanner.util.Configuration;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MyDBOpenHelper extends SQLiteOpenHelper {
	// private Context mContext;  
	public MyDBOpenHelper(Context context) {
		super(context, "dayuan.db", null, 2);
		//mContext = context; 
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		//executeAssetsSQL(db, "schema.sql"); 
		db.execSQL("create table info"
				+ " (_id integer primary key autoincrement,"
				+ " name varchar(50),method varchar(50), "
				+ "valueC varchar(50),"
				+ "valueT varchar(50),"
				+ "valueTL varchar(50),"
				+ "constant0 varchar(50),"
				+ "constant1 varchar(50),"
				+ "constant2 varchar(50),"
				+ "constant3 varchar(50),"
				+ "unit varchar(50),"
				+ "limitV varchar(50),"
				+" sampleid int(20),samplename varchar(50))"
				);
		db.execSQL("create table log "
				+ "(_id integer primary key autoincrement,"
				+ " sampleid int(20),samplename varchar(50),"
				+ "name varchar(50),result varchar(50),date datetime,type varchar(50),"
				+ "density varchar(50),data varchar(6000))");
		
//		db.execSQL("create table datamanager "
//				+ "(_id integer primary key autoincrement,"
//				+ " checknumber varchar(20),taskorigin varchar(40),"
//				+ "taskclass varchar(40),checkunit varchar(40),phone varchar(20),address varchar(40),"
//				+ "checkperson varchar(20),checkdate varchar(20),samplename varchar(20)," +
//				"name varchar(20),checkquantity varchar(20),checkaddress varchar(40),checkedunit varchar(40)," +
//				"checkedphone varchar(20),checkedaddress varchar(40),license varchar(20),sentperson varchar(20)," +
//				"sentdate varchar(20),remark varchar(200),reportnumber varchar(20),result varchar(20),checkstandard varchar(50)," +
//				"conclusion varchar(80))");
      
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		 db.execSQL("alter table info add result varchar(30)" );
		 db.execSQL("alter table info add valueone varchar(30)" );
		 db.execSQL("alter table info add valuetwo varchar(30)"  );
		 db.execSQL("alter table info add valuethree varchar(30)" );
		 //数据库不升级  
//        if (newVersion <= oldVersion) {  
//            return;  
//        }
//        Configuration.oldVersion = oldVersion;
//        int changeCnt = newVersion - oldVersion;  
//        for (int i = 0; i < changeCnt; i++) {  
//            // 依次执行updatei_i+1文件      由1更新到2 [1-2]，2更新到3 [2-3]  
//            String schemaName = "update" + (oldVersion + i) + "_"  
//                    + (oldVersion + i + 1) + ".sql";  
//            executeAssetsSQL(db, schemaName);
//        }
	}
	 /** 
     * 读取数据库文件（.sql），并执行sql语句 
     * */  
//    private void executeAssetsSQL(SQLiteDatabase db, String schemaName) {  
//        BufferedReader in = null;  
//        try {  
//            in = new BufferedReader(new InputStreamReader(mContext.getAssets()  
//                    .open(Configuration.DB_PATH + "/" + schemaName)));  
//              
//            System.out.println("路径:"+Configuration.DB_PATH + "/" + schemaName);  
//            String line;  
//            String buffer = "";  
//            while ((line = in.readLine()) != null) {  
//                buffer += line;  
//                if (line.trim().endsWith(";")) {  
//                    db.execSQL(buffer.replace(";", ""));  
//                    buffer = "";  
//                }  
//            }  
//        } catch (IOException e) {  
//            Log.e("db-error", e.toString());  
//        } finally {  
//            try {  
//                if (in != null)  
//                    in.close();  
//            } catch (IOException e) {  
//                Log.e("db-error", e.toString());  
//            }  
//        }  
//    }  
//  
}
