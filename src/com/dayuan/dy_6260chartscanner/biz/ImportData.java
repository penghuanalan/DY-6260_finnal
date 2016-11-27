package com.dayuan.dy_6260chartscanner.biz;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.widget.Toast;

import com.dayuan.dy_6260chartscanner.db.Dao;
import com.dayuan.dy_6260chartscanner.db.MyDBOpenHelper;
import com.dayuan.dy_6260chartscanner.entity.Project;

public class ImportData {

	public static void getCSV(Context context){
		Dao dao=new Dao(context);
    	String path="/mnt/usbhost/"+"dayuan/DY6260_items.csv";
    	String path02="/mnt/extsd/"+"dayuan/DY6260_items.csv";
		BufferedReader br = null;
   	 
		File file = new File(path);
		File file02=new File(path02);
		if (file.exists() && file.length() > 0) {
            String strLine = null;
            int i=0;
            int sampleId=0;
	    	 String projectName=" ";
	    	 String method=" ";
	    	 String valueC=" ";
	    	 String valueTL=" ";
	    	 String valueT=" ";
	    	 String unit=" ";
	    	 String limitV=" ";
	    	 String constant0=" ";
	    	 String constant1=" ";
	    	 String constant2=" ";
	    	 String constant3=" ";
       	  try {
       		  FileInputStream fis = new FileInputStream (file);
              InputStreamReader isr = new InputStreamReader (fis, "GBK");
              br = new BufferedReader (isr);
              dao.deleteAll();
			 while ((strLine = br.readLine()) != null) {
				     i++;
				     if(i>1){
//					 lists.add(strLine);
				    	 String [] values=strLine.split(",");
				    	 int len=values.length;
				    	 if(len==6){
				    	 sampleId=Integer.parseInt(values[0]);
				    	 projectName=values[1];
				    	 method=values[2];
				    	 valueC=values[3];
				    	 valueTL=values[4];
				    	 valueT=values[5];
				    	 }else if(len==8){
				    		 sampleId=Integer.parseInt(values[0]);
				    		 projectName=values[1];
				    		 method=values[2];
				    		 valueC=values[3];
				    		 valueTL=values[4];
				    		 valueT=values[5];	 
				    		 unit=values[6];
				    		 limitV=values[7];
				    	 }else if(len==12){
				    		 sampleId=Integer.parseInt(values[0]);
				    		 projectName=values[1];
				    		 method=values[2];
				    		 valueC=values[3];
				    		 valueTL=values[4];
				    		 valueT=values[5];	 
				    		 unit=values[6];
				    		 limitV=values[7];
				    		 constant0=values[8];
				    		 constant1=values[9];
				    		 constant2=values[10];
				    		 constant3=values[11];
				    		 
				    	 }
				    	 dao.add(sampleId,projectName, method, valueC, valueT, valueTL,
				    			 constant0, constant1, constant2, constant3, unit, limitV);
				     }
					 
			 }
		     Toast.makeText(context, "导入成功", 0).show();
		} catch (Exception ex) {
			ex.printStackTrace();
		}finally{
			try {  
                if (br != null) {  
                	br.close();  
                }  
            } catch (Exception ex) {  
                ex.printStackTrace();  
            }  
        }  
		}else if (file02.exists() && file02.length() > 0) {
			String strLine = null;
			int i=0;
			int sampleId=0;
			String projectName=" ";
			String method=" ";
			String valueC=" ";
			String valueTL=" ";
			String valueT=" ";
			String unit=" ";
			String limitV=" ";
			String constant0=" ";
			String constant1=" ";
			String constant2=" ";
			String constant3=" ";
			try {
				FileInputStream fis = new FileInputStream (file02);
				InputStreamReader isr = new InputStreamReader (fis, "GBK");
				br = new BufferedReader (isr);
				dao.deleteAll();
				while ((strLine = br.readLine()) != null) {
					i++;
					if(i>1){
//					 lists.add(strLine);
						String [] values=strLine.split(",");
						int len=values.length;
						if(len==6){
							sampleId=Integer.parseInt(values[0]);
							projectName=values[1];
							method=values[2];
							valueC=values[3];
							valueTL=values[4];
							valueT=values[5];
						}else if(len==8){
							sampleId=Integer.parseInt(values[0]);
							projectName=values[1];
							method=values[2];
							valueC=values[3];
							valueTL=values[4];
							valueT=values[5];	 
							unit=values[6];
							limitV=values[7];
						}else if(len==12){
							sampleId=Integer.parseInt(values[0]);
							projectName=values[1];
							method=values[2];
							valueC=values[3];
							valueTL=values[4];
							valueT=values[5];	 
							unit=values[6];
							limitV=values[7];
							constant0=values[8];
							constant1=values[9];
							constant2=values[10];
							constant3=values[11];
							
						}
						dao.add(sampleId, projectName, method, valueC, valueT, valueTL,
								constant0, constant1, constant2, constant3, unit, limitV);
					}
					
				}
				Toast.makeText(context, "导入成功", 0).show();
			} catch (Exception ex) {
				ex.printStackTrace();
			}finally{
				try {  
					if (br != null) {  
						br.close();  
					}  
				} catch (Exception ex) {  
					ex.printStackTrace();  
				}  
			}  
		}else{
			Toast.makeText(context, "请插入sd卡或U盘", 0).show();
		}
	}
}
