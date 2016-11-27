package com.dayuan.dy_6260chartscanner.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.dayuan.dy_6260chartscanner.R;
import com.dayuan.dy_6260chartscanner.R.layout;
import com.dayuan.dy_6260chartscanner.biz.DataManageBiz;
import com.dayuan.dy_6260chartscanner.biz.DownloadDataFromLocalServer;
import com.dayuan.dy_6260chartscanner.biz.FoodClassBiz;
import com.dayuan.dy_6260chartscanner.biz.TaskClassBiz;
import com.dayuan.dy_6260chartscanner.db.DataDao;
import com.dayuan.dy_6260chartscanner.db.ServerDao;
import com.dayuan.dy_6260chartscanner.db.TaskClassDao;
import com.dayuan.dy_6260chartscanner.entity.CheckNumber;
import com.dayuan.dy_6260chartscanner.entity.FoodClass;
import com.dayuan.dy_6260chartscanner.entity.QueryLog;
import com.dayuan.dy_6260chartscanner.entity.Server;
import com.dayuan.dy_6260chartscanner.entity.TaskClass;
import com.dayuan.dy_6260chartscanner.util.HttpRequest;
import com.dayuan.dy_6260chartscanner.util.ShowTime;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CheckNumberActivity extends BaseActivity {
	private Button btnLeft, btnRight;
	private CheckBox cbCheckAll;
	private int VIEW_COUNT = 7;
	private int checkNum;// 记录选中的条目数量
	private int index = 0;
	private TextView tvPage;
//	private BaseAdapter adapter;
	private ListView lvCheckNumber;
	//private List<CheckNumber> numbers;
	private List<TaskClass> selectid = new ArrayList<TaskClass>();
	TaskClass task;
	private Button btnYes,btnNo;
	protected int page;
	protected int totalPage;
	private int total;
	private TextView tvCounter;
	private boolean isRunning;
	private static final int MESSAGE_UPDATE_TIME = 99;
	private static final int CHECK_INTERNET = 1;
	private static final int UPDATE_DATA = 2;
	protected static final int MESSAGE_UPDATE_DATA = 3;
	private TaskClassDao taskDao;
//	List<FoodClass> foodclasses;
	
	private boolean select;
	private  ServerDao serverDao;
	private List<Server> serverList;
	private String address;
	private String user;
	private String password;
	private Handler handler;
//	=new Handler(){
//		public void handleMessage(Message msg) {
//			switch (msg.what) {
//			case UPDATE_DATA:
//				adapter.notifyDataSetChanged();
//				break;
//
//			default:
//				break;
//			}
//		};
//	};
//	TaskClass taskClass;
	List<TaskClass> taskclasses=new ArrayList<TaskClass>();
	private CheckNumberAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_check_number);
		initViews();
		taskclasses=new TaskClassBiz(this).getTaskClass();
		adapter = new CheckNumberAdapter();
		lvCheckNumber.setAdapter(adapter);

		total=taskclasses.size();
		totalPage=(total%VIEW_COUNT==0)?(total/VIEW_COUNT):(total/VIEW_COUNT+1);
		if(totalPage==0){
			totalPage=1;
		}
		tvPage.setText((index+1)+"/"+totalPage);
		taskDao=new TaskClassDao(this);
		
		isRunning = true;
		ShowTime.ShowTime(this, isRunning, tvCounter);
		handler=new Handler();
	}

	private void initViews() {
		tvCounter = (TextView) findViewById(R.id.tv_counter);
		btnLeft = (Button) findViewById(R.id.btn_left);
		btnRight = (Button) findViewById(R.id.btn_right);
		cbCheckAll = (CheckBox) findViewById(R.id.cb_check_all);
		lvCheckNumber = (ListView) findViewById(R.id.list);
		tvPage = (TextView) findViewById(R.id.tv_page);
		checkButton();
		lvCheckNumber.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				TaskClass task=taskclasses.get(position);
				int key=task.getId();
				Intent intent=new Intent(CheckNumberActivity.this,CheckNumberItemActivity.class);
				intent.putExtra("position", key);
				startActivity(intent);
				
			}
		});
	}
	@Override
    protected void onResume() {
    	isRunning = true;
    	ShowTime.ShowTime(this, isRunning, tvCounter);
    	super.onResume();
    }
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		isRunning=false;
	}
	public void doClick(View v) {
	
		switch (v.getId()) {
//		case R.id.btn_new:
//			create();
//			break;
		case R.id.btn_download:
//			new Thread(){
//				public void run() {
					
					downLoad();
			
//					handler.sendEmptyMessage(MESSAGE_UPDATE_DATA);
//				};
//			}.start();
			break;
			
		case R.id.btn_print:
			
			print();
                   
			break;
		case R.id.btn_delete:
			if(select==true&&selectid.size()!=0){
			delete();
			}else{
				Toast.makeText(this, "请选择数据", 0).show();
			}
			break;
		case R.id.btn_return:
			finish();
			break;
		case R.id.btn_left:
			leftView();
			break;
		case R.id.btn_right:
			rightView();
			break;
		case R.id.cb_check_all:
			checkAll();
			break;
		}
		tvPage.setText((index+1)+"/"+totalPage);
	}

	private void checkButton() {
		
		if (index < 1) {
			btnLeft.setEnabled(false);
			btnLeft.setSelected(false);
			if(taskclasses.size()<=VIEW_COUNT){
				btnRight.setSelected(false);
				btnRight.setEnabled(false);
			}else{
				btnRight.setSelected(true);
				btnRight.setEnabled(true);
			}
		} else if (taskclasses.size() - index * VIEW_COUNT <= VIEW_COUNT) {
			btnRight.setEnabled(false);
			btnRight.setSelected(false);
			btnLeft.setEnabled(true);
			btnLeft.setSelected(true);
		} else {
			btnLeft.setEnabled(true);
			btnLeft.setSelected(true);
			btnRight.setEnabled(true);
			btnRight.setSelected(true);
		}

	}
	private void checkAll() {
		if(cbCheckAll.isChecked()){
			selectid.clear();
			for (int i = 0; i < taskclasses.size(); i++) {
				task=taskclasses.get(i);
				task.isCheck=true;
				selectid.add(taskclasses.get(i));
			}
		}else{
			for (int i = 0; i < taskclasses.size(); i++) {
				task=taskclasses.get(i);
				task.isCheck=false;
				selectid.remove(taskclasses.get(i));
			}
			
		}
		adapter.notifyDataSetChanged();
	}

	private void leftView() {
		index--;
		adapter.notifyDataSetChanged();
		//检查Button是否可用。  
        checkButton(); 
	}

	private void rightView() {
		index++;
		adapter.notifyDataSetChanged();
		//检查Button是否可用。  
        checkButton();

	}

