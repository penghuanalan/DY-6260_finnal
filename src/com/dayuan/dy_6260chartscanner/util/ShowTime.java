package com.dayuan.dy_6260chartscanner.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.os.Handler;
import android.widget.TextView;

public class ShowTime {
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss", Locale.CHINA);
	private static Date date = new Date();
	protected static final int MESSAGE_UPDATE_TIME = 10;
	private static TextView mTvCounter;
	private static Handler handler = new Handler(){ 
		public void handleMessage(android.os.Message msg) {
			
			date.setTime(System.currentTimeMillis());
			mTvCounter.setText(sdf.format(date));
		};
  };
	public static void ShowTime(final Context context,final Boolean isRunning,final TextView tvCounter){
		mTvCounter=tvCounter;
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(isRunning){
					try {
						handler.sendEmptyMessage(MESSAGE_UPDATE_TIME);
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
			}
		}).start();
	}
}
