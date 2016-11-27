package com.dayuan.dy_6260chartscanner.activity;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

import com.ant.liao.GifView;
import com.ant.liao.GifView.GifImageType;
import com.dayuan.dy_6260chartscanner.R;
import com.dayuan.dy_6260chartscanner.TApplication;
import com.dayuan.dy_6260chartscanner.biz.ProjectBiz;
import com.dayuan.dy_6260chartscanner.entity.Project;
import com.dayuan.dy_6260chartscanner.fragment.SampleFragment;
import com.dayuan.dy_6260chartscanner.util.CopyDB;
import com.dayuan.dy_6260chartscanner.util.ShowTime;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class SampleTestActivity extends FragmentActivity {
	
	//private Button btnBack;
	private TextView tvCounter;
	private boolean isRunning;
	
	MyPagerAdapter pageAdapter;
	private ViewPager viewPager;
	private RadioGroup radioGroup;
	private ArrayList<Fragment>fs;
	private RadioButton firstButton;//第一个按钮
	
	private int total;
	private static int page;
	private static int i;
	private ProjectBiz biz;
	public List<Project> projects;
//	private GifImageView gifImageView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sample_test);
//		CopyDB copyDb=new CopyDB();
//		SQLiteDatabase  db =copyDb.openDatabase(this); 
		try {
			TApplication.instance.addActivity(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		setViews();
		setListener();
		biz=new ProjectBiz(this);
		//total=biz.getProjectSize();
        projects=biz.getProjects();
        total=projects.size();
		page=(total%12==0) ? (total/12):(total/12+1);
		//设置Adapter
		setAdapter();
		initRadioButton();
		
		isRunning = true;
		ShowTime.ShowTime(this, isRunning, tvCounter);
		
//		try {
//			GifDrawable	gifDrawable = new GifDrawable(getResources(), R.drawable.select_sample);
//			gifImageView.setBackground(gifDrawable);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
}
	
	private void initRadioButton() {
			float density = getResources().getDisplayMetrics().density;  
			RadioGroup.LayoutParams params_rb = new RadioGroup.LayoutParams( 
					(int)(12*density), (int)(12*density));
			int margin = (int)(6*density); params_rb.setMargins(margin, 0, margin, margin); 
			for ( i = 0; i < page; i++) {
				 RadioButton tempButton = new RadioButton(this);
				 tempButton.setBackgroundResource(R.drawable.selector_01);
				 tempButton.setButtonDrawable(android.R.color.transparent);
				 tempButton.setId(i);
				 if(i==0){
					 firstButton=tempButton;
				 }
				 radioGroup.addView(tempButton,  params_rb);
			}
			if(firstButton !=null){
				firstButton.setChecked(true);
			}
		
	}

	private void setViews() {
		//btnBack=(Button) findViewById(R.id.btn_back);
		tvCounter=(TextView) findViewById(R.id.tv_counter);
		viewPager=(ViewPager) findViewById(R.id.viewPager);
		radioGroup=(RadioGroup)findViewById(R.id.radioGroup1);
		GifView gifView=(GifView) findViewById(R.id.gifView_diamond);
			
		 gifView.setGifImage(R.drawable.select_sample);
		//请求网络gif的时候使用
	    gifView.setGifImageType(GifImageType.WAIT_FINISH);
	}
	public void doClick(View v){
//		Intent intent=new Intent(SampleTestActivity.this,BaseActivity.class);
//		startActivity(intent);
		finish();
	}
	@Override
    protected void onResume() {
    	super.onResume();
    	isRunning = true;
    	ShowTime.ShowTime(this, isRunning, tvCounter);
    }
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		isRunning=false;
		super.onDestroy();
	}
	private void setListener() {
		//当更换viewpager的某一页时，更新底部导航
				viewPager.setOnPageChangeListener(new OnPageChangeListener() {
					@Override
					public void onPageSelected(int id) {
						RadioButton rb = (RadioButton)findViewById(id);
						rb.setChecked(true);
					}
					@Override
					public void onPageScrollStateChanged(int id) {
					 
						
					}

					@Override
					public void onPageScrolled(int arg0, float arg1, int arg2) {
						
						
					}

				});
				radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						viewPager.setCurrentItem(checkedId);
					}
				});
	}
	
					
	private void setAdapter() {
		fs=new ArrayList<Fragment>();
		for (int i = 0; i < page; i++) {
			fs.add(new SampleFragment(i));
		}
		pageAdapter=new MyPagerAdapter(getSupportFragmentManager());
		
		viewPager.setAdapter(pageAdapter);
		viewPager.setCurrentItem(0,false);
	}
	class MyPagerAdapter extends FragmentPagerAdapter{
		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			return fs.get(position);
		}

		@Override
		public int getCount() {
			return fs.size();
		}
		
	}
	
	}
