package com.dayuan.dy_6260chartscanner.fragment;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.dayuan.dy_6260chartscanner.R;
import com.dayuan.dy_6260chartscanner.activity.BaseActivity;
import com.dayuan.dy_6260chartscanner.biz.DownloadDataFromLocalServer;
import com.dayuan.dy_6260chartscanner.db.ServerDao;
import com.dayuan.dy_6260chartscanner.entity.Server;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CloudSettingFragment extends Fragment implements View.OnClickListener{
	View view;
	private EditText etServerAddress;
	private EditText etServerUser;
	private EditText etServerPassword;
	private Socket socket;
	private String sAddress;
	private String sUser;
	private String sPassword;
	private ServerDao serverDao;
	private List<Server> serverList;
	private int id;
	private Button btnCommunicationTest,btnSave,btnReturn;
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		 view=inflater.inflate(R.layout.fragment_cloud, container,false);
		init();
		serverDao=new ServerDao(getActivity());
		serverList=serverDao.getServerLists();
		if(serverList!=null&&!serverList.isEmpty()){
			for(Server server:serverList){
				id=server.getId();
				sAddress=server.getAddress();
				sUser=server.getUser();
				sPassword=server.getPassword();
			}
			etServerAddress.setText(sAddress);
			etServerUser.setText(sUser);
			etServerPassword.setText(sPassword);
		}
		return view;
	}
	private void init() {
		etServerAddress=(EditText) view.findViewById(R.id.et_cloud);
		etServerUser=(EditText) view.findViewById(R.id.et_cloud_user);
		etServerPassword=(EditText) view.findViewById(R.id.et_cloud_password);
		btnCommunicationTest=(Button) view.findViewById(R.id.btn_communication_test);
		btnSave=(Button) view.findViewById(R.id.btn_save);
		btnReturn=(Button) view.findViewById(R.id.btn_return);
		btnCommunicationTest.setOnClickListener(this);
		btnSave.setOnClickListener(this);
		btnReturn.setOnClickListener(this);
		
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_communication_test:
			test();
			break;

		case R.id.btn_return:
//			Intent intent=new Intent(getActivity(),BaseActivity.class);
//			startActivity(intent);
			getActivity().finish();
			break;
		case R.id.btn_save:
			save();
			break;
		}
		
	}
	private void save() {
		sAddress=etServerAddress.getText().toString();
		sUser=etServerUser.getText().toString();
		sPassword=etServerPassword.getText().toString();
		if(serverList==null||serverList.isEmpty()){
			serverDao.add(sAddress, sUser, sPassword);
			Toast.makeText(getActivity(), "保存成功", 0).show();
		}else{
			serverDao.update(id, sAddress, sUser, sPassword);
			Toast.makeText(getActivity(), "保存成功", 0).show();
		}
		
	}
	private void test() {
		
				
				DownloadDataFromLocalServer.downloadDataFromLocalServer(getActivity(), sAddress,sUser,sPassword);
		
	}
	
}
