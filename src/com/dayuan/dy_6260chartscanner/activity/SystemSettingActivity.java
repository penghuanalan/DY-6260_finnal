package com.dayuan.dy_6260chartscanner.activity;

import com.dayuan.dy_6260chartscanner.R;
import com.dayuan.dy_6260chartscanner.R.layout;
import com.dayuan.dy_6260chartscanner.fragment.BluetoothFragment;
import com.dayuan.dy_6260chartscanner.fragment.CloudSettingFragment;
import com.dayuan.dy_6260chartscanner.fragment.NetSettingFragment;
import com.dayuan.dy_6260chartscanner.fragment.OthersSettingFragment;
import com.dayuan.dy_6260chartscanner.fragment.VerifyFragment;
import com.dayuan.dy_6260chartscanner.fragment.WifiFragment;
import com.dayuan.dy_6260chartscanner.util.ShowTime;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

public class SystemSettingActivity extends BaseActivity implements
		View.OnClickListener {

	private Button btnWifi;
	private Button btnCable;
	private Button btnBluetooth;
	private Button btnCloud;
	private Button btnTime;
	private Button btnOthers;
	
	private Button btnSave;
    private TextView tvCounter;
    private boolean isRunning;
	private WifiFragment wifiFragment;
	private NetSettingFragment netFragment;
	private BluetoothFragment bluetoothFragment;
	private CloudSettingFragment cloudFragment;
	private VerifyFragment timeFragment;
	private OthersSettingFragment othersFragment;
	private FragmentManager fm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_system_setting);
		initViews();
		initEvents();
		 fm = getFragmentManager();
		// 首先 我们先选定一个
		select(0);
		isRunning=true;
		ShowTime.ShowTime(this, isRunning, tvCounter);
	}

	private void initViews() {
		tvCounter=(TextView) findViewById(R.id.tv_counter);
		btnWifi = (Button) findViewById(R.id.btn_wifi);
		btnCable = (Button) findViewById(R.id.btn_cable_net);
		btnBluetooth = (Button) findViewById(R.id.btn_bluetooth);
		btnCloud = (Button) findViewById(R.id.btn_cloud);
		btnTime = (Button) findViewById(R.id.btn_time_setting);
		btnOthers = (Button) findViewById(R.id.btn_others_setting);
	}

	private void initEvents() {
		btnWifi.setOnClickListener(this);
		btnCable.setOnClickListener(this);
		btnBluetooth.setOnClickListener(this);
		btnCloud.setOnClickListener(this);
		btnTime.setOnClickListener(this);
		btnOthers.setOnClickListener(this);

	}
	private void initSelectItem() {
		btnWifi.setSelected(false);
		btnCable.setSelected(false);
		btnBluetooth.setSelected(false);
		btnCloud.setSelected(false);
		btnTime.setSelected(false);
		btnOthers.setSelected(false);
	}
	private void select(int i) {
		initSelectItem();
		FragmentTransaction ft = fm.beginTransaction(); // 开启一个事务
		hidtFragment(ft); // 先隐藏 Fragment

		switch (i) {
		case 0:
			btnWifi.setSelected(true);
			if(wifiFragment==null){
				wifiFragment=new WifiFragment();
				ft.add(R.id.fragment_container, wifiFragment);
			}else
			{
				ft.show(wifiFragment);
			}
//			netFragment=null;
//			bluetoothFragment=null;
//			cloudFragment=null;
//			timeFragment=null;
//			othersFragment=null;
			break;
		case 1:
			btnCable.setSelected(true);
			if(netFragment==null){
				netFragment=new NetSettingFragment();
				ft.add(R.id.fragment_container, netFragment);
			}else
			{
				ft.show(netFragment);
			}
//			wifiFragment=null;
//			bluetoothFragment=null;
//			cloudFragment=null;
//			timeFragment=null;
//			othersFragment=null;
			break;
		case 2:
			btnBluetooth.setSelected(true);
			if(bluetoothFragment==null){
				bluetoothFragment=new BluetoothFragment();
				ft.add(R.id.fragment_container, bluetoothFragment);
			}else
			{
				ft.show(bluetoothFragment);
			}
//			wifiFragment=null;
//			netFragment=null;
//			cloudFragment=null;
//			timeFragment=null;
//			othersFragment=null;
			break;
			
		case 3:
			btnCloud.setSelected(true);
			if(cloudFragment==null){
				cloudFragment=new CloudSettingFragment();
				ft.add(R.id.fragment_container, cloudFragment);
			}else
			{
				ft.show(cloudFragment);
			}
//			wifiFragment=null;
//			netFragment=null;
//			bluetoothFragment=null;
//			timeFragment=null;
//			othersFragment=null;
			break;
		case 4:
			btnTime.setSelected(true);
			if(timeFragment==null){
				timeFragment=new VerifyFragment();
				ft.add(R.id.fragment_container, timeFragment);
			}else
			{
				ft.show(timeFragment);
			}
//			wifiFragment=null;
//			netFragment=null;
//			bluetoothFragment=null;
//			cloudFragment=null;
//			othersFragment=null;
			break;
		case 5:
			btnOthers.setSelected(true);
			if(othersFragment==null){
				othersFragment=new OthersSettingFragment();
				ft.add(R.id.fragment_container, othersFragment);
			}else
			{
				ft.show(othersFragment);
			}
//			wifiFragment=null;
//			netFragment=null;
//			bluetoothFragment=null;
//			cloudFragment=null;
//			timeFragment=null;
			break;
		}
		ft.commit();
	}

	private void hidtFragment(FragmentTransaction fragmentTransaction) {
		if(wifiFragment!=null){
			 fragmentTransaction.hide(wifiFragment);
		}
		if(netFragment!=null){
			fragmentTransaction.hide(netFragment);
		}
		if(bluetoothFragment!=null){
			fragmentTransaction.hide(bluetoothFragment);
		}
		if(cloudFragment!=null){
			fragmentTransaction.hide(cloudFragment);
		}
		if(timeFragment!=null){
			fragmentTransaction.hide(timeFragment);
		}
		if(othersFragment!=null){
			fragmentTransaction.hide(othersFragment);
		}
		
	}

	@Override
	public void onClick(View v) {
		initSelectItem();
		switch (v.getId()) {
		case R.id.btn_wifi:
			select(0);
			break;
		case R.id.btn_cable_net:
			select(1);
			break;
		case R.id.btn_bluetooth:
			select(2);
			break;
		case R.id.btn_cloud:
			select(3);
			break;
		case R.id.btn_time_setting:
			select(4);
			break;
		case R.id.btn_others_setting:
			select(5);
			break;

		}

	}
    public void doClick(View v){
    	switch (v.getId()) {
		case R.id.btn_save:
			
			break;
		case R.id.btn_return :
//			Intent intent=new Intent(SystemSettingActivity.this,BaseActivity.class);
//			startActivity(intent);
			finish();
			break;

		default:
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
    	isRunning=false;
    	super.onDestroy();
    }
	
}
