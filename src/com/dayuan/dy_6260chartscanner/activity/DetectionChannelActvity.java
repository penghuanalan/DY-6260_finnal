package com.dayuan.dy_6260chartscanner.activity;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

import com.dayuan.dy_6260chartscanner.CustomView;
import com.dayuan.dy_6260chartscanner.CustomView01;
import com.dayuan.dy_6260chartscanner.CustomView02;
import com.dayuan.dy_6260chartscanner.R;
import com.dayuan.dy_6260chartscanner.activity.DetectionChannelOneActvity.ConnectedThread;
import com.dayuan.dy_6260chartscanner.biz.CurveSmoothing;
import com.dayuan.dy_6260chartscanner.biz.UploadData;
import com.dayuan.dy_6260chartscanner.db.Dao;
import com.dayuan.dy_6260chartscanner.db.LogDao;
import com.dayuan.dy_6260chartscanner.util.ShowTime;
import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;
import com.hoho.android.usbserial.util.SerialInputOutputManager;

import android.R.string;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class DetectionChannelActvity extends BaseActivity implements
		OnClickListener {
	private TextView tvProName, tvCounter;
	private Button btnReturn, btnStartDetection, btnComplete;
	private Button btnGenerateReport;
	private Button btnLoading;
	private Button btnUpload;
	private Button btnPrint;
	// 通道1
	private EditText etInputSample, etMultiple;
	private TextView tvNumber, tvDensity, tvResult, tvInputSample;
	// 通道2
	private EditText etInputSampleTwo, etMultipleTwo;
	private TextView tvNumberTwo, tvDensityTwo, tvResultTwo, tvInputSampleTwo;

	private LinearLayout llChannelBar;
	// 插入卡的gif
	private GifImageView gifImageView;
	// 显示加载gif动画
	private GifImageView gifView;
	// 显示加载数据动态显示gif
	private GifImageView gifViewLoad;
	private GifImageView gifViewLoadTwo;
	GifDrawable drawable;
	GifDrawable gDrawable;
	GifDrawable inDrawable;
	CustomView01 customView;
	CustomView02 customViewTwo;

	private ImageView imageView;
	private Button btnLine02;
	private Button btnLine03;
	private boolean isRunning = true;
	private boolean isLoop;
	private String etInput;
	private String etInputTwo;

	private final String TAG = "OpenDevice";
	private static final int MESSAGE_UPDATE_BUTTON = 101;
	protected static final int MESSAGE_UPDATE_COMPLETE_BUTTON = 120;
	protected static final int MESSAGE_UPLOAD_COMPLETE_BUTTON = 160;
	public int threadsenddata = 0;
	public int mthreadsenddata = 100;
	public static final int ACTION_CLEAR = 11;
	public static final int ACTION_EXIT_CARD = 1;
	public static final int ACTION_EXIT_CARD_TWO = 111;
	public static final int ACTION_ENTER_SCAN_CARD = 2;
	public static final int ACTION_READ_DATA = 3;
	protected static final int STANDARD_REQUEST_CODE1 = 200;
	protected static final int STANDARD_REQUEST_CODE2 = 300;
	public static final int ACTION_ENTER_CARD = 4;
	public static final int ACTION_STATUS_CARD = 5;
	private static final int NO_CARD_STATUS = 6;
	protected static final int DATA_MISTAKE = 8;
	private static final int USELESS_CARD = 10;
	protected static final int COUNT_STOP_TIME = 12;

	private final static ExecutorService mExecutor = Executors
			.newSingleThreadExecutor();
	private SerialInputOutputManager mSerialIoManager;
	private UsbSerialPort rPort;
	private UsbManager mUsbManager;
	private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
	private static final int MESSAGE_UPDATE_COMPLETE_BUTTON_TWO = 200;
	private int myvid = 1155, mypid = 22336;
	private int myvid1 = 6790, mypid1 = 29987;
	UsbInterface[] usbinterface = null;
	UsbEndpoint[][] endpoint = new UsbEndpoint[5][5];
	UsbInterface[] musbinterface = null;
	UsbEndpoint[][] mendpoint = new UsbEndpoint[5][5];
	private UsbDevice device = null;
	private UsbDevice mdevice = null;
	UsbDeviceConnection connection = null;
	UsbDeviceConnection mconnection = null;
	ConnectedThread mconnectedthread = null;
	byte[] mybuffer = new byte[512];
	byte[] mbuffer = new byte[512];
	int datalength;
	int mdatalength;
	boolean threadcontrol = false;
	private UsbDevice[] mdevices = new UsbDevice[2];
	private DecimalFormat f = new DecimalFormat("0000");
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss",
			Locale.CHINA);
	String projectName;
	String result;
	String resultTwo;
	String stringNumber;
	String method;
	String samplename;
	String samplenameTwo;
	String sampleName;
	String dateTime;
	String multiple;
	String multipleTwo;
	String density;
	String densityTwo;
	private String proNumber;
	private String proNumberTwo;
	private int number;
	private LogDao logDao;
	private Dao dao;
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
	private int size;
	private String data;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MESSAGE_UPDATE_BUTTON:

				etInputSample.setVisibility(View.GONE);
				etInputSampleTwo.setVisibility(View.GONE);
				tvInputSample.setVisibility(View.VISIBLE);
				tvInputSampleTwo.setVisibility(View.VISIBLE);
				tvInputSample.setText(etInput);
				tvInputSampleTwo.setText(etInputTwo);
				gifImageView.setBackground(getResources().getDrawable(
						R.drawable.detection_anniu));
				gifView.setBackground(drawable);
				gifViewLoad.setVisibility(View.VISIBLE);
				gifViewLoadTwo.setVisibility(View.VISIBLE);
				gifViewLoad.setBackground(gDrawable);
				gifViewLoadTwo.setBackground(gDrawable);
				btnStartDetection.setVisibility(View.INVISIBLE);
				customView.setVisibility(View.GONE);
				customViewTwo.setVisibility(View.GONE);
				// etInputSample.setCursorVisible(false);
				btnLine02.setSelected(false);
				btnLine03.setSelected(true);
				long time = System.currentTimeMillis();
				dateTime = sdf.format(time);
				break;
			case MESSAGE_UPDATE_COMPLETE_BUTTON:
				isRunning = false;
				gifView.setBackground(getResources().getDrawable(
						R.drawable.loading));
				btnComplete.setSelected(true);
				gifViewLoad.setVisibility(View.GONE);
				gifViewLoadTwo.setVisibility(View.GONE);
				// btnGenerateReport.setVisibility(View.VISIBLE);
				btnPrint.setVisibility(View.VISIBLE);
				btnUpload.setVisibility(View.VISIBLE);
				btnStartDetection.setVisibility(View.GONE);
				customView.setVisibility(View.VISIBLE);
				customView.setData(getData(0));
				customView.invalidate();
				// value();
				if (size < 0) {
					if (key == 2) {
						
						
						for (int i = 0; i < _tValues.size(); i++) {
							int length = _tValues.get(i).length;

							/**
							 * 根据曲线状态判断阴性阳性
							 * 
							 */

							int count = 0;
							int index1 = 0;
							boolean line = false;
							for (int n =3; n < length - 1; n++) {
								if ((_tValues.get(i)[n + 1] - _tValues.get(i)[n]) < 0) {
									count++;
									if (count >5) {
										for (int j = n; j < length - 1; j++) {
											if (_tValues.get(i)[j + 1] - _tValues.get(i)[j] > 0
													&& _tValues.get(i)[j - 5]
															- _tValues.get(i)[j] > 120) {
												System.out.println("第一个波谷值"+_tValue[i]+"---波谷开始值"
														+_tValue[i-5]+"差值"+(_tValue[i-5]-_tValue[i])+"C坐标"+j);
												index1 = j;
												line = true;
												break;
											}
										}
										break;
									}

								} else {
									count = 0;
								}
							}

							/*int count2 = 0;
							int index2 = 0;
							boolean line2 = false;
							for (int n = index1; n < length - 1; n++) {
								if ((_tValues.get(i)[n + 1] - _tValues.get(i)[n]) < 0) {
									count2++;
									if (count2 > 5
											&& (_tValues.get(i)[n - 5] - _tValues.get(i)[n] > 150)) {
										for (int j = n; j < length - 1; j++) {
											if (_tValues.get(i)[j + 1] - _tValues.get(i)[j] > 0) {
												// System.out.println("第二个波谷值"+_tValues.get(i)[j
												// +
												// 1]+"---波谷开始值"+_tValue[i-5]+"差值"+(_tValue[i-5]-_tValue[i]));
												
												 * System.out.println("第er个波谷值"+_tValues.get(i)[j
												 * ]); System.out.println("波谷2值坐标" + j+
												 * "总长度"+length);
												 
												index2 = j;
												line2 = true;
												break;
											}
										}

										break;
									}

								} else {
									count2 = 0;
								}
							}*/
							/*
							 * System.out.println("波谷1值坐标" + index1 + "--波谷2值坐标" + index2 +
							 * "数据的总长度" + length + "peak和T差值" + (int) (peak - _tValue[index2]));
							 */
							
							if(line){
								if(index1>20){
									if (i == 0) {
										tvResult.setText("无效");

									} else {
										tvResultTwo.setText("无效");
									}
								}else{
									double max = _tValues.get(i)[index1+11];
									double value=0;
									for(int j=0;j<9;j++){
										value= (value>(max-_tValues.get(i)[index1+11+j])? value:max-_tValues.get(i)[index1+11+j]);
										}
									System.out.println("-----------最大差值"+value);
									if(value>300){
										if (i == 0) {
											tvResult.setText("阴性");

										} else {
											tvResultTwo.setText("阴性");
										}
									}else{
										if (i == 0) {
											tvResult.setText("阳性");

										} else {
											tvResultTwo.setText("阳性");
										}
									}
								}
							}else{
								if (i == 0) {
									tvResult.setText("无效");

								} else {
									tvResultTwo.setText("无效");
								}
							}
/*
							if (line) {
								// index1改成20原始值30
								if (index1 <= 20 && index1 >= 10 && line2 && index2 > 25
										&& index2 < length - 5) {
									if (i == 0) {
										tvResult.setText("阴性");

									} else {
										tvResultTwo.setText("阴性");
									}

								} else if ((index1 <= 20 && !line2)
										|| (index1 <= 20 && line2 && index2 <= 20)
										|| (index1 <= 20 && line2 && index2 > length - 5)) {
									if (i == 0) {
										tvResult.setText("阳性");

									} else {
										tvResultTwo.setText("阳性");
									}

								} else if (index1 > 20) {
									if (i == 0) {
										tvResult.setText("无效");
									} else {
										tvResultTwo.setText("无效");

									}

								}
							} else {
								if (i == 0) {
									tvResult.setText("无效");
								} else {
									tvResultTwo.setText("无效");

								}
							}*/
							/**
							 * 根据曲线状态判断阴性阳性
							 */
						}
						
						
						
					/*	if (cValues.get(0) > valC) {
							if (valT >= tValues.get(0)) {
								tvResult.setText("阳性");
							} else if (tValues.get(0) > valTL) {
								tvResult.setText("阴性");
							} else if (tValues.get(0) > valT
									&& tValues.get(0) < valTL) {
								tvResult.setText("可疑");
							}
						} else {
							tvResult.setText("无效");
						}
						if (cValues.get(1) > valC) {
							if (valT >= tValues.get(1)) {
								tvResultTwo.setText("阳性");
							} else if (tValues.get(1) > valTL) {
								tvResultTwo.setText("阴性");
							} else if (tValues.get(1) > valT
									&& tValues.get(1) < valTL) {
								tvResultTwo.setText("可疑");
							}
						} else {
							tvResultTwo.setText("无效");
						}*/

					} else if (key == 3) {
						if (cValues.get(0) > valC) {
							if (cValues.get(0) - tValues.get(0) >= valueOne) {
								tvResult.setText("阳性");
							} else if (tValues.get(0) - cValues.get(0) >= valueTwo) {
								tvResult.setText("阴性");
							} else if (Math
									.abs(cValues.get(0) - tValues.get(0)) < valueThree) {
								tvResult.setText(resultChoice);
							}
						} else {
							tvResult.setText("无效");
						}
						if (cValues.get(1) > valC) {
							if (cValues.get(1) - tValues.get(1) >= valueOne) {
								tvResultTwo.setText("阳性");
							} else if (tValues.get(1) - cValues.get(1) >= valueTwo) {
								tvResultTwo.setText("阴性");
							} else if (Math
									.abs(cValues.get(1) - tValues.get(1)) < valueThree) {
								tvResultTwo.setText(resultChoice);
							}
						}
					} else {
						tvResultTwo.setText("无效");
					}
				} else if (size > 0) {
					if (key == 2) {
						
						for (int i = 0; i < _tValues.size(); i++) {
							int length = _tValues.get(i).length;

							/**
							 * 根据曲线状态判断阴性阳性
							 * 
							 */

							int count = 0;
							int index1 = 0;
							boolean line = false;
							for (int n = 3; n < length - 1; n++) {
								if ((_tValues.get(i)[n + 1] - _tValues.get(i)[n]) < 0) {
									count++;
									if (count >= 5) {
										for (int j = n; j < length - 1; j++) {
											if (_tValues.get(i)[j + 1] - _tValues.get(i)[j] > 0
													&& _tValues.get(i)[j - 5]
															- _tValues.get(i)[j] > 120) {
												
												  System.out.println("第一个波谷值"+_tValues.get(i)[j]
												  ); System.out.println("波谷1值坐标" + j+
												  "总长度"+length);
												 
												index1 = j;
												line = true;
												break;
											}
										}
										break;
									}

								} else {
									count = 0;
								}
							}

							/*int count2 = 0;
							int index2 = 0;
							boolean line2 = false;
							for (int n = index1; n < length - 1; n++) {
								if ((_tValues.get(i)[n + 1] - _tValues.get(i)[n]) < 0) {
									count2++;
									if (count2 > 5
											&& (_tValues.get(i)[n - 5] - _tValues.get(i)[n] > 200)) {
										for (int j = n; j < length - 1; j++) {
											if (_tValues.get(i)[j + 1] - _tValues.get(i)[j] > 0) {
												 System.out.println("第二个波谷值"+_tValues.get(i)[j+1]+"---波谷开始值"+_tValue[i-5]+"差值"+(_tValue[i-5]-_tValue[i]));
												
												 * System.out.println("第er个波谷值"+_tValues.get(i)[j
												 * ]); System.out.println("波谷2值坐标" + j+
												 * "总长度"+length);
												 
												index2 = j;
												line2 = true;
												break;
											}
										}

										break;
									}

								} else {
									count2 = 0;
								}
							}*/
							/*
							 * System.out.println("波谷1值坐标" + index1 + "--波谷2值坐标" + index2 +
							 * "数据的总长度" + length + "peak和T差值" + (int) (peak - _tValue[index2]));
							 */
							if(line){
								if(index1>20){
									if (i ==1) {
										tvResult.setText("无效");

									} else {
										tvResultTwo.setText("无效");
									}
								}else{
									double max = _tValues.get(i)[index1+11];
									double value=0;
									for(int j=0;j<9;j++){
										value= (value>(max-_tValues.get(i)[index1+11+j])? value:max-_tValues.get(i)[index1+11+j]);
										}
									System.out.println("-----------最大差值"+value);
									if(value>300){
										if (i == 1) {
											tvResult.setText("阴性");

										} else {
											tvResultTwo.setText("阴性");
										}
									}else{
										if (i == 1) {
											tvResult.setText("阳性");

										} else {
											tvResultTwo.setText("阳性");
										}
									}
								}
							}else{
								if (i == 1) {
									tvResult.setText("无效");

								} else {
									tvResultTwo.setText("无效");
								}
							}
							/*if (line) {
								// index1改成20原始值30
								if (index1 <= 20 && index1 >= 10 && line2 && index2 > 25
										&& index2 < length - 5) {
									if (i == 1) {
										tvResult.setText("阴性");

									} else {
										tvResultTwo.setText("阴性");
									}

								} else if ((index1 <= 20 && !line2)
										|| (index1 <= 20 && line2 && index2 <= 20)
										|| (index1 <= 20 && line2 && index2 > length - 5)) {
									if (i == 1) {
										tvResult.setText("阳性");

									} else {
										tvResultTwo.setText("阳性");
									}

								} else if (index1 > 20) {
									if (i == 1) {
										tvResult.setText("无效");
									} else {
										tvResultTwo.setText("无效");

									}

								}
							} else {
								if (i == 1) {
									tvResult.setText("无效");
								} else {
									tvResultTwo.setText("无效");

								}
							}*/
							/**
							 * 根据曲线状态判断阴性阳性
							 */
						}
						
						
						
						/*
						 * if (cValues.get(1) > valC) { if (valT >=
						 * tValues.get(1)) { tvResult.setText("阳性"); } else if
						 * (tValues.get(1) > valTL) { tvResult.setText("阴性"); }
						 * else if (tValues.get(1) > valT && tValues.get(1) <
						 * valTL) { tvResult.setText("可疑"); } } else {
						 * tvResult.setText("无效"); } if (cValues.get(0) > valC)
						 * { if (valT >= tValues.get(0)) {
						 * tvResultTwo.setText("阳性"); } else if (tValues.get(0)
						 * > valTL) { tvResultTwo.setText("阴性"); } else if
						 * (tValues.get(0) > valT && tValues.get(0) < valTL) {
						 * tvResultTwo.setText("可疑"); } } else {
						 * tvResultTwo.setText("无效"); }
						 */
					} else if (key == 3) {
						if (cValues.get(1) > valC) {
							if (cValues.get(1) - tValues.get(1) >= valueOne) {
								tvResult.setText("阳性");
							} else if (tValues.get(1) - cValues.get(1) >= valueTwo) {
								tvResult.setText("阴性");
							} else if (Math
									.abs(cValues.get(1) - tValues.get(1)) < valueThree) {
								tvResult.setText(resultChoice);
							}
						} else {
							tvResult.setText("无效");
						}
						if (cValues.get(0) > valC) {
							if (cValues.get(0) - tValues.get(0) >= valueOne) {
								tvResultTwo.setText("阳性");
							} else if (tValues.get(0) - cValues.get(0) >= valueTwo) {
								tvResultTwo.setText("阴性");
							} else if (Math
									.abs(cValues.get(0) - tValues.get(0)) < valueThree) {
								tvResultTwo.setText(resultChoice);
							}
						} else {
							tvResultTwo.setText("无效");
						}
					}
				}

				result = tvResult.getText().toString();
				resultTwo = tvResultTwo.getText().toString();
				density = tvDensity.getText().toString();
				densityTwo = tvDensityTwo.getText().toString();
				samplename = etInputSample.getText().toString().trim();
				samplenameTwo = etInputSampleTwo.getText().toString().trim();

				proNumber = tvNumber.getText().toString().trim();
				proNumberTwo = tvNumberTwo.getText().toString().trim();
				// ph增加判断
				if (!reList.isEmpty() && reList != null && !reLists.isEmpty()
						&& reLists != null) {
					if (size < 0) {
						logDao.add(number, samplename, projectName, result,
								dateTime, density, datas[0]);
						logDao.add(number + 1, samplenameTwo, projectName,
								resultTwo, dateTime, densityTwo, datas[1]);
					} else if (size > 0) {
						logDao.add(number, samplename, projectName, result,
								dateTime, density, datas[1]);
						logDao.add(number + 1, samplenameTwo, projectName,
								resultTwo, dateTime, densityTwo, datas[0]);
					}
				}
				dao.update(projectName, number, samplename);
				dao.update(projectName, number + 1, samplenameTwo);
				btnReturn.setEnabled(true);
				break;
			// 新增判断双通道
			case NO_CARD_STATUS:
				Toast.makeText(DetectionChannelActvity.this, "当前为无卡状态，请插入卡", 0)
						.show();
				btnReturn.setEnabled(true);
				break;
			// 新增上传方法
			case MESSAGE_UPLOAD_COMPLETE_BUTTON:
				ConnectivityManager mConnectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
				// 检查网络连接，如果无网络可用，就不需要进行连网操作等
				NetworkInfo info = mConnectivity.getActiveNetworkInfo();
				if (info == null || !mConnectivity.getBackgroundDataSetting()) {
					Toast.makeText(DetectionChannelActvity.this, "请检查网络", 0)
							.show();
				} else {
					UploadData.uploadCheckData(DetectionChannelActvity.this,
							number + "", samplename, projectName, result,
							dateTime, density);
					UploadData.uploadCheckData(DetectionChannelActvity.this,
							number + 1 + "", samplenameTwo, projectName,
							resultTwo, dateTime, densityTwo);
				}
				break;
			case MESSAGE_UPDATE_COMPLETE_BUTTON_TWO:
				isRunning = false;

				customViewTwo.setVisibility(View.VISIBLE);
				customViewTwo.setData(getData(1));
				customViewTwo.invalidate();

				break;
			case COUNT_STOP_TIME:
				btnStartDetection.setEnabled(true);
				tvInputSample.setVisibility(View.GONE);
				tvInputSampleTwo.setVisibility(View.GONE);
				etInputSample.setVisibility(View.VISIBLE);
				etInputSampleTwo.setVisibility(View.VISIBLE);
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detection_channel);
		dao = new Dao(this);
		logDao = new LogDao(this);
		Intent intent = getIntent();
		sampleName = intent.getStringExtra("sampleName");
		projectName = intent.getStringExtra("name");
		// String channel = intent.getStringExtra("channel");
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

		initViews();

		if (key > 1) {
			etMultiple.setText("—");
			etMultipleTwo.setText("—");
			tvDensity.setText("—");
			tvDensityTwo.setText("—");
			etMultiple.setEnabled(false);
			etMultipleTwo.setEnabled(false);
		}
		if (openDevice()) {

			if (mconnectedthread != null) {
				mconnectedthread = null;
			}
			if (device != null)
			// &&device.getVendorId() == myvid &&
			// device.getProductId() == mypid)
			{
				mconnectedthread = new ConnectedThread();
				mconnectedthread.start();

				threadcontrol = true;
				threadsenddata = ACTION_EXIT_CARD;
				mthreadsenddata = ACTION_EXIT_CARD_TWO;
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
		tvProName = (TextView) findViewById(R.id.tv_sample_name);
		tvCounter = (TextView) findViewById(R.id.tv_counter);
		btnReturn = (Button) findViewById(R.id.btn_return);
		btnStartDetection = (Button) findViewById(R.id.btn_start_detection);
		gifImageView = (GifImageView) findViewById(R.id.gif_input_card);
		gifView = (GifImageView) findViewById(R.id.gif_loading);
		btnLine02 = (Button) findViewById(R.id.btn_line_02);
		btnLine03 = (Button) findViewById(R.id.btn_line_03);
		btnComplete = (Button) findViewById(R.id.btn_complete);
		btnGenerateReport = (Button) findViewById(R.id.btn_generate_report);
		btnUpload = (Button) findViewById(R.id.btn_upload);
		btnPrint = (Button) findViewById(R.id.btn_print);
		// 通道1
		etInputSample = (EditText) findViewById(R.id.et_input_sample);
		tvInputSample = (TextView) findViewById(R.id.tv_input_sample);
		etMultiple = (EditText) findViewById(R.id.et_input_multiple);
		tvNumber = (TextView) findViewById(R.id.tv_number);
		tvDensity = (TextView) findViewById(R.id.tv_density);
		gifViewLoad = (GifImageView) findViewById(R.id.gif_data_loading);
		customView = (CustomView01) findViewById(R.id.customView_one);
		tvResult = (TextView) findViewById(R.id.tv_result);
		// 通道2
		etInputSampleTwo = (EditText) findViewById(R.id.et_input_sample_two);
		etMultipleTwo = (EditText) findViewById(R.id.et_input_multiple_two);
		tvInputSampleTwo = (TextView) findViewById(R.id.tv_input_sample_two);
		tvNumberTwo = (TextView) findViewById(R.id.tv_number_two);
		tvDensityTwo = (TextView) findViewById(R.id.tv_density_two);
		gifViewLoadTwo = (GifImageView) findViewById(R.id.gif_data_loading_two);
		customViewTwo = (CustomView02) findViewById(R.id.customView_two);
		tvResultTwo = (TextView) findViewById(R.id.tv_result_two);

		// ph隐藏增加的textView
		// tvInputSampleTwo.setVisibility(View.GONE);

		etInputSample.setFocusable(true);
		etInputSampleTwo.setFocusable(true);
		etInputSample.setText(sampleName);
		etInputSampleTwo.setText(sampleName);
		etInput = etInputSample.getText().toString().trim();
		etInputTwo = etInputSampleTwo.getText().toString().trim();

		btnStartDetection.setOnClickListener(this);
		btnReturn.setOnClickListener(this);
		// ph取消生成报告
		// btnGenerateReport.setOnClickListener(this);
		btnUpload.setOnClickListener(this);
		btnPrint.setOnClickListener(this);
		if (etInput.isEmpty() || etInputTwo.isEmpty()) {
			btnReturn.setEnabled(true);
		}
		try {
			btnLine02.setSelected(true);
			GifDrawable gifDrawable = new GifDrawable(getResources(),
					R.drawable.put_the_card);
			gifImageView.setBackground(gifDrawable);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tvNumber.setText(f.format(number));
		tvNumberTwo.setText(f.format(number + 1));
		tvProName.setText(projectName);

		etInputSample.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				Intent intent = new Intent(DetectionChannelActvity.this,
						CheckStandardActivity.class);
				intent.putExtra("get", "get");
				intent.putExtra("name", projectName);
				startActivityForResult(intent, STANDARD_REQUEST_CODE1);

				return true;
			}
		});
		etInputSampleTwo.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				Intent intent = new Intent(DetectionChannelActvity.this,
						CheckStandardActivity.class);
				intent.putExtra("get", "get");
				intent.putExtra("name", projectName);
				startActivityForResult(intent, STANDARD_REQUEST_CODE2);
				return true;
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_start_detection:

			isRunning = false;
			etInput = etInputSample.getText().toString().trim();
			etInputTwo = etInputSampleTwo.getText().toString().trim();
			if (!etInput.isEmpty() && !etInputTwo.isEmpty()) {
				threadsenddata = ACTION_STATUS_CARD;
				mthreadsenddata = ACTION_STATUS_CARD;
				btnReturn.setEnabled(false);
			} else if (etInput.isEmpty() || etInputTwo.isEmpty()) {
				Toast.makeText(DetectionChannelActvity.this, "请输入样品名称",
						Toast.LENGTH_LONG).show();
				btnReturn.setEnabled(true);
			}

			break;
		case R.id.btn_print:
			print();

			break;
		case R.id.btn_upload:
			upload();
			break;
		case R.id.btn_return:
			back();
			break;
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

	private void back() {
		// 如果点击点击返回为选中状态，显示llChannelBar,隐藏llChannelComplete
		if (!btnStartDetection.isShown()) {
			try {
				btnGenerateReport.setVisibility(View.GONE);
				btnPrint.setVisibility(View.GONE);
				btnUpload.setVisibility(View.GONE);
				btnStartDetection.setVisibility(View.VISIBLE);

				// ph修改,隐藏结果页面，编号加1
				customView.setVisibility(View.GONE);
				customViewTwo.setVisibility(View.GONE);
				gifViewLoad.setVisibility(View.GONE);
				tvResult.setText("");
				tvResultTwo.setText("");
				// gifViewLoad.setVisibility(View.INVISIBLE)
				btnComplete.setSelected(false);
				btnLine02.setSelected(true);
				btnLine03.setSelected(false);
				inDrawable = new GifDrawable(getResources(),
						R.drawable.put_the_card);

				gifImageView.setBackground(inDrawable);
				if (device != null) {
					threadsenddata = ACTION_EXIT_CARD;
					mthreadsenddata = ACTION_EXIT_CARD_TWO;
					// ph这里被注释掉？？？
					number = number + 2;
					tvNumber.setText(f.format(number));
					tvNumberTwo.setText(f.format(number + 1));
					if (dao == null) {

					}

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
				mthreadsenddata = ACTION_ENTER_CARD;
			}
			number = number - 1;
			dao.update(number, projectName);
			Intent intent = new Intent(DetectionChannelActvity.this,
					SampleTestActivity.class);
			intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
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
			int num = 0;
			int numtwo = 0;
			int i = 0;

			while (deviceIterator.hasNext()) {
				device = deviceIterator.next();
				Log.i(TAG,
						"vid: " + device.getVendorId() + "\t pid: "
								+ device.getProductId());
				if (device != null && device.getVendorId() == myvid
						&& device.getProductId() == mypid) {
					mdevices[i] = device;
					i++;
					System.out.println("获取到的usb设备" + device.getDeviceName()
							+ "/n");
				}
			}
			String deviceId = mdevices[0].getDeviceName().substring(18);
			String deviceIdtwo = mdevices[1].getDeviceName().substring(18);
			num = Integer.parseInt(deviceId);
			numtwo = Integer.parseInt(deviceIdtwo);
			size = num - numtwo;
		}
		if (device != null) {
			PendingIntent mPermissionIntent = PendingIntent.getBroadcast(this,
					0, new Intent(ACTION_USB_PERMISSION), 0);
			IntentFilter permissionFilter = new IntentFilter(
					ACTION_USB_PERMISSION);
			registerReceiver(musbReceiver, permissionFilter);
			// for(UsbDevice device:mdevices){}
			if (mUsbManager.hasPermission(mdevices[0])
					&& mUsbManager.hasPermission(mdevices[1])) {
				connection = mUsbManager.openDevice(mdevices[0]);
				mconnection = mUsbManager.openDevice(mdevices[1]);

			} else {

				mUsbManager.requestPermission(mdevices[0], mPermissionIntent);
				mUsbManager.requestPermission(mdevices[1], mPermissionIntent);
			}
			if (mUsbManager.hasPermission(mdevices[0])
					&& mUsbManager.hasPermission(mdevices[1])) {
				Log.i(TAG, "拥有访问权限");
			} else {
				Log.d(TAG, "未获得访问权限");
				return false;
			}
			Log.i(TAG, mdevices[0].getDeviceName());
			Log.i(TAG, mdevices[1].getDeviceName());
			Log.i(TAG, "" + mdevices[0].getInterfaceCount());
			Log.i(TAG, "" + mdevices[1].getInterfaceCount());
			usbinterface = new UsbInterface[mdevices[0].getInterfaceCount()];
			musbinterface = new UsbInterface[mdevices[1].getInterfaceCount()];
			for (int i = 0; i < mdevices[0].getInterfaceCount(); i++) {
				usbinterface[i] = mdevices[0].getInterface(i);
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
			for (int i = 0; i < mdevices[1].getInterfaceCount(); i++) {
				musbinterface[i] = mdevices[1].getInterface(i);
				Log.i(TAG,
						"接口" + i + "的端点数为："
								+ musbinterface[i].getEndpointCount());
				for (int j = 0; j < musbinterface[i].getEndpointCount(); j++) {
					mendpoint[i][j] = musbinterface[i].getEndpoint(j);
					if (mendpoint[i][j].getDirection() == 0) {
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
			if (mconnection != null) {
				mconnection.close();
			}
			if (mconnection == null) {
				return;
			}
			if (device != null && mUsbManager.hasPermission(mdevices[0])
					&& mUsbManager.hasPermission(mdevices[1])) {
				connection = mUsbManager.openDevice(mdevices[0]);
				mconnection = mUsbManager.openDevice(mdevices[1]);
				connection.claimInterface(usbinterface[1], true);
				mconnection.claimInterface(musbinterface[1], true);
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
				if (mthreadsenddata != ACTION_CLEAR) {
					switch (mthreadsenddata) {
					case ACTION_EXIT_CARD_TWO:
						cardsta = 2;
						mconnection.bulkTransfer(mendpoint[1][0],
								CMD_EXIT_CARD, CMD_EXIT_CARD.length, 30);
						break;
					case ACTION_STATUS_CARD:

						mconnection.bulkTransfer(mendpoint[1][0],
								CMD_STATUS_CARD, CMD_STATUS_CARD.length, 30);
						break;
					case ACTION_ENTER_SCAN_CARD:
						cardsta = 1;
						mconnection.bulkTransfer(mendpoint[1][0],
								CMD_ENTER_SCAN_CARD,
								CMD_ENTER_SCAN_CARD.length, 30);
						break;
					case ACTION_READ_DATA:
						mconnection.bulkTransfer(mendpoint[1][0],
								CMD_READ_DATA, CMD_READ_DATA.length, 30);
						Log.i(TAG, "read data");
						break;

					case ACTION_ENTER_CARD:
						cardsta = 3;
						mconnection.bulkTransfer(mendpoint[1][0],
								CMD_ENTER_CARD, CMD_ENTER_CARD.length, 30);
						break;

					default: {
					}
					}
					mthreadsenddata = ACTION_CLEAR;
				}

				readData();

				Log.i(TAG, "thread id: " + Thread.currentThread().getId() + "");
				try {

					Thread.sleep(100);
					if (threadsenddata == ACTION_CLEAR)
						threadsenddata = ACTION_READ_DATA;
					if (mthreadsenddata == ACTION_CLEAR)
						mthreadsenddata = ACTION_READ_DATA;
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
	}

	Map<Integer, double[]> dataValue = new HashMap<Integer, double[]>();
	List<Integer> list = new ArrayList<Integer>();
	List<Integer> mlist = new ArrayList<Integer>();

	public void readData() {

		datalength = connection.bulkTransfer(endpoint[1][1], mybuffer, 512,
				2000);
		mdatalength = mconnection.bulkTransfer(mendpoint[1][1], mbuffer, 512,
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
					if (mybuffer[4] == 0x01 && mbuffer[4] == 0x01) {// f

						threadsenddata = ACTION_ENTER_SCAN_CARD;
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
								isRunning = false;
								handler.sendEmptyMessage(MESSAGE_UPDATE_COMPLETE_BUTTON);

							}
						}, 8000);
						Log.i(TAG, "有卡");
					}

					else if (mybuffer[4] == 0x02 || mbuffer[4] == 0x02) {// 无卡

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
				if (len > 0) {
					for (int i = 0; i < len; i = i + 2) {

						int data01 = (mybuffer[5 + i] & 0x0FF) * 256
								+ (mybuffer[4 + i] & 0x0FF);
						list.add(data01);
					}
					Log.i("TAG01", "" + list);
					int length = list.size();
					double[] inData = new double[length];
					for (int i = 0; i < list.size(); i++) {
						inData[i] = list.get(i);
					}
					dataValue.put(0, inData);
					// list.clear();

				}

				break;
			}
			default: {

			}
			}
		}
		if (mdatalength > 0) {

			if (mdatalength < 4) {
				Log.e(TAG, "len error!");
				return;
			}

			int flag = mbuffer[0];
			int cmd = mbuffer[1];
			int len = (mbuffer[2] << 8) | (mbuffer[3] & 0x0FF);
			int endflag = mbuffer[mdatalength - 1];

			if ((flag != 0x7E) || (endflag != 0x7E)) {
				Log.e(TAG, "data error!");
				return;
			}

			Log.i(TAG, "mdatalength: " + mdatalength + " cmd: " + cmd
					+ " len: " + len);

			byte[] buffer = new byte[mdatalength];
			for (int i = 0; i < mdatalength; i++) {
				buffer[i] = mbuffer[i];
			}
			Log.i(TAG, Arrays.toString(buffer) + "");
			switch (cmd) {
			case 0x14:
				if (len == 1) {
					if (mbuffer[4] == 0x01 && mybuffer[4] == 0x01) {// 有卡

						mthreadsenddata = ACTION_ENTER_SCAN_CARD;
						handler.postDelayed(new Runnable() {
							@Override
							public void run() {
								isRunning = false;

								handler.sendEmptyMessage(MESSAGE_UPDATE_COMPLETE_BUTTON_TWO);

							}
						}, 8000);
					}

					else if (mbuffer[4] == 0x02 || mybuffer[4] == 0x02) {// 无卡

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
					mlist.clear();
				} else {
					Log.i(TAG, "出卡");
				}

				break;
			}

			case 0x16: {
				if (len > 0)

				{
					for (int i = 0; i < len; i = i + 2) {

						int data01 = (mbuffer[5 + i] & 0x0FF) * 256
								+ (mbuffer[4 + i] & 0x0FF);
						mlist.add(data01);
					}

					Log.i("TAG02", "" + mlist);
					int length = mlist.size();
					double[] inData = new double[length];
					for (int i = 0; i < mlist.size(); i++) {
						inData[i] = mlist.get(i);
					}
					dataValue.put(1, inData);
				}

			}
			default: {

			}
			}
		}
	}

	double[] _tValue;
	List<double[]> _tValues = new ArrayList<double[]>();

	List<Integer> reList;
	List<Integer> reLists;
	String[] datas = new String[2];

	public List<Integer> getData(int index) {
		reList = new ArrayList<Integer>();
		reLists = new ArrayList<Integer>();
		_tValues.clear();
		// ph添加的
		int m = 0;
		// 添加的
		try {
			Set set = dataValue.entrySet();
			Iterator iter = set.iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				Integer key = (Integer) entry.getKey();
				double[] value = (double[]) entry.getValue();
				if (value != null) {
					double[] inBuffer = new double[value.length];
					for (int i = 0; i < value.length; i++) {
						inBuffer[i] = value[i];
					}
					// 截取有效值,获取两个坐标
					Integer[] idx = CurveSmoothing.FindUsefulData(
					// ph修改了value为inBuffer
							DetectionChannelActvity.this, value);
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

					} else {
						_tValue = new double[64];

						for (int i = 0; i < _tValue.length; i++)

						{

							_tValue[i] = 10000;

						}
					}

					_tValues.add(_tValue);
					if (_tValue != null) {
						Integer[] tValue = new Integer[_tValue.length];
						for (int i = 0; i < _tValue.length; i++) {
							tValue[i] = (int) _tValue[i];
						}
						reList = Arrays.asList(tValue);
						Object[] intData = reList.toArray();
						//
						// data = Arrays.toString(intData);
						datas[m] = Arrays.toString(intData);
						m++;
					}
				} else {
					Toast.makeText(this, "此卡为无效卡", 0).show();
					_tValues.add(null);

				}
				value(index);
			}
			Integer[] tValue = new Integer[_tValues.get(0).length];
			Integer[] tvalue = new Integer[_tValues.get(1).length];
			for (int i = 0; i < _tValues.get(0).length; i++) {
				tValue[i] = (int) _tValues.get(0)[i];
			}
			reList = Arrays.asList(tValue);
			for (int i = 0; i < _tValues.get(1).length; i++) {
				tvalue[i] = (int) _tValues.get(1)[i];
			}
			reLists = Arrays.asList(tvalue);
			// index小于0为第一通道
			if (index == 0) {
				// 如果size小于0，说明reList为第一个通的数据
				if (size < 0) {
					return reList;
					// 如果size大于0，说明reLists为第一个通的数据
				} else if (size > 0) {
					return reLists;
				}
				// index大于0为第2通道
			}
			if (index == 1) {
				// 如果size小于0，说明reLists为第2个通的数据
				if (size < 0) {
					return reLists;

					// 如果size大于0，说明reList为第2个通的数据
				} else if (size > 0) {
					return reList;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}

	List<Integer> cValues;
	List<Integer> tValues;

	private void value(int index0) {
		cValues = new ArrayList<Integer>();
		tValues = new ArrayList<Integer>();
		for (int i = 0; i < _tValues.size(); i++) {
			int length = _tValues.get(i).length;
			int CIndex = 0, TIndex = 0;
			int valueC = 0;
			int valueT = 0;
			int peak = 0;
			// C值波谷坐标 10-30
			for (int j = 0; j < length; ++j) {
				if (j == 0)
					j = 10;
				if (j > 20)
					break;
				if (valueC == 0) {
					valueC = (int) _tValues.get(i)[j];
					CIndex = j;
				}
				if (valueC > _tValues.get(i)[j]) {
					valueC = (int) _tValues.get(i)[j];
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
					valueT = (int) _tValues.get(i)[j];
					TIndex = j;
				}
				if (valueT > _tValues.get(i)[j]) {
					valueT = (int) _tValues.get(i)[j];
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
					peak = (int) _tValues.get(i)[j];
					IDX = j;
				}

				if (peak < _tValues.get(i)[j]) {
					peak = (int) _tValues.get(i)[j];
					IDX = j;
				}
			}
			int index = 5;
			if (_tValues.get(i).length - TIndex <= index)
				TIndex = _tValues.get(i).length - index - 1;
			// 重新计算C值和T值
			// C=peak-valuec;T=peak-valuet
			ValueC = (int) (peak - _tValues.get(i)[CIndex]);
			ValueT = (int) (peak - _tValues.get(i)[TIndex]);
			cValues.add(ValueC);
			tValues.add(ValueT);
		}
		System.out.println("c值：" + cValues);
		System.out.println("t值：" + tValues);
	}

	private void OpenDevices(int baudRate, int dataBits, int stopBits,
			int parity) throws IOException {
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
		int len2 = etInputTwo.length();
		String deviceCmd = null;
		String deviceCmdTwo = null;
		String sampleName = etInputSample.getText().toString();
		String sampleNameTwo = etInputSampleTwo.getText().toString();
		String line = "\r\n";
		String testPerson = "检验者：" + "_ _ _ _ _ _ _ _" + line + line;
		String confirmPerson = "审核者：" + "_ _ _ _ _ _ _ _" + line;
		String time = "检测时间:  " + dateTime;
		String samplename = "样品:  " + sampleName;
		String samplenameTwo = "样品:  " + sampleNameTwo;
		String project = "编号  " + "样品名称      " + "浓度  " + "结果";
		String space = " ";
		if (len >= 6) {
			etInput = etInput.substring(0, 6);
			if (len2 >= 6) {
				spaceNum = etInputTwo.substring(0, 6);
			} else {
				int newlen = 6 - len2;
				switch (newlen) {
				case 1:
					spaceNum = etInputTwo + space + space + space;
					break;
				case 2:
					spaceNum = etInputTwo + space + space + space + space
							+ space;
					break;
				case 3:
					spaceNum = etInputTwo + space + space + space + space
							+ space + space + space;
					break;
				case 4:
					spaceNum = etInputTwo + space + space + space + space
							+ space + space + space + space + space;
					break;
				case 5:
					spaceNum = etInputTwo + space + space + space + space
							+ space + space + space + space + space + space
							+ space;
					break;
				default:
					spaceNum = etInputTwo + space + space + space + space
							+ space;
				}
			}

			deviceCmd = "------------------------------" + line
					+ f.format(number + 1) + space + space + spaceNum + space
					+ space + space + densityTwo + space + space + space
					+ resultTwo + line + f.format(number) + space + space
					+ etInput + space + space + space + density + space + space
					+ space + result + line + "------------------------------"
					+ line + project + line + line + time + line + "设备：胶体金读卡仪"
					+ line + line;
		} else {
			int newlen = 6 - len;
			switch (newlen) {
			case 1:
				spaceNum = etInput + space + space;
				break;
			case 2:
				spaceNum = etInput + space + space + space + space;
				break;
			case 3:
				spaceNum = etInput + space + space + space + space + space
						+ space;
				break;
			case 4:
				spaceNum = etInput + space + space + space + space + space
						+ space + space + space;
				break;
			case 5:
				spaceNum = etInput + space + space + space + space + space
						+ space + space + space + space + space;
				break;
			default:
				spaceNum = etInput + space + space + space + space + space;
			}
			if (len2 >= 6) {
				spaceNum2 = etInputTwo.substring(0, 6);
			} else {
				int newlen2 = 6 - len2;
				switch (newlen2) {
				case 1:
					spaceNum2 = etInputTwo + space + space;
					break;
				case 2:
					spaceNum2 = etInputTwo + space + space + space + space;
					break;
				case 3:
					spaceNum2 = etInputTwo + space + space + space + space
							+ space + space;
					break;
				case 4:
					spaceNum2 = etInputTwo + space + space + space + space
							+ space + space + space + space;
					break;
				case 5:
					spaceNum2 = etInputTwo + space + space + space + space
							+ space + space + space + space + space + space;
					break;
				default:
					spaceNum2 = etInputTwo + space + space + space + space
							+ space;
				}
			}

			deviceCmd = "------------------------------" + line
					+ f.format(number + 1) + space + space + spaceNum2 + space
					+ space + space + densityTwo + space + space + space
					+ resultTwo + line + f.format(number) + space + space
					+ spaceNum + space + space + space + density + space
					+ space + space + result + line
					+ "------------------------------" + line + project + line
					+ line + time + line + "设备：胶体金读卡仪" + line + line;

		}
		sendPrintingData(CMD_RESET);
		sendPrintingData(CMD_LINEDISTANCE);
		sendPrintingData(confirmPerson.getBytes("gbk"));
		sendPrintingData(CMD_RESET);
		sendPrintingData(CMD_LINEDISTANCE);
		sendPrintingData(testPerson.getBytes("gbk"));

		sendPrintingData(CMD_RESET);
		sendPrintingData(CMD_LINEDISTANCE);
		byte[] buffer = deviceCmd.getBytes("gbk");
		sendPrintingData(buffer);
		sendPrintingData(CMD_RESET);
		sendPrintingData(CMD_LINEDISTANCE);
		sendPrintingData(CMD_SETDOUBLESIZE);
		sendPrintingData(CMD_CENTER);
		sendPrintingData(CMD_SETGBK);
		String headCmd = projectName + line + "检测报告" + line + line;

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
	private String spaceNum;
	private String spaceNum2;

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
	}

	@Override
	protected void onResume() {
		IntentFilter usbFilter = new IntentFilter();
		usbFilter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
		usbFilter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
		registerReceiver(musbReceiver, usbFilter);
		isLoop = true;
		ShowTime.ShowTime(this, isLoop, tvCounter);
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
	protected void onDestroy() {
		if (connection != null) {
			connection.close();
		}
		if (mconnection != null) {
			mconnection.close();
		}
		isRunning = false;
		isLoop = false;
		threadcontrol = false;
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode == STANDARD_REQUEST_CODE1 && resultCode == RESULT_OK) {
			sampleName = data.getStringExtra("sample");
			etInputSample.setText(sampleName);
			// etInput = etInputSample.getText().toString();
		} else if (requestCode == STANDARD_REQUEST_CODE2
				&& resultCode == RESULT_OK) {
			samplenameTwo = data.getStringExtra("sample");
			etInputSampleTwo.setText(samplenameTwo);
		}
	}

	/*
	 * @Override public boolean onKeyDown(int keyCode, KeyEvent event) { // TODO
	 * Auto-generated method stub if(!isRunning){
	 * if(keyCode==KeyEvent.KEYCODE_BACK){ back(); return true; }else{ //return
	 * super.onKeyDown(keyCode, event); return true; } }else{ return
	 * super.onKeyDown(keyCode, event); }
	 * 
	 * }
	 */

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
}
