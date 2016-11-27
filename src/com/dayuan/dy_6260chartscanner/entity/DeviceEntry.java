package com.dayuan.dy_6260chartscanner.entity;

import java.io.Serializable;

import android.hardware.usb.UsbDevice;

import com.hoho.android.usbserial.driver.UsbSerialDriver;

public class DeviceEntry implements Serializable {
	public UsbDevice device;
	public UsbSerialDriver driver;

	public DeviceEntry(UsbDevice device, UsbSerialDriver driver) {
		this.device = device;
		this.driver = driver;
	}
}
