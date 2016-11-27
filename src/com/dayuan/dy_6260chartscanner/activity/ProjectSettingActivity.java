package com.dayuan.dy_6260chartscanner.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import android.R.anim;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dayuan.dy_6260chartscanner.R;
import com.dayuan.dy_6260chartscanner.TApplication;
import com.dayuan.dy_6260chartscanner.adapter.ProjectAdapter;
import com.dayuan.dy_6260chartscanner.biz.ExportData;
import com.dayuan.dy_6260chartscanner.biz.ImportData;
import com.dayuan.dy_6260chartscanner.biz.ProjectBiz;
import com.dayuan.dy_6260chartscanner.db.Dao;
import com.dayuan.dy_6260chartscanner.entity.Project;
import com.dayuan.dy_6260chartscanner.fragment.ProjectFragment;
import com.dayuan.dy_6260chartscanner.util.CopyDB;
import com.dayuan.dy_6260chartscanner.util.ShowTime;

public class ProjectSettingActivity extends FragmentActivity {
	private Button btnBack;
	private TextView tvCounter;
	private boolean isRunning;
//	private static final int MESSAGE_UPDATE_DATA = 100;

	public MyPagerAdapter pageAdapter;
	private ViewPager viewPager;
	private RadioGroup radioGroup;
	private ArrayList<Fragment> fs;
	private RadioButton firstButton;// 第一个按钮

	private Button btnEdit;
	private Button btnDelete;
	private Button btnExport;
	private Button btnYes;
	private Button btnNo;

	private Dao dao;
	public List<Project> projects;
	private static int position;
	private static int key;
	private BroadcastReceiver receiver;
	private ProjectBiz biz;

