package com.dayuan.dy_6260chartscanner.fragment;


import java.util.List;

import com.dayuan.dy_6260chartscanner.R;
import com.dayuan.dy_6260chartscanner.activity.DetectionChannelOneActvity;
import com.dayuan.dy_6260chartscanner.adapter.SampleAdapter;
import com.dayuan.dy_6260chartscanner.biz.ProjectBiz;
import com.dayuan.dy_6260chartscanner.biz.SampleBiz;
import com.dayuan.dy_6260chartscanner.db.LogDao;
import com.dayuan.dy_6260chartscanner.entity.Project;
import com.dayuan.dy_6260chartscanner.entity.Sample;
import com.dayuan.dy_6260chartscanner.util.CopyDB;
import com.dayuan.dy_6260chartscanner.util.DialogUtil;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

public class SampleFragment extends Fragment {
    List<Project> projects;
	private GridView gvSample;
	private SampleAdapter adapter;
	private ProjectBiz biz;
	private  int i;
    private Context context;
	public SampleFragment(int i) {
		super();
		this.i=i;
	}
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_sample, container,
				false);
		try {
			gvSample=(GridView) view.findViewById(R.id.gv_sample_one);
			biz=new ProjectBiz(getActivity());
			projects=biz.getProject(i);
			adapter=new SampleAdapter(getActivity(), projects);
			gvSample.setAdapter(adapter);
			gvSample.setOnItemClickListener(new OnItemClickListener() {
				
				
				
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Project project=projects.get(position);
					DialogUtil.showDialog(getActivity(), project);
				}
				
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return view;
		}
	
	
		
	
	
}
