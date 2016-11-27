package com.dayuan.dy_6260chartscanner.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.dayuan.dy_6260chartscanner.R;
import com.dayuan.dy_6260chartscanner.R.layout;
import com.dayuan.dy_6260chartscanner.biz.DownloadDataFromLocalServer;
import com.dayuan.dy_6260chartscanner.biz.SelectItemBiz;
import com.dayuan.dy_6260chartscanner.db.SelectItemDao;
import com.dayuan.dy_6260chartscanner.db.ServerDao;
import com.dayuan.dy_6260chartscanner.entity.SelectItem;
import com.dayuan.dy_6260chartscanner.entity.Server;
import com.dayuan.dy_6260chartscanner.util.ShowTime;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.Toast;

public class CheckStandardActivity extends Activity implements TextWatcher {
//	protected static final int DOWNLOAD_FAILURE = 10;
	private CheckBox cbCheckAll;
	private SelectItemAdapter adapter;
	private List<SelectItem> items;
	private List<SelectItem> selectItems;
	private List<SelectItem> selects;
	private List<SelectItem> datas;
	private List<SelectItem> dataLists;
	private ListView listView;
	private EditText etSampleName;
	private EditText etProjectName;
	private Button btnDownload,btnNew,btnEdit,btnDelete,btnCheck;
	private String sampleName;
	private Intent intent;
	private String projectName;
	private List<SelectItem> selectid = new ArrayList<SelectItem>();
	private SelectItem item;
	private SelectItemDao itemDao;
	private int index = 0;
	private boolean isSelect ;
	private int id;
	private String foodName;
	private String proName;
	private String stanName;
	private String stanValue;
	private String symbol;
	private String unitName;
	private  ServerDao serverDao;
	private List<Server> serverList;
	private String address;
	private String user;
	private String password;
	private LinearLayout llSelectSampleTitle;
	private LinearLayout llSelectSample;
	private Handler handler;
	private boolean isRunning;
   // private Boolean isRunning=true;
//	private Handler handler=new Handler(){
//		public void handleMessage(android.os.Message msg) {
//			switch (msg.arg1) {
//			case DOWNLOAD_FAILURE:
//				Toast.makeText(CheckStandardActivity.this, "下载失败，请检查网络", 0).show();
//				break;
//
//			default:
//				break;
//			}
//		};
//	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_check_standard);
		initViews();
		handler=new Handler();
		intent = getIntent();
		itemDao = new SelectItemDao(this);
		projectName = intent.getStringExtra("name");
		if ("get".equals(intent.getStringExtra("get"))) {
//			etProjectName.setText(intent.getStringExtra("name").toString());
			dataLists = new SelectItemBiz(this).getSelectData(projectName);
			adapter = new SelectItemAdapter(dataLists);
			listView.setAdapter(adapter);
			btnDownload.setVisibility(View.GONE);
			btnNew.setVisibility(View.GONE);
			btnEdit.setVisibility(View.GONE);
			btnDelete.setVisibility(View.GONE);
			llSelectSample.setVisibility(View.GONE);
			llSelectSampleTitle.setVisibility(View.VISIBLE);
			cbCheckAll.setEnabled(false);
		} else {
			llSelectSample.setVisibility(View.VISIBLE);
			llSelectSampleTitle.setVisibility(View.GONE);
			items = new SelectItemBiz(this).getItems();
			adapter = new SelectItemAdapter(items);
			listView.setAdapter(adapter);
		}

