package com.dayuan.dy_6260chartscanner.biz;

import java.util.ArrayList;
import java.util.List;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dayuan.dy_6260chartscanner.db.DBOpenHelper;
import com.dayuan.dy_6260chartscanner.db.MyDBOpenHelper;
import com.dayuan.dy_6260chartscanner.entity.CheckNumber;
import com.dayuan.dy_6260chartscanner.entity.FoodClass;
import com.dayuan.dy_6260chartscanner.entity.QueryLog;
import com.dayuan.dy_6260chartscanner.entity.TaskClass;
import com.dayuan.dy_6260chartscanner.entity.VerifyData;

public class TaskClassBiz {
	
	private String table="taskclass";
	 Context context;
	public TaskClassBiz(Context context) {
		this.context=context;
	}
	public List<TaskClass>getTaskClass(){
		List<TaskClass> tasks=new ArrayList<TaskClass>();
		SQLiteDatabase db=new DBOpenHelper(context).getReadableDatabase();
		Cursor c=db.query(table, null, null, null, null,null,null);
		for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
			TaskClass task=new TaskClass();
			int id=c.getInt(0);
			task.setId(id);
			task.setcheckNumber(c.getString(1));
			task.setCptproperty(c.getString(5));
			task.setCpfrom(c.getString(6));
			task.setcheckUnit(c.getString(9));
			task.seteditTime(c.getString(10));
			task.setCpmemo(c.getString(11));
			task.setPlandetail(c.getString(12));
			task.setPlandcount(c.getInt(13));
			task.setReportNumber(c.getString(16));
			task.setResult(c.getString(17));
			tasks.add(task);
		}
		c.close();
		db.close();
		return tasks;
	}
}
