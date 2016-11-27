package com.dayuan.dy_6260chartscanner.activity;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.dayuan.dy_6260chartscanner.R;
import com.dayuan.dy_6260chartscanner.TApplication;
import com.dayuan.dy_6260chartscanner.biz.OpenDeviceBiz;
import com.dayuan.dy_6260chartscanner.biz.ProjectBiz;
import com.dayuan.dy_6260chartscanner.entity.DeviceEntry;
import com.dayuan.dy_6260chartscanner.util.CmdUtil;
import com.dayuan.dy_6260chartscanner.util.CopyDB;
import com.dayuan.dy_6260chartscanner.util.CopyDataBase;
import com.dayuan.dy_6260chartscanner.util.ShowTime;
import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;
import com.hoho.android.usbserial.util.HexDump;
import com.hoho.android.usbserial.util.SerialInputOutputManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BaseActivity extends Activity implements View.OnClickListener{
	private TextView tvCounter;
	private boolean isRunning;
	private Button btnSampleTest;
	private Button btnExit;
	private Button btnQueryLog;
	private Button btnProjectSetting;
	private Button btnDataManagement;
	private Button btnSystemSetting;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu);
		 try {
				CopyDB.copyDBToDatabases(this);
				CopyDB.copyDBToDatabases02(this);
				CopyDataBase.copyDBToDatabases(this);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		try {
			TApplication.instance.addActivity(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		tvCounter = (TextView) findViewById(R.id.tv_Counter);
		btnSampleTest=(Button) findViewById(R.id.btn_sample_test);
		btnExit=(Button) findViewById(R.id.btn_exit_system);
		btnQueryLog=(Button) findViewById(R.id.btn_query_log);
		btnProjectSetting=(Button) findViewById(R.id.btn_project_setting);
		btnDataManagement=(Button) findViewById(R.id.btn_data_management);
		btnSystemSetting=(Button) findViewById(R.id.btn_system_setting);
		
		btnSampleTest.setOnClickListener(this);
		btnExit.setOnClickListener(this);
		btnQueryLog.setOnClickListener(this);
		btnProjectSetting.setOnClickListener(this);
		btnDataManagement.setOnClickListener(this);
		btnSystemSetting.setOnClickListener(this);
		
		isRunning = true;
		ShowTime.ShowTime(this, isRunning, tvCounter);
	}
	@Override
	protected void onRestart() {
		isRunning = true;
    	ShowTime.ShowTime(this, isRunning, tvCounter);
		super.onRestart();
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		isRunning=false;
		super.onDestroy();
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
		case R.id.btn_sample_test:
			Intent it=new Intent(BaseActivity.this,SampleTestActivity.class);
			startActivity(it);
			break;
		
		case R.id.btn_exit_system:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("提示");
			builder.setMessage("确定要退出登录吗?");
			builder.setPositiveButton("确定", new Dialog.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) { 
					// TODO Auto-generated method stub
					TApplication.instance.finishActivity();
				}
			});
			builder.setNegativeButton("取消", null).create().show();
			break;
		case R.id.btn_query_log:
			Intent in=new Intent(BaseActivity.this,QueryLogActivity.class);
			startActivity(in);
			break;
		case R.id.btn_project_setting:
			Intent it2=new Intent(this,ProjectSettingActivity.class);
			startActivity(it2);
			break;
		case R.id.btn_data_management:
			Intent it3=new Intent(this, DataActivity.class);
			startActivity(it3);
			break;
		case R.id.btn_system_setting:
			Intent intent=new Intent(this,SystemSettingActivity.class);
			startActivity(intent);
			break;
		}
		
	}
	
	
//	 @Override  
//	    public boolean onKeyDown(int keyCode, KeyEvent event)  
//	    {  
//	        if (keyCode == KeyEvent.KEYCODE_BACK )  
//	        {  
//	            // 创建退出对话框  
//	            AlertDialog isExit = new AlertDialog.Builder(this).create();  
//	            // 设置对话框标题  
//	            isExit.setTitle("系统提示");  
//	            // 设置对话框消息  
//	            isExit.setMessage("确定要退出吗？");  
//	            // 添加选择按钮并注册监听  
//	            isExit.setButton("确定", listener);  
//	            isExit.setButton2("取消", listener);  
//	            // 显示对话框  
//	            isExit.show();  
//	  
//	        }  
//	          
//	        return false;  
//	          
//	    }  
	  DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener()  
	    {  
	        public void onClick(DialogInterface dialog, int which)  
	        {  
	            switch (which)  
	            {  
	            case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序  
	                finish();  
	                break;  
	            case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框  
	                break;  
	            default:  
	                break;  
	            }  
	        }  
	    };    
	
}