//	private void create() {
//		Intent intent=new Intent(this,NumberCreateActivity.class);
//		startActivity(intent);
//		finish();
//
//	}

	private void downLoad() {
		serverDao=new ServerDao(this);
		serverList=serverDao.getServerLists();
		 for (Server server:serverList) {
			 address=server.getAddress();
			 user=server.getUser();
			 password=server.getPassword();
		 }
		try {
		ConnectivityManager	mConnectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);	
		// 检查网络连接，如果无网络可用，就不需要进行连网操作等
		NetworkInfo info = mConnectivity.getActiveNetworkInfo();
		 if(info==null||!mConnectivity.getBackgroundDataSetting()){
			 Toast.makeText(CheckNumberActivity.this, "请检查网络", 0).show();
//			 handler.sendEmptyMessage(CHECK_INTERNET);
		 }else{
			 if(address!=null&&user!=null&&password!=null){
			 DownloadDataFromLocalServer.downloadTaskClass(CheckNumberActivity.this, address, user, password);
			
			 }else{
				 Toast.makeText(CheckNumberActivity.this, "请检查是否输入服务器地址", 0).show(); 
//				 handler.sendEmptyMessage(CHECK_SERVER_ADDRESS);
			 }
		 }
		 
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		
		
		handler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				taskclasses=new TaskClassBiz(CheckNumberActivity.this).getTaskClass();
				adapter.notifyDataSetChanged();	
			}
		}, 3000);
		
	}

