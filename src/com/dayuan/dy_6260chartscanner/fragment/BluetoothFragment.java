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
	// ��UUID��ʾ���ڷ���
	static final String SPP_UUID = "00001101-0000-1000-8000-00805F9B34FB";
	private static final int RESULT_OK = 10;
	private static final int RESULT_CANCELED = 20;
	private Button btnReturn;
	private ListView lvBlueTooth;
	private BluetoothAdapter adapter;;
	private List<String> btList = new ArrayList<String>(); // ���ڱ�������Ե���������
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
		// ����豸�Ƿ�֧������
		adapter = BluetoothAdapter.getDefaultAdapter();
		IntentFilter intent = new IntentFilter();
		intent.addAction(BluetoothDevice.ACTION_FOUND);// ��BroadcastReceiver��ȡ���������
		intent.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
		intent.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
		intent.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
		searchDevices = new BroadcastReceiver() {
			public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();
				BluetoothDevice device = null;
				// �����豸ʱ��ȡ���豸��MAC��ַ
				if (BluetoothDevice.ACTION_FOUND.equals(action)) {
					device = intent
							.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
					if (device.getBondState() == BluetoothDevice.BOND_NONE) {
						String strName = device.getName() + "\n" + "|"
								+ device.getAddress();
						Log.i("strName", strName);
						if (btList.indexOf(strName) == -1)// ��ֹ�ظ����
							btList.add(strName); // ��ȡ�豸���ƺ�mac��ַ
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
						connect(device);// �����豸
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
			// ��ȡ�����Ѿ��󶨵������豸
			Set<BluetoothDevice> devices = adapter.getBondedDevices();
			if (devices.size() > 0) {
				for (BluetoothDevice device : devices) {
					String str = device.getName() + "\n" + "|"
							+ device.getAddress() + "|\n" + "�����";
					Log.i("btList", str);
					btList.add(str);
					btAdapter.notifyDataSetChanged();
				}
			}
			// ��ʼ���������豸,�������������豸ͨ���㲥����
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
								// ��ȡ�����Ѿ��󶨵������豸
								Set<BluetoothDevice> devices = adapter.getBondedDevices();
								Log.i("lstD", "" + devices.size());
								if (devices.size() > 0) {
									for (BluetoothDevice device : devices) {
										String str = device.getName() + "\n" + "|"
												+ device.getAddress() + "|\n" + "�����";
										Log.i("btList", str);
										btList.add(str);
										btAdapter.notifyDataSetChanged();
									}
								}
								// ��ʼ���������豸,�������������豸ͨ���㲥����
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
				// ��ȡ�����Ѿ��󶨵������豸
				Set<BluetoothDevice> devices = adapter.getBondedDevices();
				Log.i("lstD", "" + devices.size());
				if (devices.size() > 0) {
					for (BluetoothDevice device : devices) {
						String str = device.getName() + "\n" + "|"
								+ device.getAddress() + "|\n" + "�����";
						Log.i("btList", str);
						btList.add(str);
						btAdapter.notifyDataSetChanged();
					}
				}
				// ��ʼ���������豸,�������������豸ͨ���㲥����
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
				// ���÷��䷽������BluetoothDevice.createBond(BluetoothDevice
				// remoteDevice);
				Toast.makeText(getActivity(), "������ԣ����Ժ�...", 1000).show();
				Method createBondMethod = BluetoothDevice.class
						.getMethod("createBond");
				returnValue = (Boolean) createBondMethod.invoke(btDevice);
				if(returnValue==true){
//					btList.remove(position);
					btAdapter.notifyDataSetChanged();
				}

			} else if (btDevice.getBondState() == BluetoothDevice.BOND_BONDED) {
				Toast.makeText(getActivity(), "�������ӣ����Ժ�...", 1000).show();
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
					  
				     new AlertDialog.Builder(getActivity()).setTitle("��ʾ")//���öԻ������  
				  
				     .setMessage("�Ƿ�ȡ�����")//������ʾ������  
				  
				     .setPositiveButton("��",new DialogInterface.OnClickListener() {//���ȷ����ť  
				  
				          
				  
				         @Override  
				  
				         public void onClick(DialogInterface dialog, int which) {//ȷ����ť����Ӧ�¼�  
				        	 
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
				  
				     }).setNegativeButton("��",new DialogInterface.OnClickListener() {//��ӷ��ذ�ť  
				  
				          
				  
				         @Override  
				  
				         public void onClick(DialogInterface dialog, int which) {//��Ӧ�¼�  
				  
				             // TODO Auto-generated method stub  
				             dialog.dismiss();
				  
				         }  
				  
				     }).show();//�ڰ�����Ӧ�¼�����ʾ�˶Ի���  
					
				}
			});
			return view;
		}
	}
}