		Listener();
	}

	// private int clickTemp=-1;
	// private boolean isCheck=true;
	private void Listener() {
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if ("get".equals(intent.getStringExtra("get"))) {
					SelectItem item = dataLists.get(position);
					String samName = item.getSampleName();
					Intent it = new Intent(CheckStandardActivity.this,
							DetectionChannelOneActvity.class);
					it.putExtra("sample", samName);
					setResult(RESULT_OK, it);
					finish();
				}
			}
		});

	}
	private void initViews() {
		llSelectSample=(LinearLayout) findViewById(R.id.ll_select_sample);
		llSelectSampleTitle=(LinearLayout) findViewById(R.id.ll_select_sample_title);
		cbCheckAll = (CheckBox) findViewById(R.id.cb_check_all);
		listView = (ListView) findViewById(R.id.list);
		etSampleName = (EditText) findViewById(R.id.et_sample_name);
		etProjectName = (EditText) findViewById(R.id.et_project_name);
		btnCheck = (Button) findViewById(R.id.btn_check);
		btnDownload = (Button) findViewById(R.id.btn_download);
		btnNew = (Button) findViewById(R.id.btn_new);
		btnEdit = (Button) findViewById(R.id.btn_edit);
		btnDelete = (Button) findViewById(R.id.btn_delete);
		etSampleName.addTextChangedListener(this);
		etProjectName.addTextChangedListener(this);

	}

	public void doClick(View v) {
		switch (v.getId()) {
		case R.id.cb_check_all:
			checkAll();
			break;
		case R.id.btn_download:
			download();
			break;
		case R.id.btn_check:
			query();
			break;
		case R.id.btn_new:
			create();
			break;
		case R.id.btn_edit:
			int num=0;
			if (index == 0) {
				for (int j = 0; j < selectid.size(); j++) {
					for (int i = 0; i < items.size(); i++) {
						item = items.get(i);
						//item.isCheck = true;
						if (selectid.get(j).equals(item)) {
							if(isSelect==true){
								id = item.getId();
								foodName = item.getSampleName();
								proName = item.getName();
								stanName = item.getCheckStandard();
								stanValue = item.getStandardValue();
								symbol = item.getDemarcate();
								unitName = item.getUnit();
							edit();
							num++;
							isSelect = false;
							break;
							}
							}
						item.isCheck=false;
					}
				}
				selectid.clear();
			} else if (index == 1) {
				for (int j = 0; j < selectid.size(); j++) {
					for (int i = 0; i < selectItems.size(); i++) {
						item = selectItems.get(i);
						if (selectid.get(j).equals(item)) {
							if(isSelect==true){
							id = item.getId();
							foodName = item.getSampleName();
							proName = item.getName();
							stanName = item.getCheckStandard();
							stanValue = item.getStandardValue();
							symbol = item.getDemarcate();
							unitName = item.getUnit();
							edit();
							num++;
							isSelect = false;
							break;
							}
							}
						item.isCheck=false;
					}
				}
				selectid.clear();
			}else if(index==2){
				for (int j = 0; j < selectid.size(); j++) {
					for (int i = 0; i < selects.size(); i++) {
						item = selects.get(i);
						if (selectid.get(j).equals(item)) {
							if(isSelect==true){
								id = item.getId();
								foodName = item.getSampleName();
								proName = item.getName();
								stanName = item.getCheckStandard();
								stanValue = item.getStandardValue();
								symbol = item.getDemarcate();
								unitName = item.getUnit();
								edit();
								num++;
								isSelect = false;
								break;
								}
								}
							item.isCheck=false;
						}
					}
					selectid.clear();
			}else if(index==3){
				for (int j = 0; j < selectid.size(); j++) {
					for (int i = 0; i < datas.size(); i++) {
						item = datas.get(i);
						if (selectid.get(j).equals(item)) {
							if(isSelect==true){
								id = item.getId();
								foodName = item.getSampleName();
								proName = item.getName();
								stanName = item.getCheckStandard();
								stanValue = item.getStandardValue();
								symbol = item.getDemarcate();
								unitName = item.getUnit();
								edit();
								num++;
								isSelect = false;
								break;
								}
								}
							item.isCheck=false;
						}
					}
					selectid.clear();
			}
			adapter.notifyDataSetChanged();
				if (num == 0) {
					Toast.makeText(CheckStandardActivity.this, "请选择样品", 1)
							.show();
				}
			break;
		case R.id.btn_delete:
			if(isSelect==true&&selectid.size()!=0){
			delete();}
			else{
				Toast.makeText(this, "请选择样品", 0).show();
			}
			break;
		case R.id.btn_return:
			if ("get".equals(intent.getStringExtra("get"))) {
				Intent it = new Intent(CheckStandardActivity.this,
						DetectionChannelOneActvity.class);
				setResult(0, it);
			} else {
//				Intent in = new Intent(CheckStandardActivity.this,
//						DataActivity.class);
//				startActivity(in);
			}
			finish();
			break;
		}
	}

	private void checkAll() {
		if (index == 0) {
			if (cbCheckAll.isChecked()) {
				selectid.clear();
				for (int i = 0; i < items.size(); i++) {
					item = items.get(i);
					item.isCheck = true;
					selectid.add(items.get(i));
				}
			} else {
				for (int i = 0; i < items.size(); i++) {
					item = items.get(i);
					item.isCheck = false;
					selectid.remove(items.get(i));
				}

			}

		} else if (index == 1) {
			if (cbCheckAll.isChecked()) {
				selectid.clear();
				for (int i = 0; i < selectItems.size(); i++) {
					item = selectItems.get(i);
					item.isCheck = true;
					selectid.add(selectItems.get(i));
				}
			} else {
				for (int i = 0; i < selectItems.size(); i++) {
					item = selectItems.get(i);
					item.isCheck = false;
					selectid.remove(selectItems.get(i));
				}

			}

		} else if (index == 2) {
			if (cbCheckAll.isChecked()) {
				selectid.clear();
				for (int i = 0; i < selects.size(); i++) {
					item = selects.get(i);
					item.isCheck = true;
					selectid.add(selects.get(i));
				}
			} else {
				for (int i = 0; i < selects.size(); i++) {
					item = selects.get(i);
					item.isCheck = false;
					selectid.remove(selects.get(i));
				}

			}
		} else if (index == 3) {
			if (cbCheckAll.isChecked()) {
				selectid.clear();
				for (int i = 0; i < datas.size(); i++) {
					item = datas.get(i);
					item.isCheck = true;
					selectid.add(datas.get(i));
				}
			} else {
				for (int i = 0; i < datas.size(); i++) {
					item = datas.get(i);
					item.isCheck = false;
					selectid.remove(datas.get(i));
				}

			}
		}
		adapter.notifyDataSetChanged();

	}

	private PopupWindow mWindow;
	private ListView lvSample;
	private ListView lvProject;
	private ListView lvstandard;
	private ListView lvSymbol;
	private ListView lvUnit;
	List<String> listSample = new ArrayList<String>();
	List<String> listProject = new ArrayList<String>();
	List<String> liststandard = new ArrayList<String>();
	List<String> listSymbol = new ArrayList<String>();
	List<String> listUnit = new ArrayList<String>();
	String chnumber;
	String checkProject;
	private int key = 0;
	private Map<String, String> mapSample = new HashMap<String, String>();
	private Map<String, String> mapProject = new HashMap<String, String>();
	private Map<String, String> mapstandard = new HashMap<String, String>();
	private Map<String, String> mapSymbol = new HashMap<String, String>();
	private Map<String, String> mapUnit = new HashMap<String, String>();

	// Iterator it = mapSample.entrySet().iterator();
	// List<String> lists;
	private void create() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final AlertDialog alertDialog = builder.create();
		alertDialog.show();
		alertDialog.setCanceledOnTouchOutside(false);
		// 自定义UI
		alertDialog.getWindow().clearFlags(
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
						| WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		alertDialog.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		// alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
		// |
		// WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		Window window = alertDialog.getWindow();
		View dialogView = View.inflate(this, R.layout.new_edit_window, null);
		window.setContentView(dialogView);
		TextView tvTextView=(TextView) window.findViewById(R.id.tv_new_edit);
		final EditText etSampleName = (EditText) window
				.findViewById(R.id.et_sample_name);
		final EditText etProjectName = (EditText) window
				.findViewById(R.id.et_project_name);
		final EditText etStandardValue = (EditText) window
				.findViewById(R.id.et_standard_value);
		final EditText etStandardName = (EditText) window
				.findViewById(R.id.et_standard_name);
		final EditText etJudgeSymbol = (EditText) window
				.findViewById(R.id.et_judge_symbol);
		final EditText etUnit = (EditText) window.findViewById(R.id.et_unit);
		Button btnDownArrow = (Button) window.findViewById(R.id.btn_down_arrow);
		Button btnProArrow = (Button) window
				.findViewById(R.id.btn_down_arrow02);
		Button btnSNArrow = (Button) window.findViewById(R.id.btn_down_arrow04);
		Button btnJSArrow = (Button) window.findViewById(R.id.btn_down_arrow05);
		Button btnUnitArrow = (Button) window
				.findViewById(R.id.btn_down_arrow06);
		Button btnSave = (Button) window.findViewById(R.id.btn_save);
		Button btnBack = (Button) window.findViewById(R.id.btn_back);
		for (SelectItem item : items) {
			String sampleName = item.getSampleName();
			String projectName = item.getName();
			String standardName = item.getCheckStandard();
//			String judgeSymbol = item.getDemarcate();
			String unit = item.getUnit();
			mapSample.put(sampleName, sampleName);
			mapProject.put(projectName, projectName);
			mapstandard.put(standardName, standardName);
//			mapSymbol.put(judgeSymbol, judgeSymbol);
			mapUnit.put(unit, unit);
		}
        tvTextView.setText("新建");
		btnSave.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String sampleName = etSampleName.getText().toString().trim();
				String projectName = etProjectName.getText().toString().trim();
				String standardValue = etStandardValue.getText().toString()
						.trim();
				String standardName = etStandardName.getText().toString()
						.trim();
				String judgeSymbol = etJudgeSymbol.getText().toString().trim();
				String unit = etUnit.getText().toString().trim();
				if (TextUtils.isEmpty(sampleName)) {
					Toast.makeText(CheckStandardActivity.this, "样品名称不能为空", 0)
							.show();
				} else if (TextUtils.isEmpty(projectName)) {
					Toast.makeText(CheckStandardActivity.this, "项目名称不能为空", 0)
							.show();
				} else if (TextUtils.isEmpty(standardValue)) {
					Toast.makeText(CheckStandardActivity.this, "标准值不能为空", 0)
							.show();
				} else if (TextUtils.isEmpty(standardName)) {
					Toast.makeText(CheckStandardActivity.this, "标准名称不能为空", 0)
							.show();
				} else if (TextUtils.isEmpty(judgeSymbol)) {
					Toast.makeText(CheckStandardActivity.this, "判定符号不能为空", 0)
							.show();
				} else if (TextUtils.isEmpty(unit)) {
					Toast.makeText(CheckStandardActivity.this, "单位不能为空", 0)
							.show();
				} else {
					long id = itemDao.add(sampleName, projectName,
							standardName, standardValue, judgeSymbol, unit);
					if (id == -1) {
						Toast.makeText(CheckStandardActivity.this, "添加失败", 0)
								.show();
					} else {
						Toast.makeText(CheckStandardActivity.this, "添加成功", 0)
								.show();
					}
					alertDialog.dismiss();
				}
			}
		});
		btnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				alertDialog.dismiss();
			}
		});
		btnDownArrow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				key = 1;
				listSample.clear();
				for (Map.Entry<String, String> entry : mapSample.entrySet()) {
					// String key = entry.getKey().toString();
					String value = entry.getValue().toString().trim();
					if (!value.isEmpty()) {
						listSample.add(value);
					}
				}
				if (mWindow == null) {
					mWindow = new PopupWindow(CheckStandardActivity.this);
					mWindow.setWidth(etSampleName.getWidth());
					mWindow.setHeight(180);
					mWindow.setContentView(lvSample);
					// 设置点击外边消掉PopupWindow
					// window.setOutsideTouchable(true);

					mWindow.setFocusable(true);
				}
				mWindow.showAsDropDown(etSampleName, 0, 0);
			}
		});
		btnProArrow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				key = 2;
				listProject.clear();
				for (Map.Entry<String, String> entry : mapProject.entrySet()) {
					// String key = entry.getKey().toString();
					String value = entry.getValue().toString().trim();
					if (!value.isEmpty()) {
						listProject.add(value);
					}
				}
				if (mWindow == null) {
					mWindow = new PopupWindow(CheckStandardActivity.this);
					mWindow.setWidth(etProjectName.getWidth());
					mWindow.setHeight(180);
					mWindow.setContentView(lvProject);
					// 设置点击外边消掉PopupWindow
					// window.setOutsideTouchable(true);

					mWindow.setFocusable(true);
				}
				mWindow.showAsDropDown(etProjectName, 0, 0);
			}
		});
		btnSNArrow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				key = 3;
				liststandard.clear();
				for (Map.Entry<String, String> entry : mapstandard.entrySet()) {
					// String key = entry.getKey().toString();
					String value = entry.getValue().toString().trim();
					if (!value.isEmpty()) {
						liststandard.add(value);
					}
				}
				if (mWindow == null) {
					mWindow = new PopupWindow(CheckStandardActivity.this);
					mWindow.setWidth(etStandardName.getWidth());
					mWindow.setHeight(180);
					mWindow.setContentView(lvstandard);
					// 设置点击外边消掉PopupWindow
					// window.setOutsideTouchable(true);

					mWindow.setFocusable(true);
				}
				mWindow.showAtLocation(etStandardName, 0, 428, 0);
			}
		});
		btnJSArrow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				key = 4;
				listSymbol.clear();