//	private void updateData() {
//		new Thread(){
//			public void run() {
//				handler.sendEmptyMessage(UPDATE_DATA);
////				taskclasses=new TaskClassBiz(this).getTaskClass();
////				adapter = new CheckNumberAdapter(taskclasses, this);
//				
//			};
//		}.start();
//		
//	}

	private void print() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final AlertDialog alertDialog = builder.create();
		alertDialog.show();
		// 自定义UI
		Window window = alertDialog.getWindow();
		View dialogView = View.inflate(this, R.layout.printing_window, null);
		window.setContentView(dialogView);
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
//				try {
//					try {
//						printTitle();
//					} catch (ParseException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				alertDialog.dismiss();

			}
		}, 1000);

	}

	private void delete() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final AlertDialog alertDialog = builder.create();
		alertDialog.show();
		// 自定义UI
		Window window = alertDialog.getWindow();
		View dialogView = View.inflate(this, R.layout.delete_window, null);
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
				int num = 0;
				int totalNum = 0;
				int totalRec = 0;
				totalRec=taskclasses.size();
				if(cbCheckAll.isChecked()){
					for (int i = 0; i <taskclasses.size(); i++) {
						task=taskclasses.get(i);
						int id = task.getId();
						int countRow=taskDao.delete(id);
						if (countRow > 0) {
							totalNum++;
						}
					}
					taskclasses.removeAll(taskclasses);
//					task.isCheck=false;
					select=false;
					if (totalNum == 0) {
						Toast.makeText(CheckNumberActivity.this, "删除失败,没有记录", 0)
								.show();
					} else {
						Toast.makeText(CheckNumberActivity.this,
								"删除成功,删除了" + totalNum + "条记录", 0).show();
					}
					cbCheckAll.setChecked(false);
					page = 0;
					index = 0;
				}else {
					for (int j = 0; j < selectid.size(); j++) {
					for (int i = 0; i < totalRec; i++) {
						task=taskclasses.get(i);
						if(selectid.get(j).equals(task)){
									int id = task.getId();
							int count = taskDao.delete(id);
							if (count > 0) {
								num++;
							}
							taskclasses.remove(i);
						totalRec--;
						}
						task.isCheck=false;
						select=false;
					}
					}
					
					selectid.clear();
					if (num == 0) {
						Toast.makeText(CheckNumberActivity.this, "删除失败,没有记录", 0)
								.show();
					} else {
						Toast.makeText(CheckNumberActivity.this,
								"删除成功,删除了" + num + "条记录", 0).show();
					}
					int total = taskclasses.size();
					page = (total % VIEW_COUNT == 0) ? (total / VIEW_COUNT) : (total
							/ VIEW_COUNT + 1);
					if(index > (page - 1))
						index = page - 1;
				}
				adapter.notifyDataSetChanged();
				alertDialog.dismiss();
				if(page==0){
					page=1;
				}
				tvPage.setText((index + 1)+"/"+page);
				checkButton();
			}
		});
		if (taskclasses.size() == 0) {
			Toast.makeText(CheckNumberActivity.this, "删除失败,没有记录", 0).show();
			alertDialog.hide();
		}
	}

	
	public class CheckNumberAdapter extends BaseAdapter {
		// 用来控制CheckBox的选中状况
//		private List<TaskClass> taskclasses;
//		private Context context;
//		//private LayoutInflater inflater;
//		
//		// 用于显示每列5个Item项。
////		 int VIEW_COUNT=7;
////		// 用于显示页号的索引
////		 int index = 0;
////		foodclasses=new FoodClassBiz(this).
//		public CheckNumberAdapter(List<TaskClass> taskclasses, Context context) {
//			this.context = context;
////			inflater = LayoutInflater.from(this);
//			if (taskclasses == null) {
//				taskclasses = new ArrayList<TaskClass>();
//			}
//			this.taskclasses = taskclasses;
////
//		}

		@Override
		public int getCount() {
			// ori表示到目前为止的前几页的总共的个数。
			int ori = VIEW_COUNT * index;
			// 值的总个数-前几页的个数就是这一页要显示的个数，如果比默认的值小，说明这是最后一页，只需显示这么多就可以了
			if (taskclasses.size() - ori < VIEW_COUNT) {
				return taskclasses.size() - ori;
			} else {
				// 如果比默认的值还要大，说明一页显示不完，还要用换一页显示，这一页用默认的值显示满就可以了。
				return VIEW_COUNT;
			}
		}

		@Override
	    public Object getItem(int i) {
	        return null;
	    }

		 @Override
		    public long getItemId(int i) {
		        return 0;
		    }

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = CheckNumberActivity.this.getLayoutInflater()
						.inflate(R.layout.check_number_item, null);
				holder.tvNumber = (TextView) convertView
						.findViewById(R.id.tv_id);
				holder.tvCheckNumber = (TextView) convertView
						.findViewById(R.id.tv_check_number);
				holder.tvSampleName = (TextView) convertView
						.findViewById(R.id.tv_sample_name);
				holder.tvDetectUnit = (TextView) convertView
						.findViewById(R.id.tv_check_unit);
				holder.tvProjectName = (TextView) convertView
						.findViewById(R.id.tv_project_name);
				holder.tvEditTime = (TextView) convertView
						.findViewById(R.id.tv_edit_time);
				holder.check = (CheckBox) convertView
						.findViewById(R.id.cb_check);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			final TaskClass task = taskclasses.get(position + index * VIEW_COUNT);
			String planDetail=task.getPlandetail();
			String[] detailArray=planDetail.split("。");
			String detail=detailArray[0];
			String [] detailArr=detail.split("，");
			String sampleName=detailArr[0];
			String proName=detailArr[1];
			String [] ary=sampleName.split("：");
			String [] strary=proName.split("：");
			String samplename=ary[1];
			String projectname=strary[1];
		    //number.=foodclasses.get(position + index * VIEW_COUNT);
//			holder.tvNumber.setText(task.getId() + "");
			holder.tvNumber.setText(position + 1+ index * VIEW_COUNT + "");
			holder.tvCheckNumber.setText(task.getcheckNumber());
			//holder.tvSampleName.setText(foodclasses.get(position + index * VIEW_COUNT).getSampleName());
			holder.tvSampleName.setText(samplename);
			holder.tvDetectUnit.setText(task.getcheckUnit());
			holder.tvProjectName.setText(projectname);
			holder.tvEditTime.setText(task.geteditTime());
			final boolean isCheck=task.isCheck;
			if(isCheck){
				holder.check .setChecked(true);
				cbCheckAll.setChecked(true);
				select=true;
			}else{
				holder.check .setChecked(false);
			}
			holder.check.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
//					if(isCheck){
//						selectid.remove(taskclasses.get(position+index * VIEW_COUNT));
//						cbCheckAll.setChecked(true);
//					}else{
//						selectid.add(taskclasses.get(position + index * VIEW_COUNT));
//						cbCheckAll.setChecked(false);
//					}
					if (!holder.check.isChecked()) {
						task.isCheck=false;
						selectid.remove(taskclasses.get(position+index * VIEW_COUNT));
//						cbCheckAll.setChecked(false);
//						select=false;
					}else{
						task.isCheck=true;
						selectid.add(taskclasses.get(position + index * VIEW_COUNT));
						select=true;
					}
					int i;
					for (i = 0; i < taskclasses.size(); i++) {
						TaskClass task=taskclasses.get(i);
						if(task.isCheck==false){
							cbCheckAll.setChecked(false);
							break;
						}
						if(i==taskclasses.size()-1){
							cbCheckAll.setChecked(true);
						}else if(i<taskclasses.size()-1){
							cbCheckAll.setChecked(false);
						}
						
					}
				}
			});
			return convertView;
		}

		// 监听checkBox并根据原来的状态来设置新的状态
		class ViewHolder {
			CheckBox check;
			TextView tvDetectUnit;
			TextView tvNumber;
			TextView tvProjectName;
			TextView tvCheckNumber;
			TextView tvSampleName;
			TextView tvEditTime;
		}

	}

}
