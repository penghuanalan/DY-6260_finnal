package com.dayuan.dy_6260chartscanner.util;

import java.util.List;

import com.dayuan.dy_6260chartscanner.R;
import com.dayuan.dy_6260chartscanner.activity.LoginActivity;
import com.dayuan.dy_6260chartscanner.adapter.MyAdapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class NetworkUtil {

	/**
	 * 检查有没有网络，没有显示dialog
	 * 
	 * @param activity
	 */
	
	private	static Button	btnCableClose;
	private	static Button	btnCableOpen;
	private	static Button	btnCommunication; 
	private	static EditText etCommuincation;
	static View selectedItem;
	static String wifiItemSSID;
	static TextView tvShowPassword;
	static LinearLayout llPassword;
	static LinearLayout llShowPassword;
	static EditText et_Password;
	private static boolean showPassword = true;
	static View selectedMenu;
	static View item;
	static Boolean isFirst = true;
	static int wifiItemId;
	private static ListView listView;
	private static MyAdapter adapter;
	private ConnectivityManager mConnectivity;
	private static  WifiManager wifiManager;
	private static WifiAdmin wifiAdmin;
	static List<ScanResult> lists;
	private static TextView tvWalan;
	//private static RelativeLayout rlWifi;
	public static void checkNetworkState(final Context context) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		final AlertDialog alertDialog = builder.create();
		alertDialog.setCanceledOnTouchOutside(false);
		alertDialog.show();
        // 以下两行代码是对话框的EditText点击后不能显示输入法的
		alertDialog.getWindow().clearFlags(
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		alertDialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		// 自定义UI
		Window window = alertDialog.getWindow();
		window.setLayout(RelativeLayout.LayoutParams.FILL_PARENT,
				RelativeLayout.LayoutParams.FILL_PARENT);
		View dialogView = View.inflate(context,
				R.layout.networking_parameter_window, null);
		window.setContentView(dialogView);
		final LinearLayout llCable = (LinearLayout) window
				.findViewById(R.id.ll_cable);
		final View vLine = window.findViewById(R.id.v_line02);
		final LinearLayout llCommunication = (LinearLayout) window
				.findViewById(R.id.ll_communication);
		final LinearLayout llTwo = (LinearLayout) window
				.findViewById(R.id.linearLayout02);
		listView = (ListView) window.findViewById(R.id.lv_wifi);
		final TextView tvSwitch = (TextView) window.findViewById(R.id.tv_show_state);
		final TextView tvWalan = (TextView) window.findViewById(R.id.tv_wlan_item);
		final Button btnOpen = (Button) window.findViewById(R.id.btn_open);
		final Button btnClose = (Button) window.findViewById(R.id.btn_guan);
		Button btnCancel = (Button) window.findViewById(R.id.btn_cancel);
		final Button btnOk = (Button) window.findViewById(R.id.btn_connect);
		final Button btnReset = (Button) window.findViewById(R.id.btn_reset);
		TextView tvShowPassword = (TextView) window.findViewById(R.id.tv_show_password);
		final Button btnShowPassword = (Button) window.findViewById(R.id.btn_show_password);
		llPassword = (LinearLayout) window.findViewById(R.id.ll_password);
		llShowPassword = (LinearLayout) window
				.findViewById(R.id.ll_show_password);
		et_Password = (EditText) window.findViewById(R.id.et_password);
		Button btnBack = (Button) window.findViewById(R.id.btn_back);
		wifiManager=(WifiManager) (context).getSystemService(Context.WIFI_SERVICE);
		wifiAdmin = new WifiAdmin(context);
		// openWifi();
		lists = wifiManager.getScanResults();
		adapter = new MyAdapter(context, lists);
		listView.setAdapter(adapter);
		if (wifiManager.isWifiEnabled()) {
			btnClose.setVisibility(View.GONE);
			btnOpen.setVisibility(View.VISIBLE);
			tvSwitch.setText("已开启");
			listView.setVisibility(View.VISIBLE);
			llCable.setVisibility(View.GONE);
			vLine.setVisibility(View.GONE);
			llCommunication.setVisibility(View.GONE);
			wifiAdmin.getConfiguration();
		} else {
			tvSwitch.setText("已关闭");
			btnClose.setVisibility(View.VISIBLE);
			btnOpen.setVisibility(View.GONE);
			listView.setVisibility(View.GONE);
			llCable.setVisibility(View.VISIBLE);
			vLine.setVisibility(View.VISIBLE);
			llCommunication.setVisibility(View.VISIBLE);
		}
		btnBack.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				alertDialog.dismiss();
			}

		});
		btnOpen.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				wifiManager.setWifiEnabled(false);
				btnClose.setVisibility(View.VISIBLE);
				btnOpen.setVisibility(View.GONE);
				tvSwitch.setText("已关闭");
				llCable.setVisibility(View.VISIBLE);
				vLine.setVisibility(View.VISIBLE);
				llCommunication.setVisibility(View.VISIBLE);
				listView.setVisibility(View.GONE);
			}
		});
		btnClose.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					openWifi();
					Thread.sleep(1500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (wifiManager.isWifiEnabled()) {
					btnClose.setVisibility(View.GONE);
					btnOpen.setVisibility(View.VISIBLE);
					tvSwitch.setText("已开启");
					llCable.setVisibility(View.GONE);
					vLine.setVisibility(View.GONE);
					llCommunication.setVisibility(View.GONE);
					listView.setVisibility(View.VISIBLE);
					lists = wifiManager.getScanResults();
					adapter = new MyAdapter(context, lists);
					listView.setAdapter(adapter);
				}
			}
		});
		btnCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				llTwo.setVisibility(View.GONE);
				listView.setVisibility(View.VISIBLE);
				llCable.setVisibility(View.GONE);
				vLine.setVisibility(View.GONE);
				llCommunication.setVisibility(View.GONE);
			}
		});
		btnShowPassword.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (showPassword) {
					// 显示密码
					showPassword = false;
					// tvShowPassword.setText("隐藏密码");
					et_Password
							.setTransformationMethod(HideReturnsTransformationMethod
									.getInstance());
					et_Password.setSelection(et_Password.getText().toString()
							.length());
					btnShowPassword.setSelected(true);
				} else {
					// 隐藏密码
					showPassword = true;
					// tvShowPassword.setText("显示密码");
					et_Password
							.setTransformationMethod(PasswordTransformationMethod
									.getInstance());
					et_Password.setSelection(et_Password.getText().toString()
							.length());
					btnShowPassword.setSelected(false);
				}

			}
		});
		btnBack.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!listView.isShown()) {
					llTwo.setVisibility(View.GONE);
					listView.setVisibility(View.VISIBLE);
					llCable.setVisibility(View.GONE);
					vLine.setVisibility(View.GONE);
					llCommunication.setVisibility(View.GONE);
				} else {
					alertDialog.dismiss();
				}

			}
		});
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				selectedItem = view;
				try {

					if (isFirst) {
						item = view;
					}
					ScanResult list = lists.get(position);
					wifiItemSSID = list.SSID;
					wifiItemId = wifiAdmin.IsConfiguration("\""
							+ lists.get(position).SSID + "\"");
					if (wifiItemId != -1) {
						if (wifiAdmin.ConnectWifi(wifiItemId)) {

							try {
								if (!isFirst) {
									item.setBackgroundResource(R.color.white);
									item = selectedItem;
								}
								selectedItem
										.setBackgroundResource(R.color.blue_light);
								// 连接已保存密码的WiFi
								Toast.makeText(context, "连接成功",
										Toast.LENGTH_LONG).show();
								isFirst = false;

								Thread.sleep(2000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}

						}
					} else {
						llTwo.setVisibility(View.VISIBLE);
						listView.setVisibility(View.GONE);
						llCable.setVisibility(View.GONE);
						vLine.setVisibility(View.GONE);
						llCommunication.setVisibility(View.GONE);
						llPassword.setVisibility(View.VISIBLE);
						llShowPassword.setVisibility(View.VISIBLE);
						btnReset.setVisibility(View.GONE);
						btnOk.setVisibility(View.VISIBLE);
						tvWalan.setText(wifiItemSSID);
						et_Password.setText("");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				try {
					
				
				selectedMenu=view;
				ScanResult list=lists.get(position);
			    wifiItemSSID=list.SSID;
			    wifiItemId=wifiAdmin.IsConfiguration("\""
                        + lists.get(position).SSID + "\"");
				if(wifiItemId !=-1){
					if(wifiAdmin.ConnectWifi(wifiItemId)){
						   llTwo.setVisibility(View.VISIBLE);
						   listView.setVisibility(View.GONE);
							llCable.setVisibility(View.GONE);
							vLine.setVisibility(View.GONE);
							llCommunication.setVisibility(View.GONE);
				    		llPassword.setVisibility(View.GONE);
				    		llShowPassword.setVisibility(View.GONE);
				    		btnReset.setVisibility(View.VISIBLE);
				    		btnOk.setVisibility(View.GONE);
				    		tvWalan.setText(wifiItemSSID);
					}
					}
				else{
					llTwo.setVisibility(View.VISIBLE);
					listView.setVisibility(View.GONE);
					llCable.setVisibility(View.GONE);
					vLine.setVisibility(View.GONE);
					llCommunication.setVisibility(View.GONE);
					llPassword.setVisibility(View.VISIBLE);
		    		llShowPassword.setVisibility(View.VISIBLE);
					btnReset.setVisibility(View.GONE);
					btnOk.setVisibility(View.VISIBLE);
					tvWalan.setText(wifiItemSSID);
					et_Password.setText("");
				}
						
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				return true;
			}
		});
btnOk.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			String	password=et_Password.getText().toString();
				 if(password!=null){
			    	int netId=wifiAdmin.AddWifiConfig(lists,wifiItemSSID, password);
			    		Log.i("WifiPswDialog",String.valueOf(netId));
			    		if(netId!=-1){
			    			wifiAdmin.getConfiguration();
			    			if(wifiAdmin.ConnectWifi(netId)){
			    				llTwo.setVisibility(View.GONE);
			    				listView.setVisibility(View.VISIBLE);
			    				llCable.setVisibility(View.GONE);
								vLine.setVisibility(View.GONE);
								llCommunication.setVisibility(View.GONE);
			    		    	try {
			    		    		if (!isFirst) {
					    				item.setBackgroundResource(R.color.white);
					    				item=selectedItem;
									}
			    		    	selectedItem.setBackgroundResource(R.color.blue_light);
			    		    	Toast.makeText(context, "连接成功",
		                                Toast.LENGTH_LONG).show();
			    		    	isFirst=false;
									Thread.sleep(2000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
			    			}
			    		}else{
			    			Toast.makeText(context, "网络连接错误",
	                                Toast.LENGTH_SHORT).show();	
			    		}
			    	}
			}
		});
    btnReset.setOnClickListener(new View.OnClickListener() {
	@Override
	public void onClick(View v) {
				wifiManager.removeNetwork(wifiItemId);
	    		//wifiManager.saveConfiguration();
	    		wifiAdmin.getConfiguration();
	    		Toast.makeText(context, "连接取消",
                      Toast.LENGTH_SHORT).show();
	    		selectedMenu.setBackgroundResource(R.color.white);
	    		llTwo.setVisibility(View.GONE);
	    		listView.setVisibility(View.VISIBLE);
				llCable.setVisibility(View.GONE);
				vLine.setVisibility(View.GONE);
				llCommunication.setVisibility(View.GONE);
		    	
			
	}
});
    et_Password.addTextChangedListener(new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			if(et_Password.getText().toString().length()>6){
				btnOk.setTextColor(context.getResources().getColor(R.color.black_dark));
				btnOk.setEnabled(true);
			}else{
				btnOk.setTextColor(context.getResources().getColor(R.color.black_light));
				btnOk.setEnabled(false);
			 }		
		}
	});
	}
	protected static void openWifi() {
		while (!wifiManager.isWifiEnabled()) {
			wifiManager.setWifiEnabled(true);
			if (wifiManager.isWifiEnabled()) {
				break;
			}
		}
		
	}

	
}
