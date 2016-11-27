package com.dayuan.dy_6260chartscanner.activity;

import java.io.IOException;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

import com.ant.liao.GifView;
import com.dayuan.dy_6260chartscanner.CustomView;
import com.dayuan.dy_6260chartscanner.R;
import com.dayuan.dy_6260chartscanner.TApplication;
import com.dayuan.dy_6260chartscanner.biz.CurveSmoothing;
import com.dayuan.dy_6260chartscanner.biz.DataManageBiz;
import com.dayuan.dy_6260chartscanner.biz.ProjectBiz;
import com.dayuan.dy_6260chartscanner.biz.ReportDataBiz;
import com.dayuan.dy_6260chartscanner.biz.TaskClassBiz;
import com.dayuan.dy_6260chartscanner.biz.UploadData;
import com.dayuan.dy_6260chartscanner.db.Dao;
import com.dayuan.dy_6260chartscanner.db.DataDao;
import com.dayuan.dy_6260chartscanner.db.LogDao;
import com.dayuan.dy_6260chartscanner.db.OrderDao;
import com.dayuan.dy_6260chartscanner.db.ReportDataDao;
import com.dayuan.dy_6260chartscanner.db.ReportNumberDao;
import com.dayuan.dy_6260chartscanner.db.TaskClassDao;
import com.dayuan.dy_6260chartscanner.entity.CheckNum;
import com.dayuan.dy_6260chartscanner.entity.CheckNumber;
import com.dayuan.dy_6260chartscanner.entity.DeviceEntry;
import com.dayuan.dy_6260chartscanner.entity.Project;
import com.dayuan.dy_6260chartscanner.entity.ReportData;
import com.dayuan.dy_6260chartscanner.entity.TaskClass;
import com.dayuan.dy_6260chartscanner.util.CmdUtil;
import com.dayuan.dy_6260chartscanner.util.DialogUtil;
import com.dayuan.dy_6260chartscanner.util.ShowTime;
import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;
import com.hoho.android.usbserial.util.HexDump;
import com.hoho.android.usbserial.util.SerialInputOutputManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.text.Editable;
import android.text.InputFilter.LengthFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class DetectionChannelOneActvity extends BaseActivity implements
		OnClickListener {
	// private boolean isFirst ;
	private TextView tvCounter;
	private boolean isRunning = true;
	private boolean isLoop = true;
	// private boolean isChecked=true;
	private Button btnStartDetection;
	private Button btnGenerateReport;
	private Button btnDetectionAnniu, btnLoading;
	// 通道1里面的控件
	private TextView tvSample, tvChannel, tvChannelNum, tvValueC, tvValueT,
			tvResult;
	private LinearLayout llChannelComplete;
	private LinearLayout llChannelBar;
	private EditText etInputSample, etMultiple;
	private TextView tvDensity, tvNumber, tvNum, tvDensity01, tvMultiple,
			tvProName;
	CustomView customView;

	private Button btnComplete;
	private Button btnUpload;
	private Button btnPrint;
	// 插入卡的gif
	GifImageView gifImageView;
	// 显示加载gif动画
	GifImageView gifView;
	// 显示加载数据动态显示gif
	GifImageView gifViewLoad;

	ImageView imageView;
	private Button btnLine02;
	private Button btnLine03;
	private Button btnReturn;
	GifDrawable drawable;
	GifDrawable gDrawable;
	GifDrawable inDrawable;
	String etInput;
	// private static final int MESSAGE_UPDATE_TIME = 100;
	private static final int MESSAGE_UPDATE_BUTTON = 101;

	private final String TAG = "OpenDevice";
	private UsbManager mUsbManager;
	UsbSerialPort rPort;
	private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";;
	protected static final int MESSAGE_UPDATE_COMPLETE_BUTTON = 120;
	protected static final int MESSAGE_UPLOAD_COMPLETE_BUTTON = 160;

	// private String mDeviceName;
	private SerialInputOutputManager mSerialIoManager;
	private final static ExecutorService mExecutor = Executors
			.newSingleThreadExecutor();
	// private static final String ACTION_DEVICE_PERMISSION =
	// "com.android.hardware.USB_PERMISSION";

	public int threadsenddata = 0;
	public static final int ACTION_CLEAR = 11;
	public static final int ACTION_EXIT_CARD = 1;
	public static final int ACTION_ENTER_SCAN_CARD = 2;
	public static final int ACTION_READ_DATA = 3;
	protected static final int STANDARD_REQUEST_CODE = 200;
	public static final int ACTION_ENTER_CARD = 4;
	public static final int ACTION_STATUS_CARD = 5;
	private static final int NO_CARD_STATUS = 6;
	// private static final int HAVE_CARD_STATUS = 7;
	protected static final int DATA_MISTAKE = 8;
	private static final int USELESS_CARD = 10;
	protected static final int COUNT_STOP_TIME = 12;
	protected static final int MESSAGE_PUT_THE_CARD = 13;
	protected static final int MESSAGE_UPDATE_DATA_BUTTON = 14;

	private int myvid = 1155, mypid = 22336;
	private int myvid1 = 6790, mypid1 = 29987;
	UsbInterface[] usbinterface = null;
	UsbEndpoint[][] endpoint = new UsbEndpoint[5][5];
	private UsbDevice device = null;
	private UsbDevice mdevice = null;
	// private UsbDevice mUsbDevice; //找到的USB设备
	UsbSerialDriver mdriver;
	UsbSerialPort p;
	UsbDeviceConnection connection = null;
	ConnectedThread mconnectedthread = null;
	byte[] mybuffer = new byte[512];
	int datalength;
	boolean threadcontrol = false;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss",
			Locale.CHINA);
	private List<ReportData> datas;
	private List<TaskClass> taskclasses;
	private LogDao logDao;
	private Dao dao;
	// private DataDao dataDao;
	private OrderDao orderDao;
	String projectName;
	String result;
	String stringNumber;
	String method;
	String samplename;
	String sampleName;
	String dateTime;
	String multiple;
	String density;
	String data;
	private int number;
	private int chNumber;
	Bitmap bitmap;
	Display display;
	int width;
	int height;
	int w;
	int h;
	float scaleWidth;
	float scaleHeight;

	int key;
	int valC;
	int valT;
	int valTL;
	int ValueC;
	int ValueT;
	int length;
	int valueOne;
	int valueTwo;
	int valueThree;
	String resultChoice;
	private DecimalFormat f = new DecimalFormat("0000");
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			// case HAVE_CARD_STATUS:
			// threadsenddata = ACTION_ENTER_SCAN_CARD;
			// break;
			case MESSAGE_UPDATE_BUTTON:
				// threadsenddata = ACTION_ENTER_SCAN_CARD;
				gifImageView.setBackground(getResources().getDrawable(
						R.drawable.detection_anniu));
				// gifImageView.setGifImage(R.drawable.detection_anniu);
				// 显示加载gif动画
				gifView.setBackground(drawable);
				// gifView.setGifImage(R.drawable.loading_sample);
				// gifView.setVisibility(View.VISIBLE);
				// btnLoading.setVisibility(View.GONE);
				// 显示加载数据动态显示gif
				// 显示加载数据动态显示gif
				gifViewLoad.setVisibility(View.VISIBLE);
				gifViewLoad.setBackground(gDrawable);
				btnStartDetection.setVisibility(View.INVISIBLE);
				etInputSample.setCursorVisible(false);
				btnLine02.setSelected(false);
				btnLine03.setSelected(true);

				break;
			// case MESSAGE_UPDATE_DATA_BUTTON:
			// gifViewLoad.setVisibility(View.VISIBLE);
			// // gifViewLoad.setBackground(gDrawable);
			// gifViewLoad.setGifImage(R.drawable.data_loading);
			// break;
			case MESSAGE_UPDATE_COMPLETE_BUTTON:
				isRunning = false;
				llChannelBar.setVisibility(View.GONE);
				llChannelComplete.setVisibility(View.VISIBLE);
				gifView.setBackground(getResources().getDrawable(
						R.drawable.loading));
				// gifView.setGifImage(R.drawable.loading);
				// gifView.setVisibility(View.GONE);
				// btnLoading.setVisibility(View.VISIBLE);
				btnComplete.setSelected(true);
				tvSample.setText(etInputSample.getText().toString());
				tvMultiple.setText(etMultiple.getText().toString());
				btnGenerateReport.setVisibility(View.VISIBLE);
				btnPrint.setVisibility(View.VISIBLE);
				btnUpload.setVisibility(View.VISIBLE);
				btnStartDetection.setVisibility(View.GONE);

				customView.setData(getData());
				customView.invalidate();
				// 从sp中获取设置的状态，隐藏或者显示T/C值
				boolean showResult = TApplication.getInstance()
						.getSharedPreferencesIns()
						.getBoolean("showResult", false);
				if (showResult) {
					tvValueC.setText(ValueC + "");
					tvValueT.setText(ValueT + "");
				} else {
					tvValueC.setVisibility(View.GONE);
					tvValueT.setVisibility(View.GONE);
					tvValueC_tittle.setVisibility(View.GONE);
					tvValueT_tittle.setVisibility(View.GONE);
				}

				if (key == 2) {
					/*if (ValueC > valC) {
						if (valT >= ValueT) {
							tvResult.setText("阳性");
						} else if (ValueT > valTL) {
							tvResult.setText("阴性");
						} else if (ValueT > valT && ValueT < valTL) {
							tvResult.setText("可疑");
						}
					} else {
						tvResult.setText("无效");
					}*/
				} else if (key == 3) {
					if (ValueC > valC) {
						if (ValueC - ValueT >= valueOne) {
							tvResult.setText("阳性");
						} else if (ValueT - ValueC >= valueTwo) {
							tvResult.setText("阴性");
						} else if (Math.abs(ValueC - ValueT) < valueThree) {
							tvResult.setText(resultChoice);
						}
						//ph增加else判断无效无效
					}else{
						tvResult.setText("无效");
					}
				} else {
					tvResult.setText("无效");
				}
				// number++
				result = tvResult.getText().toString();
				density = tvDensity.getText().toString();
				samplename = etInputSample.getText().toString().trim();
				long time = System.currentTimeMillis();
				dateTime = sdf.format(time);
				if (!reList.isEmpty() && reList != null) {
					// 将结果存入数据库
					long id = logDao.add(number, samplename, projectName,
							result, dateTime, density, data);
				}
				dao.update(projectName, number, samplename);
				String order = orderDao.getOrder("print");
				if (order != null) {
					print();
				}
				String upload = orderDao.getUploadOrder("upload");
				if (upload != null) {
					upload();
				}
				btnReturn.setEnabled(true);
				break;
			case MESSAGE_UPLOAD_COMPLETE_BUTTON:
				ConnectivityManager mConnectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
				// 检查网络连接，如果无网络可用，就不需要进行连网操作等
				NetworkInfo info = mConnectivity.getActiveNetworkInfo();
				if (info == null || !mConnectivity.getBackgroundDataSetting()) {
					Toast.makeText(DetectionChannelOneActvity.this, "请检查网络", 0)
							.show();
				} else {
					UploadData.uploadCheckData(DetectionChannelOneActvity.this,
							number + "", samplename, projectName, result,
							dateTime, density);
				}
				break;
			case UNABLE_GENERATE_REPORT:
				Toast.makeText(DetectionChannelOneActvity.this,
						"样品名称和检测项目与抽检单号不符，请检查输入", 1).show();
				break;
			case NO_CARD_STATUS:
				Toast.makeText(DetectionChannelOneActvity.this, "当前为无卡状态，请插入卡",
						0).show();
				btnReturn.setEnabled(true);
				break;

			case DATA_MISTAKE:
				Toast.makeText(DetectionChannelOneActvity.this, "数据错误", 0)
						.show();
				break;
			case USELESS_CARD:
				Toast.makeText(DetectionChannelOneActvity.this, "此卡为无效卡", 0)
						.show();
				break;
			case COUNT_STOP_TIME:
				btnStartDetection.setEnabled(true);
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detection_channel_one);

		logDao = new LogDao(this);
		dao = new Dao(this);
		Intent intent = getIntent();
		sampleName = intent.getStringExtra("sampleName");
		projectName = intent.getStringExtra("name");
		String channel = intent.getStringExtra("channel");
		stringNumber = intent.getStringExtra("number");
		number = Integer.parseInt(stringNumber) + 1;
		String meth = intent.getStringExtra("method");
		String valueC = intent.getStringExtra("valueC");
		String valueT = intent.getStringExtra("valueT");
		String valueTL = intent.getStringExtra("valueTL");

		method = ((Integer.parseInt(meth) < 2) ? "定量" : "定性");
		key = Integer.parseInt(meth);
		valC = Integer.parseInt(valueC);
		if (key == 2) {
			valT = Integer.parseInt(valueT);
			valTL = Integer.parseInt(valueTL);
		} else if (key == 3) {
			valueOne = Integer.parseInt(intent.getStringExtra("valueOne"));
			valueTwo = Integer.parseInt(intent.getStringExtra("valueTwo"));
			valueThree = Integer.parseInt(intent.getStringExtra("valueThree"));
			resultChoice = intent.getStringExtra("resultChoice");
		}
		chNumber = Integer.parseInt(channel);
		// }
		initViews();
		isLoop = true;
		ShowTime.ShowTime(this, isLoop, tvCounter);
		if (chNumber == 1) {
			tvChannel.setText("通道2");
			tvChannelNum.setText("通道2");
		} else if (chNumber == 0) {
			tvChannel.setText("通道1");
			tvChannelNum.setText("通道1");
		}

		// int key = Integer.parseInt(method);
		if (key > 1) {
			etMultiple.setText("—");
			tvDensity01.setText("—");
			etMultiple.setEnabled(false);
			tvDensity.setText("—");
			tvMultiple.setText("—");
		}
		if (openDevice()) {

			if (mconnectedthread != null) {
				mconnectedthread = null;
			}
			if (device != null && device.getVendorId() == myvid
					&& device.getProductId() == mypid) {
				mconnectedthread = new ConnectedThread();
				mconnectedthread.start();

				threadcontrol = true;
				threadsenddata = ACTION_EXIT_CARD;
				btnStartDetection.setEnabled(false);
				handler.postDelayed(new Runnable() {

					@Override
					public void run() {
						handler.sendEmptyMessage(COUNT_STOP_TIME);

					}
				}, 3000);
			}
		}

	}

	private void initViews() {
		tvValueC_tittle = (TextView) findViewById(R.id.tv_c);
		tvValueT_tittle = (TextView) findViewById(R.id.tv_t);

		tvProName = (TextView) findViewById(R.id.tv_sample_name);
		tvResult = (TextView) findViewById(R.id.tv_result);
		tvChannelNum = (TextView) findViewById(R.id.tv_channel_num);
		tvChannel = (TextView) findViewById(R.id.tv_channel);
		tvSample = (TextView) findViewById(R.id.tv_sample);
		tvValueC = (TextView) findViewById(R.id.tv_value_c);
		tvValueT = (TextView) findViewById(R.id.tv_value_t);
		customView = (CustomView) findViewById(R.id.customView);
		llChannelComplete = (LinearLayout) findViewById(R.id.ll_channel_complete);
		llChannelBar = (LinearLayout) findViewById(R.id.ll_channel_bar);
		etInputSample = (EditText) findViewById(R.id.et_input_sample);
		etMultiple = (EditText) findViewById(R.id.et_input_multiple);
		tvMultiple = (TextView) findViewById(R.id.tv_get_multiple);
		tvDensity = (TextView) findViewById(R.id.tv_density);
		tvDensity01 = (TextView) findViewById(R.id.tv_density_);
		tvNum = (TextView) findViewById(R.id.tv_id_number);
		tvNumber = (TextView) findViewById(R.id.tv_number);
		gifViewLoad = (GifImageView) findViewById(R.id.gif_data_loading);

		btnReturn = (Button) findViewById(R.id.btn_return);
		btnLine02 = (Button) findViewById(R.id.btn_line_02);
		btnLine03 = (Button) findViewById(R.id.btn_line_03);
		btnComplete = (Button) findViewById(R.id.btn_complete);
		btnGenerateReport = (Button) findViewById(R.id.btn_generate_report);
		btnUpload = (Button) findViewById(R.id.btn_upload);
		btnPrint = (Button) findViewById(R.id.btn_print);
		btnStartDetection = (Button) findViewById(R.id.btn_start_detection);
		gifImageView = (GifImageView) findViewById(R.id.gif_input_card);
		gifView = (GifImageView) findViewById(R.id.gif_loading);
		// btnDetectionAnniu=(Button) findViewById(R.id.btn_detection_anniu);
		// btnLoading=(Button) findViewById(R.id.btn_loading);
		etInputSample.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				Intent intent = new Intent(DetectionChannelOneActvity.this,
						CheckStandardActivity.class);
				intent.putExtra("get", "get");
				intent.putExtra("name", projectName);
				startActivityForResult(intent, STANDARD_REQUEST_CODE);

				return true;
			}
		});
		etInputSample.setFocusable(true);
		etInputSample.setText(sampleName);
		etInput = etInputSample.getText().toString().trim();
		multiple = etMultiple.getText().toString();
		// etInput = etInputSample.getText().toString();
		tvCounter = (TextView) findViewById(R.id.tv_counter);
		// Toast.makeText(DetectionChannelOneActvity.this, "请放入金标卡",
		// Toast.LENGTH_LONG).show();
		try {
			btnLine02.setSelected(true);
			GifDrawable gifDrawable = new GifDrawable(getResources(),
					R.drawable.put_the_card);
			gifImageView.setBackground(gifDrawable);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// gifImageView.setGifImage(R.drawable.put_the_card);
		// handler.sendEmptyMessage(MESSAGE_PUT_THE_CARD);

		btnStartDetection.setOnClickListener(this);
		btnGenerateReport.setOnClickListener(this);
		btnUpload.setOnClickListener(this);
		btnPrint.setOnClickListener(this);
		btnReturn.setOnClickListener(this);
		// customView.setOnClickListener(this);
		tvNum.setText(f.format(number));
		tvNumber.setText(f.format(number));
		tvProName.setText(projectName);
		if (etInput.isEmpty()) {
			btnReturn.setEnabled(true);
		}
	}

	private final BroadcastReceiver musbReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (ACTION_USB_PERMISSION.equals(action)) {
				synchronized (this) {
					UsbDevice device = intent
							.getParcelableExtra(UsbManager.EXTRA_DEVICE);
					System.out
							.println("UsbManager.EXTRA_DEVICE 22222222222222222 ========"
									+ intent.getParcelableExtra(UsbManager.EXTRA_DEVICE));
					System.out.println("是否有权限了？？？？？？   "
							+ mUsbManager.hasPermission(device));
					if (intent.getBooleanExtra(
							UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
						if (device != null) {
							Log.i(TAG, "usb EXTRA_PERMISSION_GRANTED");
							// 新增刷新页面
							onCreate(null);
						}
					} else {
						Log.i(TAG, "usb EXTRA_PERMISSION_GRANTED null!!!");
					}
				}
			}
		}
	};

	public Boolean openDevice() {
		mUsbManager = (UsbManager) (this).getSystemService(Context.USB_SERVICE);
		if (mUsbManager == null) {
			return false;
		} else {
			Log.i(TAG, "usb设备：" + String.valueOf(mUsbManager.toString()));
		}
		HashMap<String, UsbDevice> deviceList = mUsbManager.getDeviceList();
		if (!deviceList.isEmpty()) {
			Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
			// int key = 0;
			Integer num = 0;
			int i = 0;
			int size = 0;
			List<Integer> lists = new ArrayList<>();
			Iterator<UsbDevice> iterator = deviceList.values().iterator();

			while (deviceIterator.hasNext()) {

				device = deviceIterator.next();
				Log.i(TAG,
						"vid: " + device.getVendorId() + "\t pid: "
								+ device.getProductId());
				if (device != null && device.getVendorId() == myvid
						&& device.getProductId() == mypid) {
					String deviceName = device.getDeviceName();
					String deviceId = deviceName.substring(18);
					num = Integer.parseInt(deviceId);
					lists.add(num);
				}

			}
			// ph新增切换通道的选项
			boolean isChange = TApplication.getInstance()
					.getSharedPreferencesIns().getBoolean("isChange", false);
			if (isChange) {
				if (chNumber == 1) {
					chNumber = 0;
				} else if (chNumber == 0) {
					chNumber = 1;
				}
			}

			size = lists.get(0) - lists.get(1);
			while (iterator.hasNext()) {
				device = iterator.next();
				if (device != null && device.getVendorId() == myvid
						&& device.getProductId() == mypid) {
					if (size < 0 && chNumber == i) {
						break;
					} else if (size > 0 && chNumber != i) {
						break;
					}
					i++;
				}
			}
			// if (!iterator.hasNext()) {
			// Log.d(TAG, "未发现支持设备");
			// Toast.makeText(DetectionChannelOneActvity.this,
			// "未发现支持设备", Toast.LENGTH_SHORT).show();
			//
			// return false;
			// }
		}
		if (device != null)
		// && device.getVendorId() == myvid
		// && device.getProductId() == mypid)
		{
			PendingIntent mPermissionIntent = PendingIntent.getBroadcast(this,
					0, new Intent(ACTION_USB_PERMISSION), 0);
			IntentFilter permissionFilter = new IntentFilter(
					ACTION_USB_PERMISSION);
			registerReceiver(musbReceiver, permissionFilter);
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
			// Intent intent = new Intent();
			// intent.setAction(ACTION_USB_PERMISSION);
			// IntentFilter filter = new IntentFilter();
			// filter.addAction(ACTION_USB_PERMISSION);
			// filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
			// registerReceiver(musbReceiver, filter);
			//
			// intent.putExtra(UsbManager.EXTRA_DEVICE, device);
			// intent.putExtra(UsbManager.EXTRA_PERMISSION_GRANTED, true);
			//
			// final PackageManager pm = getPackageManager();
			// try {
			// ApplicationInfo aInfo = pm.getApplicationInfo(getPackageName(),
			// 0);
			// try {
			// IBinder b = ServiceManager.getService(Context.USB_SERVICE);
			// IUsbManager service = IUsbManager.Stub.asInterface(b);
			// System.out.println("deviceName:"+device.getDeviceName());
			// System.out.println("uid:"+aInfo.uid);
			// service.grantDevicePermission(device, aInfo.uid);
			// service.setDevicePackage(device, getPackageName(),aInfo.uid);
			// } catch (RemoteException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			// } catch (NameNotFoundException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			//
			// getApplicationContext().sendBroadcast(intent);
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

		byte[] CMD_READ_DATA = { 0x7E, 0x15, 0x00, 0x00, 0x15, 0x7E };
		byte[] CMD_EXIT_CARD = { 0x7E, 0x11, 0x00, 0x01, 0x02, 0x14, 0x7E };
		byte[] CMD_ENTER_CARD = { 0x7E, 0x11, 0x00, 0x01, 0x03, 0x15, 0x7E };
		byte[] CMD_ENTER_SCAN_CARD = { 0x7E, 0x11, 0x00, 0x01, 0x01, 0x13, 0x7E };
		byte[] CMD_STATUS_CARD = { 0x7E, 0x13, 0x00, 0x00, 0x13, 0x7E };

		@Override
		public void run() {
			while (threadcontrol) {
				if (threadsenddata != ACTION_CLEAR) {
					switch (threadsenddata) {
					case ACTION_EXIT_CARD:
						cardsta = 0;
						connection.bulkTransfer(endpoint[1][0], CMD_EXIT_CARD,
								CMD_EXIT_CARD.length, 30);
						break;
					case ACTION_STATUS_CARD:

						connection.bulkTransfer(endpoint[1][0],
								CMD_STATUS_CARD, CMD_STATUS_CARD.length, 30);
						break;
					case ACTION_ENTER_SCAN_CARD:
						cardsta = 1;
						connection.bulkTransfer(endpoint[1][0],
								CMD_ENTER_SCAN_CARD,
								CMD_ENTER_SCAN_CARD.length, 30);
						break;
					case ACTION_READ_DATA:
						connection.bulkTransfer(endpoint[1][0], CMD_READ_DATA,
								CMD_READ_DATA.length, 30);
						Log.i(TAG, "read data");
						break;

					case ACTION_ENTER_CARD:
						cardsta = 3;
						connection.bulkTransfer(endpoint[1][0], CMD_ENTER_CARD,
								CMD_ENTER_CARD.length, 30);
						break;

					default: {
					}
					}
					threadsenddata = ACTION_CLEAR;
				}

				readData();

				Log.i(TAG, "thread id: " + Thread.currentThread().getId() + "");
				try {
					Thread.sleep(100);
					if (threadsenddata == ACTION_CLEAR)
						threadsenddata = ACTION_READ_DATA;
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
	}

	double[] inData;
	List<Integer> list = new ArrayList<Integer>();
	List<Integer> reList;
	Integer index = 0;
	// List<double[]> _tValues;
	double[] _tValue;

	public List<Integer> getData() {
		reList = new ArrayList<Integer>();
		// 读取卡原数据
		Integer[] buffer = new Integer[list.size()];
		buffer = list.toArray(buffer);
		if (buffer != null) {
			double[] inBuffer = new double[buffer.length];
			for (int i = 0; i < buffer.length; i++) {
				inBuffer[i] = buffer[i].doubleValue();
			}
			// Integer[] reBuffer=
			Integer[] idx = CurveSmoothing.FindUsefulData(
					DetectionChannelOneActvity.this, inBuffer);
			// 将Integer数组转换为int数组
			if (idx != null) {
				int[] idex = new int[idx.length];
				for (int i = 0; i < idx.length; i++) {
					idex[i] = idx[i].intValue();
				}
				int len = idex[1] - idex[0];
				_tValue = new double[len];
				System.arraycopy(inBuffer, idex[0], _tValue, 0, len);
				double[] oldData = new double[len];
				for (int i = 0; i < len; i++) {
					oldData[i] = _tValue[i];
				}
				_tValue = CurveSmoothing.BaselineCorrect(_tValue);
				double[] values = new double[_tValue.length - 1];
				System.arraycopy(_tValue, 1, values, 0, values.length);
				_tValue = new double[values.length];
				for (int i = 0; i < values.length; i++)
					_tValue[i] = values[i];

				double[][] dbs = new double[2][];

				dbs[0] = _tValue;

				dbs[1] = new double[len];

				dbs = CurveSmoothing.FFT(dbs, 1);

				dbs = CurveSmoothing.FLT(dbs);

				dbs = CurveSmoothing.FFT(dbs, -1);

				_tValue = CurveSmoothing.SUM(dbs);

				// oldData = new double[len];
				//
				// for (int i = 0; i < len; i++)
				//
				// oldData[i] = _tValue[i];
				//
				// _tValue = CurveSmoothing.ZB(_tValue);
			} else {
				_tValue = new double[64];

				for (int i = 0; i < _tValue.length; i++)

				{

					_tValue[i] = 10000;

				}
			}

			// _tValues.add(_tValue);
			if (_tValue != null) {
				Integer[] tValue = new Integer[_tValue.length];
				for (int i = 0; i < _tValue.length; i++) {
					tValue[i] = (int) _tValue[i];
				}
				reList = Arrays.asList(tValue);
				Object[] intData = reList.toArray();
				//ph截取后面五个点
				reList=reList.subList(0, reList.size()-5);
				data = Arrays.toString(intData);
				// Value();

			} else {
				Toast.makeText(this, "此卡为无效卡", 0).show();
			}
			value();
		} else {
			// _tValues.add(null);
		}

		// 求曲线面积
		// double _ValueC = 0; double _ValueT = 0;
		// for (int i = 0; i < length; i++)
		// {
		// if (i == 0 && cIndex[0] > 0) i = cIndex[0];
		// if (tIndex[1] > 0 && i > tIndex[1]) break;
		// if (i >= cIndex[0] && i <= cIndex[1]) _ValueC[0] +=
		// Math.abs(_tValue[0] - _tValue[i]);
		// if (i >= tIndex[0] && i <= tIndex[1]) _ValueT[0] +=
		// Math.abs(_tValue[length - 1] - _tValue[i]);
		// }
		return reList;

	}

	private void value() {
		int length = _tValue.length;
		int CIndex = 0, TIndex = 0;
		int valueC = 0;
		int valueT = 0;
		int peak = 0;
		
		
		
		
		/**
		 * 根据曲线状态判断阴性阳性
		 * 
		 */
		 
		int count = 0;
		int index1 = 0;
		boolean line=false;
		for (int i = 5; i < _tValue.length - 1; i++) {
			if ((_tValue[i + 1] - _tValue[i]) < 0) {
				count++;
				if(count>=5){
					for(int j =i;j<_tValue.length - 1;j++){
						if(_tValue[j + 1] - _tValue[j] > 0&&_tValue[j-5]-_tValue[j]>150){
							System.out.println("第一个波谷值"+_tValue[i]+"---波谷开始值"
						+_tValue[i-5]+"差值"+(_tValue[i-5]-_tValue[i])+"C坐标"+j);
							
							System.out.println("波谷1值坐标" + j );
									
							
							index1=j;
							line=true;
							break;
						}
					}
					break;
				}

			}else{
				count=0;
			}
		}
		
		int count2 = 0;
		int index2 = 0;
		boolean line2=false;
		for (int i = index1; i < _tValue.length - 1; i++) {
			if ((_tValue[i + 1] - _tValue[i]) < 0) {
				count2++;
				//&&_tValue[i-5]-_tValue[i]>100
				if (count2 >= 5&&_tValue[i-5]-_tValue[i]>150) {
				
					for(int j =i;j<_tValue.length - 1;j++){
						if(_tValue[j + 1] - _tValue[j] > 0){
							System.out.println("第二个波谷值"+_tValue[i]+"---波谷开始值"
						+_tValue[i-5]+"差值"+(_tValue[i-5]-_tValue[i])+"T坐标"+j);
							index2=j;
							System.out.println("波谷1值坐标" + j+ "总长度"+_tValue.length);
							line2=true;
							break;
						}
					}
					
					break;
				}

			}else{
				count2=0;
			}
		}		
		if(line){
			//index1改成20原始值30
			if(index1<=20&&index1>=10&&line2&&index2>25&&index2<_tValue.length-5){
				tvResult.setText("阴性");
			}else if((index1<=20&&!line2)||(index1<=20&&line2&&index2<=20)||(index1<=20&&line2&&index2>_tValue.length-5)){
				tvResult.setText("阳性");
			}else if(index1>20){
				tvResult.setText("无效"); 
			}
		}else{
			tvResult.setText("无效");
		}
		/**
		 *  根据曲线状态判断阴性阳性
		 */
		
		
		
		
		
		
		// C值波谷坐标 10-30
		for (int j = 0; j < length; ++j) {
			if (j == 0)
				j = 10;
			if (j > 20)
				break;
			if (valueC == 0) {
				valueC = (int) _tValue[j];
				CIndex = j;
			}
			if (valueC > _tValue[j]) {
				valueC = (int) _tValue[j];
				CIndex = j;
			}

		}
		// T值波谷坐标 25-45
		for (int j = 0; j < length; ++j) {
			if (j == 0)
				j = 30;
			if (j > 40)
				break;
			if (valueT == 0) {
				valueT = (int) _tValue[j];
				TIndex = j;
			}
			if (valueT > _tValue[j]) {
				valueT = (int) _tValue[j];
				TIndex = j;
			}
		}
		double IDX = 0;
		// C和T之间的波峰
		for (int j = 0; j < length; j++) {
			if (j == 0)
				j = CIndex;
			if (j >= TIndex)
				break;
			if (peak == 0) {
				peak = (int) _tValue[j];
				IDX = j;
			}

			if (peak < _tValue[j]) {
				peak = (int) _tValue[j];
				IDX = j;
			}
		}
		int index = 5;
		if (_tValue.length - TIndex <= index)
			TIndex = _tValue.length - index - 1;
		// 重新计算C值和T值
		// C=peak-valuec;T=peak-valuet
		ValueC = (int) (peak - _tValue[CIndex]);
		ValueT = (int) (peak - _tValue[TIndex]);
		// int length = _tValue.length, radius = _tValue.length / 2;
		// int CIndex = 0, TIndex = 0;int valueC=0; int valueT=0;
		// int ValueCTotal=0; int ValueTTotal=0;int ValueTtotal=0;
		// //C值坐标
		// for (int i = 0; i < length; ++i)
		// {
		// if (i == radius) break;
		//
		// valueC = (int) (valueC == 0 ? _tValue[i] :valueC);
		// if (valueC > _tValue[i])
		// {
		// valueC = (int) _tValue[i];
		// CIndex = i;
		// }
		// }
		// //T值坐标
		// for (int i = 0; i < length; ++i)
		// {
		// if (i == 0) i = radius;
		//
		// if(i>radius+1){
		//
		// valueT = (int) (valueT == 0 ? _tValue[i] : valueT);
		//
		// if (valueT > _tValue[i])
		// {
		// valueT = (int) _tValue[i];
		// TIndex = i;
		// }
		// }
		// }
		// //确定C和T的范围
		// int[] cIndex = new int[2];
		// int[] tIndex = new int[2];
		// //C值坐标
		// if (CIndex > 0)
		// {
		// for (int i = length; i >= 1; i--)
		// {
		// if (CIndex > 0 && i == length) i = CIndex;
		// if (Math.abs(10000 - _tValue[i]) > 0.5) cIndex[0] = i;//_tValue[0]
		// else break;
		// }
		// for (int i = 0; i < length; i++)
		// {
		// if (CIndex > 0 && i == 0) i = CIndex;
		// if (Math.abs(10000 - _tValue[i]) < 0.5) cIndex[1] = cIndex[1] == 0 ?
		// i - 1 : cIndex[1];
		// // if (i > radius) break;
		// }
		// }
		// //T值坐标
		// if (TIndex > 0)
		// {
		// for (int i = length; i >= 0; i--)
		// {
		// if (TIndex > 0 && i == length) i = TIndex;
		// if (Math.abs(10000 - _tValue[i]) > 0.5) tIndex[0] =
		// i;//_tValue[length - 1]
		// else break;
		// }
		// for (int i = 0; i < length; i++)
		// {
		// if (TIndex > 0 && i == 0) i = TIndex;
		// if (Math.abs(_tValue[length - 1] - _tValue[i]) < 0.5) tIndex[1] =
		// tIndex[1] == 0 ? i - 1 : tIndex[1];//_tValue[length - 1]
		// }
		// }
		// for (int i = 0; i < length; i++)
		// {
		// if (i == 0 && cIndex[0] > 0) i = cIndex[0];
		// if (tIndex[1] > 0 && i > tIndex[1]) break;
		// if(i >= cIndex[0] && i <= cIndex[1]) ValueCTotal +=(int)
		// Math.abs(10000 - _tValue[i]);//_tValue[0]
		// if (i >= tIndex[0] && i <= TIndex) ValueTTotal +=Math.abs(10000 -
		// _tValue[i]);//_tValue[length - 1]
		// if(i>TIndex&&i<=tIndex[1]) ValueTtotal +=Math.abs(_tValue[length - 1]
		// - _tValue[i]);
		// }
		// //C值的个数，T值的个数
		// int countC=cIndex[1]-cIndex[0]+2;
		// int countT=tIndex[1]-tIndex[0]+2;
		//
		// ValueC=ValueCTotal/countC ;
		// ValueT=(ValueTTotal+ValueTtotal)/countT ;

	}

	// public void Value() {
	//
	// // int size = list.size();
	// float MaxNum1 = 0;
	// float MaxNum2 = 0;
	// float MaxNum3 = 0;
	// valueT=valueC=0;
	// //int i = 0;
	// // 锟斤拷一锟斤拷锟斤拷锟斤拷
	// int maxIndex1 = 0, maxIndex2 = 0, maxIndex3 = 0, CIndex = 0, TIndex = 0;
	// for (int i = 0; i < reList.size(); i++) {
	// if(reList.size()>42){
	// if (i < reList.size()-40) {
	// if (reList.get(i) > MaxNum1) {
	// MaxNum1 = reList.get(i);
	// maxIndex1 = i;
	// Log.i("diyigebofeng", MaxNum1+"");
	// }
	// }
	// }else{
	// if (i < reList.size()-30) {
	// if (reList.get(i) > MaxNum1) {
	// MaxNum1 = reList.get(i);
	// maxIndex1 = i;
	// Log.i("diyigebofeng", MaxNum1+"");
	// }
	// }
	// }
	// }
	// // 值
	// for (int i = maxIndex1; i < reList.size(); i++) {
	// // if (i == 0) {
	// // i = maxIndex1;
	// // }
	// if (i <= maxIndex1 + 25) {
	// valueC = valueC == 0 ? reList.get(i) : valueC;
	// if (valueC > reList.get(i)) {
	// valueC = reList.get(i);
	// CIndex = i;
	// Log.i("valueC", valueC+"");
	// }
	// } else {
	// break;
	// }
	// }
	// // 锟节讹拷锟斤拷锟斤拷锟斤拷
	// for (int i = CIndex; i < reList.size(); i++) {
	// // if (i == 0) {
	// // i = CIndex;
	// // }
	// if(reList.size()>42){
	// if (i < CIndex + 20) {
	// if (reList.get(i) > MaxNum2) {
	// MaxNum2 = reList.get(i);
	// maxIndex2 = i;
	// Log.i("MaxNum2", MaxNum2+"");
	// }
	// } else {
	// break;
	// }
	// }else{
	// if (i < CIndex + 8) {
	// if (reList.get(i) > MaxNum2) {
	// MaxNum2 = reList.get(i);
	// maxIndex2 = i;
	// Log.i("MaxNum2", MaxNum2+"");
	// }
	// } else {
	// break;
	// }
	// }
	// }
	// for (int i = maxIndex2; i < reList.size(); i++) {
	// // if (i == 0) {
	// // i = maxIndex2;
	// // }
	// if(reList.size()>42){
	// if(i>=maxIndex2+2){
	// if (reList.get(i) > MaxNum3) {
	// MaxNum3 = reList.get(i);
	// maxIndex3 = i;
	// }
	// }
	// }else{
	// if (reList.get(i) > MaxNum3) {
	// MaxNum3 = reList.get(i);
	// maxIndex3 = i;
	// }
	// }
	// }
	// for (int i =maxIndex2; i < reList.size(); i++) {
	// // if (i == 0) {
	// // i = maxIndex2;
	// // }
	// if(reList.size()>50){
	// if (i <reList.size()-10) {
	// valueT = valueT == 0 ? reList.get(i) : valueT;
	// if (valueT > reList.get(i)) {
	// valueT = reList.get(i);
	// TIndex = i;
	// Log.i("valueT", valueT+"");
	// }
	// } else {
	// break;
	// }
	// }
	// else{
	// if (i <reList.size()-5) {
	// valueT = valueT == 0 ? reList.get(i) : valueT;
	// if (valueT > reList.get(i)) {
	// valueT = reList.get(i);
	// TIndex = i;
	// Log.i("valueT", valueT+"");
	// }
	// } else {
	// break;
	// }
	// }
	// }
	// ValueC = (int) (MaxNum2 - valueC);
	// ValueT = (int) (MaxNum2 - valueT);
	//
	// Log.i("valueC", valueC + "");
	// Log.i("valueT", valueT + "");
	// Log.i("ValueC", ValueC + "");
	// Log.i("ValueT", ValueT + "");
	//
	// }

	public void readData() {

		datalength = connection.bulkTransfer(endpoint[1][1], mybuffer, 512,
				2000);

		if (datalength > 0) {

			if (datalength < 4) {
				Log.e(TAG, "len error!");
				return;
			}

			int flag = mybuffer[0];
			int cmd = mybuffer[1];
			int len = (mybuffer[2] << 8) | (mybuffer[3] & 0x0FF);
			int endflag = mybuffer[datalength - 1];

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

						threadsenddata = ACTION_ENTER_SCAN_CARD;
						// handler.sendEmptyMessage(HAVE_CARD_STATUS);
						isRunning = true;
						new Thread() {
							@Override
							public void run() {
								while (isRunning) {
									try {
										drawable = new GifDrawable(
												getResources(),
												R.drawable.loading_sample);
										gDrawable = new GifDrawable(
												getResources(),
												R.drawable.data_loading);
										handler.sendEmptyMessage(MESSAGE_UPDATE_BUTTON);
										Thread.sleep(1000);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (IOException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}

								}
								// 显示加载数据动态显示gif
							}
						}.start();

						handler.postDelayed(new Runnable() {
							@Override
							public void run() {
								handler.sendEmptyMessage(MESSAGE_UPDATE_COMPLETE_BUTTON);
							}
						}, 8000);
						Log.i(TAG, "有卡");
					}

					else if (mybuffer[4] == 0x02) {// 无卡

						handler.sendEmptyMessage(NO_CARD_STATUS);
						Log.i(TAG, "无卡");
					} else {// 数据错误
						handler.sendEmptyMessage(DATA_MISTAKE);
						Log.i(TAG, "数据错误");
					}
				}

				break;
			case 0x12: {
				if (cardsta == 1) {
					Log.i(TAG, "进卡");
					list.clear();
				} else {
					Log.i(TAG, "出卡");
				}

				break;
			}

			case 0x16: {
				// List<Integer> list=new ArrayList<Integer>();

				if (len > 0)

				{
					// int[] ADbuffer=new int[200];
					// for(int i=0;i<len;i=+2){
					// ADbuffer[i/2]=(mybuffer[5 + i] & 0x0FF) * 256
					// + (mybuffer[4 + i] & 0x0FF);
					// }
					// ADbuffer=GetData.FindUsefulData(DetectionChannelOneActvity.this,len/2,ADbuffer);
					// if(ADbuffer==null){
					// handler.sendEmptyMessage(USELESS_CARD);
					//
					// }
					for (int i = 0; i < len; i = i + 2) {

						int data01 = (mybuffer[5 + i] & 0x0FF) * 256
								+ (mybuffer[4 + i] & 0x0FF);
						list.add(data01);
						// list.add(ADbuffer[i]);
					}

					Log.i(TAG, "" + list);
					length = list.size();
					inData = new double[length];
					for (int i = 0; i < list.size(); i++) {
						inData[i] = list.get(i);
					}
				}

				break;
			}
			default: {

			}
			}
		}
	}

	public void OpenDevices(int baudRate, int dataBits, int stopBits, int parity)
			throws IOException {
		UsbDeviceConnection connection = null;
		mUsbManager = (UsbManager) (this).getSystemService(Context.USB_SERVICE);
		HashMap<String, UsbDevice> deviceList = mUsbManager.getDeviceList();
		if (!deviceList.isEmpty()) {
			Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
			while (deviceIterator.hasNext()) {
				mdevice = deviceIterator.next();
				Log.i(TAG, "vid: " + mdevice.getVendorId() + "\t pid: "
						+ device.getProductId());
				if (mdevice.getVendorId() == myvid1
						&& mdevice.getProductId() == mypid1) {
					break;
				}
			}
			if (mdevice != null && mdevice.getVendorId() == myvid1
					&& mdevice.getProductId() == mypid1) {
				// mUsbDevice = device;
				Log.i(TAG, "找到设备:" + mdevice.getVendorId() + "\t pid: "
						+ mdevice.getProductId());
			} else {
				Log.d(TAG, "未发现支持设备");
				// Toast.makeText(DetectionChannelOneActvity.this,
				// "USB接口已断开，请连接USB接口", Toast.LENGTH_SHORT).show();
				// finish();
				// return false;
			}
		}
		if (null == mdevice) {
			Log.i(TAG, "No serial device.");
			return;
		}
		UsbSerialDriver driver = UsbSerialProber.getDefaultProber()
				.probeDevice(mdevice);
		if (null == driver) {
			Log.i(TAG, "No serial device.");
			return;
		}
		// //
		for (int i = 0; i < driver.getPorts().size(); ++i) {
			UsbSerialPort p = driver.getPorts().get(i);
			Log.i(TAG, "Port: " + p);
		}
		Log.e(TAG, "Interface count: " + mdevice.getInterfaceCount());
		rPort = driver.getPorts().get(0);
		//
		if (mdevice != null && mdevice.getVendorId() == myvid1
				&& mdevice.getProductId() == mypid1) {
			PendingIntent mPermissionIntent = PendingIntent.getBroadcast(this,
					0, new Intent(ACTION_USB_PERMISSION), 0);
			IntentFilter permissionFilter = new IntentFilter(
					ACTION_USB_PERMISSION);
			registerReceiver(musbReceiver, permissionFilter);
			if (mUsbManager.hasPermission(mdevice)) {
				connection = mUsbManager.openDevice(driver.getDevice());

			} else {

				mUsbManager.requestPermission(mdevice, mPermissionIntent);
			}
			if (mUsbManager.hasPermission(mdevice)) {
				Log.i(TAG, "拥有访问权限");
			} else {
				Log.d(TAG, "未获得访问权限");
			}

		}

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
		int len = etInput.length();
		String deviceCmd = null;
		String sampleName = etInputSample.getText().toString();
		String line = "\r\n";
		String testPerson = "检验者：" + "_ _ _ _ _ _ _ _" + line + line;
		String confirmPerson = "审核者：" + "_ _ _ _ _ _ _ _" + line;
		String time = "检测时间:  " + dateTime;
		String samplename = "样品:  " + sampleName;
		String project = "编号  " + "样品名称      " + "浓度  " + "结果";
		String space = " ";
		if (len >= 6) {
			spaceNum = etInput.substring(0, 6);
		} else if (len < 6) {
			int newlen=6-len;
			switch(newlen){
			case 1:
				spaceNum = etInput +space+space;
				break;
			case 2:
				spaceNum = etInput +space+space+space+space;
				break;
			case 3:
				spaceNum = etInput +space+space+space+space+space+space;
				break;
			case 4:
				spaceNum = etInput +space+space+space+space+space+space+space+space;
				break;
			case 5:
				spaceNum = etInput +space+space+space+space+space+space+space+space+space+space;
				break;
				default :
				spaceNum=etInput +space+space+space+space+space;
			}
		}	
		deviceCmd ="------------------------------"+line+f.format(number) + space + space+ spaceNum+ space+space+space+density + space + space+ space
				+ result + line +"------------------------------"+line+ project + line + line
				+ time + line + "设备：胶体金读卡仪"
				+ line+line;
		
		
		
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
		IntentFilter usbFilter = new IntentFilter();
		usbFilter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
		usbFilter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
		registerReceiver(musbReceiver, usbFilter);
		isRunning = true;
		ShowTime.ShowTime(this, isRunning, tvCounter);
		super.onResume();
		try {
			OpenDevices(9600, 8, UsbSerialPort.STOPBITS_1,
					UsbSerialPort.PARITY_NONE);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		unregisterReceiver(musbReceiver);
		// Toast.makeText(DetectionChannelOneActvity.this, "USB接口已断开，请连接USB接口",
		// Toast.LENGTH_SHORT).show();
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
		// finish();
	}

	@Override
	protected void onDestroy() {
		if (connection != null) {
			connection.close();
		}
		isRunning = false;
		isLoop = false;
		threadcontrol = false;
		super.onDestroy();

	}

	@Override
	public void onClick(View v) {
		orderDao = new OrderDao(this);
		switch (v.getId()) {
		case R.id.btn_generate_report:
			generateReport();
			break;
		case R.id.btn_print:
			print();

			break;

		case R.id.btn_upload:
			upload();

			break;
		case R.id.btn_start_detection:

			isRunning = false;
			etInput = etInputSample.getText().toString().trim();
			if (!etInput.isEmpty()) {
				if (device != null) {
					// &&device.getVendorId() == myvid &&
					// }
					// device.getProductId() == mypid){
					threadsenddata = ACTION_STATUS_CARD;
					// gifImageView.setVisibility(View.GONE);
					// btnDetectionAnniu.setVisibility(View.VISIBLE);
					// gifViewLoad.setVisibility(View.VISIBLE);
					// gifViewLoad.setGifImage(R.drawable.data_loading);
					// gifViewLoad.setShowDimension(216, 20);
					btnReturn.setEnabled(false);
				}

			} else if (etInput.isEmpty()) {
				Toast.makeText(DetectionChannelOneActvity.this, "请输入样品名称",
						Toast.LENGTH_LONG).show();
				btnReturn.setEnabled(true);
			}
			tvNumber.setText(f.format(number));

			break;

		case R.id.btn_return:
			// 如果点击点击返回为选中状态，显示llChannelBar,隐藏llChannelComplete
			back();
			break;
		}

	}

	private PopupWindow mWindow;
	private ListView listView;
	List<String> checkNum;
	String chnumber;
	// String back="back";
	// int reNum=0;
	private static final int REQUEST_CODE = 100;
	protected static final int UNABLE_GENERATE_REPORT = 1000;
	String foodName;
	String checkProject;
	TaskClassDao taskClassDao;
	ReportDataDao reportDao;
	ReportNumberDao numberDao;
	private TextView tvValueC_tittle;
	private TextView tvValueT_tittle;
	private String spaceNum;

	private void generateReport() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final AlertDialog alertDialog = builder.create();
		alertDialog.setCanceledOnTouchOutside(false);
		alertDialog.show();
		// 自定义UI
		alertDialog.getWindow().clearFlags(
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
						| WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		alertDialog.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		Window window = alertDialog.getWindow();
		window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		LayoutParams params = window.getAttributes();
		params.dimAmount = 0.7f;
		window.setAttributes(params);
		window.setContentView(R.layout.generate_report_window);
		Button btnConfirm = (Button) window.findViewById(R.id.btn_confirm);
		Button btnBack = (Button) window.findViewById(R.id.btn_back);
		final EditText etCheckNumber = (EditText) window
				.findViewById(R.id.et_check_number);
		Button btnDownArrow = (Button) window.findViewById(R.id.btn_down_arrow);
		checkNum = new ArrayList<>();
		taskclasses = new TaskClassBiz(DetectionChannelOneActvity.this)
				.getTaskClass();
		for (TaskClass task : taskclasses) {
			String chNum = task.getcheckNumber();
			checkNum.add(chNum);
			String planDetail = task.getPlandetail();
			String[] detailArray = planDetail.split("。");
			String detail = detailArray[0];
			String[] detailArr = detail.split("，");
			String sampleName = detailArr[0];
			String proName = detailArr[1];
			String[] ary = sampleName.split("：");
			String[] strary = proName.split("：");
			foodName = ary[1];
			checkProject = strary[1];
		}
		// datas = new ReportDataBiz(DetectionChannelOneActvity.this)
		// .getReportDataClass();
		// for (ReportData report : datas) {
		// // String chNum = report.getCheckNumber();
		// // checkNum.add(chNum);
		// String planDetail = report.getPlandetail();
		// String[] detailArray = planDetail.split("。");
		// String detail = detailArray[0];
		// String[] detailArr = detail.split("，");
		// String sampleName = detailArr[0];
		// String proName = detailArr[1];
		// String[] ary = sampleName.split("：");
		// String[] strary = proName.split("：");
		// foodName = ary[1];
		// checkProject = strary[1];
		// }
		chnumber = etCheckNumber.getText().toString();
		listView = new ListView(DetectionChannelOneActvity.this);
		listView.setBackgroundResource(R.drawable.listview_background);
		listView.setAdapter(new CheckAdapter());
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				chnumber = checkNum.get(position);
				etCheckNumber.setText(chnumber);
				if (mWindow != null && mWindow.isShowing()) {
					mWindow.dismiss();
					mWindow = null;
				}
			}
		});
		btnDownArrow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mWindow == null) {
					mWindow = new PopupWindow(DetectionChannelOneActvity.this);
					mWindow.setWidth(etCheckNumber.getWidth());
					mWindow.setHeight(78);
					mWindow.setContentView(listView);
					// 设置点击外边消掉PopupWindow
					// window.setOutsideTouchable(true);

					mWindow.setFocusable(true);
				}
				mWindow.showAsDropDown(etCheckNumber, 0, 0);

			}
		});
		btnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				alertDialog.dismiss();
			}
		});
		btnConfirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd",
						Locale.CHINA);
				long time = System.currentTimeMillis();
				String dateTime = sdf.format(time);
				reportDao = new ReportDataDao(DetectionChannelOneActvity.this);
				numberDao = new ReportNumberDao(DetectionChannelOneActvity.this);
				int id = 1;
				int reNum = numberDao.getReportNumber();
				reNum++;
				if (reNum == 1) {
					numberDao.add(reNum);
				} else {
					numberDao.update(id, reNum);
				}
				String reportNumber = "DY" + dateTime + String.valueOf(reNum);
				chnumber = etCheckNumber.getText().toString();
				if (chnumber != null) {
					if (foodName != null && foodName.equals(samplename)
							&& checkProject != null
							&& checkProject.equals(projectName)) {
						reportDao.update(chnumber, reportNumber, result);
						Intent intent = new Intent(
								DetectionChannelOneActvity.this,
								DetectReportItemActivity.class);
						intent.putExtra("back", "back");
						intent.putExtra("checkNumber", chnumber);
						startActivityForResult(intent, REQUEST_CODE);
					} else {
						handler.sendEmptyMessage(UNABLE_GENERATE_REPORT);
					}
				}
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
			// String path=data.getStringExtra("OK");
		} else if (requestCode == STANDARD_REQUEST_CODE
				&& resultCode == RESULT_OK) {
			sampleName = data.getStringExtra("sample");
			etInputSample.setText(sampleName);
			// etInput = etInputSample.getText().toString();
		}
	}

	public class CheckAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return checkNum.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view;
			ViewHolder holder;
			if (convertView != null) {
				view = convertView;
				holder = (ViewHolder) view.getTag();
			} else {
				view = View.inflate(DetectionChannelOneActvity.this,
						R.layout.popup_window_item, null);
				holder = new ViewHolder();
				holder.tvCheckNum = (TextView) view
						.findViewById(R.id.tv_check_num);
				view.setTag(holder);
			}
			String checknum = checkNum.get(position);
			holder.tvCheckNum.setText(checknum);
			return view;
		}

		class ViewHolder {
			TextView tvCheckNum;

		}
	}

	private void print() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final AlertDialog alertDialog = builder.create();
		alertDialog.show();
		alertDialog.setCanceledOnTouchOutside(false);
		// 自定义UI
		Window window = alertDialog.getWindow();
		View dialogView = View.inflate(this, R.layout.printing_window, null);
		window.setContentView(dialogView);
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				try {
					printTitle();
				} catch (IOException | ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				alertDialog.dismiss();

			}
		}, 2000);
	}

	private void upload() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final AlertDialog alertDialog = builder.create();
		alertDialog.show();
		alertDialog.setCanceledOnTouchOutside(false);
		Window window = alertDialog.getWindow();
		View view = View.inflate(this, R.layout.upload_window, null);
		window.setContentView(view);
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				alertDialog.dismiss();
				handler.sendEmptyMessage(MESSAGE_UPLOAD_COMPLETE_BUTTON);
			}
		}, 2000);
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

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
			/* 隐藏软键盘 */
			InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			if (inputMethodManager.isActive()) {
				inputMethodManager.hideSoftInputFromWindow(this
						.getCurrentFocus().getWindowToken(), 0);
			}

			return true;
		}
		return super.dispatchKeyEvent(event);

	}

	// 增加对返回键的监听，
	@Override
	public void onBackPressed() {
		if (isRunning) {
			System.out.println("测试返回键--------------");
			// super.onBackPressed();
		} else {
			System.out.println("测试返回键");
			// super.onBackPressed();
			back();
		}
	}

	private void back() {
		// TODO Auto-generated method stub
		if (!llChannelBar.isShown()) {
			try {
				llChannelBar.setVisibility(View.VISIBLE);
				llChannelComplete.setVisibility(View.GONE);
				btnGenerateReport.setVisibility(View.GONE);
				btnPrint.setVisibility(View.GONE);
				btnUpload.setVisibility(View.GONE);
				btnStartDetection.setVisibility(View.VISIBLE);
				gifViewLoad.setVisibility(View.INVISIBLE);
				btnComplete.setSelected(false);
				btnLine02.setSelected(true);
				btnLine03.setSelected(false);
				inDrawable = new GifDrawable(getResources(),
						R.drawable.put_the_card);

				gifImageView.setBackground(inDrawable);
				// gifImageView.setGifImage(R.drawable.put_the_card);
				// gifImageView.setVisibility(View.VISIBLE);
				// btnDetectionAnniu.setVisibility(View.GONE);
				if (device != null) {
					threadsenddata = ACTION_EXIT_CARD;
					number = number + 1;
					tvNum.setText(f.format(number));
					dao.update(number, projectName);
					btnStartDetection.setEnabled(false);
					handler.postDelayed(new Runnable() {

						@Override
						public void run() {
							handler.sendEmptyMessage(COUNT_STOP_TIME);

						}
					}, 3000);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// //如果再点击返回为没选中状态，实现跳转界面
		} else {
			if (device != null) {
				// && device.getVendorId() == myvid
				// && device.getProductId() == mypid) {
				threadsenddata = ACTION_ENTER_CARD;
			}
			number = number - 1;
			dao.update(number, projectName);
			Intent intent = new Intent(DetectionChannelOneActvity.this,
					SampleTestActivity.class);
			intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
		}
	}

}
