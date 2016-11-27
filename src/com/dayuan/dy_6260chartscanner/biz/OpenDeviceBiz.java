package com.dayuan.dy_6260chartscanner.biz;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.dayuan.dy_6260chartscanner.entity.DeviceEntry;
import com.dayuan.dy_6260chartscanner.util.CmdUtil;
import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;
import com.hoho.android.usbserial.util.HexDump;
import com.hoho.android.usbserial.util.SerialInputOutputManager;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.Handler;
import android.util.Log;

public class OpenDeviceBiz {
	private final String TAG = "OpenDevice";;
	private Context context;
	private UsbManager mUsbManager;
	private String mDeviceName;
	UsbSerialPort rPort;
	private SerialInputOutputManager mSerialIoManager;
	private final static ExecutorService mExecutor = Executors.newSingleThreadExecutor();
	private static final String ACTION_USB_PERMISSION = "com.android.hardware.USB_PERMISSION";
	public OpenDeviceBiz(Context context) {
		super();
		this.context = context;
	}

	public void setUsbSerialPort(UsbSerialPort rPort) {
		rPort = rPort;
	}

	public void OpenDevices(int baudRate, int dataBits, int stopBits, int parity) throws IOException {
		List<DeviceEntry> result = new ArrayList<DeviceEntry>();
		mUsbManager = (UsbManager) (context)
				.getSystemService(Context.USB_SERVICE);
		for (final UsbDevice device : mUsbManager.getDeviceList().values()) {
			final UsbSerialDriver driver = UsbSerialProber.getDefaultProber()
					.probeDevice(device);
			if (null == driver) {
				Log.d(TAG, "  - No UsbSerialDriver available.");
				result.add(new DeviceEntry(device, null));
			} else {
				Log.d(TAG, "Found usb device: " + device);
				Log.d(TAG, "  + " + driver);
				result.add(new DeviceEntry(device, driver));
			}
			mDeviceName = device.getDeviceName();
			Log.d(TAG, "  + " + mDeviceName);
		}

		HashMap<String, UsbDevice> devices = mUsbManager.getDeviceList();
		UsbDevice device = devices.get(mDeviceName);
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
		//
		rPort = driver.getPorts().get(0);

		PendingIntent mPermissionIntent = PendingIntent.getBroadcast(context,
				0, new Intent(ACTION_USB_PERMISSION),
				PendingIntent.FLAG_ONE_SHOT);
		mUsbManager.requestPermission(device, mPermissionIntent);
		// UsbDeviceConnection connection = usbManager.openDevice(device);
		UsbDeviceConnection connection = mUsbManager.openDevice(driver
				.getDevice());
		if (connection == null) {
			Log.i(TAG, "Opening device failed");
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
		
		
		
		byte[] v= sendCheckConnectCmd();
		sendCmd(sendCheckConnectCmd());
		
		Log.i(TAG,""+ sendCheckConnectCmd());
//		rPort.close();
	}
	public void onDeviceStateChange() {
		startIoManager();
		stopIoManager();
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

	public void sendCmd(byte[] data) {
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
	
	public static byte[] sendCheckConnectCmd() {
		String cmd = null;
		String headCmd = "03FF2000000000000800";
		cmd = headCmd + CmdUtil.getChecksum(headCmd);
		String dataCmd = "4445564943453F3F";
		cmd = cmd + CmdUtil.getChecksum(dataCmd) + dataCmd;
		return HexDump.hexStringToByteArray(cmd);
	}

	private SerialInputOutputManager.Listener mListener = new SerialInputOutputManager.Listener() {

		@Override
		public void onRunError(Exception e) {
			e.printStackTrace();
			Log.d(TAG, "Runner stopped.");
		}

		String mData = "";

		public void onNewData(final byte[] data) {
			mData += HexDump.toHexString(data);
			Log.d(TAG, "#1 " + mData);
		}
	};
};
