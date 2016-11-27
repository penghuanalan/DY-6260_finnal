package com.dayuan.dy_6260chartscanner.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.dayuan.dy_6260chartscanner.R;
import com.dayuan.dy_6260chartscanner.R.layout;
import com.dayuan.dy_6260chartscanner.activity.DetectionReportActivity.DetectionReportAdapter;
import com.dayuan.dy_6260chartscanner.activity.DetectionReportActivity.DetectionReportAdapter.ViewHolder;
import com.dayuan.dy_6260chartscanner.biz.ReportDataBiz;
import com.dayuan.dy_6260chartscanner.biz.TaskClassBiz;
import com.dayuan.dy_6260chartscanner.db.ReportDataDao;
import com.dayuan.dy_6260chartscanner.db.TaskClassDao;
import com.dayuan.dy_6260chartscanner.entity.ReportData;
import com.dayuan.dy_6260chartscanner.entity.ReportNumber;
import com.dayuan.dy_6260chartscanner.entity.TaskClass;
import com.dayuan.dy_6260chartscanner.util.ShowTime;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class DetectionReportActivity extends BaseActivity {
	private Button btnLeft, btnRight;
	private CheckBox cbCheckAll;
	private int VIEW_COUNT = 7;
	private int checkNum;// 记录选中的条目数量
	private int index = 0;
	private TextView tvPage;
	private BaseAdapter adapter;
	private ListView lvDetectionReport;
	private List<ReportData> datas = new ArrayList<ReportData>();
	private List<ReportData> selectid = new ArrayList<ReportData>();
	ReportData report;
	private Button btnYes, btnNo;
	protected int page;
	// private Handler handler;
	private int total;
	private int totalPage;
	private TextView tvCounter;
	private boolean isRunning;
	private static final int MESSAGE_UPDATE_TIME = 99;
	private ReportDataDao reportDataDao;
	private Boolean isSelect = false;
	private String reportNumber;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detection_report);

		initViews();
		datas = new ReportDataBiz(this).getReportDataClass();

		reportDataDao = new ReportDataDao(this);
		for (ReportData report : datas) {
			reportNumber = report.getReportNumber();
		}
		if (reportNumber != null && !TextUtils.isEmpty(reportNumber)) {
			adapter = new DetectionReportAdapter(datas, this);
			lvDetectionReport.setAdapter(adapter);
			total = datas.size();
		}
		totalPage = (total % VIEW_COUNT == 0) ? (total / VIEW_COUNT) : (total
				/ VIEW_COUNT + 1);
		if (totalPage == 0) {
			totalPage = 1;
		}
		listener();
		tvPage.setText((index + 1) + "/" + totalPage);
		isRunning = true;
		ShowTime.ShowTime(this, isRunning, tvCounter);
	}

	private void listener() {
		lvDetectionReport.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ReportData report = datas.get(position);
				int key = report.getId();
				Intent intent = new Intent(DetectionReportActivity.this,
						DetectReportItemActivity.class);
				intent.putExtra("back", "backTwo");
				intent.putExtra("position", key);
				startActivity(intent);
			}
		});

	}

	private void initViews() {
		tvCounter = (TextView) findViewById(R.id.tv_counter);
		btnLeft = (Button) findViewById(R.id.btn_left);
		btnRight = (Button) findViewById(R.id.btn_right);
		cbCheckAll = (CheckBox) findViewById(R.id.cb_check_all);
		lvDetectionReport = (ListView) findViewById(R.id.list);
		tvPage = (TextView) findViewById(R.id.tv_page);
		checkButton();

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
		isRunning = false;
	}

	public void doClick(View v) {
		switch (v.getId()) {
		case R.id.btn_print:
			// print();
			break;
		case R.id.btn_delete:
			if (isSelect == true) {
				delete();
			} else {
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
		tvPage.setText((index + 1) + "/" + totalPage);
	}

	private void checkButton() {

		if (index < 1) {
			btnLeft.setEnabled(false);
			btnLeft.setSelected(false);
			if (datas.size() <= VIEW_COUNT) {
				btnRight.setSelected(false);
				btnRight.setEnabled(false);
			} else {
				btnRight.setSelected(true);
				btnRight.setEnabled(true);
			}
		} else if (datas.size() - index * VIEW_COUNT <= VIEW_COUNT) {
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
		if (cbCheckAll.isChecked()) {
			selectid.clear();
			for (int i = 0; i < datas.size(); i++) {
				report = datas.get(i);
				report.isCheck = true;
				selectid.add(datas.get(i));
			}
		} else {

			for (int i = 0; i < datas.size(); i++) {
				report = datas.get(i);
				report.isCheck = false;
				selectid.remove(datas.get(i));
			}
		}
		if (!datas.isEmpty()) {
			adapter.notifyDataSetChanged();
		}
	}

	private void leftView() {
		index--;
		adapter.notifyDataSetChanged();
		// 检查Button是否可用。
		checkButton();
	}

	private void rightView() {
		index++;
		adapter.notifyDataSetChanged();
		// 检查Button是否可用。
		checkButton();

	}

	// private void print() {
	// AlertDialog.Builder builder = new AlertDialog.Builder(this);
	// final AlertDialog alertDialog = builder.create();
	// alertDialog.show();
	// // 自定义UI
	// Window window = alertDialog.getWindow();
	// View dialogView = View.inflate(this, R.layout.printing_window, null);
	// window.setContentView(dialogView);
	// handler.postDelayed(new Runnable() {
	//
	// @Override
	// public void run() {
	// // try {
	// // try {
	// // printTitle();
	// // } catch (ParseException e) {
	// // // TODO Auto-generated catch block
	// // e.printStackTrace();
	// // }
	// // } catch (IOException e) {
	// // // TODO Auto-generated catch block
	// // e.printStackTrace();
	// // }
	// alertDialog.dismiss();
	//
	// }
	// }, 1000);
	//
	// }
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
				totalRec = datas.size();
				if (cbCheckAll.isChecked()) {
					for (int i = 0; i < datas.size(); i++) {
						report = datas.get(i);
						int id = report.getId();
						int countRow = reportDataDao.delete(id);
						if (countRow > 0) {
							totalNum++;
						}
					}
					datas.removeAll(datas);
					if (totalNum == 0) {
						Toast.makeText(DetectionReportActivity.this,
								"删除失败,没有记录", 0).show();
					} else {
						Toast.makeText(DetectionReportActivity.this,
								"删除成功,删除了" + totalNum + "条记录", 0).show();
					}
					cbCheckAll.setChecked(false);
					page = 0;
					index = 0;
				} else {
					for (int j = 0; j < selectid.size(); j++) {
						for (int i = 0; i < totalRec; i++) {
							report = datas.get(i);
							if (selectid.get(j).equals(report)) {
								datas.remove(i);
								int id = report.getId();
								int count = reportDataDao.delete(id);
								if (count > 0) {
									num++;
								}
								datas.remove(i);
								totalRec--;
							}
							report.isCheck = false;
						}
					}
					selectid.clear();
					if (num == 0) {
						Toast.makeText(DetectionReportActivity.this,
								"删除失败,没有记录", 0).show();
					} else {
						Toast.makeText(DetectionReportActivity.this,
								"删除成功,删除了" + num + "条记录", 0).show();
					}
					int total = datas.size();
					page = (total % VIEW_COUNT == 0) ? (total / VIEW_COUNT)
							: (total / VIEW_COUNT + 1);
					if (index > (page - 1))
						index = page - 1;
				}
				adapter.notifyDataSetChanged();
				alertDialog.dismiss();
				if (page == 0) {
					page = 1;
				}
				tvPage.setText((index + 1) + "/" + page);
				checkButton();
			}
		});
		if (datas.size() == 0) {
			Toast.makeText(DetectionReportActivity.this, "删除失败,没有记录", 0).show();
			alertDialog.hide();
		}
	}

	public class DetectionReportAdapter extends BaseAdapter {
		// 用来控制CheckBox的选中状况
		private List<ReportData> datas;
		private Context context;
		private LayoutInflater inflater;

		// 用于显示每列5个Item项。
		// int VIEW_COUNT=7;
		// 用于显示页号的索引
		// int index = 0;

		public DetectionReportAdapter(List<ReportData> datas, Context context) {
			this.context = context;
			inflater = LayoutInflater.from(this.context);
			if (datas == null) {
				datas = new ArrayList<ReportData>();
			}
			this.datas = datas;

		}

		@Override
		public int getCount() {
			// ori表示到目前为止的前几页的总共的个数。
			int ori = VIEW_COUNT * index;
			// 值的总个数-前几页的个数就是这一页要显示的个数，如果比默认的值小，说明这是最后一页，只需显示这么多就可以了
			if (datas.size() - ori < VIEW_COUNT) {
				return datas.size() - ori;
			} else {
				// 如果比默认的值还要大，说明一页显示不完，还要用换一页显示，这一页用默认的值显示满就可以了。
				return VIEW_COUNT;
			}
		}

		@Override
		public ReportData getItem(int position) {
			return datas.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			final ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = DetectionReportActivity.this.getLayoutInflater()
						.inflate(R.layout.detect_report_item, null);
				holder.tvNumber = (TextView) convertView
						.findViewById(R.id.tv_id);
				holder.tvDetectUnit = (TextView) convertView
						.findViewById(R.id.tv_check_unit);
				holder.tvBeDetected = (TextView) convertView
						.findViewById(R.id.tv_be_detected);
				holder.tvReportNumber = (TextView) convertView
						.findViewById(R.id.tv_report_number);
				holder.tvProjectName = (TextView) convertView
						.findViewById(R.id.tv_detect_project);
				holder.tvDetectResult = (TextView) convertView
						.findViewById(R.id.tv_detect_result);
				holder.check = (CheckBox) convertView
						.findViewById(R.id.cb_check);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			final ReportData report = datas.get(position + index * VIEW_COUNT);
			String planDetail = report.getPlandetail();
			String[] detailArray = planDetail.split("。");
			String detail = detailArray[0];
			String[] detailArr = detail.split("，");
			// String sampleName=detailArr[0];
			String proName = detailArr[1];
			// String [] ary=sampleName.split("：");
			String[] strary = proName.split("：");
			// String samplename=ary[1];
			String projectname = strary[1];
			// holder.tvNumber.setText(report.getId() + "");
			holder.tvNumber.setText(position + 1 + index * VIEW_COUNT + "");
			holder.tvDetectUnit.setText(report.getCheckUnit());
			// holder.tvBeDetected.setText("万家菜市场");
			holder.tvReportNumber.setText(report.getReportNumber());
			holder.tvProjectName.setText(projectname);
			holder.tvDetectResult.setText(report.getResult());
			final boolean isCheck = report.isCheck;
			if (isCheck) {
				holder.check.setChecked(true);
				cbCheckAll.setChecked(true);
				isSelect = true;
			} else {
				holder.check.setChecked(false);
				// isSelect=false;
			}
			holder.check.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// if(isCheck){
					// selectid.remove(taskclasses.get(position+index *
					// VIEW_COUNT));
					// cbCheckAll.setChecked(true);
					// }else{
					// selectid.add(taskclasses.get(position + index *
					// VIEW_COUNT));
					// cbCheckAll.setChecked(false);
					// }
					if (!holder.check.isChecked()) {
						selectid.remove(datas
								.get(position + index * VIEW_COUNT));
						// cbCheckAll.setChecked(false);
						report.isCheck = false;
					} else {
						selectid.add(datas.get(position + index * VIEW_COUNT));
						report.isCheck = true;
						isSelect = true;
					}
					int i;
					for (i = 0; i < datas.size(); i++) {
						ReportData report = datas.get(i);
						if (report.isCheck == false) {
							cbCheckAll.setChecked(false);
							break;
						}
						if (i == datas.size() - 1) {
							cbCheckAll.setChecked(true);
						} else if (i < datas.size() - 1) {
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
			TextView tvBeDetected;
			TextView tvReportNumber;
			TextView tvProjectName;
			TextView tvDetectResult;
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
