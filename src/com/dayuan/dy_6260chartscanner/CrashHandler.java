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
			// �õ��쳣����ϸ��Ϣ
			//�õ�����Ϣ��null,̬����
			//final String string = ex.getMessage();

			StringWriter stringWriter=new StringWriter();
			PrintWriter printWriter=new PrintWriter(stringWriter);
			ex.printStackTrace(printWriter);
			final String string=stringWriter.toString();
			// ����������
			//Log.i("", string);
			new Thread(){public void run() {
				Looper.prepare();
				Toast.makeText(tApplication, "�ܱ�Ǹ�����򼴽�����", Toast.LENGTH_LONG).show();
				Looper.loop();
			};}.start();
			
			try {
				Thread.currentThread().sleep(5000);
			} catch (Exception e) {
				// TODO: handle exception
			}
			//�Զ�����Activity
			Intent intent=new Intent(tApplication, LoginActivity.class);
			
			PendingIntent pendingIntent=PendingIntent.getActivity(tApplication, 100, intent, 
					Intent.FLAG_ACTIVITY_NEW_TASK);
			
			//��ʱ��
			AlarmManager alarmManager=
					(AlarmManager) tApplication.getSystemService(Context.ALARM_SERVICE);
			alarmManager.set(AlarmManager.RTC, System.currentTimeMillis()+2000, pendingIntent);
			
			tApplication.finishActivity();
	}

}
