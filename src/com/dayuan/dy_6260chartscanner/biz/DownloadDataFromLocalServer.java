package com.dayuan.dy_6260chartscanner.biz;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xml.sax.InputSource;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.dayuan.dy_6260chartscanner.activity.CheckStandardActivity;
import com.dayuan.dy_6260chartscanner.db.CompanyDao;
import com.dayuan.dy_6260chartscanner.db.Dao;
import com.dayuan.dy_6260chartscanner.db.DataDao;
import com.dayuan.dy_6260chartscanner.db.FoodClassDao;
import com.dayuan.dy_6260chartscanner.db.ReportDataDao;
import com.dayuan.dy_6260chartscanner.db.SelectItemDao;
import com.dayuan.dy_6260chartscanner.db.ServerDao;
import com.dayuan.dy_6260chartscanner.db.TaskClassDao;
import com.dayuan.dy_6260chartscanner.db.VerifyDataDao;
import com.dayuan.dy_6260chartscanner.entity.CheckNumber;
import com.dayuan.dy_6260chartscanner.entity.FoodClass;
import com.dayuan.dy_6260chartscanner.entity.QueryLog;
import com.dayuan.dy_6260chartscanner.entity.Sample;
import com.dayuan.dy_6260chartscanner.entity.Server;
import com.dayuan.dy_6260chartscanner.entity.TaskClass;

