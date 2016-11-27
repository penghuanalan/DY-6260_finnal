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

import com.dayuan.dy_6260chartscanner.db.DBOpenHelper;
import com.dayuan.dy_6260chartscanner.db.MyDBOpenHelper;
import com.dayuan.dy_6260chartscanner.entity.Project;
import com.dayuan.dy_6260chartscanner.entity.QueryLog;
import com.dayuan.dy_6260chartscanner.entity.Sample;
import com.dayuan.dy_6260chartscanner.entity.SelectItem;

public class SampleBiz {
	
private String table="info";
 Context context;
 public SampleBiz(Context context){
	 super();
	 this.context=context;
 }
	public List<Sample>getSample(){
			
		
		List<Sample> samples=new ArrayList<Sample>();
		SQLiteDatabase db=new MyDBOpenHelper(context).getReadableDatabase();
		
		String[] projection={"_id","name"
				};
		Cursor c=db.query(table, projection, null, null, null,null,null);
		while(c.moveToNext()){
			Sample sample=new Sample(table);
			sample.setName(c.getString(1));
			samples.add(sample);
		}
		c.close();
		db.close();
	
		return samples;
	}
//	public List<Project>getProjectTwo(){
//		
//		List<Project> projects=new ArrayList<Project>();
//		SQLiteDatabase db=new MyDBOpenHelper(context).getReadableDatabase();
//		
//		String[] projection={"_id","name"
//		};
//		Cursor c=db.query(table, projection, null, null, null,null,null);
//		
//		for(c.moveToPosition(12);c.getPosition()!=23;c.moveToNext()){
//			Project project=new Project(table);
//			project.setName(c.getString(1));
//			projects.add(project);
//		}	
//		
//		c.close();
//		db.close();
//		return projects;
//	}
//	public List<Project>getProjectThree(){
//		
//		List<Project> projects=new ArrayList<Project>();
//		SQLiteDatabase db=new MyDBOpenHelper(context).getReadableDatabase();
//		
//		String[] projection={"_id","name"
//		};
//		Cursor c=db.query(table, projection, null, null, null,null,null);
//		
//		for(c.moveToPosition(23);c.getPosition()!=35;c.moveToNext()){
//			Project project=new Project(table);
//			project.setName(c.getString(1));
//			projects.add(project);
//		}	
//		
//		c.close();
//		db.close();
//		return projects;
//	}
//       public List<Sample>getSample(String projectName){
//		
//		List<Sample> samples=new ArrayList<Sample>();
//		SQLiteDatabase db=new MyDBOpenHelper(context).getReadableDatabase();
//		
//		Cursor c=db.query(table, null,  "name like '%"+projectName+"%'", 
//				null, null,null,null);
//		while(c.moveToNext()){
//			Sample sample=new Sample(table);
//			sample.setName(c.getString(1));
//			samples.add(sample);
//		}
//		c.close();
//		db.close();
//	
//		return samples;
//	}
       
}
