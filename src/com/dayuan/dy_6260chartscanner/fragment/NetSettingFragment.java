package com.dayuan.dy_6260chartscanner.fragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.conn.util.InetAddressUtils;


import com.dayuan.dy_6260chartscanner.R;

import android.app.Fragment;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class NetSettingFragment extends Fragment implements OnClickListener{
	View view;
	private Button btnActive,btnStatic;
	private EditText etIp,etsubnetMask,etdefaultGateway,etDNS;
	//EthernetDevInfo mEthInfo = (mEthManager).getSavedEthConfig();
	String ipaddress = "";
	int prefix=0;
	int[] ipSplit=new int[4];
	String subnetMask=null;
	String defaultGateway=null;
	String dns ;
	String getwayIpS;//网关地址  
	String netmaskIpS;
	//EthManager mEthManager;
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
	    view=inflater.inflate(R.layout.fragment_cable_setting, container,false);
		
		getLocalHostIp();
	//	getSubnetMask();
//		try {
//			getSubnetMaskandDefalutGateway();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		init();
		selectItem(btnActive);
		return view;
	}

	private String getLocalHostIp() {
		try
        {
            Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces();
         //    遍历所用的网络接口
            while (en.hasMoreElements())
            {
                NetworkInterface nif = en.nextElement();// 得到每一个网络接口绑定的所有ip
                Enumeration<InetAddress> inet = nif.getInetAddresses();
                // 遍历每一个接口绑定的所有ip
                while (inet.hasMoreElements())
                {
                    InetAddress ip = inet.nextElement();
                    if (!ip.isLoopbackAddress()
                            && InetAddressUtils.isIPv4Address(ip
                                    .getHostAddress()))
                    {
                        return ipaddress = ip.getHostAddress();
                    }
                }

            }
        }
        catch (SocketException e)
        {
            Log.e("feige", "获取本地ip地址失败");
            e.printStackTrace();
        }
		return ipaddress;
	}

//	private void getSubnetMask() {
			// TODO Auto-generated catch block
//		InetAddress localMachine=null;
//		try {
//			localMachine=InetAddress.getLocalHost();
//			} catch (UnknownHostException e) {
//			e.printStackTrace();
//			}
//		NetworkInterface netCard=null;
//		try {
//			netCard=NetworkInterface.getByInetAddress(localMachine);
//			} catch (SocketException e) {
//			e.printStackTrace();
//			}
//		List<InterfaceAddress> localInterface=null;
//		localInterface=netCard.getInterfaceAddresses();
//		Iterator<InterfaceAddress> iterator=null;
//		iterator=localInterface.iterator();
//		while(iterator.hasNext()){
//		InterfaceAddress temp=null;
//		temp=iterator.next();
//		prefix=temp.getNetworkPrefixLength();
//		}
//		int index=0;
//		int split=0;
//		int remainder=0;
//		split=prefix/8;
//		remainder=prefix%8;
//		while(index<split){
//		ipSplit[index]=255;
//		index++;
//		}
//		if(remainder==1)
//			ipSplit[index]=128;
//			if(remainder==2)
//			ipSplit[index]=192;
//			if(remainder==3)
//			ipSplit[index]=224;
//			if(remainder==4)
//			ipSplit[index]=240;
//			if(remainder==5)
//			ipSplit[index]=248;
//			if(remainder==6)
//			ipSplit[index]=252;
//			if(remainder==7)
//			ipSplit[index]=254;
//			index++;
//			while(index<remainder){
//			ipSplit[index]=0;
//			index++;
//			}
//			subnetMask=String.valueOf(ipSplit[0])+"."+String.valueOf(ipSplit[1])+"."+
//			String.valueOf(ipSplit[2])+"."+String.valueOf(ipSplit[3]);
//		
//	}
//	private void getSubnetMaskandDefalutGateway() throws IOException {
////		 Process pro = Runtime.getRuntime().exec("ipconfig");
////		    BufferedReader br = new BufferedReader(new InputStreamReader(pro.getInputStream()));
////		    List<String> rowList = new ArrayList();
////		    String temp;
////		    while((temp = br.readLine()) != null){
////		        rowList.add(temp );
////		    }
////		    for (String string : rowList) {
////		        if(string.indexOf("Subnet Mask") != -1){
////		            Matcher mc = Pattern.compile("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}").matcher(string);
////		            if(mc.find()){
////		            	subnetMask= mc.group();
////		            }else{
////		               Log.i("subnetMask", "子掩码为空");
////		            }
////		        };
////		        if(string.indexOf("Default Gateway") != -1){
////		            Matcher mc = Pattern.compile("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}").matcher(string);
////		            if(mc.find()){
////		                defaultGateway=mc.group();
////		            }else{
////		            	 Log.i("defaultGateway", "默认网关为空");
////		            }
////		            return;
////		        };
////		    }
//		long getewayIpL=di.gateway;  
//		getwayIpS=long2ip(getewayIpL);//网关地址  
//		long netmaskIpL=di.netmask;  
//		netmaskIpS=long2ip(netmaskIpL);//子网掩码地址  
//	}

//	private String long2ip(long ip) {
//		StringBuffer sb=new StringBuffer();  
//	    sb.append(String.valueOf((int)(ip&0xff)));  
//	    sb.append('.');  
//	    sb.append(String.valueOf((int)((ip>>8)&0xff)));  
//	    sb.append('.');  
//	    sb.append(String.valueOf((int)((ip>>16)&0xff)));  
//	    sb.append('.');  
//	    sb.append(String.valueOf((int)((ip>>24)&0xff)));  
//		return sb.toString(); 
//	}

	private void init() {
		etDNS=(EditText) view.findViewById(R.id.et_dns);
		etIp=(EditText) view.findViewById(R.id.et_ip);
		etsubnetMask=(EditText) view.findViewById(R.id.et_yanma);
		etdefaultGateway=(EditText) view.findViewById(R.id.et_wangguan);
		btnActive=(Button) view.findViewById(R.id.btn_active);
		btnStatic=(Button) view.findViewById(R.id.btn_static);
		btnActive.setOnClickListener(this);
		btnStatic.setOnClickListener(this);
		etIp.setText(ipaddress);
//		etsubnetMask.setText(netmaskIpS);
//		etdefaultGateway.setText(getwayIpS);
//		etDNS.setText(getwayIpS);
	}
	private void selectItem(View v) {
		btnActive.setSelected(false);
		btnStatic.setSelected(false);
		v.setSelected(true);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_active:
			btnActive.setSelected(false);
			btnStatic.setSelected(true);
			break;
		case R.id.btn_static:
			btnStatic.setSelected(false);
			btnActive.setSelected(true);
			etIp.setText(ipaddress);
			break;
		}
		
	}
}
