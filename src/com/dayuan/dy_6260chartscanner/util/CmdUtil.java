package com.dayuan.dy_6260chartscanner.util;

import java.io.UnsupportedEncodingException;

import android.util.Log;

import com.hoho.android.usbserial.util.HexDump;

public class CmdUtil {
	private final static String TAG = CmdUtil.class.getSimpleName();
    public static boolean mCmdIsFinishParse = false;

	public static final int CMD_HEARD_INDEX_START = 0;
	public static final int CMD_HEARD_INDEX_END = 4;
	
	public static final int CMD3300_HEARD_INDEX_END = 6;
	
	public static final int CMD_LEN_INDEX_START = 16;
	public static final int CMD_LEN_INDEX_END = 20;
	
	public static final int CMD_HEARD_CHECKSUM_INDEX_START = 20;
	public static final int CMD_HEARD_CHECKSUM_INDEX_END = 22;
	
	public static final int CMD_DATA_CHECKSUM_INDEX_START = 22;
	public static final int CMD_DATA_CHECKSUM_INDEX_END = 24;
	
	public static final int CMD_DATA_INDEX_START = 24;
	
	private static int m_RecordStartPosition = 0;
    private static int m_RecordEndPosition = 0;
    private static int m_RecordCount = 0;

	
	public static final String CMD_HEARD_INDEX_STR = "03FE";
	public static final String CMD3300_HEARD_INDEX_STR = "0003FE";
	public static boolean mIsClearData = false;
	public static boolean mIs3300Cmd = false;
	
	public static byte getChecksum(byte[] cmd) {
		int len = cmd.length;
		int sum = 0;
		
		for (int i = 0; i != len; i++) {
			sum ^= cmd[i];
		}
		
		return (byte)sum;
	}
	
	public static String getChecksum(String cmdStr) {
		byte[] cmd = HexDump.hexStringToByteArray(cmdStr);
		int len = cmd.length;
		int sum = 0;
		
		for (int i = 0; i != len; i++) {
			sum ^= cmd[i];
		}
		
		return HexDump.toHexString((byte)sum);
	}
	
	/**
	 * 鎻愪緵鐨勬牸寮忓彧鏀寔濡備笅鏍煎紡:"yyyy-MM-dd HH:mm:ss" or "yy-MM-dd HH:mm:ss"
	 * @param date
	 * @return time+date in 32 bytes
	 */
	public static String getDateAndTimeHex(String dataStr) {
		String year = null, month = null, day = null, 
				hour = null, min = null, second = null;
		
		if (dataStr.length() == 19) {
			//yyyy-MM-dd HH:mm:ss
			year = dataStr.substring(2, 4);
			month = dataStr.substring(5, 7);
			day = dataStr.substring(8, 10);
			hour = dataStr.substring(11, 13);
			min = dataStr.substring(14, 16);
			second = dataStr.substring(17, 19);
			
		}
		else if (dataStr.length() == 17) {
			//yy-MM-dd HH:mm:ss
			year = dataStr.substring(0, 2);
			month = dataStr.substring(3, 5);
			day = dataStr.substring(6, 8);
			hour = dataStr.substring(9, 11);
			min = dataStr.substring(12, 14);
			second = dataStr.substring(15, 17);
		}
		byte[] cmd = new byte[4]; 
		//time
		int time = Integer.parseInt(hour);
		time = time << 6;
		time += Integer.parseInt(min);
		time = time << 5;
		time += (Integer.parseInt(second) / 2);
		cmd[0] = (byte)(time & 0x00ff);
		cmd[1] = (byte)((time & 0xff00) >> 8);
		
		//date
		int date = Integer.parseInt(year);
		date <<= 4;
		date += Integer.parseInt(month);
		date <<= 5;
		date += Integer.parseInt(day);
		
		cmd[2] = (byte)(date & 0x00ff);
		cmd[3] = (byte)((date & 0xff00) >> 8);
			
		return HexDump.toHexString(cmd);
	}
	
