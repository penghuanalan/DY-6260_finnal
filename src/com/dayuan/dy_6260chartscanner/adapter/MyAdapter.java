package com.dayuan.dy_6260chartscanner.adapter;

import java.util.List;

import com.dayuan.dy_6260chartscanner.R;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter{

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
		 TextView tvWifiItem = (TextView) view.findViewById(R.id.tv_show_item);
		 tvWifiItem.setText(scanResult.SSID);
		 ImageView imageView=( ImageView) view.findViewById(R.id.iv_wifi_image);
		 if(Math.abs(scanResult.level)>=100){
			 imageView.setImageDrawable(view.getResources().getDrawable(R.drawable.wifi_xinhao_25)); 
		 }else if(Math.abs(scanResult.level)>80&&Math.abs(scanResult.level)<100){
			 imageView.setImageDrawable(view.getResources().getDrawable(R.drawable.wifi_xinhao_50));
		 }else if(Math.abs(scanResult.level) > 70&&Math.abs(scanResult.level)<100)
		 {
			 imageView.setImageDrawable(view.getResources().getDrawable(R.drawable.wifi_xinhao_50)); 
		 }else if(Math.abs(scanResult.level)>50&&Math.abs(scanResult.level)<70){
			 imageView.setImageDrawable(view.getResources().getDrawable(R.drawable.wifi_xinhao_75)); 
		 }else {
			 imageView.setImageDrawable(view.getResources().getDrawable(R.drawable.wifi_xinhao)); 
		 }
		return view;
	}

}
