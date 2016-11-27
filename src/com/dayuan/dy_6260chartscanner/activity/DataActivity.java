package com.dayuan.dy_6260chartscanner.activity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.dayuan.dy_6260chartscanner.R;
import com.dayuan.dy_6260chartscanner.R.layout;
import com.dayuan.dy_6260chartscanner.util.ShowTime;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DataActivity extends BaseActivity {

	Button btnChoujian;
	Button btnTestReport;

	private TextView tvCounter;
	private boolean isRunning;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_data);

		tvCounter = (TextView) findViewById(R.id.tv_counter);
		isRunning = true;
		ShowTime.ShowTime(this, isRunning, tvCounter);
	}


	public void doClick(View v) {
		switch (v.getId()) {
		case R.id.btn_chou_jian:
			Intent it = new Intent(this, CheckNumberActivity.class);
			startActivity(it);
			break;

		case R.id.btn_test_report:
			Intent in = new Intent(this, DetectionReportActivity.class);
			startActivity(in);
			break;
		case R.id.btn_back:
//			Intent intent = new Intent(this, BaseActivity.class);
//			startActivity(intent);
			finish();
			break;
		case R.id.btn_standard:
			Intent it2=new Intent(this,CheckStandardActivity.class);
			startActivity(it2);
			break;
		}
	}
	@Override
    protected void onResume() {
    	isRunning = true;
    	ShowTime.ShowTime(this, isRunning, tvCounter);
    	super.onResume();
    }
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		isRunning=false;
	}
}
