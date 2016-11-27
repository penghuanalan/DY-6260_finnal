package com.dayuan.dy_6260chartscanner.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.dayuan.dy_6260chartscanner.R;
import com.dayuan.dy_6260chartscanner.TApplication;
import com.dayuan.dy_6260chartscanner.adapter.MyAdapter;
import com.dayuan.dy_6260chartscanner.util.NetworkUtil;
import com.dayuan.dy_6260chartscanner.util.WifiAdmin;
import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity implements View.OnClickListener,
		TextWatcher {
	private RelativeLayout rlUser;
	private RelativeLayout rlPassword;
	private EditText etUserName;
	private EditText etPassword;
	private Button btnContact;
	private Button btnLock;
	private Button btnCircle01;
	private Button btnCircle02;
	private Button btnLogin;
	private Button btnExit;
	// private Button btnSwitchLeft;
	// private Button btnSwitchRight;
	// ph
	int size = 0;
	private int myvid = 1155, mypid = 22336;
	private int myvid1 = 6790, mypid1 = 29987;
	UsbDevice[] mdevices = new UsbDevice[6];
	String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
	String username;
	String pwd;
	
	private Button btnYes;
	private Button btnNo;
	private Button btnClose, btnOpen;
	// private TextView tvSwitch;
	// private ListView listView;
	// private MyAdapter adapter;
	private ConnectivityManager mConnectivity;
	// private WifiManager wifiManager;
	// private WifiAdmin wifiAdmin;
	// List<ScanResult> lists;
	// private TextView tvWalan;
	/**
	 * 1. 声明一个共享参数(存储数据方便的api)
	 */
	private SharedPreferences sp;
	private UsbManager mUsbManager;
	private HashMap<String, UsbDevice> deviceList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		sp = this.getSharedPreferences("config", MODE_PRIVATE);
		initViews();
		restoreInfo();
		initPremission();

	}

	private void restoreInfo() {
		// TODO:读取密码
		String username = sp.getString("username", "");
		String password = sp.getString("password", "");
		etUserName.setText(username);
		etPassword.setText(password);

	}

	private void initViews() {
		rlUser = (RelativeLayout) findViewById(R.id.rl_user);
		rlPassword = (RelativeLayout) findViewById(R.id.rl_password);
		etUserName = (EditText) findViewById(R.id.et_user);
		etPassword = (EditText) findViewById(R.id.et_password);
		btnContact = (Button) findViewById(R.id.btn_contact);
		btnLock = (Button) findViewById(R.id.btn_lock);
		btnCircle01 = (Button) findViewById(R.id.btn_circle_01);
		btnCircle02 = (Button) findViewById(R.id.btn_circle_02);
		btnLogin = (Button) findViewById(R.id.btn_login);
		btnExit = (Button) findViewById(R.id.btn_exit);
		// btnSwitchLeft = (Button) findViewById(R.id.btn_switch_left);
		// btnSwitchRight = (Button) findViewById(R.id.btn_switch_right);

		btnLogin.setOnClickListener(this);
		btnExit.setOnClickListener(this);
		btnCircle01.setOnClickListener(this);
		btnCircle02.setOnClickListener(this);
		// btnSwitchLeft.setOnClickListener(this);
		// btnSwitchRight.setOnClickListener(this);
		etUserName.addTextChangedListener(this);
		etPassword.addTextChangedListener(this);
	}

	private void initPremission() {
		mUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
		if (mUsbManager != null) {
			deviceList = mUsbManager.getDeviceList();
		}
		if (!deviceList.isEmpty()) {
			Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
			 int i = 0;
			while (deviceIterator.hasNext()) {
				UsbDevice device = deviceIterator.next();
				if (device != null) {
					if(device.getVendorId()==6790||device.getVendorId()==1155){
						mdevices[i] = device;
						i++;
					}
					
				}
			}
		}
		PendingIntent mPermissionIntent = PendingIntent.getBroadcast(this, 0,
				new Intent(ACTION_USB_PERMISSION), 0);
		IntentFilter permissionFilter = new IntentFilter(ACTION_USB_PERMISSION);
		registerReceiver(receiver, permissionFilter);
		for (int j = 0; j < mdevices.length; j++) {
			if(mdevices[j]!=null){
				if (mUsbManager.hasPermission(mdevices[j])) {
					System.out.println(mdevices[j].getProductId() + "获取到权限"+mdevices[j].getVendorId());
				} else {
					mUsbManager.requestPermission(mdevices[j], mPermissionIntent);
				}
			}
		}
	}

	BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (ACTION_USB_PERMISSION.equals(action)) {
				synchronized (this) {
					UsbDevice usb = intent
							.getParcelableExtra(UsbManager.EXTRA_DEVICE);
					System.out.println(usb.getProductId() + "是否拥有权限"
							+ mUsbManager.hasPermission(usb));
					if (intent.getBooleanExtra(
							UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
						if (usb != null) {
							System.out.println("获取权限成功");
							onCreate(null);
						}
					} else {
						System.out.println("获取权限失败");
					}
				}
			}
		}
	};

	@Override
	public void onClick(View v) {
		username = etUserName.getText().toString();
		pwd = etPassword.getText().toString();

		switch (v.getId()) {

		case R.id.btn_login:
			login();
			break;
		case R.id.btn_exit:
			exit();
			break;
		case R.id.btn_circle_01:
			etUserName.setText("");
			break;
		case R.id.btn_circle_02:
			etPassword.setText("");
			break;
		// case R.id.btn_switch_left:
		// selectButtonItem(btnSwitchLeft);
		// leftWindow();
		//
		// break;
		// case R.id.btn_switch_right:
		// selectButtonItem(btnSwitchRight);
		// rightWindow();
		// break;
		}

	}

	// private void leftWindow() {
	// AlertDialog.Builder builder = new AlertDialog.Builder(this);
	// final AlertDialog alertDialog = builder.create();
	// alertDialog.setCanceledOnTouchOutside(false);
	// alertDialog.show();
	// // 自定义UI
	// Window window = alertDialog.getWindow();
	// View dialView = View.inflate(this, R.layout.local_login_window, null);
	// window.setContentView(dialView);
	// btnNo = (Button) window.findViewById(R.id.btn_no);
	// btnYes = (Button) window.findViewById(R.id.btn_yes);
	// btnNo.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// // TODO Auto-generated method stub
	// alertDialog.dismiss();
	// }
	// });
	// btnYes.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// alertDialog.dismiss();
	// }
	//
	// });
	//
	// }

	// private void rightWindow() {
	// AlertDialog.Builder builder = new AlertDialog.Builder(this);
	// final AlertDialog alertDialog = builder.create();
	// alertDialog.setCanceledOnTouchOutside(false);
	// alertDialog.show();
	// // 自定义UI
	// Window window = alertDialog.getWindow();
	// View dialogView = View.inflate(this, R.layout.net_login_window, null);
	// window.setContentView(dialogView);
	// btnNo = (Button) window.findViewById(R.id.btn_no);
	// btnYes = (Button) window.findViewById(R.id.btn_yes);
	// btnNo.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// // TODO Auto-generated method stub
	// alertDialog.dismiss();
	// }
	// });
	// btnYes.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// alertDialog.dismiss();
	// }
	//
	// });
	//
	// }

	// private void selectButtonItem(View v) {
	// btnSwitchLeft.setSelected(false);
	// btnSwitchRight.setSelected(false);
	// v.setSelected(true);
	// }

	private void login() {
		try {

			// 本地选中时
			// if (btnSwitchLeft.isSelected() && (!btnSwitchRight.isSelected()))
			// {
			if ("user".equals(username) && "123456".equals(pwd)) {
				Intent intent = new Intent(LoginActivity.this,
						BaseActivity.class);
				startActivity(intent);
				// TODO:记录密码
				Editor editor = sp.edit();
				editor.putString("username", username);
				editor.putString("password", pwd);
				editor.commit();// 提交数据. 类似关闭流,事务
			} else if (TextUtils.isEmpty(username) || TextUtils.isEmpty(pwd)) {
				Toast.makeText(this, "您输入的用户名或密码不能为空", Toast.LENGTH_SHORT)
						.show();
				return;
			} else if (!"user".equals(username) || !"123456".equals(pwd)) {
				Toast.makeText(this, "您输入的用户名或密码有误", Toast.LENGTH_SHORT).show();
			}
			// // 联网按钮选中时
			// } else if (btnSwitchRight.isSelected()
			// && (!btnSwitchLeft.isSelected())) {
			// mConnectivity = (ConnectivityManager)
			// getSystemService(Context.CONNECTIVITY_SERVICE);
			// // TelephonyManager mTelephony=
			// // (TelephonyManager)this.getSystemService(TELEPHONY_SERVICE);
			// // 检查网络连接，如果无网络可用，就不需要进行连网操作等
			// NetworkInfo info = mConnectivity.getActiveNetworkInfo();
			// // if(info==null||!mConnectivity.getBackgroundDataSetting()){
			// setNetwork();
			// // }else{
			// // Intent intent = new Intent(LoginActivity.this,
			// // BaseActivity.class);
			// // startActivity(intent);
			// }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// View selectedItem;
	// String wifiItemSSID;
	// Button btnCancel, btnReset, btnShowPassword, btnOk;
	// TextView tvShowPassword;
	// LinearLayout llPassword, llShowPassword;
	// EditText et_Password;
	// private boolean showPassword = true;
	// View selectedMenu;
	// View item;
	// Boolean isFirst = true;
	// int wifiItemId;
	//
	// private void setNetwork() {
	// NetworkUtil.checkNetworkState(this);
	// AlertDialog.Builder builder = new AlertDialog.Builder(this);
	// final AlertDialog alertDialog = builder.create();
	// alertDialog.setCanceledOnTouchOutside(false);
	// alertDialog.show();
	// // 自定义UI
	// Window window = alertDialog.getWindow();
	// window.setLayout(RelativeLayout.LayoutParams.FILL_PARENT,
	// RelativeLayout.LayoutParams.FILL_PARENT);
	// View dialogView = View.inflate(this,
	// R.layout.networking_parameter_window, null);
	// window.setContentView(dialogView);
	// final LinearLayout llCable = (LinearLayout) window
	// .findViewById(R.id.ll_cable);
	// final View vLine = window.findViewById(R.id.v_line02);
	// final LinearLayout llCommunication = (LinearLayout) window
	// .findViewById(R.id.ll_communication);
	// final LinearLayout llTwo = (LinearLayout) window
	// .findViewById(R.id.linearLayout02);
	// listView = (ListView) window.findViewById(R.id.lv_wifi);
	// tvSwitch = (TextView) window.findViewById(R.id.tv_show_state);
	// tvWalan = (TextView) window.findViewById(R.id.tv_wlan_item);
	// btnOpen = (Button) window.findViewById(R.id.btn_open);
	// btnClose = (Button) window.findViewById(R.id.btn_guan);
	// btnCancel = (Button) window.findViewById(R.id.btn_cancel);
	// btnOk = (Button) window.findViewById(R.id.btn_connect);
	// btnReset = (Button) window.findViewById(R.id.btn_reset);
	// tvShowPassword = (TextView) window.findViewById(R.id.tv_show_password);
	// btnShowPassword = (Button) window.findViewById(R.id.btn_show_password);
	// llPassword = (LinearLayout) window.findViewById(R.id.ll_password);
	// llShowPassword = (LinearLayout) window
	// .findViewById(R.id.ll_show_password);
	// et_Password = (EditText) window.findViewById(R.id.et_password);
	// Button btnBack = (Button) window.findViewById(R.id.btn_back);
	// wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
	// wifiAdmin = new WifiAdmin(this);
	// // openWifi();
	// lists = wifiManager.getScanResults();
	// adapter = new MyAdapter(LoginActivity.this, lists);
	// listView.setAdapter(adapter);
	// if (wifiManager.isWifiEnabled()) {
	// btnClose.setVisibility(View.GONE);
	// btnOpen.setVisibility(View.VISIBLE);
	// tvSwitch.setText("已开启");
	// listView.setVisibility(View.VISIBLE);
	// llCable.setVisibility(View.GONE);
	// vLine.setVisibility(View.GONE);
	// llCommunication.setVisibility(View.GONE);
	// wifiAdmin.getConfiguration();
	// } else {
	// tvSwitch.setText("已关闭");
	// btnClose.setVisibility(View.VISIBLE);
	// btnOpen.setVisibility(View.GONE);
	// listView.setVisibility(View.GONE);
	// llCable.setVisibility(View.VISIBLE);
	// vLine.setVisibility(View.VISIBLE);
	// llCommunication.setVisibility(View.VISIBLE);
	// }
	// btnBack.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	//
	// alertDialog.dismiss();
	// }
	// });
	// btnOpen.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// wifiManager.setWifiEnabled(false);
	// btnClose.setVisibility(View.VISIBLE);
	// btnOpen.setVisibility(View.GONE);
	// tvSwitch.setText("已关闭");
	// llCable.setVisibility(View.VISIBLE);
	// vLine.setVisibility(View.VISIBLE);
	// llCommunication.setVisibility(View.VISIBLE);
	// listView.setVisibility(View.GONE);
	// }
	// });
	// btnClose.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// try {
	// openWifi();
	// Thread.sleep(1500);
	// } catch (InterruptedException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// if (wifiManager.isWifiEnabled()) {
	// btnClose.setVisibility(View.GONE);
	// btnOpen.setVisibility(View.VISIBLE);
	// tvSwitch.setText("已开启");
	// llCable.setVisibility(View.GONE);
	// vLine.setVisibility(View.GONE);
	// llCommunication.setVisibility(View.GONE);
	// listView.setVisibility(View.VISIBLE);
	// lists = wifiManager.getScanResults();
	// adapter = new MyAdapter(LoginActivity.this, lists);
	// listView.setAdapter(adapter);
	// }
	// }
	// });
	// btnCancel.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// llTwo.setVisibility(View.GONE);
	// listView.setVisibility(View.VISIBLE);
	// llCable.setVisibility(View.GONE);
	// vLine.setVisibility(View.GONE);
	// llCommunication.setVisibility(View.GONE);
	// }
	// });
	// btnShowPassword.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// if (showPassword) {
	// // 显示密码
	// showPassword = false;
	// // tvShowPassword.setText("隐藏密码");
	// etPassword
	// .setTransformationMethod(HideReturnsTransformationMethod
	// .getInstance());
	// etPassword.setSelection(etPassword.getText().toString()
	// .length());
	// btnShowPassword.setSelected(true);
	// } else {
	// // 隐藏密码
	// showPassword = true;
	// // tvShowPassword.setText("显示密码");
	// etPassword
	// .setTransformationMethod(PasswordTransformationMethod
	// .getInstance());
	// etPassword.setSelection(etPassword.getText().toString()
	// .length());
	// btnShowPassword.setSelected(false);
	// }
	//
	// }
	// });
	// btnBack.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// if (!listView.isShown()) {
	// llTwo.setVisibility(View.GONE);
	// listView.setVisibility(View.VISIBLE);
	// llCable.setVisibility(View.GONE);
	// vLine.setVisibility(View.GONE);
	// llCommunication.setVisibility(View.GONE);
	// } else {
	// alertDialog.dismiss();
	// }
	//
	// }
	// });
	// listView.setOnItemClickListener(new OnItemClickListener() {
	//
	// @Override
	// public void onItemClick(AdapterView<?> parent, View view,
	// int position, long id) {
	// selectedItem = view;
	// try {
	//
	// if (isFirst) {
	// item = view;
	// }
	// ScanResult list = lists.get(position);
	// wifiItemSSID = list.SSID;
	// wifiItemId = wifiAdmin.IsConfiguration("\""
	// + lists.get(position).SSID + "\"");
	// if (wifiItemId != -1) {
	// if (wifiAdmin.ConnectWifi(wifiItemId)) {
	//
	// try {
	// if (!isFirst) {
	// item.setBackgroundResource(R.color.white);
	// item = selectedItem;
	// }
	// selectedItem
	// .setBackgroundResource(R.color.blue_light);
	// // 连接已保存密码的WiFi
	// Toast.makeText(LoginActivity.this, "连接成功",
	// Toast.LENGTH_LONG).show();
	// isFirst = false;
	//
	// Thread.sleep(2000);
	// } catch (InterruptedException e) {
	// e.printStackTrace();
	// }
	//
	// }
	// } else {
	// llTwo.setVisibility(View.VISIBLE);
	// listView.setVisibility(View.GONE);
	// llCable.setVisibility(View.GONE);
	// vLine.setVisibility(View.GONE);
	// llCommunication.setVisibility(View.GONE);
	// llPassword.setVisibility(View.VISIBLE);
	// llShowPassword.setVisibility(View.VISIBLE);
	// btnReset.setVisibility(View.GONE);
	// btnOk.setVisibility(View.VISIBLE);
	// tvWalan.setText(wifiItemSSID);
	// etPassword.setText("");
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	// });
	// listView.setOnItemLongClickListener(new OnItemLongClickListener() {
	//
	// @Override
	// public boolean onItemLongClick(AdapterView<?> parent, View view,
	// int position, long id) {
	// try {
	//
	//
	// selectedMenu=view;
	// ScanResult list=lists.get(position);
	// wifiItemSSID=list.SSID;
	// wifiItemId=wifiAdmin.IsConfiguration("\""
	// + lists.get(position).SSID + "\"");
	// if(wifiItemId !=-1){
	// if(wifiAdmin.ConnectWifi(wifiItemId)){
	// llTwo.setVisibility(View.VISIBLE);
	// listView.setVisibility(View.GONE);
	// llCable.setVisibility(View.GONE);
	// vLine.setVisibility(View.GONE);
	// llCommunication.setVisibility(View.GONE);
	// llPassword.setVisibility(View.GONE);
	// llShowPassword.setVisibility(View.GONE);
	// btnReset.setVisibility(View.VISIBLE);
	// btnOk.setVisibility(View.GONE);
	// tvWalan.setText(wifiItemSSID);
	// }
	// }
	// else{
	// llTwo.setVisibility(View.VISIBLE);
	// listView.setVisibility(View.GONE);
	// llCable.setVisibility(View.GONE);
	// vLine.setVisibility(View.GONE);
	// llCommunication.setVisibility(View.GONE);
	// llPassword.setVisibility(View.VISIBLE);
	// llShowPassword.setVisibility(View.VISIBLE);
	// btnReset.setVisibility(View.GONE);
	// btnOk.setVisibility(View.VISIBLE);
	// tvWalan.setText(wifiItemSSID);
	// etPassword.setText("");
	// }
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// return true;
	// }
	// });
	// btnOk.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// String password=etPassword.getText().toString();
	// if(password!=null){
	// int netId=wifiAdmin.AddWifiConfig(lists,wifiItemSSID, password);
	// Log.i("WifiPswDialog",String.valueOf(netId));
	// if(netId!=-1){
	// wifiAdmin.getConfiguration();
	// if(wifiAdmin.ConnectWifi(netId)){
	// llTwo.setVisibility(View.GONE);
	// listView.setVisibility(View.VISIBLE);
	// llCable.setVisibility(View.GONE);
	// vLine.setVisibility(View.GONE);
	// llCommunication.setVisibility(View.GONE);
	// try {
	// if (!isFirst) {
	// item.setBackgroundResource(R.color.white);
	// item=selectedItem;
	// }
	// selectedItem.setBackgroundResource(R.color.blue_light);
	// Toast.makeText(LoginActivity.this, "连接成功",
	// Toast.LENGTH_LONG).show();
	// isFirst=false;
	// Thread.sleep(2000);
	// } catch (InterruptedException e) {
	// e.printStackTrace();
	// }
	// }
	// }else{
	// Toast.makeText(LoginActivity.this, "网络连接错误",
	// Toast.LENGTH_SHORT).show();
	// }
	// }
	// }
	// });
	// btnReset.setOnClickListener(new OnClickListener() {
	// @Override
	// public void onClick(View v) {
	// wifiManager.removeNetwork(wifiItemId);
	// //wifiManager.saveConfiguration();
	// wifiAdmin.getConfiguration();
	// Toast.makeText(LoginActivity.this, "连接取消",
	// Toast.LENGTH_SHORT).show();
	// selectedMenu.setBackgroundResource(R.color.white);
	// llTwo.setVisibility(View.GONE);
	// listView.setVisibility(View.VISIBLE);
	// llCable.setVisibility(View.GONE);
	// vLine.setVisibility(View.GONE);
	// llCommunication.setVisibility(View.GONE);
	//
	//
	// }
	// });
	//
	// }
	//
	// protected void openWifi() {
	// while (!wifiManager.isWifiEnabled()) {
	// wifiManager.setWifiEnabled(true);
	// if (wifiManager.isWifiEnabled()) {
	// break;
	// }
	// }
	// }
	@Override
	protected void onDestroy() {
		System.exit(0);
		super.onDestroy();
	}

	private void exit() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("提示");
		builder.setMessage("确定要退出程序吗");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

				finish();

			}
		});
		builder.setNegativeButton("取消", null);
		builder.show();

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {

	}

	@Override
	public void afterTextChanged(Editable s) {
		if (etUserName.getText().toString().length() > 0) {
			btnCircle01.setVisibility(View.VISIBLE);
			rlUser.setBackground(getResources().getDrawable(
					R.drawable.input_box_hei));
			btnContact.setBackground(getResources().getDrawable(
					R.drawable.the_contact_hei));
			if ("user".equals(etUserName.getText().toString())) {
				btnCircle01.setBackground(getResources().getDrawable(
						R.drawable.correct));
			} else {
				btnCircle01.setBackground(getResources().getDrawable(
						R.drawable.error));
			}
		} else {
			btnCircle01.setVisibility(View.GONE);
			rlUser.setBackground(getResources().getDrawable(
					R.drawable.input_box));
			btnContact.setBackground(getResources().getDrawable(
					R.drawable.the_contact));
		}
		if (etPassword.getText().toString().length() > 0) {
			btnCircle02.setVisibility(View.VISIBLE);
			rlPassword.setBackground(getResources().getDrawable(
					R.drawable.input_box_hei));
			btnLock.setBackground(getResources().getDrawable(
					R.drawable.the_lock_hei));
			if ("123456".equals(etPassword.getText().toString())) {
				btnCircle02.setBackground(getResources().getDrawable(
						R.drawable.correct));
			} else {
				btnCircle02.setBackground(getResources().getDrawable(
						R.drawable.error));
			}
		} else {
			btnCircle02.setVisibility(View.GONE);
			rlPassword.setBackground(getResources().getDrawable(
					R.drawable.input_box));
			btnLock.setBackground(getResources().getDrawable(
					R.drawable.the_lock));
		}

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

	// @Override
	// public boolean dispatchKeyEvent(KeyEvent event) {
	// if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
	//
	// /* 隐藏软键盘 */
	// InputMethodManager inputMethodManager = (InputMethodManager)
	// getSystemService(Context.INPUT_METHOD_SERVICE);
	// if (inputMethodManager.isActive()) {
	// inputMethodManager.hideSoftInputFromWindow(this
	// .getCurrentFocus().getWindowToken(), 0);
	// }
	//
	// return true;
	// }
	//
	// return super.dispatchKeyEvent(event);
	//
	// }

}