public class DownloadDataFromLocalServer {
	protected static final int HANDLER_DOWNLOAD_SUCCESS = 100;
	protected static final int HANDLER_DOWNLOAD_FAIL = 200;
	protected static final int HANDLER_DOWNLOAD_SELECTITEM_SUCCESS = 1;
	protected static final int HANDLER_DOWNLOAD_SELECTITEM_FAIL = 2;
	private static final int HANDLER_DOWNLOAD_TASKCLASS_SUCCESS = 3;
	private static final int HANDLER_DOWNLOAD_TASKCLASS_FAIL = 4;
	protected static final int CANCEL_DOWNLOAD_SELECTITEM = 5;
	protected static final int HANDLER_VERIFYDATA_FAIL = 6;
	protected static final int HANDLER_VERIFYDATA_SUCCESS = 7;
	static String result;
	public static String xmlString;
	static Context mContext;
	static FoodClassDao foodClassDao;
	static String sampleName;
	static FoodClassBiz biz;
	static CompanyDao companyDao;
	static List<FoodClass> foodclasses;
	static VerifyDataDao verifyDataDao;
	static TaskClassDao taskClassDao;
    static SelectItemDao itemDao;
    static ProgressDialog dialog;
    private static List<Sample> listSample;
    private static SampleBiz sampleBiz;
	public static void downloadDataFromLocalServer(Context context,final String address,final String user,final String password) {
		 mContext = context;
		// �����ռ�
		// biz=(FoodClassBiz) new FoodClassBiz(mContext).getFoodClass();
			// �����ռ�
			dialog = new ProgressDialog(mContext);
			dialog.setTitle("���ڽ���ͨѶ����...");
			dialog.setMessage("���Ժ�...");
//			dialog.setButton("ȡ��", new DialogInterface.OnClickListener() {
//					
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						dialog.dismiss();
//						isRunning=false;
//						handler.sendEmptyMessage(CANCEL_DOWNLOAD_SELECTITEM);
//					}
//				});
			dialog.show();
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
		verifyDataDao = new VerifyDataDao(mContext);
		new Thread(new Runnable() {  
            @Override  
            public void run() { 
		try {
			String nameSpace = "http://face.webservice.fsweb.excellence.com/";
			// ���õķ�������
			String methodName = "checkUserConnection";
			// EndPoint
			// String endPoint =
			// "http://192.168.19.148:8080/gsweb/services/DataSyncService?wsdl";
			// String endPoint =
			// "http://192.168.19.148:8080/gsweb/services/DataSyncService";
//			String endPoint = "http://120.24.239.96:8083/nxfda/services/DataSyncService";
			String endPoint = address;
			// SOAP Action
			String soapAction = nameSpace + methodName;

			// ָ��WebService�������ռ�͵��õķ�����
			SoapObject rpc = new SoapObject(nameSpace, methodName);

			// ���������WebService�ӿ���Ҫ�������������mobileCode��userId

			// StringBuffer buf = new StringBuffer();
			rpc.addProperty("in0", user);
			rpc.addProperty("in1", password);
			// rpc.addProperty("in2", "anrccs888");
			// rpc.addProperty("in3", "FoodClass");
			// rpc.addProperty("in4", "2016-01-01");

			// ���ɵ���WebService������SOAP������Ϣ,��ָ��SOAP�İ汾
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);

			envelope.bodyOut = rpc;
			// �����Ƿ���õ���dotNet������WebService
			envelope.dotNet = true;
			envelope.encodingStyle = "UTF-8";
			// �ȼ���envelope.bodyOut = rpc;
			envelope.setOutputSoapObject(rpc);

			HttpTransportSE transport = new HttpTransportSE(endPoint);
			transport.debug = true;
			try {
				// ����WebService
				transport.call(soapAction, envelope);
				Thread.sleep(1000);
			} catch (Exception e) {
				e.printStackTrace();
				Log.w("����WebServiceʧ��", e.getMessage());
				Message message = handler.obtainMessage();  
				message.what=HANDLER_VERIFYDATA_FAIL;
				handler.sendMessage(message);
			}

			// ��ȡ���ص�����
			SoapObject object = (SoapObject) envelope.bodyIn;
			// ��ȡ���صĽ��
			result = object.getProperty(0).toString();
			Log.i("result=", result);
			if(result.isEmpty()||result==null){
				Message message = handler.obtainMessage();  
				message.what=HANDLER_VERIFYDATA_FAIL;
				handler.sendMessage(message);
				return;
			}
			
			JSONObject obj = new JSONObject(result);
			JSONArray js = obj.getJSONArray("Result");
			String mResultCode=js.getJSONObject(0).getString("ResultCode");
			if(mResultCode.equals("1")){
				Message message = handler.obtainMessage();  
				message.what=HANDLER_VERIFYDATA_SUCCESS;
				handler.sendMessage(message);
			}else{
				Message message = handler.obtainMessage();  
				message.what=HANDLER_VERIFYDATA_FAIL;
				handler.sendMessage(message);	
		}
			String pointnum = js.getJSONObject(0).getString("PointNum");
			String pointname = js.getJSONObject(0).getString("PonitName");
			String pointtype = js.getJSONObject(0).getString("PointType");
			String orgnum = js.getJSONObject(0).getString("OrgNum");
			String orgname = js.getJSONObject(0).getString("OrgName");
			String pointNum=verifyDataDao.getPointNum(pointnum);
			if(pointNum==null){
			verifyDataDao.add(pointnum, pointname, pointtype, orgnum, orgname);
			}else if(pointNum.equals(pointnum)){
				verifyDataDao.update(pointNum, pointname, pointtype, orgnum, orgname);
			}
			Log.i("downloadDataFromLocalServer", pointnum + "/" + pointname
					+ "/" + pointtype + "/" + orgnum + "/" + orgname);
			System.out.println(pointnum);
			System.out.println(pointname);
			System.out.println(pointtype);
			System.out.println(orgnum);
			System.out.println(orgname);
		} catch (Exception e) {
			e.printStackTrace();
		}
            }
      }).start(); 
	}

	public static void downloadData(Context mContext) {
		// mContext = context;
		// �����ռ�
		// biz=(FoodClassBiz) new FoodClassBiz(mContext).getFoodClass();
		int number;
		int num;
		foodClassDao = new FoodClassDao(mContext);
		
		try {
			String nameSpace = "http://face.webservice.fsweb.excellence.com/";
			// ���õķ�������
			String methodName = "downLoadDataDriverBySign";
			// EndPoint
			// String endPoint =
			// "http://192.168.19.148:8080/gsweb/services/DataSyncService?wsdl";
			// String endPoint =
			// "http://192.168.19.148:8080/gsweb/services/DataSyncService";
			String endPoint = "http://120.24.239.96:8083/nxfda/services/DataSyncService";
			// SOAP Action
			String soapAction = nameSpace + methodName;

			// ָ��WebService�������ռ�͵��õķ�����
			SoapObject rpc = new SoapObject(nameSpace, methodName);

			// ���������WebService�ӿ���Ҫ�������������mobileCode��userId

			// StringBuffer buf = new StringBuffer();
			rpc.addProperty("in0", "��ҵ��");
			rpc.addProperty("in1", "yczzq");
			rpc.addProperty("in2", "yczzq888");
			rpc.addProperty("in3", "FoodClass");
			rpc.addProperty("in4", "2016-01-01");

			// ���ɵ���WebService������SOAP������Ϣ,��ָ��SOAP�İ汾
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);

			envelope.bodyOut = rpc;
			// �����Ƿ���õ���dotNet������WebService
			envelope.dotNet = true;
			envelope.encodingStyle = "UTF-8";
			// �ȼ���envelope.bodyOut = rpc;
			envelope.setOutputSoapObject(rpc);

			HttpTransportSE transport = new HttpTransportSE(endPoint);
			transport.debug = true;
			try {
				// ����WebService
				transport.call(soapAction, envelope);
				Thread.sleep(1000);
			} catch (Exception e) {
				e.printStackTrace();
				Log.w("����WebServiceʧ��", e.getMessage());
			}

			// ��ȡ���ص�����
			SoapObject object = (SoapObject) envelope.bodyIn;
			// ��ȡ���صĽ��
			result = object.getProperty(0).toString();
			Log.i("result=", result);
			result = result.replaceAll("\r\n", "");
			SAXBuilder buider = new SAXBuilder();

			java.io.StringReader read = new java.io.StringReader(result);
			InputSource source = new InputSource(read);
			Document doc = buider.build(source);

			Element rootElement = doc.getRootElement();
			List list = rootElement.getChildren();
			// for (int j = 0; j < foodclasses.size(); j++) {
			// String sysCode=foodclasses.get(j).getSysCode();

			for (int i = 0; i < list.size(); i++) {
				Element current = null;
				try {
					current = (Element) list.get(i);
					sampleName = current.getChildText("Name");
					String syscode = current.getChildText("SysCode");
					String std = current.getChildText("StdCode");
					String shortCut = current.getChildText("ShortCut");
					String checklevel = current.getChildText("CheckLevel");
					String checkitemcodes = current
							.getChildText("CheckItemCodes");
					String checkitmevalue = current
							.getChildText("CheckItemValue");
					// holder.tvName.setText(project.getName()+"("+((Integer.parseInt(project.getMethod())
					// < 2)?"����":"����") +")");
					String islock = current.getChildText("IsLock");
					if (islock.equals("true")) {
						number = 1;
					} else {
						number = 0;
					}
					String isreadonly = current.getChildText("IsReadOnly");
					if (isreadonly.equals("true")) {
						num = 1;
					} else {
						num = 0;
					}
					String udate = current.getChildText("UDate");
					if (udate == null) {
						udate = " ";
					}
					String remark = current.getChildText("Remark");
					// if(sysCode!=null){
					// foodClassDao.update(sysCode, sampleName);
					// }else{
					String sysCode=foodClassDao.getSysCode(syscode);
					if(sysCode==null){
					foodClassDao.add(syscode, std, sampleName, shortCut,
							checklevel, checkitemcodes, checkitmevalue, number,
							num, udate, remark);
					}else if(sysCode.equals(syscode)){
						foodClassDao.update(sysCode, std, sampleName, shortCut,
								checklevel, checkitemcodes, checkitmevalue, number,
								num, udate, remark);
					}
					// }
					System.out.println("Name=" + current.getChildText("Name"));
					System.out.println("StdCode=" + std);
					System.out.println("ShortCut=" + shortCut);
					System.out.println("checklevel=" + checklevel);
					System.out.println("checkitemcodes=" + checkitemcodes);
					System.out.println("checkitmevalue=" + checkitmevalue);
					System.out.println("number=" + number);
					System.out.println("num=" + num);
					System.out.println("udate=" + udate);
					System.out.println("remark=" + remark);
					// System.out.println("sysCode="+syscode);
				} catch (Exception e) {
					e.printStackTrace();
				}
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void downloadCheckedUnit(Context mContext) {
		// mContext = context;
		// �����ռ�
		int number;
		int num;
		companyDao = new CompanyDao(mContext);
		try {
			String nameSpace = "http://face.webservice.fsweb.excellence.com/";
			// ���õķ�������
			String methodName = "downLoadDataDriverBySign";
			String endPoint = "http://120.24.239.96:8083/nxfda/services/DataSyncService";
			// SOAP Action
			String soapAction = nameSpace + methodName;
			// ָ��WebService�������ռ�͵��õķ�����
			SoapObject rpc = new SoapObject(nameSpace, methodName);

			// ���������WebService�ӿ���Ҫ�������������mobileCode��userId
			// StringBuffer buf = new StringBuffer();
			rpc.addProperty("in0", "��ҵ��");
			rpc.addProperty("in1", "yczzq");
			rpc.addProperty("in2", "yczzq888");
			rpc.addProperty("in3", "Company");
			rpc.addProperty("in4", "2016-01-01");

			// ���ɵ���WebService������SOAP������Ϣ,��ָ��SOAP�İ汾
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);

			envelope.bodyOut = rpc;
			// �����Ƿ���õ���dotNet������WebService
			envelope.dotNet = true;
			envelope.encodingStyle = "UTF-8";
			// �ȼ���envelope.bodyOut = rpc;
			envelope.setOutputSoapObject(rpc);

			HttpTransportSE transport = new HttpTransportSE(endPoint);
			transport.debug = true;
			try {
				// ����WebService
				transport.call(soapAction, envelope);
				Thread.sleep(1000);
			} catch (Exception e) {
				e.printStackTrace();
				Log.w("����WebServiceʧ��", e.getMessage());
			}

			// ��ȡ���ص�����
			SoapObject object = (SoapObject) envelope.bodyIn;
			// ��ȡ���صĽ��
			String mResult = object.getProperty(0).toString();
			Log.i("mResult=", mResult);
			mResult = mResult.replaceAll("\r\n", "");
			SAXBuilder buider = new SAXBuilder();

			java.io.StringReader read = new java.io.StringReader(mResult);
			InputSource source = new InputSource(read);
			Document doc = buider.build(source);

			Element rootElement = doc.getRootElement();
			List list = rootElement.getChildren();
			for (int i = 0; i < list.size(); i++) {
				Element current = null;
				try {
					current = (Element) list.get(i);
					String syscode = current.getChildText("SysCode");
					String license = current.getChildText("StdCode");
					String companyid = current.getChildText("CompanyID");
					String checkedUnit = current.getChildText("FullName");
					String displayname = current.getChildText("DisplayName");
					String property = current.getChildText("Property");
					String kindcode = current.getChildText("KindCode");
					String regcapital = current.getChildText("RegCapital");
					String unit = current.getChildText("Unit");
					String incorporator = current.getChildText("Incorporator");
					String regdate = current.getChildText("RegDate");
					String districtcode = current.getChildText("DistrictCode");
					String postcode = current.getChildText("PostCode");
					String address = current.getChildText("Address");
					String linkman = current.getChildText("LinkMan");
					String linkinfo = current.getChildText("LinkInfo");
					String creditlevel = current.getChildText("CreditLevel");
					String creditrecord = current.getChildText("CreditRecord");
					String productinfo = current.getChildText("ProductInfo");
					String otherinfo = current.getChildText("OtherInfo");
					String checklevel = current.getChildText("CheckLevel");
					String foodsaferecord = current
							.getChildText("FoodSafeRecord");
					String islock = current.getChildText("IsLock");
					if (islock.equals("true")) {
						number = 1;
					} else {
						number = 0;
					}
					String isreadonly = current.getChildText("IsReadOnly");
					if (isreadonly.equals("true")) {
						num = 1;
					} else {
						num = 0;
					}
					String udate = current.getChildText("UDate");
					if (udate == null) {
						udate = " ";
					}
					String remark = current.getChildText("Remark");
					String sign = current.getChildText("Sign");
					String sysCode=companyDao.getSysCode(syscode);
					if(sysCode==null){
					companyDao.add(syscode, license, companyid, checkedUnit,
							displayname, property, kindcode, regcapital, unit,
							incorporator, regdate, districtcode, postcode,
							address, linkman, linkinfo, creditlevel,
							creditrecord, productinfo, otherinfo, checklevel,
							foodsaferecord, number, num, udate, remark, sign);
					}else if(sysCode.equals(syscode)){
						companyDao.update(sysCode, license, companyid, checkedUnit,
								displayname, property, kindcode, regcapital, unit,
								incorporator, regdate, districtcode, postcode,
								address, linkman, linkinfo, creditlevel,
								creditrecord, productinfo, otherinfo, checklevel,
								foodsaferecord, number, num, udate, remark, sign);
					}
					System.out.println("syscode=" + syscode);
					System.out.println("license=" + license);
					System.out.println("checkedUnit=" + checkedUnit);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
//	protected static boolean isRunning=true;
	private static String sampleNumber;
	public static void downloadCheckItem(final Context context,final String address,final String user,final String password) {
//		isRunning=true;
		 mContext = context;
		 sampleBiz=new SampleBiz(mContext);
		 listSample=sampleBiz.getSample();
		// �����ռ�
		 Log.i("samplebiz", listSample.toString());
		 System.out.println(listSample);
		itemDao=new SelectItemDao(mContext);
		dialog = new ProgressDialog(mContext);
		dialog.setTitle("����������������...");
		dialog.setMessage("���Ժ�...");
//        dialog.setButton("ȡ��", new DialogInterface.OnClickListener() {
//			
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				dialog.dismiss();
//				
//				handler.sendEmptyMessage(CANCEL_DOWNLOAD_SELECTITEM);
//			}
//		});
		dialog.show();
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
		
		 new Thread(new Runnable() {  
	            @Override  
	            public void run() { 
//	            	while(isRunning){
		// �����ռ�
		try {
			String nameSpace = "http://face.webservice.fsweb.excellence.com/";
			// ���õķ�������
			String methodName = "downLoadDataDriverBySign";
			String endPoint=address;
			// EndPoint
			// String endPoint =
			// "http://192.168.19.148:8080/gsweb/services/DataSyncService?wsdl";
			// String endPoint =
			// "http://192.168.19.148:8080/gsweb/services/DataSyncService";
//			String endPoint = "http://120.24.239.96:8083/nxfda/services/DataSyncService";
			// SOAP Action
			String soapAction = nameSpace + methodName;

			// ָ��WebService�������ռ�͵��õķ�����
			SoapObject rpc = new SoapObject(nameSpace, methodName);

			// ���������WebService�ӿ���Ҫ�������������mobileCode��userId

			// StringBuffer buf = new StringBuffer();
			String udate="";
			rpc.addProperty("in0", "��ҵ��");
			rpc.addProperty("in1", user);
			rpc.addProperty("in2", password);
//			rpc.addProperty("in1", "yczzq");
//			rpc.addProperty("in2", "yczzq888");
			rpc.addProperty("in3", "SELECTITME");
			rpc.addProperty("in4", udate);

			// ���ɵ���WebService������SOAP������Ϣ,��ָ��SOAP�İ汾
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);

			envelope.bodyOut = rpc;
			// �����Ƿ���õ���dotNet������WebService
			envelope.dotNet = true;
			envelope.encodingStyle = "UTF-8";
			// �ȼ���envelope.bodyOut = rpc;
			envelope.setOutputSoapObject(rpc);

			HttpTransportSE transport = new HttpTransportSE(endPoint);
			transport.debug = true;
			try {
				// ����WebService
				transport.call(soapAction, envelope);
				Thread.sleep(500);
			} catch (Exception e) {
				e.printStackTrace();
				Log.w("����WebServiceʧ��", e.getMessage());
				Message message = handler.obtainMessage();  
				message.what=HANDLER_DOWNLOAD_SELECTITEM_FAIL;
				handler.sendMessage(message);
				return;
			}

			// ��ȡ���ص�����
			SoapObject object = (SoapObject) envelope.bodyIn;
			// ��ȡ���صĽ��
			result = object.getProperty(0).toString();
			Log.i("result=", result);
			if(result.isEmpty()||result==null){
				
				Message message = handler.obtainMessage();  
				message.what=HANDLER_DOWNLOAD_SELECTITEM_FAIL;
				handler.sendMessage(message);
				return;
			}
				
			result = result.replaceAll("\r\n", "");
			
			SAXBuilder buider = new SAXBuilder();

			java.io.StringReader read = new java.io.StringReader(result);
			InputSource source = new InputSource(read);
			Document doc = buider.build(source);

			Element rootElement = doc.getRootElement();
			List list = rootElement.getChildren();
			for (int i = 0; i < list.size(); i++) {
				Element current = null;
				
				try {
					current = (Element) list.get(i);
					
					String sampleName = current.getChildText("FtypeNmae");
					String name = current.getChildText("Name");
					
					
					String sampleNum = current.getChildText("SampleNum");
					String itemDes = current.getChildText("ItemDes");
					String standardValue = current.getChildText("StandardValue");
					String demarcate = current
							.getChildText("Demarcate");
					String unit = current
							.getChildText("Unit");
				    sampleNumber=itemDao.getSampleNum(sampleNum);
				    boolean sign=false;
				    if(listSample.size()>0){
					    for(int j=0;j<listSample.size();j++){
							Sample sample = listSample.get(j);
							Log.d("itemName", sample.getName());
							if((sample.getName()).equals(name)){
								sign=true;
							}
						}
				    }
					
						if(sampleNumber==null){
							if(sign){
							itemDao.add(sampleName, sampleNum, name, itemDes, standardValue, demarcate, unit);
							}
						}else{
							if(sign){
								itemDao.update(sampleName, sampleNumber, name, itemDes, standardValue, demarcate, unit);
							}
								
						}
					
//					System.out.println("sampleName=" + sampleName);
//					System.out.println("sampleNum=" + sampleNum);
//					System.out.println("name=" + name);
//					System.out.println("itemDes=" + itemDes);
//					System.out.println("standardValue=" +standardValue);
//					System.out.println("demarcate=" + demarcate);
//					System.out.println("unit=" + unit);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
//			if(sampleNumber!=null){
			Message message = handler.obtainMessage();  
			message.obj = result; 
			message.what=HANDLER_DOWNLOAD_SELECTITEM_SUCCESS;
			handler.sendMessage(message);
//			}else{
			
//				Message message = handler.obtainMessage();  
//	            message.what=HANDLER_DOWNLOAD_SELECTITEM_FAIL;
//	            handler.sendMessage(message);	
			//Thread.sleep(1000);
		} catch (Exception e) {
			e.printStackTrace();
//			Message message = handler.obtainMessage();  
//            message.what=HANDLER_DOWNLOAD_SELECTITEM_FAIL;
//            handler.sendMessage(message);
		}
	            	}
//	            }
	            
		 }).start();  
	}
   //static List<TaskClass> taskclasses;
   static String checkNumber;
   
	public static void downloadTaskClass(Context context,final String address,final String user,final String password) {
//		isRunning=true;
		 mContext = context;
		// �����ռ�
		taskClassDao=new TaskClassDao(mContext);
		final ReportDataDao reportDao=new ReportDataDao(mContext);
		dialog = new ProgressDialog(mContext);
		dialog.setTitle("����������������...");
		dialog.setMessage("���Ժ�...");
//		dialog.setButton("ȡ��", new DialogInterface.OnClickListener() {
//				
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//					dialog.dismiss();
//					isRunning=false;
//					handler.sendEmptyMessage(CANCEL_DOWNLOAD_SELECTITEM);
//				}
//			});
		dialog.show();
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
		new Thread(new Runnable() {  
            @Override  
            public void run() { 
//            	while(isRunning){
		try {
			String nameSpace = "http://face.webservice.fsweb.excellence.com/";
			// ���õķ�������
			String methodName = "downLoadDataDriverBySign";
//			String endPoint = "http://120.24.239.96:8083/nxfda/services/DataSyncService";
			String endPoint=address;
			// SOAP Action
			String soapAction = nameSpace + methodName;
			// ָ��WebService�������ռ�͵��õķ�����
			SoapObject rpc = new SoapObject(nameSpace, methodName);

			// ���������WebService�ӿ���Ҫ�������������mobileCode��userId
			// StringBuffer buf = new StringBuffer();
			rpc.addProperty("in0", "��ҵ��");
			rpc.addProperty("in1", user);
			rpc.addProperty("in2", password);
//			rpc.addProperty("in1", "yczzq");
//			rpc.addProperty("in2", "yczzq888");
			rpc.addProperty("in3", "CheckPlan");
			rpc.addProperty("in4", "2016-01-01");

			// ���ɵ���WebService������SOAP������Ϣ,��ָ��SOAP�İ汾
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);

			envelope.bodyOut = rpc;
			// �����Ƿ���õ���dotNet������WebService
			envelope.dotNet = true;
			envelope.encodingStyle = "UTF-8";
			// �ȼ���envelope.bodyOut = rpc;
			envelope.setOutputSoapObject(rpc);

			HttpTransportSE transport = new HttpTransportSE(endPoint);
			transport.debug = true;
			try {
				// ����WebService
				transport.call(soapAction, envelope);
				Thread.sleep(1000);
			} catch (Exception e) {
				e.printStackTrace();
				Log.w("����WebServiceʧ��", e.getMessage());
				Message message = handler.obtainMessage();  
				message.what=HANDLER_DOWNLOAD_TASKCLASS_FAIL;
				handler.sendMessage(message);	
				return;
			}

			// ��ȡ���ص�����
			SoapObject object = (SoapObject) envelope.bodyIn;
			// ��ȡ���صĽ��
			String nResult = object.getProperty(0).toString();
			Log.i("nResult=", nResult);
			if(nResult.isEmpty()||nResult==null){
				Message message = handler.obtainMessage();  
				message.what=HANDLER_DOWNLOAD_TASKCLASS_FAIL;
				handler.sendMessage(message);
				return;
			}
			nResult = nResult.replaceAll("\r\n", "");
			SAXBuilder buider = new SAXBuilder();

			java.io.StringReader read = new java.io.StringReader(nResult);
			InputSource source = new InputSource(read);
			Document doc = buider.build(source);

			Element rootElement = doc.getRootElement();
			List list = rootElement.getChildren();
			
			for (int i = 0; i < list.size(); i++) {
				Element current = null;
				try {
					current = (Element) list.get(i);
					String CPCODE = current.getChildText("CPCODE");
					String CPTITLE = current.getChildText("CPTITLE");
					String CPSDATE = current.getChildText("CPSDATE");
					String CPEDATE = current.getChildText("CPEDATE");
					String CPTPROPERTY = current.getChildText("CPTPROPERTY");
					String CPFROM = current.getChildText("CPFROM");
					String CPEDITOR = current.getChildText("CPEDITOR");
					String CPPORGID = current.getChildText("CPPORGID");
					String CPPORG = current.getChildText("CPPORG");
					String CPEDDATE = current.getChildText("CPEDDATE");
					String CPMEMO = current.getChildText("CPMEMO");
					String PLANDETAIL = current.getChildText("PLANDETAIL");
					String PLANDCOUNT = current.getChildText("PLANDCOUNT");
					String BAOJINGTIME = current.getChildText("BAOJINGTIME");
					String UDATE = current.getChildText("UDATE");
					checkNumber=taskClassDao.getCheckNumber(CPCODE);
					if(checkNumber==null){
					 taskClassDao.add(CPCODE, CPTITLE, CPSDATE, CPEDATE, CPTPROPERTY, CPFROM,
							 CPEDITOR, CPPORGID, CPPORG, CPEDDATE, CPMEMO, 
							 PLANDETAIL, Integer.valueOf(PLANDCOUNT), BAOJINGTIME, UDATE);
					}else{
						if(checkNumber.equals(CPCODE)){
							taskClassDao.update(checkNumber, CPTITLE, CPSDATE, CPEDATE, CPTPROPERTY,
									CPFROM, CPEDITOR, CPPORGID, CPPORG, CPEDDATE, CPMEMO,
									PLANDETAIL, Integer.valueOf(PLANDCOUNT), BAOJINGTIME, UDATE);
						}else{
							taskClassDao.add(CPCODE, CPTITLE, CPSDATE, CPEDATE, CPTPROPERTY, CPFROM,
									 CPEDITOR, CPPORGID, CPPORG, CPEDDATE, CPMEMO, 
									 PLANDETAIL, Integer.valueOf(PLANDCOUNT), BAOJINGTIME, UDATE);
						}
					}
					reportDao.deleteAll();
					reportDao.add(CPCODE, CPSDATE, CPTPROPERTY, CPFROM, CPPORG, CPEDDATE, PLANDETAIL, Integer.valueOf(PLANDCOUNT));
//					System.out.println("CPCODE=" + CPCODE);
//					System.out.println("CPTITLE=" + CPTITLE);
//					System.out.println("CPSDATE=" + CPSDATE);
//					System.out.println("CPEDATE=" + CPEDATE);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
			Message message = handler.obtainMessage();  
			message.obj = result; 
			message.what=HANDLER_DOWNLOAD_TASKCLASS_SUCCESS;
			handler.sendMessage(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
            	}
//            }
		 }).start(); 
	}
	private static Handler handler = new Handler(){  
        public void handleMessage(android.os.Message msg) {  
            // ��WebService�õ��Ľ�����ظ�TextView  
        	switch (msg.what) {
			case HANDLER_DOWNLOAD_SELECTITEM_SUCCESS:
				Toast.makeText(mContext, "���سɹ�", 0).show();
				dialog.dismiss();
				break;

			case HANDLER_DOWNLOAD_SELECTITEM_FAIL:
				Toast.makeText(mContext, "����ʧ�ܣ����������", 0).show();
				dialog.dismiss();
				break;
			case HANDLER_DOWNLOAD_TASKCLASS_SUCCESS:
				Toast.makeText(mContext, "���سɹ�", 0).show();
				dialog.dismiss();
				
				break;
				
			case HANDLER_DOWNLOAD_TASKCLASS_FAIL:
				Toast.makeText(mContext, "����ʧ�ܣ����������", 0).show();
				dialog.dismiss();
				break;
			case CANCEL_DOWNLOAD_SELECTITEM:
				Toast.makeText(mContext, "����ȡ��", 0).show();
				break;
			case HANDLER_VERIFYDATA_SUCCESS:
				Toast.makeText(mContext, "���Գɹ�", 0).show();
				dialog.dismiss();
				break;
			case HANDLER_VERIFYDATA_FAIL:
				Toast.makeText(mContext, "����ʧ�ܣ�������������ַ", 0).show();
				dialog.dismiss();
				
				break;
			} 
        };  
    };
    
	/**
	 * MD5��д
	 * 
	 * @author xuanaiwu
	 * @param s
	 * @return String
	 */
	private static final String MD5(String s) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'A', 'B', 'C', 'D', 'E', 'F' };
		char str[] = null;
		byte strTemp[] = s.getBytes();
		MessageDigest mdTemp;
		try {
			mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(strTemp);
			byte md[] = mdTemp.digest();
			int j = md.length;
			str = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte b = md[i];
				str[k++] = hexDigits[b >> 4 & 0xf];
				str[k++] = hexDigits[b & 0xf];
			}
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new String(str);
	}


}
