package com.dayuan.dy_6260chartscanner.biz;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import com.dayuan.dy_6260chartscanner.activity.QueryLogActivity;
import com.dayuan.dy_6260chartscanner.util.ShowTime;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class ExportData {

	/**
	 * ����
	 * 
	 * @param file
	 *            csv�ļ�(·��+�ļ���)��csv�ļ������ڻ��Զ�����
	 * @param dataList
	 *            ����
	 * @return
	 */
	public static void exportCsv(Context context, List<String> dataList,
			String name) {
		
		String folderName = null;
		String path = "/mnt/usbhost/";
		String path02 = "/mnt/extsd/";
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {

			File fileRobo = new File(path);
			File fileSD = new File(path02);
			if (fileRobo.isDirectory() || fileSD.isDirectory()) {
				String[] files = fileRobo.list();
				String[] files02 = fileSD.list();
				if (files != null && files.length > 0) {
					folderName = path + "dayuan/";
					File filename = new File(folderName);
					if (!filename.exists()) {
						filename.mkdir();
					}
					String fileName = folderName + name;
					FileOutputStream out = null;
					OutputStreamWriter osw = null;
					BufferedWriter bw = null;
					try {

						File file = new File(fileName);
						if (file.exists()) {
							file.delete();
						}
						file = null;
						File file2 = new File(fileName);

						out = new FileOutputStream(file2, true);
						osw = new OutputStreamWriter(out, "GBK");
						bw = new BufferedWriter(osw);
						if (dataList != null && !dataList.isEmpty()) {
							if (name.equals("DY6260_rec.csv")) {
								for (int i = 0; i < dataList.size(); i++) {
									bw.write(dataList.get(i) + ",");
									if (i % 6 == 5) {
										bw.newLine();
									}
								}
							} else if (name.equals("DY6260_items.csv")) {
								for (int i = 0; i < dataList.size(); i++) {
									bw.write(dataList.get(i) + ",");
									if (i % 12 == 11) {
										bw.newLine();
									}
								}
							}
						}

						Toast.makeText(context, "�����ɹ�", 0).show();
					} catch (Exception e) {
					} finally {
						if (bw != null) {
							try {
								bw.close();
								bw = null;
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						if (osw != null) {
							try {
								osw.close();
								osw = null;
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						if (out != null) {
							try {
								out.close();
								out = null;
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						dataList.clear();
					}
				} else if (files02 != null && files02.length > 0) {
					folderName = path02 + "dayuan/";
					File filename = new File(folderName);
					if (!filename.exists()) {
						filename.mkdir();
					}
					String fileName = folderName + name;
					FileOutputStream out = null;
					OutputStreamWriter osw = null;
					BufferedWriter bw = null;
					try {
						File file = new File(fileName);
						if (file.exists()) {
							file.delete();
							file.createNewFile();
						}
						out = new FileOutputStream(file);
						osw = new OutputStreamWriter(out, "GBK");
						bw = new BufferedWriter(osw);

						if (dataList != null && !dataList.isEmpty()) {
							if (name.equals("querylog.csv")) {
								for (int i = 0; i < dataList.size(); i++) {
									bw.write(dataList.get(i) + ",");
									if (i % 6 == 5) {
										bw.newLine();
									}
								}
							} else if (name.equals("project.csv")) {
								for (int i = 0; i < dataList.size(); i++) {
									bw.write(dataList.get(i) + ",");
									if (i % 12 == 11) {
										bw.newLine();
									}
								}
							}

						}
						Toast.makeText(context, "�����ɹ�", 0).show();
					} catch (Exception e) {
					} finally {
						if (bw != null) {
							try {
								bw.close();
								bw = null;
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						if (osw != null) {
							try {
								osw.close();
								osw = null;
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						if (out != null) {
							try {
								out.close();
								out = null;
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						dataList.clear();
					}
				} else {
					Toast.makeText(context, "�����U�̻�SD��", 0).show();
				}
			} 
		}
	}
}