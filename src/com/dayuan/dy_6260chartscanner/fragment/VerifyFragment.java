package com.dayuan.dy_6260chartscanner.fragment;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;









import pl.droidsonroids.gif.GifDrawable;

import com.dayuan.dy_6260chartscanner.R;
import com.dayuan.dy_6260chartscanner.TApplication;
import com.dayuan.dy_6260chartscanner.activity.BaseActivity;
import com.dayuan.dy_6260chartscanner.activity.DetectionChannelOneActvity;
import com.dayuan.dy_6260chartscanner.activity.ProjectSettingActivity;
import com.dayuan.dy_6260chartscanner.entity.Project;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.PendingIntent;
import android.app.SearchManager.OnCancelListener;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class VerifyFragment extends Fragment implements OnClickListener{
	View view;
    private Button btnReturn,btnVerifyOne,btnVerifyTwo;
    private UsbManager mUsbManager;
    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    private UsbDevice device;
    private final String TAG = "OpenDevice";
    private int myvid = 1155, mypid = 22336;
    UsbInterface[] usbinterface = null;
	UsbEndpoint[][] endpoint = new UsbEndpoint[5][5];
	UsbDeviceConnection connection = null;
	ConnectedThread mconnectedthread = null;
	byte[] mybuffer = new byte[512];
	int datalength;
	boolean threadcontrol = false;
	public int threadsenddata = 0;
	public static final int ACTION_CLEAR = 11;
	public static final int ACTION_VERIFY_REQUEST = 12;
	public static final int ACTION_STATUS_VERIFY = 13;
	public static final int ACTION_EXIT_CARD = 1;
	public static final int ACTION_ENTER_CARD = 4;
	public static final int ACTION_STATUS_CARD = 5;
	private static final int NO_CARD_STATUS = 6;
	
	private static final int MESSAGE_VERIFY_FAILURE = 100;
	private static final int MESSAGE_VERIFY_SUCCESS = 200;
	byte[] CMD_VERIFY_REQUEST = { 0x7E, 0x17, 0x00,0x00, 0x17, 0x7E };
	byte[] CMD_EXIT_CARD = { 0x7E, 0x11, 0x00, 0x01, 0x02, 0x14, 0x7E };
	byte[] CMD_ENTER_CARD = { 0x7E, 0x11, 0x00, 0x01, 0x03, 0x15, 0x7E };
	byte[] CMD_STATUS_CARD = { 0x7E, 0x13, 0x00, 0x00, 0x13, 0x7E };
//	byte[] CMD_RESPONSE = { 0x7E, 0x18, 0x00,0x00, 0x18, 0x7E };
	private int chNumber=0;
	private Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MESSAGE_VERIFY_SUCCESS:
				btnVerifyOne.setSelected(false);
				btnVerifyTwo.setSelected(false);
				Toast.makeText(getActivity(), "校准成功", 0).show();
				break;

			case MESSAGE_VERIFY_FAILURE:
				btnVerifyOne.setSelected(false);
				btnVerifyTwo.setSelected(false);
				Toast.makeText(getActivity(), "校准失败", 0).show();
				break;
			case NO_CARD_STATUS:
				Toast.makeText(getActivity(), "当前为无卡状态，请插入卡",
						0).show();
				btnReturn.setEnabled(true);
				break;
			}
		};
	};
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.fragment_verify, container,
				false);
		init();
		return view;
	}


	private void init() {
		btnVerifyOne=(Button) view.findViewById(R.id.btn_verify_channel_one);
		btnVerifyTwo=(Button) view.findViewById(R.id.btn_verify_channel_two);
		btnReturn=(Button) view.findViewById(R.id.btn_return);
		btnVerifyOne.setOnClickListener(this);
		btnVerifyTwo.setOnClickListener(this);
		btnReturn.setOnClickListener(this);
	}
	//ph新增切换通道判断条件
	

	@Override
	public void onClick(View v) {
		boolean isChange = TApplication.getInstance()
				.getSharedPreferencesIns().getBoolean("isChange", false);
		switch (v.getId()) {
		case R.id.btn_verify_channel_one:
			//verifyOne();
			chNumber=0;
			if (isChange) {
				if (chNumber == 1) {
					chNumber = 0;
				} else if (chNumber == 0) {
					chNumber = 1;
				}
			}
			checkCard();
			break;
		case R.id.btn_verify_channel_two:
			//verifyTwo();
			chNumber=1;
			if (isChange) {
				if (chNumber == 1) {
					chNumber = 0;
				} else if (chNumber == 0) {
					chNumber = 1;
				}
			}
			checkCard();
			break;
		case R.id.btn_return:
			
			getActivity().finish();
			
			break;
		default:
			break;
		}
		
	}


	private void checkCard() {
		// TODO Auto-generated method stub
		if(openDevice()){
			if (mconnectedthread != null) {
				mconnectedthread = null;
			}
			if(device!=null&&device.getVendorId() == myvid &&
					 device.getProductId() == mypid){
				mconnectedthread = new ConnectedThread();
				mconnectedthread.start();

				threadcontrol = true;
		        threadsenddata = ACTION_EXIT_CARD;
		        //readData();
			}
		}
		/*AlertDialog dialog= new AlertDialog.Builder(getActivity())
		.setTitle("通道校准")
		.setMessage("请在校准前放入白色金标卡").setPositiveButton("校准", new AlertDialog.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				 threadsenddata = ACTION_STATUS_CARD;
			}
		})
		.setNegativeButton("取消", new AlertDialog.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
		        threadsenddata = ACTION_ENTER_CARD;
		        //threadcontrol = false;
			}
		}).show();*/
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		alertDialog = builder.create();
		alertDialog.setCanceledOnTouchOutside(false);
		alertDialog.show();
		
		// 自定义UI
		Window window = alertDialog.getWindow();
		View dialogView = View.inflate(getActivity(), R.layout.verify_window, null);
		window.setContentView(dialogView);
		Button	btnNo = (Button) window.findViewById(R.id.btn_no);
		Button btnYes = (Button) window.findViewById(R.id.btn_yes);
		btnNo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 threadsenddata = ACTION_ENTER_CARD;
				alertDialog.dismiss();
			}
		});
		btnYes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// for(Project p:projects){
				threadsenddata = ACTION_STATUS_CARD;
				
				
			}
		});

	
	}


