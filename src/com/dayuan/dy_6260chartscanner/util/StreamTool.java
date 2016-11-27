package com.dayuan.dy_6260chartscanner.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class StreamTool {

	 /** 
     * ��һ��inputstream���������ת����һ��byte[]  
     */  
      
	 public static byte[] read(InputStream inStream) throws Exception{
		  ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		  byte[] buffer = new byte[1024];
		  int len = 0;
		  while( (len = inStream.read(buffer)) != -1){
		   outputStream.write(buffer, 0, len);
		  }
		  inStream.close();
		  return outputStream.toByteArray();
		 }
}
