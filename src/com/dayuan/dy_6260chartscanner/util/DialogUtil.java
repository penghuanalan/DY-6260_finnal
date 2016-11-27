package com.dayuan.dy_6260chartscanner.util;

import com.dayuan.dy_6260chartscanner.R;
import com.dayuan.dy_6260chartscanner.activity.DetectionChannelOneActvity;
import com.dayuan.dy_6260chartscanner.activity.DetectionChannelActvity;
import com.dayuan.dy_6260chartscanner.entity.Project;
import com.dayuan.dy_6260chartscanner.entity.Sample;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;

public class DialogUtil {
	private static Button  ibChannelOne;
	private static Button ibChannelTwo;
	private static int channelOne;
	private static int channelTwo;
	private static boolean isFirst;
	private static boolean isFirstchannel2;
	public static  void showDialog(final Context context,final Project project){
		
		AlertDialog.Builder builder=new AlertDialog.Builder(context);
		final AlertDialog alertDialog=builder.create();
		alertDialog.setCanceledOnTouchOutside(false);
		alertDialog.show();
		//自定义UI
		Window window = alertDialog.getWindow();
		window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		LayoutParams params = window.getAttributes();
		params.dimAmount = 0.7f;
		window.setAttributes(params);
		window.setContentView(R.layout.sample_detail);
		
		   ibChannelOne=(Button) window.findViewById(R.id.ib_channel_one);
		   ibChannelTwo=(Button) window.findViewById(R.id.ib_channel_two);
		Button btnConfirm=(Button) window.findViewById(R.id.btn_confirm);
		Button btnBack=(Button) window.findViewById(R.id.btn_back);
//		selectFormItem(ibChannelOne);
//		channelOne=0;
		ibChannelOne.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				selectFormItem(ibChannelOne);
				if(!isFirst){
					isFirst=true;
					ibChannelOne.setSelected(true);
				}else{
					isFirst=false;
					ibChannelOne.setSelected(false);
				}
				channelOne=0;
			}
		});
		
		ibChannelTwo.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				selectFormItem(ibChannelTwo);
				if(!isFirstchannel2){
					isFirstchannel2=true;
					ibChannelTwo.setSelected(true);
				}else{
					isFirstchannel2=false;
					ibChannelTwo.setSelected(false);
				}
				channelTwo=1;
			}
		});
		btnBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				isFirst=false;
				//ph增加一个
				isFirstchannel2=false;
				alertDialog.dismiss();
			}
		});
		  btnConfirm.setOnClickListener(new OnClickListener() {
			  Intent intent;
			 
//			  int channelOne=0;
//			  int channelTwo=1;
			@Override
			public void onClick(View v) {
				
				
				/*isFirst=false;
				isFirstchannel2=false;*/
				int number=project.getSampleid();
 				String projectName=project.getName();
				String method=project.getMethod();
				String valueC=project.getValueC();
				String valueT=project.getValueT();
				String valueTL=project.getValueTL();
				String sampleName=project.getSamplename();
				String valueOne=project.getValueOne();
				String valueTwo=project.getValueTwo();
				String valueThree=project.getValueThree();
				String resultChoice=project.getResult();
				String chOne=null;
				String chTwo=null;
				if(ibChannelOne.isSelected()&&!ibChannelTwo.isSelected()&&isFirst==true){
					//ph这里将两个Boolean值置为false
					isFirst=false;
					isFirstchannel2=false;
					chOne=String.valueOf(channelOne);
					intent=new Intent(context,DetectionChannelOneActvity.class);
					intent.putExtra("number", String.valueOf(number));
					intent.putExtra("channel", chOne);
					intent.putExtra("method", method);
					intent.putExtra("valueC", valueC);
					intent.putExtra("name", projectName);
					intent.putExtra("valueT", valueT);
					intent.putExtra("valueTL", valueTL);
					intent.putExtra("sampleName", sampleName);
					intent.putExtra("valueOne", valueOne);
					intent.putExtra("valueTwo", valueTwo);
					intent.putExtra("valueThree", valueThree);
					intent.putExtra("resultChoice", resultChoice);
					context.startActivity(intent);
				}
				else if(ibChannelTwo.isSelected()&&!ibChannelOne.isSelected()&&isFirstchannel2==true)
				{
					//ph这里将两个Boolean值置为false
					isFirst=false;
					isFirstchannel2=false;
					chOne=String.valueOf(channelTwo);
					intent=new Intent(context,DetectionChannelOneActvity.class);
					intent.putExtra("number", String.valueOf(number));
					intent.putExtra("channel", chOne);
					intent.putExtra("method", method);
					intent.putExtra("valueC", valueC);
					intent.putExtra("name", projectName);
					intent.putExtra("valueT", valueT);
					intent.putExtra("valueTL", valueTL);
					intent.putExtra("sampleName", sampleName);
					intent.putExtra("valueOne", valueOne);
					intent.putExtra("valueTwo", valueTwo);
					intent.putExtra("valueThree", valueThree);
					intent.putExtra("resultChoice", resultChoice);
					context.startActivity(intent);
				}else if(ibChannelOne.isSelected()&&ibChannelTwo.isSelected()&&isFirst==true&&isFirstchannel2==true){
					//ph这里将两个Boolean值置为false
					isFirst=false;
					isFirstchannel2=false;
					chOne=String.valueOf(channelOne);
				   chTwo=String.valueOf(channelTwo);
				   intent=new Intent(context,DetectionChannelActvity.class);
				   intent.putExtra("number", String.valueOf(number));
				   intent.putExtra("channel", chOne);
				   intent.putExtra("method", method);
				   intent.putExtra("valueC", valueC);
				   intent.putExtra("name", projectName);
				   intent.putExtra("valueT", valueT);
				   intent.putExtra("valueTL", valueTL);
				   intent.putExtra("sampleName", sampleName);
				   intent.putExtra("valueOne", valueOne);
				   intent.putExtra("valueTwo", valueTwo);
				   intent.putExtra("valueThree", valueThree);
				   intent.putExtra("resultChoice", resultChoice);
				   context.startActivity(intent);
				   alertDialog.dismiss();
				}
			}
		});
	}

//	private static void selectFormItem(View v) {
//		ibChannelOne.setSelected(false);
//		ibChannelTwo.setSelected(false);
//		v.setSelected(true);
//		
//	}

    
	

	}

