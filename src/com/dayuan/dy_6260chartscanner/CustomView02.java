package com.dayuan.dy_6260chartscanner;

import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class CustomView02 extends View{

	List<Integer> list;
	public float XPoint = 30; // 原点X坐标点
	public float YPoint = 120; // 原点Y坐标点
	public float XLength = 275; // X轴的长度
	public float YLength = 112;
			 // Y轴坐标的长度
    public float valueC;
    public float valueT;
    float gap;

	public float startX = 0;
	public float startY = 0;
	float maxValue;
	float minValue;
	int CIndex;
	int TIndex;
	private float value;
	private float Y2;
	public CustomView02(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		Paint paint = new Paint();
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(2);
		paint.setAntiAlias(true);// 去锟斤拷锟�
		paint.setColor(Color.BLUE);
		paint.setTextSize(8);
		int index=(int) (maxValue/2000);
//		 Y2=(float) (6*1000*(YLength/1.1)/maxValue);
		//for (int i = 0; i < list.size()-40; i++) {
			for (int i = 0; i < list.size(); i++) {
			gap =(XLength) / (list.size()-1);
				if (i < list.size()-1) {
				startX = gap * i + XPoint;
				startY = (float) (YPoint-list.get(i) *(YLength-20) /maxValue); 
				float stopX = (i + 1) * gap + XPoint;
				float nextValue = list.get(i +1);
				float stopY = (float) (nextValue *(YLength-20) /(maxValue));
				stopY = YPoint-stopY;
				canvas.drawLine(startX, Y2+startY, stopX, Y2+stopY, paint);
			}
		}
			for (int i =0; i <1.1*index; i++) {
				value=(float)(i+1)*2000;
				float Y1=(float) (value*(YLength-20)/maxValue);
				
				canvas.drawText(value+"", XPoint-30, YPoint- Y1, paint);
			}
			
		canvas.drawLine(XPoint, YPoint - YLength, XPoint, YPoint, paint);
		canvas.drawLine(XPoint, YPoint, XPoint + XLength, YPoint, paint);
		//canvas.drawCircle(cx, cy, 4, paint);
		
	}

	public void setData(List<Integer> data) {
		this.list = data;
			for (int i =0; i < list.size(); i++) {
			float value = list.get(i);
			if (value > maxValue) {
				maxValue = value;
				Log.d("maxValue", "" + maxValue);
			}
		}
	//Value();
	}

	public void removeData() {
		list.clear();
	}

	
}