	private static final String RESULT_PATH = "/sdcard/output.xml";
	protected static final int UPDATE_RESULT = 10;
	private int total;
	private static int page;
	private static int i;
    private String click;
	private Handler mHandler;
	Context ctxDealFile;
	String filePath = "data/data/com.dayuan.dy_6260chartscanner/databases/dayuan.db";
	public void setHandler(Handler handler){
		mHandler=handler;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_project_setting);
		
		
		try {
			TApplication.instance.addActivity(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		setViews();
		setListener();
		biz = new ProjectBiz(this);
		// total=biz.getProjectSize();
		projects = biz.getProjects();
		total = projects.size();
		page = (total % 12 == 0) ? (total / 12) : (total / 12 + 1);
		// 设置Adapter
		setAdapter();
		initRadioButton();
      
		dao = new Dao(this);
		isRunning = true;
        ShowTime.ShowTime(this, isRunning, tvCounter);
	}

	private void setViews() {
		btnEdit = (Button) findViewById(R.id.btn_edit);
		btnBack = (Button) findViewById(R.id.btn_back);
		btnDelete = (Button) findViewById(R.id.btn_delete);
		btnExport = (Button) findViewById(R.id.btn_export);
		tvCounter = (TextView) findViewById(R.id.tv_counter);
		viewPager = (ViewPager) findViewById(R.id.viewPager);
		radioGroup = (RadioGroup) findViewById(R.id.radioGroup1);
		btnYes = (Button) findViewById(R.id.btn_yes);
		btnNo = (Button) findViewById(R.id.btn_no);
		
	}

	public void doClick(View v) {
		switch (v.getId()) {
		case R.id.btn_new:
			Intent it2 = new Intent(ProjectSettingActivity.this,
					NewActivity.class);

			startActivity(it2);
			break;

		case R.id.btn_edit:
            if(click!=null){
			Intent it3 = new Intent(ProjectSettingActivity.this,
					EditActvity.class);
			it3.putExtra("data", "" + position);
			it3.putExtra("positions", "" + key);
			startActivity(it3);
            }else{
            	Toast.makeText(ProjectSettingActivity.this, "请选择项目名称", 0).show();	
            }
			break;
		case R.id.btn_delete:
			 if(click!=null){
			delete();}
			 else{
				 Toast.makeText(ProjectSettingActivity.this, "请选择项目名称", 0).show();	 
			 }
			break;
		case R.id.btn_export:
			export();
			break;
		case R.id.btn_return:
//			Intent intent = new Intent(ProjectSettingActivity.this,
//					BaseActivity.class);
//			startActivity(intent);
			finish();
			break;
		}

	}

	private void delete() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final AlertDialog alertDialog = builder.create();
		alertDialog.setCanceledOnTouchOutside(false);
		alertDialog.show();
		// 自定义UI
		Window window = alertDialog.getWindow();
		View dialogView = View.inflate(this, R.layout.delete_two_window, null);
		window.setContentView(dialogView);
		btnNo = (Button) window.findViewById(R.id.btn_no);
		btnYes = (Button) window.findViewById(R.id.btn_yes);
		btnNo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				alertDialog.dismiss();
			}
		});
		btnYes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// for(Project p:projects){
				for (int j = 0; j < projects.size(); j++) {
					Project p = projects.get(j);
					int id = p.getId();
					if (id==key) {
						Log.i("key", key + "");
						Log.i("right", id + "");
						int count = dao.delete(id);
						if (count == 0) {
							Toast.makeText(ProjectSettingActivity.this,
									"删除失败,没有记录", 0).show();
						} else {
							Toast.makeText(ProjectSettingActivity.this,
									"删除成功,删除了" + count + "条记录", 0).show();
						}
						projects.remove(j);
					}

				}
				Message msg=new Message();
				msg.obj = "datalog";
				msg.what = 1;
				mHandler.sendMessage(msg);
				alertDialog.dismiss();
				
			}
		});

	}

	private void export() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final AlertDialog alertDialog = builder.create();
		alertDialog.setCanceledOnTouchOutside(true);
		alertDialog.show();
		// 自定义UI
		Window window = alertDialog.getWindow();
		View dialogView = View.inflate(this, R.layout.export_window, null);
		window.setContentView(dialogView);
		btnNo = (Button) window.findViewById(R.id.btn_import);
		btnYes = (Button) window.findViewById(R.id.btn_export);
		btnNo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ImportData.getCSV(ProjectSettingActivity.this);
				Message msg=new Message();
				msg.obj = "datalog";
				msg.what = 2;
				mHandler.sendMessage(msg);
				alertDialog.dismiss();
			}
		});
		btnYes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				outputXML();
				alertDialog.dismiss();
			}

		});

	}
	private List<String> dataList=new ArrayList<String>();
	public void outputXML() {
//		File file = new File(RESULT_PATH);
//		org.dom4j.Document doc = DocumentHelper.createDocument();// 创建document
//		org.dom4j.Element projectEle = doc.addElement("project");// 添加根元素
		
                String fileName="DY6260_items.csv";
                dataList.add("编号");
                dataList.add("项目名称");
                dataList.add("方法");
				dataList.add("C值");
				dataList.add("T值≤");
				dataList.add("T值≥");
				dataList.add("单位");
				dataList.add("限值");
				dataList.add("A0");
				dataList.add("A1");
				dataList.add("A2");
				dataList.add("A3");
			for (int i = 0; i < projects.size(); i++) {
				Project p = projects.get(i);
//				org.dom4j.Element sampleEle = projectEle.addElement("sample");
//				sampleEle.addElement("name").addText(p.getName());
//				sampleEle.addElement("method").addText(p.getMethod());
//				sampleEle.addElement("ValueC").addText(p.getValueC());
//				sampleEle.addElement("ValueT").addText(p.getValueT());
//				sampleEle.addElement("ValueTL").addText(p.getValueTL());
//				sampleEle.addElement("Constant0").addText(p.getConstant0());
//				sampleEle.addElement("Constant1").addText(p.getConstant1());
//				sampleEle.addElement("Constant2").addText(p.getConstant2());
//				sampleEle.addElement("Constant3").addText(p.getConstant3());
				    String idNumber=p.getSampleid()+"";
					String projectName=p.getName().toString().trim();
					String method=p.getMethod().toString().trim();
					String ValueC=p.getValueC().toString().trim();
					String ValueT=p.getValueT().toString().trim();
					String ValueTL=p.getValueTL().toString().trim();
					String unit=p.getUnit().toString().trim();
					String limitValue=p.getLimitV().toString().trim();
					String Constant0=p.getConstant0().toString().trim();
					String Constant1=p.getConstant1().toString().trim();
					String Constant2=p.getConstant2().toString().trim();
					String Constant3=p.getConstant3().toString().trim();
					dataList.add(idNumber);
					dataList.add(projectName);
					dataList.add(method);
					dataList.add(ValueC);
					dataList.add(ValueT);
					dataList.add(ValueTL);
					dataList.add(unit);
					dataList.add(limitValue);
					dataList.add(Constant0);
					dataList.add(Constant1);
					dataList.add(Constant2);
					dataList.add(Constant3);
			}
			ExportData.exportCsv(ProjectSettingActivity.this ,dataList,fileName);
			}
