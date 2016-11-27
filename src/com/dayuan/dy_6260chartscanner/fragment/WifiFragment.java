package com.dayuan.dy_6260chartscanner.fragment;

import java.util.List;

import android.app.Fragment;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.dayuan.dy_6260chartscanner.R;
import com.dayuan.dy_6260chartscanner.util.WifiAdmin;

public class WifiFragment extends Fragment implements OnClickListener,
		TextWatcher {
	private Context context;
	View view;
	private Button btnCancel, btnOk, btnReturn, btnReset;
	private TextView tvWalan, tvShowPassword;
	private WifiManager wifiManager;
	List<ScanResult> lists;
	private MyAdapter adapter;
	private ListView listView;
	private RelativeLayout rlOne;
	private LinearLayout llTwo;
	private LinearLayout llPassword;
	private LinearLayout llShowPassword;
	private boolean isChecked = true;
	private EditText etPassword;
	private WifiAdmin wifiAdmin;
	private Switch wifiSwitch;
	// private TextView tvConnectSuccess;
	String password;
	String wifiItemSSID;
	// int netId;
	private Button btnShowPassword;
	private boolean showPassword = true;
	private int netId;

	// private
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.fragment_wifi, container, false);
		wifiAdmin = new WifiAdmin(getActivity());
		init();
		return view;
	}

	private void init() {
		listView = (ListView) view.findViewById(R.id.list);
		tvShowPassword = (TextView) view.findViewById(R.id.tv_show_password);
		btnShowPassword = (Button) view.findViewById(R.id.btn_show_password);
		llPassword = (LinearLayout) view.findViewById(R.id.ll_password);
		llShowPassword = (LinearLayout) view
				.findViewById(R.id.ll_show_password);
		btnReturn = (Button) view.findViewById(R.id.btn_return);
		btnCancel = (Button) view.findViewById(R.id.btn_cancel);
		btnOk = (Button) view.findViewById(R.id.btn_connect);
		btnReset = (Button) view.findViewById(R.id.btn_reset);
		tvWalan = (TextView) view.findViewById(R.id.tv_wlan_item);
		wifiSwitch = (Switch) view.findViewById(R.id.sw_wifi);
		rlOne = (RelativeLayout) view.findViewById(R.id.relativeLayout01);
		llTwo = (LinearLayout) view.findViewById(R.id.linearLayout02);
		etPassword = (EditText) view.findViewById(R.id.et_password);
		wifiManager = (WifiManager) getActivity().getSystemService(
				Context.WIFI_SERVICE);
		// openWifi();

		lists = wifiManager.getScanResults();
		adapter = new MyAdapter(getActivity(), lists);
		listView.setAdapter(adapter);
		if (wifiManager.isWifiEnabled()) {
			wifiSwitch.setChecked(true);
			listView.setVisibility(View.VISIBLE);
			wifiAdmin.getConfiguration();
		} else {
			wifiSwitch.setChecked(false);
			listView.setVisibility(View.GONE);
		}
		// wifiAdmin.getConfiguration();
		btnCancel.setOnClickListener(this);
		btnShowPassword.setOnClickListener(this);
		btnReturn.setOnClickListener(this);
		etPassword.addTextChangedListener(this);
		setListener();
	}

	public void openWifi() {
		if (!wifiManager.isWifiEnabled()) {
			while (!wifiManager.isWifiEnabled()) {
				wifiManager.setWifiEnabled(true);
				if (wifiManager.isWifiEnabled()) {
					break;
				}
			}
		}
	}

	// ============
	// /** * 连接指定Id的WIFI * * @param wifiId * @return */
	// public boolean ConnectWifi(int wifiId) {
	// for (int i = 0; i < wifiConfigList.size(); i++) {
	// WifiConfiguration wifi = wifiConfigList.get(i);
	// if (wifi.networkId == wifiId) {
	// while (!(mWifiManager.enableNetwork(wifiId, true))) {// 激活该Id，建立连接
	// Log.i("ConnectWifi",
	// String.valueOf(wifiConfigList.get(wifiId).status));//
	// status:0--已经连接，1--不可连接，2--可以连接
	// }
	// return true;
	// }
	// }
	// return false;
	// }
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_cancel:
			llTwo.setVisibility(View.GONE);
			rlOne.setVisibility(View.VISIBLE);
			break;
		case R.id.btn_show_password:
			if (showPassword) {
				// 显示密码
				showPassword = false;
				// tvShowPassword.setText("隐藏密码");
				etPassword
						.setTransformationMethod(HideReturnsTransformationMethod
								.getInstance());
				etPassword.setSelection(etPassword.getText().toString()
						.length());
				btnShowPassword.setSelected(false);
			} else {
				// 隐藏密码
				showPassword = true;
				// tvShowPassword.setText("显示密码");
				etPassword.setTransformationMethod(PasswordTransformationMethod
						.getInstance());
				etPassword.setSelection(etPassword.getText().toString()
						.length());
				btnShowPassword.setSelected(true);
			}
			break;
		case R.id.btn_return:
			if (!rlOne.isShown()) {
				llTwo.setVisibility(View.GONE);
				rlOne.setVisibility(View.VISIBLE);
			} else {
				// Intent intent=new Intent(getActivity(),BaseActivity.class);
				// startActivity(intent);
				getActivity().finish();
			}
			break;
		}
	}

	View selectedItem;
	View selectedMenu;
	View item;
	Boolean isFirst = true;
	int wifiItemId;

	private void setListener() {
		wifiSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					openWifi();
					// Thread.sleep(1500);
					new Thread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							wifiManager.setWifiEnabled(true);
							try {
								Thread.sleep(1500);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							lists = wifiManager.getScanResults();

							getActivity().runOnUiThread(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub
									adapter = new MyAdapter(getActivity(),
											lists);
									listView.setAdapter(adapter);
									adapter.notifyDataSetChanged();
								}
							});
						}
					}).start();
					;

					listView.setVisibility(View.VISIBLE);

				} else {
					listView.setVisibility(View.GONE);
					wifiManager.setWifiEnabled(false);
				}
			}
		});
		listView.setOnItemClickListener(new OnItemClickListener() {
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
										.setBackgroundResource(R.color.blue);
								// 连接已保存密码的WiFi
								Toast.makeText(getActivity(), "连接成功",
										Toast.LENGTH_LONG).show();
								isFirst = false;

								Thread.sleep(2000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}

						}
					} else {
						llTwo.setVisibility(View.VISIBLE);
						rlOne.setVisibility(View.GONE);
						llPassword.setVisibility(View.VISIBLE);
						llShowPassword.setVisibility(View.VISIBLE);
						btnReset.setVisibility(View.GONE);
						btnOk.setVisibility(View.VISIBLE);
						tvWalan.setText(wifiItemSSID);
						etPassword.setText("");
						btnShowPassword.setSelected(true);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				try {

					selectedMenu = view;
					ScanResult list = lists.get(position);
					wifiItemSSID = list.SSID;
					wifiItemId = wifiAdmin.IsConfiguration("\""
							+ lists.get(position).SSID + "\"");
					if (wifiItemId != -1) {
						if (wifiAdmin.ConnectWifi(wifiItemId)) {
							llTwo.setVisibility(View.VISIBLE);
							rlOne.setVisibility(View.GONE);
							llPassword.setVisibility(View.GONE);
							llShowPassword.setVisibility(View.GONE);
							btnReset.setVisibility(View.VISIBLE);
							btnOk.setVisibility(View.GONE);
							tvWalan.setText(wifiItemSSID);
						}
					} else {
						llTwo.setVisibility(View.VISIBLE);
						rlOne.setVisibility(View.GONE);
						llPassword.setVisibility(View.VISIBLE);
						llShowPassword.setVisibility(View.VISIBLE);
						btnReset.setVisibility(View.GONE);
						btnOk.setVisibility(View.VISIBLE);
						tvWalan.setText(wifiItemSSID);
						etPassword.setText("");
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

				return true;
			}
		});
		btnOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				password = etPassword.getText().toString();
				if (password != null) {
					netId = wifiAdmin.AddWifiConfig(lists, wifiItemSSID,
							password);
					Log.i("WifiPswDialog", String.valueOf(netId));
					if (netId != -1) {
						wifiAdmin.getConfiguration();
						if (wifiAdmin.ConnectWifi(netId)) {
							llTwo.setVisibility(View.GONE);
							rlOne.setVisibility(View.VISIBLE);
							try {
								if (!isFirst) {
									item.setBackgroundResource(R.color.white);
									item = selectedItem;
								}
								selectedItem
										.setBackgroundResource(R.color.blue_light);
								Toast.makeText(getActivity(), "连接成功",
										Toast.LENGTH_LONG).show();
								isFirst = false;
								Thread.sleep(2000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					} else {
						Toast.makeText(getActivity(), "网络连接错误",
								Toast.LENGTH_SHORT).show();
					}
				}
			}
		});

		btnReset.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				wifiManager.removeNetwork(wifiItemId);
				// wifiManager.saveConfiguration();
				wifiAdmin.getConfiguration();
				Toast.makeText(getActivity(), "连接取消", Toast.LENGTH_SHORT)
						.show();
				selectedMenu.setBackgroundResource(R.color.white);
				llTwo.setVisibility(View.GONE);
				rlOne.setVisibility(View.VISIBLE);

			}
		});
	}

	public class MyAdapter extends BaseAdapter {

		LayoutInflater inflater;
		List<ScanResult> list;

		public MyAdapter(Context context, List<ScanResult> list) {
			this.inflater = LayoutInflater.from(context);
			this.list = list;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;
			view = inflater.inflate(R.layout.wifi_item, null);
			ScanResult scanResult = list.get(position);
			TextView tvWifiItem = (TextView) view
					.findViewById(R.id.tv_show_item);
			tvWifiItem.setText(scanResult.SSID);
			ImageView imageView = (ImageView) view
					.findViewById(R.id.iv_wifi_image);
			TextView tvConnectState = (TextView) view
					.findViewById(R.id.tv_connect_state);
			if (Math.abs(scanResult.level) >= 100) {
				imageView.setImageDrawable(view.getResources().getDrawable(
						R.drawable.wifi_xinhao_25));
			} else if (Math.abs(scanResult.level) > 80
					&& Math.abs(scanResult.level) < 100) {
				imageView.setImageDrawable(view.getResources().getDrawable(
						R.drawable.wifi_xinhao_50));
			} else if (Math.abs(scanResult.level) > 70
					&& Math.abs(scanResult.level) < 100) {
				imageView.setImageDrawable(view.getResources().getDrawable(
						R.drawable.wifi_xinhao_50));
			} else if (Math.abs(scanResult.level) > 50
					&& Math.abs(scanResult.level) < 70) {
				imageView.setImageDrawable(view.getResources().getDrawable(
						R.drawable.wifi_xinhao_75));
			} else {
				imageView.setImageDrawable(view.getResources().getDrawable(
						R.drawable.wifi_xinhao));
			}
			return view;
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterTextChanged(Editable s) {
		if (etPassword.getText().toString().length() > 6) {
			btnOk.setTextColor(getActivity().getResources().getColor(
					R.color.black_dark));
			btnOk.setEnabled(true);
		} else {
			btnOk.setTextColor(getActivity().getResources().getColor(
					R.color.black_light));
			btnOk.setEnabled(false);

		}

	}

}
