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
     * 向指定 URL 发送POST方法的请求
     * 
     * @param url
     *            发送请求的 URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
           // System.out.println("发送 POST 请求出现异常！"+e);
        	Log.i("TAG01", "发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
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
        //发送 GET 请求
        //String s=HttpRequest.sendGet("http://localhost:6144/Home/RequestString", "key=123&v=456");
       // System.out.println(s);
        //String param="";
        //param="{'username':'admin','itemid':'9527','deviceid':'CP-9527','totalnum':'2','Longitude':'', 'Latitude':'','details':[{'sampleid':'T5704''doublevalue':'1.34','unit':'毫克','stringvalue':'不通过','time':'2015-04-20 09:01:16'},{'sampleid':'T5705''doublevalue':'0.67','unit':'毫克','stringvalue':'通过','time':'2015-04-20 09:01:16'}]}";
        //param="{'deviceid':'CP-9527','longitude':','latitude':','deviceStatus':'1'}";
//        String param="{'username':'admin','itemid':'9527','deviceid':'CP-9527','totalnum':'2','Longitude':'','Latitude':'','details':[{'sampleid':'T5704','doublevalue':'1.34','unit':'毫克','stringvalue':'不通过','time':'2015-04-20 09:01:16'},{'sampleid':'ff','doublevalue':'0.67','unit':'毫克','stringvalue':'通过','time':'2015-04-20 09:01:16'}]}";
        //发送 POST 请求
    	//String param="{'sampleid':'KJS0016062400950','deviceid':'DY-3000'}";
    	 JSONObject jo = new JSONObject();
	      jo.put("sampleid", "13");
	      jo.put("deviceid", "11");

        
    	//String param="{'deviceid':'CP-9527','longitude':'','latitude':'','deviceStatus':'1'}";//状态上报
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
