package com.dayuan.dy_6260chartscanner.activity;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Xml.Encoding;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dayuan.dy_6260chartscanner.CustomView;
import com.dayuan.dy_6260chartscanner.CustomView01;
import com.dayuan.dy_6260chartscanner.R;
import com.dayuan.dy_6260chartscanner.biz.ExportData;
import com.dayuan.dy_6260chartscanner.biz.QueryLogBiz;
import com.dayuan.dy_6260chartscanner.biz.UploadData;
import com.dayuan.dy_6260chartscanner.biz.VerifyDataBiz;
import com.dayuan.dy_6260chartscanner.db.LogDao;
import com.dayuan.dy_6260chartscanner.db.OrderDao;
import com.dayuan.dy_6260chartscanner.db.ServerDao;
import com.dayuan.dy_6260chartscanner.entity.DeviceEntry;
import com.dayuan.dy_6260chartscanner.entity.QueryLog;
import com.dayuan.dy_6260chartscanner.entity.Server;
import com.dayuan.dy_6260chartscanner.entity.VerifyData;
import com.dayuan.dy_6260chartscanner.util.CmdUtil;
import com.dayuan.dy_6260chartscanner.util.DialogUtil;
import com.dayuan.dy_6260chartscanner.util.ShowTime;
import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;
import com.hoho.android.usbserial.util.HexDump;
import com.hoho.android.usbserial.util.SerialInputOutputManager;

