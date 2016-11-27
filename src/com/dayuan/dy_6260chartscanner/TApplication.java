package com.dayuan.dy_6260chartscanner;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public class TApplication extends Application{

	private ArrayList<Activity> listActivity = new ArrayList<Activity>();
	public static TApplication instance;
	private static SharedPreferences sp ;
//	private TApplication(){
//		
//	}
	public  SharedPreferences getSharedPreferencesIns() {
		if(sp==null){
			sp = getSharedPreferences("setting", Context.MODE_PRIVATE);
		}
		return sp;
	}
	public static TApplication getInstance(){
		if(null==instance){
			instance=new TApplication();
		}
		return instance;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		try {
			instance=this;
			CrashHandler crashHandler = new CrashHandler(this);
			//Thread.setDefaultUncaughtExceptionHandler(crashHandler);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void exit(){
		finishActivity();
	}

	public void addActivity(Activity activity) {
		listActivity.add(activity);
	}

	

	public void finishActivity() {
		for (Activity activity : listActivity) {
			activity.finish();
		}
		System.exit(0);
	}

	public void removeActivity(Activity activity) {
		listActivity.remove(activity);
	}
	
}
