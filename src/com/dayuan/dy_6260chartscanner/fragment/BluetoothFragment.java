package com.dayuan.dy_6260chartscanner.fragment;

import java.io.IOException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import android.app.AlertDialog;
import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.dayuan.dy_6260chartscanner.R;

public class BluetoothFragment extends Fragment implements OnClickListener,OnItemClickListener
		 {
	// 该UUID表示串口服务
	static final String SPP_UUID = "00001101-0000-1000-8000-00805F9B34FB";
	private static final int RESULT_OK = 10;
	private static final int RESULT_CANCELED = 20;
	private Button btnReturn;
	private ListView lvBlueTooth;
	private BluetoothAdapter adapter;;
	private List<String> btList = new ArrayList<String>(); // 用于保存已配对的蓝牙名称
	Set<BluetoothDevice> devices;
	private BluetoothDevice btDevice;
	private BTAdapter btAdapter;
	public static BluetoothSocket btSocket;
	LocalBroadcastManager broadcastManager;
	private BroadcastReceiver searchDevices;
	private ImageView ivFlush;
	private int cancelBond=0;
	private Switch blueToothSwitch;
	View view;
	Handler handler= new Handler();
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_bluetooth, container, false);
		init();
		return view;
	}

	

	private void init() {
		blueToothSwitch=(Switch) view.findViewById(R.id.sw_blue_tooth);
		btnReturn = (Button) view.findViewById(R.id.btn_return);
		lvBlueTooth = (ListView) view.findViewById(R.id.list);
		ivFlush=(ImageView) view.findViewById(R.id.iv_flush);
		btnReturn.setOnClickListener(this);
		ivFlush.setOnClickListener(this);
		btAdapter = new BTAdapter(getActivity(), btList);
		lvBlueTooth.setAdapter(btAdapter);
		lvBlueTooth.setOnItemClickListener(this);
		// 检查设备是否支持蓝牙
		adapter = BluetoothAdapter.getDefaultAdapter();
		IntentFilter intent = new IntentFilter();
		intent.addAction(BluetoothDevice.ACTION_FOUND);// 用BroadcastReceiver来取得搜索结果
		intent.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
		intent.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
		intent.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
		searchDevices = new BroadcastReceiver() {
			public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();
				BluetoothDevice device = null;
				// 搜索设备时，取得设备的MAC地址
				if (BluetoothDevice.ACTION_FOUND.equals(action)) {
					device = intent
							.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
					if (device.getBondState() == BluetoothDevice.BOND_NONE) {
						String strName = device.getName() + "\n" + "|"
								+ device.getAddress();
						Log.i("strName", strName);
						if (btList.indexOf(strName) == -1)// 防止重复添加
							btList.add(strName); // 获取设备名称和mac地址
					}
					btAdapter = new BTAdapter(getActivity(), btList);
					lvBlueTooth.setAdapter(btAdapter);
					btAdapter.notifyDataSetChanged();
				} else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED
						.equals(action)) {
					device = intent
							.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
					switch (device.getBondState()) {
					case BluetoothDevice.BOND_BONDING:
						break;
					case BluetoothDevice.BOND_BONDED:
						connect(device);// 连接设备
						break;
					case BluetoothDevice.BOND_NONE:
					default:
						break;
					}
				}

			}
		};
		getActivity().registerReceiver(searchDevices, intent);
		if (adapter.isEnabled()) {
			blueToothSwitch.setChecked(true);
			ivFlush.setVisibility(View.VISIBLE);
			// 获取所有已经绑定的蓝牙设备
			Set<BluetoothDevice> devices = adapter.getBondedDevices();
			if (devices.size() > 0) {
				for (BluetoothDevice device : devices) {
					String str = device.getName() + "\n" + "|"
							+ device.getAddress() + "|\n" + "已配对";
					Log.i("btList", str);
					btList.add(str);
					btAdapter.notifyDataSetChanged();
				}
			}
			// 开始搜索蓝牙设备,搜索到的蓝牙设备通过广播返回
			adapter.startDiscovery();
		}
		blueToothSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					adapter = BluetoothAdapter.getDefaultAdapter();
					ivFlush.setVisibility(View.VISIBLE);
					
					
					handler.postDelayed(new Runnable() {

						@Override
						public void run() {
							if (adapter != null) {
								if (!adapter.isEnabled()) {
									adapter.enable();
								}

								while (!adapter.isEnabled()) {

									if (adapter.isEnabled()) {
										break;
									}
								}
								// 获取所有已经绑定的蓝牙设备
								Set<BluetoothDevice> devices = adapter.getBondedDevices();
								Log.i("lstD", "" + devices.size());
								if (devices.size() > 0) {
									for (BluetoothDevice device : devices) {
										String str = device.getName() + "\n" + "|"
												+ device.getAddress() + "|\n" + "已配对";
										Log.i("btList", str);
										btList.add(str);
										btAdapter.notifyDataSetChanged();
									}
								}
								// 开始搜索蓝牙设备,搜索到的蓝牙设备通过广播返回
								adapter.startDiscovery();

							}

						}
					}, 10);	
				}else{
					if (adapter != null) {
						if (adapter.isEnabled()) {
							if (adapter.isDiscovering())
								adapter.cancelDiscovery();
							adapter.disable();
							btList.clear();
							lvBlueTooth.setAdapter(btAdapter);
							btAdapter.notifyDataSetChanged();
						}
					}
					ivFlush.setVisibility(View.INVISIBLE);
				}
			}
		});
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_flush:
			btList.clear();
				// 获取所有已经绑定的蓝牙设备
				Set<BluetoothDevice> devices = adapter.getBondedDevices();
				Log.i("lstD", "" + devices.size());
				if (devices.size() > 0) {
					for (BluetoothDevice device : devices) {
						String str = device.getName() + "\n" + "|"
								+ device.getAddress() + "|\n" + "已配对";
						Log.i("btList", str);
						btList.add(str);
						btAdapter.notifyDataSetChanged();
					}
				}
				// 开始搜索蓝牙设备,搜索到的蓝牙设备通过广播返回
				adapter.startDiscovery();

			break;
		case R.id.btn_return:
					if (adapter.isDiscovering())
						adapter.cancelDiscovery();
			if(cancelBond==1){
				try {
					Method removeBondMethod = BluetoothDevice.class
							.getMethod("removeBond");
					boolean returnValue = (Boolean) removeBondMethod
							.invoke(btDevice);
					if (returnValue == true) {
						getActivity().finish();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if(!adapter.isDiscovering()){
			getActivity().finish();
			}
			break;
		}

	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		String btDeviceName=null;
		if (adapter.isDiscovering())
			adapter.cancelDiscovery();
		 btDeviceName = btList.get(position);
		 String[] values = btDeviceName.split("\\|");
		 String address = values[1];
		 btDevice = adapter.getRemoteDevice(address);
		try {
			Boolean returnValue = false;
			if (btDevice.getBondState() == BluetoothDevice.BOND_NONE) {
				// 利用反射方法调用BluetoothDevice.createBond(BluetoothDevice
				// remoteDevice);
				Toast.makeText(getActivity(), "正在配对，请稍后...", 1000).show();
				Method createBondMethod = BluetoothDevice.class
						.getMethod("createBond");
				returnValue = (Boolean) createBondMethod.invoke(btDevice);
				if(returnValue==true){
//					btList.remove(position);
					btAdapter.notifyDataSetChanged();
				}

			} else if (btDevice.getBondState() == BluetoothDevice.BOND_BONDED) {
				Toast.makeText(getActivity(), "正在连接，请稍后...", 1000).show();
				connect(btDevice);
			}else if(btDevice.getBondState()==BluetoothDevice.BOND_BONDING){
				cancelBond=1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void connect(BluetoothDevice btDev) {
		UUID uuid = UUID.fromString(SPP_UUID);
		try {
			btSocket = btDev.createRfcommSocketToServiceRecord(uuid);
			btSocket.connect();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	@Override
	public void onPause() {
		getActivity().unregisterReceiver(searchDevices);
		super.onPause();
	}

	@Override
	public void onDestroy() {
		try {
			if (btSocket != null)
				btSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		super.onDestroy();
		// android.os.Process.killProcess(android.os.Process.myPid());
	}

	class BTAdapter extends BaseAdapter {
		LayoutInflater inflater;
		private List<String> btList;

		public BTAdapter(Context context, List<String> btList) {
			this.inflater = LayoutInflater.from(getActivity());
			this.btList = btList;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return btList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			View view = null;
			view = inflater.inflate(R.layout.bluetooth_item, null);
			TextView tvBluetooth = (TextView) view
					.findViewById(R.id.tv_show_item);
			ImageView ivCancel=(ImageView) view.findViewById(R.id.iv_lock);
			
			final String btDevice = btList.get(position);
			tvBluetooth.setText(btDevice);
     		String[] values = btDevice.split("\\|");
     		String address = values[1];
     		final BluetoothDevice btDev = adapter.getRemoteDevice(address);
     		
     		if(btDev.getBondState()==BluetoothDevice.BOND_NONE){
     			ivCancel.setVisibility(View.GONE);
     		}else if(btDev.getBondState()==BluetoothDevice.BOND_BONDING){
     			ivCancel.setVisibility(View.GONE);
     		}else if(btDev.getBondState()==BluetoothDevice.BOND_BONDED){
     			ivCancel.setVisibility(View.VISIBLE);
     		}
			ivCancel.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub  
					  
				     new AlertDialog.Builder(getActivity()).setTitle("提示")//设置对话框标题  
				  
				     .setMessage("是否取消配对")//设置显示的内容  
				  
				     .setPositiveButton("是",new DialogInterface.OnClickListener() {//添加确定按钮  
				  
				          
				  
				         @Override  
				  
				         public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件  
				        	 
				     		try {
				     			Boolean returnValue = false;
				     			 if (btDev.getBondState() == BluetoothDevice.BOND_BONDED) {
				     				Method removeBondMethod = BluetoothDevice.class
				     						.getMethod("removeBond");
				     				returnValue = (Boolean) removeBondMethod.invoke(btDev);
				     				if(returnValue==true){
//				     				btList.remove(position);
//				     				btList.remove(btDevice);
				     				btAdapter.notifyDataSetChanged();
				     				}
				     			}
				     		} catch (Exception e) {
				     			e.printStackTrace();
				     		}
				  
				  
				         }  
				  
				     }).setNegativeButton("否",new DialogInterface.OnClickListener() {//添加返回按钮  
				  
				          
				  
				         @Override  
				  
				         public void onClick(DialogInterface dialog, int which) {//响应事件  
				  
				             // TODO Auto-generated method stub  
				             dialog.dismiss();
				  
				         }  
				  
				     }).show();//在按键响应事件中显示此对话框  
					
				}
			});
			return view;
		}
	}
}
