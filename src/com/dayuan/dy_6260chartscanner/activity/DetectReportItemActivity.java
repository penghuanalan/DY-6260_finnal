package com.dayuan.dy_6260chartscanner.activity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.dayuan.dy_6260chartscanner.R;
import com.dayuan.dy_6260chartscanner.R.layout;
import com.dayuan.dy_6260chartscanner.biz.ReportDataBiz;
import com.dayuan.dy_6260chartscanner.biz.TaskClassBiz;
import com.dayuan.dy_6260chartscanner.entity.ReportData;
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

public class DetectReportItemActivity extends Activity {
	private TextView tvCheckReportNum, tvTaskOrigin, tvTaskClass, tvCheckUnit,
			tvPhone, tvCheckedUnit, tvCheckedPhone, tvCheckNum, tvCheckPerson,
			tvCheckDate, tvSampleName, tvProjectName, tvCheckQuantity,
			tvCheckAddress, tvResult, tvSentPerson, tvCheckStandard,
			tvConclusion;
	private Handler handler;
	private TextView tvCounter;
	private boolean isRunning;
	private String back;
	Intent intent;
	private String checkNumber;
	private List<ReportData> datas;
	private String taskProperty;
	private String taskFrom;
	private String checkUnit;
	private String editTime;
	private String remark;
	private int checkCount;
	String samplename;
	String projectname;
	String reportNumber;
	String result;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detect_report_item);
		initViews();
		datas = new ReportDataBiz(this).getReportDataClass();
		isRunning = true;
		ShowTime.ShowTime(this, isRunning, tvCounter);
	}

	private void initViews() {
		tvCounter = (TextView) findViewById(R.id.tv_counter);
		tvTaskOrigin = (TextView) findViewById(R.id.tv_task_origin);
		tvTaskClass = (TextView) findViewById(R.id.tv_task_class);
		tvCheckUnit = (TextView) findViewById(R.id.tv_check_unit);
		tvPhone = (TextView) findViewById(R.id.tv_check_unit_phone);
		tvCheckReportNum = (TextView) findViewById(R.id.tv_check_report_num);
		tvCheckNum = (TextView) findViewById(R.id.tv_check_number);
		tvCheckPerson = (TextView) findViewById(R.id.tv_check_person);
		tvCheckDate = (TextView) findViewById(R.id.tv_check_date);
		tvSampleName = (TextView) findViewById(R.id.tv_sample_name);
		tvProjectName = (TextView) findViewById(R.id.tv_check_project);
		tvCheckQuantity = (TextView) findViewById(R.id.tv_check_count);
		tvCheckAddress = (TextView) findViewById(R.id.tv_check_address);
		tvCheckedUnit = (TextView) findViewById(R.id.tv_checked_unit);
		tvCheckedPhone = (TextView) findViewById(R.id.tv_checked_unit_phone);
		tvSentPerson = (TextView) findViewById(R.id.tv_sent_person);
		tvResult = (TextView) findViewById(R.id.tv_result);
		tvCheckStandard = (TextView) findViewById(R.id.tv_check_standard);
		tvConclusion = (TextView) findViewById(R.id.tv_conclusion);
		intent = getIntent();
		checkNumber = intent.getStringExtra("checkNumber");
		int position=intent.getIntExtra("position", -1);
		datas = new ReportDataBiz(this).getReportDataClass();
		for (int i = 0; i < datas.size(); i++) {
			ReportData report = datas.get(i);
			String checkNum = report.getCheckNumber();
			int id=report.getId();
			if (checkNum.equals(checkNumber)) {
				taskProperty = report.getCptproperty();
				taskFrom = report.getCpfrom();
				checkUnit = report.getCheckUnit();
				editTime =report.getEditTime();
				//remark = task.getCpmemo();
				checkCount =report.getPlandcount();
				reportNumber =report.getReportNumber();
				result = report.getResult();
				String planDetail = report.getPlandetail();
				String[] detailArray = planDetail.split("¡£");
				String detail = detailArray[0];
				String[] detailArr = detail.split("£¬");
				String sampleName = detailArr[0];
				String proName = detailArr[1];
				String[] ary = sampleName.split("£º");
				String[] strary = proName.split("£º");
				samplename = ary[1];
				projectname = strary[1];
				tvSampleName.setText(samplename);
				tvCheckReportNum.setText(reportNumber);
				tvCheckUnit.setText(checkUnit);
				tvTaskOrigin.setText(taskFrom);
				tvTaskClass.setText(taskProperty);
				tvCheckDate.setText(editTime);
				tvCheckNum.setText(checkNum);
				tvCheckQuantity.setText(checkCount+"");
				tvProjectName.setText(projectname);
				tvResult.setText(result);
			}
			if(position==id){
				String checknumber=report.getCheckNumber();
				taskProperty = report.getCptproperty();
				taskFrom = report.getCpfrom();
				checkUnit = report.getCheckUnit();
				editTime = report.getEditTime();
				//remark = task.getCpmemo();
				checkCount = report.getPlandcount();
				reportNumber = report.getReportNumber();
				result = report.getResult();
				String planDetail =report.getPlandetail();
				String[] detailArray = planDetail.split("¡£");
				String detail = detailArray[0];
				String[] detailArr = detail.split("£¬");
				String sampleName = detailArr[0];
				String proName = detailArr[1];
				String[] ary = sampleName.split("£º");
				String[] strary = proName.split("£º");
				samplename = ary[1];
				projectname = strary[1];
				tvSampleName.setText(samplename);
				tvCheckReportNum.setText(reportNumber);
				tvCheckUnit.setText(checkUnit);
				tvTaskOrigin.setText(taskFrom);
				tvTaskClass.setText(taskProperty);
				tvCheckDate.setText(editTime);
				tvCheckNum.setText(checknumber);
				tvCheckQuantity.setText(checkCount+"");
				tvProjectName.setText(projectname);
				tvResult.setText(result);
			}
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
		isRunning = false;
	}

	public void doClick(View v) {
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

		if ("back".equals(intent.getStringExtra("back"))) {
			
			Intent in = new Intent(DetectReportItemActivity.this,
					DetectionChannelOneActvity.class);
			setResult(0, in);
		} else if ("backTwo".equals(intent.getStringExtra("back"))) {
//			Intent it = new Intent(DetectReportItemActivity.this,
//					DetectionReportActivity.class);
//			startActivity(it);
			finish();
		}
		finish();

	}

}