//				for (Map.Entry<String, String> entry : mapSymbol.entrySet()) {
//					// String key = entry.getKey().toString();
//					String value = entry.getValue().toString().trim();
//					if (!value.isEmpty()) {
//						listSymbol.add(value);
//					}
//				}
				String[] symbols={"≤","＜","≥","＞"};
				for(String symbol:symbols){
					listSymbol.add(symbol);
				}
				if (mWindow == null) {
					mWindow = new PopupWindow(CheckStandardActivity.this);
					mWindow.setWidth(etJudgeSymbol.getWidth());
					mWindow.setHeight(180);
					mWindow.setContentView(lvSymbol);
					// 设置点击外边消掉PopupWindow
					// window.setOutsideTouchable(true);

					mWindow.setFocusable(true);
				}
				mWindow.showAtLocation(etJudgeSymbol, 0, 173, 0);

			}
		});
		btnUnitArrow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				key = 5;
				listUnit.clear();
				for (Map.Entry<String, String> entry : mapUnit.entrySet()) {
					// String key = entry.getKey().toString();
					String value = entry.getValue().toString().trim();
					if (!value.isEmpty()) {
						listUnit.add(value);
					}
				}
				if (mWindow == null) {
					mWindow = new PopupWindow(CheckStandardActivity.this);
					mWindow.setWidth(etUnit.getWidth());
					mWindow.setHeight(180);
					mWindow.setContentView(lvUnit);
					// 设置点击外边消掉PopupWindow
					// window.setOutsideTouchable(true);

					mWindow.setFocusable(true);
				}
				mWindow.showAtLocation(etUnit, 0, 428, 0);
			}
		});
		lvSample = new ListView(CheckStandardActivity.this);
		lvProject = new ListView(CheckStandardActivity.this);
		lvstandard = new ListView(CheckStandardActivity.this);
		lvSymbol = new ListView(CheckStandardActivity.this);
		lvUnit = new ListView(CheckStandardActivity.this);
		lvSample.setBackgroundResource(R.drawable.listview_background);
		lvProject.setBackgroundResource(R.drawable.listview_background);
		lvstandard.setBackgroundResource(R.drawable.listview_background);
		lvSymbol.setBackgroundResource(R.drawable.listview_background);
		lvUnit.setBackgroundResource(R.drawable.listview_background);
		lvSample.setAdapter(new StandardAdapter());
		lvProject.setAdapter(new StandardAdapter());
		lvstandard.setAdapter(new StandardAdapter());
		lvSymbol.setAdapter(new StandardAdapter());
		lvUnit.setAdapter(new StandardAdapter());
		lvSample.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String sample = listSample.get(position);
				etSampleName.setText(sample);
				if (mWindow != null && mWindow.isShowing()) {
					mWindow.dismiss();
					mWindow = null;
				}

			}
		});
		lvProject.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String project = listProject.get(position);
				etProjectName.setText(project);
				if (mWindow != null && mWindow.isShowing()) {
					mWindow.dismiss();
					mWindow = null;
				}

			}
		});
		lvstandard.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String standard = liststandard.get(position);
				etStandardName.setText(standard);
				if (mWindow != null && mWindow.isShowing()) {
					mWindow.dismiss();
					mWindow = null;
				}

			}
		});
		lvSymbol.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String symbol = listSymbol.get(position);
				etJudgeSymbol.setText(symbol);
				if (mWindow != null && mWindow.isShowing()) {
					mWindow.dismiss();
					mWindow = null;
				}

			}
		});
		lvUnit.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String unit = listUnit.get(position);
				etUnit.setText(unit);
				if (mWindow != null && mWindow.isShowing()) {
					mWindow.dismiss();
					mWindow = null;
				}

			}
		});
	}

	public class StandardAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			if (key == 1) {
				return listSample.size();
			} else if (key == 2) {
				return listProject.size();
			} else if (key == 3) {
				return liststandard.size();
			} else if (key == 4) {
				return listSymbol.size();
			} else if (key == 5) {
				return listUnit.size();
			}
			return items.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view;
			ViewHolder holder;
			if (convertView != null) {
				view = convertView;
				holder = (ViewHolder) view.getTag();
			} else {
				view = View.inflate(CheckStandardActivity.this,
						R.layout.popup_window_item, null);
				holder = new ViewHolder();
				holder.textView = (TextView) view
						.findViewById(R.id.tv_check_num);
				view.setTag(holder);
			}
			if (key == 1) {
				String stringSample = listSample.get(position);
				holder.textView.setText(stringSample);
			} else if (key == 2) {
				String stringProject = listProject.get(position);
				holder.textView.setText(stringProject);
			} else if (key == 3) {
				String stringStandard = liststandard.get(position);
				holder.textView.setText(stringStandard);
			} else if (key == 4) {
				String stringSymbol = listSymbol.get(position);
				holder.textView.setText(stringSymbol);
			} else if (key == 5) {
				String stringUnit = listUnit.get(position);
				holder.textView.setText(stringUnit);
			}
			return view;
		}

		class ViewHolder {
			TextView textView;

		}
	}

	private void edit() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final AlertDialog alertDialog = builder.create();
		alertDialog.setCanceledOnTouchOutside(false);
		alertDialog.show();
		// 自定义UI
		alertDialog.getWindow().clearFlags(
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
						| WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		alertDialog.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		// alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
		// |
		// WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		 Window window = alertDialog.getWindow();
		 View dialogView = View.inflate(this, R.layout.new_edit_window, null);
		window.setContentView(dialogView);
		TextView tvTextView=(TextView) window.findViewById(R.id.tv_new_edit);
		final EditText etSampleName = (EditText) window
				.findViewById(R.id.et_sample_name);
		final EditText etProjectName = (EditText) window
				.findViewById(R.id.et_project_name);
		final EditText etStandardValue = (EditText) window
				.findViewById(R.id.et_standard_value);
		final EditText etStandardName = (EditText) window
				.findViewById(R.id.et_standard_name);
		final EditText etJudgeSymbol = (EditText) window
				.findViewById(R.id.et_judge_symbol);
		final EditText etUnit = (EditText) window.findViewById(R.id.et_unit);
		Button btnDownArrow = (Button) window.findViewById(R.id.btn_down_arrow);
		Button btnProArrow = (Button) window
				.findViewById(R.id.btn_down_arrow02);
		Button btnSNArrow = (Button) window.findViewById(R.id.btn_down_arrow04);
		Button btnJSArrow = (Button) window.findViewById(R.id.btn_down_arrow05);
		Button btnUnitArrow = (Button) window
				.findViewById(R.id.btn_down_arrow06);
		Button btnSave = (Button) window.findViewById(R.id.btn_save);
		Button btnBack = (Button) window.findViewById(R.id.btn_back);
		for (SelectItem item : items) {
			String sampleName = item.getSampleName();
			String projectName = item.getName();
			String standardName = item.getCheckStandard();
//			String judgeSymbol = item.getDemarcate();
			String unit = item.getUnit();
			mapSample.put(sampleName, sampleName);
			mapProject.put(projectName, projectName);
			mapstandard.put(standardName, standardName);
//			mapSymbol.put(judgeSymbol, judgeSymbol);
			mapUnit.put(unit, unit);
		}
		tvTextView.setText("编辑");
		etSampleName.setText(foodName);
		etProjectName.setText(proName);
		etStandardValue.setText(stanValue);
		etStandardName.setText(stanName);
		etJudgeSymbol.setText(symbol);
		etUnit.setText(unitName);
		btnSave.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String sampleName = etSampleName.getText().toString().trim();
				String projectName = etProjectName.getText().toString().trim();
				String standardValue = etStandardValue.getText().toString()
						.trim();
				String standardName = etStandardName.getText().toString()
						.trim();
				String judgeSymbol = etJudgeSymbol.getText().toString().trim();
				String unit = etUnit.getText().toString().trim();
				if (TextUtils.isEmpty(sampleName)) {
					Toast.makeText(CheckStandardActivity.this, "样品名称不能为空", 0)
							.show();
				} else if (TextUtils.isEmpty(projectName)) {
					Toast.makeText(CheckStandardActivity.this, "项目名称不能为空", 0)
							.show();
				} else if (TextUtils.isEmpty(standardValue)) {
					Toast.makeText(CheckStandardActivity.this, "标准值不能为空", 0)
							.show();
				} else if (TextUtils.isEmpty(standardName)) {
					Toast.makeText(CheckStandardActivity.this, "标准名称不能为空", 0)
							.show();
				} else if (TextUtils.isEmpty(judgeSymbol)) {
					Toast.makeText(CheckStandardActivity.this, "判定符号不能为空", 0)
							.show();
				} else if (TextUtils.isEmpty(unit)) {
					Toast.makeText(CheckStandardActivity.this, "单位不能为空", 0)
							.show();
				} else {
					long rowId = itemDao.update(id, sampleName, projectName,
							standardName, standardValue, judgeSymbol, unit);
					if (rowId == -1) {
						Toast.makeText(CheckStandardActivity.this, "编辑失败", 0)
								.show();
					} else {
						Toast.makeText(CheckStandardActivity.this, "编辑成功", 0)
								.show();
					}
					alertDialog.dismiss();
				}
			}
		});
		btnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				isSelect = false;
				item.isCheck=false;
				adapter.notifyDataSetChanged();
				alertDialog.dismiss();
			}
		});
		btnDownArrow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				key = 1;
				listSample.clear();
				for (Map.Entry<String, String> entry : mapSample.entrySet()) {
					// String key = entry.getKey().toString();
					String value = entry.getValue().toString().trim();
					if (!value.isEmpty()) {
						listSample.add(value);
					}
				}
				if (mWindow == null) {
					mWindow = new PopupWindow(CheckStandardActivity.this);
					mWindow.setWidth(etSampleName.getWidth());
					mWindow.setHeight(180);
					mWindow.setContentView(lvSample);
					// 设置点击外边消掉PopupWindow
					// window.setOutsideTouchable(true);

					mWindow.setFocusable(true);
				}
				mWindow.showAsDropDown(etSampleName, 0, 0);
			}
		});
		btnProArrow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				key = 2;
				listProject.clear();
				for (Map.Entry<String, String> entry : mapProject.entrySet()) {
					// String key = entry.getKey().toString();
					String value = entry.getValue().toString().trim();
					if (!value.isEmpty()) {
						listProject.add(value);
					}
				}
				if (mWindow == null) {
					mWindow = new PopupWindow(CheckStandardActivity.this);
					mWindow.setWidth(etProjectName.getWidth());
					mWindow.setHeight(180);
					mWindow.setContentView(lvProject);
					// 设置点击外边消掉PopupWindow
					// window.setOutsideTouchable(true);

					mWindow.setFocusable(true);
				}
				mWindow.showAsDropDown(etProjectName, 0, 0);
			}
		});
		btnSNArrow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				key = 3;
				liststandard.clear();
				for (Map.Entry<String, String> entry : mapstandard.entrySet()) {
					// String key = entry.getKey().toString();
					String value = entry.getValue().toString().trim();
					if (!value.isEmpty()) {
						liststandard.add(value);
					}
				}
				if (mWindow == null) {
					mWindow = new PopupWindow(CheckStandardActivity.this);
					mWindow.setWidth(etStandardName.getWidth());
					mWindow.setHeight(180);
					mWindow.setContentView(lvstandard);
					// 设置点击外边消掉PopupWindow
					// window.setOutsideTouchable(true);

					mWindow.setFocusable(true);
				}
				mWindow.showAtLocation(etStandardName, 0, 428, 0);
			}
		});
		btnJSArrow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				key = 4;
				listSymbol.clear();
				String[] symbols={"≤","＜","≥","＞"};
