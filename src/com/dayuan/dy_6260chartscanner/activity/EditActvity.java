package com.dayuan.dy_6260chartscanner.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.dayuan.dy_6260chartscanner.R;
import com.dayuan.dy_6260chartscanner.adapter.ProjectAdapter;
import com.dayuan.dy_6260chartscanner.biz.ProjectBiz;
import com.dayuan.dy_6260chartscanner.db.Dao;
import com.dayuan.dy_6260chartscanner.entity.Project;
import com.dayuan.dy_6260chartscanner.util.CopyDB;
import com.dayuan.dy_6260chartscanner.util.ShowTime;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class EditActvity extends BaseActivity{
	private static final int GET_RESULT = 10;
	private List<Project> projects;
	private Button btnValueT,btnValueTC,btnXiaoxian,btnBise;
	private EditText etProjectName;
	private EditText etValue,etValueT,etValueTL,etConstant0,etConstant1,etConstant2,etConstant3;
	private EditText etUnit,etLimitValue,etDifferenceValue,etDifferenceValueTwo,etDifferenceValueThree;;
	private LinearLayout llFormOne;
    private LinearLayout llFormTwo;
    private LinearLayout llFormThree;
    private TextView tvCounter,tvRange;
    private Spinner spinner;
	private Dao dao;
	private static int key;
	//private static int  position;
	private static int id;
	private static int i;
	
	private boolean isRunning;
	private String result=null;
	String[] numbers = { "请选择", "阴性", "阳性"}; 
	private int positns=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit);
		setViews();
		isRunning = true;
		ShowTime.ShowTime(this, isRunning, tvCounter);
		
		SpinnerAdapter adapter = new SpinnerAdapter(this,  
	            android.R.layout.simple_spinner_item, numbers);  
	    spinner.setAdapter(adapter);  
		
		Intent intent=getIntent();
		String name=intent.getStringExtra("data");
		String positions=intent.getStringExtra("positions");
		Log.i("buhao", name);
		//position=Integer.parseInt(name);
		 i=Integer.parseInt(positions);
		projects=new ProjectBiz(this).getProjects();
		
		dao=new Dao(this);
		for(Project p:projects){
			 id=p.getId();
			if(id==i){
				Log.i("buhao", ""+i);
				Log.i("nihao", ""+id);
			etProjectName.setText(p.getName().toString());
			String method=p.getMethod().toString();
			key=Integer.parseInt(method);
			result=p.getResult();
			Log.v("right", ""+key);
			
			etValue.setText(p.getValueC().toString());
			etValueT.setText(p.getValueT().toString());
			etValueTL.setText(p.getValueTL().toString());
			etConstant0.setText(p.getConstant0().toString());
			etConstant1.setText(p.getConstant1().toString());
			etConstant2.setText(p.getConstant2().toString());
			etConstant3.setText(p.getConstant3().toString());
			etUnit.setText(p.getUnit().toString());
			etLimitValue.setText(p.getLimitV().toString());
			tvRange.setText("( "+p.getValueT().toString()+" - "+p.getValueTL().toString()+" )");
			etDifferenceValue.setText(p.getValueOne());
			etDifferenceValueTwo.setText(p.getValueTwo());
			etDifferenceValueThree.setText(p.getValueThree());
			if(key==0){
				selectFormItem(btnValueT);	
				llFormOne.setVisibility(View.VISIBLE);
				llFormTwo.setVisibility(View.GONE);
				llFormThree.setVisibility(View.GONE);
			}else if(key==1){
				selectFormItem(btnValueTC);
				llFormOne.setVisibility(View.VISIBLE);
				llFormTwo.setVisibility(View.GONE);
				llFormThree.setVisibility(View.GONE);
			}else if(key==2){
				selectFormItem(btnXiaoxian);
				llFormOne.setVisibility(View.GONE);
				llFormTwo.setVisibility(View.VISIBLE);
				llFormThree.setVisibility(View.GONE);
			}else if(key==3){
				selectFormItem(btnBise);
				llFormOne.setVisibility(View.GONE);
				llFormTwo.setVisibility(View.GONE);
				llFormThree.setVisibility(View.VISIBLE);
			}
			}
		}
		
		       
	}
		


	
	
