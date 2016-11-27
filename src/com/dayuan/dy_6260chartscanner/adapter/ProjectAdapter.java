package com.dayuan.dy_6260chartscanner.adapter;

import java.util.ArrayList;
import java.util.List;

import com.dayuan.dy_6260chartscanner.R;
import com.dayuan.dy_6260chartscanner.entity.Project;
import com.dayuan.dy_6260chartscanner.entity.Project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ProjectAdapter extends BaseAdapter{
    private int clickTemp=-1;
	private Context context;
	private List<Project> projects;
	private LayoutInflater inflater;
	
	public void setSelection(int position){
		clickTemp=position;
	}
	public int getSelection() {
		// TODO Auto-generated method stub
		return clickTemp;
	}
	public ProjectAdapter(Context context, List<Project> projects) {
		this.context = context;
		
		this.setProject(projects);
		this.inflater=LayoutInflater.from(context);
	}

	public void setProject(List<Project> projects){
		if(projects==null){
			projects=new ArrayList<>();}
		this.projects = projects;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return projects.size();
	}

	@Override
	public Project getItem(int position) {
		// TODO Auto-generated method stub
		return projects.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder=null;
		if(convertView==null){
			convertView=inflater.inflate(R.layout.sample_gv_item, null);
			holder=new ViewHolder();
			holder.tvName=(TextView) convertView.findViewById(R.id.tv_sample);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		Project project=getItem(position);
		holder.tvName.setText(project.getName()+"("+((Integer.parseInt(project.getMethod()) < 2)?"定量":"定性") +")");
		if(clickTemp==position){
		holder.tvName.setBackgroundResource(R.drawable.sample_shenlan);
		}else{
			holder.tvName.setBackgroundResource(R.drawable.the_sample_button);
		}
		return convertView;
	}

	class ViewHolder{
		TextView tvName;
	}

	
}
