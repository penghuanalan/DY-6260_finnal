package com.dayuan.dy_6260chartscanner.activity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.dayuan.dy_6260chartscanner.R;
import com.dayuan.dy_6260chartscanner.R.layout;
import com.dayuan.dy_6260chartscanner.biz.TaskClassBiz;
import com.dayuan.dy_6260chartscanner.entity.TaskClass;
import com.dayuan.dy_6260chartscanner.util.ShowTime;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.TextView;

public class CheckNumberItemActivity extends Activity {
    private TextView tvTaskOrigin,tvTaskClass,tvCheckUnit,tvPhone,tvAddress,tvCheckNum,
    tvCheckPerson,tvCheckDate,tvSampleName,tvProjectName,tvCheckQuantity,tvCheckAddress,
    tvCheckedUnit,tvCheckedPhone,tvCheckedAddress,tvLicense,tvSentPerson,tvSentDate,
    tvRemark;
    private Handler handler;
    private TextView tvCounter;
	private boolean isRunning;
	private List<TaskClass> taskclasses;
	private int position;
	private String checkNumber;
	private String taskProperty;
	private String taskFrom;
	private String checkUnit;
	private String editTime;
	private String remark;
	private int checkCount;
	String samplename;
	String projectname;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_check_number_item);
		taskclasses=new TaskClassBiz(this).getTaskClass();
		initViews();
		
		isRunning = true;
		ShowTime.ShowTime(this, isRunning, tvCounter);
	}
	private void initViews() {
		tvCounter = (TextView) findViewById(R.id.tv_counter);
		tvTaskOrigin=(TextView) findViewById(R.id.tv_task_origin);
		tvTaskClass=(TextView) findViewById(R.id.tv_task_class);
		tvCheckUnit=(TextView) findViewById(R.id.tv_check_unit);
		tvPhone=(TextView) findViewById(R.id.tv_phone);
		tvAddress=(TextView) findViewById(R.id.tv_address);
		tvCheckNum=(TextView) findViewById(R.id.tv_check_number);
		tvCheckPerson=(TextView) findViewById(R.id.tv_check_person);
		tvCheckDate=(TextView) findViewById(R.id.tv_check_date);
		tvSampleName=(TextView) findViewById(R.id.tv_sample_name);
		tvProjectName=(TextView) findViewById(R.id.tv_project_name);
		tvCheckQuantity=(TextView) findViewById(R.id.tv_check_quantity);
		tvCheckAddress=(TextView) findViewById(R.id.tv_check_address);
		tvCheckedUnit=(TextView) findViewById(R.id.tv_checked_unit);
		tvCheckedPhone=(TextView) findViewById(R.id.tv_checked_phone);
		tvCheckedAddress=(TextView) findViewById(R.id.tv_checked_address);
		tvLicense=(TextView) findViewById(R.id.tv_license);
		tvSentPerson=(TextView) findViewById(R.id.tv_sent_person);
		tvSentDate=(TextView) findViewById(R.id.tv_sent_date);
		tvRemark=(TextView) findViewById(R.id.tv_remark);
		Intent intent=getIntent();
		position=intent.getIntExtra("position", -1);
		for (int i = 0; i <taskclasses.size(); i++) {
			TaskClass task=taskclasses.get(i);
			int id=task.getId();
			if(position==id){
				 checkNumber=task.getcheckNumber();
			     taskProperty=task.getCptproperty();
				 taskFrom=task.getCpfrom();
				 checkUnit=task.getcheckUnit();
				 editTime=task.geteditTime();
				 remark=task.getCpmemo();
				 checkCount=task.getPlandcount();
				 String planDetail=task.getPlandetail();
					String[] detailArray=planDetail.split("¡£");
					String detail=detailArray[0];
					String [] detailArr=detail.split("£¬");
					String sampleName=detailArr[0];
					String proName=detailArr[1];
					String [] ary=sampleName.split("£º");
					String [] strary=proName.split("£º");
					samplename=ary[1];
					projectname=strary[1];
			}
		}
		tvTaskOrigin.setText(taskFrom);
		tvTaskClass.setText(taskProperty);
		tvCheckUnit.setText(checkUnit);
		tvSampleName.setText(samplename);
		tvProjectName.setText(projectname);
		tvCheckNum.setText(checkNumber);
		tvCheckDate.setText(editTime);
		tvCheckQuantity.setText(String.valueOf(checkCount));
		tvRemark.setText(remark);
		
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
	public void doClick(View v){
		switch (v.getId()) {
		case R.id.btn_print:
			print();
			break;

		case R.id.btn_return:
			back();
			break;
		}
	}
	private void print() {
		// TODO Auto-generated method stub
		
	}
	private void back() {
//		Intent intent=new Intent(CheckNumberItemActivity.this,CheckNumberActivity.class);
//		startActivity(intent);
		finish();
		
	}
}