/*	private void verifyOne() {
		btnVerifyOne.setSelected(true);
		chNumber=0;
		if(openDevice()){
			if (mconnectedthread != null) {
				mconnectedthread = null;
			}
			if(device!=null&&device.getVendorId() == myvid &&
					 device.getProductId() == mypid){
				mconnectedthread = new ConnectedThread();
				mconnectedthread.start();

				threadcontrol = true;
		        threadsenddata = ACTION_VERIFY_REQUEST;
		        readData();
			}
		}
		
	}*/

/*
	private void verifyTwo() {
		btnVerifyTwo.setSelected(true);
		chNumber=1;
		if(openDevice()){
			if (mconnectedthread != null) {
				mconnectedthread = null;
			}
			if(device!=null&&device.getVendorId() == myvid &&
					 device.getProductId() == mypid){
				mconnectedthread = new ConnectedThread();
				mconnectedthread.start();

				threadcontrol = true;
		threadsenddata = ACTION_VERIFY_REQUEST;
		readData();
			}
		}
	}*/


	private void selectChannel(View v) {
//		btnVerifyOne.setSelected(false);
//		btnVerifyTwo.setSelected(false);
//		v.setSelected(true);
		
	}
	private final BroadcastReceiver musbReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (ACTION_USB_PERMISSION.equals(action)) {
				synchronized (this) {
					UsbDevice device = intent
							.getParcelableExtra(UsbManager.EXTRA_DEVICE);
					System.out.println("UsbManager.EXTRA_DEVICE 22222222222222222 ========"  
                            + intent.getParcelableExtra(UsbManager.EXTRA_DEVICE));  
					System.out.println("是否有权限了？？？？？？   " + mUsbManager.hasPermission(device));  
					if (intent.getBooleanExtra(
							UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
						if (device != null) {
							Log.i(TAG, "usb EXTRA_PERMISSION_GRANTED");
						}
					} else {
						Log.i(TAG, "usb EXTRA_PERMISSION_GRANTED null!!!");
					}
				}
			}
		}
	};
	public Boolean openDevice() {
		 mUsbManager = (UsbManager) (getActivity()).getSystemService(Context.USB_SERVICE);  
		if (mUsbManager == null) {
			return false;
		} else {
			Log.i(TAG, "usb设备：" + String.valueOf(mUsbManager.toString()));
		}
		HashMap<String, UsbDevice> deviceList = mUsbManager.getDeviceList();
		if (!deviceList.isEmpty()) {
			Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
			// int key = 0;
			 Integer num=0;
			 int i = 0;
			 int size=0;
			 List<Integer> lists=new ArrayList<>();
			 Iterator<UsbDevice> iterator=deviceList.values().iterator();

			while (deviceIterator.hasNext()) {
				 
				device = deviceIterator.next();
				Log.i(TAG,
						"vid: " + device.getVendorId() + "\t pid: "
								+ device.getProductId());
				if (device != null && device.getVendorId() == myvid
						&& device.getProductId() == mypid) {
					 String deviceName=device.getDeviceName();
					 String deviceId=deviceName.substring(18);
					 num=Integer.parseInt(deviceId);
					 lists.add(num);
				}
				
			}
			size=lists.get(0)-lists.get(1);
			while (iterator.hasNext()) {
				 device = iterator.next();
				 if (device != null &&device.getVendorId() == myvid &&
				 device.getProductId() == mypid) {
						 if(size<0&&chNumber==i){
							 break;
						 }else if(size>0&&chNumber!=i){
							 break;
						 }
						 i++;
						 }
				 }

		}
			if (device != null)
						{
					PendingIntent mPermissionIntent = PendingIntent.getBroadcast(getActivity(),
							0, new Intent(ACTION_USB_PERMISSION), 0);
					IntentFilter permissionFilter = new IntentFilter(
							ACTION_USB_PERMISSION);
					getActivity().registerReceiver(musbReceiver, permissionFilter);
					if (mUsbManager.hasPermission(device)) {
						 connection = mUsbManager.openDevice(device);

					} else {

						mUsbManager.requestPermission(device, mPermissionIntent);
					}
					if (mUsbManager.hasPermission(device)) {
						Log.i(TAG, "拥有访问权限");
					} else {
						Log.d(TAG, "未获得访问权限");
						return false;
					}
					Log.i(TAG, device.getDeviceName());
					Log.i(TAG, "" + device.getInterfaceCount());
					usbinterface = new UsbInterface[device.getInterfaceCount()];
					
					for (int i = 0; i < device.getInterfaceCount(); i++) {
						usbinterface[i] = device.getInterface(i);
						Log.i(TAG,
								"接口" + i + "的端点数为："
										+ usbinterface[i].getEndpointCount());
						for (int j = 0; j < usbinterface[i].getEndpointCount(); j++) {
							endpoint[i][j] = usbinterface[i].getEndpoint(j);
							if (endpoint[i][j].getDirection() == 0) {
								Log.i(TAG, "端点" + j + "的数据方向为输出");
							} else {
								Log.i(TAG, "端点" + j + "的数据方向为输入");
							}
						}
					}
				}
				return true;

}
	int cardsta = 0;
	private AlertDialog alertDialog;
	class ConnectedThread extends Thread {
		@Override
		public void destroy() {
			super.destroy();
		}

		public ConnectedThread() {
			if (connection != null) {
				connection.close();
			}
			if (connection == null) {
				return;
			}
			if (device != null && mUsbManager.hasPermission(device)) {
				connection = mUsbManager.openDevice(device);
				connection.claimInterface(usbinterface[1], true);
				}
			}


		@Override
		public void run() {
			while (threadcontrol) {
				if (threadsenddata != ACTION_CLEAR) {
					switch (threadsenddata) {
					case ACTION_VERIFY_REQUEST:
						connection.bulkTransfer(endpoint[1][0], CMD_VERIFY_REQUEST,
								CMD_VERIFY_REQUEST.length, 30);
						break;
					case ACTION_EXIT_CARD:
						cardsta = 0;
						connection.bulkTransfer(endpoint[1][0], CMD_EXIT_CARD,
								CMD_EXIT_CARD.length, 30);
						break;
					case ACTION_ENTER_CARD:
						cardsta = 3;
						connection.bulkTransfer(endpoint[1][0], CMD_ENTER_CARD,
								CMD_ENTER_CARD.length, 30);
						break;
					case ACTION_STATUS_CARD:

						connection.bulkTransfer(endpoint[1][0],
								CMD_STATUS_CARD, CMD_STATUS_CARD.length, 30);
						break;
					default: {
					}
					}
					threadsenddata = ACTION_CLEAR;
				}
				readData(); 
			}

		}
	}

	@Override
	public void onResume() {
		IntentFilter usbFilter = new IntentFilter();
		usbFilter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
		usbFilter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
		getActivity().registerReceiver(musbReceiver, usbFilter);
		super.onResume();
	}
	/*public void readData() {
		datalength = connection.bulkTransfer(endpoint[1][1], mybuffer, 512,
				2000);
		
		if (datalength > 0) {

			if (datalength < 4) {
				Log.e(TAG, "len error!");
				return;
			}
		}
//		int len02=CMD_RESPONSE.length;
		final int flag = mybuffer[0];
		final int cmd = mybuffer[1];
		final int len = (mybuffer[2] << 8) | (mybuffer[3] & 0x0FF);
		final int endflag = mybuffer[datalength - 1];
		System.out.println("cmd:"+cmd+"len:"+len);
		handler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				
				if(flag==0x7E&&cmd==0x18&&len==0&&endflag==0x7E){
					handler.sendEmptyMessage(MESSAGE_VERIFY_SUCCESS);
				}else{
					handler.sendEmptyMessage(MESSAGE_VERIFY_FAILURE);
				}
				
			}
		}, 19000);
		
	}*/
	public void readData() {

		datalength = connection.bulkTransfer(endpoint[1][1], mybuffer, 512,
				2000);

		if (datalength > 0) {

			if (datalength < 4) {
				Log.e(TAG, "len error!");
				return;
			}

			final int flag = mybuffer[0];
			final int cmd = mybuffer[1];
			final int len = (mybuffer[2] << 8) | (mybuffer[3] & 0x0FF);
			final int endflag = mybuffer[datalength - 1];

			if ((flag != 0x7E) || (endflag != 0x7E)) {
				Log.e(TAG, "data error!");
				return;
			}

			Log.i(TAG, "datalength: " + datalength + " cmd: " + cmd + " len: "
					+ len);

			byte[] buffer = new byte[datalength];
			for (int i = 0; i < datalength; i++) {
				buffer[i] = mybuffer[i];
			}
			Log.i(TAG, Arrays.toString(buffer) + "");

			switch (cmd) {
			case 0x14:
				if (len == 1) {
					if (mybuffer[4] == 0x01) {// 有卡
						alertDialog.dismiss();
						threadsenddata = ACTION_VERIFY_REQUEST;
							
					}

					else if (mybuffer[4] == 0x02) {// 无卡

						handler.sendEmptyMessage(NO_CARD_STATUS);
						Log.i(TAG, "无卡");
					} 
				}

				break;
			case 0x18: {
				handler.postDelayed(new Runnable() {
					
					@Override
					public void run() {
						
						if(flag==0x7E&&cmd==0x18&&len==0&&endflag==0x7E){
							handler.sendEmptyMessage(MESSAGE_VERIFY_SUCCESS);
						}else{
							handler.sendEmptyMessage(MESSAGE_VERIFY_FAILURE);
						}
						
					}
				}, 19000);

				break;
				
			}
			default: {

			}
			}
		}
	}

	@Override
	public void onDestroy() {
		getActivity().unregisterReceiver(musbReceiver);
		if (connection != null) {
			connection.close();
		}
		threadcontrol = false;
		super.onDestroy();
	}
}