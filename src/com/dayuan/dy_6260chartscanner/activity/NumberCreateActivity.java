package com.dayuan.dy_6260chartscanner.activity;

import com.dayuan.dy_6260chartscanner.R;
import com.dayuan.dy_6260chartscanner.R.layout;
import com.dayuan.dy_6260chartscanner.db.DataDao;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class NumberCreateActivity extends Activity {
private EditText etTaskOrigin,etTaskClass,etCheckUnit,etPhone,etAddress,etCheckNum,
        etCheckPerson,etCheckDate,etSampleName,etProjectName,etCheckQuantity,etCheckAddress,
        etCheckedUnit,etCheckedPhone,etCheckedAddress,etLicense,etSentPerson,etSentDate,
        etRemark;
private DataDao dao;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_number_create);
		initViews();
		dao=new DataDao(this);
	}
	private void initViews() {
		etTaskOrigin=(EditText) findViewById(R.id.et_task_origin);
		etTaskClass=(EditText) findViewById(R.id.et_task_class);
		etCheckUnit=(EditText) findViewById(R.id.et_check_unit);
		etPhone=(EditText) findViewById(R.id.et_phone);
		etAddress=(EditText) findViewById(R.id.et_address);
		etCheckNum=(EditText) findViewById(R.id.et_check_number);
		etCheckPerson=(EditText) findViewById(R.id.et_check_person);
		etCheckDate=(EditText) findViewById(R.id.et_check_date);
		etSampleName=(EditText) findViewById(R.id.et_sample_name);
		etProjectName=(EditText) findViewById(R.id.et_project_name);
		etCheckQuantity=(EditText) findViewById(R.id.et_check_quantity);
		etCheckAddress=(EditText) findViewById(R.id.et_check_address);
		etCheckedUnit=(EditText) findViewById(R.id.et_checked_unit);
		etCheckedPhone=(EditText) findViewById(R.id.et_checked_phone);
		etCheckedAddress=(EditText) findViewById(R.id.et_checked_address);
		etLicense=(EditText) findViewById(R.id.et_license);
		etSentPerson=(EditText) findViewById(R.id.et_sent_person);
		etSentDate=(EditText) findViewById(R.id.et_sent_date);
		etRemark=(EditText) findViewById(R.id.et_remark);
	}
	public void doClick(View v){
		switch (v.getId()) {
		case R.id.btn_save:
			save();
			break;

		case R.id.btn_return:
//			Intent intent=new Intent(NumberCreateActivity.this,CheckNumberActivity.class);
//			startActivity(intent);
			finish();
			break;
		}
	}
	private void save() {
		String taskOrigin=etTaskOrigin.getText().toString().trim();
		String taskClass=etTaskClass.getText().toString().trim();
		String checkUnit=etCheckUnit.getText().toString().trim();
		String phone=etPhone.getText().toString().trim();
		String address=etAddress.getText().toString().trim();
		String checkNum=etCheckNum.getText().toString().trim();
		String checkPerson=etCheckPerson.getText().toString().trim();
		String checkDate=etCheckDate.getText().toString().trim();
		String sampleName=etSampleName.getText().toString().trim();
		String projectName=etProjectName.getText().toString().trim();
		String checkQuantity=etCheckQuantity.getText().toString().trim();
		String checkAddress=etCheckAddress.getText().toString().trim();
		String checkedUnit=etCheckedUnit.getText().toString().trim();
		String checkedPhone=etCheckedPhone.getText().toString().trim();
		String checkedAddress=etCheckedAddress.getText().toString().trim();
		String license=etLicense.getText().toString().trim();
		String sentPerson=etSentPerson.getText().toString().trim();
		String sentDate=etSentDate.getText().toString().trim();
		String remark=etRemark.getText().toString().trim();
		
		long id=dao.add(checkNum, taskOrigin, taskClass, checkUnit, phone, address, checkPerson,
				checkDate, sampleName, projectName, checkQuantity, checkAddress, checkedUnit, checkedPhone,
				checkedAddress, license, sentPerson, sentDate, remark);
		if (id == -1) {
			Toast.makeText(this, "添加失败", 0).show();
		} else {
			Toast.makeText(this, "添加成功", 0).show();
		}	
	}
}
