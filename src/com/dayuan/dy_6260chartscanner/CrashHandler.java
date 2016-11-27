package com.dayuan.dy_6260chartscanner;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;

import com.dayuan.dy_6260chartscanner.activity.LoginActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.widget.Toast;

public class CrashHandler implements UncaughtExceptionHandler{
	TApplication tApplication;
	public CrashHandler(TApplication tApplication) {
		super();
		this.tApplication = tApplication;
	}
		@Override
		public void uncaughtException(Thread thread, Throwable ex) {
			// 得到异常的详细信息
			//得到的信息是null,态少了
			//final String string = ex.getMessage();

			StringWriter stringWriter=new StringWriter();
			PrintWriter printWriter=new PrintWriter(stringWriter);
			ex.printStackTrace(printWriter);
			final String string=stringWriter.toString();
			// 发给服务器
			//Log.i("", string);
			new Thread(){public void run() {
				Looper.prepare();
				Toast.makeText(tApplication, "很抱歉，程序即将重启", Toast.LENGTH_LONG).show();
				Looper.loop();
			};}.start();
			
			try {
				Thread.currentThread().sleep(5000);
			} catch (Exception e) {
				// TODO: handle exception
			}
			//自动重启Activity
			Intent intent=new Intent(tApplication, LoginActivity.class);
			
			PendingIntent pendingIntent=PendingIntent.getActivity(tApplication, 100, intent, 
					Intent.FLAG_ACTIVITY_NEW_TASK);
			
			//定时器
			AlarmManager alarmManager=
					(AlarmManager) tApplication.getSystemService(Context.ALARM_SERVICE);
			alarmManager.set(AlarmManager.RTC, System.currentTimeMillis()+2000, pendingIntent);
			
			tApplication.finishActivity();
	}

}
