package com.kyh.objects;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class ControlMaker {
 // =========================================================================
 // TODO Variables
 // =========================================================================	
	private static TextView title; 
	private static LinearLayout Mainlayout;
	private static RelativeLayout RelLeft, RelRight;
	private static EditText TextBox;
	private static Spinner SpinSelect;
	private static CheckBox Checkbox;
 // =========================================================================
 // TODO Layouts
 // =========================================================================	
	public static LinearLayout getMainLayout(Context context){
	
		Mainlayout = new LinearLayout(context);
		Mainlayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		Mainlayout.setWeightSum(100);
		Mainlayout.setOrientation(LinearLayout.VERTICAL);
		return Mainlayout;
	}
	// =========================================================================	
	public static LinearLayout getFirstLayout(Context context){
	
		Mainlayout = new LinearLayout(context);
		Mainlayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		Mainlayout.setOrientation(LinearLayout.HORIZONTAL);
		return Mainlayout;
	}
	// =========================================================================	
	public static LinearLayout getSecondLayout(Context context){
	
		Mainlayout = new LinearLayout(context);
		Mainlayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		Mainlayout.setOrientation(LinearLayout.VERTICAL);
		return Mainlayout;
	}
	// =========================================================================	
	public static TextView getTextview(Context context){
		
		title  = new TextView(context);
        title.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
        //title.setGravity(Gravity.RIGHT );
        title.setTextColor(Color.WHITE);
        title.setTextSize(15);
		return title;
	}
	// =========================================================================
	// Controls
	// =========================================================================
	public static EditText getTextBox(Context context){
		
		TextBox = new EditText(context);
		TextBox.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT ));
		
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH){
			//TextBox.setHintTextColor(Color.argb(255, 50, 50, 50));
			TextBox.setBackgroundColor(Color.argb(200, 255, 255, 255));
			TextBox.setTextColor(Color.BLACK);
		}
		
		return TextBox;
	}
	// =========================================================================
	public static Spinner getSpinner(Context context){
		
		SpinSelect = new Spinner(context);
		SpinSelect.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
		
		return SpinSelect;
	}
	// =========================================================================
	public static EditText getTextArea(Context context){
		
		TextBox = new EditText(context);
		TextBox.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,100 ));
		TextBox.setGravity(Gravity.TOP);
		
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH){
			TextBox.setBackgroundColor(Color.argb(200, 255, 255, 255));
			TextBox.setTextColor(Color.BLACK);
		}
        
		return TextBox;
	}
	// =========================================================================
	public static CheckBox getCheckBox(Context context){
		
		Checkbox = new CheckBox(context);
		Checkbox.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT ));
		
		return Checkbox;
	}
}