//				for (Map.Entry<String, String> entry : mapSymbol.entrySet()) {
//					// String key = entry.getKey().toString();
//					String value = entry.getValue().toString().trim();
//					if (!value.isEmpty()) {
//						listSymbol.add(value);
//					}
//				}
				for(String symbol:symbols){
					listSymbol.add(symbol);
				}
				if (mWindow == null) {
					mWindow = new PopupWindow(CheckStandardActivity.this);
					mWindow.setWidth(etJudgeSymbol.getWidth());
					mWindow.setHeight(180);
					mWindow.setContentView(lvSymbol);
					// 设置点击外边消掉PopupWindow
					// window.setOutsideTouchable(true);

					mWindow.setFocusable(true);
				}
				mWindow.showAtLocation(etJudgeSymbol, 0, 173, 0);

			}
		});
		btnUnitArrow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				key = 5;
				listUnit.clear();
				for (Map.Entry<String, String> entry : mapUnit.entrySet()) {
					// String key = entry.getKey().toString();
					String value = entry.getValue().toString().trim();
					if (!value.isEmpty()) {
						listUnit.add(value);
					}
				}
				if (mWindow == null) {
					mWindow = new PopupWindow(CheckStandardActivity.this);
					mWindow.setWidth(etUnit.getWidth());
					mWindow.setHeight(180);
					mWindow.setContentView(lvUnit);
					// 设置点击外边消掉PopupWindow
					// window.setOutsideTouchable(true);

					mWindow.setFocusable(true);
				}
				mWindow.showAtLocation(etUnit, 0, 428, 0);
			}
		});
		lvSample = new ListView(CheckStandardActivity.this);
		lvProject = new ListView(CheckStandardActivity.this);
		lvstandard = new ListView(CheckStandardActivity.this);
		lvSymbol = new ListView(CheckStandardActivity.this);
		lvUnit = new ListView(CheckStandardActivity.this);
		lvSample.setBackgroundResource(R.drawable.listview_background);
		lvProject.setBackgroundResource(R.drawable.listview_background);
		lvstandard.setBackgroundResource(R.drawable.listview_background);
		lvSymbol.setBackgroundResource(R.drawable.listview_background);
		lvUnit.setBackgroundResource(R.drawable.listview_background);
		lvSample.setAdapter(new StandardAdapter());
		lvProject.setAdapter(new StandardAdapter());
		lvstandard.setAdapter(new StandardAdapter());
		lvSymbol.setAdapter(new StandardAdapter());
		lvUnit.setAdapter(new StandardAdapter());
		lvSample.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String sample = listSample.get(position);
				etSampleName.setText(sample);
				if (mWindow != null && mWindow.isShowing()) {
					mWindow.dismiss();
					mWindow = null;
				}

			}
		});
		lvProject.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String project = listProject.get(position);
				etProjectName.setText(project);
				if (mWindow != null && mWindow.isShowing()) {
					mWindow.dismiss();
					mWindow = null;
				}

			}
		});
		lvstandard.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String standard = liststandard.get(position);
				etStandardName.setText(standard);
				if (mWindow != null && mWindow.isShowing()) {
					mWindow.dismiss();
					mWindow = null;
				}

			}
		});
		lvSymbol.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String symbol = listSymbol.get(position);
				etJudgeSymbol.setText(symbol);
				if (mWindow != null && mWindow.isShowing()) {
					mWindow.dismiss();
					mWindow = null;
				}

			}
		});
		lvUnit.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String unit = listUnit.get(position);
				etUnit.setText(unit);
				if (mWindow != null && mWindow.isShowing()) {
					mWindow.dismiss();
					mWindow = null;
				}

			}
		});
	}

	private void delete() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final AlertDialog alertDialog = builder.create();
		alertDialog.show();
		// 自定义UI
		Window window = alertDialog.getWindow();
		View dialogView = View.inflate(this, R.layout.delete_window, null);
		window.setContentView(dialogView);
		Button btnNo = (Button) window.findViewById(R.id.btn_no);
		Button btnYes = (Button) window.findViewById(R.id.btn_yes);
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
				// totalRec=items.size();
				if (index == 0) {
					if (cbCheckAll.isChecked()) {
//						for (int i = 0; i < items.size(); i++) {
//							item = items.get(i);
//							int id = item.getId();
//							int countRow = itemDao.delete(id);
//							
//						}
						int countRow = itemDao.deleteAll();
						if (countRow > 0) {
							totalNum++;
						}
						items.removeAll(items);
						isSelect=false;
						if (totalNum == 0) {
							Toast.makeText(CheckStandardActivity.this,
									"删除失败,没有记录", 0).show();
						} else {
							Toast.makeText(CheckStandardActivity.this,
									"删除成功,删除了所有记录", 0).show();
						}
						cbCheckAll.setChecked(false);
						// page = 0;
						// index = 0;
					} else {
						for (int j = 0; j < selectid.size(); j++) {
							for (int i = 0; i < items.size(); i++) {
								item = items.get(i);
								if (selectid.get(j).equals(item)) {
									int id = item.getId();
									int count = itemDao.delete(id);
									if (count > 0) {
										num++;
									}
									items.remove(i);
									// totalRec--;
								}
								item.isCheck = false;
								isSelect=false;
							}
						}
						selectid.clear();
						if (num == 0) {
							Toast.makeText(CheckStandardActivity.this,
									"删除失败,没有记录", 0).show();
						} else {
							Toast.makeText(CheckStandardActivity.this,
									"删除成功,删除了" + num + "条记录", 0).show();
						}
					}
				} else if (index == 1) {
					if (cbCheckAll.isChecked()) {
//						for (int i = 0; i < selectItems.size(); i++) {
//							item = selectItems.get(i);
//							int id = item.getId();
//							int countRow = itemDao.delete(id);
//							if (countRow > 0) {
//								totalNum++;
//							}
//						}
						int countRow = itemDao.deleteAll();
						if (countRow > 0) {
							totalNum++;
						}
						selectItems.removeAll(selectItems);
						
						if (totalNum == 0) {
							Toast.makeText(CheckStandardActivity.this,
									"删除失败,没有记录", 0).show();
						} else {
							Toast.makeText(CheckStandardActivity.this,
									"删除成功,删除了所有记录", 0).show();
						}
						isSelect=false;
						cbCheckAll.setChecked(false);
						// page = 0;
						// index = 0;
					} else {
						for (int j = 0; j < selectid.size(); j++) {
							for (int i = 0; i < selectItems.size(); i++) {
								item = selectItems.get(i);
								if (selectid.get(j).equals(item)) {
									int id = item.getId();
									int count = itemDao.delete(id);
									if (count > 0) {
										num++;
									}
									selectItems.remove(i);
									// totalRec--;
								}
								item.isCheck = false;
								isSelect=false;
							}
						}
						selectid.clear();
						if (num == 0) {
							Toast.makeText(CheckStandardActivity.this,
									"删除失败,没有记录", 0).show();
						} else {
							Toast.makeText(CheckStandardActivity.this,
									"删除成功,删除了" + num + "条记录", 0).show();
						}
					}
				} else if (index == 2) {
					if (cbCheckAll.isChecked()) {
//						for (int i = 0; i < selects.size(); i++) {
//							item = selects.get(i);
//							int id = item.getId();
//							int countRow = itemDao.delete(id);
//							if (countRow > 0) {
//								totalNum++;
//							}
//						}
						int countRow = itemDao.deleteAll();
						if (countRow > 0) {
							totalNum++;
						}
						selects.removeAll(selects);
						if (totalNum == 0) {
							Toast.makeText(CheckStandardActivity.this,
									"删除失败,没有记录", 0).show();
						} else {
							Toast.makeText(CheckStandardActivity.this,
									"删除成功,删除了所有记录", 0).show();
						}
						isSelect=false;
						cbCheckAll.setChecked(false);
						// page = 0;
						// index = 0;
					} else {
						for (int j = 0; j < selectid.size(); j++) {
							for (int i = 0; i < selects.size(); i++) {
								item = selects.get(i);
								if (selectid.get(j).equals(item)) {
									int id = item.getId();
									int count = itemDao.delete(id);
									if (count > 0) {
										num++;
									}
									selects.remove(i);
									// totalRec--;
								}
								isSelect=false;
								item.isCheck = false;
							}
						}
						selectid.clear();
						if (num == 0) {
							Toast.makeText(CheckStandardActivity.this,
									"删除失败,没有记录", 0).show();
						} else {
							Toast.makeText(CheckStandardActivity.this,
									"删除成功,删除了" + num + "条记录", 0).show();
						}
					}
				} else if (index == 3) {
					if (cbCheckAll.isChecked()) {
//						for (int i = 0; i < datas.size(); i++) {
//							item = datas.get(i);
//							int id = item.getId();
//							int countRow = itemDao.delete(id);
//							if (countRow > 0) {
//								totalNum++;
//							}
//						}
						int countRow = itemDao.deleteAll();
						if (countRow > 0) {
							totalNum++;
						}
						datas.removeAll(datas);
						if (totalNum == 0) {
							Toast.makeText(CheckStandardActivity.this,
									"删除失败,没有记录", 0).show();
						} else {
							Toast.makeText(CheckStandardActivity.this,
									"删除成功,删除了所有记录", 0).show();
						}
						isSelect=false;
						cbCheckAll.setChecked(false);
						// page = 0;
						// index = 0;
					} else {
						for (int j = 0; j < selectid.size(); j++) {
							for (int i = 0; i < datas.size(); i++) {
								item = datas.get(i);
								if (selectid.get(j).equals(item)) {
									int id = item.getId();
									int count = itemDao.delete(id);
									if (count > 0) {
										num++;
									}
									datas.remove(i);
									// totalRec--;
								}
								isSelect=false;
								item.isCheck = false;
							}
						}
						selectid.clear();
						if (num == 0) {
							Toast.makeText(CheckStandardActivity.this,
									"删除失败,没有记录", 0).show();
						} else {
							Toast.makeText(CheckStandardActivity.this,
									"删除成功,删除了" + num + "条记录", 0).show();
						}
					}
				}
				adapter.notifyDataSetChanged();
				alertDialog.dismiss();
			}
		});
		if (index == 0) {
			if (items.size() == 0) {
				Toast.makeText(CheckStandardActivity.this, "删除失败,没有记录", 0)
						.show();
				alertDialog.hide();
			}
		} else if (index == 1) {
			if (selectItems.size() == 0) {
				Toast.makeText(CheckStandardActivity.this, "删除失败,没有记录", 0)
						.show();
				alertDialog.hide();
			}
		} else if (index == 2) {
			if (selects.size() == 0) {
				Toast.makeText(CheckStandardActivity.this, "删除失败,没有记录", 0)
						.show();
				alertDialog.hide();
			}
		} else if (index == 3) {
			if (datas.size() == 0) {
				Toast.makeText(CheckStandardActivity.this, "删除失败,没有记录", 0)
						.show();
				alertDialog.hide();
			}
		}

	}

	private void download() {
		serverDao=new ServerDao(this);
		serverList=serverDao.getServerLists();
		 for (Server server:serverList) {
			 address=server.getAddress();
			 user=server.getUser();
			 password=server.getPassword();
		 }
		new AlertDialog.Builder(this)
		 .setTitle("系统提示")//设置对话框标题  
		  
	     .setMessage("下载需要一点时间，确定要下载么")//设置显示的内容  
	  
	     .setPositiveButton("确定",new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				try {
					ConnectivityManager	mConnectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);	
					// 检查网络连接，如果无网络可用，就不需要进行连网操作等
					NetworkInfo info = mConnectivity.getActiveNetworkInfo();
					 if(info==null||!mConnectivity.getBackgroundDataSetting()){
						 Toast.makeText(CheckStandardActivity.this, "请检查网络", 0).show();
//						 handler.sendEmptyMessage(DOWNLOAD_FAILURE);
					 }else{
						 if(address!=null&&user!=null&&password!=null){
						 DownloadDataFromLocalServer
						 .downloadCheckItem(CheckStandardActivity.this,address,user,password);
						 }else{
							 Toast.makeText(CheckStandardActivity.this, "请检查是否输入服务器地址", 0).show(); 
						 }
					 }
					//Thread.sleep(1000);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	handler.postDelayed(new Runnable() {
					
					@Override
					public void run() {
						items = new SelectItemBiz(CheckStandardActivity.this).getItems();
						adapter = new SelectItemAdapter(items);
						listView.setAdapter(adapter);
						
					}
				}, 60000);
				dialog.dismiss();
			}
	    	 
	     })
	     .setNegativeButton("取消", null)
	     .show();//添加确定按钮  
	         
		
				
	}

	private void query() {
		etSampleName.addTextChangedListener(this);
		etProjectName.addTextChangedListener(this);

	}

	class SelectItemAdapter extends BaseAdapter {
		private List<SelectItem> items;
		 //private Map<Integer,Integer> selected;
		private boolean[] checks; //用于保存checkBox的选择状态  
		public SelectItemAdapter(List<SelectItem> items) {
			super();
			this.items = items;
			if (items == null) {
				items = new ArrayList<SelectItem>();
			}
			//selected=new HashMap<Integer,Integer>();
			checks = new boolean[items.size()];  
		}

		@Override
		public int getCount() {
			return items.size();
		}

		@Override
		public Object getItem(int position) {
			return items.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		// View itemView;
		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			// convertView=itemView;
			final SelectItem item;
			final ViewHolder holder;
			if (convertView == null) {
				convertView = CheckStandardActivity.this.getLayoutInflater()
						.inflate(R.layout.check_standard_item, null);
				holder = new ViewHolder();
				holder.tvNumber = (TextView) convertView
						.findViewById(R.id.tv_number);
				holder.tvSampleName = (TextView) convertView
						.findViewById(R.id.tv_sample_name);
				holder.tvProjectName = (TextView) convertView
						.findViewById(R.id.tv_project_name);
				holder.tvCheckStandard = (TextView) convertView
						.findViewById(R.id.tv_standard_name);
				holder.tvCriterionSymbol = (TextView) convertView
						.findViewById(R.id.tv_criterion_symbol);
				holder.tvStandardValue = (TextView) convertView
						.findViewById(R.id.tv_standard_value);
				holder.tvUnit = (TextView) convertView
						.findViewById(R.id.tv_unit);
				holder.check = (CheckBox) convertView
						.findViewById(R.id.cb_check);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			item = items.get(position);
			
			// holder.tvNumber.setText(item.getId() + "");
			holder.tvNumber.setText(position + 1 + "");
			holder.tvSampleName.setText(item.getSampleName());
			holder.tvProjectName.setText(item.getName());
			holder.tvCheckStandard.setText(item.getCheckStandard());
			holder.tvCriterionSymbol.setText(item.getDemarcate());
			holder.tvStandardValue.setText(item.getStandardValue());
			holder.tvUnit.setText(item.getUnit());
			// final boolean isCheck=item.isCheck;
			final boolean isCheck = item.isCheck;
			
			if ("get".equals(intent.getStringExtra("get"))) {
				holder.check.setVisibility(View.GONE);
			}else{
				holder.check.setVisibility(View.VISIBLE);
			}
			
			holder.check.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
				{ 
					item.isCheck = isChecked; 
					}
				});	
			holder.check.setChecked(item.isCheck );
			if (isCheck) {
				holder.check.setChecked(true);
				cbCheckAll.setChecked(true);
				item.isCheck=true;
				isSelect=true;
			} else {
				holder.check.setChecked(false);
				cbCheckAll.setChecked(false);
			}
			holder.check.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (!holder.check.isChecked()) {
						selectid.remove(items.get(position));
//						cbCheckAll.setChecked(false);
						 item.isCheck=false;
//						isSelect=false;
					}else{
						selectid.add(items.get(position));
						 item.isCheck=true;
						isSelect=true;
					}
					int i;
					for (i = 0; i < items.size(); i++) {
						SelectItem item=items.get(i);
						if(item.isCheck==false){
							cbCheckAll.setChecked(false);
							break;
						}
						if(i==items.size()-1){
							cbCheckAll.setChecked(true);
						}else if(i<items.size()-1){
							cbCheckAll.setChecked(false);
						}
						
					}
				}
			});
			
			return convertView;
		}
		class ViewHolder {
			TextView tvNumber, tvSampleName, tvProjectName, tvCheckStandard,
					tvStandardValue, tvCriterionSymbol, tvUnit;
			CheckBox check;
		}

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterTextChanged(Editable s) {
		items.clear();
		adapter.isEmpty();
		adapter = new SelectItemAdapter(selectItems);
		sampleName = etSampleName.getText().toString();
		String projectName = etProjectName.getText().toString();
		if (sampleName.length() > 0 && projectName.length() < 1) {
			index = 1;
			selectItems = new SelectItemBiz(this).getSelectItems(sampleName);
			adapter = new SelectItemAdapter(selectItems);
			listView.setAdapter(adapter);
		} else if (projectName.length() > 0 && sampleName.length() < 1) {
			index = 2;
			selects = new SelectItemBiz(this).getSelect(projectName);
			adapter = new SelectItemAdapter(selects);
			listView.setAdapter(adapter);
		} else if (sampleName.length() > 0 && projectName.length() > 0) {
			index = 3;
//			items.clear();
//			adapter.isEmpty();
			datas = new SelectItemBiz(this).getSelects(sampleName, projectName);
			adapter = new SelectItemAdapter(datas);
			listView.setAdapter(adapter);
		} else if (sampleName.length() < 1 || projectName.length() < 1) {
			index = 0;
			items = new SelectItemBiz(this).getItems();
			adapter = new SelectItemAdapter(items);
			listView.setAdapter(adapter);
		}
	}
//	@Override
//		public boolean onKeyDown(int keyCode, KeyEvent event) {
//		
//		if (keyCode == KeyEvent.KEYCODE_BACK )  
//	      { 
//			isRunning=false;
//	      	Toast.makeText(CheckStandardActivity.this, "下载停止", 0).show();
//	      	return true;
//	      }
//		return super.onKeyDown(keyCode, event);
//	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (null != this.getCurrentFocus()) {
			/**
			 * 点击空白位置 隐藏软键盘
			 */
			InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
			return mInputMethodManager.hideSoftInputFromWindow(this
					.getCurrentFocus().getWindowToken(), 0);
		}
		return super.onTouchEvent(event);
	}		
}