	/**
	 * 鏃堕棿鏍煎紡16锟�?
		锟�?	璇存槑
		0-4	锟�?/2锛屾椂闂寸簿搴︿负2锟�?
		5-10	锟�?(00-59)
		11-15	锟�?(00-23)
		鏃ユ湡鏍煎紡16锟�?
		锟�?	璇存槑
		0-4	锟�?
		5-8	锟�?
		9-15	锟�?(鍙寘锟�?0-99閮ㄥ垎)
	 * @param hexStr
	 * @return
	 */
	public static String getDatetimeFromHex(String hexStr) {
		//Log.d(TAG, "Datetime: " + hexStr);
		if (hexStr.length() != 8)
		{
			return "error-datetime";
		}
		String dateStr = null;
		byte[] data = HexDump.hexStringToByteArray(hexStr);
		//get time
		int tmp = (data[1] & 0xff);
		tmp <<= 8;
		tmp += (data[0] & 0xff);
		int second = (tmp & 0x001F) * 2;
		int min = ((tmp >> 5) & 0x003F);
		int hour = ((tmp >> 11) & 0x001F);
		
		tmp = (data[3] & 0xff);
		tmp <<= 8;
		tmp += (data[2] & 0xff);
		int day = tmp & 0x001F;
		int month = ((tmp >> 5) & 0x000F);
		int year = ((tmp >> 9) & 0x007F);
		String yearPre = "";
		if (year <= 9)
		{
			yearPre = "0";
		}
		dateStr = "20" + yearPre + year + "-" + month +"-" + day + " " + hour + ":" + min + ":" + second;
		
		return dateStr;
	}
	
	public static boolean isCmdStart(String cmdStr) {
		if (!cmdStr.substring(CMD_HEARD_INDEX_START, 
				CMD_HEARD_INDEX_END).equalsIgnoreCase(CMD_HEARD_INDEX_STR)) {
			Log.d(TAG, "checkCmdValid: cmdStr heard error:");
			return false;
		}
		return true;
	}
	
	public static boolean isCmdStart3300(String cmdStr) {
		if (!cmdStr.substring(CMD_HEARD_INDEX_START, 
				CMD3300_HEARD_INDEX_END).equalsIgnoreCase(CMD3300_HEARD_INDEX_STR)) {
			Log.d(TAG, "not 3300");
			return false;
		}
		return true;
	}
	
	public static boolean checkCmdValid(String cmdStr) {
		Log.d(TAG, "##0 " + cmdStr);
		mIsClearData = false;
		if (cmdStr.length() < 24) {
			Log.d(TAG, "checkCmdValid: cmdStr len error:" + cmdStr.length());
			mIsClearData = false;
			return false;
		}
		if (isCmdStart(cmdStr)) {
			mIs3300Cmd = false;
		}
		else if(isCmdStart3300(cmdStr)) {
			Log.d(TAG, "checkCmdValid 3300: is 3300:");
			mIs3300Cmd = true;
			cmdStr = cmdStr.substring(2);
			if (cmdStr.length() < 24)
			{
				mIsClearData = false;
				return false;
			}
		}
		else {
			mIsClearData = true;
			return false;
		}
		
		int len = paresHexToShortInLE(cmdStr.substring(CMD_LEN_INDEX_START, CMD_LEN_INDEX_END));
		
		String headCmd = cmdStr.substring(CMD_HEARD_INDEX_START, CMD_LEN_INDEX_END);
		String heardCs = getChecksum(headCmd);
		if (!heardCs.equalsIgnoreCase(
				cmdStr.substring(CMD_HEARD_CHECKSUM_INDEX_START, 
						CMD_HEARD_CHECKSUM_INDEX_END))) {
			Log.d(TAG, "checkCmdValid: cmdStr heard cs error:" + heardCs);
			mIsClearData = true;
			return false;
		}
		String dataCmd = cmdStr.substring(CMD_DATA_INDEX_START);
		String dataCs = getChecksum(dataCmd);
		
		if (len <= (dataCmd.length() / 2) ) {
			if (!dataCs.equalsIgnoreCase(
					cmdStr.substring(CMD_DATA_CHECKSUM_INDEX_START, CMD_DATA_CHECKSUM_INDEX_END))){
				Log.d(TAG, "checkCmdValid: cmdStr data cs error:" + dataCs);
				mIsClearData = true;
				return false;
			}
		}else {
			Log.d(TAG, "checkCmdValid: cmdStr data len error: len:" + len + ", real len:" + (dataCmd.length() / 2));
			mIsClearData = false;
			return false;
		}
		return true;
	}
	
