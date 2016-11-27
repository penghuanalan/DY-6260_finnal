package com.dayuan.dy_6260chartscanner.util;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

import com.dayuan.dy_6260chartscanner.R;
import com.dayuan.dy_6260chartscanner.entity.QueryLog;

public class LogUtil {
	
	public static void showDialog(final Context context,final QueryLog log ,final int position){
		
		AlertDialog.Builder builder=new AlertDialog.Builder(context);
		final AlertDialog alertDialog=builder.create();
		alertDialog.show();
		//×Ô¶¨ÒåUI
		Window window = alertDialog.getWindow();
		View dialogView=View.inflate(context, R.layout.delete_window, null);
		window.setContentView(dialogView);
		Button btnNo=(Button)window.findViewById(R.id.btn_no);
		Button btnYes=(Button)window.findViewById(R.id.btn_yes);
		btnNo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				alertDialog.dismiss();
			}
		});
//		btnYes.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//			log.remove(position);	
//			}
//		});
}
}