public class QueryLogActivity extends BaseActivity implements
		View.OnClickListener {
	private Button btnPrint, btnUpload, btnDelete, btnBack, btnNo,btnExport;
	private Button btnYes;
	private CheckBox cbCheckAll;
	private Button btnLeft, btnRight;
	private HashMap<Integer, Boolean> isSelected = new HashMap<Integer, Boolean>();
	private List<QueryLog> queryLog = new ArrayList<QueryLog>();
	private List<QueryLog> selectid = new ArrayList<QueryLog>();
	private List<String> dataList=new ArrayList<String>();
	private ListView lvLog;
	private LogDao logDao;
	QueryLogAdapter adapter;
	int VIEW_COUNT = 7;
	private int checkNum;// 记录选中的条目数量
	public int index = 0;
	private TextView tvPage;
	private int i;
	private int page;
	private int total;
	//private int totalPage;
	QueryLog log ;
	
	private TextView tvCounter;
	private boolean isRunning;

	private int myvid1 = 6790, mypid1 = 29987;
	UsbDevice device;
	private final String TAG = "OpenDevice";
	private UsbManager mUsbManager;
	private String mDeviceName;
	UsbSerialPort rPort;
	private SerialInputOutputManager mSerialIoManager;
	private final static ExecutorService mExecutor = Executors
			.newSingleThreadExecutor();
	private static final String ACTION_USB_PERMISSION = "com.android.hardware.USB_PERMISSION";
	private SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");// 小写的mm表示的是分钟 .
	// private String datetime;
	private String number;
	private String sampleName;
	private String projectName;
	private String result;
	DecimalFormat f = new DecimalFormat("0000");
	List<Integer> lists = new ArrayList<>();
	private ConnectivityManager mConnectivity;
	private static String data;
	private CustomView customView;
	private boolean check;
//	private Boolean isCheck;
	private  ServerDao serverDao;
	private List<Server> serverList;
	private List<VerifyData> verifyData;
	private Handler handler;
	private Date datetime;
	String dTime ;
	String density ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_query_log);
		queryLog = new QueryLogBiz(this).getQueryLog();
		adapter = new QueryLogAdapter(queryLog, this);
		logDao = new LogDao(this);
		setViews();

		total=queryLog.size();
		page = (total % VIEW_COUNT == 0) ? (total / VIEW_COUNT) : (total
				/ VIEW_COUNT + 1);
		if(page==0){
			page=1;
		}
		tvPage.setText( (index + 1)+"/"+page);
		lvLog.setAdapter(adapter);
		lvLog.setChoiceMode(lvLog.CHOICE_MODE_SINGLE);
        //listener();
		// 添加2个Button的监听事件。
		
		isRunning = true;
		ShowTime.ShowTime(this, isRunning, tvCounter);
		handler=new Handler();
		
		
	}
	private void setViews() {
		tvCounter = (TextView) findViewById(R.id.tv_counter);
		lvLog = (ListView) findViewById(R.id.list);
		cbCheckAll = (CheckBox) findViewById(R.id.cb_check_all);
		btnLeft = (Button) findViewById(R.id.btn_left);
		btnRight = (Button) findViewById(R.id.btn_right);
		tvPage = (TextView) findViewById(R.id.tv_page);
		btnPrint = (Button) findViewById(R.id.btn_print);
		btnUpload = (Button) findViewById(R.id.btn_upload);
		btnDelete = (Button) findViewById(R.id.btn_delete);
		btnBack = (Button) findViewById(R.id.btn_return);
		btnYes = (Button) findViewById(R.id.btn_yes);
		btnExport=(Button) findViewById(R.id.btn_export);
		btnLeft.setOnClickListener(this);
		btnRight.setOnClickListener(this);
		cbCheckAll.setOnClickListener(this);
		btnPrint.setOnClickListener(this);
		btnUpload.setOnClickListener(this);
		btnDelete.setOnClickListener(this);
		btnBack.setOnClickListener(this);
		btnExport.setOnClickListener(this);
		checkButton();
		
	}
	List<Integer> list=new ArrayList<Integer>();
	public List<Integer> getData() {
		
		return list;
 }
	private void listener() {
		lvLog.setOnItemLongClickListener(new OnItemLongClickListener() {
			
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				QueryLog log=queryLog.get(position);
				String data=log.getData();
				System.out.println(data+"------------------------------------");
				int length=data.length();
				Log.i("length", ""+length);
				String mdata=data.substring(1,length-1);
				Log.i("mData", mdata);
				String[] strData=mdata.split(",");
				//Object[] intData=mdata.split(",");
				List<String> strlist=Arrays.asList(strData);
				for(String str:strlist){
					Integer in=Integer.valueOf(str.trim());
					list.add(in);
				}
				showImage();
				
				return false;
			}
		});
		
	}


	public void showImage() {
				 AlertDialog.Builder builder=new
				 AlertDialog.Builder(QueryLogActivity.this);
				 final AlertDialog alertDialog=builder.create();
				 alertDialog.setCanceledOnTouchOutside(false);
				 alertDialog.show();
				 //自定义UI
				 Window window = alertDialog.getWindow();
				 window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
//				 window.setLayout(RelativeLayout.LayoutParams.FILL_PARENT,
//				 RelativeLayout.LayoutParams.FILL_PARENT);
				 LayoutParams params = window.getAttributes();
				 //params.dimAmount = 0.7f;
				 params.gravity=Gravity.CENTER;
				 window.setAttributes(params);
				 window.setContentView(R.layout.customview_dialog);
				 Button btnReturn=(Button) window.findViewById(R.id.btn_return);
				 customView=(CustomView) window.findViewById(R.id.customView);
				 customView.setData(getData());
				 //customView.refreshDrawableState();
				 customView.invalidate();
				 btnReturn.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						alertDialog.dismiss();
						 customView.removeData();
						//customView.setDrawingCacheEnabled(false);
						
					}
				});
				 }


	
	public void OpenDevices(int baudRate, int dataBits, int stopBits, int parity)
			throws IOException {
		List<DeviceEntry> result = new ArrayList<DeviceEntry>();
		mUsbManager = (UsbManager) (this).getSystemService(Context.USB_SERVICE);
		// for (final UsbDevice device : mUsbManager.getDeviceList().values()) {
		// final UsbSerialDriver driver = UsbSerialProber.getDefaultProber()
		// .probeDevice(device);
		// if (null == driver) {
		// Log.d(TAG, "  - No UsbSerialDriver available.");
		// result.add(new DeviceEntry(device, null));
		// } else {
		// Log.d(TAG, "Found usb device: " + device);
		// Log.d(TAG, "  + " + driver);
		// result.add(new DeviceEntry(device, driver));
		// }
		// mDeviceName = device.getDeviceName();
		// Log.d(TAG, "  + " + mDeviceName);
		// }
		// HashMap<String, UsbDevice> devices = mUsbManager.getDeviceList();
		// mdevice = devices.get(mDeviceName);
		HashMap<String, UsbDevice> deviceList = mUsbManager.getDeviceList();
		if (!deviceList.isEmpty()) {
			Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
			while (deviceIterator.hasNext()) {
				device = deviceIterator.next();
				Log.i(TAG,
						"vid: " + device.getVendorId() + "\t pid: "
								+ device.getProductId());
				if (device.getVendorId() == myvid1
						&& device.getProductId() == mypid1) {
					break;
				}
			}
			if (device != null && device.getVendorId() == myvid1
					&& device.getProductId() == mypid1) {
				// mUsbDevice = device;
				Log.i(TAG,
						"找到设备:" + device.getVendorId() + "\t pid: "
								+ device.getProductId());
			} else {
				Log.d(TAG, "未发现支持设备");
				// Toast.makeText(DetectionChannelOneActvity.this,
				// "USB接口已断开，请连接USB接口", Toast.LENGTH_SHORT).show();
				// finish();
				// return false;
			}
		}
		if (null == device) {
			Log.i(TAG, "No serial device.");
			return;
		}
		UsbSerialDriver driver = UsbSerialProber.getDefaultProber()
				.probeDevice(device);
		if (null == driver) {
			Log.i(TAG, "No serial device.");
			return;
		}
		// //
		for (int i = 0; i < driver.getPorts().size(); ++i) {
			UsbSerialPort p = driver.getPorts().get(i);
			Log.i(TAG, "Port: " + p);
		}
		Log.e(TAG, "Interface count: " + device.getInterfaceCount());
		rPort = driver.getPorts().get(0);
		//

		PendingIntent mPermissionIntent = PendingIntent.getBroadcast(this, 0,
				new Intent(ACTION_USB_PERMISSION), PendingIntent.FLAG_ONE_SHOT);
		mUsbManager.requestPermission(device, mPermissionIntent);

		UsbDeviceConnection connection = mUsbManager.openDevice(driver
				.getDevice());
		if (connection == null) {
			Log.e(TAG, "Opening device failed");
			return;
		}

		try {
			rPort.open(connection);
			rPort.setParameters(baudRate, dataBits, stopBits, parity);

		} catch (IOException e) {
			Log.e(TAG, "Error setting up device: " + e.getMessage(), e);
			try {
				rPort.close();
			} catch (IOException e2) {
				// Ignore.
			}
			rPort = null;
			return;
		}
		Log.i(TAG, "Serial device: " + rPort.getClass().getSimpleName());

		// rPort.close();
	}

	public void onDeviceStateChange() {
		stopIoManager();
		startIoManager();
	}

	private void stopIoManager() {
		if (mSerialIoManager != null) {
			Log.i(TAG, "Stopping io manager ..");
			mSerialIoManager.stop();
			mSerialIoManager = null;
		}

	}

	private void startIoManager() {
		if (rPort != null) {
			Log.i(TAG, "Starting io manager ..");
			mSerialIoManager = new SerialInputOutputManager(rPort, mListener);
			mExecutor.submit(mSerialIoManager);
		}

	}

	public void sendPrintingData(byte[] data) {
		if (rPort != null) {
			try {
				rPort.write(data, 500);
			} catch (IOException e) {
				Log.e(TAG, "Error send data: " + e.getMessage(), e);
				try {
					rPort.close();
				} catch (IOException e2) {
					// Ignore.
				}
				rPort = null;
			}
		}
	}

	private static byte[] CMD_RESET = { 0x1b, 0x40 };
	private static byte[] CMD_SETDOUBLESIZE = { 0x1c, 0x21, 0x0c };
	private static byte[] CMD_CENTER = { 0x1b, 0x61, 0x01 };
	private static byte[] CMD_SETGBK = { 0x1c, 0x26, 0x1b, 0x39, 0x00 };
	private static byte[] CMD_LINEDISTANCE = { 0x1B, 0x31, 0x5 };
	private static byte[] CMD_DISTANCE = { 0x1B, 0x31, 0x10 };

	public void printTitle() throws IOException, ParseException {
		for (int j = 0; j < selectid.size(); j++) {
			for (i = 0; i < queryLog.size(); i++) {
				 log = queryLog.get(i);
				if (selectid.get(j).equals(log)) {
					datetime = sdf.parse(log.getDate());
					String str = sdf.format(datetime);
					density = log.getDensity() ;
					number = f.format(log.getNumber()) ;
					sampleName = log.getSamplename() ;
					projectName = log.getName();
					result = log.getResult();
					String line = "\r\n";
					String testPerson = "检验者：" + "_ _ _ _ _ _ _ _" + line
							+ line;
					String confirmPerson = "审核者：" + "_ _ _ _ _ _ _ _" + line;
					String samplename="样品:  "+sampleName+ line;;
					String time = "检测时间:  " + str;
					String project = "编号  " + "样品名称      " + "浓度  " + "结果";
					String space=" ";
					String deviceCmd=null;
					int len=sampleName.length();
					
					/*if(len>=6){
					 deviceCmd = number +space+space+ sampleName+space+space
							+ density +space+space+ result + line
							+ project + line + line 
							+ time+line + "设备：胶体金读卡仪" + line + line;
					}else if(len<6){
					 deviceCmd = number +space+space+sampleName+space+space+space+space
								+ density +space+space+space+ result + line
								+ project + line + line 
								+ time+line + "设备：胶体金读卡仪" + line + line;	
					}*/		
					if (len >= 6) {
						spaceNum = sampleName.substring(0, 6);
					} else if (len < 6) {
						int newlen=6-len;
						switch(newlen){
						case 1:
							spaceNum = sampleName +space+space;
							break;
						case 2:
							spaceNum = sampleName +space+space+space+space;
							break;
						case 3:
							spaceNum = sampleName +space+space+space+space+space+space;
							break;
						case 4:
							spaceNum = sampleName +space+space+space+space+space+space+space+space;
							break;
						case 5:
							spaceNum = sampleName +space+space+space+space+space+space+space+space+space+space;
							break;
							default :
							spaceNum=sampleName +space+space+space+space+space;
						}
					}	
					deviceCmd = number + space + space+ spaceNum+ space+space+space+density + space + space+ space
							+ result + line + project + line + line
							+ time + line + "设备：胶体金读卡仪"
							+ line+line;
					
					
					
					// sendPrintingData(deviceCmd.getBytes("gbk"));
					sendPrintingData(CMD_RESET);
					sendPrintingData(CMD_LINEDISTANCE);
					sendPrintingData(confirmPerson.getBytes("gbk"));
					sendPrintingData(CMD_RESET);
					sendPrintingData(CMD_LINEDISTANCE);
					sendPrintingData(testPerson.getBytes("gbk"));

					byte[] buffer = deviceCmd.getBytes("gbk");
					sendPrintingData(CMD_RESET);
					sendPrintingData(CMD_LINEDISTANCE);
					sendPrintingData(buffer);
					
					sendPrintingData(CMD_RESET);
					sendPrintingData(CMD_LINEDISTANCE);
					sendPrintingData(CMD_SETDOUBLESIZE);
					sendPrintingData(CMD_CENTER);
					sendPrintingData(CMD_SETGBK);
					String headCmd = projectName+line+"检测报告" + line + line;

					byte[] bytes = headCmd.getBytes("gbk");
					sendPrintingData(bytes);
				}
			}
		}

	}

	private SerialInputOutputManager.Listener mListener = new SerialInputOutputManager.Listener() {

		@Override
		public void onRunError(Exception e) {
			e.printStackTrace();
			Log.d(TAG, "Runner stopped.");
		}

		String mData = "";

		public void onNewData(final byte[] data) {
			// mData += HexDump.toHexString(data);
			// Log.d(TAG, "#1 " + mData);
		}
	};

	@Override
	protected void onResume() {
		super.onResume();
		try {
			OpenDevices(9600, 8, UsbSerialPort.STOPBITS_1,
					UsbSerialPort.PARITY_NONE);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	};

	@Override
	protected void onPause() {
		super.onPause();
		stopIoManager();
		if (rPort != null) {
			try {
				rPort.close();
			} catch (IOException e) {
				// Ignore.
			}
			rPort = null;
		}
		finish();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		isRunning = false;
		super.onDestroy();
	}

	

	private void checkButton() {
		if (index < 1) {
			btnLeft.setEnabled(false);
			btnLeft.setSelected(false);
			if (queryLog.size() <= VIEW_COUNT) {
				btnRight.setSelected(false);
				btnRight.setEnabled(false);
			} else {
				btnRight.setSelected(true);
				btnRight.setEnabled(true);
			}
		} else if (queryLog.size() - index * VIEW_COUNT <= VIEW_COUNT) {
			btnRight.setEnabled(false);
			btnRight.setSelected(false);
			btnLeft.setEnabled(true);
			btnLeft.setSelected(true);
		} else {
			btnLeft.setEnabled(true);
			btnLeft.setSelected(true);
			btnRight.setEnabled(true);
			btnRight.setSelected(true);
		}

	}

	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btn_left:
			leftView();
			break;

		case R.id.btn_right:
			rightView();
			break;
		case R.id.cb_check_all:
//			if (!cbCheckAll.isChecked()) {
//				for (int j = 0; j < queryLog.size(); j++) {
//
//					// 取消选中
//					adapter.getIsSelected().put(j, false);
//					checkNum--;
//					Log.i("checkNum", checkNum + "");
//					selectid.remove(queryLog.get(j));
//					// 全部选中
//				}
//			} else {
//				for (int j = 0; j < queryLog.size(); j++) {
//					adapter.getIsSelected().put(j, true);
//					checkNum++;
//					Log.i("checkNum0", checkNum + "");
//					selectid.add(queryLog.get(j));
//				}
//
//			}
			// 刷新listview和TextView的显示
			if(cbCheckAll.isChecked()){
				selectid.clear();
				for (int i = 0; i < queryLog.size(); i++) {
					log=queryLog.get(i);
					log.isCheck=true;
					selectid.add(queryLog.get(i));
				}
				adapter.notifyDataSetChanged();
			}else{
				for (int i = 0; i < queryLog.size(); i++) {
					log=queryLog.get(i);
					log.isCheck=false;
					selectid.remove(queryLog.get(i));
				}
				adapter.notifyDataSetChanged();
			}
//			dataChanged();
			break;
		case R.id.btn_print:
			if(check==true&&selectid.size()!=0){
			print();
			}else{
				Toast.makeText(this, "请选择记录", 0).show();
			}
			
			break;
		case R.id.btn_upload:
			if(check==true&&selectid.size()!=0){
				upload();
				}else{
					Toast.makeText(this, "请选择记录", 0).show();
				}
			
			break;
		case R.id.btn_delete:
			if(check==true&&selectid.size()!=0){
				delete();
				}else{
					Toast.makeText(this, "请选择记录", 0).show();
				}
			
			break;
		case R.id.btn_export:
			if(check==true&&selectid.size()!=0){
				export();
				}else{
					Toast.makeText(this, "请选择记录", 0).show();
				}
			break;
		case R.id.btn_return:
//			Intent intent = new Intent(QueryLogActivity.this,
//					BaseActivity.class);
//			startActivity(intent);
			finish();
			break;
		}
		tvPage.setText( (index + 1)+"/"+page);
		
	}
	
	private void export() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final AlertDialog alertDialog = builder.create();
		alertDialog.setCanceledOnTouchOutside(false);
		alertDialog.show();
		// 自定义UI
		Window window = alertDialog.getWindow();
		View dialogView = View.inflate(this, R.layout.export02_window, null);
		window.setContentView(dialogView);
		btnNo = (Button) window.findViewById(R.id.btn_no);
		btnYes = (Button) window.findViewById(R.id.btn_yes);
		btnNo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				alertDialog.dismiss();
			}
		});
		btnYes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				alertDialog.dismiss();
				String fileName="DY6260_rec.csv";
				    dataList.add("编号");
			        dataList.add("样品名称");
			        dataList.add("项目名称");
			        dataList.add("浓度");
			        dataList.add("结果");
			        dataList.add("时间");
				if (cbCheckAll.isChecked()) {
					for (i = 0; i < queryLog.size(); i++) {
						QueryLog log = queryLog.get(i);
						try {
							number = f.format(log.getNumber());
							sampleName = log.getSamplename();
							projectName = log.getName();
							density = log.getDensity();
							result = log.getResult();
							datetime = sdf.parse(log.getDate());
							dTime = sdf.format(datetime);
							dataList.add(number);
							dataList.add(sampleName);
							dataList.add(projectName);
							dataList.add(density);
							dataList.add(result);
							dataList.add(dTime);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					ExportData.exportCsv(QueryLogActivity.this ,dataList,fileName);
					}else{
						for (int j = 0; j < selectid.size(); j++) {
							for (i = 0; i < queryLog.size(); i++) {
								QueryLog log = queryLog.get(i);
								    
								if (selectid.get(j).equals(log)) {
									try {
										number =f.format(log.getNumber());
										sampleName =log.getSamplename();
										projectName = log.getName();
										density = log.getDensity();
										result = log.getResult();
										datetime = sdf.parse(log.getDate());
										dTime =sdf.format(datetime);
										dataList.add(number);
										dataList.add(sampleName);
										dataList.add(projectName);
										dataList.add(density);
										dataList.add(result);
										dataList.add(dTime);
										ExportData.exportCsv(QueryLogActivity.this,dataList,fileName);
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							}
						}
					}
				
			}

		});

	}
	private void print() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final AlertDialog alertDialog = builder.create();
		alertDialog.show();
		// 自定义UI
		Window window = alertDialog.getWindow();
		View dialogView = View.inflate(this, R.layout.printing_window, null);
		window.setContentView(dialogView);
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				try {
					try {
						printTitle();
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				alertDialog.dismiss();

			}
		}, 1000);
	}
   String address;
   String user;
   String password;