//	public void setPosition(int position){
//		this.position=position;
//	}
	private void setViews() {
		spinner=(Spinner) findViewById(R.id.sp_yinyang);
		etDifferenceValue=(EditText) findViewById(R.id.et_difference_value);
		etDifferenceValueTwo=(EditText) findViewById(R.id.et_difference_value_two);
		etDifferenceValueThree=(EditText) findViewById(R.id.et_difference_value_three);
		tvCounter=(TextView) findViewById(R.id.tv_Counter);
		etProjectName=(EditText) findViewById(R.id.et_project_name);
		etValue=(EditText) findViewById(R.id.et_value);
		etUnit=(EditText) findViewById(R.id.et_unit);
		etLimitValue=(EditText) findViewById(R.id.et_limit_value);
		etValueT=(EditText) findViewById(R.id.et_leT_value);
		etValueTL=(EditText) findViewById(R.id.et_greaterT_value);
		btnValueT=(Button) findViewById(R.id.btn_t_value);
		btnValueTC=(Button) findViewById(R.id.btn_tc_value);
		etConstant0=(EditText) findViewById(R.id.et_constant0);
		etConstant1=(EditText) findViewById(R.id.et_constant1);
		etConstant2=(EditText) findViewById(R.id.et_constant2);
		etConstant3=(EditText) findViewById(R.id.et_constant3);
		btnXiaoxian=(Button) findViewById(R.id.btn_xiaoxian);
		btnBise=(Button) findViewById(R.id.btn_bise);
		llFormOne=(LinearLayout) findViewById(R.id.linearLayout01);
		llFormTwo=(LinearLayout) findViewById(R.id.linearLayout02);
		llFormThree=(LinearLayout) findViewById(R.id.linearLayout03);
		tvRange=(TextView) findViewById(R.id.tv_range);
	}
	public void doMethod(View v){
		
		//key=v.getId();
		switch (v.getId()) {
		case R.id.btn_t_value:
			selectFormItem(btnValueT);	
			llFormOne.setVisibility(View.VISIBLE);
			llFormTwo.setVisibility(View.GONE);
			llFormThree.setVisibility(View.GONE);
			etValueT.setText("");
			etValueTL.setText("");
			key=0;
			break;
		case R.id.btn_tc_value:
			selectFormItem(btnValueTC);
			llFormOne.setVisibility(View.VISIBLE);
			llFormTwo.setVisibility(View.GONE);
			llFormThree.setVisibility(View.GONE);
			etValueT.setText("");
			etValueTL.setText("");
			key=1;
			break;
		case R.id.btn_xiaoxian:
			selectFormItem(btnXiaoxian);
			llFormOne.setVisibility(View.GONE);
			llFormTwo.setVisibility(View.VISIBLE);
			llFormThree.setVisibility(View.GONE);
			etConstant0.setText("");
			etConstant1.setText("");
			etConstant2.setText("");
			etConstant3.setText("");
			etUnit.setText("");
			etLimitValue.setText("");
			key=2;
			break;
		case R.id.btn_bise:
			selectFormItem(btnBise);
			llFormOne.setVisibility(View.GONE);
			llFormTwo.setVisibility(View.GONE);
			llFormThree.setVisibility(View.VISIBLE);
			etValueT.setText("");
			etValueTL.setText("");
			etConstant0.setText("");
			etConstant1.setText("");
			etConstant2.setText("");
			etConstant3.setText("");
			etUnit.setText("");
			etLimitValue.setText("");
			key=3;
			break;
		}
			
		}
		private void selectFormItem(View v) {
			btnValueT.setSelected(false);
			btnValueTC.setSelected(false);
			btnXiaoxian.setSelected(false);
			btnBise.setSelected(false);
			v.setSelected(true);
		}
	public void doClick(View v){
		switch (v.getId()) {
		case R.id.btn_save:
			String method=String.valueOf(key);
			//	Log.i("duima", method);
				String name=etProjectName.getText().toString().trim();
				String valueC=etValue.getText().toString().trim();
				String valueT=etValueT.getText().toString().trim();
				String valueTL=etValueTL.getText().toString().trim();
				String constant0=etConstant0.getText().toString().trim();
				String constant1=etConstant1.getText().toString().trim();
				String constant2=etConstant2.getText().toString().trim();
				String constant3=etConstant3.getText().toString().trim();
				String unit=etUnit.getText().toString().trim();
				String limitV=etLimitValue.getText().toString().trim();
				String valueOne=etDifferenceValue.getText().toString();
				String valueTwo=etDifferenceValueTwo.getText().toString();
				String valueThree=etDifferenceValueThree.getText().toString();
				String result=numbers[positns];
				if(key==0||key==1){
					if(TextUtils.isEmpty(name)){
						Toast.makeText(this, "项目名称不能为空", 0).show();
					}else if(TextUtils.isEmpty(valueC)){
						Toast.makeText(this, "C值不能为空", 0).show();
					}
					else if(TextUtils.isEmpty(unit)){
						Toast.makeText(this, "单位不能为空", 0).show();
					}else{
					long count = dao.update(i,name,method,valueC,valueT,valueTL,constant0,constant1,
							constant2,constant3,unit,limitV);
					if (count== 0) {
						Toast.makeText(this, "编辑失败,没有记录", 0).show();
					} else {
						Toast.makeText(this, "编辑成功", 0).show();
					}
					Intent in=new Intent(this,ProjectSettingActivity.class);
					in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(in);
					finish();
					}
				}
				else if(key==2){
				if(TextUtils.isEmpty(name)){
					Toast.makeText(this, "项目名称不能为空", 0).show();
				}else if(TextUtils.isEmpty(valueC)){
					Toast.makeText(this, "C值不能为空", 0).show();
				}else if(TextUtils.isEmpty(valueT)||TextUtils.isEmpty(valueTL)){
					Toast.makeText(this, "T值不能为空", 0).show();
				}else{
				long count = dao.update(i,name,method,valueC,valueT,valueTL,constant0,constant1,
						constant2,constant3,unit,limitV);
				if (count== 0) {
					Toast.makeText(this, "编辑失败,没有记录", 0).show();
				} else {
					Toast.makeText(this, "编辑成功", 0).show();
				}
				Intent in=new Intent(this,ProjectSettingActivity.class);
				in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(in);
				finish();
				}
				}else if(key==3){
					if(TextUtils.isEmpty(name)){
						Toast.makeText(this, "项目名称不能为空", 0).show();
					}else if(TextUtils.isEmpty(valueC)){
						Toast.makeText(this, "C值不能为空", 0).show();
					}else{
					long count = dao.update(i,name,method,valueC,valueT,valueTL,constant0,constant1,
							constant2,constant3,unit,limitV,valueOne,valueTwo,valueThree,result);
					if (count== 0) {
						Toast.makeText(this, "编辑失败,没有记录", 0).show();
					} else {
						Toast.makeText(this, "编辑成功", 0).show();
					}
					Intent in=new Intent(this,ProjectSettingActivity.class);
					in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(in);
					finish();
					}
				} else {
					Log.i("hao", ""+id);
					Log.i("buhao", ""+i);
					
				}
				
			break;

		case R.id.btn_return:
			back();
			break;
		}
	}
	private void back() {
		finish();
	}
	private class SpinnerAdapter extends ArrayAdapter<String> { 
		Context context;  
	    String[] items = new String[] {};  
		public SpinnerAdapter(Context context, int textViewResourceId, String[] objects) {
			super(context, textViewResourceId, objects);
			this.items = objects;  
	        this.context = context;  
	        
		}
		
	    @Override  
	    public View getDropDownView(int position, View convertView,  
	            ViewGroup parent) {  
	        if (convertView == null) {  
	            LayoutInflater inflater = LayoutInflater.from(context);  
	            convertView = inflater.inflate(  
	                    android.R.layout.simple_spinner_item, parent, false);  
	        }  
	  
	        TextView tv = (TextView) convertView  
	                .findViewById(android.R.id.text1);  
	        tv.setText(items[position]);  
	        tv.setTextColor(Color.BLACK);  
	        tv.setTextSize(20);  
	        return convertView;  
	    }  
	  
	    @Override  
	    public View getView(int position, View convertView, ViewGroup parent) {
	    	 positns=position;
	        if (convertView == null) {  
	            LayoutInflater inflater = LayoutInflater.from(context);  
	            convertView = inflater.inflate(  
	                    android.R.layout.simple_spinner_item, parent, false);  
	        }  
	  
	        // android.R.id.text1 is default text view in resource of the android.  
	        // android.R.layout.simple_spinner_item is default layout in resources of android.  
	  
	        TextView tv = (TextView) convertView  
	                .findViewById(android.R.id.text1);
	        if(positns==0){
	        	if(TextUtils.isEmpty(result)){
	        		 tv.setText(items[0]);
	        	}else{
	        		
	        		tv.setText(result);
	        	}
	        }else{
	        tv.setText(items[position]);
	        }
	        tv.setTextColor(Color.BLACK);  
	        tv.setTextSize(20);  
	        return convertView;  
	    }  
	}  
	@Override
	protected void onDestroy() {
		isRunning=false;
		super.onDestroy();
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (null != this.getCurrentFocus()) {
			/**
			 * 点击空白位置 隐藏软键盘
			 */
			InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
			return mInputMethodManager.hideSoftInputFromWindow(this
					.getCurrentFocus().getWindowToken(), 0);
		}
		return super.onTouchEvent(event);
	}
}
