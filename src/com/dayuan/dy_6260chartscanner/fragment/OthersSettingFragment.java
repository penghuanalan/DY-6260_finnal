package com.dayuan.dy_6260chartscanner.fragment;

import com.dayuan.dy_6260chartscanner.R;
import com.dayuan.dy_6260chartscanner.TApplication;
import com.dayuan.dy_6260chartscanner.adapter.MyAdapter;
import com.dayuan.dy_6260chartscanner.db.OrderDao;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TextView;

public class OthersSettingFragment extends Fragment implements OnCheckedChangeListener {

	private View view;
	private SharedPreferences sp;
	private Switch chanelChange, printSetting, autoUpload, linkPrint;
	private Editor edit;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_others, container, false);
		chanelChange = (Switch) view.findViewById(R.id.sw_channel_switch);
		printSetting = (Switch) view.findViewById(R.id.sw_print);
		autoUpload = (Switch) view.findViewById(R.id.sw_upload);
		linkPrint = (Switch) view.findViewById(R.id.sw_link_print);
		initViews();
		return view;
	}

	private void initViews() {
		sp = TApplication.getInstance().getSharedPreferencesIns();
		edit = sp.edit();
		boolean ischange = sp.getBoolean("isChange", false);
		boolean showResult = sp.getBoolean("showResult", false);
		boolean ifAutoPrint = sp.getBoolean("autoPrint", false);
		boolean ifAutoUpload = sp.getBoolean("autoUpload", false);
		if(ischange){
			chanelChange.setChecked(true);
		}else{
			chanelChange.setChecked(false);
		}
		if(showResult){
			linkPrint.setChecked(true);
		}else{
			linkPrint.setChecked(false);
		}
		if(ifAutoPrint){
			printSetting.setChecked(true);
		}else{
			printSetting.setChecked(false);
		}
		if(ifAutoUpload){
			autoUpload.setChecked(true);
		}else{
			autoUpload.setChecked(false);
		}
		chanelChange.setOnCheckedChangeListener(this);
		printSetting.setOnCheckedChangeListener(this);
		autoUpload.setOnCheckedChangeListener(this);
		linkPrint.setOnCheckedChangeListener(this);
	}
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		switch (buttonView.getId()) {
		case R.id.sw_channel_switch:
			if(isChecked){
				edit.putBoolean("isChange", true);
				edit.commit();
			}else{
				edit.putBoolean("isChange", false);
				edit.commit();
			}
			break;
		case R.id.sw_link_print:
			if(isChecked){
				edit.putBoolean("showResult", true);
				edit.commit();
			}else{
				edit.putBoolean("showResult", false);
				edit.commit();
			}
			
			break;
		case R.id.sw_print:
			if(isChecked){
				edit.putBoolean("autoPrint", true);
				edit.commit();
			}else{
				edit.putBoolean("autoPrint", false);
				edit.commit();
			}
			break;
		case R.id.sw_upload:
			if(isChecked){
				edit.putBoolean("autoUpload", true);
				edit.commit();
			}else{
				edit.putBoolean("autoUpload", false);
				edit.commit();
			}
			break;

		default:
			break;
		}
	}
}
