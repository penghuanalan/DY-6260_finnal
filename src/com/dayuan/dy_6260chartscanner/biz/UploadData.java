package com.dayuan.dy_6260chartscanner.biz;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlSerializer;

import com.dayuan.dy_6260chartscanner.activity.QueryLogActivity;
import com.dayuan.dy_6260chartscanner.entity.QueryLog;
import com.dayuan.dy_6260chartscanner.entity.VerifyData;
import com.dayuan.dy_6260chartscanner.util.StreamTool;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Xml;
import android.widget.Toast;

public class UploadData {
	protected static final int HANDLER_UPLOAD_SUCCESS = 100;
	protected static final int HANDLER_UPLOAD_FAIL =200;
	protected static final int HANDLER_UPLOAD_CHECK_SUCCESS = 300;
	protected static final int HANDLER_UPLOAD_CHECK_FAIL = 400;
	static String result ;
	public static String xmlString;
    static Context mContext;
    public static String mResult;
    static String pointNum;
    static String pointName;
    static String pointType;
    static String orgNum;
    static String orgName;
    static DecimalFormat f = new DecimalFormat("00000");//数字转换格式
    
//	public  UploadData(final Context context) {
//		super();
//		this.context = context;
//	}
    static String now;
	public static void uploadData(final Context context,final QueryLog log,
			final String address,final String user,final String password,final List<VerifyData> verifyData)  {
		mContext = context;
		for(VerifyData data:verifyData){
			pointNum=data.getPointnum();
			pointName=data.getPonitname();
			pointType=data.getPointtype();
			orgNum=data.getOrgnum();
			orgName=data.getOrgname();
		}
		new Thread(new Runnable() {  
            
            @Override  
            public void run() {  
		// 命名空间
				String nameSpace = "http://face.webservice.fsweb.excellence.com/";
				// 调用的方法名称
				String methodName = "SetDataDriver";
				// EndPoint
//				String endPoint = "http://192.168.19.148:8080/gsweb/services/DataSyncService?wsdl";
//				String endPoint = "http://58.67.148.169:6091/fdawebnew/services/DataSyncService";
//				String endPoint = "http://192.168.19.148:8080/gsweb/services/DataSyncService";
//				String endPoint = "http://120.24.239.96:8083/nxfda/services/DataSyncService";
//				String endPoint = "192.168.19.148:8080/fdawebnew/services/DataSyncService";
				String endPoint =address;
				// SOAP Action
				String soapAction =nameSpace+methodName;

				// 指定WebService的命名空间和调用的方法名
				SoapObject rpc = new SoapObject(nameSpace, methodName);

				// 设置需调用WebService接口需要传入的两个参数mobileCode、userId
				
				String mNumber=f.format(log.getNumber());
				String foodName=log.getSamplename().toString();
				String mDate=log.getDate().toString();
				String projectName=log.getName().toString();
				String mResult=log.getResult().toString();
				Date date;
				try {
					date = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss").parse(mDate);
					now = new SimpleDateFormat("yyyyMMddHHmmss").format(date);//将字符串时间转为数字格式
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				String checkNum=now+mNumber;
				StringBuffer buf = new StringBuffer();
				buf.append("<UpdateResult>\r\n");
				buf.append("<Result>\r\n");
				buf.append("<ResultType>胶体金</ResultType>\r\n");//检测手段
				buf.append("<CheckNo>"+checkNum+"</CheckNo>\r\n");//检测编号 
				buf.append("<SampleCode>"+mNumber+"</SampleCode>\r\n");//样品编号SampleCode   
//				buf.append("<OrCheckNo>"+pointNum+"</OrCheckNo>\r\n");// 原检测编号
				buf.append("<CheckPlaceCode>"+pointNum+"</CheckPlaceCode>\r\n");
				buf.append("<CheckPlace>"+orgName+"</CheckPlace>\r\n");//这个值必须是系统的组织架构之一//餐饮单位所属行政机构名称
				buf.append("<FoodName>"+foodName+"</FoodName>\r\n");//被检食品名称
				buf.append("<TakeDate>"+mDate+"</TakeDate>\r\n");//抽检日期 
				buf.append("<CheckStartDate>"+mDate+"</CheckStartDate>\r\n");// 送检日期/检测时间  
				buf.append("<Standard>GB/DY6260-2016</Standard>\r\n");//检测依据 
				buf.append("<CheckMachine>DY-6260胶体金读卡仪</CheckMachine>\r\n");//检测设备 
				buf.append("<CheckMachineModel>DY6260</CheckMachineModel>\r\n");
				buf.append("<MachineCompany>广州达元食品安全技术有限公司</MachineCompany>\r\n");
				buf.append("<CheckTotalItem>"+projectName+"</CheckTotalItem>\r\n");// 检测项目
				buf.append("<CheckValueInfo>"+mResult+"</CheckValueInfo>\r\n");//	检测值   
				buf.append("<StandValue>200</StandValue>\r\n");// 检测标准值
				buf.append("<Result>合格</Result>\r\n");//	检测结论
				buf.append("<ResultInfo>RLU</ResultInfo>\r\n");//	检测值单位  
				buf.append("<CheckUnitName>"+pointName+"</CheckUnitName\r\n>");//检测单位 
				buf.append("<CheckUnitInfo>dxhlcs</CheckUnitInfo>\r\n");//检测人姓名  
				buf.append("<Organizer>"+user+"</Organizer>\r\n");// 抽样人
				buf.append("<UpLoader>"+user+"</UpLoader>\r\n");//录入人
				buf.append("<ReportDeliTime>"+mDate+"</ReportDeliTime>\r\n");// 检测报告送达时间
				buf.append("<IsReconsider>false</IsReconsider>\r\n");//是否已经发送
				buf.append("<ReconsiderTime>"+mDate+"</ReconsiderTime>\r\n");// 提出复议时间
				buf.append("<ProceResults>合格</ProceResults>\r\n");//处理结果
				buf.append("<CheckCompanyCode>"+pointNum+"</CheckCompanyCode>\r\n");
				buf.append("<CheckCompany>"+pointName+"</CheckCompany>\r\n");
				buf.append("<CheckMethod>标准曲线法</CheckMethod>\r\n");
				buf.append("<APRACategory>"+pointType+"</APRACategory>\r\n");
				buf.append("<Hole>02</Hole>\r\n");
				buf.append("<CheckType>抽检</CheckType>\r\n");//检测类型
				buf.append("</Result>\r\n");
				buf.append("</UpdateResult>\r\n");
				
				rpc.addProperty("in0",buf.toString());
				
//				rpc.addProperty("in1", "check");
//			    rpc.addProperty("in2", MD5("888888"));
//			    rpc.addProperty("in1", "yczzq");
//			    rpc.addProperty("in2", MD5("yczzq888"));
				rpc.addProperty("in1",user);
				
				rpc.addProperty("in2",MD5(password));
//				rpc.addProperty("in1","anrccs");
//				rpc.addProperty("in2",MD5("anrccs888"));
				rpc.addProperty("in3","");
				rpc.addProperty("in4",3);
//				

				// 生成调用WebService方法的SOAP请求信息,并指定SOAP的版本
				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
						SoapEnvelope.VER11);

				envelope.bodyOut = rpc;
				// 设置是否调用的是dotNet开发的WebService
				envelope.dotNet = true;
				envelope.encodingStyle="UTF-8";
				// 等价于envelope.bodyOut = rpc;
				envelope.setOutputSoapObject(rpc);

				HttpTransportSE transport = new HttpTransportSE(endPoint);
				transport.debug=true;
				try {
					// 调用WebService
					transport.call(soapAction, envelope);
					Thread.sleep(1000);
				} catch (Exception e) {
					e.printStackTrace();
					Log.w("调用WebService失败", e.getMessage());
					Message message = handler.obtainMessage();  
	                message.what=HANDLER_UPLOAD_FAIL;
	                handler.sendMessage(message);	
				}

				// 获取返回的数据
				SoapObject object = (SoapObject) envelope.bodyIn;
				// 获取返回的结果
				if(object!=null){
				 result = object.getProperty(0).toString();
				 Log.i("result=", result);
				}
				if(result!=null&&result.equals("1")){
				Message message = handler.obtainMessage();  
                message.obj = result; 
                message.what=HANDLER_UPLOAD_SUCCESS;
                handler.sendMessage(message);
				}else{
					Message message = handler.obtainMessage();  
	                message.what=HANDLER_UPLOAD_FAIL;
	                handler.sendMessage(message);	
				}
                
            }
            }).start();  
	}
	public static void uploadCheckData(final Context context,final String number,
			final String samplename,final String projectName,final String result,
			final String datetime,final String density
			)  {
		mContext = context;
		
		new Thread(new Runnable() {  
			
			@Override  
			public void run() {  
				// 命名空间
				String nameSpace = "http://face.webservice.fsweb.excellence.com/";
				// 调用的方法名称
				String methodName = "SetDataDriver";
				// EndPoint
//				String endPoint = "http://192.168.19.148:8080/gsweb/services/DataSyncService?wsdl";
//				String endPoint = "http://58.67.148.169:6091/fdawebnew/services/DataSyncService";
				// SOAP Action
//				String endPoint = "http://120.24.239.96:8083/nxfda/services/DataSyncService";
				String endPoint = "http://58.67.148.169:6091/fdawebnew/services/DataSyncService";
				String soapAction =nameSpace+methodName;
				
				// 指定WebService的命名空间和调用的方法名
				SoapObject rpc = new SoapObject(nameSpace, methodName);
				
				// 设置需调用WebService接口需要传入的两个参数mobileCode、userId
				
				StringBuffer buf = new StringBuffer();
				
				buf.append("<UpdateResult>\r\n");
				buf.append("<Result>\r\n");
				buf.append("<ResultType>胶体金手动检测</ResultType>\r\n");
				buf.append("<CheckNo>"+number+"</CheckNo>\r\n");
				buf.append("<SampleCode>"+number+"</SampleCode>\r\n");
				buf.append("<OrCheckNo>001002</OrCheckNo>\r\n");
				buf.append("<CheckPlace>城区食品药品监督管理局</CheckPlace>\r\n");//这个值必须是系统的组织架构之一
				buf.append("<FoodName>"+samplename+"</FoodName>\r\n");
				buf.append("<TakeDate>"+datetime+"</TakeDate>\r\n");
				buf.append("<CheckStartDate>"+datetime+"</CheckStartDate>\r\n");
				buf.append("<Standard>GB/DY6260-2016</Standard>\r\n");
				buf.append("<CheckMachine>DY-6260胶体金读卡仪</CheckMachine>\r\n");
				buf.append("<CheckMachineModel>DY6260</CheckMachineModel>\r\n");
				buf.append("<MachineCompany>广州达元食品安全技术有限公司</MachineCompany>\r\n");
				buf.append("<CheckTotalItem>"+projectName+"</CheckTotalItem>\r\n");
				buf.append("<CheckValueInfo>22</CheckValueInfo>\r\n");
				buf.append("<StandValue>200</StandValue>\r\n");
				buf.append("<Result>"+result+"</Result>\r\n");
				buf.append("<ResultInfo>RLU</ResultInfo>\r\n");
				buf.append("<CheckUnitName>城区食品药品监督管理局</CheckUnitName\r\n>");
				buf.append("<CheckUnitInfo>dxhlcs</CheckUnitInfo>\r\n");
				buf.append("<Organizer>dxhlcs</Organizer>\r\n");
				buf.append("<UpLoader>dxhlcs</UpLoader>\r\n");
				buf.append("<ReportDeliTime>"+datetime+"</ReportDeliTime>\r\n");
				buf.append("<IsReconsider>false</IsReconsider>\r\n");
				buf.append("<ReconsiderTime>"+datetime+"</ReconsiderTime>\r\n");
				buf.append("<ProceResults>"+result+"</ProceResults>\r\n");
				buf.append("<CheckCompanyCode>001002</CheckCompanyCode>\r\n");
				buf.append("<CheckCompany>城区食品药品监督管理局</CheckCompany>\r\n");
				buf.append("<CheckMethod>标准曲线法</CheckMethod>\r\n");
				buf.append("<APRACategory>检测中心</APRACategory>\r\n");
				buf.append("<Hole>02</Hole>\r\n");
				buf.append("<CheckType>抽检</CheckType>\r\n");
				buf.append("</Result>\r\n");
				buf.append("</UpdateResult>\r\n");
				
				rpc.addProperty("in0",buf.toString());
				rpc.addProperty("in1", "check");
			    rpc.addProperty("in2", MD5("888888"));
//			    rpc.addProperty("in1", "yczzq");
//			    rpc.addProperty("in2", MD5("yczzq888"));
//				rpc.addProperty("in1","check");
//				rpc.addProperty("in2",MD5("888888"));
				rpc.addProperty("in3","");
				rpc.addProperty("in4",3);
				
				
				// 生成调用WebService方法的SOAP请求信息,并指定SOAP的版本
				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
						SoapEnvelope.VER11);
				
				envelope.bodyOut = rpc;
				// 设置是否调用的是dotNet开发的WebService
				envelope.dotNet = true;
				envelope.encodingStyle="UTF-8";
				// 等价于envelope.bodyOut = rpc;
				envelope.setOutputSoapObject(rpc);
				
				HttpTransportSE transport = new HttpTransportSE(endPoint);
				transport.debug=true;
				try {
					// 调用WebService
					transport.call(soapAction, envelope);
					Thread.sleep(1000);
				} catch (Exception e) {
					e.printStackTrace();
					Log.w("调用WebService失败", e.getMessage());
					Message message = handler.obtainMessage();  
					message.what=HANDLER_UPLOAD_CHECK_FAIL;
					handler.sendMessage(message);	
				}
				
				// 获取返回的数据
				SoapObject object = (SoapObject) envelope.bodyIn;
				// 获取返回的结果
				if(object!=null){
					mResult = object.getProperty(0).toString();
				
				Log.i("mResult=", mResult);
				}
				if(mResult!=null&&mResult.equals("1")){
					Message message = handler.obtainMessage();  
					message.obj = mResult; 
					message.what=HANDLER_UPLOAD_CHECK_SUCCESS;
					handler.sendMessage(message);
				}else{
					Message message = handler.obtainMessage();  
					message.what=HANDLER_UPLOAD_CHECK_FAIL;
					handler.sendMessage(message);	
				}
				
			}
		}).start();  
	}
	
	 private static Handler handler = new Handler(){  
	        public void handleMessage(android.os.Message msg) {  
	            // 将WebService得到的结果返回给TextView  
	        	switch (msg.what) {
				case HANDLER_UPLOAD_SUCCESS:
					Toast.makeText(mContext, "上传成功", 0).show();
					break;

				case HANDLER_UPLOAD_FAIL:
					Toast.makeText(mContext, "上传失败", 0).show();
					break;
				case HANDLER_UPLOAD_CHECK_SUCCESS:
					Toast.makeText(mContext, "上传成功", 0).show();
					break;
				case HANDLER_UPLOAD_CHECK_FAIL:
					Toast.makeText(mContext, "上传失败", 0).show();
					break;
				} 
	        };  
	    };
	    
	    /**
		 * MD5大写
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
