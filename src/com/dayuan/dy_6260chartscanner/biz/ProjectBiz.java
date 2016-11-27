package com.dayuan.dy_6260chartscanner.biz;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.CancellationSignal;
import android.provider.ContactsContract.Contacts.Data;
import android.support.v4.app.FragmentActivity;

import com.dayuan.dy_6260chartscanner.db.MyDBOpenHelper;
import com.dayuan.dy_6260chartscanner.entity.Project;
import com.dayuan.dy_6260chartscanner.entity.QueryLog;
import com.dayuan.dy_6260chartscanner.entity.Sample;
import com.dayuan.dy_6260chartscanner.util.CopyDB;

public class ProjectBiz {
		
private String table="info";
 Context context;
 SQLiteDatabase db;
 public ProjectBiz(Context context){
	 super();
	 this.context=context;
//	 CopyDB copyDb=new CopyDB();
//	  db =copyDb.openDatabase(context); 
 }
	int i;
	public List<Project>getProject(int page){
		//CopyDB copyDb=new CopyDB();
		 //db =copyDb.openDatabase(context); 
		List<Project> projects=new ArrayList<Project>();
		SQLiteDatabase db=new MyDBOpenHelper(context).getReadableDatabase();
		Cursor c=db.query(table, null, null, null, null,null,null);
		i = 0;
		for(c.moveToPosition(page*12);!c.isAfterLast();c.moveToNext()){
			
			Project project=new Project(table);
			int id=c.getInt(0);
			project.setId(id);
			project.setName(c.getString(1));
			project.setMethod(c.getString(2));
			project.setValueC(c.getString(3));
   			project.setValueT(c.getString(4));
   			project.setValueTL(c.getString(5));
   			project.setConstant0(c.getString(6));
   			project.setConstant1(c.getString(7));
   			project.setConstant2(c.getString(8));
   			project.setConstant3(c.getString(9));
   			project.setUnit(c.getString(10));
   			project.setLimitV(c.getString(11));
   			project.setSampleid(c.getInt(12));
   			project.setSamplename(c.getString(13));
   			project.setResult(c.getString(14));
   			project.setValueOne(c.getString(15));
   			project.setValueTwo(c.getString(16));
   			project.setValueThree(c.getString(17));
			projects.add(project);
			i++;
			if(i==12){
				break;
			}
		}
		c.close();
		db.close();
		return projects;
	}
	public List<Project>getProjects(){
		
		List<Project> projects=new ArrayList<Project>();
		 db=new MyDBOpenHelper(context).getReadableDatabase();
		
		Cursor c=db.query(table, null, null, null, null,null,null);
		for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
			
			Project project=new Project(table);
			int id=c.getInt(0);
			project.setId(id);
			project.setName(c.getString(1));
			project.setMethod(c.getString(2));
			project.setValueC(c.getString(3));
			project.setValueT(c.getString(4));
			project.setValueTL(c.getString(5));
			project.setConstant0(c.getString(6));
			project.setConstant1(c.getString(7));
			project.setConstant2(c.getString(8));
			project.setConstant3(c.getString(9));
			project.setUnit(c.getString(10));
   			project.setLimitV(c.getString(11));
   			project.setSampleid(c.getInt(12));
   			project.setSamplename(c.getString(13));
   			project.setResult(c.getString(14));
   			project.setValueOne(c.getString(15));
   			project.setValueTwo(c.getString(16));
   			project.setValueThree(c.getString(17));
			projects.add(project);
			
		}
		
		c.close();
		db.close();
		return projects;
	}
	   int num;
		public int getProjectSize(){
						
			SQLiteDatabase db=new MyDBOpenHelper(context).getReadableDatabase();
			
			Cursor c=db.query(table, null, null, null, null,null,null);

			for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
				num++;
			}

		c.close();
		db.close();
		return num;
		}
		
}