//			OutputFormat format = OutputFormat.createPrettyPrint();
//			format.setEncoding("utf-8");
//			XMLWriter writer = new XMLWriter(new FileWriter(file), format);
//			writer.write(doc);
//			writer.close();
//			file.createNewFile();

	@Override
	protected void onResume() {
		receiver = new InnerReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("com.dayuan");
		registerReceiver(receiver, filter);
	    isRunning = true;
	    ShowTime.ShowTime(this, isRunning, tvCounter);
		super.onResume();
	}

	private class InnerReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String name = intent.getExtras().getString("name");
			String data = intent.getExtras().getString("data");
			position = Integer.parseInt(name);			
			Log.i("receiver", position + "");
		    click=intent.getExtras().getString("click");
			if(position != 0xFF)
			{
				key = Integer.parseInt(data);
				
//				btnEdit.setClickable(true);
//				btnDelete.setClickable(true);
			}
			else
			{
				
			}
		}
	}


	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		isRunning = false;
		unregisterReceiver(receiver);
		super.onDestroy();
	}

	private void initRadioButton() {
		float density = getResources().getDisplayMetrics().density;
		RadioGroup.LayoutParams params_rb = new RadioGroup.LayoutParams(
				(int) (12 * density), (int) (12 * density));
		int margin = (int) (6 * density);
		params_rb.setMargins(margin, margin, margin, margin);
		for (i = 0; i < page; i++) {
			RadioButton tempButton = new RadioButton(this);
			tempButton.setBackgroundResource(R.drawable.selector_01);
			tempButton.setButtonDrawable(android.R.color.transparent);
			tempButton.setId(i);
			if (i == 0) {
				firstButton = tempButton;
			}
			radioGroup.addView(tempButton, params_rb);
		}
		if (firstButton != null) {
			firstButton.setChecked(true);
		}

	}

	private void setListener() {
		// 当更换viewpager的某一页时，更新底部导航
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int id) {
				RadioButton rb = (RadioButton) findViewById(id);
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
		// if(findViewById(R.id.main_container)!=null){
		fs = new ArrayList<Fragment>();
		for (i = 0; i < page; i++) {
			fs.add(new ProjectFragment(i));
		}
		FragmentManager manager = this.getSupportFragmentManager();
		pageAdapter = new MyPagerAdapter(manager);

		viewPager.setAdapter(pageAdapter);
		viewPager.setCurrentItem(0, false);		
	}

	class MyPagerAdapter extends FragmentStatePagerAdapter {
		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			return fs.get(position);

			// return fs[position];
		}

		@Override
		public int getCount() {
			return fs.size();
		}

	}

}