	/**
	 * 灏忕byte鏁扮粍杞瑂hort
	 * @param lenStr
	 * @return
	 */
	public static short paresHexToShortInLE(String lenStr) {
		int len = 0;
		if (lenStr.length() < 4)
			return 0;
		len = Integer.parseInt(lenStr.substring(2), 16);
		len <<= 8;
		len += Integer.parseInt(lenStr.substring(0, 2), 16);
		
		return (short)len;
	}
}
	/**
	 * 杩斿洖Json 鏍煎紡鐨勮褰曚俊锟�?
	 * @param cmdStr
	 * @return
	 */
//	public static String parseRevData(String cmdStr) {
//		int j=0;
//		if (cmdStr.length() < 24)
//		{
//			mCmdIsFinishParse = true;
//			return "{\"error\":\"data error\"}";
//		}
//		Log.v(TAG, "cmdStr = " + cmdStr);
//		String cmdType = cmdStr.substring(4, 6);
//		Log.d(TAG, "cmdType: " + cmdType);
//		int cmdDataLen = paresHexToShortInLE(cmdStr.substring(CMD_LEN_INDEX_START, CMD_LEN_INDEX_END));
//		if (cmdType.equals("42") || cmdType.equals("62") || cmdType.equals("72") ) {
//			
//			if (cmdDataLen == 0) {
//				Gson gson = new Gson();
//				mCmdIsFinishParse = true;
//				return gson.toJson(DySerialManager.mProjectItems);
//				//Log.v("浣嶇疆1",String.valueOf(j++));
//			}
//			
//			if (cmdStr.substring(10, 12).equals("00")) {
//				CmdEntity.PROJECT_ITEM_INDEX = 0;
//				DySerialManager.mProjectItems.clear();
//				Log.v("浣嶇疆2", String.valueOf(j++));
//			}
//			
//			String name = cmdStr.substring(24, 56);
//			String unit = cmdStr.substring(56, 72);
//			int checkValue = Integer.parseInt(cmdStr.substring(72, 74), 16);
//			name = gb2312String(HexDump.hexStringToByteArray(name));
//			unit = gb2312String(HexDump.hexStringToByteArray(unit));
//
//			//String data = null;
//			String methods=null;
//			String projects=null;
//			int proItems=0;
//			int values=0;
//			String units=null;
//			Log.v("浣嶇疆3",String.valueOf(j++));
//			if (cmdType.equals("42")) {
//				//data = "闈炶瘯绾告硶-" + name + ":-1:";
//				methods="闈炶瘯绾告硶";
//				proItems=-1;
//			}else if(cmdType.equals("62")){
//				//data = "閲戞爣锟�?-" + name + ":-6:";
//				methods="閲戞爣鍗℃硶";
//				proItems=-6;
//			}else if (cmdType.equals("72")){
//				//data = "骞插寲瀛︽硶-" + name + ":-7:";
//				methods="骞插寲瀛︽硶";
//				proItems=-7;
//			}
//			values=checkValue;
//			projects=name;
//			units=unit;
//			//data += checkValue + ":" + unit;
//			ProItems mProItems1=new ProItems();
//			mProItems1.setMethods(methods);
//			mProItems1.setProjects(projects);
//			mProItems1.setValues(values);
//			mProItems1.setUnits(units);
//			DySerialManager.mProjectItems.add(mProItems1);
//			Log.v("瀵硅薄锟�?:",String.valueOf(cmdStr.length()));
//			if (cmdStr.length() > 512) {
//				name = cmdStr.substring(280, 312);
//				unit = cmdStr.substring(312, 328);
//				checkValue = Integer.parseInt(cmdStr.substring(328, 330));
//				name = gb2312String(HexDump.hexStringToByteArray(name));
//				unit = gb2312String(HexDump.hexStringToByteArray(unit));
//				if (cmdType.equals("42")) {
//					//data = "闈炶瘯绾告硶-" + name + ":-1:";
//					methods="闈炶瘯绾告硶";
//					proItems=-1;
//				}else if(cmdType.equals("62")){
//					//data = "閲戞爣锟�?-" + name + ":-6:";
//					methods="閲戞爣鍗℃硶";
//					proItems=-6;
//				}else if (cmdType.equals("72")){
//					//data = "骞插寲瀛︽硶-" + name + ":-7:";
//					methods="骞插寲瀛︽硶";
//					proItems=-7;
//				}
//				//data += checkValue + ":" + unit;
//				//DySerialManager.mProjectItems.add(data);
//				values=checkValue;
//				projects=name;
//				units=unit;
//				ProItems mProItems2=new ProItems();
//				mProItems2.setMethods(methods);
//				mProItems2.setProjects(projects);
//				mProItems2.setValues(values);
//				mProItems2.setUnits(units);
//				DySerialManager.mProjectItems.add(mProItems2);
//				Log.v("浣嶇疆4",String.valueOf(j++));
//				
//			}
//			//
//			//send read next item
//			DySerialManager.sendCmd(CmdEntity.sendReadPorjectCmd((byte)Integer.parseInt(cmdType, 16), 
//					++(CmdEntity.PROJECT_ITEM_INDEX)));
//			mCmdIsFinishParse = false;
//			return null;
//		}
//		else if (cmdType.equals("43") || cmdType.equals("63") || cmdType.equals("73")) {
//			int li = 0;
//			m_RecordCount = 0;
//			DySerialManager.mLogs.clear();
//			// Start position
//			li = Integer.parseInt(cmdStr.substring(42, 44), 16);
//			li <<= 8;
//			li += Integer.parseInt(cmdStr.substring(40, 42), 16);
//			m_RecordStartPosition = li;
//			// End position
//			li = Integer.parseInt(cmdStr.substring(46, 48), 16);
//			li <<= 8;
//			li += Integer.parseInt(cmdStr.substring(44, 46), 16);
//			m_RecordEndPosition = li;
//			Log.d(TAG, "m_RecordStartPosition: " + m_RecordStartPosition
//					+ "; m_RecordEndPosition: " + m_RecordEndPosition);
//			if (m_RecordStartPosition + 1 <= m_RecordEndPosition) {
//				Log.d(TAG, "Send to 45 CMD");
//				DySerialManager.sendCmd(CmdEntity.sendReadTestLogCmd((byte)(Integer.parseInt(cmdType, 16) + 2), m_RecordStartPosition));
//			}
//			else {
//				m_RecordStartPosition = 0;
//				m_RecordEndPosition = 0;
//			}
//			mCmdIsFinishParse = false;
//			return null;
//		} 
//		else if (cmdType.equals("45") || cmdType.equals("65") || cmdType.equals("75")) {
//			Log.d(TAG, "Begin 45/65/75");
//            if (!cmdStr.substring(24, 26).equals("00") )
//            {
//            	Log.d(TAG, "Have datas!");
//            	int itemID = Integer.parseInt(cmdStr.substring(26, 28), 16);   //浠櫒涓婄殑椤圭洰搴忓彿
//            	String datetime = null;
//			
//			   
//			    if(cmdType.equals("45"))
//			    {
//			    	datetime = getDatetimeFromHex(cmdStr.substring(32, 40)); // dataRead.Substring(32, 8);
//			    }
//			    else if (cmdType.equals("65") || cmdType.equals("75"))
//			    {
//			    	datetime = getDatetimeFromHex(cmdStr.substring(28, 36)); // dataRead.Substring(28, 8);
//			    }
//			    
//			    Log.d(TAG, "datetime: " + datetime);
//			
//			   String num = null;
//			   String checkValue = null;   //锟�?娴嬶拷??
//			   String error = "锟�?";    // 鏁版嵁鏄惁鍙枒
//			   String partOutcome = null;
//			   String tempStr = cmdStr.substring(48);
//			
//			   int byteCount = 16;    //鏁版嵁涓查暱锟�?(8涓瓧锟�?)
//			
//			   int recordCount = tempStr.length() / byteCount;
//			   int p = 0;
//			   int channelNO = 0;
//			   if (cmdType.equals("45"))
//			   {
//			       for (int i = 0; i < recordCount; i++)
//			       {
//			           channelNO = Integer.parseInt(cmdStr.substring(40 + i * 16, 42 + i * 16), 16);
//			           p = Integer.parseInt(cmdStr.substring(42 + i * 16, 44 + i * 16), 16);
//			           if (channelNO > 0)
//			           {
//			        	   error = "锟�?";
//			        	   if (p >= 192)
//			        	   {
//			        		   error = "锟�?";       //璇存槑鏁版嵁鍙枒
//			        	   }
//			        	   num = "" + channelNO;
//			
//			        	   if (itemID == 0) //涓烘姂鍒剁巼锟�?
//			        	   {
//			        		   partOutcome = cmdStr.substring(48 + i * 16, 48 + i * 16 + 4).toUpperCase();
//			
//			        		   if (partOutcome.equals("FF7F"))//鏃犳晥鏁版嵁
//			        		   {
//			        			   checkValue = "---.-";
//			        		   }
//			        		   else if (partOutcome.equals("FE7F"))
//			        		   {
//			        			   checkValue = "NA";
//			        		   }
//			        		   else
//			        		   {
//			        			   checkValue = HexString2Float2String(partOutcome); //StringUtil.HexString2Float2String(partOutcome);
//			        		   }
//			        	   }
//			        	   else //鍏朵粬鏂规硶
//			        	   {
//			        		   partOutcome = cmdStr.substring(48 + i * 16, 48 + i * 16 + 8).toUpperCase();
//			        		   if (partOutcome.equals("FFFFFF7F") || partOutcome.equals("FEFFFF7F") || partOutcome.equals("FF7F0000") || partOutcome.equals("2C420F00"))    //鏃犳晥鏁版嵁
//			        		   {
//			        			   checkValue = "-.---";
//			        		   }
//			        		   else if (partOutcome.equals("FDFFFF7F") || partOutcome.equals("FE7F0000"))    //鍙拷??鐤戞暟锟�?
//			        		   {
//			        			   checkValue = "NA";
//			        		   }
//			        		   else
//			        		   {
//			        			   Log.d(TAG, "partOutcome: " + partOutcome);
//			        			   checkValue = HexString2Float4String(partOutcome);//StringUtil.HexString2Float4String(partOutcome);
//			        			   Log.d(TAG, "checkValue: " + checkValue);
//			        		   }
//			        	   }
//			        	   if (!(checkValue.equals("NA") || checkValue.equals("-.---") || checkValue.equals("---.-")))
//			               {
//			        		   LogEntity log = new LogEntity();
//			        		   log.setItemid(itemID);
//			        		   log.setDatetime(datetime);
//			        		   log.setError(error);
//			        		   log.setCheckValue(checkValue);
//			        		   log.setNum(num);
//			        		   DySerialManager.mLogs.add(log);
//			               }
//			           }
//			       }
//			   }
//			   if (cmdType.equals("65") || cmdType.equals("75"))
//			   {
//				   String OUTnum = cmdStr.substring(36, 40);
//				   //TODO: FIX IT
//				   double NOE = Float.parseFloat(HexString2Float2String(OUTnum));//Convert.ToDouble(StringUtil.HexString2Float2String(OUTnum));
//				   num = (NOE*10) + "";
//			  
//				   byte[] TempArry = HexDump.hexStringToByteArray(cmdStr.substring(24));
//				   //String o = cmdStr.substring(24);
//				   if (cmdType.equals("65"))
//				   {
//					   switch (TempArry[8] >> 6 & 0x03)
//					   {  //锟�?2浣嶈〃绀烘娴嬫柟锟�?
//					   case 0: 
//					   case 1: switch (TempArry[8] & 0x03)
//			           {
//			               case 1: checkValue = "闃筹拷??";
//			                   break;
//			               case 2: checkValue = "闃达拷??";
//			                   break;
//			               case 3: checkValue = "NA";
//			                   break;
//			               default: checkValue = "NA";
//			                       break;
//			           }
//			           break;
//			           case 2:
//			           case 3:
//			               partOutcome = cmdStr.substring(42, 54);
//			               Log.d(TAG, "partOutcome: " + partOutcome);
//			               checkValue = HexString2Float4String(partOutcome);//StringUtil.HexString2Float4String(partOutcome);
//			               Log.d(TAG, "checkValue: " + checkValue);
//			               break;
//					   }                   
//				   }
//				   if (cmdType.equals("75"))
//				   {
//				       switch (TempArry[8] >> 6 & 0x03)
//				       {  //锟�?2浣嶈〃绀烘娴嬫柟锟�?
//				       case 0: switch (TempArry[8] & 0x03)
//				           {
//				               case 1: checkValue = "闃筹拷??";
//				                   break;
//				               case 2: checkValue = "闃达拷??";
//				                   break;
//				               case 3: checkValue = "NA";
//				                   break;
//				               default: checkValue = "NA";
//				                       break;
//				               }; break;
//				
//				       case 2: partOutcome = cmdStr.substring(42, 54);
//				       		checkValue = HexString2Float4String(partOutcome); //StringUtil.HexString2Float4String(partOutcome);
//				       		break;
//				
//				       }   
//				   }
//				   if (!(checkValue.equals("NA") || checkValue.equals("-.---") || checkValue.equals("---.-")))
//				   {
//					   LogEntity log = new LogEntity();
//	        		   log.setItemid(itemID);
//	        		   log.setDatetime(datetime);
//	        		   log.setError(error);
//	        		   log.setCheckValue(checkValue);
//	        		   log.setNum(num);
//	        		   DySerialManager.mLogs.add(log);
//				   }
//			   }
//            }
//            Log.d(TAG, "Finish parasing");
//            m_RecordCount++;
//            int endPosition = m_RecordStartPosition + m_RecordCount;
//            
//            if (endPosition >= m_RecordEndPosition)
//            {
//            	Log.d(TAG, "Finish and send data");
//            	// Finish
//                m_RecordCount = 0;
//                m_RecordStartPosition = 0;
//                m_RecordEndPosition = 0;
//
//                //杩斿洖 缁撴潫鏍囧織
//                mCmdIsFinishParse = true;
//                Gson gson = new Gson();
//                Log.v("43鏁版嵁","{\"dayuan\":"+gson.toJson(DySerialManager.mLogs)+"}");
//                return gson.toJson(DySerialManager.mLogs);
//            }
//            else
//            {
//            	Log.d(TAG, "Has another datas!");
//            	mCmdIsFinishParse = false;
//            	DySerialManager.sendCmd(CmdEntity.sendReadTestLogCmd((byte)Integer.parseInt(cmdType, 16), endPosition));
//            	return null;
//            }
//		}
//		else if (cmdType.equals("20")) {
//			String data = cmdStr.substring(24);
//			data = new String(HexDump.hexStringToByteArray(data));
//			mCmdIsFinishParse = true;
//			Log.v(TAG,"{\"rev\":\"" + data + "\"}");
//			return "{\"rev\":\"" + data + "\"}";
//		}
//		return null;
//	}
//	
//	public static String gb2312String(byte[] data) {
//		String retStr = null;
//		try {
//			 retStr = new String(data, "GB2312").trim();
//			 
//			 int idx = retStr.indexOf("\0");
//			 Log.d(TAG, "gb2312String: " + retStr + "; idx = " + idx);
//			 if ( idx != -1) {
//				 retStr = retStr.substring(0, idx);
//			 }
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return retStr;
//	}
//	
//    /// <summary>
//    /// 鎶婂崄鍏繘鍒跺瓧绗︿覆杞寲涓轰袱浣嶆诞鐐规暟锟�?
//    /// </summary>
//    /// <param name="input"></param>
//    /// <returns></returns>
//   public static String HexString2Float2String(String input)
//   {
//       int i2 = 0;
//       i2 = Integer.parseInt(input.substring(2, 4), 16) << 8;
//       i2 += Integer.parseInt(input.substring(0, 2), 16);
//       String temp = "";
//       temp += ((float)i2 / 10);
//       return temp;
//   }
//
//    /// <summary>
//    /// 鎶婂崄鍏埗瀛楃涓茶浆鍖栧洓浣嶆诞鐐规暟鎹瓧绗︿覆
//    /// </summary>
//    /// <param name="input"></param>
//    /// <returns></returns>
//   public static String HexString2Float4String(String input)
//   {
//       int iflag = 1;
//       int ret = 0;
//       ret = Integer.parseInt(input.substring(6, 8), 16);
//       if (ret >= 128)
//       {
//           iflag = -1;
//           ret = (ret - 128) << 24;
//       }
//
//       ret += Integer.parseInt(input.substring(4, 6), 16) << 16;
//       ret += Integer.parseInt(input.substring(2, 4), 16) << 8;
//       ret += Integer.parseInt(input.substring(0, 2), 16);
//
//       float fRet = ((float)ret / 1000) * iflag;
//       String temp = "" + fRet;
//       return temp;
//   }
//}
