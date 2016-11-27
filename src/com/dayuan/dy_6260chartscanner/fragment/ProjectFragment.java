package com.dayuan.dy_6260chartscanner.fragment;

import java.util.ArrayList;
import java.util.List;

import com.dayuan.dy_6260chartscanner.R;
import com.dayuan.dy_6260chartscanner.activity.ProjectSettingActivity;
import com.dayuan.dy_6260chartscanner.adapter.ProjectAdapter;
import com.dayuan.dy_6260chartscanner.biz.ProjectBiz;
import com.dayuan.dy_6260chartscanner.entity.Project;
import com.dayuan.dy_6260chartscanner.util.CopyDB;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class ProjectFragment extends Fragment {
	// public static final String action="com.dayuan";
	List<Project> projects;
	private GridView gvProject;
	private ProjectBiz biz;
	private ProjectAdapter adapter;
	private int i;

	public ProjectFragment(int i) {
		super();
		this.i = i;
	}

	private ProjectSettingActivity mActivity;
	IntentFilter filter;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_project, container,
				false);
		gvProject = (GridView) view.findViewById(R.id.gv_project);
		biz = new ProjectBiz(getActivity());
		projects = biz.getProject(i);
		adapter = new ProjectAdapter(getActivity(), projects);
		gvProject.setAdapter(adapter);
		setListener();

		return view;
	}

	public void setListener() {
		gvProject.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				adapter.setSelection(position);
				Project project = projects.get(position);
				int key = project.getId();
				Intent intent = new Intent();
				intent.setAction("com.dayuan");
				intent.putExtra("name", "" + position);
				intent.putExtra("data", "" + key);
				intent.putExtra("click", "click");
				getActivity().sendBroadcast(intent, null);
				adapter.notifyDataSetChanged();

			}

		});
	}

	public Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				Log.v("datalog", (String) msg.obj);
				projects = biz.getProject(i);
				adapter.setProject(projects);
				adapter.notifyDataSetChanged();
				
				Intent intent = new Intent();
				intent.setAction("com.dayuan");
				intent.putExtra("name", "" + 0xFF);
				getActivity().sendBroadcast(intent, null);
				break;
			case 2:
				Log.v("datalog", (String) msg.obj);
				projects = biz.getProject(i);
				adapter.setProject(projects);
				adapter.notifyDataSetChanged();
				adapter.setSelection(-1);		
				break;

			}
		};
	};

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity = (ProjectSettingActivity) activity;
		mActivity.setHandler(mHandler);
	};
}
