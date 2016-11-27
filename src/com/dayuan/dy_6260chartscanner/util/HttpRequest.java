package com.dayuan.dy_6260chartscanner.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



import android.util.Log;

public class HttpRequest {

	static String sampleid;
    static String proName;
    static String sampleDate;
    static String sampleDept;
	/**
     * ��ָ�� URL ����POST����������
     * 
     * @param url
     *            ��������� URL
     * @param param
     *            ����������������Ӧ���� name1=value1&name2=value2 ����ʽ��
     * @return ������Զ����Դ����Ӧ���
     */
    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // �򿪺�URL֮�������
            URLConnection conn = realUrl.openConnection();
            // ����ͨ�õ���������
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // ����POST�������������������
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // ��ȡURLConnection�����Ӧ�������
            out = new PrintWriter(conn.getOutputStream());
            // �����������
            out.print(param);
            // flush������Ļ���
            out.flush();
            // ����BufferedReader����������ȡURL����Ӧ
            
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
           // System.out.println("���� POST ��������쳣��"+e);
        	Log.i("TAG01", "���� POST ��������쳣��"+e);
            e.printStackTrace();
        }
        //ʹ��finally�����ر��������������
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        Log.i("result", result);
        return result;
    }
    
    
    public static void download() throws Exception {
        //���� GET ����
        //String s=HttpRequest.sendGet("http://localhost:6144/Home/RequestString", "key=123&v=456");
       // System.out.println(s);
        //String param="";
        //param="{'username':'admin','itemid':'9527','deviceid':'CP-9527','totalnum':'2','Longitude':'', 'Latitude':'','details':[{'sampleid':'T5704''doublevalue':'1.34','unit':'����','stringvalue':'��ͨ��','time':'2015-04-20 09:01:16'},{'sampleid':'T5705''doublevalue':'0.67','unit':'����','stringvalue':'ͨ��','time':'2015-04-20 09:01:16'}]}";
        //param="{'deviceid':'CP-9527','longitude':','latitude':','deviceStatus':'1'}";
//        String param="{'username':'admin','itemid':'9527','deviceid':'CP-9527','totalnum':'2','Longitude':'','Latitude':'','details':[{'sampleid':'T5704','doublevalue':'1.34','unit':'����','stringvalue':'��ͨ��','time':'2015-04-20 09:01:16'},{'sampleid':'ff','doublevalue':'0.67','unit':'����','stringvalue':'ͨ��','time':'2015-04-20 09:01:16'}]}";
        //���� POST ����
    	//String param="{'sampleid':'KJS0016062400950','deviceid':'DY-3000'}";
    	 JSONObject jo = new JSONObject();
	      jo.put("sampleid", "13");
	      jo.put("deviceid", "11");

        
    	//String param="{'deviceid':'CP-9527','longitude':'','latitude':'','deviceStatus':'1'}";//״̬�ϱ�
        String sr=HttpRequest.sendPost("http://122.13.2.155:8087/inspectinfo/jsp/inspect/quick_inspect.jsp?method=getsample", "usr=11&pwd=11&result="+jo.toString());
        String str="";
//        try {
//        	//str = new String(sr.getBytes("gbk"),"utf-8");
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
        //System.out.println(str);
        Log.i("download", sr);
//        JSONArray ary=new JSONArray(sr);
//        for(int i=0; i<ary.length(); i++){
        JSONObject obj=new JSONObject(sr);	
        sampleid=obj.getString("sampleid");
        proName=obj.getString("productName");
        sampleDate=obj.getString("sampleDate");
        sampleDept=obj.getString("sampleDept");
//        }
        Log.i("downloadResult", sampleid+"/"+proName+"/"+sampleDate+"/"+sampleDept);
    }
}
