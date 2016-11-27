package com.dayuan.dy_6260chartscanner.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TheDBOpenHelper extends SQLiteOpenHelper {

	public TheDBOpenHelper(Context context) {
		super(context, "dayuan03.db", null, 2);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table reportnumber"
				+ " (_id integer primary key autoincrement,"
				+"number int(20))"
				);
		db.execSQL("create table autosetting"
				+ " (_id integer primary key autoincrement,"
				+"open varchar(20),autoupload varchar(20),locate varchar(20),outprint varchar(20))"
				);
		db.execSQL("create table connect "
				+ "(_id integer primary key autoincrement,"
				+ "address varchar(60),"
				+ "user varchar(30),password varchar(30))");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		 //db.execSQL("alter table info add samplename varchar(50)" );

	}

}