private String spaceNum;
   
	private void upload() {
		verifyData=new VerifyDataBiz(this).getVerifyData();
		serverDao=new ServerDao(this);
		serverList=serverDao.getServerLists();
		
		 for (Server server:serverList) {
			 address=server.getAddress();
			 user=server.getUser();
			 password=server.getPassword();
		 }
		mConnectivity= (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE); 
//		 检查网络连接，如果无网络可用，就不需要进行连网操作等    
		 final NetworkInfo info = mConnectivity.getActiveNetworkInfo();
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final AlertDialog alertDialog = builder.create();
		alertDialog.show();
		// 自定义UI
		Window window = alertDialog.getWindow();
		View dialogView = View.inflate(this, R.layout.upload_window, null);
		window.setContentView(dialogView);
		
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				alertDialog.dismiss();
				try {
//					Toast.makeText(QueryLogActivity.this, "上传失败", 0).show();
					if (cbCheckAll.isChecked()) {
						for (i = 0; i < queryLog.size(); i++) {
							QueryLog log = queryLog.get(i);
							 if(info==null||!mConnectivity.getBackgroundDataSetting()){
								UploadData.uploadData(QueryLogActivity.this,log,address,user,password,verifyData);
								}else{
									Toast.makeText(QueryLogActivity.this, "上传失败，请检查网络", 0).show();
								}
						}
						}else{
							for (int j = 0; j < selectid.size(); j++) {
								for (i = 0; i < queryLog.size(); i++) {
									QueryLog log = queryLog.get(i);
									if (selectid.get(j).equals(log)) {
										if(info==null||!mConnectivity.getBackgroundDataSetting()){
											Toast.makeText(QueryLogActivity.this, "上传失败，请检查网络", 0).show();
										 }else{
											 UploadData.uploadData(QueryLogActivity.this, log,address,user,password,verifyData);
										 }
									}
								}
							}
						}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, 2000);
		

	}

	private void delete() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final AlertDialog alertDialog = builder.create();
		alertDialog.show();
		// 自定义UI
		Window window = alertDialog.getWindow();
		View dialogView = View.inflate(this, R.layout.delete_window, null);
		window.setContentView(dialogView);
		btnNo = (Button) window.findViewById(R.id.btn_no);
		btnYes = (Button) window.findViewById(R.id.btn_yes);
		btnNo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				alertDialog.dismiss();
			}
		});

		btnYes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int num = 0;
				int totalNum = 0;
				int totalRec = 0;
				totalRec = queryLog.size();
				Log.i("total", totalRec + "");

				if (cbCheckAll.isChecked()) {
//					for (i = 0; i < queryLog.size(); i++) {
//						QueryLog log = queryLog.get(i);
//
//						int id = log.getId();
//						int countRow = logDao.delete(id);
//						if (countRow > 0) {
//							totalNum++;
//						}
//					}
					int countRow = logDao.deleteAll();
					if (countRow > 0) {
						totalNum++;
					}
					queryLog.removeAll(queryLog);
//					log.isCheck=false;
					check=false;
//					adapter.getIsSelected().put(i, false);

					if (totalNum == 0) {
						Toast.makeText(QueryLogActivity.this, "删除失败,没有记录", 0)
								.show();
					} else {
						Toast.makeText(QueryLogActivity.this,
								"删除成功,删除了所有记录", 0).show();
					}
					cbCheckAll.setChecked(false);
					page = 0;
					index = 0;
				} else {
					for (int j = 0; j < selectid.size(); j++) {
						for (i = 0; i < queryLog.size(); i++) {
							QueryLog log = queryLog.get(i);
							if (selectid.get(j).equals(log)) {
								int id = log.getId();
								int count = logDao.delete(id);
								if (count > 0) {
									num++;
								}
								queryLog.remove(i);
							}
							log.isCheck=false;
							check=false;
//							adapter.getIsSelected().put(i, false);
							adapter.notifyDataSetChanged();
						}
					}
					if (num == 0) {
						Toast.makeText(QueryLogActivity.this, "删除失败,没有记录", 0)
								.show();
					} else {
						Toast.makeText(QueryLogActivity.this,
								"删除成功,删除了" + num + "条记录", 0).show();
					}

					int total = queryLog.size();
					page = (total % VIEW_COUNT == 0) ? (total / VIEW_COUNT)
							: (total / VIEW_COUNT + 1);
					if (index > (page - 1))
						index = page - 1;
				}

				selectid.clear();
				adapter.notifyDataSetChanged();
//				queryLog = new QueryLogBiz(QueryLogActivity.this).getQueryLog();
//				adapter = new QueryLogAdapter(queryLog, QueryLogActivity.this);
//				adapter.notifyDataSetChanged();
				alertDialog.dismiss();
				if(page==0){
					page=1;
				}
				tvPage.setText( (index + 1)+"/"+page);
				checkButton();
			}

		});
		if (queryLog.size() == 0) {
			Toast.makeText(QueryLogActivity.this, "删除失败,没有记录", 0).show();
			alertDialog.hide();
		}
	}

	private void dataChanged() {
		adapter.notifyDataSetChanged();

	}

	private void leftView() {

		// tvPage.setText(""+index);
		index--;
		adapter.notifyDataSetChanged();
		// 检查Button是否可用。
		checkButton();
	}

	private void rightView() {

		index++;
		adapter.notifyDataSetChanged();
		// 检查Button是否可用。
		checkButton();
		// tvPage.setText(""+index);

	}

//	public String getData(String data) {
//		Log.i("data", data);
//		return data;
//	}
	
	
	public class QueryLogAdapter extends BaseAdapter {
		private HashMap<Integer, Boolean> isSelected;
		// 用来控制CheckBox的选中状况
		private List<QueryLog> queryLog;
		private Context context;
		private LayoutInflater inflater;
		// 用于显示每列5个Item项。
		// int VIEW_COUNT=7;
		// 用于显示页号的索引
		// int index = 0;

        
		public QueryLogAdapter(List<QueryLog> queryLog, Context context) {
			this.context = context;
			inflater = LayoutInflater.from(this.context);
			if (queryLog == null) {
				queryLog = new ArrayList<QueryLog>();
			}
			this.queryLog = queryLog;
//			isSelected = new HashMap<Integer, Boolean>();
//			for (i = 0; i < queryLog.size(); i++) {
//				getIsSelected().put(i, false);
//
//			}
		}


		@Override
		public int getCount() {
			// ori表示到目前为止的前几页的总共的个数。
			int ori = VIEW_COUNT * index;
			// 值的总个数-前几页的个数就是这一页要显示的个数，如果比默认的值小，说明这是最后一页，只需显示这么多就可以了
			if (queryLog.size() - ori < VIEW_COUNT) {
				return queryLog.size() - ori;
			} else {
				// 如果比默认的值还要大，说明一页显示不完，还要用换一页显示，这一页用默认的值显示满就可以了。
				return VIEW_COUNT;
			}
		}

		@Override
		public QueryLog getItem(int position) {
			// TODO Auto-generated method stub
			return queryLog.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			final ViewHolder holder;
			 final QueryLog log;
			try {

				if (convertView == null) {
					holder = new ViewHolder();
					convertView = inflater
							.inflate(R.layout.querylog_item, null);
					holder.number = (TextView) convertView
							.findViewById(R.id.tv_id);
					holder.sampName = (TextView) convertView
							.findViewById(R.id.tv_sample_name);
					holder.name = (TextView) convertView
							.findViewById(R.id.tv_name);
					holder.result = (TextView) convertView
							.findViewById(R.id.tv_result);
					holder.date = (TextView) convertView
							.findViewById(R.id.tv_datetime);
					holder.density = (TextView) convertView
							.findViewById(R.id.tv_density);
					holder.selected = (CheckBox) convertView
							.findViewById(R.id.cb_check);
					convertView.setTag(holder);
				} else {
					holder = (ViewHolder) convertView.getTag();
				}
				 log = queryLog.get(position + index * VIEW_COUNT);
				holder.number.setText(f.format(log.getNumber()));
				holder.sampName.setText(log.getSamplename());
				holder.name.setText(log.getName());
				holder.result.setText(log.getResult());

				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");// 小写的mm表示的是分钟 .
				Date datetime = sdf.parse(log.getDate());
				String str = sdf.format(datetime);
				holder.date.setText(str);
				holder.density.setText(log.getDensity());
				// 监听checkBox并根据原来的状态来设置新的状态
				final boolean isCheck=log.isCheck;
				if(isCheck){
					holder.selected .setChecked(true);
					check=true;
				}else{
					holder.selected .setChecked(false);
					check=false;
				}
				holder.selected.setOnClickListener(new View.OnClickListener() {

					public void onClick(View v) {

//						if (isSelected.get(position + index * VIEW_COUNT)) {
//							isSelected
//									.put(position + index * VIEW_COUNT, false);
//							setIsSelected(isSelected);
//							cbCheckAll.setChecked(false);
//							check=false;
//							selectid.remove(queryLog.get(position + index
//									* VIEW_COUNT));
//						} else {
//							isSelected.put(position + index * VIEW_COUNT, true);
//							setIsSelected(isSelected);
//							int j;
//							for (j = 0; j < queryLog.size(); j++) {
//								if (!isSelected.get(j)) {
//									break;
//								}
//							}
//							if (j == queryLog.size()) {
//								cbCheckAll.setChecked(true);
//							}
//
//							selectid.add(queryLog.get(position + index
//									* VIEW_COUNT));
//							check=true;
//						}
						
						if(holder.selected.isChecked()){
							log.isCheck=true;
							check=true;
							selectid.add(queryLog.get(position + index * VIEW_COUNT));
//							
						}else{
							log.isCheck=false;
//							check=false;
							selectid.remove(queryLog.get(position+index * VIEW_COUNT));
//							cbCheckAll.setChecked(false);
						}
							int i;
							for (i = 0; i < queryLog.size(); i++) {
								QueryLog log=queryLog.get(i);
								if(log.isCheck==false){
									cbCheckAll.setChecked(false);
									break;
								}
								if(i==queryLog.size()-1){
									cbCheckAll.setChecked(true);
								}else if(i<queryLog.size()-1){
									cbCheckAll.setChecked(false);
								}
								
							}
						}
				});
//				holder.selected.setChecked(log.isCheck);
				
				// 根据isSelected来设置checkbox的选中状况
//				holder.selected.setChecked(getIsSelected().get(
//						position + index * VIEW_COUNT));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return convertView;

		}
		
		
		class ViewHolder {
			CheckBox selected;
			TextView sampName;
			TextView number;
			TextView name;
			TextView result;
			TextView date;
			TextView density;
		}
//		private HashMap<Integer, Boolean> getIsSelected() {
//			// TODO Auto-generated method stub
//			return isSelected;
//		}
//
//		public void setIsSelected(HashMap<Integer, Boolean> isSelected) {
//			this.isSelected = isSelected;
//		}
	}

	
}
